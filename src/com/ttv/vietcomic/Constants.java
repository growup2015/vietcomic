package com.ttv.vietcomic;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Environment;

public class Constants {

	public static final String APP_NAME = "Truyá»‡n Audio";
	public static final String APP_HEADER = "AUDIO-TTV";
	public static String URL_SHARE = "http://10h.vn/download.jsp?file=audio.ttv.v1.0.apk";
	public static String NEW_VERSION = null;
	public static final String TRUYEN_DOWNLOAD_FOLDER = Environment
			.getExternalStorageDirectory() + "/TruyenTranh/";

	public static final String URL_MORE_APP = "http://10h.vn/more_app/more_app_android_ttv.txt";
	public static final String URL_GET_USER_INFO = "http://api.10h.vn:8080/api-users.jsp?ttv-auth=HjuGc9Jy1OkW1hBcdGpLMg==&AppHeader="
			+ APP_HEADER
			+ "&osPlatform=2&providerName=audio-a-mobivas&appVersion=1.0&method=userInfo&username=";

	public static final String QUERY_STORY_NEWEST = "?action=getContentNew&appId=8&limit=5";
	public static final String QUERY_STORY_CATEGORY = "?action=getCategory&appId=8&type=5";
	public static final String QUERY_STORY_LIST = "?action=getContentDetail&appId=8&content_id=";
	public static final String QUERY_STORY_INC_LISTEN = "?action=updateDownload&content_id=";
	public static final String QUERY_STORY_INC_DOWNLOAD = "?action=updateListen&content_id=";
	public static final String QUERY_STORY_SEARCH = "?action=getSearch&appId=8&limit=LIMIT_NUM&catId=0&page=PAGE_NUM&keyword=";
	/*
	 * - Chi tiet & Danh sach file audio:
	 * ?action=getContentDetail&appId=8&content_id=68 - Tang luot tai:
	 * ?action=updateDownload&content_id=1 - Tang luot nghe:
	 * ?action=updateListen&content_id=1 - Tim kiem:
	 * ?action=getSearch&appId=8&limit=4&catId=0&page=1&keyword=li di
	 */

	public static ReadingViewActivity readingViewActivity = null;
	public static Activity searchActivity = null;

	// Story Downloading
	public static JSONObject chapterDownloading = null;
	public static int chapterDownloadingPecent = 0;

	// Story Playing
	public static MediaPlayer mediaPlayer = null;
	public static int mediaFileLengthInMilliseconds = 0;
	public static int storyIndexPlaying = 0;
	public static long storyIdPlaying = 0;
	public static String storyNamePlaying = "";
	public static String chapterNamePlaying = "";
	public static String storyUrlPlaying = "";
	public static JSONObject chapterPlaying = null;
	public static JSONObject storyPlaying = null;

	public static final int ROWS_PER_PAGE = 15;
	public static final int SHOW = 0;
	public static final int DISMISS = 1;

	public static final int BTN_PLAY = 1;
	public static final int BTN_LOADING = 2;
	public static final int BTN_STOP = 3;

	public static final int PLAYER_STATUS_STOP = 0;
	public static final int PLAYER_STATUS_PLAY = 1;
	public static final int PLAYER_STATUS_PAUSE = 2;
	public static int PLAYER_STATUS = 0;

	public static int RESULT_OK = 0;
	public static int RESULT_CANCEL = 1;
	public static int RESULT_ERROR = 2;
	public static int RESULT_START = 3;
	public static int RESULT_FINISH = 4;
	public static int RESULT_PROGRESS = 5;
	public static int RESULT_DOWNLOADING = 6;
	public static int RESULT_EXISTS = 7;

	public static int iAlphaDown = 90;
	public static int iAlphaUp = 255;

	// File Type
	public static final int FILE_TYPE_AUDIO = 0;
	public static final int FILE_TYPE_APK = 1;

	public static final String SEARCH_KEYWORD = "SEARCH_KEYWORD";
	public static final String ALERT_NETWORK = "Káº¿t ná»‘i bá»‹ giÃ¡n Ä‘oáº¡n!";
	public static final String FILE_CHARGING = "charg";
	public static final String STATUS_CHARGING = "chargs";
	public static final String LOAD_MORE = "Xem thÃªm";
	public static final String LOADING_MORE = "Äang táº£i thÃªm ...";
	public static final String WAITING = "Äang táº£i. Vui lÃ²ng Ä‘á»£i ...";

	public static String searchKeyword = "";

	public static String[] SMS_COST = { "500Ä‘", "1000Ä‘", "2000Ä‘", "3000Ä‘",
			"4000Ä?", "5000Ä?", "10000Ä?", "15000Ä?" };

	public static ArrayList<JSONObject> mThumbIds = null;
	public static JSONObject catLibrary = new JSONObject();
	public static JSONObject catMoreApp = new JSONObject();
	public static final int ID_CATEGORY_LIBRARY = -1;
	public static final int ID_CATEGORY_MORE_APP = -2;

	public static String POLICY_CONTENT = "Vui lÃ?ng Ä?á??c ká?? Ä?iá??u khoáº?n sá?? dá??ng trÆ?á??c khi báº?n tiáº?n hÃ?nh táº?i, cÃ?i Ä?áº?t, sá?? dá??ng báº?t ká?? tÃ?nh nÄ?ng nÃ?o cá??a á??ng dá??ng Truyá??n Audio.\n\n"
			+ "+ á??ng dá??ng Truyá??n Audio bao gá??m cÃ?c tÃ?nh nÄ?ng nghe truyá??n online, táº?i truyá??n vá?? Ä?iá??n thoáº?i.\n\n"
			+ "+ PhÃ? sá?? dá??ng á??ng dá??ng: SMS_COST.\nThá??i háº?n sá?? dá??ng: NUM_DAY ngÃ?y/1 kÃ?ch hoáº?t (hoáº?c gia háº?n)\n\n"
			+ "+ Ä?iá??u khoáº?n sá?? dá??ng nÃ?y cÃ? thá?? Ä?Æ?á??c cáº?p nháº?t thÆ?á??ng xuyÃªn. PhiÃªn báº?n cáº?p nháº?t sáº? thay tháº? cho cÃ?c quy Ä?á??nh vÃ? Ä?iá??u kiá??n trong thá??a thuáº?n cá??a cÃ?c phiÃªn báº?n trÆ?á??c.\n\n"
			+ "+ Khi Ä?á??ng Ã? sá?? dá??ng á??ng dá??ng Truyá??n Audio cÃ? nghÄ?a lÃ? báº?n Ä?Ã? Ä?á??ng Ã? vá??i táº?t cáº? cÃ?c Ä?iá??u khoáº?n sá?? dá??ng cá??a á??ng dá??ng. Sáº? khÃ?ng cÃ? báº?t ká?? má??t khiáº?u kiá??n, khiáº?u náº?i nÃ?o Ä?á??i vá??i á??ng dá??ng cÅ?ng nhÆ? cÃ?c tÃ?nh nÄ?ng, ná??i dung cá??a á??ng dá??ng. TrÆ?á??ng há??p báº?n khÃ?ng Ä?á??ng Ã? vá??i báº?t ká?? Ä?iá??u khoáº?n sá?? dá??ng nÃ?o cá??a á??ng dá??ng, báº?n vui lÃ?ng khÃ?ng táº?i, cÃ?i Ä?áº?t vÃ? sá?? dá??ng á??ng dá??ng hoáº?c thÃ?o gá?? á??ng dá??ng ra khá??i thiáº?t bá?? cá??a báº?n.\n\n";

	public static final String URL_API = "http://kenhkiemtien.com/kkt_api/comicAPI.php";
	public static final String COMIC_DETAIL_CHAPTER = URL_API
			+ "?action=getComicDetailAndChapter&appId=64&contentId=";
	public static final String CHAPTER_FILE = URL_API
			+ "?action=getComicFile&get_by=2&chapterId=";
	// bao gồm object "comic" và mảng "chapters"
	public static final String TAG_COMIC = "comic";
	public static final String TAG_CHAPTER = "chapters";
	public static final String T_CHAPTER_TITLE = "title";
	public static final String C_TITLE = "title";
	public static final String C_AUTHOR = "author";
	public static final String C_CHAPTER = "c_chapter";
	public static final String C_STATUS = "STATUS";
	public static final String C_VIEWS = "hit";
	public static final String C_IMAGE = "image";

}
