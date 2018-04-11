package com.example.swipecardspro_eva01.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamUtil {
	public static String stream2String(InputStream is){
		String result = "";
		
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line="";
			StringBuilder strbuilder = new StringBuilder();
			while((line=reader.readLine())!=null){
				strbuilder.append(line);
			}
			
			result = strbuilder.toString();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		return result;
	}
}
