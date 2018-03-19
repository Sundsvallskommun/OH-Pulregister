<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="utf-8" indent="yes" />

	<xsl:template name="AddNode">
	
		<script type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/form-verification.js"></script>
		<script type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/dynamic-attributes.js"></script>
	
		<script>
			var isAdding = true;
			var isUpdating = false;
			
		</script>
		
		<xsl:call-template name="FormScripts"/>
		<xsl:call-template name="DynamicAttributesInit"/>		
		
		<div class="container">

			<xsl:if test="validationException">
				<div class="alert alert-danger" role="alert">
					<xsl:apply-templates select="validationException/validationError" />
				</div>
			</xsl:if>

			<h2>
				Lägg till
				<xsl:value-of select="NodeTypes/NodeType/name"></xsl:value-of>
			</h2>		
			<div id="save_message_top" name="save_message_top" style="background:white;width:auto;position:fixed;position:sticky;top:0;z-index:2;">
				<div class="panel-body">
					<span id="time_left_message"></span>
					<span onclick="onClickSubmit()" class="btn btn-success pull-right">
						<span class="glyphicon glyphicon-edit"></span>
						Lägg till
					</span>
				</div>
			</div>
			<form id="dynamic_attributes_form" class="form-horizontal" role="form" action="{/document/requestinfo/url}"
				method="post" enctype="multipart/form-data" onsubmit="return false;">	
								
				<xsl:call-template name="AddFacilityForm" />
				<xsl:call-template name="IterateTemplateAttributes" />

				<input type="hidden" id="type" name="type" class="form-control"
					value="{NodeTypes/NodeType/type_id}" />


				<div class="panel-body">
					<span onclick="onClickSubmit()" class="btn btn-success pull-right">
						<span class="glyphicon glyphicon-plus"></span>
						Lägg till
					</span>
				</div>
				
				<xsl:apply-templates select="validationException/validationError" />
											
			</form>
		</div>
	</xsl:template>
	
	<xsl:template name="ApplyAttributeHeaderStyle">
	
	</xsl:template>
	
	<xsl:template name="ApplyAttributeFooterStyle">
	
	</xsl:template>

	<xsl:template name="AddFacilityForm">
	
		<article requiredcontent="true" name="name">
		
			<!-- 
			<xsl:call-template name="AddMenuButton"/>
			 -->
			
			<xsl:call-template name="AddHiddenRequiredInfoBox"/>
			
			<xsl:call-template name="ApplyAttributeDescription">
				<xsl:with-param name="forceRequired">true</xsl:with-param>
			</xsl:call-template>
			
			<div>
				<div class="col-xs-9 col-sm-7 col-md-7 col-lg-6">
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="'name'" />
							<xsl:with-param name="name" select="'name'" />
							<xsl:with-param name="value" select="NodeOwner/name" />
							<xsl:with-param name="placeholder" select="description" />
							<xsl:with-param name="class" select="'form-control'" />
							<xsl:with-param name="width" select="'100%'" />
							<xsl:with-param name="maxlength" select="'50'" />
						</xsl:call-template>
					
				</div>
			</div>
			
			<div class="vspace20"/>
			<div class="vspace20"/>
			
		</article>

	</xsl:template>
	
	<xsl:template name="AddHiddenRequiredInfoBox">
		<div requiredcontent-info-box="false" class="panel panel-body alert-danger" style="display:none;">Du måste fylla i detta fält.</div>
	</xsl:template>
	
	
	<xsl:template name="AddMenuButton">
		
		<a data-toggle="dropdown" href="javascript:void(0);" title="Alternativ"><div name="hamburger-menu-button" style="color:rgba(0,0,0,0.2);" onclick="" class="upper-left-corner glyphicon glyphicon-menu-hamburger"></div></a>
		
		<xsl:if test="note != '' ">		
			<a href="#" onclick="$('[id=&quot;{AttributeDetails}-notes-div&quot;]').show();return false;" id="{AttributeDetails}-noteIcon"><div title="{note}" style="color:rgba(0,0,0,0.2);padding-right: 8px;" onclick="" class="upper-left-corner glyphicon glyphicon-comment"></div></a>
		</xsl:if>
		
		<ul class="dropdown-menu upper-left-corner">
	      <li><a href="#" onclick="$('[id=&quot;{AttributeDetails}-notes-div&quot;]').show();return false;"><span class="glyphicon glyphicon-pencil" style="padding-right: 5px;"/>Anteckningar</a></li>
	      <li><a href="#" onclick="$('[id=&quot;{AttributeDetails}-notes-div&quot;]').hide();$('[id=&quot;{AttributeDetails}|note&quot;]').val('');$('[id=&quot;{AttributeDetails}-noteIcon&quot;]').hide();return false;"><span class="glyphicon glyphicon-remove" style="padding-right: 5px;"/>Rensa</a></li>	 
	    </ul> 
	</xsl:template>
	
	<xsl:template name="AddNotesSection">
		
		<div id="{AttributeDetails}-notes-div" style="display:none;" class="notes-div">
			<h3 class="pull-right">Anteckningar <a href="javascript:void(0);" onclick="$('[id=&quot;{AttributeDetails}-notes-div&quot;]').slideUp();"><span title="Dölj" class="glyphicon glyphicon-chevron-up pull-right" style="color:rgba(0,0,0,0.2);padding-left: 5px;"/></a> </h3>
			
			<textarea type="text" id="{AttributeDetails}|note" name="{AttributeDetails}|note" rows="5" style="width: 100%; resize:vertical; margin-top: 0px; margin-bottom: 0px;"><xsl:value-of select="note"/></textarea>			
		</div>
		<input type="hidden" id="{AttributeDetails}|note" name="{AttributeDetails}|note" value="" />	
		
	</xsl:template>
	

	<xsl:template name="HandleTemplateAttributeType">
	
		<article requiredcontent="{required}" name="{AttributeDetails}" >
			
			<xsl:if test="NodeTemplateAttribute/parentTemplateAttribute">
				<xsl:attribute name="parentTemplate"><xsl:value-of select="NodeTemplateAttribute/parentTemplateAttribute"/></xsl:attribute>
			</xsl:if>			
			<xsl:if test="parentTemplateAttribute">
				<xsl:attribute name="parentTemplate"><xsl:value-of select="parentTemplateAttribute"/></xsl:attribute>
			</xsl:if>
			
			<xsl:if test="NodeTemplateAttribute/templateAttributeID">
				<xsl:attribute name="templateID"><xsl:value-of select="NodeTemplateAttribute/templateAttributeID"/></xsl:attribute>
				<xsl:attribute name="id"><xsl:text>viewTemplateAttribute</xsl:text><xsl:value-of select="NodeTemplateAttribute/templateAttributeID"/></xsl:attribute>
			</xsl:if>			
			<xsl:if test="templateAttributeID">
				<xsl:attribute name="templateID"><xsl:value-of select="templateAttributeID"/></xsl:attribute>
				<xsl:attribute name="id"><xsl:text>viewTemplateAttribute</xsl:text><xsl:value-of select="templateAttributeID"/></xsl:attribute>
			</xsl:if>
			
			<xsl:choose>
			
				<xsl:when test="NodeTemplateAttribute/showOnlyWhenValueEquals">
					<xsl:attribute name="expandmatch"><xsl:value-of select="NodeTemplateAttribute/showOnlyWhenValueEquals"/></xsl:attribute>
				</xsl:when>
				
				<xsl:when test="templateAttributeID/../showOnlyWhenValueEquals">
					<xsl:attribute name="expandmatch"><xsl:value-of select="showOnlyWhenValueEquals"/></xsl:attribute>
				</xsl:when>
				<xsl:when test="NodeTemplateAttribute/parentTemplateAttribute"/>
				<xsl:when test="parentTemplateAttribute"/>
				<xsl:otherwise>
					<xsl:call-template name="AddMenuButton"/>
				</xsl:otherwise>	
			
			</xsl:choose>
			
			<!--  Remember that attributes needs to be above -->
			
			
			<xsl:call-template name="AddNotesSection"/>
			
			
			<xsl:if test="required='true'">		
				<xsl:call-template name="AddHiddenRequiredInfoBox"/>
			</xsl:if>			
			
			
			<xsl:if test="type='FILES'">
				<xsl:call-template name="FileType" />
			</xsl:if>
			<xsl:if test="type='ENUM'">
				<xsl:call-template name="EnumType" />
			</xsl:if>
			<xsl:if test="type='MULTISELECT'">
				<xsl:call-template name="MultiSelectType" >
					<xsl:with-param name="multi">true</xsl:with-param>
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="type='SINGLESELECT'">
				<xsl:call-template name="MultiSelectType"/>
			</xsl:if>
			<xsl:if test="type='INFO'">
				<xsl:call-template name="InfoType" />
			</xsl:if>
			<xsl:if test="type='STRING'">
				<xsl:call-template name="StringType" />
			</xsl:if>
			<xsl:if test="type='SPECIALSTRING'">
				<xsl:call-template name="SpecialStringType" />
			</xsl:if>
			<xsl:if test="type='STRINGBOX'">
				<xsl:call-template name="StringBoxType" />
			</xsl:if>			
			<xsl:if test="type='PHONE'">
				<xsl:call-template name="PhoneType" />
			</xsl:if>
			<xsl:if test="type='DATE'">
				<xsl:call-template name="DateType" />
			</xsl:if>
			<xsl:if test="type='YEAR'">
				<xsl:call-template name="YearType" />
			</xsl:if>
			<xsl:if test="type='BOOL'">
				<xsl:call-template name="BoolType" />
			</xsl:if>
			<xsl:if test="type='INTEGER'">
				<xsl:call-template name="IntegerType" />
			</xsl:if>
			<xsl:if test="type='UINTEGER'">			
				<xsl:call-template name="IntegerType" />
			</xsl:if>		
			<xsl:if test="type='TAGS'">
				<xsl:call-template name="TagsType" />
			</xsl:if>
			<xsl:if test="type='URL'">
				<xsl:call-template name="UrlType" />
			</xsl:if>
			
			<script>
				$("[id='<xsl:value-of select="AttributeDetails"/>']").on("change", function(event) { updateDynamicAttributes(this);} );
			</script>
			
		</article>
		
	</xsl:template>
	
	<xsl:template name="RequiredHeader">
		<a href="javascript:void(0)"><p class="pull-left" title="Obligatorisk" ><span class="glyphicon glyphicon-asterisk" style="color:#993333;font-size:small;"></span></p></a>
	</xsl:template>
	
	<xsl:template name="ApplyAttributeDescription">
		
		<xsl:param name="forceRequired" select="false"/>
	
		<div>			
			<xsl:if test="required='true'">
				<xsl:call-template name="RequiredHeader"/>
			</xsl:if>
			<xsl:if test="$forceRequired='true'">
				<xsl:call-template name="RequiredHeader"/>
			</xsl:if>
			<h3>
				<xsl:choose>
					<xsl:when test="name">
						<xsl:value-of select="name" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>Namn</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</h3>
		</div>		
		<xsl:if test="type!='STRING'">
			<div class="line-divider"/>
			<div class="panel-body">
			 	<xsl:choose>
			 		<xsl:when test="string-length(./NodeTemplateAttribute/description)&gt;0">
			 			<div class="vspace20"/>
						<xsl:value-of select="NodeTemplateAttribute/description" disable-output-escaping="yes"/>
						
			 		</xsl:when>
			 		<xsl:otherwise>
			 			<xsl:if test="string-length(description)&gt;0">
			 				<div class="vspace20"/>
							<xsl:value-of select="description" disable-output-escaping="yes"/>
							
			 			</xsl:if>
			 		</xsl:otherwise>		 		
			 	</xsl:choose>			
			</div>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="ApplyAttributeTooltip">
		<xsl:if test="string-length(./NodeTemplateAttribute/description)&gt;0">
			<a href="#" data-toggle="tooltip" title="{NodeTemplateAttribute/description}"><div onclick='' class='glyphicon glyphicon-info-sign'/></a>
		</xsl:if>
		<xsl:if test="string-length(description)&gt;0">
			<a href="#" data-toggle="tooltip" title="{description}"><div onclick='' class='glyphicon glyphicon-info-sign'/></a>
		</xsl:if>		
	</xsl:template>

	<xsl:template match="ParentNodeType|NodeType">

		<xsl:apply-templates select="ParentNodeType" />
		<h4>
			<xsl:value-of select="name" />
		</h4>
		<div class="container">
			<xsl:for-each select="NodeTemplateAttributes/NodeTemplateAttribute">

				<xsl:call-template name="HandleTemplateAttributeType" />

			</xsl:for-each>
			<xsl:for-each select="NodeAttributes/NodeAttribute">

				<xsl:call-template name="HandleTemplateAttributeType" />

			</xsl:for-each>
			
		</div>
	</xsl:template>

	<xsl:template name="IterateTemplateAttributes">
		<xsl:for-each
			select="NodeTypes/NodeType/NodeTemplateAttributes/NodeTemplateAttribute">

			<xsl:call-template name="HandleTemplateAttributeType" />

		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="NodeTags">
	</xsl:template>
	
	<xsl:template name="FormScripts">
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
					verifyAndSubmit(afterVerification);
				}
				else verifyAndSubmit(function(){});
			}
			
			$( document ).ready(function() {
  				updateDynamicAttributes();
			});	
			]]>
		
			var sessionTimeout = <xsl:value-of select="sessionTimeout"></xsl:value-of>;
			function getCurrentLogin(){ return new Date(<xsl:value-of select="sessionLastAccess"></xsl:value-of>);}
			<![CDATA[
			$( document ).ready(function() {
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
			]]>
		</script>
	</xsl:template>

</xsl:stylesheet>