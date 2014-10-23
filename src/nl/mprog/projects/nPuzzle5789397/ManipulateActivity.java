package nl.mprog.projects.nPuzzle5789397;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;

public class ManipulateActivity extends ActionBarActivity implements View.OnClickListener, OnItemSelectedListener, OnSeekBarChangeListener{

	BitmapManipulater fullPicture;
	SeekBar seekBar;
	Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manipulate);
		// get the intent from PuzzleClicked
		Intent intent = getIntent();

		// get the amount of moves and id of the picture that was clicked
		int picture_id = Integer.parseInt(intent.getStringExtra("picture"));

		// get the picture we want to use as a puzzle
		fullPicture = new BitmapManipulater(picture_id, this.getApplicationContext(), this);

		int[] bmp_size = fullPicture.getSize();

		// get the first X and Y coordinates on the screen
		int firstX = fullPicture.getFirstX();
		int firstY = fullPicture.getFirstY();
		int firstYButton = firstY + bmp_size[1];
		int spinner_array = R.array.manipulations;

		// create the buttons, seek bar and spinner
		UICreator creator = new UICreator(this.getApplicationContext(), this);
		spinner = creator.createManipulateSpinner(spinner_array, bmp_size[0]);
		Button button_continue = creator.addButton("Continue", firstX, firstYButton + 100, bmp_size[0] - 30, 120);
		button_continue.setTag(0);
		Button button_undo = creator.addButton("Undo", firstX, 160, bmp_size[0] - 30, 100);
		button_undo.setTag(1);
		Button button_test = creator.addButton("Test", firstX, firstYButton, bmp_size[0] - 30, 100);
		button_test.setTag(2);
		seekBar = creator.addSeekbar(bmp_size[0]);

		// create a bitmap which is an enlarged version of a shrunk version of the original bitmap
		// get it?
		Bitmap bitmapShrunk = fullPicture.shrinkBitmap(fullPicture.getBitmapReady());
		fullPicture.setBitmapReady(bitmapShrunk);
		Bitmap bitmapBlow = fullPicture.blowUpBitmap(fullPicture.getBitmapReady());
		fullPicture.setBitmapReady(bitmapBlow);

		// place bitmap and buttons, etc.
		fullPicture.placeBitmap(fullPicture.getBitmapReady());
		fullPicture.fixOriginalManipulateSmall();
		fullPicture.fixCurrentManipulateSmall();
		button_continue.setOnClickListener(this);
		button_undo.setOnClickListener(this);
		button_test.setOnClickListener(this);
		spinner.setOnItemSelectedListener(this);
		seekBar.setOnSeekBarChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manipulate, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				fullPicture.clearData();
				Intent back = new Intent(this, MainActivity.class);
				startActivity(back);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) 
	{
		int clicked = (Integer) v.getTag();

		if (clicked == 0)
		{
			// save the picture in the folder /nPuzzlePictures
			SavePictures save = new SavePictures();
			String fileName = save.createNewDateName();
			save.createFolderAndSave(fullPicture.getFullBitmapManipulated(), fileName);
			String uri = save.getUri();
			fullPicture.setUri(uri);
			Intent intent= new Intent(this, PuzzleGameActivity.class);
			
			// put all the picture data in the intent
			intent.putExtra("picture", fullPicture);
			intent.putExtra("difficulty", "4");
			
			// recycle all the pictures, to save memory
			fullPicture.clearData();
			
			// go to the puzzle
			startActivity(intent);
			finish();
		}
		// respond to undo button
		else if (clicked == 1)
		{
			fullPicture.undo();
		}
		// see the picture in its full resolution
		else
		{
			Bitmap test_picture = fullPicture.getFullBitmapManipulated();
			fullPicture.placeBitmap(test_picture);
		}
	}

	// fix the manipulations when a new manipulation is selected
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
	{
		fullPicture.fixCurrentManipulateSmall();
		fullPicture.increaseCurrentValue();
		fullPicture.setSelectedManipulation(pos);
		seekBar.setProgress(50);
	}

	// methods that have to be included
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {}

	// manipulate the picture, but not after recycling the previous ones
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if(fullPicture.getBitmapReady() != null)
		{
			fullPicture.getBitmapReady().recycle();
		}
		if(fullPicture.getBitmapSmall() != null)
		{
			fullPicture.getBitmapSmall().recycle();
		}
		int pos = spinner.getSelectedItemPosition();
		fullPicture.setSelectedManipulation(pos);
		int value = seekBar.getProgress();
		ImageView existing_view = (ImageView) findViewById(1);
		existing_view.setImageResource(0);
		
		// iterate through the pixels to manipulate it bitwise
		if (value > 0)
		{
			fullPicture.iteratePixels(value, true, false);
		}
	}
}
