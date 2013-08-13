package com.ttv.vietcomic.object;

public class ComicIndex {
	private int id;
	private String title;
	private String image;
	private int chapter;
	private String author;
	private String content;
	private String tags;
	private int hit;
	private int status;
	private int date;

	public ComicIndex(int id, String title, String image, int chapter,
			String author, String content, String tags, int hit, int status,
			int date) {
		super();
		this.id = id;
		this.title = title;
		this.image = image;
		this.chapter = chapter;
		this.author = author;
		this.content = content;
		this.tags = tags;
		this.hit = hit;
		this.status = status;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getChapter() {
		return chapter;
	}

	public void setChapter(int chapter) {
		this.chapter = chapter;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}
}
