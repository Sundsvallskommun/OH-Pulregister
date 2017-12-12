<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>
	
	<xsl:template name="initializeFCKEditor">
		<xsl:param name="editorContainerClass" />
		<xsl:param name="basePath" select="''" />
		<xsl:param name="editorHeight" select="'400'"/>
		<xsl:param name="contentsCss" select="''" />
		<xsl:param name="toolbar" select="''" />
		<xsl:param name="callback" select="''" />
		<xsl:param name="customConfig" select="''" />
		<xsl:param name="filebrowserBrowseUri" select="''" />
		<xsl:param name="filebrowserImageBrowseUri" select="''" />
		<xsl:param name="contextPath" select="/Document/requestinfo/contextpath" />
		<xsl:param name="bodyClass" select="''" />
		
		<script type="text/javascript">
		var fckSettings = new Object();
		var basePath = '<xsl:value-of select="$basePath" />';
		fckSettings.editorContainerClass = '<xsl:value-of select="$editorContainerClass" />';
		fckSettings.editorHeight = <xsl:value-of select="$editorHeight" />;
		
		<xsl:if test="$filebrowserBrowseUri != ''">
			fckSettings.filebrowserBrowseUri = basePath + '<xsl:value-of select="$filebrowserBrowseUri" />';
		</xsl:if>		
		
		<xsl:if test="$filebrowserImageBrowseUri != ''">
	       	fckSettings.filebrowserImageBrowseUri = basePath + '<xsl:value-of select="$filebrowserImageBrowseUri" />';
		</xsl:if>

		<xsl:if test="$contentsCss != ''">
			fckSettings.contentsCss = '<xsl:value-of select="$contextPath"/><xsl:value-of select="$contentsCss"/>';
		</xsl:if>
		
		<xsl:if test="$customConfig != ''">
			fckSettings.customConfig = basePath + '<xsl:value-of select="$customConfig" />';
		</xsl:if>
		
		<xsl:if test="$toolbar != ''">
			fckSettings.toolbar = '<xsl:value-of select="$toolbar" />';
		</xsl:if>
		
		<xsl:if test="$callback != ''">
			fckSettings.callback = <xsl:value-of select="$callback" />;
		</xsl:if>
		
		<xsl:if test="$bodyClass != ''">
			fckSettings.bodyClass = '<xsl:value-of select="$bodyClass" />';
		</xsl:if>
		
		</script>
	</xsl:template>
	
</xsl:stylesheet>