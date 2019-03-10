package com.github.svnkit.test;

import java.util.Collection;
import java.util.Iterator;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SvnRepositoryTreeTestCase {

	static ISVNAuthenticationManager authManager;

	public static void main(String[] args) throws Exception {
		DAVRepositoryFactory.setup();

//		SVNURL url = SVNURL.create("https", "apple:lizhongwen2019", "vip.alisvn.com", 443, "/apple.svn_psbc", false);
//		SVNURL url = SVNURL.create("http", null, "vip.alisvn.com", 80, "/apple.svn_psbc", false);
		SVNURL url = SVNURL.create("https", null, "vip.alisvn.com", 443, "/apple.svn_psbc", false);

		System.out.println(url);

		SVNRepository repository = SVNRepositoryFactory.create(url, null);
		ISVNAuthenticationManager basicAuthManager = SVNWCUtil.createDefaultAuthenticationManager("apple",
				"lizhongwen2019".toCharArray());

		// set an auth manager which will provide user credentials
		repository.setAuthenticationManager(basicAuthManager);

		System.out.println("Repository Root: " + repository.getRepositoryRoot(true));
		System.out.println("Repository UUID: " + repository.getRepositoryUUID(true));
		System.out.println("Latest Revision: " + repository.getLatestRevision());

		// System.out.println(repository.getRepositoryPath("branches/master"));

		System.out.println();
		
		listRoot(repository, "");
		
		System.out.println();
		
		listEntries(repository, "");

	}

	public static void listRoot(SVNRepository repository, String path) throws Exception {
		SVNNodeKind nodeKind = repository.checkPath(path, -1);
		if (nodeKind == SVNNodeKind.NONE) {
			System.err.println("There is no entry.");
			return;
		} else if (nodeKind == SVNNodeKind.FILE) {
			System.out.println(path);
			return;
		} else if (nodeKind == SVNNodeKind.DIR) {
			System.out.println(path + "/");
		}
	}

	/**
	 * DevOps 会用到这个来给前端提供数据做源码展示
	 * 
	 * @param repository
	 * @param path
	 * @throws SVNException
	 */
	@SuppressWarnings("rawtypes")
	public static void listEntries(SVNRepository repository, String path) throws SVNException {
		Collection entries = repository.getDir(path, -1, null, (Collection) null);
		Iterator iterator = entries.iterator();
		while (iterator.hasNext()) {
			SVNDirEntry entry = (SVNDirEntry) iterator.next();
			System.out.println("/" + (path.equals("") ? "" : path + "/") + entry.getName() + " ( author: '"
					+ entry.getAuthor() + "'; revision: " + entry.getRevision() + "; date: " + entry.getDate() + ")");
			if (entry.getKind() == SVNNodeKind.DIR) {
				listEntries(repository, (path.equals("")) ? entry.getName() : path + "/" + entry.getName());
			}
		}
	}

}
