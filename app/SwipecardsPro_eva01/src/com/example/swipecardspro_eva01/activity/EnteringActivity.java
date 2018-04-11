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
	
	private int mLocalVersionCode;   //����app�İ汾��
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int messres = msg.what;
			switch (messres) {
			case UPDATE_VERSION:
				//�汾����
				showUpdateDialog();
				break;
			case ENTER_HOME:
				//������ҳ
				enterHome();
				break;
				
			case USER_REGISTER:
				//�û�ע��
				userRegister();
				break;	
			
			case USER_LOGIN:
				//�û���¼
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
		//��ʼ������
		initUI();
		initData();
		initAnimation();
		
	}
	
	
	
	//�û���¼
	public void userLogin(){
		
		Intent login_intent = new Intent(this,);
		
		
	}
	
	public void showUpdateDialog(){
		Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);   //�������Ͻ�ͼ��
		builder.setTitle("�汾����");
		builder.setMessage(versionDes);
		builder.setPositiveButton("����", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				updateVerions();
				
			}
		});
		
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				//ȡ������  ��ת��������
				enterHome();
			}
		});
		//���ȡ��ʱ�İ�ť
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
	
	//������ҳ��
	public void enterHome(){
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		finish();
		
	}
	
	//�û�ע��
	public void userRegister(){
		Intent intent = new Intent(this,UserRegisterActivity.class);
		startActivity(intent);
		finish();
	}
	
	
	//����App
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
						System.out.println("���سɹ�=====");
						Log.i("tag", "���سɹ�");
						File file = info.result;   //�õ����سɹ���APK
						
						//��ʾ�û���װ
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
	//��װAPK
	public void  installAPK(File file){
		//ϵͳӦ�ý���,Դ��,��װapk���
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
		startActivityForResult(intent, 0);
		
	}
	
	//����һ��activity��,���ؽ�����õķ���
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//��װ��ɺ�  �ж��Ƿ��Ѿ���¼
		boolean is_login = SpUtils.checkUserLogin(this, Constants.LOGIN_STATE);
		if(is_login){
			//����ǵ�½״̬  ��ֱ�ӽ�����ҳ��
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
	
	
	//��ʼ��
	public void initData(){
		//�õ���ǰ�汾��
		String vname=  getVerionName();
		tv_version_name.setText(vname);
		mLocalVersionCode = getVersionCode();
		checkVersion();
		
	}
	
	
	
	//���汾�Ƿ���Ҫ����
	public void checkVersion(){
		VersionCheckInstance vi = new VersionCheckInstance();
		Thread tt = new Thread(vi);
		tt.start();
	}
	
	//�õ��汾code
	private int getVersionCode() {
		// TODO Auto-generated method stub
		//
		 PackageManager packageManager = getPackageManager();
		 //�Ӱ��Ĺ����߶����� ��ȡ��Ϣ
		 int versioncode = 0;
		 try {
			PackageInfo packageinfo = packageManager.getPackageInfo(getPackageName(), 0);
			//��ȡ�汾����
			versioncode = packageinfo.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return versioncode;
		 
	}
	
	public void initAnimation(){
		//��ʼ������
		AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
		alphaAnimation.setDuration(3000);
		rl_root.startAnimation(alphaAnimation);
		
	}
	
	
	
	//��ȡ�汾����
	public String getVerionName(){
		 PackageManager packageManager = getPackageManager();
		 //�Ӱ��Ĺ����߶����� ��ȡ��Ϣ
		 String name = "";
		 try {
			PackageInfo packageinfo = packageManager.getPackageInfo(getPackageName(), 0);
			//��ȡ�汾����
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
				//����ʱ2����
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
						//��Ҫ�����°汾
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
