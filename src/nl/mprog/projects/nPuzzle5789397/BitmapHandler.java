package nl.mprog.projects.nPuzzle5789397;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.app.Activity;

public class BitmapHandle {
	
	private static int width_scr;
	private static int height_scr;
	private static int width_bmp;
	private static int height_bmp;
	private static int tiles;
	private static int gap = 50;
	private static int first_X;
	private static int first_Y;
	private static Bitmap puzzle;
	private static Bitmap blackbox;
	private static int[] order;
	private static int moves = 0;
	
	// constructor
	public BitmapHandle(int width_scr_in, int height_scr_in, Bitmap bitmap, int tiles_in, Bitmap black) {
		width_scr = width_scr_in;
		height_scr = height_scr_in;
		height_bmp = bitmap.getHeight();
		width_bmp = bitmap.getWidth();
		tiles = tiles_in;
		blackbox = black;
		order = new int[tiles * tiles];
	}
	
	// some setter and getter methods
	public void setPuzzle(Bitmap puzzle_in)
	{
		puzzle = puzzle_in;
	}
	
	public void setOrder(int[] order_in)
	{
		order = order_in;
	}
	
	public int[] getOrder()
	{
		return order;
	}
	
	public Bitmap getPuzzle()
	{
		return puzzle;
	}
	
	public Bitmap getBlack()
	{
		return blackbox;
	}
	
	public int getTiles()
	{
		return tiles;
	}
	
	// a method to determine whether the resize should be mainly vertical or not
	public boolean resizeVertically()
	{
		// calculate the aspect ratio of screen and image
		double aspect_ratio_bmp = height_bmp / width_bmp;
		double aspect_ratio_scr = height_scr / width_scr;
		
		if (aspect_ratio_bmp <= aspect_ratio_scr)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	// a method to determine what the size of the picture should be
	public int[] getSize() {

		int height_res;
		int width_res;
		
		boolean resize_vert = resizeVertically();
		if (!resize_vert)
		{
			double resize_factor = (double) width_bmp / width_scr;
			width_res = width_scr;
			height_res  = (int) Math.floor(height_bmp / resize_factor);
		}
		else
		{
			double resize_factor = (double) height_bmp / height_scr;
			height_res = height_scr;
			width_res  = (int) Math.floor(width_bmp / resize_factor);
		}
		
		int[] sizes = new int[]{
			width_res - 100,
			height_res - 100
		};
		
		return sizes;
	}
	
	// a method to get the non-trivial first coordinate
	public int getRelevantCoordinates() {
		boolean resize_vert = resizeVertically();
		int[] sizes_bmp = getSize();
		
		if (!resize_vert)
		{
			int height_res = sizes_bmp[1] + 100;
			int half_screen = height_scr / 2;
			int half_bmp = height_res / 2;
			int yCoordinate = half_screen - half_bmp;
			return yCoordinate;
		}
		else
		{
			int width_res = sizes_bmp[0] + 100;
			int half_screen = width_scr / 2;
			int half_bmp = width_res / 2;
			int xCoordinate = half_screen - half_bmp;
			return xCoordinate;
		}
	}
	
	// a method to get the size of the tiles
	public int[] getTileSize() {
		int[] sizes_bmp = getSize();
		int width_res = sizes_bmp[0];
		int height_res = sizes_bmp[1];
		
		int width_tile = width_res / tiles;
		int height_tile = height_res / tiles;
		
		int[] tileSizes = new int[]{
			width_tile,
			height_tile
		};
		
		return tileSizes;
	}
	
	// get the x-coordinate of the first tile
	public int getFirstX()
	{
		if (this.resizeVertically())
		{
			return this.getRelevantCoordinates();
		}
		else
		{
			return 0;
		}
	}
	
	// get the y-coordinate of the first tile
	public int getFirstY()
	{
		if (!this.resizeVertically())
		{
			return this.getRelevantCoordinates();
		}
		else
		{
			return 0;
		}
	}
	
	// create a random array that is solvable
	public void createRandomArray()
	{
		int n = tiles * tiles;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < n; i++)
		{
			list.add(i);
		}
		// check whether solvable
		boolean solvable = false;
		while (!solvable)
		{
			int position_zero = -1;
			int inversions = 0;
			Collections.shuffle(list);
			for (int i = 0; i < n; i++)
			{
				int number = list.get(i);
				if (number == 0)
				{
					position_zero = i;
				}
				// count the inversions of each number
				// http://www.cs.bham.ac.uk/~mdr/teaching/modules04/java2/TilesSolvability.html
				for (int j = i; j < n; j++)
				{
					if (list.get(j) < number && list.get(j) != 0)
					{
						inversions++;
					}
				}
			}
			if (inversions % 2 == 0)
			{
				if (tiles % 2 == 1 || (position_zero / tiles) % 2 == 1)
				{
					solvable = true;
				}
			}
			else if (tiles % 2 == 0 && (position_zero / tiles) % 2 == 0)
			{
				solvable = true;
			}
			else
			{
				solvable = false;
			}
		}
		// copy the list to an array
		for (int i = 0; i < n; i++) {
		    order[i] = list.get(i);
		}
	}
	
	// check whether the tile clicked is a movable tile
	public boolean legalMove(int clicked, int pos_clicked, int pos_zero)
	{	
		if ((pos_clicked - pos_zero) == tiles)
		{
			return true;
		}
		else if ((pos_clicked - pos_zero) == -tiles)
		{
			return true;
		}
		else if ((pos_clicked - pos_zero) == 1 && pos_clicked % tiles != 0)
		{
			return true;
		}
		else if ((pos_clicked - pos_zero) == -1 && pos_clicked % tiles != tiles - 1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	// switch the entries of the array after a move is made
	public void switchPictures(int clicked, int pos_zero, int pos_clicked)
	{
		order[pos_zero] = clicked;
		order[pos_clicked] = 0;
	}
	
	// check whether the puzzle is solved
	public boolean solved()
	{
		int n = tiles * tiles;
		for (int i = 0; i < n - 1; i++)
		{
			if (order[i] != i + 1)
			{
				return false;
			}
		}
		return true;
	}
	
	public int getMoves()
	{
		return moves;
	}
	
	public void setMoves(int moves_in)
	{
		moves = moves_in;
	}
}
