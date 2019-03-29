/*
 * ====================================================================
 * Copyright (c) 2004-2011 TMate Software Ltd.  All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.  The terms
 * are also available at http://svnkit.com/license.html
 * If newer versions of this license are posted there, you may use a
 * newer version instead, at your option.
 * ====================================================================
 */
package com.github.svnkit.test;

import java.util.Collection;
import java.util.Iterator;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class Test4 {
    public static void main(String[] args) throws Exception {
        String url = "http://vip.alisvn.com/apple.svn_psbc/";
        String name = "apple";
        String password = "lizhongwen2019";
        String path = "";
        int limitDeep = 3;
        
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();

        SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password.toCharArray());
        repository.setAuthenticationManager(authManager);
        
        listEntries(repository, path, limitDeep);

    }
	
	@SuppressWarnings("rawtypes")
	public static void listEntries(SVNRepository repository, String path, int limitDeep) throws SVNException {
		if (limitDeep <= 0) {
			return;
		}
		Collection entries = repository.getDir(path, -1, null, (Collection) null);
		Iterator iterator = entries.iterator();
		while (iterator.hasNext()) {
			SVNDirEntry entry = (SVNDirEntry) iterator.next();
			System.out.println("/" + (path.equals("") ? "" : path + "/") + entry.getName() + " ( author: '"
					+ entry.getAuthor() + "'; revision: " + entry.getRevision() + "; date: " + entry.getDate() + ")");
			if (entry.getKind() == SVNNodeKind.DIR) {
				listEntries(repository, (path.equals("")) ? entry.getName() : path + "/" + entry.getName(), limitDeep - 1);
			}
		}
	}

}
