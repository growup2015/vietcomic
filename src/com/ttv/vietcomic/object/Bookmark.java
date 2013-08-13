package com.ttv.vietcomic.object;

public class Bookmark {
	private int chapterId;
	private int page;
	public Bookmark(int chapterId, int page) {
		super();
		this.chapterId = chapterId;
		this.page = page;
	}
	public int getChapterId() {
		return chapterId;
	}
	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
}
