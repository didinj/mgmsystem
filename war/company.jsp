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
<title>MGM Client</title>
<script src='js/jquery-1.7.2.min.js'></script>
<script src='js/company.js'></script>
</head>
<body>
	<div id="company-list">
		<h1>Daftar Perusahaan/Client</h1>
		<button class="button" onclick="create()">Perusahaan Baru</button>
		<br>
		<table id="company-table">
			<thead class="ui-widget-header">
				<tr>
					<th width="15%" class="br">UNIT</th>
					<th width="50%" class="br">NAMA PERUSAHAAN</th>
					<td width="15%" class="br">INISIAL</td>
					<th width="20%">EDIT</th>
				</tr>
			</thead>
			<tbody id="company-tbody"></tbody>
		</table>
		<label id="message"></label><br>
	</div>
	<div id="company-show" style="display: none">
		<h1>Detail Perusahaan</h1>
		<span id="edit" style="width:60px !important; padding-top:0 !important;"></span>
		<table id="company-show-table" style="border:none !important;">
			<tbody>
				<tr>
					<td width="18%">UNIT</td>
					<td width="82%" id="show-unit"></td>
				</tr>
				<tr>
					<td>NAMA PERUSAHAAN</td>
					<td id="show-nama"></td>
				</tr>
				<tr>
					<td>INISIAL</td>
					<td id="show-init"></td>
				</tr>
				<tr>
				<td>ALAMAT</td>
					<td>
						<table>
							<thead>
								<tr>
									<th class="borright" width="30%">Address</th>
									<th class="borright" width="20%">NPWP</th>
									<th class="borright" width="20%">Kota</th>
									<th class="borright" width="20%">Propinsi</th>
									<th width="10%">Telepon</th>
								</tr>
							</thead>
							<tbody id="compadd-show-tbody"></tbody>
						</table>
					</td>
				</tr>
			</tbody>
		</table>

	</div>
	<div id="company-create" style="display: none">
		<h1>Tambah Perusahaan</h1>
		<form id="company-form">
			<input type="hidden" id="id" name="id" />
			<table style="width: 100%" class="ui-corner-all">
				<tr>
					<td width="18%">Unit</td>
					<td width="82%"><input type="text" name="unit_nbr"
						id="unit_nbr" /></td>
				</tr>
				<tr>
					<td>Nama Perusahaan</td>
					<td><input type="text" name="company_name" id="company_name" /></td>
				</tr>
				<tr>
					<td>Inisial Perusahaan</td>
					<td><input type="text" name="company_initial"
						id="company_initial" /></td>
				</tr>
				<tr>
					<td>Alamat</td>
					<td><span id="rincian"><a href="#" id="add-addr"
							onclick="addAddress()">Tambah Alamat</a></span></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><table id="company-address-table" class="rounded"
							style="width: 100%">
							<thead>
								<tr>
									<th class="borright" width="25%">Address</th>
									<th class="borright" width="15%">NPWP</th>
									<th class="borright" width="14%">Kota</th>
									<th class="borright" width="19%">Propinsi</th>
									<th class="borright" width="12%">Telepon</th>
									<th width="5%">Hapus</th>
								</tr>
							</thead>
							<tbody id="company-address-tbody"></tbody>
						</table></td>
				</tr>
				<tr>
					<td colspan="2"><input type="button" id="save-button"
						value="Simpan" onclick="save()" /><input type="reset"
						id="reset-company" style="display: none" /></td>
				</tr>
			</table>
		</form>
	</div>
	<script type="text/javascript">
		$(window).load(function() {
			init();
		});
		
		$(document).ready(function() {
			$(".button").button();
			$("#add-addr,#save-button,#edit").button();
		});
	</script>
</body>
</html>
<%
	}
%>