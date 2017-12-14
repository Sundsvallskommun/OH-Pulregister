var verified = undefined;
var invalid_element = undefined;
var verification_id = "first_verification_error";
var current_div = undefined;
var current_interval_id = undefined;
var org_class = undefined;

function verifyInputChanges(val)
{
	if ( current_div != undefined && val != "" && val != null )
	{
		$(current_div).find('div[requiredcontent-info-box="false"]').hide();
		current_div.className = org_class;		
		current_div = undefined;
		stopCurrentInterval();
	}
}

function stopCurrentInterval()
{
	if ( current_interval_id )
	{
		clearInterval(current_interval_id);
		current_interval_id = undefined;
	}
}

function verifyInput()
{	
	var elementName = this.getAttribute("name");
	var inputElement = $('[id="'+elementName+'"]');
	if ( inputElement.length === 0) {
		
		console.log("Could not find input-element to verify.");
	}
	var val = inputElement.val();
	if ( ( val == null || val == "" ) && verified )
	{
		current_div = this;
		verified = false;
		invalid_element = this;
		org_class = invalid_element.className; 
		invalid_element.className = org_class + " missingcontent";
		var currentID = invalid_element.getAttribute("id");
		invalid_element.setAttribute("id",verification_id);
		document.location.href = '#'+verification_id;
		invalid_element.setAttribute("id",currentID);
		stopCurrentInterval();
		current_interval_id = setInterval(function() { verifyInputChanges(inputElement.val()); }, 250);
		inputElement.focus();
		$(this).find('div[requiredcontent-info-box="false"]').show();
		
	}
}

function verifyAndSubmit(callback)
{	
	verified = true;
	
	clearHiddenDynamicAttributes();
	
	$('article:visible[requiredcontent="true"]').each(verifyInput)
	
	
	callback(verified);
	
	if ( verified )
	{
		$('#dynamic_attributes_form')[0].submit();
	}
}