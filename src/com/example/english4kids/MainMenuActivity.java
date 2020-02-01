package com.example.english4kids;
import com.example.english4kids.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.*;

public class MainMenuActivity extends Activity {


	private MediaPlayer bkgrdmsc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		
		
		final ImageView vocaView = (ImageView)findViewById(R.id.vocaView);
		vocaView.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				   Intent i = new Intent(getApplicationContext(), TopicpageActivity.class);       
			       startActivity(i);
			}
		});

		
		final ImageView dialogView = (ImageView)findViewById(R.id.dialogView);
		dialogView.setOnClickListener(new OnClickListener(){
			public void onClick(View v){  
				   Intent i = new Intent(getApplicationContext(), DialogpageActivity.class); 
			       startActivity(i);
			}
		});
		
		final ImageView gameView = (ImageView)findViewById(R.id.gameView);
		 gameView.setOnClickListener(new OnClickListener(){
			public void onClick(View v){ 
				  Intent i = new Intent(getApplicationContext(), TrueFalseGameActivity.class); 
			       startActivity(i);
			}
		});
		
		final ImageView storyView = (ImageView)findViewById(R.id.storyView);
		storyView.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				   Intent i = new Intent(getApplicationContext(), DialogpageActivity.class); 
			       startActivity(i);
			}
		});
		
		
		final ImageView exitView = (ImageView)findViewById(R.id.exitView);
		exitView.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				new AlertDialog.Builder(v.getContext())
			    .setTitle("Message")
			    .setMessage("Are you sure to exit this app ?")
			    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
//			        	int pid = android.os.Process.myPid();
//				        android.os.Process.killProcess(pid);
				        exit();
				     //   System.exit(0);
				       
			        }
			     })
			    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // do nothing
			        }
			     })
			    .setIcon(android.R.drawable.ic_delete)
			     .show();
			}
		});
	
//		RotateAnimation anim = new RotateAnimation(60.0f, 120.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//		anim.setInterpolator(new LinearInterpolator());
//		anim.setRepeatCount(Animation.INFINITE);
//		anim.setDuration(700);
//		
		Animation fadeIn = new AlphaAnimation(0.3f, 1.0f);
		fadeIn.setDuration(1000);

		Animation fadeOut = new AlphaAnimation(1.0f, 0.3f);
		fadeOut.setRepeatCount(Animation.INFINITE);
		fadeOut.setStartOffset(1000);
		fadeOut.setDuration(1000);
		
		AnimationSet animation = new AnimationSet(false); //change to false
		animation.addAnimation(fadeIn);
		animation.addAnimation(fadeOut);
		animation.setRepeatCount(Animation.INFINITE);
	
	 // Start animating the image
	//	final ImageView splash = (ImageView) findViewById(R.id.splash);
		vocaView.startAnimation(animation);
		dialogView.startAnimation(animation);
		gameView.startAnimation(animation);
		storyView.startAnimation(animation);
		
		bkgrdmsc = MediaPlayer.create(MainMenuActivity.this, R.raw.mainmenu);
		bkgrdmsc.setLooping(true);
		bkgrdmsc.start();
		
	}

	@Override
	public void onResume(){
		super.onResume();
	}
	@Override
	public void onPause() {
	    super.onPause();
	    bkgrdmsc.release();
	    finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void exit(){
		   Intent intent = new Intent(Intent.ACTION_MAIN);
		   intent.addCategory(Intent.CATEGORY_HOME);
		   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		   startActivity(intent);
		   finish();
		   System.exit(0);
	}
	
	@Override
	public void onBackPressed() {
//		   int pid = android.os.Process.myPid();
//           android.os.Process.killProcess(pid);
//           System.exit(0);
	
		   Intent intent = new Intent(Intent.ACTION_MAIN);
		   intent.addCategory(Intent.CATEGORY_HOME);
		   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		   startActivity(intent);
		   finish();
		   System.exit(0);
	}
}
