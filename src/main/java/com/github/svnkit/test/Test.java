package com.github.svnkit.test;

import java.net.URI;
import java.util.Date;

import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class Test {
	
	public static void main(String[] args) throws Exception {
		// createRepo();
		
		System.out.println("cdksacd/cdsacds/cdsacds/cds".split("/").length);
		System.out.println("cdksacd/cdsacds/cdsacds/cds/".split("/").length);
		
		System.out.println("cdksacd/cdsacds/cdsacds/cds".split("/"));
		System.out.println("cdksacd/cdsacds/cdsacds/cds/".split("/").length);
		
		String s = null;
		s = SvnkitUtil.stripUrlSuffix("http://10.15.15.120/svn/libu-test/02baseline/New08", 1);
		System.out.println(s);
		SvnkitUtil.stripUrlSuffix("http://10.15.15.120/svn/libu-test/02baseline/New08/", 1);
		System.out.println(s);
		
		s = SvnkitUtil.stripUrlSuffix("http://10.15.15.120/svn/libu-test/02baseline/New08", 2);
		System.out.println(s);
		s = SvnkitUtil.stripUrlSuffix("http://10.15.15.120/svn/libu-test/02baseline/New08/", 2);
		System.out.println(s);
		
		s = SvnkitUtil.stripUrlSuffix("http://10.15.15.120/svn/libu-test/02baseline/New08", -1);
		System.out.println(s);
		s = SvnkitUtil.stripUrlSuffix("http://10.15.15.120/svn/libu-test/02baseline/New08/", -1);
		System.out.println(s);
	}
	
	public static void createRepo() throws Exception {
		// 对于SVN来说创建仓库就是创建目录
		//String path = URI.create(repo.getRepoUrl()).getPath();
		//path = SvnkitUtil.trimPathSuffix(path) + SvnkitUtil.trimPath(repo.getRepoName());
		
		String absPath = SvnkitUtil.trimPath(URI.create("http://10.15.15.120/svn/libu-test/02baseline/New08").getPath());
		String rootPath = SvnkitUtil.trimPath(URI.create("http://10.15.15.120/svn/libu-test").getPath()) + "/";
		String relativePath = absPath.startsWith(rootPath) ? absPath.substring(rootPath.length()) : null;
		
		System.out.println(relativePath);
		
		SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded("http://10.15.15.120/svn/libu-test/02baseline"));
		repository.setAuthenticationManager(SVNWCUtil.createDefaultAuthenticationManager("libu", "libu".toCharArray()));
		
        ISVNEditor editor = repository.getCommitEditor(String.format("New repository %s", "New08"), null); //$NON-NLS-1$
        SvnkitUtil.addDir(editor, relativePath, relativePath + "/README.md", String.format("# Created by DevOps at %s", new Date().toString()).getBytes());
	}

}
