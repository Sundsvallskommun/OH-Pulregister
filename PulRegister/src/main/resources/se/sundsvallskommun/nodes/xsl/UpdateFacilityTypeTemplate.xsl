<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="utf-8" indent="yes" />

	<xsl:template name="UpdateNodeType">
	
	
		<div class="container">

			<xsl:if test="validationException">
				<div class="alert alert-danger" role="alert">
					<xsl:apply-templates select="validationException/validationError" />
				</div>
			</xsl:if>

			<h2>
				Uppdatera  
				<xsl:value-of select="NodeType/name"></xsl:value-of>				
			</h2>

			<form id="dynamic_attributes_form" class="form-horizontal" role="form" action="{/document/requestinfo/url}"
				method="post" enctype="multipart/form-data">
		
				<script>
					<![CDATA[
					$(document).on("keypress", "form", function(event) {
						return event.keyCode != 13;
					});
					]]>
					var isUpdating = true;
				</script>

				<input type="hidden" id="type" name="type" class="form-control"
					value="{NodeType/type_id}" />
					
				<xsl:call-template name="IterateNodeTypeAttributes"/>

				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-11 col-lg-9" style="padding-right: 0px;">
						<button type="submit" class="btn btn-success pull-right">
							<span class="glyphicon glyphicon-ok"></span>
							Uppdatera
						</button>
					</div>
				</div>
				<xsl:apply-templates select="validationException/validationError" />
			</form>
		</div>
	</xsl:template>
	
	
	<xsl:template name="IterateNodeTypeAttributes">
		 
		<xsl:for-each
			select="NodeType/NodeTemplateAttributes/NodeTemplateAttribute">

			<div class="row">
				<div class="col-xs-12 col-sm-5 col-md-4 col-lg-3">
					<label for="name" class="control-label">
						<xsl:value-of select="name" />						
					</label>
				</div>
				<div class="col-xs-9 col-sm-7 col-md-7 col-lg-6">
					<label for="name" class="control-label">
						<xsl:value-of select="type" />
					</label>
				</div>
			</div>

		</xsl:for-each>
		 
		<!-- <xsl:apply-templates select="NodeTypes" />-->
	</xsl:template>

	

</xsl:stylesheet>