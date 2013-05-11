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
<title>MGM Invoice : Bank Account</title>
<style type="text/css">
@import "css/account.css";

@import "css/header.css";
</style>
<script src='js/jquery-1.7.2.min.js'></script>
<script src='js/account.js'></script>
</head>
<body>

	<div id="account-list" class="bottomround">
		<br>
		<h1>Daftar Rekening</h1>
		<span id="baru"> <a href="#" onclick="create()">Baru</a></span>
		<table id="account-table" class="medround">
			<thead>
				<tr>
					<th width="30%" class="br">NAMA BANK</th>
					<th width="15%" class="br">NOMOR REKENING</th>
					<th width="30%" class="br">NAMA REKENING</th>
					<th width="5%">EDIT</th>
				</tr>
			</thead>
			<tbody id="account-tbody"></tbody>
		</table>
		<label id="message"></label><br>
	</div>
	<div id="account-create" style="display: none" class="bottomround">
		<br>
		<h1>Tambah Rekening</h1>
		<form id="account-form">
			<input type="hidden" id="id" name="id" />
			<table>
				<tr>
					<td>Nama Bank</td>
					<td><input type="text" name="bankname" id="bankname" /></td>
				</tr>
				<tr>
					<td>Nomor Rekening</td>
					<td><input type="text" name="accountnbr" id="accountnbr" /></td>
				</tr>
				<tr>
					<td>Nama Rekening</td>
					<td><input type="text" name="accountname" id="accountname" /></td>
				</tr>
				<tr>
					<td colspan="2"><input type="button" id="save-button"
						value="Simpan" onclick="save()" /><input type="reset"
						id="reset-account" style="display: none" /></td>
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