package com.github.svnkit.test;

import java.io.File;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.admin.SVNAdminClient;

public class NewLocalRepositoryTestCase {
	
	static SVNAdminClient adminClient = new SVNAdminClient(SVNClientManager.newInstance(), new DefaultSVNOptions());

	public static void main(String[] args) {
		// svnadmin create --fs-type bdb ~/svn-repo
		/*
		/
		 |_conf/
		 |_dav/
		 |_db/
		 |_hooks/
		 |_locks/
		 |_format
		 |_readme.txt
		 */
		try {
			String tgtPath = "/Users/zhongwen/svn-repo";
			SVNURL tgtURL = SVNRepositoryFactory.createLocalRepository(new File(tgtPath), true, false);
			System.out.println(tgtURL);
			
			
			adminClient.doCreateRepository(new File(tgtPath + "-1"), null, true, false);
		} catch (SVNException e) {
			e.printStackTrace();
		}
	}
	
}
