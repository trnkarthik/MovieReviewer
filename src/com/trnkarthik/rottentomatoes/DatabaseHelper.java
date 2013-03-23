/*
			Assignment # 5
			File Name :DatabaseHelper.java
			Full name of all students in our group:
			                   Raja Narasimha Karthik Tangirala. (UNC Charlotte ID : 800791204)
			                   Chakraprakash Venigella .(UNC Charlotte ID : 800781600)

 */
package com.trnkarthik.rottentomatoes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{
	static final String DATABASE_NAME = "notes.db";
	static final int DATABASE_VERSION = 2;
	static final String TAG = "demo1";
	
	DatabaseHelper(Context mContext){
		super(mContext, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		MoviesTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading db from version " + oldVersion + " to " + newVersion + ", this will destroy all old data");
		MoviesTable.onUpgrade(db, oldVersion, newVersion);		
	}
}
