var ws;
var client;
var subscription;

$(document).ready(function() {
	connection();
	registerSearch();
});

function connection() {
	ws = new SockJS("/twitter");
	client = Stomp.over(ws);
	
	var headers = {};
	var connectCallback = {};
	client.connect(headers, connectCallback);
}

function registerSearch() {
	$("#search").submit(function(ev){
		event.preventDefault();
		
		//al realizar una búsqueda, cancelamos la suscripción anterior, si la hay
		if(subscription != undefined){
			subscription.unsubscribe();
			 $("#response").empty();
		}
		
		var query = $("#q").val();
		client.send("/app/search", {}, query);
		subscription = client.subscribe("/queue/search/" + query, function(message) {
		    // called when the client receives a STOMP message from the server
			var data = JSON.parse(message.body);	
			var template = $('#tweets').html();
		    var info = Mustache.to_html(template, data);
		    $("#response").prepend(info);
		}, {id: query});
	});
}