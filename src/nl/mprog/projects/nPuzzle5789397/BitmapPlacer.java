package nl.mprog.projects.nPuzzle5789397;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class BitmapPlacer implements Parcelable {

	private static int width_scr;
	private static int height_scr;
	private static int width_bmp;
	private static int height_bmp;
	private static int picture_id;
	private static Bitmap picture;
	private static Bitmap picture_res;
	private static Context context;
	private static Activity activity;
	private static String uri;

	// constructors

	public BitmapPlacer(Bitmap bitmap, int picture_id_in, Context context_in, Activity activity_in) 
	{
		height_bmp = bitmap.getHeight();
		width_bmp = bitmap.getWidth();
		picture_id = picture_id_in;
		picture = bitmap;
		context = context_in;
		activity = activity_in;
		setDisplayMetrics();
		initResizedPic(picture);
	}

	public BitmapPlacer(Bitmap bitmap, Context context_in, Activity activity_in) 
	{
		height_bmp = bitmap.getHeight();
		width_bmp = bitmap.getWidth();
		context = context_in;
	}

	public void setResizedPicture(Bitmap bitmap)
	{
		picture_res = bitmap;
	}
	
	public void setDisplayMetrics()
	{
		// get the display height and width
		DisplayMetrics metrics = context.getResources().getDisplayMetrics(); 
		height_scr = metrics.heightPixels;
		width_scr = metrics.widthPixels;
	}

	public Bitmap getResizedPicture()
	{
		return picture_res;
	}
	
	public Bitmap getPicture()
	{
		return picture;
	}
	
	public void initResizedPic(Bitmap bitmap)
	{
		int[] bmp_size = this.getSize();
		picture_res = Bitmap.createScaledBitmap(picture, bmp_size[0], bmp_size[1], false);
	}

	public int getWidthScr()
	{
		return width_scr;
	}

	public int getHeightScr()
	{
		return height_scr;
	}
	
	public void setUri(String uri_in)
	{
		uri = uri_in;
	}
	
	public String getUri()
	{
		return uri;
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
						width_res - 50,
						height_res - 50
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

	// get the x-coordinate of the picture or first tile
	public int getFirstX()
	{
		int[] bmp_size = getSize();
		if (this.resizeVertically())
		{
			return this.getRelevantCoordinates();
		}
		else
		{
			return (width_scr - bmp_size[0]) / 2;
		}
	}

	// get the y-coordinate of the picture or first tile
	public int getFirstY()
	{
		int[] bmp_size = getSize();
		if (!this.resizeVertically())
		{
			return this.getRelevantCoordinates();
		}
		else
		{
			return (height_scr - bmp_size[1]) / 2;
		}
	}
	
	public void placeBitmap(Bitmap bitmap)
	{
		ImageView imageView = new ImageView(activity);
		imageView.setId(1);
		int[] bmp_size = this.getSize();
		imageView.setImageBitmap(bitmap);

		// place the picture in the right place
		RelativeLayout root = (RelativeLayout)activity.findViewById(R.id.root_layout);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(bmp_size[0], bmp_size[1]);
		
		params.leftMargin = this.getFirstX();
		params.topMargin = this.getFirstY();
		
		// add the picture
		root.addView(imageView, params);
	}

	protected BitmapPlacer(Parcel in) {
		int[] data = new int[3];

		in.readIntArray(data);
		width_scr = data[0];
		height_scr = data[1];
		picture_id = data[2];
		
		uri = in.readString();
	}

	@Override
	public int describeContents(){
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeIntArray(new int[] {width_scr,
						height_scr,
						picture_id});
		dest.writeString(uri);
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public BitmapPlacer createFromParcel(Parcel in) {
			return new BitmapPlacer(in); 
		}

		public BitmapPlacer[] newArray(int size) {
			return new BitmapPlacer[size];
		}
	};
}
