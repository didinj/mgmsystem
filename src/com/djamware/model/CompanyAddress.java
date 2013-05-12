package com.djamware.model;

import javax.persistence.Id;

import com.googlecode.objectify.Key;

public class CompanyAddress {
	@Id
	Long id;
	Key<Company> company;
	String address;
	String npwp;

	public CompanyAddress() {
	}

	public CompanyAddress(Key<Company> company, String address, String npwp) {
		this();
		this.company = company;
		this.address = address;
		this.npwp = npwp;
	}

	public Key<Company> getCompany() {
		return company;
	}

	public void setCompany(Key<Company> company) {
		this.company = company;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNpwp() {
		return npwp;
	}

	public void setNpwp(String npwp) {
		this.npwp = npwp;
	}

	public Long getId() {
		return id;
	}

}
