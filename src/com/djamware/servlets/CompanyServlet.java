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

import com.djamware.model.Company;
import com.djamware.model.CompanyAddress;
import com.google.gson.Gson;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

@SuppressWarnings("serial")
public class CompanyServlet extends HttpServlet {

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();
		Gson gson = new Gson();
		Objectify ofy = ObjectifyService.begin();
		if (req.getParameterMap().containsKey("id")) {
			if (req.getParameterMap().containsKey("action")) {
				@SuppressWarnings("rawtypes")
				List calist = new ArrayList();
				Long id = Long.parseLong(req.getParameter("id"));
				Company company = ofy.query(Company.class).filter("id", id)
						.get();
				calist.add(company);
				List<CompanyAddress> ca = ofy.query(CompanyAddress.class).filter("company",company).list();
				Iterator<CompanyAddress> iterator = ca.iterator();
				while (iterator.hasNext()) {
					CompanyAddress coad = (CompanyAddress) iterator.next();
					calist.add(coad);
				}
				out.println(gson.toJson(calist));
			} else {
				@SuppressWarnings("rawtypes")
				List calist = new ArrayList();
				Long id = Long.parseLong(req.getParameter("id"));
				Company company = ofy.query(Company.class).filter("id", id)
						.get();
				calist.add(company);
				List<CompanyAddress> ca = ofy.query(CompanyAddress.class).filter("company",company).list();
				Iterator<CompanyAddress> iterator = ca.iterator();
				while (iterator.hasNext()) {
					CompanyAddress coad = (CompanyAddress) iterator.next();
					calist.add(coad);
				}
				out.println(gson.toJson(calist));
			}
		} else {
			List<Company> company = ofy.query(Company.class).list();
			@SuppressWarnings("rawtypes")
			List colist = new ArrayList();
			Iterator<Company> iterator = company.iterator();
			while (iterator.hasNext()) {
				Company comp = (Company) iterator.next();
				colist.add(comp);
			}
			out.println(gson.toJson(colist));
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Objectify ofy;
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();
		String unit_nbr = req.getParameter("unit_nbr");
		String company_name = req.getParameter("company_name");
		String company_initial = req.getParameter("company_initial");
		Long compid = null;
		if (req.getParameter("id").equalsIgnoreCase("")
				|| req.getParameter("id") == null) {
			ofy = ObjectifyService.beginTransaction();
			try {
				Company company = new Company(unit_nbr, company_name,
						company_initial, new Date(), new Date());
				Key<Company> coid = ofy.put(company);
				ofy.getTxn().commit();
				if (coid.getId() > 0) {
					compid = coid.getId();
				}
			} finally {
				if (ofy.getTxn().isActive())
					ofy.getTxn().rollback();
			}
			out.println("Data " + compid + " telah di simpan");
		} else {
			Long id = Long.parseLong(req.getParameter("id"));
			ofy = ObjectifyService.beginTransaction();
			try {
				Company company = ofy.get(Company.class, id);
				if (!company.getUnit_nbr().equals(unit_nbr))
					company.setUnit_nbr(unit_nbr);
				if (!company.getCompany_name().equals(company_name))
					company.setCompany_name(company_name);
				if (!company.getCompany_initial().equals(company_initial))
					company.setCompany_initial(company_initial);
				company.setUpdatedate(new Date());
				ofy.put(company);
				ofy.getTxn().commit();
			} finally {
				if (ofy.getTxn().isActive())
					ofy.getTxn().rollback();
			}
			out.println("Data telah di update");
		}

		Integer addressrow = Integer.parseInt(req.getParameter("address_row"));
		for (Integer i = 0; i < addressrow; i++) {
			Key<Company> keycomp = new Key<Company>(Company.class, compid);
			String address = req.getParameter("address" + i);
			String npwp = req.getParameter("npwp" + i);
			String city = req.getParameter("company_city" + i);
			String province = req.getParameter("company_province" + i);
			String phone = req.getParameter("company_phone" + i);
			if (req.getParameter("addrid" + i).equalsIgnoreCase("")
					|| req.getParameter("addrid" + i) == null) {
				ofy = ObjectifyService.beginTransaction();
				try {
					CompanyAddress ca = new CompanyAddress(keycomp, address,
							npwp, city, province, phone);
					ofy.put(ca);
					ofy.getTxn().commit();
				} finally {
					if (ofy.getTxn().isActive())
						ofy.getTxn().rollback();
				}
			} else {
				Long addrid = Long.parseLong(req.getParameter("addrid" + i));
				ofy = ObjectifyService.beginTransaction();
				try {
					CompanyAddress ca = ofy.get(CompanyAddress.class, addrid);
					if (!ca.getAddress().equals(address))
						ca.setAddress(address);
					if (!ca.getCompany_city().equals(city))
						ca.setCompany_city(city);
					if (!ca.getCompany_province().equals(province))
						ca.setCompany_province(province);
					if (!ca.getCompany_phone().equals(phone))
						ca.setCompany_phone(phone);
					ofy.put(ca);
					ofy.getTxn().commit();
				} finally {
					if (ofy.getTxn().isActive())
						ofy.getTxn().rollback();
				}
			}
		}
	}
}
