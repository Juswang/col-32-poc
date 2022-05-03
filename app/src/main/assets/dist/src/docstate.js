/* global Proxy */
window.app = {
    definitions: {},
    dpiScale: window.devicePixelRatio,
    roundedDpiScale: Math.round(window.devicePixelRatio),
    twipsToPixels: 0,
    pixelsToTwips: 0,
    file: {
        editComment: false,
        readOnly: true,
        disableSidebar: false,
        size: {
            pixels: [0, 0],
            twips: [0, 0]
        },
        viewedRectangle: [0, 0, 0, 0],
        fileBasedView: false,
        calc: {
            cellCursor: {
                address: [0, 0],
                rectangle: {
                    pixels: [0, 0, 0, 0],
                    twips: [0, 0, 0, 0]
                },
                visible: false,
            }
        },
        writer: {
            pageRectangleList: [] // Array of arrays: [x, y, w, h] (as usual) // twips only. Pixels will be calculated on the fly. Corresponding pixels may change too ofte
        },
    },
    view: {
        commentHasFocus: false,
        size: {
            pixels: [0, 0] // This can be larger than the document's size.
        }
    },
    tile: {
        size: {
            pixels: [0, 0],
            twips: [0, 0]
        }
    },
    socket: window.app.socket,
    console: window.app.console,
};
var activateValidation = false;
if (activateValidation) {
    /*
        For debugging purposes.
    
        * Easier debugging.
        * Value range checks.
    
        It logs the changes of the variables of "window.app" object.
        This provides debugging of window.app object and makes easier use of these global states of the document.
        window.app object can contain cursor position etc. variables related to document state.
        One needs to only watch the data structure and add new variables into related sub-object (file, view, tile etc.).
    
        This validator also enables global data validation.
        If a variable of window.app should stay in a specified range:
            * One can add a check for that variable into "set" function below.
        This validation feature may also be useful with Cypress tests and Javascript unit tests.
    
        This first version only contains the logging of the changes.
    */
    var validator = {
        set: function (obj, prop, value) {
            // The default behavior to store the value
            obj[prop] = value;
            console.log('window.app property changed: ' + prop, value);
            // Indicate success
            return true;
        }
    };
    window.app = new Proxy(window.app, validator);
}
//# sourceMappingURL=docstate.js.map