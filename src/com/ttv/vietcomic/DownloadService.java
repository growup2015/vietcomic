package com.ttv.vietcomic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Messenger;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;

import com.ttv.vietcomic.SQLite.DBAdapter;
import com.ttv.vietcomic.utils.NetworkUtils;
import com.ttv.vietcomic.utils.Utils;

public class DownloadService extends IntentService {
	private int comicId = 0;
	private int chapterId = 0;
	private int date = 0;
	private String title = null;

	private JSONArray chapterContent;
	private DBAdapter dbAdapter = null;

	public DownloadService() {
		super("DownloadService");
		// TODO Auto-generated constructor stub
	}

	String downloadUrl;
	public static boolean serviceState = false;
	private int result = Activity.RESULT_CANCELED;

	private void sendMessage(Messenger messenger, Message msg, int startus,
			String message) {
		try {
			msg = Message.obtain();
			msg.arg1 = startus;
			msg.obj = message;
			try {
				messenger.send(msg);
			} catch (android.os.RemoteException e1) {
			}
		} catch (Exception e) {
		}
	}

	private Bitmap download(String url) {

		StatFs stat_fs = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double avail_sd_space = (double) stat_fs.getAvailableBlocks()
				* (double) stat_fs.getBlockSize();
		// double GB_Available = (avail_sd_space / 1073741824);
		double MB_Available = (avail_sd_space / 10485783);
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			int fileSize = conn.getContentLength() / 1048576;
			Log.d("FILESIZE", "" + fileSize);
			if (MB_Available <= fileSize) {
				Toast.makeText(getApplicationContext(), "Thẻ nhớ đầy",
						Toast.LENGTH_LONG).show();
				conn.disconnect();
				return null;
			}
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			return bitmap;
		} catch (Throwable ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// Constant.chapterDownloadingPecent = 0;
		Log.d("downloading", "contentn");
		Bundle extras = intent.getExtras();
		Messenger messenger = null;
		Message msg = null;
		if (extras != null) {
			messenger = (Messenger) extras.get("MESSENGER");
			msg = Message.obtain();
		}

		this.comicId = intent.getIntArrayExtra("chapter_DOWNLOAD")[0];
		this.chapterId = intent.getIntArrayExtra("chapter_DOWNLOAD")[1];
		this.date = intent.getIntArrayExtra("chapter_DOWNLOAD")[2];
		this.title = intent.getStringExtra("titleChapter");
		File direct = new File(Environment.getExternalStorageState()
				+ "/VietComic/" + comicId + "/" + chapterId + "/");
		String api = null;
		String apiUrl = Constants.CHAPTER_FILE + chapterId;
		try {
			api = NetworkUtils.doGet(apiUrl);
			chapterContent = new JSONArray(api);
			Log.d("getApiArray", chapterContent.toString());
		} catch (Exception e0) {
		}

		dbAdapter = new DBAdapter(getApplicationContext());
		dbAdapter.open();
		int stt = dbAdapter.getComicChapterStatus(comicId);
		if (stt == 0) {
			for (int i = 0; i < chapterContent.length(); i++) {
				File file = null;
				try {
					String[] array = chapterContent.get(i).toString()
							.split("/");// http://download.vuimanga.vn/manga/0259/hiep-khach-giang-ho/volume-2/image_1.jpg
					// chia lam 7 substring
					file = new File(direct, array[7]);
					Log.d("chi tiet", "" + chapterContent.get(i) + "/"
							+ array[7]);
					if (file.exists())
						file.delete();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}

				try {
					Bitmap bitmap = download(chapterContent.get(i).toString());
					try {
						Log.d("liet ke", chapterContent.get(i).toString());
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					FileOutputStream out = new FileOutputStream(file);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
					Log.d("ket qua",
							""
									+ bitmap.compress(
											Bitmap.CompressFormat.JPEG, 100,
											out));
					result = Activity.RESULT_OK;
					out.flush();
					out.close();
				} catch (Exception e) {
					sendMessage(messenger, msg, -2,
							"Có lỗi trong lúc tải sách.");
				}
				dbAdapter = new DBAdapter(getApplicationContext());
				sendMessage(messenger, msg, 1, "Đang tải " + (i + 1));
				dbAdapter.open();
				dbAdapter.createComicChapter(comicId, title, date, 1, i,
						chapterContent.length());
				dbAdapter.close();
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}
		} else if ((stt == 1 || stt == 3)) {
			int total = Utils.getTotalFile(direct);
			for (int i = total; i < chapterContent.length(); i++) {
				String[] array = api.split("/");// http://download.vuimanga.vn/manga/0259/hiep-khach-giang-ho/volume-2/image_1.jpg
												// chia lam 7 substring
				File file = new File(direct, array[7]);
				if (file.exists())
					file.delete();
				try {
					Bitmap bitmap = download(chapterContent.get(i).toString());
					FileOutputStream out = new FileOutputStream(file);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
					result = Activity.RESULT_OK;
					out.flush();
					out.close();
				} catch (Exception e) {
					sendMessage(messenger, msg, -2,
							"Có lỗi trong lúc tải sách ");
				}
				dbAdapter = new DBAdapter(getApplicationContext());
				sendMessage(messenger, msg, 1, "Đang tải " + (i + 1));
				dbAdapter.open();
				dbAdapter.updateChapterDownloaded(i, chapterId);
				dbAdapter.close();
			}

		}
	}
}
