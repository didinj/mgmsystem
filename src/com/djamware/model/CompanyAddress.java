package com.djamware.model;

import javax.persistence.Id;

import com.googlecode.objectify.Key;

public class CompanyAddress {
	@Id
	Long id;
	Key<Company> company;
	String address;
	String npwp;
	String company_city;
	String company_province;
	String company_phone;

	public CompanyAddress() {
	}

	public CompanyAddress(Key<Company> company, String address, String npwp,
			String company_city, String company_province, String company_phone) {
		this();
		this.company = company;
		this.address = address;
		this.npwp = npwp;
		this.company_city = company_city;
		this.company_province = company_province;
		this.company_phone = company_phone;
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

	public String getCompany_city() {
		return company_city;
	}

	public void setCompany_city(String company_city) {
		this.company_city = company_city;
	}

	public String getCompany_province() {
		return company_province;
	}

	public void setCompany_province(String company_province) {
		this.company_province = company_province;
	}

	public String getCompany_phone() {
		return company_phone;
	}

	public void setCompany_phone(String company_phone) {
		this.company_phone = company_phone;
	}

	public Long getId() {
		return id;
	}
	
}
