package nl.mprog.projects.nPuzzle5789397;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class PuzzleGameActivity extends ActionBarActivity implements OnClickListener{
	private int picture;
	private int tiles;
	private boolean start = false;
	private int[] order = new int[tiles * tiles];
	private int moves;

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
		BitmapPlacer fullPicture = (BitmapPlacer) intent.getParcelableExtra("picture");
		String uri = fullPicture.getUri();
		
		final RelativeLayout layout = (RelativeLayout)this.findViewById(R.id.root_layout);

		// get the picture we want to use as a puzzle
		Bitmap puzzle = BitmapFactory.decodeResource(this.getResources(), R.drawable.ajax);;
		try
		{
			puzzle = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse("file://" + uri));
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// get a picture of a black square which we use as the empty tile
		Bitmap blackbox = BitmapFactory.decodeResource(this.getResources(), R.drawable.blackbox);
		
		int width_scr = fullPicture.getWidthScr();
		int height_scr = fullPicture.getHeightScr();
		
		// create a new PuzzlePlacer object to help us chop up the picture and place the tiles
		final PuzzlePlacer chopUp = new PuzzlePlacer(puzzle, tiles, blackbox, this, picture, this.getApplicationContext());
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

//	public void placePictures(BitmapHandler chopUp, Gameplay curGame)
//	{
//		// get the first X and Y coordinates on the screen
//		int firstX = chopUp.getFirstX();
//		int firstY = chopUp.getFirstY();
//
//		// get the sizes of each tile
//		int[] tileSizes = chopUp.getTileSize();
//		int tiles = chopUp.getTiles();
//
//		Bitmap puzzle_res = chopUp.getPuzzle();
//		Bitmap blackbox = chopUp.getBlack();
//
//		Bitmap cropped;
//
//		// create n tiles of the picture and one black tile
//		for (int i = 0; i < tiles; i++)
//		{
//			// I define xCoordinate and x_pic here to save memory
//			int xCoordinate = firstX + (tileSizes[0] + 7) * i;
//
//			for (int j = 0; j < tiles; j++)
//			{
//				int tile = order[tiles * i + j];
//				if (tile != 0)
//				{
//					int x_pic = tileSizes[0] * ((tile - 1) / tiles);
//					int y_pic = tileSizes[1] * ((tile - 1) % tiles);
//
//					cropped = Bitmap.createBitmap(puzzle_res, x_pic, y_pic, tileSizes[0], tileSizes[1]);
//				}
//				else
//				{
//					cropped = Bitmap.createBitmap(blackbox, 0, 0, tileSizes[0], tileSizes[1]);
//				}
//
//				// create an imageView for each tile (also the empty one)
//				ImageView imageView = new ImageView(this);
//				imageView.setTag(R.string.pos_clicked, tiles * i + j);
//				imageView.setTag(R.string.clicked, tile);
//				imageView.setTag(R.string.curGame, curGame);
//				imageView.setId(tiles * i + j);
//				imageView.setImageBitmap(cropped);
//
//				// create a relative layout
//				RelativeLayout root = (RelativeLayout)this.findViewById(R.id.root_layout);
//				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tileSizes[0], tileSizes[1]);
//				int yCoordinate = firstY + (tileSizes[1] + 7) * j;
//				params.leftMargin = xCoordinate;
//				params.topMargin = yCoordinate;
//
//				// add the picture
//				root.addView(imageView, params);
//				if (start)
//				{
//					imageView.setOnClickListener(this);
//				}
//			}
//		}
//	}

	// handle the clicks on every tile
	public void onClick(View v) {
		// get the tags I sent with the click
		int pos_clicked = (Integer) v.getTag(R.string.pos_clicked);
		Gameplay curGame = (Gameplay) v.getTag(R.string.curGame);

		// get other relevant information
		int tiles = curGame.getTiles();

		int clicked = order[pos_clicked];
		int pos_zero = -1;
		int n = tiles * tiles;
		for (int i = 0; i < n; i++)
		{
			if (order[i] == 0)
			{
				pos_zero = i;
			}
		}

		// check whether the move is legal
		boolean legal = curGame.legalMove(clicked, pos_clicked, pos_zero);

		// if so, switch the tiles both visually and in the array
		if (legal)
		{
			// keep track of the moves
			moves++;

			ImageView view_click = (ImageView) findViewById(pos_clicked);
			Drawable clicked_on = view_click.getDrawable();

			ImageView view_zero = (ImageView) findViewById(pos_zero);
			Drawable black = view_zero.getDrawable();

			view_zero.setImageDrawable(clicked_on);
			view_click.setImageDrawable(black);

			curGame.switchPictures(clicked, pos_zero, pos_clicked);
			order = curGame.getOrder();
			// if the puzzle is solved: hurray!
			if (curGame.solved())
			{
				Intent intent= new Intent(PuzzleGameActivity.this, PuzzleSolvedActivity.class);
				String moves_str = Integer.toString(moves);
				String picture_name = Integer.toString(picture);
				intent.putExtra("picture_name", picture_name);
				intent.putExtra("moves", moves_str);
				startActivity(intent);
				finish(); 
			}
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
				return true;
				// create the possibility to switch difficulty level
			case R.id.super_easy:
				Intent intent_super_easy = new Intent(this, PuzzleGameActivity.class);
				intent_super_easy.putExtra("difficulty", "2");
				intent_super_easy.putExtra("picture", Integer.toString(picture));
				startActivity(intent_super_easy);
				return true;
			case R.id.easy:
				Intent intent_easy = new Intent(this, PuzzleGameActivity.class);
				intent_easy.putExtra("difficulty", "3");
				intent_easy.putExtra("picture", Integer.toString(picture));
				startActivity(intent_easy);
				return true;
			case R.id.medium:
				Intent intent_med = new Intent(this, PuzzleGameActivity.class);
				intent_med.putExtra("difficulty", "4");
				intent_med.putExtra("picture", Integer.toString(picture));
				startActivity(intent_med);
				return true;
			case R.id.hard:
				Intent intent_hard = new Intent(this, PuzzleGameActivity.class);
				intent_hard.putExtra("difficulty", "5");
				intent_hard.putExtra("picture", Integer.toString(picture));
				startActivity(intent_hard);
				return true;
			case R.id.super_hard:
				Intent intent_super_hard = new Intent(this, PuzzleGameActivity.class);
				intent_super_hard.putExtra("difficulty", "6");
				intent_super_hard.putExtra("picture", Integer.toString(picture));
				startActivity(intent_super_hard);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
