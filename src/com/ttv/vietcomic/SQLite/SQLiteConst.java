package com.ttv.vietcomic.SQLite;

public class SQLiteConst {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Database_Comic";
	// tables in database
	public static final String TABLE_COMIC = "c_comic";
	public static final String TABLE_COMIC_CHAPTER = "c_comic_chapter";
	public static final String TABLE_BOOKMARK = "c_read_bookmark";
	public static final String TABLE_CATEGORY = "c_category";
	public static final String TABLE_COMIC_INDEX = "c_comic_index";
	public static final String TABLE_APP = "c_app";

	// table comic
	public static final String C_COMIC_ID = "id";
	public static final String C_COMIC_TITLE = "title";
	public static final String C_COMIC_IMAGE = "image";
	public static final String C_COMIC_C_CHAPTER = "c_chapter";
	public static final String C_COMIC_AUHTOR = "author";
	public static final String C_COMIC_CONTENT = "content";
	public static final String C_COMIC_TAGS = "tags";
	public static final String C_COMIC_HIT = "isHot";
	public static final String C_COMIC_STATUS = "status";
	public static final String C_COMIC_DATE = "create_date";

	// table comic chapter
	public static final String C_COMIC_CHAPTER_ID = "id";
	public static final String C_COMIC_CHAPTER_COMIC_ID = "comic_id";
	public static final String C_COMIC_CHAPTER_TITLE = "title";
	public static final String C_COMIC_CHAPTER_DATE = "create_date";
	public static final String C_COMIC_CHAPTER_STATUS = "status";
	public static final String C_COMIC_CHAPTER_TOTAL_PAGE = "totalPage";
	public static final String C_COMIC_CHAPTER_TOTAL_PAGE_DOWNLOADED = "totalPageDownloaded";

	// table bookmark
	public static final String C_BOOKMARK_CHAPTER_ID = "chapterId";
	public static final String C_BOOKMARK_PAGE = "page";
	// table category

	public static final String C_CATEGORY_ID = "id";
	public static final String C_CATEGORY_NAME = "name";
	public static final String C_CATEGORY_PICTURE = "picture";
	public static final String C_CATEGORY_DATE = "create_date";
	// table comic index
	public static final String C_COMIC_INDEX_ID = "id";
	public static final String C_COMIC_INDEX_TITLE = "title";
	public static final String C_COMIC_INDEX_IMAGE = "image";
	public static final String C_COMIC_INDEX_CHAPTER = "c_chapter";
	public static final String C_COMIC_INDEX_AUTHOR = "author";
	public static final String C_COMIC_INDEX_CONTENT = "content";
	public static final String C_COMIC_INDEX_TAGS = "tags";
	public static final String C_COMIC_INDEX_HIT = "isHot";
	public static final String C_COMIC_INDEX_STATUS = "status";
	public static final String C_COMIC_INDEX_DATE = "create_date";
	// table app
	public static final String C_APP_ID = "id";
	public static final String C_APP_NAME = "name";
	public static final String C_APP_DESCRIPTION = "description";
	public static final String C_APP_LINK = "link";
	public static final String C_APP_IMAGE = "image";
	public static final String C_APP_VERSION = "version";
	public static final String C_APP_DATE = "create_date";

	// / sql command create tables
	public static final String CREATE_TABLE_COMIC = "create table "
			+ TABLE_COMIC + "(" + C_COMIC_ID + " integer primary key, "
			+ C_COMIC_TITLE + " text not null default '', " + C_COMIC_IMAGE
			+ " text not null default '', " + C_COMIC_C_CHAPTER
			+ " integer not null default 0, " + C_COMIC_AUHTOR
			+ " text not null default '', " + C_COMIC_CONTENT
			+ " text not null default '', " + C_COMIC_TAGS
			+ " text not null default '', " + C_COMIC_HIT
			+ " integer not null default 0, " + C_COMIC_STATUS
			+ " integer not null default 0, " + C_COMIC_DATE
			+ " integer not null default 0)";

	public static final String CREATE_TABLE_COMIC_CHAPTER = "create table "
			+ TABLE_COMIC_CHAPTER + "(" + C_COMIC_CHAPTER_ID
			+ " integer primary key, " + C_COMIC_CHAPTER_COMIC_ID
			+ " integer not null default 0, " + C_COMIC_CHAPTER_TITLE
			+ " text not null default '', " + C_COMIC_CHAPTER_DATE
			+ " integer not null default 0, " + C_COMIC_CHAPTER_STATUS
			+ " integer not null default -1, " + C_COMIC_CHAPTER_TOTAL_PAGE
			+ " integer not null default 0, "
			+ C_COMIC_CHAPTER_TOTAL_PAGE_DOWNLOADED
			+ " integer not null default 0)";

	public static final String CREATE_TABLE_BOOKMARK = "create table "
			+ TABLE_BOOKMARK + "(" + C_BOOKMARK_CHAPTER_ID + " integer, "
			+ C_BOOKMARK_PAGE + " integer)";

	public static final String CREATE_TABLE_CATEGORY = "create table "
			+ TABLE_CATEGORY + "(" + C_CATEGORY_ID
			+ " integer primary key, " + C_CATEGORY_NAME
			+ " text, " + C_CATEGORY_PICTURE + " text, " + C_CATEGORY_DATE
			+ " integer)";

	public static final String CREATE_TABLE_COMIC_INDEX = "create table "
			+ TABLE_COMIC_INDEX + "(" + C_COMIC_INDEX_ID
			+ " integer primary key autoincrement, " + C_COMIC_INDEX_TITLE
			+ " text, " + C_COMIC_INDEX_IMAGE + " text, "
			+ C_COMIC_INDEX_CHAPTER + " integer, " + C_COMIC_INDEX_AUTHOR
			+ " text, " + C_COMIC_INDEX_CONTENT + " text, "
			+ C_COMIC_INDEX_TAGS + " text, " + C_COMIC_INDEX_HIT + " integer, "
			+ C_COMIC_INDEX_STATUS + " integer, " + C_COMIC_INDEX_DATE
			+ " integer)";

	public static final String CREATE_TABLE_APP = "create table " + TABLE_APP
			+ "(" + C_APP_ID + " integer primary key, "
			+ C_APP_NAME + " text, " + C_APP_DESCRIPTION + " text, "
			+ C_APP_LINK + " text, " + C_APP_IMAGE + " text, " + C_APP_VERSION
			+ " text, " + C_APP_DATE + " integer)";
}
