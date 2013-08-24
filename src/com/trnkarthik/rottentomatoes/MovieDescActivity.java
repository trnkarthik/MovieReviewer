package com.trnkarthik.rottentomatoes;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieDescActivity extends Activity {

	private static DataManager dm;

	public Handler handler;
	HttpClient client = new DefaultHttpClient();

	//List of URLs to retrive the JSON files
	String URLForRetrievingMovie = 
			"api.rottentomatoes.com";

	AdvMovies moviesObject;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_desc);

		//Getting data from intent
		Bundle extras = getIntent().getExtras();
		String movieid = extras.getString("id");

		//settingup the database
		dm = new DataManager(this);


		//Loading selected Movie option on start of an Activity
		new JSONBackgroundStuff().execute(movieid+"");


		//back button
		ImageView backButton = (ImageView) findViewById(R.id.movieDesc_back);
		backButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});



	}

	private void afterloadingdone() {

		String critic_score_temp;
		String audience_score_temp;

		if(moviesObject.critics_score=="-1" )
		{
			critic_score_temp = "NA";
		}
		if(moviesObject.critics_score.equals(null))
		{
			critic_score_temp = "NA";
		}
		else
		{
			critic_score_temp = moviesObject.critics_score + "%";
		}

		if(moviesObject.audience_score=="-1" || moviesObject.audience_score==null)
		{
			audience_score_temp = "NA";
		}
		else
		{
			audience_score_temp = moviesObject.audience_score + "%";
		}

		//setting all the values

		ImageView audienceRating = (ImageView)findViewById(R.id.imageView1);
		if(moviesObject.audience_rating==null)
		{
			audienceRating.setImageResource(R.drawable.notranked);
			//	Log.d("test9", "audience changes");

		}
		else
		{
			audienceRating.setImageResource(getIDFromImageRating(moviesObject.audience_rating));
		}


		ImageView criticRating = (ImageView)findViewById(R.id.imageView2);
		if(moviesObject.critics_rating==null)
		{
			criticRating.setImageResource(R.drawable.notranked);
			//	Log.d("test9", "critics changes");
		}
		else
		{
			criticRating.setImageResource(getIDFromImageRating(moviesObject.critics_rating));
		}




		//Toast.makeText(getApplicationContext(),moviesObject.critics_rating +"     " + moviesObject.audience_rating , Toast.LENGTH_LONG).show();

		Log.d("test9", moviesObject.title+"");

		TextView title = (TextView)findViewById(R.id.movieDesc_title);
		title.setText(moviesObject.title+"");

		Log.d("test9", moviesObject.theater+"");

		TextView date = (TextView)findViewById(R.id.movieDesc_releaseDate);
		date.setText(moviesObject.theater+"");

		Log.d("test9", moviesObject.mpaa_rating+"");


		TextView mpaa_rating = (TextView)findViewById(R.id.movieDesc_mpaa_rating);
		mpaa_rating.setText(moviesObject.mpaa_rating+"");

		TextView time = (TextView)findViewById(R.id.movieDesc_runtime);
		time.setText(getLengthFromTime(moviesObject.runtime)+"");


		TextView critic_score = (TextView)findViewById(R.id.textView1);
		critic_score.setText(critic_score_temp+"");

		TextView audience_score = (TextView)findViewById(R.id.textView2);
		audience_score.setText(audience_score_temp+"");

		//Toast.makeText(getApplicationContext(),moviesObject.alternate +" " , Toast.LENGTH_LONG).show();
		Log.d("test9", moviesObject.alternate+"");

		ImageView browser = (ImageView)findViewById(R.id.movieDesc_web);
		browser.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(moviesObject.alternate));
				startActivity(browserIntent);
			}
		});





		//Loading an image using AsyncTask

		ImageView detailedImage = (ImageView)findViewById(R.id.movieDesc_detailed);
		detailedImage.setTag(moviesObject.detailed);

		Log.d("test9", moviesObject.detailed+"");

		new LoadImageFromURL().execute(detailedImage);


		//Now we have to deal with database stuff.
		//Here we need to Add/Remove to fav movies
		//First check the status of the movie

		final ImageView star = (ImageView)findViewById(R.id.movieDesc_star);

		setStar(star);

		star.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if(moviePresentInDataBase())
				{
					dm.deleteNote(getNewMovieFromAdvMovie(moviesObject));
					setStar(star);
				}
				else
				{
					dm.saveNote(getNewMovieFromAdvMovie(moviesObject));
					setStar(star);
				}
			}

		});


	}

	public NewMovie getNewMovieFromAdvMovie(AdvMovies movies) {
		NewMovie temp = new NewMovie(movies);
		return temp;
	}


	private void setStar(ImageView star) {
		if(moviePresentInDataBase())
		{
			star.setImageResource(R.drawable.rating_important);
		}
		else 
		{
			star.setImageResource(R.drawable.rating_not_important);
		}
	}

	public boolean moviePresentInDataBase() {		

		List<NewMovie> notes = dm.getAllNotes();
		for(int i=0;i<notes.size();i++){
			if(notes.get(i).get_id().equals(moviesObject.id))
			{
				return true;
			}
		}			
		return false;

	}


	@Override
	public void onDestroy() {
		dm.close();
		super.onDestroy();
	}


	private int getIDFromImageRating(String critics_rating) {

		if(critics_rating.equals("Certified Fresh"))
			return (R.drawable.certified_fresh);
		else if (critics_rating.equals("Rotten"))
			return (R.drawable.rotten);
		else if (critics_rating.equals("Upright"))
			return (R.drawable.upright);
		else if (critics_rating.equals("Spilled"))
			return (R.drawable.spilled);
		else if (critics_rating.equals("Fresh"))
			return (R.drawable.fresh);
		else
			return(R.drawable.notranked);

	}


	public String getLengthFromTime(String runtime) {

		int length = Integer.parseInt(runtime);
		int hours,min;
		hours = length/60;
		min = length%60;
		return (hours + ":"+min );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_movie_desc, menu);
		return true;
	}



	//Json Background AsyncTask call 

	private class JSONBackgroundStuff extends AsyncTask< String ,Boolean , StringBuffer> {


		public  StringBuffer sb = new StringBuffer("");
		String movieIDCalled;

		public ProgressDialog dialog;

		public void onPreExecute() {
			//setup the ProgressDialog on the MainUI here
			dialog =ProgressDialog.show(MovieDescActivity.this,
					null, "Loading Movies");
		}

		@Override
		public StringBuffer doInBackground(String... params) {

			movieIDCalled = params[0];

			try {
				sb = GetDataFromAPI();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}

			try {
				ParseJSONData(sb);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return sb; 
		}

		public void onPostExecute(StringBuffer sb ) {

			//changing message 
			dialog.setMessage("Finished Loading! ");

			@SuppressWarnings("unused")
			Thread ShowMessageForSomeTime = new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(3000);
						// Do some stuff
					} catch (Exception e) {
						e.getLocalizedMessage();
					}
				}
			});

			//dismissing the progressDialog
			if(dialog.isShowing())
			{
				dialog.dismiss();
			}

			afterloadingdone();


		}

		//other methods


		public StringBuffer GetDataFromAPI() throws IOException, URISyntaxException
		{

			public String api_key = "your_api_key"; //replace this with your api key
			
			BufferedReader  in  = null;
			List<NameValuePair>  params  = new ArrayList<NameValuePair>();

			URI  uri = null ;

			params.add(new BasicNameValuePair("apikey", api_key));

			uri  =  URIUtils.createURI("http", URLForRetrievingMovie, -1,"/api/public/v1.0/movies/"+movieIDCalled +".json" , 
					URLEncodedUtils.format(params, "UTF-8"), null);

			try {
				HttpGet  request  = new HttpGet(uri);
				HttpResponse  response  = client.execute(request);
				if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					in  = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					String  line  = "";
					while((line = in.readLine()) != null){
						sb.append(line  + "\n");
					}
					in.close();
					//bundle.putString("RESULT",  sb.toString());
				} else{
					//bundle.putString("ERROR", "Problem with Response");
				}
			} 
			catch (Exception e) {
				//bundle.putString("ERROR", "Problem with URL");
			}

			Log.d("BBB2", sb.toString());
			return sb;
		}

		private void ParseJSONData(StringBuffer sb) throws IOException, JSONException {

			InputStream in =new ByteArrayInputStream(sb.toString().getBytes());

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder strbuilder = new StringBuilder();
			String line = reader.readLine();
			while(line != null){
				strbuilder.append(line);
				line = reader.readLine();
			}
			reader.close();

			//JSONObject initobj = new JSONObject(strbuilder.toString());
			//JSONArray movies =  initobj.getJSONArray("movies");   


			JSONObject movieJSON = new JSONObject(strbuilder.toString());  
			moviesObject = new AdvMovies(movieJSON);
			Log.d("demo3", moviesObject.toString());


		}     
		// end of ParseJSONData
	}
	//end of AsyncTask




	public class LoadImageFromURL extends AsyncTask<ImageView, Void, Drawable> {

		ImageView iv;
		@Override
		protected Drawable doInBackground(ImageView... params) {

			iv = params[0];
			String imageURL = (String)iv.getTag();

			Drawable nd= loadImageDrawableFromURL(imageURL, "temp");
			return nd;

		}

		protected void onPostExecute(Drawable result) {

			Log.d("bgimg", result.toString());

			iv.setImageDrawable(result);
			super.onPostExecute(result);
		}


		public Drawable loadImageDrawableFromURL(String url, String name) {
			try {
				InputStream is = (InputStream) new URL(url).getContent();
				Drawable d = Drawable.createFromStream(is, name);
				return d;
			} catch (Exception e) {
				return null;
			}
		}

	}








}
