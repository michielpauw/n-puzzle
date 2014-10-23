// Name: Michiel Pauw
// E-mail: pauw.michiel@gmail.com
// Student ID: 5789397
// Source: http://wptrafficanalyzer.in/blog/listview-with-images-and-text-using-simple-adapter-in-android/

package nl.mprog.projects.nPuzzle5789397;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
	// get the picture id's
	int[] picture_id = new int[]{
					R.drawable.ajax,
					R.drawable.polder,
					R.drawable.balloon,
					R.drawable.universum
	};
	
	Button button;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		String[] pictures = this.getResources().getStringArray(R.array.pictures);

		// create a PictureList object which only loads small enough pictures
		// to save memory.
		PictureList list = new PictureList(this.getApplicationContext(), this);

		int n = picture_id.length;
		for(int i = 0; i < n; i++)
		{
			button = list.addListItem(picture_id[i], pictures[i], i);
			button.setOnClickListener(this);
		}

	}

	// go to next activity on button click
	@Override
	public void onClick(View v) 
	{
		int clicked = (Integer) v.getId();
		Intent intent= new Intent(this, ManipulateActivity.class);
		String picture = Integer.toString(picture_id[clicked]);
		intent.putExtra("picture", picture);
		RelativeLayout root = (RelativeLayout) this.findViewById(R.id.root_layout);
		root.removeAllViews();
		startActivity(intent);
		finish();
	}
}
