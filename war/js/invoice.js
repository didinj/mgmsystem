var init = function() {
	populateList();
}

var create = function() {
	$("#invoice-list").hide();
	$("#invoice-create").show();
	$("select[id$=company-select] > option").remove();
	populateSelectBox(null);
	$("select[id$=account-select] > option").remove();
	populateSelectBox2(null);
}

// parameter object definition
var param = function(name, value) {
	this.name = name;
	this.value = value;
}

var save = function() {
	var data = new Array();
	// collecting the field values from the form
	if ($("#id").val() == "")
		data[data.length] = new param("id", "0");
	else
		data[data.length] = new param("id", $("#id").val());
	if ($("#company-select").val() == "")
		data[data.length] = new param("compaddr-select", "0");
	else
		data[data.length] = new param("compaddr-select", $("#compaddr-select")
				.val());
	data[data.length] = new param("kwitansi_nbr", $("#kwitansi_nbr").val());
	data[data.length] = new param("invmonth", $("#invmonth").val());
	data[data.length] = new param("invyear", $("#invyear").val());
	if ($("#inv_startdate").val() == "")
		data[data.length] = new param("inv_startdate", "0");
	else
		data[data.length] = new param("inv_startdate", $("#inv_startdate")
				.val());
	if ($("#inv_enddate").val() == "")
		data[data.length] = new param("inv_enddate", "0");
	else
		data[data.length] = new param("inv_enddate", $("#inv_enddate").val());
	if ($("#account-select").val() == "")
		data[data.length] = new param("account-select", "0");
	else
		data[data.length] = new param("account-select", $("#account-select")
				.val());
	if ($("#create_date").val() == "")
		data[data.length] = new param("create_date", "0");
	else
		data[data.length] = new param("create_date", $("#create_date").val());
	if ($("#due_date").val() == "")
		data[data.length] = new param("due_date", "0");
	else
		data[data.length] = new param("due_date", $("#due_date").val());
	if ($("#fee_management").val() == "")
		data[data.length] = new param("fee_management", "0");
	else
		data[data.length] = new param("fee_management", $("#fee_management")
				.val());
	if ($('#ppncheck').is(':checked') == true)
		data[data.length] = new param("ppn_10", "true");
	else
		data[data.length] = new param("ppn_10", "false");
	if ($('#pphcheck').is(':checked') == true)
		data[data.length] = new param("pph_23", "true");
	else
		data[data.length] = new param("pph_23", "false");
	if ($("#total_bill").text() == "")
		data[data.length] = new param("total_bill", "0");
	else
		data[data.length] = new param("total_bill", $("#total_bill").text());

	var row = $("#invoice-detail-table >tbody >tr").length;
	data[data.length] = new param("detail_row", row);
	for ( var i = 0; i < row; i++) {
		data[data.length] = new param("description" + i, $("#description" + i)
				.val());
		data[data.length] = new param("qty" + i, $("#qty" + i).val());
		data[data.length] = new param("price" + i, $("#price" + i).val());
		data[data.length] = new param("total_price" + i, $("#total_price" + i)
				.text());
		data[data.length] = new param("detid" + i, $("#detid" + i).val());
	}
	// making the ajax call
	$.ajax({
		url : "/invoiceservlet",
		type : "POST",
		data : data,
		success : function(data) {
			$("#invoice-create").hide();
			$("#invoice-list").show();
			$("#message").text("Data telah di simpan");
			$("#reset-invoice").click();
			detail(data);
		}
	});
	$('#reset-invoice').click();
	init();
}

var edit = function(id) {
	var parameter = new Array();
	parameter[parameter.length] = new param('id', id);
	$.ajax({
		url : "/invoiceservlet",
		type : "GET",
		dataType : "json",
		data : parameter,
		success : function(resp) {
			var data = resp.invoice;
			$("#id").val(data[0].id);
			$("#kwitansi_nbr").val(data[0].kwitansi_nbr);
			$("select[id$=company-select] > option").remove();
			populateSelectBox(data[3].id, data[2].id);
			var invdt = data[0].inv_period.split(" ");
			$("#invmonth").val(invdt[0]);
			$("#invyear").val(invdt[1]);
			var orgsdate = $.datepicker.formatDate('dd/mm/yy', new Date(
					data[0].inv_startdate));
			$("#inv_startdate").val(orgsdate);
			var orgedate = $.datepicker.formatDate('dd/mm/yy', new Date(
					data[0].inv_enddate));
			$("#inv_enddate").val(orgedate);
			var orgcdate = $.datepicker.formatDate('dd/mm/yy', new Date(
					data[0].create_date));
			$("#create_date").val(orgcdate);
			var orgddate = $.datepicker.formatDate('dd/mm/yy', new Date(
					data[0].due_date));
			$("#due_date").val(orgddate);
			$("select[id$=account-select] > option").remove();
			populateSelectBox2(data[1].id);
			var det = resp.detail;
			for(var i=0;i<det.length;i++) {
				var htm = "";
				htm += "<tr><td><input type='text' id='description"
						+ i
						+ "' name='description"
						+ i
						+ "' value="+det[i].description+"/></td>"
						+ "<td><input style='text-align:center;' type='text' id='qty"
						+ i
						+ "' name='qty"
						+ i
						+ "' value="+det[i].qty+" /></td>"
						+ "<td><input class='rt' type='text' id='price"
						+ i
						+ "' name='price"
						+ i
						+ "' onblur='total("
						+ i
						+ ")' value="+det[i].price+" /></td>"
						+ "<td style='text-align: right;' id='total_price"
						+ i
						+ "'>"+det[i].total_price+"</td><td style='display:none'><input type='text' id='detid"
						+ i
						+ "' name='detid"
						+ i
						+ "' value="+det[i].id+" /></td><td><a href='#' class='ui-icon ui-icon-trash ct'>Hapus</a></td></tr>";
				$("#detail-invoice-tbody").append(htm);
			}
			total_detail();
			$("#total_bill").text(data[0].total_bill);
			if(data[0].fee_management!=""||data[0].fee_management!="0") {
				$("#feecheck").prop("checked","checked");
				$("#feeman").show();
				$("#fee_management").val(data[0].fee_management);
				if((data[0].fee_management*100)/$("#total_detail").text()==10) {
					$("#selectmfee").val("fromsub");
				} else {
					$("#selectmfee").val("manualmfee");
					$("#manmfee").show();
				}
				$("#total1").text(parseFloat($("#total_detail").text())+parseFloat($("#fee_management").val()));
			}
			if(data[0].ppn_10==true) {
				$("#ppncheck").prop("checked","checked");
				$("#total2").text((parseFloat($("#total1").text()*10)/100)+parseFloat($("#total1").text()));
			}
			if(data[0].pph_23==true) {
				$("#pphcheck").prop("checked","checked");
				var pph = parseFloat(($("#fee_management").val() * 2) / 100);
				$("#total_bill").text(parseFloat($("#total_bill").text() - pph));
			}
			$("#invoice-list").hide();
			$("#invoice-create").show();
		}
	});
}

// function to populate the select box which takes input as id of the selectbox
// element and url to get the data
var populateSelectBox = function(id, addrid) {
	// making the ajax call
	$.ajax({
		url : "/companyservlet",
		type : "GET",
		dataType : "json",
		success : function(resp) {
			// getting the select box element
			var selectBox = $('#company-select');
			// setting the content inside as empty
			selectBox.innerHTML = '';
			// getting the data from the response object
			var data = resp;
			$("select[id$=company-select] > option").remove();
			// appending the first option as select to the select box
			selectBox.append('<option value="">Pilih Perusahaan</option>');
			// adding all other values
			for ( var i = 0; i < data.length; i++) {
				if (id == data[i].id) {
					selectBox.append('<option value="' + data[i].id
							+ '" selected="selected">' + data[i].company_name
							+ '</option>');
					$("select[id$=compaddr-select] > option").remove();
					populateSelectBox3(addrid, data[i].id);
				} else {
					selectBox.append('<option value="' + data[i].id + '">'
							+ data[i].company_name + '</option>');
				}
			}
		},
		error : function(e) {
			// calling the user defined error function
			if (errorFn)
				errorFn(e);
		}
	});
}

var populateSelectBox3 = function(id, cid) {
	var parameter = new Array();
	if (cid != null)
		parameter[parameter.length] = new param('compid', cid);
	else
		parameter[parameter.length] = new param('compid', $('#company-select')
				.val());
	// making the ajax call
	$.ajax({
		url : "/companyservlet",
		type : "GET",
		dataType : "json",
		data : parameter,
		success : function(resp) {
			// getting the select box element
			var selectBox = $('#compaddr-select');
			// setting the content inside as empty
			selectBox.innerHTML = '';
			// getting the data from the response object
			var data = resp;
			$("select[id$=compaddr-select] > option").remove();
			// appending the first option as select to the select box
			selectBox.append('<option value="">Pilih Alamat</option>');
			// adding all other values
			for ( var i = 0; i < data.length; i++) {
				if (id == data[i].id) {
					selectBox.append('<option value="' + data[i].id
							+ '" selected="selected">' + data[i].address
							+ '</option>');
				} else {
					selectBox.append('<option value="' + data[i].id + '">'
							+ data[i].address + '</option>');
				}
			}
		},
		error : function(e) {
			// calling the user defined error function
			if (errorFn)
				errorFn(e);
		}
	});
}

var populateSelectBox2 = function(id) {
	// making the ajax call
	$.ajax({
		url : "/accountservlet",
		type : "GET",
		dataType : "json",
		success : function(resp) {
			// getting the select box element
			var selectBox = $('#account-select');
			// setting the content inside as empty
			selectBox.innerHTML = '';
			// getting the data from the response object
			var data = resp;
			// appending the first option as select to the select box
			selectBox.append('<option value="">Pilih Rekening</option>');
			// adding all other values
			for ( var i = 0; i < data.length; i++) {
				if (id == data[i].id) {
					selectBox.append('<option value="' + data[i].id
							+ '" selected="selected">' + data[i].accountnbr
							+ ' - ' + data[i].bankname + '</option>');
				} else {
					selectBox.append('<option value="' + data[i].id + '">'
							+ data[i].accountnbr + ' - ' + data[i].bankname
							+ '</option>');
				}
			}
		},
		error : function(e) {
			// calling the user defined error function
			if (errorFn)
				errorFn(e);
		}
	});
}

var populateList = function() {
	$
			.ajax({
				url : "/invoiceservlet",
				type : "GET",
				dataType : "json",
				success : function(resp) {
					var data = resp;
					var htm = '';
					var confirmlink = '';
					var editlink = '';
					if (data.length > 0) {
						for ( var i = 0; i < data.length; i++) {
							// creating a row
							if (data[i].is_confirm == "true") {
								confirmlink = 'Confirm';
							} else {
								confirmlink = '<a href="#" class="edit-entity" onclick=\'confirm("'
										+ data[i].id + '")\'>Confirm</a>';
							}
							editlink = '<a href="#" class="edit-entity" onclick=\'edit("'
									+ data[i].id + '")\'>Edit</a>';
							htm += '<tr>';
							htm += '<td class="borright">'
									+ data[i].kwitansi_nbr
									+ '</td><td class="borright">'
									+ data[i].invoice_nbr
									+ '</td><td>'
									+ data[i].company
									+ '</td><td align="center" class="borright">'
									+ data[i].inv_period
									+ '</td><td align="center" class="borright">'
									+ data[i].create_date
									+ '</td><td align="center" class="borright">'
									+ data[i].due_date
									+ '</td><td align="right" class="borright">'
									+ data[i].total_bill + '</td>';
							htm += '<td align="center">'
									+ editlink
									+ '/<a href="#" class="edit-entity" onclick=\'detail("'
									+ data[i].id + '")\'>Detail</a>/'
									+ confirmlink + '</td></tr>';
						}
					} else {
						// condition to show message when data is not available
						var thElesLength = $('#invoice-table').length;
						htm += '<tr><td colspan="' + thElesLength
								+ '">No items found</td></tr>';
					}
					$('#invoice-tbody').html(htm);
					$("#invoice-tbody tr:odd").css("background-color",
							"#ffffff");
				},
				error : function(e) {
				}
			});
}

var addDetail = function() {
	var rowcount = $("#invoice-detail-table >tbody >tr").length;
	var htm = "";
	htm += "<tr><td><input type='text' id='description"
			+ rowcount
			+ "' name='description"
			+ rowcount
			+ "'/></td>"
			+ "<td><input style='text-align:center;' type='text' id='qty"
			+ rowcount
			+ "' name='qty"
			+ rowcount
			+ "' /></td>"
			+ "<td><input class='rt' type='text' id='price"
			+ rowcount
			+ "' name='price"
			+ rowcount
			+ "' onblur='total("
			+ rowcount
			+ ")' /></td>"
			+ "<td style='text-align: right;' id='total_price"
			+ rowcount
			+ "'></td><td style='display:none'><input type='text' id='detid"
			+ rowcount
			+ "' name='detid"
			+ rowcount
			+ "' /></td><td><a href='#' class='ui-icon ui-icon-trash ct'>Hapus</a></td></tr>";
	$("#detail-invoice-tbody").append(htm);
}

var total = function(parms) {
	$("#total_price" + parms).text(
			$("#qty" + parms).val() * $("#price" + parms).val());
	var sum = 0;
	$('#detail-invoice-tbody tr').each(function() {
		sum += parseFloat($('td:eq(3)', $(this)).text());
	});
	$("#total_detail").text(sum);
	$("#total_bill").text($("#total_detail").text());
	if($("#feecheck").prop("checked")) {
		checkfee();
	}
	if($("#ppncheck").prop("checked")) {
		checkppn();
	}
	if($("#pphcheck").prop("checked")) {
		checkpph();
	}
}

var total_detail = function() {
	var sum = 0;
	$('#detail-invoice-tbody tr').each(function() {
		sum += parseFloat($('td:eq(3)', $(this)).text());
	});
	$("#total_detail").text(sum);
}

var total1 = function() {
	if ($("#fee_management").length > 0) {
		$("#total1").text(
				parseFloat($("#total_bill").text())
						+ parseFloat($("#fee_management").val()));
		$("#total_bill").text($("#total1").text());
	}
}

var ppn;
var pph;
var tbill;

var total2 = function() {
	ppn = (parseFloat($("#total_bill").text()) * 10) / 100;
	$("#total2").text(parseFloat($("#total_bill").text()) + ppn);
	$("#total_bill").text($("#total2").text());
}

var grandtotal = function() {
	pph = parseFloat(($("#fee_management").val() * 2) / 100);
	$("#total_bill").text(parseFloat($("#total_bill").text() - pph));
}

var gt1 = function() {
	$("#total_bill").text($("#total2").text());
}

var checkfee = function() {
	if ($('#feecheck').is(':checked') == true) {
		$("#fee_management").val(
				parseFloat(($("#total_detail").text() * 10) / 100));
		$("#feeman").show();
		total1();
	} else {
		$("#feeman").hide();
	}
}

var countfee = function() {
	if ($("#fee_management").length > 0) {
		total1();
	}
}

var checkselectedmfee = function() {
	if ($("#selectmfee").val() == "manualmfee") {
		$("#manmfee").show();
	} else {
		$("#manmfee").hide();
		$("#fee_management").val(
				parseFloat(($("#total_detail").text() * 10) / 100));
	}
}

var checkppn = function() {
	if ($('#ppncheck').is(':checked') == true) {
		total2();
	} else {
		$("#total2").text("");
		$("#total_bill").text(parseFloat($("#total_bill").text()) - ppn);
	}
}

var checkpph = function() {
	if ($('#pphcheck').is(':checked') == true) {
		grandtotal();
	} else {
		tbill = tbill;
	}
}

/*
 * var detail = function(params) { //var content = "<embed id=\"pdf\"
 * src=\"/detail?id="+params+"\" width=\"100%\" height=\"100%\"
 * type=\"application/pdf\" />"; //var content = '<object id="pdf" border="0"
 * width="100%" height="100%" type="application/pdf" data="detail?id='+params+'"
 * standby="Loading pdf..." />'; var content = '<iframe id="pdf"
 * src="detail?id='+params+'" frameborder="0" width="100%" height="100%"
 * type="application/pdf"></iframe>'; $("#detail-invoice").attr("Detail
 * Invoice"); $("#detail-invoice").html(content); $("#invoice-list").hide();
 * $("#detail-invoice").show(); }
 */

// Popup dialog
var detail = function(params) {
	var content = "<embed id=\"pdf\" src=\"/detail?id=" + params
			+ "\" width=\"100%\" height=\"100%\" type=\"application/pdf\" />";
	// get the screen height and width
	var maskHeight = $(document).height();
	var maskWidth = $(window).width();

	// calculate the values for center alignment
	var dialogLeft = (maskWidth / 2) - ($('#dialog-box').width() / 2);

	// assign values to the overlay and dialog box
	$('#dialog-overlay').css({
		height : maskHeight,
		width : maskWidth
	}).show();
	$('#dialog-box').css({
		top : "10%",
		left : dialogLeft
	}).show();

	// display the message
	$('#detail-invoice').html(content);

}

var confirm = function(params) {
	var parameter = new Array();
	parameter[parameter.length] = new param('id', params);
	$.ajax({
		url : "/invoiceservlet",
		type : "GET",
		dataType : "json",
		data : parameter,
		success : function(resp) {
			var data = resp;
			$("#bank_account").text(
					data[1].bankname + " - " + data[1].accountnbr);
			$("#bank_account_id").val(data[1].id);
			$("#invoice_id").val(data[0].id);
			$("#invoice_nbr").text(data[0].invoice_nbr);
			$("#total_tagihan").val(data[0].total_bill);
			$("#invoice-list").hide();
			$("#confirm-invoice").show();
		}
	});
}

var saveconfirm = function() {
	var data = new Array();
	data[data.length] = new param("confirm", "true");
	data[data.length] = new param("id", $("#id").val());
	data[data.length] = new param("receive_date", $("#receive_date").val());
	data[data.length] = new param("receive_amount", $("#receive_amount").val());
	data[data.length] = new param("bank_account_id", $("#bank_account_id")
			.val());
	data[data.length] = new param("invoice_id", $("#invoice_id").val());
	// making the ajax call
	$.ajax({
		url : "/paymentservlet",
		type : "POST",
		data : data,
		success : function(data) {
			$("#invoice-list").show();
			$("#confirm-invoice").hide();
			$("#message").text("Data telah di konfirmasi");
			$("#reset-invoice-confirm").click();
		}
	});
}
