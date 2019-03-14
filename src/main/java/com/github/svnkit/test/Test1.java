package com.github.svnkit.test;

import java.net.URI;

public class Test1 {
	
	public static void main(String[] args) {
		System.out.println(fillRepoUrl("http://10.15.15.171/devops10/devops.git"));
		System.out.println(fillRepoUrl("http://10.15.15.171/devops10/devops"));
		System.out.println(fillRepoUrl("http://10.15.15.171/devops10/devops/"));
		System.out.println(fillRepoUrl("http://10.15.15.120/svn/lizw-test"));
		System.out.println(fillRepoUrl("http://10.15.15.120/svn/lizw-test/"));
		System.out.println();
		
		System.out.println(fillRepoUrl2("http://10.15.15.171/devops10/devops.git"));
		System.out.println(fillRepoUrl2("http://10.15.15.171/devops10/devops"));
		System.out.println(fillRepoUrl2("http://10.15.15.171/devops10/devops/"));
		System.out.println(fillRepoUrl2("http://10.15.15.120/svn/lizw-test"));
		System.out.println(fillRepoUrl2("http://10.15.15.120/svn/lizw-test/"));
		System.out.println();
	}

	protected static String fillRepoUrl(String url) {
		String gitPoxfix = ".git";
		if (url.endsWith(gitPoxfix)) {
			url = url.substring(0, url.length() - gitPoxfix.length());
		}
		String[] urlArray = url.split("/");
		String newUrl = "";
		if (urlArray.length > 3) {
			for (int i = 2; i < urlArray.length; i++) {
				newUrl += urlArray[i] + "/";
			}
			return newUrl.substring(0, newUrl.length() - 1);
		} else {
			return url;
		}
	}
	
	static final String GIT_SUFFIX = ".git";
	protected static String fillRepoUrl2(String url) {
		
		URI uri = URI.create(url);
		final String originPath = uri.getPath();
		String fixedPath = originPath;
		
		// ignore '.git' suffix
		fixedPath = fixedPath.endsWith(GIT_SUFFIX) ? fixedPath.substring(0, fixedPath.length() - GIT_SUFFIX.length()) : fixedPath;
		// ignore '/' suffix
		fixedPath = '/' == fixedPath.charAt(fixedPath.length() - 1) ? fixedPath.substring(0, fixedPath.length() - 1) : fixedPath;
		
		return originPath.equals(fixedPath) ? url : url.substring(0, url.length() - originPath.length()) + fixedPath;
	}
	
}
