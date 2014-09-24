n-puzzle
========

# clone repo
git clone git@github.com:michielpauw/project0.git michielpauw

# make sure all gems are there
cd michielpauw
bundle

# start server
rails s

Notes on Beta release:
- I realized a little bit late that PuzzleStart and PuzzleGame should actually be one activity.
I will fix this before the Alpha-release, also giving me an opportunity of cleaning up the mess in
both activities.
- As of now I have not been able to implement functioning up/back buttons. I will fix this.
- I need to create edges around my tiles.
- My MainActivity class will display a nicer list of pictures in the Alpha-release.
- As of now I have not been able to support pictures of which the dimensions are smaller than
the screen size of a phone.

A fun little game for Android where you're supposed to rearrange a shuffled picture to its original shape.

Overview:
- Pick a picture you want to play the game with.
- The picture will be shown in solved state, cut up in n equally sized tiles (n depending on the difficulty)
  with the bottom right tile missing. The picture will be displayed as large as possible with the correct
  aspect ratio, with a border around every tile.
- After three seconds the tiles will be shuffled pseudo-randomly, in a way that the puzzle remains solvable.
  The tiles remain the same as the ones in the original, solved picture, including border. The missing tile
  will also be located at a random position.
- If the player taps a tile directly adjacent to the missing tile, the tapped tile swaps places with the
  missing tile.
- The player can always reset the game or change the difficulty of the game in a menu, which can be opened
  during gameplay. Changing the difficulty automatically restarts the game.
- The state of the game must be saved when the player quits the app or when other activities appear above it.
  I should keep in mind to save the picture chosen, the difficulty, the current arrangement of the tiles and
  the amount of moves taken.
- The difficulty levels which can be chosen are 'Easy', 'Medium' and 'Hard' with a respective amount of tiles
  of n=8, n=15 and n=24. The medium level is the default setting.
- If the puzzle is solved, the player should see the original picture, combined with the amount of moves it
  took him to solve the puzzle.
  
Functionalities that use in-built frameworks, libraries, or other technologies
- Navigation and linking between different screens
- Action Bar
- Displaying, resizing, manipulating pictures
- Radio buttons, lists, buttons, options dialog
- Handling of screentouch in different places of the screen
- Persistence
