package com.samphippen.explaygamejam2012;

public class Logger {
	public static boolean mIsLogging = true; 
	
	public static void println(String str) {
		if (mIsLogging == true) System.out.println(str); 		
	}
	
	public static void println(Object obj) {
		if (mIsLogging == true) System.out.println(obj); 		
	}
	
	public static void println(int value) {
		if (mIsLogging == true) System.out.println(value); 		
	}
}
