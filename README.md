n-puzzle
========

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
- The difficulty levels which can be chosen are 'Super easy', 'Easy', 'Medium', 'Hard' and 'Super hard' 
  with a respective amount of tiles of n=3, n=8, n=15, n=24 and n = 35. The medium level is the default setting.
- If the puzzle is solved, the player should see the original picture, combined with the amount of moves it
  took him to solve the puzzle.
- Navigation is only possible using the Action Bar.
  
Functionalities that use in-built frameworks, libraries, or other technologies
- Navigation and linking between different screens
- Action Bar
- Displaying, resizing, manipulating pictures
- Lists, buttons, options dialog
- Handling of screentouch in ImageViews
- Persistence
