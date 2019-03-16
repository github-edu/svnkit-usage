package com.github.svnkit.test;

import java.io.File;

import org.codehaus.plexus.archiver.gzip.GZipArchiver;
import org.codehaus.plexus.archiver.tar.TarArchiver;
import org.codehaus.plexus.archiver.zip.ZipArchiver;

public class ZipTest {
	
	public static void main(String[] args) throws Exception {

		File srcDir = new File("/Users/zhongwen/.m2/repository/org/codehaus/plexus/plexus-archiver/2.4.4");
		ZipArchiver archiver = new ZipArchiver();
		archiver.addDirectory(srcDir);
		archiver.setDestFile(new File(srcDir.getAbsolutePath() + ".zip"));
		archiver.createArchive();
		
		TarArchiver tarArchiver = new TarArchiver();
		tarArchiver.addDirectory(srcDir);
		tarArchiver.setDestFile(new File(srcDir.getAbsolutePath() + ".tar"));
		tarArchiver.createArchive();
		
		GZipArchiver gZipArchiver = new GZipArchiver();
		gZipArchiver.addFile(new File(srcDir.getAbsolutePath() + ".tar"), "xyz.tar");
		gZipArchiver.setDestFile(new File(srcDir.getAbsolutePath() + ".tar.gz"));
		gZipArchiver.createArchive();
		
		new File(srcDir.getAbsolutePath() + ".zip").delete();
		new File(srcDir.getAbsolutePath() + ".tar").delete();
		new File(srcDir.getAbsolutePath() + ".tar.gz").delete();
	}

}
