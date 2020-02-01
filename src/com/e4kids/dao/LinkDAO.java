package com.e4kids.dao;

import java.util.ArrayList;
import java.util.List;

import com.e4kids.model.Link;
import com.e4kids.model.Word;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LinkDAO {

	public static final String TAG = "LinkDAO";
	private Context mContext;
	// Database fields
	private SQLiteDatabase mDatabase;
	private DatabaseHelper mDbHelper;
	private String[] mAllColumns = { 
			DatabaseHelper.COLUMN_LINK_ID,
			DatabaseHelper.COLUMN_LINK,
			DatabaseHelper.COLUMN_LINK_CONTENT,
			DatabaseHelper.COLUMN_LINK_TYPE };

	public LinkDAO(Context context) {
		this.mContext = context;
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
	
	public Link createLink(String link) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COLUMN_LINK, link);
		long insertId = mDatabase.insert(DatabaseHelper.TABLE_LINK, null, values);
		Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_LINK, mAllColumns,
				                        DatabaseHelper.COLUMN_LINK_ID + " = " + insertId, null, null,null, null);
		cursor.moveToFirst();
		Link newLink = cursorToLink(cursor);
		cursor.close();
		return newLink;
	}

	public void deleteLink(Link link) {
		long id = link.getId();
		System.out.println("the deleted employee has the id: " + id);
		mDatabase.delete(DatabaseHelper.TABLE_LINK, DatabaseHelper.COLUMN_LINK_ID
				+ " = " + id, null);
	}

	public List<Link> getAllLinks(long type) {  // type = 1 -> dialog link, type = 0 -> story link
		List<Link> listLinks = new ArrayList<Link>();
		Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_LINK, mAllColumns,DatabaseHelper.COLUMN_LINK_TYPE+" =?",
				new String[]{String.valueOf(type)},null,null,null);			
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Link link = cursorToLink(cursor);
			listLinks.add(link);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return listLinks;
	}
	
	
	private Link cursorToLink(Cursor cursor) {
		Link link = new Link();
		link.setId(cursor.getLong(0));
		link.setLink(cursor.getString(1));
		link.setContent(cursor.getString(2));
		return link;
	}

}
