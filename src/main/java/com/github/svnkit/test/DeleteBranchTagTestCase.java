package com.github.svnkit.test;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class DeleteBranchTagTestCase {
	
	public static void main(String[] args) throws Exception {
		
		 String srcUrl = "https://vip.alisvn.com/libu.demo";
	        /*
	         * Initializes the library (it must be done before ever using the
	         * library itself)
	         */
	        setupLibrary();

	        SVNURL srcURL = SVNURL.parseURIEncoded(srcUrl);
	        
	        SVNRepository srcRepository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(srcUrl));
	        srcRepository.setAuthenticationManager(SVNWCUtil.createDefaultAuthenticationManager("libu", "libubb163".toCharArray()));
	        
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
//	            DisplayRepositoryTree.listEntries(srcRepository, "");
	            System.out.println();
	            
	            ISVNEditor editor = srcRepository.getCommitEditor( "Delete branch dev" , null); // 第一个参数，svn提交信息
	            
	            deleteDir(editor, "branches/dev");
	            
//	            DisplayRepositoryTree.listEntries(tgtRepository, "");
	            
	        }catch(SVNException svne){
	            System.err.println("An error occurred while accessing source repository: " + svne.getErrorMessage().getFullMessage());
	            System.exit(1);
	        }
	        
	}

    /*
     * This method performs committing a deletion of a directory.
     */
    private static SVNCommitInfo deleteDir(ISVNEditor editor, String dirPath) throws SVNException {
        /*
         * Always called first. Opens the current root directory. It  means  all
         * modifications will be applied to this directory until  a  next  entry
         * (located inside the root) is opened/added.
         * 
         * -1 - revision is HEAD
         */
        editor.openRoot(-1);
        /*
         * Deletes the subdirectory with all its contents.
         * 
         * dirPath is relative to the root directory.
         */
        editor.deleteEntry(dirPath, -1);
        /*
         * Closes the root directory.
         */
        editor.closeDir();
        /*
         * This is the final point in all editor handling. Only now all that new
         * information previously described with the editor's methods is sent to
         * the server for committing. As a result the server sends the new
         * commit information.
         */
        return editor.closeEdit();
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
