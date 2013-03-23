/*
			Assignment # 5
			File Name :NewMovies.java
			Full name of all students in our group:
			                   Raja Narasimha Karthik Tangirala. (UNC Charlotte ID : 800791204)
			                   Chakraprakash Venigella .(UNC Charlotte ID : 800781600)

 */
package com.trnkarthik.rottentomatoes;


public class NewMovie {
	public String _id;
	public String year;
	public String mpaa_rating;
	public String title;
	public String audience_rating;
	public String critics_rating;
	public String thumbnail;

	public NewMovie() {
	}
	public NewMovie(Movies movies) {
		_id = movies.getId();
		year=movies.getYear()+"";
		mpaa_rating=movies.getMpaa_rating();
		title=movies.getTitle();
		audience_rating=movies.getAudience_rating();
		critics_rating=movies.getCritics_rating();
		thumbnail=movies.getThumbnail();
	}


	public NewMovie(AdvMovies movies) {
		_id = movies.getId();
		year=movies.getYear()+"";
		mpaa_rating=movies.getMpaa_rating();
		title=movies.getTitle();
		audience_rating=movies.getAudience_rating();
		critics_rating=movies.getCritics_rating();
		thumbnail=movies.getThumbnail();
	}
	@Override
	public String toString() {
		return "Note [_id=" + _id + ", year=" + year + ", mpaa_rating=" + mpaa_rating + 
					", title=" + title+ ", audience_rating=" + audience_rating+ ", critics_rating=" 
				+ critics_rating+ ", thumbnail=" + thumbnail   + "]";
	}


	//getter setter methods



	public String get_id() {
		return _id;
	}


	public void set_id(String _id) {
		this._id = _id;
	}


	public String getYear() {
		return year;
	}


	public void setYear(String year) {
		this.year = year;
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


	public String getAudience_rating() {
		return audience_rating;
	}


	public void setAudience_rating(String audience_rating) {
		this.audience_rating = audience_rating;
	}


	public String getCritics_rating() {
		return critics_rating;
	}


	public void setCritics_rating(String critics_rating) {
		this.critics_rating = critics_rating;
	}


	public String getThumbnail() {
		return this.thumbnail;
	}


	public void setThumbnail(String thumbnail) {
		this.thumbnail=thumbnail + "";

	}
}
