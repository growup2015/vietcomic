package com.ttv.vietcomic.object;

public class Category {

	private int id;
	private String name;
	private String picture;
	private int date;

	public Category(int id, String name, String picture, int date) {
		super();
		this.id = id;
		this.name = name;
		this.picture = picture;
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

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

}
