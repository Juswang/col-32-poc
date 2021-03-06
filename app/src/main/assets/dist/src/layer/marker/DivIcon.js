/* -*- js-indent-level: 8 -*- */
/*
 * L.DivIcon is a lightweight HTML-based icon class (as opposed to the image-based L.Icon)
 * to use with L.Marker.
 */
L.DivIcon = L.Icon.extend({
    options: {
        iconSize: [12, 12],
        /*
        iconAnchor: (Point)
        popupAnchor: (Point)
        html: (String)
        bgPos: (Point)
        */
        className: 'leaflet-div-icon',
        html: false
    },
    createIcon: function (oldIcon) {
        var div = (oldIcon && oldIcon.tagName === 'DIV') ? oldIcon : document.createElement('div'), options = this.options;
        div.innerHTML = options.html !== false ? options.html : '';
        if (options.bgPos) {
            div.style.backgroundPosition = (-options.bgPos.x) + 'px ' + (-options.bgPos.y) + 'px';
        }
        this._setIconStyles(div, 'icon');
        return div;
    },
    createShadow: function () {
        return null;
    }
});
L.divIcon = function (options) {
    return new L.DivIcon(options);
};
//# sourceMappingURL=DivIcon.js.map