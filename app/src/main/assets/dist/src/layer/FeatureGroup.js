/* -*- js-indent-level: 8 -*- */
/*
 * L.FeatureGroup extends L.LayerGroup by introducing mouse events and additional methods
 * shared between a group of interactive layers (like vectors or markers).
 */
L.FeatureGroup = L.LayerGroup.extend({
    addLayer: function (layer) {
        if (this.hasLayer(layer)) {
            return this;
        }
        layer.addEventParent(this);
        L.LayerGroup.prototype.addLayer.call(this, layer);
        if (this._popupContent && layer.bindPopup) {
            layer.bindPopup(this._popupContent, this._popupOptions);
        }
        return this.fire('layeradd', { layer: layer });
    },
    removeLayer: function (layer) {
        if (!this.hasLayer(layer)) {
            return this;
        }
        if (layer in this._layers) {
            layer = this._layers[layer];
        }
        layer.removeEventParent(this);
        L.LayerGroup.prototype.removeLayer.call(this, layer);
        if (this._popupContent) {
            this.invoke('unbindPopup');
        }
        return this.fire('layerremove', { layer: layer });
    },
    bindPopup: function (content, options) {
        this._popupContent = content;
        this._popupOptions = options;
        return this.invoke('bindPopup', content, options);
    },
    openPopup: function (latlng) {
        // open popup on the first layer
        for (var id in this._layers) {
            this._layers[id].openPopup(latlng);
            break;
        }
        return this;
    },
    setStyle: function (style) {
        return this.invoke('setStyle', style);
    },
    bringToFront: function () {
        return this.invoke('bringToFront');
    },
    bringToBack: function () {
        return this.invoke('bringToBack');
    },
    getBounds: function () {
        var bounds = new L.LatLngBounds();
        this.eachLayer(function (layer) {
            bounds.extend(layer.getBounds ? layer.getBounds() : layer.getLatLng());
        });
        return bounds;
    }
});
L.featureGroup = function (layers) {
    return new L.FeatureGroup(layers);
};
//# sourceMappingURL=FeatureGroup.js.map