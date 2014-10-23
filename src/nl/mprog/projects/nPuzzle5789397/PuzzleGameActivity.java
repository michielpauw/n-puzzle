package nl.mprog.projects.nPuzzle5789397;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class PuzzleGameActivity extends ActionBarActivity implements OnClickListener{
	private int picture;
	private int tiles;
	private boolean start = false;
	private int[] order = new int[tiles * tiles];
	private int moves;
	private BitmapPlacer fullPicture;
	private RelativeLayout layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle_game);

		// the action bar is available from API 11 onwards
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		// get the intent from ManipulateActivity
		Intent intent = getIntent();

		// get the id of the picture and other relevant information from ManipulateActivity
		tiles = Integer.parseInt(intent.getStringExtra("difficulty"));
		fullPicture = (BitmapPlacer) intent.getParcelableExtra("picture");
		String uri = fullPicture.getUri();

		layout = (RelativeLayout)this.findViewById(R.id.root_layout);

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
		// get a picture of a black square which we use as the empty tile
		Bitmap blackbox = BitmapFactory.decodeResource(this.getResources(), R.drawable.blackbox);

		// create a new PuzzlePlacer object to help us chop up the picture and place the tiles
		final PuzzlePlacer chopUp = new PuzzlePlacer(tiles, blackbox, this, this.getApplicationContext(), puzzle);
		final Gameplay curGame = new Gameplay(tiles);

		// create a (random) array
		curGame.createRandomArray(false);
		order = curGame.getOrder();

		// place pictures in the right order
		chopUp.placePictures(curGame, order, start);
		
		// wait for three seconds
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// and place the pictures in a random order
				curGame.createRandomArray(true);
				moves = 0;
				order = curGame.getOrder();
				start = true;
				layout.removeAllViews();
				chopUp.placePictures(curGame, order, start);
			}
		}, 3000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.puzzle_game, menu);
		return true;
	}

	// handle the clicks on every tile
	public void onClick(View v) {
		// get the tags I sent with the click
		int pos_clicked = (Integer) v.getTag(R.string.pos_clicked);
		Gameplay curGame = (Gameplay) v.getTag(R.string.curGame);

		boolean solved = curGame.handleClick(pos_clicked, this);

		// if the puzzle is solved: hurray!
		if (solved)
		{
			Intent intent= new Intent(this, PuzzleSolvedActivity.class);
			moves = curGame.getMoves();
			String moves_str = Integer.toString(moves);
			String picture_name = Integer.toString(picture);
			intent.putExtra("picture_name", picture_name);
			intent.putExtra("moves", moves_str);
			intent.putExtra("picture", fullPicture);
			layout.removeAllViews();
			fullPicture.getPicture().recycle();
			startActivity(intent);
			finish(); 
		}
	}

	// write the order of the tiles and the picture chosen to memory
	@Override
	public void onPause() {
		super.onPause();
		SharedPreferences sharedPref = PuzzleGameActivity.this.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("moves", moves);
		editor.putInt("picture", picture);
		int n = tiles * tiles;
		for (int i = 0; i < n; i++)
		{
			editor.putInt(Integer.toString(i), order[i]);
		}
		editor.commit();
	}

	// retrieve the order of the tiles and the picture chosen from memory
	protected void onResume()
	{
		super.onResume();

		SharedPreferences sharedPref = PuzzleGameActivity.this.getPreferences(Context.MODE_PRIVATE);
		picture = sharedPref.getInt("picture", R.drawable.ajax);
		moves = sharedPref.getInt("moves", 0);
		int n = tiles * tiles;
		for (int i = 0; i < n; i++)
		{
			order[i] = sharedPref.getInt(Integer.toString(i), i);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				Intent back = new Intent(this, MainActivity.class);
				startActivity(back);
				layout.removeAllViews();
				fullPicture.getPicture().recycle();
				return true;
				// create the possibility to switch difficulty level
			case R.id.super_easy:
				Intent intent_super_easy = new Intent(this, PuzzleGameActivity.class);
				intent_super_easy.putExtra("difficulty", "2");
				intent_super_easy.putExtra("picture", fullPicture);
				startActivity(intent_super_easy);
				return true;
			case R.id.easy:
				Intent intent_easy = new Intent(this, PuzzleGameActivity.class);
				intent_easy.putExtra("difficulty", "3");
				intent_easy.putExtra("picture", fullPicture);
				startActivity(intent_easy);
				return true;
			case R.id.medium:
				Intent intent_med = new Intent(this, PuzzleGameActivity.class);
				intent_med.putExtra("difficulty", "4");
				intent_med.putExtra("picture", fullPicture);
				startActivity(intent_med);
				return true;
			case R.id.hard:
				Intent intent_hard = new Intent(this, PuzzleGameActivity.class);
				intent_hard.putExtra("difficulty", "5");
				intent_hard.putExtra("picture", fullPicture);
				startActivity(intent_hard);
				return true;
			case R.id.super_hard:
				Intent intent_super_hard = new Intent(this, PuzzleGameActivity.class);
				intent_super_hard.putExtra("difficulty", "6");
				intent_super_hard.putExtra("picture", fullPicture);
				startActivity(intent_super_hard);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
