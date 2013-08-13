package com.ttv.vietcomic.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;

public class Utils {

	

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static void creatFolder(String directory) {
		File direct = new File(Environment.getExternalStorageState()
				+ "/VietComic/" + directory);
		if (!direct.exists()) {
			direct.mkdir();
		}
	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean checkExternalStorageState() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	// check file in
	private static boolean checkImageFile(File f) {
		String jpgPattern = ".jpg";
		String pngPattern = ".png";
		String gifPattern = ".gif";
		if (f.getName().endsWith(pngPattern)
				|| f.getName().endsWith(jpgPattern)
				|| f.getName().endsWith(gifPattern)) {
			return true;
		}
		return false;
	}

	// get total files in a folder
	public static int getTotalFile(File f) {
		File[] f1 = f.listFiles();
		int size = 0;
		for (int i = 0; i < f1.length; i++) {
			if (checkImageFile(f1[i])) {
				size++;
			}
		}
		return size;

	}
}
