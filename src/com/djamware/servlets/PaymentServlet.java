package com.djamware.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.djamware.model.BankAccount;
import com.djamware.model.Invoice;
import com.djamware.model.Payment;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

@SuppressWarnings("serial")
public class PaymentServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();

		// Long id = Long.parseLong(req.getParameter("id"));
		System.out.println(req.getParameter("bank_account_id"));
		Long accid = Long.parseLong(req.getParameter("bank_account_id"));
		Key<BankAccount> bankAccount = new Key<BankAccount>(BankAccount.class,
				accid);
		Date receive_date = null;
		try {
			receive_date = df.parse(req.getParameter("receive_date"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Long invid = Long.parseLong(req.getParameter("invoice_id"));
		Key<Invoice> invoice = new Key<Invoice>(Invoice.class, invid);
		Float receive_amount = Float.parseFloat(req
				.getParameter("receive_amount"));
		String notes = req.getParameter("notes");
		Objectify ofy = ObjectifyService.begin();
		Invoice inv = ofy.get(Invoice.class, invoice.getId());
		Float gap = inv.getTotal_bill() - receive_amount;
		ofy = ObjectifyService.beginTransaction();
		try {
			Payment paymt = new Payment(bankAccount, receive_date, invoice,
					receive_amount, gap, notes);
			Key<Payment> paykey = ofy.put(paymt);
			inv.setIs_confirm(true);
			ofy.put(inv);
			ofy.getTxn().commit();
			if (paykey.getId() > 0)
				invid = paykey.getId();
		} finally {
			if (ofy.getTxn().isActive())
				ofy.getTxn().rollback();
		}
		out.println(invid);
	}

}
