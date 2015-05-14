package com.philemonworks.util.test;

/**
 * Insert the type's description here.
 * Creation date: (14-06-2000 9:32:58)
 * @author: Ernest Micklei
 */
public class TimeBlock {
/**
 * TimeBlock constructor comment.
 */
public TimeBlock() {
	
	long timeBefore = System.currentTimeMillis();
	run();
	long timeAfter = System.currentTimeMillis();
	System.out.println("Execution time: " + (timeAfter - timeBefore) + " ms");
}
/**
 * TimeBlock constructor comment.
 */
public TimeBlock(int loops) {
	
	long timeBefore = System.currentTimeMillis();
	for (int i = loops; i > 0; i--)
		run();
	long timeAfter = System.currentTimeMillis();
	System.out.println("Execution it " + loops + " times took " + (timeAfter - timeBefore) + " ms");
}
/**
 * TimeBlock constructor comment.
 */
public TimeBlock(String activity, int loops) {
	super();
	long timeBefore = System.currentTimeMillis();
	for (int i=loops; i>0; i--) run();
	long timeAfter = System.currentTimeMillis();
	System.out.println("Running "+activity+" "+loops+" times took "+ (timeAfter-timeBefore) + " ms");
}
/**
 * TimeBlock constructor comment.
 */
public TimeBlock(String activity, int loops, int repeats) {

	super();
	long sum = 0;
	for (int r = repeats; r > 0; r--) {
		long timeBefore = System.currentTimeMillis();
		for (int i = loops; i > 0; i--)
			run();
		long timeAfter = System.currentTimeMillis();
		System.out.println("Running " + activity + " " + loops + " times took " + (timeAfter - timeBefore) + " ms");
		sum = sum + timeAfter - timeBefore;
	}
	System.out.println("Average time running " + activity + " " + loops + " times took " + (sum / repeats) + " ms");
}
/**
 * TimeBlock constructor comment.
 */
public void run() {
	System.out.println("super run");
}
public static void test1() {
	
	new TimeBlock() {
		public void run() {
			System.out.println("testje");
		}
	};
}
public static void test2() {
	
	new TimeBlock(100) {
		public void run() {
			System.out.println("testje");
		}
	};
}
public static void test3() {
	
	new TimeBlock("Testje",100,10) {
		public void run() {
			System.out.println("testje");
		}
	};
}
public static void test4() {
	
	new TimeBlock("Error catch",100,10) {
		public void run() {
			System.out.println("error catch");
			throw new RuntimeException("error raised");
		}
	};
}
}
