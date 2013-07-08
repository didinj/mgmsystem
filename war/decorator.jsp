<html>
<head>
<title><sitemesh:write property='title' /></title>
<link href='http://fonts.googleapis.com/css?family=Merriweather+Sans'
	rel='stylesheet' type='text/css'>
<link href="css/custom-theme/jquery-ui-1.10.3.custom.css"
	rel="stylesheet">
<link href="css/header.css" rel="stylesheet">
<link href="css/invoice.css" rel="stylesheet">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.0/jquery.min.js"></script>
<script src="js/jquery-ui-1.10.3.custom.js"></script>
<script src='js/jquery.maskedinput.min.js'></script>
<script src='js/invoice.js'></script>
<sitemesh:write property='head' />
</head>
<body>
	<div id="main" class="clearfix">
		<div id="head" class="ui-corner-top">
			<div id="header">
				<div id="logo">
					<img src="images/mgmhead.png" />
				</div>
				<div id="userinfo">
					<span id="fullname" class="textshadow"><%=session.getAttribute("fullname")%>,</span>
					<span id="logout" class="textshadow"><a
						href="<%=session.getAttribute("logout")%>">Sign out</a></span>
				</div>
			</div>
			<div id="nav">
				<ul id="menu">
					<li><a href="/invoice">INVOICE</a></li>
					<li><a href="/journal">JURNAL</a></li>
					<li><a href="/company">PERUSAHAAN</a></li>
					<li><a href="/account">REKENING</a></li>
				</ul>
			</div>
		</div>
		<div id="content" class="ui-corner-bottom ui-widget-content">
			<sitemesh:write property='body' />
		</div>

		<div class='disclaimer'>
			<p align="center">&copy;2013 PT. Mitra Garda Mandiri. Allright
				Reserved</p>
		</div>
	</div>
</body>
</html>