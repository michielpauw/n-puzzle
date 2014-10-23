package nl.mprog.projects.nPuzzle5789397;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PictureList {

	private static Context context;
	private static Activity activity;
	private static int width_scr;
	private static RelativeLayout root;
	private static int width_picture;
	private static int width_text;
	private static int height_entry;
	private static int margins = 20;

	// constructor
	public PictureList(Context context_in, Activity activity_in)
	{
		context = context_in;
		activity = activity_in;
		setDisplayMetrics();
		width_picture = 2 * (width_scr - 2 * margins) / 5;
		width_text = 3 * (width_scr - 2 * margins) / 5;
		height_entry = width_picture + margins;
		root = (RelativeLayout)activity.findViewById(R.id.root_layout);
	}

	public void setDisplayMetrics()
	{
		// get the display width (height not relevant)
		DisplayMetrics metrics = context.getResources().getDisplayMetrics(); 
		width_scr = metrics.widthPixels;
	}

	// create and add a list item
	public Button addListItem(int picture_id, String text, int index)
	{
		// the button is basically the background of the image and its name
		Button button = new Button(activity);
		
		//set the properties for button
		RelativeLayout.LayoutParams paramsButton = new RelativeLayout.LayoutParams(width_scr, height_entry + 20);
		paramsButton.topMargin = index * height_entry + 20;
		button.setBackgroundColor(color.background_light);
		button.setId(index);
		root.addView(button, paramsButton);
		
		// create an image and text view which will be placed on the button
		ImageView imageView = new ImageView(activity);
		TextView textView = new TextView(activity);
		
		// set positions of the views
		RelativeLayout.LayoutParams paramsThumbnail = new RelativeLayout.LayoutParams(width_picture, height_entry);
		RelativeLayout.LayoutParams paramsText = new RelativeLayout.LayoutParams(width_text, height_entry);
		paramsThumbnail.topMargin = index * height_entry + 20;
		paramsThumbnail.leftMargin = margins;
		paramsText.topMargin = index * height_entry + 20;
		paramsText.leftMargin = width_picture + 2 * margins;
		
		// set image
		Bitmap bitmap = BitmapLoader.loadBitmap(picture_id, height_entry, width_picture, activity);
		imageView.setImageBitmap(bitmap);
		
		// set textview properties
		textView.setText(text);
		textView.setGravity(Gravity.CENTER);
		textView.setTextSize(20);
		textView.setTypeface(null, Typeface.BOLD);
		
		// add the views
		root.addView(imageView, paramsThumbnail);
		root.addView(textView, paramsText);
		
		// return the button so it can be used for a click listener
		return button;
	}
}
