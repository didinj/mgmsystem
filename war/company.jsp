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
<style type="text/css">
@import "css/company.css";

@import "css/header.css";
</style>
<script src='js/jquery-1.7.2.min.js'></script>
<script src='js/company.js'></script>
</head>
<body>
	<div id="company-list" class="bottomround">
		<br>
		<h1>Daftar Perusahaan/Client</h1>
		<span id="baru"> <a href="#" onclick="create()">Baru</a></span>
		<table id="company-table" class="medround">
			<thead>
				<tr>
					<th width="5%" class="br">UNIT</th>
					<th width="23%" class="br">NAMA PERUSAHAAN</th>
					<th width="33%" class="br">ALAMAT</th>
					<th width="12%" class="br">KOTA</th>
					<th width="12%" class="br">PROVINSI</th>
					<th width="10%" class="br">TELEPON</th>
					<th width="5%">EDIT</th>
				</tr>
			</thead>
			<tbody id="company-tbody"></tbody>
		</table>
		<label id="message"></label><br>
	</div>
	<div id="company-create" style="display: none" class="bottomround">
		<br>
		<h1>Tambah Perusahaan</h1>
		<form id="company-form">
			<input type="hidden" id="id" name="id" />
			<table>
				<tr>
					<td>Unit</td>
					<td><input type="text" name="unit_nbr" id="unit_nbr" /></td>
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
					<td><table id="company-address-table" class="rounded">
							<thead>
								<tr>
									<th class="borright" width="20%">Address</th>
									<th class="borright" width="20%">NPWP</th>
									<th width="20%">Hapus</th>
								</tr>
							</thead>
							<tbody id="company-address-tbody"></tbody>
						</table></td>
				</tr>
				<tr>
					<td>Kota</td>
					<td><input type="text" name="company_city" id="company_city" /></td>
				</tr>
				<tr>
					<td>Provinsi</td>
					<td><input type="text" name="company_province"
						id="company_province" /></td>
				</tr>
				<tr>
					<td>Telepon</td>
					<td><input type="text" name="company_phone" id="company_phone" /></td>
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
	</script>
</body>
</html>
<%
	}
%>