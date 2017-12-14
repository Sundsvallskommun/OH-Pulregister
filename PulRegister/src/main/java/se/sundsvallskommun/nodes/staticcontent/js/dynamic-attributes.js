
var animateTransitions = false;


function handleTemplateWithParent(){
	
	var parentTemplate = $('article[templateid="'+this.getAttribute("parenttemplate")+'"]');
	if ( parentTemplate.length == 0 )
	{
		console.log("Error!");
	}
	var matchThis = $(this)[0].getAttribute("expandmatch");
	var name = parentTemplate[0].getAttribute("name");
	var inputElement = $('[id="'+name+'"]');
	var inputValue = String(inputElement.val());
	
	if ( inputElement.attr("type") == 'checkbox' ) {
		
		inputValue = inputElement.prop('checked').toString(); 
	} 

	$(this).appendTo(parentTemplate);
	
	if ( matchThis == null || ( inputValue != null && inputValue.length > 0 && inputValue.match( matchThis ) ) ){
		
		if ( animateTransitions ) {
			$(this).slideDown( "slow", function() {});
		}
		else {
			$(this).show();
		}
		
	} else {
		
		if ( animateTransitions ) {
			$(this).slideUp( "slow", function() {});
		}
		else {
			$(this).hide();
		}
	}
}


function updateDynamicAttributes(){	
	
	$('article[parenttemplate]').each( handleTemplateWithParent );
	animateTransitions = true;

}

function clearDynamicAttribute(){
	
	$(this).find("input").val("");
	$(this).find("select").val("");
	$(this).find("textarea").val("");
}

function clearHiddenDynamicAttributes(){
	
	$('article:hidden[parenttemplate]').each( clearDynamicAttribute );
}

