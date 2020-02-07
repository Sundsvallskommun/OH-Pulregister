<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="utf-8" indent="yes" />

	<xsl:template name="AddForm">
	
		<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/form-verification.js"/>
		<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/dynamic-attributes.js"/>
		<script type="text/javascript" src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/filehandler.js"/>
		<script>
			var isAdding = true;
			var isUpdating = false;
		</script>
		<xsl:call-template name="FormScripts" />
		<xsl:call-template name="DynamicAttributesInit" />
		<div class="container" id="form">
			<xsl:if test="validationException">
				<div class="alert alert-danger" role="alert">
					<xsl:apply-templates select="validationException/validationError" />
				</div>
			</xsl:if>
			<h2>
				Lägg till nytt <xsl:value-of select="FormType"/>
			</h2>
			<div id="save_message_top" name="save_message_top" style="background:white;width:auto;position:fixed;position:sticky;top:0;z-index:2;">
				<div class="panel-body">
					<span id="time_left_message" />
					<span onclick="onClickSubmit()" class="btn btn-success pull-right">
						<span class="glyphicon glyphicon-edit" />
						Lägg till
					</span>
					<button type="button" onclick="location.href='{/document/requestinfo/currentURI}/{/document/module/alias}/formlist'; return false" class="btn btn-info pull-right">
						<span class="glyphicon glyphicon-list" />
						&#160;Återgå
					</button>
				</div>
			</div>
			<form id="dynamic_attributes_form" class="form-horizontal" role="form" action="{/document/requestinfo/url}" method="post" enctype="multipart/form-data" onsubmit="return false;">
				<xsl:call-template name="AddFormNameTemplate" />
				<xsl:call-template name="IterateTemplateAttributes" />
					<input type="hidden" id="type" name="type" class="form-control" value="{NodeTypes/NodeType/type_id}" />
						<div class="panel-body" id="submitButtonBottom">
							<span onclick="onClickSubmit()" class="btn btn-success pull-right">
								<span class="glyphicon glyphicon-plus" />
								Spara
							</span>
							<span class="hspace20"/>
						</div>
				<xsl:apply-templates select="validationException/validationError" />
			</form>
		</div>
	</xsl:template>

	<xsl:template name="ApplyAttributeHeaderStyle"/>
	<xsl:template name="ApplyAttributeFooterStyle"/>

	<!-- Article Name static -->
	<xsl:template name="AddFormNameTemplate">
		<xsl:param name="formName" select="value" />
		<article requiredcontent="true" name="name">

			<xsl:call-template name="AddHiddenRequiredInfoBox" />
			<div id="questionNamnStatic">
				<xsl:call-template name="RequiredHeader" />
				<h3>
					<xsl:text>Namn</xsl:text>
				</h3>
			</div>

			<div>
				<div class="col-xs-9 col-sm-7 col-md-7 col-lg-6">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="'name'" />
						<xsl:with-param name="name" select="'name'" />
						<xsl:with-param name="value" select="$formName" />
						<xsl:with-param name="placeholder" select="description" />
						<xsl:with-param name="class" select="'form-control'" />
						<xsl:with-param name="width" select="'100%'" />
						<xsl:with-param name="maxlength" select="'50'" />
					</xsl:call-template>
				</div>
			</div>
			<div class="vspace20" />
			<div class="vspace20" />
		</article>
	</xsl:template>

	<!-- alerts questions required -->
	<xsl:template name="AddHiddenRequiredInfoBox">
		<div requiredcontent-info-box="false" class="panel panel-body alert-danger" style="display:none;">Du måste fylla i detta fält.</div>
	</xsl:template>

	<!-- Meny at the top right of article -->
	<xsl:template name="AddMenuButton">
		<div title="Ändra" id="{AttributeDetails}-rotate" class="arrow" />
		<a title="Alternativ" data-toggle="dropdown" href="javascript:void(0);">
			<div name="hamburger-menu-button" style="color:rgba(0,0,0,0.2);" onclick="" class="upper-left-corner glyphicon glyphicon-menu-hamburger" />
		</a>
		<xsl:if test="note != '' ">
			<a href="#" onclick="$('[id=&quot;{AttributeDetails}-notes-div&quot;]').show();return false;" id="{AttributeDetails}-noteIcon">
				<div title="{note}" style="color:rgba(0,0,0,0.2);padding-right: 8px;" onclick="" class="upper-left-corner glyphicon glyphicon-comment"></div>
			</a>
		</xsl:if>
		<ul class="dropdown-menu upper-left-corner">
			<li>
				<a href="#" onclick="$('[id=&quot;{AttributeDetails}-notes-div&quot;]').show();return false;">
					<span class="glyphicon glyphicon-pencil" style="padding-right: 5px;" />
					Anteckningar
				</a>
			</li>
			<li>
				<a href="#"	onclick="$('[id=&quot;{AttributeDetails}-notes-div&quot;]').hide();$('[id=&quot;{AttributeDetails}|note&quot;]').val('');$('[id=&quot;{AttributeDetails}-noteIcon&quot;]').hide();return false;">
					<span class="glyphicon glyphicon-remove" style="padding-right: 5px;" />
					Rensa
				</a>
			</li>
		</ul>
	</xsl:template>

	<xsl:template name="AddNotesSection">
		<div id="{AttributeDetails}-notes-div" style="display:none;" class="notes-div">
			<h3 class="pull-right">
				Anteckningar
				<a href="javascript:void(0);" onclick="$('[id=&quot;{AttributeDetails}-notes-div&quot;]').slideUp();">
					<span title="Dölj" class="glyphicon glyphicon-chevron-up pull-right" style="color:rgba(0,0,0,0.2);padding-left: 5px;" />
				</a>
			</h3>
			<textarea type="text" id="{AttributeDetails}|note" name="{AttributeDetails}|note" rows="5" style="width: 100%; resize:vertical; margin-top: 0px; margin-bottom: 0px;">
				<xsl:value-of select="note" />
			</textarea>
		</div>
		<!-- <input type="hidden" id="{AttributeDetails}|note" name="{AttributeDetails}|note" value="" /> -->
	</xsl:template>
	<!-- Draws parent elements with attributes calls templates to add child elements and questions types -->
	<xsl:template name="HandleTemplateAttributeType">
		<xsl:param name="selectedValue" />
		<article name="{AttributeDetails}" requiredcontent="{Required}">
			
			<xsl:if test="Parent">
				<xsl:attribute name="parentTemplate">
					<xsl:value-of select="Parent"/>
				</xsl:attribute>
			</xsl:if>

			<xsl:if test="ID">
				<xsl:attribute name="id">
					<xsl:text>viewTempletAttribute</xsl:text>
					<xsl:value-of select="ID" />
				</xsl:attribute>

				<xsl:attribute name="templateID">
					<xsl:value-of select="ID" />
				</xsl:attribute>
			</xsl:if>

			<xsl:choose>
				<xsl:when test="Option">
					<xsl:attribute name="expandmatch">
						<xsl:value-of select="Option" />
					</xsl:attribute>
				</xsl:when>
				<xsl:when test="Parent" />
				<xsl:otherwise>
					<xsl:if test="Required='false'">
						<xsl:call-template name="AddMenuButton"></xsl:call-template>
						<xsl:call-template name="AddNotesSection"></xsl:call-template>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>

			<!-- QuestionHeader -->
			<div id="questionHeader">
				<xsl:if test="Required='true'">
					<xsl:call-template name="RequiredHeader" />
				</xsl:if>
				<h3>
					<xsl:choose>
						<xsl:when test="Name">
							<xsl:value-of select="Name" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>Namn</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</h3>
			</div>
			
			<xsl:call-template name="QuestionType">
				<xsl:with-param name="selectedValue" select="$selectedValue" />
			</xsl:call-template>

			<script>
				$("[id='<xsl:value-of select="AttributeDetails" />']").on("change", function(event) {
				updateDynamicAttributes(this);
				});
			</script>
			
			<!-- Draws child elements connected to a parent and sends eventual selected values to the ENUM types -->
			<xsl:variable name="parentId" select="ID" />
			<xsl:for-each select="../Question">
				<xsl:if test="Parent=$parentId">
					<xsl:call-template name="HandleTemplateAttributeType">
						<xsl:with-param name="selectedValue" select="QuestionOptions/QuestionOption/selected/../value" />
					</xsl:call-template>
				</xsl:if>
			</xsl:for-each>
			<xsl:if test="Required='false'">
				<xsl:if test="not(Parent)">
					<xsl:call-template name="ActionRequiredType" />
				</xsl:if>
			</xsl:if>
		</article>
	</xsl:template>
	<!-- draws asterisk for required elements -->
	<xsl:template name="RequiredHeader">
		<a href="javascript:void(0)">
			<p class="pull-left" title="Obligatorisk">
				<span class="glyphicon glyphicon-asterisk" style="color:#993333;font-size:small;"></span>
			</p>
		</a>
	</xsl:template>
	<!-- draws description for elements -->
	<xsl:template name="ApplyAttributeDescription">
		<xsl:param name="forceRequired" select="false" />
		<xsl:if test="QuestionType/Type!='STRING'">
			<div class="line-divider" />
			<div class="panel-body">
				<xsl:choose>
					<xsl:when test="string-length(./Question/Description)&gt;0">
						<div class="vspace20" />
						<xsl:value-of select="Question/Description" disable-output-escaping="yes" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="string-length(Description)&gt;0">
							<div class="vspace20" />
							<xsl:value-of select="Description" disable-output-escaping="yes" />
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</div>
		</xsl:if>
	</xsl:template>
	<!-- draws asterisk for required elements -->
	<xsl:template name="ApplyAttributeTooltip">
		<xsl:if test="string-length(./NodeTemplateAttribute/description)&gt;0">
			<a href="#" data-toggle="tooltip" title="{NodeTemplateAttribute/description}">
				<div onclick='' class='glyphicon glyphicon-info-sign' />
			</a>
		</xsl:if>
		<xsl:if test="string-length(description)&gt;0">
			<a href="#" data-toggle="tooltip" title="{description}">
				<div onclick='' class='glyphicon glyphicon-info-sign' />
			</a>
		</xsl:if>
	</xsl:template>
	<!-- Iterates over question elements calls template for all parent elements -->
	<xsl:template name="IterateTemplateAttributes">
		<xsl:for-each select="Forms/Question">
			<xsl:if test="not(Parent)">
				<xsl:call-template name="HandleTemplateAttributeType" />
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<!-- Draws elements depending of question types -->
	<!-- Remember that attributes needs to be above -->
	<xsl:template name="QuestionType">

		<xsl:param name="selectedValue" select="null" />

		<div id="{AttributeDetails}-question" class="question">

			<xsl:if test="Required='true'">
				<xsl:call-template name="AddHiddenRequiredInfoBox" />
			</xsl:if>

			<xsl:if test="QuestionType/Type='FILES'">
				<xsl:call-template name="FileType" />
			</xsl:if>

			<xsl:if test="QuestionType/Type='ENUM'">
				<xsl:call-template name="EnumType">
					<xsl:with-param name="selectedValue" select="$selectedValue" />
				</xsl:call-template>
			</xsl:if>

			<xsl:if test="QuestionType/Type='MULTISELECT'">
				<xsl:call-template name="MultiSelectType">
					<xsl:with-param name="multi">
						true
					</xsl:with-param>
				</xsl:call-template>
			</xsl:if>

			<xsl:if test="QuestionType/Type='SINGLESELECT'">
				<xsl:call-template name="MultiSelectType" />
			</xsl:if>

			<xsl:if test="QuestionType/Type='INFO'">
				<xsl:call-template name="InfoType" />
			</xsl:if>

			<xsl:if test="QuestionType/Type='STRING'">
				<xsl:call-template name="StringType" />

			</xsl:if>

			<xsl:if test="QuestionType/Type='SPECIALSTRING'">
				<xsl:call-template name="SpecialStringType" />
			</xsl:if>

			<xsl:if test="QuestionType/Type='STRINGBOX'">
				<xsl:call-template name="StringBoxType" />
			</xsl:if>

			<xsl:if test="QuestionType/Type='PHONE'">
				<xsl:call-template name="PhoneType" />
			</xsl:if>

			<xsl:if test="QuestionType/Type='DATE'">
				<xsl:call-template name="DateType" />
			</xsl:if>

			<xsl:if test="QuestionType/Type='YEAR'">
				<xsl:call-template name="YearType" />
			</xsl:if>

			<xsl:if test="QuestionType/Type='BOOL'">
				<xsl:call-template name="BoolType" />
			</xsl:if>

			<xsl:if test="QuestionType/Type='INTEGER'">
				<xsl:call-template name="IntegerType" />
			</xsl:if>

			<xsl:if test="QuestionType/Type='UINTEGER'">
				<xsl:call-template name="IntegerType" />
			</xsl:if>

			<xsl:if test="QuestionType/Type='TAGS'">
				<xsl:call-template name="TagsType" />
			</xsl:if>

			<xsl:if test="QuestionType/Type='URL'">
				<xsl:call-template name="UrlType" />
			</xsl:if>

		</div>
	</xsl:template>

	<xsl:template match="NodeTags" />

	<xsl:template name="FormScripts">
		<script>
<!-- 			<![CDATA[ -->
			
<!-- 			function afterVerification(verified) -->
<!-- 			{ -->
<!-- 				mapClient.map.updateSize(); -->
<!-- 				console.log("update map"); -->
<!-- 			} -->
			
<!-- 			function onClickSubmit() -->
<!-- 			{ -->
<!-- 				if (typeof mapInitialized !== 'undefined' && mapInitialized ){ -->
<!-- 					verifyAndSubmit(afterVerification); -->
<!-- 				} -->
<!-- 				else verifyAndSubmit(function(){}); -->
<!-- 			} -->
			
<!-- 			$( document ).ready(function() { -->
<!--   				updateDynamicAttributes(); -->
<!-- 			});	 -->
<!-- 			]]> -->

			var sessionTimeout =<xsl:value-of select="sessionTimeout"></xsl:value-of>;
			function getCurrentLogin(){ return new Date(<xsl:value-of select="sessionLastAccess"></xsl:value-of>);}
<!-- 			<![CDATA[ -->
					
<!-- 			function updateTimeLeftMessage(){ -->
<!-- 				currentLogin = getCurrentLogin(); -->
<!-- 				currentTime = new Date($.now()); -->
<!-- 				timeSinceLogin = Math.floor((currentTime - currentLogin)/(1000)); -->
<!-- 				timeLeft = sessionTimeout - timeSinceLogin; -->
<!-- 				showTimeLeftMessage(timeLeft); -->
<!-- 			}	 -->
				
<!-- 			function showTimeLeftMessage(loginTimeRemaining) -->
<!-- 			{ -->
<!-- 				if(loginTimeRemaining <= 5*60) -->
<!-- 				{ -->
<!-- 					var message; -->
<!-- 					if(loginTimeRemaining < 0){ -->
<!-- 						$("#time_left_message").html(' <a target="_blank" href="/login"><font color="black"><b>Du har blivit utloggad. <i>Klicka här</i> för att logga in igen innan du sparar.</b></font></a> '); -->
<!-- 						$('#save_message_top').css({background: 'red'}); -->
<!-- 					} -->
<!-- 					else if(loginTimeRemaining <1*60) -->
<!-- 					{ -->
<!-- 						$("#time_left_message").text("Du kommer att loggas ut om mindre än 1 minut."); -->
<!-- 						$('#save_message_top').css({background: 'orange'}); -->
<!-- 					}else  -->
<!-- 					{						 -->
<!-- 						$('#save_message_top').css({background: 'yellow'}); -->
<!-- 						if(Math.floor((loginTimeRemaining)/(60)) == 1){  -->
<!-- 							message = "Du kommer att loggas ut om 1 minut."; -->
<!-- 						} -->
<!-- 						else -->
<!-- 						{ -->
<!-- 					    	message = "Du kommer att loggas ut om " + Math.floor((loginTimeRemaining)/(60)) + " minuter.";  -->
<!-- 				    	} -->
<!-- 						$("#time_left_message").text(message); -->
<!-- 					} -->
				    
<!-- 				} -->
<!-- 				else -->
<!-- 				{ -->
<!-- 					$('#save_message_top').css({background: 'white'}); -->
<!-- 					$("#time_left_message").text(""); -->
<!-- 				} -->
<!-- 			} -->
			
<!-- 			function clearNote(noteTextId){ -->
<!-- 				var noteTextArea = $("textarea[id='" + noteTextId + "']"); -->
<!-- 				noteTextArea.val(""); -->
<!-- 			} -->
<!-- 			]]> -->
		</script>

	</xsl:template>
</xsl:stylesheet>