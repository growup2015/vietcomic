package com.ttv.vietcomic;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ttv.vietcomic.SQLite.DBAdapter;
import com.ttv.vietcomic.object.Bookmark;
import com.ttv.vietcomic.object.ComicChapter;
import com.ttv.vietcomic.utils.NetworkUtils;
import com.ttv.vietcomic.utils.Utils;

public class ChapterListActivity extends Activity implements OnClickListener {

	private ListView list_chapter;
	private ImageButton btn_back, btn_khotruyen;
	private TextView ten_truyen, trang_thai, tac_gia, so_chuong, luot_xem;
	private ImageView anh_bia;
	public Context mcontext = this;
	
	private static final String TAG = "ChapterListActivity";
	private static final String C_STATUS[] = { "Chưa hoàn thành", "Hoàn thành" };
	static String author;
	static String status;
	Bookmark mBookmark = null;
	DBAdapter mDBAdapter;
	ArrayList<ComicChapter> ComicChapter = new ArrayList<ComicChapter>();
	ChapterListAdapter adapter;
	
	boolean isFirstTime = true;
	int numChap;
	JSONObject comicJSOn;

	// public Handler handler = new Handler() {
	// public void handleMessage(Message message) {
	// Bundle data = message.getData();
	// if (message.arg1 == RESULT_OK && data != null) {
	//
	// String path = data.getString("absolutePath");
	// Toast.makeText(ChapterListActivity.this, "Downloaded" + path,
	// Toast.LENGTH_LONG).show();
	// } else {
	// Toast.makeText(ChapterListActivity.this, "Download failed.",
	// Toast.LENGTH_LONG).show();
	// }
	//
	// };
	// };

	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tu_truyen);
		mDBAdapter = new DBAdapter(this);
		Log.d(TAG, "list chapter");

		Bundle recieve_bundle = getIntent().getExtras();
		final String[] readComic = recieve_bundle.getStringArray("comic");
		int status = 0;

		Intent intent = new Intent(this, DownloadService.class);
		// Create a new Messenger for the communication back
		// Messenger messenger = new Messenger(handler);
		// intent.putExtra("MESSENGER", messenger);
		// int[] values = { 2, 2, 0 };
		// intent.putExtra("chapter_DOWNLOAD", values);
		// intent.putExtra("chapter_DOWNLOAD", "vinh cuop bien");
		// startService(intent);

		// lần đầu tải khi, c_commic chưa có dữ liệu. 3s chuyển tới màn hình đọc
		// new Handler().postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// int[] truyen = new int[] { ComicChapter.get(0).getComicId(), 1 };
		// if (isFirstTime) {
		// Intent mainIntent = new Intent(ChapterListActivity.this,
		// ReadingViewActivity.class);
		// mainIntent.putExtra("chap", truyen);
		// startActivity(mainIntent);
		// } else {
		// int mBookmark = mDBAdapter.getBookmark(5);// get chapterid
		// // form sql
		// Intent mainIntent = new Intent(ChapterListActivity.this,
		// ReadingViewActivity.class);
		// mainIntent.putExtra("chap", mBookmark);
		// startActivity(mainIntent);
		// }
		// }
		// }, 10000);
		list_chapter = (ListView) findViewById(R.id.listView1);
		adapter = new ChapterListAdapter(mcontext, R.id.listView1, ComicChapter);
		list_chapter.setAdapter(adapter);

		ten_truyen = (TextView) findViewById(R.id.lb_ten_truyen);
		trang_thai = (TextView) findViewById(R.id.lb_stt);
		tac_gia = (TextView) findViewById(R.id.lb_author);
		so_chuong = (TextView) findViewById(R.id.lb_numchap);
		luot_xem = (TextView) findViewById(R.id.lb_view);
		anh_bia = (ImageView) findViewById(R.id.img_daidien);
		// Lấy thông tin từ activity trước hoặc từ SQLite

		if (status == 0) {
			// lấy tin từ trên mạng
			trang_thai.setText(C_STATUS[0]);
			if (NetworkUtils.canConnect(this)) {
				new GetComicDetailAndChapter()
						.execute(Constants.COMIC_DETAIL_CHAPTER + 2);
			} else {
				Toast.makeText(this,
						"KhÃ´ng káº¿t ná»‘i Ä‘Æ°á»£c vá»›i server",
						Toast.LENGTH_LONG).show();
			}
		} else if (status == 1) {
			if (!Utils.checkExternalStorageState()) {
				Toast.makeText(mcontext, "Không tìm thấy thẻ nhớ",
						Toast.LENGTH_LONG);

			} else {

			}
			// lấy tin từ cơ sở dữ liệu
			// trang_thai.setText(C_STATUS[1]);
			// ten_truyen.setText(co.getId());
			// trang_thai.setText(co.getStatus());
			// tac_gia.setText(co.getAuthor());
			// so_chuong.setText(co.getChapter());
			// luot_xem.setText(co.getHit());
			//
			// for (int i = 0; i < co.getChapter(); i++) {
			// listChapter.add("Tập " + i);
			// }
		}

		btn_back = (ImageButton) findViewById(R.id.bt_back);
		btn_back.setOnClickListener(this);

		btn_khotruyen = (ImageButton) findViewById(R.id.bt_khotruyen);
		btn_khotruyen.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int view = v.getId();
		switch (view) {
		case R.id.bt_back:
			Log.d(TAG, "back " + v.getId());
			break;
		case R.id.bt_khotruyen:
			Log.d(TAG, "Kho truyen " + v.getId());
			break;
		}
	}

	private class GetComicDetailAndChapter extends
			AsyncTask<String, Void, String> {

		protected String doInBackground(String... urls) {
			String url = urls[0];

			try {
				return NetworkUtils.doGet(url);

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		protected void onPostExecute(String result) {
			try {
				if (result == null)
					return;
				JSONObject json = new JSONObject(result);

				comicJSOn = json.getJSONObject(Constants.TAG_COMIC);

				String strUrl = comicJSOn.getString(Constants.C_IMAGE);
				Log.d(TAG, "" + strUrl);
				String title = comicJSOn.getString(Constants.C_TITLE)
						.toString();
				String author = comicJSOn.getString(Constants.C_AUTHOR)
						.toString().trim();
				String views = comicJSOn.getString(Constants.C_VIEWS)
						.toString();
				int intStatus = comicJSOn.getInt(Constants.C_STATUS);
				String status = null;
				switch (intStatus) {
				case 0:
					status = "Chưa hoàn thành";
					break;
				case 1:
					status = "Hoàn thành";
					// query from sql
					break;
				}
				JSONArray chapter = json.getJSONArray(Constants.TAG_CHAPTER);
				for (int a = 0; a < chapter.length(); a++) {
					JSONObject chapterDetail = chapter.getJSONObject(a);
					int id = chapterDetail.getInt("id");
					int comicId = chapterDetail.getInt("comic_id");
					int date = chapterDetail.getInt("create_date");
					String titlechapter = chapterDetail.getString("title");
					int totalpage = chapterDetail.getInt("total_file");
					ComicChapter.add(new ComicChapter(id, comicId,
							titlechapter, date, 0, totalpage, 0));
				}

				ten_truyen.setText(title);
				trang_thai.setText("Trạng thái: " + status);
				tac_gia.setText("Tác giải: " + author);
				so_chuong.setText("Số chương: " + numChap);
				luot_xem.setText("Lượt xem: " + views);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
