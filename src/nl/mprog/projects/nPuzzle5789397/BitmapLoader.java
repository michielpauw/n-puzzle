package nl.mprog.projects.nPuzzle5789397;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

// a class to load a bitmap into memory, without using too much memory, inspired by the
// Android developers website

public class BitmapLoader {

	public static Bitmap loadBitmap(int picture_id, int height_req, int width_req, Activity activity)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();

		// set to true, it will give us information about the picture without loading it
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(activity.getResources(), picture_id, options);
		int inSampleSize = calculateInSampleSize(options, height_req, width_req);

		// inSampleSize is a power of 2 by which the image will be shrunk
		options.inSampleSize = inSampleSize;

		// now we want to load the picture into memory
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(activity.getResources(), picture_id, options);
	}

	// a method to determine the inSampleSize, which was provided by the Android developers website
	public static int calculateInSampleSize(BitmapFactory.Options options, int height_req, int width_req) 
	{
		// raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > height_req || width > width_req) 
		{

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > height_req
							&& (halfWidth / inSampleSize) > width_req) 
			{
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
}
