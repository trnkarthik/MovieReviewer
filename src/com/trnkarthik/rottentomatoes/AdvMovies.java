package com.trnkarthik.rottentomatoes;

import android.annotation.SuppressLint;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("NewApi")
public class AdvMovies {
	int year;
	String id, mpaa_rating,title;
	String audience_score;
	String critics_score;
	String critics_rating;
	String audience_rating;
	String runtime;

	JSONObject release_dates;
	String theater;

	// this is present in object p
	JSONObject ratings;
	JSONObject posters;
	JSONObject links;

	String detailed;
	String thumbnail;
	String alternate;


	public AdvMovies(JSONObject movieJSON) {
		try {

			runtime = movieJSON.getString("runtime");
			title = movieJSON.getString("title");
			year = movieJSON.getInt("year");

			critics_rating = null;
			audience_rating = null;


			id = movieJSON.getString("id");

			release_dates = movieJSON.getJSONObject("release_dates");
			theater = release_dates.getString("theater");


			//getting alternate link from posters object which is included in p object
			links = movieJSON.getJSONObject("links");
			alternate = links.getString("alternate");

			mpaa_rating = movieJSON.getString("mpaa_rating");

			//getting detailed image from posters object which is included in p object
			posters = movieJSON.getJSONObject("posters");
			detailed = posters.getString("detailed");
			thumbnail = posters.getString("thumbnail");


			ratings = movieJSON.getJSONObject("ratings");
			critics_score = ratings.getString("critics_score");
			audience_score = ratings.getString("audience_score");
			critics_rating = ratings.getString("critics_rating");
			audience_rating = ratings.getString("audience_rating");





		} catch (JSONException e) {
		}
	}
	public String toString() {
		return "Movie Title=" + detailed + "  " + audience_rating + " " + alternate + "  " + critics_score;
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
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}




}
