package com.djamware.model;

import javax.persistence.Id;

import com.googlecode.objectify.Key;

public class ItemDetail {
	@Id
	Long id;
	Key<Items> items;
	String itemdetaildesc;

	public ItemDetail() {
	}

	public ItemDetail(Key<Items> items, String itemdetaildesc) {
		this();
		this.items = items;
		this.itemdetaildesc = itemdetaildesc;
	}

	public Key<Items> getItems() {
		return items;
	}

	public void setItems(Key<Items> items) {
		this.items = items;
	}

	public String getItemdetaildesc() {
		return itemdetaildesc;
	}

	public void setItemdetaildesc(String itemdetaildesc) {
		this.itemdetaildesc = itemdetaildesc;
	}

	public Long getId() {
		return id;
	}

}
