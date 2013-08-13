package com.ttv.vietcomic;

public class Chapters {
	private String title;
	private String id;
	

	public Chapters(String title, String id) {
		super();
		this.title = title;
		this.id = id;
	}

	String getId() {
		return id;
	}

	void setId(String id) {
		this.id = id;
	}

	String getTitle() {
		return "Tập "+title;
	}

	void setTitle(String title) {
		this.title = title;
	}
}
