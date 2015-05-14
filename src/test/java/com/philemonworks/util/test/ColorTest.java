/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004,2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * @author emicklei
 * 2-mrt-2005: created
 *
 */
package com.philemonworks.util.test;

import com.philemonworks.util.Color;
import junit.framework.TestCase;

/**
 * 
 */
public class ColorTest extends TestCase {
    public void testBlack(){
        System.out.println(Color.black);
    }
    public void testDecode(){
        System.out.println(Color.decode("#FF00FF"));
    }
}
