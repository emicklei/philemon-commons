/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 9-nov-2004: created
 *
 */
package com.philemonworks.util.test;

import java.io.PrintWriter;
import com.philemonworks.util.NLS;
import junit.framework.TestCase;

/**
 * @author E.M.Micklei
 *
 */
public class NLSTest extends TestCase {
    public NLSTest(String arg0) {
        super(arg0);
    }

    public void testSimple3() {
        System.out.println(NLS.get("gt", new Object[] { "2", "1" }));
    }

    public void testSimple4() {
        System.out.println(NLS.get("gt", new Object[] { "2", "1" }));
    }

    public void testDump() {
        NLS.printAllOn(new PrintWriter(System.out));
    }
}