n-puzzle design
========

Built-in classes and methods
- R
- View
  - void - setOnClickListener(View view)
  - int - getId
  - void - setId(int n)
- LinearLayout
  - void - addView(View view)
  - void - removeView (View view)
  - void - removeViewAt(View view)
- Button
- ListView
- ListActivity
  - ListView - getListView()
  - void - setListAdapter(ListAdapter)
- ArrayAdapter
- BitmapFactory
  - static Bitmap - decodeResource(Resources res, int id)
- Context
  - abstract Resources - getResources()
- Bitmap
  - static Bitmap - createScaledBitmap(Bitmap bitmap, int width, int height, boolean filter)
  - static Bitmap - createBitmap(Bitmap bitmap, int x, int y, int width, int height)
- Resources
  - DisplayMetrics - getDisplayMetrics()
- TableLayout
- RelativeLayout
- Intent
- Activity(.super)
  - void - onPause()
  - void - onResume()
  - void - onCreate()
  - SharedPreferences - getPreferences(int)
  - boolean - onCreateOptionsMenu(Menu menu)
  - boolean - onOptionsItemSelected(Menu item)
- SharedPreferences
  - type - get<type>(String name, int value)
- SharedPreferences.Editor
  - abstract boolean - commit()
  - abstract SharedPreferences.Editor - put<type>(String name, int value)
- MenuInflater
  - void - inflate(int menuRes, Menu menu)
- Handler
  - boolean postDelayed(Runnable r, long delayMillis)
- Runnable
  - abstract void - Run()

Classes I will implement
- PuzzleGameplay
  - boolean - checkSolved(array order)
  - array - setupRandomOrder(int difficulty)

Packages
- android.view
- android.widget
- android.content
- android.graphics
- android.app

Resources
- layouts
- drawable
- menu
