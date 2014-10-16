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

	// some variables to be able to undo manipulations
	private static int[] black_white_value;
	private static int[] grey_value;
	private static int[] red1_value;
	private static int[] blue1_value;
	private static int[] green1_value;
	private static int[] red2_value;
	private static int[] blue2_value;
	private static int[] green2_value;
	private static int current_value;

	public BitmapManipulater(Bitmap bitmap_in, int puzzle_id_in, Context context_in, Activity activity_in)
	{
		super(bitmap_in, puzzle_id_in, context_in, activity_in);
		int[] size_res = this.getSize();
		width_res = size_res[0];
		height_res = size_res[1];
		bitmap_original = this.getResizedPicture();
		bitmap_ready = this.getResizedPicture();
		black_white_value = new int[10];
		grey_value = new int[10];
		red1_value = new int[10];
		blue1_value = new int[10];
		green1_value = new int[10];
		red2_value = new int[10];
		blue2_value = new int[10];
		green2_value = new int[10];
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
	
	public void setBitmapReady(Bitmap bitmap) 
	{
		bitmap_ready = bitmap;
	}

	public void setSelectedManipulation(int manipulation_in)
	{
		manipulation = manipulation_in;
	}

	public void fixCurrentManipulateSmall()
	{
		manipulate_small = copyBitmapPixels(shrinkBitmap(bitmap_ready), shrunk_width, shrunk_height);
	}

	public void fixOriginalManipulateSmall()
	{
		manipulate_small_original = copyBitmapPixels(shrinkBitmap(bitmap_ready), shrunk_width, shrunk_height);
	}

	public void fixCurrentManipulateLarge()
	{
		manipulate_large = copyBitmapPixels(this.getResizedPicture(), width_res, height_res);
	}
	
	public void fixOriginalManipulateLarge()
	{
		manipulate_large_original = copyBitmapPixels(bitmap_original, width_res, height_res);
	}

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

	public Bitmap shrinkBitmap(Bitmap bitmap)
	{
		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bitmap
		float i = (float) 0.2;
		float j = (float) 0.2;
		matrix.setScale(i, j);
		Bitmap bitmap_man = Bitmap.createBitmap(bitmap, 0, 0, width_res - 6, height_res - 6, matrix, true);
		shrunk_width = bitmap_man.getWidth();
		shrunk_height = bitmap_man.getHeight();
		return bitmap_man;
	}

	public Bitmap blowUpBitmap(Bitmap bitmap)
	{
		Bitmap bitmap_to_blow = bitmap;
		Matrix matrix = new Matrix();
		// resize the bitmap
		matrix.postScale(5, 5);
		Bitmap bitmap_bigger = Bitmap.createBitmap(bitmap_to_blow, 0, 0, shrunk_width, shrunk_height, matrix, true);
		return bitmap_bigger;
	}

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

	public Bitmap createBitmapFromPixels(int[] pixels, int width, int height)
	{
		Bitmap bitmap_new = bitmap_original.copy(Bitmap.Config.ARGB_8888, true);
		bitmap_new.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap_new;
	}

	public void iteratePixels(int value, boolean small, boolean undo)
	{
		int n;
		int k;

		if (small && !undo)
		{
			n = shrunk_width * shrunk_height;
			manipulate_change = new int[n];
			System.arraycopy(manipulate_small, 0, manipulate_change, 0, manipulate_small.length );
		}
		else if (!small && !undo)
		{
			n = width_res * height_res;
			manipulate_change = new int[n];
			System.arraycopy(manipulate_large, 0, manipulate_change, 0, manipulate_large.length );
			k = 2;
		}
		else
		{
			n = shrunk_width * shrunk_height;
			manipulate_change = new int[n];
			System.arraycopy(manipulate_small_original, 0, manipulate_change, 0, manipulate_small_original.length );
		}
		int cut = (255 * value) / 100;
		for (int i = 0; i < n; i++)
		{
			int blue = Color.blue(manipulate_change[i]);
			int red = Color.red(manipulate_change[i]);
			int green = Color.green(manipulate_change[i]);
			switch (manipulation) 
			{
				case 0:
					black_white_value[current_value] = value;
					if ((blue + green + red) / 3 < cut)
					{
						manipulate_change[i] = Color.rgb(1, 1, 1);
					}
					else
					{
						manipulate_change[i] = Color.rgb(255, 255, 255);
					}
					break;
				case 1:
					int grey = (int) (((0.1140 * blue + 0.5870 * green + 0.2989 * red) / 100) * value);
					manipulate_change[i] = Color.rgb(grey, grey, grey);
					grey_value[current_value] = value;
					break;
				case 2: case 3: case 4:
					int[] values = colorOne(value, manipulation, i, small);
					manipulate_change[i] = Color.rgb(values[0], values[1], values[2]);
					break;
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
			bitmap_small = createBitmapFromPixels(manipulate_change, shrunk_width, shrunk_height);
			bitmap_ready = blowUpBitmap(bitmap_small);
			this.setBitmapReady(bitmap_ready);
			this.placeBitmap(bitmap_ready);
//			bitmap_ready.recycle();
		}
		else
		{
			System.arraycopy(manipulate_change, 0, manipulate_large, 0, manipulate_change.length);
		}
	}

	public int[] colorOne(int value, int color, int i, boolean small)
	{
		double factor = value / 50.0;
		int blue;
		int red;
		int green;
		switch (color)
		{
			case 2:
				red1_value[current_value] = value;
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
				green = (int) (green / (factor * 2));
				blue = (int) (blue / (factor * 2));
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
				green1_value[current_value] = value;
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
				red = (int) (green / (factor * 2));
				blue = (int) (blue / (factor * 2));
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
				blue1_value[current_value] = value;
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
				green = (int) (green / (factor * 2));
				red = (int) (red / (factor * 2));
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

	public int[] colorTwo(int value, int color, int i, boolean small)
	{
		int blue;
		int red;
		int green;
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
				red2_value[current_value] = value;
				if (red > cutoff && blue < value && green < value)
				{
					if (red + value / 2 > blue + green)
					{
						blue = 0;
						green = 0;
					}
				}
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
				green2_value[current_value] = value;
				if (green > cutoff && blue < value && red < value)
				{
					if (green + value / 2 > red + blue)
					{
						red = 0;
						blue = 0;
					}
				}
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
				blue2_value[current_value] = value;
				if (blue + value / 2 > red + green)
				{
					red = 0;
					green = 0;
				}
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

	public void undo()
	{
		boolean didSomething = false;
		if (current_value > 0)
		{
			current_value--;
		}
		else
		{
			current_value = 9;
		}
		
		int[] current_values = new int[] {
						black_white_value[current_value],
						grey_value[current_value],
						red1_value[current_value],
						green1_value[current_value],
						blue1_value[current_value],
						red2_value[current_value],
						green2_value[current_value],
						blue2_value[current_value]
		};

		int n = current_values.length;
		
		for (int i = 0; i < n; i++)
		{
			if (current_values[i] > 0)
			{
				manipulation = i;
				iteratePixels(current_values[i], true, true);
				didSomething = true;
			}
		}
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
		current_values = null;
	}

	public Bitmap getFullBitmapManipulated()
	{
		fixOriginalManipulateLarge();
		fixCurrentManipulateLarge();
		int k = black_white_value.length;
		for (int j = 0; j < k; j++)
		{
			increaseCurrentValue();
			int[] current_values = new int[] {
							black_white_value[current_value],
							grey_value[current_value],
							red1_value[current_value],
							green1_value[current_value],
							blue1_value[current_value],
							red2_value[current_value],
							green2_value[current_value],
							blue2_value[current_value]
			};
			int n = current_values.length;
			for (int i = 0; i < n; i++)
			{
				if (current_values[i] > 0)
				{
					manipulation = i;
					iteratePixels(current_values[i], false, false);
				}
			}
		}
		
		Bitmap bitmap_large = createBitmapFromPixels(manipulate_large, width_res, height_res);
		this.placeBitmap(bitmap_large);
		return bitmap_large;
	}
	
	public void clearData()
	{
		bitmap_original = null;
		bitmap_ready = null;
		black_white_value = null;
		grey_value = null;
		red1_value = null;
		blue1_value = null;
		green1_value = null;
		red2_value = null;
		blue2_value = null;
		green2_value = null;
	}
}
