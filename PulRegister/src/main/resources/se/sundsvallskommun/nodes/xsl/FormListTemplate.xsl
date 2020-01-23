<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" encoding="utf-8" indent="yes" />
      
<xsl:template match="ListForms">

	<xsl:if test="./Forms">
	</xsl:if>
	<xsl:choose>
  		<xsl:when test="./Forms">
    		<xsl:apply-templates/>
  		</xsl:when>
  		<xsl:otherwise>
    		<div class="container">     
        		<div class="span12">      
            		<button type="button" onclick="location.href='{/document/requestinfo/currentURI}/{/document/module/alias}/addform'; return false" class="btn btn-success pull-center biggermargintop"><span class="glyphicon glyphicon-plus"></span>&#160;Skapa ny</button>
          		</div> 
      		</div>
		</xsl:otherwise>
 	</xsl:choose>
		    
	</xsl:template> 
    
	<xsl:template match="Forms"> 
	
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css" integrity="sha384-50oBUHEmvpQ+1lW4y57PTFmhCaXp0ML5d60M1M7uH2+nqUivzIebhndOJK28anvf" crossorigin="anonymous"></link>
    <link href="https://unpkg.com/bootstrap-table@1.14.2/dist/bootstrap-table.min.css" rel="stylesheet"></link>
	<script src="https://unpkg.com/bootstrap-table@1.14.2/dist/bootstrap-table.min.js"></script>
	
	<form name="myForm_1" onsubmit="return false"> 
	<style>
	  	.btn {
	  	margin-left: 6px;
	  	margin-right: 6px;
	  	}
	  	.label {
	  	margin-left: 6px;
	  	margin-right: 6px;
	  	font-size:17px;
	  	color: black;
	  	}
  	</style> 
   	<div class="toolbar">
  		<label class="label">Välj typ för export:</label>
		<select class="btn btn-default" id="urls" data-show-icon="true">
			<option value="{/document/requestinfo/currentURI}/{/document/module/alias}/exportemptycsv"> Välj </option>
		    <option value="{/document/requestinfo/currentURI}/{/document/module/alias}/exportemptycsv"> Tom mall </option>
		    <option value="{/document/requestinfo/currentURI}/{/document/module/alias}/exportcsv/0"> Alla Frågor </option>
		    <option value="{/document/requestinfo/currentURI}/{/document/module/alias}/exportcsv/1"> Ej påbörjad </option>
		   	<option value="{/document/requestinfo/currentURI}/{/document/module/alias}/exportcsv/2"> Påbörjad </option>
		   	<option value="{/document/requestinfo/currentURI}/{/document/module/alias}/exportcsv/3"> Klar </option>
		   	<option value="{/document/requestinfo/currentURI}/{/document/module/alias}/exportcsv/4"> Åtgärder krävs </option>
		   	<option value="{/document/requestinfo/currentURI}/{/document/module/alias}/exportcsv/5"> Har anteckningar </option>
		</select>
		<button class="btn btn-default" title="Download" onclick="openLink(document.forms['myForm_1']); return false"><span class="glyphicon glyphicon-export" ></span> Exportera till CSV</button>
		<button class="btn btn-success" onclick="location.href='{/document/requestinfo/currentURI}/{/document/module/alias}/addform'; return false"><span class="glyphicon glyphicon-plus"></span> Skapa ny</button>
	</div>	 
    <table id="table" class="table table-striped" 
    	data-toggle="table" 
    	data-toolbar=".toolbar" 
    	data-search="true" 
    	data-search-on-enter-key="false"
    	data-sort-class="table-active"
    	data-sort-name="id"
    	data-sort-order="desc"
    	data-sortable="true"
    	data-page-size="15"
    	data-pagination="true">
         	
	   <thead>
	    	<tr>
<!-- 				<th data-field="state" data-checkbox="true" data-sortable="false"></th> -->
				<th data-field="id" data-sortable="true">ID</th>
				<th data-field="namn" data-sortable="true">Namn</th>
				<th data-field="ansvarig" data-sortable="true">Ansvarig</th>
				<th data-field="kontakt" data-sortable="true">Kontakt</th>
				<th data-field="register typ" data-sortable="true">Register Typ</th>
				<th data-field="status" data-sortable="true">Status</th>
				<th width="20" data-sortable="false"></th>			
			</tr>
	    </thead>
	   
	    <tbody> 
			<xsl:apply-templates select="Form"/>
	    </tbody>  
    </table>
    </form>
   
	
   	<script>
   	var $table = $('#table');
   	
   	$(document).on("keydown", "input", function(e) {
  		if (e.which==13) e.preventDefault();
	});
   	function openLink(myForm) {
   		var selectedIndex = myForm.elements["urls"].selectedIndex;
   		var url = myForm.elements["urls"].options[selectedIndex].value;
   		window.open(url);	
   	}
	</script>
</xsl:template>
  
  <xsl:template match="Form">  
  <tr>
  <!-- Col state --> 
<!--     <td> -->
<!--     </td> -->
   <!-- Col ID --> 
    <td>
      <xsl:value-of select="FormID"></xsl:value-of>
    </td>
   <!-- Col Namn and comment / actionrequired  --> 
    <td>    
        <a href="{/document/requestinfo/currentURI}/{/document/module/alias}/updateform/{FormID}">
          <xsl:value-of select="Name"/>
        </a> 
        <xsl:if test="Questionnaire/Notes='true'">       
          <div title="Registret har anteckningar." style="color:rgba(0,0,0,0.2);padding-right: 8px;;padding-top: 8px;" onclick="" class="upper-left-corner glyphicon glyphicon-comment"></div>
        </xsl:if>
        <xsl:if test="Questionnaire/ActionRequired='true'"> 
        	<div title="Åtgärder krävs." style="color:rgba(ff,ff,00,0.2);padding-right: 8px;;padding-top: 8px;" onclick="" class="upper-left-corner glyphicon glyphicon-exclamation-sign"></div>
        </xsl:if>
      </td>
     <!-- Col Ansvariga grupper -->
      <td>    
      	<xsl:value-of select="GroupName"></xsl:value-of>
      </td> 
     <!-- Col Kontakt -->
      <td>
      	<xsl:if test="Questionnaire/ContactName">
      		<xsl:value-of select="Questionnaire/ContactName" />
		</xsl:if>
      </td>    
      <!-- Col Typ -->
      <td>
      	<xsl:choose>
	      	<xsl:when test="Questionnaire/RegisterType ='26'">
	      		<xsl:text>IT-system</xsl:text>
	      	</xsl:when>
      		<xsl:when test="Questionnaire/RegisterType ='27'">
	      		<xsl:text>Strukturerat material</xsl:text>
	      	</xsl:when>
	      	<xsl:when test="Questionnaire/RegisterType ='28'">
	      		<xsl:text>Ostrukturerat material</xsl:text>
	      	</xsl:when>
	      	<xsl:otherwise>
	      		<xsl:text>Ej angivet</xsl:text>
	      	</xsl:otherwise>
      	</xsl:choose>
      </td>    
     <!-- Col Status -->
      <td>
<!--       	<xsl:value-of select="Questionnaire/QuestionnaireStatus/Status"></xsl:value-of> -->
        <xsl:choose>
        	<xsl:when test="Questionnaire/QuestionnaireStatus/StatusID=1">
           		<div style="background: red; width:24px;height:24px;border-radius: 50%;" title="Ej påbörjad"></div> 
        	</xsl:when>
          	<xsl:when test="Questionnaire/QuestionnaireStatus/StatusID=2">
         		<div style="background: orange; width:24px;height:24px;border-radius: 50%;" title="Påbörjad"></div> 
          	</xsl:when>
          	<xsl:when test="Questionnaire/QuestionnaireStatus/StatusID=3">
          		<div style="background: green; width:24px;height:24px;border-radius: 50%;" title="Klar"></div>  
          	</xsl:when>
          	<!-- Can be used if more statuses is added later on -->
<!--           	<xsl:when test="Questionnaire/QuestionnaireStatus/StatusID=4"> -->
<!--           		<div style="background: blue; width:24px;height:24px;border-radius: 50%;"></div>   -->
<!--           	</xsl:when> -->
<!--           	<xsl:when test="Questionnaire/QuestionnaireStatus/StatusID=5"> -->
<!--           		<div style="background: purple; width:24px;height:24px;border-radius: 50%;"></div>   -->
<!--           	</xsl:when> -->
          	<xsl:otherwise>
            	<xsl:value-of select="Questionnaire/QuestionnaireStatus/Status"></xsl:value-of>
          	</xsl:otherwise>
        </xsl:choose>
      </td>
     <!-- Col Icons -->
	  <td>
	  	<a style="padding-left: 20px;" href="{/document/requestinfo/currentURI}/{/document/module/alias}/updateform/{FormID}">
	    	<span class="glyphicon glyphicon-pencil" title="Uppdatera"></span></a>
	    <a style="padding-left: 20px;" href="#" onclick="if(confirm('Ta bort?')) window.location.href='{/document/requestinfo/currentURI}/{/document/module/alias}/removeform/{FormID}'">
	      	<span class="glyphicon glyphicon-remove" title="Ta bort"></span></a>
	    <a style="padding-left: 20px;" href="{/document/requestinfo/currentURI}/{/document/module/alias}/exportpdf/{FormID}">
	  		<span class="glyphicon glyphicon-export" title="Exportera till PDF"></span></a> 	  
	  </td>
   </tr> 
  </xsl:template>
</xsl:stylesheet>