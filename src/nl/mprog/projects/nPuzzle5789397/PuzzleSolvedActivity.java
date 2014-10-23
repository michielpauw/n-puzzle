package nl.mprog.projects.nPuzzle5789397;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PuzzleSolvedActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle_solved);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		// get the intent from PuzzleGameActivity
		Intent intent = getIntent();

		// create a BitmapPlacer object
		BitmapPlacer fullPicture = (BitmapPlacer) intent.getParcelableExtra("picture");
		String uri = fullPicture.getUri();
		String moves = intent.getStringExtra("moves");

		// get the picture we want to use as a puzzle
		int width = BitmapPlacer.getDisplayWidth(this.getApplicationContext());
		Bitmap puzzle;
		try
		{
			puzzle = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse("file://" + uri));
			Toast.makeText(getApplicationContext(), "file://" + uri, Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e)
		{
			puzzle = BitmapLoader.loadBitmap(R.drawable.ajax, width, width, this);
			e.printStackTrace();
		} catch (IOException e)
		{
			puzzle = BitmapLoader.loadBitmap(R.drawable.ajax, width, width, this);
			e.printStackTrace();
		}
		fullPicture.setActivity(this);
		fullPicture.placeBitmap(puzzle);

		// create a nice text view with the amount of moves
		TextView textView = new TextView(PuzzleSolvedActivity.this);
		textView.setText("Congratulations! You have solved the puzzle in " + moves + " moves!");
		textView.setGravity(Gravity.CENTER);
		textView.setTextSize(20);
		textView.setTypeface(null, Typeface.BOLD);

		// get the display height and width
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		RelativeLayout root = (RelativeLayout)this.findViewById(R.id.root_layout);
		RelativeLayout.LayoutParams paramsText = new RelativeLayout.LayoutParams(fullPicture.getSize()[0] - 20, 120);
		paramsText.topMargin = fullPicture.getFirstY() / 3;
		paramsText.leftMargin = fullPicture.getFirstX();

		// add the textview
		root.addView(textView, paramsText);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.puzzle_solved, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				Intent back = new Intent(this, MainActivity.class);
				startActivity(back);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
