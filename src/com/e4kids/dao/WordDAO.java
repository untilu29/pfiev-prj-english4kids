package com.e4kids.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.e4kids.model.Word;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class WordDAO {
	public static final String TAG = "WordDAO";
	private Context mContext;
	// Database fields
	private SQLiteDatabase mDatabase;
	private DatabaseHelper mDbHelper;
	private String[] mAllColumns = { DatabaseHelper.COLUMN_WORD_ID,
			DatabaseHelper.COLUMN_TOPIC_ID,
			DatabaseHelper.COLUMN_WORD
			 };

	public WordDAO(Context context) {
		mDbHelper = new DatabaseHelper(context);
		this.mContext = context;
		// open the database
		try {
			open();
		} catch (SQLException e) {
			Log.e(TAG, "SQLException on openning database " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void open() throws SQLException {
		mDatabase = mDbHelper.getWritableDatabase();
	}

	public void close() {
		mDbHelper.close();
	}

	public Word createWord(String word, long topicId) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COLUMN_WORD, word);
		values.put(DatabaseHelper.COLUMN_TOPIC_ID, topicId);
		long insertId = mDatabase.insert(DatabaseHelper.TABLE_WORD, null, values);
		Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_WORD, mAllColumns,
				                        DatabaseHelper.COLUMN_WORD_ID + " = " + insertId, null, null,null, null);
		cursor.moveToFirst();
		Word newWord = cursorToWord(cursor);
		cursor.close();
		return newWord;
	}

	public void deleteWord(Word word) {
		long id = word.getId();
		System.out.println("the deleted employee has the id: " + id);
		mDatabase.delete(DatabaseHelper.TABLE_WORD, DatabaseHelper.COLUMN_WORD_ID
				+ " = " + id, null);
	}

	public List<Word> getAllWords() {
		List<Word> listWords = new ArrayList<Word>();
		Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_WORD, mAllColumns,
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Word word = cursorToWord(cursor);
			listWords.add(word);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return listWords;
	}
	
	public List<Word> getAllRandom(){
		List<Word> listWords = new ArrayList<Word>();
		Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_WORD, mAllColumns,
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Word word = cursorToWord(cursor);
			listWords.add(word);
			cursor.moveToNext();	
		}
		// make sure to close the cursor
		cursor.close();
		
//		Random generation
		long seed = System.nanoTime();
		Collections.shuffle(listWords, new Random(seed));
		
		return listWords;
	}

	public List<Word> getWordsOfTopic(long topicId) {
		List<Word> listWords = new ArrayList<Word>();

		Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_WORD, mAllColumns,
				DatabaseHelper.COLUMN_TOPIC_ID + " = ?",
				new String[] { String.valueOf(topicId) }, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Word word = cursorToWord(cursor);
			listWords.add(word);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return listWords;
	}

	private Word cursorToWord(Cursor cursor) {
		Word word = new Word();
		word.setId(cursor.getLong(0));
		word.setContent(cursor.getString(2));
		// get The Topic by id
		long topicId = cursor.getLong(2);
		word.setTopicId(topicId);
		return word;
	}


}
