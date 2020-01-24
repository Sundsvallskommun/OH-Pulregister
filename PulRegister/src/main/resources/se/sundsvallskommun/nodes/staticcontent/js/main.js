
function is_running_msie() {

    var ua = window.navigator.userAgent;
    var msie = ua.indexOf("MSIE ");

    if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))  // If Internet Explorer, return version number
    {
        return true;
    }
    else  // If another browser, return 0
    {
        return false;
    }
}


function afterVerification(verified){
	mapClient.map.updateSize();
	console.log("update map");
}

function onClickSubmit(){
	if (typeof mapInitialized !== 'undefined' && mapInitialized ){
		verifyAndSubmit(afterVerification);
		console.log("something wrong with the map");
	}
	else {
		$('#fileselect').removeAttr('disabled');
		verifyAndSubmit(function(){});
	}
}


function updateTimeLeftMessage(){
	currentLogin = getCurrentLogin();
	currentTime = new Date($.now());
	timeSinceLogin = Math.floor((currentTime - currentLogin)/(1000));
	timeLeft = sessionTimeout - timeSinceLogin;
	showTimeLeftMessage(timeLeft);
}	
	
function showTimeLeftMessage(loginTimeRemaining)
{
	if(loginTimeRemaining <= 5*60)
	{
		var message;
		if(loginTimeRemaining < 0){
			$("#time_left_message").html(' <a target="_blank" href="/login"><font color="black"><b>Du har blivit utloggad. <i>Klicka här</i> för att logga in igen innan du sparar.</b></font></a> ');
			$('#save_message_top').css({background: 'red'});
		}
		else if(loginTimeRemaining <1*60)
		{
			$("#time_left_message").text("Du kommer att loggas ut om mindre än 1 minut.");
			$('#save_message_top').css({background: 'orange'});
		}else 
		{						
			$('#save_message_top').css({background: 'yellow'});
			if(Math.floor((loginTimeRemaining)/(60)) == 1){ 
				message = "Du kommer att loggas ut om 1 minut.";
			}
			else
			{
		    	message = "Du kommer att loggas ut om " + Math.floor((loginTimeRemaining)/(60)) + " minuter."; 
	    	}
			$("#time_left_message").text(message);
		}
	    
	}
	else
	{
		$('#save_message_top').css({background: 'white'});
		$("#time_left_message").text("");
	}
}

function clearNote(noteTextId){
	var noteTextArea = $("textarea[id='" + noteTextId + "']");
	noteTextArea.val("");
}


$(document).ready(function(){
	
	updateDynamicAttributes();
	
	//Slides down the notes field and changes the background-color of the article and the child articles and the line-dividers.
	$("[id$='-notes']").click(function() {		
		
		var currentId = this.id;
		var questionNumber = currentId.match(/\d/g);
		var articleId = "#viewTempletAttribute" + questionNumber;
		var BGColor;
		if($(this).is(":checked")) {
			$("[id='"+currentId+"-div']").slideDown();
			$(this).attr('checked', 'checked');
			BGColor = "#f7f777";
		} else {
			$("[id='"+currentId+"-div']").slideUp();
			$(this).removeAttr('checked');
			BGColor = "#f7f7f7";			
		}
		$(articleId).css('background-color', BGColor);
		$(articleId + " article").css('background-color', BGColor);
		$(articleId + " .line-divider").css('border', '1px ' + BGColor);

	});
	

	$(".arrow").click(function(){
		var attributeDetails = this.id.replace("-rotate", "");
		var id = attributeDetails.replace("question-", "");
		var panel = '#' + attributeDetails.concat("-", "question");
		
		if(id == '24') {
			var div = $('#question-45-question');
			$(panel).toggle();
			$(div).toggle();
			$(this).toggleClass('rotated');

		} else if(id == 7) {
			$(panel).toggle();
			$('#viewTempletAttribute65').toggle();
			$('#viewTempletAttribute29').toggle();
			$('#viewTempletAttribute30').toggle();
			$('#viewTempletAttribute31').toggle();
			$('#viewTempletAttribute32').toggle();
			$('#viewTempletAttribute33').toggle();
			$('#viewTempletAttribute34').toggle();
			$('#viewTempletAttribute35').toggle();
			$(this).toggleClass('rotated');
			
		} else {
			$(panel).toggle();
			$(this).toggleClass('rotated');
			
		}
	});
	
});