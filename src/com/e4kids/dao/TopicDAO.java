package com.e4kids.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.e4kids.model.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.util.Log;

public class TopicDAO {
    public static final String TAG = "TopicDAO";
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;
    private Context mContext;
    private String[] mAllColumns = { 
    		DatabaseHelper.COLUMN_TOPIC_ID,
    		DatabaseHelper.COLUMN_TOPIC_NAME };
   
	public TopicDAO(Context context) {
		this.mContext = context;
		mDbHelper = new DatabaseHelper(context);
		//open the database
		try{
			open();
		}catch(SQLException e){
			Log.e(TAG, "SQLException on opening database "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void open()throws SQLException{
		mDatabase = mDbHelper.getWritableDatabase();
	}

	public void close(){
		mDbHelper.close();
	}
	
	public Topic createTopic(String name) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COLUMN_TOPIC_NAME, name);
		long insertId = mDatabase
				.insert(DatabaseHelper.TABLE_TOPIC, null, values);
		Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_TOPIC, mAllColumns,
				DatabaseHelper.COLUMN_TOPIC_ID + " = " + insertId, null, null,
				null, null);
		cursor.moveToFirst();
		Topic newTopic = cursorToTopic(cursor);
		cursor.close();
		return newTopic;
	}

	public void deleteTopic(Topic topic) {
		long id = topic.getId();
		// delete all words of this topic
		WordDAO wordDao = new WordDAO(mContext);
		List<Word> listWords = wordDao.getWordsOfTopic(id);
		if (listWords != null && !listWords.isEmpty()) {
			for (Word w : listWords) {
				wordDao.deleteWord(w);
			}
		}
		System.out.println("the deleted Topic has the id: " + id);
		mDatabase.delete(DatabaseHelper.TABLE_TOPIC, DatabaseHelper.COLUMN_TOPIC_ID
				+ " = " + id, null);
	}

	public List<Topic> getAllTopics() {
		List<Topic> listTopics = new ArrayList<Topic>();

		Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_TOPIC, mAllColumns,
				null, null,null,null,null);
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Topic topic = cursorToTopic(cursor);
				listTopics.add(topic);
				cursor.moveToNext();
			}

			// make sure to close the cursor
			cursor.close();
		}
		return listTopics;
	}

	public Topic getTopicById(long id) {
		Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_TOPIC, mAllColumns,
				DatabaseHelper.COLUMN_TOPIC_ID + " = ?",
				new String[] { String.valueOf(id) }, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		Topic topic = cursorToTopic(cursor);
		return topic;
	}

	protected Topic cursorToTopic(Cursor cursor) {
		Topic topic = new Topic();
		topic.setId(cursor.getLong(0));
		topic.setName(cursor.getString(1));
		return topic;
	}

}
