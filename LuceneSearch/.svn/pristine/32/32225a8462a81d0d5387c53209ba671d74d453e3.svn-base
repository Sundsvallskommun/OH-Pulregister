<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1"/>

	<xsl:template match="Document">		
		
		<xsl:apply-templates select="Search"/>
		
	</xsl:template>
	
	<xsl:template match="Search">
	
		<div class="contentitem">
			
			<h1>
				<xsl:value-of select="../module/name"/>
            </h1>
			
			<form method="GET" action="{../ContextPath}{../FullAlias}">
			
				<xsl:call-template name="createSearchForm"/>
						
			</form>
			
				<xsl:choose>
					<xsl:when test="Hits">
					
						<p class="tiny">
							<xsl:value-of select="HitCount"/>
							<xsl:text>&#x20;</xsl:text>
							<xsl:value-of select="$i18n.hits"/>
						</p>
					
						<xsl:apply-templates select="Hits/Hit"/>
					
					</xsl:when>
					<xsl:when test="UnableToParseQueryString">
						
						<hr/>
						<p class="error"><xsl:value-of select="$i18n.UnableToParseQueryString"/></p>
						
					</xsl:when>
					<xsl:when test="NoHits">
					
						<hr/>
						<p ><xsl:value-of select="$i18n.NoHits"/></p>
						
					</xsl:when>
				</xsl:choose>
		</div>	
	
	</xsl:template>
	
	<xsl:template name="createSearchForm">
		<p>
			<input type="text" style="width: 80%;" value="{QueryString}" name="q"/>
			
			<xsl:text>&#x20;</xsl:text>
			
			<input type="submit" value="{$i18n.search}"/>
		</p>
	</xsl:template>
	
	<xsl:template match="Hit">
	
		<div class="bigmarginbottom">
			
			<hr/>
			
			<p class="nomargin bold">
				<a href="{/Document/ContextPath}{FullAlias}"><xsl:value-of select="Title"/></a>
			</p>
			
			<xsl:if test="Fragment">
				<p class="nomargin">
					<xsl:value-of select="Fragment" disable-output-escaping="yes"/>
				</p>
			</xsl:if>
			
			<p class="tiny bigmarginbottom">
				<xsl:value-of select="/Document/ServerURL"/>
				<xsl:value-of select="/Document/ContextPath"/>
				<xsl:value-of select="FullAlias"/>
			</p>
		
		</div>
	
	</xsl:template>
	
</xsl:stylesheet>