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
	
	<xsl:template name="FileType">
	
		<script>		
			var attachedFilelist = [];
		</script>
		
		<xsl:apply-templates mode="handleAttachedFiles" select="../../AttachedFiles"/>
		
	
		<fieldset>
	
		<input type="hidden" id="fileUploaderInput" name="{AttributeDetails}" value="{value}" />
	
		<xsl:call-template name="ApplyAttributeDescription"/>
					
		
		<label class="pull-left vcenter">
			<span class="btn btn-default btn-file">
   			Lägg till filer 
   			<input class="btn hide" type="file" id="fileselect" name="fileselect[]" multiple="multiple" />
   			<span style="padding-left:5px;padding-right:5px;font-size:1.5em;" class="glyphicon glyphicon-folder-open" id="filedrag"/>
			</span>
		</label>
		</fieldset>
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
		
		<script type="text/javascript"
			src="{/document/requestinfo/contextpath}/static/f/{/document/module/sectionID}/{/document/module/moduleID}/js/filehandler.js"></script>
		<script>
		var fileValue = "<xsl:value-of select="value"/>";
		var jqList = undefined;
		<![CDATA[
	
			$(document).ready( function() {
			    $(':file').on('fileselect', onFileSelect);			    
			    if ( attachedFilelist.length > 0 )
			    {			    	
			    	document.getElementById('filesAdded').appendChild(makeUL( attachedFilelist, true , false ));
			    }
			});	
		]]>		
		</script>
	</xsl:template>
		
</xsl:stylesheet>