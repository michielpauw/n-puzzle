package nl.mprog.projects.nPuzzle5789397;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import android.graphics.Bitmap;
import android.os.Environment;

public class SavePictures {

	public static String fileName;
	public static String directory;

	public String createNewDateName()
	{
		Calendar c = Calendar.getInstance(); 
		String seconds = Integer.toString(c.get(Calendar.SECOND));
		String minute = Integer.toString(c.get(Calendar.MINUTE));
		String hour = Integer.toString(c.get(Calendar.HOUR));
		String day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
		String month = Integer.toString(c.get(Calendar.MONTH) + 1);
		String year = Integer.toString(c.get(Calendar.YEAR));
		fileName = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + seconds + ".bmp";
		return fileName;
	}

	public void createFolderAndSave(Bitmap picture, String fileName) 
	{
		directory = "/nPuzzlePictures";
		File folder = new File(Environment.getExternalStorageDirectory() + directory);

		if (!folder.exists()) 
		{
			directory = "/sdcard/nPuzzlePictures/";
			File pictureDirectory = new File(directory);
			pictureDirectory.mkdirs();
		}

		File file = new File(new File("/sdcard/nPuzzlePictures/"), fileName);
		if (file.exists()) 
		{
			file.delete();
		}
		try 
		{
			FileOutputStream out = new FileOutputStream(file);
			picture.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getUri()
	{
		String sdCard = Environment.getExternalStorageDirectory().getPath();
		String uri = sdCard + directory + "/" + fileName;
		return uri;
	}
}
