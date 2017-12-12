$(function() {
	$searchwrapper = $("#searchwrapper");
	$("#search, #small-search-input").keyup(function() {
		$this = $(this);
		
		if ($this.val().length > 1) {
			if (!$searchwrapper.is(":visible")) {
				$searchwrapper.slideToggle();
			}
			
			$.get(contextPath + '/search?q=' + $this.val(), function(response) {
				var resultDivs = '';
				
				if (response.hits > 0) {
					resultDivs += '<table class="table table-striped">';
					for (i = 0; i < response.result.length && i < 5; i++) {
						var fragment = response.result[i].fragment == null ? "" : response.result[i].fragment; 
						resultDivs += '<tr>';
						resultDivs += '<td class="result-icon"><span class="glyphicon glyphicon-list-alt"></span></td>';
						resultDivs += '<td><a href="' + contextPath + "/" + response.result[i].fullAlias + '"><strong>' + response.result[i].title + '</strong></a><br/>' + fragment + '</td>';
						resultDivs += '<td class="result-link"><a href="' + contextPath + "/" + response.result[i].fullAlias + '"><span class="glyphicon glyphicon-play-circle"></span></a></td>';
						resultDivs += '</tr>';
					}
					resultDivs += '</table>';
				}
				else {
					resultDivs += '<div>Inga projekt matchar angivet sökord.</div>';
				}
				
				$("#search-projects").html(resultDivs);
			});
			
			$.get(contextPath + '/lmsearch/addresses?json&q=' + $this.val(), function(response) {
				var resultDivs = '';
				
				if (response.result && response.result.length > 0) {
					resultDivs += '<table class="table table-striped table-condensed">';
					for (i = 0; i < response.result.length && i < 6; i++) {
						var fragment = response.result[i];
						resultDivs += '<tr>';
						resultDivs += '<td class="result-icon"><span class="glyphicon glyphicon-map-marker"></span></td>';
						resultDivs += '<td><a href="' + contextPath + '/#' + fragment[2] + ',' + fragment[3] + '">' + fragment[1] + '</a></td>';
						resultDivs += '<td class="result-link"><a href="' + contextPath + '/#' + fragment[2] + ',' + fragment[3] + '"><span class="glyphicon glyphicon-play-circle"></span></a></td>';
						resultDivs += '</tr>';
					}
					resultDivs += '</table>';
				}
				else {
					resultDivs += '<div>Inga adresser matchar angivet sökord.</div>';
				}
				
				$("#search-addresses").html(resultDivs);
			});
			
			$.get(contextPath + '/lmsearch/placenames?json&q=' + $this.val(), function(response) {
				var resultDivs = '';
				
				if (response.result && response.result.length > 0) {
					resultDivs += '<table class="table table-striped table-condensed">';
					for (i = 0; i < response.result.length && i < 2; i++) {
						var fragment = response.result[i];
						resultDivs += '<tr>';
						resultDivs += '<td class="result-icon"><span class="glyphicon glyphicon-list-alt"></span></td>';
						resultDivs += '<td><a href="' + contextPath + '/#' + fragment.geometry.coordinates[0] + ',' + fragment.geometry.coordinates[1] + '">' + fragment.properties.name + ', ' + fragment.properties.municipality + '</a></td>';
						resultDivs += '<td class="result-link"><a href="' + contextPath + '/#' + fragment.geometry.coordinates[0] + ',' + fragment.geometry.coordinates[1] + '"><span class="glyphicon glyphicon-play-circle"></span></a></td>';
						resultDivs += '</tr>';
					}
					resultDivs += '</table>';
				}
				else {
					resultDivs += '<div>Inga områden matchar angivet sökord.</div>';
				}
				
				$("#search-areas").html(resultDivs);
			});
		}
	});
	
	$("#search").keyup(function() {
		$("#small-search-input").val($(this).val());
	});
	
	$("#small-search-input").keyup(function() {
		$("#search").val($(this).val());
	});
	
	$("#closesearch").click(function() {
		$searchwrapper.slideToggle();
		
		return false;
	});
	
	$("#small-header").on("click", ".togglesearch", function() {
		$("#small-menu-wrapper, #small-search").toggle()
	});
	
	$("#toggle-primary").click(function() {
		$("#small-menu").toggle();
	});
});