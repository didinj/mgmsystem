package com.djamware.model;

import javax.persistence.Id;

import com.googlecode.objectify.Key;

public class InvoiceDetail {
	@Id
	Long id;
	Key<Invoice> invoice;
	String description;
	Integer qty;
	Float price;
	Float total_price;

	public InvoiceDetail() {
	}

	public InvoiceDetail(Key<Invoice> invoice, String description, Integer qty,
			Float price, Float total_price) {
		this();
		this.invoice = invoice;
		this.description = description;
		this.qty = qty;
		this.price = price;
		this.total_price = total_price;
	}

	public Key<Invoice> getInvoice() {
		return invoice;
	}

	public void setInvoice(Key<Invoice> invoice) {
		this.invoice = invoice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getTotal_price() {
		return total_price;
	}

	public void setTotal_price(Float total_price) {
		this.total_price = total_price;
	}

	public Long getId() {
		return id;
	}

}
