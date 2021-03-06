/* -*- js-indent-level: 8 -*- */
/*
 * L.Handler.ShiftDragZoom is used to add shift-drag zoom interaction to the map
  * (zoom to a selected bounding box), enabled by default.
 */
L.Map.mergeOptions({
    boxZoom: true
});
L.Map.BoxZoom = L.Handler.extend({
    initialize: function (map) {
        this._map = map;
        this._container = map._container;
        this._pane = map._panes.overlayPane;
    },
    addHooks: function () {
        L.DomEvent.on(this._container, 'mousedown', this._onMouseDown, this);
    },
    removeHooks: function () {
        L.DomEvent.off(this._container, 'mousedown', this._onMouseDown, this);
    },
    moved: function () {
        return this._moved;
    },
    _onMouseDown: function (e) {
        if (this._map._docLayer._hasActiveSelection)
            return false;
        if (!e.shiftKey || ((e.which !== 1) && (e.button !== 0))) {
            return false;
        }
        this._moved = false;
        L.DomUtil.disableTextSelection();
        L.DomUtil.disableImageDrag();
        this._startPoint = this._map.mouseEventToContainerPoint(e);
        L.DomEvent.on(document, {
            contextmenu: L.DomEvent.stop,
            mousemove: this._onMouseMove,
            mouseup: this._onMouseUp,
            keydown: this._onKeyDown
        }, this);
    },
    _onMouseMove: function (e) {
        if (!this._moved) {
            this._moved = true;
            this._box = L.DomUtil.create('div', 'leaflet-zoom-box', this._container);
            L.DomUtil.addClass(this._container, 'leaflet-crosshair');
            this._map.fire('boxzoomstart');
        }
        this._point = this._map.mouseEventToContainerPoint(e);
        var bounds = new L.Bounds(this._point, this._startPoint), size = bounds.getSize();
        L.DomUtil.setPosition(this._box, bounds.min);
        this._box.style.width = size.x + 'px';
        this._box.style.height = size.y + 'px';
    },
    _finish: function () {
        if (this._moved) {
            L.DomUtil.remove(this._box);
            L.DomUtil.removeClass(this._container, 'leaflet-crosshair');
        }
        L.DomUtil.enableTextSelection();
        L.DomUtil.enableImageDrag();
        L.DomEvent.off(document, {
            contextmenu: L.DomEvent.stop,
            mousemove: this._onMouseMove,
            mouseup: this._onMouseUp,
            keydown: this._onKeyDown
        }, this);
    },
    _onMouseUp: function (e) {
        if ((e.which !== 1) && (e.button !== 0)) {
            return;
        }
        this._finish();
        if (!this._moved) {
            return;
        }
    },
    _onKeyDown: function (e) {
        if (e.keyCode === 27) {
            this._finish();
        }
    }
});
//# sourceMappingURL=Map.BoxZoom.js.map