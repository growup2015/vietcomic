package com.ttv.vietcomic.object;

public class ComicChapter {

	private int id;
	private int comicId;
	private String title;
	private int date;
	private int status;
	private int totalPage;
	private int totalPageDownloaded;

	public ComicChapter(int id, int comicId, String title, int date,
			int status, int totalpage, int dowloaded) {
		super();
		this.id = id;
		this.comicId = comicId;
		this.title = title;
		this.date = date;
		this.status = status;
		this.totalPage = totalpage;
		this.totalPageDownloaded = dowloaded;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getComicId() {
		return comicId;
	}

	public void setComicId(int comicId) {
		this.comicId = comicId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalPageDownloaded() {
		return totalPageDownloaded;
	}

	public void setTotalPageDownloaded(int totalPageDownloaded) {
		this.totalPageDownloaded = totalPageDownloaded;
	}

}
