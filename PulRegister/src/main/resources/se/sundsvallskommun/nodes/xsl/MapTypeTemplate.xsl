<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="utf-8" indent="yes" />

	<xsl:template name="DebugOpeneMap">
		<script type="text/javascript">
			var debug='source';
			var devPath = '';
		</script>
	</xsl:template>
	
	<xsl:template name="AddMapGeoDataVariable">
		<script>
			var wktData = '<xsl:value-of select="NodeOwner/geo/geoData"></xsl:value-of>';
		</script>
	</xsl:template>


	<xsl:template name="MapType">
		<xsl:param name="configJson"/>
		<xsl:param name="mapType"/>

		<script type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/base64.js"></script>
		<script type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/openemap/OpenEMap.js"></script>
		<script type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/openemap-integration.js"></script>

		<script>
			var contextPath = '<xsl:value-of select="/document/requestinfo/contextpath" />';
			var sectionID = '<xsl:value-of select="/document/module/sectionID" />';
			var moduleID = '<xsl:value-of select="/document/module/moduleID" />';
			var uriPrefix = contextPath + '/static/f/' + sectionID + '/' + moduleID;
	
	<![CDATA[
		var options = {
			gui : {
				map: { renderTo: 'map' },
				toolbar : {},
				zoomTools : {},
				layers : {type: 'basic', legendDelay: 0},
				baseLayers : {},				
				search: {renderTo: 'search'},
				identifyResults: {renderTo:'results'},				
				scalebar : {}
			},
			OpenEMap : {
				basePathImages : uriPrefix + '/openemap/resources/images/',
				basePathES : '../esproxy?url=',
				basePathLM : '../lmproxy?url=https://kartatest.e-tjansteportalen.se/search/lm/',
				wmsURLs : {
					basePath : '/geoserver/wms',
					url : 'https://extmaptest.sundsvall.se/geoserver/wms',
					getCapabilities : 'https://extmaptest.sundsvall.se/getcapabilities/wms.xml'
				},
				basePathProxy : '../wfsproxy?url=',
				wsUrls : {
					basePath : '/',
					configs : '/',
					adminconfigs : '/adminconfigs',
					permalinks : '/openemappermalink/permalinks',
					metadata : 'geometadata/getmetadatabyid',
					metadata_abstract : 'geometadata/getabstractbyid'
				}
			}
		};
		
	]]>
		</script>

		<script>
			
			var mapInputID = '<xsl:value-of select="AttributeDetails" />';
			var mapInitialized = false;
			var configJson = '<xsl:value-of select="$configJson"/>';
			var setMapType = '<xsl:value-of select="$mapType"/>';
			<![CDATA[
			
			function lazyloadMap()
			{
				if ( mapInitialized == false )
				{
					mapType = setMapType;
					mapInitialized = true;
					setInputElement(mapInputID);
					initOpenEMap('../getmapconfig/'+configJson, options, callback);										
				}				
			}
			]]>
			
		</script>
		
		
		<div class="row" requiredcontent="{required}" name="{AttributeDetails}">
			<div class="accordion" id="mapInputDiv" onclick="lazyloadMap()">
				<h1><xsl:value-of select="name" /></h1>
				<div>
					<div>
						<div id="map" style="width:100%; height:800px">
							<div id="search" style="position: fixed; right: 10px; top: 670px; width: 300px; height: 100px;"/>
							<div id="results" style="position: absolute; left: 150px; bottom: 0px;"/>
						</div>				
					</div>
				</div>
			</div>
			<xsl:call-template name="ApplyAttributeTooltip"/>
		</div>
		

		<input type="hidden" name="{AttributeDetails}" id="{AttributeDetails}"
			value="{value}" />
	
		<script>			
		
			var hasGeoData = ( wktData.length > 0 );
			var expandMap = true;
			
			$("[id='mapInputDiv']").accordion({
			firstChildExpand: ( expandMap ),
			multiExpand: true,
			slideSpeed: 500,
			dropDownIcon: '&#9660;'
			});
			
			if ( hasGeoData || expandMap ) 
			{
				lazyloadMap()	
			} 			

		</script>


	</xsl:template>
	
	<xsl:template name="MapAreaType">
		<xsl:call-template name="MapType">
			<xsl:with-param name="configJson" >default.json</xsl:with-param>
			<xsl:with-param name="mapType">AREA</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template name="MapPointType">
		<xsl:call-template name="MapType">
			<xsl:with-param name="configJson">serviceobjekt.json</xsl:with-param>
			<xsl:with-param name="mapType">POINT</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

</xsl:stylesheet>