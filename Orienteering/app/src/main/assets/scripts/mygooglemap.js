
var imgAndroid = "images/android.png";

function initialize() {
    var mapOptions = {
      center: new google.maps.LatLng(-34.397, 150.644),
      zoom: 16,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
    myloc = new google.maps.Marker({
        clickable: false,
        icon: imgAndroid,
        shadow: null,
        zIndex: 999,
        map:map // your google.maps.Map object
    });
    var centerControlDiv = document.createElement('div');
    var centerControl = new CenterControl(centerControlDiv, map);

    centerControlDiv.index = 1;
    map.controls[google.maps.ControlPosition.RIGHT_BOTTOM].push(centerControlDiv);

}

function moveMarkerTo( latitude , longitude ){
    myLatlng = new google.maps.LatLng(latitude,longitude);
    myloc.setPosition( myLatlng );
}
function centerAt(latitude, longitude){//地圖以給予的座標為中心，即移動地圖至給定的座標
     myLatlng = new google.maps.LatLng(latitude,longitude);
     map.panTo(myLatlng);
     myloc.setPosition( myLatlng );
}

function CenterControl(controlDiv, map) {

  // Set CSS for the control border
  var controlUI = document.createElement('div');
  controlUI.style.backgroundColor = '#fff';
  controlUI.style.border = '2px solid #fff';
  controlUI.style.borderRadius = '3px';
  controlUI.style.boxShadow = '0 2px 6px rgba(0,0,0,.3)';
  controlUI.style.cursor = 'pointer';
  controlUI.style.marginBottom = '22px';
  controlUI.style.marginRight  = '10px';
  controlUI.style.textAlign = 'center';
  controlUI.style.verticalAlign = 'center'
  controlUI.style.width = '34px'
  controlUI.style.height = '34px'
  controlUI.title = 'Click to recenter the map';
  controlDiv.appendChild(controlUI);

  // Set CSS for the control interior
  var controlImg = document.createElement('img');
  controlImg.src = 'images/myposition.png';
  controlImg.height = '30'
  controlImg.width = '30'
  controlUI.appendChild(controlImg);

  // Setup the click event listeners: simply set the map to
  // Chicago
  google.maps.event.addDomListener(controlUI, 'click', goToCurrentPosition);

}


function goToCurrentPosition(){
    if(navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function(position) {
      var pos = new google.maps.LatLng(position.coords.latitude,
                                       position.coords.longitude);

      var infowindow = new google.maps.InfoWindow({
        map: map,
        position: pos,
        content: 'You are here.'
      });

      centerAt( position.coords.latitude, position.coords.longitude );
    }, function() {
      handleNoGeolocation(true);
    });
  } else {
    // Browser doesn't support Geolocation
    handleNoGeolocation(false);
  }
}




function handleNoGeolocation(errorFlag) {
  if (errorFlag) {
    var content = 'Error: The Geolocation service failed.';
  } else {
    var content = 'Error: Your browser doesn\'t support geolocation.';
  }

  var options = {
    map: map,
    position: new google.maps.LatLng(23.5, 120.5),
    content: content
  };

  var infowindow = new google.maps.InfoWindow(options);
  map.setCenter(options.position);
}