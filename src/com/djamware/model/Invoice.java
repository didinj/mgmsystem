package com.djamware.model;

import java.util.Date;

import javax.persistence.Id;

import com.googlecode.objectify.Key;

public class Invoice {
	@Id
	Long id;
	Key<CompanyAddress> compaddr;
	String kwitansi_nbr;
	String invoice_nbr;
	String inv_period;
	Date inv_startdate;
	Date inv_enddate;
	Date create_date;
	Date due_date;
	Float fee_management;
	boolean ppn_10;
	String no_faktur_pajak;
	boolean pph_23;
	Float total_bill;
	Key<BankAccount> bankAccount;
	boolean is_confirm;

	public Invoice() {
	}

	public Invoice(Key<CompanyAddress> compaddr, String kwitansi_nbr,
			String invoice_nbr, String inv_period, Date inv_startdate,
			Date inv_enddate, Date create_date, Date due_date,
			Float fee_management, boolean ppn_10, String no_faktur_pajak, boolean pph_23,
			Float total_bill, Key<BankAccount> bankAccount, boolean is_confirm) {
		this();
		this.compaddr = compaddr;
		this.kwitansi_nbr = kwitansi_nbr;
		this.invoice_nbr = invoice_nbr;
		this.inv_period = inv_period;
		this.inv_startdate = inv_startdate;
		this.inv_enddate = inv_enddate;
		this.create_date = create_date;
		this.due_date = due_date;
		this.fee_management = fee_management;
		this.ppn_10 = ppn_10;
		this.no_faktur_pajak = no_faktur_pajak;
		this.pph_23 = pph_23;
		this.total_bill = total_bill;
		this.bankAccount = bankAccount;
		this.is_confirm = is_confirm;
	}

	public Key<CompanyAddress> getCompaddr() {
		return compaddr;
	}

	public void setCompaddr(Key<CompanyAddress> compaddr) {
		this.compaddr = compaddr;
	}

	public String getKwitansi_nbr() {
		return kwitansi_nbr;
	}

	public void setKwitansi_nbr(String kwitansi_nbr) {
		this.kwitansi_nbr = kwitansi_nbr;
	}

	public String getInvoice_nbr() {
		return invoice_nbr;
	}

	public void setInvoice_nbr(String invoice_nbr) {
		this.invoice_nbr = invoice_nbr;
	}

	public String getInv_period() {
		return inv_period;
	}

	public void setInv_period(String inv_period) {
		this.inv_period = inv_period;
	}

	public Date getInv_startdate() {
		return inv_startdate;
	}

	public void setInv_startdate(Date inv_startdate) {
		this.inv_startdate = inv_startdate;
	}

	public Date getInv_enddate() {
		return inv_enddate;
	}

	public void setInv_enddate(Date inv_enddate) {
		this.inv_enddate = inv_enddate;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getDue_date() {
		return due_date;
	}

	public void setDue_date(Date due_date) {
		this.due_date = due_date;
	}

	public Float getFee_management() {
		return fee_management;
	}

	public void setFee_management(Float fee_management) {
		this.fee_management = fee_management;
	}

	public boolean isPpn_10() {
		return ppn_10;
	}

	public void setPpn_10(boolean ppn_10) {
		this.ppn_10 = ppn_10;
	}

	public String getNo_faktur_pajak() {
		return no_faktur_pajak;
	}

	public void setNo_faktur_pajak(String no_faktur_pajak) {
		this.no_faktur_pajak = no_faktur_pajak;
	}

	public boolean isPph_23() {
		return pph_23;
	}

	public void setPph_23(boolean pph_23) {
		this.pph_23 = pph_23;
	}

	public Float getTotal_bill() {
		return total_bill;
	}

	public void setTotal_bill(Float total_bill) {
		this.total_bill = total_bill;
	}

	public Key<BankAccount> getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(Key<BankAccount> bankAccount) {
		this.bankAccount = bankAccount;
	}

	public Long getId() {
		return id;
	}

	public boolean isIs_confirm() {
		return is_confirm;
	}

	public void setIs_confirm(boolean is_confirm) {
		this.is_confirm = is_confirm;
	}

}
