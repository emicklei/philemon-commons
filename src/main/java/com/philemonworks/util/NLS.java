/*
    Copyright 2005 Ernest Micklei @ PhilemonWorks.com

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
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * NLS provides static behavior for National Language Support.
 * The implementation uses the standard Java ResourceBundle and Locale concept.
 * 
 * 
 * the NLS class can be used to retrieve Locale dependent messages:
 * 
 * 	NLS.get("filenotfound");
 * 
 * The message is read from the properties file 
 * (NLS_en.properties or NLS.properties if no local specific file was fount):
 * 
 * 	filenotfound=File does not exist
 * 
 * @author E.M.Micklei
 */
public class NLS {
	private static final ThreadLocal threadlocal = new ThreadLocal();
	/**
	 * Set the active Locale for the execution of this thread
	 * @param activeLocale
	 */
	public static void setLocale(Locale activeLocale){
		threadlocal.set(activeLocale);
	}
	/**
	 * Return the active Locale (maybe default)
	 * @return Locale
	 */
	public static Locale getLocale(){
		Locale localeOrNull = (Locale)threadlocal.get();
		if (localeOrNull == null) localeOrNull = Locale.getDefault();
		return localeOrNull;
	}
	/**
	 * @return a formatted String using the objects from the info parameter
	 * @param key is the entry point in the locale specific properties file
	 */
	public static String get(String key) {
		return getBundle().getString(key);
	}
	/**
	 * @return a formatted String using the parameter object
	 * @param key is the entry point in the locale specific properties file
	 * @param info a string representation is inserted into the message pattern
	 */
	public static String get(String key,String info) {
		String pattern = getBundle().getString(key);
		return MessageFormat.format(pattern,new Object[]{info});
	}	
	/**
	 * @return a formatted String using the objects from the info parameter
	 * @param key is the entry point in the locale specific properties file
	 * @param info contains zero or more objects that are inserted into the message pattern
	 */
	public static String get(String key,Object[] info) {
		String pattern = getBundle().getString(key);
		return MessageFormat.format(pattern,info);
	}	
	/** 
	 * Print all specified key-message pairs on the writer
	 * @param writer
	 */
	public static void printAllOn(PrintWriter writer){
		Enumeration e = getBundle().getKeys();	
		while (e.hasMoreElements()){
			String key = (String)e.nextElement();
			writer.print(key);
			writer.print('=');
			writer.print(getBundle().getObject(key));
			writer.print('\n');			
		}		
		writer.flush();
	}
	/**
	 * @return the resource bundle for the current NLS class
	 */
	private static ResourceBundle getBundle(){
		return ResourceBundle.getBundle("com.philemonworks.util.NLS",getLocale());
	}
}
