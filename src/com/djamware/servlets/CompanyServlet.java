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
			Long id = Long.parseLong(req.getParameter("id"));
			Company company = ofy.query(Company.class).filter("id", id).get();
			out.println(gson.toJson(company));
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
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();
		String unit_nbr = req.getParameter("unit_nbr");
		String company_name = req.getParameter("company_name");
		String company_initial = req.getParameter("company_initial");
		String company_address = req.getParameter("company_address");
		String company_city = req.getParameter("company_city");
		String company_province = req.getParameter("company_province");
		String company_phone = req.getParameter("company_phone");
		Long compid = null;
		if (req.getParameter("id").equalsIgnoreCase("")
				|| req.getParameter("id") == null) {
			Objectify ofy = ObjectifyService.beginTransaction();
			try {
				Company company = new Company(unit_nbr, company_name,
						company_initial, company_address, company_city,
						company_province, company_phone, new Date(), new Date());
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
			Objectify ofy = ObjectifyService.beginTransaction();
			try {
				Company company = ofy.get(Company.class, id);
				if (!company.getUnit_nbr().equals(unit_nbr))
					company.setUnit_nbr(unit_nbr);
				if (!company.getCompany_name().equals(company_name))
					company.setCompany_name(company_name);
				if (!company.getCompany_initial().equals(company_initial))
					company.setCompany_initial(company_initial);
				if (!company.getCompany_address().equals(company_address))
					company.setCompany_address(company_address);
				if (!company.getCompany_city().equals(company_city))
					company.setCompany_city(company_city);
				if (!company.getCompany_province().equals(company_province))
					company.setCompany_province(company_province);
				company.setUpdatedate(new Date());
				ofy.put(company);
				ofy.getTxn().commit();
			} finally {
				if (ofy.getTxn().isActive())
					ofy.getTxn().rollback();
			}
			out.println("Data telah di update");
		}
	}
}
