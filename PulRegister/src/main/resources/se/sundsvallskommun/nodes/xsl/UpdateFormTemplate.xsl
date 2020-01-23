<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="utf-8" indent="yes" />

	<xsl:template name="UpdateForm">

		<script type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/form-verification.js"></script>
		<script type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/dynamic-attributes.js"></script>

		<script>
			var isAdding = false;
			var isUpdating = true;
		</script>

		<xsl:call-template name="FormScripts" />
		<xsl:call-template name="DynamicAttributesInit" />
		<div class="container">
			<xsl:if test="validationException">
				<div class="alert alert-danger" role="alert">
					<xsl:apply-templates
						select="validationException/validationError" />
				</div>
			</xsl:if>
			<h2>
				Uppdatera
				<xsl:value-of select="Forms/Form/Name"></xsl:value-of>
			</h2>
			<div id="save_message_top" name="save_message_top"
				style="background:white;width:auto;position:fixed;position:sticky;top:0;z-index:2;">
				<div class="panel-body">
					<span id="time_left_message"></span>
					<span onclick="onClickSubmit()"
						class="btn btn-success pull-right">
						<span class="glyphicon glyphicon-edit"></span>
						Uppdatera
					</span>
					<!-- Återgå till formList -->
					<button type="button"
						onclick="location.href='{/document/requestinfo/currentURI}/{/document/module/alias}/formlist'; return false"
						class="btn btn-info pull-right">
						<span class="glyphicon glyphicon-list"></span>
						&#160;Återgå
					</button>
				</div>
			</div>
			<form id="dynamic_attributes_form" class="form-horizontal"
				role="form" action="{/document/requestinfo/url}" method="post"
				enctype="multipart/form-data">
				<xsl:call-template name="AddFormNameTemplate">
					<xsl:with-param name="formName"
						select="Forms/Form/Name"></xsl:with-param>
				</xsl:call-template>
				<xsl:call-template
					name="IterateUpdateTemplateAttributes" />
				<input type="hidden" id="type" name="type"
					class="form-control" value="{FormID}" />
				<div class="panel-body">
					<span onclick="onClickSubmit()"
						class="btn btn-success pull-right">
						<span class="glyphicon glyphicon-edit"></span>
						Uppdatera
					</span>
				</div>
				<xsl:apply-templates
					select="validationException/validationError" />
			</form>
		</div>
	</xsl:template>
	<!-- Iterates over parent elements, special action for two types see below, 
		child elements see AddFormTemplate.xml 235 -->
	<xsl:template name="IterateUpdateTemplateAttributes">
		<xsl:for-each select="Forms/Question">
			<xsl:choose>
				<xsl:when test="Name = 'Personuppgiftsansvarig'">
					<xsl:call-template
						name="HandleTemplateAttributeType">
						<xsl:with-param name="selectedValue"
							select="../Form/GroupID" />
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="Name = 'Status'">
					<xsl:call-template
						name="HandleTemplateAttributeType">
						<xsl:with-param name="selectedValue"
							select="../Form/Questionnaire/QuestionnaireStatus/StatusID" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:if test="not(Parent)">
						<xsl:call-template
							name="HandleTemplateAttributeType" />
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>