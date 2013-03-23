/*
			Assignment # 5
			File Name :Movies.java
			Full name of all students in our group:
			                   Raja Narasimha Karthik Tangirala. (UNC Charlotte ID : 800791204)
			                   Chakraprakash Venigella .(UNC Charlotte ID : 800781600)

 */
package com.trnkarthik.rottentomatoes;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;

@SuppressLint("NewApi")
public class Movies {
	int year;
	String id, mpaa_rating,title;

	// this is present in object p
	JSONObject ratings;

	String critics_rating=null,audience_rating=null;

	// this is present in object p
	JSONObject posters;

	String thumbnail;

	public Movies(NewMovie n) {
		
		id=n.get_id();
		year=Integer.parseInt(n.getYear());
		mpaa_rating=n.getMpaa_rating();
		title=n.getTitle();
		audience_rating=n.getAudience_rating();
		critics_rating=n.getCritics_rating();
		thumbnail=n.getThumbnail();	
				
	}
	public Movies(JSONObject p) {
		try {
			critics_rating = null;
			audience_rating = null;
			id = p.getString("id");
			mpaa_rating = p.getString("mpaa_rating");
			title = p.getString("title");
			year = p.getInt("year");


			//getting thumbnail from posters object which is included in p object

			posters = p.getJSONObject("posters");
			thumbnail = posters.getString("thumbnail");


			//getting rating from ratings object which is included in p object
			ratings = p.getJSONObject("ratings");

			critics_rating = ratings.getString("critics_rating");
			audience_rating = ratings.getString("audience_rating");

		} catch (JSONException e) {
		}
	}
	public String toString() {
		return "Movie Title=" + title + "   url :" + thumbnail + " ";
	}


	//getter setter methods

	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMpaa_rating() {
		return mpaa_rating;
	}
	public void setMpaa_rating(String mpaa_rating) {
		this.mpaa_rating = mpaa_rating;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public JSONObject getRatings() {
		return ratings;
	}
	public void setRatings(JSONObject ratings) {
		this.ratings = ratings;
	}
	public String getCritics_rating() {
		return critics_rating;
	}
	public void setCritics_rating(String critics_rating) {
		this.critics_rating = critics_rating;
	}
	public String getAudience_rating() {
		return audience_rating;
	}
	public void setAudience_rating(String audience_rating) {
		this.audience_rating = audience_rating;
	}
	public JSONObject getPosters() {
		return posters;
	}
	public void setPosters(JSONObject posters) {
		this.posters = posters;
	}
	public String getThumbnail() {
		return this.thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}


}
