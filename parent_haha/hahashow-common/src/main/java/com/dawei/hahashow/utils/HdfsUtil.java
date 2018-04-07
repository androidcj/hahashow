package com.dawei.hahashow.utils;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HdfsUtil {
	//得到fileSystem
		public  FileSystem getFileSystem(String path){
			FileSystem coreSys = null;
			try {
				Configuration conf = new Configuration(); 
				coreSys = FileSystem.get(URI.create(path), conf);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return coreSys;
}
		
	public String uploadToHDFS(String root,String localPath,String targetPath){
		String result = "true";
		try {
			FileSystem fs = getFileSystem(root);
			Path local_path = new Path(URI.create(localPath));
			Path target_path = new Path(URI.create(targetPath));
			fs.copyFromLocalFile(local_path, target_path);
		} catch (Exception e) {
			// TODO: handle exception
			result = "false";
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	public String downloadFromHDFS(String root,String localPath,String targetPath){
		String result = "true";
		try {
			FileSystem fs = getFileSystem(root);
			Path local_path = new Path(URI.create(localPath));
			Path target_path = new Path(URI.create(targetPath));
			fs.copyToLocalFile(local_path, target_path);
			
		} catch (Exception e) {
			// TODO: handle exception
			result = "false";
			e.printStackTrace();
		}
		return result;
	}
	
		
}