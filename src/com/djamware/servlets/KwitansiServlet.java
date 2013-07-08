package com.djamware.servlets;

import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.djamware.model.Company;
import com.djamware.model.CompanyAddress;
import com.djamware.model.Invoice;
import com.djamware.model.Payment;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

@SuppressWarnings("serial")
public class KwitansiServlet extends HttpServlet {
	
	private static Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 14,
			Font.BOLD, BaseColor.RED);

	private static Font boldFont = new Font(Font.FontFamily.HELVETICA, 12,
			Font.BOLD);

	@SuppressWarnings("unused")
	private static Font uline = new Font(Font.FontFamily.HELVETICA, 12,
			Font.UNDERLINE);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DateFormat df1 = new SimpleDateFormat("dd MMM");
		DateFormat df2 = new SimpleDateFormat("dd MMM yyyy");
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		resp.setContentType("application/pdf");
		Document document = new Document(PageSize.A5.rotate());
		Objectify ofy = ObjectifyService.begin();
		Long id = Long.parseLong(req.getParameter("id"));
		Invoice invoice = ofy.query(Invoice.class).filter("id", id).get();
		try {
			PdfWriter.getInstance(document, resp.getOutputStream());
			document.open();
			Image image = Image.getInstance("images/mgm_kop.jpg");
			image.scalePercent(50);
			PdfPTable table = new PdfPTable(1);
			table.setTotalWidth(550);
			table.setLockedWidth(true);
			table.setWidths(new float[] { 40 });
			table.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			PdfPCell headcell;
			headcell = new PdfPCell(image);
			headcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			headcell.setBorder(Rectangle.NO_BORDER);
			table.addCell(headcell);
			document.add(table);
			LineSeparator logoline = new LineSeparator(1, 100, null,
					Element.ALIGN_CENTER, -2);
			logoline.setLineWidth(2);
			document.add(logoline);
			/* Content */
			Paragraph spacer = new Paragraph();
			spacer.add(new Paragraph("", subTitleFont));
			document.add(spacer);

			Font invfont = new Font();
			invfont.setSize(20);
			invfont.setStyle("bold");

			Paragraph title = new Paragraph();
			title.add(new Paragraph("KWITANSI", invfont));
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);

			document.add(spacer);
			PdfPTable kwitable = new PdfPTable(3);
			kwitable.setWidthPercentage(100);
			kwitable.setWidths(new float[]{1.5f, 0.1f, 4.0f});
	        
			PdfPCell kwicell;
			kwicell = new PdfPCell(new Phrase("Nomor"));
			kwicell.setBorder(Rectangle.NO_BORDER);
			kwitable.addCell(kwicell);
			kwicell = new PdfPCell(new Phrase(":"));
			kwicell.setBorder(Rectangle.NO_BORDER);
			kwitable.addCell(kwicell);
			kwicell = new PdfPCell(new Phrase(invoice.getKwitansi_nbr(),boldFont));
			kwicell.setBorder(Rectangle.NO_BORDER);
			kwitable.addCell(kwicell);
			
			CompanyAddress ca = ofy.get(CompanyAddress.class, invoice.getCompaddr().getId());
			Company co = ofy.get(Company.class, ca.getCompany().getId());
			kwicell = new PdfPCell(new Phrase("Diterima dari"));
			kwicell.setBorder(Rectangle.NO_BORDER);
			kwitable.addCell(kwicell);
			kwicell = new PdfPCell(new Phrase(":"));
			kwicell.setBorder(Rectangle.NO_BORDER);
			kwitable.addCell(kwicell);
			kwicell = new PdfPCell(new Phrase(co.getCompany_name(),boldFont));
			kwicell.setBorder(Rectangle.NO_BORDER);
			kwitable.addCell(kwicell);
			
			kwicell = new PdfPCell(new Phrase("Pembayaran"));
			kwicell.setBorder(Rectangle.NO_BORDER);
			kwitable.addCell(kwicell);
			kwicell = new PdfPCell(new Phrase(":"));
			kwicell.setBorder(Rectangle.NO_BORDER);
			kwitable.addCell(kwicell);
			kwicell = new PdfPCell(new Phrase("Biaya pelayanan pengamanan "+co.getCompany_name()+"\nPeriode "+df1.format(invoice.getInv_startdate())+" - "+df2.format(invoice.getInv_enddate())+"\nInvoice "+invoice.getInvoice_nbr(),boldFont));
			kwicell.setBorder(Rectangle.NO_BORDER);
			kwitable.addCell(kwicell);
			
			Payment paymt = ofy.query(Payment.class).filter("invoice", invoice).get();
			kwicell = new PdfPCell(new Phrase("Sejumlah"));
			kwicell.setBorder(Rectangle.NO_BORDER);
			kwitable.addCell(kwicell);
			kwicell = new PdfPCell(new Phrase(":"));
			kwicell.setBorder(Rectangle.NO_BORDER);
			kwitable.addCell(kwicell);
			kwicell = new PdfPCell(new Phrase("",boldFont));
			kwicell.setBorder(Rectangle.NO_BORDER);
			kwitable.addCell(kwicell);
			
			kwicell = new PdfPCell(new Phrase(""));
			kwicell.setBorder(Rectangle.NO_BORDER);
			kwitable.addCell(kwicell);
			kwicell = new PdfPCell(new Phrase(""));
			kwicell.setBorder(Rectangle.NO_BORDER);
			kwitable.addCell(kwicell);
			
			PdfPTable rcvtable = new PdfPTable(1);
			rcvtable.setWidthPercentage(30);
			rcvtable.setHorizontalAlignment(Element.ALIGN_LEFT);
			PdfPCell rcvcell;
			rcvcell = new PdfPCell(new Phrase(nf.format(paymt.getReceive_amount())));
			rcvcell.setPadding(5f);
			rcvcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			rcvtable.addCell(rcvcell);
			kwicell.addElement(rcvtable);
			kwicell.setBorder(Rectangle.NO_BORDER);
			kwicell.setHorizontalAlignment(Element.ALIGN_LEFT);
			kwitable.addCell(kwicell);
						
			document.add(kwitable);
			document.add(spacer);
			document.add(spacer);
			
			PdfPTable signtable = new PdfPTable(1);
			signtable.setWidthPercentage(30);
			signtable.setHorizontalAlignment(Element.ALIGN_RIGHT);
			PdfPCell signcell;
			signcell = new PdfPCell(new Phrase("Serpong, "+df2.format(paymt.getReceive_date())));
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setBorder(Rectangle.NO_BORDER);
			signtable.addCell(signcell);
			signcell = new PdfPCell(new Phrase("\n\n"));
			signcell.setBorder(Rectangle.NO_BORDER);
			signtable.addCell(signcell);
			signcell = new PdfPCell(new Phrase(""));
			signcell.setBorder(Rectangle.NO_BORDER);
			signtable.addCell(signcell);
			signcell = new PdfPCell(new Phrase(""));
			signcell.setBorder(Rectangle.NO_BORDER);
			signtable.addCell(signcell);
			signcell = new PdfPCell(new Phrase("WAHYUNI"));
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setBorder(Rectangle.BOTTOM);
			signtable.addCell(signcell);
			signcell = new PdfPCell(new Phrase("FINANCE"));
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setBorder(Rectangle.NO_BORDER);
			signtable.addCell(signcell);
			document.add(signtable);
			
			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

}
