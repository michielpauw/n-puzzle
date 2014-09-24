package nl.mprog.projects.nPuzzle5789397;

import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class PuzzleStart extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final int tiles = 4;
		Bitmap cropped;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle_start);
		// get the intent from MainActivity
		Intent intent = getIntent();
		// get the name of the picture that was clicked
		int picture = Integer.parseInt(intent.getStringExtra(MainActivity.EXTRA_MESSAGE));
		final String picture_str = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		// get the display height and width
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int height_scr = metrics.heightPixels;
		int width_scr = metrics.widthPixels;
		
		// get the picture we want to use as a puzzle
		Bitmap puzzle = BitmapFactory.decodeResource(this.getResources(), picture);
		// get a picture of a black square which we use as the empty tile
		Bitmap blackbox = BitmapFactory.decodeResource(this.getResources(), R.drawable.blackbox);
		// create a new BitmapHandle object to help us chop up the picture
		BitmapHandle chopUp = new BitmapHandle(width_scr, height_scr, puzzle, tiles, blackbox);
		
		// get the first X and Y coordinates on the screen
		int firstX = chopUp.getFirstX();
		int firstY = chopUp.getFirstY();
		
		// get the sizes of each tile and the size of the resized picture
		int[] tileSizes = chopUp.getTileSize();
		int[] bmp_size = chopUp.getSize();
		
		// create a scaled bitmap to use for the puzzle
		Bitmap puzzle_res = Bitmap.createScaledBitmap(puzzle, bmp_size[0], bmp_size[1], false);
		
		// create n tiles of the picture and one black tile
		for (int i = 0; i < tiles; i++)
		{
			// x_pic and y_pic are used to determine which part of the picture should be displayed
			int x_pic = tileSizes[0] * i;
			// I define xCoordinate and x_pic here to save memory
			int xCoordinate = firstX + (tileSizes[0] + 7) * i;
			
			for (int j = 0; j < tiles; j++)
			{
				int y_pic = tileSizes[1] * j;
				
				// create a black tile in the bottom right
				if (i == tiles - 1 && j == tiles - 1)
				{
					cropped = Bitmap.createBitmap(blackbox, 0, 0, tileSizes[0], tileSizes[1]);
				}
				else
				{
					cropped = Bitmap.createBitmap(puzzle_res, x_pic, y_pic, tileSizes[0], tileSizes[1]);
				}
				
				// create an imageView for each tile (also the empty one)
				ImageView imageView = new ImageView(this);
				imageView.setImageBitmap(cropped);
				// place it in the right position with a 7dp gap
				RelativeLayout root = (RelativeLayout)this.findViewById(R.id.root_layout);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tileSizes[0], tileSizes[1]);
				params.leftMargin = xCoordinate;
				params.topMargin = firstY + (tileSizes[1] + 7) * j;
				// add the picture
				root.addView(imageView, params);
			}
		}
		
		// wait 3 seconds before going to the next activity
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent= new Intent(PuzzleStart.this, PuzzleGame.class);
				intent.putExtra("picture_name", picture_str);
				intent.putExtra("difficulty", Integer.toString(tiles));
				startActivity(intent);
				finish(); 
			}
		}, 3000);
		// the recycling of the bitmap continued to give me errors.
		// I will sort this out before the alpha release.
		// puzzle.recycle();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
