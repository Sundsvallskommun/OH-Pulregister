var OH = OH || {};

(function (OH) {
	OH.JSONPost = (function(action, value) {
		'use strict';
		
		var form;
		form = $('<form />', {
			action : action,
			method : 'post',
			style : 'display: none;'
		});
		
		if (typeof value !== 'undefined' && value !== null) {
			for (var prop in value) {
				appendToForm(prop, value[prop], form);
			}
		}
		
		form.appendTo('body').submit();
	});
	
	var appendToForm = (function (name, value, form) {
		name = name === undefined ? "" : name;
		
		if (typeof value === 'object') {
			for (var prop in value) {
				if ($.isArray(value)) {
					appendToForm(name, prop, form);					
				}
				
				appendToForm(name + "." + prop, value[prop], form);
			}
		}
		else {
			$('<input />', {
				type: 'hidden',
				name: name,
				value: value
			}).appendTo(form);
		}
	});
})(OH);