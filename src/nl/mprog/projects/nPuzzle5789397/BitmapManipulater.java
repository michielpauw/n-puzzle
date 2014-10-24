package nl.mprog.projects.nPuzzle5789397;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;

public class BitmapManipulater extends BitmapPlacer {

	private static int height_res;
	private static int width_res;
	private static Bitmap bitmap_original;
	private static int shrunk_width;
	private static int shrunk_height;
	private static int manipulation = 0;
	private static Bitmap bitmap_ready;
	private static Bitmap bitmap_small;
	private static int[] manipulate_small;
	private static int[] manipulate_small_original;
	private static int[] manipulate_large;
	private static int[] manipulate_large_original;
	private static int[] manipulate_change;
	private static int manipulation_tracker;

	// some variables to be able to undo manipulations
	private static int[] which_manipulation;
	private static int[] value_manipulation;
	private static int current_value;

	public BitmapManipulater(int puzzle_id_in, Context context_in, Activity activity_in)
	{
		super(puzzle_id_in, context_in, activity_in);
		int[] size_res = this.getSize();

		// initiate a couple of variables and arrays
		width_res = size_res[0];
		height_res = size_res[1];
		bitmap_original = this.getResizedPicture();
		bitmap_ready = this.getResizedPicture();
		which_manipulation = new int[10];
		value_manipulation = new int[10];
		current_value = 0;
	}

	public Bitmap getBitmapOriginal()
	{
		return bitmap_original;
	}

	public Bitmap getBitmapReady()
	{
		return bitmap_ready;
	}

	public Bitmap getBitmapSmall()
	{
		return bitmap_small;
	}

	public void setManipulationTracker(int man_in)
	{
		manipulation_tracker = man_in;
	}

	// the bitmap we place after it has been manipulated
	public void setBitmapReady(Bitmap bitmap) 
	{
		bitmap_ready = bitmap;
	}

	public void setSelectedManipulation(int manipulation_in)
	{
		manipulation = manipulation_in;
	}

	// save the array after it has been manipulated after each manipulation
	public void fixCurrentManipulateSmall()
	{
		manipulate_small = copyBitmapPixels(shrinkBitmap(bitmap_ready), shrunk_width, shrunk_height);
	}

	// one of the arrays we need for a number of manipulations, saved not after every manipulation
	public void fixOriginalManipulateSmall()
	{
		manipulate_small_original = copyBitmapPixels(shrinkBitmap(bitmap_ready), shrunk_width, shrunk_height);
	}

	// save the array after it has been manipulated after each manipulation
	public void fixCurrentManipulateLarge()
	{
		manipulate_large = copyBitmapPixels(this.getResizedPicture(), width_res, height_res);
	}

	// one of the arrays we need for a number of manipulations, saved not after every manipulation
	public void fixOriginalManipulateLarge()
	{
		manipulate_large_original = copyBitmapPixels(bitmap_original, width_res, height_res);
	}

	// we keep track of the last manipulations in arrays and the current value makes
	// sure we can keep the arrays running parallel
	public void increaseCurrentValue()
	{
		if (current_value < 9)
		{
			current_value++;
		}
		else
		{
			current_value = 0;
		}
	}

	// shrink the bitmap to an easily manipulable size
	public Bitmap shrinkBitmap(Bitmap bitmap)
	{
		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bitmap
		float i = (float) 0.4;
		float j = (float) 0.4;
		matrix.setScale(i, j);

		// the -6 is to correct for rounding errors
		Bitmap bitmap_man = Bitmap.createBitmap(bitmap, 0, 0, width_res - 6, height_res - 6, matrix, true);
		shrunk_width = bitmap_man.getWidth();
		shrunk_height = bitmap_man.getHeight();
		return bitmap_man;
	}

	// blow up the bitmap to its original size, with lower resolution
	public Bitmap blowUpBitmap(Bitmap bitmap)
	{
		Bitmap bitmap_to_blow = bitmap;
		Matrix matrix = new Matrix();
		// resize the bitmap
		float i = (float) 2.5;
		float j = (float) 2.5;
		matrix.postScale(i, j);
		Bitmap bitmap_bigger = Bitmap.createBitmap(bitmap_to_blow, 0, 0, shrunk_width, shrunk_height, matrix, true);
		return bitmap_bigger;
	}

	// create a pixel array based on a bitmap
	public int[] copyBitmapPixels(Bitmap bitmap, int width, int height)
	{
		int[] manipulate = new int[width * height];
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				manipulate[i * width + j] = bitmap.getPixel(j, i);
			}
		}
		return manipulate;
	}

	// create a bitmap, based on a pixel array
	public Bitmap createBitmapFromPixels(int[] pixels, int width, int height)
	{
		Bitmap bitmap_new = bitmap_original.copy(Bitmap.Config.ARGB_8888, true);
		bitmap_new.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap_new;
	}

	// the method where the actual manipulation takes place, by manipulating
	// every pixel.
	public void iteratePixels(int value, boolean small, boolean undo)
	{
		int n;
		// manipulation while still in ManipulateActivity
		if (small && !undo)
		{
			n = shrunk_width * shrunk_height;
			// create an array of the pixels of the current shrunk bitmap
			manipulate_change = new int[n];
			System.arraycopy(manipulate_small, 0, manipulate_change, 0, manipulate_small.length );
		}
		// manipulation when the full sized picture should be used
		else if (!small && !undo)
		{
			n = width_res * height_res;
			// create an array of the pixels of the full bitmap
			manipulate_change = new int[n];
			System.arraycopy(manipulate_large, 0, manipulate_change, 0, manipulate_large.length );
		}
		// undo a manipulation
		else
		{
			n = shrunk_width * shrunk_height;
			// create an array of the pixels of the original shrunk bitmap
			manipulate_change = new int[n];
			System.arraycopy(manipulate_small_original, 0, manipulate_change, 0, manipulate_small_original.length );
		}
		// change the values of every pixel, based on which manipulation is selected
		value_manipulation[current_value] = value;
		which_manipulation[current_value] = manipulation;
		int cut = (255 * value) / 100;
		for (int i = 0; i < n; i++)
		{
			int blue = Color.blue(manipulate_change[i]);
			int red = Color.red(manipulate_change[i]);
			int green = Color.green(manipulate_change[i]);
			switch (manipulation) 
			{
				// change every pixel to either black or white
				case 0:
					if ((blue + green + red) / 3 < cut)
					{
						manipulate_change[i] = Color.rgb(1, 1, 1);
					}
					else
					{
						manipulate_change[i] = Color.rgb(255, 255, 255);
					}
					break;
					// change every pixel to a shade of grey
				case 1:
					int grey = (int) (((0.1140 * blue + 0.5870 * green + 0.2989 * red) / 100) * value);
					manipulate_change[i] = Color.rgb(grey, grey, grey);
					break;
					// do the first color manipulation
				case 2: case 3: case 4:
					int[] values = colorOne(value, manipulation, i, small);
					manipulate_change[i] = Color.rgb(values[0], values[1], values[2]);
					break;
					// do the second color manipulation
				case 5: case 6: case 7:
					int[] values_two = colorTwo(value, manipulation, i, small);
					manipulate_change[i] = Color.rgb(values_two[0], values_two[1], values_two[2]);
					break;
				default:
					break;
			}
		}

		if (small)
		{
			// create a small bitmap from the manipulated pixels
			bitmap_small = createBitmapFromPixels(manipulate_change, shrunk_width, shrunk_height);

			// blow up this bitmap, so it fills the screen
			bitmap_ready = blowUpBitmap(bitmap_small);
			this.setBitmapReady(bitmap_ready);

			// place the bitmap
			this.placeBitmap(bitmap_ready);
		}
		else
		{
			// after each manipulation the manipulated array is copied to the array which we will
			// make into a full sized bitmap later on
			System.arraycopy(manipulate_change, 0, manipulate_large, 0, manipulate_change.length);
		}
	}

	// a manipulation to give the image a red, green or blue filter
	public int[] colorOne(int value, int color, int i, boolean small)
	{
		double factor = value / 50.0;
		int blue;
		int red;
		int green;
		switch (color)
		{
			case 2:
				if (small)
				{
					red = Color.red(manipulate_small_original[i]);
					green = Color.green(manipulate_small[i]);
					blue = Color.blue(manipulate_small[i]);
				}
				else
				{
					red = Color.red(manipulate_large_original[i]);
					green = Color.green(manipulate_large[i]);
					blue = Color.blue(manipulate_large[i]);
				}
				// decrease the amount of the other colors, increase the one manipulated
				green = (int) (green / (factor * 2));
				blue = (int) (blue / (factor * 2));
				// of course with a maximum of 255
				if (red * factor < 255)
				{
					red = (int) (red * factor);
				}
				else
				{
					red = 255;
				}
				break;
			case 3:
				if (small)
				{
					red = Color.red(manipulate_small[i]);
					green = Color.green(manipulate_small_original[i]);
					blue = Color.blue(manipulate_small[i]);
				}
				else
				{
					red = Color.red(manipulate_large[i]);
					green = Color.green(manipulate_large_original[i]);
					blue = Color.blue(manipulate_large[i]);
				}
				// decrease the amount of the other colors, increase the one manipulated
				red = (int) (green / (factor * 2));
				blue = (int) (blue / (factor * 2));
				// of course with a maximum of 255
				if (green * factor < 255)
				{
					green = (int) (green * factor);
				}
				else
				{
					green = 255;
				}
				break;
			case 4:
				if (small)
				{
					blue = Color.blue(manipulate_small_original[i]);
					green = Color.green(manipulate_small[i]);
					red = Color.red(manipulate_small[i]);
				}
				else
				{
					blue = Color.blue(manipulate_large_original[i]);
					green = Color.green(manipulate_large[i]);
					red = Color.red(manipulate_large[i]);
				}
				// decrease the amount of the other colors, increase the one manipulated
				green = (int) (green / (factor * 2));
				red = (int) (red / (factor * 2));
				// of course with a maximum of 255
				if (blue * factor < 255)
				{
					blue = (int) (blue * factor);
				}
				else
				{
					blue = 255;
				}
				break;
			default:
				red = 0;
				green = 0;
				blue = 0;
				break;
		}
		int[] values = new int[] {red, green, blue};
		return values;
	}

	// a manipulation bringing back certain colors
	// only visible after other manipulations
	public int[] colorTwo(int value, int color, int i, boolean small)
	{
		int blue;
		int red;
		int green;

		// get the original bitmaps, which we base the manipulation on
		if (small)
		{
			red = Color.red(manipulate_small_original[i]);
			green = Color.green(manipulate_small_original[i]);
			blue = Color.blue(manipulate_small_original[i]);
		}
		else
		{
			red = Color.red(manipulate_large_original[i]);
			green = Color.green(manipulate_large_original[i]);
			blue = Color.blue(manipulate_large_original[i]);
		}
		int cutoff = 255 - value * 2;
		switch (color)
		{
			case 5:
				// if red is mainly present at this pixel: show it
				if (red > cutoff && blue < value && green < value)
				{
					if (red + value / 2 > blue + green)
					{
						blue = 0;
						green = 0;
					}
				}
				// otherwise it stays the same color as it was before
				else
				{
					if (small)
					{
						red = Color.red(manipulate_small[i]);
						green = Color.green(manipulate_small[i]);
						blue = Color.blue(manipulate_small[i]);
					}
					else
					{
						red = Color.red(manipulate_large[i]);
						green = Color.green(manipulate_large[i]);
						blue = Color.blue(manipulate_large[i]);
					}
				}
				break;
			case 6:
				// if green is mainly present at this pixel: show it
				if (green > cutoff && blue < value && red < value)
				{
					if (green + value / 2 > red + blue)
					{
						red = 0;
						blue = 0;
					}
				}
				// otherwise it stays the same color as it was before
				else
				{
					if (small)
					{
						red = Color.red(manipulate_small[i]);
						green = Color.green(manipulate_small[i]);
						blue = Color.blue(manipulate_small[i]);
					}
					else
					{
						red = Color.red(manipulate_large[i]);
						green = Color.green(manipulate_large[i]);
						blue = Color.blue(manipulate_large[i]);
					}
				}
				break;
			case 7:
				// if blue is mainly present at this pixel: show it
				if (blue + value / 2 > red + green)
				{
					red = 0;
					green = 0;
				}
				// otherwise it stays the same color as it was before
				else
				{
					if (small)
					{
						red = Color.red(manipulate_small[i]);
						green = Color.green(manipulate_small[i]);
						blue = Color.blue(manipulate_small[i]);
					}
					else
					{
						red = Color.red(manipulate_large[i]);
						green = Color.green(manipulate_large[i]);
						blue = Color.blue(manipulate_large[i]);
					}
				}
				break;
			default:
				red = 0;
				green = 0;
				blue = 0;
				break;
		}
		int[] values = new int[] {red, green, blue};
		return values;
	}

	// the method to undo the last manipulation(s)
	public void undo()
	{
		boolean didSomething = false;

		// get the value of the last manipulation
		if (current_value > 0)
		{
			current_value--;
		}
		else
		{
			current_value = 9;
		}

		setManipulationTracker(which_manipulation[current_value]);
		manipulation = manipulation_tracker;
		int value = value_manipulation[current_value];
		if (value > 0)
		{
			iteratePixels(value, true, true);
			didSomething = true;
		}
		// if there was nothing to undo, make sure all the arrays are in the right spot
		if (!didSomething && current_value < 9)
		{
			current_value++;
			manipulation = 10;
			iteratePixels(0, true, true);
		}
		else if (current_value == 9)
		{
			current_value = 0;
			manipulation = 10;
			iteratePixels(0, true, true);
		}
	}

	public Bitmap getFullBitmapManipulated()
	{
		fixOriginalManipulateLarge();
		fixCurrentManipulateLarge();
		int k = which_manipulation.length;
		// iterate through the last 10 manipulations to create a full sized
		// manipulated bitmap
		for (int j = 0; j < k; j++)
		{
			increaseCurrentValue();
			setManipulationTracker(which_manipulation[current_value]);
			manipulation = manipulation_tracker;
			int value = value_manipulation[current_value];
			if (value > 0)
			{
				iteratePixels(value, false, false);
			}
		}

		// create the bitmap from pixels
		Bitmap bitmap_large = createBitmapFromPixels(manipulate_large, width_res, height_res);
		return bitmap_large;
	}

	// clear all data when moving to another activity to save memory
	public void clearData()
	{
		if(getBitmapOriginal() != null)
		{
			getBitmapOriginal().recycle();
		}
		if(getBitmapReady() != null)
		{
			getBitmapReady().recycle();
		}
		if(getBitmapSmall() != null)
		{
			getBitmapSmall().recycle();
		}
		getPicture().recycle();
		getResizedPicture().recycle();
		bitmap_original = null;
		bitmap_ready = null;
	}
}
