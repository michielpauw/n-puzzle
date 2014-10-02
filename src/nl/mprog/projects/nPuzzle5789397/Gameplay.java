package nl.mprog.projects.nPuzzle5789397;

import java.util.ArrayList;
import java.util.Collections;

public class Gameplay {

	private static int[] order;
	private static int tiles;

	// constructor
	public Gameplay(int tiles_in) {
		tiles = tiles_in;
		order = new int[tiles * tiles];
	}

	// a couple of getter/setter methods
	public int getTiles()
	{
		return tiles;
	}

	public void setOrder(int[] order_in)
	{
		order = order_in;
	}

	public int[] getOrder()
	{
		return order;
	}

	// create a random array that is solvable
	public void createRandomArray(boolean random)
	{
		int n = tiles * tiles;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i < n; i++)
		{
			list.add(i);
		}
		list.add(0);
		// check whether solvable
		boolean solvable = false;
		while (!solvable && random)
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
}
