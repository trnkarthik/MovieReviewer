package com.trnkarthik.rottentomatoes;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MoviesDAO { //Data Access Object (DAO)
	private SQLiteDatabase db;

	
	public MoviesDAO(SQLiteDatabase db){
		this.db = db;
	}
	
	public long save(NewMovie note){
		ContentValues values = new ContentValues();
		values.put(MoviesTable.MOVIE_ID, note.get_id());
		values.put(MoviesTable.MOVIE_YEAR, note.getYear());
		values.put(MoviesTable.MOVIE_MPAA_Rating, note.getMpaa_rating());
		values.put(MoviesTable.MOVIE_TITLE, note.getTitle());
		values.put(MoviesTable.MOVIE_AUDIENCE_Rating, note.getAudience_rating());
		values.put(MoviesTable.MOVIE_CRITICS_Rating, note.getCritics_rating());
		return db.insert(MoviesTable.TABLE_NAME, null, values);
	}
	
	public boolean update(NewMovie note){
		ContentValues values = new ContentValues();
		
		values.put(MoviesTable.MOVIE_ID, note.get_id());
		values.put(MoviesTable.MOVIE_YEAR, note.getYear());
		values.put(MoviesTable.MOVIE_MPAA_Rating, note.getMpaa_rating());
		values.put(MoviesTable.MOVIE_TITLE, note.getTitle());
		values.put(MoviesTable.MOVIE_AUDIENCE_Rating, note.getAudience_rating());
		values.put(MoviesTable.MOVIE_CRITICS_Rating, note.getCritics_rating());

		
		return db.update(MoviesTable.TABLE_NAME, values, MoviesTable.MOVIE_ID+"="+ note.get_id(), null) > 0;		
	}	
	
	
	public boolean delete(NewMovie note){
		return db.delete(MoviesTable.TABLE_NAME, MoviesTable.MOVIE_ID+"="+note.get_id(), null)>0;
	}
	
	public NewMovie get(long id){

		NewMovie note = null;

		Cursor c = db.query(true, MoviesTable.TABLE_NAME, 
				new String[]{MoviesTable.MOVIE_ID, MoviesTable.MOVIE_YEAR, MoviesTable.MOVIE_MPAA_Rating,
						MoviesTable.MOVIE_TITLE,MoviesTable.MOVIE_AUDIENCE_Rating,
						MoviesTable.MOVIE_CRITICS_Rating,MoviesTable.MOVIE_THUMBNAIL}, 
				MoviesTable.MOVIE_ID+"="+ id, null, null, null, null, null);
		if(c != null){
			c.moveToFirst();
			note = this.buildNoteFromCursor(c);			
		}	
		
		if(!c.isClosed()){
			c.close();
		}		
		if(note.get_id().equals(null))
		{
			Log.d("retcheck", "yes we ");
		}
		Log.d("retcheck", "no we ");

		return note;
	}
	
	public List<NewMovie> getAll(){
		List<NewMovie> list = new ArrayList<NewMovie>();
		Cursor c = db.query(MoviesTable.TABLE_NAME, 
				new String[]{MoviesTable.MOVIE_ID, MoviesTable.MOVIE_YEAR, MoviesTable.MOVIE_MPAA_Rating,
				MoviesTable.MOVIE_TITLE,MoviesTable.MOVIE_AUDIENCE_Rating,
				MoviesTable.MOVIE_CRITICS_Rating,MoviesTable.MOVIE_THUMBNAIL}, 
				null, null, null, null, null);
		if(c != null){
			c.moveToFirst();			
			do{
				NewMovie note = this.buildNoteFromCursor(c);
				if(note != null){
					list.add(note);
				}				
			} while(c.moveToNext());
			
			if(!c.isClosed()){
				c.close();
			}
		}
		return list;
	}
	
	private NewMovie buildNoteFromCursor(Cursor c){
		NewMovie note = null;	

		if(c != null){
			note = new NewMovie();
			note.set_id(c.getString(0));
			note.setYear(c.getString(1));
			note.setMpaa_rating(c.getString(2));			
			note.setTitle(c.getString(3));			
			note.setAudience_rating(c.getString(4));			
			note.setCritics_rating(c.getString(5));			
			note.setThumbnail(c.getString(6));			
		}
		Log.d("retcheck", note._id +"		just in ");

		return note;
	}
}
