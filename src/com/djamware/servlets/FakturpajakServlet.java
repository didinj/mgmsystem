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
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@SuppressWarnings("serial")
public class FakturpajakServlet extends HttpServlet {
	private static Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 14,
			Font.BOLD, BaseColor.RED);

	@SuppressWarnings("unused")
	private static Font boldFont = new Font(Font.FontFamily.HELVETICA, 12,
			Font.BOLD);

	private static Font smallFont = new Font(Font.FontFamily.HELVETICA, 8,
			Font.NORMAL);

	private static Font uline = new Font(Font.FontFamily.HELVETICA, 12,
			Font.UNDERLINE);

	@SuppressWarnings("unused")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DateFormat df1 = new SimpleDateFormat("dd MMM");
		DateFormat df2 = new SimpleDateFormat("dd MMM yyyy");
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		resp.setContentType("application/pdf");
		Document document = new Document(PageSize.A4);
		Objectify ofy = ObjectifyService.begin();
		Long id = Long.parseLong(req.getParameter("id"));
		Invoice invoice = ofy.query(Invoice.class).filter("id", id).get();
		try {
			PdfWriter.getInstance(document, resp.getOutputStream());
			document.open();

			PdfPTable lbrtable = new PdfPTable(2);
			lbrtable.setWidthPercentage(40);
			float[] widths = { 0.8f, 2.2f };
			lbrtable.setWidths(widths);
			lbrtable.setHorizontalAlignment(Element.ALIGN_RIGHT);
			PdfPCell lbrcell;
			lbrcell = new PdfPCell(new Phrase("Lembar ke-1 :", smallFont));
			lbrcell.setBorder(Rectangle.NO_BORDER);
			lbrtable.addCell(lbrcell);
			lbrcell = new PdfPCell(new Phrase("Untuk Pembeli BKP/Penerima JKP",
					smallFont));
			lbrcell.setBorder(Rectangle.NO_BORDER);
			lbrtable.addCell(lbrcell);
			lbrcell = new PdfPCell(new Phrase("", smallFont));
			lbrcell.setBorder(Rectangle.NO_BORDER);
			lbrtable.addCell(lbrcell);
			lbrcell = new PdfPCell(new Phrase("sebagai bukti Pajak Masukan",
					smallFont));
			lbrcell.setBorder(Rectangle.NO_BORDER);
			lbrtable.addCell(lbrcell);
			document.add(lbrtable);

			Paragraph spacer = new Paragraph();
			spacer.add(new Paragraph("", subTitleFont));
			document.add(spacer);

			Font invfont = new Font();
			invfont.setSize(18);
			invfont.setStyle("bold");

			Paragraph title = new Paragraph();
			title.add(new Paragraph("FAKTUR PAJAK", invfont));
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);

			document.add(spacer);
			PdfPTable faktabel = new PdfPTable(3);
			faktabel.setWidthPercentage(100);
			faktabel.setWidths(new float[] { 0.5f, 3.5f, 2.0f });

			PdfPCell fakcell;
			fakcell = new PdfPCell(new Phrase(
					"Kode dan Nomor Seri Faktur Pajak : "
							+ invoice.getNo_faktur_pajak()));
			fakcell.setColspan(3);
			fakcell.setBorder(Rectangle.BOX);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);

			fakcell = new PdfPCell(new Phrase("Pengusaha Kena Pajak"));
			fakcell.setColspan(3);
			fakcell.setBorder(Rectangle.BOX);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);

			PdfPTable cotable = new PdfPTable(2);
			cotable.setWidthPercentage(100);
			float[] rekwidth = { 2, 8 };
			cotable.setWidths(rekwidth);
			cotable.setHorizontalAlignment(Element.ALIGN_LEFT);
			PdfPCell cocell;
			cocell = new PdfPCell(new Phrase("Nama"));
			cocell.setBorder(Rectangle.NO_BORDER);
			cocell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cocell.setPadding(5);
			cotable.addCell(cocell);
			cocell = new PdfPCell(new Phrase(": PT. MITRA GARDA MANDIRI"));
			cocell.setBorder(Rectangle.NO_BORDER);
			cocell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cocell.setPadding(5);
			cotable.addCell(cocell);
			cocell = new PdfPCell(new Phrase("Alamat"));
			cocell.setBorder(Rectangle.NO_BORDER);
			cocell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cocell.setPadding(5);
			cotable.addCell(cocell);
			cocell = new PdfPCell(
					new Phrase(
							": Bukit Serpong Mas Cluster D Blok D5 No.8 Jakarta Selatan"));
			cocell.setBorder(Rectangle.NO_BORDER);
			cocell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cocell.setPadding(5);
			cotable.addCell(cocell);
			cocell = new PdfPCell(new Phrase("NPWP"));
			cocell.setBorder(Rectangle.NO_BORDER);
			cocell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cocell.setPadding(5);
			cotable.addCell(cocell);
			cocell = new PdfPCell(new Phrase(": 21.123.200.4-411.000"));
			cocell.setBorder(Rectangle.NO_BORDER);
			cocell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cocell.setPadding(5);
			cotable.addCell(cocell);

			fakcell = new PdfPCell(new Phrase(""));
			fakcell.addElement(cotable);
			fakcell.setColspan(3);
			fakcell.setBorder(Rectangle.BOX);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);

			fakcell = new PdfPCell(new Phrase(
					"Pembeli Barang Kena Pajak/Penerima Jasa Kena Pajak"));
			fakcell.setColspan(3);
			fakcell.setBorder(Rectangle.BOX);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);

			CompanyAddress ca = ofy.get(CompanyAddress.class, invoice
					.getCompaddr().getId());
			Company co = ofy.get(Company.class, ca.getCompany().getId());
			PdfPTable co2table = new PdfPTable(2);
			co2table.setWidthPercentage(100);
			float[] co2width = { 2, 8 };
			co2table.setWidths(co2width);
			co2table.setHorizontalAlignment(Element.ALIGN_LEFT);
			PdfPCell co2cell;
			co2cell = new PdfPCell(new Phrase("Nama"));
			co2cell.setBorder(Rectangle.NO_BORDER);
			co2cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			co2cell.setPadding(5);
			co2table.addCell(co2cell);
			co2cell = new PdfPCell(new Phrase(": " + co.getCompany_name()));
			co2cell.setBorder(Rectangle.NO_BORDER);
			co2cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			co2cell.setPadding(5);
			co2table.addCell(co2cell);
			co2cell = new PdfPCell(new Phrase("Alamat"));
			co2cell.setBorder(Rectangle.NO_BORDER);
			co2cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			co2cell.setPadding(5);
			co2table.addCell(co2cell);
			co2cell = new PdfPCell(new Phrase(": " + ca.getAddress()));
			co2cell.setBorder(Rectangle.NO_BORDER);
			co2cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			co2cell.setPadding(5);
			co2table.addCell(co2cell);
			co2cell = new PdfPCell(new Phrase("NPWP"));
			co2cell.setBorder(Rectangle.NO_BORDER);
			co2cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			co2cell.setPadding(5);
			co2table.addCell(co2cell);
			co2cell = new PdfPCell(new Phrase(": " + ca.getNpwp()));
			co2cell.setBorder(Rectangle.NO_BORDER);
			co2cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			co2cell.setPadding(5);
			co2table.addCell(co2cell);

			fakcell = new PdfPCell(new Phrase(""));
			fakcell.addElement(co2table);
			fakcell.setColspan(3);
			fakcell.setBorder(Rectangle.BOX);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);

			fakcell = new PdfPCell(new Phrase("No Urut"));
			fakcell.setBorder(Rectangle.BOX);
			fakcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			fakcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);
			fakcell = new PdfPCell(new Phrase(
					"Nama Barang Kena Pajak/Jasa Kena Pajak"));
			fakcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			fakcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			fakcell.setBorder(Rectangle.BOX);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);
			fakcell = new PdfPCell(new Phrase(
					"Harga Jual/Penggantian/\nUang Muka/Termin)"));
			fakcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			fakcell.setBorder(Rectangle.BOX);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);

			Key<Invoice> invkey = new Key<Invoice>(Invoice.class, id);
			List<InvoiceDetail> invdets = ofy.query(InvoiceDetail.class)
					.filter("invoice", invkey).list();
			Iterator<InvoiceDetail> iterator = invdets.iterator();
			Float subtotal = 0f;
			Integer i = 1;
			while (iterator.hasNext()) {
				InvoiceDetail invdtl = (InvoiceDetail) iterator.next();
				fakcell = new PdfPCell(new Phrase(i.toString()));
				fakcell.setBorder(Rectangle.LEFT);
				fakcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fakcell.setPadding(5);
				faktabel.addCell(fakcell);
				fakcell = new PdfPCell(new Phrase(invdtl.getDescription()));
				fakcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				fakcell.setBorder(Rectangle.LEFT);
				fakcell.setPadding(5);
				faktabel.addCell(fakcell);
				fakcell = new PdfPCell(new Phrase(nf.format(invdtl
						.getTotal_price())));
				fakcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				fakcell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				fakcell.setPadding(5);
				faktabel.addCell(fakcell);
				subtotal = subtotal + invdtl.getTotal_price();
				i++;
			}

			fakcell = new PdfPCell(new Phrase(
					"Harga Jual/Penggantian/Uang Muka/Termin *)"));
			fakcell.setColspan(2);
			fakcell.setBorder(Rectangle.BOX);
			fakcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);
			fakcell = new PdfPCell(new Phrase(nf.format(subtotal)));
			fakcell.setBorder(Rectangle.BOX);
			fakcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);

			fakcell = new PdfPCell(new Phrase("Dikurangi Potongan Harga"));
			fakcell.setColspan(2);
			fakcell.setBorder(Rectangle.BOX);
			fakcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);
			fakcell = new PdfPCell(new Phrase(""));
			fakcell.setBorder(Rectangle.BOX);
			fakcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);

			fakcell = new PdfPCell(new Phrase(
					"Dikurangi Uang Muka yang telah diterima"));
			fakcell.setColspan(2);
			fakcell.setBorder(Rectangle.BOX);
			fakcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);
			fakcell = new PdfPCell(new Phrase(""));
			fakcell.setBorder(Rectangle.BOX);
			fakcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);

			fakcell = new PdfPCell(new Phrase("Dasar Pengenaan Pajak"));
			fakcell.setColspan(2);
			fakcell.setBorder(Rectangle.BOX);
			fakcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);
			fakcell = new PdfPCell(new Phrase(nf.format(subtotal)));
			fakcell.setBorder(Rectangle.BOX);
			fakcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);

			fakcell = new PdfPCell(new Phrase(
					"PPN = 10% x Dasar Pengenaan Pajak"));
			fakcell.setColspan(2);
			fakcell.setBorder(Rectangle.BOX);
			fakcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);
			fakcell = new PdfPCell(new Phrase(nf.format((subtotal * 10) / 100)));
			fakcell.setBorder(Rectangle.BOX);
			fakcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);

			fakcell = new PdfPCell(new Phrase(""));
			fakcell.setColspan(3);
			fakcell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);

			fakcell = new PdfPCell(new Phrase(
					"Pajak Penjualan Atas Barang Mewah"));
			fakcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			fakcell.setColspan(3);
			fakcell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);

			PdfPTable ppnbmtable = new PdfPTable(3);
			ppnbmtable.setWidthPercentage(100);
			float[] ppnbmwidth = { 1, 2, 2 };
			ppnbmtable.setWidths(ppnbmwidth);
			ppnbmtable.setHorizontalAlignment(Element.ALIGN_LEFT);
			PdfPCell ppnbmcell;
			ppnbmcell = new PdfPCell(new Phrase("Tarif"));
			ppnbmcell.setBorder(Rectangle.BOX);
			ppnbmcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			ppnbmcell.setPadding(5);
			ppnbmtable.addCell(ppnbmcell);
			ppnbmcell = new PdfPCell(new Phrase("DPP"));
			ppnbmcell.setBorder(Rectangle.BOX);
			ppnbmcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			ppnbmcell.setPadding(5);
			ppnbmtable.addCell(ppnbmcell);
			ppnbmcell = new PdfPCell(new Phrase("PPn BM"));
			ppnbmcell.setBorder(Rectangle.BOX);
			ppnbmcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			ppnbmcell.setPadding(5);
			ppnbmtable.addCell(ppnbmcell);

			for (int j = 0; j < 4; j++) {
				ppnbmcell = new PdfPCell(new Phrase(".......%"));
				ppnbmcell.setBorder(Rectangle.LEFT);
				ppnbmcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				ppnbmcell.setPadding(5);
				ppnbmtable.addCell(ppnbmcell);
				ppnbmcell = new PdfPCell(new Phrase("Rp..................."));
				ppnbmcell.setBorder(Rectangle.LEFT);
				ppnbmcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				ppnbmcell.setPadding(5);
				ppnbmtable.addCell(ppnbmcell);
				ppnbmcell = new PdfPCell(new Phrase("Rp..................."));
				ppnbmcell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				ppnbmcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				ppnbmcell.setPadding(5);
				ppnbmtable.addCell(ppnbmcell);
			}
			ppnbmcell = new PdfPCell(new Phrase("Jumlah"));
			ppnbmcell.setBorder(Rectangle.BOX);
			ppnbmcell.setColspan(2);
			ppnbmcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			ppnbmcell.setPadding(5);
			ppnbmtable.addCell(ppnbmcell);
			ppnbmcell = new PdfPCell(new Phrase("Rp..................."));
			ppnbmcell.setBorder(Rectangle.BOX);
			ppnbmcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			ppnbmcell.setPadding(5);
			ppnbmtable.addCell(ppnbmcell);

			fakcell = new PdfPCell(new Phrase(""));
			fakcell.setColspan(2);
			fakcell.addElement(ppnbmtable);
			fakcell.setBorder(Rectangle.LEFT);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);

			PdfPTable signtable = new PdfPTable(1);
			signtable.setWidthPercentage(100);
			signtable.setHorizontalAlignment(Element.ALIGN_CENTER);
			PdfPCell signcell;
			signcell = new PdfPCell(new Phrase(""));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5);
			signtable.addCell(signcell);
			signcell = new PdfPCell(new Phrase("Tangerang, "
					+ df2.format(invoice.getCreate_date())));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5);
			signtable.addCell(signcell);
			signcell = new PdfPCell(new Phrase(""));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5);
			signtable.addCell(signcell);
			signcell = new PdfPCell(new Phrase(""));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5);
			signtable.addCell(signcell);
			signcell = new PdfPCell(new Phrase(""));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5);
			signtable.addCell(signcell);
			signcell = new PdfPCell(new Phrase(""));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5);
			signtable.addCell(signcell);
			signcell = new PdfPCell(new Phrase(""));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5);
			signtable.addCell(signcell);
			signcell = new PdfPCell(new Phrase(""));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5);
			signtable.addCell(signcell);
			signcell = new PdfPCell(new Phrase("REMON ALIE TEGE",uline));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5);
			signtable.addCell(signcell);
			signcell = new PdfPCell(new Phrase("DIREKTUR"));
			signcell.setBorder(Rectangle.NO_BORDER);
			signcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell.setPadding(5);
			signtable.addCell(signcell);

			fakcell = new PdfPCell(new Phrase(""));
			fakcell.addElement(signtable);
			fakcell.setBorder(Rectangle.RIGHT);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);
			
			fakcell = new PdfPCell(new Phrase(""));
			fakcell.setColspan(3);
			fakcell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);
			
			fakcell = new PdfPCell(new Phrase(""));
			fakcell.setColspan(3);
			fakcell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);
			
			fakcell = new PdfPCell(new Phrase(" *) Coret yang tidak perlu"));
			fakcell.setColspan(3);
			fakcell.setBorder(Rectangle.NO_BORDER);
			fakcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			fakcell.setPadding(5);
			faktabel.addCell(fakcell);

			document.add(faktabel);
			
			document.newPage();
			
			PdfPTable lbrtable2 = new PdfPTable(2);
			lbrtable2.setWidthPercentage(40);
			float[] widths2 = { 0.8f, 2.2f };
			lbrtable2.setWidths(widths2);
			lbrtable2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			PdfPCell lbrcell2;
			lbrcell2 = new PdfPCell(new Phrase("Lembar ke-2 :", smallFont));
			lbrcell2.setBorder(Rectangle.NO_BORDER);
			lbrtable2.addCell(lbrcell2);
			lbrcell2 = new PdfPCell(new Phrase("Untuk Penjual BKP/Pemberi JKP",
					smallFont));
			lbrcell2.setBorder(Rectangle.NO_BORDER);
			lbrtable2.addCell(lbrcell2);
			lbrcell2 = new PdfPCell(new Phrase("", smallFont));
			lbrcell2.setBorder(Rectangle.NO_BORDER);
			lbrtable2.addCell(lbrcell2);
			lbrcell2 = new PdfPCell(new Phrase("sebagai bukti Pajak Masukan",
					smallFont));
			lbrcell2.setBorder(Rectangle.NO_BORDER);
			lbrtable2.addCell(lbrcell2);
			document.add(lbrtable2);

			document.add(spacer);

			Paragraph title2 = new Paragraph();
			title2.add(new Paragraph("FAKTUR PAJAK", invfont));
			title2.setAlignment(Element.ALIGN_CENTER);
			document.add(title2);

			document.add(spacer);
			PdfPTable faktabel2 = new PdfPTable(3);
			faktabel2.setWidthPercentage(100);
			faktabel2.setWidths(new float[] { 0.5f, 3.5f, 2.0f });

			PdfPCell fakcell2;
			fakcell2 = new PdfPCell(new Phrase(
					"Kode dan Nomor Seri Faktur Pajak : "
							+ invoice.getNo_faktur_pajak()));
			fakcell2.setColspan(3);
			fakcell2.setBorder(Rectangle.BOX);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);

			fakcell2 = new PdfPCell(new Phrase("Pengusaha Kena Pajak"));
			fakcell2.setColspan(3);
			fakcell2.setBorder(Rectangle.BOX);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);

			PdfPTable cotable2 = new PdfPTable(2);
			cotable2.setWidthPercentage(100);
			float[] rekwidth2 = { 2, 8 };
			cotable2.setWidths(rekwidth2);
			cotable2.setHorizontalAlignment(Element.ALIGN_LEFT);
			PdfPCell cocell2;
			cocell2 = new PdfPCell(new Phrase("Nama"));
			cocell2.setBorder(Rectangle.NO_BORDER);
			cocell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			cocell2.setPadding(5);
			cotable2.addCell(cocell2);
			cocell2 = new PdfPCell(new Phrase(": PT. MITRA GARDA MANDIRI"));
			cocell2.setBorder(Rectangle.NO_BORDER);
			cocell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			cocell2.setPadding(5);
			cotable2.addCell(cocell2);
			cocell2 = new PdfPCell(new Phrase("Alamat"));
			cocell2.setBorder(Rectangle.NO_BORDER);
			cocell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			cocell2.setPadding(5);
			cotable2.addCell(cocell2);
			cocell2 = new PdfPCell(
					new Phrase(
							": Bukit Serpong Mas Cluster D Blok D5 No.8 Jakarta Selatan"));
			cocell2.setBorder(Rectangle.NO_BORDER);
			cocell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			cocell2.setPadding(5);
			cotable2.addCell(cocell2);
			cocell2 = new PdfPCell(new Phrase("NPWP"));
			cocell2.setBorder(Rectangle.NO_BORDER);
			cocell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			cocell2.setPadding(5);
			cotable2.addCell(cocell2);
			cocell2 = new PdfPCell(new Phrase(": 21.123.200.4-411.000"));
			cocell2.setBorder(Rectangle.NO_BORDER);
			cocell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			cocell2.setPadding(5);
			cotable2.addCell(cocell2);

			fakcell2 = new PdfPCell(new Phrase(""));
			fakcell2.addElement(cotable2);
			fakcell2.setColspan(3);
			fakcell2.setBorder(Rectangle.BOX);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);

			fakcell2 = new PdfPCell(new Phrase(
					"Pembeli Barang Kena Pajak/Penerima Jasa Kena Pajak"));
			fakcell2.setColspan(3);
			fakcell2.setBorder(Rectangle.BOX);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);

			PdfPTable co2table2 = new PdfPTable(2);
			co2table2.setWidthPercentage(100);
			float[] co2width2 = { 2, 8 };
			co2table2.setWidths(co2width2);
			co2table2.setHorizontalAlignment(Element.ALIGN_LEFT);
			PdfPCell co2cell2;
			co2cell2 = new PdfPCell(new Phrase("Nama"));
			co2cell2.setBorder(Rectangle.NO_BORDER);
			co2cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			co2cell2.setPadding(5);
			co2table2.addCell(co2cell2);
			co2cell2 = new PdfPCell(new Phrase(": " + co.getCompany_name()));
			co2cell2.setBorder(Rectangle.NO_BORDER);
			co2cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			co2cell2.setPadding(5);
			co2table2.addCell(co2cell2);
			co2cell2 = new PdfPCell(new Phrase("Alamat"));
			co2cell2.setBorder(Rectangle.NO_BORDER);
			co2cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			co2cell2.setPadding(5);
			co2table2.addCell(co2cell2);
			co2cell2 = new PdfPCell(new Phrase(": " + ca.getAddress()));
			co2cell2.setBorder(Rectangle.NO_BORDER);
			co2cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			co2cell2.setPadding(5);
			co2table2.addCell(co2cell2);
			co2cell2 = new PdfPCell(new Phrase("NPWP"));
			co2cell2.setBorder(Rectangle.NO_BORDER);
			co2cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			co2cell2.setPadding(5);
			co2table2.addCell(co2cell2);
			co2cell2 = new PdfPCell(new Phrase(": " + ca.getNpwp()));
			co2cell2.setBorder(Rectangle.NO_BORDER);
			co2cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			co2cell2.setPadding(5);
			co2table2.addCell(co2cell2);

			fakcell2 = new PdfPCell(new Phrase(""));
			fakcell2.addElement(co2table2);
			fakcell2.setColspan(3);
			fakcell2.setBorder(Rectangle.BOX);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);

			fakcell2 = new PdfPCell(new Phrase("No Urut"));
			fakcell2.setBorder(Rectangle.BOX);
			fakcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			fakcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);
			fakcell2 = new PdfPCell(new Phrase(
					"Nama Barang Kena Pajak/Jasa Kena Pajak"));
			fakcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			fakcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			fakcell2.setBorder(Rectangle.BOX);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);
			fakcell2 = new PdfPCell(new Phrase(
					"Harga Jual/Penggantian/\nUang Muka/Termin)"));
			fakcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			fakcell2.setBorder(Rectangle.BOX);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);

			List<InvoiceDetail> invdets2 = ofy.query(InvoiceDetail.class)
					.filter("invoice", invkey).list();
			Iterator<InvoiceDetail> iterator2 = invdets2.iterator();
			Float subtotal2 = 0f;
			Integer l = 1;
			while (iterator2.hasNext()) {
				InvoiceDetail invdtl2 = (InvoiceDetail) iterator2.next();
				fakcell2 = new PdfPCell(new Phrase(l.toString()));
				fakcell2.setBorder(Rectangle.LEFT);
				fakcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
				fakcell2.setPadding(5);
				faktabel2.addCell(fakcell2);
				fakcell2 = new PdfPCell(new Phrase(invdtl2.getDescription()));
				fakcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
				fakcell2.setBorder(Rectangle.LEFT);
				fakcell2.setPadding(5);
				faktabel2.addCell(fakcell2);
				fakcell2 = new PdfPCell(new Phrase(nf.format(invdtl2
						.getTotal_price())));
				fakcell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
				fakcell2.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				fakcell2.setPadding(5);
				faktabel2.addCell(fakcell2);
				subtotal2 = subtotal2 + invdtl2.getTotal_price();
				l++;
			}

			fakcell2 = new PdfPCell(new Phrase(
					"Harga Jual/Penggantian/Uang Muka/Termin *)"));
			fakcell2.setColspan(2);
			fakcell2.setBorder(Rectangle.BOX);
			fakcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);
			fakcell2 = new PdfPCell(new Phrase(nf.format(subtotal2)));
			fakcell2.setBorder(Rectangle.BOX);
			fakcell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);

			fakcell2 = new PdfPCell(new Phrase("Dikurangi Potongan Harga"));
			fakcell2.setColspan(2);
			fakcell2.setBorder(Rectangle.BOX);
			fakcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);
			fakcell2 = new PdfPCell(new Phrase(""));
			fakcell2.setBorder(Rectangle.BOX);
			fakcell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);

			fakcell2 = new PdfPCell(new Phrase(
					"Dikurangi Uang Muka yang telah diterima"));
			fakcell2.setColspan(2);
			fakcell2.setBorder(Rectangle.BOX);
			fakcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);
			fakcell2 = new PdfPCell(new Phrase(""));
			fakcell2.setBorder(Rectangle.BOX);
			fakcell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);

			fakcell2 = new PdfPCell(new Phrase("Dasar Pengenaan Pajak"));
			fakcell2.setColspan(2);
			fakcell2.setBorder(Rectangle.BOX);
			fakcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);
			fakcell2 = new PdfPCell(new Phrase(nf.format(subtotal2)));
			fakcell2.setBorder(Rectangle.BOX);
			fakcell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);

			fakcell2 = new PdfPCell(new Phrase(
					"PPN = 10% x Dasar Pengenaan Pajak"));
			fakcell2.setColspan(2);
			fakcell2.setBorder(Rectangle.BOX);
			fakcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);
			fakcell2 = new PdfPCell(new Phrase(nf.format((subtotal2 * 10) / 100)));
			fakcell2.setBorder(Rectangle.BOX);
			fakcell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);

			fakcell2 = new PdfPCell(new Phrase(""));
			fakcell2.setColspan(3);
			fakcell2.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);

			fakcell2 = new PdfPCell(new Phrase(
					"Pajak Penjualan Atas Barang Mewah"));
			fakcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			fakcell2.setColspan(3);
			fakcell2.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);

			PdfPTable ppnbmtable2 = new PdfPTable(3);
			ppnbmtable2.setWidthPercentage(100);
			float[] ppnbmwidth2 = { 1, 2, 2 };
			ppnbmtable2.setWidths(ppnbmwidth2);
			ppnbmtable2.setHorizontalAlignment(Element.ALIGN_LEFT);
			PdfPCell ppnbmcell2;
			ppnbmcell2 = new PdfPCell(new Phrase("Tarif"));
			ppnbmcell2.setBorder(Rectangle.BOX);
			ppnbmcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			ppnbmcell2.setPadding(5);
			ppnbmtable2.addCell(ppnbmcell2);
			ppnbmcell2 = new PdfPCell(new Phrase("DPP"));
			ppnbmcell2.setBorder(Rectangle.BOX);
			ppnbmcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			ppnbmcell2.setPadding(5);
			ppnbmtable2.addCell(ppnbmcell2);
			ppnbmcell2 = new PdfPCell(new Phrase("PPn BM"));
			ppnbmcell2.setBorder(Rectangle.BOX);
			ppnbmcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			ppnbmcell2.setPadding(5);
			ppnbmtable2.addCell(ppnbmcell2);

			for (int k = 0; k < 4; k++) {
				ppnbmcell2 = new PdfPCell(new Phrase(".......%"));
				ppnbmcell2.setBorder(Rectangle.LEFT);
				ppnbmcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
				ppnbmcell2.setPadding(5);
				ppnbmtable2.addCell(ppnbmcell2);
				ppnbmcell2 = new PdfPCell(new Phrase("Rp..................."));
				ppnbmcell2.setBorder(Rectangle.LEFT);
				ppnbmcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
				ppnbmcell2.setPadding(5);
				ppnbmtable2.addCell(ppnbmcell2);
				ppnbmcell2 = new PdfPCell(new Phrase("Rp..................."));
				ppnbmcell2.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				ppnbmcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
				ppnbmcell2.setPadding(5);
				ppnbmtable2.addCell(ppnbmcell2);
			}
			ppnbmcell2 = new PdfPCell(new Phrase("Jumlah"));
			ppnbmcell2.setBorder(Rectangle.BOX);
			ppnbmcell2.setColspan(2);
			ppnbmcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			ppnbmcell2.setPadding(5);
			ppnbmtable2.addCell(ppnbmcell2);
			ppnbmcell2 = new PdfPCell(new Phrase("Rp..................."));
			ppnbmcell2.setBorder(Rectangle.BOX);
			ppnbmcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			ppnbmcell2.setPadding(5);
			ppnbmtable2.addCell(ppnbmcell2);

			fakcell2 = new PdfPCell(new Phrase(""));
			fakcell2.setColspan(2);
			fakcell2.addElement(ppnbmtable2);
			fakcell2.setBorder(Rectangle.LEFT);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);

			PdfPTable signtable2 = new PdfPTable(1);
			signtable2.setWidthPercentage(100);
			signtable2.setHorizontalAlignment(Element.ALIGN_CENTER);
			PdfPCell signcell2;
			signcell2 = new PdfPCell(new Phrase(""));
			signcell2.setBorder(Rectangle.NO_BORDER);
			signcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell2.setPadding(5);
			signtable2.addCell(signcell2);
			signcell2 = new PdfPCell(new Phrase("Tangerang, "
					+ df2.format(invoice.getCreate_date())));
			signcell2.setBorder(Rectangle.NO_BORDER);
			signcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell2.setPadding(5);
			signtable2.addCell(signcell2);
			signcell2 = new PdfPCell(new Phrase(""));
			signcell2.setBorder(Rectangle.NO_BORDER);
			signcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell2.setPadding(5);
			signtable2.addCell(signcell2);
			signcell2 = new PdfPCell(new Phrase(""));
			signcell2.setBorder(Rectangle.NO_BORDER);
			signcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell2.setPadding(5);
			signtable2.addCell(signcell2);
			signcell2 = new PdfPCell(new Phrase(""));
			signcell2.setBorder(Rectangle.NO_BORDER);
			signcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell2.setPadding(5);
			signtable2.addCell(signcell2);
			signcell2 = new PdfPCell(new Phrase(""));
			signcell2.setBorder(Rectangle.NO_BORDER);
			signcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell2.setPadding(5);
			signtable2.addCell(signcell2);
			signcell2 = new PdfPCell(new Phrase(""));
			signcell2.setBorder(Rectangle.NO_BORDER);
			signcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell2.setPadding(5);
			signtable2.addCell(signcell2);
			signcell2 = new PdfPCell(new Phrase(""));
			signcell2.setBorder(Rectangle.NO_BORDER);
			signcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell2.setPadding(5);
			signtable2.addCell(signcell2);
			signcell2 = new PdfPCell(new Phrase("REMON ALIE TEGE",uline));
			signcell2.setBorder(Rectangle.NO_BORDER);
			signcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell2.setPadding(5);
			signtable2.addCell(signcell2);
			signcell2 = new PdfPCell(new Phrase("DIREKTUR"));
			signcell2.setBorder(Rectangle.NO_BORDER);
			signcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			signcell2.setPadding(5);
			signtable2.addCell(signcell2);

			fakcell2 = new PdfPCell(new Phrase(""));
			fakcell2.addElement(signtable2);
			fakcell2.setBorder(Rectangle.RIGHT);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);
			
			fakcell2 = new PdfPCell(new Phrase(""));
			fakcell2.setColspan(3);
			fakcell2.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);
			
			fakcell2 = new PdfPCell(new Phrase(""));
			fakcell2.setColspan(3);
			fakcell2.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);
			
			fakcell2 = new PdfPCell(new Phrase(" *) Coret yang tidak perlu"));
			fakcell2.setColspan(3);
			fakcell2.setBorder(Rectangle.NO_BORDER);
			fakcell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			fakcell2.setPadding(5);
			faktabel2.addCell(fakcell2);

			document.add(faktabel2);
		
			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
