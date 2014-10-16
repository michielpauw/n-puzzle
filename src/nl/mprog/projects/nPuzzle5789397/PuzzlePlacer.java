package nl.mprog.projects.nPuzzle5789397;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class PuzzlePlacer extends BitmapPlacer{

	private static int tiles;
	private static Bitmap blackbox;
	private static Activity activity;

	public PuzzlePlacer(Bitmap bitmap, int tiles_in, Bitmap black, Activity activity_in, int puzzle_id_in, Context context) 
	{
		super(bitmap, puzzle_id_in, context, activity_in);
		tiles = tiles_in;
		blackbox = black;
		activity = activity_in;
	}

	// some setter and getter methods
	public Bitmap getBlack()
	{
		return blackbox;
	}

	public int getTiles()
	{
		return tiles;
	}

	// a method to get the size of the tiles
	public int[] getTileSize() 
	{
		int[] sizes_bmp = getSize();
		int width_res = sizes_bmp[0];
		int height_res = sizes_bmp[1];

		int width_tile = (int) (width_res / tiles - 7.5);
		int height_tile = (int) (height_res / tiles - 7.5);

		int[] tileSizes = new int[]{
						width_tile,
						height_tile
		};

		return tileSizes;
	}

	public void placePictures(Gameplay curGame, int[] order, boolean start)
	{
		// get the sizes of each tile
		int[] tileSizes = this.getTileSize();
		int tiles = this.getTiles();

		// get the first X and Y coordinates on the screen
		int firstX = (int) (this.getFirstX() - tiles * 7);
		int firstY = this.getFirstY();

		Bitmap puzzle_res = this.getResizedPicture();
		Bitmap blackbox = this.getBlack();

		Bitmap cropped;

		// create n tiles of the picture and one black tile
		for (int i = 0; i < tiles; i++)
		{
			// I define xCoordinate and x_pic here to save memory
			int xCoordinate = firstX + (tileSizes[0] + 7) * i;

			for (int j = 0; j < tiles; j++)
			{
				int tile = order[tiles * i + j];
				if (tile != 0)
				{
					int x_pic = tileSizes[0] * ((tile - 1) / tiles);
					int y_pic = tileSizes[1] * ((tile - 1) % tiles);

					cropped = Bitmap.createBitmap(puzzle_res, x_pic, y_pic, tileSizes[0], tileSizes[1]);
				}
				else
				{
					cropped = Bitmap.createBitmap(blackbox, 0, 0, tileSizes[0], tileSizes[1]);
				}

				// create an imageView for each tile (also the empty one)
				ImageView imageView = new ImageView(activity);
				imageView.setTag(R.string.pos_clicked, tiles * i + j);
				imageView.setTag(R.string.clicked, tile);
				imageView.setTag(R.string.curGame, curGame);
				imageView.setId(tiles * i + j);
				imageView.setImageBitmap(cropped);

				// create a relative layout
				RelativeLayout root = (RelativeLayout)activity.findViewById(R.id.root_layout);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tileSizes[0], tileSizes[1]);
				int yCoordinate = firstY + (tileSizes[1] + 7) * j;
				params.leftMargin = xCoordinate;
				params.topMargin = yCoordinate;

				// add the picture
				root.addView(imageView, params);
				if (start)
				{
					imageView.setOnClickListener((OnClickListener) activity);
				}
			}
		}
	}
}
