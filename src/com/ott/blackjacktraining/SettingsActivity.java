package com.ott.blackjacktraining;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;

public class SettingsActivity extends Activity
{
	private SharedPreferences settings;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		settings = getSharedPreferences(MainActivity.PREF_FILE,0);
		((Switch)findViewById(R.id.scoreCheck)).setChecked(settings.getBoolean(MainActivity.SCORE, true));
		((Switch)findViewById(R.id.deckCheck)).setChecked(settings.getBoolean(MainActivity.DECK, true));
		((Switch)findViewById(R.id.countCheck)).setChecked(settings.getBoolean(MainActivity.COUNT, true));
		((Switch)findViewById(R.id.accCheck)).setChecked(settings.getBoolean(MainActivity.ACCURACY, true));
		((Switch)findViewById(R.id.winCheck)).setChecked(settings.getBoolean(MainActivity.WIN, true));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.returnToHome)
		{
			SharedPreferences.Editor edit = settings.edit();
			edit.putBoolean(MainActivity.SCORE, ((Switch)findViewById(R.id.scoreCheck)).isChecked());
			edit.putBoolean(MainActivity.DECK, ((Switch)findViewById(R.id.deckCheck)).isChecked());
			edit.putBoolean(MainActivity.COUNT, ((Switch)findViewById(R.id.countCheck)).isChecked());
			edit.putBoolean(MainActivity.ACCURACY, ((Switch)findViewById(R.id.accCheck)).isChecked());
			edit.putBoolean(MainActivity.WIN, ((Switch)findViewById(R.id.winCheck)).isChecked());
			edit.commit();
			finish();
		}
		return true;
	}
}
