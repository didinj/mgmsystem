<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	if (null == session.getAttribute(session.getId())) {
%><jsp:forward page="/" />
<%
	} else {
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>MGM Invoice</title>
</head>
<body>
	<div id="invoice-list">
		<h1>Daftar Invoice</h1>
		<button class="button" onclick="create()">Invoice Baru</button>
		<br>
		<table id="invoice-table-list">
			<thead class="ui-widget-header">
				<tr>
					<th class="borright">KWITANSI</th>
					<th class="borright">NOMOR</th>
					<th class="borright">NAMA PERUSAHAAN</th>
					<th class="borright">PERIODE</th>
					<th class="borright">TGL INVOICE</th>
					<th class="borright">JATUH TEMPO</th>
					<th class="borright">TOTAL INVOICE</th>
					<th>TINDAKAN</th>
				</tr>
			</thead>
			<tbody id="invoice-tbody"></tbody>
		</table>
		<br> <label id="message"></label>
	</div>
	<div id="invoice-create" style="display: none">
		<h1>Invoice Baru</h1>
		<form id="invoice-form">
			<input type="hidden" id="id" name="id" />
			<table id="invoice-table-create" class="ui-corner-all">
				<tr>
					<td>Nomor Kwitansi</td>
					<td><input type="text" name="kwitansi_nbr" id="kwitansi_nbr" /></td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td>Perusahaan</td>
					<td><select id="company-select" name="company-select"
						onchange="populateSelectBox3();"></select></td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td>Alamat Perusahaan</td>
					<td><select id="compaddr-select" name="compaddr-select"></select></td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td>Periode Tagihan</td>
					<td><select id="invmonth" name="invmonth" class="hwidth">
							<option value="0">Pilih Bulan</option>
							<option value="Januari">Januari</option>
							<option value="Februari">Februari</option>
							<option value="Maret">Maret</option>
							<option value="April">April</option>
							<option value="Mei">Mei</option>
							<option value="Juni">Juni</option>
							<option value="Juli">Juli</option>
							<option value="Agustus">Agustus</option>
							<option value="September">September</option>
							<option value="Oktober">Oktober</option>
							<option value="Nopember">Nopember</option>
							<option value="Desember">Desember</option>
					</select> <select id="invyear" name="invyear" class="hwidth">
							<option value="0">Pilih Tahun</option>
							<option value="2009">2009</option>
							<option value="2010">2010</option>
							<option value="2011">2011</option>
							<option value="2012">2012</option>
							<option value="2013">2013</option>
							<option value="2014">2014</option>
							<option value="2015">2015</option>
							<option value="2016">2016</option>
							<option value="2017">2017</option>
							<option value="2018">2018</option>
					</select></td>
				</tr>
				<tr>
					<td width="15%">Tgl Awal</td>
					<td width="35%"><input type="text" name="inv_startdate"
						id="inv_startdate" /></td>
					<td width="15%">Tgl Akhir</td>
					<td width="35%"><input type="text" name="inv_enddate"
						id="inv_enddate" /></td>
				</tr>
				<tr>
					<td>Tgl Invoice</td>
					<td><input type="text" name="create_date" id="create_date" /></td>
					<td>Tgl Jatuh Tempo</td>
					<td><input type="text" name="due_date" id="due_date" /></td>
				</tr>
				<tr>
					<td>Rekening Bank</td>
					<td><select id="account-select" name="account-select"></select></td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td>Rincian Tagihan</td>
					<td colspan="3"><span id="rincian"><a id="add-detail"
							onclick="addDetail()">Tambah Rincian</a></span></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td colspan="3">
						<table id="invoice-detail-table" class="rounded"
							style="width: 100%">
							<thead>
								<tr>
									<th class="borright" width="40%">Uraian</th>
									<th class="borright" width="10%">Jumlah</th>
									<th class="borright" width="20%">Harga</th>
									<th class="borright" width="20%">Total Harga</th>
									<th width="10%">Hapus</th>
								</tr>
							</thead>
							<tbody id="detail-invoice-tbody"></tbody>
							<tfoot id="detail-invoice-tfoot">
								<tr>
									<td colspan="3" align="center">SUB TOTAL</td>
									<td class=rt id="total_detail"></td>
									<td>&nbsp;</td>
								</tr>
							</tfoot>
						</table>
					</td>
				</tr>
				<tr>
					<td>Manajemen Fee</td>
					<td><input type="checkbox" id="feecheck" onclick="checkfee()" />&nbsp;<span
						id="feeman" style="display: none"><select class="hwidth" id="selectmfee"
							onchange="checkselectedmfee()"><option value="fromsub">10%
									Subtotal</option>
								<option value="manualmfee">Input Manual</option></select></span></td>
					<td><span id="manmfee" style="display: none"><input
							type="text" id="fee_management" name="fee_management"
							onblur="countfee()" /></span></td>
					<td></td>

				</tr>
				<tr>
					<td></td>
					<td></td>
					<td>TOTAL I</td>
					<td id="total1"></td>
				</tr>
				<tr>
					<td>PPN 10%</td>
					<td><input type="checkbox" id="ppncheck" onclick="checkppn()" /></td>
					<td>TOTAL II</td>
					<td id="total2"></td>
				</tr>
				<tr>
					<td>PPH 23</td>
					<td><input type="checkbox" id="pphcheck" onclick="checkpph()" /></td>
					<td>JUMLAH TOTAL</td>
					<td id="total_bill"></td>
				</tr>
				<tr>
					<td colspan="4" style="border-top: 2px solid #bdd3fe;"><input type="button" id="save-button"
						value="Simpan" onclick="save()" /><input type="reset"
						id="reset-invoice" style="display: none" /></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="confirm-invoice" style="display: none" class="bottomround">
		<br>
		<h1>Konfirmasi Pembayaran</h1>
		<form id="invoice-confirm-form">
			<input type="text" style="display: none" name="id" id="id" />
			<table id="invoice-confirm-table">
				<tr>
					<td>Rekening Bank</td>
					<td id="bank_account"></td>
				</tr>
				<tr>
					<td>Nomor Invoice</td>
					<td id="invoice"></td>
				</tr>
				<tr>
					<td>Tanggal Penerimaan</td>
					<td><input type="text" name="receive_date" id="receive_date" /></td>
				</tr>
				<tr>
					<td>Total Penerimaan</td>
					<td><input type="text" name="receive_amount" id="receive_amount" /></td>
				</tr>
				<tr>
					<td>Selisih</td>
					<td id="selisih"></td>
				</tr>
				<tr>
					<td>Catatan</td>
					<td><textarea cols="80" rows="3" name="notes" id="notes"></textarea></td>
				</tr>
				<tr>
					<td colspan="2"><input type="button" id="save-button"
						value="Konfirmasi" onclick="saveconfirm()" style="width: 100px !important;" /><input type="reset"
						id="reset-invoice-confirm" style="display: none" /></td>
				</tr>
			</table>
		</form>
	</div>

	<div id="loading"
		style="display: none; position: fixed; top: 50%; left: 50%; margin-top: -17px; margin-left: -17px; width: 100%; height: 100%;">
		<p>
			<img src="images/ajax-loader.gif" />
		</p>
	</div>
	<script type="text/javascript">
		$(window)
				.load(
						function() {
							init();
							$(
									"#create_date,#due_date,#inv_startdate,#inv_enddate,#receive_date")
									.datepicker({
										dateFormat : "dd/mm/yy"
									});
						});
		$(document).ready(function() {

			// if user clicked on button, the overlay layer or the dialogbox, close the dialog 
			$('a.btn-ok, #dialog-overlay, #dialog-box').click(function() {
				$('#dialog-overlay, #dialog-box').hide();
				return false;
			});

			$('#loading').bind("ajaxSend", function() {
				$(this).show();
			}).bind("ajaxComplete", function() {
				$(this).hide();
			});

			$(".button").button();
			$("#add-detail,#save-button").button();

		});
	</script>
</body>
</html>
<%
	}
%>