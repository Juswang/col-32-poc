/* -*- js-indent-level: 8 -*- */
/*
 * L.ImageOverlay is used to overlay images over the map (to specific geographical bounds).
 */
L.ImageOverlay = L.Layer.extend({
    options: {
        opacity: 1,
        alt: '',
        interactive: false
    },
    initialize: function (url, bounds, options) {
        this._url = url;
        this._bounds = L.latLngBounds(bounds);
        L.setOptions(this, options);
    },
    onAdd: function () {
        if (!this._image) {
            this._initImage();
            if (this.options.opacity < 1) {
                this._updateOpacity();
            }
        }
        if (this.options.interactive) {
            L.DomUtil.addClass(this._image, 'leaflet-interactive');
            this.addInteractiveTarget(this._image);
        }
        this.getPane().appendChild(this._image);
        this._reset();
    },
    onRemove: function () {
        L.DomUtil.remove(this._image);
        if (this.options.interactive) {
            this.removeInteractiveTarget(this._image);
        }
    },
    setOpacity: function (opacity) {
        this.options.opacity = opacity;
        if (this._image) {
            this._updateOpacity();
        }
        return this;
    },
    setStyle: function (styleOpts) {
        if (styleOpts.opacity) {
            this.setOpacity(styleOpts.opacity);
        }
        return this;
    },
    bringToFront: function () {
        if (this._map) {
            L.DomUtil.toFront(this._image);
        }
        return this;
    },
    bringToBack: function () {
        if (this._map) {
            L.DomUtil.toBack(this._image);
        }
        return this;
    },
    setUrl: function (url) {
        this._url = url;
        if (this._image) {
            this._image.src = url;
        }
        return this;
    },
    getAttribution: function () {
        return this.options.attribution;
    },
    getEvents: function () {
        var events = {
            viewreset: this._reset
        };
        return events;
    },
    getBounds: function () {
        return this._bounds;
    },
    _initImage: function () {
        var img = this._image = L.DomUtil.create('img', 'leaflet-image-layer');
        img.onselectstart = L.Util.falseFn;
        img.onmousemove = L.Util.falseFn;
        img.onload = L.bind(this.fire, this, 'load');
        img.src = this._url;
        img.alt = this.options.alt;
    },
    _reset: function () {
        var image = this._image, bounds = new L.Bounds(this._map.latLngToLayerPoint(this._bounds.getNorthWest()), this._map.latLngToLayerPoint(this._bounds.getSouthEast())), size = bounds.getSize();
        L.DomUtil.setPosition(image, bounds.min);
        image.style.width = size.x + 'px';
        image.style.height = size.y + 'px';
    },
    _updateOpacity: function () {
        L.DomUtil.setOpacity(this._image, this.options.opacity);
    }
});
L.imageOverlay = function (url, bounds, options) {
    return new L.ImageOverlay(url, bounds, options);
};
//# sourceMappingURL=ImageOverlay.js.map