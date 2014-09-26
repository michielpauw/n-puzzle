n-puzzle design
========

- MainActivity
  - I've implemented a list using SimpleAdapter. This way I was able to place pictures in my list. I am not
  happy with the result, so I will change this for the Alpha release.
  - The Intent I send to the PuzzleStart activity contains a unique ID for the picture that was clicked.
- PuzzleStart
  - I've implemented a class BitmapHandle to give me easy access to everything I need for the puzzle to be
  drawn (and in the next activity to be played). I've used a RelativeLayout to draw the puzzle for any amount
  of vertical/horizontal tiles (so I can implement a super hard difficulty level without problems).
- PuzzleGame
  - In PuzzleGame I use the BitmapHandle class. I only need to draw the tiles in a solvable, random state,
  once again using RelativeLayout. Besides that I handle OnClick events, checking whether a move is legal
  (using BitmapHandle) and to reorder the array and the pictures.
  - When the puzzle is solved (as checked by BitmapHandle) I send an Intent to PuzzleSolved containing the 
  amount of moves and the picture ID.
  - When the player clicks the menu button, he can choose between difficulty levels. At the moment the Activity
  sends an Intent to itself, but this will be changed, so it sends it to PuzzleStart.
- PuzzleSolved
  - So far I haven't implemented a nice PuzzleSolved Activity. Here I will display the complete picture and
  tell the player how many moves it took him to solve the puzzle.
  - When the user clicks 'back' he should be redirected to MainActivity.
- BitmapHandle
  - A class which makes the drawing of the pictures and gameplay much easier. It keeps track of the Bitmaps,
  the sizes, coordinates, moves and amount of tiles.
  - boolean resizeVertically() checks whether the picture should be as wide or as high as the screen.
  - int[] getSize() returns an array containing the sizes the picture should be.
  - int getRelevantCoordinates() returns either the first Y or first X coordinate (the non-trivial one).
  - int[] getTileSize() returns the sizes of each tile.
  - void createRandomArray() creates a random, solvable order of the tiles.
  - boolean legalMove(int clicked, int pos_clicked, int pos_zero) tells whether a move is legal.
  - void switchPictures(int clicked, int pos_zero, int pos_clicked) changes the order of the tiles in the
  order array.
  - boolean solved() tells us whether the game is in a solved state.

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
- SimpleAdapter
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
