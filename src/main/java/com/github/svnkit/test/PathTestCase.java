package com.github.svnkit.test;

import java.net.URI;

public class PathTestCase {
	
	public static void main(String[] args) {
		System.out.println(URI.create("http://10.15.15.120/svn").getPath());
		System.out.println(URI.create("svn://10.15.15.120/svn/lizw").getPath());
		System.out.println(URI.create("ssh://10.15.15.120/svn/libu/").getPath());
		
		boolean isTag = true;
		String branchName = "dev";
		String refName = "2.x";
		System.out.println(String.format("New %s %s created, base on branch %s", (isTag ? "tag" : "branch"), branchName, refName));
	}

}
