<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" encoding="utf-8" indent="yes" />
 
	<xsl:template name="ChooseNodeType">
	
		<script type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}//js/select2.min.js"></script>
		
		<link rel="stylesheet"
			href="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}//css/select2.min.css"></link>	
		
		<div class="container">
			<ul class="nav nav-tabs nav-justified">		
			  
			  <li id="tab_1"><a href="1" >Anläggningar</a></li>
			  <li id="tab_2"><a href="2" >Byggnader</a></li>
			  <li id="tab_3"><a href="3" >Serviceobjekt</a></li>
			  <script>
			  	var types = ["Anläggningar","Byggnader","Serviceobjekt"]
			  	var url = window.location.href;
			  	var tab = "tab_"+url.substr(url.length-1);
			  	$("[id='"+tab+"']").addClass("active");
			  </script>
			</ul>
		  	<h1>Välj typ att lägga till:</h1>  	
		  	<form class="form-horizontal" role="form" action="{/document/requestinfo/url}" method="post" >
				<table class="row col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<tr id="facilityDiv">
					<td class="row col-xs-12 col-sm-12 col-md-12 col-lg-12">
						<xsl:call-template name="ChooseNodeTypeFormDetails">
							<xsl:with-param name="element" select="NodeTypes/NodeType"/>
						</xsl:call-template>
					</td>
				</tr>				
				<tr>
					<td>
						<div class="pull-right">
							<button type="submit" class="btn btn-success">Nästa<span class="glyphicon icon-next"></span></button>				
						</div>						
					</td>					
				</tr>
				</table>
			</form>
		</div>
		<script>
			$('#defaultTab').trigger('click');
		</script>
  	</xsl:template>
  
 	<xsl:template name="ChooseNodeTypeFormDetails"> 			
  			<div class="row col-xs-12 col-sm-12 col-md-12 col-lg-12">
					<div class="form-group">
						<xsl:call-template name="createDropdown">
							<xsl:with-param name="id" select="'NodeTypes'"/>
							<xsl:with-param name="name" select="'NodeTypes'"/>
							<xsl:with-param name="element" select="NodeTypes/NodeType"/>
							<xsl:with-param name="labelElementName" select="'name'" />
							<xsl:with-param name="valueElementName" select="'type_id'" />
							<xsl:with-param name="selectedValue" select="NodeTypes/NodeType"/>						
							<xsl:with-param name="class" select="'form-control'" />
						</xsl:call-template>
						<script type="text/javascript">
						$(document).ready(function() {
						  $(".form-control").select2();
						});
						</script>
				</div>		
			</div>
	</xsl:template>
		
</xsl:stylesheet>