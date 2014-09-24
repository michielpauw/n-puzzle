package nl.mprog.projects.nPuzzle5789397;

import java.util.ArrayList;
import java.util.Collections;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class PuzzleGame extends ActionBarActivity implements OnClickListener{
	private int picture;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle_start);
		// get the intent from PuzzleClicked
		Intent intent = getIntent();
		// get the id of the picture that was clicked
		picture = Integer.parseInt(intent.getStringExtra("picture_name"));
		int tiles = Integer.parseInt(intent.getStringExtra("difficulty"));
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
		// get the size of the resized picture
		int[] bmp_size = chopUp.getSize();
		int[] order = chopUp.getOrder();
		
		chopUp.createRandomArray();
		// create a scaled bitmap to use for the puzzle
		Bitmap puzzle_res = Bitmap.createScaledBitmap(puzzle, bmp_size[0], bmp_size[1], false);
		chopUp.setPuzzle(puzzle_res);

		placePictures(chopUp);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.puzzle_game, menu);
		return true;
	}

	public void placePictures(BitmapHandle chopUp)
	{
		int[] order = chopUp.getOrder();
		// get the first X and Y coordinates on the screen
		int firstX = chopUp.getFirstX();
		int firstY = chopUp.getFirstY();
				
		// get the sizes of each tile
		int[] tileSizes = chopUp.getTileSize();
		int tiles = chopUp.getTiles();
		
		Bitmap puzzle_res = chopUp.getPuzzle();
		Bitmap blackbox = chopUp.getBlack();
		
		Bitmap cropped;
		// create n tiles of the picture and one black tile
		for (int i = 0; i < tiles; i++)
		{
			// I define xCoordinate and x_pic here to save memory
			int xCoordinate = firstX + (tileSizes[0] + 7) * i;
			
			for (int j = 0; j < tiles; j++)
			{
				int tile = order[tiles * i + j];
				if (tile != 0)
				{
					int x_pic = tileSizes[0] * ((tile - 1) / tiles);
					int y_pic = tileSizes[1] * ((tile - 1) % tiles);
			
					cropped = Bitmap.createBitmap(puzzle_res, x_pic, y_pic, tileSizes[0], tileSizes[1]);
				}
				else
				{
					cropped = Bitmap.createBitmap(blackbox, 0, 0, tileSizes[0], tileSizes[1]);
				}
				// create an imageView for each tile (also the empty one)
				ImageView imageView = new ImageView(this);
				imageView.setTag(R.string.pos_clicked, tiles * i + j);
				imageView.setTag(R.string.clicked, tile);
				imageView.setTag(R.string.chopUp, chopUp);
				imageView.setId(tiles * i + j);
				imageView.setImageBitmap(cropped);
				// create n relative layouts
				RelativeLayout root = (RelativeLayout)this.findViewById(R.id.root_layout);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tileSizes[0], tileSizes[1]);
				params.leftMargin = xCoordinate;
				params.topMargin = firstY + (tileSizes[1] + 7) * j;
				// add the picture
				root.addView(imageView, params);
				imageView.setOnClickListener(this);
			}
		}
	}
	
	// handle the clicks on every tile
	public void onClick(View v) {
		// get the tags I sent with the click
        int pos_clicked = (Integer) v.getTag(R.string.pos_clicked);
        BitmapHandle chopUp = (BitmapHandle) v.getTag(R.string.chopUp);
        // get other relevant information
        int tiles = chopUp.getTiles();
        int[] order = chopUp.getOrder();
        int moves = chopUp.getMoves();
        // keep track of the moves
        moves++;
        chopUp.setMoves(moves);
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
        boolean legal = chopUp.legalMove(clicked, pos_clicked, pos_zero);
        // if so, switch the tiles both visually and in the array
        if (legal)
        {
        	ImageView view_click = (ImageView) findViewById(pos_clicked);
        	Drawable clicked_on = view_click.getDrawable();
        	
            ImageView view_zero = (ImageView) findViewById(pos_zero);
            Drawable black = view_zero.getDrawable();
        	
            view_zero.setImageDrawable(clicked_on);
            view_click.setImageDrawable(black);
            
            chopUp.switchPictures(clicked, pos_zero, pos_clicked);
            // if the puzzle is solved: hurray!
            if (chopUp.solved())
            {
            	Intent intent= new Intent(PuzzleGame.this, PuzzleSolved.class);
            	String moves_str = Integer.toString(moves);
				intent.putExtra("picture_name", moves_str);
				startActivity(intent);
				finish(); 
            }
        }
    }
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// create the possibility to switch difficulty level
	    switch (item.getItemId()) {
	    case R.id.easy:
	    	Intent intent_easy = new Intent(this, PuzzleGame.class);
	    	intent_easy.putExtra("difficulty", "3");
	    	intent_easy.putExtra("picture_name", Integer.toString(picture));
	    	startActivity(intent_easy);
	        return true;
	    case R.id.medium:
	    	Intent intent_med = new Intent(this, PuzzleGame.class);
	    	intent_med.putExtra("difficulty", "4");
	    	intent_med.putExtra("picture_name", Integer.toString(picture));
	    	startActivity(intent_med);
	        return true;
	    case R.id.hard:
	    	Intent intent_hard = new Intent(this, PuzzleGame.class);
	    	intent_hard.putExtra("difficulty", "5");
	    	intent_hard.putExtra("picture_name", Integer.toString(picture));
	    	startActivity(intent_hard);
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
}
