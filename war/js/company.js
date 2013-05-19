var init = function() {
	populateList();
}

var create = function() {
	$("#company-list").hide();
	$("#company-create").show();
}

// parameter object definition
var param = function(name, value) {
	this.name = name;
	this.value = value;
}

var save = function() {
	var data = new Array();
	// collecting the field values from the form
	var formEleList = $('form#company-form').serializeArray();
	for ( var i = 0; i < formEleList.length; i++) {
		data[data.length] = new param(formEleList[i].name, formEleList[i].value);
	}
	
	var row = $("#company-address-table >tbody >tr").length;
	data[data.length] = new param("address_row", row);
	for ( var i = 0; i < row; i++) {
		data[data.length] = new param("address" + i, $("#address" + i)
				.val());
		data[data.length] = new param("npwp" + i, $("#npwp" + i).val());
		data[data.length] = new param("company_city" + i, $("#company_city" + i).val());
		data[data.length] = new param("company_province" + i, $("#company_province" + i)
				.text());
		data[data.length] = new param("company_phone" + i, $("#company_phone" + i).val());
		data[data.length] = new param("addrid" + i, $("#addrid" + i).val());
	}
	// making the ajax call
	$.ajax({
		url : "/companyservlet",
		type : "POST",
		data : data,
		success : function(data) {
			$("#company-create").hide();
			$("#company-list").show();
			$("#message").text("Data telah di simpan");
		}
	});
	$('#reset-company').click();
	init();
}

var edit = function(id) {
	var parameter = new Array();
	parameter[parameter.length] = new param('id', id);
	$.ajax({
		url : "/companyservlet",
		type : "GET",
		dataType : "json",
		data : parameter,
		success : function(resp) {
			var data = resp;
			$("#unit_nbr").val(data[0].unit_nbr);
			$("#company_name").val(data[0].company_name);
			$("#company_initial").val(data[0].company_initial);
			var htm = "";
			for (var i = 1; i<data.length; i++) {
				htm += "<tr><td><input type='text' id='address" + i
				+ "' name='address" + i + "' value='"+data[i].address+"'/></td>"
				+ "<td><input type='text' id='npwp" + i + "' name='npwp"
				+ i + "' value='"+data[i].npwp+"' /></td><td><input type='text' name='company_city" 
				+ i + "' id='company_city" + i + "' value='"+data[i].company_city+"' /></td><td><input type='text' name='company_province" 
				+ i + "' id='company_province" + i + "' value='"+data[i].company_province+"' /></td><td><input type='text' name='company_phone" 
				+ i + "' id='company_phone" + i + "' value='"+data[i].company_phone+"' /></td><td style='display:none'><input type='text' id='addrid"
				+ i + "' name='addrid" + i	+ "' /></td><td><a href='delete'>Hapus</a></td></tr>";
			}
			$('#company-address-tbody').append(htm);
			$("#company-list").hide();
			$("#company-show").hide();
			$("#company-create").show();
		}
	});
}

var show = function(id,action) {
	var parameter = new Array();
	parameter[parameter.length] = new param('id',id);
	parameter[parameter.length] = new param('action',action);
	$.ajax({
		url : "/companyservlet",
		type : "GET",
		dataType : "json",
		data : parameter,
		success : function(resp) {
			var data = resp;
			if (data.length > 0) {
				$("#edit").append("<a href='#' onclick='edit("+data[0].id+")'>Edit</a>");
				$("#show-unit").text(data[0].unit_nbr);
				$("#show-nama").text(data[0].company_name);
				$("#show-init").text(data[0].company_initial);
				var htm = '';
				for (var i = 1; i<data.length; i++) {
					htm+='<tr>';
					htm+='<td class="br" align="center">'
						+ data[i].address+'<td class="br" align="center">'
						+ data[i].npwp+'</td><td class="br" align="center">'
						+ data[i].company_city+'</td><td class="br" align="center">'
						+ data[i].company_province+'</td><td align="center">'
						+ data[i].company_phone+'</td></tr>';
				}
				$('#compadd-show-tbody').html(htm);
			}
			$("#company-list").hide();
			$("#company-show").show();
		}
	})
}

var populateList = function() {
	$
			.ajax({
				url : "/companyservlet",
				type : "GET",
				dataType : "json",
				success : function(resp) {
					var data = resp;
					var htm = '';
					if (data.length > 0) {
						for ( var i = 0; i < data.length; i++) {
							// creating a row
							htm += '<tr>';
							htm += '<td class="br" align="center">'
									+ data[i].unit_nbr + '</td><td class="br"><a href="#" class="show-entity" onclick=\'show("'+data[i].id+'","show")\'>'
									+ data[i].company_name
									+ '</a></td><td class="br">'
									+ data[i].company_initial
									+ '</td>';
							htm += '<td align="center"><a href="#" class="edit-entity" onclick=\'edit("'
									+ data[i].id + '")\'>Edit</a></td></tr>';
						}
					} else {
						htm += '<tr><td colspan="7">No items found</td></tr>';
					}
					$('#company-tbody').html(htm);
				},
				error : function(e) {
				}
			});
}

var addAddress = function() {
	var rowcount = $("#company-address-table >tbody >tr").length;
	var htm = "";
	htm += "<tr><td><input type='text' id='address" + rowcount
			+ "' name='address" + rowcount + "'/></td>"
			+ "<td><input type='text' id='npwp" + rowcount + "' name='npwp"
			+ rowcount + "' /></td><td><input type='text' name='company_city" 
			+ rowcount + "' id='company_city" + rowcount + "' /></td><td><input type='text' name='company_province" 
			+ rowcount + "' id='company_province" + rowcount + "' /></td><td><input type='text' name='company_phone" 
			+ rowcount + "' id='company_phone" + rowcount + "' /></td><td style='display:none'><input type='text' id='addrid"
			+ rowcount + "' name='addrid" + rowcount	+ "' /></td><td><a href='delete'>Hapus</a></td></tr>";
	$("#company-address-tbody").append(htm);
}