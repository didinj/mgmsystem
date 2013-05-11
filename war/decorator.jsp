<html>
<head>
<title><sitemesh:write property='title' /></title>
<style type="text/css">
@import "css/smoothness.datepick.css";

@import "css/header.css";

@import "css/invoice.css";
</style>
<script src='js/jquery-1.7.2.min.js'></script>
<script src='js/jquery.datepick.min.js'></script>
<script src='js/jquery.datepick-id.js'></script>
<script src='js/invoice.js'></script>
<sitemesh:write property='head' />
</head>
<body>
	<div id="invoice-main">
		<div id="header" class="topround">
			<div id="logo">
				<img src="images/mgmhead.png" />
			</div>
			<div id="userinfo">
				<span id="fullname" class="textshadow"><%=session.getAttribute("fullname")%>,</span>
				<span id="logout" class="textshadow"><a
					href="<%=session.getAttribute("logout")%>">Sign out</a></span>
			</div>
		</div>
		<div>
			<ul id="nav">
				<li class="navhome"><a href="/invoice">INVOICE</a></li>
				<li><a href="/journal">JURNAL</a></li>
				<li><a href="/company">PERUSAHAAN</a></li>
				<li><a href="/account">REKENING</a></li>
			</ul>

		</div>

		<sitemesh:write property='body' />

		<div class='disclaimer'>
			<p align="center">&copy;2013 PT. Mitra Garda Mandiri. Allright
				Reserved</p>
		</div>
	</div>
</body>
</html>