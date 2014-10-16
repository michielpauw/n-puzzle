package nl.mprog.projects.nPuzzle5789397;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

public class ManipulateActivity extends ActionBarActivity implements View.OnClickListener, OnItemSelectedListener, OnSeekBarChangeListener{

	BitmapManipulater fullPicture;
	SeekBar seekBar;
	Spinner spinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manipulate);
		// get the intent from PuzzleClicked
		Intent intent = getIntent();

		// get the amount of moves and id of the picture that was clicked
		int picture_id = Integer.parseInt(intent.getStringExtra("picture"));
		// get the picture we want to use as a puzzle
		Bitmap picture = BitmapFactory.decodeResource(this.getResources(), picture_id);

		fullPicture = new BitmapManipulater(picture, picture_id, this.getApplicationContext(), this);
		
		int[] bmp_size = fullPicture.getSize();

		// get the first X and Y coordinates on the screen
		int firstX = fullPicture.getFirstX();
		int firstY = fullPicture.getFirstY();
		int firstYButton = firstY + bmp_size[1];
		int spinner_array = R.array.manipulations;

		

		UICreator creator = new UICreator(this.getApplicationContext(), this);
		spinner = creator.createManipulateSpinner(spinner_array, bmp_size[0]);
		Button button_continue = creator.addButton("Continue", firstX, firstYButton + 100, bmp_size[0] - 30, 120);
		button_continue.setTag(0);
		Button button_undo = creator.addButton("Undo", firstX, 160, bmp_size[0] - 30, 100);
		button_undo.setTag(1);
		Button button_test = creator.addButton("Test", firstX, firstYButton, bmp_size[0] - 30, 100);
		button_test.setTag(2);
		seekBar = creator.addSeekbar(bmp_size[0]);

		
		Bitmap bitmapShrunk = fullPicture.shrinkBitmap(fullPicture.getBitmapReady());
		fullPicture.setBitmapReady(bitmapShrunk);
		Bitmap bitmapBlow = fullPicture.blowUpBitmap(fullPicture.getBitmapReady());
		fullPicture.setBitmapReady(bitmapBlow);

		fullPicture.placeBitmap(fullPicture.getBitmapReady());
		fullPicture.fixOriginalManipulateSmall();
		fullPicture.fixCurrentManipulateSmall();
		picture.recycle();
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
				if(fullPicture.getBitmapOriginal() != null)
				{
					fullPicture.getBitmapOriginal().recycle();
				}
				if(fullPicture.getBitmapReady() != null)
				{
					fullPicture.getBitmapReady().recycle();
				}
				if(fullPicture.getBitmapSmall() != null)
				{
					fullPicture.getBitmapSmall().recycle();
				}
				fullPicture.getPicture().recycle();
				fullPicture.getResizedPicture().recycle();
				Intent back = new Intent(this, MainActivity.class);
				startActivity(back);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		int clicked = (Integer) v.getTag();
		
		if (clicked == 0)
		{
			SavePictures save = new SavePictures();
			String fileName = save.createNewDateName();
			save.createFolderAndSave(fullPicture.getFullBitmapManipulated(), fileName);
			String uri = save.getUri();
			fullPicture.setUri(uri);
			Intent intent= new Intent(this, PuzzleGameActivity.class);
			intent.putExtra("picture", fullPicture);
			intent.putExtra("difficulty", "4");
			startActivity(intent);
			finish(); 
		}
		else if (clicked == 1)
		{
			fullPicture.undo();
		}
		else
		{
			Bitmap test_picture = fullPicture.getFullBitmapManipulated();
			fullPicture.placeBitmap(test_picture);
//			test_picture.recycle();
		}
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		fullPicture.fixCurrentManipulateSmall();
		fullPicture.increaseCurrentValue();
		fullPicture.setSelectedManipulation(pos);
		seekBar.setProgress(50);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {}

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
		if (value > 0)
		{
			fullPicture.iteratePixels(value, true, false);
		}
	}
}
