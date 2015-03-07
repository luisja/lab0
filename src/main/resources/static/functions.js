$(document).ready(function() {
	registerSearch();
});

function registerSearch() {
	$("#search").submit(function(ev){
		event.preventDefault();
		$.getJSON($(this).attr('action'), {q: $("#q").val()}, function(data) {
		    var template = $('#tweets').html();
		    var info = Mustache.to_html(template, data);
		    $("#resultsBlock").empty().append(info);
		});
	});
}

