$(document).ready(function() {
	 
	 $(".usergroup-list").each(function(){
		 initUserGroupList($(this));
	 });
	 
	 $(".readonly-usergroup-list li:odd").addClass("lightbackground");
	 
});

function initUserGroupList(list){
	
	if(list.hasClass("readonly-usergroup-list")){
		
		list.addClass("lightbackground");
		return;
	}
	
	var prefix = getUserGroupPrefix(list);
	var $url = list.find("input[name='connectorURL']").val();
	var template = getUserGroupTemplate(prefix);
	
	var $searchInput = $( "#" + prefix + "-search");
	
	if($searchInput.data('ui-autocomplete') != undefined){
		return;
	}
	 
	$searchInput.autocomplete({
		source: function(request, response) {
			return searchUsersAndGroups(request, response, $url, $searchInput, template);
		},
		
		select: function( event, ui ) {
			addUserGroupEntry(ui.item, list, prefix);
			$(this).val("");
			
			return false;
		},
		
		focus: function(event, ui) {
	       event.preventDefault();
	   }
	});
	
	var $entries = list.find("li");
	
	$entries.each(function() {
		var $entry = $(this);
		initUserGroupDeleteButton($entry, list, prefix);
	}); 
	
	list.bind("change", function() {
		list.find("li").removeClass("lightbackground");
		list.find("li:odd").addClass("lightbackground");
	});
	
	list.trigger("change");
}

function getUserGroupPrefix($list){
	return $list.find("input[name='prefix']").val();
}

function getUserGroupType($list){
	return $list.find("input[name='type']").val();
}

function searchUsersAndGroups(request, response, $searchURL, $searchInput, template) {
	
	$searchInput.addClass("ui-autocomplete-loading");
	
	$.ajax({
		url : $searchURL,
		dataType : "json",
		contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		data : {
			q : encodeURIComponent(request.term)
		},
		success : function(data) {
			
			if(data.hits != undefined && data.hits.length > 0) {
				
				response($.map(data.hits, function(item) {
					
					return {
						label : getUserGroupLabel(template, item),
						value : item.ID,
						Name  : item.Name,
						ID : item.ID,
						Email : item.Email,
						Username : item.Username
					}
				}));
			} else {
				response(null);
			}
			
			$searchInput.removeClass("ui-autocomplete-loading");
			
		},
		error : function() {
			
			$searchInput.removeClass("ui-autocomplete-loading");
		}
	});
}

function getUserGroupTemplate(prefix){
	return $("#" + prefix + "-template");
}

function addUserGroupEntry(item, $list, prefix, template){
	
	if($("#" + prefix + "_" + item.value).length > 0) {
		return;
	}
	
	var $clone = getUserGroupTemplate(prefix).clone();
	
	var label = getUserGroupLabel($clone, item);
	
	$clone.find("span.text").text(label);
	$clone.find("input[name='" + prefix + "']").removeAttr("disabled").val(item.value);
	$clone.find("input[name='" + prefix + "-name']").removeAttr("disabled").attr('name', prefix + "-name" + item.value).val(label);
	$clone.attr("id", prefix + "_" + item.value);
	$clone.attr("class", prefix + "-list-entry");
	
	var $deleteButton = initUserGroupDeleteButton($clone, $list, prefix);
	$deleteButton.attr("title", $deleteButton.attr("title") + " " + label);
	
	$list.find("li:last").before($clone);
	$clone.show();
	
	$list.trigger("change");
}

function getUserGroupLabel($template, item){
	var label = item.Name;
	
	var showUsername = $template.hasClass("show-username-true") && item.Username;
	var showEmail = $template.hasClass("show-email-true") && item.Email;
	
	if(showUsername){
		label += " (" + item.Username;
	}
	
	if(showEmail){
		if(showUsername){
			label += ", ";
		} else {
			label += " (";
		}
		label += item.Email;
	}
	
	if(showUsername || showEmail){
		label += ")";
	}
	
	return label;
}

function initUserGroupDeleteButton($entry, $list, prefix) {
	
	var $deleteButton = $entry.find("a.delete");
	
	$deleteButton.click(function(e) {
		e.preventDefault();
//		$("#" + prefix + "_" + $entry.find("input[type='hidden']").val()).removeClass("disabled").show();
		$entry.remove();
		
		$list.trigger("change");
		
	});
	
	return $deleteButton;
}
