<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" encoding="utf-8" indent="yes" />

  <xsl:template match="ListNodes">
    <xsl:if test="./Nodes">
    </xsl:if>
    <xsl:choose>
      <xsl:when test="./Nodes">
        <xsl:apply-templates/>
      </xsl:when>
      <xsl:otherwise>
        <div class="container">     
            <div class="span12">      
                <button type="button" onclick="location.href='{/document/requestinfo/currentURI}/{/document/module/alias}/addfacility/1'; return false" class="btn btn-success pull-center biggermargintop"><span class="glyphicon glyphicon-plus"></span>&#160;Skapa ny</button>
            </div> 
        </div>
          
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template> 
  
  <xsl:template match="Nodes">  
  
  <div class="panel-content pull-right" style="margin-right: 50px;">
    <button type="button" onclick="location.href='{/document/requestinfo/currentURI}/{/document/module/alias}/exportcsv'; return false" class="btn btn-default biggermargintop"><span class="glyphicon glyphicon-export"></span>&#160;Exportera till CSV</button>
    <span class="hspace20"></span>
    <button type="button" onclick="location.href='{/document/requestinfo/currentURI}/{/document/module/alias}/addfacility/1'; return false" class="btn btn-success biggermargintop"><span class="glyphicon glyphicon-plus"></span>&#160;Skapa ny</button>
  </div>
    <table class="table table-condense table-striped">
      <thead>
      
      <tr>
        <th width="10"></th>
        <th width="10"></th>
        <th width="40">Namn</th>                  
        <th width="40">Ansvarig</th>              
        <th width="40">Status</th>
        <th width="20"></th>
      </tr>
      
    </thead>
    <tbody> 
      <xsl:apply-templates select="NodeOwner"/>
      </tbody>      
    </table>
  </xsl:template>
  
  <xsl:template match="NodeAttribute">      
      <xsl:choose>
        <xsl:when test="./name='Roll'">         
          <xsl:value-of select="value"/>          
        </xsl:when>
        <xsl:when test="./name='Ändamål'">          
          <xsl:value-of select="value"/>          
        </xsl:when>
        <xsl:otherwise>         
        </xsl:otherwise>        
      </xsl:choose>                 
  </xsl:template>
  
  <xsl:template match="NodeOwner">  
  <tr>
    <td/>
    <td>
      <span style="padding:8px;"/>
    </td>
    <td>    
        <a href="{/document/requestinfo/currentURI}/{/document/module/alias}/updatefacility/{node_id}">
          <xsl:value-of select="name"/>
        </a> 
        <xsl:if test="hasNotes='true'">       
          <div title="Registret har anteckningar." style="color:rgba(0,0,0,0.2);padding-right: 8px;;padding-top: 8px;" onclick="" class="upper-left-corner glyphicon glyphicon-comment"></div>
        </xsl:if>
      </td>
      <td>    
        <xsl:choose>
          <xsl:when test="NodeAttributes/NodeAttribute/NodeTemplateAttribute[templateAttributeID='2']/../value='11'">
            <xsl:value-of select="NodeAttributes/NodeAttribute/NodeTemplateAttribute[templateAttributeID='35']/../value"></xsl:value-of>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="NodeAttributes/NodeAttribute/NodeTemplateAttribute[templateAttributeID='2']/../displayvalue"></xsl:value-of>
          </xsl:otherwise>
        </xsl:choose>                     
      </td>     
      <td>
        <xsl:choose>
          <xsl:when test="NodeAttributes/NodeAttribute/NodeTemplateAttribute[templateAttributeID='64']/../displayvalue='Ej påbörjad'">
          <div style="background: red; width:24px;height:24px;border-radius: 50%;"></div> <!-- #de6c53 -->
          </xsl:when>
          <xsl:when test="NodeAttributes/NodeAttribute/NodeTemplateAttribute[templateAttributeID='64']/../displayvalue='Påbörjad'">
          <div style="background: orange; width:24px;height:24px;border-radius: 50%;"></div> <!-- #f4b73c -->
          </xsl:when>
          <xsl:when test="NodeAttributes/NodeAttribute/NodeTemplateAttribute[templateAttributeID='64']/../displayvalue='Klar'">
          <div style="background: green; width:24px;height:24px;border-radius: 50%;"></div> <!--  #c6de51 -->
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="NodeAttributes/NodeAttribute/NodeTemplateAttribute[templateAttributeID='64']/../displayvalue"></xsl:value-of>
          </xsl:otherwise>
        </xsl:choose>
        
      </td>
    <td>
    
      <a style="padding-left: 15px;" href="{/document/requestinfo/currentURI}/{/document/module/alias}/updatefacility/{node_id}">
          <span class="glyphicon glyphicon-pencil"></span>
        </a>
        <a style="padding-left: 15px;" href="#" onclick="if(confirm('Ta bort?')) window.location.href='{/document/requestinfo/currentURI}/{/document/module/alias}/removefacility/{node_id}'">
          <span class="glyphicon glyphicon-remove"></span>
        </a>
        
    </td>
    </tr> 
  </xsl:template>
  
 </xsl:stylesheet>