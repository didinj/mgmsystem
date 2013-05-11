package com.djamware.servlets;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.djamware.model.Users;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

/**
 * @author Didin
 * 
 */
@SuppressWarnings("serial")
public class SigninServlet extends HttpServlet {
	private static final Logger logger = Logger.getLogger(SigninServlet.class
			.getCanonicalName());

	@SuppressWarnings("rawtypes")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.log(Level.INFO, "Sign In");
		resp.setContentType("text/plain");
		String response = "";
		String username = req.getParameter("username");
		String password = req.getParameter("password");

		HttpSession session = req.getSession(true);

		Objectify ofy = ObjectifyService.begin();
		List<Users> users = ofy.query(Users.class)
				.filter("username", username.trim())
				.filter("password", password.trim()).list();
		if (users == null || users.isEmpty()) {
			response = "Email dan Password tidak cocok, silahkan ulangi.";
			resp.sendRedirect("/");
		} else {
			Iterator iterator = users.iterator();
			String logouturl = "/signout";
			while (iterator.hasNext()) {
				Users user = (Users) iterator.next();
				session.setAttribute(session.getId(), username);
				session.setAttribute("logout", logouturl);
				session.setAttribute("fullname", user.getFullname());
				if (user.getRoleId() == 1)
					resp.sendRedirect("/invoice");

			}
		}

		resp.getWriter().println(response);
	}
}
