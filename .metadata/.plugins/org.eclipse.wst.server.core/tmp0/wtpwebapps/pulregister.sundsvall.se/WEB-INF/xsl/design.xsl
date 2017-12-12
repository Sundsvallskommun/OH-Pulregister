<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" indent="yes"/>
	
	<xsl:include href="classpath://se/unlogic/hierarchy/core/xsl/CommonCoreTemplates.xsl" />
	<xsl:include href="classpath://se/unlogic/hierarchy/core/xsl/Errors.sv.xsl" />
	
	<xsl:template match="document">
	
		<xsl:text disable-output-escaping='yes'><![CDATA[<!DOCTYPE html>]]></xsl:text>
		<html>
			<head>
			
				
				<xsl:choose>
					<xsl:when test="/document/requestinfo/uriparts/uripart/value[text()='pulregister']">
						
					</xsl:when>
					<xsl:otherwise>
							
						<script src="{/document/requestinfo/contextpath}/static/global/jquery/jquery.js"/>	
						<xsl:apply-templates select="scripts/script[src != '/static/global/jquery/jquery.js']" />				
						<script src="{/document/requestinfo/contextpath}/js/bootstrap-3.3.5/bootstrap.min.js"/>		
						     				
					</xsl:otherwise>
				</xsl:choose>
				
				<title>Förteckning över personuppgiftsregister</title>
			
				<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
				<meta name="viewport" content="width=device-width, initial-scale=1"/>
								
				
				<link rel="shortcut icon" href="{/document/requestinfo/contextpath}/favicon.ico"/>
				<link rel="stylesheet" href="{/document/requestinfo/contextpath}/css/bootstrap-3.3.5/bootstrap.min.css" />
				<link rel="stylesheet" href="{/document/requestinfo/contextpath}/css/bootstrap-3.3.5/bootstrap-theme.min.css" />
				<link rel="stylesheet" href="{/document/requestinfo/contextpath}/static/global/css/openhierarchy.css" media="screen,projection" />
				<link rel="stylesheet" href="{/document/requestinfo/contextpath}/css/byggsundsvall.css?v=5" />
				
				<xsl:apply-templates select="links/link" />
			
			</head>
			
			<body class="non-responsive">
				<nav class="navbar navbar-default navbar-background">
					<div class="panel-body">
						<div class="row">
							<xsl:if test="user">
								<div class="col-xs-12 col-sm-3 col-md-3 col-lg-2">
									<div class="navbar-header">
										<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse">
											<span class="sr-only">Öppna meny</span>
											<span class="icon-bar"></span>
											<span class="icon-bar"></span>
											<span class="icon-bar"></span>
											<span class="icon-bar"></span>
										</button>
									</div>
								</div>
								
								<div class="col-xs-12 col-sm-9 col-md-9 col-lg-10">
									<div class="collapse navbar-collapse" id="navbar-collapse">
										<ul class="nav navbar-nav navbar-right">
											<xsl:apply-templates select="/document/menus/menu/menuitem[itemType = 'SECTION']" mode="sections"/>
											
											<li>
												<a href="{/document/requestinfo/contextpath}/logout">
													<span class="glyphicon glyphicon-log-out"></span>
													Logga ut
												</a>
											</li>
										</ul>
									</div>
								</div>
							</xsl:if>
						</div>
					</div>
				</nav>
				
				<div class="fluid-container">
					<nav class="hidden-sm hidden-md hidden-lg">
						<div class="container" id="small-header">
							<div id="small-menu-wrapper">
								<div class="row">
									<div class="small-menu-button floatleft">
										<a href="#" id="toggle-primary"><img src="{/document/requestinfo/contextpath}/img/menu.png"/></a>
									</div>
									
								</div>
								
								<ul class="primary" id="small-menu" style="display: none;">
									<li>
										<xsl:attribute name="class">
											<xsl:if test="/document/requestinfo/uriparts/uripart[value = 'byggkarta'] and count(/document/requestinfo/uriparts/uripart) = 1">
												selected
											</xsl:if>
										</xsl:attribute>
										<a title="Start" href="{/document/requestinfo/contextpath}/"><span>Start</span></a>
									</li>									
								</ul>
							</div>
							
						</div>
					</nav>
					
					<div class="container">
						<div class="row logo-search-row hidden-xs" id="searchrow">
							<div class="hidden-xs col-sm-8 col-md-7 col-lg-6">
								<div class="logo-container">
									<h1>
										<a href="{/document/requestinfo/contextpath}/">Förteckning över personuppgiftsregister</a>
									</h1>
								</div>
							</div>
						</div>
					</div>
					 
					<div class="content-container">
						
						<xsl:choose>
							<xsl:when test="menus/menu/menuitem/menu/menuitem">
								<div class="container">
									<div class="row">
										<div class="hidden-xs col-sm-3 col-md-3 col-lg-2">
											<div class="list-group">
												<xsl:choose>
													<xsl:when test="menus/menu/menuitem/menu/menuitem">
														<xsl:apply-templates select="menus/menu/menuitem/menu/menuitem" mode="menuitem" />
													</xsl:when>
													<xsl:otherwise>
														<xsl:apply-templates select="menus/menu/menuitem" mode="menuitem" />
													</xsl:otherwise>
												</xsl:choose>
											</div>
										</div>
										<div class="col-xs-12 col-sm-9 col-md-9 col-lg-10">
											<xsl:apply-templates select="errors" />
											<xsl:apply-templates select="moduleXMLResponse" />
											<xsl:value-of select="moduleHTMLResponse" disable-output-escaping="yes"/>
											<xsl:value-of select="moduleTransformedResponse" disable-output-escaping="yes"/>
											<div class="clearboth"></div>
										</div>
									</div>
								</div>
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="errors" />
								<xsl:apply-templates select="moduleXMLResponse" />
								<xsl:value-of select="moduleHTMLResponse" disable-output-escaping="yes"/>
								<xsl:value-of select="moduleTransformedResponse" disable-output-escaping="yes"/>
								
								<div class="clearboth"></div>
							</xsl:otherwise>
						</xsl:choose>
					</div>
				</div>
				
				<footer>
					<div class="container">
						<div class="row">
							<div class="col-xs-12 col-md-4">
								<div class="footerheader">
									<a href="http://www.sundsvall.se"><img class="img-responsive logo" src="{/document/requestinfo/contextpath}/img/sundsvalls-kommun-white.svg"/></a>
								</div>								
							</div>							
						</div>
					</div>
				</footer>
				
				<script>
					var contextPath = 'http://<xsl:value-of select="/document/requestinfo/servername" /><xsl:value-of select="/document/requestinfo/contextpath" />';
				</script>
			</body>
		
		</html>
		
	</xsl:template>
	
	<xsl:template name="login">
	
		<div class="container">
			<xsl:apply-templates select="errors" />
			<xsl:apply-templates select="moduleXMLResponse" />
			<xsl:value-of select="moduleHTMLResponse" disable-output-escaping="yes"/>
			<xsl:value-of select="moduleTransformedResponse" disable-output-escaping="yes"/>
		</div>
		
	</xsl:template>
	
	<xsl:template match="menuitem" mode="menuitem">
	
		<xsl:if test="itemType = 'MENUITEM'">
			<a class="list-group-item">
				<xsl:if test="selected">
					<xsl:attribute name="class">list-group-item active</xsl:attribute>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="urlType='RELATIVE_FROM_CONTEXTPATH'">
						<xsl:attribute name="href">
							<xsl:value-of select="/document/requestinfo/contextpath" /><xsl:value-of select="url" />
						</xsl:attribute>
					</xsl:when>
					<xsl:when test="urlType='FULL'">
						<xsl:attribute name="href"><xsl:value-of select="url" /></xsl:attribute>
					</xsl:when>
				</xsl:choose>
				
				<xsl:variable name="icon">
					<xsl:choose>
						<xsl:when test="Attributes/Attribute[Name='icon']">
							<xsl:value-of select="Attributes/Attribute[Name='icon']/Value"/>
						</xsl:when>
						<xsl:otherwise>
							glyphicon-menu-right
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				
				<span class="glyphicon {$icon}" aria-hidden="true"></span>&#160;<xsl:value-of select="name" />
			</a>
		</xsl:if>
		
		<xsl:if test="itemType = 'TITLE'">
			<span class="list-group-item list-group-title"><xsl:value-of select="name"/></span>
		</xsl:if>
	
	</xsl:template>
	
	<xsl:template match="menuitem" mode="togglemenu">
		
		<li class="visible-xs-block">
			<xsl:if test="selected">
				<xsl:attribute name="class">visible-xs-block active</xsl:attribute>
			</xsl:if>
			<a title="{description}">
				<xsl:choose>
					<xsl:when test="urlType='RELATIVE_FROM_CONTEXTPATH'">
						<xsl:attribute name="href">
							<xsl:value-of select="/document/requestinfo/contextpath" />
							<xsl:value-of select="url" />
						</xsl:attribute>
					</xsl:when>
					<xsl:when test="urlType='FULL'">
						<xsl:attribute name="href"><xsl:value-of select="url" /></xsl:attribute>
					</xsl:when>
				</xsl:choose>
				
				<span class="glyphicon glyphicon-menu-right bigmarginleft"></span>
				<xsl:value-of select="name" />
			</a>
		</li>
	
	</xsl:template>
	
	<xsl:template match="menuitem" mode="sections">
	
		<li>
			<xsl:if test="starts-with(/document/requestinfo/uri, concat(/document/requestinfo/contextpath, url))">
				<xsl:attribute name="class">active</xsl:attribute>
			</xsl:if>
			<a>
				<xsl:choose>
					<xsl:when test="urlType='RELATIVE_FROM_CONTEXTPATH'">
						<xsl:attribute name="href">
							<xsl:value-of select="/document/requestinfo/contextpath" />
							<xsl:value-of select="url" />
						</xsl:attribute>
					</xsl:when>
					<xsl:when test="urlType='FULL'">
						<xsl:attribute name="href"><xsl:value-of select="url" /></xsl:attribute>
					</xsl:when>
				</xsl:choose>
				
				<xsl:choose>
					<xsl:when test="url = '/administration'">
						<span class="glyphicon glyphicon-cog"></span>
					</xsl:when>
					<xsl:otherwise>
						<span class="glyphicon glyphicon-th-large"></span>
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:value-of select="name" />
			</a>
		</li>
		
		<xsl:apply-templates select="menu/menuitem[itemType = 'MENUITEM']" mode="togglemenu"/>
		
	</xsl:template>
	
	<xsl:template match="breadcrumb">
	
		<xsl:choose>
			<xsl:when test="position() = last()">
				<li class="active"><xsl:value-of select="name"/></li>
			</xsl:when>
			<xsl:otherwise>
				<li>
					<a>
						<xsl:choose>
							<xsl:when test="urlType='RELATIVE_FROM_CONTEXTPATH'">
								<xsl:attribute name="href">
									<xsl:value-of select="/document/requestinfo/contextpath" />
									<xsl:value-of select="url" />
								</xsl:attribute>
							</xsl:when>
							<xsl:when test="urlType='FULL'">
								<xsl:attribute name="href"><xsl:value-of select="url" /></xsl:attribute>
							</xsl:when>
						</xsl:choose>
						
						<xsl:value-of select="name" />
					</a>
				</li>
			</xsl:otherwise>
		</xsl:choose>
	
	</xsl:template>
</xsl:stylesheet>