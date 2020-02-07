<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="utf-8" indent="yes" />
	
	<xsl:template match="NodeFile" mode="handleAttachedFiles">
		<script>	
			var attachedFile = {
		        fileid:<xsl:value-of select="nodeFileID" />,
		        name :'<xsl:value-of select="fileName" />',
		        size:<xsl:value-of select="fileSize" />,
		        filedate:Date('<xsl:value-of select="dateAdded"/>')		        
		      };
		     attachedFilelist.push(attachedFile);
		</script>
	</xsl:template>
	
	<xsl:template name="FileType" match="document">
		<xsl:value-of select="document" />

		
		<script>		
<!-- 			var attachedFilelist = []; -->
			
		
		function downloadFile() {
			var url = window.location.href;
			url = url.substr(0, url.lastIndexOf("nodes") + ("nodes").length) + "/downloadfile/" + "<xsl:value-of select="value/fileId"/>";
   			window.open(url);	
   			}
   			

		</script>
		
		
		<xsl:apply-templates mode="handleAttachedFiles" select="../../AttachedFiles"/>
		
	
		<fieldset>
	
		<input type="hidden" id="fileUploaderInput-{AttributeDetails}" name="file-{AttributeDetails}" value="{value/fileId};{value/fileData}" />
	
		<xsl:call-template name="ApplyAttributeDescription"/>
					
		<label id="add_file_btn-{AttributeDetails}" class="pull-left vcenter btn btn-default btn-file">
<!-- 			<span class="btn btn-default btn-file"> -->
   			Lägg till fil
   			<input class="btn hide" type="file" id="fileselect-{AttributeDetails}" name="fileselect[]" /> 
   			<span style="padding-left:5px;padding-right:5px;font-size:1.5em; pointer-events: none;" class="glyphicon glyphicon-folder-open" id="filedrag-{AttributeDetails}"/>
<!-- 			</span> -->
		</label>
		</fieldset>
		
<!-- 		<div id="filesAdded" class="panel-body"><div class="" id="existingFilesDiv"><h4>Redan tillagda filer:</h4><h4><div class="divider"></div></h4><div id="filerow_undefined" class="row vdivide"><div><div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 ">KvittoUsbMinne.pdf</div><div class="col-xs-4 col-sm-4 col-md-4 col-lg-4 text-center">286.7 kB</div></div></div><button class="btn btn-default btn-file pull-right" onclick="cleanFiles()">Rensa alla<span class="glyphicon glyphicon-trash"></span></button></div></div> -->
		
		<xsl:call-template name="ApplyAttributeTooltip"/>
		
		<div class="panel-body" id="filesAdded-{AttributeDetails}"></div>

		<script>
		
		
		var fileValue = "<xsl:value-of select="value"/>";
		var jqList = undefined;

		
		$(document).ready( function() {
		
		
			var attachedFilelist = [];
			var file = { name: "<xsl:value-of select="value/fileName"/>", 
 				    size: "<xsl:value-of select="value/fileSize"/>",
 			    	fileid:	 "<xsl:value-of select="value/fileId"/>"
 			};
 			   
 			if(file.fileid != ""){
 				attachedFilelist.push(file);
 			}
		
		
		  	if ( attachedFilelist.length > 0 ) 
  			    {			    	 
  			    	document.getElementById('filesAdded-<xsl:value-of select="AttributeDetails"/>').appendChild(makeUL( attachedFilelist, true , false ));
  			    }
		
		
			if($('#filesAdded-<xsl:value-of select="AttributeDetails"/>').children().children('[id^=filerow]').length > 0){
			    	$('#add_file_btn-<xsl:value-of select="AttributeDetails"/>').attr("disabled", true);
			    	$('#add_file_btn-<xsl:value-of select="AttributeDetails"/>').children('input').attr("disabled", true);
			    	$('#add_file_btn-<xsl:value-of select="AttributeDetails"/>').attr("title", "Du måste ta bort den tillagda filen för att kunna lägga till en ny.");
			};
		});		
		</script>
	</xsl:template>
		
</xsl:stylesheet>