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

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class NewBranchTestCase2 {

	static SVNClientManager ourClientManager = null;
	
    public static void main(String[] args) throws Exception {
    	
    	DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
        
        /*
         * Creates an instance of SVNClientManager providing authentication
         * information (name, password) and an options driver
         */
    	ourClientManager = SVNClientManager.newInstance(options, "libu", "libubb163");
        
        /*
         * Default values:
         * source and target repository paths
         */
        String srcUrl = "https://vip.alisvn.com/libu.demo/trunk";
        String tgtUrl = "https://vip.alisvn.com/libu.demo/branches/dev";
        /*
         * Initializes the library (it must be done before ever using the
         * library itself)
         */
        setupLibrary();

        SVNURL srcURL = SVNURL.parseURIEncoded(srcUrl);
        SVNURL tgtURL = SVNURL.parseURIEncoded(tgtUrl);
        
        SVNRepository srcRepository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(srcUrl));
        SVNRepository tgtRepository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(tgtUrl));

        /*
         * Deault auth manager is used to cache a username in the 
         * default Subversion credentials storage area.
         */
        srcRepository.setAuthenticationManager(SVNWCUtil.createDefaultAuthenticationManager("libu", "libubb163".toCharArray()));
        tgtRepository.setAuthenticationManager(SVNWCUtil.createDefaultAuthenticationManager("libu", "libubb163".toCharArray()));
        
        try{
            System.out.println();
            long srcLatestRevision = srcRepository.getLatestRevision();
            System.out.println("The latest source revision: " + srcLatestRevision);
            System.out.println();
            System.out.println("'" + srcURL + "' repository tree:");
            System.out.println();
            /*
             * Using the DisplayRepositoryTree example to show the source 
             * repository tree in the latest revision. 
             */
//            DisplayRepositoryTree.listEntries(srcRepository, "");
            System.out.println();
            
            copy(srcURL, tgtURL, false, "New branch 1.0");
            
//            DisplayRepositoryTree.listEntries(tgtRepository, "");
            
        }catch(SVNException svne){
            System.err.println("An error occurred while accessing source repository: " + svne.getErrorMessage().getFullMessage());
            System.exit(1);
        }
        
    }
    
    private static SVNCommitInfo copy(SVNURL srcURL, SVNURL dstURL, boolean isMove, String commitMessage) throws SVNException {
        /*
         * SVNRevision.HEAD means the latest revision.
         * Returns SVNCommitInfo containing information on the new revision committed 
         * (revision number, etc.) 
         */        
        return ourClientManager.getCopyClient().doCopy(new SVNCopySource[] {new SVNCopySource(SVNRevision.HEAD, SVNRevision.HEAD, srcURL)},
                dstURL, isMove, true, false, commitMessage, null);
    }

    
    /*
     * Initializes the library to work with a repository via 
     * different protocols.
     */
    private static void setupLibrary() {
        /*
         * For using over http:// and https://
         */
        DAVRepositoryFactory.setup();
        /*
         * For using over svn:// and svn+xxx://
         */
        SVNRepositoryFactoryImpl.setup();
        /*
         * For using over file:///
         */
        FSRepositoryFactory.setup();
    }

}
