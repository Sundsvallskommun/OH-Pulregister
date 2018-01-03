<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="utf-8" indent="yes" />

	<xsl:template name="UpdateNode">
	
		<script type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/form-verification.js"></script>
		<script type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/dynamic-attributes.js"></script>
			
		<script>
			var isAdding = false;
			var isUpdating = true;
			
			var sessionTimeout = <xsl:value-of select="sessionTimeout"></xsl:value-of>;
			function getCurrentLogin(){ return new Date(<xsl:value-of select="sessionLastAccess"></xsl:value-of>);}
		</script>

		<script>
					<![CDATA[
					
					function afterVerification(verified)
					{
						mapClient.map.updateSize();
						console.log("update map");
					}
					
					function onClickSubmit()
					{
						
						if (typeof mapInitialized !== 'undefined' && mapInitialized ){
						 
							updateGeo();
							verifyAndSubmit(afterVerification);
						}
						else verifyAndSubmit(function(){});
												
					}
					
					$( document ).ready(function() {
    					updateDynamicAttributes();
    					
    					$("input").on("change", function(event) { 
				     		if($(this).parent().text().trim() == "Åtgärder krävs"){
				     			var parentArticle =  $(this).closest("article").parent("article");
				     			if($(this).closest("li").attr("class") == "active"){
						     		updateRequiredAction(parentArticle,1);
					     		}
					     		else{
					     			updateRequiredAction(parentArticle,0);
					     		}
				     		}
						} );
						$( "input" ).each(function() {
							if($(this).parent().text().trim() == "Åtgärder krävs"){
				     			var parentArticle =  $(this).closest("article").parent("article");
				     			if($(this).closest("li").attr("class") == "active"){
						     		updateRequiredAction(parentArticle,1);
					     		}
				     		}		
						});
										
    					setInterval(updateTimeLeftMessage, 10000);

					});		
					
					
					function updateRequiredAction(updatedArticle,isChecked){
						var BGColor;
						if(isChecked){
							BGColor = "#f7f777";
							updatedArticle.children(".notes-div").first().slideDown();
						}else{
							BGColor = "#f7f7f7";
						}
						updatedArticle.css('background-color',BGColor);
						updatedArticle.find("article").css('background-color',BGColor);
						updatedArticle.find(".line-divider").css('border', '1px '+BGColor);
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
							if(loginTimeRemaining <0){
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

					]]>
				</script>
	
		<xsl:call-template name="DynamicAttributesInit"/>
		<xsl:call-template name="AddMapGeoDataVariable"/>

		<div class="container">

			<xsl:if test="validationException">
				<div class="alert alert-danger" role="alert">
					<xsl:apply-templates select="validationException/validationError" />
				</div>
			</xsl:if>

			<h2>
				Uppdatera 				 
				<xsl:value-of select="NodeOwner/NodeType/name"></xsl:value-of>	
			</h2>
			<div id="save_message_top" style="background:white;width:auto;position:sticky;top:0;z-index:2;">
				<div class="panel-body">
					<span id="time_left_message"></span>
					<span onclick="onClickSubmit()" class="btn btn-success pull-right">
						<span class="glyphicon glyphicon-edit"></span>
						Uppdatera
					</span>
				</div>
			</div>
			<form id="dynamic_attributes_form" class="form-horizontal" role="form" action="{/document/requestinfo/url}"
				method="post" enctype="multipart/form-data">
				
				<xsl:call-template name="AddFacilityForm" />
				<xsl:call-template name="IterateUpdateTemplateAttributes" />
				
				

				<input type="hidden" id="type" name="type" class="form-control"
					value="{NodeOwner/NodeType/type_id}" />

				<div class="panel-body">
					<span onclick="onClickSubmit()" class="btn btn-success pull-right">
						<span class="glyphicon glyphicon-edit"></span>
						Uppdatera
					</span>
				</div>
				
				<xsl:apply-templates select="validationException/validationError" />
			</form>
		</div>
	</xsl:template>
	
	<xsl:template name="IterateUpdateTemplateAttributes">
		 
		<xsl:for-each
			select="NodeOwner/NodeAttributes/NodeAttribute">
			
			<xsl:call-template name="HandleTemplateAttributeType" />

		</xsl:for-each>
		 
		<!-- <xsl:apply-templates select="NodeTypes" />-->
	</xsl:template>

	

</xsl:stylesheet>