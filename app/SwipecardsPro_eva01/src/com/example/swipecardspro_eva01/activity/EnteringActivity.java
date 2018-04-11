package com.example.swipecardspro_eva01.activity;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.example.swipecardspro_eva01.R;
import com.example.swipecardspro_eva01.utils.Constants;
import com.example.swipecardspro_eva01.utils.SpUtils;
import com.example.swipecardspro_eva01.utils.StreamUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EnteringActivity extends Activity{
	
	RelativeLayout rl_root = null;
	TextView tv_version_name = null;
	protected static final int UPDATE_VERSION = 100;
	protected static final int ENTER_HOME = 101;
	protected static final int URL_ERROR = 102;
	protected static final int IO_ERROR = 103;
	protected static final int JSON_ERROR = 104;
	protected static final int USER_REGISTER = 105;
	protected static final int USER_LOGIN = 106;
	
	
	private String download_url;
	private String versionDes;
	private String app_name;
	
	private int mLocalVersionCode;   //本地app的版本号
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int messres = msg.what;
			switch (messres) {
			case UPDATE_VERSION:
				//版本更行
				showUpdateDialog();
				break;
			case ENTER_HOME:
				//进入主页
				enterHome();
				break;
				
			case USER_REGISTER:
				//用户注册
				userRegister();
				break;	
			
			case USER_LOGIN:
				//用户登录
				userLogin();
				break;
				
				
			default:
				break;
			}
			
			
			
			super.handleMessage(msg);
		}
		
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		setContentView(R.layout.activity_entering);
		super.onCreate(savedInstanceState);
		//初始化界面
		initUI();
		initData();
		initAnimation();
		
	}
	
	
	
	//用户登录
	public void userLogin(){
		
		Intent login_intent = new Intent(this,);
		
		
	}
	
	public void showUpdateDialog(){
		Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);   //设置左上角图标
		builder.setTitle("版本更新");
		builder.setMessage(versionDes);
		builder.setPositiveButton("更新", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				updateVerions();
				
			}
		});
		
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				//取消更新  跳转到主界面
				enterHome();
			}
		});
		//点击取消时的按钮
		builder.setOnCancelListener(new  DialogInterface.OnCancelListener(){

			@Override
			public void onCancel(DialogInterface dailog) {
				// TODO Auto-generated method stub
				enterHome();
				dailog.dismiss();
			}
		});
				
		builder.show();
	}
	
	//进入主页面
	public void enterHome(){
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		finish();
		
	}
	
	//用户注册
	public void userRegister(){
		Intent intent = new Intent(this,UserRegisterActivity.class);
		startActivity(intent);
		finish();
	}
	
	
	//更行App
	public void updateVerions(){
		String downPath = "";
		try {
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				downPath = Environment.getExternalStorageDirectory().getAbsolutePath();
				downPath = downPath+File.separator+app_name+".apk";
				HttpUtils httputil = new HttpUtils();
//				httputil.se
//				httputil.send(HttpMethod.POST, url, callBack)
				httputil.download(download_url, downPath, new RequestCallBack<File>() {
					
					@Override
					public void onSuccess(ResponseInfo<File> info) {
						// TODO Auto-generated method stub
						System.out.println("下载成功=====");
						Log.i("tag", "下载成功");
						File file = info.result;   //得到下载成功的APK
						
						//提示用户安装
						installAPK(file);
						
						
					}
					
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}
				});
				
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	//安装APK
	public void  installAPK(File file){
		//系统应用界面,源码,安装apk入口
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
		startActivityForResult(intent, 0);
		
	}
	
	//开启一个activity后,返回结果调用的方法
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//安装完成后  判断是否已经登录
		boolean is_login = SpUtils.checkUserLogin(this, Constants.LOGIN_STATE);
		if(is_login){
			//如果是登陆状态  则直接进入主页面
			enterHome();
		}else{
			userLogin();
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void initUI(){
		rl_root = (RelativeLayout) findViewById(R.id.rl_root);
		tv_version_name = (TextView) findViewById(R.id.tv_version_name);
	}
	
	
	//初始化
	public void initData(){
		//得到当前版本号
		String vname=  getVerionName();
		tv_version_name.setText(vname);
		mLocalVersionCode = getVersionCode();
		checkVersion();
		
	}
	
	
	
	//检查版本是否需要更新
	public void checkVersion(){
		VersionCheckInstance vi = new VersionCheckInstance();
		Thread tt = new Thread(vi);
		tt.start();
	}
	
	//得到版本code
	private int getVersionCode() {
		// TODO Auto-generated method stub
		//
		 PackageManager packageManager = getPackageManager();
		 //从包的管理者对象中 获取信息
		 int versioncode = 0;
		 try {
			PackageInfo packageinfo = packageManager.getPackageInfo(getPackageName(), 0);
			//获取版本名称
			versioncode = packageinfo.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return versioncode;
		 
	}
	
	public void initAnimation(){
		//初始化动画
		AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
		alphaAnimation.setDuration(3000);
		rl_root.startAnimation(alphaAnimation);
		
	}
	
	
	
	//获取版本名称
	public String getVerionName(){
		 PackageManager packageManager = getPackageManager();
		 //从包的管理者对象中 获取信息
		 String name = "";
		 try {
			PackageInfo packageinfo = packageManager.getPackageInfo(getPackageName(), 0);
			//获取版本名称
			name = packageinfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return name;
	}
	
	
	public class  VersionCheckInstance implements Runnable{
		String path = "http://192.168.48.1:8080/versionData.json";
		Message mess = handler.obtainMessage();
		Long starttime = System.currentTimeMillis();
		@Override
		public void run() {
			// TODO Auto-generated method stub
			URL url;
			try {
				url = new URL(path);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				int rescode = connection.getResponseCode();
				//请求超时2秒钟
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(3000);
				if(rescode==200){
					InputStream is = connection.getInputStream();
					String jsonstr=  StreamUtil.stream2String(is);
					JSONObject jobj = new JSONObject(jsonstr);
					
					
					String vserioncode = jobj.getString("version_code");
					app_name = jobj.getString("app_name");
				    download_url = jobj.getString("download_url");
				    versionDes = jobj.getString("versionDes");
				    
					if(Integer.parseInt(vserioncode)>mLocalVersionCode){
						//需要下载新版本
						mess.what = UPDATE_VERSION;
						
					}else{
						mess.what = ENTER_HOME;
					}
					
				}
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mess.what = URL_ERROR;
			}finally{
				Long endtime = System.currentTimeMillis();
				
				if(endtime-starttime <4000){
					Long lefttime = 4000-(endtime-starttime);
					try {
						Thread.sleep(lefttime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				handler.sendMessage(mess);
			}
			
			
			
			
			
		}
		
		
	}
	
}
