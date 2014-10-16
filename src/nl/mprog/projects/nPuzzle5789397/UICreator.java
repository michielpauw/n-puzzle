package nl.mprog.projects.nPuzzle5789397;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;

public class UICreator {
	
	private static Context context;
	private static Activity activity;
	private static int width_scr;
	private static int height_scr;
	private static RelativeLayout root;
	private static int heightSpinner = 120;
	private static int topMargin = 30;
	private static int heightSeekBar = 60;
	
	public UICreator(Context context_in, Activity activity_in) 
	{
		context = context_in;
		activity = activity_in;
		setDisplayMetrics();
		root = (RelativeLayout)activity.findViewById(R.id.root_layout);
	}
	
	public void setDisplayMetrics()
	{
		// get the display height and width
		DisplayMetrics metrics = context.getResources().getDisplayMetrics(); 
		height_scr = metrics.heightPixels;
		width_scr = metrics.widthPixels;
	}
	
	public Spinner createManipulateSpinner(int array, int width_full)
	{
		int width = (width_full / 6) * 2;
		RelativeLayout.LayoutParams paramsSpinner = new RelativeLayout.LayoutParams(width, heightSpinner);
		paramsSpinner.topMargin = topMargin;
		Spinner spinner = new Spinner(activity);
		String[] manipulations = activity.getResources().getStringArray(array);
		// Application of the Array to the Spinner
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, manipulations);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerArrayAdapter);
		root.addView(spinner, paramsSpinner);
		return spinner;
	}
	
	public Button addButton(String string, int x, int y, int width, int height)
	{
		RelativeLayout.LayoutParams paramsText = new RelativeLayout.LayoutParams(width, height);
		paramsText.topMargin = y;
		paramsText.leftMargin = x + 15;
		//set the properties for button
		Button button = new Button(activity);
		button.setText(string);
		button.setId(R.string.manipulate_cont);
		root.addView(button, paramsText);
		return button;
	}
	
	public SeekBar addSeekbar(int width_full)
	{
		int width = (width_full / 6) * 4 - 10;
		int position = (width_full / 6) * 2 + 10;
		RelativeLayout.LayoutParams paramsSeekBar = new RelativeLayout.LayoutParams(width, heightSeekBar);
		paramsSeekBar.topMargin = heightSpinner / 2 + topMargin - heightSeekBar / 2;
		paramsSeekBar.leftMargin = position;
		SeekBar seekBar = new SeekBar(activity);
		seekBar.setProgress(50);
		root.addView(seekBar, paramsSeekBar);
		return seekBar;
	}
	
	public static void setSeekBarProgress(int value, SeekBar seekBar)
	{
		seekBar.setProgress(value);
	}
}
