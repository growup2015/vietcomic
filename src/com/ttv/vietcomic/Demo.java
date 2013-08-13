package com.ttv.vietcomic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ttv.vietcomic.SQLite.DBAdapter;
import com.ttv.vietcomic.object.Comic;

/**
 * Class po phong tu truyeenj.
 * 
 * */

public class Demo extends Activity {
	private static final String TAG = "DEMO";
	private Button button;
	private Context mContext = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo);
		Log.d(TAG, "list chapter");

		DBAdapter dbAdapter = new DBAdapter(mContext);
		Log.d("adapter", "" + dbAdapter.toString());
		dbAdapter.open();
		final Comic c = new Comic(51, "Vua hải tặc", "j. King", 38,
				"c/data/vietcomic/i.jpg",
				"Đây là tập truyện nổi tiếng của nhật", "manga", 0, 0, 0);
		 dbAdapter.addComic(c);

		// final Comic co = dbAdapter.getComic(50);
		// Log.d("comic get", "" + co.toString());
		dbAdapter.close();

		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			final String[] values = { c.getTitle(),
					new String("" + c.getStatus()), c.getAuthor(),
					new String("" + c.getChapter()),
					new String("" + c.getHit()) };

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "send click");
				Intent intent = new Intent(Demo.this, ChapterListActivity.class);
				intent.putExtra("comic", values);
				startActivity(intent);
			}
		});
	}
}
