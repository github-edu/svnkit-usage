package com.github.svnkit.test;

import java.net.URI;

import org.apache.commons.lang.ArrayUtils;

public class Test3 {
	
	public static void main(String[] args) {
		System.out.println("xx/yy/zz".contains("/"));
		
		URI uri = URI.create("http://user:password@192.123.11.123/api/ping");
		
		System.out.println(uri.getPath());
		System.out.println(uri.getUserInfo());
		
		System.out.println(ArrayUtils.toString(new String[] { "xxx", "yyy", "zzz" }));
		
		System.out.println(String.join(",", new String[] { "xxx", "yyy", "zzz" }));
	}

}
