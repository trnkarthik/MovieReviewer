package com.trnkarthik.rottentomatoes;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static DataManager dm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//settingup the database
        dm = new DataManager(this);
	
		
		getOverflowMenu();

		ListView myListView = (ListView) findViewById(R.id.mylist);
		//String of values to be inflated to listview
		final String[] values = new String[] 
				{
				"My Favorite Movies", 
				"Box Office Movies", 
				"In Theaters Movies", 
				"Opening Movies", 
				"Upcoming Movies", 
				};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, android.R.id.text1, values);
		myListView.setAdapter(adapter);   

		myListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				//create a new string.Use this string to get the item clicked
				String getClickedString = (String) ((TextView) view).getText();
				//create a new int.Use this value to pass the clicked item to movies activity
				int valueToPass = 999;   //initializing variable
				for(int i = 0;i<values.length;i++)
				{
					if(getClickedString.equals(values[i]))
					{
						valueToPass = i;
					}
				}
				//	Toast.makeText(getApplicationContext(),valueToPass +"" , Toast.LENGTH_LONG).show();

				//passing an intent

				Intent mainToMovies = new Intent(MainActivity.this, MoviesActivity.class);
				mainToMovies.putExtra("clickeditemid", valueToPass);
				startActivity(mainToMovies);

			}
		});


	}



	private void getOverflowMenu() {

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if(menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		int itemId = item.getItemId();

		switch (itemId) {
		case R.id.exit_button:
			finish();
			break;
		default:
			Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
			break;

		}

		return super.onMenuItemSelected(featureId, item);
	}
	

	@Override
	protected void onDestroy() {
		dm.close();
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		return true;
	}
}


