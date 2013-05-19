/**
 * Copyright 2011 Google
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.djamware.servlets;

import java.util.Iterator;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.djamware.model.BankAccount;
import com.djamware.model.Company;
import com.djamware.model.CompanyAddress;
import com.djamware.model.Invoice;
import com.djamware.model.InvoiceDetail;
import com.djamware.model.Users;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

/**
 * This servlet pre populates some data into customers and products
 * 
 * @author
 */

@SuppressWarnings("serial")
public class PrePopulateDataServlet extends HttpServlet {
	static {
		ObjectifyService.register(Users.class);
		ObjectifyService.register(Invoice.class);
		ObjectifyService.register(InvoiceDetail.class);
		ObjectifyService.register(Company.class);
		ObjectifyService.register(CompanyAddress.class);
		ObjectifyService.register(BankAccount.class);
	}
	
	@Override
	public void init() throws ServletException {
		// populateUsers();
		// deleteKind("Pasien");
	}

	private void populateUsers() {
		ResourceBundle rb = ResourceBundle.getBundle("Users");
		Iterator<String> keys = rb.keySet().iterator();
		String prop;
		Objectify ofy = ObjectifyService.begin();
		while (keys.hasNext()) {
			prop = rb.getString(keys.next());
			String values[] = prop.split(",");
			Users users = new Users(values[0].trim(), values[1].trim(),
					values[2].trim(), Integer.parseInt(values[3].trim()));
			ofy.put(users);
		}
	}
}