function getString(code){
	
	var tmp = null;
	
	$.ajax({
		url: "/locales/" + code.replace(/\./g,"-"),
		async: false,
		success: function(result){
			tmp = result;
		}
	});
	
	return tmp;
}