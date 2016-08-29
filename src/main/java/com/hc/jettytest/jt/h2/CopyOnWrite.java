package com.hc.jettytest.jt.h2;

import java.io.FileFilter;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CopyOnWrite {
	
	public static CopyOnWrite cow = new CopyOnWrite();
	
	static {
		System.out.println("static block");
	}
	{
		System.out.println("general block");
	}
	
	public static void main(String[] args) {
		
		System.exit(0);
		String str1 = "abc";
		Object o;
		Thread t;
		String str2 = str1;
		
		List<String> a = new ArrayList<String>();
		
		// str1.
		
		IntBuffer ib;
		
		Collection c;
		
		String globalId = String.format("%s.%s.%s", 3, "s", "tName");
		
		System.out.println(str1.split("\\.").length);
		
		System.out.println("12'345'".replace("'", "\\'"));
		
		String s = "consumer.a.b.c.d";
		
		System.out.println(s.substring(s.indexOf('.') + 1));
		
		 // Proxy.newProxyInstance
		
		Arrays.asList(a);
		FileFilter f;
		Arrays.sort(null, null);
		
		java.lang.Runnable r;
		
		// Comparator c;
		
		Collections.sort(null ,null);
		
	}

}
