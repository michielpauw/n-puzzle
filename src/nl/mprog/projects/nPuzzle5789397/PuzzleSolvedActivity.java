package nl.mprog.projects.nPuzzle5789397;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PuzzleSolved extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle_solved);
		// get the intent from PuzzleClicked
		Intent intent = getIntent();
		// get the id of the picture that was clicked
		String moves = intent.getStringExtra("picture_name");
		
		TextView textView = new TextView(PuzzleSolved.this);
		textView.setText("You have solved it in " + moves + " moves! I will make this bit much nicer, of course.");
		
		// add the textview
		View linearLayout =  findViewById(R.id.info);

        ((LinearLayout) linearLayout).addView(textView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.puzzle_solved, menu);
		return true;
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
