package com.djamware.servlets;

import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.djamware.model.BankAccount;
import com.djamware.model.Company;
import com.djamware.model.CompanyAddress;
import com.djamware.model.Invoice;
import com.djamware.model.InvoiceDetail;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

@SuppressWarnings("serial")
public class InvoiceDetailServlet extends HttpServlet {
	// private static Font titleFont = new Font(Font.FontFamily.HELVETICA, 18,
	// Font.BOLD);
	private static Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 14,
			Font.BOLD, BaseColor.RED);

	private static Font boldFont = new Font(Font.FontFamily.HELVETICA, 12,
			Font.BOLD);

	private static Font uline = new Font(Font.FontFamily.HELVETICA, 12,
			Font.UNDERLINE);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat month = new SimpleDateFormat("MMM yyyy");
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		resp.setContentType("application/pdf");
		Objectify ofy = ObjectifyService.begin();
		Long id = Long.parseLong(req.getParameter("id"));
		Invoice invoice = ofy.query(Invoice.class).filter("id", id).get();
		try {
			Document document = new Document(PageSize.A4, 36, 36, 80, 36);
			PdfWriter writer = PdfWriter.getInstance(document,
					resp.getOutputStream());
			writer.setPageEvent(new HeaderFooter());

			document.open();

			/* Content */
			LineSeparator logoline = new LineSeparator(1, 100, null,
					Element.ALIGN_CENTER, -2);
			logoline.setLineWidth(2);
			document.add(logoline);
			
			Paragraph spacer = new Paragraph();
			spacer.add(new Paragraph("", subTitleFont));
			document.add(spacer);

			Font invfont = new Font();
			invfont.setSize(20);
			invfont.setStyle("bold");

			Paragraph title = new Paragraph();
			title.add(new Paragraph("INVOICE", invfont));
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);

			document.add(spacer);

			PdfPTable invnbr = new PdfPTable(2);
			float[] widths = { 1, 2 };
			invnbr.setWidths(widths);
			invnbr.setHorizontalAlignment(Element.ALIGN_LEFT);
			PdfPCell invnbrcell;
			invnbrcell = new PdfPCell(new Phrase("No. Invoice"));
			invnbrcell.setBorder(Rectangle.BOX);
			invnbr.addCell(invnbrcell);
			invnbrcell = new PdfPCell(new Phrase(invoice.getInvoice_nbr()));
			invnbrcell.setBorder(Rectangle.BOX);
			invnbr.addCell(invnbrcell);
			invnbrcell = new PdfPCell(new Phrase("Tanggal Invoice"));
			invnbrcell.setBorder(Rectangle.BOX);
			invnbr.addCell(invnbrcell);
			invnbrcell = new PdfPCell(new Phrase(df.format(invoice
					.getCreate_date())));
			invnbrcell.setBorder(Rectangle.BOX);
			invnbr.addCell(invnbrcell);
			invnbrcell = new PdfPCell(new Phrase("Jatuh Tempo"));
			invnbrcell.setBorder(Rectangle.BOX);
			invnbr.addCell(invnbrcell);
			invnbrcell = new PdfPCell(new Phrase(df.format(invoice
					.getDue_date())));
			invnbrcell.setBorder(Rectangle.BOX);
			invnbr.addCell(invnbrcell);
			document.add(invnbr);

			document.add(spacer);

			CompanyAddress ca = ofy.get(CompanyAddress.class, invoice
					.getCompaddr().getId());
			Company co = ofy.get(Company.class, ca.getCompany().getId());
			Paragraph sendto = new Paragraph();
			sendto.add(new Paragraph("Ditujukan ke:"));
			sendto.add(new Paragraph(co.getCompany_name(), boldFont));
			sendto.add(new Paragraph(ca.getAddress()));
			sendto.add(new Paragraph(ca.getCompany_city() + ", "
					+ ca.getCompany_province()));
			document.add(sendto);

			document.add(spacer);

			PdfPTable invamount = new PdfPTable(4);
			invamount.setWidthPercentage(100);
			float[] amtwidth = { 0.8f, 4.2f, 2.5f, 2.5f };
			invamount.setWidths(amtwidth);
			invamount.setHorizontalAlignment(Element.ALIGN_LEFT);
			PdfPCell amountcell;
			amountcell = new PdfPCell(new Phrase("NO", boldFont));
			amountcell.setBorder(Rectangle.BOX);
			amountcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			amountcell.setPadding(5f);
			invamount.addCell(amountcell);
			amountcell = new PdfPCell(new Phrase("URAIAN", boldFont));
			amountcell.setBorder(Rectangle.BOX);
			amountcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			amountcell.setPadding(5f);
			invamount.addCell(amountcell);
			amountcell = new PdfPCell(new Phrase("HARGA", boldFont));
			amountcell.setBorder(Rectangle.BOX);
			amountcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			amountcell.setPadding(5f);
			invamount.addCell(amountcell);
			amountcell = new PdfPCell(new Phrase("JUMLAH", boldFont));
			amountcell.setBorder(Rectangle.BOX);
			amountcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			amountcell.setPadding(5f);
			invamount.addCell(amountcell);
			amountcell = new PdfPCell(new Phrase("1"));
			amountcell.setBorder(Rectangle.BOX);
			amountcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			amountcell.setPadding(5f);
			invamount.addCell(amountcell);
			amountcell = new PdfPCell(new Phrase(
					"Biaya pelayanan pengamanan \n" + co.getCompany_name()
							+ ", " + ca.getAddress()
							+ "\n \ndengan perincian terlampir"));
			amountcell.setBorder(Rectangle.BOX);
			amountcell.setPadding(5f);
			invamount.addCell(amountcell);
			amountcell = new PdfPCell(new Phrase(nf.format(invoice
					.getTotal_bill())));
			amountcell.setBorder(Rectangle.BOX);
			amountcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			amountcell.setPadding(5f);
			invamount.addCell(amountcell);
			amountcell = new PdfPCell(new Phrase(nf.format(invoice
					.getTotal_bill())));
			amountcell.setBorder(Rectangle.BOX);
			amountcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			amountcell.setPadding(5f);
			invamount.addCell(amountcell);
			amountcell = new PdfPCell(new Phrase(" "));
			amountcell.setBorder(Rectangle.NO_BORDER);
			amountcell.setPadding(5f);
			invamount.addCell(amountcell);
			amountcell = new PdfPCell(new Phrase(" "));
			amountcell.setBorder(Rectangle.NO_BORDER);
			amountcell.setPadding(5f);
			invamount.addCell(amountcell);
			amountcell = new PdfPCell(new Phrase("Total Invoice :", boldFont));
			amountcell.setBorder(Rectangle.NO_BORDER);
			amountcell.setPadding(5f);
			invamount.addCell(amountcell);
			amountcell = new PdfPCell(new Phrase(nf.format(invoice
					.getTotal_bill()), boldFont));
			amountcell.setBorder(Rectangle.BOX);
			amountcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			amountcell.setPadding(5f);
			invamount.addCell(amountcell);
			document.add(invamount);

			document.add(spacer);

			BankAccount bacc = ofy.get(BankAccount.class, invoice
					.getBankAccount().getId());
			Paragraph bankacc = new Paragraph();
			bankacc.add(new Paragraph(
					"Pembayaran melalui transfer Bank (diluar biaya administrasi Bank) ke :"));
			document.add(bankacc);
			document.add(spacer);
			PdfPTable norek = new PdfPTable(2);
			float[] rekwidth = { 2, 5 };
			norek.setWidths(rekwidth);
			norek.setHorizontalAlignment(Element.ALIGN_LEFT);
			PdfPCell rekcell;
			rekcell = new PdfPCell(new Phrase("Bank"));
			rekcell.setBorder(Rectangle.NO_BORDER);
			rekcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			norek.addCell(rekcell);
			rekcell = new PdfPCell(new Phrase(": " + bacc.getBankname(),
					boldFont));
			rekcell.setBorder(Rectangle.NO_BORDER);
			rekcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			norek.addCell(rekcell);
			rekcell = new PdfPCell(new Phrase("Nomor Rekening"));
			rekcell.setBorder(Rectangle.NO_BORDER);
			rekcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			norek.addCell(rekcell);
			rekcell = new PdfPCell(new Phrase(": " + bacc.getAccountnbr(),
					boldFont));
			rekcell.setBorder(Rectangle.NO_BORDER);
			rekcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			norek.addCell(rekcell);
			rekcell = new PdfPCell(new Phrase("Atas Nama"));
			rekcell.setBorder(Rectangle.NO_BORDER);
			rekcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			norek.addCell(rekcell);
			rekcell = new PdfPCell(new Phrase(": " + bacc.getAccountname(),
					boldFont));
			rekcell.setBorder(Rectangle.NO_BORDER);
			rekcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			norek.addCell(rekcell);
			document.add(norek);
			document.add(spacer);
			document.add(spacer);
			document.add(spacer);

			PdfPTable sign = new PdfPTable(4);
			sign.setWidthPercentage(100);
			float[] signwidth = { 0.8f, 4.2f, 2.5f, 2.5f };
			sign.setWidths(signwidth);
			sign.setHorizontalAlignment(Element.ALIGN_LEFT);
			PdfPCell signcell;
			signcell = new PdfPCell(new Phrase(" "));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5f);
			sign.addCell(signcell);
			signcell = new PdfPCell(new Phrase(" "));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5f);
			sign.addCell(signcell);
			signcell = new PdfPCell(new Phrase(" "));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5f);
			sign.addCell(signcell);
			signcell = new PdfPCell(new Phrase("Hormat Kami,"));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5f);
			sign.addCell(signcell);
			signcell = new PdfPCell(new Phrase(" "));
			signcell.setColspan(4);
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5f);
			sign.addCell(signcell);
			signcell = new PdfPCell(new Phrase(" "));
			signcell.setColspan(4);
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5f);
			sign.addCell(signcell);
			signcell = new PdfPCell(new Phrase(" "));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5f);
			sign.addCell(signcell);
			signcell = new PdfPCell(new Phrase(" "));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5f);
			sign.addCell(signcell);
			signcell = new PdfPCell(new Phrase(" "));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5f);
			sign.addCell(signcell);
			signcell = new PdfPCell(new Phrase("Wahyuni", boldFont));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5f);
			sign.addCell(signcell);
			signcell = new PdfPCell(new Phrase(" "));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5f);
			sign.addCell(signcell);
			signcell = new PdfPCell(new Phrase(" "));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5f);
			sign.addCell(signcell);
			signcell = new PdfPCell(new Phrase(" "));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5f);
			sign.addCell(signcell);
			signcell = new PdfPCell(new Phrase("Finance"));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5f);
			sign.addCell(signcell);
			document.add(sign);
			document.add(spacer);
			document.add(spacer);
			document.add(spacer);
			Paragraph cc = new Paragraph();
			cc.add(new Paragraph("Cc: Arsip"));
			document.add(cc);
			document.add(spacer);

			// 2nd page
			document.newPage();

			PdfPTable headtbl = new PdfPTable(1);
			headtbl.setTotalWidth(600);
			headtbl.setLockedWidth(true);
			headtbl.setWidths(new float[] { 40 });
			headtbl.setHorizontalAlignment(Element.ALIGN_CENTER);
			headtbl.getDefaultCell().setBorder(Rectangle.NO_BORDER);

			document.add(logoline);

			// Content page 2
			document.add(spacer);
			Paragraph title2 = new Paragraph();
			title2.add(new Paragraph("RINCIAN TAGIHAN", invfont));
			title2.setAlignment(Element.ALIGN_CENTER);
			document.add(title2);

			document.add(spacer);
			PdfPTable hal = new PdfPTable(2);
			float[] halwidth = { 2, 5 };
			hal.setWidths(halwidth);
			hal.setHorizontalAlignment(Element.ALIGN_LEFT);
			PdfPCell halcell;
			halcell = new PdfPCell(new Phrase("Atas Nama"));
			halcell.setBorder(Rectangle.NO_BORDER);
			halcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			hal.addCell(halcell);
			halcell = new PdfPCell(new Phrase(": PT. Mitra Garda Mandiri",
					boldFont));
			halcell.setBorder(Rectangle.NO_BORDER);
			halcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			hal.addCell(halcell);
			halcell = new PdfPCell(new Phrase("Pekerjaan"));
			halcell.setBorder(Rectangle.NO_BORDER);
			halcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			hal.addCell(halcell);
			halcell = new PdfPCell(new Phrase(": Pelayanan Pengamanan "
					+ co.getCompany_name(), boldFont));
			halcell.setBorder(Rectangle.NO_BORDER);
			halcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			hal.addCell(halcell);
			halcell = new PdfPCell(new Phrase("Periode"));
			halcell.setBorder(Rectangle.NO_BORDER);
			halcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			hal.addCell(halcell);
			halcell = new PdfPCell(new Phrase(": "
					+ month.format(invoice.getCreate_date()), boldFont));
			halcell.setBorder(Rectangle.NO_BORDER);
			halcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			hal.addCell(halcell);
			halcell = new PdfPCell(new Phrase("No/Tgl. Invoice"));
			halcell.setBorder(Rectangle.NO_BORDER);
			halcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			hal.addCell(halcell);
			halcell = new PdfPCell(new Phrase(": " + invoice.getInvoice_nbr()
					+ " - " + df.format(invoice.getCreate_date()), boldFont));
			halcell.setBorder(Rectangle.NO_BORDER);
			halcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			hal.addCell(halcell);
			halcell = new PdfPCell(new Phrase("Jatuh Tempo"));
			halcell.setBorder(Rectangle.NO_BORDER);
			halcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			hal.addCell(halcell);
			halcell = new PdfPCell(new Phrase(": "
					+ df.format(invoice.getDue_date()), boldFont));
			halcell.setBorder(Rectangle.NO_BORDER);
			halcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			hal.addCell(halcell);

			document.add(hal);
			document.add(spacer);

			PdfPTable dettbl = new PdfPTable(6);
			dettbl.setWidthPercentage(100);
			float[] detwidth = { 0.7f, 3.7f, 1.2f, 2f, 2.2f, 0.2f };
			dettbl.setWidths(detwidth);
			dettbl.setHorizontalAlignment(Element.ALIGN_CENTER);
			PdfPCell detcell;
			detcell = new PdfPCell(new Phrase("NO", boldFont));
			detcell.setBorder(Rectangle.BOX);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase("URAIAN", boldFont));
			detcell.setBorder(Rectangle.BOX);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase("JUMLAH", boldFont));
			detcell.setBorder(Rectangle.BOX);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase("HARGA", boldFont));
			detcell.setBorder(Rectangle.BOX);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase("TOTAL HARGA", boldFont));
			detcell.setBorder(Rectangle.BOX);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase(""));
			detcell.setBorder(Rectangle.NO_BORDER);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase("1"));
			detcell.setBorder(Rectangle.LEFT);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase("Biaya pelayanan pengamanan"));
			detcell.setBorder(Rectangle.LEFT);
			detcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase(""));
			detcell.setBorder(Rectangle.LEFT);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase(""));
			detcell.setBorder(Rectangle.LEFT);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase(""));
			detcell.setBorder(Rectangle.LEFT);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase(""));
			detcell.setBorder(Rectangle.LEFT);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);

			Key<Invoice> invkey = new Key<Invoice>(Invoice.class, id);
			List<InvoiceDetail> invdets = ofy.query(InvoiceDetail.class)
					.filter("invoice", invkey).list();
			Iterator<InvoiceDetail> iterator = invdets.iterator();
			Float subtotal = 0f;
			while (iterator.hasNext()) {
				InvoiceDetail invdtl = (InvoiceDetail) iterator.next();
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase("- "
						+ invdtl.getDescription()));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(invdtl.getQty().toString()));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(nf.format(invdtl.getPrice())));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(nf.format(invdtl
						.getTotal_price())));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				subtotal = subtotal + (invdtl.getTotal_price());
			}
			detcell = new PdfPCell(new Phrase(""));
			detcell.setBorder(Rectangle.LEFT);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase(""));
			detcell.setBorder(Rectangle.LEFT);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase(""));
			detcell.setBorder(Rectangle.LEFT);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase(""));
			detcell.setBorder(Rectangle.LEFT);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase("_____________+"));
			detcell.setBorder(Rectangle.LEFT);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase(""));
			detcell.setBorder(Rectangle.LEFT);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase(""));
			detcell.setBorder(Rectangle.LEFT);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase("  Sub Total", boldFont));
			detcell.setBorder(Rectangle.LEFT);
			detcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase(""));
			detcell.setBorder(Rectangle.LEFT);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase(""));
			detcell.setBorder(Rectangle.LEFT);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase(nf.format(subtotal)));
			detcell.setBorder(Rectangle.LEFT);
			detcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			detcell = new PdfPCell(new Phrase(""));
			detcell.setBorder(Rectangle.LEFT);
			detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			detcell.setPadding(5f);
			dettbl.addCell(detcell);
			Float total1 = 0f;
			if (invoice.getFee_management() != null) {
				total1 = subtotal + (invoice.getFee_management());
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase("- Fee Manajemen"));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(nf.format(invoice
						.getFee_management()), uline));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase("  TOTAL I", boldFont));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(nf.format(total1), boldFont));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
			} else {
				total1 = subtotal;
			}
			Float total2 = 0f;
			if (invoice.isPpn_10() == true) {
				Float ppn10 = (total1 * 10) / 100;
				total2 = total1 + ppn10;
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase("- PPN 10%"));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(nf.format(ppn10), uline));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase("  TOTAL II", boldFont));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(nf.format(total2), boldFont));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
			} else {
				total2 = total1;
			}
			Float gtotal = 0f;
			if (invoice.isPph_23() == true) {
				Float pph23 = (float) (invoice.getFee_management() * 2) / 100;
				gtotal = total2 - pph23;
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase("- Potongan PPh 23"));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase("( " + nf.format(pph23)
						+ " )"));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase("Jumlah Total", boldFont));
				detcell.setBorder(Rectangle.TOP);
				detcell.setColspan(4);
				detcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(nf.format(gtotal), boldFont));
				detcell.setBorder(Rectangle.BOX);
				detcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
			} else {
				gtotal = total2;
				detcell = new PdfPCell(new Phrase("Jumlah Total", boldFont));
				detcell.setBorder(Rectangle.TOP);
				detcell.setColspan(4);
				detcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(nf.format(gtotal), boldFont));
				detcell.setBorder(Rectangle.BOX);
				detcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
				detcell = new PdfPCell(new Phrase(""));
				detcell.setBorder(Rectangle.LEFT);
				detcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				detcell.setPadding(5f);
				dettbl.addCell(detcell);
			}
			document.add(dettbl);
			document.add(spacer);
			document.add(spacer);

			PdfPTable notetbl = new PdfPTable(3);
			notetbl.setWidthPercentage(60);
			float[] notewidth = { 1f, 3f, 6f };
			notetbl.setWidths(notewidth);
			notetbl.setHorizontalAlignment(Element.ALIGN_LEFT);
			PdfPCell notecell;
			notecell = new PdfPCell(new Phrase("Catatan :", uline));
			notecell.setBorder(Rectangle.NO_BORDER);
			notecell.setColspan(2);
			notecell.setHorizontalAlignment(Element.ALIGN_LEFT);
			notetbl.addCell(notecell);
			notecell = new PdfPCell(new Phrase(""));
			notecell.setBorder(Rectangle.NO_BORDER);
			notecell.setHorizontalAlignment(Element.ALIGN_LEFT);
			notetbl.addCell(notecell);
			notecell = new PdfPCell(new Phrase(""));
			notecell.setBorder(Rectangle.NO_BORDER);
			notecell.setHorizontalAlignment(Element.ALIGN_LEFT);
			notetbl.addCell(notecell);
			notecell = new PdfPCell(new Phrase(
					"Pembayaran dilakukan melalui Transfer Bank :"));
			notecell.setBorder(Rectangle.NO_BORDER);
			notecell.setColspan(2);
			notecell.setHorizontalAlignment(Element.ALIGN_LEFT);
			notetbl.addCell(notecell);
			notecell = new PdfPCell(new Phrase(""));
			notecell.setBorder(Rectangle.NO_BORDER);
			notecell.setHorizontalAlignment(Element.ALIGN_LEFT);
			notetbl.addCell(notecell);
			notecell = new PdfPCell(new Phrase("Atas Nama"));
			notecell.setBorder(Rectangle.NO_BORDER);
			notecell.setHorizontalAlignment(Element.ALIGN_LEFT);
			notetbl.addCell(notecell);
			notecell = new PdfPCell(new Phrase(": " + bacc.getAccountname(),
					boldFont));
			notecell.setBorder(Rectangle.NO_BORDER);
			notecell.setHorizontalAlignment(Element.ALIGN_LEFT);
			notetbl.addCell(notecell);
			notecell = new PdfPCell(new Phrase(""));
			notecell.setBorder(Rectangle.NO_BORDER);
			notecell.setHorizontalAlignment(Element.ALIGN_LEFT);
			notetbl.addCell(notecell);
			notecell = new PdfPCell(new Phrase("Rekening"));
			notecell.setBorder(Rectangle.NO_BORDER);
			notecell.setHorizontalAlignment(Element.ALIGN_LEFT);
			notetbl.addCell(notecell);
			notecell = new PdfPCell(new Phrase(": " + bacc.getAccountnbr(),
					boldFont));
			notecell.setBorder(Rectangle.NO_BORDER);
			notecell.setHorizontalAlignment(Element.ALIGN_LEFT);
			notetbl.addCell(notecell);
			notecell = new PdfPCell(new Phrase(""));
			notecell.setBorder(Rectangle.NO_BORDER);
			notecell.setHorizontalAlignment(Element.ALIGN_LEFT);
			notetbl.addCell(notecell);
			notecell = new PdfPCell(new Phrase("Bank"));
			notecell.setBorder(Rectangle.NO_BORDER);
			notecell.setHorizontalAlignment(Element.ALIGN_LEFT);
			notetbl.addCell(notecell);
			notecell = new PdfPCell(new Phrase(": " + bacc.getBankname(),
					boldFont));
			notecell.setBorder(Rectangle.NO_BORDER);
			notecell.setHorizontalAlignment(Element.ALIGN_LEFT);
			notetbl.addCell(notecell);
			document.add(notetbl);
			document.add(spacer);
			document.add(spacer);
			document.add(spacer);

			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/** Inner class to add a header and a footer. */
	class HeaderFooter extends PdfPageEventHelper {
		public void onEndPage(PdfWriter writer, Document document) {
			try {
				Rectangle page = document.getPageSize();
				
				PdfPTable head = new PdfPTable(1);				
				Image headimg = Image.getInstance("images/mgm_kop.jpg");
				headimg.scalePercent(70);

				PdfPCell headcell = new PdfPCell(headimg);
				headcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				headcell.setBorder(Rectangle.NO_BORDER);

				head.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				head.addCell(headcell);
				head.setTotalWidth(page.getWidth() - document.leftMargin()
						- document.rightMargin());
				head.writeSelectedRows(0, -1, document.leftMargin(),
						page.getHeight() - document.topMargin() + head.getTotalHeight(),
						writer.getDirectContent());

				PdfPTable foot = new PdfPTable(1);
				Image footimg = Image.getInstance("images/mgmfoot.png");
				footimg.scalePercent(90);

				PdfPCell footcell = new PdfPCell(footimg);
				footcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				footcell.setBorder(Rectangle.NO_BORDER);

				foot.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				foot.addCell(footcell);
				foot.setTotalWidth(page.getWidth() - document.leftMargin()
						- document.rightMargin());
				foot.writeSelectedRows(0, -1, document.leftMargin(),
						document.bottomMargin(), writer.getDirectContent());

			} catch (Exception e) {
				throw new ExceptionConverter(e);
			}
		}
	}
}
