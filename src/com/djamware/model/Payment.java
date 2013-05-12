package com.djamware.model;

import java.util.Date;

import javax.persistence.Id;

import com.googlecode.objectify.Key;

public class Payment {
	@Id
	Long id;
	Key<BankAccount> bankAccount;
	Date receive_date;
	Key<Invoice> invoice;
	Float receive_amount;
	Float gap;
	String notes;

	public Payment() {
	}

	public Payment(Key<BankAccount> bankAccount, Date receive_date,
			Key<Invoice> invoice, Float receive_amount, Float gap, String notes) {
		this();
		this.bankAccount = bankAccount;
		this.receive_date = receive_date;
		this.invoice = invoice;
		this.receive_amount = receive_amount;
		this.gap = gap;
		this.notes = notes;
	}

	public Key<BankAccount> getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(Key<BankAccount> bankAccount) {
		this.bankAccount = bankAccount;
	}

	public Date getReceive_date() {
		return receive_date;
	}

	public void setReceive_date(Date receive_date) {
		this.receive_date = receive_date;
	}

	public Key<Invoice> getInvoice() {
		return invoice;
	}

	public void setInvoice(Key<Invoice> invoice) {
		this.invoice = invoice;
	}

	public Float getReceive_amount() {
		return receive_amount;
	}

	public void setReceive_amount(Float receive_amount) {
		this.receive_amount = receive_amount;
	}

	public Float getGap() {
		return gap;
	}

	public void setGap(Float gap) {
		this.gap = gap;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Long getId() {
		return id;
	}

}
