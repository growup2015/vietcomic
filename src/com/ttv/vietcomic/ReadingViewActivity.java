package com.ttv.vietcomic;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ttv.vietcomic.SQLite.DBAdapter;
import com.ttv.vietcomic.utils.NetworkUtils;

@SuppressLint("NewApi")
public class ReadingViewActivity extends FragmentActivity implements
		OnClickListener, OnSeekBarChangeListener {

	private ImageButton btnBack, btnNext, btnPrev;
	private TextView tvNumchap, tvLoading, tvNumpage;
	public static LinearLayout top;
	public static RelativeLayout botton;
	ProgressDialog progressDialog;
	private SeekBar mBar;
	private ViewPager mPager;
	private ScreenSlidePagerAdapter mPageAdapter;
	ArrayList<File> listImages = new ArrayList<File>();
	Context context = this;
	JSONArray contentArray;
	public String[] Images;

	private int mCount;
	private static final String TAG = "ReadingViewActivity";

	public static boolean isShowbar = true;
	int[] chapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doc_truyen);
		try {
			Bundle recieve_bundle = getIntent().getExtras();
			chapter = recieve_bundle.getIntArray("page");

			Log.d(TAG, "go to this point " + chapter[1]);
			if (NetworkUtils.canConnect(this)) {
				String url = Constants.CHAPTER_FILE + chapter[1];
				Log.d("url", "" + url);
				new GetComicPage().execute(url);
			} else {
				Toast.makeText(getApplicationContext(),
						"KhÃ´ng thá»ƒ káº¿t ná»‘i internet! ",
						Toast.LENGTH_LONG).show();
				return;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}
			top = (LinearLayout) findViewById(R.id.top);
			botton = (RelativeLayout) findViewById(R.id.bar_botton);
			// /progress
			// mCount = Images.length;
			mPager = (ViewPager) findViewById(R.id.viewpager);
			mBar = (SeekBar) findViewById(R.id.seekBar1);
			mBar.setMax(mCount);
			mBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					mPager.setCurrentItem(progress);

				}

				public void onStartTrackingTouch(SeekBar seekBar) {

				}

				public void onStopTrackingTouch(SeekBar seekBar) {

				}
			});

			// /status
			tvNumchap = (TextView) findViewById(R.id.stt_chap);
			tvNumchap.setText("Tập " + chapter[0]);
			tvNumpage = (TextView) findViewById(R.id.stt_page);
			tvNumpage.setText("Trang 1/" + mCount);

			mPageAdapter = new ScreenSlidePagerAdapter(
					getSupportFragmentManager());
			mPager.setAdapter(mPageAdapter);
			mPager.setPageTransformer(true, new ZoomOutPageTransformer());
			mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					tvNumpage.setText("Trang " + (position + 1) + "/" + mCount);
					mBar.setProgress((position + 1));
					DBAdapter adapter = new DBAdapter(getApplicationContext());
					adapter.open();
					adapter.updateBookmark(chapter[1], position);
					invalidateOptionsMenu();
				}
			});

			// button back
			btnBack = (ImageButton) findViewById(R.id.btn_back);
			btnBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d(TAG, "get button back=-----");
					finish();
				}
			});
			// button next and previous
			btnNext = (ImageButton) findViewById(R.id.btn_nextchap);
			btnNext.setOnClickListener(this);
			btnPrev = (ImageButton) findViewById(R.id.btn_prevchap);
			btnPrev.setOnClickListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_nextchap:
			Log.d(TAG, "get button next =====");
			nextChapter(chapter[1]);
			break;
		case R.id.btn_prevchap:
			Log.d(TAG, "get button previous");
			break;
		}
	}

	private void nextChapter(int currentchap) {
		int i = currentchap;
		tvNumchap.setText("Tập " + (i));
		i++;
	}

	// scan whole sdcard for image files
	public void walkdir(File dir) {
		String jpgPattern = ".jpg";

		File listFile[] = dir.listFiles();

		if (listFile != null) {
			for (int i = 0; i < listFile.length; i++) {
				if (listFile[i].isDirectory()) {
					walkdir(listFile[i]);
				} else {
					if (listFile[i].getName().endsWith(jpgPattern)) {
						listImages.add(listFile[i].getParentFile());
						break;

					}
				}

			}
		}
	}

	// ///
	class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

		public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {
			try {
				return new ScreenSlidePageFragment(
						contentArray.getString(position));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public int getCount() {
			return mCount;
		}

		public void setCount(int count) {
			if (count > 0 && count <= 10) {
				mCount = count;
				notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		mPager.setCurrentItem(progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	// / get link online
	private class GetComicPage extends AsyncTask<String, JSONArray, String> {

		protected String doInBackground(String... urls) {
			String url = urls[0];
			try {
				return NetworkUtils.doGet(url);

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		protected synchronized void onPostExecute(String result) {
			try {
				contentArray = new JSONArray(result);
				Log.d("loop", contentArray.getString(1));
				mCount = contentArray.length();
				// Images = new String[mCount];
				// for (int i = 0; i < mCount; i++) {
				// Images[i] = arrayPages.getString(i);
				// Log.d("loop", Images[i]);
				// }
				Log.d("get comic page", "" + Images);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
