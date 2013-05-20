var init = function() {
	populateList();
}

var create = function() {	
	$("#account-list").hide();
	$("#account-create").show();
}

//parameter object definition
var param=function(name,value){
	this.name=name;
	this.value=value;
}

var save = function() {
	var data=new Array();
	// collecting the field values from the form
	 var formEleList = $('form#account-form').serializeArray();
	 for(var i=0;i<formEleList.length;i++){
		data[data.length]=new param(formEleList[i].name,formEleList[i].value);
	 }
	 //making the ajax call
	 $.ajax({
			url : "/accountservlet",
			type : "POST",
			data:data,
			success : function(data) {				
				$("#account-create").hide();
				$("#account-list").show();
				$("#message").text("Data telah di simpan");
			}
		});
	 $('#reset-account').click();
	 init();
}

var edit = function(id){
	var parameter=new Array();
	parameter[parameter.length]=new param('id',id);
	$.ajax({
		url : "/accountservlet",
		type : "GET",
		dataType : "json",
		data:parameter,
		success : function(resp) {
			var data=resp;
			var formElements = $('form#account-form :input');
			for(var i=0;i<formElements.length;i++){
				if(formElements[i].type !="button" && formElements[i].type !="reset"){
					var ele=$(formElements[i]);
					ele.val(eval('data.'+ele.attr('name')));
				}
			}
			$("#account-list").hide();
			$("#account-create").show();
		}
	});
}

var populateList = function() {
	$
			.ajax({
				url : "/accountservlet",
				type : "GET",
				dataType : "json",
				success : function(resp) {
					var data=resp;
					var htm = '';
					if (data.length > 0) {
						for ( var i = 0; i < data.length; i++) {
							// creating a row
							htm += '<tr>';
							htm += '<td class="br">' + data[i].bankname + '</td><td class="br" align="center">'
									+ data[i].accountnbr + '</td><td class="br">'
									+ data[i].accountname + '</td>';
							htm += '<td align="center"><a href="#" class="edit-entity" onclick=\'edit("'
									+ data[i].id
									+ '")\'>Edit</a></td></tr>';
						}
					} else {
						htm += '<tr><td colspan="7">No items found</td></tr>';
					}
					$('#account-tbody').html(htm);
					$("#account-tbody tr:odd").css("background-color",
					"#ffffff");
				},
				error : function(e) {
				}
			});
}