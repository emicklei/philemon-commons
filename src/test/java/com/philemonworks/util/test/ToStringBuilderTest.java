package com.philemonworks.util.test;
import java.util.Date;
import com.philemonworks.util.ToStringBuilder;

import junit.framework.TestCase;

/*
 * Created on 28-jan-05
 *
 */
/**
 * @author E.M.Micklei
 *
 */
public class ToStringBuilderTest extends TestCase {
	public void testSimple(){
		Date today = new Date();
		System.out.println(ToStringBuilder.build(today));
	}
	public void testStream(){
		System.out.println(ToStringBuilder.build(System.out,false,false, 0));
	}	
	public void testThis(){
		System.out.println(ToStringBuilder.build(this,false,false, 0));
	}		
}
