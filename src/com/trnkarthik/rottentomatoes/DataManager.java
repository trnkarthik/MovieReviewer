package com.trnkarthik.rottentomatoes;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataManager {
	Context mContext;
	DatabaseHelper dbOpenHelper;
	SQLiteDatabase db;
	MoviesDAO noteDao;
	
	public DataManager(Context mContext){
		this.mContext = mContext;
		dbOpenHelper = new DatabaseHelper(mContext);
		db = dbOpenHelper.getWritableDatabase();
		noteDao = new MoviesDAO(db);
	}
	
	public void close(){
		db.close();
	}
	
	public long saveNote(NewMovie note){
		return noteDao.save(note);
	}
	
	public boolean updateNote(NewMovie note){
		return noteDao.update(note);
	}
	
	public boolean deleteNote(NewMovie note){
		return noteDao.delete(note);
	}
	
	public NewMovie getNote(long id){
		return noteDao.get(id);
	}
	
	public List<NewMovie> getAllNotes(){
		return noteDao.getAll();
	}
}
