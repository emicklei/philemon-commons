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

import java.io.PrintWriter;
import java.util.Properties;

/**
 * @author emicklei
 * 
 */
public class Configuration {
    private static PropertiesAccess access;
    
    static {
        setFileName("configuration.properties");
    }

    public static void setFileName(String newFileName) {
        access = new PropertiesAccess(newFileName);
    }
	/** 
	 * Answer the name of the file from which the properties are read.
	 */
	public static String getFileName(){
		return access.getFileName();
	}
    /**
     * Answer the String value of a property
     * @param key of the property
     * @return value of the property
     */
    public static String get(String key) {
        return access.get(key);
    }

    /**
     * Answer the integer value of a property
     * @param key of the property
     * @return value of the property converted to an int
     */
    public static int getInt(String key) {
        return access.getInt(key);
    }

    /**
     * Answer the property value for the parameter key, answer the defaultString
     * if this property is not available
     * 
     * @param key
     * @param defaultString
     * @return value
     */
    public static String get(String key, String defaultString) {
        return access.get(key, defaultString);
    }

    /**
     * Set the property value for the paramter key.
     * @param key
     * @param value
     */
    public static void put(String key, String value) {
        access.put(key, value);
    }

    /**
     * Get the underlying Properties instance.
     * @return a Properties
     */
    public static Properties getProperties() {
        return access.getProperties();
    }

    /**
     * Forget about the cached properties.
     * The next property access will cause a (re)load of the properties
     */
    public static void flush() {
        access.flush();
    }

    /**
     * Writer all entries as key-value pairs.
     */
    public static void printAll(PrintWriter writer) {
        access.printAll(writer);
    }
}