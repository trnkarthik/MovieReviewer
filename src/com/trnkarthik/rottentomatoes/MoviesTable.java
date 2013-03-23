/*
			Assignment # 5
			File Name :MoviesTable.java
			Full name of all students in our group:
			                   Raja Narasimha Karthik Tangirala. (UNC Charlotte ID : 800791204)
			                   Chakraprakash Venigella .(UNC Charlotte ID : 800781600)

 */
package com.trnkarthik.rottentomatoes;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class MoviesTable {
	static final String TABLE_NAME = "movies";
	static final String MOVIE_ID = "_id";
	static final String MOVIE_YEAR = "year";
	static final String MOVIE_MPAA_Rating = "mpaa_rating";
	static final String MOVIE_TITLE = "title";
	static final String MOVIE_AUDIENCE_Rating = "audience_rating";
	static final String MOVIE_CRITICS_Rating = "critics_rating";
	static final String MOVIE_THUMBNAIL = "thumbnail";
	
	static public void onCreate(SQLiteDatabase db){		
		StringBuilder sb = new StringBuilder();		
		sb.append("CREATE TABLE " + MoviesTable.TABLE_NAME + " (");
		sb.append(MoviesTable.MOVIE_ID + " varchar primary key , ");
		sb.append(MoviesTable.MOVIE_YEAR + " varchar , ");
		sb.append(MoviesTable.MOVIE_MPAA_Rating + " varchar , ");
		sb.append(MoviesTable.MOVIE_TITLE + " varchar , ");
		sb.append(MoviesTable.MOVIE_AUDIENCE_Rating + " varchar , ");
		sb.append(MoviesTable.MOVIE_CRITICS_Rating + " varchar ,");
		sb.append(MoviesTable.MOVIE_THUMBNAIL + " varchar ");
		sb.append(");");		
		try{
			db.execSQL(sb.toString());
		} catch (SQLException e){				
			e.printStackTrace();
		}
	}
	
	static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS " + MoviesTable.TABLE_NAME);
		MoviesTable.onCreate(db);
	}	
}
