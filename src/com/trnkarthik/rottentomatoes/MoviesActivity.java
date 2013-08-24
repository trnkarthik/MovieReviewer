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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MoviesActivity extends Activity {


	private static DataManager dm;

	static int valueToUse;


	public Handler handler;
	static HttpClient client = new DefaultHttpClient();

	ArrayList<Movies> moviesArrayList= new ArrayList<Movies>();

	static String newimageURL;

	//List of URLs to retreive the JSON files
	String URLForBoxOfficeMovies = 
			"api.rottentomatoes.com";

	Movies[] valuesarray;
	Movies[] nvaluesarray;
	Movies[] allvaluesarray;
	Movies[] rvaluesarray;
	Movies[] pgvaluesarray;
	Movies[] pg13valuesarray;
	Movies[] unratedvaluesarray;

	int rsize=0,pgsize=0,pg13size=0,unratedsize=0;

	MoviesAdapter adapter;

	ListView myListView ;

	
	@Override
	protected void onResume() {

		if(valueToUse==0)
		{
			getNotesAndUpdateValuesArray(); 
			UpdateAllArraysWithGivenValuesArray(valuesarray);

			adapter= new MoviesAdapter(getApplicationContext(), valuesarray);
			myListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		//Toast.makeText(getApplicationContext(), "resumed", Toast.LENGTH_LONG).show();
		super.onResume();
	}


	@Override
	protected void onRestart() {

		if(valueToUse==0)
		{
			getNotesAndUpdateValuesArray(); 
			UpdateAllArraysWithGivenValuesArray(valuesarray);

			adapter= new MoviesAdapter(getApplicationContext(), valuesarray);
			myListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}

		super.onRestart();
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movies);

		//Getting data from intent
		Bundle extras = getIntent().getExtras();
		valueToUse = extras.getInt("clickeditemid");
		//Toast.makeText(getApplicationContext(),valueToUse +" in movies" , Toast.LENGTH_LONG).show();

		//settingup the database
		dm = new DataManager(this);


		if(valueToUse!=0)
		{
			//Loading selected Movie option on start of an Activity
			new JSONBackgroundStuff().execute(valueToUse+"");
		}
		else if (valueToUse == 0)
		{
			//retrive data from database
			//Toast.makeText(getApplicationContext(),"db stuff " , Toast.LENGTH_LONG).show();

			getNotesAndUpdateValuesArray();


			myListView = (ListView) findViewById(R.id.mymovielist);
			Context con = getApplicationContext();
			adapter = new MoviesAdapter(con, valuesarray);
			myListView.setAdapter(adapter);
			//adapter.setNotifyOnChange(true);


			//onClickListner for list.Here we get the id of the movie clicked
			myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					//Toast.makeText(getApplicationContext(),view.getTag() +"" , Toast.LENGTH_LONG).show();

					Intent fromMoviesToMovieDesc = new Intent(MoviesActivity.this, MovieDescActivity.class);
					fromMoviesToMovieDesc.putExtra("id", view.getTag()+"");
					startActivity(fromMoviesToMovieDesc);

				}
			});


			UpdateAllArraysWithGivenValuesArray(valuesarray);





			myListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			myListView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

				int listitemposition;

				public void onItemCheckedStateChanged(ActionMode mode, int position,
						long id, boolean checked) {
					// Here you can do something when items are selected/de-selected,
					// such as update the title in the CAB
					//	Toast.makeText(getApplicationContext(), position+"", Toast.LENGTH_LONG).show();

					listitemposition=position;
				}

				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					// Respond to clicks on the actions in the CAB


					int id = item.getItemId();

					Log.d("rating", valuesarray.length + "   before");
					Log.d("rating", " hi  before");

					switch (id) {
					case R.id.rating_all:
						//updating valuesarray
						valuesarray = new Movies[allvaluesarray.length];
						for(int i=0;i<allvaluesarray.length;i++){
							valuesarray[i] = allvaluesarray[i];
						}				
						Log.d("rating", valuesarray.length + "   all");
						Toast.makeText(getApplicationContext(), valuesarray.length + "   all" , Toast.LENGTH_LONG).show();
						break;

					case R.id.rating_r:
						//updating valuesarray
						valuesarray = new Movies[rvaluesarray.length];
						for(int i=0;i<rvaluesarray.length;i++){
							valuesarray[i] = rvaluesarray[i];
						}				
						Log.d("rating", valuesarray.length + "   r");
						//Toast.makeText(getApplicationContext(), valuesarray.length + "   r" , Toast.LENGTH_LONG).show();
						break;

					case R.id.rating_pg:
						//updating valuesarray
						valuesarray = new Movies[pgvaluesarray.length];
						for(int i=0;i<pgvaluesarray.length;i++){
							valuesarray[i] = pgvaluesarray[i];
						}				
						Log.d("rating", valuesarray.length + "   pg");
						//Toast.makeText(getApplicationContext(), valuesarray.length + "   pg" , Toast.LENGTH_LONG).show();
						break;

					case R.id.rating_pg13:
						//updating valuesarray
						valuesarray = new Movies[pg13valuesarray.length];
						for(int i=0;i<pg13valuesarray.length;i++){
							valuesarray[i] = pg13valuesarray[i];
						}				
						Log.d("rating", valuesarray.length + "   pg13");
						//Toast.makeText(getApplicationContext(), valuesarray.length + "   pg13" , Toast.LENGTH_LONG).show();
						break;	

					case R.id.rating_unrated:
						//updating valuesarray
						valuesarray = new Movies[unratedvaluesarray.length];
						for(int i=0;i<unratedvaluesarray.length;i++){
							valuesarray[i] = unratedvaluesarray[i];
						}				
						Log.d("rating", valuesarray.length + "   unrated");
						break;	

					case R.id.remove_text:
						dm.deleteNote(getNewMovieFromMovie(valuesarray[listitemposition]));
						getNotesAndUpdateValuesArray(); 
						//Toast.makeText(getApplicationContext(), item.getItemId()+"", Toast.LENGTH_LONG).show();
						break;	

					default:
						Log.d("rating", valuesarray.length + "   default");
						//	Toast.makeText(getApplicationContext(), valuesarray.length + "   after" , Toast.LENGTH_LONG).show();
						break;
					}


					adapter= new MoviesAdapter(getApplicationContext(), valuesarray);
					myListView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					mode.finish();
					return true;

				}

				public NewMovie getNewMovieFromMovie(Movies movies) {
					NewMovie temp = new NewMovie(movies);
					return temp;
				}

				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					// Inflate the menu for the CAB
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.activity_movies_long, menu);
					return true;
				}

				public void onDestroyActionMode(ActionMode mode) {
					// Here you can make any necessary updates to the activity when
					// the CAB is removed. By default, selected items are deselected/unchecked.

				}

				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					// Here you can perform updates to the CAB due to
					// an invalidate() request
					return false;
				}
			});







		}

	}


	private void getNotesAndUpdateValuesArray() {

		List<NewMovie> notes = dm.getAllNotes();
		int size=notes.size();
		//Toast.makeText(getApplicationContext(),"db stuff " + size , Toast.LENGTH_LONG).show();

		valuesarray = new Movies[size];
		for(int i=0;i<size;i++){
			Movies temp = new Movies(notes.get(i));	
			valuesarray[i] = temp;
		}			
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_movies, menu);
		return true;
	}



	public void afterloadingdone() {

		//as this method is called after movies are loaded,everything should be done in this

		myListView = (ListView) findViewById(R.id.mymovielist);


		ArrayList<Movies> values = new ArrayList<Movies>();
		values.addAll(moviesArrayList);

		//Toast.makeText(getApplicationContext(),values.size() +"" , Toast.LENGTH_LONG).show();

		valuesarray = new Movies[moviesArrayList.size()];

		for(int i=0;i<moviesArrayList.size();i++){
			valuesarray[i] = moviesArrayList.get(i);
		}

		UpdateAllArraysWithGivenValuesArray(valuesarray);

		//Toast.makeText(getApplicationContext(),valuesarray[0] +"" , Toast.LENGTH_LONG).show();

		final Context con = getApplicationContext();

		//Toast.makeText(getApplicationContext(),con +"" , Toast.LENGTH_LONG).show();

		adapter = new MoviesAdapter(con, valuesarray);
		myListView.setAdapter(adapter);
		adapter.setNotifyOnChange(true);

		//onClickListner for list.Here we get the id of the movie clicked
		myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				//Toast.makeText(getApplicationContext(),view.getTag() +"" , Toast.LENGTH_LONG).show();

				Intent fromMoviesToMovieDesc = new Intent(MoviesActivity.this, MovieDescActivity.class);
				fromMoviesToMovieDesc.putExtra("id", view.getTag()+"");
				startActivity(fromMoviesToMovieDesc);

			}
		});

		//my longclick

		myListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		myListView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

			public void onItemCheckedStateChanged(ActionMode mode, int position,
					long id, boolean checked) {
				// Here you can do something when items are selected/de-selected,
				// such as update the title in the CAB

			}

			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				// Respond to clicks on the actions in the CAB


				int id = item.getItemId();

				Log.d("rating", valuesarray.length + "   before");
				Log.d("rating", " hi  before");

				switch (id) {
				case R.id.rating_all:
					//updating valuesarray
					valuesarray = new Movies[allvaluesarray.length];
					for(int i=0;i<allvaluesarray.length;i++){
						valuesarray[i] = allvaluesarray[i];
					}				
					Log.d("rating", valuesarray.length + "   all");
					Toast.makeText(getApplicationContext(), valuesarray.length + "   all" , Toast.LENGTH_LONG).show();
					break;

				case R.id.rating_r:
					//updating valuesarray
					valuesarray = new Movies[rvaluesarray.length];
					for(int i=0;i<rvaluesarray.length;i++){
						valuesarray[i] = rvaluesarray[i];
					}				
					Log.d("rating", valuesarray.length + "   r");
					Toast.makeText(getApplicationContext(), valuesarray.length + "   r" , Toast.LENGTH_LONG).show();
					break;

				case R.id.rating_pg:
					//updating valuesarray
					valuesarray = new Movies[pgvaluesarray.length];
					for(int i=0;i<pgvaluesarray.length;i++){
						valuesarray[i] = pgvaluesarray[i];
					}				
					Log.d("rating", valuesarray.length + "   pg");
					Toast.makeText(getApplicationContext(), valuesarray.length + "   pg" , Toast.LENGTH_LONG).show();
					break;

				case R.id.rating_pg13:
					//updating valuesarray
					valuesarray = new Movies[pg13valuesarray.length];
					for(int i=0;i<pg13valuesarray.length;i++){
						valuesarray[i] = pg13valuesarray[i];
					}				
					Log.d("rating", valuesarray.length + "   pg13");
					Toast.makeText(getApplicationContext(), valuesarray.length + "   pg13" , Toast.LENGTH_LONG).show();
					break;	

				case R.id.rating_unrated:
					//updating valuesarray
					valuesarray = new Movies[unratedvaluesarray.length];
					for(int i=0;i<unratedvaluesarray.length;i++){
						valuesarray[i] = unratedvaluesarray[i];
					}				
					Log.d("rating", valuesarray.length + "   unrated");
					break;	

				default:
					Log.d("rating", valuesarray.length + "   default");
					Toast.makeText(getApplicationContext(), valuesarray.length + "   after" , Toast.LENGTH_LONG).show();
					break;
				}


				adapter= new MoviesAdapter(con, valuesarray);
				myListView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				mode.finish();
				return true;

			}

			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				// Inflate the menu for the CAB
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.activity_movies_long, menu);
				return true;
			}

			public void onDestroyActionMode(ActionMode mode) {
				// Here you can make any necessary updates to the activity when
				// the CAB is removed. By default, selected items are deselected/unchecked.

			}

			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// Here you can perform updates to the CAB due to
				// an invalidate() request
				return false;
			}
		});


	}


	public void UpdateAllArraysWithGivenValuesArray(Movies[] valuesarray) {


		allvaluesarray = new Movies[valuesarray.length];

		for(int i=0;i<valuesarray.length;i++){
			allvaluesarray[i] = valuesarray[i];
		}

		for(int i=0;i<valuesarray.length;i++){
			if(valuesarray[i].mpaa_rating.equals("R"))
			{
				rsize++;
			}
			if(valuesarray[i].mpaa_rating.equals("PG"))
			{
				pgsize++;
			}
			if(valuesarray[i].mpaa_rating.equals("PG-13"))
			{
				pg13size++;
			}
			if(valuesarray[i].mpaa_rating.equals("Unrated"))
			{
				unratedsize++;
			}
		}

		rvaluesarray = new Movies[rsize];
		pgvaluesarray = new Movies[pgsize];
		pg13valuesarray = new Movies[pg13size];
		unratedvaluesarray = new Movies[unratedsize];

		int temp1=0;

		for(int i=0;i<valuesarray.length;i++){
			if(valuesarray[i].mpaa_rating.equals("R"))
			{
				rvaluesarray[temp1] = valuesarray[i];
				temp1++;
			}
		}
		int temp2=0;
		for(int i=0;i<valuesarray.length;i++){
			if(valuesarray[i].mpaa_rating.equals("PG"))
			{
				pgvaluesarray[temp2] = valuesarray[i];
				temp2++;
			}
		}
		int temp3=0;
		for(int i=0;i<valuesarray.length;i++){
			if(valuesarray[i].mpaa_rating.equals("PG-13"))
			{
				pg13valuesarray[temp3] = valuesarray[i];
				temp3++;
			}
		}
		int temp4=0;
		for(int i=0;i<valuesarray.length;i++){
			if(valuesarray[i].mpaa_rating.equals("Unrated"))
			{
				unratedvaluesarray[temp4] = valuesarray[i];
				temp4++;
			}
		}




	}



	@Override
	protected void onDestroy() {
		dm.close();
		super.onDestroy();
	}

	//Json Background AsyncTask call 


	private class JSONBackgroundStuff extends AsyncTask< String ,Boolean , StringBuffer> {


		public  StringBuffer sb = new StringBuffer("");
		String apiCalled;

		public ProgressDialog dialog;

		public void onPreExecute() {
			//setup the ProgressDialog on the MainUI here
			dialog =ProgressDialog.show(MoviesActivity.this,
					null, "Loading Movies");
		}

		@Override
		public StringBuffer doInBackground(String... params) {

			apiCalled = params[0];

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
			dialog.setMessage("Finished Loading Polls! ");

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
			public String api_key = "your api key"; //replace this with your api key
			BufferedReader  in  = null;
			List<NameValuePair>  params  = new ArrayList<NameValuePair>();

			URI  uri = null ;

			//differentiating different APIs is done here

			if(apiCalled.equals("1"))
			{

				params.add(new BasicNameValuePair("limit", "50"));
				params.add(new BasicNameValuePair("country", "us"));
				params.add(new BasicNameValuePair("apikey", api_key));

				uri  =  URIUtils.createURI("http", URLForBoxOfficeMovies, -1,"/api/public/v1.0/lists/movies/box_office.json" , 
						URLEncodedUtils.format(params, "UTF-8"), null);
			}

			else if(apiCalled.equals("2"))
			{

				params.add(new BasicNameValuePair("page_limit", "50"));
				params.add(new BasicNameValuePair("page", "1"));
				params.add(new BasicNameValuePair("country", "us"));
				params.add(new BasicNameValuePair("apikey", api_key));

				uri  =  URIUtils.createURI("http", URLForBoxOfficeMovies, -1,"/api/public/v1.0/lists/movies/in_theaters.json" , 
						URLEncodedUtils.format(params, "UTF-8"), null);
			}

			else if(apiCalled.equals("3"))
			{

				params.add(new BasicNameValuePair("limit", "50"));
				params.add(new BasicNameValuePair("country", "us"));
				params.add(new BasicNameValuePair("apikey", api_key));

				uri  =  URIUtils.createURI("http", URLForBoxOfficeMovies, -1,"/api/public/v1.0/lists/movies/opening.json" , 
						URLEncodedUtils.format(params, "UTF-8"), null);
			}

			else if(apiCalled.equals("4"))
			{

				params.add(new BasicNameValuePair("page_limit", "50"));
				params.add(new BasicNameValuePair("page", "1"));
				params.add(new BasicNameValuePair("country", "us"));
				params.add(new BasicNameValuePair("apikey", api_key));

				uri  =  URIUtils.createURI("http", URLForBoxOfficeMovies, -1,"/api/public/v1.0/lists/movies/upcoming.json" , 
						URLEncodedUtils.format(params, "UTF-8"), null);
			}

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

			Log.d("BBB", sb.toString());
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

			JSONObject initobj = new JSONObject(strbuilder.toString());
			JSONArray movies =  initobj.getJSONArray("movies");   

			for(int i=0; i<movies.length(); i++){
				JSONObject movieJSON = movies.getJSONObject(i);    
				Movies p = new Movies(movieJSON);
				moviesArrayList.add(p);
				Log.d("demo1", p.toString());
			}

		}     
		// end of ParseJSONData
	}
	//end of AsyncTask

	//MovieAdapter comes here
	public static class MoviesAdapter extends ArrayAdapter<Movies>{

		Context context;

		Movies[] objects;

		public MoviesAdapter(Context context, Movies[] objects) {
			super(context, R.layout.item_row_layout, objects);
			this.context = context;
			this.objects = objects;

			//Toast.makeText(context, this.objects[objects.length-1].title , Toast.LENGTH_LONG).show();

		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) { 
				LayoutInflater  inflater  =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView  =  inflater.inflate(R.layout.item_row_layout, parent, false);
			}


			TextView  movieTitle  =  (TextView)convertView.findViewById(R.id.movieTitle); 
			TextView  year  =  (TextView)convertView.findViewById(R.id.year); 		 	
			TextView  mpaaRating  =  (TextView)convertView.findViewById(R.id.mpaa_rating); 		 	
			ImageView criticRating = (ImageView)convertView.findViewById(R.id.criticRatingimg);
			ImageView audienceRating = (ImageView)convertView.findViewById(R.id.audianceRatings);



			ImageView thumbnailImg = (ImageView) convertView.findViewById(R.id.thumbNailItem);
			String imageURL = objects[position].thumbnail;


			Log.d("bgimg2", imageURL);

			movieTitle.setText(objects[position].title+ "");
			year.setText(objects[position].year+"");
			mpaaRating.setText(objects[position].mpaa_rating +"");

			//setting tag to a convertview
			convertView.setTag(objects[position].id);

			//Handling null values
			if(objects[position].critics_rating==null)
			{
				criticRating.setImageResource(R.drawable.notranked);
				//Log.d("null val", objects[position].critics_rating + "    " + objects[position].audience_rating);
			}
			else
			{
				criticRating.setImageResource(getIDFromImageName(objects[position].critics_rating));
			}
			if(objects[position].audience_rating==null)
			{
				audienceRating.setImageResource(R.drawable.notranked);
				//Log.d("null val", objects[position].critics_rating + "    " + objects[position].audience_rating);
			}
			else
			{
				audienceRating.setImageResource(getIDFromImageName(objects[position].audience_rating));
			}


			if(valueToUse==0 )
			{
				Log.d("nullval", imageURL + "     url");
				//database called.Thumbnail will be null.So you should get thumbnail using your id from API
			//	imageURL = getImageURLFromID(objects[position].id);

				Log.d("nurl", imageURL);
				
				//thumbnailImg.setTag(imageURL);
			//	new LoadImageFromURL().execute(thumbnailImg);

			}
			else
			{
				Log.d("url", imageURL);
				thumbnailImg.setTag(imageURL);
				new LoadImageFromURL().execute(thumbnailImg);
			}


			return convertView;	 	 	
		}


		private String getImageURLFromID(String id) {
			new GetURL().execute(id+"");
			Log.d("nurl", newimageURL + "   int");
			return newimageURL;
		}


		public Bitmap getBitmap(String bitmapUrl) {
			try {
				URL url = new URL(bitmapUrl);
				return BitmapFactory.decodeStream(url.openConnection().getInputStream()); 
			}
			catch(Exception ex) {return null;}
		}



		private int getIDFromImageName(String critics_rating) {

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


		

		
		
		
		
		
		
		
		
		
		
		

		private class GetURL extends AsyncTask< String ,Boolean , StringBuffer> {


			public  StringBuffer sb = new StringBuffer("");
			String movieIDCalled;
			String tempurl;
			
			public void onPreExecute() {
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
					tempurl = ParseJSONData(sb);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}

				sb=new StringBuffer(tempurl);

				return sb; 
			}


			//other methods


			@Override
			protected void onPostExecute(StringBuffer result) {

				newimageURL = result.toString();
				super.onPostExecute(result);
			}

			public StringBuffer GetDataFromAPI() throws IOException, URISyntaxException
			{


				BufferedReader  in  = null;
				List<NameValuePair>  params  = new ArrayList<NameValuePair>();

				URI  uri = null ;

				params.add(new BasicNameValuePair("apikey", api_key));

				uri  =  URIUtils.createURI("http", "api.rottentomatoes.com", -1,"/api/public/v1.0/movies/"+movieIDCalled +".json" , 
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

			private String ParseJSONData(StringBuffer sb) throws IOException, JSONException {

				InputStream in =new ByteArrayInputStream(sb.toString().getBytes());

				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				StringBuilder strbuilder = new StringBuilder();
				String line = reader.readLine();
				while(line != null){
					strbuilder.append(line);
					line = reader.readLine();
				}
				reader.close();


				JSONObject movieJSON = new JSONObject(strbuilder.toString());  
				
				JSONObject posters = movieJSON.getJSONObject("posters");
				String thumbnail = posters.getString("thumbnail");

				 return thumbnail;
			}     
			// end of ParseJSONData
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		




	}

}




































