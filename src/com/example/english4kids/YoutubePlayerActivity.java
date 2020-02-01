package com.example.english4kids;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class YoutubePlayerActivity extends AtractActivity  {

	public static final String API_KEY ="AIzaSyC6BijiqH9AZKBfTm7o7tLJciBJPW55fsc";
	//http://youtu.be/<VIDEO_ID>
	public static  String VIDEO_ID = "";
	private static final int RECOVERY_DIALOG_REQUEST = 1;
	private WebView wv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youtube_player);
//		YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
//		youTubePlayerView.initialize(API_KEY, this);
		Intent i = getIntent();
		VIDEO_ID = i.getStringExtra("video_id");
		
		wv=(WebView)findViewById(R.id.webview);
		wv.getSettings().setPluginState(PluginState.ON);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setWebChromeClient(new WebChromeClient());
		
		final ProgressDialog pd=new ProgressDialog(this);
		pd.show();
		wv.loadUrl("https://www.youtube.com/embed/"+VIDEO_ID+"?autoplay=1");
		wv.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                //hide loading image
                //findViewById(R.id.imageLoading1).setVisibility(View.GONE);
                //show webview
                //findViewById(R.id.webview).setVisibility(View.VISIBLE);
            	pd.dismiss();
            }


        });     
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.youtube_player, menu);
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

	@Override
	public void onBackPressed() {
	    // your code.
		super.onBackPressed();
		wv.destroy();
	}

	@Override
	protected String getTitleText() {
		// TODO Auto-generated method stub
		return "Easy Dialog";
	}

	@Override
	protected void onClickBackButton() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(YoutubePlayerActivity.this,DialogpageActivity.class);
		startActivity(intent);
		wv.destroy();
	}
}
