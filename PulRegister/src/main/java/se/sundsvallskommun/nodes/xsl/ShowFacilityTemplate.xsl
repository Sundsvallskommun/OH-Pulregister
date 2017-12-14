<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="utf-8" indent="yes" />

	<xsl:template name="ShowNode">

		<xsl:call-template name="ShowNodeOwner" />

	</xsl:template>

	<xsl:template name="ShowHeader">
	</xsl:template>

	<xsl:template name="ShowNodeOwner">
		<div class="container">

			<xsl:if test="validationException">
				<div class="alert alert-danger" role="alert">
					<xsl:apply-templates select="validationException/validationError" />
				</div>
			</xsl:if>

			<h2>
				Visar <xsl:value-of select="NodeOwner/name"></xsl:value-of> av typ <xsl:value-of select="NodeOwner/NodeType/name"/> 				
			</h2>
							
				<xsl:call-template name="IterateShowNodeTypeAttributes"/>

				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-11 col-lg-9" style="padding-right: 0px;">
					<a href="#" onclick="window.location.href='../updatefacility/{NodeOwner/node_id}'">
						<span type="submit" class="btn btn-success pull-right">
							<span class="glyphicon glyphicon-pencil"></span>
							Editera
						</span>
					</a>
					</div>
				</div>
			<xsl:apply-templates select="validationException/validationError" />
			
		</div>
	</xsl:template>
	
	<xsl:template name="IterateShowNodeTypeAttributes">
		 
		<xsl:for-each
			select="NodeOwner/NodeTemplateAttributes/NodeAttribute">
			
			<xsl:choose>
				<xsl:when test="type='GEO-AREA'">
					<div class="row">
						<div class="col-xs-12 col-sm-5 col-md-4 col-lg-3">
							<label for="name" class="control-label">
								<xsl:text>Geografiskt område</xsl:text>						
							</label>
						</div>
						<div class="col-xs-9 col-sm-7 col-md-7 col-lg-6">
							<label for="name" class="control-label">
								<xsl:text>Ja</xsl:text>
							</label>
						</div>
					</div>
				</xsl:when>
				<xsl:when test="type='GEO-POINT'">
					<div class="row">
						<div class="col-xs-12 col-sm-5 col-md-4 col-lg-3">
								<label for="name" class="control-label">
									<xsl:text>Geografisk punkt</xsl:text>						
								</label>
						</div>
						<div class="col-xs-9 col-sm-7 col-md-7 col-lg-6">
							<label for="name" class="control-label">
								<xsl:text>Ja</xsl:text>
							</label>
						</div>					
					</div>
				</xsl:when>
				<xsl:when test="type='FILES'">
					<div class="row">
						<div class="col-xs-12 col-sm-5 col-md-4 col-lg-3">
							<label for="name" class="control-label">
								<xsl:value-of select="name" />						
							</label>
						</div>
					</div>
					<div class="row">			
						<div class="col-xs-8 col-sm-8 col-md-8 col-lg-8" id="showFilesAdded"></div>
					</div>
					<script type="text/javascript"
						src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/filehandler.js">
					</script>
					<script>		
						var attachedFilelist = [];
					</script>
					<xsl:apply-templates mode="handleAttachedFiles" select="../../AttachedFiles"/>
					<script>
						if ( attachedFilelist.length > 0 )
					    {			    	
					    	document.getElementById('showFilesAdded').appendChild(makeUL( attachedFilelist, false, true ));
					    }
					</script>
				</xsl:when>
				<xsl:otherwise>
					
					<div class="row">
						<div class="col-xs-12 col-sm-5 col-md-4 col-lg-3">
							<label for="name" class="control-label">
								<xsl:value-of select="name" />						
							</label>
						</div>
						<div class="col-xs-9 col-sm-7 col-md-7 col-lg-6">
							<label for="name" class="control-label">
								<xsl:value-of select="value" />
							</label>
						</div>
					</div>
				</xsl:otherwise>
				
			</xsl:choose>

		</xsl:for-each>
		 
		<!-- <xsl:apply-templates select="NodeTypes" />-->
	</xsl:template>

</xsl:stylesheet>