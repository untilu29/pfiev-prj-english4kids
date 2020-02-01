package com.example.english4kids;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class GamepageActivity extends Activity{

	protected MediaPlayer bgmusik;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamepage);
		
		bgmusik=MediaPlayer.create(GamepageActivity.this,R.raw.gamebg);
		bgmusik.setLooping(true);
		bgmusik.start();
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    bgmusik.release();
	    finish();
	}
}
