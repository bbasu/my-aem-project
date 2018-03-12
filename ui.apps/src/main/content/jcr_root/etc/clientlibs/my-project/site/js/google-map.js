 /*exported initialize */
function initialize(){

        var mapCenter = {lat: Number($("#google-map").attr("data-latitude")),lng:Number($("#google-map").attr("data-longitude"))};
        var mapProp = {
                center: mapCenter,
                zoom:Number($("#google-map").attr("data-zoom")),
                streetViewControl:false,
                mapTypeId: google.maps.MapTypeId.ROADMAP};
        var map = new google.maps.Map(document.getElementById("google-map"), mapProp);

        var marker,infowindow;
        if($("#google-map").attr("data-map-overlay") === "marker" || $("#google-map").attr("data-map-overlay") === "animatedmarker"){
            if($("#google-map").attr("data-map-overlay") === "animatedmarker"){
                marker=new google.maps.Marker({
                    position:mapCenter,
                    animation:google.maps.Animation.BOUNCE
                });
            }else{
                 marker=new google.maps.Marker({
                    position:mapCenter
                });
            }
            marker.setMap(map);

            infowindow = new google.maps.InfoWindow({
                content:$("#google-map").attr("data-marker-info")
            });

            google.maps.event.addListener(marker, 'click', function() {
                infowindow.open(map,marker);
            });
        }
}

function loadScript(){

    if(document.getElementById("google-map")!==null){
		if(($("#google-map").attr("data-latitude")=== null || 
           $("#google-map").attr("data-latitude")=== undefined ||
           $("#google-map").attr("data-longitude")=== null || 
           $("#google-map").attr("data-longitude")=== undefined) &&
           ($("#google-map").attr("data-searchaddress")!== null || 
            $("#google-map").attr("data-searchaddress")!== undefined)){

               var map = "<iframe width='100%' height='100%'";
               map = map + "src='//www.google.com/maps/embed/v1/search?key="+$("#google-map").attr("data-google-key");
               map = map + "&q=" + $("#google-map").attr("data-search-address").replace(" ", "+") + "' allowfullscreen>";
               map = map + "</iframe>";

               $("#google-map").append( map );
           }else{
               var script = document.createElement("script");
               script.type = "text/javascript";
               script.src = "//maps.googleapis.com/maps/api/js?key="+$("#google-map").attr("data-google-key")+"&callback=initialize";
               document.getElementById("google-map").appendChild(script);
           }
    }
}

jQuery(document).ready(function ($) { 
	loadScript();
});