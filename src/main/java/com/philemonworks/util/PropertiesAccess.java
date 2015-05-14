/*
    Copyright 2004 Ernest Micklei @ PhilemonWorks.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
*/
package com.philemonworks.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * @author emicklei
 * 
 */
public class PropertiesAccess {
    private Properties properties = null;
    private String fileName;

    public PropertiesAccess(String fileName){
        super();
        this.fileName = fileName;
    }
	/**
	 * Answer the name of the file from which the properties can be loaded.
	 * @return String
	 */
	public String getFileName(){
		return fileName;
	}
    /**
     * Load the properties using the file name.
     * If the file name includes path information, then open it as a File stream.
     * If the file does not include path information, then try finding it on the classpath.
     */
    protected void load() {
        try {
            Logger.getLogger(PropertiesAccess.class).info("Finding and loading " + fileName);
            InputStream input;
            // Detect whether an absolute path is given or a local resource file
            if (fileName.indexOf('/') > -1 || (fileName.indexOf('\\') > -1)) {
                File inputFile = new File(fileName);
                if (!inputFile.exists())
                    throw new RuntimeException("File does not exists:" + fileName);
                if (!inputFile.canRead())
                    throw new RuntimeException("File can not be read:" + fileName);
                input = new FileInputStream(fileName);
            } else {
                input = PropertiesAccess.class.getClassLoader().getResourceAsStream(fileName);
				if (input == null) throw new RuntimeException("Unable to find on classpath:" + fileName);
            }
            properties = new Properties();
            properties.load(input);
        } catch (Exception ex) {
            Logger.getLogger("PropertiesAccess").error(ex);
        }
    }

    /**
     * Answer the String value of a property
     * @param key of the property
     * @return value of the property
     */
    public String get(String key) {
        String value = getProperties().getProperty(key);
        if (value == null)
            throw new RuntimeException("Missing property named:" + key + " in file named:" + fileName);
        return value;
    }

    /**
     * Answer the integer value of a property
     * @param key of the property
     * @return value of the property converted to an int
     */
    public int getInt(String key) {
        return Integer.valueOf(get(key)).intValue();
    }

    /**
     * Answer the property value for the parameter key, answer the defaultString
     * if this property is not available
     * 
     * @param key
     * @param defaultString
     * @return value
     */
    public String get(String key, String defaultString) {
        String value = getProperties().getProperty(key);
        if (value == null)
            return defaultString;
        else
            return value;
    }

    public void put(String key, String value) {
        getProperties().put(key, value); // overwrite
    }

    public Properties getProperties() {
        if (properties == null)
            load();
        return properties;
    }

    /**
     * Forget about the cached properties.
     * The next property access will cause a (re)load of the properties
     */
    public void flush() {
        properties = null;
    }

    /**
     * Writer all entries
     *
     */
    public void printAll(PrintWriter writer) {
        Properties myProperties = getProperties();
        List myList = Arrays.asList(myProperties.keySet().toArray());
        Collections.sort(myList);
        for (int k = 0; k < myList.size(); k++) {
            writer.write((String) myList.get(k));
            writer.write("=");
            writer.write((String) myProperties.get((String) myList.get(k)));
            writer.write("\n");
        }
    }
}