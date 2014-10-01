package nl.mprog.projects.nPuzzle5789397;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PuzzleSolvedActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle_solved);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		// get the intent from PuzzleClicked
		Intent intent = getIntent();

		// get the amount of moves and id of the picture that was clicked
		String moves = intent.getStringExtra("moves");
		int picture = Integer.parseInt(intent.getStringExtra("picture_name"));
		TextView textView = new TextView(PuzzleSolvedActivity.this);
		textView.setText("Congratulations! You have solved the puzzle in " + moves + " moves!");
		textView.setGravity(Gravity.CENTER);
		textView.setTextSize(20);
		textView.setTypeface(null, Typeface.BOLD);

		// get the display height and width
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int height_scr = metrics.heightPixels;
		int width_scr = metrics.widthPixels;

		// get the picture we want to use as a puzzle
		Bitmap puzzle = BitmapFactory.decodeResource(this.getResources(), picture);

		// create a new BitmapHandle object to
		BitmapHandler fullPicture = new BitmapHandler(width_scr, height_scr, puzzle);

		// get the size of the resized picture
		int[] bmp_size = fullPicture.getSize();

		// get the first X and Y coordinates on the screen
		int firstX = (width_scr - bmp_size[0]) / 2;
		int firstY = fullPicture.getFirstY();

		// create a scaled bitmap to use for the picture
		Bitmap picture_res = Bitmap.createScaledBitmap(puzzle, bmp_size[0], bmp_size[1], false);
		ImageView imageView = new ImageView(this);

		imageView.setImageBitmap(picture_res);

		// create n relative layouts
		RelativeLayout root = (RelativeLayout)this.findViewById(R.id.root_layout);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(bmp_size[0], bmp_size[1]);
		RelativeLayout.LayoutParams paramsText = new RelativeLayout.LayoutParams(bmp_size[0] - 20, 120);
		params.leftMargin = firstX;
		params.topMargin = firstY;
		paramsText.topMargin = firstY / 3;
		paramsText.leftMargin = firstX;

		// add the textview
		root.addView(textView, paramsText);

		// add the picture
		root.addView(imageView, params);

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
