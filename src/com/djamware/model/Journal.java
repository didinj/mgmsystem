package com.djamware.model;

import java.util.Date;

import javax.persistence.Id;

public class Journal {
	@Id
	Long id;
	Date tgl;
	String itemnbr;
	String itemname;
	String itemdetail;
	String vouchernbr;
	Double debit; 
	String user;
	String departement;
	String unitproject;
	public Journal() {
	}
	public Journal(Date tgl, String itemnbr, String itemname,
			String itemdetail, String vouchernbr, Double debit, String user,
			String departement, String unitproject) {
		this();
		this.tgl = tgl;
		this.itemnbr = itemnbr;
		this.itemname = itemname;
		this.itemdetail = itemdetail;
		this.vouchernbr = vouchernbr;
		this.debit = debit;
		this.user = user;
		this.departement = departement;
		this.unitproject = unitproject;
	}
	public Date getTgl() {
		return tgl;
	}
	public void setTgl(Date tgl) {
		this.tgl = tgl;
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
	public String getItemdetail() {
		return itemdetail;
	}
	public void setItemdetail(String itemdetail) {
		this.itemdetail = itemdetail;
	}
	public String getVouchernbr() {
		return vouchernbr;
	}
	public void setVouchernbr(String vouchernbr) {
		this.vouchernbr = vouchernbr;
	}
	public Double getDebit() {
		return debit;
	}
	public void setDebit(Double debit) {
		this.debit = debit;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getDepartement() {
		return departement;
	}
	public void setDepartement(String departement) {
		this.departement = departement;
	}
	public String getUnitproject() {
		return unitproject;
	}
	public void setUnitproject(String unitproject) {
		this.unitproject = unitproject;
	}
	public Long getId() {
		return id;
	}
	
}
