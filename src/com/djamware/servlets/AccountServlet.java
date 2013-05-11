package com.djamware.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.djamware.model.BankAccount;
import com.google.gson.Gson;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

@SuppressWarnings("serial")
public class AccountServlet extends HttpServlet {

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();
		Gson gson = new Gson();
		Objectify ofy = ObjectifyService.begin();
		if (req.getParameterMap().containsKey("id")) {
			Long id = Long.parseLong(req.getParameter("id"));
			BankAccount account = ofy.query(BankAccount.class).filter("id", id).get();
			out.println(gson.toJson(account));
		} else {
			List<BankAccount> account = ofy.query(BankAccount.class).list();
			@SuppressWarnings("rawtypes")
			List acclist = new ArrayList();
			Iterator<BankAccount> iterator = account.iterator();
			while (iterator.hasNext()) {
				BankAccount acc = (BankAccount) iterator.next();
				acclist.add(acc);
			}
			out.println(gson.toJson(acclist));
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();
		String bankname = req.getParameter("bankname");
		String accountnbr = req.getParameter("accountnbr");
		String accountname = req.getParameter("accountname");
		Long accid = null;
		if (req.getParameter("id").equalsIgnoreCase("")
				|| req.getParameter("id") == null) {
			Objectify ofy = ObjectifyService.beginTransaction();
			try {
				BankAccount bankAccount = new BankAccount(bankname, accountnbr, accountname, new Date());
				Key<BankAccount> baid = ofy.put(bankAccount);
				ofy.getTxn().commit();
				if (baid.getId() > 0) {
					accid = baid.getId();
				}
			} finally {
				if (ofy.getTxn().isActive())
					ofy.getTxn().rollback();
			}
			out.println("Data " + accid + " telah di simpan");
		} else {
			Long id = Long.parseLong(req.getParameter("id"));
			Objectify ofy = ObjectifyService.beginTransaction();
			try {
				BankAccount bankAccount = ofy.get(BankAccount.class, id);
				if(!bankAccount.getBankname().equals(bankname))
					bankAccount.setBankname(bankname);
				if(!bankAccount.getAccountnbr().equals(accountnbr))
					bankAccount.setAccountnbr(accountnbr);
				if(!bankAccount.getAccountname().equals(accountname))
					bankAccount.setAccountname(accountname);
				ofy.put(bankAccount);
				ofy.getTxn().commit();
			} finally {
				if (ofy.getTxn().isActive())
					ofy.getTxn().rollback();
			}
			out.println("Data telah di update");
		}
	}
}
