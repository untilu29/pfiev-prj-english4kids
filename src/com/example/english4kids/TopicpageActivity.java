package com.example.english4kids;

import com.e4kids.adapter.ListTopicAdapter;
import com.e4kids.dao.TopicDAO;
import com.e4kids.model.Topic;
import com.example.english4kids.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class TopicpageActivity extends AtractActivity {

	private TopicDAO tpdao;
	private GridView tpGrView;
	private ListTopicAdapter ltpAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topicpage);
		
		tpGrView = (GridView)findViewById(R.id.topic_gridview);
		tpdao = new TopicDAO(this);
		ltpAdapter = new ListTopicAdapter(this,tpdao.getAllTopics());
		tpGrView.setAdapter(ltpAdapter);

		tpGrView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
				Topic clickedtp = ltpAdapter.getItem(position);
				//Toast.makeText(	getApplicationContext(),clickedtp.getName()+"", Toast.LENGTH_SHORT).show();
				Intent singletopic = new Intent(getApplicationContext(), WordpageActivity.class);
	            singletopic.putExtra("idTopic", clickedtp.getId());
	            singletopic.putExtra("nameTopic", clickedtp.getName());
	            startActivity(singletopic);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.topicpage, menu);
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
	protected String getTitleText() {
		// TODO Auto-generated method stub
		return "Topics";
	}

	@Override
	protected void onClickBackButton() {
		// TODO Auto-generated method stub
	    Intent intent = new Intent(TopicpageActivity.this,MainMenuActivity.class);
        startActivity(intent);
        return;
	}
	@Override
	public void onBackPressed() {
	    // your code.
		//super.onBackPressed();
		Intent intent = new Intent(TopicpageActivity.this,MainMenuActivity.class);
        startActivity(intent);
	}
}
