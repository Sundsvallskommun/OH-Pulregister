<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="utf-8" indent="yes" />

	<xsl:include
		href="classpath://se/unlogic/hierarchy/core/utils/xsl/Common.xsl" />
	<xsl:include href="xsl/UpdateFacilityTemplate.xsl" />
	<xsl:include href="xsl/AddFacilityTemplate.xsl" />
	<xsl:include href="xsl/ChooseFacilityTypeTemplate.xsl" />
	<xsl:include href="xsl/FlatListTemplate.xsl" />		
	<xsl:include href="xsl/AttributeTypesTemplate.xsl" />
	<xsl:include href="xsl/UpdateFacilityTypeTemplate.xsl" />
	<xsl:include href="xsl/ShowFacilityTemplate.xsl" />

	<xsl:template match="document">
	
		<script
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/jquery-3.1.1.min.js"></script>
		<script
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/main.js"></script>
	
		<script type="text/javascript"
			src="{requestinfo/contextpath}/static/f/{module/sectionID}/{module/moduleID}/js/bootstrap.min.js"></script>

		<link rel="stylesheet"
			href="{requestinfo/contextpath}/static/f/{module/sectionID}/{module/moduleID}/css/main.css"></link>
			

		<!-- module path -->
		<input id="modulePath" type="hidden"
			value="{requestinfo/currentURI}/{module/alias}/"></input>

		
			
		<xsl:apply-templates/>
			

	</xsl:template>

	<xsl:template match="AddNode">
		<xsl:call-template name="AddNode" />
	</xsl:template>

	<xsl:template match="UpdateNode">
		<xsl:call-template name="UpdateNode" />
	</xsl:template>
	
	<xsl:template match="UpdateNodeType">
		<xsl:call-template name="UpdateNodeType" />
	</xsl:template>
	
	<xsl:template match="ShowNode">
		<xsl:call-template name="ShowNode" />
	</xsl:template>



	<xsl:template match="ChooseNodeType">
		<xsl:call-template name="ChooseNodeType" />
	</xsl:template>



	<xsl:template match="requestinfo" />
	<xsl:template match="module" />
	<xsl:template match="user" />

	<!-- <xsl:template match="node_id"/> -->
	<xsl:template match="type" />
	<xsl:template match="parent_node_id" />

</xsl:stylesheet>