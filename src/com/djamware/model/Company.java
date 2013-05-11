package com.djamware.model;

import java.util.Date;

import javax.persistence.Id;

public class Company {
	@Id
	Long id;
	String unit_nbr;
	String company_name;
	String company_initial;
	String company_address;
	String company_city;
	String company_province;
	String company_phone;
	Date createdate;
	Date updatedate;

	public Company() {
	}

	public Company(String unit_nbr, String company_name,String company_initial,
			String company_address, String company_city,
			String company_province, String company_phone, Date createdate,
			Date updatedate) {
		this();
		this.unit_nbr = unit_nbr;
		this.company_name = company_name;
		this.company_initial = company_initial;
		this.company_address = company_address;
		this.company_city = company_city;
		this.company_province = company_province;
		this.company_phone = company_phone;
		this.createdate = createdate;
		this.updatedate = updatedate;
	}

	public String getUnit_nbr() {
		return unit_nbr;
	}

	public void setUnit_nbr(String unit_nbr) {
		this.unit_nbr = unit_nbr;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getCompany_initial() {
		return company_initial;
	}

	public void setCompany_initial(String company_initial) {
		this.company_initial = company_initial;
	}

	public String getCompany_address() {
		return company_address;
	}

	public void setCompany_address(String company_address) {
		this.company_address = company_address;
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

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}

	public Long getId() {
		return id;
	}

}