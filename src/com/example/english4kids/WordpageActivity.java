package com.example.english4kids;


import java.util.Locale;
import com.e4kids.adapter.ListWordAdapter;
import com.e4kids.dao.WordDAO;
import com.e4kids.model.Word;
import com.example.english4kids.R;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;


public class WordpageActivity extends AtractActivity {

	private GridView wordGrView;
	private ListWordAdapter listwordAdapter;
	private WordDAO wordDao;
	private TextToSpeech tts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wordpage);
		//get idtopic
		Intent i = getIntent();
		long idtopic = i.getExtras().getLong("idTopic");
	    tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
	          @Override
	          public void onInit(int status) {
	             if(status != TextToSpeech.ERROR) 
	             {
	                tts.setLanguage(Locale.UK);
	             }
	          }
	       });
		
		wordDao = new WordDAO(this);
		listwordAdapter = new ListWordAdapter(this,wordDao.getWordsOfTopic(idtopic),idtopic);
		wordGrView = (GridView)findViewById(R.id.word_gridview);
		wordGrView.setAdapter(listwordAdapter);
		wordGrView.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?>parent,View v,int position,long id){
				Word clickedWord = listwordAdapter.getItem(position);
				Toast.makeText(getApplicationContext(), clickedWord.getContent(),Toast.LENGTH_SHORT).show();
	            tts.speak(clickedWord.getContent(), TextToSpeech.QUEUE_FLUSH, null);
				
			}
		});
	}  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wordpage, menu);
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
	
	public void onPause(){
		if(tts !=null){
			tts.stop();
			tts.shutdown();
		}
		super.onPause();
	}

	public String getTopicTitle(){
		Intent i = this.getIntent();
		String nametopic= i.getExtras().getString("nameTopic");
		return nametopic.substring(0, 1).toUpperCase()+nametopic.substring(1);
	}
	@Override
	protected String getTitleText() {
		return getTopicTitle();
	}

	@Override
	protected void onClickBackButton() {
	    Intent intent = new Intent(WordpageActivity.this,TopicpageActivity.class);
        startActivity(intent);
        return;
	}
	@Override
	public void onBackPressed() {
	    // your code.
		//super.onBackPressed();
		Intent intent = new Intent(WordpageActivity.this,TopicpageActivity.class);
        startActivity(intent);
	}

}
