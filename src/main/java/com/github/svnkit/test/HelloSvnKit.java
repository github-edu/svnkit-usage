package com.github.svnkit.test;

import java.io.File;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class HelloSvnKit {
	
	public static void main(String[] args) throws Exception {
    	
        SvnkitUtil.setupLibrary();

        String url ="http://10.15.15.120/svn/lizw-test/trunk";
        SVNURL srcURL = SVNURL.parseURIEncoded(url);
        
        SVNRepository srcRepository = SVNRepositoryFactory.create(srcURL);
        srcRepository.setAuthenticationManager(SVNWCUtil.createDefaultAuthenticationManager("libu", "libu".toCharArray()));
        
        
        // srcRepository.checkout(-1L, "", true, editor);
        
        
        SVNClientManager cm = SVNClientManager.newInstance(); 
        cm.setAuthenticationManager(SVNWCUtil.createDefaultAuthenticationManager("libu", "libu".toCharArray()));
        SVNUpdateClient updateClient = cm.getUpdateClient();
        long revision = updateClient.doCheckout(srcURL, new File("/tmp/svn-test"), SVNRevision.UNDEFINED, SVNRevision.HEAD, SVNDepth.INFINITY, 
                false);
        System.out.println(revision);
		
	}

}
