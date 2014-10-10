Design Doc for my improved n-puzzle
==========

The basis of my program will be the n-puzzle which I have programmed in the last couple of weeks. Before I start working on my extension I will improve the following features:

Modularazation
- I will change my BitmapHandler class to a BitmapResizer class. 
- BitmapResizer is responsible for determining the size of the picture in the screen and also the position of the top left coordinate. This way my PuzzleSolvedActivity needs less code to place the picture.
- I will create a PuzzlePlacer class which extends BitmapResizer, so that I won't need a seperate function to place the puzzle in my PuzzleGameActivity. The method getTileSize is moved from the original BitmapHandler class.
- I will make my onClick method smaller by moving some of its features to the GameActivity class.

Sending information to and from activities
- Instead of using multiple putExtra(...) methods, I will send entire objects containing all necessary information from activity to activity. In order to do this I implement Parcelable in the classes which I want to send.
- Instead of sending picture id's, I save pictures in a folder on my phone and send the URI to the next activity. This is necessary, because Bitmaps are to large to send through an intent and because I will manipulate pictures in my extension.

After these improvements I will implement the extension in the following way:

New activities
- I will implement the option to select a picture from the Gallery. I will use Intent(String action, Uri uri), where the action is Intent.ACTION_PICK and the URI is the URI of the image Gallery. Then startActivityForResult(intent, RESULT_LOAD_IMAGE) will open the gallery from which I can pick a picture. Then in onActivityResult(int requestCode, int resultCode, Intent data) I will handle the data the Intent from gallery sent me.
- I will implement the option to capture a picture using the camera, if a camera is present. I will follow the tutorial on the Android website to implement this (http://developer.android.com/training/camera/photobasics.html). This is just a fun extra feature.
- After a picture is selected I will give the player the option to manipulate the picture. 
	- I will implement a spinner so the player can select a specific manipulation he wants to impose on the picture (http://developer.android.com/guide/topics/ui/controls/spinner.html)
	- After a manipulation is selected, I will show one or multiple SeekBar Views (http://developer.android.com/reference/android/widget/SeekBar.html), with a short explanation of what the seekbar does (for example: set Grey Scale). 
	- The player can move the SeekBar. The change to the picture will not be real-time, but if the player clicks the button "Apply" the picture will be shown in its manipulated state. 
	- The manipulation itself will occur in a seperate class (BitmapManipulater which extends BitmapResizer). Here I will implement pixelwise manipulations. The main goal I set myself is to make the process of manipulation as quickly as possible. I've already noticed that manipulating a picture by looping through every pixel seperately is kind of a hassle.
	- After the picture is manipulated, the player gets the option to undo the previous n manipulations, or he can choose to manipulate it even more. After every manipulation the picture is saved (in its resized version). When the player clicks the button "Play", he will get the option to delete previous versions of the picture, or to keep all of them. He will now be directed to the PuzzleGameActivity, where he will play with the last version of his picture.
