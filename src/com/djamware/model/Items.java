package com.djamware.model;

import javax.persistence.Id;

public class Items {
	@Id
	Long id;
	String itemnbr;
	String itemname;

	public Items() {
	}

	public Items(String itemnbr, String itemname) {
		this();
		this.itemnbr = itemnbr;
		this.itemname = itemname;
	}

	public String getItemnbr() {
		return itemnbr;
	}

	public void setItemnbr(String itemnbr) {
		this.itemnbr = itemnbr;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public Long getId() {
		return id;
	}

}
