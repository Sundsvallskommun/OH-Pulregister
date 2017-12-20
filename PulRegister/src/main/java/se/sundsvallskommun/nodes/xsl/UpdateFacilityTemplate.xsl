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
			var lastLogin = '<xsl:value-of select="NodeOwner/NodeType/name"/>';
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
    					
    					$( "article" ).each(function() {
						  if($(this).parent().attr('requiredaction')=="true"){
						  	var parentBGColor = $(this).parent().css('background-color');
						  	$(this).css('background-color',parentBGColor);
						  }
						});
						    					
    					setInterval(updateTimeLeftMessage, 30000);
    					
    					/*alert(lastLogin);*/
					});		
					
					function updateTimeLeftMessage(){
						showTimeLeftMessage(123);
						$("#time_left_message").text($.now());
					}	
					
					function showTimeLeftMessage(loginTimeRemaining)
					{
						if(loginTimeRemaining <= 300)
						{
							var timeLeftInMinutes = Math.floor(loginTimeRemaining/60);
							var message;
							if(loginTimeRemaining <60)
							{
								$("#time_left_message").text("Du kommer att loggas ut om mindre än 1 minut. Icke sparade ändringar kommer att förloras!");
								$('#save_message_top').css({background: 'red'});
							}else 
							{						
								$('#save_message_top').css({background: 'yellow'});
								if(timeLeftInMinutes == 1){ 
									message = "Du kommer att loggas ut om 1 minut.";
								}
								else
								{
							    	message = "Du kommer att loggas ut om " + timeLeftInMinutes + " minuter.";
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