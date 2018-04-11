package com.example.swipecardspro_eva01.activity;
import com.example.swipecardspro_eva01.R;
import com.example.swipecardspro_eva01.R.id;
import com.example.swipecardspro_eva01.R.layout;
import com.example.swipecardspro_eva01.R.menu;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class RadioActivity extends Activity{
	VideoView vv = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Vitamio.isInitialized(RadioActivity.this);
		setContentView(R.layout.activity_rideo);
		vv = (VideoView) findViewById(R.id.vv);
//		String path = "http://192.168.1.3:8080/1111.avi";
		String path = "http://39.106.208.119/mv_clip1.avi";
		vv.setVideoPath(path);
		vv.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				vv.start();
			}
		});
		vv.setMediaController(new MediaController(this));
		
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
