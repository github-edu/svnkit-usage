package com.github.svnkit.test;

import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;

public class SvnRepositoryTestCase {

	static ISVNAuthenticationManager authManager;

	public static void main(String[] args) throws Exception {
		DAVRepositoryFactory.setup();

//		SVNURL url = SVNURL.create("https", "apple:lizhongwen2019", "vip.alisvn.com", 443, "/apple.svn_psbc", false);
//		SVNURL url = SVNURL.create("http", null, "vip.alisvn.com", 80, "/apple.svn_psbc", false);
		SVNURL url = SVNURL.create("https", null, "vip.alisvn.com", 443, "/apple.svn_psbc", false);

		System.out.println(url);

		SVNRepository repository = SVNRepositoryFactory.create(url, null);
		BasicAuthenticationManager basicAuthManager = BasicAuthenticationManager.newInstance("apple",
				"lizhongwen2019".toCharArray());
		// ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);

		// set an auth manager which will provide user credentials
		repository.setAuthenticationManager(basicAuthManager);

	}

}
