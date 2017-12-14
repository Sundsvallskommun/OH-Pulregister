var currentGML = undefined;
var currentWKT = undefined;
var inputElement = undefined;


var handleNewElements = false;
var popupStyleStr = 'left: 200px; top: 100px; width: 300px; height: 400px;';
var searchPopup = undefined;
var mapDiv = undefined;
var mapType = undefined;
var currentFeature = undefined;

function handleNewPopups(e)
{
	
	if ( handleNewElements && e.target.id.startsWith('gx_popup-') && e.target.style.cssText == popupStyleStr ) 
    {
    	searchPopup = $(e.target);		
    }
	else
	if ( searchPopup && e.target.nodeName.toLowerCase() == 'div' )
	{
		var mapPos = mapDiv.position();
		searchPopup.offset({ top: mapPos.top + 400 , left: mapPos.left})
		// console.log(e.target.id);
	}
}

function setCurrentFeature(feature)
{
	if ( currentFeature != feature )
	{
		mapClient.drawLayer.removeFeatures(currentFeature);		
	}
	currentFeature = feature;
}

 

function onNewShape(e,instance)
{		
	console.log("Uppdatera geometri");
	if ( mapType == 'POINT' )
	{
		mapClient.drawLayer.removeAllFeatures();		
	}	

	setCurrentFeature( e.feature ); 
}

function setMapInputValue(value)
{
	queryStr = "[id='"+inputElement+"']"; 
	$(queryStr).val(value);
}

function setCurrentXmlfromFeature(feature)
{
	if ( !feature || !feature.geometry )
	{
		setMapInputValue("");
		setCurrentFeature(null);
		return;
	}
	
	var newWKT = new OpenLayers.Format.WKT();
	currentWKT = newWKT.write( feature );
	console.log(currentWKT);
	if ( inputElement != undefined )
	{
		var encodedWKT = Base64.encode(currentWKT)
		setMapInputValue(encodedWKT);
	}
}

function onFeatureSelectedInSearch(e,instance)
{
	//console.log(instance);
	//console.log(e);
	if ( e.feature.fid && e.feature.fid.startsWith("Anlaggningar_yta") )
	{
		if ( e.feature.geometry && mapClient.drawLayer.getFeatureById(e.feature.fid) == null )
		{
			mapClient.drawLayer.removeAllFeatures()
			cloneFeature = e.feature.clone();
			mapClient.drawLayer.addFeatures( cloneFeature );
			setCurrentFeature( cloneFeature );
		}
	}
}

function onDrawLayerUnselect(e, instance)
{
	console.log("Unselect");
}

function drawCurrentGeo()
{
	if ( wktData.length > 0 )
	{
		var newWKT = new OpenLayers.Format.WKT();		
		features = newWKT.read( wktData );
		mapClient.drawLayer.addFeatures( features );
		setCurrentFeature( features );
		
		var centroid = features.geometry.getCentroid();
		var bounds = features.geometry.getBounds();
				
		mapClient.map.zoomToExtent(bounds);
	}
}

function updateGeo()
{
	setCurrentXmlfromFeature( currentFeature );	
}

function callback(client) {

	mapClient = client;
	
	client.gui.mapPanel.drawLayer.events.register('sketchcomplete', null, onNewShape );				
	client.gui.mapPanel.drawLayer.events.register('afterfeaturemodified', null, onNewShape );
	client.gui.mapPanel.drawLayer.events.register('featureunselected', null, onDrawLayerUnselect );
	
	client.mapPanel.searchLayer.events.register('featureselected', null , onFeatureSelectedInSearch );
	
	handleNewElements = true;
	mapDiv = $("[id='mapInputDiv']").first();
	$(document).on('DOMNodeInserted', handleNewPopups);
	
	$('div[requiredcontent="true"]').on('')
	
	drawCurrentGeo();
}		

function setInputElement(id)
{
	inputElement = id;
}
	