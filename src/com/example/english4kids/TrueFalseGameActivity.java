package com.example.english4kids;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.e4kids.adapter.TrueFalseGameAdapter;
import com.e4kids.dao.WordDAO;
import com.e4kids.model.Word;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListView;

public class TrueFalseGameActivity extends AtractActivity {

	private ListView listView;
	private TrueFalseGameAdapter listtruefalseAdapter;
	private WordDAO wordDao;
	private List<Word> listwords = new ArrayList<Word>();
	private Timer myTimer;
	private int numRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_true_false_game);

		wordDao = new WordDAO(this);
		listwords = wordDao.getAllRandom().subList(0, 3); // random
		listtruefalseAdapter = new TrueFalseGameAdapter(this, listwords);
		listView = (ListView) findViewById(R.id.listTrueFalseItemView);
		listView.setAdapter(listtruefalseAdapter);

		// Tắt action click, tắt thanh cuộn, làm trong suốt đường viền
		listView.setClickable(false);
		listView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return (event.getAction() == MotionEvent.ACTION_MOVE);
			}
		});
		listView.setDivider(null);
		// --------------------------

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu., menu);
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
		return null;
	}

	@Override
	protected void onClickBackButton() {
		// TODO Auto-generated method stub

	}
	 }
