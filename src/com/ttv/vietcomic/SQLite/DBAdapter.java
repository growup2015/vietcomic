package com.ttv.vietcomic.SQLite;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ttv.vietcomic.object.Comic;

public class DBAdapter {
	private Context mContext;
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDB;

	public static final int EXIST_RECORD = -2;

	public DBAdapter(Context context) {
		this.mContext = context;
	}

	public DBAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mContext, SQLiteConst.DATABASE_NAME,
				null, SQLiteConst.DATABASE_VERSION);
		Log.d("truoc", "" + mDbHelper.toString());
		try {
			mDB = this.mDbHelper.getWritableDatabase();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return this;
	}

	public void closeCursor(Cursor c) {
		try {
			c.close();
		} catch (Exception e) {
		}
	}

	public void close() {
		try {
			mDB.close();
			mDbHelper.close();
		} catch (Exception e) {
		}
	}

	// COMIC TABLE
	public long addComic(Comic comic) {
		ContentValues values = new ContentValues();
		values.put(SQLiteConst.C_COMIC_ID, comic.getId());
		values.put(SQLiteConst.C_COMIC_TITLE, comic.getTitle());
		values.put(SQLiteConst.C_COMIC_IMAGE, comic.getImage());
		values.put(SQLiteConst.C_COMIC_C_CHAPTER, comic.getChapter());
		values.put(SQLiteConst.C_COMIC_AUHTOR, comic.getAuthor());
		values.put(SQLiteConst.C_COMIC_CONTENT, comic.getContent());
		values.put(SQLiteConst.C_COMIC_TAGS, comic.getTags());
		values.put(SQLiteConst.C_COMIC_HIT, comic.getHit());
		values.put(SQLiteConst.C_COMIC_STATUS, comic.getStatus());
		values.put(SQLiteConst.C_COMIC_DATE, comic.getDate());
		Log.d("exception", "" + values);
		long res = 0;
		Comic cm = getComic(comic.getId());
		if (cm == null) {
			mDB.insert(SQLiteConst.TABLE_COMIC, null, values);
		} else {
			// đã tồn tại trong list truyện
			res = EXIST_RECORD;
		}
		return res;
	}

	public Comic getComic(int comicId) {
		Comic comic = null;
		Cursor cursor = null;
		try {
			cursor = mDB.query(SQLiteConst.TABLE_COMIC, null,
					SQLiteConst.C_COMIC_ID + "=?",
					new String[] { String.valueOf(comicId) }, null, null, null);
			while (cursor.moveToNext()) {

				new Comic(cursor.getInt(0), cursor.getString(1),
						cursor.getString(2), cursor.getInt(3),
						cursor.getString(4), cursor.getString(5),
						cursor.getString(6), cursor.getInt(7),
						cursor.getInt(8), cursor.getInt(9));
			}
			cursor.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return comic;
	}

	public ArrayList<Comic> getAllComic() {
		ArrayList<Comic> list = new ArrayList<Comic>();
		String query = "SELECT * FROM " + SQLiteConst.TABLE_COMIC;
		Cursor cursor = mDB.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			do {
				Comic comic = new Comic(cursor.getInt(0), cursor.getString(1),
						cursor.getString(2), cursor.getInt(3),
						cursor.getString(4), cursor.getString(5),
						cursor.getString(6), cursor.getInt(7),
						cursor.getInt(8), cursor.getInt(9));
				// Adding contact to list
				list.add(comic);
			} while (cursor.moveToNext());
		}
		return list;
	}

	// delete comic
	public void deleteComic(int comicId) {
		try {
			mDB.delete(SQLiteConst.TABLE_COMIC, SQLiteConst.C_COMIC_ID + "="
					+ comicId, null);
		} catch (Exception e) {
		}

	}

	// BOOKMARK TABLE
	public int getBookmark(int chapterId) {
		int page = 0;
		Cursor cursor = null;
		try {
			cursor = mDB.query(SQLiteConst.TABLE_BOOKMARK,
					new String[] { SQLiteConst.C_BOOKMARK_PAGE },
					SQLiteConst.C_BOOKMARK_CHAPTER_ID + "=?",
					new String[] { String.valueOf(chapterId) }, null, null,
					null);
			while (cursor.moveToNext()) {
				page = cursor.getInt(0);
			}
		} catch (Exception e) {
		}
		return page;
	}

	public long createBookmark(int chapterId) {
		ContentValues values = new ContentValues();
		values.put(SQLiteConst.C_BOOKMARK_CHAPTER_ID, chapterId);
		values.put(SQLiteConst.C_BOOKMARK_PAGE, 1);// tạo mới bookmark thì giá
		return mDB.insert(SQLiteConst.TABLE_BOOKMARK, null, values);
	}

	public long updateBookmark(int chapterId, int currentPage) {
		ContentValues value = new ContentValues();
		value.put(SQLiteConst.C_BOOKMARK_PAGE, currentPage);
		return mDB.update(SQLiteConst.TABLE_BOOKMARK, value,
				SQLiteConst.C_BOOKMARK_CHAPTER_ID + "=" + chapterId, null);

	}

	// COMIC CHAPTER
	public long createComicChapter(int comicId, String title, int date,
			int status, int totalPage, int totalPageDownloaded) {
		ContentValues values = new ContentValues();
		values.put(SQLiteConst.C_COMIC_CHAPTER_COMIC_ID, comicId);
		values.put(SQLiteConst.C_COMIC_CHAPTER_TITLE, title);
		values.put(SQLiteConst.C_COMIC_CHAPTER_DATE, date);
		values.put(SQLiteConst.C_COMIC_CHAPTER_STATUS, status);
		values.put(SQLiteConst.C_COMIC_CHAPTER_TOTAL_PAGE, totalPage);
		values.put(SQLiteConst.C_COMIC_CHAPTER_TOTAL_PAGE_DOWNLOADED, totalPageDownloaded);
		long res;
		if (comicId != getComicChapter(comicId))
			res = mDB.insert(SQLiteConst.TABLE_COMIC_CHAPTER, null, values);
		else
			res = EXIST_RECORD;
		return res;
	}

	public int getComicChapterStatus(int comicIn) {
		int status = 0;
		Cursor cursor = null;
		try {
			cursor = mDB.query(SQLiteConst.TABLE_COMIC_CHAPTER,
					new String[] { SQLiteConst.C_COMIC_CHAPTER_STATUS },
					SQLiteConst.C_COMIC_CHAPTER_COMIC_ID + "=" + comicIn, null,
					null, null, null);
			while (cursor.moveToNext()) {
				status = cursor.getInt(4);
			}
			cursor.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return status;
	}

	public int getComicChapter(int comicIn) {
		int comicId = 0;
		Cursor cursor = null;
		try {
			cursor = mDB.query(SQLiteConst.TABLE_COMIC_CHAPTER,
					new String[] { SQLiteConst.C_COMIC_CHAPTER_STATUS },
					SQLiteConst.C_COMIC_CHAPTER_COMIC_ID + "=" + comicIn, null,
					null, null, null);
			while (cursor.moveToNext()) {
				comicId = cursor.getInt(0);
			}
			cursor.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return comicId;
	}

	public int updateCStatus(int statut, int comicID) {
		ContentValues values = new ContentValues();
		values.put(SQLiteConst.TABLE_COMIC_CHAPTER, statut);
		return mDB.update(SQLiteConst.TABLE_COMIC_CHAPTER, values,
				SQLiteConst.C_COMIC_CHAPTER_COMIC_ID + "=" + statut, null);
	}

	public int updateChapterDownloaded(int pageDownloaded, int id) {
		ContentValues values = new ContentValues();
		values.put(SQLiteConst.TABLE_COMIC_CHAPTER, pageDownloaded);
		return mDB.update(SQLiteConst.TABLE_COMIC_CHAPTER, values,
				SQLiteConst.C_COMIC_CHAPTER_ID + "=?",
				new String[] { String.valueOf(id) });
	}

	public void deleteChapter(int chapterId) {
		try {
			mDB.delete(SQLiteConst.TABLE_COMIC_CHAPTER,
					SQLiteConst.C_COMIC_CHAPTER_ID + "=" + chapterId, null);
		} catch (Exception e) {
		}
	}

	// CATEGORY TABLE
	// COMIC INDEX TABLE
	// COMIC APP
	// -----------------------------------------------------------------------
	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQLiteConst.CREATE_TABLE_COMIC);
			db.execSQL(SQLiteConst.CREATE_TABLE_COMIC_CHAPTER);
			db.execSQL(SQLiteConst.CREATE_TABLE_BOOKMARK);
			db.execSQL(SQLiteConst.CREATE_TABLE_CATEGORY);
			db.execSQL(SQLiteConst.CREATE_TABLE_COMIC_INDEX);
			db.execSQL(SQLiteConst.CREATE_TABLE_APP);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + SQLiteConst.TABLE_COMIC);
			db.execSQL("DROP TABLE IF EXISTS "
					+ SQLiteConst.CREATE_TABLE_COMIC_CHAPTER);
			db.execSQL("DROP TABLE IF EXISTS "
					+ SQLiteConst.CREATE_TABLE_BOOKMARK);
			db.execSQL("DROP TABLE IF EXISTS "
					+ SQLiteConst.CREATE_TABLE_CATEGORY);
			db.execSQL("DROP TABLE IF EXISTS "
					+ SQLiteConst.CREATE_TABLE_COMIC_INDEX);
			db.execSQL("DROP TABLE IF EXISTS " + SQLiteConst.CREATE_TABLE_APP);
			onCreate(db);
		}
	}
	

}
