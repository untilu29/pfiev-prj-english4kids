package com.example.english4kids;

import com.e4kids.adapter.ListDialogAdapter;
import com.e4kids.dao.LinkDAO;
import com.e4kids.helper.Network;
import com.e4kids.model.Link;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class DialogpageActivity extends AtractActivity {
	
	private ListView dialogListView;
	private ListDialogAdapter ldAdapter;
	private LinkDAO linkDAO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialogpage);
		dialogListView = (ListView)findViewById(R.id.listDialogView);
		linkDAO = new LinkDAO(this);
		ldAdapter = new ListDialogAdapter(this,linkDAO.getAllLinks(1));
		dialogListView.setAdapter(ldAdapter);
		dialogListView.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?>parent,View v,int position, long id){
				
				if(new Network(v.getContext()).isOnline()){
					
					Link choosedLink = ldAdapter.getItem(position);
					//startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(choosedLink.getLink())));	
					Intent myvideo = new Intent(getApplicationContext(),YoutubePlayerActivity.class);
					myvideo.putExtra("video_id",choosedLink.getLink());
					startActivity(myvideo);
					
					  
					   
					}else {
						new AlertDialog.Builder(v.getContext())
					    .setTitle("Check Network")
					    .setMessage("Please connect internet to watch dialogs !")
					    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) { 
					            // continue with delete
					        	startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
					        }
					     })
					    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) { 
					            // do nothing
					        }
					     })
					    .setIcon(android.R.drawable.ic_dialog_alert)
					     .show();
					}
					return;
				
			
				
			}
		});
		
   }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dialogpage, menu);
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
		return "Easy Dialog";
	}

	@Override
	protected void onClickBackButton() {
		// TODO Auto-generated method stub
		 Intent intent = new Intent(DialogpageActivity.this,MainMenuActivity.class);
		 startActivity(intent);
		   
	}
	@Override
	public void onBackPressed() {
	    // your code.
		//super.onBackPressed();
		Intent intent = new Intent(DialogpageActivity.this,MainMenuActivity.class);
		startActivity(intent);
	}
}
