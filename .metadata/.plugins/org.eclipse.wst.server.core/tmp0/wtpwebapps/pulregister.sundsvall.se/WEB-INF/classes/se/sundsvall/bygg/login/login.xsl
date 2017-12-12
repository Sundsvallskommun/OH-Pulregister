<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" version="4.0" encoding="ISO-8859-1" />

	<xsl:template match="document">
	
		<div class="row">
			<div class="col-xs-offset-3 col-xs-6 col-sm-offset-3 col-sm-6 col-md-offset-4 col-md-4 col-lg-offset-4 col-lg-4">
				<h3 class="text-center">
					<strong>Inloggning</strong>
				</h3>
				<div class="jumbotron">
					<form method="post" action="{/document/requestinfo/uri}">
						<xsl:if test="LoginFailed">
							<span class="text-danger">Felaktigt anv�ndarnamn eller l�senord!</span>
						</xsl:if>
						<xsl:if test="AccountDisabled">
							<span class="text-danger">Ditt konto �r avst�ngt, kontakta systemadministrat�ren f�r mer information.</span>
						</xsl:if>
						<xsl:if test="AccountLocked">
							<span class="text-danger">Kontot l�st i <xsl:value-of select="AccountLocked" /> minuter p.g.a f�r m�nga felaktiga inloggningsf�rs�k!</span>
						</xsl:if>
						
						<div class="form-group">
							<label for="username">Anv�ndarnamn</label>
							<input type="text" class="form-control" name="username" id="username" placeholder="Anv�ndarnamn" />
						</div>
						
						<div class="form-group">
							<label for="password">L�senord</label>
							<input type="password" class="form-control" name="password" id="password" placeholder="L�senord" />
						</div>
						<div class="text-right">
							<button type="submit" class="btn btn-success">Logga in</button>
						</div>
					</form>
				</div>
				<!-- 
				<div class="text-center">
					<a href="www.sundsvall.se" target="_blank">
						<img class="footer-logo" src="{contextpath}/img/logo.png" alt="Sundsvalls Kommun" />
					</a>
				</div> -->
			</div>
		</div>
		
	</xsl:template>

</xsl:stylesheet>