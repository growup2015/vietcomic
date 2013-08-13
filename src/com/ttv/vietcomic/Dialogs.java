package com.ttv.vietcomic;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

/**
 * 
 * @author Tinh
 * 
 */
public class Dialogs extends Dialog implements
		android.view.View.OnClickListener {
	private Button bt_download, bt_cancel, bt_read_load, bt_read;
	private static final String CANCEL = "Hủy";
	private static final String DELETE = "Xóa dữ liệu";
	private static final String DOWN_READ = "Vừa đọc vừa tải";
	private static final String PAUSE = "Ngừng tải";
	private static final String CONTINUE = "Tiếp tục tải";
	private static final String DOWNLOAD = "Tải truyện";
	private static final String READ = "Đọc truyện";
	public Context ctx = new ChapterListActivity().getApplicationContext();
	private String url;

	public Dialogs(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		setContentView(R.layout.dialog_new);
		getWindow().getAttributes().width = LayoutParams.FILL_PARENT;
		getWindow().getAttributes().height = LayoutParams.WRAP_CONTENT;
		init(theme);
	}

	private void init(int stt) {
		switch (stt) {
		case 0:
			initialStatus();
			break;
		case 1:
			downloading();
			break;
		case 2:
			finish();
			break;
		case 3:
			pause();
			break;

		}
	}

	private void initialStatus() {

		bt_read = (Button) findViewById(R.id.btn1);
		bt_read.setVisibility(View.INVISIBLE);

		bt_read_load = (Button) findViewById(R.id.btn2);
		bt_read_load.setText(DOWN_READ);
		bt_read_load.setOnClickListener(this);

		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		bt_cancel.setText(CANCEL);
		bt_cancel.setOnClickListener(this);

		bt_download = (Button) findViewById(R.id.btn3);
		bt_download.setText(DOWNLOAD);
		bt_download.setOnClickListener(this);

	}

	private void downloading() {
		Log.d("dialog", "download");
		bt_read = (Button) findViewById(R.id.btn1);
		bt_read.setText(PAUSE);
		bt_read.setOnClickListener(this);

		bt_read_load = (Button) findViewById(R.id.btn2);
		bt_read_load.setText(READ);
		bt_read_load.setOnClickListener(this);

		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		bt_cancel.setText(CANCEL);
		bt_cancel.setOnClickListener(this);

		bt_download = (Button) findViewById(R.id.btn3);
		bt_download.setText(DELETE);
		bt_download.setOnClickListener(this);
	}

	private void finish() {
		Log.d("dialog", "finish");
		bt_read = (Button) findViewById(R.id.btn1);
		bt_read.setVisibility(View.INVISIBLE);

		bt_read_load = (Button) findViewById(R.id.btn2);
		bt_read_load.setText(READ);
		bt_read_load.setOnClickListener(this);

		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		bt_cancel.setText(CANCEL);
		bt_cancel.setOnClickListener(this);

		bt_download = (Button) findViewById(R.id.btn3);
		bt_download.setText(DELETE);
		bt_download.setOnClickListener(this);
	}

	private void pause() {
		Log.d("dialog", "pause");
		bt_read = (Button) findViewById(R.id.btn1);
		bt_read.setText(CONTINUE);
		bt_read.setOnClickListener(this);

		bt_read_load = (Button) findViewById(R.id.btn2);
		bt_read_load.setText(READ);
		bt_read_load.setOnClickListener(this);

		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		bt_cancel.setText(CANCEL);
		bt_cancel.setOnClickListener(this);

		bt_download = (Button) findViewById(R.id.btn3);
		bt_download.setText(DELETE);
		bt_download.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Button btn = (Button) v;
		String str = btn.getText().toString();
		if (str.equals(CANCEL)) {
			Log.d("dialog event", "huy");
			dismiss();
		} else if (str.equals(READ)) {
			Intent mainIntent = new Intent(ctx, ReadingViewActivity.class);
			ctx.startActivity(mainIntent);
			Log.d("dialog event", "doc");
		} else if (str.equals(DOWN_READ)) {
			Log.d("dialog event", "tai & doc");
			Intent mainIntent = new Intent(ctx, ReadingViewActivity.class);
			ctx.startActivity(mainIntent);

		} else if (str.equals(CONTINUE)) {
			Log.d("dialog event", "tiep");

		} else if (str.equals(DELETE)) {
			Log.d("dialog event", "xoa");
			

		} else if (str.equals(DOWNLOAD)) {
			Log.d("dialog event", "tai");

		} else if (str.equals(PAUSE)) {
			Log.d("dialog event", "dung tai");

		}
	}
}
