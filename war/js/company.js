var init = function() {
	populateList();
}

var create = function() {	
	$("#company-list").hide();
	$("#company-create").show();
}

//parameter object definition
var param=function(name,value){
	this.name=name;
	this.value=value;
}

var save = function() {
	var data=new Array();
	// collecting the field values from the form
	 var formEleList = $('form#company-form').serializeArray();
	 for(var i=0;i<formEleList.length;i++){
		data[data.length]=new param(formEleList[i].name,formEleList[i].value);
	 }
	 //making the ajax call
	 $.ajax({
			url : "/companyservlet",
			type : "POST",
			data:data,
			success : function(data) {				
				$("#company-create").hide();
				$("#company-list").show();
				$("#message").text("Data telah di simpan");
			}
		});
	 $('#reset-company').click();
	 init();
}

var edit = function(id){
	var parameter=new Array();
	parameter[parameter.length]=new param('id',id);
	$.ajax({
		url : "/companyservlet",
		type : "GET",
		dataType : "json",
		data:parameter,
		success : function(resp) {
			var data=resp;
			var formElements = $('form#company-form :input');
			for(var i=0;i<formElements.length;i++){
				if(formElements[i].type !="button" && formElements[i].type !="reset"){
					var ele=$(formElements[i]);
					ele.val(eval('data.'+ele.attr('name')));
				}
			}
			$("#company-list").hide();
			$("#company-create").show();
		}
	});
}

var populateList = function() {
	$
			.ajax({
				url : "/companyservlet",
				type : "GET",
				dataType : "json",
				success : function(resp) {
					var data=resp;
					var htm = '';
					if (data.length > 0) {
						for ( var i = 0; i < data.length; i++) {
							// creating a row
							htm += '<tr>';
							htm += '<td class="br" align="center">' + data[i].unit_nbr + '</td><td class="br">'
									+ data[i].company_name + '</td><td class="br">'
									+ data[i].company_address + '</td><td class="br">' 
									+ data[i].company_city + '</td><td class="br">' 
									+ data[i].company_province + '</td><td class="br">' 
									+ data[i].company_phone + '</td>';
							htm += '<td align="center"><a href="#" class="edit-entity" onclick=\'edit("'
									+ data[i].id
									+ '")\'>Edit</a></td></tr>';
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