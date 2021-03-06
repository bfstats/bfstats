var BfStats = (function() {

function getGeoJsonProps(type) {
  var geoJsonProps = {
    onEachFeature: function (feature, layer) {
      if (feature.properties && feature.properties.popupContent) {
        layer.bindPopup(feature.properties.popupContent);
      }
    },
    pointToLayer: function (feature, latLng) {
      return L.circleMarker(latLng, {
        radius: 5,
        fillColor: "#ff7800",
        color: "#000",
        weight: 1,
        opacity: 1,
        fillOpacity: 0.8
      });
    },
    style: function (feature) {
      switch (type) {
        case 'kill':  return {fillColor: "yellow"};
        case 'death': return {fillColor: "gray"};
      }
    }
  };

  return geoJsonProps;
}

var heatMapProps = {
  radius: 35,
  blur: 15,
  gradient: {
    0.4: 'blue',
    0.6: 'cyan',
    0.7: 'lime',
    0.8: 'yellow',
    1.0: 'red'
  },
}

function geoJson2heat(geojson) {
  return geojson.features.map(function(feature) {
    return [parseFloat(feature.geometry.coordinates[1]), parseFloat(feature.geometry.coordinates[0])];
  });
}

function initMap(containerId, mapEvents, radarImagePath, lightmapImagePath, activeOverlayLayers) {
  var killsGroup = L.geoJson(mapEvents.killFeatureCollection, getGeoJsonProps('kill'));
  var deathsGroup = L.geoJson(mapEvents.deathFeatureCollection, getGeoJsonProps('death'));

  var bounds = [[0, 0], [mapEvents.mapSize, mapEvents.mapSize]];
  var radarLayer = L.imageOverlay(radarImagePath, bounds);
  var lightmapLayer = L.imageOverlay(lightmapImagePath, bounds);

  var killsForHeatLayer = geoJson2heat(mapEvents.killFeatureCollection);
  var killsHeatLayer = L.heatLayer(killsForHeatLayer, heatMapProps);

  var deathsForHeatLayer = geoJson2heat(mapEvents.deathFeatureCollection);
  var deathsHeatLayer = L.heatLayer(deathsForHeatLayer, heatMapProps);


  var activeLayers = activeOverlayLayers.map(function(layerName) {
    switch (layerName) {
      case "kills":
        return killsGroup;
      case "deaths":
        return deathsGroup;
      case "kills_heatmap":
        return killsHeatLayer;
      case "deaths_heatmap":
        return deathsHeatLayer;
      default:
        throw "illegal layer: " + layerName;
    }
  })

  activeLayers.push(radarLayer); // default background

  var leafletMap = L.map(containerId, {
    crs: L.CRS.Simple,
    dragging: !L.Browser.mobile,
    tap: false,
    minZoom: -2,
    maxZoom: 8,
    layers: activeLayers // default active layers
  });

  leafletMap.fitBounds(bounds);

  var baseMaps = {
      "Radar": radarLayer,
      "Lightmap": lightmapLayer
  };
  var overlayMaps = {
      "Kills Heatmap": killsHeatLayer,
      "Deaths Heatmap": deathsHeatLayer,
      "Kills": killsGroup,
      "Deaths": deathsGroup
  };
  L.control.layers(baseMaps, overlayMaps).addTo(leafletMap);

  var legend = L.control({position: 'bottomright'});

  legend.onAdd = function (map) {
      var kills = mapEvents.killFeatureCollection.features.length;
      var deaths = mapEvents.deathFeatureCollection.features.length;

      var div = L.DomUtil.create('div', 'info legend');

      var inner = '<div class="legend map_point map_point_kills" style="float:left;"></div>' +
      '<span style="float:left;">Kills (' + kills + ')</span>' +
      '<div class="legend map_point map_point_deaths" style="float:left;"></div>' +
      '<span style="float:left;">Deaths (' + deaths + ')</span>';

      div.innerHTML = inner;
      return div;
  };

  legend.addTo(leafletMap);
}

 return {
    initializeMap: function(containerId, mapEvents, radarImagePath, lightmapImagePath, activeOverlayLayers) {
      initMap(containerId, mapEvents, radarImagePath, lightmapImagePath, activeOverlayLayers);
    }
 }
})();
