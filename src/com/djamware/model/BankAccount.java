package com.djamware.model;

import java.util.Date;

import javax.persistence.Id;

public class BankAccount {
	@Id
	Long id;
	String bankname;
	String accountnbr;
	String accountname;
	Date acc_createdate;

	public BankAccount() {
	}

	public BankAccount(String bankname, String accountnbr, String accountname,
			Date acc_createdate) {
		this();
		this.bankname = bankname;
		this.accountnbr = accountnbr;
		this.accountname = accountname;
		this.acc_createdate = acc_createdate;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getAccountnbr() {
		return accountnbr;
	}

	public void setAccountnbr(String accountnbr) {
		this.accountnbr = accountnbr;
	}

	public String getAccountname() {
		return accountname;
	}

	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}

	public Date getAcc_createdate() {
		return acc_createdate;
	}

	public void setAcc_createdate(Date acc_createdate) {
		this.acc_createdate = acc_createdate;
	}

	public Long getId() {
		return id;
	}

}
