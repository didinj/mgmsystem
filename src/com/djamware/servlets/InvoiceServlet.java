package com.djamware.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.djamware.model.BankAccount;
import com.djamware.model.Company;
import com.djamware.model.Invoice;
import com.djamware.model.InvoiceDetail;
import com.google.appengine.api.datastore.Blob;
import com.google.gson.Gson;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

@SuppressWarnings("serial")
public class InvoiceServlet extends HttpServlet {

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();
		Gson gson = new Gson();
		Objectify ofy = ObjectifyService.begin();
		if (req.getParameterMap().containsKey("id")) {
			Long id = Long.parseLong(req.getParameter("id"));
			Invoice invoice = ofy.query(Invoice.class).filter("id", id).get();
			out.println(gson.toJson(invoice));
		} else {
			List<Invoice> invoice = ofy.query(Invoice.class).list();
			@SuppressWarnings("rawtypes")
			List invlist = new ArrayList();
			Iterator<Invoice> iterator = invoice.iterator();
			Map<String, String> map = new HashMap<String, String>();
			while (iterator.hasNext()) {
				Invoice inv = (Invoice) iterator.next();
				map.put("id", inv.getId().toString());
				map.put("kwitansi_nbr", inv.getKwitansi_nbr());
				map.put("invoice_nbr", inv.getInvoice_nbr());
				Company comp = ofy.get(Company.class, inv.getCompany().getId());
				map.put("company", comp.getCompany_name());
				map.put("inv_period", inv.getInv_period());
				map.put("create_date", df.format(inv.getCreate_date()));
				map.put("due_date", df.format(inv.getDue_date()));
				if (inv.getPaid_date() == null)
					map.put("paid_date", "-");
				else
					map.put("paid_date", df.format(inv.getPaid_date()));
				map.put("total_bill", nf.format(inv.getTotal_bill()));
				if (inv.getReceive_bill() == null)
					map.put("receive_bill", "-");
				else
					map.put("receive_bill", nf.format(inv.getReceive_bill()));
				invlist.add(gson.toJson(map));
				// System.out.println(inv.getCompany().getName());
			}
			out.println(invlist);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat decf = new DecimalFormat("000");
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();
		if (req.getParameterMap().containsKey("confirm")) {
			Long id = Long.parseLong(req.getParameter("id"));
			Date paid_date = null;
			try {
				paid_date = df.parse(req.getParameter("paid_date"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Float receive_bill = Float.parseFloat(req
					.getParameter("receive_bill"));
			Objectify ofy = ObjectifyService.begin();
			ofy = ObjectifyService.beginTransaction();
			try {
				Invoice invs = ofy.get(Invoice.class, id);
				invs.setPaid_date(paid_date);
				invs.setReceive_bill(receive_bill);
				ofy.put(invs);
				ofy.getTxn().commit();
			} finally {
				if (ofy.getTxn().isActive())
					ofy.getTxn().rollback();
			}
			out.println("Data telah di confirm");
		} else {
			Long compid = Long.parseLong(req.getParameter("company-select"));
			Key<Company> company = new Key<Company>(Company.class, compid);
			String kwitansi_nbr = req.getParameter("kwitansi_nbr");
			Date create_date = null, due_date = null, inv_startdate = null, inv_enddate = null;
			String cdate = req.getParameter("create_date");
			try {
				create_date = df.parse(cdate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			try {
				due_date = df.parse(req.getParameter("due_date"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			try {
				inv_startdate = df.parse(req.getParameter("inv_startdate"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			try {
				inv_enddate = df.parse(req.getParameter("inv_enddate"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Date paid_date = null;
			Blob payment_struck = null;
			Float fee_management = Float.parseFloat(req
					.getParameter("fee_management"));
			boolean ppn_10, pph_23;
			if (req.getParameter("ppn_10").equalsIgnoreCase("true"))
				ppn_10 = true;
			else
				ppn_10 = false;
			if (req.getParameter("pph_23").equalsIgnoreCase("true"))
				pph_23 = true;
			else
				pph_23 = false;
			Float total_bill = Float.parseFloat(req.getParameter("total_bill"));
			Float receive_bill = null;
			Long accid = Long.parseLong(req.getParameter("account-select"));
			String inv_period = req.getParameter("invmonth") + " "
					+ req.getParameter("invyear");
			Key<BankAccount> bankAccount = new Key<BankAccount>(
					BankAccount.class, accid);

			// Inovoice Numbering
			Objectify ofy = ObjectifyService.begin();
			Invoice invoice = ofy.query(Invoice.class).order("-id").get();
			String nbr;
			String[] months = new String[] { "I", "II", "III", "IV", "V", "VI",
					"VII", "VIII", "IX", "X", "XI", "XII" };
			if (invoice != null) {
				nbr = invoice.getInvoice_nbr();
				Integer inbr = Integer.parseInt(nbr.substring(0, 3)) + 1;
				nbr = decf.format(inbr);
			} else {
				nbr = "001";
			}
			Integer cmonth = (Integer.parseInt(cdate.substring(3, 5))) - 1;
			String cyear = cdate.substring(6, 10);
			Company comp = ofy
					.query(Company.class)
					.filter("id",
							Long.parseLong(req.getParameter("company-select")))
					.get();
			String invoice_nbr = nbr + "/MGM-" + comp.getCompany_initial()
					+ "/SEC/" + months[cmonth] + "/" + cyear;
			Long invid = null;
			if (req.getParameter("id").equalsIgnoreCase("0")
					|| req.getParameter("id") == null) {
				ofy = ObjectifyService.beginTransaction();
				try {
					Invoice invs = new Invoice(company, kwitansi_nbr, invoice_nbr,
							inv_period, inv_startdate, inv_enddate,
							create_date, due_date, paid_date, payment_struck,
							fee_management, ppn_10, pph_23, total_bill,
							receive_bill, bankAccount);
					Key<Invoice> invkey = ofy.put(invs);
					ofy.getTxn().commit();
					if (invkey.getId() > 0)
						invid = invkey.getId();
				} finally {
					if (ofy.getTxn().isActive())
						ofy.getTxn().rollback();
				}
				out.println(invid);
			} else {
				Long id = Long.parseLong(req.getParameter("id"));
				ofy = ObjectifyService.beginTransaction();
				try {
					Invoice invs = ofy.get(Invoice.class, id);
					if (!invs.getCompany().equals(company))
						invs.setCompany(company);
					if (!invs.getKwitansi_nbr().equals(kwitansi_nbr))
						invs.setKwitansi_nbr(kwitansi_nbr);
					if (!invs.getInv_period().equals(inv_period))
						invs.setInv_period(inv_period);
					if (!invs.getInv_startdate().equals(inv_startdate))
						invs.setInv_startdate(inv_startdate);
					if (!invs.getInv_enddate().equals(inv_enddate))
						invs.setInv_enddate(inv_enddate);
					if (!invs.getCreate_date().equals(create_date))
						invs.setCreate_date(create_date);
					if (!invs.getDue_date().equals(due_date))
						invs.setDue_date(due_date);
					if (!invs.getInvoice_nbr().equals(invoice_nbr))
						invs.setInvoice_nbr(invoice_nbr);
					if (!invs.getPaid_date().equals(paid_date))
						invs.setPaid_date(paid_date);
					if (!equals(invs.isPph_23()))
						invs.setPph_23(pph_23);
					if (!equals(invs.isPpn_10()))
						invs.setPpn_10(ppn_10);
					if (!invs.getFee_management().equals(fee_management))
						invs.setFee_management(fee_management);
					if (!invs.getTotal_bill().equals(total_bill))
						invs.setTotal_bill(total_bill);
					if (!invs.getReceive_bill().equals(receive_bill))
						invs.setReceive_bill(receive_bill);
					if (!invs.getBankAccount().equals(bankAccount))
						invs.setBankAccount(bankAccount);
					ofy.put(invs);
					ofy.getTxn().commit();
				} finally {
					if (ofy.getTxn().isActive())
						ofy.getTxn().rollback();
				}
				out.println("Data " + invid + " telah di update");
			}

			Integer detrow = Integer.parseInt(req.getParameter("detail_row"));
			for (Integer i = 0; i < detrow; i++) {
				Key<Invoice> keyinvoice = new Key<Invoice>(Invoice.class, invid);
				String description = req.getParameter("description" + i);
				Integer qty = Integer.parseInt(req.getParameter("qty" + i));
				Float price = Float.parseFloat(req.getParameter("price" + i));
				Float total_price = Float.parseFloat(req
						.getParameter("total_price" + i));
				if (req.getParameter("detid" + i).equalsIgnoreCase("")
						|| req.getParameter("detid" + i) == null) {
					ofy = ObjectifyService.beginTransaction();
					try {
						InvoiceDetail invdet = new InvoiceDetail(keyinvoice,
								description, qty, price, total_price);
						ofy.put(invdet);
						ofy.getTxn().commit();
					} finally {
						if (ofy.getTxn().isActive())
							ofy.getTxn().rollback();
					}
				} else {
					Long detid = Long.parseLong(req.getParameter("detid" + i));
					ofy = ObjectifyService.beginTransaction();
					try {
						InvoiceDetail invdet = ofy.get(InvoiceDetail.class,
								detid);
						if (!invdet.getDescription().equals(description))
							invdet.setDescription(description);
						if (!invdet.getQty().equals(qty))
							invdet.setQty(qty);
						if (!invdet.getPrice().equals(price))
							invdet.setPrice(price);
						if (!invdet.getTotal_price().equals(total_price))
							invdet.setTotal_price(total_price);
						ofy.put(invdet);
						ofy.getTxn().commit();
					} finally {
						if (ofy.getTxn().isActive())
							ofy.getTxn().rollback();
					}
				}
			}
		}
	}

}
