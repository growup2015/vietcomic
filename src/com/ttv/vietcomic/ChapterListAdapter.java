package com.ttv.vietcomic;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ttv.vietcomic.SQLite.DBAdapter;
import com.ttv.vietcomic.object.ComicChapter;

public class ChapterListAdapter extends ArrayAdapter<ComicChapter> {

	ArrayList<ComicChapter> chapters;
	int resource;
	int textViewSrc;
	int s;
	Context context;
	public int numDownloaded;
	private Button bt_download, bt_cancel, bt_read_load, bt_read;
	private static LayoutInflater inflater = null;

	private static final String CANCEL = "Hủy";
	private static final String DELETE = "Xóa dữ liệu";
	private static final String DOWN_READ = "Vừa đọc vừa tải";
	private static final String PAUSE = "Ngừng tải";
	private static final String CONTINUE = "Tiếp tục tải";
	private static final String DOWNLOAD = "Tải truyện";
	private static final String READ = "Đọc truyện";

	// private Handler handler = new Handler() {
	// public void handleMessage(Message message) {
	// Bundle data = message.getData();
	// if (data != null) {
	// int downloadedPage = data.getInt("absolutePath " + data);
	// numDownloaded = downloadedPage;
	// } else {
	// Toast.makeText(context, "Download failed.", Toast.LENGTH_LONG)
	// .show();
	// }
	//
	// };
	// };

	public ChapterListAdapter(Context context, int textViewResourceId,
			ArrayList<ComicChapter> objects) {
		super(context, R.layout.single_row_tu_truyen, objects);
		this.context = context;
		resource = textViewResourceId;
		chapters = objects;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rowView = inflater.inflate(R.layout.single_row_tu_truyen, null);
		TextView textViewTilte = (TextView) rowView.findViewById(R.id.chapter);
		textViewTilte.setText("Tập " + (position + 1));
		final ProgressBar progressbar = (ProgressBar) rowView
				.findViewById(R.id.progressBar1);
		final TextView textStatus = (TextView) rowView
				.findViewById(R.id.loading_stt);

		DBAdapter dbAdapter = new DBAdapter(context);
		Log.d("adapter", "" + dbAdapter.toString());
		dbAdapter.open();
		dbAdapter.getComicChapterStatus(chapters.get(position).getComicId());
		s = 0;
//		new Handler().postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//
//			}
//		}, 3000);
		switch (s) {
		case 0:// chưa tải
			progressbar.setVisibility(View.INVISIBLE);
			textStatus.setVisibility(View.INVISIBLE);
			rowView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.dialog_new);
					dialog.getWindow().getAttributes().width = LayoutParams.FILL_PARENT;
					dialog.getWindow().getAttributes().height = LayoutParams.WRAP_CONTENT;

					bt_read = (Button) dialog.findViewById(R.id.btn1);
					bt_read.setVisibility(View.INVISIBLE);

					bt_read_load = (Button) dialog.findViewById(R.id.btn2);
					bt_read_load.setText(DOWN_READ);
					bt_read_load.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// Messenger messenger = new Messenger(handler);
//							Intent intent = new Intent(context,
//									DownloadService.class);
//							int[] valuesService = {
//									chapters.get(position).getId(),
//									chapters.get(position).getComicId(),
//									chapters.get(position)
//											.getTotalPageDownloaded() };
//							intent.putExtra("chapter_DOWNLOAD", valuesService);
//							intent.putExtra("titleChapter",
//									chapters.get(position).getTitle());
//							context.startService(intent);
							Intent mainIntent = new Intent(context,
									ReadingViewActivity.class);
							int[] values = { position + 1,
									chapters.get(position).getId(),
									chapters.get(position).getTotalPage(),
									chapters.get(position).getComicId() };
							Log.d("doc truc tiep", values.toString());
							mainIntent.putExtra("page", values);
							context.startActivity(mainIntent);

						}
					});

					bt_cancel = (Button) dialog.findViewById(R.id.bt_cancel);
					bt_cancel.setText(CANCEL);
					bt_cancel.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});

					bt_download = (Button) dialog.findViewById(R.id.btn3);
					bt_download.setText(DOWNLOAD);
					bt_download.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context,
									DownloadService.class);
							int[] values = { chapters.get(position).getId(),
									chapters.get(position).getComicId() };
							intent.putExtra("chapterId", values);
							context.startService(intent);
							Log.d("chapterlist addapter", "" + values + "...."
									+ s);
							s = 1;
						}
					});
					dialog.show();
				}
			});
			break;
		case 1:// đang tải
			progressbar.setVisibility(View.VISIBLE);
			textStatus.setText("Đang tải " + numDownloaded + "/"
					+ chapters.get(position).getTotalPage());
			textStatus.setVisibility(View.VISIBLE);
			rowView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.dialog_new);
					dialog.getWindow().getAttributes().width = LayoutParams.FILL_PARENT;
					dialog.getWindow().getAttributes().height = LayoutParams.WRAP_CONTENT;
					bt_read = (Button) dialog.findViewById(R.id.btn1);
					bt_read.setText(PAUSE);
					bt_read.setOnClickListener(this);

					bt_read_load = (Button) dialog.findViewById(R.id.btn2);
					bt_read_load.setText(READ);
					bt_read_load.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							
						}
					});

					bt_cancel = (Button) dialog.findViewById(R.id.bt_cancel);
					bt_cancel.setText(CANCEL);
					bt_cancel.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});

					bt_download = (Button) dialog.findViewById(R.id.btn3);
					bt_download.setText(DELETE);
					bt_download.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							DBAdapter dbAdapter = new DBAdapter(context);
							Log.d("adapter", "" + dbAdapter.toString());
							dbAdapter.open();
							dbAdapter.deleteChapter(chapters.get(position)
									.getId());
							File file = new File(Environment
									.getExternalStorageDirectory()
									+ "/"
									+ chapters.get(position).getComicId()
									+ "/"
									+ chapters.get(position).getId());
							deleteDirectory(file);
						}
					});
					dialog.show();
				}
			});
			break;
		case 2:// đã tải
			progressbar.setVisibility(View.INVISIBLE);
			textStatus.setText("Đã tải");
			textStatus.setVisibility(View.VISIBLE);
			rowView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.dialog_new);
					dialog.getWindow().getAttributes().width = LayoutParams.FILL_PARENT;
					dialog.getWindow().getAttributes().height = LayoutParams.WRAP_CONTENT;
					bt_read = (Button) dialog.findViewById(R.id.btn1);
					bt_read.setVisibility(View.INVISIBLE);
					bt_read_load = (Button) dialog.findViewById(R.id.btn2);
					bt_read_load.setText(READ);
					bt_read_load.setOnClickListener(this);

					bt_cancel = (Button) dialog.findViewById(R.id.bt_cancel);
					bt_cancel.setText(CANCEL);
					bt_cancel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});

					bt_download = (Button) dialog.findViewById(R.id.btn3);
					bt_download.setText(DELETE);
					bt_download.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							DBAdapter dbAdapter = new DBAdapter(context);
							Log.d("adapter", "" + dbAdapter.toString());
							dbAdapter.open();
							dbAdapter.deleteChapter(chapters.get(position)
									.getId());
							File file = new File(Environment
									.getExternalStorageDirectory()
									+ "/"
									+ chapters.get(position).getComicId()
									+ "/"
									+ chapters.get(position).getId());
							deleteDirectory(file);
						}
					});
					dialog.show();
				}
			});
			break;
		case 3: // tam dung
			progressbar.setVisibility(View.INVISIBLE);
			textStatus.setText("Tạm dừng " + numDownloaded + "/"
					+ chapters.get(position).getTotalPage());
			textStatus.setVisibility(View.VISIBLE);
			rowView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.dialog_new);
					dialog.getWindow().getAttributes().width = LayoutParams.FILL_PARENT;
					dialog.getWindow().getAttributes().height = LayoutParams.WRAP_CONTENT;
					bt_read = (Button) dialog.findViewById(R.id.btn1);
					bt_read.setText(CONTINUE);
					bt_read.setOnClickListener(this);

					bt_read_load = (Button) dialog.findViewById(R.id.btn2);
					bt_read_load.setText(READ);
					bt_read_load.setOnClickListener(this);

					bt_cancel = (Button) dialog.findViewById(R.id.bt_cancel);
					bt_cancel.setText(CANCEL);
					bt_cancel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});

					bt_download = (Button) dialog.findViewById(R.id.btn3);
					bt_download.setText(DELETE);
					bt_download.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							DBAdapter dbAdapter = new DBAdapter(context);
							Log.d("adapter", "" + dbAdapter.toString());
							dbAdapter.open();
							dbAdapter.deleteChapter(chapters.get(position)
									.getId());
							File file = new File(Environment
									.getExternalStorageDirectory()
									+ "/"
									+ chapters.get(position).getComicId()
									+ "/"
									+ chapters.get(position).getId());
							deleteDirectory(file);
						}
					});
					dialog.show();
				}
			});
			break;
		default:
			dbAdapter.close();
		}

		// }
		//
		// }, 0, 100);

		return rowView;
	}

	private boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

}
