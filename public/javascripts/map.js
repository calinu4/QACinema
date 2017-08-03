jQuery(function($) {
    // Asynchronously Load the map API
    var script = document.createElement('script');
    script.src = "//maps.googleapis.com/maps/api/js?key=AIzaSyDyB23qJ23ZzxifPVEnJN-0PkQI_PApExw&sensor=false&callback=initialize";
    document.body.appendChild(script);
});


function initialize() {
    var map;
    var labels = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    var labelIndex = 0;
    var bounds = new google.maps.LatLngBounds();
    var mapOptions = {
        mapTypeId: 'roadmap'
    };

    // Display a map on the page
    map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
    map.setTilt(45);

    // Multiple Markers
    var markers = [
        ['QA Cinema', 53.470843,-2.295886],
        ['Lime Restaurant', 53.471012,-2.294400],
        ['Nandos', 53.471292,-2.294805],
        ['Pizz Express', 53.470404,-2.294526],
        ['MediaCityUKTS', 53.472131, -2.297373],
        ['MediaCityUKBS', 53.471768, -2.295256]
    ];

    // Info Window Content
    var infoWindowContent = [
        ['<div class="info_content">' +
        '<h3>QA Cinema</h3>' +
        '<p>Showing the latest and best movies, family owned since 1955bc</p>' + '</div>'],
        ['<div class="info_content">' +
        '<h3>Lime Restaurant</h3>' +
        '<p>Best Limes in in Lowry</p>' + '</div>'],
        ['<div class="info_content">' +
        '<h3>Nandos</h3>' +
        '<p>Hottest chicks in town</p>' + '</div>'],
        ['<div class="info_content">' +
        '<h3>Pizza Express</h3>' +
        '<p>Delicious pizza and such</p>' + '</div>'],
        ['<div class="info_content">' +
        '<h3>MediaCity Tram Stop</h3>' +
        '<p>Local tram stop</p>' + '</div>'],
        ['<div class="info_content">' +
        '<h3>MediaCity Bus Stop</h3>' +
        '<p>Local Bus stop</p>' + '</div>']
    ];

    // Display multiple markers on a map
    var infoWindow = new google.maps.InfoWindow(), marker, i;

    // Loop through our array of markers & place each one on the map
    for( i = 0; i < markers.length; i++ ) {
        var position = new google.maps.LatLng(markers[i][1], markers[i][2]);
        bounds.extend(position);
        marker = new google.maps.Marker({
            position: position,
            label: labels[labelIndex++ % labels.length],
            map: map,
            title: markers[i][0]
        });

        // Allow each marker to have an info window
        google.maps.event.addListener(marker, 'click', (function(marker, i) {
            return function() {
                infoWindow.setContent(infoWindowContent[i][0]);
                infoWindow.open(map, marker);
            }
        })(marker, i));

        // Automatically center the map fitting all markers on the screen
        map.fitBounds(bounds);
    }

    // Override our map zoom level once our fitBounds function runs (Make sure it only runs once)
    var boundsListener = google.maps.event.addListener((map), 'bounds_changed', function(event) {
        this.setZoom(17);
        google.maps.event.removeListener(boundsListener);
    });

}