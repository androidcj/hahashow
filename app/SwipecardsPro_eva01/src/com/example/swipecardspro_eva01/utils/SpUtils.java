package com.example.swipecardspro_eva01.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {

	
	
	public static boolean checkUserLogin(Context cx,String keyword){
		SharedPreferences sp = cx.getSharedPreferences("config", Context.MODE_PRIVATE);
		String isloginKey = sp.getString(keyword, "");
		return isloginKey.equals(keyword)?true:false;
	}
	
}
