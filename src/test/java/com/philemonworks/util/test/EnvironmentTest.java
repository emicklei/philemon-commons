/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004,2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * 19-apr-2005: created
 *
 */
package com.philemonworks.util.test;

import com.philemonworks.util.Configuration;
import com.philemonworks.util.Environment;
import junit.framework.TestCase;

/**
 * 
 */
public class EnvironmentTest extends TestCase {
    
    public void testGet(){
        assertEquals("get it","environment", Environment.get("test"));
    }
    public void testGetConfig(){
        assertEquals("get it","configuration", Configuration.get("test"));
    }
}
