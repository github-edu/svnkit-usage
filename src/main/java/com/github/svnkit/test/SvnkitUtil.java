package com.github.svnkit.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.diff.SVNDeltaGenerator;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNRevision;

public class SvnkitUtil {
	
	// public static final String SVN_BRANCH_MASTER = "master";
	// public static final String SVN_BRANCH_TRUNK = "trunk";
	
	public static final int LIMIT_SIZE = 1024 * 1024;
	
	private SvnkitUtil() {
	}
	
	public static String stripUrlSuffix(String url, int strip) {
		if (StringUtils.isBlank(url) || (strip < 1 && strip != -1)) {
			return url;
		}
		URI uri = URI.create(url);
		String path = uri.getPath();
		if (strip == -1) {
			return url.substring(0, url.length() - path.length());
		}
		String[] originArray = path.split("/");
		if (strip >= originArray.length) {
			return "";
		}
		
		String[] fixedArray = (String[]) ArrayUtils.subarray(originArray, 0, originArray.length - strip);
		String fixedPath = StringUtils.join(fixedArray, "/");
		fixedPath = trimPath(fixedPath);
		
		return url.substring(0, url.length() - path.length()) + fixedPath;
	}
	
	public static String untrimPath(String url) {
		String uri = trimPathSuffix(url);
		return uri.length() < 2 ? uri : (uri.charAt(0) == '/' ? uri.substring(1) : uri);
	}
	
	public static String trimPath(String url) {
		return trimPathSuffix(trimPathPreffix(url));
	}
	
	public static String trimPathPreffix(String url) {
		if (null == url || url.trim().isEmpty()) {
			return "";
		}
		
		String uri = url.trim();
		return '/' == uri.charAt(0) ? uri : "/" + uri;
	}
	
	public static String trimPathSuffix(String url) {
		if (null == url || url.trim().isEmpty()) {
			return "";
		}
		
		String uri = url.trim();
		if ('/' == uri.charAt(uri.length() - 1)) {
			return uri.substring(0, uri.length() - 1);
		}
		return uri;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<SVNDirEntry> getChildren(SVNRepository repository, String path, boolean onlyDir) throws SVNException {
		List<SVNDirEntry> children = new ArrayList<SVNDirEntry>();
		SVNNodeKind nodeKind = repository.checkPath(path, -1L);
        if (nodeKind == SVNNodeKind.NONE) {
        	return new ArrayList<>();
        }
		Collection<SVNDirEntry> entries = repository.getDir(path, -1, null, (Collection) null);
		if (null == entries || entries.isEmpty()) {
			return children;
		}
		if (!onlyDir) {
			children.addAll(entries);
			return children;
		}
		for (SVNDirEntry entry : entries) {
			if (entry.getKind() == SVNNodeKind.DIR) {
				children.add(entry);
			}
		}
		return children;
	}
	
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

    /*
     * This method performs commiting an addition of a  directory  containing  a
     * file.
     */
	public static SVNCommitInfo addDir(ISVNEditor editor, String dirPath,
			String filePath, byte[] data) throws SVNException {
		return addDir(editor, dirPath, null, filePath, data);
	}
	
    public static SVNCommitInfo addDir(ISVNEditor editor, String dirPath, String[] childDirs,
            String filePath, byte[] data) throws SVNException {
        /*
         * Always called first. Opens the current root directory. It  means  all
         * modifications will be applied to this directory until  a  next  entry
         * (located inside the root) is opened/added.
         * 
         * -1 - revision is HEAD (actually, for a commit  editor  this number  is 
         * irrelevant)
         */
        editor.openRoot(-1);
        /*
         * Adds a new directory (in this  case - to the  root  directory  for 
         * which the SVNRepository was  created). 
         * Since this moment all changes will be applied to this new  directory.
         * 
         * dirPath is relative to the root directory.
         * 
         * copyFromPath (the 2nd parameter) is set to null and  copyFromRevision
         * (the 3rd) parameter is set to  -1  since  the  directory is not added 
         * with history (is not copied, in other words).
         */
        editor.addDir(dirPath, null, -1);
        
        if (!ArrayUtils.isEmpty(childDirs)) {
        	for (String dir : childDirs) {
				editor.addDir(trimPathSuffix(dirPath) + trimPath(dir), null, -1);
				editor.closeDir();
			}
        }
        /*
         * Adds a new file to the just added  directory. The  file  path is also 
         * defined as relative to the root directory.
         *
         * copyFromPath (the 2nd parameter) is set to null and  copyFromRevision
         * (the 3rd parameter) is set to -1 since  the file is  not  added  with 
         * history.
         */
        editor.addFile(filePath, null, -1);
        /*
         * The next steps are directed to applying delta to the  file  (that  is 
         * the full contents of the file in this case).
         */
        editor.applyTextDelta(filePath, null);
        /*
         * Use delta generator utility class to generate and send delta
         * 
         * Note that you may use only 'target' data to generate delta when there is no 
         * access to the 'base' (previous) version of the file. However, using 'base' 
         * data will result in smaller network overhead.
         * 
         * SVNDeltaGenerator will call editor.textDeltaChunk(...) method for each generated 
         * "diff window" and then editor.textDeltaEnd(...) in the end of delta transmission.  
         * Number of diff windows depends on the file size. 
         *  
         */
        SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
        String checksum = deltaGenerator.sendDelta(filePath, new ByteArrayInputStream(data), editor, true);

        /*
         * Closes the new added file.
         */
        editor.closeFile(filePath, checksum);
        /*
         * Closes the new added directory.
         */
        editor.closeDir();
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
     * This method performs committing a deletion of a directory.
     */
    public static SVNCommitInfo deleteDir(ISVNEditor editor, String dirPath) throws SVNException {
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

    /**
     * 
     * @param repository
     * @param revision
     * @param filePath
     * @return
     */
    public static byte[] getRawFileContent(SVNRepository repository, String revision, String filePath) {
    	long version = -1L;
    	
    	try {
			version = null == revision ? -1L : Long.parseLong(revision);
		} catch (NumberFormatException ignore) {
		}
    	return getRawFileContent(repository, version, filePath);
    }
    
    /**
     * 
     * @param repository
     * @param revision
     * @param filePath
     * @return
     */
    public static byte[] getRawFileContent(SVNRepository repository, long revision, String filePath) {
    	long version = revision >= -1L ? revision : -1L;

        /*
         * This Map will be used to get the file properties. Each Map key is a
         * property name and the value associated with the key is the property
         * value.
         */
        SVNProperties fileProperties = new SVNProperties();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
            /*
             * Checks up if the specified path really corresponds to a file. If
             * doesn't the program exits. SVNNodeKind is that one who says what is
             * located at a path in a revision. -1 means the latest revision.
             */
            SVNNodeKind nodeKind = repository.checkPath(filePath, version);
            
            if (nodeKind == SVNNodeKind.NONE) {
                System.err.println("There is no entry at '" + filePath + "', revision = " + version + ".");
                return new byte[0];
            } else if (nodeKind == SVNNodeKind.DIR) {
                System.err.println("The entry at '" + filePath
                        + "' is a directory while a file was expected, revision = " + version + ".");
                return new byte[0];
            }
            /*
             * Gets the contents and properties of the file located at filePath
             * in the repository at the latest revision (which is meant by a
             * negative revision number).
             */
            repository.getFile(filePath, -1, fileProperties, baos);

            /*
             * Here the SVNProperty class is used to get the value of the
             * svn:mime-type property (if any). SVNProperty is used to facilitate
             * the work with versioned properties.
             */
            // String mimeType = fileProperties.getStringValue(SVNProperty.MIME_TYPE);
            
            /*
             * SVNProperty.isTextMimeType(..) method checks up the value of the mime-type
             * file property and says if the file is a text (true) or not (false).
             */
            // boolean isTextType = SVNProperty.isTextMimeType(mimeType);
            
            /*
	        Iterator iterator = fileProperties.nameSet().iterator();
	        while (iterator.hasNext()) {
	            String propertyName = (String) iterator.next();
	            String propertyValue = fileProperties.getStringValue(propertyName);
	            System.out.println("File property: " + propertyName + "="
	                    + propertyValue);
	        }
            if (isTextType) {
            	return baos.toByteArray();
            }
             */
            if (baos.size() > LIMIT_SIZE) {
            	return String.format("This file is too large, so it is not shown here. path=%s, revision=%s", filePath, revision).getBytes();
            }
            return baos.toByteArray();
        } catch (Exception svne) {
            System.err.println("Error while fetching the file contents and properties: " + svne.getMessage());
            return new byte[0];
        }
    }

    /*
     * This  method  performs how a directory in the repository can be copied to
     * branch.
     */
    public static SVNCommitInfo copyDir(ISVNEditor editor, String srcDirPath,
            String dstDirPath, long revision) throws SVNException {
        /*
         * Always called first. Opens the current root directory. It  means  all
         * modifications will be applied to this directory until  a  next  entry
         * (located inside the root) is opened/added.
         * 
         * -1 - revision is HEAD
         */
        editor.openRoot(-1);
        
        /*
         * Adds a new directory that is a copy of the existing one.
         * 
         * srcDirPath   -  the  source  directory  path (relative  to  the  root 
         * directory).
         * 
         * dstDirPath - the destination directory path where the source will be
         * copied to (relative to the root directory).
         * 
         * revision    - the number of the source directory revision. 
         */
        editor.addDir(dstDirPath, srcDirPath, revision);
        /*
         * Closes the just added copy of the directory.
         */
        editor.closeDir();
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
    

    public static SVNCommitInfo copy(SVNClientManager clientManager, SVNURL srcURL, SVNURL dstURL, boolean isMove, String commitMessage) throws SVNException {
        /*
         * SVNRevision.HEAD means the latest revision.
         * Returns SVNCommitInfo containing information on the new revision committed 
         * (revision number, etc.) 
         */        
        return clientManager.getCopyClient().doCopy(new SVNCopySource[] {new SVNCopySource(SVNRevision.HEAD, SVNRevision.HEAD, srcURL)},
                dstURL, isMove, true, false, commitMessage, null);
    }

}
