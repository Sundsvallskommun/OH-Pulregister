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
			var attachedFilelist = [];	
			
		
		function downloadFile() {
			var url = window.location.href;
			url = url.substr(0, url.lastIndexOf("nodes") + ("nodes").length) + "/downloadfile/" + "<xsl:value-of select="value/fileId"/>";
   			window.open(url);	
   		}
   		
<!--    		$(document).ready(function() { -->
<!-- 			$('#file_to_download').on("click", function(e) { -->
<!-- 		e.preventDefault() -->
<!-- 		var url = window.location.href; -->
<!-- 		url = url.substr(0, url.lastIndexOf("nodes") + ("nodes").length) + "/downloadfile/" + "<xsl:value-of select="value/fileId"/>"; -->
<!-- 		window.open(url); -->
<!-- 	}); -->
<!-- }); -->
		</script>
		
		
		<xsl:apply-templates mode="handleAttachedFiles" select="../../AttachedFiles"/>
		
	
		<fieldset>
	
		<input type="hidden" id="fileUploaderInput" name="file" value="{value/fileId};{value/fileData}" />
	
		<xsl:call-template name="ApplyAttributeDescription"/>
					
		<label id="add_file_btn" class="pull-left vcenter btn btn-default btn-file">
<!-- 			<span class="btn btn-default btn-file"> -->
   			Lägg till fil 
   			<input class="btn hide" type="file" id="fileselect" name="fileselect[]" multiple="multiple" />
   			<span style="padding-left:5px;padding-right:5px;font-size:1.5em;" class="glyphicon glyphicon-folder-open" id="filedrag"/>
<!-- 			</span> -->
		</label>
		</fieldset>
		
<!-- 		<div id="filesAdded" class="panel-body"><div class="" id="existingFilesDiv"><h4>Redan tillagda filer:</h4><h4><div class="divider"></div></h4><div id="filerow_undefined" class="row vdivide"><div><div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 ">KvittoUsbMinne.pdf</div><div class="col-xs-4 col-sm-4 col-md-4 col-lg-4 text-center">286.7 kB</div></div></div><button class="btn btn-default btn-file pull-right" onclick="cleanFiles()">Rensa alla<span class="glyphicon glyphicon-trash"></span></button></div></div> -->
		
		<xsl:call-template name="ApplyAttributeTooltip"/>
		
		<div class="panel-body" id="filesAdded"></div>
			<!--   </div> 
			<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
				<label id="filesAdded"><xsl:value-of select="value" /></label>
			</div>-->				
		
		<!-- var item = document.createElement('li');
			        var node = document.createTextNode(files[i].name);
			        
			        item.appendChild(node);
			        list.appendChild(item); -->
			        
			        
<!-- 		<button class="btn btn-default" title="Download" onclick="downloadFile(); return false"><span class="glyphicon glyphicon-export" ></span> Exportera till CSV</button> -->
		<script type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/filehandler.js">
			</script>
		<script>
		
		
		var fileValue = "<xsl:value-of select="value"/>";
		var jqList = undefined;
		var attachedFilelist = [];
		var file = { name: "<xsl:value-of select="value/fileName"/>", 
 			    size: "<xsl:value-of select="value/fileSize"/>",
 			    fileid:	 "<xsl:value-of select="value/fileId"/>"
 			    };
 			    
 		if(file.fileid != ""){
 			attachedFilelist.push(file);
 		}
 		
 		
		<![CDATA[
			$(document).ready( function() {
			    $(':file').on('fileselect', onFileSelect);
			    		    
			    if ( attachedFilelist.length > 0 )
			    {			    	
			    	document.getElementById('filesAdded').appendChild(makeUL( attachedFilelist, true , false ));
			    }
			   
			});	
		]]>		
		
		$(document).ready( function() {
		if($('#existingFilesDiv').children('[id^=filerow]').length > 0){
			    	$('#add_file_btn').attr("disabled", true);
			    	$('#add_file_btn').children('input').click(false);
			    	$('#add_file_btn').attr("title", "Du måste ta bort den tillagda filen för att kunna lägga till en ny.");
			    };
<!-- 			    console.log($('#existingFilesDiv').children('[id^=filerow]')); -->
<!--     			console.log($('#newFilesDiv').children('[id^=filerow]')); -->
				});	
		
		
		</script>
	</xsl:template>
		
</xsl:stylesheet>