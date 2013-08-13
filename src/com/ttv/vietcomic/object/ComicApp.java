package com.ttv.vietcomic.object;

public class ComicApp {
	private int id;
	private String name;
	private String description;
	private String link;
	private String image;
	private String version;
	private int date;

	public ComicApp(int id, String name, String description, String link,
			String image, String version, int date) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.link = link;
		this.image = image;
		this.version = version;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

}
