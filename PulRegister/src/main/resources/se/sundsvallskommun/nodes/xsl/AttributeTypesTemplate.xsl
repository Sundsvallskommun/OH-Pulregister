<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="utf-8" indent="yes" />
	
	<xsl:include href="MapTypeTemplate.xsl" />
	<xsl:include href="FileTypeTemplate.xsl" />


	<xsl:template name="DynamicAttributesInit">
		<!-- <link href="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/css/bootstrap.css" 
			rel="stylesheet"/> -->
			
		<link
			href="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/css/bootstrap3/bootstrap-switch.min.css"
			rel="stylesheet" />
		
			
		<script type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}//js/select2.min.js"></script>
		
		<link rel="stylesheet"
			href="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}//css/select2.min.css"></link>

		<script type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/jquery.bootstrap-touchspin.min.js"></script>
		<script type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/bootstrap-switch.min.js"></script>

		<script
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/bootstrap-datepicker.min.js"></script>

		<script
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/locales/bootstrap-datepicker.sv.min.js"
			charset="UTF-8"></script>
		<script
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/locales/bootstrap-datepicker.en-GB.min.js"
			charset="UTF-8"></script>
		<script type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/woco.accordion.js"/>			
		
		<link id="bsdp-css" 
			href="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/css/bootstrap-datepicker3.standalone.min.css"
			rel="stylesheet" />
			
			
			
		<script type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/bootstrap-tagsinput.min.js"></script>
			
		<script type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/typeahead.bundle.js"></script>
			
		<link href="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/css/bootstrap-tagsinput.css"
			rel="stylesheet" />
			
		<link href="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/css/typeaheadjs.css"
			rel="stylesheet" />
		
		<link href="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/css/woco-accordion.css"
			rel="stylesheet" />
			
			
		
		<script type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/bootstrap-multiselect.js"></script>
		<link href="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/css/bootstrap-multiselect.css"
			rel="stylesheet" />			
		
			
		
			
			
<!--	<script>
		var datepicker = $.fn.datepicker.noConflict();
			$.fn.bootstrapDP = datepicker;    
		</script> 
-->

	</xsl:template>
		
	<xsl:template name="TagsType">
		<div class="row">
			<div class="col-xs-12 col-sm-5 col-md-4 col-lg-3">
				<label for="name" class="control-label">
					<xsl:value-of select="name" />
					:
				</label>
			</div>
			<div class="col-xs-11 col-sm-6 col-md-7 col-lg-8 form-group">
				<input  type="text" class="col-xs-11 col-sm-6 col-md-7 col-lg-8" data-role="tagsinput" value="{value}" name="{AttributeDetails}" id="{AttributeDetails}">
					<!-- <xsl:call-template name="generateTagOptions"/> -->
				</input>
				<script>
				var bloodhoundTags = new Bloodhound({
					datumTokenizer: Bloodhound.tokenizers.obj.whitespace('text'),
					queryTokenizer: Bloodhound.tokenizers.whitespace,
					prefetch: {
						url : '../gettagsjson',
						cache:false
					}					  
				});						
					
				bloodhoundTags.initialize();
												
				$("[id='<xsl:value-of select="AttributeDetails"/>']").tagsinput({
					typeaheadjs: {
						name: 'tags',
					   	valueKey: 'text',
					   	displayKey: 'text',
					   	source: bloodhoundTags.ttAdapter()
					}
				});
				</script>
			</div>
			<xsl:call-template name="ApplyAttributeTooltip"/>
		</div>
	</xsl:template>

	<xsl:template name="BoolType">
		<xsl:call-template name="ApplyAttributeDescription"/>
			<div class="pannel panel-body panel-default">
				<xsl:call-template name="createCheckbox">
					<xsl:with-param name="id" select="AttributeDetails" />
					<xsl:with-param name="name" select="AttributeDetails" />
					<!-- <xsl:with-param name="value" select="true"/> -->
					<!-- <xsl:with-param name="class" select="'form-control'" /> -->
					<!-- <xsl:with-param name="width" select="'50%'" />-->
					<xsl:with-param name="checked" select="value" />
				</xsl:call-template>
				<script>
					if ( is_running_msie() == false )
					{
						$("[id='<xsl:value-of select="AttributeDetails" />']").bootstrapSwitch({
							onText : 'Ja',
							offText : 'Nej',
							onSwitchChange : updateDynamicAttributes
						});
					}
				</script>
		</div>						
	</xsl:template>

	<xsl:template name="ActionRequiredType">
<!-- 		<article requiredcontent="{required}" name="{AttributeDetails}-actionrequired" > -->
		<div id="action">
			<div class="line-divider"/>		
				<div class="pannel panel-body panel-default">
					<label for="notes">
						<input type="checkbox" id="{AttributeDetails}-notes" name="{AttributeDetails}-actionrequired" />
						 Åtgärd krävs
					</label>
<!-- 				<script> -->
<!-- 					$(function() { -->
<!-- 						if(<xsl:value-of select="ActionRequired"/> != false) { -->
						
<!-- 							$("[id='<xsl:value-of select="AttributeDetails" />-notes']").attr('checked', 'checked') -->
<!-- 							$("[id='<xsl:value-of select="AttributeDetails" />-notes-div']").slideDown(); -->
						
<!-- 						} else { -->
									
<!-- 						} -->
<!-- 					}); -->
<!-- 					$(function(){ -->
<!-- 						$("[id='<xsl:value-of select="AttributeDetails" />-notes']").click(function () { -->
<!-- 							if($(this).is(":checked")) { -->
<!-- 								$("[id='<xsl:value-of select="AttributeDetails" />-notes-div']").slideDown(); -->
<!-- 								$("[id='<xsl:value-of select="AttributeDetails" />-notes']").attr('checked', 'checked') -->
<!-- 							} else { -->
<!-- 								$("[id='<xsl:value-of select="AttributeDetails" />-notes-div']").slideUp(); -->
<!-- 								$("[id='<xsl:value-of select="AttributeDetails" />-notes']").removeAttr('checked') -->
<!-- 							} -->
<!-- 						}); -->
<!-- 					}); -->
<!-- 				</script> -->
			</div>
		</div>
<!--</article> -->
	</xsl:template>
			
	<xsl:template name="InfoType">
		<xsl:call-template name="ApplyAttributeDescription"/>		
			<div class="pannel panel-body panel-default">		
				<xsl:choose>
					<xsl:when test="NodeTemplateAttribute">
						<mark><xsl:value-of select="NodeTemplateAttribute/value"></xsl:value-of></mark>
					</xsl:when>
					<xsl:otherwise>
						<mark><xsl:value-of select="value"></xsl:value-of></mark>
					</xsl:otherwise>
				</xsl:choose>
				<input type="hidden" id="{AttributeDetails}" name="{AttributeDetails}" value="{value}"/>
			</div>
	</xsl:template>
	
	<xsl:template name="StringBoxType">
		<xsl:call-template name="ApplyAttributeDescription"/>
			<div class="panel-body panel-default" id="{AttributeDetails}">			
					<textarea type="text" id="{AttributeDetails}" name="{AttributeDetails}" rows="5" style="width:100%;resize:vertical;"><xsl:value-of select="value"></xsl:value-of></textarea>
			</div>
	</xsl:template>

	<xsl:template name="StringType">
		<xsl:call-template name="ApplyAttributeDescription"/>
			<div class="pannel panel-body panel-default">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="AttributeDetails" />
					<xsl:with-param name="name" select="AttributeDetails" />
					<xsl:with-param name="value" select="value" />
					<xsl:with-param name="placeholder" select="Description" />
					<xsl:with-param name="class" select="'form-control'" />
					<xsl:with-param name="width" select="'100%'" />				
				</xsl:call-template>
			</div>
	</xsl:template>
	
	<xsl:template name="SpecialStringType">
		<xsl:call-template name="ApplyAttributeDescription"/>
			<div class="pannel panel-body panel-default">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="AttributeDetails" />
					<xsl:with-param name="name" select="AttributeDetails" />
					<xsl:with-param name="value" select="value" />
					<xsl:with-param name="class" select="'form-control'" />
					<xsl:with-param name="width" select="'100%'" />				
				</xsl:call-template>
			</div>
	</xsl:template>
	
	<xsl:template name="PhoneType">
		<div class="row">
			<div class="col-xs-12 col-sm-5 col-md-4 col-lg-3">
				<label for="name" class="control-label">
					<xsl:value-of select="name" />
					:
				</label>				
			</div>
			<div class="col-xs-9 col-sm-7 col-md-7 col-lg-6">
				<div class="form-group">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="AttributeDetails" />
						<xsl:with-param name="name" select="AttributeDetails" />
						<xsl:with-param name="value" select="value" />
						<xsl:with-param name="placeholder" select="description" />
						<xsl:with-param name="class" select="'form-control'" />
						<xsl:with-param name="width" select="'100%'" />
						<!-- <xsl:with-param name="maxlength" select="'50'" /> -->
					</xsl:call-template>
				</div>
			</div>
			<xsl:call-template name="ApplyAttributeTooltip"/>
		</div>
	</xsl:template>
	
	<xsl:template name="UrlType">
		<div class="row">
			<div class="col-xs-12 col-sm-5 col-md-4 col-lg-3">
				<label for="name" class="control-label">
					<xsl:value-of select="name" />
					:
				</label>
			</div>
			<div class="col-xs-9 col-sm-7 col-md-7 col-lg-6">
				<div class="form-group">
					<xsl:call-template name="createTextField">
						<xsl:with-param name="id" select="AttributeDetails" />
						<xsl:with-param name="name" select="AttributeDetails" />
						<xsl:with-param name="value" select="value" />
						<xsl:with-param name="placeholder" select="description" />
						<xsl:with-param name="class" select="'form-control'" />
						<xsl:with-param name="width" select="'100%'" />
						
					</xsl:call-template>
				</div>
			</div>			
		</div>
	</xsl:template>

	<xsl:template name="DateType">
		<div class="row">
			<div class="col-xs-12 col-sm-5 col-md-4 col-lg-3">
				<label for="name" class="control-label">
					<xsl:value-of select="name" />
					:
				</label>
			</div>
			<div class="col-xs-8 col-sm-6 col-md-4 col-lg-4">
				<div class="form-group">
					<div class="input-group date">
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="AttributeDetails" />
							<xsl:with-param name="name" select="AttributeDetails" />
							<xsl:with-param name="value" select="value" />
							<xsl:with-param name="placeholder" select="description" />
							<xsl:with-param name="class" select="'form-control'" />
							<xsl:with-param name="width" select="'100%'" />
							<xsl:with-param name="maxlength" select="'50'" />
						</xsl:call-template>
						<span class="input-group-addon">
							<i class="glyphicon glyphicon-th"></i>
						</span>
						<script>
							var currentValue = '<xsl:value-of select="value" />';
						</script>
						<script>
						<![CDATA[
							$(".input-group.date").datepicker({
								language: "sv",
								startView: 1,				                
				                showButtonPanel: true,
				                dateFormat: 'yyyy-mm-dd',
				                autoclose: true,
				                todayBtn: true,
								todayHighlight: true
				                
				            });
				            if ( ( isAdding != undefined && isAdding == true ) && ( !currentValue || currentValue == "" ) )
				            {
				            	$(".input-group.date").datepicker("setDate", new Date());
				            }
				        ]]>
						</script>
					</div>
				</div>
			</div>
			<xsl:call-template name="ApplyAttributeTooltip"/>
		</div>
	</xsl:template>

	<xsl:template name="YearType">
		<div class="row">
			<div class="col-xs-12 col-sm-5 col-md-4 col-lg-3">
				<label for="name" class="control-label">
					<xsl:value-of select="name" />
					:
				</label>
			</div>
			<div class="col-xs-8 col-sm-6 col-md-3 col-lg-3">
				<div class="form-group">
					<div class="input-group date">
						<xsl:call-template name="createTextField">
							<xsl:with-param name="id" select="AttributeDetails" />
							<xsl:with-param name="name" select="AttributeDetails" />
							<xsl:with-param name="value" select="value" />
							<xsl:with-param name="placeholder" select="description" />
							<xsl:with-param name="class" select="'form-control'" />
							<xsl:with-param name="width" select="'100%'" />
							<xsl:with-param name="maxlength" select="'50'" />
						</xsl:call-template>
						<span class="input-group-addon">
							<i class="glyphicon glyphicon-th"></i>
						</span>
						<script>
							$(".input-group.date").datepicker({
								language: "sv",
								startView: 2,
								minViewMode:2,
								maxViewMode:2,
				                changeYear: true,
				                changeMonth: false,
				                showButtonPanel: true,
				                format: 'yyyy',
				                autoclose: true						        				                
				            });
						</script>
					</div>
				</div>
			</div>
			<xsl:call-template name="ApplyAttributeTooltip"/>
		</div>
	</xsl:template>

	<xsl:template name="EnumType">
		<xsl:param name="selectedValue" select="null" />				
		<xsl:call-template name="ApplyAttributeDescription"/>
		<div class="pannel panel-body panel-default">
				<xsl:call-template name="createDropdown">
					<xsl:with-param name="id" select="AttributeDetails" />
					<xsl:with-param name="name" select="AttributeDetails" />
					<xsl:with-param name="element" select="QuestionOptions/QuestionOption" />
					<xsl:with-param name="labelElementName" select="'name'" />
					<xsl:with-param name="valueElementName" select="'value'" />
					<xsl:with-param name="selectedValue" select="$selectedValue" />
					<xsl:with-param name="class" select="'form-control'" />
				</xsl:call-template>
				<script>
					$("[id='<xsl:value-of select="AttributeDetails" />']").select2({					
						width:'resolve'
					});
				</script>									
		</div>		
	</xsl:template>
	
	<!-- If multiple attribute render checkboxes otherwise render radio for single select -->
	
	<xsl:template name="MultiSelectType">
		<xsl:param name="multi" select="null"/>
		<xsl:param name="selectAllOption" select="false"/>
						
		<xsl:call-template name="ApplyAttributeDescription">
			
		</xsl:call-template>
		
		<div class="pannel panel-body panel-default">
			<select class="form-control" id="{AttributeDetails}" name="{AttributeDetails}" >
				<xsl:if test="$multi != ''">
					<xsl:attribute name="multiple"/>
					<xsl:attribute name="name"><xsl:value-of select="AttributeDetails" /></xsl:attribute>
				</xsl:if>
					<xsl:attribute name="size"><xsl:value-of select="count(QuestionOptions/QuestionOption)"/></xsl:attribute>			
					<xsl:for-each select="QuestionOptions/QuestionOption">
						<xsl:if test="name!=''">
							<option value="{value}">						
								<xsl:if test="selected">
									<xsl:attribute name="selected"/>
								</xsl:if>			
								<xsl:value-of select="name"/>
							</option>
						</xsl:if>
					</xsl:for-each>
			</select>			
			<script type="text/javascript">
				var curMultiSelectID = '<xsl:value-of select="AttributeDetails" />';
				var selectAllOption = '<xsl:value-of select="$selectAllOption" />';
				var multiSelect = false;
				<xsl:if test="$multi != ''">
					<xsl:text>multiSelect = true;</xsl:text>					
				</xsl:if>
				 <![CDATA[
				    $("[id='"+curMultiSelectID+"']").multiselect({		
				    	selectAllText: 'Markera alla',	            
			            includeSelectAllOption: selectAllOption,
			            includeSelectAllDivider: false,			            
			            multiple:multiSelect,
			            templates : {		
			            	button: '<button type="button" data-toggle="dropdown" style="display:none" ><span class="multiselect-selected-text"></span> <b class="caret"></b></button>',	            	
			            	li: '<li><a class="mslistitem" tabindex="0"><label class="mslistitem"></label></a></li>',
			            	liGroup: '<li class="mslistitem"><label class="mslistitem"></label></li>',
			            	ul: '<ul style="list-style-type: none;" class="mslistitem" id="multiselect-{AttributeDetails}"></ul>'  ,
			            	divider: '<li class="multiselect-item divider"></li>'}
			          });
			      ]]>    
<!-- 					$("[id='multiselect-<xsl:value-of select="AttributeDetails" />']").show(); -->
			</script>					
		</div>
		
	</xsl:template>
		
	<xsl:template name="IntegerType">
				
		<xsl:call-template name="ApplyAttributeDescription"/>
		<div class="panel-body">
			<div class="col-xs-6 col-sm-6 col-md-3 col-lg-3">
				<xsl:call-template name="createTextField">
					<xsl:with-param name="id" select="AttributeDetails" />
					<xsl:with-param name="name" select="AttributeDetails" />
					<xsl:with-param name="value" select="value" />
					<xsl:with-param name="placeholder" select="description" />
					<xsl:with-param name="class" select="'form-control'" />
					<xsl:with-param name="width" select="'100%'" />
					<xsl:with-param name="maxlength" select="'50'" />
				</xsl:call-template>
				<script>
					$("input[name='<xsl:value-of select="AttributeDetails" />']").TouchSpin({
					initval:
					<xsl:choose>
						<xsl:when test="string-length(value)>0">
							<xsl:value-of select="value" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>0</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
					,max: 1000000000
					,min: 0
					});
				</script>
			</div>
		</div>
					
	</xsl:template>

</xsl:stylesheet>