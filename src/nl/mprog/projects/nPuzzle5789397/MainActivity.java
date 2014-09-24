// Name: Michiel Pauw
// E-mail: pauw.michiel@gmail.com
// Student ID: 5789397
// Source: http://wptrafficanalyzer.in/blog/listview-with-images-and-text-using-simple-adapter-in-android/

package nl.mprog.projects.nPuzzle5789397;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter;

public class MainActivity extends ListActivity implements AdapterView.OnItemClickListener{
	public final static String EXTRA_MESSAGE = "nl.mprog.projects.nPuzzle5789397.MESSAGE";
	
	// get the picture id's
	int[] picture_id = new int[]{
		R.drawable.ajax,
		R.drawable.polder,
		R.drawable.universum
    };
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		String[] pictures = this.getResources().getStringArray(R.array.pictures);
 
        // each row in the list stores picture and picture name
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
 
        for(int i = 0; i < 3; i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("txt", "Name: " + pictures[i]);
            hm.put("picid", Integer.toString(picture_id[i]) );
            aList.add(hm);
        }
 
        // keys used in Hashmap
        String[] from = {"picid", "txt"};
 
        // ids of views in listview_layout
        int[] to = {R.id.picid, R.id.txt};
 
        // instantiating an adapter to store each items
        // R.layout.activity_main defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.list_item, from, to);
 
        // Getting a reference to listview of main.xml layout file
        ListView listView = (ListView)findViewById(android.R.id.list);
 
        // Setting the adapter to the listView
        listView.setAdapter(adapter);
        
        ListView list = this.getListView();
		
        list.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Intent intent= new Intent(this, PuzzleStart.class);
		String picture = Integer.toString(picture_id[position]);
		intent.putExtra(EXTRA_MESSAGE, picture);
		startActivity(intent);
		finish(); 
	}
}
