(function (L) {
    var u;
    L.fn.emptyForce = function () {
        for (var ah = 0, ai; (ai = L(this)[ah]) != null; ah++)
        {
            if (ai.nodeType === 1)
            {
                L.cleanData(ai.getElementsByTagName("*"))
            }
            if (L.jqplot.use_excanvas)
            {
                ai.outerHTML = ""
            }
            else
            {
                while (ai.firstChild)
                {
                    ai.removeChild(ai.firstChild)
                }
            }
            ai = null
        }
        return L(this)
    };
    L.fn.removeChildForce = function (ah) {
        while (ah.firstChild)
        {
            this.removeChildForce(ah.firstChild);
            ah.removeChild(ah.firstChild)
        }
    };
    L.fn.jqplot = function () {
        var ah = [];
        var aj = [];
        for (var ak = 0, ai = arguments.length; ak < ai; ak++)
        {
            if (L.isArray(arguments[ak]))
            {
                ah.push(arguments[ak])
            }
            else
            {
                if (L.isPlainObject(arguments[ak]))
                {
                    aj.push(arguments[ak])
                }
            }
        }
        return this.each(function (an) {
            var at, ar, aq = L(this), am = ah.length, al = aj.length, ap, ao;
            if (an < am)
            {
                ap = ah[an]
            }
            else
            {
                ap = am ? ah[am - 1] : null
            }
            if (an < al)
            {
                ao = aj[an]
            }
            else
            {
                ao = al ? aj[al - 1] : null
            }
            at = aq.attr("id");
            if (at === u)
            {
                at = "jqplot_target_" + L.jqplot.targetCounter++;
                aq.attr("id", at)
            }
            ar = L.jqplot(at, ap, ao);
            aq.data("jqplot", ar)
        })
    };
    L.jqplot = function (an, ak, ai) {
        var aj = null, ah = null;
        if (arguments.length === 3)
        {
            aj = ak;
            ah = ai
        }
        else
        {
            if (arguments.length === 2)
            {
                if (L.isArray(ak))
                {
                    aj = ak
                }
                else
                {
                    if (L.isPlainObject(ak))
                    {
                        ah = ak
                    }
                }
            }
        }
        if (aj === null && ah !== null && ah.data)
        {
            aj = ah.data
        }
        var am = new R();
        L("#" + an).removeClass("jqplot-error");
        if (L.jqplot.config.catchErrors)
        {
            try
            {
                am.init(an, aj, ah);
                am.draw();
                am.themeEngine.init.call(am);
                return am
            }
            catch (al)
            {
                var ao = L.jqplot.config.errorMessage || al.message;
                L("#" + an).append(
                    '<div class="jqplot-error-message">' + ao + "</div>");
                L("#" + an).addClass("jqplot-error");
                document.getElementById(an).style.background = L.jqplot.config.errorBackground;
                document.getElementById(an).style.border = L.jqplot.config.errorBorder;
                document.getElementById(an).style.fontFamily = L.jqplot.config.errorFontFamily;
                document.getElementById(an).style.fontSize = L.jqplot.config.errorFontSize;
                document.getElementById(an).style.fontStyle = L.jqplot.config.errorFontStyle;
                document.getElementById(an).style.fontWeight = L.jqplot.config.errorFontWeight
            }
        }
        else
        {
            am.init(an, aj, ah);
            am.draw();
            am.themeEngine.init.call(am);
            return am
        }
    };
    L.jqplot.version = "1.0.8";
    L.jqplot.revision = "1250";
    L.jqplot.targetCounter = 1;
    L.jqplot.CanvasManager = function () {
        if (typeof L.jqplot.CanvasManager.canvases == "undefined")
        {
            L.jqplot.CanvasManager.canvases = [];
            L.jqplot.CanvasManager.free = []
        }
        var ah = [];
        this.getCanvas = function () {
            var ak;
            var aj = true;
            if (!L.jqplot.use_excanvas)
            {
                for (var al = 0, ai = L.jqplot.CanvasManager.canvases.length; al < ai; al++)
                {
                    if (L.jqplot.CanvasManager.free[al] === true)
                    {
                        aj = false;
                        ak = L.jqplot.CanvasManager.canvases[al];
                        L.jqplot.CanvasManager.free[al] = false;
                        ah.push(al);
                        break
                    }
                }
            }
            if (aj)
            {
                ak = document.createElement("canvas");
                ah.push(L.jqplot.CanvasManager.canvases.length);
                L.jqplot.CanvasManager.canvases.push(ak);
                L.jqplot.CanvasManager.free.push(false)
            }
            return ak
        };
        this.initCanvas = function (ai) {
            if (L.jqplot.use_excanvas)
            {
                return window.G_vmlCanvasManager.initElement(ai)
            }
            return ai
        };
        this.freeAllCanvases = function () {
            for (var aj = 0, ai = ah.length; aj < ai; aj++)
            {
                this.freeCanvas(ah[aj])
            }
            ah = []
        };
        this.freeCanvas = function (ai) {
            if (L.jqplot.use_excanvas
                && window.G_vmlCanvasManager.uninitElement !== u)
            {
                window.G_vmlCanvasManager
                    .uninitElement(L.jqplot.CanvasManager.canvases[ai]);
                L.jqplot.CanvasManager.canvases[ai] = null
            }
            else
            {
                var aj = L.jqplot.CanvasManager.canvases[ai];
                aj.getContext("2d").clearRect(0, 0, aj.width, aj.height);
                L(aj).unbind().removeAttr("class").removeAttr("style");
                L(aj).css({
                    left: "",
                    top: "",
                    position: ""
                });
                aj.width = 0;
                aj.height = 0;
                L.jqplot.CanvasManager.free[ai] = true
            }
        }
    };
    L.jqplot.log = function () {
        if (window.console)
        {
            window.console.log.apply(window.console, arguments)
        }
    };
    L.jqplot.config = {
        addDomReference: false,
        enablePlugins: false,
        defaultHeight: 300,
        defaultWidth: 400,
        UTCAdjust: false,
        timezoneOffset: new Date(new Date().getTimezoneOffset() * 60000),
        errorMessage: "",
        errorBackground: "",
        errorBorder: "",
        errorFontFamily: "",
        errorFontSize: "",
        errorFontStyle: "",
        errorFontWeight: "",
        catchErrors: false,
        defaultTickFormatString: "%.1f",
        defaultColors: ["#4bb2c5", "#EAA228", "#c5b47f", "#579575",
            "#839557", "#958c12", "#953579", "#4b5de4", "#d8b83f",
            "#ff5800", "#0085cc", "#c747a3", "#cddf54", "#FBD178",
            "#26B4E3", "#bd70c7"],
        defaultNegativeColors: ["#498991", "#C08840", "#9F9274", "#546D61",
            "#646C4A", "#6F6621", "#6E3F5F", "#4F64B0", "#A89050",
            "#C45923", "#187399", "#945381", "#959E5C", "#C7AF7B",
            "#478396", "#907294"],
        dashLength: 4,
        gapLength: 4,
        dotGapLength: 2.5,
        srcLocation: "jqplot/src/",
        pluginLocation: "jqplot/src/plugins/"
    };
    L.jqplot.arrayMax = function (ah) {
        return Math.max.apply(Math, ah)
    };
    L.jqplot.arrayMin = function (ah) {
        return Math.min.apply(Math, ah)
    };
    L.jqplot.enablePlugins = L.jqplot.config.enablePlugins;
    L.jqplot.support_canvas = function () {
        if (typeof L.jqplot.support_canvas.result == "undefined")
        {
            L.jqplot.support_canvas.result = !!document.createElement("canvas").getContext
        }
        return L.jqplot.support_canvas.result
    };
    L.jqplot.support_canvas_text = function () {
        if (typeof L.jqplot.support_canvas_text.result == "undefined")
        {
            if (window.G_vmlCanvasManager !== u
                && window.G_vmlCanvasManager._version > 887)
            {
                L.jqplot.support_canvas_text.result = true
            }
            else
            {
                L.jqplot.support_canvas_text.result = !!(document
                    .createElement("canvas").getContext && typeof document
                    .createElement("canvas").getContext("2d").fillText == "function")
            }
        }
        return L.jqplot.support_canvas_text.result
    };
    L.jqplot.use_excanvas = ((!L.support.boxModel || !L.support.objectAll || !$support.leadingWhitespace) && !L.jqplot
        .support_canvas()) ? true : false;
    L.jqplot.preInitHooks = [];
    L.jqplot.postInitHooks = [];
    L.jqplot.preParseOptionsHooks = [];
    L.jqplot.postParseOptionsHooks = [];
    L.jqplot.preDrawHooks = [];
    L.jqplot.postDrawHooks = [];
    L.jqplot.preDrawSeriesHooks = [];
    L.jqplot.postDrawSeriesHooks = [];
    L.jqplot.preDrawLegendHooks = [];
    L.jqplot.addLegendRowHooks = [];
    L.jqplot.preSeriesInitHooks = [];
    L.jqplot.postSeriesInitHooks = [];
    L.jqplot.preParseSeriesOptionsHooks = [];
    L.jqplot.postParseSeriesOptionsHooks = [];
    L.jqplot.eventListenerHooks = [];
    L.jqplot.preDrawSeriesShadowHooks = [];
    L.jqplot.postDrawSeriesShadowHooks = [];
    L.jqplot.ElemContainer = function () {
        this._elem;
        this._plotWidth;
        this._plotHeight;
        this._plotDimensions = {
            height: null,
            width: null
        }
    };
    L.jqplot.ElemContainer.prototype.createElement = function (ak, am, ai, aj,
                                                               an) {
        this._offsets = am;
        var ah = ai || "jqplot";
        var al = document.createElement(ak);
        this._elem = L(al);
        this._elem.addClass(ah);
        this._elem.css(aj);
        this._elem.attr(an);
        al = null;
        return this._elem
    };
    L.jqplot.ElemContainer.prototype.getWidth = function () {
        if (this._elem)
        {
            return this._elem.outerWidth(true)
        }
        else
        {
            return null
        }
    };
    L.jqplot.ElemContainer.prototype.getHeight = function () {
        if (this._elem)
        {
            return this._elem.outerHeight(true)
        }
        else
        {
            return null
        }
    };
    L.jqplot.ElemContainer.prototype.getPosition = function () {
        if (this._elem)
        {
            return this._elem.position()
        }
        else
        {
            return {
                top: null,
                left: null,
                bottom: null,
                right: null
            }
        }
    };
    L.jqplot.ElemContainer.prototype.getTop = function () {
        return this.getPosition().top
    };
    L.jqplot.ElemContainer.prototype.getLeft = function () {
        return this.getPosition().left
    };
    L.jqplot.ElemContainer.prototype.getBottom = function () {
        return this._elem.css("bottom")
    };
    L.jqplot.ElemContainer.prototype.getRight = function () {
        return this._elem.css("right")
    };

    function w(ah)
    {
        L.jqplot.ElemContainer.call(this);
        this.name = ah;
        this._series = [];
        this.show = false;
        this.tickRenderer = L.jqplot.AxisTickRenderer;
        this.tickOptions = {};
        this.labelRenderer = L.jqplot.AxisLabelRenderer;
        this.labelOptions = {};
        this.label = null;
        this.showLabel = true;
        this.min = null;
        this.max = null;
        this.autoscale = false;
        this.pad = 1.2;
        this.padMax = null;
        this.padMin = null;
        this.ticks = [];
        this.numberTicks;
        this.tickInterval;
        this.renderer = L.jqplot.LinearAxisRenderer;
        this.rendererOptions = {};
        this.showTicks = true;
        this.showTickMarks = true;
        this.showMinorTicks = true;
        this.drawMajorGridlines = true;
        this.drawMinorGridlines = false;
        this.drawMajorTickMarks = true;
        this.drawMinorTickMarks = true;
        this.useSeriesColor = false;
        this.borderWidth = null;
        this.borderColor = null;
        this.scaleToHiddenSeries = false;
        this._dataBounds = {
            min: null,
            max: null
        };
        this._intervalStats = [];
        this._offsets = {
            min: null,
            max: null
        };
        this._ticks = [];
        this._label = null;
        this.syncTicks = null;
        this.tickSpacing = 75;
        this._min = null;
        this._max = null;
        this._tickInterval = null;
        this._numberTicks = null;
        this.__ticks = null;
        this._options = {}
    }

    w.prototype = new L.jqplot.ElemContainer();
    w.prototype.constructor = w;
    w.prototype.init = function () {
        if (L.isFunction(this.renderer))
        {
            this.renderer = new this.renderer()
        }
        this.tickOptions.axis = this.name;
        if (this.tickOptions.showMark == null)
        {
            this.tickOptions.showMark = this.showTicks
        }
        if (this.tickOptions.showMark == null)
        {
            this.tickOptions.showMark = this.showTickMarks
        }
        if (this.tickOptions.showLabel == null)
        {
            this.tickOptions.showLabel = this.showTicks
        }
        if (this.label == null || this.label == "")
        {
            this.showLabel = false
        }
        else
        {
            this.labelOptions.label = this.label
        }
        if (this.showLabel == false)
        {
            this.labelOptions.show = false
        }
        if (this.pad == 0)
        {
            this.pad = 1
        }
        if (this.padMax == 0)
        {
            this.padMax = 1
        }
        if (this.padMin == 0)
        {
            this.padMin = 1
        }
        if (this.padMax == null)
        {
            this.padMax = (this.pad - 1) / 2 + 1
        }
        if (this.padMin == null)
        {
            this.padMin = (this.pad - 1) / 2 + 1
        }
        this.pad = this.padMax + this.padMin - 1;
        if (this.min != null || this.max != null)
        {
            this.autoscale = false
        }
        if (this.syncTicks == null && this.name.indexOf("y") > -1)
        {
            this.syncTicks = true
        }
        else
        {
            if (this.syncTicks == null)
            {
                this.syncTicks = false
            }
        }
        this.renderer.init.call(this, this.rendererOptions)
    };
    w.prototype.draw = function (ah, ai) {
        if (this.__ticks)
        {
            this.__ticks = null
        }
        return this.renderer.draw.call(this, ah, ai)
    };
    w.prototype.set = function () {
        this.renderer.set.call(this)
    };
    w.prototype.pack = function (ai, ah) {
        if (this.show)
        {
            this.renderer.pack.call(this, ai, ah)
        }
        if (this._min == null)
        {
            this._min = this.min;
            this._max = this.max;
            this._tickInterval = this.tickInterval;
            this._numberTicks = this.numberTicks;
            this.__ticks = this._ticks
        }
    };
    w.prototype.reset = function () {
        this.renderer.reset.call(this)
    };
    w.prototype.resetScale = function (ah) {
        L.extend(true, this, {
            min: null,
            max: null,
            numberTicks: null,
            tickInterval: null,
            _ticks: [],
            ticks: []
        }, ah);
        this.resetDataBounds()
    };
    w.prototype.resetDataBounds = function () {
        var ao = this._dataBounds;
        ao.min = null;
        ao.max = null;
        var ai, ap, am;
        var aj = (this.show) ? true : false;
        for (var al = 0; al < this._series.length; al++)
        {
            ap = this._series[al];
            if (ap.show || this.scaleToHiddenSeries)
            {
                am = ap._plotData;
                if (ap._type === "line" && ap.renderer.bands.show
                    && this.name.charAt(0) !== "x")
                {
                    am = [[0, ap.renderer.bands._min],
                        [1, ap.renderer.bands._max]]
                }
                var ah = 1, an = 1;
                if (ap._type != null && ap._type == "ohlc")
                {
                    ah = 3;
                    an = 2
                }
                for (var ak = 0, ai = am.length; ak < ai; ak++)
                {
                    if (this.name == "xaxis" || this.name == "x2axis")
                    {
                        if ((am[ak][0] != null && am[ak][0] < ao.min)
                            || ao.min == null)
                        {
                            ao.min = am[ak][0]
                        }
                        if ((am[ak][0] != null && am[ak][0] > ao.max)
                            || ao.max == null)
                        {
                            ao.max = am[ak][0]
                        }
                    }
                    else
                    {
                        if ((am[ak][ah] != null && am[ak][ah] < ao.min)
                            || ao.min == null)
                        {
                            ao.min = am[ak][ah]
                        }
                        if ((am[ak][an] != null && am[ak][an] > ao.max)
                            || ao.max == null)
                        {
                            ao.max = am[ak][an]
                        }
                    }
                }
                if (aj && ap.renderer.constructor !== L.jqplot.BarRenderer)
                {
                    aj = false
                }
                else
                {
                    if (aj && this._options.hasOwnProperty("forceTickAt0")
                        && this._options.forceTickAt0 == false)
                    {
                        aj = false
                    }
                    else
                    {
                        if (aj
                            && ap.renderer.constructor === L.jqplot.BarRenderer)
                        {
                            if (ap.barDirection == "vertical"
                                && this.name != "xaxis"
                                && this.name != "x2axis")
                            {
                                if (this._options.pad != null
                                    || this._options.padMin != null)
                                {
                                    aj = false
                                }
                            }
                            else
                            {
                                if (ap.barDirection == "horizontal"
                                    && (this.name == "xaxis" || this.name == "x2axis"))
                                {
                                    if (this._options.pad != null
                                        || this._options.padMin != null)
                                    {
                                        aj = false
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (aj && this.renderer.constructor === L.jqplot.LinearAxisRenderer
            && ao.min >= 0)
        {
            this.padMin = 1;
            this.forceTickAt0 = true
        }
    };

    function q(ah)
    {
        L.jqplot.ElemContainer.call(this);
        this.show = false;
        this.location = "ne";
        this.labels = [];
        this.showLabels = true;
        this.showSwatches = true;
        this.placement = "insideGrid";
        this.xoffset = 0;
        this.yoffset = 0;
        this.border;
        this.background;
        this.textColor;
        this.fontFamily;
        this.fontSize;
        this.rowSpacing = "0.5em";
        this.renderer = L.jqplot.TableLegendRenderer;
        this.rendererOptions = {};
        this.preDraw = false;
        this.marginTop = null;
        this.marginRight = null;
        this.marginBottom = null;
        this.marginLeft = null;
        this.escapeHtml = false;
        this._series = [];
        L.extend(true, this, ah)
    }

    q.prototype = new L.jqplot.ElemContainer();
    q.prototype.constructor = q;
    q.prototype.setOptions = function (ah) {
        L.extend(true, this, ah);
        if (this.placement == "inside")
        {
            this.placement = "insideGrid"
        }
        if (this.xoffset > 0)
        {
            if (this.placement == "insideGrid")
            {
                switch (this.location)
                {
                    case "nw":
                    case "w":
                    case "sw":
                        if (this.marginLeft == null)
                        {
                            this.marginLeft = this.xoffset + "px"
                        }
                        this.marginRight = "0px";
                        break;
                    case "ne":
                    case "e":
                    case "se":
                    default:
                        if (this.marginRight == null)
                        {
                            this.marginRight = this.xoffset + "px"
                        }
                        this.marginLeft = "0px";
                        break
                }
            }
            else
            {
                if (this.placement == "outside")
                {
                    switch (this.location)
                    {
                        case "nw":
                        case "w":
                        case "sw":
                            if (this.marginRight == null)
                            {
                                this.marginRight = this.xoffset + "px"
                            }
                            this.marginLeft = "0px";
                            break;
                        case "ne":
                        case "e":
                        case "se":
                        default:
                            if (this.marginLeft == null)
                            {
                                this.marginLeft = this.xoffset + "px"
                            }
                            this.marginRight = "0px";
                            break
                    }
                }
            }
            this.xoffset = 0
        }
        if (this.yoffset > 0)
        {
            if (this.placement == "outside")
            {
                switch (this.location)
                {
                    case "sw":
                    case "s":
                    case "se":
                        if (this.marginTop == null)
                        {
                            this.marginTop = this.yoffset + "px"
                        }
                        this.marginBottom = "0px";
                        break;
                    case "ne":
                    case "n":
                    case "nw":
                    default:
                        if (this.marginBottom == null)
                        {
                            this.marginBottom = this.yoffset + "px"
                        }
                        this.marginTop = "0px";
                        break
                }
            }
            else
            {
                if (this.placement == "insideGrid")
                {
                    switch (this.location)
                    {
                        case "sw":
                        case "s":
                        case "se":
                            if (this.marginBottom == null)
                            {
                                this.marginBottom = this.yoffset + "px"
                            }
                            this.marginTop = "0px";
                            break;
                        case "ne":
                        case "n":
                        case "nw":
                        default:
                            if (this.marginTop == null)
                            {
                                this.marginTop = this.yoffset + "px"
                            }
                            this.marginBottom = "0px";
                            break
                    }
                }
            }
            this.yoffset = 0
        }
    };
    q.prototype.init = function () {
        if (L.isFunction(this.renderer))
        {
            this.renderer = new this.renderer()
        }
        this.renderer.init.call(this, this.rendererOptions)
    };
    q.prototype.draw = function (ai, aj) {
        for (var ah = 0; ah < L.jqplot.preDrawLegendHooks.length; ah++)
        {
            L.jqplot.preDrawLegendHooks[ah].call(this, ai)
        }
        return this.renderer.draw.call(this, ai, aj)
    };
    q.prototype.pack = function (ah) {
        this.renderer.pack.call(this, ah)
    };

    function y(ah)
    {
        L.jqplot.ElemContainer.call(this);
        this.text = ah;
        this.show = true;
        this.fontFamily;
        this.fontSize;
        this.textAlign;
        this.textColor;
        this.renderer = L.jqplot.DivTitleRenderer;
        this.rendererOptions = {};
        this.escapeHtml = false
    }

    y.prototype = new L.jqplot.ElemContainer();
    y.prototype.constructor = y;
    y.prototype.init = function () {
        if (L.isFunction(this.renderer))
        {
            this.renderer = new this.renderer()
        }
        this.renderer.init.call(this, this.rendererOptions)
    };
    y.prototype.draw = function (ah) {
        return this.renderer.draw.call(this, ah)
    };
    y.prototype.pack = function () {
        this.renderer.pack.call(this)
    };

    function S(ah)
    {
        ah = ah || {};
        L.jqplot.ElemContainer.call(this);
        this.show = true;
        this.xaxis = "xaxis";
        this._xaxis;
        this.yaxis = "yaxis";
        this._yaxis;
        this.gridBorderWidth = 2;
        this.renderer = L.jqplot.LineRenderer;
        this.rendererOptions = {};
        this.data = [];
        this.gridData = [];
        this.label = "";
        this.showLabel = true;
        this.color;
        this.negativeColor;
        this.lineWidth = 2.5;
        this.lineJoin = "round";
        this.lineCap = "round";
        this.linePattern = "solid";
        this.shadow = true;
        this.shadowAngle = 45;
        this.shadowOffset = 1.25;
        this.shadowDepth = 3;
        this.shadowAlpha = "0.1";
        this.breakOnNull = false;
        this.markerRenderer = L.jqplot.MarkerRenderer;
        this.markerOptions = {};
        this.showLine = true;
        this.showMarker = true;
        this.index;
        this.fill = false;
        this.fillColor;
        this.fillAlpha;
        this.fillAndStroke = false;
        this.disableStack = false;
        this._stack = false;
        this.neighborThreshold = 4;
        this.fillToZero = false;
        this.fillToValue = 0;
        this.fillAxis = "y";
        this.useNegativeColors = true;
        this._stackData = [];
        this._plotData = [];
        this._plotValues = {
            x: [],
            y: []
        };
        this._intervals = {
            x: {},
            y: {}
        };
        this._prevPlotData = [];
        this._prevGridData = [];
        this._stackAxis = "y";
        this._primaryAxis = "_xaxis";
        this.canvas = new L.jqplot.GenericCanvas();
        this.shadowCanvas = new L.jqplot.GenericCanvas();
        this.plugins = {};
        this._sumy = 0;
        this._sumx = 0;
        this._type = ""
    }

    S.prototype = new L.jqplot.ElemContainer();
    S.prototype.constructor = S;
    S.prototype.init = function (ak, ao, am) {
        this.index = ak;
        this.gridBorderWidth = ao;
        var an = this.data;
        var aj = [], al, ah;
        for (al = 0, ah = an.length; al < ah; al++)
        {
            if (!this.breakOnNull)
            {
                if (an[al] == null || an[al][0] == null)
                {
                    continue
                }
                else
                {
                    if (an[al][1] == null)
                    {
                        an[al][1] = "-";
                        aj.push(an[al])
                    }
                    else
                    {
                        aj.push(an[al])
                    }
                }
            }
            else
            {
                aj.push(an[al])
            }
        }
        this.data = aj;
        if (!this.color)
        {
            this.color = am.colorGenerator.get(this.index)
        }
        if (!this.negativeColor)
        {
            this.negativeColor = am.negativeColorGenerator.get(this.index)
        }
        if (!this.fillColor)
        {
            this.fillColor = this.color
        }
        if (this.fillAlpha)
        {
            var ai = L.jqplot.normalize2rgb(this.fillColor);
            var ai = L.jqplot.getColorComponents(ai);
            this.fillColor = "rgba(" + ai[0] + "," + ai[1] + "," + ai[2] + ","
                + this.fillAlpha + ")"
        }
        if (L.isFunction(this.renderer))
        {
            this.renderer = new this.renderer()
        }
        this.renderer.init.call(this, this.rendererOptions, am);
        this.markerRenderer = new this.markerRenderer();
        if (!this.markerOptions.color)
        {
            this.markerOptions.color = this.color
        }
        if (this.markerOptions.show == null)
        {
            this.markerOptions.show = this.showMarker
        }
        this.showMarker = this.markerOptions.show;
        this.markerRenderer.init(this.markerOptions)
    };
    S.prototype.draw = function (an, ak, am) {
        var ai = (ak == u) ? {} : ak;
        an = (an == u) ? this.canvas._ctx : an;
        var ah, al, aj;
        for (ah = 0; ah < L.jqplot.preDrawSeriesHooks.length; ah++)
        {
            L.jqplot.preDrawSeriesHooks[ah].call(this, an, ai)
        }
        if (this.show)
        {
            this.renderer.setGridData.call(this, am);
            if (!ai.preventJqPlotSeriesDrawTrigger)
            {
                L(an.canvas).trigger("jqplotSeriesDraw",
                    [this.data, this.gridData])
            }
            al = [];
            if (ai.data)
            {
                al = ai.data
            }
            else
            {
                if (!this._stack)
                {
                    al = this.data
                }
                else
                {
                    al = this._plotData
                }
            }
            aj = ai.gridData || this.renderer.makeGridData.call(this, al, am);
            if (this._type === "line" && this.renderer.smooth
                && this.renderer._smoothedData.length)
            {
                aj = this.renderer._smoothedData
            }
            this.renderer.draw.call(this, an, aj, ai, am)
        }
        for (ah = 0; ah < L.jqplot.postDrawSeriesHooks.length; ah++)
        {
            L.jqplot.postDrawSeriesHooks[ah].call(this, an, ai, am)
        }
        an = ak = am = ah = al = aj = null
    };
    S.prototype.drawShadow = function (an, ak, am) {
        var ai = (ak == u) ? {} : ak;
        an = (an == u) ? this.shadowCanvas._ctx : an;
        var ah, al, aj;
        for (ah = 0; ah < L.jqplot.preDrawSeriesShadowHooks.length; ah++)
        {
            L.jqplot.preDrawSeriesShadowHooks[ah].call(this, an, ai)
        }
        if (this.shadow)
        {
            this.renderer.setGridData.call(this, am);
            al = [];
            if (ai.data)
            {
                al = ai.data
            }
            else
            {
                if (!this._stack)
                {
                    al = this.data
                }
                else
                {
                    al = this._plotData
                }
            }
            aj = ai.gridData || this.renderer.makeGridData.call(this, al, am);
            this.renderer.drawShadow.call(this, an, aj, ai, am)
        }
        for (ah = 0; ah < L.jqplot.postDrawSeriesShadowHooks.length; ah++)
        {
            L.jqplot.postDrawSeriesShadowHooks[ah].call(this, an, ai)
        }
        an = ak = am = ah = al = aj = null
    };
    S.prototype.toggleDisplay = function (ai, ak) {
        var ah, aj;
        if (ai.data.series)
        {
            ah = ai.data.series
        }
        else
        {
            ah = this
        }
        if (ai.data.speed)
        {
            aj = ai.data.speed
        }
        if (aj)
        {
            if (ah.canvas._elem.is(":hidden") || !ah.show)
            {
                ah.show = true;
                ah.canvas._elem.removeClass("jqplot-series-hidden");
                if (ah.shadowCanvas._elem)
                {
                    ah.shadowCanvas._elem.fadeIn(aj)
                }
                ah.canvas._elem.fadeIn(aj, ak);
                ah.canvas._elem.nextAll(
                    ".jqplot-point-label.jqplot-series-" + ah.index)
                    .fadeIn(aj)
            }
            else
            {
                ah.show = false;
                ah.canvas._elem.addClass("jqplot-series-hidden");
                if (ah.shadowCanvas._elem)
                {
                    ah.shadowCanvas._elem.fadeOut(aj)
                }
                ah.canvas._elem.fadeOut(aj, ak);
                ah.canvas._elem.nextAll(
                    ".jqplot-point-label.jqplot-series-" + ah.index)
                    .fadeOut(aj)
            }
        }
        else
        {
            if (ah.canvas._elem.is(":hidden") || !ah.show)
            {
                ah.show = true;
                ah.canvas._elem.removeClass("jqplot-series-hidden");
                if (ah.shadowCanvas._elem)
                {
                    ah.shadowCanvas._elem.show()
                }
                ah.canvas._elem.show(0, ak);
                ah.canvas._elem.nextAll(
                    ".jqplot-point-label.jqplot-series-" + ah.index).show()
            }
            else
            {
                ah.show = false;
                ah.canvas._elem.addClass("jqplot-series-hidden");
                if (ah.shadowCanvas._elem)
                {
                    ah.shadowCanvas._elem.hide()
                }
                ah.canvas._elem.hide(0, ak);
                ah.canvas._elem.nextAll(
                    ".jqplot-point-label.jqplot-series-" + ah.index).hide()
            }
        }
    };

    function M()
    {
        L.jqplot.ElemContainer.call(this);
        this.drawGridlines = true;
        this.gridLineColor = "#cccccc";
        this.gridLineWidth = 1;
        this.background = "#fffdf6";
        this.borderColor = "#999999";
        this.borderWidth = 2;
        this.drawBorder = true;
        this.shadow = true;
        this.shadowAngle = 45;
        this.shadowOffset = 1.5;
        this.shadowWidth = 3;
        this.shadowDepth = 3;
        this.shadowColor = null;
        this.shadowAlpha = "0.07";
        this._left;
        this._top;
        this._right;
        this._bottom;
        this._width;
        this._height;
        this._axes = [];
        this.renderer = L.jqplot.CanvasGridRenderer;
        this.rendererOptions = {};
        this._offsets = {
            top: null,
            bottom: null,
            left: null,
            right: null
        }
    }

    M.prototype = new L.jqplot.ElemContainer();
    M.prototype.constructor = M;
    M.prototype.init = function () {
        if (L.isFunction(this.renderer))
        {
            this.renderer = new this.renderer()
        }
        this.renderer.init.call(this, this.rendererOptions)
    };
    M.prototype.createElement = function (ah, ai) {
        this._offsets = ah;
        return this.renderer.createElement.call(this, ai)
    };
    M.prototype.draw = function () {
        this.renderer.draw.call(this)
    };
    L.jqplot.GenericCanvas = function () {
        L.jqplot.ElemContainer.call(this);
        this._ctx
    };
    L.jqplot.GenericCanvas.prototype = new L.jqplot.ElemContainer();
    L.jqplot.GenericCanvas.prototype.constructor = L.jqplot.GenericCanvas;
    L.jqplot.GenericCanvas.prototype.createElement = function (al, aj, ai, am) {
        this._offsets = al;
        var ah = "jqplot";
        if (aj != u)
        {
            ah = aj
        }
        var ak;
        ak = am.canvasManager.getCanvas();
        if (ai != null)
        {
            this._plotDimensions = ai
        }
        ak.width = this._plotDimensions.width - this._offsets.left
            - this._offsets.right;
        ak.height = this._plotDimensions.height - this._offsets.top
            - this._offsets.bottom;
        this._elem = L(ak);
        this._elem.css({
            position: "absolute",
            left: this._offsets.left,
            top: this._offsets.top
        });
        this._elem.addClass(ah);
        ak = am.canvasManager.initCanvas(ak);
        ak = null;
        return this._elem
    };
    L.jqplot.GenericCanvas.prototype.setContext = function () {
        this._ctx = this._elem.get(0).getContext("2d");
        return this._ctx
    };
    L.jqplot.GenericCanvas.prototype.resetCanvas = function () {
        if (this._elem)
        {
            if (L.jqplot.use_excanvas
                && window.G_vmlCanvasManager.uninitElement !== u)
            {
                window.G_vmlCanvasManager.uninitElement(this._elem.get(0))
            }
            this._elem.emptyForce()
        }
        this._ctx = null
    };
    L.jqplot.HooksManager = function () {
        this.hooks = [];
        this.args = []
    };
    L.jqplot.HooksManager.prototype.addOnce = function (ak, ai) {
        ai = ai || [];
        var al = false;
        for (var aj = 0, ah = this.hooks.length; aj < ah; aj++)
        {
            if (this.hooks[aj] == ak)
            {
                al = true
            }
        }
        if (!al)
        {
            this.hooks.push(ak);
            this.args.push(ai)
        }
    };
    L.jqplot.HooksManager.prototype.add = function (ai, ah) {
        ah = ah || [];
        this.hooks.push(ai);
        this.args.push(ah)
    };
    L.jqplot.EventListenerManager = function () {
        this.hooks = []
    };
    L.jqplot.EventListenerManager.prototype.addOnce = function (al, ak) {
        var am = false, aj, ai;
        for (var ai = 0, ah = this.hooks.length; ai < ah; ai++)
        {
            aj = this.hooks[ai];
            if (aj[0] == al && aj[1] == ak)
            {
                am = true
            }
        }
        if (!am)
        {
            this.hooks.push([al, ak])
        }
    };
    L.jqplot.EventListenerManager.prototype.add = function (ai, ah) {
        this.hooks.push([ai, ah])
    };
    var U = ["yMidAxis", "xaxis", "yaxis", "x2axis", "y2axis", "y3axis",
        "y4axis", "y5axis", "y6axis", "y7axis", "y8axis", "y9axis"];

    function R()
    {
        this.animate = false;
        this.animateReplot = false;
        this.axes = {
            xaxis: new w("xaxis"),
            yaxis: new w("yaxis"),
            x2axis: new w("x2axis"),
            y2axis: new w("y2axis"),
            y3axis: new w("y3axis"),
            y4axis: new w("y4axis"),
            y5axis: new w("y5axis"),
            y6axis: new w("y6axis"),
            y7axis: new w("y7axis"),
            y8axis: new w("y8axis"),
            y9axis: new w("y9axis"),
            yMidAxis: new w("yMidAxis")
        };
        this.baseCanvas = new L.jqplot.GenericCanvas();
        this.captureRightClick = false;
        this.data = [];
        this.dataRenderer;
        this.dataRendererOptions;
        this.defaults = {
            axesDefaults: {},
            axes: {
                xaxis: {},
                yaxis: {},
                x2axis: {},
                y2axis: {},
                y3axis: {},
                y4axis: {},
                y5axis: {},
                y6axis: {},
                y7axis: {},
                y8axis: {},
                y9axis: {},
                yMidAxis: {}
            },
            seriesDefaults: {},
            series: []
        };
        this.defaultAxisStart = 1;
        this.drawIfHidden = false;
        this.eventCanvas = new L.jqplot.GenericCanvas();
        this.fillBetween = {
            series1: null,
            series2: null,
            color: null,
            baseSeries: 0,
            fill: true
        };
        this.fontFamily;
        this.fontSize;
        this.grid = new M();
        this.legend = new q();
        this.noDataIndicator = {
            show: false,
            indicator: "Loading Data...",
            axes: {
                xaxis: {
                    min: 0,
                    max: 10,
                    tickInterval: 2,
                    show: true
                },
                yaxis: {
                    min: 0,
                    max: 12,
                    tickInterval: 3,
                    show: true
                }
            }
        };
        this.negativeSeriesColors = L.jqplot.config.defaultNegativeColors;
        this.options = {};
        this.previousSeriesStack = [];
        this.plugins = {};
        this.series = [];
        this.seriesStack = [];
        this.seriesColors = L.jqplot.config.defaultColors;
        this.sortData = true;
        this.stackSeries = false;
        this.syncXTicks = true;
        this.syncYTicks = true;
        this.target = null;
        this.targetId = null;
        this.textColor;
        this.title = new y();
        this._drawCount = 0;
        this._sumy = 0;
        this._sumx = 0;
        this._stackData = [];
        this._plotData = [];
        this._width = null;
        this._height = null;
        this._plotDimensions = {
            height: null,
            width: null
        };
        this._gridPadding = {
            top: null,
            right: null,
            bottom: null,
            left: null
        };
        this._defaultGridPadding = {
            top: 10,
            right: 10,
            bottom: 23,
            left: 10
        };
        this._addDomReference = L.jqplot.config.addDomReference;
        this.preInitHooks = new L.jqplot.HooksManager();
        this.postInitHooks = new L.jqplot.HooksManager();
        this.preParseOptionsHooks = new L.jqplot.HooksManager();
        this.postParseOptionsHooks = new L.jqplot.HooksManager();
        this.preDrawHooks = new L.jqplot.HooksManager();
        this.postDrawHooks = new L.jqplot.HooksManager();
        this.preDrawSeriesHooks = new L.jqplot.HooksManager();
        this.postDrawSeriesHooks = new L.jqplot.HooksManager();
        this.preDrawLegendHooks = new L.jqplot.HooksManager();
        this.addLegendRowHooks = new L.jqplot.HooksManager();
        this.preSeriesInitHooks = new L.jqplot.HooksManager();
        this.postSeriesInitHooks = new L.jqplot.HooksManager();
        this.preParseSeriesOptionsHooks = new L.jqplot.HooksManager();
        this.postParseSeriesOptionsHooks = new L.jqplot.HooksManager();
        this.eventListenerHooks = new L.jqplot.EventListenerManager();
        this.preDrawSeriesShadowHooks = new L.jqplot.HooksManager();
        this.postDrawSeriesShadowHooks = new L.jqplot.HooksManager();
        this.colorGenerator = new L.jqplot.ColorGenerator();
        this.negativeColorGenerator = new L.jqplot.ColorGenerator();
        this.canvasManager = new L.jqplot.CanvasManager();
        this.themeEngine = new L.jqplot.ThemeEngine();
        var aj = 0;
        this.init = function (av, ar, ay) {
            ay = ay || {};
            for (var at = 0; at < L.jqplot.preInitHooks.length; at++)
            {
                L.jqplot.preInitHooks[at].call(this, av, ar, ay)
            }
            for (var at = 0; at < this.preInitHooks.hooks.length; at++)
            {
                this.preInitHooks.hooks[at].call(this, av, ar, ay)
            }
            this.targetId = "#" + av;
            this.target = L("#" + av);
            if (this._addDomReference)
            {
                this.target.data("jqplot", this)
            }
            this.target.removeClass("jqplot-error");
            if (!this.target.get(0))
            {
                throw new Error("No plot target specified")
            }
            if (this.target.css("position") == "static")
            {
                this.target.css("position", "relative")
            }
            if (!this.target.hasClass("jqplot-target"))
            {
                this.target.addClass("jqplot-target")
            }
            if (!this.target.height())
            {
                var au;
                if (ay && ay.height)
                {
                    au = parseInt(ay.height, 10)
                }
                else
                {
                    if (this.target.attr("data-height"))
                    {
                        au = parseInt(this.target.attr("data-height"), 10)
                    }
                    else
                    {
                        au = parseInt(L.jqplot.config.defaultHeight, 10)
                    }
                }
                this._height = au;
                this.target.css("height", au + "px")
            }
            else
            {
                this._height = au = this.target.height()
            }
            if (!this.target.width())
            {
                var aw;
                if (ay && ay.width)
                {
                    aw = parseInt(ay.width, 10)
                }
                else
                {
                    if (this.target.attr("data-width"))
                    {
                        aw = parseInt(this.target.attr("data-width"), 10)
                    }
                    else
                    {
                        aw = parseInt(L.jqplot.config.defaultWidth, 10)
                    }
                }
                this._width = aw;
                this.target.css("width", aw + "px")
            }
            else
            {
                this._width = aw = this.target.width()
            }
            for (var at = 0, ap = U.length; at < ap; at++)
            {
                this.axes[U[at]] = new w(U[at])
            }
            this._plotDimensions.height = this._height;
            this._plotDimensions.width = this._width;
            this.grid._plotDimensions = this._plotDimensions;
            this.title._plotDimensions = this._plotDimensions;
            this.baseCanvas._plotDimensions = this._plotDimensions;
            this.eventCanvas._plotDimensions = this._plotDimensions;
            this.legend._plotDimensions = this._plotDimensions;
            if (this._height <= 0 || this._width <= 0 || !this._height
                || !this._width)
            {
                throw new Error("Canvas dimension not set")
            }
            if (ay.dataRenderer && L.isFunction(ay.dataRenderer))
            {
                if (ay.dataRendererOptions)
                {
                    this.dataRendererOptions = ay.dataRendererOptions
                }
                this.dataRenderer = ay.dataRenderer;
                ar = this.dataRenderer(ar, this, this.dataRendererOptions)
            }
            if (ay.noDataIndicator && L.isPlainObject(ay.noDataIndicator))
            {
                L.extend(true, this.noDataIndicator, ay.noDataIndicator)
            }
            if (ar == null || L.isArray(ar) == false || ar.length == 0
                || L.isArray(ar[0]) == false || ar[0].length == 0)
            {
                if (this.noDataIndicator.show == false)
                {
                    throw new Error("No data specified")
                }
                else
                {
                    for (var al in this.noDataIndicator.axes)
                    {
                        for (var an in this.noDataIndicator.axes[al])
                        {
                            this.axes[al][an] = this.noDataIndicator.axes[al][an]
                        }
                    }
                    this.postDrawHooks
                        .add(function () {
                            var aD = this.eventCanvas.getHeight();
                            var aA = this.eventCanvas.getWidth();
                            var az = L('<div class="jqplot-noData-container" style="position:absolute;"></div>');
                            this.target.append(az);
                            az.height(aD);
                            az.width(aA);
                            az.css("top", this.eventCanvas._offsets.top);
                            az.css("left", this.eventCanvas._offsets.left);
                            var aC = L('<div class="jqplot-noData-contents" style="text-align:center; position:relative; margin-left:auto; margin-right:auto;"></div>');
                            az.append(aC);
                            aC.html(this.noDataIndicator.indicator);
                            var aB = aC.height();
                            var ax = aC.width();
                            aC.height(aB);
                            aC.width(ax);
                            aC.css("top", (aD - aB) / 2 + "px")
                        })
                }
            }
            this.data = L.extend(true, [], ar);
            this.parseOptions(ay);
            if (this.textColor)
            {
                this.target.css("color", this.textColor)
            }
            if (this.fontFamily)
            {
                this.target.css("font-family", this.fontFamily)
            }
            if (this.fontSize)
            {
                this.target.css("font-size", this.fontSize)
            }
            this.title.init();
            this.legend.init();
            this._sumy = 0;
            this._sumx = 0;
            this.computePlotData();
            for (var at = 0; at < this.series.length; at++)
            {
                this.seriesStack.push(at);
                this.previousSeriesStack.push(at);
                this.series[at].shadowCanvas._plotDimensions = this._plotDimensions;
                this.series[at].canvas._plotDimensions = this._plotDimensions;
                for (var aq = 0; aq < L.jqplot.preSeriesInitHooks.length; aq++)
                {
                    L.jqplot.preSeriesInitHooks[aq].call(this.series[at], av,
                        this.data, this.options.seriesDefaults,
                        this.options.series[at], this)
                }
                for (var aq = 0; aq < this.preSeriesInitHooks.hooks.length; aq++)
                {
                    this.preSeriesInitHooks.hooks[aq].call(this.series[at], av,
                        this.data, this.options.seriesDefaults,
                        this.options.series[at], this)
                }
                this.series[at]._plotDimensions = this._plotDimensions;
                this.series[at].init(at, this.grid.borderWidth, this);
                for (var aq = 0; aq < L.jqplot.postSeriesInitHooks.length; aq++)
                {
                    L.jqplot.postSeriesInitHooks[aq].call(this.series[at], av,
                        this.data, this.options.seriesDefaults,
                        this.options.series[at], this)
                }
                for (var aq = 0; aq < this.postSeriesInitHooks.hooks.length; aq++)
                {
                    this.postSeriesInitHooks.hooks[aq].call(this.series[at],
                        av, this.data, this.options.seriesDefaults,
                        this.options.series[at], this)
                }
                this._sumy += this.series[at]._sumy;
                this._sumx += this.series[at]._sumx
            }
            var am, ao;
            for (var at = 0, ap = U.length; at < ap; at++)
            {
                am = U[at];
                ao = this.axes[am];
                ao._plotDimensions = this._plotDimensions;
                ao.init();
                if (this.axes[am].borderColor == null)
                {
                    if (am.charAt(0) !== "x" && ao.useSeriesColor === true
                        && ao.show)
                    {
                        ao.borderColor = ao._series[0].color
                    }
                    else
                    {
                        ao.borderColor = this.grid.borderColor
                    }
                }
            }
            if (this.sortData)
            {
                ah(this.series)
            }
            this.grid.init();
            this.grid._axes = this.axes;
            this.legend._series = this.series;
            for (var at = 0; at < L.jqplot.postInitHooks.length; at++)
            {
                L.jqplot.postInitHooks[at].call(this, av, this.data, ay)
            }
            for (var at = 0; at < this.postInitHooks.hooks.length; at++)
            {
                this.postInitHooks.hooks[at].call(this, av, this.data, ay)
            }
        };
        this.resetAxesScale = function (aq, am) {
            var ao = am || {};
            var ap = aq || this.axes;
            if (ap === true)
            {
                ap = this.axes
            }
            if (L.isArray(ap))
            {
                for (var an = 0; an < ap.length; an++)
                {
                    this.axes[ap[an]].resetScale(ao[ap[an]])
                }
            }
            else
            {
                if (typeof (ap) === "object")
                {
                    for (var al in ap)
                    {
                        this.axes[al].resetScale(ao[al])
                    }
                }
            }
        };
        this.reInitialize = function (au, al) {
            var ay = L.extend(true, {}, this.options, al);
            var aw = this.targetId.substr(1);
            var ar = (au == null) ? this.data : au;
            for (var av = 0; av < L.jqplot.preInitHooks.length; av++)
            {
                L.jqplot.preInitHooks[av].call(this, aw, ar, ay)
            }
            for (var av = 0; av < this.preInitHooks.hooks.length; av++)
            {
                this.preInitHooks.hooks[av].call(this, aw, ar, ay)
            }
            this._height = this.target.height();
            this._width = this.target.width();
            if (this._height <= 0 || this._width <= 0 || !this._height
                || !this._width)
            {
                throw new Error("Target dimension not set")
            }
            this._plotDimensions.height = this._height;
            this._plotDimensions.width = this._width;
            this.grid._plotDimensions = this._plotDimensions;
            this.title._plotDimensions = this._plotDimensions;
            this.baseCanvas._plotDimensions = this._plotDimensions;
            this.eventCanvas._plotDimensions = this._plotDimensions;
            this.legend._plotDimensions = this._plotDimensions;
            var am, ax, at, ao;
            for (var av = 0, aq = U.length; av < aq; av++)
            {
                am = U[av];
                ao = this.axes[am];
                ax = ao._ticks;
                for (var at = 0, ap = ax.length; at < ap; at++)
                {
                    var an = ax[at]._elem;
                    if (an)
                    {
                        if (L.jqplot.use_excanvas
                            && window.G_vmlCanvasManager.uninitElement !== u)
                        {
                            window.G_vmlCanvasManager.uninitElement(an.get(0))
                        }
                        an.emptyForce();
                        an = null;
                        ax._elem = null
                    }
                }
                ax = null;
                delete ao.ticks;
                delete ao._ticks;
                this.axes[am] = new w(am);
                this.axes[am]._plotWidth = this._width;
                this.axes[am]._plotHeight = this._height
            }
            if (au)
            {
                if (ay.dataRenderer && L.isFunction(ay.dataRenderer))
                {
                    if (ay.dataRendererOptions)
                    {
                        this.dataRendererOptions = ay.dataRendererOptions
                    }
                    this.dataRenderer = ay.dataRenderer;
                    au = this.dataRenderer(au, this, this.dataRendererOptions)
                }
                this.data = L.extend(true, [], au)
            }
            if (al)
            {
                this.parseOptions(ay)
            }
            this.title._plotWidth = this._width;
            if (this.textColor)
            {
                this.target.css("color", this.textColor)
            }
            if (this.fontFamily)
            {
                this.target.css("font-family", this.fontFamily)
            }
            if (this.fontSize)
            {
                this.target.css("font-size", this.fontSize)
            }
            this.title.init();
            this.legend.init();
            this._sumy = 0;
            this._sumx = 0;
            this.seriesStack = [];
            this.previousSeriesStack = [];
            this.computePlotData();
            for (var av = 0, aq = this.series.length; av < aq; av++)
            {
                this.seriesStack.push(av);
                this.previousSeriesStack.push(av);
                this.series[av].shadowCanvas._plotDimensions = this._plotDimensions;
                this.series[av].canvas._plotDimensions = this._plotDimensions;
                for (var at = 0; at < L.jqplot.preSeriesInitHooks.length; at++)
                {
                    L.jqplot.preSeriesInitHooks[at].call(this.series[av], aw,
                        this.data, this.options.seriesDefaults,
                        this.options.series[av], this)
                }
                for (var at = 0; at < this.preSeriesInitHooks.hooks.length; at++)
                {
                    this.preSeriesInitHooks.hooks[at].call(this.series[av], aw,
                        this.data, this.options.seriesDefaults,
                        this.options.series[av], this)
                }
                this.series[av]._plotDimensions = this._plotDimensions;
                this.series[av].init(av, this.grid.borderWidth, this);
                for (var at = 0; at < L.jqplot.postSeriesInitHooks.length; at++)
                {
                    L.jqplot.postSeriesInitHooks[at].call(this.series[av], aw,
                        this.data, this.options.seriesDefaults,
                        this.options.series[av], this)
                }
                for (var at = 0; at < this.postSeriesInitHooks.hooks.length; at++)
                {
                    this.postSeriesInitHooks.hooks[at].call(this.series[av],
                        aw, this.data, this.options.seriesDefaults,
                        this.options.series[av], this)
                }
                this._sumy += this.series[av]._sumy;
                this._sumx += this.series[av]._sumx
            }
            for (var av = 0, aq = U.length; av < aq; av++)
            {
                am = U[av];
                ao = this.axes[am];
                ao._plotDimensions = this._plotDimensions;
                ao.init();
                if (ao.borderColor == null)
                {
                    if (am.charAt(0) !== "x" && ao.useSeriesColor === true
                        && ao.show)
                    {
                        ao.borderColor = ao._series[0].color
                    }
                    else
                    {
                        ao.borderColor = this.grid.borderColor
                    }
                }
            }
            if (this.sortData)
            {
                ah(this.series)
            }
            this.grid.init();
            this.grid._axes = this.axes;
            this.legend._series = this.series;
            for (var av = 0, aq = L.jqplot.postInitHooks.length; av < aq; av++)
            {
                L.jqplot.postInitHooks[av].call(this, aw, this.data, ay)
            }
            for (var av = 0, aq = this.postInitHooks.hooks.length; av < aq; av++)
            {
                this.postInitHooks.hooks[av].call(this, aw, this.data, ay)
            }
        };
        this.quickInit = function () {
            this._height = this.target.height();
            this._width = this.target.width();
            if (this._height <= 0 || this._width <= 0 || !this._height
                || !this._width)
            {
                throw new Error("Target dimension not set")
            }
            this._plotDimensions.height = this._height;
            this._plotDimensions.width = this._width;
            this.grid._plotDimensions = this._plotDimensions;
            this.title._plotDimensions = this._plotDimensions;
            this.baseCanvas._plotDimensions = this._plotDimensions;
            this.eventCanvas._plotDimensions = this._plotDimensions;
            this.legend._plotDimensions = this._plotDimensions;
            for (var aq in this.axes)
            {
                this.axes[aq]._plotWidth = this._width;
                this.axes[aq]._plotHeight = this._height
            }
            this.title._plotWidth = this._width;
            if (this.textColor)
            {
                this.target.css("color", this.textColor)
            }
            if (this.fontFamily)
            {
                this.target.css("font-family", this.fontFamily)
            }
            if (this.fontSize)
            {
                this.target.css("font-size", this.fontSize)
            }
            this._sumy = 0;
            this._sumx = 0;
            this.computePlotData();
            for (var ao = 0; ao < this.series.length; ao++)
            {
                if (this.series[ao]._type === "line"
                    && this.series[ao].renderer.bands.show)
                {
                    this.series[ao].renderer.initBands.call(this.series[ao],
                        this.series[ao].renderer.options, this)
                }
                this.series[ao]._plotDimensions = this._plotDimensions;
                this.series[ao].canvas._plotDimensions = this._plotDimensions;
                this._sumy += this.series[ao]._sumy;
                this._sumx += this.series[ao]._sumx
            }
            var am;
            for (var al = 0; al < 12; al++)
            {
                am = U[al];
                var an = this.axes[am]._ticks;
                for (var ao = 0; ao < an.length; ao++)
                {
                    var ap = an[ao]._elem;
                    if (ap)
                    {
                        if (L.jqplot.use_excanvas
                            && window.G_vmlCanvasManager.uninitElement !== u)
                        {
                            window.G_vmlCanvasManager.uninitElement(ap.get(0))
                        }
                        ap.emptyForce();
                        ap = null;
                        an._elem = null
                    }
                }
                an = null;
                this.axes[am]._plotDimensions = this._plotDimensions;
                this.axes[am]._ticks = []
            }
            if (this.sortData)
            {
                ah(this.series)
            }
            this.grid._axes = this.axes;
            this.legend._series = this.series
        };

        function ah(ap)
        {
            var au, av, aw, al, at;
            for (var aq = 0; aq < ap.length; aq++)
            {
                var am;
                var ar = [ap[aq].data, ap[aq]._stackData, ap[aq]._plotData,
                    ap[aq]._prevPlotData];
                for (var an = 0; an < 4; an++)
                {
                    am = true;
                    au = ar[an];
                    if (ap[aq]._stackAxis == "x")
                    {
                        for (var ao = 0; ao < au.length; ao++)
                        {
                            if (typeof (au[ao][1]) != "number")
                            {
                                am = false;
                                break
                            }
                        }
                        if (am)
                        {
                            au.sort(function (ay, ax) {
                                return ay[1] - ax[1]
                            })
                        }
                    }
                    else
                    {
                        for (var ao = 0; ao < au.length; ao++)
                        {
                            if (typeof (au[ao][0]) != "number")
                            {
                                am = false;
                                break
                            }
                        }
                        if (am)
                        {
                            au.sort(function (ay, ax) {
                                return ay[0] - ax[0]
                            })
                        }
                    }
                }
            }
        }

        this.computePlotData = function () {
            this._plotData = [];
            this._stackData = [];
            var at, au, ao;
            for (au = 0, ao = this.series.length; au < ao; au++)
            {
                at = this.series[au];
                this._plotData.push([]);
                this._stackData.push([]);
                var am = at.data;
                this._plotData[au] = L.extend(true, [], am);
                this._stackData[au] = L.extend(true, [], am);
                at._plotData = this._plotData[au];
                at._stackData = this._stackData[au];
                var ax = {
                    x: [],
                    y: []
                };
                if (this.stackSeries && !at.disableStack)
                {
                    at._stack = true;
                    var av = (at._stackAxis === "x") ? 0 : 1;
                    for (var ap = 0, al = am.length; ap < al; ap++)
                    {
                        var aw = am[ap][av];
                        if (aw == null)
                        {
                            aw = 0
                        }
                        this._plotData[au][ap][av] = aw;
                        this._stackData[au][ap][av] = aw;
                        if (au > 0)
                        {
                            for (var aq = au; aq--;)
                            {
                                var an = this._plotData[aq][ap][av];
                                if (aw * an >= 0)
                                {
                                    this._plotData[au][ap][av] += an;
                                    this._stackData[au][ap][av] += an;
                                    break
                                }
                            }
                        }
                    }
                }
                else
                {
                    for (var ar = 0; ar < at.data.length; ar++)
                    {
                        ax.x.push(at.data[ar][0]);
                        ax.y.push(at.data[ar][1])
                    }
                    this._stackData.push(at.data);
                    this.series[au]._stackData = at.data;
                    this._plotData.push(at.data);
                    at._plotData = at.data;
                    at._plotValues = ax
                }
                if (au > 0)
                {
                    at._prevPlotData = this.series[au - 1]._plotData
                }
                at._sumy = 0;
                at._sumx = 0;
                for (ar = at.data.length - 1; ar > -1; ar--)
                {
                    at._sumy += at.data[ar][1];
                    at._sumx += at.data[ar][0]
                }
            }
        };
        this.populatePlotData = function (au, av) {
            this._plotData = [];
            this._stackData = [];
            au._stackData = [];
            au._plotData = [];
            var ay = {
                x: [],
                y: []
            };
            if (this.stackSeries && !au.disableStack)
            {
                au._stack = true;
                var ax = (au._stackAxis === "x") ? 0 : 1;
                var az = L.extend(true, [], au.data);
                var aA = L.extend(true, [], au.data);
                var an, am, ao, aw, al;
                for (var ar = 0; ar < av; ar++)
                {
                    var ap = this.series[ar].data;
                    for (var aq = 0; aq < ap.length; aq++)
                    {
                        ao = ap[aq];
                        an = (ao[0] != null) ? ao[0] : 0;
                        am = (ao[1] != null) ? ao[1] : 0;
                        az[aq][0] += an;
                        az[aq][1] += am;
                        aw = (ax) ? am : an;
                        if (au.data[aq][ax] * aw >= 0)
                        {
                            aA[aq][ax] += aw
                        }
                    }
                }
                for (var at = 0; at < aA.length; at++)
                {
                    ay.x.push(aA[at][0]);
                    ay.y.push(aA[at][1])
                }
                this._plotData.push(aA);
                this._stackData.push(az);
                au._stackData = az;
                au._plotData = aA;
                au._plotValues = ay
            }
            else
            {
                for (var at = 0; at < au.data.length; at++)
                {
                    ay.x.push(au.data[at][0]);
                    ay.y.push(au.data[at][1])
                }
                this._stackData.push(au.data);
                this.series[av]._stackData = au.data;
                this._plotData.push(au.data);
                au._plotData = au.data;
                au._plotValues = ay
            }
            if (av > 0)
            {
                au._prevPlotData = this.series[av - 1]._plotData
            }
            au._sumy = 0;
            au._sumx = 0;
            for (at = au.data.length - 1; at > -1; at--)
            {
                au._sumy += au.data[at][1];
                au._sumx += au.data[at][0]
            }
        };
        this.getNextSeriesColor = (function (am) {
            var al = 0;
            var an = am.seriesColors;
            return function () {
                if (al < an.length)
                {
                    return an[al++]
                }
                else
                {
                    al = 0;
                    return an[al++]
                }
            }
        })(this);
        this.parseOptions = function (ay) {
            for (var at = 0; at < this.preParseOptionsHooks.hooks.length; at++)
            {
                this.preParseOptionsHooks.hooks[at].call(this, ay)
            }
            for (var at = 0; at < L.jqplot.preParseOptionsHooks.length; at++)
            {
                L.jqplot.preParseOptionsHooks[at].call(this, ay)
            }
            this.options = L.extend(true, {}, this.defaults, ay);
            var am = this.options;
            this.animate = am.animate;
            this.animateReplot = am.animateReplot;
            this.stackSeries = am.stackSeries;
            if (L.isPlainObject(am.fillBetween))
            {
                var ax = ["series1", "series2", "color", "baseSeries", "fill"], au;
                for (var at = 0, aq = ax.length; at < aq; at++)
                {
                    au = ax[at];
                    if (am.fillBetween[au] != null)
                    {
                        this.fillBetween[au] = am.fillBetween[au]
                    }
                }
            }
            if (am.seriesColors)
            {
                this.seriesColors = am.seriesColors
            }
            if (am.negativeSeriesColors)
            {
                this.negativeSeriesColors = am.negativeSeriesColors
            }
            if (am.captureRightClick)
            {
                this.captureRightClick = am.captureRightClick
            }
            this.defaultAxisStart = (ay && ay.defaultAxisStart != null) ? ay.defaultAxisStart
                : this.defaultAxisStart;
            this.colorGenerator.setColors(this.seriesColors);
            this.negativeColorGenerator.setColors(this.negativeSeriesColors);
            L.extend(true, this._gridPadding, am.gridPadding);
            this.sortData = (am.sortData != null) ? am.sortData : this.sortData;
            for (var at = 0; at < 12; at++)
            {
                var an = U[at];
                var ap = this.axes[an];
                ap._options = L.extend(true, {}, am.axesDefaults, am.axes[an]);
                L.extend(true, ap, am.axesDefaults, am.axes[an]);
                ap._plotWidth = this._width;
                ap._plotHeight = this._height
            }
            var aw = function (aD, aB, aE) {
                var aA = [];
                var aC, az;
                aB = aB || "vertical";
                if (!L.isArray(aD[0]))
                {
                    for (aC = 0, az = aD.length; aC < az; aC++)
                    {
                        if (aB == "vertical")
                        {
                            aA.push([aE + aC, aD[aC]])
                        }
                        else
                        {
                            aA.push([aD[aC], aE + aC])
                        }
                    }
                }
                else
                {
                    L.extend(true, aA, aD)
                }
                return aA
            };
            var av = 0;
            this.series = [];
            for (var at = 0; at < this.data.length; at++)
            {
                var al = L.extend(true, {
                    index: at
                }, {
                    seriesColors: this.seriesColors,
                    negativeSeriesColors: this.negativeSeriesColors
                }, this.options.seriesDefaults, this.options.series[at], {
                    rendererOptions: {
                        animation: {
                            show: this.animate
                        }
                    }
                });
                var ax = new S(al);
                for (var ar = 0; ar < L.jqplot.preParseSeriesOptionsHooks.length; ar++)
                {
                    L.jqplot.preParseSeriesOptionsHooks[ar].call(ax,
                        this.options.seriesDefaults,
                        this.options.series[at])
                }
                for (var ar = 0; ar < this.preParseSeriesOptionsHooks.hooks.length; ar++)
                {
                    this.preParseSeriesOptionsHooks.hooks[ar].call(ax,
                        this.options.seriesDefaults,
                        this.options.series[at])
                }
                L.extend(true, ax, al);
                var ao = "vertical";
                if (ax.renderer === L.jqplot.BarRenderer && ax.rendererOptions
                    && ax.rendererOptions.barDirection == "horizontal")
                {
                    ao = "horizontal";
                    ax._stackAxis = "x";
                    ax._primaryAxis = "_yaxis"
                }
                ax.data = aw(this.data[at], ao, this.defaultAxisStart);
                switch (ax.xaxis)
                {
                    case "xaxis":
                        ax._xaxis = this.axes.xaxis;
                        break;
                    case "x2axis":
                        ax._xaxis = this.axes.x2axis;
                        break;
                    default:
                        break
                }
                ax._yaxis = this.axes[ax.yaxis];
                ax._xaxis._series.push(ax);
                ax._yaxis._series.push(ax);
                if (ax.show)
                {
                    ax._xaxis.show = true;
                    ax._yaxis.show = true
                }
                else
                {
                    if (ax._xaxis.scaleToHiddenSeries)
                    {
                        ax._xaxis.show = true
                    }
                    if (ax._yaxis.scaleToHiddenSeries)
                    {
                        ax._yaxis.show = true
                    }
                }
                if (!ax.label)
                {
                    ax.label = "Series " + (at + 1).toString()
                }
                this.series.push(ax);
                for (var ar = 0; ar < L.jqplot.postParseSeriesOptionsHooks.length; ar++)
                {
                    L.jqplot.postParseSeriesOptionsHooks[ar].call(
                        this.series[at], this.options.seriesDefaults,
                        this.options.series[at])
                }
                for (var ar = 0; ar < this.postParseSeriesOptionsHooks.hooks.length; ar++)
                {
                    this.postParseSeriesOptionsHooks.hooks[ar].call(
                        this.series[at], this.options.seriesDefaults,
                        this.options.series[at])
                }
            }
            L.extend(true, this.grid, this.options.grid);
            for (var at = 0, aq = U.length; at < aq; at++)
            {
                var an = U[at];
                var ap = this.axes[an];
                if (ap.borderWidth == null)
                {
                    ap.borderWidth = this.grid.borderWidth
                }
            }
            if (typeof this.options.title == "string")
            {
                this.title.text = this.options.title
            }
            else
            {
                if (typeof this.options.title == "object")
                {
                    L.extend(true, this.title, this.options.title)
                }
            }
            this.title._plotWidth = this._width;
            this.legend.setOptions(this.options.legend);
            for (var at = 0; at < L.jqplot.postParseOptionsHooks.length; at++)
            {
                L.jqplot.postParseOptionsHooks[at].call(this, ay)
            }
            for (var at = 0; at < this.postParseOptionsHooks.hooks.length; at++)
            {
                this.postParseOptionsHooks.hooks[at].call(this, ay)
            }
        };
        this.destroy = function () {
            this.canvasManager.freeAllCanvases();
            if (this.eventCanvas && this.eventCanvas._elem)
            {
                this.eventCanvas._elem.unbind()
            }
            this.target.empty();
            this.target[0].innerHTML = ""
        };
        this.replot = function (am) {
            var an = am || {};
            var ap = an.data || null;
            var al = (an.clear === false) ? false : true;
            var ao = an.resetAxes || false;
            delete an.data;
            delete an.clear;
            delete an.resetAxes;
            this.target.trigger("jqplotPreReplot");
            if (al)
            {
                this.destroy()
            }
            if (ap || !L.isEmptyObject(an))
            {
                this.reInitialize(ap, an)
            }
            else
            {
                this.quickInit()
            }
            if (ao)
            {
                this.resetAxesScale(ao, an.axes)
            }
            this.draw();
            this.target.trigger("jqplotPostReplot")
        };
        this.redraw = function (al) {
            al = (al != null) ? al : true;
            this.target.trigger("jqplotPreRedraw");
            if (al)
            {
                this.canvasManager.freeAllCanvases();
                this.eventCanvas._elem.unbind();
                this.target.empty()
            }
            for (var an in this.axes)
            {
                this.axes[an]._ticks = []
            }
            this.computePlotData();
            this._sumy = 0;
            this._sumx = 0;
            for (var am = 0, ao = this.series.length; am < ao; am++)
            {
                this._sumy += this.series[am]._sumy;
                this._sumx += this.series[am]._sumx
            }
            this.draw();
            this.target.trigger("jqplotPostRedraw")
        };
        this.draw = function () {
            if (this.drawIfHidden || this.target.is(":visible"))
            {
                this.target.trigger("jqplotPreDraw");
                var aH, aF, aE, ao;
                for (aH = 0, aE = L.jqplot.preDrawHooks.length; aH < aE; aH++)
                {
                    L.jqplot.preDrawHooks[aH].call(this)
                }
                for (aH = 0, aE = this.preDrawHooks.hooks.length; aH < aE; aH++)
                {
                    this.preDrawHooks.hooks[aH].apply(this,
                        this.preDrawSeriesHooks.args[aH])
                }
                this.target.append(this.baseCanvas.createElement({
                    left: 0,
                    right: 0,
                    top: 0,
                    bottom: 0
                }, "jqplot-base-canvas", null, this));
                this.baseCanvas.setContext();
                this.target.append(this.title.draw());
                this.title.pack({
                    top: 0,
                    left: 0
                });
                var aL = this.legend.draw({}, this);
                var al = {
                    top: 0,
                    left: 0,
                    bottom: 0,
                    right: 0
                };
                if (this.legend.placement == "outsideGrid")
                {
                    this.target.append(aL);
                    switch (this.legend.location)
                    {
                        case "n":
                            al.top += this.legend.getHeight();
                            break;
                        case "s":
                            al.bottom += this.legend.getHeight();
                            break;
                        case "ne":
                        case "e":
                        case "se":
                            al.right += this.legend.getWidth();
                            break;
                        case "nw":
                        case "w":
                        case "sw":
                            al.left += this.legend.getWidth();
                            break;
                        default:
                            al.right += this.legend.getWidth();
                            break
                    }
                    aL = aL.detach()
                }
                var ar = this.axes;
                var aM;
                for (aH = 0; aH < 12; aH++)
                {
                    aM = U[aH];
                    this.target.append(ar[aM].draw(this.baseCanvas._ctx, this));
                    ar[aM].set()
                }
                if (ar.yaxis.show)
                {
                    al.left += ar.yaxis.getWidth()
                }
                var aG = ["y2axis", "y3axis", "y4axis", "y5axis", "y6axis",
                    "y7axis", "y8axis", "y9axis"];
                var az = [0, 0, 0, 0, 0, 0, 0, 0];
                var aC = 0;
                var aB;
                for (aB = 0; aB < 8; aB++)
                {
                    if (ar[aG[aB]].show)
                    {
                        aC += ar[aG[aB]].getWidth();
                        az[aB] = aC
                    }
                }
                al.right += aC;
                if (ar.x2axis.show)
                {
                    al.top += ar.x2axis.getHeight()
                }
                if (this.title.show)
                {
                    al.top += this.title.getHeight()
                }
                if (ar.xaxis.show)
                {
                    al.bottom += ar.xaxis.getHeight()
                }
                if (this.options.gridDimensions
                    && L.isPlainObject(this.options.gridDimensions))
                {
                    var at = parseInt(this.options.gridDimensions.width, 10) || 0;
                    var aI = parseInt(this.options.gridDimensions.height, 10) || 0;
                    var an = (this._width - al.left - al.right - at) / 2;
                    var aK = (this._height - al.top - al.bottom - aI) / 2;
                    if (aK >= 0 && an >= 0)
                    {
                        al.top += aK;
                        al.bottom += aK;
                        al.left += an;
                        al.right += an
                    }
                }
                var am = ["top", "bottom", "left", "right"];
                for (var aB in am)
                {
                    if (this._gridPadding[am[aB]] == null && al[am[aB]] > 0)
                    {
                        this._gridPadding[am[aB]] = al[am[aB]]
                    }
                    else
                    {
                        if (this._gridPadding[am[aB]] == null)
                        {
                            this._gridPadding[am[aB]] = this._defaultGridPadding[am[aB]]
                        }
                    }
                }
                var aA = this._gridPadding;
                if (this.legend.placement === "outsideGrid")
                {
                    aA = {
                        top: this.title.getHeight(),
                        left: 0,
                        right: 0,
                        bottom: 0
                    };
                    if (this.legend.location === "s")
                    {
                        aA.left = this._gridPadding.left;
                        aA.right = this._gridPadding.right
                    }
                }
                ar.xaxis.pack({
                    position: "absolute",
                    bottom: this._gridPadding.bottom - ar.xaxis.getHeight(),
                    left: 0,
                    width: this._width
                }, {
                    min: this._gridPadding.left,
                    max: this._width - this._gridPadding.right
                });
                ar.yaxis.pack({
                    position: "absolute",
                    top: 0,
                    left: this._gridPadding.left - ar.yaxis.getWidth(),
                    height: this._height
                }, {
                    min: this._height - this._gridPadding.bottom,
                    max: this._gridPadding.top
                });
                ar.x2axis.pack({
                    position: "absolute",
                    top: this._gridPadding.top - ar.x2axis.getHeight(),
                    left: 0,
                    width: this._width
                }, {
                    min: this._gridPadding.left,
                    max: this._width - this._gridPadding.right
                });
                for (aH = 8; aH > 0; aH--)
                {
                    ar[aG[aH - 1]].pack({
                        position: "absolute",
                        top: 0,
                        right: this._gridPadding.right - az[aH - 1]
                    }, {
                        min: this._height - this._gridPadding.bottom,
                        max: this._gridPadding.top
                    })
                }
                var au = (this._width - this._gridPadding.left - this._gridPadding.right)
                    / 2
                    + this._gridPadding.left
                    - ar.yMidAxis.getWidth()
                    / 2;
                ar.yMidAxis.pack({
                    position: "absolute",
                    top: 0,
                    left: au,
                    zIndex: 9,
                    textAlign: "center"
                }, {
                    min: this._height - this._gridPadding.bottom,
                    max: this._gridPadding.top
                });
                this.target.append(this.grid.createElement(this._gridPadding,
                    this));
                this.grid.draw();
                var aq = this.series;
                var aJ = aq.length;
                for (aH = 0, aE = aJ; aH < aE; aH++)
                {
                    aF = this.seriesStack[aH];
                    this.target.append(aq[aF].shadowCanvas.createElement(
                        this._gridPadding, "jqplot-series-shadowCanvas",
                        null, this));
                    aq[aF].shadowCanvas.setContext();
                    aq[aF].shadowCanvas._elem.data("seriesIndex", aF)
                }
                for (aH = 0, aE = aJ; aH < aE; aH++)
                {
                    aF = this.seriesStack[aH];
                    this.target.append(aq[aF].canvas.createElement(
                        this._gridPadding, "jqplot-series-canvas", null,
                        this));
                    aq[aF].canvas.setContext();
                    aq[aF].canvas._elem.data("seriesIndex", aF)
                }
                this.target.append(this.eventCanvas.createElement(
                    this._gridPadding, "jqplot-event-canvas", null, this));
                this.eventCanvas.setContext();
                this.eventCanvas._ctx.fillStyle = "rgba(0,0,0,0)";
                this.eventCanvas._ctx.fillRect(0, 0,
                    this.eventCanvas._ctx.canvas.width,
                    this.eventCanvas._ctx.canvas.height);
                this.bindCustomEvents();
                if (this.legend.preDraw)
                {
                    this.eventCanvas._elem.before(aL);
                    this.legend.pack(aA);
                    if (this.legend._elem)
                    {
                        this.drawSeries({
                            legendInfo: {
                                location: this.legend.location,
                                placement: this.legend.placement,
                                width: this.legend.getWidth(),
                                height: this.legend.getHeight(),
                                xoffset: this.legend.xoffset,
                                yoffset: this.legend.yoffset
                            }
                        })
                    }
                    else
                    {
                        this.drawSeries()
                    }
                }
                else
                {
                    this.drawSeries();
                    if (aJ)
                    {
                        L(aq[aJ - 1].canvas._elem).after(aL)
                    }
                    this.legend.pack(aA)
                }
                for (var aH = 0, aE = L.jqplot.eventListenerHooks.length; aH < aE; aH++)
                {
                    this.eventCanvas._elem.bind(
                        L.jqplot.eventListenerHooks[aH][0], {
                            plot: this
                        }, L.jqplot.eventListenerHooks[aH][1])
                }
                for (var aH = 0, aE = this.eventListenerHooks.hooks.length; aH < aE; aH++)
                {
                    this.eventCanvas._elem.bind(
                        this.eventListenerHooks.hooks[aH][0], {
                            plot: this
                        }, this.eventListenerHooks.hooks[aH][1])
                }
                var ay = this.fillBetween;
                if (ay.fill && ay.series1 !== ay.series2 && ay.series1 < aJ
                    && ay.series2 < aJ && aq[ay.series1]._type === "line"
                    && aq[ay.series2]._type === "line")
                {
                    this.doFillBetweenLines()
                }
                for (var aH = 0, aE = L.jqplot.postDrawHooks.length; aH < aE; aH++)
                {
                    L.jqplot.postDrawHooks[aH].call(this)
                }
                for (var aH = 0, aE = this.postDrawHooks.hooks.length; aH < aE; aH++)
                {
                    this.postDrawHooks.hooks[aH].apply(this,
                        this.postDrawHooks.args[aH])
                }
                if (this.target.is(":visible"))
                {
                    this._drawCount += 1
                }
                var av, aw, aD, ap;
                for (aH = 0, aE = aJ; aH < aE; aH++)
                {
                    av = aq[aH];
                    aw = av.renderer;
                    aD = ".jqplot-point-label.jqplot-series-" + aH;
                    if (aw.animation && aw.animation._supported
                        && aw.animation.show
                        && (this._drawCount < 2 || this.animateReplot))
                    {
                        ap = this.target.find(aD);
                        ap.stop(true, true).hide();
                        av.canvas._elem.stop(true, true).hide();
                        av.shadowCanvas._elem.stop(true, true).hide();
                        av.canvas._elem.jqplotEffect("blind", {
                            mode: "show",
                            direction: aw.animation.direction
                        }, aw.animation.speed);
                        av.shadowCanvas._elem.jqplotEffect("blind", {
                            mode: "show",
                            direction: aw.animation.direction
                        }, aw.animation.speed);
                        ap.fadeIn(aw.animation.speed * 0.8)
                    }
                }
                ap = null;
                this.target.trigger("jqplotPostDraw", [this])
            }
        };
        R.prototype.doFillBetweenLines = function () {
            var an = this.fillBetween;
            var ax = an.series1;
            var av = an.series2;
            var aw = (ax < av) ? ax : av;
            var au = (av > ax) ? av : ax;
            var ar = this.series[aw];
            var aq = this.series[au];
            if (aq.renderer.smooth)
            {
                var ap = aq.renderer._smoothedData.slice(0).reverse()
            }
            else
            {
                var ap = aq.gridData.slice(0).reverse()
            }
            if (ar.renderer.smooth)
            {
                var at = ar.renderer._smoothedData.concat(ap)
            }
            else
            {
                var at = ar.gridData.concat(ap)
            }
            var ao = (an.color !== null) ? an.color : this.series[ax].fillColor;
            var ay = (an.baseSeries !== null) ? an.baseSeries : aw;
            var am = this.series[ay].renderer.shapeRenderer;
            var al = {
                fillStyle: ao,
                fill: true,
                closePath: true
            };
            am.draw(ar.shadowCanvas._ctx, at, al)
        };
        this.bindCustomEvents = function () {
            this.eventCanvas._elem.bind("click", {
                plot: this
            }, this.onClick);
            this.eventCanvas._elem.bind("dblclick", {
                plot: this
            }, this.onDblClick);
            this.eventCanvas._elem.bind("mousedown", {
                plot: this
            }, this.onMouseDown);
            this.eventCanvas._elem.bind("mousemove", {
                plot: this
            }, this.onMouseMove);
            this.eventCanvas._elem.bind("mouseenter", {
                plot: this
            }, this.onMouseEnter);
            this.eventCanvas._elem.bind("mouseleave", {
                plot: this
            }, this.onMouseLeave);
            if (this.captureRightClick)
            {
                this.eventCanvas._elem.bind("mouseup", {
                    plot: this
                }, this.onRightClick);
                this.eventCanvas._elem.get(0).oncontextmenu = function () {
                    return false
                }
            }
            else
            {
                this.eventCanvas._elem.bind("mouseup", {
                    plot: this
                }, this.onMouseUp)
            }
        };

        function ai(av)
        {
            var au = av.data.plot;
            var ap = au.eventCanvas._elem.offset();
            var at = {
                x: av.pageX - ap.left,
                y: av.pageY - ap.top
            };
            var aq = {
                xaxis: null,
                yaxis: null,
                x2axis: null,
                y2axis: null,
                y3axis: null,
                y4axis: null,
                y5axis: null,
                y6axis: null,
                y7axis: null,
                y8axis: null,
                y9axis: null,
                yMidAxis: null
            };
            var ar = ["xaxis", "yaxis", "x2axis", "y2axis", "y3axis",
                "y4axis", "y5axis", "y6axis", "y7axis", "y8axis", "y9axis",
                "yMidAxis"];
            var al = au.axes;
            var am, ao;
            for (am = 11; am > 0; am--)
            {
                ao = ar[am - 1];
                if (al[ao].show)
                {
                    aq[ao] = al[ao].series_p2u(at[ao.charAt(0)])
                }
            }
            return {
                offsets: ap,
                gridPos: at,
                dataPos: aq
            }
        }

        function ak(al, am)
        {
            var aq = am.series;
            var aW, aU, aT, aO, aP, aJ, aI, aw, au, az, aA, aK;
            var aS, aX, aQ, ar, aH, aM, aV;
            var an, aN;
            for (aT = am.seriesStack.length - 1; aT >= 0; aT--)
            {
                aW = am.seriesStack[aT];
                aO = aq[aW];
                aV = aO._highlightThreshold;
                switch (aO.renderer.constructor)
                {
                    case L.jqplot.BarRenderer:
                        aJ = al.x;
                        aI = al.y;
                        for (aU = 0; aU < aO._barPoints.length; aU++)
                        {
                            aH = aO._barPoints[aU];
                            aQ = aO.gridData[aU];
                            if (aJ > aH[0][0] && aJ < aH[2][0] && aI > aH[2][1]
                                && aI < aH[0][1])
                            {
                                return {
                                    seriesIndex: aO.index,
                                    pointIndex: aU,
                                    gridData: aQ,
                                    data: aO.data[aU],
                                    points: aO._barPoints[aU]
                                }
                            }
                        }
                        break;
                    case L.jqplot.PyramidRenderer:
                        aJ = al.x;
                        aI = al.y;
                        for (aU = 0; aU < aO._barPoints.length; aU++)
                        {
                            aH = aO._barPoints[aU];
                            aQ = aO.gridData[aU];
                            if (aJ > aH[0][0] + aV[0][0]
                                && aJ < aH[2][0] + aV[2][0] && aI > aH[2][1]
                                && aI < aH[0][1])
                            {
                                return {
                                    seriesIndex: aO.index,
                                    pointIndex: aU,
                                    gridData: aQ,
                                    data: aO.data[aU],
                                    points: aO._barPoints[aU]
                                }
                            }
                        }
                        break;
                    case L.jqplot.DonutRenderer:
                        az = aO.startAngle / 180 * Math.PI;
                        aJ = al.x - aO._center[0];
                        aI = al.y - aO._center[1];
                        aP = Math.sqrt(Math.pow(aJ, 2) + Math.pow(aI, 2));
                        if (aJ > 0 && -aI >= 0)
                        {
                            aw = 2 * Math.PI - Math.atan(-aI / aJ)
                        }
                        else
                        {
                            if (aJ > 0 && -aI < 0)
                            {
                                aw = -Math.atan(-aI / aJ)
                            }
                            else
                            {
                                if (aJ < 0)
                                {
                                    aw = Math.PI - Math.atan(-aI / aJ)
                                }
                                else
                                {
                                    if (aJ == 0 && -aI > 0)
                                    {
                                        aw = 3 * Math.PI / 2
                                    }
                                    else
                                    {
                                        if (aJ == 0 && -aI < 0)
                                        {
                                            aw = Math.PI / 2
                                        }
                                        else
                                        {
                                            if (aJ == 0 && aI == 0)
                                            {
                                                aw = 0
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (az)
                        {
                            aw -= az;
                            if (aw < 0)
                            {
                                aw += 2 * Math.PI
                            }
                            else
                            {
                                if (aw > 2 * Math.PI)
                                {
                                    aw -= 2 * Math.PI
                                }
                            }
                        }
                        au = aO.sliceMargin / 180 * Math.PI;
                        if (aP < aO._radius && aP > aO._innerRadius)
                        {
                            for (aU = 0; aU < aO.gridData.length; aU++)
                            {
                                aA = (aU > 0) ? aO.gridData[aU - 1][1] + au : au;
                                aK = aO.gridData[aU][1];
                                if (aw > aA && aw < aK)
                                {
                                    return {
                                        seriesIndex: aO.index,
                                        pointIndex: aU,
                                        gridData: [al.x, al.y],
                                        data: aO.data[aU]
                                    }
                                }
                            }
                        }
                        break;
                    case L.jqplot.PieRenderer:
                        az = aO.startAngle / 180 * Math.PI;
                        aJ = al.x - aO._center[0];
                        aI = al.y - aO._center[1];
                        aP = Math.sqrt(Math.pow(aJ, 2) + Math.pow(aI, 2));
                        if (aJ > 0 && -aI >= 0)
                        {
                            aw = 2 * Math.PI - Math.atan(-aI / aJ)
                        }
                        else
                        {
                            if (aJ > 0 && -aI < 0)
                            {
                                aw = -Math.atan(-aI / aJ)
                            }
                            else
                            {
                                if (aJ < 0)
                                {
                                    aw = Math.PI - Math.atan(-aI / aJ)
                                }
                                else
                                {
                                    if (aJ == 0 && -aI > 0)
                                    {
                                        aw = 3 * Math.PI / 2
                                    }
                                    else
                                    {
                                        if (aJ == 0 && -aI < 0)
                                        {
                                            aw = Math.PI / 2
                                        }
                                        else
                                        {
                                            if (aJ == 0 && aI == 0)
                                            {
                                                aw = 0
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (az)
                        {
                            aw -= az;
                            if (aw < 0)
                            {
                                aw += 2 * Math.PI
                            }
                            else
                            {
                                if (aw > 2 * Math.PI)
                                {
                                    aw -= 2 * Math.PI
                                }
                            }
                        }
                        au = aO.sliceMargin / 180 * Math.PI;
                        if (aP < aO._radius)
                        {
                            for (aU = 0; aU < aO.gridData.length; aU++)
                            {
                                aA = (aU > 0) ? aO.gridData[aU - 1][1] + au : au;
                                aK = aO.gridData[aU][1];
                                if (aw > aA && aw < aK)
                                {
                                    return {
                                        seriesIndex: aO.index,
                                        pointIndex: aU,
                                        gridData: [al.x, al.y],
                                        data: aO.data[aU]
                                    }
                                }
                            }
                        }
                        break;
                    case L.jqplot.BubbleRenderer:
                        aJ = al.x;
                        aI = al.y;
                        var aF = null;
                        if (aO.show)
                        {
                            for (var aU = 0; aU < aO.gridData.length; aU++)
                            {
                                aQ = aO.gridData[aU];
                                aX = Math.sqrt((aJ - aQ[0]) * (aJ - aQ[0])
                                    + (aI - aQ[1]) * (aI - aQ[1]));
                                if (aX <= aQ[2] && (aX <= aS || aS == null))
                                {
                                    aS = aX;
                                    aF = {
                                        seriesIndex: aW,
                                        pointIndex: aU,
                                        gridData: aQ,
                                        data: aO.data[aU]
                                    }
                                }
                            }
                            if (aF != null)
                            {
                                return aF
                            }
                        }
                        break;
                    case L.jqplot.FunnelRenderer:
                        aJ = al.x;
                        aI = al.y;
                        var aL = aO._vertices, ap = aL[0], ao = aL[aL.length - 1], at, aE, ay;

                    function aR(a0, a2, a1)
                    {
                        var aZ = (a2[1] - a1[1]) / (a2[0] - a1[0]);
                        var aY = a2[1] - aZ * a2[0];
                        var a3 = a0 + a2[1];
                        return [(a3 - aY) / aZ, a3]
                    }

                        at = aR(aI, ap[0], ao[3]);
                        aE = aR(aI, ap[1], ao[2]);
                        for (aU = 0; aU < aL.length; aU++)
                        {
                            ay = aL[aU];
                            if (aI >= ay[0][1] && aI <= ay[3][1] && aJ >= at[0]
                                && aJ <= aE[0])
                            {
                                return {
                                    seriesIndex: aO.index,
                                    pointIndex: aU,
                                    gridData: null,
                                    data: aO.data[aU]
                                }
                            }
                        }
                        break;
                    case L.jqplot.LineRenderer:
                        aJ = al.x;
                        aI = al.y;
                        aP = aO.renderer;
                        if (aO.show)
                        {
                            if ((aO.fill || (aO.renderer.bands.show && aO.renderer.bands.fill))
                                && (!am.plugins.highlighter || !am.plugins.highlighter.show))
                            {
                                var ax = false;
                                if (aJ > aO._boundingBox[0][0]
                                    && aJ < aO._boundingBox[1][0]
                                    && aI > aO._boundingBox[1][1]
                                    && aI < aO._boundingBox[0][1])
                                {
                                    var aD = aO._areaPoints.length;
                                    var aG;
                                    var aU = aD - 1;
                                    for (var aG = 0; aG < aD; aG++)
                                    {
                                        var aC = [aO._areaPoints[aG][0],
                                            aO._areaPoints[aG][1]];
                                        var aB = [aO._areaPoints[aU][0],
                                            aO._areaPoints[aU][1]];
                                        if (aC[1] < aI && aB[1] >= aI || aB[1] < aI
                                            && aC[1] >= aI)
                                        {
                                            if (aC[0] + (aI - aC[1])
                                                / (aB[1] - aC[1])
                                                * (aB[0] - aC[0]) < aJ)
                                            {
                                                ax = !ax
                                            }
                                        }
                                        aU = aG
                                    }
                                }
                                if (ax)
                                {
                                    return {
                                        seriesIndex: aW,
                                        pointIndex: null,
                                        gridData: aO.gridData,
                                        data: aO.data,
                                        points: aO._areaPoints
                                    }
                                }
                                break
                            }
                            else
                            {
                                aN = aO.markerRenderer.size / 2
                                    + aO.neighborThreshold;
                                an = (aN > 0) ? aN : 0;
                                for (var aU = 0; aU < aO.gridData.length; aU++)
                                {
                                    aQ = aO.gridData[aU];
                                    if (aP.constructor == L.jqplot.OHLCRenderer)
                                    {
                                        if (aP.candleStick)
                                        {
                                            var av = aO._yaxis.series_u2p;
                                            if (aJ >= aQ[0] - aP._bodyWidth / 2
                                                && aJ <= aQ[0] + aP._bodyWidth
                                                / 2
                                                && aI >= av(aO.data[aU][2])
                                                && aI <= av(aO.data[aU][3]))
                                            {
                                                return {
                                                    seriesIndex: aW,
                                                    pointIndex: aU,
                                                    gridData: aQ,
                                                    data: aO.data[aU]
                                                }
                                            }
                                        }
                                        else
                                        {
                                            if (!aP.hlc)
                                            {
                                                var av = aO._yaxis.series_u2p;
                                                if (aJ >= aQ[0] - aP._tickLength
                                                    && aJ <= aQ[0]
                                                    + aP._tickLength
                                                    && aI >= av(aO.data[aU][2])
                                                    && aI <= av(aO.data[aU][3]))
                                                {
                                                    return {
                                                        seriesIndex: aW,
                                                        pointIndex: aU,
                                                        gridData: aQ,
                                                        data: aO.data[aU]
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                var av = aO._yaxis.series_u2p;
                                                if (aJ >= aQ[0] - aP._tickLength
                                                    && aJ <= aQ[0]
                                                    + aP._tickLength
                                                    && aI >= av(aO.data[aU][1])
                                                    && aI <= av(aO.data[aU][2]))
                                                {
                                                    return {
                                                        seriesIndex: aW,
                                                        pointIndex: aU,
                                                        gridData: aQ,
                                                        data: aO.data[aU]
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        if (aQ[0] != null && aQ[1] != null)
                                        {
                                            aX = Math.sqrt((aJ - aQ[0])
                                                * (aJ - aQ[0]) + (aI - aQ[1])
                                                * (aI - aQ[1]));
                                            if (aX <= an
                                                && (aX <= aS || aS == null))
                                            {
                                                aS = aX;
                                                return {
                                                    seriesIndex: aW,
                                                    pointIndex: aU,
                                                    gridData: aQ,
                                                    data: aO.data[aU]
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    default:
                        aJ = al.x;
                        aI = al.y;
                        aP = aO.renderer;
                        if (aO.show)
                        {
                            aN = aO.markerRenderer.size / 2 + aO.neighborThreshold;
                            an = (aN > 0) ? aN : 0;
                            for (var aU = 0; aU < aO.gridData.length; aU++)
                            {
                                aQ = aO.gridData[aU];
                                if (aP.constructor == L.jqplot.OHLCRenderer)
                                {
                                    if (aP.candleStick)
                                    {
                                        var av = aO._yaxis.series_u2p;
                                        if (aJ >= aQ[0] - aP._bodyWidth / 2
                                            && aJ <= aQ[0] + aP._bodyWidth / 2
                                            && aI >= av(aO.data[aU][2])
                                            && aI <= av(aO.data[aU][3]))
                                        {
                                            return {
                                                seriesIndex: aW,
                                                pointIndex: aU,
                                                gridData: aQ,
                                                data: aO.data[aU]
                                            }
                                        }
                                    }
                                    else
                                    {
                                        if (!aP.hlc)
                                        {
                                            var av = aO._yaxis.series_u2p;
                                            if (aJ >= aQ[0] - aP._tickLength
                                                && aJ <= aQ[0] + aP._tickLength
                                                && aI >= av(aO.data[aU][2])
                                                && aI <= av(aO.data[aU][3]))
                                            {
                                                return {
                                                    seriesIndex: aW,
                                                    pointIndex: aU,
                                                    gridData: aQ,
                                                    data: aO.data[aU]
                                                }
                                            }
                                        }
                                        else
                                        {
                                            var av = aO._yaxis.series_u2p;
                                            if (aJ >= aQ[0] - aP._tickLength
                                                && aJ <= aQ[0] + aP._tickLength
                                                && aI >= av(aO.data[aU][1])
                                                && aI <= av(aO.data[aU][2]))
                                            {
                                                return {
                                                    seriesIndex: aW,
                                                    pointIndex: aU,
                                                    gridData: aQ,
                                                    data: aO.data[aU]
                                                }
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    aX = Math.sqrt((aJ - aQ[0]) * (aJ - aQ[0])
                                        + (aI - aQ[1]) * (aI - aQ[1]));
                                    if (aX <= an && (aX <= aS || aS == null))
                                    {
                                        aS = aX;
                                        return {
                                            seriesIndex: aW,
                                            pointIndex: aU,
                                            gridData: aQ,
                                            data: aO.data[aU]
                                        }
                                    }
                                }
                            }
                        }
                        break
                }
            }
            return null
        }

        this.onClick = function (an) {
            var am = ai(an);
            var ap = an.data.plot;
            var ao = ak(am.gridPos, ap);
            var al = L.Event("jqplotClick");
            al.pageX = an.pageX;
            al.pageY = an.pageY;
            L(this).trigger(al, [am.gridPos, am.dataPos, ao, ap])
        };
        this.onDblClick = function (an) {
            var am = ai(an);
            var ap = an.data.plot;
            var ao = ak(am.gridPos, ap);
            var al = L.Event("jqplotDblClick");
            al.pageX = an.pageX;
            al.pageY = an.pageY;
            L(this).trigger(al, [am.gridPos, am.dataPos, ao, ap])
        };
        this.onMouseDown = function (an) {
            var am = ai(an);
            var ap = an.data.plot;
            var ao = ak(am.gridPos, ap);
            var al = L.Event("jqplotMouseDown");
            al.pageX = an.pageX;
            al.pageY = an.pageY;
            L(this).trigger(al, [am.gridPos, am.dataPos, ao, ap])
        };
        this.onMouseUp = function (an) {
            var am = ai(an);
            var al = L.Event("jqplotMouseUp");
            al.pageX = an.pageX;
            al.pageY = an.pageY;
            L(this).trigger(al, [am.gridPos, am.dataPos, null, an.data.plot])
        };
        this.onRightClick = function (an) {
            var am = ai(an);
            var ap = an.data.plot;
            var ao = ak(am.gridPos, ap);
            if (ap.captureRightClick)
            {
                if (an.which == 3)
                {
                    var al = L.Event("jqplotRightClick");
                    al.pageX = an.pageX;
                    al.pageY = an.pageY;
                    L(this).trigger(al, [am.gridPos, am.dataPos, ao, ap])
                }
                else
                {
                    var al = L.Event("jqplotMouseUp");
                    al.pageX = an.pageX;
                    al.pageY = an.pageY;
                    L(this).trigger(al, [am.gridPos, am.dataPos, ao, ap])
                }
            }
        };
        this.onMouseMove = function (an) {
            var am = ai(an);
            var ap = an.data.plot;
            var ao = ak(am.gridPos, ap);
            var al = L.Event("jqplotMouseMove");
            al.pageX = an.pageX;
            al.pageY = an.pageY;
            L(this).trigger(al, [am.gridPos, am.dataPos, ao, ap])
        };
        this.onMouseEnter = function (an) {
            var am = ai(an);
            var ao = an.data.plot;
            var al = L.Event("jqplotMouseEnter");
            al.pageX = an.pageX;
            al.pageY = an.pageY;
            al.relatedTarget = an.relatedTarget;
            L(this).trigger(al, [am.gridPos, am.dataPos, null, ao])
        };
        this.onMouseLeave = function (an) {
            var am = ai(an);
            var ao = an.data.plot;
            var al = L.Event("jqplotMouseLeave");
            al.pageX = an.pageX;
            al.pageY = an.pageY;
            al.relatedTarget = an.relatedTarget;
            L(this).trigger(al, [am.gridPos, am.dataPos, null, ao])
        };
        this.drawSeries = function (an, al) {
            var ap, ao, am;
            al = (typeof (an) === "number" && al == null) ? an : al;
            an = (typeof (an) === "object") ? an : {};
            if (al != u)
            {
                ao = this.series[al];
                am = ao.shadowCanvas._ctx;
                am.clearRect(0, 0, am.canvas.width, am.canvas.height);
                ao.drawShadow(am, an, this);
                am = ao.canvas._ctx;
                am.clearRect(0, 0, am.canvas.width, am.canvas.height);
                ao.draw(am, an, this);
                if (ao.renderer.constructor == L.jqplot.BezierCurveRenderer)
                {
                    if (al < this.series.length - 1)
                    {
                        this.drawSeries(al + 1)
                    }
                }
            }
            else
            {
                for (ap = 0; ap < this.series.length; ap++)
                {
                    ao = this.series[ap];
                    am = ao.shadowCanvas._ctx;
                    am.clearRect(0, 0, am.canvas.width, am.canvas.height);
                    ao.drawShadow(am, an, this);
                    am = ao.canvas._ctx;
                    am.clearRect(0, 0, am.canvas.width, am.canvas.height);
                    ao.draw(am, an, this)
                }
            }
            an = al = ap = ao = am = null
        };
        this.moveSeriesToFront = function (am) {
            am = parseInt(am, 10);
            var ap = L.inArray(am, this.seriesStack);
            if (ap == -1)
            {
                return
            }
            if (ap == this.seriesStack.length - 1)
            {
                this.previousSeriesStack = this.seriesStack.slice(0);
                return
            }
            var al = this.seriesStack[this.seriesStack.length - 1];
            var ao = this.series[am].canvas._elem.detach();
            var an = this.series[am].shadowCanvas._elem.detach();
            this.series[al].shadowCanvas._elem.after(an);
            this.series[al].canvas._elem.after(ao);
            this.previousSeriesStack = this.seriesStack.slice(0);
            this.seriesStack.splice(ap, 1);
            this.seriesStack.push(am)
        };
        this.moveSeriesToBack = function (am) {
            am = parseInt(am, 10);
            var ap = L.inArray(am, this.seriesStack);
            if (ap == 0 || ap == -1)
            {
                return
            }
            var al = this.seriesStack[0];
            var ao = this.series[am].canvas._elem.detach();
            var an = this.series[am].shadowCanvas._elem.detach();
            this.series[al].shadowCanvas._elem.before(an);
            this.series[al].canvas._elem.before(ao);
            this.previousSeriesStack = this.seriesStack.slice(0);
            this.seriesStack.splice(ap, 1);
            this.seriesStack.unshift(am)
        };
        this.restorePreviousSeriesOrder = function () {
            var ar, aq, ap, ao, an, al, am;
            if (this.seriesStack == this.previousSeriesStack)
            {
                return
            }
            for (ar = 1; ar < this.previousSeriesStack.length; ar++)
            {
                al = this.previousSeriesStack[ar];
                am = this.previousSeriesStack[ar - 1];
                ap = this.series[al].canvas._elem.detach();
                ao = this.series[al].shadowCanvas._elem.detach();
                this.series[am].shadowCanvas._elem.after(ao);
                this.series[am].canvas._elem.after(ap)
            }
            an = this.seriesStack.slice(0);
            this.seriesStack = this.previousSeriesStack.slice(0);
            this.previousSeriesStack = an
        };
        this.restoreOriginalSeriesOrder = function () {
            var ap, ao, al = [], an, am;
            for (ap = 0; ap < this.series.length; ap++)
            {
                al.push(ap)
            }
            if (this.seriesStack == al)
            {
                return
            }
            this.previousSeriesStack = this.seriesStack.slice(0);
            this.seriesStack = al;
            for (ap = 1; ap < this.seriesStack.length; ap++)
            {
                an = this.series[ap].canvas._elem.detach();
                am = this.series[ap].shadowCanvas._elem.detach();
                this.series[ap - 1].shadowCanvas._elem.after(am);
                this.series[ap - 1].canvas._elem.after(an)
            }
        };
        this.activateTheme = function (al) {
            this.themeEngine.activate(this, al)
        }
    }

    L.jqplot.computeHighlightColors = function (ai) {
        var ak;
        if (L.isArray(ai))
        {
            ak = [];
            for (var am = 0; am < ai.length; am++)
            {
                var al = L.jqplot.getColorComponents(ai[am]);
                var ah = [al[0], al[1], al[2]];
                var an = ah[0] + ah[1] + ah[2];
                for (var aj = 0; aj < 3; aj++)
                {
                    ah[aj] = (an > 660) ? ah[aj] * 0.85 : 0.73 * ah[aj] + 90;
                    ah[aj] = parseInt(ah[aj], 10);
                    (ah[aj] > 255) ? 255 : ah[aj]
                }
                ah[3] = 0.3 + 0.35 * al[3];
                ak.push("rgba(" + ah[0] + "," + ah[1] + "," + ah[2] + ","
                    + ah[3] + ")")
            }
        }
        else
        {
            var al = L.jqplot.getColorComponents(ai);
            var ah = [al[0], al[1], al[2]];
            var an = ah[0] + ah[1] + ah[2];
            for (var aj = 0; aj < 3; aj++)
            {
                ah[aj] = (an > 660) ? ah[aj] * 0.85 : 0.73 * ah[aj] + 90;
                ah[aj] = parseInt(ah[aj], 10);
                (ah[aj] > 255) ? 255 : ah[aj]
            }
            ah[3] = 0.3 + 0.35 * al[3];
            ak = "rgba(" + ah[0] + "," + ah[1] + "," + ah[2] + "," + ah[3]
                + ")"
        }
        return ak
    };
    L.jqplot.ColorGenerator = function (ai) {
        ai = ai || L.jqplot.config.defaultColors;
        var ah = 0;
        this.next = function () {
            if (ah < ai.length)
            {
                return ai[ah++]
            }
            else
            {
                ah = 0;
                return ai[ah++]
            }
        };
        this.previous = function () {
            if (ah > 0)
            {
                return ai[ah--]
            }
            else
            {
                ah = ai.length - 1;
                return ai[ah]
            }
        };
        this.get = function (ak) {
            var aj = ak - ai.length * Math.floor(ak / ai.length);
            return ai[aj]
        };
        this.setColors = function (aj) {
            ai = aj
        };
        this.reset = function () {
            ah = 0
        };
        this.getIndex = function () {
            return ah
        };
        this.setIndex = function (aj) {
            ah = aj
        }
    };
    L.jqplot.hex2rgb = function (aj, ah) {
        aj = aj.replace("#", "");
        if (aj.length == 3)
        {
            aj = aj.charAt(0) + aj.charAt(0) + aj.charAt(1) + aj.charAt(1)
                + aj.charAt(2) + aj.charAt(2)
        }
        var ai;
        ai = "rgba(" + parseInt(aj.slice(0, 2), 16) + ", "
            + parseInt(aj.slice(2, 4), 16) + ", "
            + parseInt(aj.slice(4, 6), 16);
        if (ah)
        {
            ai += ", " + ah
        }
        ai += ")";
        return ai
    };
    L.jqplot.rgb2hex = function (am) {
        var aj = /rgba?\( *([0-9]{1,3}\.?[0-9]*%?) *, *([0-9]{1,3}\.?[0-9]*%?) *, *([0-9]{1,3}\.?[0-9]*%?) *(?:, *[0-9.]*)?\)/;
        var ah = am.match(aj);
        var al = "#";
        for (var ak = 1; ak < 4; ak++)
        {
            var ai;
            if (ah[ak].search(/%/) != -1)
            {
                ai = parseInt(255 * ah[ak] / 100, 10).toString(16);
                if (ai.length == 1)
                {
                    ai = "0" + ai
                }
            }
            else
            {
                ai = parseInt(ah[ak], 10).toString(16);
                if (ai.length == 1)
                {
                    ai = "0" + ai
                }
            }
            al += ai
        }
        return al
    };
    L.jqplot.normalize2rgb = function (ai, ah) {
        if (ai.search(/^ *rgba?\(/) != -1)
        {
            return ai
        }
        else
        {
            if (ai.search(/^ *#?[0-9a-fA-F]?[0-9a-fA-F]/) != -1)
            {
                return L.jqplot.hex2rgb(ai, ah)
            }
            else
            {
                throw new Error("Invalid color spec")
            }
        }
    };
    L.jqplot.getColorComponents = function (am) {
        am = L.jqplot.colorKeywordMap[am] || am;
        var ak = L.jqplot.normalize2rgb(am);
        var aj = /rgba?\( *([0-9]{1,3}\.?[0-9]*%?) *, *([0-9]{1,3}\.?[0-9]*%?) *, *([0-9]{1,3}\.?[0-9]*%?) *,? *([0-9.]* *)?\)/;
        var ah = ak.match(aj);
        var ai = [];
        for (var al = 1; al < 4; al++)
        {
            if (ah[al].search(/%/) != -1)
            {
                ai[al - 1] = parseInt(255 * ah[al] / 100, 10)
            }
            else
            {
                ai[al - 1] = parseInt(ah[al], 10)
            }
        }
        ai[3] = parseFloat(ah[4]) ? parseFloat(ah[4]) : 1;
        return ai
    };
    L.jqplot.colorKeywordMap = {
        aliceblue: "rgb(240, 248, 255)",
        antiquewhite: "rgb(250, 235, 215)",
        aqua: "rgb( 0, 255, 255)",
        aquamarine: "rgb(127, 255, 212)",
        azure: "rgb(240, 255, 255)",
        beige: "rgb(245, 245, 220)",
        bisque: "rgb(255, 228, 196)",
        black: "rgb( 0, 0, 0)",
        blanchedalmond: "rgb(255, 235, 205)",
        blue: "rgb( 0, 0, 255)",
        blueviolet: "rgb(138, 43, 226)",
        brown: "rgb(165, 42, 42)",
        burlywood: "rgb(222, 184, 135)",
        cadetblue: "rgb( 95, 158, 160)",
        chartreuse: "rgb(127, 255, 0)",
        chocolate: "rgb(210, 105, 30)",
        coral: "rgb(255, 127, 80)",
        cornflowerblue: "rgb(100, 149, 237)",
        cornsilk: "rgb(255, 248, 220)",
        crimson: "rgb(220, 20, 60)",
        cyan: "rgb( 0, 255, 255)",
        darkblue: "rgb( 0, 0, 139)",
        darkcyan: "rgb( 0, 139, 139)",
        darkgoldenrod: "rgb(184, 134, 11)",
        darkgray: "rgb(169, 169, 169)",
        darkgreen: "rgb( 0, 100, 0)",
        darkgrey: "rgb(169, 169, 169)",
        darkkhaki: "rgb(189, 183, 107)",
        darkmagenta: "rgb(139, 0, 139)",
        darkolivegreen: "rgb( 85, 107, 47)",
        darkorange: "rgb(255, 140, 0)",
        darkorchid: "rgb(153, 50, 204)",
        darkred: "rgb(139, 0, 0)",
        darksalmon: "rgb(233, 150, 122)",
        darkseagreen: "rgb(143, 188, 143)",
        darkslateblue: "rgb( 72, 61, 139)",
        darkslategray: "rgb( 47, 79, 79)",
        darkslategrey: "rgb( 47, 79, 79)",
        darkturquoise: "rgb( 0, 206, 209)",
        darkviolet: "rgb(148, 0, 211)",
        deeppink: "rgb(255, 20, 147)",
        deepskyblue: "rgb( 0, 191, 255)",
        dimgray: "rgb(105, 105, 105)",
        dimgrey: "rgb(105, 105, 105)",
        dodgerblue: "rgb( 30, 144, 255)",
        firebrick: "rgb(178, 34, 34)",
        floralwhite: "rgb(255, 250, 240)",
        forestgreen: "rgb( 34, 139, 34)",
        fuchsia: "rgb(255, 0, 255)",
        gainsboro: "rgb(220, 220, 220)",
        ghostwhite: "rgb(248, 248, 255)",
        gold: "rgb(255, 215, 0)",
        goldenrod: "rgb(218, 165, 32)",
        gray: "rgb(128, 128, 128)",
        grey: "rgb(128, 128, 128)",
        green: "rgb( 0, 128, 0)",
        greenyellow: "rgb(173, 255, 47)",
        honeydew: "rgb(240, 255, 240)",
        hotpink: "rgb(255, 105, 180)",
        indianred: "rgb(205, 92, 92)",
        indigo: "rgb( 75, 0, 130)",
        ivory: "rgb(255, 255, 240)",
        khaki: "rgb(240, 230, 140)",
        lavender: "rgb(230, 230, 250)",
        lavenderblush: "rgb(255, 240, 245)",
        lawngreen: "rgb(124, 252, 0)",
        lemonchiffon: "rgb(255, 250, 205)",
        lightblue: "rgb(173, 216, 230)",
        lightcoral: "rgb(240, 128, 128)",
        lightcyan: "rgb(224, 255, 255)",
        lightgoldenrodyellow: "rgb(250, 250, 210)",
        lightgray: "rgb(211, 211, 211)",
        lightgreen: "rgb(144, 238, 144)",
        lightgrey: "rgb(211, 211, 211)",
        lightpink: "rgb(255, 182, 193)",
        lightsalmon: "rgb(255, 160, 122)",
        lightseagreen: "rgb( 32, 178, 170)",
        lightskyblue: "rgb(135, 206, 250)",
        lightslategray: "rgb(119, 136, 153)",
        lightslategrey: "rgb(119, 136, 153)",
        lightsteelblue: "rgb(176, 196, 222)",
        lightyellow: "rgb(255, 255, 224)",
        lime: "rgb( 0, 255, 0)",
        limegreen: "rgb( 50, 205, 50)",
        linen: "rgb(250, 240, 230)",
        magenta: "rgb(255, 0, 255)",
        maroon: "rgb(128, 0, 0)",
        mediumaquamarine: "rgb(102, 205, 170)",
        mediumblue: "rgb( 0, 0, 205)",
        mediumorchid: "rgb(186, 85, 211)",
        mediumpurple: "rgb(147, 112, 219)",
        mediumseagreen: "rgb( 60, 179, 113)",
        mediumslateblue: "rgb(123, 104, 238)",
        mediumspringgreen: "rgb( 0, 250, 154)",
        mediumturquoise: "rgb( 72, 209, 204)",
        mediumvioletred: "rgb(199, 21, 133)",
        midnightblue: "rgb( 25, 25, 112)",
        mintcream: "rgb(245, 255, 250)",
        mistyrose: "rgb(255, 228, 225)",
        moccasin: "rgb(255, 228, 181)",
        navajowhite: "rgb(255, 222, 173)",
        navy: "rgb( 0, 0, 128)",
        oldlace: "rgb(253, 245, 230)",
        olive: "rgb(128, 128, 0)",
        olivedrab: "rgb(107, 142, 35)",
        orange: "rgb(255, 165, 0)",
        orangered: "rgb(255, 69, 0)",
        orchid: "rgb(218, 112, 214)",
        palegoldenrod: "rgb(238, 232, 170)",
        palegreen: "rgb(152, 251, 152)",
        paleturquoise: "rgb(175, 238, 238)",
        palevioletred: "rgb(219, 112, 147)",
        papayawhip: "rgb(255, 239, 213)",
        peachpuff: "rgb(255, 218, 185)",
        peru: "rgb(205, 133, 63)",
        pink: "rgb(255, 192, 203)",
        plum: "rgb(221, 160, 221)",
        powderblue: "rgb(176, 224, 230)",
        purple: "rgb(128, 0, 128)",
        red: "rgb(255, 0, 0)",
        rosybrown: "rgb(188, 143, 143)",
        royalblue: "rgb( 65, 105, 225)",
        saddlebrown: "rgb(139, 69, 19)",
        salmon: "rgb(250, 128, 114)",
        sandybrown: "rgb(244, 164, 96)",
        seagreen: "rgb( 46, 139, 87)",
        seashell: "rgb(255, 245, 238)",
        sienna: "rgb(160, 82, 45)",
        silver: "rgb(192, 192, 192)",
        skyblue: "rgb(135, 206, 235)",
        slateblue: "rgb(106, 90, 205)",
        slategray: "rgb(112, 128, 144)",
        slategrey: "rgb(112, 128, 144)",
        snow: "rgb(255, 250, 250)",
        springgreen: "rgb( 0, 255, 127)",
        steelblue: "rgb( 70, 130, 180)",
        tan: "rgb(210, 180, 140)",
        teal: "rgb( 0, 128, 128)",
        thistle: "rgb(216, 191, 216)",
        tomato: "rgb(255, 99, 71)",
        turquoise: "rgb( 64, 224, 208)",
        violet: "rgb(238, 130, 238)",
        wheat: "rgb(245, 222, 179)",
        white: "rgb(255, 255, 255)",
        whitesmoke: "rgb(245, 245, 245)",
        yellow: "rgb(255, 255, 0)",
        yellowgreen: "rgb(154, 205, 50)"
    };
    L.jqplot.AxisLabelRenderer = function (ah) {
        L.jqplot.ElemContainer.call(this);
        this.axis;
        this.show = true;
        this.label = "";
        this.fontFamily = null;
        this.fontSize = null;
        this.textColor = null;
        this._elem;
        this.escapeHTML = false;
        L.extend(true, this, ah)
    };
    L.jqplot.AxisLabelRenderer.prototype = new L.jqplot.ElemContainer();
    L.jqplot.AxisLabelRenderer.prototype.constructor = L.jqplot.AxisLabelRenderer;
    L.jqplot.AxisLabelRenderer.prototype.init = function (ah) {
        L.extend(true, this, ah)
    };
    L.jqplot.AxisLabelRenderer.prototype.draw = function (ah, ai) {
        if (this._elem)
        {
            this._elem.emptyForce();
            this._elem = null
        }
        this._elem = L('<div style="position:absolute;" class="jqplot-'
            + this.axis + '-label"></div>');
        if (Number(this.label))
        {
            this._elem.css("white-space", "nowrap")
        }
        if (!this.escapeHTML)
        {
            this._elem.html(this.label)
        }
        else
        {
            this._elem.text(this.label)
        }
        if (this.fontFamily)
        {
            this._elem.css("font-family", this.fontFamily)
        }
        if (this.fontSize)
        {
            this._elem.css("font-size", this.fontSize)
        }
        if (this.textColor)
        {
            this._elem.css("color", this.textColor)
        }
        return this._elem
    };
    L.jqplot.AxisLabelRenderer.prototype.pack = function () {
    };
    L.jqplot.AxisTickRenderer = function (ah) {
        L.jqplot.ElemContainer.call(this);
        this.mark = "outside";
        this.axis;
        this.showMark = true;
        this.showGridline = true;
        this.isMinorTick = false;
        this.size = 4;
        this.markSize = 6;
        this.show = true;
        this.showLabel = true;
        this.label = null;
        this.value = null;
        this._styles = {};
        this.formatter = L.jqplot.DefaultTickFormatter;
        this.prefix = "";
        this.suffix = "";
        this.formatString = "";
        this.fontFamily;
        this.fontSize;
        this.textColor;
        this.escapeHTML = false;
        this._elem;
        this._breakTick = false;
        L.extend(true, this, ah)
    };
    L.jqplot.AxisTickRenderer.prototype.init = function (ah) {
        L.extend(true, this, ah)
    };
    L.jqplot.AxisTickRenderer.prototype = new L.jqplot.ElemContainer();
    L.jqplot.AxisTickRenderer.prototype.constructor = L.jqplot.AxisTickRenderer;
    L.jqplot.AxisTickRenderer.prototype.setTick = function (ah, aj, ai) {
        this.value = ah;
        this.axis = aj;
        if (ai)
        {
            this.isMinorTick = true
        }
        return this
    };
    L.jqplot.AxisTickRenderer.prototype.draw = function () {
        if (this.label === null)
        {
            this.label = this.prefix
                + this.formatter(this.formatString, this.value)
                + this.suffix
        }
        var ai = {
            position: "absolute"
        };
        if (Number(this.label))
        {
            ai.whitSpace = "nowrap"
        }
        if (this._elem)
        {
            this._elem.emptyForce();
            this._elem = null
        }
        this._elem = L(document.createElement("div"));
        this._elem.addClass("jqplot-" + this.axis + "-tick");
        if (!this.escapeHTML)
        {
            this._elem.html(this.label)
        }
        else
        {
            this._elem.text(this.label)
        }
        this._elem.css(ai);
        for (var ah in this._styles)
        {
            this._elem.css(ah, this._styles[ah])
        }
        if (this.fontFamily)
        {
            this._elem.css("font-family", this.fontFamily)
        }
        if (this.fontSize)
        {
            this._elem.css("font-size", this.fontSize)
        }
        if (this.textColor)
        {
            this._elem.css("color", this.textColor)
        }
        if (this._breakTick)
        {
            this._elem.addClass("jqplot-breakTick")
        }
        return this._elem
    };
    L.jqplot.DefaultTickFormatter = function (ah, ai) {
        if (typeof ai == "number")
        {
            if (!ah)
            {
                ah = L.jqplot.config.defaultTickFormatString
            }
            return L.jqplot.sprintf(ah, ai)
        }
        else
        {
            return String(ai)
        }
    };
    L.jqplot.PercentTickFormatter = function (ah, ai) {
        if (typeof ai == "number")
        {
            ai = 100 * ai;
            if (!ah)
            {
                ah = L.jqplot.config.defaultTickFormatString
            }
            return L.jqplot.sprintf(ah, ai)
        }
        else
        {
            return String(ai)
        }
    };
    L.jqplot.AxisTickRenderer.prototype.pack = function () {
    };
    L.jqplot.CanvasGridRenderer = function () {
        this.shadowRenderer = new L.jqplot.ShadowRenderer()
    };
    L.jqplot.CanvasGridRenderer.prototype.init = function (ai) {
        this._ctx;
        L.extend(true, this, ai);
        var ah = {
            lineJoin: "miter",
            lineCap: "round",
            fill: false,
            isarc: false,
            angle: this.shadowAngle,
            offset: this.shadowOffset,
            alpha: this.shadowAlpha,
            depth: this.shadowDepth,
            lineWidth: this.shadowWidth,
            closePath: false,
            strokeStyle: this.shadowColor
        };
        this.renderer.shadowRenderer.init(ah)
    };
    L.jqplot.CanvasGridRenderer.prototype.createElement = function (ak) {
        var aj;
        if (this._elem)
        {
            if (L.jqplot.use_excanvas
                && window.G_vmlCanvasManager.uninitElement !== u)
            {
                aj = this._elem.get(0);
                window.G_vmlCanvasManager.uninitElement(aj);
                aj = null
            }
            this._elem.emptyForce();
            this._elem = null
        }
        aj = ak.canvasManager.getCanvas();
        var ah = this._plotDimensions.width;
        var ai = this._plotDimensions.height;
        aj.width = ah;
        aj.height = ai;
        this._elem = L(aj);
        this._elem.addClass("jqplot-grid-canvas");
        this._elem.css({
            position: "absolute",
            left: 0,
            top: 0
        });
        aj = ak.canvasManager.initCanvas(aj);
        this._top = this._offsets.top;
        this._bottom = ai - this._offsets.bottom;
        this._left = this._offsets.left;
        this._right = ah - this._offsets.right;
        this._width = this._right - this._left;
        this._height = this._bottom - this._top;
        aj = null;
        return this._elem
    };
    L.jqplot.CanvasGridRenderer.prototype.draw = function () {
        this._ctx = this._elem.get(0).getContext("2d");
        var at = this._ctx;
        var aw = this._axes;
        at.save();
        at.clearRect(0, 0, this._plotDimensions.width,
            this._plotDimensions.height);
        at.fillStyle = this.backgroundColor || this.background;
        at.fillRect(this._left, this._top, this._width, this._height);
        at.save();
        at.lineJoin = "miter";
        at.lineCap = "butt";
        at.lineWidth = this.gridLineWidth;
        at.strokeStyle = this.gridLineColor;
        var aA, az, ap, aq;
        var am = ["xaxis", "yaxis", "x2axis", "y2axis"];
        for (var ay = 4; ay > 0; ay--)
        {
            var aD = am[ay - 1];
            var ah = aw[aD];
            var aB = ah._ticks;
            var ar = aB.length;
            if (ah.show)
            {
                if (ah.drawBaseline)
                {
                    var aC = {};
                    if (ah.baselineWidth !== null)
                    {
                        aC.lineWidth = ah.baselineWidth
                    }
                    if (ah.baselineColor !== null)
                    {
                        aC.strokeStyle = ah.baselineColor
                    }
                    switch (aD)
                    {
                        case "xaxis":
                            ao(this._left, this._bottom, this._right, this._bottom,
                                aC);
                            break;
                        case "yaxis":
                            ao(this._left, this._bottom, this._left, this._top, aC);
                            break;
                        case "x2axis":
                            ao(this._left, this._bottom, this._right, this._bottom,
                                aC);
                            break;
                        case "y2axis":
                            ao(this._right, this._bottom, this._right, this._top,
                                aC);
                            break
                    }
                }
                for (var au = ar; au > 0; au--)
                {
                    var an = aB[au - 1];
                    if (an.show)
                    {
                        var ak = Math.round(ah.u2p(an.value)) + 0.5;
                        switch (aD)
                        {
                            case "xaxis":
                                if (an.showGridline
                                    && this.drawGridlines
                                    && ((!an.isMinorTick && ah.drawMajorGridlines) || (an.isMinorTick && ah.drawMinorGridlines)))
                                {
                                    ao(ak, this._top, ak, this._bottom)
                                }
                                if (an.showMark
                                    && an.mark
                                    && ((!an.isMinorTick && ah.drawMajorTickMarks) || (an.isMinorTick && ah.drawMinorTickMarks)))
                                {
                                    ap = an.markSize;
                                    aq = an.mark;
                                    var ak = Math.round(ah.u2p(an.value)) + 0.5;
                                    switch (aq)
                                    {
                                        case "outside":
                                            aA = this._bottom;
                                            az = this._bottom + ap;
                                            break;
                                        case "inside":
                                            aA = this._bottom - ap;
                                            az = this._bottom;
                                            break;
                                        case "cross":
                                            aA = this._bottom - ap;
                                            az = this._bottom + ap;
                                            break;
                                        default:
                                            aA = this._bottom;
                                            az = this._bottom + ap;
                                            break
                                    }
                                    if (this.shadow)
                                    {
                                        this.renderer.shadowRenderer.draw(at, [
                                            [ak, aA], [ak, az]], {
                                            lineCap: "butt",
                                            lineWidth: this.gridLineWidth,
                                            offset: this.gridLineWidth * 0.75,
                                            depth: 2,
                                            fill: false,
                                            closePath: false
                                        })
                                    }
                                    ao(ak, aA, ak, az)
                                }
                                break;
                            case "yaxis":
                                if (an.showGridline
                                    && this.drawGridlines
                                    && ((!an.isMinorTick && ah.drawMajorGridlines) || (an.isMinorTick && ah.drawMinorGridlines)))
                                {
                                    ao(this._right, ak, this._left, ak)
                                }
                                if (an.showMark
                                    && an.mark
                                    && ((!an.isMinorTick && ah.drawMajorTickMarks) || (an.isMinorTick && ah.drawMinorTickMarks)))
                                {
                                    ap = an.markSize;
                                    aq = an.mark;
                                    var ak = Math.round(ah.u2p(an.value)) + 0.5;
                                    switch (aq)
                                    {
                                        case "outside":
                                            aA = this._left - ap;
                                            az = this._left;
                                            break;
                                        case "inside":
                                            aA = this._left;
                                            az = this._left + ap;
                                            break;
                                        case "cross":
                                            aA = this._left - ap;
                                            az = this._left + ap;
                                            break;
                                        default:
                                            aA = this._left - ap;
                                            az = this._left;
                                            break
                                    }
                                    if (this.shadow)
                                    {
                                        this.renderer.shadowRenderer.draw(at, [
                                            [aA, ak], [az, ak]], {
                                            lineCap: "butt",
                                            lineWidth: this.gridLineWidth * 1.5,
                                            offset: this.gridLineWidth * 0.75,
                                            fill: false,
                                            closePath: false
                                        })
                                    }
                                    ao(aA, ak, az, ak, {
                                        strokeStyle: ah.borderColor
                                    })
                                }
                                break;
                            case "x2axis":
                                if (an.showGridline
                                    && this.drawGridlines
                                    && ((!an.isMinorTick && ah.drawMajorGridlines) || (an.isMinorTick && ah.drawMinorGridlines)))
                                {
                                    ao(ak, this._bottom, ak, this._top)
                                }
                                if (an.showMark
                                    && an.mark
                                    && ((!an.isMinorTick && ah.drawMajorTickMarks) || (an.isMinorTick && ah.drawMinorTickMarks)))
                                {
                                    ap = an.markSize;
                                    aq = an.mark;
                                    var ak = Math.round(ah.u2p(an.value)) + 0.5;
                                    switch (aq)
                                    {
                                        case "outside":
                                            aA = this._top - ap;
                                            az = this._top;
                                            break;
                                        case "inside":
                                            aA = this._top;
                                            az = this._top + ap;
                                            break;
                                        case "cross":
                                            aA = this._top - ap;
                                            az = this._top + ap;
                                            break;
                                        default:
                                            aA = this._top - ap;
                                            az = this._top;
                                            break
                                    }
                                    if (this.shadow)
                                    {
                                        this.renderer.shadowRenderer.draw(at, [
                                            [ak, aA], [ak, az]], {
                                            lineCap: "butt",
                                            lineWidth: this.gridLineWidth,
                                            offset: this.gridLineWidth * 0.75,
                                            depth: 2,
                                            fill: false,
                                            closePath: false
                                        })
                                    }
                                    ao(ak, aA, ak, az)
                                }
                                break;
                            case "y2axis":
                                if (an.showGridline
                                    && this.drawGridlines
                                    && ((!an.isMinorTick && ah.drawMajorGridlines) || (an.isMinorTick && ah.drawMinorGridlines)))
                                {
                                    ao(this._left, ak, this._right, ak)
                                }
                                if (an.showMark
                                    && an.mark
                                    && ((!an.isMinorTick && ah.drawMajorTickMarks) || (an.isMinorTick && ah.drawMinorTickMarks)))
                                {
                                    ap = an.markSize;
                                    aq = an.mark;
                                    var ak = Math.round(ah.u2p(an.value)) + 0.5;
                                    switch (aq)
                                    {
                                        case "outside":
                                            aA = this._right;
                                            az = this._right + ap;
                                            break;
                                        case "inside":
                                            aA = this._right - ap;
                                            az = this._right;
                                            break;
                                        case "cross":
                                            aA = this._right - ap;
                                            az = this._right + ap;
                                            break;
                                        default:
                                            aA = this._right;
                                            az = this._right + ap;
                                            break
                                    }
                                    if (this.shadow)
                                    {
                                        this.renderer.shadowRenderer.draw(at, [
                                            [aA, ak], [az, ak]], {
                                            lineCap: "butt",
                                            lineWidth: this.gridLineWidth * 1.5,
                                            offset: this.gridLineWidth * 0.75,
                                            fill: false,
                                            closePath: false
                                        })
                                    }
                                    ao(aA, ak, az, ak, {
                                        strokeStyle: ah.borderColor
                                    })
                                }
                                break;
                            default:
                                break
                        }
                    }
                }
                an = null
            }
            ah = null;
            aB = null
        }
        am = ["y3axis", "y4axis", "y5axis", "y6axis", "y7axis", "y8axis",
            "y9axis", "yMidAxis"];
        for (var ay = 7; ay > 0; ay--)
        {
            var ah = aw[am[ay - 1]];
            var aB = ah._ticks;
            if (ah.show)
            {
                var ai = aB[ah.numberTicks - 1];
                var al = aB[0];
                var aj = ah.getLeft();
                var av = [[aj, ai.getTop() + ai.getHeight() / 2],
                    [aj, al.getTop() + al.getHeight() / 2 + 1]];
                if (this.shadow)
                {
                    this.renderer.shadowRenderer.draw(at, av, {
                        lineCap: "butt",
                        fill: false,
                        closePath: false
                    })
                }
                ao(av[0][0], av[0][1], av[1][0], av[1][1], {
                    lineCap: "butt",
                    strokeStyle: ah.borderColor,
                    lineWidth: ah.borderWidth
                });
                for (var au = aB.length; au > 0; au--)
                {
                    var an = aB[au - 1];
                    ap = an.markSize;
                    aq = an.mark;
                    var ak = Math.round(ah.u2p(an.value)) + 0.5;
                    if (an.showMark && an.mark)
                    {
                        switch (aq)
                        {
                            case "outside":
                                aA = aj;
                                az = aj + ap;
                                break;
                            case "inside":
                                aA = aj - ap;
                                az = aj;
                                break;
                            case "cross":
                                aA = aj - ap;
                                az = aj + ap;
                                break;
                            default:
                                aA = aj;
                                az = aj + ap;
                                break
                        }
                        av = [[aA, ak], [az, ak]];
                        if (this.shadow)
                        {
                            this.renderer.shadowRenderer.draw(at, av, {
                                lineCap: "butt",
                                lineWidth: this.gridLineWidth * 1.5,
                                offset: this.gridLineWidth * 0.75,
                                fill: false,
                                closePath: false
                            })
                        }
                        ao(aA, ak, az, ak, {
                            strokeStyle: ah.borderColor
                        })
                    }
                    an = null
                }
                al = null
            }
            ah = null;
            aB = null
        }
        at.restore();

        function ao(aH, aG, aE, ax, aF)
        {
            at.save();
            aF = aF || {};
            if (aF.lineWidth == null || aF.lineWidth != 0)
            {
                L.extend(true, at, aF);
                at.beginPath();
                at.moveTo(aH, aG);
                at.lineTo(aE, ax);
                at.stroke();
                at.restore()
            }
        }

        if (this.shadow)
        {
            var av = [[this._left, this._bottom],
                [this._right, this._bottom], [this._right, this._top]];
            this.renderer.shadowRenderer.draw(at, av)
        }
        if (this.borderWidth != 0 && this.drawBorder)
        {
            ao(this._left, this._top, this._right, this._top, {
                lineCap: "round",
                strokeStyle: aw.x2axis.borderColor,
                lineWidth: aw.x2axis.borderWidth
            });
            ao(this._right, this._top, this._right, this._bottom, {
                lineCap: "round",
                strokeStyle: aw.y2axis.borderColor,
                lineWidth: aw.y2axis.borderWidth
            });
            ao(this._right, this._bottom, this._left, this._bottom, {
                lineCap: "round",
                strokeStyle: aw.xaxis.borderColor,
                lineWidth: aw.xaxis.borderWidth
            });
            ao(this._left, this._bottom, this._left, this._top, {
                lineCap: "round",
                strokeStyle: aw.yaxis.borderColor,
                lineWidth: aw.yaxis.borderWidth
            })
        }
        at.restore();
        at = null;
        aw = null
    };
    L.jqplot.DivTitleRenderer = function () {
    };
    L.jqplot.DivTitleRenderer.prototype.init = function (ah) {
        L.extend(true, this, ah)
    };
    L.jqplot.DivTitleRenderer.prototype.draw = function () {
        if (this._elem)
        {
            this._elem.emptyForce();
            this._elem = null
        }
        var ak = this.renderer;
        var aj = document.createElement("div");
        this._elem = L(aj);
        this._elem.addClass("jqplot-title");
        if (!this.text)
        {
            this.show = false;
            this._elem.height(0);
            this._elem.width(0)
        }
        else
        {
            if (this.text)
            {
                var ah;
                if (this.color)
                {
                    ah = this.color
                }
                else
                {
                    if (this.textColor)
                    {
                        ah = this.textColor
                    }
                }
                var ai = {
                    position: "absolute",
                    top: "0px",
                    left: "0px"
                };
                if (this._plotWidth)
                {
                    ai.width = this._plotWidth + "px"
                }
                if (this.fontSize)
                {
                    ai.fontSize = this.fontSize
                }
                if (typeof this.textAlign === "string")
                {
                    ai.textAlign = this.textAlign
                }
                else
                {
                    ai.textAlign = "center"
                }
                if (ah)
                {
                    ai.color = ah
                }
                if (this.paddingBottom)
                {
                    ai.paddingBottom = this.paddingBottom
                }
                if (this.fontFamily)
                {
                    ai.fontFamily = this.fontFamily
                }
                this._elem.css(ai);
                if (this.escapeHtml)
                {
                    this._elem.text(this.text)
                }
                else
                {
                    this._elem.html(this.text)
                }
            }
        }
        aj = null;
        return this._elem
    };
    L.jqplot.DivTitleRenderer.prototype.pack = function () {
    };
    var r = 0.1;
    L.jqplot.LinePattern = function (aw, aq) {
        var ap = {
            dotted: [r, L.jqplot.config.dotGapLength],
            dashed: [L.jqplot.config.dashLength, L.jqplot.config.gapLength],
            solid: null
        };
        if (typeof aq === "string")
        {
            if (aq[0] === "." || aq[0] === "-")
            {
                var ax = aq;
                aq = [];
                for (var ao = 0, al = ax.length; ao < al; ao++)
                {
                    if (ax[ao] === ".")
                    {
                        aq.push(r)
                    }
                    else
                    {
                        if (ax[ao] === "-")
                        {
                            aq.push(L.jqplot.config.dashLength)
                        }
                        else
                        {
                            continue
                        }
                    }
                    aq.push(L.jqplot.config.gapLength)
                }
            }
            else
            {
                aq = ap[aq]
            }
        }
        if (!(aq && aq.length))
        {
            return aw
        }
        var ak = 0;
        var ar = aq[0];
        var au = 0;
        var at = 0;
        var an = 0;
        var ah = 0;
        var av = function (ay, az) {
            aw.moveTo(ay, az);
            au = ay;
            at = az;
            an = ay;
            ah = az
        };
        var aj = function (ay, aE) {
            var aC = aw.lineWidth;
            var aA = ay - au;
            var az = aE - at;
            var aB = Math.sqrt(aA * aA + az * az);
            if ((aB > 0) && (aC > 0))
            {
                aA /= aB;
                az /= aB;
                while (true)
                {
                    var aD = aC * ar;
                    if (aD < aB)
                    {
                        au += aD * aA;
                        at += aD * az;
                        if ((ak & 1) == 0)
                        {
                            aw.lineTo(au, at)
                        }
                        else
                        {
                            aw.moveTo(au, at)
                        }
                        aB -= aD;
                        ak++;
                        if (ak >= aq.length)
                        {
                            ak = 0
                        }
                        ar = aq[ak]
                    }
                    else
                    {
                        au = ay;
                        at = aE;
                        if ((ak & 1) == 0)
                        {
                            aw.lineTo(au, at)
                        }
                        else
                        {
                            aw.moveTo(au, at)
                        }
                        ar -= aB / aC;
                        break
                    }
                }
            }
        };
        var ai = function () {
            aw.beginPath()
        };
        var am = function () {
            aj(an, ah)
        };
        return {
            moveTo: av,
            lineTo: aj,
            beginPath: ai,
            closePath: am
        }
    };
    L.jqplot.LineRenderer = function () {
        this.shapeRenderer = new L.jqplot.ShapeRenderer();
        this.shadowRenderer = new L.jqplot.ShadowRenderer()
    };
    L.jqplot.LineRenderer.prototype.init = function (ai, an) {
        ai = ai || {};
        this._type = "line";
        this.renderer.animation = {
            show: false,
            direction: "left",
            speed: 2500,
            _supported: true
        };
        this.renderer.smooth = false;
        this.renderer.tension = null;
        this.renderer.constrainSmoothing = true;
        this.renderer._smoothedData = [];
        this.renderer._smoothedPlotData = [];
        this.renderer._hiBandGridData = [];
        this.renderer._lowBandGridData = [];
        this.renderer._hiBandSmoothedData = [];
        this.renderer._lowBandSmoothedData = [];
        this.renderer.bandData = [];
        this.renderer.bands = {
            show: false,
            hiData: [],
            lowData: [],
            color: this.color,
            showLines: false,
            fill: true,
            fillColor: null,
            _min: null,
            _max: null,
            interval: "3%"
        };
        var al = {
            highlightMouseOver: ai.highlightMouseOver,
            highlightMouseDown: ai.highlightMouseDown,
            highlightColor: ai.highlightColor
        };
        delete (ai.highlightMouseOver);
        delete (ai.highlightMouseDown);
        delete (ai.highlightColor);
        L.extend(true, this.renderer, ai);
        this.renderer.options = ai;
        if (this.renderer.bandData.length > 1
            && (!ai.bands || ai.bands.show == null))
        {
            this.renderer.bands.show = true
        }
        else
        {
            if (ai.bands && ai.bands.show == null && ai.bands.interval != null)
            {
                this.renderer.bands.show = true
            }
        }
        if (this.fill)
        {
            this.renderer.bands.show = false
        }
        if (this.renderer.bands.show)
        {
            this.renderer.initBands.call(this, this.renderer.options, an)
        }
        if (this._stack)
        {
            this.renderer.smooth = false
        }
        var am = {
            lineJoin: this.lineJoin,
            lineCap: this.lineCap,
            fill: this.fill,
            isarc: false,
            strokeStyle: this.color,
            fillStyle: this.fillColor,
            lineWidth: this.lineWidth,
            linePattern: this.linePattern,
            closePath: this.fill
        };
        this.renderer.shapeRenderer.init(am);
        var aj = ai.shadowOffset;
        if (aj == null)
        {
            if (this.lineWidth > 2.5)
            {
                aj = 1.25 * (1 + (Math.atan((this.lineWidth / 2.5)) / 0.785398163 - 1) * 0.6)
            }
            else
            {
                aj = 1.25 * Math.atan((this.lineWidth / 2.5)) / 0.785398163
            }
        }
        var ah = {
            lineJoin: this.lineJoin,
            lineCap: this.lineCap,
            fill: this.fill,
            isarc: false,
            angle: this.shadowAngle,
            offset: aj,
            alpha: this.shadowAlpha,
            depth: this.shadowDepth,
            lineWidth: this.lineWidth,
            linePattern: this.linePattern,
            closePath: this.fill
        };
        this.renderer.shadowRenderer.init(ah);
        this._areaPoints = [];
        this._boundingBox = [[], []];
        if (!this.isTrendline && this.fill || this.renderer.bands.show)
        {
            this.highlightMouseOver = true;
            this.highlightMouseDown = false;
            this.highlightColor = null;
            if (al.highlightMouseDown && al.highlightMouseOver == null)
            {
                al.highlightMouseOver = false
            }
            L.extend(true, this, {
                highlightMouseOver: al.highlightMouseOver,
                highlightMouseDown: al.highlightMouseDown,
                highlightColor: al.highlightColor
            });
            if (!this.highlightColor)
            {
                var ak = (this.renderer.bands.show) ? this.renderer.bands.fillColor
                    : this.fillColor;
                this.highlightColor = L.jqplot.computeHighlightColors(ak)
            }
            if (this.highlighter)
            {
                this.highlighter.show = false
            }
        }
        if (!this.isTrendline && an)
        {
            an.plugins.lineRenderer = {};
            an.postInitHooks.addOnce(z);
            an.postDrawHooks.addOnce(af);
            an.eventListenerHooks.addOnce("jqplotMouseMove", h);
            an.eventListenerHooks.addOnce("jqplotMouseDown", e);
            an.eventListenerHooks.addOnce("jqplotMouseUp", ad);
            an.eventListenerHooks.addOnce("jqplotClick", g);
            an.eventListenerHooks.addOnce("jqplotRightClick", s)
        }
    };
    L.jqplot.LineRenderer.prototype.initBands = function (ak, av) {
        var al = ak.bandData || [];
        var an = this.renderer.bands;
        an.hiData = [];
        an.lowData = [];
        var aB = this.data;
        an._max = null;
        an._min = null;
        if (al.length == 2)
        {
            if (L.isArray(al[0][0]))
            {
                var ao;
                var ah = 0, ar = 0;
                for (var aw = 0, at = al[0].length; aw < at; aw++)
                {
                    ao = al[0][aw];
                    if ((ao[1] != null && ao[1] > an._max) || an._max == null)
                    {
                        an._max = ao[1]
                    }
                    if ((ao[1] != null && ao[1] < an._min) || an._min == null)
                    {
                        an._min = ao[1]
                    }
                }
                for (var aw = 0, at = al[1].length; aw < at; aw++)
                {
                    ao = al[1][aw];
                    if ((ao[1] != null && ao[1] > an._max) || an._max == null)
                    {
                        an._max = ao[1];
                        ar = 1
                    }
                    if ((ao[1] != null && ao[1] < an._min) || an._min == null)
                    {
                        an._min = ao[1];
                        ah = 1
                    }
                }
                if (ar === ah)
                {
                    an.show = false
                }
                an.hiData = al[ar];
                an.lowData = al[ah]
            }
            else
            {
                if (al[0].length === aB.length && al[1].length === aB.length)
                {
                    var aj = (al[0][0] > al[1][0]) ? 0 : 1;
                    var aC = (aj) ? 0 : 1;
                    for (var aw = 0, at = aB.length; aw < at; aw++)
                    {
                        an.hiData.push([aB[aw][0], al[aj][aw]]);
                        an.lowData.push([aB[aw][0], al[aC][aw]])
                    }
                }
                else
                {
                    an.show = false
                }
            }
        }
        else
        {
            if (al.length > 2 && !L.isArray(al[0][0]))
            {
                var aj = (al[0][0] > al[0][1]) ? 0 : 1;
                var aC = (aj) ? 0 : 1;
                for (var aw = 0, at = al.length; aw < at; aw++)
                {
                    an.hiData.push([aB[aw][0], al[aw][aj]]);
                    an.lowData.push([aB[aw][0], al[aw][aC]])
                }
            }
            else
            {
                var aq = an.interval;
                var aA = null;
                var az = null;
                var ai = null;
                var au = null;
                if (L.isArray(aq))
                {
                    aA = aq[0];
                    az = aq[1]
                }
                else
                {
                    aA = aq
                }
                if (isNaN(aA))
                {
                    if (aA.charAt(aA.length - 1) === "%")
                    {
                        ai = "multiply";
                        aA = parseFloat(aA) / 100 + 1
                    }
                }
                else
                {
                    aA = parseFloat(aA);
                    ai = "add"
                }
                if (az !== null && isNaN(az))
                {
                    if (az.charAt(az.length - 1) === "%")
                    {
                        au = "multiply";
                        az = parseFloat(az) / 100 + 1
                    }
                }
                else
                {
                    if (az !== null)
                    {
                        az = parseFloat(az);
                        au = "add"
                    }
                }
                if (aA !== null)
                {
                    if (az === null)
                    {
                        az = -aA;
                        au = ai;
                        if (au === "multiply")
                        {
                            az += 2
                        }
                    }
                    if (aA < az)
                    {
                        var ax = aA;
                        aA = az;
                        az = ax;
                        ax = ai;
                        ai = au;
                        au = ax
                    }
                    for (var aw = 0, at = aB.length; aw < at; aw++)
                    {
                        switch (ai)
                        {
                            case "add":
                                an.hiData.push([aB[aw][0], aB[aw][1] + aA]);
                                break;
                            case "multiply":
                                an.hiData.push([aB[aw][0], aB[aw][1] * aA]);
                                break
                        }
                        switch (au)
                        {
                            case "add":
                                an.lowData.push([aB[aw][0], aB[aw][1] + az]);
                                break;
                            case "multiply":
                                an.lowData.push([aB[aw][0], aB[aw][1] * az]);
                                break
                        }
                    }
                }
                else
                {
                    an.show = false
                }
            }
        }
        var am = an.hiData;
        var ap = an.lowData;
        for (var aw = 0, at = am.length; aw < at; aw++)
        {
            if ((am[aw][1] != null && am[aw][1] > an._max) || an._max == null)
            {
                an._max = am[aw][1]
            }
        }
        for (var aw = 0, at = ap.length; aw < at; aw++)
        {
            if ((ap[aw][1] != null && ap[aw][1] < an._min) || an._min == null)
            {
                an._min = ap[aw][1]
            }
        }
        if (an.fillColor === null)
        {
            var ay = L.jqplot.getColorComponents(an.color);
            ay[3] = ay[3] * 0.5;
            an.fillColor = "rgba(" + ay[0] + ", " + ay[1] + ", " + ay[2] + ", "
                + ay[3] + ")"
        }
    };

    function K(ai, ah)
    {
        return (3.4182054 + ah) * Math.pow(ai, -0.3534992)
    }

    function n(aj, ai)
    {
        var ah = Math.sqrt(Math.pow((ai[0] - aj[0]), 2)
            + Math.pow((ai[1] - aj[1]), 2));
        return 5.7648 * Math.log(ah) + 7.4456
    }

    function A(ah)
    {
        var ai = (Math.exp(2 * ah) - 1) / (Math.exp(2 * ah) + 1);
        return ai
    }

    function J(aJ)
    {
        var at = this.renderer.smooth;
        var aD = this.canvas.getWidth();
        var an = this._xaxis.series_p2u;
        var aG = this._yaxis.series_p2u;
        var aF = null;
        var am = null;
        var az = aJ.length / aD;
        var aj = [];
        var ay = [];
        if (!isNaN(parseFloat(at)))
        {
            aF = parseFloat(at)
        }
        else
        {
            aF = K(az, 0.5)
        }
        var aw = [];
        var ak = [];
        for (var aE = 0, aA = aJ.length; aE < aA; aE++)
        {
            aw.push(aJ[aE][1]);
            ak.push(aJ[aE][0])
        }

        function av(aK, aL)
        {
            if (aK - aL == 0)
            {
                return Math.pow(10, 10)
            }
            else
            {
                return aK - aL
            }
        }

        var ax, ar, aq, ap;
        var ah = aJ.length - 1;
        for (var al = 1, aB = aJ.length; al < aB; al++)
        {
            var ai = [];
            var au = [];
            for (var aC = 0; aC < 2; aC++)
            {
                var aE = al - 1 + aC;
                if (aE == 0 || aE == ah)
                {
                    ai[aC] = Math.pow(10, 10)
                }
                else
                {
                    if (aw[aE + 1] - aw[aE] == 0 || aw[aE] - aw[aE - 1] == 0)
                    {
                        ai[aC] = 0
                    }
                    else
                    {
                        if (((ak[aE + 1] - ak[aE]) / (aw[aE + 1] - aw[aE]) + (ak[aE] - ak[aE - 1])
                            / (aw[aE] - aw[aE - 1])) == 0)
                        {
                            ai[aC] = 0
                        }
                        else
                        {
                            if ((aw[aE + 1] - aw[aE]) * (aw[aE] - aw[aE - 1]) < 0)
                            {
                                ai[aC] = 0
                            }
                            else
                            {
                                ai[aC] = 2 / (av(ak[aE + 1], ak[aE])
                                    / (aw[aE + 1] - aw[aE]) + av(ak[aE],
                                        ak[aE - 1])
                                    / (aw[aE] - aw[aE - 1]))
                            }
                        }
                    }
                }
            }
            if (al == 1)
            {
                ai[0] = 3 / 2 * (aw[1] - aw[0]) / av(ak[1], ak[0]) - ai[1] / 2
            }
            else
            {
                if (al == ah)
                {
                    ai[1] = 3 / 2 * (aw[ah] - aw[ah - 1])
                        / av(ak[ah], ak[ah - 1]) - ai[0] / 2
                }
            }
            au[0] = -2 * (ai[1] + 2 * ai[0]) / av(ak[al], ak[al - 1]) + 6
                * (aw[al] - aw[al - 1])
                / Math.pow(av(ak[al], ak[al - 1]), 2);
            au[1] = 2 * (2 * ai[1] + ai[0]) / av(ak[al], ak[al - 1]) - 6
                * (aw[al] - aw[al - 1])
                / Math.pow(av(ak[al], ak[al - 1]), 2);
            ap = 1 / 6 * (au[1] - au[0]) / av(ak[al], ak[al - 1]);
            aq = 1 / 2 * (ak[al] * au[0] - ak[al - 1] * au[1])
                / av(ak[al], ak[al - 1]);
            ar = (aw[al] - aw[al - 1] - aq
                * (Math.pow(ak[al], 2) - Math.pow(ak[al - 1], 2)) - ap
                * (Math.pow(ak[al], 3) - Math.pow(ak[al - 1], 3)))
                / av(ak[al], ak[al - 1]);
            ax = aw[al - 1] - ar * ak[al - 1] - aq * Math.pow(ak[al - 1], 2)
                - ap * Math.pow(ak[al - 1], 3);
            var aI = (ak[al] - ak[al - 1]) / aF;
            var aH, ao;
            for (var aC = 0, aA = aF; aC < aA; aC++)
            {
                aH = [];
                ao = ak[al - 1] + aC * aI;
                aH.push(ao);
                aH.push(ax + ar * ao + aq * Math.pow(ao, 2) + ap
                    * Math.pow(ao, 3));
                aj.push(aH);
                ay.push([an(aH[0]), aG(aH[1])])
            }
        }
        aj.push(aJ[aE]);
        ay.push([an(aJ[aE][0]), aG(aJ[aE][1])]);
        return [aj, ay]
    }

    function F(ap)
    {
        var ao = this.renderer.smooth;
        var aU = this.renderer.tension;
        var ah = this.canvas.getWidth();
        var aH = this._xaxis.series_p2u;
        var aq = this._yaxis.series_p2u;
        var aI = null;
        var aJ = null;
        var aT = null;
        var aO = null;
        var aM = null;
        var at = null;
        var aR = null;
        var am = null;
        var aK, aL, aD, aC, aA, ay;
        var ak, ai, av, au;
        var aB, az, aN;
        var aw = [];
        var aj = [];
        var al = ap.length / ah;
        var aS, ax, aF, aG, aE;
        var ar = [];
        var an = [];
        if (!isNaN(parseFloat(ao)))
        {
            aI = parseFloat(ao)
        }
        else
        {
            aI = K(al, 0.5)
        }
        if (!isNaN(parseFloat(aU)))
        {
            aU = parseFloat(aU)
        }
        for (var aQ = 0, aP = ap.length - 1; aQ < aP; aQ++)
        {
            if (aU === null)
            {
                at = Math.abs((ap[aQ + 1][1] - ap[aQ][1])
                    / (ap[aQ + 1][0] - ap[aQ][0]));
                aS = 0.3;
                ax = 0.6;
                aF = (ax - aS) / 2;
                aG = 2.5;
                aE = -1.4;
                am = at / aG + aE;
                aO = aF * A(am) - aF * A(aE) + aS;
                if (aQ > 0)
                {
                    aR = Math.abs((ap[aQ][1] - ap[aQ - 1][1])
                        / (ap[aQ][0] - ap[aQ - 1][0]))
                }
                am = aR / aG + aE;
                aM = aF * A(am) - aF * A(aE) + aS;
                aT = (aO + aM) / 2
            }
            else
            {
                aT = aU
            }
            for (aK = 0; aK < aI; aK++)
            {
                aL = aK / aI;
                aD = (1 + 2 * aL) * Math.pow((1 - aL), 2);
                aC = aL * Math.pow((1 - aL), 2);
                aA = Math.pow(aL, 2) * (3 - 2 * aL);
                ay = Math.pow(aL, 2) * (aL - 1);
                if (ap[aQ - 1])
                {
                    ak = aT * (ap[aQ + 1][0] - ap[aQ - 1][0]);
                    ai = aT * (ap[aQ + 1][1] - ap[aQ - 1][1])
                }
                else
                {
                    ak = aT * (ap[aQ + 1][0] - ap[aQ][0]);
                    ai = aT * (ap[aQ + 1][1] - ap[aQ][1])
                }
                if (ap[aQ + 2])
                {
                    av = aT * (ap[aQ + 2][0] - ap[aQ][0]);
                    au = aT * (ap[aQ + 2][1] - ap[aQ][1])
                }
                else
                {
                    av = aT * (ap[aQ + 1][0] - ap[aQ][0]);
                    au = aT * (ap[aQ + 1][1] - ap[aQ][1])
                }
                aB = aD * ap[aQ][0] + aA * ap[aQ + 1][0] + aC * ak + ay * av;
                az = aD * ap[aQ][1] + aA * ap[aQ + 1][1] + aC * ai + ay * au;
                aN = [aB, az];
                ar.push(aN);
                an.push([aH(aB), aq(az)])
            }
        }
        ar.push(ap[aP]);
        an.push([aH(ap[aP][0]), aq(ap[aP][1])]);
        return [ar, an]
    }

    L.jqplot.LineRenderer.prototype.setGridData = function (ap) {
        var al = this._xaxis.series_u2p;
        var ah = this._yaxis.series_u2p;
        var am = this._plotData;
        var aq = this._prevPlotData;
        this.gridData = [];
        this._prevGridData = [];
        this.renderer._smoothedData = [];
        this.renderer._smoothedPlotData = [];
        this.renderer._hiBandGridData = [];
        this.renderer._lowBandGridData = [];
        this.renderer._hiBandSmoothedData = [];
        this.renderer._lowBandSmoothedData = [];
        var ak = this.renderer.bands;
        var ai = false;
        for (var an = 0, aj = am.length; an < aj; an++)
        {
            if (am[an][0] != null && am[an][1] != null)
            {
                this.gridData.push([al.call(this._xaxis, am[an][0]),
                    ah.call(this._yaxis, am[an][1])])
            }
            else
            {
                if (am[an][0] == null)
                {
                    ai = true;
                    this.gridData
                        .push([null, ah.call(this._yaxis, am[an][1])])
                }
                else
                {
                    if (am[an][1] == null)
                    {
                        ai = true;
                        this.gridData.push([al.call(this._xaxis, am[an][0]),
                            null])
                    }
                }
            }
            if (aq[an] != null && aq[an][0] != null && aq[an][1] != null)
            {
                this._prevGridData.push([al.call(this._xaxis, aq[an][0]),
                    ah.call(this._yaxis, aq[an][1])])
            }
            else
            {
                if (aq[an] != null && aq[an][0] == null)
                {
                    this._prevGridData.push([null,
                        ah.call(this._yaxis, aq[an][1])])
                }
                else
                {
                    if (aq[an] != null && aq[an][0] != null
                        && aq[an][1] == null)
                    {
                        this._prevGridData.push([
                            al.call(this._xaxis, aq[an][0]), null])
                    }
                }
            }
        }
        if (ai)
        {
            this.renderer.smooth = false;
            if (this._type === "line")
            {
                ak.show = false
            }
        }
        if (this._type === "line" && ak.show)
        {
            for (var an = 0, aj = ak.hiData.length; an < aj; an++)
            {
                this.renderer._hiBandGridData.push([
                    al.call(this._xaxis, ak.hiData[an][0]),
                    ah.call(this._yaxis, ak.hiData[an][1])])
            }
            for (var an = 0, aj = ak.lowData.length; an < aj; an++)
            {
                this.renderer._lowBandGridData.push([
                    al.call(this._xaxis, ak.lowData[an][0]),
                    ah.call(this._yaxis, ak.lowData[an][1])])
            }
        }
        if (this._type === "line" && this.renderer.smooth
            && this.gridData.length > 2)
        {
            var ao;
            if (this.renderer.constrainSmoothing)
            {
                ao = J.call(this, this.gridData);
                this.renderer._smoothedData = ao[0];
                this.renderer._smoothedPlotData = ao[1];
                if (ak.show)
                {
                    ao = J.call(this, this.renderer._hiBandGridData);
                    this.renderer._hiBandSmoothedData = ao[0];
                    ao = J.call(this, this.renderer._lowBandGridData);
                    this.renderer._lowBandSmoothedData = ao[0]
                }
                ao = null
            }
            else
            {
                ao = F.call(this, this.gridData);
                this.renderer._smoothedData = ao[0];
                this.renderer._smoothedPlotData = ao[1];
                if (ak.show)
                {
                    ao = F.call(this, this.renderer._hiBandGridData);
                    this.renderer._hiBandSmoothedData = ao[0];
                    ao = F.call(this, this.renderer._lowBandGridData);
                    this.renderer._lowBandSmoothedData = ao[0]
                }
                ao = null
            }
        }
    };
    L.jqplot.LineRenderer.prototype.makeGridData = function (ao, aq) {
        var am = this._xaxis.series_u2p;
        var ah = this._yaxis.series_u2p;
        var ar = [];
        var aj = [];
        this.renderer._smoothedData = [];
        this.renderer._smoothedPlotData = [];
        this.renderer._hiBandGridData = [];
        this.renderer._lowBandGridData = [];
        this.renderer._hiBandSmoothedData = [];
        this.renderer._lowBandSmoothedData = [];
        var al = this.renderer.bands;
        var ai = false;
        for (var an = 0; an < ao.length; an++)
        {
            if (ao[an][0] != null && ao[an][1] != null)
            {
                ar.push([am.call(this._xaxis, ao[an][0]),
                    ah.call(this._yaxis, ao[an][1])])
            }
            else
            {
                if (ao[an][0] == null)
                {
                    ai = true;
                    ar.push([null, ah.call(this._yaxis, ao[an][1])])
                }
                else
                {
                    if (ao[an][1] == null)
                    {
                        ai = true;
                        ar.push([am.call(this._xaxis, ao[an][0]), null])
                    }
                }
            }
        }
        if (ai)
        {
            this.renderer.smooth = false;
            if (this._type === "line")
            {
                al.show = false
            }
        }
        if (this._type === "line" && al.show)
        {
            for (var an = 0, ak = al.hiData.length; an < ak; an++)
            {
                this.renderer._hiBandGridData.push([
                    am.call(this._xaxis, al.hiData[an][0]),
                    ah.call(this._yaxis, al.hiData[an][1])])
            }
            for (var an = 0, ak = al.lowData.length; an < ak; an++)
            {
                this.renderer._lowBandGridData.push([
                    am.call(this._xaxis, al.lowData[an][0]),
                    ah.call(this._yaxis, al.lowData[an][1])])
            }
        }
        if (this._type === "line" && this.renderer.smooth && ar.length > 2)
        {
            var ap;
            if (this.renderer.constrainSmoothing)
            {
                ap = J.call(this, ar);
                this.renderer._smoothedData = ap[0];
                this.renderer._smoothedPlotData = ap[1];
                if (al.show)
                {
                    ap = J.call(this, this.renderer._hiBandGridData);
                    this.renderer._hiBandSmoothedData = ap[0];
                    ap = J.call(this, this.renderer._lowBandGridData);
                    this.renderer._lowBandSmoothedData = ap[0]
                }
                ap = null
            }
            else
            {
                ap = F.call(this, ar);
                this.renderer._smoothedData = ap[0];
                this.renderer._smoothedPlotData = ap[1];
                if (al.show)
                {
                    ap = F.call(this, this.renderer._hiBandGridData);
                    this.renderer._hiBandSmoothedData = ap[0];
                    ap = F.call(this, this.renderer._lowBandGridData);
                    this.renderer._lowBandSmoothedData = ap[0]
                }
                ap = null
            }
        }
        return ar
    };
    L.jqplot.LineRenderer.prototype.draw = function (ax, aI, ai, aB) {
        var aC;
        var aq = L.extend(true, {}, ai);
        var ak = (aq.shadow != u) ? aq.shadow : this.shadow;
        var aJ = (aq.showLine != u) ? aq.showLine : this.showLine;
        var aA = (aq.fill != u) ? aq.fill : this.fill;
        var ah = (aq.fillAndStroke != u) ? aq.fillAndStroke
            : this.fillAndStroke;
        var ar, ay, av, aE;
        ax.save();
        if (aI.length)
        {
            if (aJ)
            {
                if (aA)
                {
                    if (this.fillToZero)
                    {
                        var aF = this.negativeColor;
                        if (!this.useNegativeColors)
                        {
                            aF = aq.fillStyle
                        }
                        var ao = false;
                        var ap = aq.fillStyle;
                        if (ah)
                        {
                            var aH = aI.slice(0)
                        }
                        if (this.index == 0 || !this._stack)
                        {
                            var aw = [];
                            var aL = (this.renderer.smooth) ? this.renderer._smoothedPlotData
                                : this._plotData;
                            this._areaPoints = [];
                            var aG = this._yaxis.series_u2p(this.fillToValue);
                            var aj = this._xaxis.series_u2p(this.fillToValue);
                            aq.closePath = true;
                            if (this.fillAxis == "y")
                            {
                                aw.push([aI[0][0], aG]);
                                this._areaPoints.push([aI[0][0], aG]);
                                for (var aC = 0; aC < aI.length - 1; aC++)
                                {
                                    aw.push(aI[aC]);
                                    this._areaPoints.push(aI[aC]);
                                    if (aL[aC][1] * aL[aC + 1][1] <= 0)
                                    {
                                        if (aL[aC][1] < 0)
                                        {
                                            ao = true;
                                            aq.fillStyle = aF
                                        }
                                        else
                                        {
                                            ao = false;
                                            aq.fillStyle = ap
                                        }
                                        var an = aI[aC][0]
                                            + (aI[aC + 1][0] - aI[aC][0])
                                            * (aG - aI[aC][1])
                                            / (aI[aC + 1][1] - aI[aC][1]);
                                        aw.push([an, aG]);
                                        this._areaPoints.push([an, aG]);
                                        if (ak)
                                        {
                                            this.renderer.shadowRenderer.draw(
                                                ax, aw, aq)
                                        }
                                        this.renderer.shapeRenderer.draw(ax,
                                            aw, aq);
                                        aw = [[an, aG]]
                                    }
                                }
                                if (aL[aI.length - 1][1] < 0)
                                {
                                    ao = true;
                                    aq.fillStyle = aF
                                }
                                else
                                {
                                    ao = false;
                                    aq.fillStyle = ap
                                }
                                aw.push(aI[aI.length - 1]);
                                this._areaPoints.push(aI[aI.length - 1]);
                                aw.push([aI[aI.length - 1][0], aG]);
                                this._areaPoints.push([aI[aI.length - 1][0],
                                    aG])
                            }
                            if (ak)
                            {
                                this.renderer.shadowRenderer.draw(ax, aw, aq)
                            }
                            this.renderer.shapeRenderer.draw(ax, aw, aq)
                        }
                        else
                        {
                            var au = this._prevGridData;
                            for (var aC = au.length; aC > 0; aC--)
                            {
                                aI.push(au[aC - 1])
                            }
                            if (ak)
                            {
                                this.renderer.shadowRenderer.draw(ax, aI, aq)
                            }
                            this._areaPoints = aI;
                            this.renderer.shapeRenderer.draw(ax, aI, aq)
                        }
                    }
                    else
                    {
                        if (ah)
                        {
                            var aH = aI.slice(0)
                        }
                        if (this.index == 0 || !this._stack)
                        {
                            var al = ax.canvas.height;
                            aI.unshift([aI[0][0], al]);
                            var aD = aI.length;
                            aI.push([aI[aD - 1][0], al])
                        }
                        else
                        {
                            var au = this._prevGridData;
                            for (var aC = au.length; aC > 0; aC--)
                            {
                                aI.push(au[aC - 1])
                            }
                        }
                        this._areaPoints = aI;
                        if (ak)
                        {
                            this.renderer.shadowRenderer.draw(ax, aI, aq)
                        }
                        this.renderer.shapeRenderer.draw(ax, aI, aq)
                    }
                    if (ah)
                    {
                        var az = L.extend(true, {}, aq, {
                            fill: false,
                            closePath: false
                        });
                        this.renderer.shapeRenderer.draw(ax, aH, az);
                        if (this.markerRenderer.show)
                        {
                            if (this.renderer.smooth)
                            {
                                aH = this.gridData
                            }
                            for (aC = 0; aC < aH.length; aC++)
                            {
                                this.markerRenderer.draw(aH[aC][0], aH[aC][1],
                                    ax, aq.markerOptions)
                            }
                        }
                    }
                }
                else
                {
                    if (this.renderer.bands.show)
                    {
                        var am;
                        var aK = L.extend(true, {}, aq);
                        if (this.renderer.bands.showLines)
                        {
                            am = (this.renderer.smooth) ? this.renderer._hiBandSmoothedData
                                : this.renderer._hiBandGridData;
                            this.renderer.shapeRenderer.draw(ax, am, aq);
                            am = (this.renderer.smooth) ? this.renderer._lowBandSmoothedData
                                : this.renderer._lowBandGridData;
                            this.renderer.shapeRenderer.draw(ax, am, aK)
                        }
                        if (this.renderer.bands.fill)
                        {
                            if (this.renderer.smooth)
                            {
                                am = this.renderer._hiBandSmoothedData
                                    .concat(this.renderer._lowBandSmoothedData
                                        .reverse())
                            }
                            else
                            {
                                am = this.renderer._hiBandGridData
                                    .concat(this.renderer._lowBandGridData
                                        .reverse())
                            }
                            this._areaPoints = am;
                            aK.closePath = true;
                            aK.fill = true;
                            aK.fillStyle = this.renderer.bands.fillColor;
                            this.renderer.shapeRenderer.draw(ax, am, aK)
                        }
                    }
                    if (ak)
                    {
                        this.renderer.shadowRenderer.draw(ax, aI, aq)
                    }
                    this.renderer.shapeRenderer.draw(ax, aI, aq)
                }
            }
            var ar = av = ay = aE = null;
            for (aC = 0; aC < this._areaPoints.length; aC++)
            {
                var at = this._areaPoints[aC];
                if (ar > at[0] || ar == null)
                {
                    ar = at[0]
                }
                if (aE < at[1] || aE == null)
                {
                    aE = at[1]
                }
                if (av < at[0] || av == null)
                {
                    av = at[0]
                }
                if (ay > at[1] || ay == null)
                {
                    ay = at[1]
                }
            }
            if (this.type === "line" && this.renderer.bands.show)
            {
                aE = this._yaxis.series_u2p(this.renderer.bands._min);
                ay = this._yaxis.series_u2p(this.renderer.bands._max)
            }
            this._boundingBox = [[ar, aE], [av, ay]];
            if (this.markerRenderer.show && !aA)
            {
                if (this.renderer.smooth)
                {
                    aI = this.gridData
                }
                for (aC = 0; aC < aI.length; aC++)
                {
                    if (aI[aC][0] != null && aI[aC][1] != null)
                    {
                        this.markerRenderer.draw(aI[aC][0], aI[aC][1], ax,
                            aq.markerOptions)
                    }
                }
            }
        }
        ax.restore()
    };
    L.jqplot.LineRenderer.prototype.drawShadow = function (ah, aj, ai) {
    };

    function z(ak, aj, ah)
    {
        for (var ai = 0; ai < this.series.length; ai++)
        {
            if (this.series[ai].renderer.constructor == L.jqplot.LineRenderer)
            {
                if (this.series[ai].highlightMouseOver)
                {
                    this.series[ai].highlightMouseDown = false
                }
            }
        }
    }

    function af()
    {
        if (this.plugins.lineRenderer
            && this.plugins.lineRenderer.highlightCanvas)
        {
            this.plugins.lineRenderer.highlightCanvas.resetCanvas();
            this.plugins.lineRenderer.highlightCanvas = null
        }
        this.plugins.lineRenderer.highlightedSeriesIndex = null;
        this.plugins.lineRenderer.highlightCanvas = new L.jqplot.GenericCanvas();
        this.eventCanvas._elem.before(this.plugins.lineRenderer.highlightCanvas
            .createElement(this._gridPadding,
                "jqplot-lineRenderer-highlight-canvas",
                this._plotDimensions, this));
        this.plugins.lineRenderer.highlightCanvas.setContext();
        this.eventCanvas._elem.bind("mouseleave", {
            plot: this
        }, function (ah) {
            aa(ah.data.plot)
        })
    }

    function ac(an, am, ak, aj)
    {
        var ai = an.series[am];
        var ah = an.plugins.lineRenderer.highlightCanvas;
        ah._ctx.clearRect(0, 0, ah._ctx.canvas.width, ah._ctx.canvas.height);
        ai._highlightedPoint = ak;
        an.plugins.lineRenderer.highlightedSeriesIndex = am;
        var al = {
            fillStyle: ai.highlightColor
        };
        if (ai.type === "line" && ai.renderer.bands.show)
        {
            al.fill = true;
            al.closePath = true
        }
        ai.renderer.shapeRenderer.draw(ah._ctx, aj, al);
        ah = null
    }

    function aa(aj)
    {
        var ah = aj.plugins.lineRenderer.highlightCanvas;
        ah._ctx.clearRect(0, 0, ah._ctx.canvas.width, ah._ctx.canvas.height);
        for (var ai = 0; ai < aj.series.length; ai++)
        {
            aj.series[ai]._highlightedPoint = null
        }
        aj.plugins.lineRenderer.highlightedSeriesIndex = null;
        aj.target.trigger("jqplotDataUnhighlight");
        ah = null
    }

    function h(al, ak, ao, an, am)
    {
        if (an)
        {
            var aj = [an.seriesIndex, an.pointIndex, an.data];
            var ai = jQuery.Event("jqplotDataMouseOver");
            ai.pageX = al.pageX;
            ai.pageY = al.pageY;
            am.target.trigger(ai, aj);
            if (am.series[aj[0]].highlightMouseOver
                && !(aj[0] == am.plugins.lineRenderer.highlightedSeriesIndex))
            {
                var ah = jQuery.Event("jqplotDataHighlight");
                ah.which = al.which;
                ah.pageX = al.pageX;
                ah.pageY = al.pageY;
                am.target.trigger(ah, aj);
                ac(am, an.seriesIndex, an.pointIndex, an.points)
            }
        }
        else
        {
            if (an == null)
            {
                aa(am)
            }
        }
    }

    function e(ak, aj, an, am, al)
    {
        if (am)
        {
            var ai = [am.seriesIndex, am.pointIndex, am.data];
            if (al.series[ai[0]].highlightMouseDown
                && !(ai[0] == al.plugins.lineRenderer.highlightedSeriesIndex))
            {
                var ah = jQuery.Event("jqplotDataHighlight");
                ah.which = ak.which;
                ah.pageX = ak.pageX;
                ah.pageY = ak.pageY;
                al.target.trigger(ah, ai);
                ac(al, am.seriesIndex, am.pointIndex, am.points)
            }
        }
        else
        {
            if (am == null)
            {
                aa(al)
            }
        }
    }

    function ad(aj, ai, am, al, ak)
    {
        var ah = ak.plugins.lineRenderer.highlightedSeriesIndex;
        if (ah != null && ak.series[ah].highlightMouseDown)
        {
            aa(ak)
        }
    }

    function g(ak, aj, an, am, al)
    {
        if (am)
        {
            var ai = [am.seriesIndex, am.pointIndex, am.data];
            var ah = jQuery.Event("jqplotDataClick");
            ah.which = ak.which;
            ah.pageX = ak.pageX;
            ah.pageY = ak.pageY;
            al.target.trigger(ah, ai)
        }
    }

    function s(al, ak, ao, an, am)
    {
        if (an)
        {
            var aj = [an.seriesIndex, an.pointIndex, an.data];
            var ah = am.plugins.lineRenderer.highlightedSeriesIndex;
            if (ah != null && am.series[ah].highlightMouseDown)
            {
                aa(am)
            }
            var ai = jQuery.Event("jqplotDataRightClick");
            ai.which = al.which;
            ai.pageX = al.pageX;
            ai.pageY = al.pageY;
            am.target.trigger(ai, aj)
        }
    }

    L.jqplot.LinearAxisRenderer = function () {
    };
    L.jqplot.LinearAxisRenderer.prototype.init = function (ah) {
        this.breakPoints = null;
        this.breakTickLabel = "&asymp;";
        this.drawBaseline = true;
        this.baselineWidth = null;
        this.baselineColor = null;
        this.forceTickAt0 = false;
        this.forceTickAt100 = false;
        this.tickInset = 0;
        this.minorTicks = 0;
        this.alignTicks = false;
        this._autoFormatString = "";
        this._overrideFormatString = false;
        this._scalefact = 1;
        L.extend(true, this, ah);
        if (this.breakPoints)
        {
            if (!L.isArray(this.breakPoints))
            {
                this.breakPoints = null
            }
            else
            {
                if (this.breakPoints.length < 2
                    || this.breakPoints[1] <= this.breakPoints[0])
                {
                    this.breakPoints = null
                }
            }
        }
        if (this.numberTicks != null && this.numberTicks < 2)
        {
            this.numberTicks = 2
        }
        this.resetDataBounds()
    };
    L.jqplot.LinearAxisRenderer.prototype.draw = function (ah, ao) {
        if (this.show)
        {
            this.renderer.createTicks.call(this, ao);
            var an = 0;
            var ai;
            if (this._elem)
            {
                this._elem.emptyForce();
                this._elem = null
            }
            this._elem = L(document.createElement("div"));
            this._elem.addClass("jqplot-axis jqplot-" + this.name);
            this._elem.css("position", "absolute");
            if (this.name == "xaxis" || this.name == "x2axis")
            {
                this._elem.width(this._plotDimensions.width)
            }
            else
            {
                this._elem.height(this._plotDimensions.height)
            }
            this.labelOptions.axis = this.name;
            this._label = new this.labelRenderer(this.labelOptions);
            if (this._label.show)
            {
                var am = this._label.draw(ah, ao);
                am.appendTo(this._elem);
                am = null
            }
            var al = this._ticks;
            var ak;
            for (var aj = 0; aj < al.length; aj++)
            {
                ak = al[aj];
                if (ak.show && ak.showLabel
                    && (!ak.isMinorTick || this.showMinorTicks))
                {
                    this._elem.append(ak.draw(ah, ao))
                }
            }
            ak = null;
            al = null
        }
        return this._elem
    };
    L.jqplot.LinearAxisRenderer.prototype.reset = function () {
        this.min = this._options.min;
        this.max = this._options.max;
        this.tickInterval = this._options.tickInterval;
        this.numberTicks = this._options.numberTicks;
        this._autoFormatString = "";
        if (this._overrideFormatString && this.tickOptions
            && this.tickOptions.formatString)
        {
            this.tickOptions.formatString = ""
        }
    };
    L.jqplot.LinearAxisRenderer.prototype.set = function () {
        var ao = 0;
        var aj;
        var ai = 0;
        var an = 0;
        var ah = (this._label == null) ? false : this._label.show;
        if (this.show)
        {
            var am = this._ticks;
            var al;
            for (var ak = 0; ak < am.length; ak++)
            {
                al = am[ak];
                if (!al._breakTick && al.show && al.showLabel
                    && (!al.isMinorTick || this.showMinorTicks))
                {
                    if (this.name == "xaxis" || this.name == "x2axis")
                    {
                        aj = al._elem.outerHeight(true)
                    }
                    else
                    {
                        aj = al._elem.outerWidth(true)
                    }
                    if (aj > ao)
                    {
                        ao = aj
                    }
                }
            }
            al = null;
            am = null;
            if (ah)
            {
                ai = this._label._elem.outerWidth(true);
                an = this._label._elem.outerHeight(true)
            }
            if (this.name == "xaxis")
            {
                ao = ao + an;
                this._elem.css({
                    height: ao + "px",
                    left: "0px",
                    bottom: "0px"
                })
            }
            else
            {
                if (this.name == "x2axis")
                {
                    ao = ao + an;
                    this._elem.css({
                        height: ao + "px",
                        left: "0px",
                        top: "0px"
                    })
                }
                else
                {
                    if (this.name == "yaxis")
                    {
                        ao = ao + ai;
                        this._elem.css({
                            width: ao + "px",
                            left: "0px",
                            top: "0px"
                        });
                        if (ah
                            && this._label.constructor == L.jqplot.AxisLabelRenderer)
                        {
                            this._label._elem.css("width", ai + "px")
                        }
                    }
                    else
                    {
                        ao = ao + ai;
                        this._elem.css({
                            width: ao + "px",
                            right: "0px",
                            top: "0px"
                        });
                        if (ah
                            && this._label.constructor == L.jqplot.AxisLabelRenderer)
                        {
                            this._label._elem.css("width", ai + "px")
                        }
                    }
                }
            }
        }
    };
    L.jqplot.LinearAxisRenderer.prototype.createTicks = function (aj) {
        var aT = this._ticks;
        var aK = this.ticks;
        var az = this.name;
        var aB = this._dataBounds;
        var ah = (this.name.charAt(0) === "x") ? this._plotDimensions.width
            : this._plotDimensions.height;
        var an;
        var a6, aI;
        var ap, ao;
        var a4, a0;
        var aH = this.min;
        var a5 = this.max;
        var aW = this.numberTicks;
        var ba = this.tickInterval;
        var am = 30;
        this._scalefact = (Math.max(ah, am + 1) - am) / 300;
        if (aK.length)
        {
            for (a0 = 0; a0 < aK.length; a0++)
            {
                var aO = aK[a0];
                var aU = new this.tickRenderer(this.tickOptions);
                if (L.isArray(aO))
                {
                    aU.value = aO[0];
                    if (this.breakPoints)
                    {
                        if (aO[0] == this.breakPoints[0])
                        {
                            aU.label = this.breakTickLabel;
                            aU._breakTick = true;
                            aU.showGridline = false;
                            aU.showMark = false
                        }
                        else
                        {
                            if (aO[0] > this.breakPoints[0]
                                && aO[0] <= this.breakPoints[1])
                            {
                                aU.show = false;
                                aU.showGridline = false;
                                aU.label = aO[1]
                            }
                            else
                            {
                                aU.label = aO[1]
                            }
                        }
                    }
                    else
                    {
                        aU.label = aO[1]
                    }
                    aU.setTick(aO[0], this.name);
                    this._ticks.push(aU)
                }
                else
                {
                    if (L.isPlainObject(aO))
                    {
                        L.extend(true, aU, aO);
                        aU.axis = this.name;
                        this._ticks.push(aU)
                    }
                    else
                    {
                        aU.value = aO;
                        if (this.breakPoints)
                        {
                            if (aO == this.breakPoints[0])
                            {
                                aU.label = this.breakTickLabel;
                                aU._breakTick = true;
                                aU.showGridline = false;
                                aU.showMark = false
                            }
                            else
                            {
                                if (aO > this.breakPoints[0]
                                    && aO <= this.breakPoints[1])
                                {
                                    aU.show = false;
                                    aU.showGridline = false
                                }
                            }
                        }
                        aU.setTick(aO, this.name);
                        this._ticks.push(aU)
                    }
                }
            }
            this.numberTicks = aK.length;
            this.min = this._ticks[0].value;
            this.max = this._ticks[this.numberTicks - 1].value;
            this.tickInterval = (this.max - this.min) / (this.numberTicks - 1)
        }
        else
        {
            if (az == "xaxis" || az == "x2axis")
            {
                ah = this._plotDimensions.width
            }
            else
            {
                ah = this._plotDimensions.height
            }
            var ax = this.numberTicks;
            if (this.alignTicks)
            {
                if (this.name === "x2axis" && aj.axes.xaxis.show)
                {
                    ax = aj.axes.xaxis.numberTicks
                }
                else
                {
                    if (this.name.charAt(0) === "y" && this.name !== "yaxis"
                        && this.name !== "yMidAxis" && aj.axes.yaxis.show)
                    {
                        ax = aj.axes.yaxis.numberTicks
                    }
                }
            }
            a6 = ((this.min != null) ? this.min : aB.min);
            aI = ((this.max != null) ? this.max : aB.max);
            var av = aI - a6;
            var aS, ay;
            var at;
            if (this.tickOptions == null || !this.tickOptions.formatString)
            {
                this._overrideFormatString = true
            }
            if (this.min == null || this.max == null
                && this.tickInterval == null && !this.autoscale)
            {
                if (this.forceTickAt0)
                {
                    if (a6 > 0)
                    {
                        a6 = 0
                    }
                    if (aI < 0)
                    {
                        aI = 0
                    }
                }
                if (this.forceTickAt100)
                {
                    if (a6 > 100)
                    {
                        a6 = 100
                    }
                    if (aI < 100)
                    {
                        aI = 100
                    }
                }
                var aE = false, a1 = false;
                if (this.min != null)
                {
                    aE = true
                }
                else
                {
                    if (this.max != null)
                    {
                        a1 = true
                    }
                }
                var aP = L.jqplot.LinearTickGenerator(a6, aI, this._scalefact,
                    ax, aE, a1);
                var aw = (this.min != null) ? a6 : a6 + av * (this.padMin - 1);
                var aQ = (this.max != null) ? aI : aI - av * (this.padMax - 1);
                if (a6 < aw || aI > aQ)
                {
                    aw = (this.min != null) ? a6 : a6 - av * (this.padMin - 1);
                    aQ = (this.max != null) ? aI : aI + av * (this.padMax - 1);
                    aP = L.jqplot.LinearTickGenerator(aw, aQ, this._scalefact,
                        ax, aE, a1)
                }
                this.min = aP[0];
                this.max = aP[1];
                this.numberTicks = aP[2];
                this._autoFormatString = aP[3];
                this.tickInterval = aP[4]
            }
            else
            {
                if (a6 == aI)
                {
                    var ai = 0.05;
                    if (a6 > 0)
                    {
                        ai = Math.max(Math.log(a6) / Math.LN10, 0.05)
                    }
                    a6 -= ai;
                    aI += ai
                }
                if (this.autoscale && this.min == null && this.max == null)
                {
                    var ak, al, ar;
                    var aC = false;
                    var aN = false;
                    var aA = {
                        min: null,
                        max: null,
                        average: null,
                        stddev: null
                    };
                    for (var a0 = 0; a0 < this._series.length; a0++)
                    {
                        var aV = this._series[a0];
                        var aD = (aV.fillAxis == "x") ? aV._xaxis.name
                            : aV._yaxis.name;
                        if (this.name == aD)
                        {
                            var aR = aV._plotValues[aV.fillAxis];
                            var aG = aR[0];
                            var a2 = aR[0];
                            for (var aZ = 1; aZ < aR.length; aZ++)
                            {
                                if (aR[aZ] < aG)
                                {
                                    aG = aR[aZ]
                                }
                                else
                                {
                                    if (aR[aZ] > a2)
                                    {
                                        a2 = aR[aZ]
                                    }
                                }
                            }
                            var au = (a2 - aG) / a2;
                            if (aV.renderer.constructor == L.jqplot.BarRenderer)
                            {
                                if (aG >= 0 && (aV.fillToZero || au > 0.1))
                                {
                                    aC = true
                                }
                                else
                                {
                                    aC = false;
                                    if (aV.fill && aV.fillToZero && aG < 0
                                        && a2 > 0)
                                    {
                                        aN = true
                                    }
                                    else
                                    {
                                        aN = false
                                    }
                                }
                            }
                            else
                            {
                                if (aV.fill)
                                {
                                    if (aG >= 0 && (aV.fillToZero || au > 0.1))
                                    {
                                        aC = true
                                    }
                                    else
                                    {
                                        if (aG < 0 && a2 > 0 && aV.fillToZero)
                                        {
                                            aC = false;
                                            aN = true
                                        }
                                        else
                                        {
                                            aC = false;
                                            aN = false
                                        }
                                    }
                                }
                                else
                                {
                                    if (aG < 0)
                                    {
                                        aC = false
                                    }
                                }
                            }
                        }
                    }
                    if (aC)
                    {
                        this.numberTicks = 2 + Math
                            .ceil((ah - (this.tickSpacing - 1))
                                / this.tickSpacing);
                        this.min = 0;
                        aH = 0;
                        al = aI / (this.numberTicks - 1);
                        at = Math.pow(10, Math.abs(Math.floor(Math.log(al)
                            / Math.LN10)));
                        if (al / at == parseInt(al / at, 10))
                        {
                            al += at
                        }
                        this.tickInterval = Math.ceil(al / at) * at;
                        this.max = this.tickInterval * (this.numberTicks - 1)
                    }
                    else
                    {
                        if (aN)
                        {
                            this.numberTicks = 2 + Math
                                .ceil((ah - (this.tickSpacing - 1))
                                    / this.tickSpacing);
                            var aJ = Math.ceil(Math.abs(a6) / av
                                * (this.numberTicks - 1));
                            var a9 = this.numberTicks - 1 - aJ;
                            al = Math.max(Math.abs(a6 / aJ), Math.abs(aI / a9));
                            at = Math.pow(10, Math.abs(Math.floor(Math.log(al)
                                / Math.LN10)));
                            this.tickInterval = Math.ceil(al / at) * at;
                            this.max = this.tickInterval * a9;
                            this.min = -this.tickInterval * aJ
                        }
                        else
                        {
                            if (this.numberTicks == null)
                            {
                                if (this.tickInterval)
                                {
                                    this.numberTicks = 3 + Math.ceil(av
                                        / this.tickInterval)
                                }
                                else
                                {
                                    this.numberTicks = 2 + Math
                                        .ceil((ah - (this.tickSpacing - 1))
                                            / this.tickSpacing)
                                }
                            }
                            if (this.tickInterval == null)
                            {
                                al = av / (this.numberTicks - 1);
                                if (al < 1)
                                {
                                    at = Math.pow(10, Math.abs(Math.floor(Math
                                            .log(al)
                                        / Math.LN10)))
                                }
                                else
                                {
                                    at = 1
                                }
                                this.tickInterval = Math.ceil(al * at
                                    * this.pad)
                                    / at
                            }
                            else
                            {
                                at = 1 / this.tickInterval
                            }
                            ak = this.tickInterval * (this.numberTicks - 1);
                            ar = (ak - av) / 2;
                            if (this.min == null)
                            {
                                this.min = Math.floor(at * (a6 - ar)) / at
                            }
                            if (this.max == null)
                            {
                                this.max = this.min + ak
                            }
                        }
                    }
                    var aF = L.jqplot.getSignificantFigures(this.tickInterval);
                    var aM;
                    if (aF.digitsLeft >= aF.significantDigits)
                    {
                        aM = "%d"
                    }
                    else
                    {
                        var at = Math.max(0, 5 - aF.digitsLeft);
                        at = Math.min(at, aF.digitsRight);
                        aM = "%." + at + "f"
                    }
                    this._autoFormatString = aM
                }
                else
                {
                    aS = (this.min != null) ? this.min : a6 - av
                        * (this.padMin - 1);
                    ay = (this.max != null) ? this.max : aI + av
                        * (this.padMax - 1);
                    av = ay - aS;
                    if (this.numberTicks == null)
                    {
                        if (this.tickInterval != null)
                        {
                            this.numberTicks = Math.ceil((ay - aS)
                                / this.tickInterval) + 1
                        }
                        else
                        {
                            if (ah > 100)
                            {
                                this.numberTicks = parseInt(
                                    3 + (ah - 100) / 75, 10)
                            }
                            else
                            {
                                this.numberTicks = 2
                            }
                        }
                    }
                    if (this.tickInterval == null)
                    {
                        this.tickInterval = av / (this.numberTicks - 1)
                    }
                    if (this.max == null)
                    {
                        ay = aS + this.tickInterval * (this.numberTicks - 1)
                    }
                    if (this.min == null)
                    {
                        aS = ay - this.tickInterval * (this.numberTicks - 1)
                    }
                    var aF = L.jqplot.getSignificantFigures(this.tickInterval);
                    var aM;
                    if (aF.digitsLeft >= aF.significantDigits)
                    {
                        aM = "%d"
                    }
                    else
                    {
                        var at = Math.max(0, 5 - aF.digitsLeft);
                        at = Math.min(at, aF.digitsRight);
                        aM = "%." + at + "f"
                    }
                    this._autoFormatString = aM;
                    this.min = aS;
                    this.max = ay
                }
                if (this.renderer.constructor == L.jqplot.LinearAxisRenderer
                    && this._autoFormatString == "")
                {
                    av = this.max - this.min;
                    var a7 = new this.tickRenderer(this.tickOptions);
                    var aL = a7.formatString
                        || L.jqplot.config.defaultTickFormatString;
                    var aL = aL.match(L.jqplot.sprintf.regex)[0];
                    var a3 = 0;
                    if (aL)
                    {
                        if (aL.search(/[fFeEgGpP]/) > -1)
                        {
                            var aY = aL.match(/\%\.(\d{0,})?[eEfFgGpP]/);
                            if (aY)
                            {
                                a3 = parseInt(aY[1], 10)
                            }
                            else
                            {
                                a3 = 6
                            }
                        }
                        else
                        {
                            if (aL.search(/[di]/) > -1)
                            {
                                a3 = 0
                            }
                        }
                        var aq = Math.pow(10, -a3);
                        if (this.tickInterval < aq)
                        {
                            if (aW == null && ba == null)
                            {
                                this.tickInterval = aq;
                                if (a5 == null && aH == null)
                                {
                                    this.min = Math.floor(this._dataBounds.min
                                        / aq)
                                        * aq;
                                    if (this.min == this._dataBounds.min)
                                    {
                                        this.min = this._dataBounds.min
                                            - this.tickInterval
                                    }
                                    this.max = Math.ceil(this._dataBounds.max
                                        / aq)
                                        * aq;
                                    if (this.max == this._dataBounds.max)
                                    {
                                        this.max = this._dataBounds.max
                                            + this.tickInterval
                                    }
                                    var aX = (this.max - this.min)
                                        / this.tickInterval;
                                    aX = aX.toFixed(11);
                                    aX = Math.ceil(aX);
                                    this.numberTicks = aX + 1
                                }
                                else
                                {
                                    if (a5 == null)
                                    {
                                        var aX = (this._dataBounds.max - this.min)
                                            / this.tickInterval;
                                        aX = aX.toFixed(11);
                                        this.numberTicks = Math.ceil(aX) + 2;
                                        this.max = this.min + this.tickInterval
                                            * (this.numberTicks - 1)
                                    }
                                    else
                                    {
                                        if (aH == null)
                                        {
                                            var aX = (this.max - this._dataBounds.min)
                                                / this.tickInterval;
                                            aX = aX.toFixed(11);
                                            this.numberTicks = Math.ceil(aX) + 2;
                                            this.min = this.max
                                                - this.tickInterval
                                                * (this.numberTicks - 1)
                                        }
                                        else
                                        {
                                            this.numberTicks = Math
                                                .ceil((a5 - aH)
                                                    / this.tickInterval) + 1;
                                            this.min = Math.floor(aH
                                                * Math.pow(10, a3))
                                                / Math.pow(10, a3);
                                            this.max = Math.ceil(a5
                                                * Math.pow(10, a3))
                                                / Math.pow(10, a3);
                                            this.numberTicks = Math
                                                .ceil((this.max - this.min)
                                                    / this.tickInterval) + 1
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (this._overrideFormatString && this._autoFormatString != "")
            {
                this.tickOptions = this.tickOptions || {};
                this.tickOptions.formatString = this._autoFormatString
            }
            var aU, a8;
            for (var a0 = 0; a0 < this.numberTicks; a0++)
            {
                a4 = this.min + a0 * this.tickInterval;
                aU = new this.tickRenderer(this.tickOptions);
                aU.setTick(a4, this.name);
                this._ticks.push(aU);
                if (a0 < this.numberTicks - 1)
                {
                    for (var aZ = 0; aZ < this.minorTicks; aZ++)
                    {
                        a4 += this.tickInterval / (this.minorTicks + 1);
                        a8 = L.extend(true, {}, this.tickOptions, {
                            name: this.name,
                            value: a4,
                            label: "",
                            isMinorTick: true
                        });
                        aU = new this.tickRenderer(a8);
                        this._ticks.push(aU)
                    }
                }
                aU = null
            }
        }
        if (this.tickInset)
        {
            this.min = this.min - this.tickInset * this.tickInterval;
            this.max = this.max + this.tickInset * this.tickInterval
        }
        aT = null
    };
    L.jqplot.LinearAxisRenderer.prototype.resetTickValues = function (aj) {
        if (L.isArray(aj) && aj.length == this._ticks.length)
        {
            var ai;
            for (var ah = 0; ah < aj.length; ah++)
            {
                ai = this._ticks[ah];
                ai.value = aj[ah];
                ai.label = ai.formatter(ai.formatString, aj[ah]);
                ai.label = ai.prefix + ai.label;
                ai._elem.html(ai.label)
            }
            ai = null;
            this.min = L.jqplot.arrayMin(aj);
            this.max = L.jqplot.arrayMax(aj);
            this.pack()
        }
    };
    L.jqplot.LinearAxisRenderer.prototype.pack = function (aj, ai) {
        aj = aj || {};
        ai = ai || this._offsets;
        var ay = this._ticks;
        var au = this.max;
        var at = this.min;
        var ao = ai.max;
        var am = ai.min;
        var aq = (this._label == null) ? false : this._label.show;
        for (var ar in aj)
        {
            this._elem.css(ar, aj[ar])
        }
        this._offsets = ai;
        var ak = ao - am;
        var al = au - at;
        if (this.breakPoints)
        {
            al = al - this.breakPoints[1] + this.breakPoints[0];
            this.p2u = function (aA) {
                return (aA - am) * al / ak + at
            };
            this.u2p = function (aA) {
                if (aA > this.breakPoints[0] && aA < this.breakPoints[1])
                {
                    aA = this.breakPoints[0]
                }
                if (aA <= this.breakPoints[0])
                {
                    return (aA - at) * ak / al + am
                }
                else
                {
                    return (aA - this.breakPoints[1] + this.breakPoints[0] - at)
                        * ak / al + am
                }
            };
            if (this.name.charAt(0) == "x")
            {
                this.series_u2p = function (aA) {
                    if (aA > this.breakPoints[0] && aA < this.breakPoints[1])
                    {
                        aA = this.breakPoints[0]
                    }
                    if (aA <= this.breakPoints[0])
                    {
                        return (aA - at) * ak / al
                    }
                    else
                    {
                        return (aA - this.breakPoints[1] + this.breakPoints[0] - at)
                            * ak / al
                    }
                };
                this.series_p2u = function (aA) {
                    return aA * al / ak + at
                }
            }
            else
            {
                this.series_u2p = function (aA) {
                    if (aA > this.breakPoints[0] && aA < this.breakPoints[1])
                    {
                        aA = this.breakPoints[0]
                    }
                    if (aA >= this.breakPoints[1])
                    {
                        return (aA - au) * ak / al
                    }
                    else
                    {
                        return (aA + this.breakPoints[1] - this.breakPoints[0] - au)
                            * ak / al
                    }
                };
                this.series_p2u = function (aA) {
                    return aA * al / ak + au
                }
            }
        }
        else
        {
            this.p2u = function (aA) {
                return (aA - am) * al / ak + at
            };
            this.u2p = function (aA) {
                return (aA - at) * ak / al + am
            };
            if (this.name == "xaxis" || this.name == "x2axis")
            {
                this.series_u2p = function (aA) {
                    return (aA - at) * ak / al
                };
                this.series_p2u = function (aA) {
                    return aA * al / ak + at
                }
            }
            else
            {
                this.series_u2p = function (aA) {
                    return (aA - au) * ak / al
                };
                this.series_p2u = function (aA) {
                    return aA * al / ak + au
                }
            }
        }
        if (this.show)
        {
            if (this.name == "xaxis" || this.name == "x2axis")
            {
                for (var av = 0; av < ay.length; av++)
                {
                    var ap = ay[av];
                    if (ap.show && ap.showLabel)
                    {
                        var ah;
                        if (ap.constructor == L.jqplot.CanvasAxisTickRenderer
                            && ap.angle)
                        {
                            var ax = (this.name == "xaxis") ? 1 : -1;
                            switch (ap.labelPosition)
                            {
                                case "auto":
                                    if (ax * ap.angle < 0)
                                    {
                                        ah = -ap.getWidth()
                                            + ap._textRenderer.height
                                            * Math.sin(-ap._textRenderer.angle)
                                            / 2
                                    }
                                    else
                                    {
                                        ah = -ap._textRenderer.height
                                            * Math.sin(ap._textRenderer.angle)
                                            / 2
                                    }
                                    break;
                                case "end":
                                    ah = -ap.getWidth() + ap._textRenderer.height
                                        * Math.sin(-ap._textRenderer.angle) / 2;
                                    break;
                                case "start":
                                    ah = -ap._textRenderer.height
                                        * Math.sin(ap._textRenderer.angle) / 2;
                                    break;
                                case "middle":
                                    ah = -ap.getWidth() / 2
                                        + ap._textRenderer.height
                                        * Math.sin(-ap._textRenderer.angle) / 2;
                                    break;
                                default:
                                    ah = -ap.getWidth() / 2
                                        + ap._textRenderer.height
                                        * Math.sin(-ap._textRenderer.angle) / 2;
                                    break
                            }
                        }
                        else
                        {
                            ah = -ap.getWidth() / 2
                        }
                        var az = this.u2p(ap.value) + ah + "px";
                        ap._elem.css("left", az);
                        ap.pack()
                    }
                }
                if (aq)
                {
                    var an = this._label._elem.outerWidth(true);
                    this._label._elem.css("left", am + ak / 2 - an / 2 + "px");
                    if (this.name == "xaxis")
                    {
                        this._label._elem.css("bottom", "0px")
                    }
                    else
                    {
                        this._label._elem.css("top", "0px")
                    }
                    this._label.pack()
                }
            }
            else
            {
                for (var av = 0; av < ay.length; av++)
                {
                    var ap = ay[av];
                    if (ap.show && ap.showLabel)
                    {
                        var ah;
                        if (ap.constructor == L.jqplot.CanvasAxisTickRenderer
                            && ap.angle)
                        {
                            var ax = (this.name == "yaxis") ? 1 : -1;
                            switch (ap.labelPosition)
                            {
                                case "auto":
                                case "end":
                                    if (ax * ap.angle < 0)
                                    {
                                        ah = -ap._textRenderer.height
                                            * Math.cos(-ap._textRenderer.angle)
                                            / 2
                                    }
                                    else
                                    {
                                        ah = -ap.getHeight()
                                            + ap._textRenderer.height
                                            * Math.cos(ap._textRenderer.angle)
                                            / 2
                                    }
                                    break;
                                case "start":
                                    if (ap.angle > 0)
                                    {
                                        ah = -ap._textRenderer.height
                                            * Math.cos(-ap._textRenderer.angle)
                                            / 2
                                    }
                                    else
                                    {
                                        ah = -ap.getHeight()
                                            + ap._textRenderer.height
                                            * Math.cos(ap._textRenderer.angle)
                                            / 2
                                    }
                                    break;
                                case "middle":
                                    ah = -ap.getHeight() / 2;
                                    break;
                                default:
                                    ah = -ap.getHeight() / 2;
                                    break
                            }
                        }
                        else
                        {
                            ah = -ap.getHeight() / 2
                        }
                        var az = this.u2p(ap.value) + ah + "px";
                        ap._elem.css("top", az);
                        ap.pack()
                    }
                }
                if (aq)
                {
                    var aw = this._label._elem.outerHeight(true);
                    this._label._elem.css("top", ao - ak / 2 - aw / 2 + "px");
                    if (this.name == "yaxis")
                    {
                        this._label._elem.css("left", "0px")
                    }
                    else
                    {
                        this._label._elem.css("right", "0px")
                    }
                    this._label.pack()
                }
            }
        }
        ay = null
    };

    function i(ai)
    {
        var ah;
        ai = Math.abs(ai);
        if (ai >= 10)
        {
            ah = "%d"
        }
        else
        {
            if (ai > 1)
            {
                if (ai === parseInt(ai, 10))
                {
                    ah = "%d"
                }
                else
                {
                    ah = "%.1f"
                }
            }
            else
            {
                var aj = -Math.floor(Math.log(ai) / Math.LN10);
                ah = "%." + aj + "f"
            }
        }
        return ah
    }

    var b = [0.1, 0.2, 0.3, 0.4, 0.5, 0.8, 1, 2, 3, 4, 5];
    var c = function (ai) {
        var ah = b.indexOf(ai);
        if (ah > 0)
        {
            return b[ah - 1]
        }
        else
        {
            return b[b.length - 1] / 100
        }
    };
    var k = function (ai) {
        var ah = b.indexOf(ai);
        if (ah < b.length - 1)
        {
            return b[ah + 1]
        }
        else
        {
            return b[0] * 100
        }
    };

    function d(al, au, at)
    {
        var aq = Math.floor(at / 2);
        var ai = Math.ceil(at * 1.5);
        var ak = Number.MAX_VALUE;
        var ah = (au - al);
        var ax;
        var ap;
        var ar;
        var ay = L.jqplot.getSignificantFigures;
        var aw;
        var an;
        var ao;
        var av;
        for (var am = 0, aj = ai - aq + 1; am < aj; am++)
        {
            ao = aq + am;
            ax = ah / (ao - 1);
            ap = ay(ax);
            ax = Math.abs(at - ao) + ap.digitsRight;
            if (ax < ak)
            {
                ak = ax;
                ar = ao;
                av = ap.digitsRight
            }
            else
            {
                if (ax === ak)
                {
                    if (ap.digitsRight < av)
                    {
                        ar = ao;
                        av = ap.digitsRight
                    }
                }
            }
        }
        aw = Math.max(av, Math.max(ay(al).digitsRight, ay(au).digitsRight));
        if (aw === 0)
        {
            an = "%d"
        }
        else
        {
            an = "%." + aw + "f"
        }
        ax = ah / (ar - 1);
        return [al, au, ar, an, ax]
    }

    function W(ai, al)
    {
        al = al || 7;
        var ak = ai / (al - 1);
        var aj = Math.pow(10, Math.floor(Math.log(ak) / Math.LN10));
        var am = ak / aj;
        var ah;
        if (aj < 1)
        {
            if (am > 5)
            {
                ah = 10 * aj
            }
            else
            {
                if (am > 2)
                {
                    ah = 5 * aj
                }
                else
                {
                    if (am > 1)
                    {
                        ah = 2 * aj
                    }
                    else
                    {
                        ah = aj
                    }
                }
            }
        }
        else
        {
            if (am > 5)
            {
                ah = 10 * aj
            }
            else
            {
                if (am > 4)
                {
                    ah = 5 * aj
                }
                else
                {
                    if (am > 3)
                    {
                        ah = 4 * aj
                    }
                    else
                    {
                        if (am > 2)
                        {
                            ah = 3 * aj
                        }
                        else
                        {
                            if (am > 1)
                            {
                                ah = 2 * aj
                            }
                            else
                            {
                                ah = aj
                            }
                        }
                    }
                }
            }
        }
        return ah
    }

    function Q(ai, ah)
    {
        ah = ah || 1;
        var ak = Math.floor(Math.log(ai) / Math.LN10);
        var am = Math.pow(10, ak);
        var al = ai / am;
        var aj;
        al = al / ah;
        if (al <= 0.38)
        {
            aj = 0.1
        }
        else
        {
            if (al <= 1.6)
            {
                aj = 0.2
            }
            else
            {
                if (al <= 4)
                {
                    aj = 0.5
                }
                else
                {
                    if (al <= 8)
                    {
                        aj = 1
                    }
                    else
                    {
                        if (al <= 16)
                        {
                            aj = 2
                        }
                        else
                        {
                            aj = 5
                        }
                    }
                }
            }
        }
        return aj * am
    }

    function x(aj, ai)
    {
        var al = Math.floor(Math.log(aj) / Math.LN10);
        var an = Math.pow(10, al);
        var am = aj / an;
        var ah;
        var ak;
        am = am / ai;
        if (am <= 0.38)
        {
            ak = 0.1
        }
        else
        {
            if (am <= 1.6)
            {
                ak = 0.2
            }
            else
            {
                if (am <= 4)
                {
                    ak = 0.5
                }
                else
                {
                    if (am <= 8)
                    {
                        ak = 1
                    }
                    else
                    {
                        if (am <= 16)
                        {
                            ak = 2
                        }
                        else
                        {
                            ak = 5
                        }
                    }
                }
            }
        }
        ah = ak * an;
        return [ah, ak, an]
    }

    L.jqplot.LinearTickGenerator = function (an, aq, aj, ak, ao, ar) {
        ao = (ao === null) ? false : ao;
        ar = (ar === null || ao) ? false : ar;
        if (an === aq)
        {
            aq = (aq) ? 0 : 1
        }
        aj = aj || 1;
        if (aq < an)
        {
            var at = aq;
            aq = an;
            an = at
        }
        var ai = [];
        var aw = Q(aq - an, aj);
        var av = L.jqplot.getSignificantFigures;
        if (ak == null)
        {
            if (!ao && !ar)
            {
                ai[0] = Math.floor(an / aw) * aw;
                ai[1] = Math.ceil(aq / aw) * aw;
                ai[2] = Math.round((ai[1] - ai[0]) / aw + 1);
                ai[3] = i(aw);
                ai[4] = aw
            }
            else
            {
                if (ao)
                {
                    ai[0] = an;
                    ai[2] = Math.ceil((aq - an) / aw + 1);
                    ai[1] = an + (ai[2] - 1) * aw;
                    var au = av(an).digitsRight;
                    var ap = av(aw).digitsRight;
                    if (au < ap)
                    {
                        ai[3] = i(aw)
                    }
                    else
                    {
                        ai[3] = "%." + au + "f"
                    }
                    ai[4] = aw
                }
                else
                {
                    if (ar)
                    {
                        ai[1] = aq;
                        ai[2] = Math.ceil((aq - an) / aw + 1);
                        ai[0] = aq - (ai[2] - 1) * aw;
                        var al = av(aq).digitsRight;
                        var ap = av(aw).digitsRight;
                        if (al < ap)
                        {
                            ai[3] = i(aw)
                        }
                        else
                        {
                            ai[3] = "%." + al + "f"
                        }
                        ai[4] = aw
                    }
                }
            }
        }
        else
        {
            var am = [];
            am[0] = Math.floor(an / aw) * aw;
            am[1] = Math.ceil(aq / aw) * aw;
            am[2] = Math.round((am[1] - am[0]) / aw + 1);
            am[3] = i(aw);
            am[4] = aw;
            if (am[2] === ak)
            {
                ai = am
            }
            else
            {
                var ah = W(am[1] - am[0], ak);
                ai[0] = am[0];
                ai[2] = ak;
                ai[4] = ah;
                ai[3] = i(ah);
                ai[1] = ai[0] + (ai[2] - 1) * ai[4]
            }
        }
        return ai
    };
    L.jqplot.LinearTickGenerator.bestLinearInterval = Q;
    L.jqplot.LinearTickGenerator.bestInterval = W;
    L.jqplot.LinearTickGenerator.bestLinearComponents = x;
    L.jqplot.LinearTickGenerator.bestConstrainedInterval = d;
    L.jqplot.MarkerRenderer = function (ah) {
        this.show = true;
        this.style = "filledCircle";
        this.lineWidth = 2;
        this.size = 9;
        this.color = "#666666";
        this.shadow = true;
        this.shadowAngle = 45;
        this.shadowOffset = 1;
        this.shadowDepth = 3;
        this.shadowAlpha = "0.07";
        this.shadowRenderer = new L.jqplot.ShadowRenderer();
        this.shapeRenderer = new L.jqplot.ShapeRenderer();
        L.extend(true, this, ah)
    };
    L.jqplot.MarkerRenderer.prototype.init = function (ah) {
        L.extend(true, this, ah);
        var aj = {
            angle: this.shadowAngle,
            offset: this.shadowOffset,
            alpha: this.shadowAlpha,
            lineWidth: this.lineWidth,
            depth: this.shadowDepth,
            closePath: true
        };
        if (this.style.indexOf("filled") != -1)
        {
            aj.fill = true
        }
        if (this.style.indexOf("ircle") != -1)
        {
            aj.isarc = true;
            aj.closePath = false
        }
        this.shadowRenderer.init(aj);
        var ai = {
            fill: false,
            isarc: false,
            strokeStyle: this.color,
            fillStyle: this.color,
            lineWidth: this.lineWidth,
            closePath: true
        };
        if (this.style.indexOf("filled") != -1)
        {
            ai.fill = true
        }
        if (this.style.indexOf("ircle") != -1)
        {
            ai.isarc = true;
            ai.closePath = false
        }
        this.shapeRenderer.init(ai)
    };
    L.jqplot.MarkerRenderer.prototype.drawDiamond = function (aj, ai, am, al, ao) {
        var ah = 1.2;
        var ap = this.size / 2 / ah;
        var an = this.size / 2 * ah;
        var ak = [[aj - ap, ai], [aj, ai + an], [aj + ap, ai],
            [aj, ai - an]];
        if (this.shadow)
        {
            this.shadowRenderer.draw(am, ak)
        }
        this.shapeRenderer.draw(am, ak, ao)
    };
    L.jqplot.MarkerRenderer.prototype.drawPlus = function (ak, aj, an, am, aq) {
        var ai = 1;
        var ar = this.size / 2 * ai;
        var ao = this.size / 2 * ai;
        var ap = [[ak, aj - ao], [ak, aj + ao]];
        var al = [[ak + ar, aj], [ak - ar, aj]];
        var ah = L.extend(true, {}, this.options, {
            closePath: false
        });
        if (this.shadow)
        {
            this.shadowRenderer.draw(an, ap, {
                closePath: false
            });
            this.shadowRenderer.draw(an, al, {
                closePath: false
            })
        }
        this.shapeRenderer.draw(an, ap, ah);
        this.shapeRenderer.draw(an, al, ah)
    };
    L.jqplot.MarkerRenderer.prototype.drawX = function (ak, aj, an, am, aq) {
        var ai = 1;
        var ar = this.size / 2 * ai;
        var ao = this.size / 2 * ai;
        var ah = L.extend(true, {}, this.options, {
            closePath: false
        });
        var ap = [[ak - ar, aj - ao], [ak + ar, aj + ao]];
        var al = [[ak - ar, aj + ao], [ak + ar, aj - ao]];
        if (this.shadow)
        {
            this.shadowRenderer.draw(an, ap, {
                closePath: false
            });
            this.shadowRenderer.draw(an, al, {
                closePath: false
            })
        }
        this.shapeRenderer.draw(an, ap, ah);
        this.shapeRenderer.draw(an, al, ah)
    };
    L.jqplot.MarkerRenderer.prototype.drawDash = function (aj, ai, am, al, ao) {
        var ah = 1;
        var ap = this.size / 2 * ah;
        var an = this.size / 2 * ah;
        var ak = [[aj - ap, ai], [aj + ap, ai]];
        if (this.shadow)
        {
            this.shadowRenderer.draw(am, ak)
        }
        this.shapeRenderer.draw(am, ak, ao)
    };
    L.jqplot.MarkerRenderer.prototype.drawLine = function (am, al, ah, ak, ai) {
        var aj = [am, al];
        if (this.shadow)
        {
            this.shadowRenderer.draw(ah, aj)
        }
        this.shapeRenderer.draw(ah, aj, ai)
    };
    L.jqplot.MarkerRenderer.prototype.drawSquare = function (aj, ai, am, al, ao) {
        var ah = 1;
        var ap = this.size / 2 / ah;
        var an = this.size / 2 * ah;
        var ak = [[aj - ap, ai - an], [aj - ap, ai + an],
            [aj + ap, ai + an], [aj + ap, ai - an]];
        if (this.shadow)
        {
            this.shadowRenderer.draw(am, ak)
        }
        this.shapeRenderer.draw(am, ak, ao)
    };
    L.jqplot.MarkerRenderer.prototype.drawCircle = function (ai, ao, ak, an, al) {
        var ah = this.size / 2;
        var aj = 2 * Math.PI;
        var am = [ai, ao, ah, 0, aj, true];
        if (this.shadow)
        {
            this.shadowRenderer.draw(ak, am)
        }
        this.shapeRenderer.draw(ak, am, al)
    };
    L.jqplot.MarkerRenderer.prototype.draw = function (ah, ak, ai, aj) {
        aj = aj || {};
        if (aj.show == null || aj.show != false)
        {
            if (aj.color && !aj.fillStyle)
            {
                aj.fillStyle = aj.color
            }
            if (aj.color && !aj.strokeStyle)
            {
                aj.strokeStyle = aj.color
            }
            switch (this.style)
            {
                case "diamond":
                    this.drawDiamond(ah, ak, ai, false, aj);
                    break;
                case "filledDiamond":
                    this.drawDiamond(ah, ak, ai, true, aj);
                    break;
                case "circle":
                    this.drawCircle(ah, ak, ai, false, aj);
                    break;
                case "filledCircle":
                    this.drawCircle(ah, ak, ai, true, aj);
                    break;
                case "square":
                    this.drawSquare(ah, ak, ai, false, aj);
                    break;
                case "filledSquare":
                    this.drawSquare(ah, ak, ai, true, aj);
                    break;
                case "x":
                    this.drawX(ah, ak, ai, true, aj);
                    break;
                case "plus":
                    this.drawPlus(ah, ak, ai, true, aj);
                    break;
                case "dash":
                    this.drawDash(ah, ak, ai, true, aj);
                    break;
                case "line":
                    this.drawLine(ah, ak, ai, false, aj);
                    break;
                default:
                    this.drawDiamond(ah, ak, ai, false, aj);
                    break
            }
        }
    };
    L.jqplot.ShadowRenderer = function (ah) {
        this.angle = 45;
        this.offset = 1;
        this.alpha = 0.07;
        this.lineWidth = 1.5;
        this.lineJoin = "miter";
        this.lineCap = "round";
        this.closePath = false;
        this.fill = false;
        this.depth = 3;
        this.strokeStyle = "rgba(0,0,0,0.1)";
        this.isarc = false;
        L.extend(true, this, ah)
    };
    L.jqplot.ShadowRenderer.prototype.init = function (ah) {
        L.extend(true, this, ah)
    };
    L.jqplot.ShadowRenderer.prototype.draw = function (av, at, ax) {
        av.save();
        var ah = (ax != null) ? ax : {};
        var au = (ah.fill != null) ? ah.fill : this.fill;
        var ap = (ah.fillRect != null) ? ah.fillRect : this.fillRect;
        var ao = (ah.closePath != null) ? ah.closePath : this.closePath;
        var al = (ah.offset != null) ? ah.offset : this.offset;
        var aj = (ah.alpha != null) ? ah.alpha : this.alpha;
        var an = (ah.depth != null) ? ah.depth : this.depth;
        var aw = (ah.isarc != null) ? ah.isarc : this.isarc;
        var aq = (ah.linePattern != null) ? ah.linePattern : this.linePattern;
        av.lineWidth = (ah.lineWidth != null) ? ah.lineWidth : this.lineWidth;
        av.lineJoin = (ah.lineJoin != null) ? ah.lineJoin : this.lineJoin;
        av.lineCap = (ah.lineCap != null) ? ah.lineCap : this.lineCap;
        av.strokeStyle = ah.strokeStyle || this.strokeStyle || "rgba(0,0,0,"
            + aj + ")";
        av.fillStyle = ah.fillStyle || this.fillStyle || "rgba(0,0,0," + aj
            + ")";
        for (var ak = 0; ak < an; ak++)
        {
            var ar = L.jqplot.LinePattern(av, aq);
            av.translate(Math.cos(this.angle * Math.PI / 180) * al, Math
                    .sin(this.angle * Math.PI / 180)
                * al);
            ar.beginPath();
            if (aw)
            {
                av.arc(at[0], at[1], at[2], at[3], at[4], true)
            }
            else
            {
                if (ap)
                {
                    if (ap)
                    {
                        av.fillRect(at[0], at[1], at[2], at[3])
                    }
                }
                else
                {
                    if (at && at.length)
                    {
                        var ai = true;
                        for (var am = 0; am < at.length; am++)
                        {
                            if (at[am][0] != null && at[am][1] != null)
                            {
                                if (ai)
                                {
                                    ar.moveTo(at[am][0], at[am][1]);
                                    ai = false
                                }
                                else
                                {
                                    ar.lineTo(at[am][0], at[am][1])
                                }
                            }
                            else
                            {
                                ai = true
                            }
                        }
                    }
                }
            }
            if (ao)
            {
                ar.closePath()
            }
            if (au)
            {
                av.fill()
            }
            else
            {
                av.stroke()
            }
        }
        av.restore()
    };
    L.jqplot.ShapeRenderer = function (ah) {
        this.lineWidth = 1.5;
        this.linePattern = "solid";
        this.lineJoin = "miter";
        this.lineCap = "round";
        this.closePath = false;
        this.fill = false;
        this.isarc = false;
        this.fillRect = false;
        this.strokeRect = false;
        this.clearRect = false;
        this.strokeStyle = "#999999";
        this.fillStyle = "#999999";
        L.extend(true, this, ah)
    };
    L.jqplot.ShapeRenderer.prototype.init = function (ah) {
        L.extend(true, this, ah)
    };
    L.jqplot.ShapeRenderer.prototype.draw = function (at, aq, av) {
        at.save();
        var ah = (av != null) ? av : {};
        var ar = (ah.fill != null) ? ah.fill : this.fill;
        var am = (ah.closePath != null) ? ah.closePath : this.closePath;
        var an = (ah.fillRect != null) ? ah.fillRect : this.fillRect;
        var ak = (ah.strokeRect != null) ? ah.strokeRect : this.strokeRect;
        var ai = (ah.clearRect != null) ? ah.clearRect : this.clearRect;
        var au = (ah.isarc != null) ? ah.isarc : this.isarc;
        var ao = (ah.linePattern != null) ? ah.linePattern : this.linePattern;
        var ap = L.jqplot.LinePattern(at, ao);
        at.lineWidth = ah.lineWidth || this.lineWidth;
        at.lineJoin = ah.lineJoin || this.lineJoin;
        at.lineCap = ah.lineCap || this.lineCap;
        at.strokeStyle = (ah.strokeStyle || ah.color) || this.strokeStyle;
        at.fillStyle = ah.fillStyle || this.fillStyle;
        at.beginPath();
        if (au)
        {
            at.arc(aq[0], aq[1], aq[2], aq[3], aq[4], true);
            if (am)
            {
                at.closePath()
            }
            if (ar)
            {
                at.fill()
            }
            else
            {
                at.stroke()
            }
            at.restore();
            return
        }
        else
        {
            if (ai)
            {
                at.clearRect(aq[0], aq[1], aq[2], aq[3]);
                at.restore();
                return
            }
            else
            {
                if (an || ak)
                {
                    if (an)
                    {
                        at.fillRect(aq[0], aq[1], aq[2], aq[3])
                    }
                    if (ak)
                    {
                        at.strokeRect(aq[0], aq[1], aq[2], aq[3]);
                        at.restore();
                        return
                    }
                }
                else
                {
                    if (aq && aq.length)
                    {
                        var aj = true;
                        for (var al = 0; al < aq.length; al++)
                        {
                            if (aq[al][0] != null && aq[al][1] != null)
                            {
                                if (aj)
                                {
                                    ap.moveTo(aq[al][0], aq[al][1]);
                                    aj = false
                                }
                                else
                                {
                                    ap.lineTo(aq[al][0], aq[al][1])
                                }
                            }
                            else
                            {
                                aj = true
                            }
                        }
                        if (am)
                        {
                            ap.closePath()
                        }
                        if (ar)
                        {
                            at.fill()
                        }
                        else
                        {
                            at.stroke()
                        }
                    }
                }
            }
        }
        at.restore()
    };
    L.jqplot.TableLegendRenderer = function () {
    };
    L.jqplot.TableLegendRenderer.prototype.init = function (ah) {
        L.extend(true, this, ah)
    };
    L.jqplot.TableLegendRenderer.prototype.addrow = function (aq, ak, ah, ao) {
        var al = (ah) ? this.rowSpacing + "px" : "0px";
        var ap;
        var aj;
        var ai;
        var an;
        var am;
        ai = document.createElement("tr");
        ap = L(ai);
        ap.addClass("jqplot-table-legend");
        ai = null;
        if (ao)
        {
            ap.prependTo(this._elem)
        }
        else
        {
            ap.appendTo(this._elem)
        }
        if (this.showSwatches)
        {
            aj = L(document.createElement("td"));
            aj.addClass("jqplot-table-legend jqplot-table-legend-swatch");
            aj.css({
                textAlign: "center",
                paddingTop: al
            });
            an = L(document.createElement("div"));
            an.addClass("jqplot-table-legend-swatch-outline");
            am = L(document.createElement("div"));
            am.addClass("jqplot-table-legend-swatch");
            am.css({
                backgroundColor: ak,
                borderColor: ak
            });
            ap.append(aj.append(an.append(am)))
        }
        if (this.showLabels)
        {
            aj = L(document.createElement("td"));
            aj.addClass("jqplot-table-legend jqplot-table-legend-label");
            aj.css("paddingTop", al);
            ap.append(aj);
            if (this.escapeHtml)
            {
                aj.text(aq)
            }
            else
            {
                aj.html(aq)
            }
        }
        aj = null;
        an = null;
        am = null;
        ap = null;
        ai = null
    };
    L.jqplot.TableLegendRenderer.prototype.draw = function () {
        if (this._elem)
        {
            this._elem.emptyForce();
            this._elem = null
        }
        if (this.show)
        {
            var am = this._series;
            var ai = document.createElement("table");
            this._elem = L(ai);
            this._elem.addClass("jqplot-table-legend");
            var ar = {
                position: "absolute"
            };
            if (this.background)
            {
                ar.background = this.background
            }
            if (this.border)
            {
                ar.border = this.border
            }
            if (this.fontSize)
            {
                ar.fontSize = this.fontSize
            }
            if (this.fontFamily)
            {
                ar.fontFamily = this.fontFamily
            }
            if (this.textColor)
            {
                ar.textColor = this.textColor
            }
            if (this.marginTop != null)
            {
                ar.marginTop = this.marginTop
            }
            if (this.marginBottom != null)
            {
                ar.marginBottom = this.marginBottom
            }
            if (this.marginLeft != null)
            {
                ar.marginLeft = this.marginLeft
            }
            if (this.marginRight != null)
            {
                ar.marginRight = this.marginRight
            }
            var ah = false, ao = false, aq;
            for (var an = 0; an < am.length; an++)
            {
                aq = am[an];
                if (aq._stack
                    || aq.renderer.constructor == L.jqplot.BezierCurveRenderer)
                {
                    ao = true
                }
                if (aq.show && aq.showLabel)
                {
                    var al = this.labels[an] || aq.label.toString();
                    if (al)
                    {
                        var aj = aq.color;
                        if (ao && an < am.length - 1)
                        {
                            ah = true
                        }
                        else
                        {
                            if (ao && an == am.length - 1)
                            {
                                ah = false
                            }
                        }
                        this.renderer.addrow.call(this, al, aj, ah, ao);
                        ah = true
                    }
                    for (var ak = 0; ak < L.jqplot.addLegendRowHooks.length; ak++)
                    {
                        var ap = L.jqplot.addLegendRowHooks[ak].call(this, aq);
                        if (ap)
                        {
                            this.renderer.addrow.call(this, ap.label, ap.color,
                                ah);
                            ah = true
                        }
                    }
                    al = null
                }
            }
        }
        return this._elem
    };
    L.jqplot.TableLegendRenderer.prototype.pack = function (aj) {
        if (this.show)
        {
            if (this.placement == "insideGrid")
            {
                switch (this.location)
                {
                    case "nw":
                        var ai = aj.left;
                        var ah = aj.top;
                        this._elem.css("left", ai);
                        this._elem.css("top", ah);
                        break;
                    case "n":
                        var ai = (aj.left + (this._plotDimensions.width - aj.right))
                            / 2 - this.getWidth() / 2;
                        var ah = aj.top;
                        this._elem.css("left", ai);
                        this._elem.css("top", ah);
                        break;
                    case "ne":
                        var ai = aj.right;
                        var ah = aj.top;
                        this._elem.css({
                            right: ai,
                            top: ah
                        });
                        break;
                    case "e":
                        var ai = aj.right;
                        var ah = (aj.top + (this._plotDimensions.height - aj.bottom))
                            / 2 - this.getHeight() / 2;
                        this._elem.css({
                            right: ai,
                            top: ah
                        });
                        break;
                    case "se":
                        var ai = aj.right;
                        var ah = aj.bottom;
                        this._elem.css({
                            right: ai,
                            bottom: ah
                        });
                        break;
                    case "s":
                        var ai = (aj.left + (this._plotDimensions.width - aj.right))
                            / 2 - this.getWidth() / 2;
                        var ah = aj.bottom;
                        this._elem.css({
                            left: ai,
                            bottom: ah
                        });
                        break;
                    case "sw":
                        var ai = aj.left;
                        var ah = aj.bottom;
                        this._elem.css({
                            left: ai,
                            bottom: ah
                        });
                        break;
                    case "w":
                        var ai = aj.left;
                        var ah = (aj.top + (this._plotDimensions.height - aj.bottom))
                            / 2 - this.getHeight() / 2;
                        this._elem.css({
                            left: ai,
                            top: ah
                        });
                        break;
                    default:
                        var ai = aj.right;
                        var ah = aj.bottom;
                        this._elem.css({
                            right: ai,
                            bottom: ah
                        });
                        break
                }
            }
            else
            {
                if (this.placement == "outside")
                {
                    switch (this.location)
                    {
                        case "nw":
                            var ai = this._plotDimensions.width - aj.left;
                            var ah = aj.top;
                            this._elem.css("right", ai);
                            this._elem.css("top", ah);
                            break;
                        case "n":
                            var ai = (aj.left + (this._plotDimensions.width - aj.right))
                                / 2 - this.getWidth() / 2;
                            var ah = this._plotDimensions.height - aj.top;
                            this._elem.css("left", ai);
                            this._elem.css("bottom", ah);
                            break;
                        case "ne":
                            var ai = this._plotDimensions.width - aj.right;
                            var ah = aj.top;
                            this._elem.css({
                                left: ai,
                                top: ah
                            });
                            break;
                        case "e":
                            var ai = this._plotDimensions.width - aj.right;
                            var ah = (aj.top + (this._plotDimensions.height - aj.bottom))
                                / 2 - this.getHeight() / 2;
                            this._elem.css({
                                left: ai,
                                top: ah
                            });
                            break;
                        case "se":
                            var ai = this._plotDimensions.width - aj.right;
                            var ah = aj.bottom;
                            this._elem.css({
                                left: ai,
                                bottom: ah
                            });
                            break;
                        case "s":
                            var ai = (aj.left + (this._plotDimensions.width - aj.right))
                                / 2 - this.getWidth() / 2;
                            var ah = this._plotDimensions.height - aj.bottom;
                            this._elem.css({
                                left: ai,
                                top: ah
                            });
                            break;
                        case "sw":
                            var ai = this._plotDimensions.width - aj.left;
                            var ah = aj.bottom;
                            this._elem.css({
                                right: ai,
                                bottom: ah
                            });
                            break;
                        case "w":
                            var ai = this._plotDimensions.width - aj.left;
                            var ah = (aj.top + (this._plotDimensions.height - aj.bottom))
                                / 2 - this.getHeight() / 2;
                            this._elem.css({
                                right: ai,
                                top: ah
                            });
                            break;
                        default:
                            var ai = aj.right;
                            var ah = aj.bottom;
                            this._elem.css({
                                right: ai,
                                bottom: ah
                            });
                            break
                    }
                }
                else
                {
                    switch (this.location)
                    {
                        case "nw":
                            this._elem.css({
                                left: 0,
                                top: aj.top
                            });
                            break;
                        case "n":
                            var ai = (aj.left + (this._plotDimensions.width - aj.right))
                                / 2 - this.getWidth() / 2;
                            this._elem.css({
                                left: ai,
                                top: aj.top
                            });
                            break;
                        case "ne":
                            this._elem.css({
                                right: 0,
                                top: aj.top
                            });
                            break;
                        case "e":
                            var ah = (aj.top + (this._plotDimensions.height - aj.bottom))
                                / 2 - this.getHeight() / 2;
                            this._elem.css({
                                right: aj.right,
                                top: ah
                            });
                            break;
                        case "se":
                            this._elem.css({
                                right: aj.right,
                                bottom: aj.bottom
                            });
                            break;
                        case "s":
                            var ai = (aj.left + (this._plotDimensions.width - aj.right))
                                / 2 - this.getWidth() / 2;
                            this._elem.css({
                                left: ai,
                                bottom: aj.bottom
                            });
                            break;
                        case "sw":
                            this._elem.css({
                                left: aj.left,
                                bottom: aj.bottom
                            });
                            break;
                        case "w":
                            var ah = (aj.top + (this._plotDimensions.height - aj.bottom))
                                / 2 - this.getHeight() / 2;
                            this._elem.css({
                                left: aj.left,
                                top: ah
                            });
                            break;
                        default:
                            this._elem.css({
                                right: aj.right,
                                bottom: aj.bottom
                            });
                            break
                    }
                }
            }
        }
    };
    L.jqplot.ThemeEngine = function () {
        this.themes = {};
        this.activeTheme = null
    };
    L.jqplot.ThemeEngine.prototype.init = function () {
        var ak = new L.jqplot.Theme({
            _name: "Default"
        });
        var an, ai, am;
        for (an in ak.target)
        {
            if (an == "textColor")
            {
                ak.target[an] = this.target.css("color")
            }
            else
            {
                ak.target[an] = this.target.css(an)
            }
        }
        if (this.title.show && this.title._elem)
        {
            for (an in ak.title)
            {
                if (an == "textColor")
                {
                    ak.title[an] = this.title._elem.css("color")
                }
                else
                {
                    ak.title[an] = this.title._elem.css(an)
                }
            }
        }
        for (an in ak.grid)
        {
            ak.grid[an] = this.grid[an]
        }
        if (ak.grid.backgroundColor == null && this.grid.background != null)
        {
            ak.grid.backgroundColor = this.grid.background
        }
        if (this.legend.show && this.legend._elem)
        {
            for (an in ak.legend)
            {
                if (an == "textColor")
                {
                    ak.legend[an] = this.legend._elem.css("color")
                }
                else
                {
                    ak.legend[an] = this.legend._elem.css(an)
                }
            }
        }
        var aj;
        for (ai = 0; ai < this.series.length; ai++)
        {
            aj = this.series[ai];
            if (aj.renderer.constructor == L.jqplot.LineRenderer)
            {
                ak.series.push(new p())
            }
            else
            {
                if (aj.renderer.constructor == L.jqplot.BarRenderer)
                {
                    ak.series.push(new T())
                }
                else
                {
                    if (aj.renderer.constructor == L.jqplot.PieRenderer)
                    {
                        ak.series.push(new f())
                    }
                    else
                    {
                        if (aj.renderer.constructor == L.jqplot.DonutRenderer)
                        {
                            ak.series.push(new G())
                        }
                        else
                        {
                            if (aj.renderer.constructor == L.jqplot.FunnelRenderer)
                            {
                                ak.series.push(new Z())
                            }
                            else
                            {
                                if (aj.renderer.constructor == L.jqplot.MeterGaugeRenderer)
                                {
                                    ak.series.push(new D())
                                }
                                else
                                {
                                    ak.series.push({})
                                }
                            }
                        }
                    }
                }
            }
            for (an in ak.series[ai])
            {
                ak.series[ai][an] = aj[an]
            }
        }
        var ah, al;
        for (an in this.axes)
        {
            al = this.axes[an];
            ah = ak.axes[an] = new P();
            ah.borderColor = al.borderColor;
            ah.borderWidth = al.borderWidth;
            if (al._ticks && al._ticks[0])
            {
                for (am in ah.ticks)
                {
                    if (al._ticks[0].hasOwnProperty(am))
                    {
                        ah.ticks[am] = al._ticks[0][am]
                    }
                    else
                    {
                        if (al._ticks[0]._elem)
                        {
                            ah.ticks[am] = al._ticks[0]._elem.css(am)
                        }
                    }
                }
            }
            if (al._label && al._label.show)
            {
                for (am in ah.label)
                {
                    if (al._label[am])
                    {
                        ah.label[am] = al._label[am]
                    }
                    else
                    {
                        if (al._label._elem)
                        {
                            if (am == "textColor")
                            {
                                ah.label[am] = al._label._elem.css("color")
                            }
                            else
                            {
                                ah.label[am] = al._label._elem.css(am)
                            }
                        }
                    }
                }
            }
        }
        this.themeEngine._add(ak);
        this.themeEngine.activeTheme = this.themeEngine.themes[ak._name]
    };
    L.jqplot.ThemeEngine.prototype.get = function (ah) {
        if (!ah)
        {
            return this.activeTheme
        }
        else
        {
            return this.themes[ah]
        }
    };

    function O(ai, ah)
    {
        return ai - ah
    }

    L.jqplot.ThemeEngine.prototype.getThemeNames = function () {
        var ah = [];
        for (var ai in this.themes)
        {
            ah.push(ai)
        }
        return ah.sort(O)
    };
    L.jqplot.ThemeEngine.prototype.getThemes = function () {
        var ai = [];
        var ah = [];
        for (var ak in this.themes)
        {
            ai.push(ak)
        }
        ai.sort(O);
        for (var aj = 0; aj < ai.length; aj++)
        {
            ah.push(this.themes[ai[aj]])
        }
        return ah
    };
    L.jqplot.ThemeEngine.prototype.activate = function (av, aB) {
        var ah = false;
        if (!aB && this.activeTheme && this.activeTheme._name)
        {
            aB = this.activeTheme._name
        }
        if (!this.themes.hasOwnProperty(aB))
        {
            throw new Error("No theme of that name")
        }
        else
        {
            var am = this.themes[aB];
            this.activeTheme = am;
            var aA, at = false, ar = false;
            var ai = ["xaxis", "x2axis", "yaxis", "y2axis"];
            for (aw = 0; aw < ai.length; aw++)
            {
                var an = ai[aw];
                if (am.axesStyles.borderColor != null)
                {
                    av.axes[an].borderColor = am.axesStyles.borderColor
                }
                if (am.axesStyles.borderWidth != null)
                {
                    av.axes[an].borderWidth = am.axesStyles.borderWidth
                }
            }
            for (var az in av.axes)
            {
                var ak = av.axes[az];
                if (ak.show)
                {
                    var aq = am.axes[az] || {};
                    var ao = am.axesStyles;
                    var al = L.jqplot.extend(true, {}, aq, ao);
                    aA = (am.axesStyles.borderColor != null) ? am.axesStyles.borderColor
                        : al.borderColor;
                    if (al.borderColor != null)
                    {
                        ak.borderColor = al.borderColor;
                        ah = true
                    }
                    aA = (am.axesStyles.borderWidth != null) ? am.axesStyles.borderWidth
                        : al.borderWidth;
                    if (al.borderWidth != null)
                    {
                        ak.borderWidth = al.borderWidth;
                        ah = true
                    }
                    if (ak._ticks && ak._ticks[0])
                    {
                        for (var aj in al.ticks)
                        {
                            aA = al.ticks[aj];
                            if (aA != null)
                            {
                                ak.tickOptions[aj] = aA;
                                ak._ticks = [];
                                ah = true
                            }
                        }
                    }
                    if (ak._label && ak._label.show)
                    {
                        for (var aj in al.label)
                        {
                            aA = al.label[aj];
                            if (aA != null)
                            {
                                ak.labelOptions[aj] = aA;
                                ah = true
                            }
                        }
                    }
                }
            }
            for (var au in am.grid)
            {
                if (am.grid[au] != null)
                {
                    av.grid[au] = am.grid[au]
                }
            }
            if (!ah)
            {
                av.grid.draw()
            }
            if (av.legend.show)
            {
                for (au in am.legend)
                {
                    if (am.legend[au] != null)
                    {
                        av.legend[au] = am.legend[au]
                    }
                }
            }
            if (av.title.show)
            {
                for (au in am.title)
                {
                    if (am.title[au] != null)
                    {
                        av.title[au] = am.title[au]
                    }
                }
            }
            var aw;
            for (aw = 0; aw < am.series.length; aw++)
            {
                var ap = {};
                var ay = false;
                for (au in am.series[aw])
                {
                    aA = (am.seriesStyles[au] != null) ? am.seriesStyles[au]
                        : am.series[aw][au];
                    if (aA != null)
                    {
                        ap[au] = aA;
                        if (au == "color")
                        {
                            av.series[aw].renderer.shapeRenderer.fillStyle = aA;
                            av.series[aw].renderer.shapeRenderer.strokeStyle = aA;
                            av.series[aw][au] = aA
                        }
                        else
                        {
                            if ((au == "lineWidth") || (au == "linePattern"))
                            {
                                av.series[aw].renderer.shapeRenderer[au] = aA;
                                av.series[aw][au] = aA
                            }
                            else
                            {
                                if (au == "markerOptions")
                                {
                                    V(av.series[aw].markerOptions, aA);
                                    V(av.series[aw].markerRenderer, aA)
                                }
                                else
                                {
                                    av.series[aw][au] = aA
                                }
                            }
                        }
                        ah = true
                    }
                }
            }
            if (ah)
            {
                av.target.empty();
                av.draw()
            }
            for (au in am.target)
            {
                if (am.target[au] != null)
                {
                    av.target.css(au, am.target[au])
                }
            }
        }
    };
    L.jqplot.ThemeEngine.prototype._add = function (ai, ah) {
        if (ah)
        {
            ai._name = ah
        }
        if (!ai._name)
        {
            ai._name = Date.parse(new Date())
        }
        if (!this.themes.hasOwnProperty(ai._name))
        {
            this.themes[ai._name] = ai
        }
        else
        {
            throw new Error("jqplot.ThemeEngine Error: Theme already in use")
        }
    };
    L.jqplot.ThemeEngine.prototype.remove = function (ah) {
        if (ah == "Default")
        {
            return false
        }
        return delete this.themes[ah]
    };
    L.jqplot.ThemeEngine.prototype.newTheme = function (ah, aj) {
        if (typeof (ah) == "object")
        {
            aj = aj || ah;
            ah = null
        }
        if (aj && aj._name)
        {
            ah = aj._name
        }
        else
        {
            ah = ah || Date.parse(new Date())
        }
        var ai = this.copy(this.themes.Default._name, ah);
        L.jqplot.extend(ai, aj);
        return ai
    };

    function B(aj)
    {
        if (aj == null || typeof (aj) != "object")
        {
            return aj
        }
        var ah = new aj.constructor();
        for (var ai in aj)
        {
            ah[ai] = B(aj[ai])
        }
        return ah
    }

    L.jqplot.clone = B;

    function V(aj, ai)
    {
        if (ai == null || typeof (ai) != "object")
        {
            return
        }
        for (var ah in ai)
        {
            if (ah == "highlightColors")
            {
                aj[ah] = B(ai[ah])
            }
            if (ai[ah] != null && typeof (ai[ah]) == "object")
            {
                if (!aj.hasOwnProperty(ah))
                {
                    aj[ah] = {}
                }
                V(aj[ah], ai[ah])
            }
            else
            {
                aj[ah] = ai[ah]
            }
        }
    }

    L.jqplot.merge = V;
    L.jqplot.extend = function () {
        var am = arguments[0] || {}, ak = 1, al = arguments.length, ah = false, aj;
        if (typeof am === "boolean")
        {
            ah = am;
            am = arguments[1] || {};
            ak = 2
        }
        if (typeof am !== "object"
            && !toString.call(am) === "[object Function]")
        {
            am = {}
        }
        for (; ak < al; ak++)
        {
            if ((aj = arguments[ak]) != null)
            {
                for (var ai in aj)
                {
                    var an = am[ai], ao = aj[ai];
                    if (am === ao)
                    {
                        continue
                    }
                    if (ah && ao && typeof ao === "object" && !ao.nodeType)
                    {
                        am[ai] = L.jqplot.extend(ah, an
                            || (ao.length != null ? [] : {}), ao)
                    }
                    else
                    {
                        if (ao !== u)
                        {
                            am[ai] = ao
                        }
                    }
                }
            }
        }
        return am
    };
    L.jqplot.ThemeEngine.prototype.rename = function (ai, ah) {
        if (ai == "Default" || ah == "Default")
        {
            throw new Error(
                "jqplot.ThemeEngine Error: Cannot rename from/to Default")
        }
        if (this.themes.hasOwnProperty(ah))
        {
            throw new Error(
                "jqplot.ThemeEngine Error: New name already in use.")
        }
        else
        {
            if (this.themes.hasOwnProperty(ai))
            {
                var aj = this.copy(ai, ah);
                this.remove(ai);
                return aj
            }
        }
        throw new Error(
            "jqplot.ThemeEngine Error: Old name or new name invalid")
    };
    L.jqplot.ThemeEngine.prototype.copy = function (ah, aj, al) {
        if (aj == "Default")
        {
            throw new Error(
                "jqplot.ThemeEngine Error: Cannot copy over Default theme")
        }
        if (!this.themes.hasOwnProperty(ah))
        {
            var ai = "jqplot.ThemeEngine Error: Source name invalid";
            throw new Error(ai)
        }
        if (this.themes.hasOwnProperty(aj))
        {
            var ai = "jqplot.ThemeEngine Error: Target name invalid";
            throw new Error(ai)
        }
        else
        {
            var ak = B(this.themes[ah]);
            ak._name = aj;
            L.jqplot.extend(true, ak, al);
            this._add(ak);
            return ak
        }
    };
    L.jqplot.Theme = function (ah, ai) {
        if (typeof (ah) == "object")
        {
            ai = ai || ah;
            ah = null
        }
        ah = ah || Date.parse(new Date());
        this._name = ah;
        this.target = {
            backgroundColor: null
        };
        this.legend = {
            textColor: null,
            fontFamily: null,
            fontSize: null,
            border: null,
            background: null
        };
        this.title = {
            textColor: null,
            fontFamily: null,
            fontSize: null,
            textAlign: null
        };
        this.seriesStyles = {};
        this.series = [];
        this.grid = {
            drawGridlines: null,
            gridLineColor: null,
            gridLineWidth: null,
            backgroundColor: null,
            borderColor: null,
            borderWidth: null,
            shadow: null
        };
        this.axesStyles = {
            label: {},
            ticks: {}
        };
        this.axes = {};
        if (typeof (ai) == "string")
        {
            this._name = ai
        }
        else
        {
            if (typeof (ai) == "object")
            {
                L.jqplot.extend(true, this, ai)
            }
        }
    };
    var P = function () {
        this.borderColor = null;
        this.borderWidth = null;
        this.ticks = new o();
        this.label = new t()
    };
    var o = function () {
        this.show = null;
        this.showGridline = null;
        this.showLabel = null;
        this.showMark = null;
        this.size = null;
        this.textColor = null;
        this.whiteSpace = null;
        this.fontSize = null;
        this.fontFamily = null
    };
    var t = function () {
        this.textColor = null;
        this.whiteSpace = null;
        this.fontSize = null;
        this.fontFamily = null;
        this.fontWeight = null
    };
    var p = function () {
        this.color = null;
        this.lineWidth = null;
        this.linePattern = null;
        this.shadow = null;
        this.fillColor = null;
        this.showMarker = null;
        this.markerOptions = new I()
    };
    var I = function () {
        this.show = null;
        this.style = null;
        this.lineWidth = null;
        this.size = null;
        this.color = null;
        this.shadow = null
    };
    var T = function () {
        this.color = null;
        this.seriesColors = null;
        this.lineWidth = null;
        this.shadow = null;
        this.barPadding = null;
        this.barMargin = null;
        this.barWidth = null;
        this.highlightColors = null
    };
    var f = function () {
        this.seriesColors = null;
        this.padding = null;
        this.sliceMargin = null;
        this.fill = null;
        this.shadow = null;
        this.startAngle = null;
        this.lineWidth = null;
        this.highlightColors = null
    };
    var G = function () {
        this.seriesColors = null;
        this.padding = null;
        this.sliceMargin = null;
        this.fill = null;
        this.shadow = null;
        this.startAngle = null;
        this.lineWidth = null;
        this.innerDiameter = null;
        this.thickness = null;
        this.ringMargin = null;
        this.highlightColors = null
    };
    var Z = function () {
        this.color = null;
        this.lineWidth = null;
        this.shadow = null;
        this.padding = null;
        this.sectionMargin = null;
        this.seriesColors = null;
        this.highlightColors = null
    };
    var D = function () {
        this.padding = null;
        this.backgroundColor = null;
        this.ringColor = null;
        this.tickColor = null;
        this.ringWidth = null;
        this.intervalColors = null;
        this.intervalInnerRadius = null;
        this.intervalOuterRadius = null;
        this.hubRadius = null;
        this.needleThickness = null;
        this.needlePad = null
    };
    L.fn.jqplotChildText = function () {
        return L(this).contents().filter(function () {
            return this.nodeType == 3
        }).text()
    };
    L.fn.jqplotGetComputedFontStyle = function () {
        var ak = window.getComputedStyle ? window.getComputedStyle(this[0], "")
            : this[0].currentStyle;
        var ai = ak["font-style"] ? ["font-style", "font-weight", "font-size",
            "font-family"] : ["fontStyle", "fontWeight", "fontSize",
            "fontFamily"];
        var al = [];
        for (var aj = 0; aj < ai.length; ++aj)
        {
            var ah = String(ak[ai[aj]]);
            if (ah && ah != "normal")
            {
                al.push(ah)
            }
        }
        return al.join(" ")
    };
    L.fn.jqplotToImageCanvas = function (aj) {
        aj = aj || {};
        var av = (aj.x_offset == null) ? 0 : aj.x_offset;
        var ax = (aj.y_offset == null) ? 0 : aj.y_offset;
        var al = (aj.backgroundColor == null) ? "rgb(255,255,255)"
            : aj.backgroundColor;
        if (L(this).width() == 0 || L(this).height() == 0)
        {
            return null
        }
        if (L.jqplot.use_excanvas)
        {
            return null
        }
        var an = document.createElement("canvas");
        var aA = L(this).outerHeight(true);
        var at = L(this).outerWidth(true);
        var am = L(this).offset();
        var ao = am.left;
        var aq = am.top;
        var au = 0, ar = 0;
        var ay = ["jqplot-table-legend", "jqplot-xaxis-tick",
            "jqplot-x2axis-tick", "jqplot-yaxis-tick",
            "jqplot-y2axis-tick", "jqplot-y3axis-tick",
            "jqplot-y4axis-tick", "jqplot-y5axis-tick",
            "jqplot-y6axis-tick", "jqplot-y7axis-tick",
            "jqplot-y8axis-tick", "jqplot-y9axis-tick",
            "jqplot-xaxis-label", "jqplot-x2axis-label",
            "jqplot-yaxis-label", "jqplot-y2axis-label",
            "jqplot-y3axis-label", "jqplot-y4axis-label",
            "jqplot-y5axis-label", "jqplot-y6axis-label",
            "jqplot-y7axis-label", "jqplot-y8axis-label",
            "jqplot-y9axis-label"];
        var ap, ah, ai, aB;
        for (var az = 0; az < ay.length; az++)
        {
            L(this).find("." + ay[az]).each(function () {
                ap = L(this).offset().top - aq;
                ah = L(this).offset().left - ao;
                aB = ah + L(this).outerWidth(true) + au;
                ai = ap + L(this).outerHeight(true) + ar;
                if (ah < -au)
                {
                    at = at - au - ah;
                    au = -ah
                }
                if (ap < -ar)
                {
                    aA = aA - ar - ap;
                    ar = -ap
                }
                if (aB > at)
                {
                    at = aB
                }
                if (ai > aA)
                {
                    aA = ai
                }
            })
        }
        an.width = at + Number(av);
        an.height = aA + Number(ax);
        var ak = an.getContext("2d");
        ak.save();
        ak.fillStyle = al;
        ak.fillRect(0, 0, an.width, an.height);
        ak.restore();
        ak.translate(au, ar);
        ak.textAlign = "left";
        ak.textBaseline = "top";

        function aC(aE)
        {
            var aF = parseInt(L(aE).css("line-height"), 10);
            if (isNaN(aF))
            {
                aF = parseInt(L(aE).css("font-size"), 10) * 1.2
            }
            return aF
        }

        function aD(aF, aE, aS, aG, aO, aH)
        {
            var aQ = aC(aF);
            var aK = L(aF).innerWidth();
            var aL = L(aF).innerHeight();
            var aN = aS.split(/\s+/);
            var aR = aN.length;
            var aP = "";
            var aM = [];
            var aU = aO;
            var aT = aG;
            for (var aJ = 0; aJ < aR; aJ++)
            {
                aP += aN[aJ];
                if (aE.measureText(aP).width > aK)
                {
                    aM.push(aJ);
                    aP = "";
                    aJ--
                }
            }
            if (aM.length === 0)
            {
                if (L(aF).css("textAlign") === "center")
                {
                    aT = aG + (aH - aE.measureText(aP).width) / 2 - au
                }
                aE.fillText(aS, aT, aO)
            }
            else
            {
                aP = aN.slice(0, aM[0]).join(" ");
                if (L(aF).css("textAlign") === "center")
                {
                    aT = aG + (aH - aE.measureText(aP).width) / 2 - au
                }
                aE.fillText(aP, aT, aU);
                aU += aQ;
                for (var aJ = 1, aI = aM.length; aJ < aI; aJ++)
                {
                    aP = aN.slice(aM[aJ - 1], aM[aJ]).join(" ");
                    if (L(aF).css("textAlign") === "center")
                    {
                        aT = aG + (aH - aE.measureText(aP).width) / 2 - au
                    }
                    aE.fillText(aP, aT, aU);
                    aU += aQ
                }
                aP = aN.slice(aM[aJ - 1], aN.length).join(" ");
                if (L(aF).css("textAlign") === "center")
                {
                    aT = aG + (aH - aE.measureText(aP).width) / 2 - au
                }
                aE.fillText(aP, aT, aU)
            }
        }

        function aw(aG, aJ, aE)
        {
            var aN = aG.tagName.toLowerCase();
            var aF = L(aG).position();
            var aK = window.getComputedStyle ? window.getComputedStyle(aG, "")
                : aG.currentStyle;
            var aI = aJ + aF.left + parseInt(aK.marginLeft, 10)
                + parseInt(aK.borderLeftWidth, 10)
                + parseInt(aK.paddingLeft, 10);
            var aL = aE + aF.top + parseInt(aK.marginTop, 10)
                + parseInt(aK.borderTopWidth, 10)
                + parseInt(aK.paddingTop, 10);
            var aM = an.width;
            if ((aN == "div" || aN == "span")
                && !L(aG).hasClass("jqplot-highlighter-tooltip"))
            {
                L(aG).children().each(function () {
                    aw(this, aI, aL)
                });
                var aO = L(aG).jqplotChildText();
                if (aO)
                {
                    ak.font = L(aG).jqplotGetComputedFontStyle();
                    ak.fillStyle = L(aG).css("color");
                    aD(aG, ak, aO, aI, aL, aM)
                }
            }
            else
            {
                if (aN === "table" && L(aG).hasClass("jqplot-table-legend"))
                {
                    ak.strokeStyle = L(aG).css("border-top-color");
                    ak.fillStyle = L(aG).css("background-color");
                    ak
                        .fillRect(aI, aL, L(aG).innerWidth(), L(aG)
                            .innerHeight());
                    if (parseInt(L(aG).css("border-top-width"), 10) > 0)
                    {
                        ak.strokeRect(aI, aL, L(aG).innerWidth(), L(aG)
                            .innerHeight())
                    }
                    L(aG)
                        .find("div.jqplot-table-legend-swatch-outline")
                        .each(
                            function () {
                                var aU = L(this);
                                ak.strokeStyle = aU
                                    .css("border-top-color");
                                var aQ = aI + aU.position().left;
                                var aR = aL + aU.position().top;
                                ak.strokeRect(aQ, aR, aU.innerWidth(),
                                    aU.innerHeight());
                                aQ += parseInt(aU.css("padding-left"),
                                    10);
                                aR += parseInt(aU.css("padding-top"),
                                    10);
                                var aT = aU.innerHeight()
                                    - 2
                                    * parseInt(aU
                                        .css("padding-top"), 10);
                                var aP = aU.innerWidth()
                                    - 2
                                    * parseInt(aU
                                            .css("padding-left"),
                                        10);
                                var aS = aU
                                    .children("div.jqplot-table-legend-swatch");
                                ak.fillStyle = aS
                                    .css("background-color");
                                ak.fillRect(aQ, aR, aP, aT)
                            });
                    L(aG).find("td.jqplot-table-legend-label").each(
                        function () {
                            var aR = L(this);
                            var aP = aI + aR.position().left;
                            var aQ = aL + aR.position().top
                                + parseInt(aR.css("padding-top"), 10);
                            ak.font = aR.jqplotGetComputedFontStyle();
                            ak.fillStyle = aR.css("color");
                            aD(aR, ak, aR.text(), aP, aQ, aM)
                        });
                    var aH = null
                }
                else
                {
                    if (aN == "canvas")
                    {
                        ak.drawImage(aG, aI, aL)
                    }
                }
            }
        }

        L(this).children().each(function () {
            aw(this, av, ax)
        });
        return an
    };
    L.fn.jqplotToImageStr = function (ai) {
        var ah = L(this).jqplotToImageCanvas(ai);
        if (ah)
        {
            return ah.toDataURL("image/png")
        }
        else
        {
            return null
        }
    };
    L.fn.jqplotToImageElem = function (ah) {
        var ai = document.createElement("img");
        var aj = L(this).jqplotToImageStr(ah);
        ai.src = aj;
        return ai
    };
    L.fn.jqplotToImageElemStr = function (ah) {
        var ai = "<img src=" + L(this).jqplotToImageStr(ah) + " />";
        return ai
    };
    L.fn.jqplotSaveImage = function () {
        var ah = L(this).jqplotToImageStr({});
        if (ah)
        {
            window.location.href = ah
                .replace("image/png", "image/octet-stream")
        }
    };
    L.fn.jqplotViewImage = function () {
        var ai = L(this).jqplotToImageElemStr({});
        var aj = L(this).jqplotToImageStr({});
        if (ai)
        {
            var ah = window.open("");
            ah.document.open("image/png");
            ah.document.write(ai);
            ah.document.close();
            ah = null
        }
    };
    var ag = function () {
        this.syntax = ag.config.syntax;
        this._type = "jsDate";
        this.proxy = new Date();
        this.options = {};
        this.locale = ag.regional.getLocale();
        this.formatString = "";
        this.defaultCentury = ag.config.defaultCentury;
        switch (arguments.length)
        {
            case 0:
                break;
            case 1:
                if (l(arguments[0]) == "[object Object]"
                    && arguments[0]._type != "jsDate")
                {
                    var aj = this.options = arguments[0];
                    this.syntax = aj.syntax || this.syntax;
                    this.defaultCentury = aj.defaultCentury || this.defaultCentury;
                    this.proxy = ag.createDate(aj.date)
                }
                else
                {
                    this.proxy = ag.createDate(arguments[0])
                }
                break;
            default:
                var ah = [];
                for (var ai = 0; ai < arguments.length; ai++)
                {
                    ah.push(arguments[ai])
                }
                this.proxy = new Date();
                this.proxy.setFullYear.apply(this.proxy, ah.slice(0, 3));
                if (ah.slice(3).length)
                {
                    this.proxy.setHours.apply(this.proxy, ah.slice(3))
                }
                break
        }
    };
    ag.config = {
        defaultLocale: "en",
        syntax: "perl",
        defaultCentury: 1900
    };
    ag.prototype.add = function (aj, ai) {
        var ah = E[ai] || E.day;
        if (typeof ah == "number")
        {
            this.proxy.setTime(this.proxy.getTime() + (ah * aj))
        }
        else
        {
            ah.add(this, aj)
        }
        return this
    };
    ag.prototype.clone = function () {
        return new ag(this.proxy.getTime())
    };
    ag.prototype.getUtcOffset = function () {
        return this.proxy.getTimezoneOffset() * 60000
    };
    ag.prototype.diff = function (ai, al, ah) {
        ai = new ag(ai);
        if (ai === null)
        {
            return null
        }
        var aj = E[al] || E.day;
        if (typeof aj == "number")
        {
            var ak = (this.proxy.getTime() - ai.proxy.getTime()) / aj
        }
        else
        {
            var ak = aj.diff(this.proxy, ai.proxy)
        }
        return (ah ? ak : Math[ak > 0 ? "floor" : "ceil"](ak))
    };
    ag.prototype.getAbbrDayName = function () {
        return ag.regional[this.locale]["dayNamesShort"][this.proxy.getDay()]
    };
    ag.prototype.getAbbrMonthName = function () {
        return ag.regional[this.locale]["monthNamesShort"][this.proxy
            .getMonth()]
    };
    ag.prototype.getAMPM = function () {
        return this.proxy.getHours() >= 12 ? "PM" : "AM"
    };
    ag.prototype.getAmPm = function () {
        return this.proxy.getHours() >= 12 ? "pm" : "am"
    };
    ag.prototype.getCentury = function () {
        return parseInt(this.proxy.getFullYear() / 100, 10)
    };
    ag.prototype.getDate = function () {
        return this.proxy.getDate()
    };
    ag.prototype.getDay = function () {
        return this.proxy.getDay()
    };
    ag.prototype.getDayOfWeek = function () {
        var ah = this.proxy.getDay();
        return ah === 0 ? 7 : ah
    };
    ag.prototype.getDayOfYear = function () {
        var ai = this.proxy;
        var ah = ai - new Date("" + ai.getFullYear() + "/1/1 GMT");
        ah += ai.getTimezoneOffset() * 60000;
        ai = null;
        return parseInt(ah / 60000 / 60 / 24, 10) + 1
    };
    ag.prototype.getDayName = function () {
        return ag.regional[this.locale]["dayNames"][this.proxy.getDay()]
    };
    ag.prototype.getFullWeekOfYear = function () {
        var ak = this.proxy;
        var ah = this.getDayOfYear();
        var aj = 6 - ak.getDay();
        var ai = parseInt((ah + aj) / 7, 10);
        return ai
    };
    ag.prototype.getFullYear = function () {
        return this.proxy.getFullYear()
    };
    ag.prototype.getGmtOffset = function () {
        var ah = this.proxy.getTimezoneOffset() / 60;
        var ai = ah < 0 ? "+" : "-";
        ah = Math.abs(ah);
        return ai + N(Math.floor(ah), 2) + ":" + N((ah % 1) * 60, 2)
    };
    ag.prototype.getHours = function () {
        return this.proxy.getHours()
    };
    ag.prototype.getHours12 = function () {
        var ah = this.proxy.getHours();
        return ah > 12 ? ah - 12 : (ah == 0 ? 12 : ah)
    };
    ag.prototype.getIsoWeek = function () {
        var ak = this.proxy;
        var aj = this.getWeekOfYear();
        var ah = (new Date("" + ak.getFullYear() + "/1/1")).getDay();
        var ai = aj + (ah > 4 || ah <= 1 ? 0 : 1);
        if (ai == 53
            && (new Date("" + ak.getFullYear() + "/12/31")).getDay() < 4)
        {
            ai = 1
        }
        else
        {
            if (ai === 0)
            {
                ak = new ag(new Date("" + (ak.getFullYear() - 1) + "/12/31"));
                ai = ak.getIsoWeek()
            }
        }
        ak = null;
        return ai
    };
    ag.prototype.getMilliseconds = function () {
        return this.proxy.getMilliseconds()
    };
    ag.prototype.getMinutes = function () {
        return this.proxy.getMinutes()
    };
    ag.prototype.getMonth = function () {
        return this.proxy.getMonth()
    };
    ag.prototype.getMonthName = function () {
        return ag.regional[this.locale]["monthNames"][this.proxy.getMonth()]
    };
    ag.prototype.getMonthNumber = function () {
        return this.proxy.getMonth() + 1
    };
    ag.prototype.getSeconds = function () {
        return this.proxy.getSeconds()
    };
    ag.prototype.getShortYear = function () {
        return this.proxy.getYear() % 100
    };
    ag.prototype.getTime = function () {
        return this.proxy.getTime()
    };
    ag.prototype.getTimezoneAbbr = function () {
        return this.proxy.toString().replace(/^.*\(([^)]+)\)$/, "$1")
    };
    ag.prototype.getTimezoneName = function () {
        var ah = /(?:\((.+)\)$| ([A-Z]{3}) )/.exec(this.toString());
        return ah[1] || ah[2] || "GMT" + this.getGmtOffset()
    };
    ag.prototype.getTimezoneOffset = function () {
        return this.proxy.getTimezoneOffset()
    };
    ag.prototype.getWeekOfYear = function () {
        var ah = this.getDayOfYear();
        var aj = 7 - this.getDayOfWeek();
        var ai = parseInt((ah + aj) / 7, 10);
        return ai
    };
    ag.prototype.getUnix = function () {
        return Math.round(this.proxy.getTime() / 1000, 0)
    };
    ag.prototype.getYear = function () {
        return this.proxy.getYear()
    };
    ag.prototype.next = function (ah) {
        ah = ah || "day";
        return this.clone().add(1, ah)
    };
    ag.prototype.set = function () {
        switch (arguments.length)
        {
            case 0:
                this.proxy = new Date();
                break;
            case 1:
                if (l(arguments[0]) == "[object Object]"
                    && arguments[0]._type != "jsDate")
                {
                    var aj = this.options = arguments[0];
                    this.syntax = aj.syntax || this.syntax;
                    this.defaultCentury = aj.defaultCentury || this.defaultCentury;
                    this.proxy = ag.createDate(aj.date)
                }
                else
                {
                    this.proxy = ag.createDate(arguments[0])
                }
                break;
            default:
                var ah = [];
                for (var ai = 0; ai < arguments.length; ai++)
                {
                    ah.push(arguments[ai])
                }
                this.proxy = new Date();
                this.proxy.setFullYear.apply(this.proxy, ah.slice(0, 3));
                if (ah.slice(3).length)
                {
                    this.proxy.setHours.apply(this.proxy, ah.slice(3))
                }
                break
        }
        return this
    };
    ag.prototype.setDate = function (ah) {
        this.proxy.setDate(ah);
        return this
    };
    ag.prototype.setFullYear = function () {
        this.proxy.setFullYear.apply(this.proxy, arguments);
        return this
    };
    ag.prototype.setHours = function () {
        this.proxy.setHours.apply(this.proxy, arguments);
        return this
    };
    ag.prototype.setMilliseconds = function (ah) {
        this.proxy.setMilliseconds(ah);
        return this
    };
    ag.prototype.setMinutes = function () {
        this.proxy.setMinutes.apply(this.proxy, arguments);
        return this
    };
    ag.prototype.setMonth = function () {
        this.proxy.setMonth.apply(this.proxy, arguments);
        return this
    };
    ag.prototype.setSeconds = function () {
        this.proxy.setSeconds.apply(this.proxy, arguments);
        return this
    };
    ag.prototype.setTime = function (ah) {
        this.proxy.setTime(ah);
        return this
    };
    ag.prototype.setYear = function () {
        this.proxy.setYear.apply(this.proxy, arguments);
        return this
    };
    ag.prototype.strftime = function (ah) {
        ah = ah || this.formatString
            || ag.regional[this.locale]["formatString"];
        return ag.strftime(this, ah, this.syntax)
    };
    ag.prototype.toString = function () {
        return this.proxy.toString()
    };
    ag.prototype.toYmdInt = function () {
        return (this.proxy.getFullYear() * 10000)
            + (this.getMonthNumber() * 100) + this.proxy.getDate()
    };
    ag.regional = {
        en: {
            monthNames: ["January", "February", "March", "April", "May",
                "June", "July", "August", "September", "October",
                "November", "December"],
            monthNamesShort: ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
            dayNames: ["Sunday", "Monday", "Tuesday", "Wednesday",
                "Thursday", "Friday", "Saturday"],
            dayNamesShort: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
            formatString: "%Y-%m-%d %H:%M:%S"
        },
        fr: {
            monthNames: ["Janvier", "Février", "Mars", "Avril", "Mai",
                "Juin", "Juillet", "Août", "Septembre", "Octobre",
                "Novembre", "Décembre"],
            monthNamesShort: ["Jan", "Fév", "Mar", "Avr", "Mai", "Jun",
                "Jul", "Aoû", "Sep", "Oct", "Nov", "Déc"],
            dayNames: ["Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi",
                "Vendredi", "Samedi"],
            dayNamesShort: ["Dim", "Lun", "Mar", "Mer", "Jeu", "Ven", "Sam"],
            formatString: "%Y-%m-%d %H:%M:%S"
        },
        de: {
            monthNames: ["Januar", "Februar", "März", "April", "Mai", "Juni",
                "Juli", "August", "September", "Oktober", "November",
                "Dezember"],
            monthNamesShort: ["Jan", "Feb", "Mär", "Apr", "Mai", "Jun",
                "Jul", "Aug", "Sep", "Okt", "Nov", "Dez"],
            dayNames: ["Sonntag", "Montag", "Dienstag", "Mittwoch",
                "Donnerstag", "Freitag", "Samstag"],
            dayNamesShort: ["So", "Mo", "Di", "Mi", "Do", "Fr", "Sa"],
            formatString: "%Y-%m-%d %H:%M:%S"
        },
        es: {
            monthNames: ["Enero", "Febrero", "Marzo", "Abril", "Mayo",
                "Junio", "Julio", "Agosto", "Septiembre", "Octubre",
                "Noviembre", "Diciembre"],
            monthNamesShort: ["Ene", "Feb", "Mar", "Abr", "May", "Jun",
                "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"],
            dayNames: ["Domingo", "Lunes", "Martes", "Mi&eacute;rcoles",
                "Jueves", "Viernes", "S&aacute;bado"],
            dayNamesShort: ["Dom", "Lun", "Mar", "Mi&eacute;", "Juv", "Vie",
                "S&aacute;b"],
            formatString: "%Y-%m-%d %H:%M:%S"
        },
        ru: {
            monthNames: ["Январь", "Февраль", "Март", "Апрель", "Май",
                "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь",
                "Декабрь"],
            monthNamesShort: ["Янв", "Фев", "Мар", "Апр", "Май", "Июн",
                "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"],
            dayNames: ["воскресенье", "понедельник", "вторник", "среда",
                "четверг", "пятница", "суббота"],
            dayNamesShort: ["вск", "пнд", "втр", "срд", "чтв", "птн", "сбт"],
            formatString: "%Y-%m-%d %H:%M:%S"
        },
        ar: {
            monthNames: ["كانون الثاني", "شباط", "آذار", "نيسان", "آذار",
                "حزيران", "تموز", "آب", "أيلول", "تشرين الأول",
                "تشرين الثاني", "كانون الأول"],
            monthNamesShort: ["1", "2", "3", "4", "5", "6", "7", "8", "9",
                "10", "11", "12"],
            dayNames: ["السبت", "الأحد", "الاثنين", "الثلاثاء", "الأربعاء",
                "الخميس", "الجمعة"],
            dayNamesShort: ["سبت", "أحد", "اثنين", "ثلاثاء", "أربعاء",
                "خميس", "جمعة"],
            formatString: "%Y-%m-%d %H:%M:%S"
        },
        pt: {
            monthNames: ["Janeiro", "Fevereiro", "Mar&ccedil;o", "Abril",
                "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro",
                "Novembro", "Dezembro"],
            monthNamesShort: ["Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
                "Jul", "Ago", "Set", "Out", "Nov", "Dez"],
            dayNames: ["Domingo", "Segunda-feira", "Ter&ccedil;a-feira",
                "Quarta-feira", "Quinta-feira", "Sexta-feira",
                "S&aacute;bado"],
            dayNamesShort: ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex",
                "S&aacute;b"],
            formatString: "%Y-%m-%d %H:%M:%S"
        },
        "pt-BR": {
            monthNames: ["Janeiro", "Fevereiro", "Mar&ccedil;o", "Abril",
                "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro",
                "Novembro", "Dezembro"],
            monthNamesShort: ["Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
                "Jul", "Ago", "Set", "Out", "Nov", "Dez"],
            dayNames: ["Domingo", "Segunda-feira", "Ter&ccedil;a-feira",
                "Quarta-feira", "Quinta-feira", "Sexta-feira",
                "S&aacute;bado"],
            dayNamesShort: ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex",
                "S&aacute;b"],
            formatString: "%Y-%m-%d %H:%M:%S"
        },
        pl: {
            monthNames: ["Styczeń", "Luty", "Marzec", "Kwiecień", "Maj",
                "Czerwiec", "Lipiec", "Sierpień", "Wrzesień",
                "Październik", "Listopad", "Grudzień"],
            monthNamesShort: ["Sty", "Lut", "Mar", "Kwi", "Maj", "Cze",
                "Lip", "Sie", "Wrz", "Paź", "Lis", "Gru"],
            dayNames: ["Niedziela", "Poniedziałek", "Wtorek", "Środa",
                "Czwartek", "Piątek", "Sobota"],
            dayNamesShort: ["Ni", "Pn", "Wt", "Śr", "Cz", "Pt", "Sb"],
            formatString: "%Y-%m-%d %H:%M:%S"
        },
        nl: {
            monthNames: ["Januari", "Februari", "Maart", "April", "Mei",
                "Juni", "July", "Augustus", "September", "Oktober",
                "November", "December"],
            monthNamesShort: ["Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
                "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"],
            dayNames: ","["Zondag", "Maandag", "Dinsdag", "Woensdag",
                "Donderdag", "Vrijdag", "Zaterdag"],
            dayNamesShort: ["Zo", "Ma", "Di", "Wo", "Do", "Vr", "Za"],
            formatString: "%Y-%m-%d %H:%M:%S"
        },
        sv: {
            monthNames: ["januari", "februari", "mars", "april", "maj",
                "juni", "juli", "augusti", "september", "oktober",
                "november", "december"],
            monthNamesShort: ["jan", "feb", "mar", "apr", "maj", "jun",
                "jul", "aug", "sep", "okt", "nov", "dec"],
            dayNames: ["söndag", "måndag", "tisdag", "onsdag", "torsdag",
                "fredag", "lördag"],
            dayNamesShort: ["sön", "mån", "tis", "ons", "tor", "fre", "lör"],
            formatString: "%Y-%m-%d %H:%M:%S"
        }
    };
    ag.regional["en-US"] = ag.regional["en-GB"] = ag.regional.en;
    ag.regional.getLocale = function () {
        var ah = ag.config.defaultLocale;
        if (document && document.getElementsByTagName("html")
            && document.getElementsByTagName("html")[0].lang)
        {
            ah = document.getElementsByTagName("html")[0].lang;
            if (!ag.regional.hasOwnProperty(ah))
            {
                ah = ag.config.defaultLocale
            }
        }
        return ah
    };
    var C = 24 * 60 * 60 * 1000;
    var N = function (ah, ak) {
        ah = String(ah);
        var ai = ak - ah.length;
        var aj = String(Math.pow(10, ai)).slice(1);
        return aj.concat(ah)
    };
    var E = {
        millisecond: 1,
        second: 1000,
        minute: 60 * 1000,
        hour: 60 * 60 * 1000,
        day: C,
        week: 7 * C,
        month: {
            add: function (aj, ah) {
                E.year.add(aj, Math[ah > 0 ? "floor" : "ceil"](ah / 12));
                var ai = aj.getMonth() + (ah % 12);
                if (ai == 12)
                {
                    ai = 0;
                    aj.setYear(aj.getFullYear() + 1)
                }
                else
                {
                    if (ai == -1)
                    {
                        ai = 11;
                        aj.setYear(aj.getFullYear() - 1)
                    }
                }
                aj.setMonth(ai)
            },
            diff: function (al, aj) {
                var ah = al.getFullYear() - aj.getFullYear();
                var ai = al.getMonth() - aj.getMonth() + (ah * 12);
                var ak = al.getDate() - aj.getDate();
                return ai + (ak / 30)
            }
        },
        year: {
            add: function (ai, ah) {
                ai.setYear(ai.getFullYear()
                    + Math[ah > 0 ? "floor" : "ceil"](ah))
            },
            diff: function (ai, ah) {
                return E.month.diff(ai, ah) / 12
            }
        }
    };
    for (var Y in E)
    {
        if (Y.substring(Y.length - 1) != "s")
        {
            E[Y + "s"] = E[Y]
        }
    }
    var H = function (al, ak, ai) {
        if (ag.formats[ai]["shortcuts"][ak])
        {
            return ag.strftime(al, ag.formats[ai]["shortcuts"][ak], ai)
        }
        else
        {
            var ah = (ag.formats[ai]["codes"][ak] || "").split(".");
            var aj = al["get" + ah[0]] ? al["get" + ah[0]]() : "";
            if (ah[1])
            {
                aj = N(aj, ah[1])
            }
            return aj
        }
    };
    ag.strftime = function (an, ak, aj, ao) {
        var ai = "perl";
        var am = ag.regional.getLocale();
        if (aj && ag.formats.hasOwnProperty(aj))
        {
            ai = aj
        }
        else
        {
            if (aj && ag.regional.hasOwnProperty(aj))
            {
                am = aj
            }
        }
        if (ao && ag.formats.hasOwnProperty(ao))
        {
            ai = ao
        }
        else
        {
            if (ao && ag.regional.hasOwnProperty(ao))
            {
                am = ao
            }
        }
        if (l(an) != "[object Object]" || an._type != "jsDate")
        {
            an = new ag(an);
            an.locale = am
        }
        if (!ak)
        {
            ak = an.formatString || ag.regional[am]["formatString"]
        }
        var ah = ak || "%Y-%m-%d", ap = "", al;
        while (ah.length > 0)
        {
            if (al = ah.match(ag.formats[ai].codes.matcher))
            {
                ap += ah.slice(0, al.index);
                ap += (al[1] || "") + H(an, al[2], ai);
                ah = ah.slice(al.index + al[0].length)
            }
            else
            {
                ap += ah;
                ah = ""
            }
        }
        return ap
    };
    ag.formats = {
        ISO: "%Y-%m-%dT%H:%M:%S.%N%G",
        SQL: "%Y-%m-%d %H:%M:%S"
    };
    ag.formats.perl = {
        codes: {
            matcher: /()%(#?(%|[a-z]))/i,
            Y: "FullYear",
            y: "ShortYear.2",
            m: "MonthNumber.2",
            "#m": "MonthNumber",
            B: "MonthName",
            b: "AbbrMonthName",
            d: "Date.2",
            "#d": "Date",
            e: "Date",
            A: "DayName",
            a: "AbbrDayName",
            w: "Day",
            H: "Hours.2",
            "#H": "Hours",
            I: "Hours12.2",
            "#I": "Hours12",
            p: "AMPM",
            M: "Minutes.2",
            "#M": "Minutes",
            S: "Seconds.2",
            "#S": "Seconds",
            s: "Unix",
            N: "Milliseconds.3",
            "#N": "Milliseconds",
            O: "TimezoneOffset",
            Z: "TimezoneName",
            G: "GmtOffset"
        },
        shortcuts: {
            F: "%Y-%m-%d",
            T: "%H:%M:%S",
            X: "%H:%M:%S",
            x: "%m/%d/%y",
            D: "%m/%d/%y",
            "#c": "%a %b %e %H:%M:%S %Y",
            v: "%e-%b-%Y",
            R: "%H:%M",
            r: "%I:%M:%S %p",
            t: "\t",
            n: "\n",
            "%": "%"
        }
    };
    ag.formats.php = {
        codes: {
            matcher: /()%((%|[a-z]))/i,
            a: "AbbrDayName",
            A: "DayName",
            d: "Date.2",
            e: "Date",
            j: "DayOfYear.3",
            u: "DayOfWeek",
            w: "Day",
            U: "FullWeekOfYear.2",
            V: "IsoWeek.2",
            W: "WeekOfYear.2",
            b: "AbbrMonthName",
            B: "MonthName",
            m: "MonthNumber.2",
            h: "AbbrMonthName",
            C: "Century.2",
            y: "ShortYear.2",
            Y: "FullYear",
            H: "Hours.2",
            I: "Hours12.2",
            l: "Hours12",
            p: "AMPM",
            P: "AmPm",
            M: "Minutes.2",
            S: "Seconds.2",
            s: "Unix",
            O: "TimezoneOffset",
            z: "GmtOffset",
            Z: "TimezoneAbbr"
        },
        shortcuts: {
            D: "%m/%d/%y",
            F: "%Y-%m-%d",
            T: "%H:%M:%S",
            X: "%H:%M:%S",
            x: "%m/%d/%y",
            R: "%H:%M",
            r: "%I:%M:%S %p",
            t: "\t",
            n: "\n",
            "%": "%"
        }
    };
    ag.createDate = function (aj) {
        if (aj == null)
        {
            return new Date()
        }
        if (aj instanceof Date)
        {
            return aj
        }
        if (typeof aj == "number")
        {
            return new Date(aj)
        }
        var ao = String(aj).replace(/^\s*(.+)\s*$/g, "$1");
        ao = ao.replace(/^([0-9]{1,4})-([0-9]{1,2})-([0-9]{1,4})/, "$1/$2/$3");
        ao = ao.replace(/^(3[01]|[0-2]?\d)[-\/]([a-z]{3,})[-\/](\d{4})/i,
            "$1 $2 $3");
        var an = ao.match(/^(3[01]|[0-2]?\d)[-\/]([a-z]{3,})[-\/](\d{2})\D*/i);
        if (an && an.length > 3)
        {
            var at = parseFloat(an[3]);
            var am = ag.config.defaultCentury + at;
            am = String(am);
            ao = ao.replace(
                /^(3[01]|[0-2]?\d)[-\/]([a-z]{3,})[-\/](\d{2})\D*/i, an[1]
                + " " + an[2] + " " + am)
        }
        an = ao.match(/^([0-9]{1,2})[-\/]([0-9]{1,2})[-\/]([0-9]{1,2})[^0-9]/);

        function ar(ax, aw)
        {
            var aC = parseFloat(aw[1]);
            var aB = parseFloat(aw[2]);
            var aA = parseFloat(aw[3]);
            var az = ag.config.defaultCentury;
            var av, au, aD, ay;
            if (aC > 31)
            {
                au = aA;
                aD = aB;
                av = az + aC
            }
            else
            {
                au = aB;
                aD = aC;
                av = az + aA
            }
            ay = aD + "/" + au + "/" + av;
            return ax.replace(
                /^([0-9]{1,2})[-\/]([0-9]{1,2})[-\/]([0-9]{1,2})/, ay)
        }

        if (an && an.length > 3)
        {
            ao = ar(ao, an)
        }
        var an = ao.match(/^([0-9]{1,2})[-\/]([0-9]{1,2})[-\/]([0-9]{1,2})$/);
        if (an && an.length > 3)
        {
            ao = ar(ao, an)
        }
        var al = 0;
        var ai = ag.matchers.length;
        var aq, ah, ap = ao, ak;
        while (al < ai)
        {
            ah = Date.parse(ap);
            if (!isNaN(ah))
            {
                return new Date(ah)
            }
            aq = ag.matchers[al];
            if (typeof aq == "function")
            {
                ak = aq.call(ag, ap);
                if (ak instanceof Date)
                {
                    return ak
                }
            }
            else
            {
                ap = ao.replace(aq[0], aq[1])
            }
            al++
        }
        return NaN
    };
    ag.daysInMonth = function (ah, ai) {
        if (ai == 2)
        {
            return new Date(ah, 1, 29).getDate() == 29 ? 29 : 28
        }
        return [u, 31, u, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][ai]
    };
    ag.matchers = [
        [/(3[01]|[0-2]\d)\s*\.\s*(1[0-2]|0\d)\s*\.\s*([1-9]\d{3})/,
            "$2/$1/$3"],
        [/([1-9]\d{3})\s*-\s*(1[0-2]|0\d)\s*-\s*(3[01]|[0-2]\d)/,
            "$2/$3/$1"],
        function (ak) {
            var ai = ak
                .match(/^(?:(.+)\s+)?([012]?\d)(?:\s*\:\s*(\d\d))?(?:\s*\:\s*(\d\d(\.\d*)?))?\s*(am|pm)?\s*$/i);
            if (ai)
            {
                if (ai[1])
                {
                    var aj = this.createDate(ai[1]);
                    if (isNaN(aj))
                    {
                        return
                    }
                }
                else
                {
                    var aj = new Date();
                    aj.setMilliseconds(0)
                }
                var ah = parseFloat(ai[2]);
                if (ai[6])
                {
                    ah = ai[6].toLowerCase() == "am" ? (ah == 12 ? 0 : ah)
                        : (ah == 12 ? 12 : ah + 12)
                }
                aj.setHours(ah, parseInt(ai[3] || 0, 10), parseInt(
                    ai[4] || 0, 10),
                    ((parseFloat(ai[5] || 0)) || 0) * 1000);
                return aj
            }
            else
            {
                return ak
            }
        },
        function (ak) {
            var ai = ak
                .match(/^(?:(.+))[T|\s+]([012]\d)(?:\:(\d\d))(?:\:(\d\d))(?:\.\d+)([\+\-]\d\d\:\d\d)$/i);
            if (ai)
            {
                if (ai[1])
                {
                    var aj = this.createDate(ai[1]);
                    if (isNaN(aj))
                    {
                        return
                    }
                }
                else
                {
                    var aj = new Date();
                    aj.setMilliseconds(0)
                }
                var ah = parseFloat(ai[2]);
                aj.setHours(ah, parseInt(ai[3], 10), parseInt(ai[4], 10),
                    parseFloat(ai[5]) * 1000);
                return aj
            }
            else
            {
                return ak
            }
        },
        function (al) {
            var aj = al
                .match(/^([0-3]?\d)\s*[-\/.\s]{1}\s*([a-zA-Z]{3,9})\s*[-\/.\s]{1}\s*([0-3]?\d)$/);
            if (aj)
            {
                var ak = new Date();
                var am = ag.config.defaultCentury;
                var ao = parseFloat(aj[1]);
                var an = parseFloat(aj[3]);
                var ai, ah, ap;
                if (ao > 31)
                {
                    ah = an;
                    ai = am + ao
                }
                else
                {
                    ah = ao;
                    ai = am + an
                }
                var ap = ab(
                    aj[2],
                    ag.regional[ag.regional.getLocale()]["monthNamesShort"]);
                if (ap == -1)
                {
                    ap = ab(
                        aj[2],
                        ag.regional[ag.regional.getLocale()]["monthNames"])
                }
                ak.setFullYear(ai, ap, ah);
                ak.setHours(0, 0, 0, 0);
                return ak
            }
            else
            {
                return al
            }
        }];

    function ab(aj, ak)
    {
        if (ak.indexOf)
        {
            return ak.indexOf(aj)
        }
        for (var ah = 0, ai = ak.length; ah < ai; ah++)
        {
            if (ak[ah] === aj)
            {
                return ah
            }
        }
        return -1
    }

    function l(ah)
    {
        if (ah === null)
        {
            return "[object Null]"
        }
        return Object.prototype.toString.call(ah)
    }

    L.jsDate = ag;
    L.jqplot.sprintf = function () {
        function an(au, ap, aq, at)
        {
            var ar = (au.length >= ap) ? "" : Array(1 + ap - au.length >>> 0)
                .join(aq);
            return at ? au + ar : ar + au
        }

        function ak(ar)
        {
            var aq = new String(ar);
            for (var ap = 10; ap > 0; ap--)
            {
                if (aq == (aq = aq.replace(/^(\d+)(\d{3})/, "$1"
                    + L.jqplot.sprintf.thousandsSeparator + "$2")))
                {
                    break
                }
            }
            return aq
        }

        function aj(av, au, ax, ar, at, aq)
        {
            var aw = ar - av.length;
            if (aw > 0)
            {
                var ap = " ";
                if (aq)
                {
                    ap = "&nbsp;"
                }
                if (ax || !at)
                {
                    av = an(av, ar, ap, ax)
                }
                else
                {
                    av = av.slice(0, au.length) + an("", aw, "0", true)
                        + av.slice(au.length)
                }
            }
            return av
        }

        function ao(ay, aq, aw, ar, ap, av, ax, au)
        {
            var at = ay >>> 0;
            aw = aw && at && {
                "2": "0b",
                "8": "0",
                "16": "0x"
            }[aq] || "";
            ay = aw + an(at.toString(aq), av || 0, "0", false);
            return aj(ay, aw, ar, ap, ax, au)
        }

        function ah(au, av, ar, ap, at, aq)
        {
            if (ap != null)
            {
                au = au.slice(0, ap)
            }
            return aj(au, "", av, ar, at, aq)
        }

        var ai = arguments, al = 0, am = ai[al++];
        return am
            .replace(
                L.jqplot.sprintf.regex,
                function (aM, ax, ay, aB, aO, aJ, av) {
                    if (aM == "%%")
                    {
                        return "%"
                    }
                    var aD = false, az = "", aA = false, aL = false, aw = false, au = false;
                    for (var aI = 0; ay && aI < ay.length; aI++)
                    {
                        switch (ay.charAt(aI))
                        {
                            case " ":
                                az = " ";
                                break;
                            case "+":
                                az = "+";
                                break;
                            case "-":
                                aD = true;
                                break;
                            case "0":
                                aA = true;
                                break;
                            case "#":
                                aL = true;
                                break;
                            case "&":
                                aw = true;
                                break;
                            case "'":
                                au = true;
                                break
                        }
                    }
                    if (!aB)
                    {
                        aB = 0
                    }
                    else
                    {
                        if (aB == "*")
                        {
                            aB = +ai[al++]
                        }
                        else
                        {
                            if (aB.charAt(0) == "*")
                            {
                                aB = +ai[aB.slice(1, -1)]
                            }
                            else
                            {
                                aB = +aB
                            }
                        }
                    }
                    if (aB < 0)
                    {
                        aB = -aB;
                        aD = true
                    }
                    if (!isFinite(aB))
                    {
                        throw new Error(
                            "$.jqplot.sprintf: (minimum-)width must be finite")
                    }
                    if (!aJ)
                    {
                        aJ = "fFeE".indexOf(av) > -1 ? 6
                            : (av == "d") ? 0 : void (0)
                    }
                    else
                    {
                        if (aJ == "*")
                        {
                            aJ = +ai[al++]
                        }
                        else
                        {
                            if (aJ.charAt(0) == "*")
                            {
                                aJ = +ai[aJ.slice(1, -1)]
                            }
                            else
                            {
                                aJ = +aJ
                            }
                        }
                    }
                    var aF = ax ? ai[ax.slice(0, -1)] : ai[al++];
                    switch (av)
                    {
                        case "s":
                            if (aF == null)
                            {
                                return ""
                            }
                            return ah(String(aF), aD, aB, aJ, aA, aw);
                        case "c":
                            return ah(String.fromCharCode(+aF), aD, aB, aJ,
                                aA, aw);
                        case "b":
                            return ao(aF, 2, aL, aD, aB, aJ, aA, aw);
                        case "o":
                            return ao(aF, 8, aL, aD, aB, aJ, aA, aw);
                        case "x":
                            return ao(aF, 16, aL, aD, aB, aJ, aA, aw);
                        case "X":
                            return ao(aF, 16, aL, aD, aB, aJ, aA, aw)
                                .toUpperCase();
                        case "u":
                            return ao(aF, 10, aL, aD, aB, aJ, aA, aw);
                        case "i":
                            var ar = parseInt(+aF, 10);
                            if (isNaN(ar))
                            {
                                return ""
                            }
                            var aH = ar < 0 ? "-" : az;
                            var aK = au ? ak(String(Math.abs(ar)))
                                : String(Math.abs(ar));
                            aF = aH + an(aK, aJ, "0", false);
                            return aj(aF, aH, aD, aB, aA, aw);
                        case "d":
                            var ar = Math.round(+aF);
                            if (isNaN(ar))
                            {
                                return ""
                            }
                            var aH = ar < 0 ? "-" : az;
                            var aK = au ? ak(String(Math.abs(ar)))
                                : String(Math.abs(ar));
                            aF = aH + an(aK, aJ, "0", false);
                            return aj(aF, aH, aD, aB, aA, aw);
                        case "e":
                        case "E":
                        case "f":
                        case "F":
                        case "g":
                        case "G":
                            var ar = +aF;
                            if (isNaN(ar))
                            {
                                return ""
                            }
                            var aH = ar < 0 ? "-" : az;
                            var at = ["toExponential", "toFixed",
                                "toPrecision"]["efg".indexOf(av
                                .toLowerCase())];
                            var aN = ["toString", "toUpperCase"]["eEfFgG"
                                .indexOf(av) % 2];
                            var aK = Math.abs(ar)[at](aJ);
                            var aE = aK.toString().split(".");
                            aE[0] = au ? ak(aE[0]) : aE[0];
                            aK = aE.join(L.jqplot.sprintf.decimalMark);
                            aF = aH + aK;
                            var aC = aj(aF, aH, aD, aB, aA, aw)[aN]();
                            return aC;
                        case "p":
                        case "P":
                            var ar = +aF;
                            if (isNaN(ar))
                            {
                                return ""
                            }
                            var aH = ar < 0 ? "-" : az;
                            var aE = String(
                                Number(Math.abs(ar)).toExponential())
                                .split(/e|E/);
                            var aq = (aE[0].indexOf(".") != -1) ? aE[0].length - 1
                                : String(ar).length;
                            var aG = (aE[1] < 0) ? -aE[1] - 1 : 0;
                            if (Math.abs(ar) < 1)
                            {
                                if (aq + aG <= aJ)
                                {
                                    aF = aH + Math.abs(ar).toPrecision(aq)
                                }
                                else
                                {
                                    if (aq <= aJ - 1)
                                    {
                                        aF = aH
                                            + Math.abs(ar)
                                                .toExponential(
                                                    aq - 1)
                                    }
                                    else
                                    {
                                        aF = aH
                                            + Math.abs(ar)
                                                .toExponential(
                                                    aJ - 1)
                                    }
                                }
                            }
                            else
                            {
                                var ap = (aq <= aJ) ? aq : aJ;
                                aF = aH + Math.abs(ar).toPrecision(ap)
                            }
                            var aN = ["toString", "toUpperCase"]["pP"
                                .indexOf(av) % 2];
                            return aj(aF, aH, aD, aB, aA, aw)[aN]();
                        case "n":
                            return "";
                        default:
                            return aM
                    }
                })
    };
    L.jqplot.sprintf.thousandsSeparator = ",";
    L.jqplot.sprintf.decimalMark = ".";
    L.jqplot.sprintf.regex = /%%|%(\d+\$)?([-+#0&\' ]*)(\*\d+\$|\*|\d+)?(\.(\*\d+\$|\*|\d+))?([nAscboxXuidfegpEGP])/g;
    L.jqplot.getSignificantFigures = function (al) {
        var an = String(Number(Math.abs(al)).toExponential()).split(/e|E/);
        var am = (an[0].indexOf(".") != -1) ? an[0].length - 1 : an[0].length;
        var ai = (an[1] < 0) ? -an[1] - 1 : 0;
        var ah = parseInt(an[1], 10);
        var aj = (ah + 1 > 0) ? ah + 1 : 0;
        var ak = (am <= aj) ? 0 : am - ah - 1;
        return {
            significantDigits: am,
            digitsLeft: aj,
            digitsRight: ak,
            zeros: ai,
            exponent: ah
        }
    };
    L.jqplot.getPrecision = function (ah) {
        return L.jqplot.getSignificantFigures(ah).digitsRight
    };
    var X = L.uiBackCompat !== false;
    L.jqplot.effects = {
        effect: {}
    };
    var m = "jqplot.storage.";
    L.extend(L.jqplot.effects, {
        version: "1.9pre",
        save: function (ai, aj) {
            for (var ah = 0; ah < aj.length; ah++)
            {
                if (aj[ah] !== null)
                {
                    ai.data(m + aj[ah], ai[0].style[aj[ah]])
                }
            }
        },
        restore: function (ai, aj) {
            for (var ah = 0; ah < aj.length; ah++)
            {
                if (aj[ah] !== null)
                {
                    ai.css(aj[ah], ai.data(m + aj[ah]))
                }
            }
        },
        setMode: function (ah, ai) {
            if (ai === "toggle")
            {
                ai = ah.is(":hidden") ? "show" : "hide"
            }
            return ai
        },
        createWrapper: function (ai) {
            if (ai.parent().is(".ui-effects-wrapper"))
            {
                return ai.parent()
            }
            var aj = {
                width: ai.outerWidth(true),
                height: ai.outerHeight(true),
                "float": ai.css("float")
            }, al = L("<div></div>").addClass("ui-effects-wrapper").css({
                fontSize: "100%",
                background: "transparent",
                border: "none",
                margin: 0,
                padding: 0
            }), ah = {
                width: ai.width(),
                height: ai.height()
            }, ak = document.activeElement;
            ai.wrap(al);
            if (ai[0] === ak || L.contains(ai[0], ak))
            {
                L(ak).focus()
            }
            al = ai.parent();
            if (ai.css("position") === "static")
            {
                al.css({
                    position: "relative"
                });
                ai.css({
                    position: "relative"
                })
            }
            else
            {
                L.extend(aj, {
                    position: ai.css("position"),
                    zIndex: ai.css("z-index")
                });
                L.each(["top", "left", "bottom", "right"], function (am, an) {
                    aj[an] = ai.css(an);
                    if (isNaN(parseInt(aj[an], 10)))
                    {
                        aj[an] = "auto"
                    }
                });
                ai.css({
                    position: "relative",
                    top: 0,
                    left: 0,
                    right: "auto",
                    bottom: "auto"
                })
            }
            ai.css(ah);
            return al.css(aj).show()
        },
        removeWrapper: function (ah) {
            var ai = document.activeElement;
            if (ah.parent().is(".ui-effects-wrapper"))
            {
                ah.parent().replaceWith(ah);
                if (ah[0] === ai || L.contains(ah[0], ai))
                {
                    L(ai).focus()
                }
            }
            return ah
        }
    });

    function j(ai, ah, aj, ak)
    {
        if (L.isPlainObject(ai))
        {
            return ai
        }
        ai = {
            effect: ai
        };
        if (ah === u)
        {
            ah = {}
        }
        if (L.isFunction(ah))
        {
            ak = ah;
            aj = null;
            ah = {}
        }
        if (L.type(ah) === "number" || L.fx.speeds[ah])
        {
            ak = aj;
            aj = ah;
            ah = {}
        }
        if (L.isFunction(aj))
        {
            ak = aj;
            aj = null
        }
        if (ah)
        {
            L.extend(ai, ah)
        }
        aj = aj || ah.duration;
        ai.duration = L.fx.off ? 0 : typeof aj === "number" ? aj
            : aj in L.fx.speeds ? L.fx.speeds[aj] : L.fx.speeds._default;
        ai.complete = ak || ah.complete;
        return ai
    }

    function ae(ah)
    {
        if (!ah || typeof ah === "number" || L.fx.speeds[ah])
        {
            return true
        }
        if (typeof ah === "string" && !L.jqplot.effects.effect[ah])
        {
            if (X && L.jqplot.effects[ah])
            {
                return false
            }
            return true
        }
        return false
    }

    L.fn
        .extend({
            jqplotEffect: function (ap, aq, ai, ao) {
                var an = j.apply(this, arguments), ak = an.mode, al = an.queue, am = L.jqplot.effects.effect[an.effect],
                    ah = !am
                        && X && L.jqplot.effects[an.effect];
                if (L.fx.off || !(am || ah))
                {
                    if (ak)
                    {
                        return this[ak](an.duration, an.complete)
                    }
                    else
                    {
                        return this.each(function () {
                            if (an.complete)
                            {
                                an.complete.call(this)
                            }
                        })
                    }
                }

                function aj(au)
                {
                    var av = L(this), at = an.complete, aw = an.mode;

                    function ar()
                    {
                        if (L.isFunction(at))
                        {
                            at.call(av[0])
                        }
                        if (L.isFunction(au))
                        {
                            au()
                        }
                    }

                    if (av.is(":hidden") ? aw === "hide" : aw === "show")
                    {
                        ar()
                    }
                    else
                    {
                        am.call(av[0], an, ar)
                    }
                }

                if (am)
                {
                    return al === false ? this.each(aj) : this.queue(al
                        || "fx", aj)
                }
                else
                {
                    return ah.call(this, {
                        options: an,
                        duration: an.duration,
                        callback: an.complete,
                        mode: an.mode
                    })
                }
            }
        });
    var a = /up|down|vertical/, v = /up|left|vertical|horizontal/;
    L.jqplot.effects.effect.blind = function (aj, ao) {
        var ak = L(this), ar = ["position", "top", "bottom", "left", "right",
            "height", "width"], ap = L.jqplot.effects.setMode(ak, aj.mode
            || "hide"), au = aj.direction || "up", am = a.test(au), al = am ? "height"
            : "width", aq = am ? "top" : "left", aw = v.test(au), an = {}, av = ap === "show", ai, ah, at;
        if (ak.parent().is(".ui-effects-wrapper"))
        {
            L.jqplot.effects.save(ak.parent(), ar)
        }
        else
        {
            L.jqplot.effects.save(ak, ar)
        }
        ak.show();
        at = parseInt(ak.css("top"), 10);
        ai = L.jqplot.effects.createWrapper(ak).css({
            overflow: "hidden"
        });
        ah = am ? ai[al]() + at : ai[al]();
        an[al] = av ? String(ah) : "0";
        if (!aw)
        {
            ak.css(am ? "bottom" : "right", 0).css(am ? "top" : "left", "")
                .css({
                    position: "absolute"
                });
            an[aq] = av ? "0" : String(ah)
        }
        if (av)
        {
            ai.css(al, 0);
            if (!aw)
            {
                ai.css(aq, ah)
            }
        }
        ai.animate(an, {
            duration: aj.duration,
            easing: aj.easing,
            queue: false,
            complete: function () {
                if (ap === "hide")
                {
                    ak.hide()
                }
                L.jqplot.effects.restore(ak, ar);
                L.jqplot.effects.removeWrapper(ak);
                ao()
            }
        })
    }
})(jQuery);
(function (a) {
    a.jqplot.CategoryAxisRenderer = function (b) {
        a.jqplot.LinearAxisRenderer.call(this);
        this.sortMergedLabels = false
    };
    a.jqplot.CategoryAxisRenderer.prototype = new a.jqplot.LinearAxisRenderer();
    a.jqplot.CategoryAxisRenderer.prototype.constructor = a.jqplot.CategoryAxisRenderer;
    a.jqplot.CategoryAxisRenderer.prototype.init = function (e) {
        this.groups = 1;
        this.groupLabels = [];
        this._groupLabels = [];
        this._grouped = false;
        this._barsPerGroup = null;
        this.reverse = false;
        a.extend(true, this, {
            tickOptions: {
                formatString: "%d"
            }
        }, e);
        var b = this._dataBounds;
        for (var f = 0; f < this._series.length; f++)
        {
            var g = this._series[f];
            if (g.groups)
            {
                this.groups = g.groups
            }
            var h = g.data;
            for (var c = 0; c < h.length; c++)
            {
                if (this.name == "xaxis" || this.name == "x2axis")
                {
                    if (h[c][0] < b.min || b.min == null)
                    {
                        b.min = h[c][0]
                    }
                    if (h[c][0] > b.max || b.max == null)
                    {
                        b.max = h[c][0]
                    }
                }
                else
                {
                    if (h[c][1] < b.min || b.min == null)
                    {
                        b.min = h[c][1]
                    }
                    if (h[c][1] > b.max || b.max == null)
                    {
                        b.max = h[c][1]
                    }
                }
            }
        }
        if (this.groupLabels.length)
        {
            this.groups = this.groupLabels.length
        }
    };
    a.jqplot.CategoryAxisRenderer.prototype.createTicks = function () {
        var D = this._ticks;
        var z = this.ticks;
        var F = this.name;
        var C = this._dataBounds;
        var v, A;
        var q, w;
        var d, c;
        var b, x;
        if (z.length)
        {
            if (this.groups > 1 && !this._grouped)
            {
                var r = z.length;
                var p = parseInt(r / this.groups, 10);
                var e = 0;
                for (var x = p; x < r; x += p)
                {
                    z.splice(x + e, 0, " ");
                    e++
                }
                this._grouped = true
            }
            this.min = 0.5;
            this.max = z.length + 0.5;
            var m = this.max - this.min;
            this.numberTicks = 2 * z.length + 1;
            for (x = 0; x < z.length; x++)
            {
                b = this.min + 2 * x * m / (this.numberTicks - 1);
                var h = new this.tickRenderer(this.tickOptions);
                h.showLabel = false;
                h.setTick(b, this.name);
                this._ticks.push(h);
                var h = new this.tickRenderer(this.tickOptions);
                h.label = z[x];
                h.showMark = false;
                h.showGridline = false;
                h.setTick(b + 0.5, this.name);
                this._ticks.push(h)
            }
            var h = new this.tickRenderer(this.tickOptions);
            h.showLabel = false;
            h.setTick(b + 1, this.name);
            this._ticks.push(h)
        }
        else
        {
            if (F == "xaxis" || F == "x2axis")
            {
                v = this._plotDimensions.width
            }
            else
            {
                v = this._plotDimensions.height
            }
            if (this.min != null && this.max != null
                && this.numberTicks != null)
            {
                this.tickInterval = null
            }
            if (this.min != null && this.max != null
                && this.tickInterval != null)
            {
                if (parseInt((this.max - this.min) / this.tickInterval, 10) != (this.max - this.min)
                    / this.tickInterval)
                {
                    this.tickInterval = null
                }
            }
            var y = [];
            var B = 0;
            var q = 0.5;
            var w, E;
            var f = false;
            for (var x = 0; x < this._series.length; x++)
            {
                var k = this._series[x];
                for (var u = 0; u < k.data.length; u++)
                {
                    if (this.name == "xaxis" || this.name == "x2axis")
                    {
                        E = k.data[u][0]
                    }
                    else
                    {
                        E = k.data[u][1]
                    }
                    if (a.inArray(E, y) == -1)
                    {
                        f = true;
                        B += 1;
                        y.push(E)
                    }
                }
            }
            if (f && this.sortMergedLabels)
            {
                if (typeof y[0] == "string")
                {
                    y.sort()
                }
                else
                {
                    y.sort(function (j, i) {
                        return j - i
                    })
                }
            }
            this.ticks = y;
            for (var x = 0; x < this._series.length; x++)
            {
                var k = this._series[x];
                for (var u = 0; u < k.data.length; u++)
                {
                    if (this.name == "xaxis" || this.name == "x2axis")
                    {
                        E = k.data[u][0]
                    }
                    else
                    {
                        E = k.data[u][1]
                    }
                    var n = a.inArray(E, y) + 1;
                    if (this.name == "xaxis" || this.name == "x2axis")
                    {
                        k.data[u][0] = n
                    }
                    else
                    {
                        k.data[u][1] = n
                    }
                }
            }
            if (this.groups > 1 && !this._grouped)
            {
                var r = y.length;
                var p = parseInt(r / this.groups, 10);
                var e = 0;
                for (var x = p; x < r; x += p + 1)
                {
                    y[x] = " "
                }
                this._grouped = true
            }
            w = B + 0.5;
            if (this.numberTicks == null)
            {
                this.numberTicks = 2 * B + 1
            }
            var m = w - q;
            this.min = q;
            this.max = w;
            var o = 0;
            var g = parseInt(3 + v / 10, 10);
            var p = parseInt(B / g, 10);
            if (this.tickInterval == null)
            {
                this.tickInterval = m / (this.numberTicks - 1)
            }
            for (var x = 0; x < this.numberTicks; x++)
            {
                b = this.min + x * this.tickInterval;
                var h = new this.tickRenderer(this.tickOptions);
                if (x / 2 == parseInt(x / 2, 10))
                {
                    h.showLabel = false;
                    h.showMark = true
                }
                else
                {
                    if (p > 0 && o < p)
                    {
                        h.showLabel = false;
                        o += 1
                    }
                    else
                    {
                        h.showLabel = true;
                        o = 0
                    }
                    h.label = h.formatter(h.formatString, y[(x - 1) / 2]);
                    h.showMark = false;
                    h.showGridline = false
                }
                h.setTick(b, this.name);
                this._ticks.push(h)
            }
        }
    };
    a.jqplot.CategoryAxisRenderer.prototype.draw = function (b, j) {
        if (this.show)
        {
            this.renderer.createTicks.call(this);
            var h = 0;
            var c;
            if (this._elem)
            {
                this._elem.emptyForce()
            }
            this._elem = this._elem
                || a('<div class="jqplot-axis jqplot-' + this.name
                    + '" style="position:absolute;"></div>');
            if (this.name == "xaxis" || this.name == "x2axis")
            {
                this._elem.width(this._plotDimensions.width)
            }
            else
            {
                this._elem.height(this._plotDimensions.height)
            }
            this.labelOptions.axis = this.name;
            this._label = new this.labelRenderer(this.labelOptions);
            if (this._label.show)
            {
                var g = this._label.draw(b, j);
                g.appendTo(this._elem)
            }
            var f = this._ticks;
            for (var e = 0; e < f.length; e++)
            {
                var d = f[e];
                if (d.showLabel && (!d.isMinorTick || this.showMinorTicks))
                {
                    var g = d.draw(b, j);
                    g.appendTo(this._elem)
                }
            }
            this._groupLabels = [];
            for (var e = 0; e < this.groupLabels.length; e++)
            {
                var g = a('<div style="position:absolute;" class="jqplot-'
                    + this.name + '-groupLabel"></div>');
                g.html(this.groupLabels[e]);
                this._groupLabels.push(g);
                g.appendTo(this._elem)
            }
        }
        return this._elem
    };
    a.jqplot.CategoryAxisRenderer.prototype.set = function () {
        var e = 0;
        var m;
        var k = 0;
        var f = 0;
        var d = (this._label == null) ? false : this._label.show;
        if (this.show)
        {
            var n = this._ticks;
            for (var c = 0; c < n.length; c++)
            {
                var g = n[c];
                if (g.showLabel && (!g.isMinorTick || this.showMinorTicks))
                {
                    if (this.name == "xaxis" || this.name == "x2axis")
                    {
                        m = g._elem.outerHeight(true)
                    }
                    else
                    {
                        m = g._elem.outerWidth(true)
                    }
                    if (m > e)
                    {
                        e = m
                    }
                }
            }
            var j = 0;
            for (var c = 0; c < this._groupLabels.length; c++)
            {
                var b = this._groupLabels[c];
                if (this.name == "xaxis" || this.name == "x2axis")
                {
                    m = b.outerHeight(true)
                }
                else
                {
                    m = b.outerWidth(true)
                }
                if (m > j)
                {
                    j = m
                }
            }
            if (d)
            {
                k = this._label._elem.outerWidth(true);
                f = this._label._elem.outerHeight(true)
            }
            if (this.name == "xaxis")
            {
                e += j + f;
                this._elem.css({
                    height: e + "px",
                    left: "0px",
                    bottom: "0px"
                })
            }
            else
            {
                if (this.name == "x2axis")
                {
                    e += j + f;
                    this._elem.css({
                        height: e + "px",
                        left: "0px",
                        top: "0px"
                    })
                }
                else
                {
                    if (this.name == "yaxis")
                    {
                        e += j + k;
                        this._elem.css({
                            width: e + "px",
                            left: "0px",
                            top: "0px"
                        });
                        if (d
                            && this._label.constructor == a.jqplot.AxisLabelRenderer)
                        {
                            this._label._elem.css("width", k + "px")
                        }
                    }
                    else
                    {
                        e += j + k;
                        this._elem.css({
                            width: e + "px",
                            right: "0px",
                            top: "0px"
                        });
                        if (d
                            && this._label.constructor == a.jqplot.AxisLabelRenderer)
                        {
                            this._label._elem.css("width", k + "px")
                        }
                    }
                }
            }
        }
    };
    a.jqplot.CategoryAxisRenderer.prototype.pack = function (e, c) {
        var C = this._ticks;
        var v = this.max;
        var s = this.min;
        var n = c.max;
        var l = c.min;
        var q = (this._label == null) ? false : this._label.show;
        var x;
        for (var r in e)
        {
            this._elem.css(r, e[r])
        }
        this._offsets = c;
        var g = n - l;
        var k = v - s;
        if (!this.reverse)
        {
            this.u2p = function (h) {
                return (h - s) * g / k + l
            };
            this.p2u = function (h) {
                return (h - l) * k / g + s
            };
            if (this.name == "xaxis" || this.name == "x2axis")
            {
                this.series_u2p = function (h) {
                    return (h - s) * g / k
                };
                this.series_p2u = function (h) {
                    return h * k / g + s
                }
            }
            else
            {
                this.series_u2p = function (h) {
                    return (h - v) * g / k
                };
                this.series_p2u = function (h) {
                    return h * k / g + v
                }
            }
        }
        else
        {
            this.u2p = function (h) {
                return l + (v - h) * g / k
            };
            this.p2u = function (h) {
                return s + (h - l) * k / g
            };
            if (this.name == "xaxis" || this.name == "x2axis")
            {
                this.series_u2p = function (h) {
                    return (v - h) * g / k
                };
                this.series_p2u = function (h) {
                    return h * k / g + v
                }
            }
            else
            {
                this.series_u2p = function (h) {
                    return (s - h) * g / k
                };
                this.series_p2u = function (h) {
                    return h * k / g + s
                }
            }
        }
        if (this.show)
        {
            if (this.name == "xaxis" || this.name == "x2axis")
            {
                for (x = 0; x < C.length; x++)
                {
                    var o = C[x];
                    if (o.show && o.showLabel)
                    {
                        var b;
                        if (o.constructor == a.jqplot.CanvasAxisTickRenderer
                            && o.angle)
                        {
                            var A = (this.name == "xaxis") ? 1 : -1;
                            switch (o.labelPosition)
                            {
                                case "auto":
                                    if (A * o.angle < 0)
                                    {
                                        b = -o.getWidth() + o._textRenderer.height
                                            * Math.sin(-o._textRenderer.angle)
                                            / 2
                                    }
                                    else
                                    {
                                        b = -o._textRenderer.height
                                            * Math.sin(o._textRenderer.angle)
                                            / 2
                                    }
                                    break;
                                case "end":
                                    b = -o.getWidth() + o._textRenderer.height
                                        * Math.sin(-o._textRenderer.angle) / 2;
                                    break;
                                case "start":
                                    b = -o._textRenderer.height
                                        * Math.sin(o._textRenderer.angle) / 2;
                                    break;
                                case "middle":
                                    b = -o.getWidth() / 2 + o._textRenderer.height
                                        * Math.sin(-o._textRenderer.angle) / 2;
                                    break;
                                default:
                                    b = -o.getWidth() / 2 + o._textRenderer.height
                                        * Math.sin(-o._textRenderer.angle) / 2;
                                    break
                            }
                        }
                        else
                        {
                            b = -o.getWidth() / 2
                        }
                        var D = this.u2p(o.value) + b + "px";
                        o._elem.css("left", D);
                        o.pack()
                    }
                }
                var z = ["bottom", 0];
                if (q)
                {
                    var m = this._label._elem.outerWidth(true);
                    this._label._elem.css("left", l + g / 2 - m / 2 + "px");
                    if (this.name == "xaxis")
                    {
                        this._label._elem.css("bottom", "0px");
                        z = ["bottom", this._label._elem.outerHeight(true)]
                    }
                    else
                    {
                        this._label._elem.css("top", "0px");
                        z = ["top", this._label._elem.outerHeight(true)]
                    }
                    this._label.pack()
                }
                var d = parseInt(this._ticks.length / this.groups, 10) + 1;
                for (x = 0; x < this._groupLabels.length; x++)
                {
                    var B = 0;
                    var f = 0;
                    for (var u = x * d; u < (x + 1) * d; u++)
                    {
                        if (u >= this._ticks.length - 1)
                        {
                            continue
                        }
                        if (this._ticks[u]._elem && this._ticks[u].label != " ")
                        {
                            var o = this._ticks[u]._elem;
                            var r = o.position();
                            B += r.left + o.outerWidth(true) / 2;
                            f++
                        }
                    }
                    B = B / f;
                    this._groupLabels[x].css({
                        left: (B - this._groupLabels[x].outerWidth(true) / 2)
                    });
                    this._groupLabels[x].css(z[0], z[1])
                }
            }
            else
            {
                for (x = 0; x < C.length; x++)
                {
                    var o = C[x];
                    if (o.show && o.showLabel)
                    {
                        var b;
                        if (o.constructor == a.jqplot.CanvasAxisTickRenderer
                            && o.angle)
                        {
                            var A = (this.name == "yaxis") ? 1 : -1;
                            switch (o.labelPosition)
                            {
                                case "auto":
                                case "end":
                                    if (A * o.angle < 0)
                                    {
                                        b = -o._textRenderer.height
                                            * Math.cos(-o._textRenderer.angle)
                                            / 2
                                    }
                                    else
                                    {
                                        b = -o.getHeight() + o._textRenderer.height
                                            * Math.cos(o._textRenderer.angle)
                                            / 2
                                    }
                                    break;
                                case "start":
                                    if (o.angle > 0)
                                    {
                                        b = -o._textRenderer.height
                                            * Math.cos(-o._textRenderer.angle)
                                            / 2
                                    }
                                    else
                                    {
                                        b = -o.getHeight() + o._textRenderer.height
                                            * Math.cos(o._textRenderer.angle)
                                            / 2
                                    }
                                    break;
                                case "middle":
                                    b = -o.getHeight() / 2;
                                    break;
                                default:
                                    b = -o.getHeight() / 2;
                                    break
                            }
                        }
                        else
                        {
                            b = -o.getHeight() / 2
                        }
                        var D = this.u2p(o.value) + b + "px";
                        o._elem.css("top", D);
                        o.pack()
                    }
                }
                var z = ["left", 0];
                if (q)
                {
                    var y = this._label._elem.outerHeight(true);
                    this._label._elem.css("top", n - g / 2 - y / 2 + "px");
                    if (this.name == "yaxis")
                    {
                        this._label._elem.css("left", "0px");
                        z = ["left", this._label._elem.outerWidth(true)]
                    }
                    else
                    {
                        this._label._elem.css("right", "0px");
                        z = ["right", this._label._elem.outerWidth(true)]
                    }
                    this._label.pack()
                }
                var d = parseInt(this._ticks.length / this.groups, 10) + 1;
                for (x = 0; x < this._groupLabels.length; x++)
                {
                    var B = 0;
                    var f = 0;
                    for (var u = x * d; u < (x + 1) * d; u++)
                    {
                        if (u >= this._ticks.length - 1)
                        {
                            continue
                        }
                        if (this._ticks[u]._elem && this._ticks[u].label != " ")
                        {
                            var o = this._ticks[u]._elem;
                            var r = o.position();
                            B += r.top + o.outerHeight() / 2;
                            f++
                        }
                    }
                    B = B / f;
                    this._groupLabels[x].css({
                        top: B - this._groupLabels[x].outerHeight() / 2
                    });
                    this._groupLabels[x].css(z[0], z[1])
                }
            }
        }
    }
})(jQuery);
(function (e) {
    e.jqplot.PieRenderer = function () {
        e.jqplot.LineRenderer.call(this)
    };
    e.jqplot.PieRenderer.prototype = new e.jqplot.LineRenderer();
    e.jqplot.PieRenderer.prototype.constructor = e.jqplot.PieRenderer;
    e.jqplot.PieRenderer.prototype.init = function (q, u) {
        this.diameter = null;
        this.padding = 20;
        this.sliceMargin = 0;
        this.fill = true;
        this.shadowOffset = 2;
        this.shadowAlpha = 0.07;
        this.shadowDepth = 5;
        this.highlightMouseOver = true;
        this.highlightMouseDown = false;
        this.highlightColors = [];
        this.dataLabels = "percent";
        this.showDataLabels = false;
        this.dataLabelFormatString = null;
        this.dataLabelThreshold = 3;
        this.dataLabelPositionFactor = 0.52;
        this.dataLabelNudge = 2;
        this.dataLabelCenterOn = true;
        this.startAngle = 0;
        this.tickRenderer = e.jqplot.PieTickRenderer;
        this._drawData = true;
        this._type = "pie";
        if (q.highlightMouseDown && q.highlightMouseOver == null)
        {
            q.highlightMouseOver = false
        }
        e.extend(true, this, q);
        if (this.sliceMargin < 0)
        {
            this.sliceMargin = 0
        }
        this._diameter = null;
        this._radius = null;
        this._sliceAngles = [];
        this._highlightedPoint = null;
        if (this.highlightColors.length == 0)
        {
            for (var s = 0; s < this.seriesColors.length; s++)
            {
                var r = e.jqplot.getColorComponents(this.seriesColors[s]);
                var o = [r[0], r[1], r[2]];
                var t = o[0] + o[1] + o[2];
                for (var p = 0; p < 3; p++)
                {
                    o[p] = (t > 570) ? o[p] * 0.8 : o[p] + 0.3 * (255 - o[p]);
                    o[p] = parseInt(o[p], 10)
                }
                this.highlightColors.push("rgb(" + o[0] + "," + o[1] + ","
                    + o[2] + ")")
            }
        }
        this.highlightColorGenerator = new e.jqplot.ColorGenerator(
            this.highlightColors);
        u.postParseOptionsHooks.addOnce(m);
        u.postInitHooks.addOnce(g);
        u.eventListenerHooks.addOnce("jqplotMouseMove", b);
        u.eventListenerHooks.addOnce("jqplotMouseDown", a);
        u.eventListenerHooks.addOnce("jqplotMouseUp", l);
        u.eventListenerHooks.addOnce("jqplotClick", f);
        u.eventListenerHooks.addOnce("jqplotRightClick", n);
        u.postDrawHooks.addOnce(i)
    };
    e.jqplot.PieRenderer.prototype.setGridData = function (t) {
        var p = [];
        var u = [];
        var o = this.startAngle / 180 * Math.PI;
        var s = 0;
        this._drawData = false;
        for (var r = 0; r < this.data.length; r++)
        {
            if (this.data[r][1] != 0)
            {
                this._drawData = true
            }
            p.push(this.data[r][1]);
            u.push([this.data[r][0]]);
            if (r > 0)
            {
                p[r] += p[r - 1]
            }
            s += this.data[r][1]
        }
        var q = Math.PI * 2 / p[p.length - 1];
        for (var r = 0; r < p.length; r++)
        {
            u[r][1] = p[r] * q;
            u[r][2] = this.data[r][1] / s
        }
        this.gridData = u
    };
    e.jqplot.PieRenderer.prototype.makeGridData = function (t, u) {
        var p = [];
        var v = [];
        var s = 0;
        var o = this.startAngle / 180 * Math.PI;
        this._drawData = false;
        for (var r = 0; r < t.length; r++)
        {
            if (this.data[r][1] != 0)
            {
                this._drawData = true
            }
            p.push(t[r][1]);
            v.push([t[r][0]]);
            if (r > 0)
            {
                p[r] += p[r - 1]
            }
            s += t[r][1]
        }
        var q = Math.PI * 2 / p[p.length - 1];
        for (var r = 0; r < p.length; r++)
        {
            v[r][1] = p[r] * q;
            v[r][2] = t[r][1] / s
        }
        return v
    };

    function h(o)
    {
        return Math.sin((o - (o - Math.PI) / 8 / Math.PI) / 2)
    }

    function j(u, t, o, v, r)
    {
        var w = 0;
        var q = t - u;
        var s = Math.abs(q);
        var p = o;
        if (v == false)
        {
            p += r
        }
        if (p > 0 && s > 0.01 && s < 6.282)
        {
            w = parseFloat(p) / 2 / h(q)
        }
        return w
    }

    e.jqplot.PieRenderer.prototype.drawSlice = function (B, z, y, u, w) {
        if (this._drawData)
        {
            var p = this._radius;
            var A = this.fill;
            var x = this.lineWidth;
            var s = this.sliceMargin;
            if (this.fill == false)
            {
                s += this.lineWidth
            }
            B.save();
            B.translate(this._center[0], this._center[1]);
            var D = j(z, y, this.sliceMargin, this.fill, this.lineWidth);
            var o = D * Math.cos((z + y) / 2);
            var C = D * Math.sin((z + y) / 2);
            if ((y - z) <= Math.PI)
            {
                p -= D
            }
            else
            {
                p += D
            }
            B.translate(o, C);
            if (w)
            {
                for (var v = 0, t = this.shadowDepth; v < t; v++)
                {
                    B.save();
                    B.translate(this.shadowOffset
                        * Math.cos(this.shadowAngle / 180 * Math.PI),
                        this.shadowOffset
                        * Math
                            .sin(this.shadowAngle / 180
                                * Math.PI));
                    q(p)
                }
                for (var v = 0, t = this.shadowDepth; v < t; v++)
                {
                    B.restore()
                }
            }
            else
            {
                q(p)
            }
            B.restore()
        }

        function q(r)
        {
            if (y > 6.282 + this.startAngle)
            {
                y = 6.282 + this.startAngle;
                if (z > y)
                {
                    z = 6.281 + this.startAngle
                }
            }
            if (z >= y)
            {
                return
            }
            B.beginPath();
            B.fillStyle = u;
            B.strokeStyle = u;
            B.lineWidth = x;
            B.arc(0, 0, r, z, y, false);
            B.lineTo(0, 0);
            B.closePath();
            if (A)
            {
                B.fill()
            }
            else
            {
                B.stroke()
            }
        }
    };
    e.jqplot.PieRenderer.prototype.draw = function (B, z, E, o) {
        var W;
        var H = (E != undefined) ? E : {};
        var t = 0;
        var s = 0;
        var N = 1;
        var L = new e.jqplot.ColorGenerator(this.seriesColors);
        if (E.legendInfo && E.legendInfo.placement == "insideGrid")
        {
            var J = E.legendInfo;
            switch (J.location)
            {
                case "nw":
                    t = J.width + J.xoffset;
                    break;
                case "w":
                    t = J.width + J.xoffset;
                    break;
                case "sw":
                    t = J.width + J.xoffset;
                    break;
                case "ne":
                    t = J.width + J.xoffset;
                    N = -1;
                    break;
                case "e":
                    t = J.width + J.xoffset;
                    N = -1;
                    break;
                case "se":
                    t = J.width + J.xoffset;
                    N = -1;
                    break;
                case "n":
                    s = J.height + J.yoffset;
                    break;
                case "s":
                    s = J.height + J.yoffset;
                    N = -1;
                    break;
                default:
                    break
            }
        }
        var K = (H.shadow != undefined) ? H.shadow : this.shadow;
        var A = (H.fill != undefined) ? H.fill : this.fill;
        var C = B.canvas.width;
        var I = B.canvas.height;
        var Q = C - t - 2 * this.padding;
        var X = I - s - 2 * this.padding;
        var M = Math.min(Q, X);
        var Y = M;
        this._sliceAngles = [];
        var v = this.sliceMargin;
        if (this.fill == false)
        {
            v += this.lineWidth
        }
        var q;
        var G = 0;
        var R, aa, Z, ab;
        var D = this.startAngle / 180 * Math.PI;
        for (var W = 0, V = z.length; W < V; W++)
        {
            aa = (W == 0) ? D : z[W - 1][1] + D;
            Z = z[W][1] + D;
            this._sliceAngles.push([aa, Z]);
            q = j(aa, Z, this.sliceMargin, this.fill, this.lineWidth);
            if (Math.abs(Z - aa) > Math.PI)
            {
                G = Math.max(q, G)
            }
        }
        if (this.diameter != null && this.diameter > 0)
        {
            this._diameter = this.diameter - 2 * G
        }
        else
        {
            this._diameter = Y - 2 * G
        }
        if (this._diameter < 6)
        {
            e.jqplot.log("Diameter of pie too small, not rendering.");
            return
        }
        var S = this._radius = this._diameter / 2;
        this._center = [(C - N * t) / 2 + N * t + G * Math.cos(D),
            (I - N * s) / 2 + N * s + G * Math.sin(D)];
        if (this.shadow)
        {
            for (var W = 0, V = z.length; W < V; W++)
            {
                ab = "rgba(0,0,0," + this.shadowAlpha + ")";
                this.renderer.drawSlice.call(this, B, this._sliceAngles[W][0],
                    this._sliceAngles[W][1], ab, true)
            }
        }
        for (var W = 0; W < z.length; W++)
        {
            this.renderer.drawSlice.call(this, B, this._sliceAngles[W][0],
                this._sliceAngles[W][1], L.next(), false);
            if (this.showDataLabels && z[W][2] * 100 >= this.dataLabelThreshold)
            {
                var F, U = (this._sliceAngles[W][0] + this._sliceAngles[W][1]) / 2, T;
                if (this.dataLabels == "label")
                {
                    F = this.dataLabelFormatString || "%s";
                    T = e.jqplot.sprintf(F, z[W][0])
                }
                else
                {
                    if (this.dataLabels == "value")
                    {
                        F = this.dataLabelFormatString || "%d";
                        T = e.jqplot.sprintf(F, this.data[W][1])
                    }
                    else
                    {
                        if (this.dataLabels == "percent")
                        {
                            F = this.dataLabelFormatString || "%d%%";
                            T = e.jqplot.sprintf(F, z[W][2] * 100)
                        }
                        else
                        {
                            if (this.dataLabels.constructor == Array)
                            {
                                F = this.dataLabelFormatString || "%s";
                                T = e.jqplot.sprintf(F, this.dataLabels[W])
                            }
                        }
                    }
                }
                var p = (this._radius) * this.dataLabelPositionFactor
                    + this.sliceMargin + this.dataLabelNudge;
                var P = this._center[0] + Math.cos(U) * p
                    + this.canvas._offsets.left;
                var O = this._center[1] + Math.sin(U) * p
                    + this.canvas._offsets.top;
                var u = e(
                    '<div class="jqplot-pie-series jqplot-data-label" style="position:absolute;">'
                    + T + "</div>").insertBefore(
                    o.eventCanvas._elem);
                if (this.dataLabelCenterOn)
                {
                    P -= u.width() / 2;
                    O -= u.height() / 2
                }
                else
                {
                    P -= u.width() * Math.sin(U / 2);
                    O -= u.height() / 2
                }
                P = Math.round(P);
                O = Math.round(O);
                u.css({
                    left: P,
                    top: O
                })
            }
        }
    };
    e.jqplot.PieAxisRenderer = function () {
        e.jqplot.LinearAxisRenderer.call(this)
    };
    e.jqplot.PieAxisRenderer.prototype = new e.jqplot.LinearAxisRenderer();
    e.jqplot.PieAxisRenderer.prototype.constructor = e.jqplot.PieAxisRenderer;
    e.jqplot.PieAxisRenderer.prototype.init = function (o) {
        this.tickRenderer = e.jqplot.PieTickRenderer;
        e.extend(true, this, o);
        this._dataBounds = {
            min: 0,
            max: 100
        };
        this.min = 0;
        this.max = 100;
        this.showTicks = false;
        this.ticks = [];
        this.showMark = false;
        this.show = false
    };
    e.jqplot.PieLegendRenderer = function () {
        e.jqplot.TableLegendRenderer.call(this)
    };
    e.jqplot.PieLegendRenderer.prototype = new e.jqplot.TableLegendRenderer();
    e.jqplot.PieLegendRenderer.prototype.constructor = e.jqplot.PieLegendRenderer;
    e.jqplot.PieLegendRenderer.prototype.init = function (o) {
        this.numberRows = null;
        this.numberColumns = null;
        e.extend(true, this, o)
    };
    e.jqplot.PieLegendRenderer.prototype.draw = function () {
        var r = this;
        if (this.show)
        {
            var B = this._series;
            this._elem = e(document.createElement("table"));
            this._elem.addClass("jqplot-table-legend");
            var E = {
                position: "absolute"
            };
            if (this.background)
            {
                E.background = this.background
            }
            if (this.border)
            {
                E.border = this.border
            }
            if (this.fontSize)
            {
                E.fontSize = this.fontSize
            }
            if (this.fontFamily)
            {
                E.fontFamily = this.fontFamily
            }
            if (this.textColor)
            {
                E.textColor = this.textColor
            }
            if (this.marginTop != null)
            {
                E.marginTop = this.marginTop
            }
            if (this.marginBottom != null)
            {
                E.marginBottom = this.marginBottom
            }
            if (this.marginLeft != null)
            {
                E.marginLeft = this.marginLeft
            }
            if (this.marginRight != null)
            {
                E.marginRight = this.marginRight
            }
            this._elem.css(E);
            var I = false, A = false, o, y;
            var C = B[0];
            var p = new e.jqplot.ColorGenerator(C.seriesColors);
            if (C.show)
            {
                var J = C.data;
                if (this.numberRows)
                {
                    o = this.numberRows;
                    if (!this.numberColumns)
                    {
                        y = Math.ceil(J.length / o)
                    }
                    else
                    {
                        y = this.numberColumns
                    }
                }
                else
                {
                    if (this.numberColumns)
                    {
                        y = this.numberColumns;
                        o = Math.ceil(J.length / this.numberColumns)
                    }
                    else
                    {
                        o = J.length;
                        y = 1
                    }
                }
                var H, G;
                var q, w, v;
                var x, z, F;
                var D = 0;
                var u, t;
                for (H = 0; H < o; H++)
                {
                    q = e(document.createElement("tr"));
                    q.addClass("jqplot-table-legend");
                    if (A)
                    {
                        q.prependTo(this._elem)
                    }
                    else
                    {
                        q.appendTo(this._elem)
                    }
                    for (G = 0; G < y; G++)
                    {
                        if (D < J.length)
                        {
                            x = this.labels[D] || J[D][0].toString();
                            F = p.next();
                            if (!A)
                            {
                                if (H > 0)
                                {
                                    I = true
                                }
                                else
                                {
                                    I = false
                                }
                            }
                            else
                            {
                                if (H == o - 1)
                                {
                                    I = false
                                }
                                else
                                {
                                    I = true
                                }
                            }
                            z = (I) ? this.rowSpacing : "0";
                            w = e(document.createElement("td"));
                            w
                                .addClass("jqplot-table-legend jqplot-table-legend-swatch");
                            w.css({
                                textAlign: "center",
                                paddingTop: z
                            });
                            u = e(document.createElement("div"));
                            u.addClass("jqplot-table-legend-swatch-outline");
                            t = e(document.createElement("div"));
                            t.addClass("jqplot-table-legend-swatch");
                            t.css({
                                backgroundColor: F,
                                borderColor: F
                            });
                            w.append(u.append(t));
                            v = e(document.createElement("td"));
                            v
                                .addClass("jqplot-table-legend jqplot-table-legend-label");
                            v.css("paddingTop", z);
                            if (this.escapeHtml)
                            {
                                v.text(x)
                            }
                            else
                            {
                                v.html(x)
                            }
                            if (A)
                            {
                                v.prependTo(q);
                                w.prependTo(q)
                            }
                            else
                            {
                                w.appendTo(q);
                                v.appendTo(q)
                            }
                            I = true
                        }
                        D++
                    }
                }
            }
        }
        return this._elem
    };
    e.jqplot.PieRenderer.prototype.handleMove = function (q, p, t, s, r) {
        if (s)
        {
            var o = [s.seriesIndex, s.pointIndex, s.data];
            r.target.trigger("jqplotDataMouseOver", o);
            if (r.series[o[0]].highlightMouseOver
                && !(o[0] == r.plugins.pieRenderer.highlightedSeriesIndex && o[1] == r.series[o[0]]._highlightedPoint))
            {
                r.target.trigger("jqplotDataHighlight", o);
                d(r, o[0], o[1])
            }
        }
        else
        {
            if (s == null)
            {
                k(r)
            }
        }
    };

    function c(s, r, p)
    {
        p = p || {};
        p.axesDefaults = p.axesDefaults || {};
        p.legend = p.legend || {};
        p.seriesDefaults = p.seriesDefaults || {};
        var o = false;
        if (p.seriesDefaults.renderer == e.jqplot.PieRenderer)
        {
            o = true
        }
        else
        {
            if (p.series)
            {
                for (var q = 0; q < p.series.length; q++)
                {
                    if (p.series[q].renderer == e.jqplot.PieRenderer)
                    {
                        o = true
                    }
                }
            }
        }
        if (o)
        {
            p.axesDefaults.renderer = e.jqplot.PieAxisRenderer;
            p.legend.renderer = e.jqplot.PieLegendRenderer;
            p.legend.preDraw = true;
            p.seriesDefaults.pointLabels = {
                show: false
            }
        }
    }

    function g(r, q, o)
    {
        for (var p = 0; p < this.series.length; p++)
        {
            if (this.series[p].renderer.constructor == e.jqplot.PieRenderer)
            {
                if (this.series[p].highlightMouseOver)
                {
                    this.series[p].highlightMouseDown = false
                }
            }
        }
    }

    function m(o)
    {
        for (var p = 0; p < this.series.length; p++)
        {
            this.series[p].seriesColors = this.seriesColors;
            this.series[p].colorGenerator = e.jqplot.colorGenerator
        }
    }

    function d(t, r, q)
    {
        var p = t.series[r];
        var o = t.plugins.pieRenderer.highlightCanvas;
        o._ctx.clearRect(0, 0, o._ctx.canvas.width, o._ctx.canvas.height);
        p._highlightedPoint = q;
        t.plugins.pieRenderer.highlightedSeriesIndex = r;
        p.renderer.drawSlice.call(p, o._ctx, p._sliceAngles[q][0],
            p._sliceAngles[q][1], p.highlightColorGenerator.get(q), false)
    }

    function k(q)
    {
        var o = q.plugins.pieRenderer.highlightCanvas;
        o._ctx.clearRect(0, 0, o._ctx.canvas.width, o._ctx.canvas.height);
        for (var p = 0; p < q.series.length; p++)
        {
            q.series[p]._highlightedPoint = null
        }
        q.plugins.pieRenderer.highlightedSeriesIndex = null;
        q.target.trigger("jqplotDataUnhighlight")
    }

    function b(s, r, v, u, t)
    {
        if (u)
        {
            var q = [u.seriesIndex, u.pointIndex, u.data];
            var p = jQuery.Event("jqplotDataMouseOver");
            p.pageX = s.pageX;
            p.pageY = s.pageY;
            t.target.trigger(p, q);
            if (t.series[q[0]].highlightMouseOver
                && !(q[0] == t.plugins.pieRenderer.highlightedSeriesIndex && q[1] == t.series[q[0]]._highlightedPoint))
            {
                var o = jQuery.Event("jqplotDataHighlight");
                o.which = s.which;
                o.pageX = s.pageX;
                o.pageY = s.pageY;
                t.target.trigger(o, q);
                d(t, q[0], q[1])
            }
        }
        else
        {
            if (u == null)
            {
                k(t)
            }
        }
    }

    function a(r, q, u, t, s)
    {
        if (t)
        {
            var p = [t.seriesIndex, t.pointIndex, t.data];
            if (s.series[p[0]].highlightMouseDown
                && !(p[0] == s.plugins.pieRenderer.highlightedSeriesIndex && p[1] == s.series[p[0]]._highlightedPoint))
            {
                var o = jQuery.Event("jqplotDataHighlight");
                o.which = r.which;
                o.pageX = r.pageX;
                o.pageY = r.pageY;
                s.target.trigger(o, p);
                d(s, p[0], p[1])
            }
        }
        else
        {
            if (t == null)
            {
                k(s)
            }
        }
    }

    function l(q, p, t, s, r)
    {
        var o = r.plugins.pieRenderer.highlightedSeriesIndex;
        if (o != null && r.series[o].highlightMouseDown)
        {
            k(r)
        }
    }

    function f(r, q, u, t, s)
    {
        if (t)
        {
            var p = [t.seriesIndex, t.pointIndex, t.data];
            var o = jQuery.Event("jqplotDataClick");
            o.which = r.which;
            o.pageX = r.pageX;
            o.pageY = r.pageY;
            s.target.trigger(o, p)
        }
    }

    function n(s, r, v, u, t)
    {
        if (u)
        {
            var q = [u.seriesIndex, u.pointIndex, u.data];
            var o = t.plugins.pieRenderer.highlightedSeriesIndex;
            if (o != null && t.series[o].highlightMouseDown)
            {
                k(t)
            }
            var p = jQuery.Event("jqplotDataRightClick");
            p.which = s.which;
            p.pageX = s.pageX;
            p.pageY = s.pageY;
            t.target.trigger(p, q)
        }
    }

    function i()
    {
        if (this.plugins.pieRenderer
            && this.plugins.pieRenderer.highlightCanvas)
        {
            this.plugins.pieRenderer.highlightCanvas.resetCanvas();
            this.plugins.pieRenderer.highlightCanvas = null
        }
        this.plugins.pieRenderer = {
            highlightedSeriesIndex: null
        };
        this.plugins.pieRenderer.highlightCanvas = new e.jqplot.GenericCanvas();
        var p = e(this.targetId + " .jqplot-data-label");
        if (p.length)
        {
            e(p[0]).before(
                this.plugins.pieRenderer.highlightCanvas.createElement(
                    this._gridPadding,
                    "jqplot-pieRenderer-highlight-canvas",
                    this._plotDimensions, this))
        }
        else
        {
            this.eventCanvas._elem
                .before(this.plugins.pieRenderer.highlightCanvas
                    .createElement(this._gridPadding,
                        "jqplot-pieRenderer-highlight-canvas",
                        this._plotDimensions, this))
        }
        var o = this.plugins.pieRenderer.highlightCanvas.setContext();
        this.eventCanvas._elem.bind("mouseleave", {
            plot: this
        }, function (q) {
            k(q.data.plot)
        })
    }

    e.jqplot.preInitHooks.push(c);
    e.jqplot.PieTickRenderer = function () {
        e.jqplot.AxisTickRenderer.call(this)
    };
    e.jqplot.PieTickRenderer.prototype = new e.jqplot.AxisTickRenderer();
    e.jqplot.PieTickRenderer.prototype.constructor = e.jqplot.PieTickRenderer
})(jQuery);
(function (d) {
    d.jqplot.BarRenderer = function () {
        d.jqplot.LineRenderer.call(this)
    };
    d.jqplot.BarRenderer.prototype = new d.jqplot.LineRenderer();
    d.jqplot.BarRenderer.prototype.constructor = d.jqplot.BarRenderer;
    d.jqplot.BarRenderer.prototype.init = function (o, q) {
        this.barPadding = 8;
        this.barMargin = 10;
        this.barDirection = "vertical";
        this.barWidth = null;
        this.shadowOffset = 2;
        this.shadowDepth = 5;
        this.shadowAlpha = 0.08;
        this.waterfall = false;
        this.groups = 1;
        this.varyBarColor = false;
        this.highlightMouseOver = true;
        this.highlightMouseDown = false;
        this.highlightColors = [];
        this.transposedData = true;
        this.renderer.animation = {
            show: false,
            direction: "down",
            speed: 3000,
            _supported: true
        };
        this._type = "bar";
        if (o.highlightMouseDown && o.highlightMouseOver == null)
        {
            o.highlightMouseOver = false
        }
        d.extend(true, this, o);
        d.extend(true, this.renderer, o);
        this.fill = true;
        if (this.barDirection === "horizontal"
            && this.rendererOptions.animation
            && this.rendererOptions.animation.direction == null)
        {
            this.renderer.animation.direction = "left"
        }
        if (this.waterfall)
        {
            this.fillToZero = false;
            this.disableStack = true
        }
        if (this.barDirection == "vertical")
        {
            this._primaryAxis = "_xaxis";
            this._stackAxis = "y";
            this.fillAxis = "y"
        }
        else
        {
            this._primaryAxis = "_yaxis";
            this._stackAxis = "x";
            this.fillAxis = "x"
        }
        this._highlightedPoint = null;
        this._plotSeriesInfo = null;
        this._dataColors = [];
        this._barPoints = [];
        var p = {
            lineJoin: "miter",
            lineCap: "round",
            fill: true,
            isarc: false,
            strokeStyle: this.color,
            fillStyle: this.color,
            closePath: this.fill
        };
        this.renderer.shapeRenderer.init(p);
        var n = {
            lineJoin: "miter",
            lineCap: "round",
            fill: true,
            isarc: false,
            angle: this.shadowAngle,
            offset: this.shadowOffset,
            alpha: this.shadowAlpha,
            depth: this.shadowDepth,
            closePath: this.fill
        };
        this.renderer.shadowRenderer.init(n);
        q.postInitHooks.addOnce(h);
        q.postDrawHooks.addOnce(j);
        q.eventListenerHooks.addOnce("jqplotMouseMove", b);
        q.eventListenerHooks.addOnce("jqplotMouseDown", a);
        q.eventListenerHooks.addOnce("jqplotMouseUp", l);
        q.eventListenerHooks.addOnce("jqplotClick", e);
        q.eventListenerHooks.addOnce("jqplotRightClick", m)
    };

    function g(t, p, o, w)
    {
        if (this.rendererOptions.barDirection == "horizontal")
        {
            this._stackAxis = "x";
            this._primaryAxis = "_yaxis"
        }
        if (this.rendererOptions.waterfall == true)
        {
            this._data = d.extend(true, [], this.data);
            var s = 0;
            var u = (!this.rendererOptions.barDirection
                || this.rendererOptions.barDirection === "vertical" || this.transposedData === false) ? 1
                : 0;
            for (var q = 0; q < this.data.length; q++)
            {
                s += this.data[q][u];
                if (q > 0)
                {
                    this.data[q][u] += this.data[q - 1][u]
                }
            }
            this.data[this.data.length] = (u == 1) ? [this.data.length + 1, s]
                : [s, this.data.length + 1];
            this._data[this._data.length] = (u == 1) ? [this._data.length + 1,
                s] : [s, this._data.length + 1]
        }
        if (this.rendererOptions.groups > 1)
        {
            this.breakOnNull = true;
            var n = this.data.length;
            var v = parseInt(n / this.rendererOptions.groups, 10);
            var r = 0;
            for (var q = v; q < n; q += v)
            {
                this.data.splice(q + r, 0, [null, null]);
                this._plotData.splice(q + r, 0, [null, null]);
                this._stackData.splice(q + r, 0, [null, null]);
                r++
            }
            for (q = 0; q < this.data.length; q++)
            {
                if (this._primaryAxis == "_xaxis")
                {
                    this.data[q][0] = q + 1;
                    this._plotData[q][0] = q + 1;
                    this._stackData[q][0] = q + 1
                }
                else
                {
                    this.data[q][1] = q + 1;
                    this._plotData[q][1] = q + 1;
                    this._stackData[q][1] = q + 1
                }
            }
        }
    }

    d.jqplot.preSeriesInitHooks.push(g);
    d.jqplot.BarRenderer.prototype.calcSeriesNumbers = function () {
        var r = 0;
        var t = 0;
        var q = this[this._primaryAxis];
        var p, o, u;
        for (var n = 0; n < q._series.length; n++)
        {
            o = q._series[n];
            if (o === this)
            {
                u = t
            }
            if (o.renderer.constructor == d.jqplot.BarRenderer)
            {
                r += o.data.length;
                t += 1
            }
        }
        return [r, t, u]
    };
    d.jqplot.BarRenderer.prototype.setBarWidth = function () {
        var q;
        var n = 0;
        var o = 0;
        var t = this[this._primaryAxis];
        var x, r, v;
        var w = this._plotSeriesInfo = this.renderer.calcSeriesNumbers
            .call(this);
        n = w[0];
        o = w[1];
        var u = t.numberTicks;
        var p = (u - 1) / 2;
        if (t.name == "xaxis" || t.name == "x2axis")
        {
            if (this._stack)
            {
                this.barWidth = (t._offsets.max - t._offsets.min) / n * o
                    - this.barMargin
            }
            else
            {
                this.barWidth = ((t._offsets.max - t._offsets.min) / p
                    - this.barPadding * (o - 1) - this.barMargin * 2)
                    / o
            }
        }
        else
        {
            if (this._stack)
            {
                this.barWidth = (t._offsets.min - t._offsets.max) / n * o
                    - this.barMargin
            }
            else
            {
                this.barWidth = ((t._offsets.min - t._offsets.max) / p
                    - this.barPadding * (o - 1) - this.barMargin * 2)
                    / o
            }
        }
        return [n, o]
    };

    function f(o)
    {
        var q = [];
        for (var s = 0; s < o.length; s++)
        {
            var r = d.jqplot.getColorComponents(o[s]);
            var n = [r[0], r[1], r[2]];
            var t = n[0] + n[1] + n[2];
            for (var p = 0; p < 3; p++)
            {
                n[p] = (t > 570) ? n[p] * 0.8 : n[p] + 0.3 * (255 - n[p]);
                n[p] = parseInt(n[p], 10)
            }
            q.push("rgb(" + n[0] + "," + n[1] + "," + n[2] + ")")
        }
        return q
    }

    function i(v, u, s, t, o)
    {
        var q = v, w = v - 1, n, p, r = (o === "x") ? 0 : 1;
        if (q > 0)
        {
            p = t.series[w]._plotData[u][r];
            if ((s * p) < 0)
            {
                n = i(w, u, s, t, o)
            }
            else
            {
                n = t.series[w].gridData[u][r]
            }
        }
        else
        {
            n = (r === 0) ? t.series[q]._xaxis.series_u2p(0)
                : t.series[q]._yaxis.series_u2p(0)
        }
        return n
    }

    d.jqplot.BarRenderer.prototype.draw = function (E, L, q, G) {
        var I;
        var A = d.extend({}, q);
        var w = (A.shadow != undefined) ? A.shadow : this.shadow;
        var O = (A.showLine != undefined) ? A.showLine : this.showLine;
        var F = (A.fill != undefined) ? A.fill : this.fill;
        var p = this.xaxis;
        var J = this.yaxis;
        var y = this._xaxis.series_u2p;
        var K = this._yaxis.series_u2p;
        var D, C;
        this._dataColors = [];
        this._barPoints = [];
        if (this.barWidth == null)
        {
            this.renderer.setBarWidth.call(this)
        }
        var N = this._plotSeriesInfo = this.renderer.calcSeriesNumbers
            .call(this);
        var x = N[0];
        var v = N[1];
        var s = N[2];
        var H = [];
        if (this._stack)
        {
            this._barNudge = 0
        }
        else
        {
            this._barNudge = (-Math.abs(v / 2 - 0.5) + s)
                * (this.barWidth + this.barPadding)
        }
        if (O)
        {
            var u = new d.jqplot.ColorGenerator(this.negativeSeriesColors);
            var B = new d.jqplot.ColorGenerator(this.seriesColors);
            var M = u.get(this.index);
            if (!this.useNegativeColors)
            {
                M = A.fillStyle
            }
            var t = A.fillStyle;
            var r;
            var P;
            var o;
            if (this.barDirection == "vertical")
            {
                for (var I = 0; I < L.length; I++)
                {
                    if (!this._stack && this.data[I][1] == null)
                    {
                        continue
                    }
                    H = [];
                    r = L[I][0] + this._barNudge;
                    if (this._stack && this._prevGridData.length)
                    {
                        o = i(this.index, I, this._plotData[I][1], G, "y")
                    }
                    else
                    {
                        if (this.fillToZero)
                        {
                            o = this._yaxis.series_u2p(0)
                        }
                        else
                        {
                            if (this.waterfall && I > 0
                                && I < this.gridData.length - 1)
                            {
                                o = this.gridData[I - 1][1]
                            }
                            else
                            {
                                if (this.waterfall && I == 0
                                    && I < this.gridData.length - 1)
                                {
                                    if (this._yaxis.min <= 0
                                        && this._yaxis.max >= 0)
                                    {
                                        o = this._yaxis.series_u2p(0)
                                    }
                                    else
                                    {
                                        if (this._yaxis.min > 0)
                                        {
                                            o = E.canvas.height
                                        }
                                        else
                                        {
                                            o = 0
                                        }
                                    }
                                }
                                else
                                {
                                    if (this.waterfall
                                        && I == this.gridData.length - 1)
                                    {
                                        if (this._yaxis.min <= 0
                                            && this._yaxis.max >= 0)
                                        {
                                            o = this._yaxis.series_u2p(0)
                                        }
                                        else
                                        {
                                            if (this._yaxis.min > 0)
                                            {
                                                o = E.canvas.height
                                            }
                                            else
                                            {
                                                o = 0
                                            }
                                        }
                                    }
                                    else
                                    {
                                        o = E.canvas.height
                                    }
                                }
                            }
                        }
                    }
                    if ((this.fillToZero && this._plotData[I][1] < 0)
                        || (this.waterfall && this._data[I][1] < 0))
                    {
                        if (this.varyBarColor && !this._stack)
                        {
                            if (this.useNegativeColors)
                            {
                                A.fillStyle = u.next()
                            }
                            else
                            {
                                A.fillStyle = B.next()
                            }
                        }
                        else
                        {
                            A.fillStyle = M
                        }
                    }
                    else
                    {
                        if (this.varyBarColor && !this._stack)
                        {
                            A.fillStyle = B.next()
                        }
                        else
                        {
                            A.fillStyle = t
                        }
                    }
                    if (!this.fillToZero || this._plotData[I][1] >= 0)
                    {
                        H.push([r - this.barWidth / 2, o]);
                        H.push([r - this.barWidth / 2, L[I][1]]);
                        H.push([r + this.barWidth / 2, L[I][1]]);
                        H.push([r + this.barWidth / 2, o])
                    }
                    else
                    {
                        H.push([r - this.barWidth / 2, L[I][1]]);
                        H.push([r - this.barWidth / 2, o]);
                        H.push([r + this.barWidth / 2, o]);
                        H.push([r + this.barWidth / 2, L[I][1]])
                    }
                    this._barPoints.push(H);
                    if (w && !this._stack)
                    {
                        var z = d.extend(true, {}, A);
                        delete z.fillStyle;
                        this.renderer.shadowRenderer.draw(E, H, z)
                    }
                    var n = A.fillStyle || this.color;
                    this._dataColors.push(n);
                    this.renderer.shapeRenderer.draw(E, H, A)
                }
            }
            else
            {
                if (this.barDirection == "horizontal")
                {
                    for (var I = 0; I < L.length; I++)
                    {
                        if (!this._stack && this.data[I][0] == null)
                        {
                            continue
                        }
                        H = [];
                        r = L[I][1] - this._barNudge;
                        P;
                        if (this._stack && this._prevGridData.length)
                        {
                            P = i(this.index, I, this._plotData[I][0], G, "x")
                        }
                        else
                        {
                            if (this.fillToZero)
                            {
                                P = this._xaxis.series_u2p(0)
                            }
                            else
                            {
                                if (this.waterfall && I > 0
                                    && I < this.gridData.length - 1)
                                {
                                    P = this.gridData[I - 1][0]
                                }
                                else
                                {
                                    if (this.waterfall && I == 0
                                        && I < this.gridData.length - 1)
                                    {
                                        if (this._xaxis.min <= 0
                                            && this._xaxis.max >= 0)
                                        {
                                            P = this._xaxis.series_u2p(0)
                                        }
                                        else
                                        {
                                            if (this._xaxis.min > 0)
                                            {
                                                P = 0
                                            }
                                            else
                                            {
                                                P = 0
                                            }
                                        }
                                    }
                                    else
                                    {
                                        if (this.waterfall
                                            && I == this.gridData.length - 1)
                                        {
                                            if (this._xaxis.min <= 0
                                                && this._xaxis.max >= 0)
                                            {
                                                P = this._xaxis.series_u2p(0)
                                            }
                                            else
                                            {
                                                if (this._xaxis.min > 0)
                                                {
                                                    P = 0
                                                }
                                                else
                                                {
                                                    P = E.canvas.width
                                                }
                                            }
                                        }
                                        else
                                        {
                                            P = 0
                                        }
                                    }
                                }
                            }
                        }
                        if ((this.fillToZero && this._plotData[I][0] < 0)
                            || (this.waterfall && this._data[I][0] < 0))
                        {
                            if (this.varyBarColor && !this._stack)
                            {
                                if (this.useNegativeColors)
                                {
                                    A.fillStyle = u.next()
                                }
                                else
                                {
                                    A.fillStyle = B.next()
                                }
                            }
                            else
                            {
                                A.fillStyle = M
                            }
                        }
                        else
                        {
                            if (this.varyBarColor && !this._stack)
                            {
                                A.fillStyle = B.next()
                            }
                            else
                            {
                                A.fillStyle = t
                            }
                        }
                        if (!this.fillToZero || this._plotData[I][0] >= 0)
                        {
                            H.push([P, r + this.barWidth / 2]);
                            H.push([P, r - this.barWidth / 2]);
                            H.push([L[I][0], r - this.barWidth / 2]);
                            H.push([L[I][0], r + this.barWidth / 2])
                        }
                        else
                        {
                            H.push([L[I][0], r + this.barWidth / 2]);
                            H.push([L[I][0], r - this.barWidth / 2]);
                            H.push([P, r - this.barWidth / 2]);
                            H.push([P, r + this.barWidth / 2])
                        }
                        this._barPoints.push(H);
                        if (w && !this._stack)
                        {
                            var z = d.extend(true, {}, A);
                            delete z.fillStyle;
                            this.renderer.shadowRenderer.draw(E, H, z)
                        }
                        var n = A.fillStyle || this.color;
                        this._dataColors.push(n);
                        this.renderer.shapeRenderer.draw(E, H, A)
                    }
                }
            }
        }
        if (this.highlightColors.length == 0)
        {
            this.highlightColors = d.jqplot
                .computeHighlightColors(this._dataColors)
        }
        else
        {
            if (typeof (this.highlightColors) == "string")
            {
                var N = this.highlightColors;
                this.highlightColors = [];
                for (var I = 0; I < this._dataColors.length; I++)
                {
                    this.highlightColors.push(N)
                }
            }
        }
    };
    d.jqplot.BarRenderer.prototype.drawShadow = function (z, G, p, B) {
        var D;
        var w = (p != undefined) ? p : {};
        var t = (w.shadow != undefined) ? w.shadow : this.shadow;
        var I = (w.showLine != undefined) ? w.showLine : this.showLine;
        var A = (w.fill != undefined) ? w.fill : this.fill;
        var o = this.xaxis;
        var E = this.yaxis;
        var v = this._xaxis.series_u2p;
        var F = this._yaxis.series_u2p;
        var y, C, x, u, s, r;
        if (this._stack && this.shadow)
        {
            if (this.barWidth == null)
            {
                this.renderer.setBarWidth.call(this)
            }
            var H = this._plotSeriesInfo = this.renderer.calcSeriesNumbers
                .call(this);
            u = H[0];
            s = H[1];
            r = H[2];
            if (this._stack)
            {
                this._barNudge = 0
            }
            else
            {
                this._barNudge = (-Math.abs(s / 2 - 0.5) + r)
                    * (this.barWidth + this.barPadding)
            }
            if (I)
            {
                if (this.barDirection == "vertical")
                {
                    for (var D = 0; D < G.length; D++)
                    {
                        if (this.data[D][1] == null)
                        {
                            continue
                        }
                        C = [];
                        var q = G[D][0] + this._barNudge;
                        var n;
                        if (this._stack && this._prevGridData.length)
                        {
                            n = i(this.index, D, this._plotData[D][1], B, "y")
                        }
                        else
                        {
                            if (this.fillToZero)
                            {
                                n = this._yaxis.series_u2p(0)
                            }
                            else
                            {
                                n = z.canvas.height
                            }
                        }
                        C.push([q - this.barWidth / 2, n]);
                        C.push([q - this.barWidth / 2, G[D][1]]);
                        C.push([q + this.barWidth / 2, G[D][1]]);
                        C.push([q + this.barWidth / 2, n]);
                        this.renderer.shadowRenderer.draw(z, C, w)
                    }
                }
                else
                {
                    if (this.barDirection == "horizontal")
                    {
                        for (var D = 0; D < G.length; D++)
                        {
                            if (this.data[D][0] == null)
                            {
                                continue
                            }
                            C = [];
                            var q = G[D][1] - this._barNudge;
                            var J;
                            if (this._stack && this._prevGridData.length)
                            {
                                J = i(this.index, D, this._plotData[D][0], B,
                                    "x")
                            }
                            else
                            {
                                if (this.fillToZero)
                                {
                                    J = this._xaxis.series_u2p(0)
                                }
                                else
                                {
                                    J = 0
                                }
                            }
                            C.push([J, q + this.barWidth / 2]);
                            C.push([G[D][0], q + this.barWidth / 2]);
                            C.push([G[D][0], q - this.barWidth / 2]);
                            C.push([J, q - this.barWidth / 2]);
                            this.renderer.shadowRenderer.draw(z, C, w)
                        }
                    }
                }
            }
        }
    };

    function h(q, p, n)
    {
        for (var o = 0; o < this.series.length; o++)
        {
            if (this.series[o].renderer.constructor == d.jqplot.BarRenderer)
            {
                if (this.series[o].highlightMouseOver)
                {
                    this.series[o].highlightMouseDown = false
                }
            }
        }
    }

    function j()
    {
        if (this.plugins.barRenderer
            && this.plugins.barRenderer.highlightCanvas)
        {
            this.plugins.barRenderer.highlightCanvas.resetCanvas();
            this.plugins.barRenderer.highlightCanvas = null
        }
        this.plugins.barRenderer = {
            highlightedSeriesIndex: null
        };
        this.plugins.barRenderer.highlightCanvas = new d.jqplot.GenericCanvas();
        this.eventCanvas._elem.before(this.plugins.barRenderer.highlightCanvas
            .createElement(this._gridPadding,
                "jqplot-barRenderer-highlight-canvas",
                this._plotDimensions, this));
        this.plugins.barRenderer.highlightCanvas.setContext();
        this.eventCanvas._elem.bind("mouseleave", {
            plot: this
        }, function (n) {
            k(n.data.plot)
        })
    }

    function c(u, t, q, p)
    {
        var o = u.series[t];
        var n = u.plugins.barRenderer.highlightCanvas;
        n._ctx.clearRect(0, 0, n._ctx.canvas.width, n._ctx.canvas.height);
        o._highlightedPoint = q;
        u.plugins.barRenderer.highlightedSeriesIndex = t;
        var r = {
            fillStyle: o.highlightColors[q]
        };
        o.renderer.shapeRenderer.draw(n._ctx, p, r);
        n = null
    }

    function k(p)
    {
        var n = p.plugins.barRenderer.highlightCanvas;
        n._ctx.clearRect(0, 0, n._ctx.canvas.width, n._ctx.canvas.height);
        for (var o = 0; o < p.series.length; o++)
        {
            p.series[o]._highlightedPoint = null
        }
        p.plugins.barRenderer.highlightedSeriesIndex = null;
        p.target.trigger("jqplotDataUnhighlight");
        n = null
    }

    function b(r, q, u, t, s)
    {
        if (t)
        {
            var p = [t.seriesIndex, t.pointIndex, t.data];
            var o = jQuery.Event("jqplotDataMouseOver");
            o.pageX = r.pageX;
            o.pageY = r.pageY;
            s.target.trigger(o, p);
            if (s.series[p[0]].show
                && s.series[p[0]].highlightMouseOver
                && !(p[0] == s.plugins.barRenderer.highlightedSeriesIndex && p[1] == s.series[p[0]]._highlightedPoint))
            {
                var n = jQuery.Event("jqplotDataHighlight");
                n.which = r.which;
                n.pageX = r.pageX;
                n.pageY = r.pageY;
                s.target.trigger(n, p);
                c(s, t.seriesIndex, t.pointIndex, t.points)
            }
        }
        else
        {
            if (t == null)
            {
                k(s)
            }
        }
    }

    function a(q, p, t, s, r)
    {
        if (s)
        {
            var o = [s.seriesIndex, s.pointIndex, s.data];
            if (r.series[o[0]].highlightMouseDown
                && !(o[0] == r.plugins.barRenderer.highlightedSeriesIndex && o[1] == r.series[o[0]]._highlightedPoint))
            {
                var n = jQuery.Event("jqplotDataHighlight");
                n.which = q.which;
                n.pageX = q.pageX;
                n.pageY = q.pageY;
                r.target.trigger(n, o);
                c(r, s.seriesIndex, s.pointIndex, s.points)
            }
        }
        else
        {
            if (s == null)
            {
                k(r)
            }
        }
    }

    function l(p, o, s, r, q)
    {
        var n = q.plugins.barRenderer.highlightedSeriesIndex;
        if (n != null && q.series[n].highlightMouseDown)
        {
            k(q)
        }
    }

    function e(q, p, t, s, r)
    {
        if (s)
        {
            var o = [s.seriesIndex, s.pointIndex, s.data];
            var n = jQuery.Event("jqplotDataClick");
            n.which = q.which;
            n.pageX = q.pageX;
            n.pageY = q.pageY;
            r.target.trigger(n, o)
        }
    }

    function m(r, q, u, t, s)
    {
        if (t)
        {
            var p = [t.seriesIndex, t.pointIndex, t.data];
            var n = s.plugins.barRenderer.highlightedSeriesIndex;
            if (n != null && s.series[n].highlightMouseDown)
            {
                k(s)
            }
            var o = jQuery.Event("jqplotDataRightClick");
            o.which = r.which;
            o.pageX = r.pageX;
            o.pageY = r.pageY;
            s.target.trigger(o, p)
        }
    }
})(jQuery);
(function (e) {
    e.jqplot.DonutRenderer = function () {
        e.jqplot.LineRenderer.call(this)
    };
    e.jqplot.DonutRenderer.prototype = new e.jqplot.LineRenderer();
    e.jqplot.DonutRenderer.prototype.constructor = e.jqplot.DonutRenderer;
    e.jqplot.DonutRenderer.prototype.init = function (p, t) {
        this.diameter = null;
        this.innerDiameter = null;
        this.thickness = null;
        this.padding = 20;
        this.sliceMargin = 0;
        this.ringMargin = null;
        this.fill = true;
        this.shadowOffset = 2;
        this.shadowAlpha = 0.07;
        this.shadowDepth = 5;
        this.highlightMouseOver = true;
        this.highlightMouseDown = false;
        this.highlightColors = [];
        this.dataLabels = "percent";
        this.showDataLabels = false;
        this.dataLabelFormatString = null;
        this.dataLabelThreshold = 3;
        this.dataLabelPositionFactor = 0.4;
        this.dataLabelNudge = 0;
        this.startAngle = 0;
        this.tickRenderer = e.jqplot.DonutTickRenderer;
        this._drawData = true;
        this._type = "donut";
        if (p.highlightMouseDown && p.highlightMouseOver == null)
        {
            p.highlightMouseOver = false
        }
        e.extend(true, this, p);
        if (this.diameter != null)
        {
            this.diameter = this.diameter - this.sliceMargin
        }
        this._diameter = null;
        this._innerDiameter = null;
        this._radius = null;
        this._innerRadius = null;
        this._thickness = null;
        this._previousSeries = [];
        this._numberSeries = 1;
        this._sliceAngles = [];
        this._highlightedPoint = null;
        if (this.highlightColors.length == 0)
        {
            for (var r = 0; r < this.seriesColors.length; r++)
            {
                var q = e.jqplot.getColorComponents(this.seriesColors[r]);
                var n = [q[0], q[1], q[2]];
                var s = n[0] + n[1] + n[2];
                for (var o = 0; o < 3; o++)
                {
                    n[o] = (s > 570) ? n[o] * 0.8 : n[o] + 0.3 * (255 - n[o]);
                    n[o] = parseInt(n[o], 10)
                }
                this.highlightColors.push("rgb(" + n[0] + "," + n[1] + ","
                    + n[2] + ")")
            }
        }
        t.postParseOptionsHooks.addOnce(l);
        t.postInitHooks.addOnce(g);
        t.eventListenerHooks.addOnce("jqplotMouseMove", b);
        t.eventListenerHooks.addOnce("jqplotMouseDown", a);
        t.eventListenerHooks.addOnce("jqplotMouseUp", j);
        t.eventListenerHooks.addOnce("jqplotClick", f);
        t.eventListenerHooks.addOnce("jqplotRightClick", m);
        t.postDrawHooks.addOnce(h)
    };
    e.jqplot.DonutRenderer.prototype.setGridData = function (s) {
        var o = [];
        var t = [];
        var n = this.startAngle / 180 * Math.PI;
        var r = 0;
        this._drawData = false;
        for (var q = 0; q < this.data.length; q++)
        {
            if (this.data[q][1] != 0)
            {
                this._drawData = true
            }
            o.push(this.data[q][1]);
            t.push([this.data[q][0]]);
            if (q > 0)
            {
                o[q] += o[q - 1]
            }
            r += this.data[q][1]
        }
        var p = Math.PI * 2 / o[o.length - 1];
        for (var q = 0; q < o.length; q++)
        {
            t[q][1] = o[q] * p;
            t[q][2] = this.data[q][1] / r
        }
        this.gridData = t
    };
    e.jqplot.DonutRenderer.prototype.makeGridData = function (s, t) {
        var o = [];
        var u = [];
        var r = 0;
        var n = this.startAngle / 180 * Math.PI;
        this._drawData = false;
        for (var q = 0; q < s.length; q++)
        {
            if (this.data[q][1] != 0)
            {
                this._drawData = true
            }
            o.push(s[q][1]);
            u.push([s[q][0]]);
            if (q > 0)
            {
                o[q] += o[q - 1]
            }
            r += s[q][1]
        }
        var p = Math.PI * 2 / o[o.length - 1];
        for (var q = 0; q < o.length; q++)
        {
            u[q][1] = o[q] * p;
            u[q][2] = s[q][1] / r
        }
        return u
    };
    e.jqplot.DonutRenderer.prototype.drawSlice = function (x, u, t, p, s) {
        var n = this._diameter / 2;
        var v = n - this._thickness;
        var w = this.fill;
        x.save();
        x.translate(this._center[0], this._center[1]);
        if (s)
        {
            for (var q = 0; q < this.shadowDepth; q++)
            {
                x.save();
                x.translate(this.shadowOffset
                    * Math.cos(this.shadowAngle / 180 * Math.PI),
                    this.shadowOffset
                    * Math.sin(this.shadowAngle / 180 * Math.PI));
                o()
            }
        }
        else
        {
            o()
        }

        function o()
        {
            if (t > 6.282 + this.startAngle)
            {
                t = 6.282 + this.startAngle;
                if (u > t)
                {
                    u = 6.281 + this.startAngle
                }
            }
            if (u >= t)
            {
                return
            }
            x.beginPath();
            x.fillStyle = p;
            x.strokeStyle = p;
            x.arc(0, 0, n, u, t, false);
            x.lineTo(v * Math.cos(t), v * Math.sin(t));
            x.arc(0, 0, v, t, u, true);
            x.closePath();
            if (w)
            {
                x.fill()
            }
            else
            {
                x.stroke()
            }
        }

        if (s)
        {
            for (var q = 0; q < this.shadowDepth; q++)
            {
                x.restore()
            }
        }
        x.restore()
    };
    e.jqplot.DonutRenderer.prototype.draw = function (N, V, t, P) {
        var Q;
        var J = (t != undefined) ? t : {};
        var q = 0;
        var p = 0;
        var u = 1;
        if (t.legendInfo && t.legendInfo.placement == "insideGrid")
        {
            var I = t.legendInfo;
            switch (I.location)
            {
                case "nw":
                    q = I.width + I.xoffset;
                    break;
                case "w":
                    q = I.width + I.xoffset;
                    break;
                case "sw":
                    q = I.width + I.xoffset;
                    break;
                case "ne":
                    q = I.width + I.xoffset;
                    u = -1;
                    break;
                case "e":
                    q = I.width + I.xoffset;
                    u = -1;
                    break;
                case "se":
                    q = I.width + I.xoffset;
                    u = -1;
                    break;
                case "n":
                    p = I.height + I.yoffset;
                    break;
                case "s":
                    p = I.height + I.yoffset;
                    u = -1;
                    break;
                default:
                    break
            }
        }
        var B = (J.shadow != undefined) ? J.shadow : this.shadow;
        var W = (J.showLine != undefined) ? J.showLine : this.showLine;
        var O = (J.fill != undefined) ? J.fill : this.fill;
        var s = N.canvas.width;
        var H = N.canvas.height;
        var G = s - q - 2 * this.padding;
        var R = H - p - 2 * this.padding;
        var v = Math.min(G, R);
        var T = v;
        var X = (this.ringMargin == null) ? this.sliceMargin * 2
            : this.ringMargin;
        for (var Q = 0; Q < this._previousSeries.length; Q++)
        {
            T -= 2 * this._previousSeries[Q]._thickness + 2 * X
        }
        this._diameter = this.diameter || T;
        if (this.innerDiameter != null)
        {
            var M = (this._numberSeries > 1 && this.index > 0) ? this._previousSeries[0]._diameter
                : this._diameter;
            this._thickness = this.thickness
                || (M - this.innerDiameter - 2 * X * this._numberSeries)
                / this._numberSeries / 2
        }
        else
        {
            this._thickness = this.thickness || v / 2
                / (this._numberSeries + 1) * 0.85
        }
        var K = this._radius = this._diameter / 2;
        this._innerRadius = this._radius - this._thickness;
        var o = this.startAngle / 180 * Math.PI;
        this._center = [(s - u * q) / 2 + u * q, (H - u * p) / 2 + u * p];
        if (this.shadow)
        {
            var L = "rgba(0,0,0," + this.shadowAlpha + ")";
            for (var Q = 0; Q < V.length; Q++)
            {
                var A = (Q == 0) ? o : V[Q - 1][1] + o;
                A += this.sliceMargin / 180 * Math.PI;
                this.renderer.drawSlice.call(this, N, A, V[Q][1] + o, L, true)
            }
        }
        for (var Q = 0; Q < V.length; Q++)
        {
            var A = (Q == 0) ? o : V[Q - 1][1] + o;
            A += this.sliceMargin / 180 * Math.PI;
            var z = V[Q][1] + o;
            this._sliceAngles.push([A, z]);
            this.renderer.drawSlice.call(this, N, A, z, this.seriesColors[Q],
                false);
            if (this.showDataLabels && V[Q][2] * 100 >= this.dataLabelThreshold)
            {
                var S, U = (A + z) / 2, C;
                if (this.dataLabels == "label")
                {
                    S = this.dataLabelFormatString || "%s";
                    C = e.jqplot.sprintf(S, V[Q][0])
                }
                else
                {
                    if (this.dataLabels == "value")
                    {
                        S = this.dataLabelFormatString || "%d";
                        C = e.jqplot.sprintf(S, this.data[Q][1])
                    }
                    else
                    {
                        if (this.dataLabels == "percent")
                        {
                            S = this.dataLabelFormatString || "%d%%";
                            C = e.jqplot.sprintf(S, V[Q][2] * 100)
                        }
                        else
                        {
                            if (this.dataLabels.constructor == Array)
                            {
                                S = this.dataLabelFormatString || "%s";
                                C = e.jqplot.sprintf(S, this.dataLabels[Q])
                            }
                        }
                    }
                }
                var n = this._innerRadius + this._thickness
                    * this.dataLabelPositionFactor + this.sliceMargin
                    + this.dataLabelNudge;
                var F = this._center[0] + Math.cos(U) * n
                    + this.canvas._offsets.left;
                var E = this._center[1] + Math.sin(U) * n
                    + this.canvas._offsets.top;
                var D = e(
                    '<span class="jqplot-donut-series jqplot-data-label" style="position:absolute;">'
                    + C + "</span>").insertBefore(
                    P.eventCanvas._elem);
                F -= D.width() / 2;
                E -= D.height() / 2;
                F = Math.round(F);
                E = Math.round(E);
                D.css({
                    left: F,
                    top: E
                })
            }
        }
    };
    e.jqplot.DonutAxisRenderer = function () {
        e.jqplot.LinearAxisRenderer.call(this)
    };
    e.jqplot.DonutAxisRenderer.prototype = new e.jqplot.LinearAxisRenderer();
    e.jqplot.DonutAxisRenderer.prototype.constructor = e.jqplot.DonutAxisRenderer;
    e.jqplot.DonutAxisRenderer.prototype.init = function (n) {
        this.tickRenderer = e.jqplot.DonutTickRenderer;
        e.extend(true, this, n);
        this._dataBounds = {
            min: 0,
            max: 100
        };
        this.min = 0;
        this.max = 100;
        this.showTicks = false;
        this.ticks = [];
        this.showMark = false;
        this.show = false
    };
    e.jqplot.DonutLegendRenderer = function () {
        e.jqplot.TableLegendRenderer.call(this)
    };
    e.jqplot.DonutLegendRenderer.prototype = new e.jqplot.TableLegendRenderer();
    e.jqplot.DonutLegendRenderer.prototype.constructor = e.jqplot.DonutLegendRenderer;
    e.jqplot.DonutLegendRenderer.prototype.init = function (n) {
        this.numberRows = null;
        this.numberColumns = null;
        e.extend(true, this, n)
    };
    e.jqplot.DonutLegendRenderer.prototype.draw = function () {
        var q = this;
        if (this.show)
        {
            var y = this._series;
            var B = "position:absolute;";
            B += (this.background) ? "background:" + this.background + ";" : "";
            B += (this.border) ? "border:" + this.border + ";" : "";
            B += (this.fontSize) ? "font-size:" + this.fontSize + ";" : "";
            B += (this.fontFamily) ? "font-family:" + this.fontFamily + ";"
                : "";
            B += (this.textColor) ? "color:" + this.textColor + ";" : "";
            B += (this.marginTop != null) ? "margin-top:" + this.marginTop
                + ";" : "";
            B += (this.marginBottom != null) ? "margin-bottom:"
                + this.marginBottom + ";" : "";
            B += (this.marginLeft != null) ? "margin-left:" + this.marginLeft
                + ";" : "";
            B += (this.marginRight != null) ? "margin-right:"
                + this.marginRight + ";" : "";
            this._elem = e('<table class="jqplot-table-legend" style="' + B
                + '"></table>');
            var F = false, x = false, n, v;
            var z = y[0];
            var o = new e.jqplot.ColorGenerator(z.seriesColors);
            if (z.show)
            {
                var G = z.data;
                if (this.numberRows)
                {
                    n = this.numberRows;
                    if (!this.numberColumns)
                    {
                        v = Math.ceil(G.length / n)
                    }
                    else
                    {
                        v = this.numberColumns
                    }
                }
                else
                {
                    if (this.numberColumns)
                    {
                        v = this.numberColumns;
                        n = Math.ceil(G.length / this.numberColumns)
                    }
                    else
                    {
                        n = G.length;
                        v = 1
                    }
                }
                var E, D, p, t, r, u, w, C;
                var A = 0;
                for (E = 0; E < n; E++)
                {
                    if (x)
                    {
                        p = e('<tr class="jqplot-table-legend"></tr>')
                            .prependTo(this._elem)
                    }
                    else
                    {
                        p = e('<tr class="jqplot-table-legend"></tr>')
                            .appendTo(this._elem)
                    }
                    for (D = 0; D < v; D++)
                    {
                        if (A < G.length)
                        {
                            u = this.labels[A] || G[A][0].toString();
                            C = o.next();
                            if (!x)
                            {
                                if (E > 0)
                                {
                                    F = true
                                }
                                else
                                {
                                    F = false
                                }
                            }
                            else
                            {
                                if (E == n - 1)
                                {
                                    F = false
                                }
                                else
                                {
                                    F = true
                                }
                            }
                            w = (F) ? this.rowSpacing : "0";
                            t = e('<td class="jqplot-table-legend" style="text-align:center;padding-top:'
                                + w
                                + ';"><div><div class="jqplot-table-legend-swatch" style="border-color:'
                                + C + ';"></div></div></td>');
                            r = e('<td class="jqplot-table-legend" style="padding-top:'
                                + w + ';"></td>');
                            if (this.escapeHtml)
                            {
                                r.text(u)
                            }
                            else
                            {
                                r.html(u)
                            }
                            if (x)
                            {
                                r.prependTo(p);
                                t.prependTo(p)
                            }
                            else
                            {
                                t.appendTo(p);
                                r.appendTo(p)
                            }
                            F = true
                        }
                        A++
                    }
                }
            }
        }
        return this._elem
    };

    function c(r, q, o)
    {
        o = o || {};
        o.axesDefaults = o.axesDefaults || {};
        o.legend = o.legend || {};
        o.seriesDefaults = o.seriesDefaults || {};
        var n = false;
        if (o.seriesDefaults.renderer == e.jqplot.DonutRenderer)
        {
            n = true
        }
        else
        {
            if (o.series)
            {
                for (var p = 0; p < o.series.length; p++)
                {
                    if (o.series[p].renderer == e.jqplot.DonutRenderer)
                    {
                        n = true
                    }
                }
            }
        }
        if (n)
        {
            o.axesDefaults.renderer = e.jqplot.DonutAxisRenderer;
            o.legend.renderer = e.jqplot.DonutLegendRenderer;
            o.legend.preDraw = true;
            o.seriesDefaults.pointLabels = {
                show: false
            }
        }
    }

    function g(r, q, o)
    {
        for (var p = 1; p < this.series.length; p++)
        {
            if (!this.series[p]._previousSeries.length)
            {
                for (var n = 0; n < p; n++)
                {
                    if (this.series[p].renderer.constructor == e.jqplot.DonutRenderer
                        && this.series[n].renderer.constructor == e.jqplot.DonutRenderer)
                    {
                        this.series[p]._previousSeries.push(this.series[n])
                    }
                }
            }
        }
        for (p = 0; p < this.series.length; p++)
        {
            if (this.series[p].renderer.constructor == e.jqplot.DonutRenderer)
            {
                this.series[p]._numberSeries = this.series.length;
                if (this.series[p].highlightMouseOver)
                {
                    this.series[p].highlightMouseDown = false
                }
            }
        }
    }

    var k = false;

    function l(n)
    {
        for (var o = 0; o < this.series.length; o++)
        {
            this.series[o].seriesColors = this.seriesColors;
            this.series[o].colorGenerator = e.jqplot.colorGenerator
        }
    }

    function d(r, q, p)
    {
        var o = r.series[q];
        var n = r.plugins.donutRenderer.highlightCanvas;
        n._ctx.clearRect(0, 0, n._ctx.canvas.width, n._ctx.canvas.height);
        o._highlightedPoint = p;
        r.plugins.donutRenderer.highlightedSeriesIndex = q;
        o.renderer.drawSlice.call(o, n._ctx, o._sliceAngles[p][0],
            o._sliceAngles[p][1], o.highlightColors[p], false)
    }

    function i(p)
    {
        var n = p.plugins.donutRenderer.highlightCanvas;
        n._ctx.clearRect(0, 0, n._ctx.canvas.width, n._ctx.canvas.height);
        for (var o = 0; o < p.series.length; o++)
        {
            p.series[o]._highlightedPoint = null
        }
        p.plugins.donutRenderer.highlightedSeriesIndex = null;
        p.target.trigger("jqplotDataUnhighlight")
    }

    function b(r, q, u, t, s)
    {
        if (t)
        {
            var p = [t.seriesIndex, t.pointIndex, t.data];
            var o = jQuery.Event("jqplotDataMouseOver");
            o.pageX = r.pageX;
            o.pageY = r.pageY;
            s.target.trigger(o, p);
            if (s.series[p[0]].highlightMouseOver
                && !(p[0] == s.plugins.donutRenderer.highlightedSeriesIndex && p[1] == s.series[p[0]]._highlightedPoint))
            {
                var n = jQuery.Event("jqplotDataHighlight");
                n.which = r.which;
                n.pageX = r.pageX;
                n.pageY = r.pageY;
                s.target.trigger(n, p);
                d(s, p[0], p[1])
            }
        }
        else
        {
            if (t == null)
            {
                i(s)
            }
        }
    }

    function a(q, p, t, s, r)
    {
        if (s)
        {
            var o = [s.seriesIndex, s.pointIndex, s.data];
            if (r.series[o[0]].highlightMouseDown
                && !(o[0] == r.plugins.donutRenderer.highlightedSeriesIndex && o[1] == r.series[o[0]]._highlightedPoint))
            {
                var n = jQuery.Event("jqplotDataHighlight");
                n.which = q.which;
                n.pageX = q.pageX;
                n.pageY = q.pageY;
                r.target.trigger(n, o);
                d(r, o[0], o[1])
            }
        }
        else
        {
            if (s == null)
            {
                i(r)
            }
        }
    }

    function j(p, o, s, r, q)
    {
        var n = q.plugins.donutRenderer.highlightedSeriesIndex;
        if (n != null && q.series[n].highlightMouseDown)
        {
            i(q)
        }
    }

    function f(q, p, t, s, r)
    {
        if (s)
        {
            var o = [s.seriesIndex, s.pointIndex, s.data];
            var n = jQuery.Event("jqplotDataClick");
            n.which = q.which;
            n.pageX = q.pageX;
            n.pageY = q.pageY;
            r.target.trigger(n, o)
        }
    }

    function m(r, q, u, t, s)
    {
        if (t)
        {
            var p = [t.seriesIndex, t.pointIndex, t.data];
            var n = s.plugins.donutRenderer.highlightedSeriesIndex;
            if (n != null && s.series[n].highlightMouseDown)
            {
                i(s)
            }
            var o = jQuery.Event("jqplotDataRightClick");
            o.which = r.which;
            o.pageX = r.pageX;
            o.pageY = r.pageY;
            s.target.trigger(o, p)
        }
    }

    function h()
    {
        if (this.plugins.donutRenderer
            && this.plugins.donutRenderer.highlightCanvas)
        {
            this.plugins.donutRenderer.highlightCanvas.resetCanvas();
            this.plugins.donutRenderer.highlightCanvas = null
        }
        this.plugins.donutRenderer = {
            highlightedSeriesIndex: null
        };
        this.plugins.donutRenderer.highlightCanvas = new e.jqplot.GenericCanvas();
        var o = e(this.targetId + " .jqplot-data-label");
        if (o.length)
        {
            e(o[0]).before(
                this.plugins.donutRenderer.highlightCanvas.createElement(
                    this._gridPadding,
                    "jqplot-donutRenderer-highlight-canvas",
                    this._plotDimensions, this))
        }
        else
        {
            this.eventCanvas._elem
                .before(this.plugins.donutRenderer.highlightCanvas
                    .createElement(this._gridPadding,
                        "jqplot-donutRenderer-highlight-canvas",
                        this._plotDimensions, this))
        }
        var n = this.plugins.donutRenderer.highlightCanvas.setContext();
        this.eventCanvas._elem.bind("mouseleave", {
            plot: this
        }, function (p) {
            i(p.data.plot)
        })
    }

    e.jqplot.preInitHooks.push(c);
    e.jqplot.DonutTickRenderer = function () {
        e.jqplot.AxisTickRenderer.call(this)
    };
    e.jqplot.DonutTickRenderer.prototype = new e.jqplot.AxisTickRenderer();
    e.jqplot.DonutTickRenderer.prototype.constructor = e.jqplot.DonutTickRenderer
})(jQuery);
(function (c) {
    c.jqplot.EnhancedLegendRenderer = function () {
        c.jqplot.TableLegendRenderer.call(this)
    };
    c.jqplot.EnhancedLegendRenderer.prototype = new c.jqplot.TableLegendRenderer();
    c.jqplot.EnhancedLegendRenderer.prototype.constructor = c.jqplot.EnhancedLegendRenderer;
    c.jqplot.EnhancedLegendRenderer.prototype.init = function (d) {
        this.numberRows = null;
        this.numberColumns = null;
        this.seriesToggle = "normal";
        this.seriesToggleReplot = false;
        this.disableIEFading = true;
        c.extend(true, this, d);
        if (this.seriesToggle)
        {
            c.jqplot.postDrawHooks.push(b)
        }
    };
    c.jqplot.EnhancedLegendRenderer.prototype.draw = function (m, y) {
        var f = this;
        if (this.show)
        {
            var r = this._series;
            var u;
            var w = "position:absolute;";
            w += (this.background) ? "background:" + this.background + ";" : "";
            w += (this.border) ? "border:" + this.border + ";" : "";
            w += (this.fontSize) ? "font-size:" + this.fontSize + ";" : "";
            w += (this.fontFamily) ? "font-family:" + this.fontFamily + ";"
                : "";
            w += (this.textColor) ? "color:" + this.textColor + ";" : "";
            w += (this.marginTop != null) ? "margin-top:" + this.marginTop
                + ";" : "";
            w += (this.marginBottom != null) ? "margin-bottom:"
                + this.marginBottom + ";" : "";
            w += (this.marginLeft != null) ? "margin-left:" + this.marginLeft
                + ";" : "";
            w += (this.marginRight != null) ? "margin-right:"
                + this.marginRight + ";" : "";
            this._elem = c('<table class="jqplot-table-legend" style="' + w
                + '"></table>');
            if (this.seriesToggle)
            {
                this._elem.css("z-index", "3")
            }
            var C = false, q = false, d, o;
            if (this.numberRows)
            {
                d = this.numberRows;
                if (!this.numberColumns)
                {
                    o = Math.ceil(r.length / d)
                }
                else
                {
                    o = this.numberColumns
                }
            }
            else
            {
                if (this.numberColumns)
                {
                    o = this.numberColumns;
                    d = Math.ceil(r.length / this.numberColumns)
                }
                else
                {
                    d = r.length;
                    o = 1
                }
            }
            var B, z, e, l, k, n, p, t, h, g;
            var v = 0;
            for (B = r.length - 1; B >= 0; B--)
            {
                if (o == 1
                    && r[B]._stack
                    || r[B].renderer.constructor == c.jqplot.BezierCurveRenderer)
                {
                    q = true
                }
            }
            for (B = 0; B < d; B++)
            {
                e = c(document.createElement("tr"));
                e.addClass("jqplot-table-legend");
                e.appendTo(this._elem);
                for (z = 0; z < o; z++)
                {
                    if (v < r.length && (r[v].show || r[v].showLabel))
                    {
                        u = r[v];
                        n = this.labels[v] || u.label.toString();
                        if (n)
                        {
                            var x = u.color;
                            if (!q)
                            {
                                if (B > 0)
                                {
                                    C = true
                                }
                                else
                                {
                                    C = false
                                }
                            }
                            else
                            {
                                if (B == d - 1)
                                {
                                    C = false
                                }
                                else
                                {
                                    C = true
                                }
                            }
                            p = (C) ? this.rowSpacing : "0";
                            l = c(document.createElement("td"));
                            l
                                .addClass("jqplot-table-legend jqplot-table-legend-swatch");
                            l.css({
                                textAlign: "center",
                                paddingTop: p
                            });
                            h = c(document.createElement("div"));
                            h.addClass("jqplot-table-legend-swatch-outline");
                            g = c(document.createElement("div"));
                            g.addClass("jqplot-table-legend-swatch");
                            g.css({
                                backgroundColor: x,
                                borderColor: x
                            });
                            l.append(h.append(g));
                            k = c(document.createElement("td"));
                            k
                                .addClass("jqplot-table-legend jqplot-table-legend-label");
                            k.css("paddingTop", p);
                            if (this.escapeHtml)
                            {
                                k.text(n)
                            }
                            else
                            {
                                k.html(n)
                            }
                            if (q)
                            {
                                if (this.showLabels)
                                {
                                    k.prependTo(e)
                                }
                                if (this.showSwatches)
                                {
                                    l.prependTo(e)
                                }
                            }
                            else
                            {
                                if (this.showSwatches)
                                {
                                    l.appendTo(e)
                                }
                                if (this.showLabels)
                                {
                                    k.appendTo(e)
                                }
                            }
                            if (this.seriesToggle)
                            {
                                var A;
                                if (typeof (this.seriesToggle) === "string"
                                    || typeof (this.seriesToggle) === "number")
                                {
                                    if (!c.jqplot.use_excanvas
                                        || !this.disableIEFading)
                                    {
                                        A = this.seriesToggle
                                    }
                                }
                                if (this.showSwatches)
                                {
                                    l.bind("click", {
                                        series: u,
                                        speed: A,
                                        plot: y,
                                        replot: this.seriesToggleReplot
                                    }, a);
                                    l.addClass("jqplot-seriesToggle")
                                }
                                if (this.showLabels)
                                {
                                    k.bind("click", {
                                        series: u,
                                        speed: A,
                                        plot: y,
                                        replot: this.seriesToggleReplot
                                    }, a);
                                    k.addClass("jqplot-seriesToggle")
                                }
                                if (!u.show && u.showLabel)
                                {
                                    l.addClass("jqplot-series-hidden");
                                    k.addClass("jqplot-series-hidden")
                                }
                            }
                            C = true
                        }
                    }
                    v++
                }
                l = k = h = g = null
            }
        }
        return this._elem
    };
    var a = function (j) {
        var i = j.data, m = i.series, k = i.replot, h = i.plot, f = i.speed, l = m.index, g = false;
        if (m.canvas._elem.is(":hidden") || !m.show)
        {
            g = true
        }
        var e = function () {
            if (k)
            {
                var n = {};
                if (c.isPlainObject(k))
                {
                    c.extend(true, n, k)
                }
                h.replot(n);
                if (g && f)
                {
                    var d = h.series[l];
                    if (d.shadowCanvas._elem)
                    {
                        d.shadowCanvas._elem.hide().fadeIn(f)
                    }
                    d.canvas._elem.hide().fadeIn(f);
                    d.canvas._elem.nextAll(
                        ".jqplot-point-label.jqplot-series-" + d.index)
                        .hide().fadeIn(f)
                }
            }
            else
            {
                var d = h.series[l];
                if (d.canvas._elem.is(":hidden") || !d.show)
                {
                    if (typeof h.options.legend.showSwatches === "undefined"
                        || h.options.legend.showSwatches === true)
                    {
                        h.legend._elem.find("td").eq(l * 2).addClass(
                            "jqplot-series-hidden")
                    }
                    if (typeof h.options.legend.showLabels === "undefined"
                        || h.options.legend.showLabels === true)
                    {
                        h.legend._elem.find("td").eq((l * 2) + 1).addClass(
                            "jqplot-series-hidden")
                    }
                }
                else
                {
                    if (typeof h.options.legend.showSwatches === "undefined"
                        || h.options.legend.showSwatches === true)
                    {
                        h.legend._elem.find("td").eq(l * 2).removeClass(
                            "jqplot-series-hidden")
                    }
                    if (typeof h.options.legend.showLabels === "undefined"
                        || h.options.legend.showLabels === true)
                    {
                        h.legend._elem.find("td").eq((l * 2) + 1).removeClass(
                            "jqplot-series-hidden")
                    }
                }
            }
        };
        m.toggleDisplay(j, e)
    };
    var b = function () {
        if (this.legend.renderer.constructor == c.jqplot.EnhancedLegendRenderer
            && this.legend.seriesToggle)
        {
            var d = this.legend._elem.detach();
            this.eventCanvas._elem.after(d)
        }
    }
})(jQuery);
(function (f) {
    var d = function (m) {
        return Math.max.apply(Math, m)
    };
    var j = function (m) {
        return Math.min.apply(Math, m)
    };
    f.jqplot.BubbleRenderer = function () {
        f.jqplot.LineRenderer.call(this)
    };
    f.jqplot.BubbleRenderer.prototype = new f.jqplot.LineRenderer();
    f.jqplot.BubbleRenderer.prototype.constructor = f.jqplot.BubbleRenderer;
    f.jqplot.BubbleRenderer.prototype.init = function (w, t) {
        this.varyBubbleColors = true;
        this.autoscaleBubbles = true;
        this.autoscaleMultiplier = 1;
        this.autoscalePointsFactor = -0.07;
        this.escapeHtml = true;
        this.highlightMouseOver = true;
        this.highlightMouseDown = false;
        this.highlightColors = [];
        this.bubbleAlpha = 1;
        this.highlightAlpha = null;
        this.bubbleGradients = false;
        this.showLabels = true;
        this.radii = [];
        this.maxRadius = 0;
        this._highlightedPoint = null;
        this.labels = [];
        this.bubbleCanvases = [];
        this._type = "bubble";
        if (w.highlightMouseDown && w.highlightMouseOver == null)
        {
            w.highlightMouseOver = false
        }
        f.extend(true, this, w);
        if (this.highlightAlpha == null)
        {
            this.highlightAlpha = this.bubbleAlpha;
            if (this.bubbleGradients)
            {
                this.highlightAlpha = 0.35
            }
        }
        this.autoscaleMultiplier = this.autoscaleMultiplier
            * Math.pow(this.data.length, this.autoscalePointsFactor);
        this._highlightedPoint = null;
        var n;
        for (var r = 0; r < this.data.length; r++)
        {
            var p = null;
            var v = this.data[r];
            this.maxRadius = Math.max(this.maxRadius, v[2]);
            if (v[3])
            {
                if (typeof (v[3]) == "object")
                {
                    p = v[3]["color"]
                }
            }
            if (p == null)
            {
                if (this.seriesColors[r] != null)
                {
                    p = this.seriesColors[r]
                }
            }
            if (p && this.bubbleAlpha < 1)
            {
                n = f.jqplot.getColorComponents(p);
                p = "rgba(" + n[0] + ", " + n[1] + ", " + n[2] + ", "
                    + this.bubbleAlpha + ")"
            }
            if (p)
            {
                this.seriesColors[r] = p
            }
        }
        if (!this.varyBubbleColors)
        {
            this.seriesColors = [this.color]
        }
        this.colorGenerator = new f.jqplot.ColorGenerator(this.seriesColors);
        if (this.highlightColors.length == 0)
        {
            for (var r = 0; r < this.seriesColors.length; r++)
            {
                var o = f.jqplot.getColorComponents(this.seriesColors[r]);
                var u = [o[0], o[1], o[2]];
                var s = u[0] + u[1] + u[2];
                for (var q = 0; q < 3; q++)
                {
                    u[q] = (s > 570) ? u[q] * 0.8 : u[q] + 0.3 * (255 - u[q]);
                    u[q] = parseInt(u[q], 10)
                }
                this.highlightColors.push("rgba(" + u[0] + "," + u[1] + ","
                    + u[2] + ", " + this.highlightAlpha + ")")
            }
        }
        this.highlightColorGenerator = new f.jqplot.ColorGenerator(
            this.highlightColors);
        var m = {
            fill: true,
            isarc: true,
            angle: this.shadowAngle,
            alpha: this.shadowAlpha,
            closePath: true
        };
        this.renderer.shadowRenderer.init(m);
        this.canvas = new f.jqplot.DivCanvas();
        this.canvas._plotDimensions = this._plotDimensions;
        t.eventListenerHooks.addOnce("jqplotMouseMove", a);
        t.eventListenerHooks.addOnce("jqplotMouseDown", b);
        t.eventListenerHooks.addOnce("jqplotMouseUp", k);
        t.eventListenerHooks.addOnce("jqplotClick", g);
        t.eventListenerHooks.addOnce("jqplotRightClick", l);
        t.postDrawHooks.addOnce(h)
    };
    f.jqplot.BubbleRenderer.prototype.setGridData = function (w) {
        var q = this._xaxis.series_u2p;
        var m = this._yaxis.series_u2p;
        var t = this._plotData;
        this.gridData = [];
        var s = [];
        this.radii = [];
        var v = Math.min(w._height, w._width);
        for (var u = 0; u < this.data.length; u++)
        {
            if (t[u] != null)
            {
                this.gridData.push([q.call(this._xaxis, t[u][0]),
                    m.call(this._yaxis, t[u][1]), t[u][2]]);
                this.radii.push([u, t[u][2]]);
                s.push(t[u][2])
            }
        }
        var n, o, x = this.maxRadius = d(s);
        var p = this.gridData.length;
        if (this.autoscaleBubbles)
        {
            for (var u = 0; u < p; u++)
            {
                o = s[u] / x;
                n = this.autoscaleMultiplier * v / 6;
                this.gridData[u][2] = n * o
            }
        }
        this.radii.sort(function (y, r) {
            return r[1] - y[1]
        })
    };
    f.jqplot.BubbleRenderer.prototype.makeGridData = function (t, w) {
        var q = this._xaxis.series_u2p;
        var n = this._yaxis.series_u2p;
        var x = [];
        var s = [];
        this.radii = [];
        var v = Math.min(w._height, w._width);
        for (var u = 0; u < t.length; u++)
        {
            if (t[u] != null)
            {
                x.push([q.call(this._xaxis, t[u][0]),
                    n.call(this._yaxis, t[u][1]), t[u][2]]);
                s.push(t[u][2]);
                this.radii.push([u, t[u][2]])
            }
        }
        var m, o, y = this.maxRadius = d(s);
        var p = this.gridData.length;
        if (this.autoscaleBubbles)
        {
            for (var u = 0; u < p; u++)
            {
                o = s[u] / y;
                m = this.autoscaleMultiplier * v / 6;
                x[u][2] = m * o
            }
        }
        this.radii.sort(function (z, r) {
            return r[1] - z[1]
        });
        return x
    };
    f.jqplot.BubbleRenderer.prototype.draw = function (D, J, n) {
        if (this.plugins.pointLabels)
        {
            this.plugins.pointLabels.show = false
        }
        var A = (n != undefined) ? n : {};
        var r = (A.shadow != undefined) ? A.shadow : this.shadow;
        this.canvas._elem.empty();
        for (var G = 0; G < this.radii.length; G++)
        {
            var C = this.radii[G][0];
            var z = null;
            var F = null;
            var m = null;
            var p = null;
            var I = this.data[C];
            var J = this.gridData[C];
            if (I[3])
            {
                if (typeof (I[3]) == "object")
                {
                    z = I[3]["label"]
                }
                else
                {
                    if (typeof (I[3]) == "string")
                    {
                        z = I[3]
                    }
                }
            }
            F = this.colorGenerator.get(C);
            var E = J[2];
            var q, K;
            if (this.shadow)
            {
                q = (0.7 + J[2] / 40).toFixed(1);
                K = 1 + Math.ceil(J[2] / 15);
                E += q * K
            }
            this.bubbleCanvases[C] = new f.jqplot.BubbleCanvas();
            this.canvas._elem.append(this.bubbleCanvases[C].createElement(J[0],
                J[1], E));
            this.bubbleCanvases[C].setContext();
            var D = this.bubbleCanvases[C]._ctx;
            var u = D.canvas.width / 2;
            var s = D.canvas.height / 2;
            if (this.shadow)
            {
                this.renderer.shadowRenderer.draw(D, [u, s, J[2], 0,
                    2 * Math.PI], {
                    offset: q,
                    depth: K
                })
            }
            this.bubbleCanvases[C].draw(J[2], F, this.bubbleGradients,
                this.shadowAngle / 180 * Math.PI);
            if (z && this.showLabels)
            {
                p = f('<div style="position:absolute;" class="jqplot-bubble-label"></div>');
                if (this.escapeHtml)
                {
                    p.text(z)
                }
                else
                {
                    p.html(z)
                }
                this.canvas._elem.append(p);
                var H = f(p).outerHeight();
                var v = f(p).outerWidth();
                var B = J[1] - 0.5 * H;
                var o = J[0] - 0.5 * v;
                p.css({
                    top: B,
                    left: o
                });
                this.labels[C] = f(p)
            }
        }
    };
    f.jqplot.DivCanvas = function () {
        f.jqplot.ElemContainer.call(this);
        this._ctx
    };
    f.jqplot.DivCanvas.prototype = new f.jqplot.ElemContainer();
    f.jqplot.DivCanvas.prototype.constructor = f.jqplot.DivCanvas;
    f.jqplot.DivCanvas.prototype.createElement = function (s, p, n) {
        this._offsets = s;
        var m = "jqplot-DivCanvas";
        if (p != undefined)
        {
            m = p
        }
        var r;
        if (this._elem)
        {
            r = this._elem.get(0)
        }
        else
        {
            r = document.createElement("div")
        }
        if (n != undefined)
        {
            this._plotDimensions = n
        }
        var o = this._plotDimensions.width - this._offsets.left
            - this._offsets.right + "px";
        var q = this._plotDimensions.height - this._offsets.top
            - this._offsets.bottom + "px";
        this._elem = f(r);
        this._elem.css({
            position: "absolute",
            width: o,
            height: q,
            left: this._offsets.left,
            top: this._offsets.top
        });
        this._elem.addClass(m);
        return this._elem
    };
    f.jqplot.DivCanvas.prototype.setContext = function () {
        this._ctx = {
            canvas: {
                width: 0,
                height: 0
            },
            clearRect: function () {
                return null
            }
        };
        return this._ctx
    };
    f.jqplot.BubbleCanvas = function () {
        f.jqplot.ElemContainer.call(this);
        this._ctx
    };
    f.jqplot.BubbleCanvas.prototype = new f.jqplot.ElemContainer();
    f.jqplot.BubbleCanvas.prototype.constructor = f.jqplot.BubbleCanvas;
    f.jqplot.BubbleCanvas.prototype.createElement = function (n, u, s) {
        var m = "jqplot-bubble-point";
        var q;
        if (this._elem)
        {
            q = this._elem.get(0)
        }
        else
        {
            q = document.createElement("canvas")
        }
        q.width = (s != null) ? 2 * s : q.width;
        q.height = (s != null) ? 2 * s : q.height;
        this._elem = f(q);
        var o = (n != null && s != null) ? n - s : this._elem.css("left");
        var p = (u != null && s != null) ? u - s : this._elem.css("top");
        this._elem.css({
            position: "absolute",
            left: o,
            top: p
        });
        this._elem.addClass(m);
        if (f.jqplot.use_excanvas)
        {
            window.G_vmlCanvasManager.init_(document);
            q = window.G_vmlCanvasManager.initElement(q)
        }
        return this._elem
    };
    f.jqplot.BubbleCanvas.prototype.draw = function (m, s, v, p) {
        var D = this._ctx;
        var B = D.canvas.width / 2;
        var z = D.canvas.height / 2;
        D.save();
        if (v && !f.jqplot.use_excanvas)
        {
            m = m * 1.04;
            var o = f.jqplot.getColorComponents(s);
            var u = "rgba(" + Math.round(o[0] + 0.8 * (255 - o[0])) + ", "
                + Math.round(o[1] + 0.8 * (255 - o[1])) + ", "
                + Math.round(o[2] + 0.8 * (255 - o[2])) + ", " + o[3] + ")";
            var t = "rgba(" + o[0] + ", " + o[1] + ", " + o[2] + ", 0)";
            var C = 0.35 * m;
            var A = B - Math.cos(p) * 0.33 * m;
            var n = z - Math.sin(p) * 0.33 * m;
            var w = D.createRadialGradient(A, n, C, B, z, m);
            w.addColorStop(0, u);
            w.addColorStop(0.93, s);
            w.addColorStop(0.96, t);
            w.addColorStop(1, t);
            D.fillStyle = w;
            D.fillRect(0, 0, D.canvas.width, D.canvas.height)
        }
        else
        {
            D.fillStyle = s;
            D.strokeStyle = s;
            D.lineWidth = 1;
            D.beginPath();
            var q = 2 * Math.PI;
            D.arc(B, z, m, 0, q, 0);
            D.closePath();
            D.fill()
        }
        D.restore()
    };
    f.jqplot.BubbleCanvas.prototype.setContext = function () {
        this._ctx = this._elem.get(0).getContext("2d");
        return this._ctx
    };
    f.jqplot.BubbleAxisRenderer = function () {
        f.jqplot.LinearAxisRenderer.call(this)
    };
    f.jqplot.BubbleAxisRenderer.prototype = new f.jqplot.LinearAxisRenderer();
    f.jqplot.BubbleAxisRenderer.prototype.constructor = f.jqplot.BubbleAxisRenderer;
    f.jqplot.BubbleAxisRenderer.prototype.init = function (n) {
        f.extend(true, this, n);
        var I = this._dataBounds;
        var H = 0, v = 0, m = 0, y = 0, q = 0, r = 0, D = 0, t = 0, F = 0, z = 0;
        for (var E = 0; E < this._series.length; E++)
        {
            var x = this._series[E];
            var G = x._plotData;
            for (var B = 0; B < G.length; B++)
            {
                if (this.name == "xaxis" || this.name == "x2axis")
                {
                    if (G[B][0] < I.min || I.min == null)
                    {
                        I.min = G[B][0];
                        H = E;
                        v = B;
                        r = G[B][2];
                        D = x.maxRadius;
                        z = x.autoscaleMultiplier
                    }
                    if (G[B][0] > I.max || I.max == null)
                    {
                        I.max = G[B][0];
                        m = E;
                        y = B;
                        q = G[B][2];
                        t = x.maxRadius;
                        F = x.autoscaleMultiplier
                    }
                }
                else
                {
                    if (G[B][1] < I.min || I.min == null)
                    {
                        I.min = G[B][1];
                        H = E;
                        v = B;
                        r = G[B][2];
                        D = x.maxRadius;
                        z = x.autoscaleMultiplier
                    }
                    if (G[B][1] > I.max || I.max == null)
                    {
                        I.max = G[B][1];
                        m = E;
                        y = B;
                        q = G[B][2];
                        t = x.maxRadius;
                        F = x.autoscaleMultiplier
                    }
                }
            }
        }
        var o = r / D;
        var w = q / t;
        var C = I.max - I.min;
        var A = Math.min(this._plotDimensions.width,
            this._plotDimensions.height);
        var p = o * z / 3 * C;
        var u = w * F / 3 * C;
        I.max += u;
        I.min -= p
    };

    function e(p, v, q)
    {
        p.plugins.bubbleRenderer.highlightLabelCanvas.empty();
        var z = p.series[v];
        var n = p.plugins.bubbleRenderer.highlightCanvas;
        var w = n._ctx;
        w.clearRect(0, 0, w.canvas.width, w.canvas.height);
        z._highlightedPoint = q;
        p.plugins.bubbleRenderer.highlightedSeriesIndex = v;
        var o = z.highlightColorGenerator.get(q);
        var u = z.gridData[q][0], t = z.gridData[q][1], m = z.gridData[q][2];
        w.save();
        w.fillStyle = o;
        w.strokeStyle = o;
        w.lineWidth = 1;
        w.beginPath();
        w.arc(u, t, m, 0, 2 * Math.PI, 0);
        w.closePath();
        w.fill();
        w.restore();
        if (z.labels[q])
        {
            p.plugins.bubbleRenderer.highlightLabel = z.labels[q].clone();
            p.plugins.bubbleRenderer.highlightLabel
                .appendTo(p.plugins.bubbleRenderer.highlightLabelCanvas);
            p.plugins.bubbleRenderer.highlightLabel
                .addClass("jqplot-bubble-label-highlight")
        }
    }

    function i(p)
    {
        var m = p.plugins.bubbleRenderer.highlightCanvas;
        var o = p.plugins.bubbleRenderer.highlightedSeriesIndex;
        p.plugins.bubbleRenderer.highlightLabelCanvas.empty();
        m._ctx.clearRect(0, 0, m._ctx.canvas.width, m._ctx.canvas.height);
        for (var n = 0; n < p.series.length; n++)
        {
            p.series[n]._highlightedPoint = null
        }
        p.plugins.bubbleRenderer.highlightedSeriesIndex = null;
        p.target.trigger("jqplotDataUnhighlight")
    }

    function a(s, p, m, v, r)
    {
        if (v)
        {
            var n = v.seriesIndex;
            var o = v.pointIndex;
            var q = [n, o, v.data, r.series[n].gridData[o][2]];
            var t = jQuery.Event("jqplotDataMouseOver");
            t.pageX = s.pageX;
            t.pageY = s.pageY;
            r.target.trigger(t, q);
            if (r.series[q[0]].highlightMouseOver
                && !(q[0] == r.plugins.bubbleRenderer.highlightedSeriesIndex && q[1] == r.series[q[0]]._highlightedPoint))
            {
                var u = jQuery.Event("jqplotDataHighlight");
                u.which = s.which;
                u.pageX = s.pageX;
                u.pageY = s.pageY;
                r.target.trigger(u, q);
                e(r, q[0], q[1])
            }
        }
        else
        {
            if (v == null)
            {
                i(r)
            }
        }
    }

    function b(s, p, m, u, r)
    {
        if (u)
        {
            var n = u.seriesIndex;
            var o = u.pointIndex;
            var q = [n, o, u.data, r.series[n].gridData[o][2]];
            if (r.series[q[0]].highlightMouseDown
                && !(q[0] == r.plugins.bubbleRenderer.highlightedSeriesIndex && q[1] == r.series[q[0]]._highlightedPoint))
            {
                var t = jQuery.Event("jqplotDataHighlight");
                t.which = s.which;
                t.pageX = s.pageX;
                t.pageY = s.pageY;
                r.target.trigger(t, q);
                e(r, q[0], q[1])
            }
        }
        else
        {
            if (u == null)
            {
                i(r)
            }
        }
    }

    function k(o, n, r, q, p)
    {
        var m = p.plugins.bubbleRenderer.highlightedSeriesIndex;
        if (m != null && p.series[m].highlightMouseDown)
        {
            i(p)
        }
    }

    function g(s, p, m, u, r)
    {
        if (u)
        {
            var n = u.seriesIndex;
            var o = u.pointIndex;
            var q = [n, o, u.data, r.series[n].gridData[o][2]];
            var t = jQuery.Event("jqplotDataClick");
            t.which = s.which;
            t.pageX = s.pageX;
            t.pageY = s.pageY;
            r.target.trigger(t, q)
        }
    }

    function l(s, p, m, v, r)
    {
        if (v)
        {
            var n = v.seriesIndex;
            var o = v.pointIndex;
            var q = [n, o, v.data, r.series[n].gridData[o][2]];
            var t = r.plugins.bubbleRenderer.highlightedSeriesIndex;
            if (t != null && r.series[t].highlightMouseDown)
            {
                i(r)
            }
            var u = jQuery.Event("jqplotDataRightClick");
            u.which = s.which;
            u.pageX = s.pageX;
            u.pageY = s.pageY;
            r.target.trigger(u, q)
        }
    }

    function h()
    {
        if (this.plugins.bubbleRenderer
            && this.plugins.bubbleRenderer.highlightCanvas)
        {
            this.plugins.bubbleRenderer.highlightCanvas.resetCanvas();
            this.plugins.bubbleRenderer.highlightCanvas = null
        }
        this.plugins.bubbleRenderer = {
            highlightedSeriesIndex: null
        };
        this.plugins.bubbleRenderer.highlightCanvas = new f.jqplot.GenericCanvas();
        this.plugins.bubbleRenderer.highlightLabel = null;
        this.plugins.bubbleRenderer.highlightLabelCanvas = f('<div style="position:absolute;"></div>');
        var q = this._gridPadding.top;
        var p = this._gridPadding.left;
        var n = this._plotDimensions.width - this._gridPadding.left
            - this._gridPadding.right;
        var m = this._plotDimensions.height - this._gridPadding.top
            - this._gridPadding.bottom;
        this.plugins.bubbleRenderer.highlightLabelCanvas.css({
            top: q,
            left: p,
            width: n + "px",
            height: m + "px"
        });
        this.eventCanvas._elem
            .before(this.plugins.bubbleRenderer.highlightCanvas
                .createElement(this._gridPadding,
                    "jqplot-bubbleRenderer-highlight-canvas",
                    this._plotDimensions, this));
        this.eventCanvas._elem
            .before(this.plugins.bubbleRenderer.highlightLabelCanvas);
        var o = this.plugins.bubbleRenderer.highlightCanvas.setContext()
    }

    function c(q, p, n)
    {
        n = n || {};
        n.axesDefaults = n.axesDefaults || {};
        n.seriesDefaults = n.seriesDefaults || {};
        var m = false;
        if (n.seriesDefaults.renderer == f.jqplot.BubbleRenderer)
        {
            m = true
        }
        else
        {
            if (n.series)
            {
                for (var o = 0; o < n.series.length; o++)
                {
                    if (n.series[o].renderer == f.jqplot.BubbleRenderer)
                    {
                        m = true
                    }
                }
            }
        }
        if (m)
        {
            n.axesDefaults.renderer = f.jqplot.BubbleAxisRenderer;
            n.sortData = false
        }
    }

    f.jqplot.preInitHooks.push(c)
})(jQuery);
(function (a) {
    a.jqplot.OHLCRenderer = function () {
        a.jqplot.LineRenderer.call(this);
        this.candleStick = false;
        this.tickLength = "auto";
        this.bodyWidth = "auto";
        this.openColor = null;
        this.closeColor = null;
        this.wickColor = null;
        this.fillUpBody = false;
        this.fillDownBody = true;
        this.upBodyColor = null;
        this.downBodyColor = null;
        this.hlc = false;
        this.lineWidth = 1.5;
        this._tickLength;
        this._bodyWidth
    };
    a.jqplot.OHLCRenderer.prototype = new a.jqplot.LineRenderer();
    a.jqplot.OHLCRenderer.prototype.constructor = a.jqplot.OHLCRenderer;
    a.jqplot.OHLCRenderer.prototype.init = function (e) {
        e = e || {};
        this.lineWidth = e.lineWidth || 1.5;
        a.jqplot.LineRenderer.prototype.init.call(this, e);
        this._type = "ohlc";
        var b = this._yaxis._dataBounds;
        var f = this._plotData;
        if (f[0].length < 5)
        {
            this.renderer.hlc = true;
            for (var c = 0; c < f.length; c++)
            {
                if (f[c][2] < b.min || b.min == null)
                {
                    b.min = f[c][2]
                }
                if (f[c][1] > b.max || b.max == null)
                {
                    b.max = f[c][1]
                }
            }
        }
        else
        {
            for (var c = 0; c < f.length; c++)
            {
                if (f[c][3] < b.min || b.min == null)
                {
                    b.min = f[c][3]
                }
                if (f[c][2] > b.max || b.max == null)
                {
                    b.max = f[c][2]
                }
            }
        }
    };
    a.jqplot.OHLCRenderer.prototype.draw = function (A, N, j) {
        var J = this.data;
        var v = this._xaxis.min;
        var z = this._xaxis.max;
        var l = 0;
        var K = J.length;
        var p = this._xaxis.series_u2p;
        var G = this._yaxis.series_u2p;
        var D, E, f, M, F, n, O, C;
        var y;
        var u = this.renderer;
        var s = (j != undefined) ? j : {};
        var k = (s.shadow != undefined) ? s.shadow : this.shadow;
        var B = (s.fill != undefined) ? s.fill : this.fill;
        var c = (s.fillAndStroke != undefined) ? s.fillAndStroke
            : this.fillAndStroke;
        u.bodyWidth = (s.bodyWidth != undefined) ? s.bodyWidth : u.bodyWidth;
        u.tickLength = (s.tickLength != undefined) ? s.tickLength
            : u.tickLength;
        A.save();
        if (this.show)
        {
            var m, q, g, Q, t;
            for (var D = 0; D < J.length; D++)
            {
                if (J[D][0] < v)
                {
                    l = D
                }
                else
                {
                    if (J[D][0] < z)
                    {
                        K = D + 1
                    }
                }
            }
            var I = this.gridData[K - 1][0] - this.gridData[l][0];
            var L = K - l;
            try
            {
                var P = Math
                    .abs(this._xaxis
                            .series_u2p(parseInt(
                                this._xaxis._intervalStats[0].sortedIntervals[0].interval,
                                10))
                        - this._xaxis.series_u2p(0))
            }
            catch (H)
            {
                var P = I / L
            }
            if (u.candleStick)
            {
                if (typeof (u.bodyWidth) == "number")
                {
                    u._bodyWidth = u.bodyWidth
                }
                else
                {
                    u._bodyWidth = Math.min(20, P / 1.65)
                }
            }
            else
            {
                if (typeof (u.tickLength) == "number")
                {
                    u._tickLength = u.tickLength
                }
                else
                {
                    u._tickLength = Math.min(10, P / 3.5)
                }
            }
            for (var D = l; D < K; D++)
            {
                m = p(J[D][0]);
                if (u.hlc)
                {
                    q = null;
                    g = G(J[D][1]);
                    Q = G(J[D][2]);
                    t = G(J[D][3])
                }
                else
                {
                    q = G(J[D][1]);
                    g = G(J[D][2]);
                    Q = G(J[D][3]);
                    t = G(J[D][4])
                }
                y = {};
                if (u.candleStick && !u.hlc)
                {
                    n = u._bodyWidth;
                    O = m - n / 2;
                    if (t < q)
                    {
                        if (u.wickColor)
                        {
                            y.color = u.wickColor
                        }
                        else
                        {
                            if (u.downBodyColor)
                            {
                                y.color = u.upBodyColor
                            }
                        }
                        f = a.extend(true, {}, s, y);
                        u.shapeRenderer.draw(A, [[m, g], [m, t]], f);
                        u.shapeRenderer.draw(A, [[m, q], [m, Q]], f);
                        y = {};
                        M = t;
                        F = q - t;
                        if (u.fillUpBody)
                        {
                            y.fillRect = true
                        }
                        else
                        {
                            y.strokeRect = true;
                            n = n - this.lineWidth;
                            O = m - n / 2
                        }
                        if (u.upBodyColor)
                        {
                            y.color = u.upBodyColor;
                            y.fillStyle = u.upBodyColor
                        }
                        C = [O, M, n, F]
                    }
                    else
                    {
                        if (t > q)
                        {
                            if (u.wickColor)
                            {
                                y.color = u.wickColor
                            }
                            else
                            {
                                if (u.downBodyColor)
                                {
                                    y.color = u.downBodyColor
                                }
                            }
                            f = a.extend(true, {}, s, y);
                            u.shapeRenderer.draw(A, [[m, g], [m, q]], f);
                            u.shapeRenderer.draw(A, [[m, t], [m, Q]], f);
                            y = {};
                            M = q;
                            F = t - q;
                            if (u.fillDownBody)
                            {
                                y.fillRect = true
                            }
                            else
                            {
                                y.strokeRect = true;
                                n = n - this.lineWidth;
                                O = m - n / 2
                            }
                            if (u.downBodyColor)
                            {
                                y.color = u.downBodyColor;
                                y.fillStyle = u.downBodyColor
                            }
                            C = [O, M, n, F]
                        }
                        else
                        {
                            if (u.wickColor)
                            {
                                y.color = u.wickColor
                            }
                            f = a.extend(true, {}, s, y);
                            u.shapeRenderer.draw(A, [[m, g], [m, Q]], f);
                            y = {};
                            y.fillRect = false;
                            y.strokeRect = false;
                            O = [m - n / 2, q];
                            M = [m + n / 2, t];
                            n = null;
                            F = null;
                            C = [O, M]
                        }
                    }
                    f = a.extend(true, {}, s, y);
                    u.shapeRenderer.draw(A, C, f)
                }
                else
                {
                    E = s.color;
                    if (u.openColor)
                    {
                        s.color = u.openColor
                    }
                    if (!u.hlc)
                    {
                        u.shapeRenderer.draw(A, [[m - u._tickLength, q],
                            [m, q]], s)
                    }
                    s.color = E;
                    if (u.wickColor)
                    {
                        s.color = u.wickColor
                    }
                    u.shapeRenderer.draw(A, [[m, g], [m, Q]], s);
                    s.color = E;
                    if (u.closeColor)
                    {
                        s.color = u.closeColor
                    }
                    u.shapeRenderer.draw(A, [[m, t],
                        [m + u._tickLength, t]], s);
                    s.color = E
                }
            }
        }
        A.restore()
    };
    a.jqplot.OHLCRenderer.prototype.drawShadow = function (b, d, c) {
    };
    a.jqplot.OHLCRenderer.checkOptions = function (d, c, b) {
        if (!b.highlighter)
        {
            b.highlighter = {
                showMarker: false,
                tooltipAxes: "y",
                yvalues: 4,
                formatString: '<table class="jqplot-highlighter"><tr><td>date:</td><td>%s</td></tr><tr><td>open:</td><td>%s</td></tr><tr><td>hi:</td><td>%s</td></tr><tr><td>low:</td><td>%s</td></tr><tr><td>close:</td><td>%s</td></tr></table>'
            }
        }
    }
})(jQuery);
(function (c) {
    c.jqplot.MeterGaugeRenderer = function () {
        c.jqplot.LineRenderer.call(this)
    };
    c.jqplot.MeterGaugeRenderer.prototype = new c.jqplot.LineRenderer();
    c.jqplot.MeterGaugeRenderer.prototype.constructor = c.jqplot.MeterGaugeRenderer;
    c.jqplot.MeterGaugeRenderer.prototype.init = function (e) {
        this.diameter = null;
        this.padding = null;
        this.shadowOffset = 2;
        this.shadowAlpha = 0.07;
        this.shadowDepth = 4;
        this.background = "#efefef";
        this.ringColor = "#BBC6D0";
        this.needleColor = "#C3D3E5";
        this.tickColor = "#989898";
        this.ringWidth = null;
        this.min;
        this.max;
        this.ticks = [];
        this.showTicks = true;
        this.showTickLabels = true;
        this.label = null;
        this.labelHeightAdjust = 0;
        this.labelPosition = "inside";
        this.intervals = [];
        this.intervalColors = ["#4bb2c5", "#EAA228", "#c5b47f", "#579575",
            "#839557", "#958c12", "#953579", "#4b5de4", "#d8b83f",
            "#ff5800", "#0085cc", "#c747a3", "#cddf54", "#FBD178",
            "#26B4E3", "#bd70c7"];
        this.intervalInnerRadius = null;
        this.intervalOuterRadius = null;
        this.tickRenderer = c.jqplot.MeterGaugeTickRenderer;
        this.tickPositions = [1, 2, 2.5, 5, 10];
        this.tickSpacing = 30;
        this.numberMinorTicks = null;
        this.hubRadius = null;
        this.tickPadding = null;
        this.needleThickness = null;
        this.needlePad = 6;
        this.pegNeedle = true;
        this._type = "meterGauge";
        c.extend(true, this, e);
        this.type = null;
        this.numberTicks = null;
        this.tickInterval = null;
        this.span = 180;
        if (this.type == "circular")
        {
            this.semiCircular = false
        }
        else
        {
            if (this.type != "circular")
            {
                this.semiCircular = true
            }
            else
            {
                this.semiCircular = (this.span <= 180) ? true : false
            }
        }
        this._tickPoints = [];
        this._labelElem = null;
        this.startAngle = (90 + (360 - this.span) / 2) * Math.PI / 180;
        this.endAngle = (90 - (360 - this.span) / 2) * Math.PI / 180;
        this.setmin = !!(this.min == null);
        this.setmax = !!(this.max == null);
        if (this.intervals.length)
        {
            if (this.intervals[0].length == null || this.intervals.length == 1)
            {
                for (var f = 0; f < this.intervals.length; f++)
                {
                    this.intervals[f] = [this.intervals[f], this.intervals[f],
                        this.intervalColors[f]]
                }
            }
            else
            {
                if (this.intervals[0].length == 2)
                {
                    for (f = 0; f < this.intervals.length; f++)
                    {
                        this.intervals[f] = [this.intervals[f][0],
                            this.intervals[f][1], this.intervalColors[f]]
                    }
                }
            }
        }
        if (this.ticks.length)
        {
            if (this.ticks[0].length == null || this.ticks[0].length == 1)
            {
                for (var f = 0; f < this.ticks.length; f++)
                {
                    this.ticks[f] = [this.ticks[f], this.ticks[f]]
                }
            }
            this.min = (this.min == null) ? this.ticks[0][0] : this.min;
            this.max = (this.max == null) ? this.ticks[this.ticks.length - 1][0]
                : this.max;
            this.setmin = false;
            this.setmax = false;
            this.numberTicks = this.ticks.length;
            this.tickInterval = this.ticks[1][0] - this.ticks[0][0];
            this.tickFactor = Math.floor(parseFloat((Math
                .log(this.tickInterval) / Math.log(10)).toFixed(11)));
            this.numberMinorTicks = b(this.tickPositions, this.tickInterval,
                this.tickFactor);
            if (!this.numberMinorTicks)
            {
                this.numberMinorTicks = b(this.tickPositions,
                    this.tickInterval, this.tickFactor - 1)
            }
            if (!this.numberMinorTicks)
            {
                this.numberMinorTicks = 1
            }
        }
        else
        {
            if (this.intervals.length)
            {
                this.min = (this.min == null) ? 0 : this.min;
                this.setmin = false;
                if (this.max == null)
                {
                    if (this.intervals[this.intervals.length - 1][0] >= this.data[0][1])
                    {
                        this.max = this.intervals[this.intervals.length - 1][0];
                        this.setmax = false
                    }
                }
                else
                {
                    this.setmax = false
                }
            }
            else
            {
                this.min = (this.min == null) ? 0 : this.min;
                this.setmin = false;
                if (this.max == null)
                {
                    this.max = this.data[0][1] * 1.25;
                    this.setmax = true
                }
                else
                {
                    this.setmax = false
                }
            }
        }
    };
    c.jqplot.MeterGaugeRenderer.prototype.setGridData = function (j) {
        var f = [];
        var k = [];
        var e = this.startAngle;
        for (var h = 0; h < this.data.length; h++)
        {
            f.push(this.data[h][1]);
            k.push([this.data[h][0]]);
            if (h > 0)
            {
                f[h] += f[h - 1]
            }
        }
        var g = Math.PI * 2 / f[f.length - 1];
        for (var h = 0; h < f.length; h++)
        {
            k[h][1] = f[h] * g
        }
        this.gridData = k
    };
    c.jqplot.MeterGaugeRenderer.prototype.makeGridData = function (j, k) {
        var f = [];
        var l = [];
        var e = this.startAngle;
        for (var h = 0; h < j.length; h++)
        {
            f.push(j[h][1]);
            l.push([j[h][0]]);
            if (h > 0)
            {
                f[h] += f[h - 1]
            }
        }
        var g = Math.PI * 2 / f[f.length - 1];
        for (var h = 0; h < f.length; h++)
        {
            l[h][1] = f[h] * g
        }
        return l
    };

    function b(j, f, g)
    {
        var e;
        for (var h = j.length - 1; h >= 0; h--)
        {
            e = f / (j[h] * Math.pow(10, g));
            if (e == 4 || e == 5)
            {
                return e - 1
            }
        }
        return null
    }

    c.jqplot.MeterGaugeRenderer.prototype.draw = function (X, aC, ap) {
        var aa;
        var aM = (ap != undefined) ? ap : {};
        var ai = 0;
        var ah = 0;
        var at = 1;
        if (ap.legendInfo && ap.legendInfo.placement == "inside")
        {
            var aI = ap.legendInfo;
            switch (aI.location)
            {
                case "nw":
                    ai = aI.width + aI.xoffset;
                    break;
                case "w":
                    ai = aI.width + aI.xoffset;
                    break;
                case "sw":
                    ai = aI.width + aI.xoffset;
                    break;
                case "ne":
                    ai = aI.width + aI.xoffset;
                    at = -1;
                    break;
                case "e":
                    ai = aI.width + aI.xoffset;
                    at = -1;
                    break;
                case "se":
                    ai = aI.width + aI.xoffset;
                    at = -1;
                    break;
                case "n":
                    ah = aI.height + aI.yoffset;
                    break;
                case "s":
                    ah = aI.height + aI.yoffset;
                    at = -1;
                    break;
                default:
                    break
            }
        }
        if (this.label)
        {
            this._labelElem = c('<div class="jqplot-meterGauge-label" style="position:absolute;">'
                + this.label + "</div>");
            this.canvas._elem.after(this._labelElem)
        }
        var m = (aM.shadow != undefined) ? aM.shadow : this.shadow;
        var N = (aM.showLine != undefined) ? aM.showLine : this.showLine;
        var I = (aM.fill != undefined) ? aM.fill : this.fill;
        var K = X.canvas.width;
        var S = X.canvas.height;
        if (this.padding == null)
        {
            this.padding = Math.round(Math.min(K, S) / 30)
        }
        var Q = K - ai - 2 * this.padding;
        var ab = S - ah - 2 * this.padding;
        if (this.labelPosition == "bottom" && this.label)
        {
            ab -= this._labelElem.outerHeight(true)
        }
        var L = Math.min(Q, ab);
        var ad = L;
        if (!this.diameter)
        {
            if (this.semiCircular)
            {
                if (Q >= 2 * ab)
                {
                    if (!this.ringWidth)
                    {
                        this.ringWidth = 2 * ab / 35
                    }
                    this.needleThickness = this.needleThickness || 2
                        + Math.pow(this.ringWidth, 0.8);
                    this.innerPad = this.ringWidth / 2 + this.needleThickness
                        / 2 + this.needlePad;
                    this.diameter = 2 * (ab - 2 * this.innerPad)
                }
                else
                {
                    if (!this.ringWidth)
                    {
                        this.ringWidth = Q / 35
                    }
                    this.needleThickness = this.needleThickness || 2
                        + Math.pow(this.ringWidth, 0.8);
                    this.innerPad = this.ringWidth / 2 + this.needleThickness
                        / 2 + this.needlePad;
                    this.diameter = Q - 2 * this.innerPad - this.ringWidth
                        - this.padding
                }
                this._center = [
                    (K - at * ai) / 2 + at * ai,
                    (S + at * ah - this.padding - this.ringWidth - this.innerPad)]
            }
            else
            {
                if (!this.ringWidth)
                {
                    this.ringWidth = ad / 35
                }
                this.needleThickness = this.needleThickness || 2
                    + Math.pow(this.ringWidth, 0.8);
                this.innerPad = 0;
                this.diameter = ad - this.ringWidth;
                this._center = [(K - at * ai) / 2 + at * ai,
                    (S - at * ah) / 2 + at * ah]
            }
            if (this._labelElem && this.labelPosition == "bottom")
            {
                this._center[1] -= this._labelElem.outerHeight(true)
            }
        }
        this._radius = this.diameter / 2;
        this.tickSpacing = 6000 / this.diameter;
        if (!this.hubRadius)
        {
            this.hubRadius = this.diameter / 18
        }
        this.shadowOffset = 0.5 + this.ringWidth / 9;
        this.shadowWidth = this.ringWidth * 1;
        this.tickPadding = 3 + Math.pow(this.diameter / 20, 0.7);
        this.tickOuterRadius = this._radius - this.ringWidth / 2
            - this.tickPadding;
        this.tickLength = (this.showTicks) ? this._radius / 13 : 0;
        if (this.ticks.length == 0)
        {
            var A = this.max, aL = this.min, q = this.setmax, aG = this.setmin, au = (A - aL)
                * this.tickSpacing / this.span;
            var aw = Math.floor(parseFloat((Math.log(au) / Math.log(10))
                .toFixed(11)));
            var an = (au / Math.pow(10, aw));
            (an > 2 && an <= 2.5) ? an = 2.5 : an = Math.ceil(an);
            var T = this.tickPositions;
            var aA, ak;
            for (aa = 0; aa < T.length; aa++)
            {
                if (an == T[aa] || aa && T[aa - 1] < an && an < T[aa])
                {
                    au = T[aa] * Math.pow(10, aw);
                    aA = aa
                }
            }
            for (aa = 0; aa < T.length; aa++)
            {
                if (an == T[aa] || aa && T[aa - 1] < an && an < T[aa])
                {
                    au = T[aa] * Math.pow(10, aw);
                    ak = Math.ceil((A - aL) / au)
                }
            }
            if (q && aG)
            {
                var aP = (aL > 0) ? aL - aL % au : aL - aL % au - au;
                if (!this.forceZero)
                {
                    var D = Math.min(aL - aP, 0.8 * au);
                    var o = Math.floor(D / T[aA]);
                    if (o > 1)
                    {
                        aP = aP + T[aA] * (o - 1);
                        if (parseInt(aP, 10) != aP
                            && parseInt(aP - T[aA], 10) == aP - T[aA])
                        {
                            aP = aP - T[aA]
                        }
                    }
                }
                if (aL == aP)
                {
                    aL -= au
                }
                else
                {
                    if (aL - aP > 0.23 * au)
                    {
                        aL = aP
                    }
                    else
                    {
                        aL = aP - au;
                        ak += 1
                    }
                }
                ak += 1;
                var E = aL + (ak - 1) * au;
                if (A >= E)
                {
                    E += au;
                    ak += 1
                }
                if (E - A < 0.23 * au)
                {
                    E += au;
                    ak += 1
                }
                this.max = A = E;
                this.min = aL;
                this.tickInterval = au;
                this.numberTicks = ak;
                var O;
                for (aa = 0; aa < ak; aa++)
                {
                    O = parseFloat((aL + aa * au).toFixed(11));
                    this.ticks.push([O, O])
                }
                this.max = this.ticks[ak - 1][1];
                this.tickFactor = aw;
                this.numberMinorTicks = b(this.tickPositions,
                    this.tickInterval, this.tickFactor);
                if (!this.numberMinorTicks)
                {
                    this.numberMinorTicks = b(this.tickPositions,
                        this.tickInterval, this.tickFactor - 1)
                }
            }
            else
            {
                if (q)
                {
                    var E = aL + (ak - 1) * au;
                    if (A >= E)
                    {
                        A = E + au;
                        ak += 1
                    }
                    else
                    {
                        A = E
                    }
                    this.tickInterval = this.tickInterval || au;
                    this.numberTicks = this.numberTicks || ak;
                    var O;
                    for (aa = 0; aa < this.numberTicks; aa++)
                    {
                        O = parseFloat((aL + aa * this.tickInterval)
                            .toFixed(11));
                        this.ticks.push([O, O])
                    }
                    this.max = this.ticks[this.numberTicks - 1][1];
                    this.tickFactor = aw;
                    this.numberMinorTicks = b(this.tickPositions,
                        this.tickInterval, this.tickFactor);
                    if (!this.numberMinorTicks)
                    {
                        this.numberMinorTicks = b(this.tickPositions,
                            this.tickInterval, this.tickFactor - 1)
                    }
                }
            }
            if (!q && !aG)
            {
                var P = this.max - this.min;
                aw = Math.floor(parseFloat((Math.log(P) / Math.log(10))
                    .toFixed(11))) - 1;
                var aN = [5, 6, 4, 7, 3, 8, 9, 10, 2], V, C, av = 0, M;
                if (P > 1)
                {
                    var aJ = String(P);
                    if (aJ.search(/\./) == -1)
                    {
                        var aF = aJ.search(/0+$/);
                        av = (aF > 0) ? aJ.length - aF - 1 : 0
                    }
                }
                M = P / Math.pow(10, av);
                for (aa = 0; aa < aN.length; aa++)
                {
                    V = M / (aN[aa] - 1);
                    if (V == parseInt(V, 10))
                    {
                        this.numberTicks = aN[aa];
                        this.tickInterval = P / (this.numberTicks - 1);
                        this.tickFactor = aw + 1;
                        break
                    }
                }
                var O;
                for (aa = 0; aa < this.numberTicks; aa++)
                {
                    O = parseFloat((this.min + aa * this.tickInterval)
                        .toFixed(11));
                    this.ticks.push([O, O])
                }
                this.numberMinorTicks = b(this.tickPositions,
                    this.tickInterval, this.tickFactor);
                if (!this.numberMinorTicks)
                {
                    this.numberMinorTicks = b(this.tickPositions,
                        this.tickInterval, this.tickFactor - 1)
                }
                if (!this.numberMinorTicks)
                {
                    this.numberMinorTicks = 1;
                    var aH = [4, 5, 3, 6, 2];
                    for (aa = 0; aa < 5; aa++)
                    {
                        var ao = this.tickInterval / aH[aa];
                        if (ao == parseInt(ao, 10))
                        {
                            this.numberMinorTicks = aH[aa] - 1;
                            break
                        }
                    }
                }
            }
        }
        var U = this._radius, aE = this.startAngle, k = this.endAngle, H = Math.PI, e = Math.PI / 2;
        if (this.semiCircular)
        {
            var z = Math.atan(this.innerPad / U), ac = this.outerStartAngle = aE
                - z, aB = this.outerEndAngle = k + z, B = this.hubStartAngle = aE
                - Math.atan(this.innerPad / this.hubRadius * 2), af = this.hubEndAngle = k
                + Math.atan(this.innerPad / this.hubRadius * 2);
            X.save();
            X.translate(this._center[0], this._center[1]);
            X.lineJoin = "round";
            X.lineCap = "round";
            X.save();
            X.beginPath();
            X.fillStyle = this.background;
            X.arc(0, 0, U, ac, aB, false);
            X.closePath();
            X.fill();
            X.restore();
            var aj = "rgba(0,0,0," + this.shadowAlpha + ")";
            X.save();
            for (var aa = 0; aa < this.shadowDepth; aa++)
            {
                X.translate(this.shadowOffset
                    * Math.cos(this.shadowAngle / 180 * Math.PI),
                    this.shadowOffset
                    * Math.sin(this.shadowAngle / 180 * Math.PI));
                X.beginPath();
                X.strokeStyle = aj;
                X.lineWidth = this.shadowWidth;
                X.arc(0, 0, U, ac, aB, false);
                X.closePath();
                X.stroke()
            }
            X.restore();
            X.save();
            var az = parseInt((this.shadowDepth + 1) / 2, 10);
            for (var aa = 0; aa < az; aa++)
            {
                X.translate(this.shadowOffset
                    * Math.cos(this.shadowAngle / 180 * Math.PI),
                    this.shadowOffset
                    * Math.sin(this.shadowAngle / 180 * Math.PI));
                X.beginPath();
                X.fillStyle = aj;
                X.arc(0, 0, this.hubRadius, B, af, false);
                X.closePath();
                X.fill()
            }
            X.restore();
            X.save();
            X.beginPath();
            X.strokeStyle = this.ringColor;
            X.lineWidth = this.ringWidth;
            X.arc(0, 0, U, ac, aB, false);
            X.closePath();
            X.stroke();
            X.restore();
            X.save();
            X.beginPath();
            X.fillStyle = this.ringColor;
            X.arc(0, 0, this.hubRadius, B, af, false);
            X.closePath();
            X.fill();
            X.restore();
            if (this.showTicks)
            {
                X.save();
                var f = this.tickOuterRadius, aq = this.tickLength, v = aq / 2, F = this.numberMinorTicks,
                    am = this.span
                        * Math.PI / 180 / (this.ticks.length - 1), p = am
                    / (F + 1);
                for (aa = 0; aa < this.ticks.length; aa++)
                {
                    X.beginPath();
                    X.lineWidth = 1.5 + this.diameter / 360;
                    X.strokeStyle = this.ringColor;
                    var ae = am * aa + aE;
                    X.moveTo(-f * Math.cos(am * aa + aE), f
                        * Math.sin(am * aa + aE));
                    X.lineTo(-(f - aq) * Math.cos(am * aa + aE), (f - aq)
                        * Math.sin(am * aa + aE));
                    this._tickPoints.push([
                        (f - aq) * Math.cos(am * aa + aE) + this._center[0]
                        + this.canvas._offsets.left,
                        (f - aq) * Math.sin(am * aa + aE) + this._center[1]
                        + this.canvas._offsets.top, am * aa + aE]);
                    X.stroke();
                    X.lineWidth = 1 + this.diameter / 440;
                    if (aa < this.ticks.length - 1)
                    {
                        for (var Y = 1; Y <= F; Y++)
                        {
                            X.beginPath();
                            X.moveTo(-f * Math.cos(am * aa + p * Y + aE), f
                                * Math.sin(am * aa + p * Y + aE));
                            X.lineTo(-(f - v) * Math.cos(am * aa + p * Y + aE),
                                (f - v) * Math.sin(am * aa + p * Y + aE));
                            X.stroke()
                        }
                    }
                }
                X.restore()
            }
            if (this.showTickLabels)
            {
                var J, W, T, aO, g, G, n = 0;
                var an = this.tickPadding * (1 - 1 / (this.diameter / 80 + 1));
                for (aa = 0; aa < this.ticks.length; aa++)
                {
                    J = c('<div class="jqplot-meterGauge-tick" style="position:absolute;">'
                        + this.ticks[aa][1] + "</div>");
                    this.canvas._elem.after(J);
                    aO = J.outerWidth(true);
                    g = J.outerHeight(true);
                    W = this._tickPoints[aa][0] - aO
                        * (this._tickPoints[aa][2] - Math.PI) / Math.PI
                        - an * Math.cos(this._tickPoints[aa][2]);
                    T = this._tickPoints[aa][1]
                        - g
                        / 2
                        + g
                        / 2
                        * Math.pow(Math.abs((Math
                            .sin(this._tickPoints[aa][2]))), 0.5)
                        + an
                        / 3
                        * Math.pow(Math.abs((Math
                            .sin(this._tickPoints[aa][2]))), 0.5);
                    J.css({
                        left: W,
                        top: T,
                        color: this.tickColor
                    });
                    G = aO
                        * Math.cos(this._tickPoints[aa][2])
                        + g
                        * Math.sin(Math.PI / 2 + this._tickPoints[aa][2]
                            / 2);
                    n = (G > n) ? G : n
                }
            }
            if (this.label && this.labelPosition == "inside")
            {
                var W = this._center[0] + this.canvas._offsets.left;
                var an = this.tickPadding * (1 - 1 / (this.diameter / 80 + 1));
                var T = 0.5
                    * (this._center[1] + this.canvas._offsets.top - this.hubRadius)
                    + 0.5
                    * (this._center[1] + this.canvas._offsets.top
                        - this.tickOuterRadius + this.tickLength + an)
                    + this.labelHeightAdjust;
                W -= this._labelElem.outerWidth(true) / 2;
                T -= this._labelElem.outerHeight(true) / 2;
                this._labelElem.css({
                    left: W,
                    top: T
                })
            }
            else
            {
                if (this.label && this.labelPosition == "bottom")
                {
                    var W = this._center[0] + this.canvas._offsets.left
                        - this._labelElem.outerWidth(true) / 2;
                    var T = this._center[1] + this.canvas._offsets.top
                        + this.innerPad + this.ringWidth + this.padding
                        + this.labelHeightAdjust;
                    this._labelElem.css({
                        left: W,
                        top: T
                    })
                }
            }
            X.save();
            var ax = this.intervalInnerRadius || this.hubRadius * 1.5;
            if (this.intervalOuterRadius == null)
            {
                if (this.showTickLabels)
                {
                    var ag = (this.tickOuterRadius - this.tickLength
                        - this.tickPadding - this.diameter / 8)
                }
                else
                {
                    var ag = (this.tickOuterRadius - this.tickLength - this.diameter / 16)
                }
            }
            else
            {
                var ag = this.intervalOuterRadius
            }
            var P = this.max - this.min;
            var aD = this.intervals[this.intervals.length - 1] - this.min;
            var y, Z, u = this.span * Math.PI / 180;
            for (aa = 0; aa < this.intervals.length; aa++)
            {
                y = (aa == 0) ? aE : aE
                    + (this.intervals[aa - 1][0] - this.min) * u / P;
                if (y < 0)
                {
                    y = 0
                }
                Z = aE + (this.intervals[aa][0] - this.min) * u / P;
                if (Z < 0)
                {
                    Z = 0
                }
                X.beginPath();
                X.fillStyle = this.intervals[aa][2];
                X.arc(0, 0, ax, y, Z, false);
                X.lineTo(ag * Math.cos(Z), ag * Math.sin(Z));
                X.arc(0, 0, ag, Z, y, true);
                X.lineTo(ax * Math.cos(y), ax * Math.sin(y));
                X.closePath();
                X.fill()
            }
            X.restore();
            var ay = this.data[0][1];
            var R = this.max - this.min;
            if (this.pegNeedle)
            {
                if (this.data[0][1] > this.max + R * 3 / this.span)
                {
                    ay = this.max + R * 3 / this.span
                }
                if (this.data[0][1] < this.min - R * 3 / this.span)
                {
                    ay = this.min - R * 3 / this.span
                }
            }
            var al = (ay - this.min) / R * this.span * Math.PI / 180
                + this.startAngle;
            X.save();
            X.beginPath();
            X.fillStyle = this.ringColor;
            X.strokeStyle = this.ringColor;
            this.needleLength = (this.tickOuterRadius - this.tickLength) * 0.85;
            this.needleThickness = (this.needleThickness < 2) ? 2
                : this.needleThickness;
            var aK = this.needleThickness * 0.4;
            var x = this.needleLength / 10;
            var s = (this.needleThickness - aK) / 10;
            var ar;
            for (var aa = 0; aa < 10; aa++)
            {
                ar = this.needleThickness - aa * s;
                X.moveTo(x * aa * Math.cos(al), x * aa * Math.sin(al));
                X.lineWidth = ar;
                X.lineTo(x * (aa + 1) * Math.cos(al), x * (aa + 1)
                    * Math.sin(al));
                X.stroke()
            }
            X.restore()
        }
        else
        {
            this._center = [(K - at * ai) / 2 + at * ai,
                (S - at * ah) / 2 + at * ah]
        }
    };
    c.jqplot.MeterGaugeAxisRenderer = function () {
        c.jqplot.LinearAxisRenderer.call(this)
    };
    c.jqplot.MeterGaugeAxisRenderer.prototype = new c.jqplot.LinearAxisRenderer();
    c.jqplot.MeterGaugeAxisRenderer.prototype.constructor = c.jqplot.MeterGaugeAxisRenderer;
    c.jqplot.MeterGaugeAxisRenderer.prototype.init = function (e) {
        this.tickRenderer = c.jqplot.MeterGaugeTickRenderer;
        c.extend(true, this, e);
        this._dataBounds = {
            min: 0,
            max: 100
        };
        this.min = 0;
        this.max = 100;
        this.showTicks = false;
        this.ticks = [];
        this.showMark = false;
        this.show = false
    };
    c.jqplot.MeterGaugeLegendRenderer = function () {
        c.jqplot.TableLegendRenderer.call(this)
    };
    c.jqplot.MeterGaugeLegendRenderer.prototype = new c.jqplot.TableLegendRenderer();
    c.jqplot.MeterGaugeLegendRenderer.prototype.constructor = c.jqplot.MeterGaugeLegendRenderer;
    c.jqplot.MeterGaugeLegendRenderer.prototype.init = function (e) {
        this.numberRows = null;
        this.numberColumns = null;
        c.extend(true, this, e)
    };
    c.jqplot.MeterGaugeLegendRenderer.prototype.draw = function () {
        if (this.show)
        {
            var p = this._series;
            var x = "position:absolute;";
            x += (this.background) ? "background:" + this.background + ";" : "";
            x += (this.border) ? "border:" + this.border + ";" : "";
            x += (this.fontSize) ? "font-size:" + this.fontSize + ";" : "";
            x += (this.fontFamily) ? "font-family:" + this.fontFamily + ";"
                : "";
            x += (this.textColor) ? "color:" + this.textColor + ";" : "";
            x += (this.marginTop != null) ? "margin-top:" + this.marginTop
                + ";" : "";
            x += (this.marginBottom != null) ? "margin-bottom:"
                + this.marginBottom + ";" : "";
            x += (this.marginLeft != null) ? "margin-left:" + this.marginLeft
                + ";" : "";
            x += (this.marginRight != null) ? "margin-right:"
                + this.marginRight + ";" : "";
            this._elem = c('<table class="jqplot-table-legend" style="' + x
                + '"></table>');
            var f = false, q = false, u, o;
            var w = p[0];
            if (w.show)
            {
                var t = w.data;
                if (this.numberRows)
                {
                    u = this.numberRows;
                    if (!this.numberColumns)
                    {
                        o = Math.ceil(t.length / u)
                    }
                    else
                    {
                        o = this.numberColumns
                    }
                }
                else
                {
                    if (this.numberColumns)
                    {
                        o = this.numberColumns;
                        u = Math.ceil(t.length / this.numberColumns)
                    }
                    else
                    {
                        u = t.length;
                        o = 1
                    }
                }
                var n, m, r, g, e, l, k, h;
                var v = 0;
                for (n = 0; n < u; n++)
                {
                    if (q)
                    {
                        r = c('<tr class="jqplot-table-legend"></tr>')
                            .prependTo(this._elem)
                    }
                    else
                    {
                        r = c('<tr class="jqplot-table-legend"></tr>')
                            .appendTo(this._elem)
                    }
                    for (m = 0; m < o; m++)
                    {
                        if (v < t.length)
                        {
                            l = this.labels[v] || t[v][0].toString();
                            h = w.color;
                            if (!q)
                            {
                                if (n > 0)
                                {
                                    f = true
                                }
                                else
                                {
                                    f = false
                                }
                            }
                            else
                            {
                                if (n == u - 1)
                                {
                                    f = false
                                }
                                else
                                {
                                    f = true
                                }
                            }
                            k = (f) ? this.rowSpacing : "0";
                            g = c('<td class="jqplot-table-legend" style="text-align:center;padding-top:'
                                + k
                                + ';"><div><div class="jqplot-table-legend-swatch" style="border-color:'
                                + h + ';"></div></div></td>');
                            e = c('<td class="jqplot-table-legend" style="padding-top:'
                                + k + ';"></td>');
                            if (this.escapeHtml)
                            {
                                e.text(l)
                            }
                            else
                            {
                                e.html(l)
                            }
                            if (q)
                            {
                                e.prependTo(r);
                                g.prependTo(r)
                            }
                            else
                            {
                                g.appendTo(r);
                                e.appendTo(r)
                            }
                            f = true
                        }
                        v++
                    }
                }
            }
        }
        return this._elem
    };

    function a(j, h, f)
    {
        f = f || {};
        f.axesDefaults = f.axesDefaults || {};
        f.legend = f.legend || {};
        f.seriesDefaults = f.seriesDefaults || {};
        f.grid = f.grid || {};
        var e = false;
        if (f.seriesDefaults.renderer == c.jqplot.MeterGaugeRenderer)
        {
            e = true
        }
        else
        {
            if (f.series)
            {
                for (var g = 0; g < f.series.length; g++)
                {
                    if (f.series[g].renderer == c.jqplot.MeterGaugeRenderer)
                    {
                        e = true
                    }
                }
            }
        }
        if (e)
        {
            f.axesDefaults.renderer = c.jqplot.MeterGaugeAxisRenderer;
            f.legend.renderer = c.jqplot.MeterGaugeLegendRenderer;
            f.legend.preDraw = true;
            f.grid.background = f.grid.background || "white";
            f.grid.drawGridlines = false;
            f.grid.borderWidth = (f.grid.borderWidth != null) ? f.grid.borderWidth
                : 0;
            f.grid.shadow = (f.grid.shadow != null) ? f.grid.shadow : false
        }
    }

    function d(e)
    {
    }

    c.jqplot.preInitHooks.push(a);
    c.jqplot.postParseOptionsHooks.push(d);
    c.jqplot.MeterGaugeTickRenderer = function () {
        c.jqplot.AxisTickRenderer.call(this)
    };
    c.jqplot.MeterGaugeTickRenderer.prototype = new c.jqplot.AxisTickRenderer();
    c.jqplot.MeterGaugeTickRenderer.prototype.constructor = c.jqplot.MeterGaugeTickRenderer
})(jQuery);
(function (a) {
    a.jqplot.CanvasTextRenderer = function (b) {
        this.fontStyle = "normal";
        this.fontVariant = "normal";
        this.fontWeight = "normal";
        this.fontSize = "10px";
        this.fontFamily = "sans-serif";
        this.fontStretch = 1;
        this.fillStyle = "#666666";
        this.angle = 0;
        this.textAlign = "start";
        this.textBaseline = "alphabetic";
        this.text;
        this.width;
        this.height;
        this.pt2px = 1.28;
        a.extend(true, this, b);
        this.normalizedFontSize = this.normalizeFontSize(this.fontSize);
        this.setHeight()
    };
    a.jqplot.CanvasTextRenderer.prototype.init = function (b) {
        a.extend(true, this, b);
        this.normalizedFontSize = this.normalizeFontSize(this.fontSize);
        this.setHeight()
    };
    a.jqplot.CanvasTextRenderer.prototype.normalizeFontSize = function (b) {
        b = String(b);
        var c = parseFloat(b);
        if (b.indexOf("px") > -1)
        {
            return c / this.pt2px
        }
        else
        {
            if (b.indexOf("pt") > -1)
            {
                return c
            }
            else
            {
                if (b.indexOf("em") > -1)
                {
                    return c * 12
                }
                else
                {
                    if (b.indexOf("%") > -1)
                    {
                        return c * 12 / 100
                    }
                    else
                    {
                        return c / this.pt2px
                    }
                }
            }
        }
    };
    a.jqplot.CanvasTextRenderer.prototype.fontWeight2Float = function (b) {
        if (Number(b))
        {
            return b / 400
        }
        else
        {
            switch (b)
            {
                case "normal":
                    return 1;
                    break;
                case "bold":
                    return 1.75;
                    break;
                case "bolder":
                    return 2.25;
                    break;
                case "lighter":
                    return 0.75;
                    break;
                default:
                    return 1;
                    break
            }
        }
    };
    a.jqplot.CanvasTextRenderer.prototype.getText = function () {
        return this.text
    };
    a.jqplot.CanvasTextRenderer.prototype.setText = function (c, b) {
        this.text = c;
        this.setWidth(b);
        return this
    };
    a.jqplot.CanvasTextRenderer.prototype.getWidth = function (b) {
        return this.width
    };
    a.jqplot.CanvasTextRenderer.prototype.setWidth = function (c, b) {
        if (!b)
        {
            this.width = this.measure(c, this.text)
        }
        else
        {
            this.width = b
        }
        return this
    };
    a.jqplot.CanvasTextRenderer.prototype.getHeight = function (b) {
        return this.height
    };
    a.jqplot.CanvasTextRenderer.prototype.setHeight = function (b) {
        if (!b)
        {
            this.height = this.normalizedFontSize * this.pt2px
        }
        else
        {
            this.height = b
        }
        return this
    };
    a.jqplot.CanvasTextRenderer.prototype.letter = function (b) {
        return this.letters[b]
    };
    a.jqplot.CanvasTextRenderer.prototype.ascent = function () {
        return this.normalizedFontSize
    };
    a.jqplot.CanvasTextRenderer.prototype.descent = function () {
        return 7 * this.normalizedFontSize / 25
    };
    a.jqplot.CanvasTextRenderer.prototype.measure = function (d, g) {
        var f = 0;
        var b = g.length;
        for (var e = 0; e < b; e++)
        {
            var h = this.letter(g.charAt(e));
            if (h)
            {
                f += h.width * this.normalizedFontSize / 25 * this.fontStretch
            }
        }
        return f
    };
    a.jqplot.CanvasTextRenderer.prototype.draw = function (s, n) {
        var r = 0;
        var o = this.height * 0.72;
        var p = 0;
        var l = n.length;
        var k = this.normalizedFontSize / 25;
        s.save();
        var h, f;
        if ((-Math.PI / 2 <= this.angle && this.angle <= 0)
            || (Math.PI * 3 / 2 <= this.angle && this.angle <= Math.PI * 2))
        {
            h = 0;
            f = -Math.sin(this.angle) * this.width
        }
        else
        {
            if ((0 < this.angle && this.angle <= Math.PI / 2)
                || (-Math.PI * 2 <= this.angle && this.angle <= -Math.PI * 3 / 2))
            {
                h = Math.sin(this.angle) * this.height;
                f = 0
            }
            else
            {
                if ((-Math.PI < this.angle && this.angle < -Math.PI / 2)
                    || (Math.PI <= this.angle && this.angle <= Math.PI * 3 / 2))
                {
                    h = -Math.cos(this.angle) * this.width;
                    f = -Math.sin(this.angle) * this.width
                        - Math.cos(this.angle) * this.height
                }
                else
                {
                    if ((-Math.PI * 3 / 2 < this.angle && this.angle < Math.PI)
                        || (Math.PI / 2 < this.angle && this.angle < Math.PI))
                    {
                        h = Math.sin(this.angle) * this.height
                            - Math.cos(this.angle) * this.width;
                        f = -Math.cos(this.angle) * this.height
                    }
                }
            }
        }
        s.strokeStyle = this.fillStyle;
        s.fillStyle = this.fillStyle;
        s.translate(h, f);
        s.rotate(this.angle);
        s.lineCap = "round";
        var t = (this.normalizedFontSize > 30) ? 2
            : 2 + (30 - this.normalizedFontSize) / 20;
        s.lineWidth = t * k * this.fontWeight2Float(this.fontWeight);
        for (var g = 0; g < l; g++)
        {
            var m = this.letter(n.charAt(g));
            if (!m)
            {
                continue
            }
            s.beginPath();
            var e = 1;
            var b = 0;
            for (var d = 0; d < m.points.length; d++)
            {
                var q = m.points[d];
                if (q[0] == -1 && q[1] == -1)
                {
                    e = 1;
                    continue
                }
                if (e)
                {
                    s.moveTo(r + q[0] * k * this.fontStretch, o - q[1] * k);
                    e = false
                }
                else
                {
                    s.lineTo(r + q[0] * k * this.fontStretch, o - q[1] * k)
                }
            }
            s.stroke();
            r += m.width * k * this.fontStretch
        }
        s.restore();
        return p
    };
    a.jqplot.CanvasTextRenderer.prototype.letters = {
        " ": {
            width: 16,
            points: []
        },
        "!": {
            width: 10,
            points: [[5, 21], [5, 7], [-1, -1], [5, 2], [4, 1],
                [5, 0], [6, 1], [5, 2]]
        },
        '"': {
            width: 16,
            points: [[4, 21], [4, 14], [-1, -1], [12, 21], [12, 14]]
        },
        "#": {
            width: 21,
            points: [[11, 25], [4, -7], [-1, -1], [17, 25],
                [10, -7], [-1, -1], [4, 12], [18, 12], [-1, -1],
                [3, 6], [17, 6]]
        },
        "$": {
            width: 20,
            points: [[8, 25], [8, -4], [-1, -1], [12, 25],
                [12, -4], [-1, -1], [17, 18], [15, 20], [12, 21],
                [8, 21], [5, 20], [3, 18], [3, 16], [4, 14],
                [5, 13], [7, 12], [13, 10], [15, 9], [16, 8],
                [17, 6], [17, 3], [15, 1], [12, 0], [8, 0],
                [5, 1], [3, 3]]
        },
        "%": {
            width: 24,
            points: [[21, 21], [3, 0], [-1, -1], [8, 21], [10, 19],
                [10, 17], [9, 15], [7, 14], [5, 14], [3, 16],
                [3, 18], [4, 20], [6, 21], [8, 21], [10, 20],
                [13, 19], [16, 19], [19, 20], [21, 21], [-1, -1],
                [17, 7], [15, 6], [14, 4], [14, 2], [16, 0],
                [18, 0], [20, 1], [21, 3], [21, 5], [19, 7],
                [17, 7]]
        },
        "&": {
            width: 26,
            points: [[23, 12], [23, 13], [22, 14], [21, 14],
                [20, 13], [19, 11], [17, 6], [15, 3], [13, 1],
                [11, 0], [7, 0], [5, 1], [4, 2], [3, 4],
                [3, 6], [4, 8], [5, 9], [12, 13], [13, 14],
                [14, 16], [14, 18], [13, 20], [11, 21], [9, 20],
                [8, 18], [8, 16], [9, 13], [11, 10], [16, 3],
                [18, 1], [20, 0], [22, 0], [23, 1], [23, 2]]
        },
        "'": {
            width: 10,
            points: [[5, 19], [4, 20], [5, 21], [6, 20], [6, 18],
                [5, 16], [4, 15]]
        },
        "(": {
            width: 14,
            points: [[11, 25], [9, 23], [7, 20], [5, 16], [4, 11],
                [4, 7], [5, 2], [7, -2], [9, -5], [11, -7]]
        },
        ")": {
            width: 14,
            points: [[3, 25], [5, 23], [7, 20], [9, 16], [10, 11],
                [10, 7], [9, 2], [7, -2], [5, -5], [3, -7]]
        },
        "*": {
            width: 16,
            points: [[8, 21], [8, 9], [-1, -1], [3, 18], [13, 12],
                [-1, -1], [13, 18], [3, 12]]
        },
        "+": {
            width: 26,
            points: [[13, 18], [13, 0], [-1, -1], [4, 9], [22, 9]]
        },
        ",": {
            width: 10,
            points: [[6, 1], [5, 0], [4, 1], [5, 2], [6, 1],
                [6, -1], [5, -3], [4, -4]]
        },
        "-": {
            width: 18,
            points: [[6, 9], [12, 9]]
        },
        ".": {
            width: 10,
            points: [[5, 2], [4, 1], [5, 0], [6, 1], [5, 2]]
        },
        "/": {
            width: 22,
            points: [[20, 25], [2, -7]]
        },
        "0": {
            width: 20,
            points: [[9, 21], [6, 20], [4, 17], [3, 12], [3, 9],
                [4, 4], [6, 1], [9, 0], [11, 0], [14, 1],
                [16, 4], [17, 9], [17, 12], [16, 17], [14, 20],
                [11, 21], [9, 21]]
        },
        "1": {
            width: 20,
            points: [[6, 17], [8, 18], [11, 21], [11, 0]]
        },
        "2": {
            width: 20,
            points: [[4, 16], [4, 17], [5, 19], [6, 20], [8, 21],
                [12, 21], [14, 20], [15, 19], [16, 17], [16, 15],
                [15, 13], [13, 10], [3, 0], [17, 0]]
        },
        "3": {
            width: 20,
            points: [[5, 21], [16, 21], [10, 13], [13, 13],
                [15, 12], [16, 11], [17, 8], [17, 6], [16, 3],
                [14, 1], [11, 0], [8, 0], [5, 1], [4, 2],
                [3, 4]]
        },
        "4": {
            width: 20,
            points: [[13, 21], [3, 7], [18, 7], [-1, -1], [13, 21],
                [13, 0]]
        },
        "5": {
            width: 20,
            points: [[15, 21], [5, 21], [4, 12], [5, 13], [8, 14],
                [11, 14], [14, 13], [16, 11], [17, 8], [17, 6],
                [16, 3], [14, 1], [11, 0], [8, 0], [5, 1],
                [4, 2], [3, 4]]
        },
        "6": {
            width: 20,
            points: [[16, 18], [15, 20], [12, 21], [10, 21],
                [7, 20], [5, 17], [4, 12], [4, 7], [5, 3],
                [7, 1], [10, 0], [11, 0], [14, 1], [16, 3],
                [17, 6], [17, 7], [16, 10], [14, 12], [11, 13],
                [10, 13], [7, 12], [5, 10], [4, 7]]
        },
        "7": {
            width: 20,
            points: [[17, 21], [7, 0], [-1, -1], [3, 21], [17, 21]]
        },
        "8": {
            width: 20,
            points: [[8, 21], [5, 20], [4, 18], [4, 16], [5, 14],
                [7, 13], [11, 12], [14, 11], [16, 9], [17, 7],
                [17, 4], [16, 2], [15, 1], [12, 0], [8, 0],
                [5, 1], [4, 2], [3, 4], [3, 7], [4, 9],
                [6, 11], [9, 12], [13, 13], [15, 14], [16, 16],
                [16, 18], [15, 20], [12, 21], [8, 21]]
        },
        "9": {
            width: 20,
            points: [[16, 14], [15, 11], [13, 9], [10, 8], [9, 8],
                [6, 9], [4, 11], [3, 14], [3, 15], [4, 18],
                [6, 20], [9, 21], [10, 21], [13, 20], [15, 18],
                [16, 14], [16, 9], [15, 4], [13, 1], [10, 0],
                [8, 0], [5, 1], [4, 3]]
        },
        ":": {
            width: 10,
            points: [[5, 14], [4, 13], [5, 12], [6, 13], [5, 14],
                [-1, -1], [5, 2], [4, 1], [5, 0], [6, 1],
                [5, 2]]
        },
        ";": {
            width: 10,
            points: [[5, 14], [4, 13], [5, 12], [6, 13], [5, 14],
                [-1, -1], [6, 1], [5, 0], [4, 1], [5, 2],
                [6, 1], [6, -1], [5, -3], [4, -4]]
        },
        "<": {
            width: 24,
            points: [[20, 18], [4, 9], [20, 0]]
        },
        "=": {
            width: 26,
            points: [[4, 12], [22, 12], [-1, -1], [4, 6], [22, 6]]
        },
        ">": {
            width: 24,
            points: [[4, 18], [20, 9], [4, 0]]
        },
        "?": {
            width: 18,
            points: [[3, 16], [3, 17], [4, 19], [5, 20], [7, 21],
                [11, 21], [13, 20], [14, 19], [15, 17], [15, 15],
                [14, 13], [13, 12], [9, 10], [9, 7], [-1, -1],
                [9, 2], [8, 1], [9, 0], [10, 1], [9, 2]]
        },
        "@": {
            width: 27,
            points: [[18, 13], [17, 15], [15, 16], [12, 16],
                [10, 15], [9, 14], [8, 11], [8, 8], [9, 6],
                [11, 5], [14, 5], [16, 6], [17, 8], [-1, -1],
                [12, 16], [10, 14], [9, 11], [9, 8], [10, 6],
                [11, 5], [-1, -1], [18, 16], [17, 8], [17, 6],
                [19, 5], [21, 5], [23, 7], [24, 10], [24, 12],
                [23, 15], [22, 17], [20, 19], [18, 20], [15, 21],
                [12, 21], [9, 20], [7, 19], [5, 17], [4, 15],
                [3, 12], [3, 9], [4, 6], [5, 4], [7, 2],
                [9, 1], [12, 0], [15, 0], [18, 1], [20, 2],
                [21, 3], [-1, -1], [19, 16], [18, 8], [18, 6],
                [19, 5]]
        },
        A: {
            width: 18,
            points: [[9, 21], [1, 0], [-1, -1], [9, 21], [17, 0],
                [-1, -1], [4, 7], [14, 7]]
        },
        B: {
            width: 21,
            points: [[4, 21], [4, 0], [-1, -1], [4, 21], [13, 21],
                [16, 20], [17, 19], [18, 17], [18, 15], [17, 13],
                [16, 12], [13, 11], [-1, -1], [4, 11], [13, 11],
                [16, 10], [17, 9], [18, 7], [18, 4], [17, 2],
                [16, 1], [13, 0], [4, 0]]
        },
        C: {
            width: 21,
            points: [[18, 16], [17, 18], [15, 20], [13, 21],
                [9, 21], [7, 20], [5, 18], [4, 16], [3, 13],
                [3, 8], [4, 5], [5, 3], [7, 1], [9, 0],
                [13, 0], [15, 1], [17, 3], [18, 5]]
        },
        D: {
            width: 21,
            points: [[4, 21], [4, 0], [-1, -1], [4, 21], [11, 21],
                [14, 20], [16, 18], [17, 16], [18, 13], [18, 8],
                [17, 5], [16, 3], [14, 1], [11, 0], [4, 0]]
        },
        E: {
            width: 19,
            points: [[4, 21], [4, 0], [-1, -1], [4, 21], [17, 21],
                [-1, -1], [4, 11], [12, 11], [-1, -1], [4, 0],
                [17, 0]]
        },
        F: {
            width: 18,
            points: [[4, 21], [4, 0], [-1, -1], [4, 21], [17, 21],
                [-1, -1], [4, 11], [12, 11]]
        },
        G: {
            width: 21,
            points: [[18, 16], [17, 18], [15, 20], [13, 21],
                [9, 21], [7, 20], [5, 18], [4, 16], [3, 13],
                [3, 8], [4, 5], [5, 3], [7, 1], [9, 0],
                [13, 0], [15, 1], [17, 3], [18, 5], [18, 8],
                [-1, -1], [13, 8], [18, 8]]
        },
        H: {
            width: 22,
            points: [[4, 21], [4, 0], [-1, -1], [18, 21], [18, 0],
                [-1, -1], [4, 11], [18, 11]]
        },
        I: {
            width: 8,
            points: [[4, 21], [4, 0]]
        },
        J: {
            width: 16,
            points: [[12, 21], [12, 5], [11, 2], [10, 1], [8, 0],
                [6, 0], [4, 1], [3, 2], [2, 5], [2, 7]]
        },
        K: {
            width: 21,
            points: [[4, 21], [4, 0], [-1, -1], [18, 21], [4, 7],
                [-1, -1], [9, 12], [18, 0]]
        },
        L: {
            width: 17,
            points: [[4, 21], [4, 0], [-1, -1], [4, 0], [16, 0]]
        },
        M: {
            width: 24,
            points: [[4, 21], [4, 0], [-1, -1], [4, 21], [12, 0],
                [-1, -1], [20, 21], [12, 0], [-1, -1], [20, 21],
                [20, 0]]
        },
        N: {
            width: 22,
            points: [[4, 21], [4, 0], [-1, -1], [4, 21], [18, 0],
                [-1, -1], [18, 21], [18, 0]]
        },
        O: {
            width: 22,
            points: [[9, 21], [7, 20], [5, 18], [4, 16], [3, 13],
                [3, 8], [4, 5], [5, 3], [7, 1], [9, 0],
                [13, 0], [15, 1], [17, 3], [18, 5], [19, 8],
                [19, 13], [18, 16], [17, 18], [15, 20], [13, 21],
                [9, 21]]
        },
        P: {
            width: 21,
            points: [[4, 21], [4, 0], [-1, -1], [4, 21], [13, 21],
                [16, 20], [17, 19], [18, 17], [18, 14], [17, 12],
                [16, 11], [13, 10], [4, 10]]
        },
        Q: {
            width: 22,
            points: [[9, 21], [7, 20], [5, 18], [4, 16], [3, 13],
                [3, 8], [4, 5], [5, 3], [7, 1], [9, 0],
                [13, 0], [15, 1], [17, 3], [18, 5], [19, 8],
                [19, 13], [18, 16], [17, 18], [15, 20], [13, 21],
                [9, 21], [-1, -1], [12, 4], [18, -2]]
        },
        R: {
            width: 21,
            points: [[4, 21], [4, 0], [-1, -1], [4, 21], [13, 21],
                [16, 20], [17, 19], [18, 17], [18, 15], [17, 13],
                [16, 12], [13, 11], [4, 11], [-1, -1], [11, 11],
                [18, 0]]
        },
        S: {
            width: 20,
            points: [[17, 18], [15, 20], [12, 21], [8, 21],
                [5, 20], [3, 18], [3, 16], [4, 14], [5, 13],
                [7, 12], [13, 10], [15, 9], [16, 8], [17, 6],
                [17, 3], [15, 1], [12, 0], [8, 0], [5, 1],
                [3, 3]]
        },
        T: {
            width: 16,
            points: [[8, 21], [8, 0], [-1, -1], [1, 21], [15, 21]]
        },
        U: {
            width: 22,
            points: [[4, 21], [4, 6], [5, 3], [7, 1], [10, 0],
                [12, 0], [15, 1], [17, 3], [18, 6], [18, 21]]
        },
        V: {
            width: 18,
            points: [[1, 21], [9, 0], [-1, -1], [17, 21], [9, 0]]
        },
        W: {
            width: 24,
            points: [[2, 21], [7, 0], [-1, -1], [12, 21], [7, 0],
                [-1, -1], [12, 21], [17, 0], [-1, -1], [22, 21],
                [17, 0]]
        },
        X: {
            width: 20,
            points: [[3, 21], [17, 0], [-1, -1], [17, 21], [3, 0]]
        },
        Y: {
            width: 18,
            points: [[1, 21], [9, 11], [9, 0], [-1, -1], [17, 21],
                [9, 11]]
        },
        Z: {
            width: 20,
            points: [[17, 21], [3, 0], [-1, -1], [3, 21], [17, 21],
                [-1, -1], [3, 0], [17, 0]]
        },
        "[": {
            width: 14,
            points: [[4, 25], [4, -7], [-1, -1], [5, 25], [5, -7],
                [-1, -1], [4, 25], [11, 25], [-1, -1], [4, -7],
                [11, -7]]
        },
        "\\": {
            width: 14,
            points: [[0, 21], [14, -3]]
        },
        "]": {
            width: 14,
            points: [[9, 25], [9, -7], [-1, -1], [10, 25],
                [10, -7], [-1, -1], [3, 25], [10, 25], [-1, -1],
                [3, -7], [10, -7]]
        },
        "^": {
            width: 16,
            points: [[6, 15], [8, 18], [10, 15], [-1, -1], [3, 12],
                [8, 17], [13, 12], [-1, -1], [8, 17], [8, 0]]
        },
        _: {
            width: 16,
            points: [[0, -2], [16, -2]]
        },
        "`": {
            width: 10,
            points: [[6, 21], [5, 20], [4, 18], [4, 16], [5, 15],
                [6, 16], [5, 17]]
        },
        a: {
            width: 19,
            points: [[15, 14], [15, 0], [-1, -1], [15, 11],
                [13, 13], [11, 14], [8, 14], [6, 13], [4, 11],
                [3, 8], [3, 6], [4, 3], [6, 1], [8, 0],
                [11, 0], [13, 1], [15, 3]]
        },
        b: {
            width: 19,
            points: [[4, 21], [4, 0], [-1, -1], [4, 11], [6, 13],
                [8, 14], [11, 14], [13, 13], [15, 11], [16, 8],
                [16, 6], [15, 3], [13, 1], [11, 0], [8, 0],
                [6, 1], [4, 3]]
        },
        c: {
            width: 18,
            points: [[15, 11], [13, 13], [11, 14], [8, 14],
                [6, 13], [4, 11], [3, 8], [3, 6], [4, 3],
                [6, 1], [8, 0], [11, 0], [13, 1], [15, 3]]
        },
        d: {
            width: 19,
            points: [[15, 21], [15, 0], [-1, -1], [15, 11],
                [13, 13], [11, 14], [8, 14], [6, 13], [4, 11],
                [3, 8], [3, 6], [4, 3], [6, 1], [8, 0],
                [11, 0], [13, 1], [15, 3]]
        },
        e: {
            width: 18,
            points: [[3, 8], [15, 8], [15, 10], [14, 12], [13, 13],
                [11, 14], [8, 14], [6, 13], [4, 11], [3, 8],
                [3, 6], [4, 3], [6, 1], [8, 0], [11, 0],
                [13, 1], [15, 3]]
        },
        f: {
            width: 12,
            points: [[10, 21], [8, 21], [6, 20], [5, 17], [5, 0],
                [-1, -1], [2, 14], [9, 14]]
        },
        g: {
            width: 19,
            points: [[15, 14], [15, -2], [14, -5], [13, -6],
                [11, -7], [8, -7], [6, -6], [-1, -1], [15, 11],
                [13, 13], [11, 14], [8, 14], [6, 13], [4, 11],
                [3, 8], [3, 6], [4, 3], [6, 1], [8, 0],
                [11, 0], [13, 1], [15, 3]]
        },
        h: {
            width: 19,
            points: [[4, 21], [4, 0], [-1, -1], [4, 10], [7, 13],
                [9, 14], [12, 14], [14, 13], [15, 10], [15, 0]]
        },
        i: {
            width: 8,
            points: [[3, 21], [4, 20], [5, 21], [4, 22], [3, 21],
                [-1, -1], [4, 14], [4, 0]]
        },
        j: {
            width: 10,
            points: [[5, 21], [6, 20], [7, 21], [6, 22], [5, 21],
                [-1, -1], [6, 14], [6, -3], [5, -6], [3, -7],
                [1, -7]]
        },
        k: {
            width: 17,
            points: [[4, 21], [4, 0], [-1, -1], [14, 14], [4, 4],
                [-1, -1], [8, 8], [15, 0]]
        },
        l: {
            width: 8,
            points: [[4, 21], [4, 0]]
        },
        m: {
            width: 30,
            points: [[4, 14], [4, 0], [-1, -1], [4, 10], [7, 13],
                [9, 14], [12, 14], [14, 13], [15, 10], [15, 0],
                [-1, -1], [15, 10], [18, 13], [20, 14], [23, 14],
                [25, 13], [26, 10], [26, 0]]
        },
        n: {
            width: 19,
            points: [[4, 14], [4, 0], [-1, -1], [4, 10], [7, 13],
                [9, 14], [12, 14], [14, 13], [15, 10], [15, 0]]
        },
        o: {
            width: 19,
            points: [[8, 14], [6, 13], [4, 11], [3, 8], [3, 6],
                [4, 3], [6, 1], [8, 0], [11, 0], [13, 1],
                [15, 3], [16, 6], [16, 8], [15, 11], [13, 13],
                [11, 14], [8, 14]]
        },
        p: {
            width: 19,
            points: [[4, 14], [4, -7], [-1, -1], [4, 11], [6, 13],
                [8, 14], [11, 14], [13, 13], [15, 11], [16, 8],
                [16, 6], [15, 3], [13, 1], [11, 0], [8, 0],
                [6, 1], [4, 3]]
        },
        q: {
            width: 19,
            points: [[15, 14], [15, -7], [-1, -1], [15, 11],
                [13, 13], [11, 14], [8, 14], [6, 13], [4, 11],
                [3, 8], [3, 6], [4, 3], [6, 1], [8, 0],
                [11, 0], [13, 1], [15, 3]]
        },
        r: {
            width: 13,
            points: [[4, 14], [4, 0], [-1, -1], [4, 8], [5, 11],
                [7, 13], [9, 14], [12, 14]]
        },
        s: {
            width: 17,
            points: [[14, 11], [13, 13], [10, 14], [7, 14],
                [4, 13], [3, 11], [4, 9], [6, 8], [11, 7],
                [13, 6], [14, 4], [14, 3], [13, 1], [10, 0],
                [7, 0], [4, 1], [3, 3]]
        },
        t: {
            width: 12,
            points: [[5, 21], [5, 4], [6, 1], [8, 0], [10, 0],
                [-1, -1], [2, 14], [9, 14]]
        },
        u: {
            width: 19,
            points: [[4, 14], [4, 4], [5, 1], [7, 0], [10, 0],
                [12, 1], [15, 4], [-1, -1], [15, 14], [15, 0]]
        },
        v: {
            width: 16,
            points: [[2, 14], [8, 0], [-1, -1], [14, 14], [8, 0]]
        },
        w: {
            width: 22,
            points: [[3, 14], [7, 0], [-1, -1], [11, 14], [7, 0],
                [-1, -1], [11, 14], [15, 0], [-1, -1], [19, 14],
                [15, 0]]
        },
        x: {
            width: 17,
            points: [[3, 14], [14, 0], [-1, -1], [14, 14], [3, 0]]
        },
        y: {
            width: 16,
            points: [[2, 14], [8, 0], [-1, -1], [14, 14], [8, 0],
                [6, -4], [4, -6], [2, -7], [1, -7]]
        },
        z: {
            width: 17,
            points: [[14, 14], [3, 0], [-1, -1], [3, 14], [14, 14],
                [-1, -1], [3, 0], [14, 0]]
        },
        "{": {
            width: 14,
            points: [[9, 25], [7, 24], [6, 23], [5, 21], [5, 19],
                [6, 17], [7, 16], [8, 14], [8, 12], [6, 10],
                [-1, -1], [7, 24], [6, 22], [6, 20], [7, 18],
                [8, 17], [9, 15], [9, 13], [8, 11], [4, 9],
                [8, 7], [9, 5], [9, 3], [8, 1], [7, 0],
                [6, -2], [6, -4], [7, -6], [-1, -1], [6, 8],
                [8, 6], [8, 4], [7, 2], [6, 1], [5, -1],
                [5, -3], [6, -5], [7, -6], [9, -7]]
        },
        "|": {
            width: 8,
            points: [[4, 25], [4, -7]]
        },
        "}": {
            width: 14,
            points: [[5, 25], [7, 24], [8, 23], [9, 21], [9, 19],
                [8, 17], [7, 16], [6, 14], [6, 12], [8, 10],
                [-1, -1], [7, 24], [8, 22], [8, 20], [7, 18],
                [6, 17], [5, 15], [5, 13], [6, 11], [10, 9],
                [6, 7], [5, 5], [5, 3], [6, 1], [7, 0],
                [8, -2], [8, -4], [7, -6], [-1, -1], [8, 8],
                [6, 6], [6, 4], [7, 2], [8, 1], [9, -1],
                [9, -3], [8, -5], [7, -6], [5, -7]]
        },
        "~": {
            width: 24,
            points: [[3, 6], [3, 8], [4, 11], [6, 12], [8, 12],
                [10, 11], [14, 8], [16, 7], [18, 7], [20, 8],
                [21, 10], [-1, -1], [3, 8], [4, 10], [6, 11],
                [8, 11], [10, 10], [14, 7], [16, 6], [18, 6],
                [20, 7], [21, 10], [21, 12]]
        }
    };
    a.jqplot.CanvasFontRenderer = function (b) {
        b = b || {};
        if (!b.pt2px)
        {
            b.pt2px = 1.5
        }
        a.jqplot.CanvasTextRenderer.call(this, b)
    };
    a.jqplot.CanvasFontRenderer.prototype = new a.jqplot.CanvasTextRenderer({});
    a.jqplot.CanvasFontRenderer.prototype.constructor = a.jqplot.CanvasFontRenderer;
    a.jqplot.CanvasFontRenderer.prototype.measure = function (c, e) {
        var d = this.fontSize + " " + this.fontFamily;
        c.save();
        c.font = d;
        var b = c.measureText(e).width;
        c.restore();
        return b
    };
    a.jqplot.CanvasFontRenderer.prototype.draw = function (e, g) {
        var c = 0;
        var h = this.height * 0.72;
        e.save();
        var d, b;
        if ((-Math.PI / 2 <= this.angle && this.angle <= 0)
            || (Math.PI * 3 / 2 <= this.angle && this.angle <= Math.PI * 2))
        {
            d = 0;
            b = -Math.sin(this.angle) * this.width
        }
        else
        {
            if ((0 < this.angle && this.angle <= Math.PI / 2)
                || (-Math.PI * 2 <= this.angle && this.angle <= -Math.PI * 3 / 2))
            {
                d = Math.sin(this.angle) * this.height;
                b = 0
            }
            else
            {
                if ((-Math.PI < this.angle && this.angle < -Math.PI / 2)
                    || (Math.PI <= this.angle && this.angle <= Math.PI * 3 / 2))
                {
                    d = -Math.cos(this.angle) * this.width;
                    b = -Math.sin(this.angle) * this.width
                        - Math.cos(this.angle) * this.height
                }
                else
                {
                    if ((-Math.PI * 3 / 2 < this.angle && this.angle < Math.PI)
                        || (Math.PI / 2 < this.angle && this.angle < Math.PI))
                    {
                        d = Math.sin(this.angle) * this.height
                            - Math.cos(this.angle) * this.width;
                        b = -Math.cos(this.angle) * this.height
                    }
                }
            }
        }
        e.strokeStyle = this.fillStyle;
        e.fillStyle = this.fillStyle;
        var f = this.fontSize + " " + this.fontFamily;
        e.font = f;
        e.translate(d, b);
        e.rotate(this.angle);
        e.fillText(g, c, h);
        e.restore()
    }
})(jQuery);
(function (a) {
    a.jqplot.CanvasAxisTickRenderer = function (b) {
        this.mark = "outside";
        this.showMark = true;
        this.showGridline = true;
        this.isMinorTick = false;
        this.angle = 0;
        this.markSize = 4;
        this.show = true;
        this.showLabel = true;
        this.labelPosition = "auto";
        this.label = "";
        this.value = null;
        this._styles = {};
        this.formatter = a.jqplot.DefaultTickFormatter;
        this.formatString = "";
        this.prefix = "";
        this.fontFamily = '"Trebuchet MS", Arial, Helvetica, sans-serif';
        this.fontSize = "10pt";
        this.fontWeight = "normal";
        this.fontStretch = 1;
        this.textColor = "#666666";
        this.enableFontSupport = true;
        this.pt2px = null;
        this._elem;
        this._ctx;
        this._plotWidth;
        this._plotHeight;
        this._plotDimensions = {
            height: null,
            width: null
        };
        a.extend(true, this, b);
        var c = {
            fontSize: this.fontSize,
            fontWeight: this.fontWeight,
            fontStretch: this.fontStretch,
            fillStyle: this.textColor,
            angle: this.getAngleRad(),
            fontFamily: this.fontFamily
        };
        if (this.pt2px)
        {
            c.pt2px = this.pt2px
        }
        if (this.enableFontSupport)
        {
            if (a.jqplot.support_canvas_text())
            {
                this._textRenderer = new a.jqplot.CanvasFontRenderer(c)
            }
            else
            {
                this._textRenderer = new a.jqplot.CanvasTextRenderer(c)
            }
        }
        else
        {
            this._textRenderer = new a.jqplot.CanvasTextRenderer(c)
        }
    };
    a.jqplot.CanvasAxisTickRenderer.prototype.init = function (b) {
        a.extend(true, this, b);
        this._textRenderer.init({
            fontSize: this.fontSize,
            fontWeight: this.fontWeight,
            fontStretch: this.fontStretch,
            fillStyle: this.textColor,
            angle: this.getAngleRad(),
            fontFamily: this.fontFamily
        })
    };
    a.jqplot.CanvasAxisTickRenderer.prototype.getWidth = function (d) {
        if (this._elem)
        {
            return this._elem.outerWidth(true)
        }
        else
        {
            var f = this._textRenderer;
            var c = f.getWidth(d);
            var e = f.getHeight(d);
            var b = Math.abs(Math.sin(f.angle) * e)
                + Math.abs(Math.cos(f.angle) * c);
            return b
        }
    };
    a.jqplot.CanvasAxisTickRenderer.prototype.getHeight = function (d) {
        if (this._elem)
        {
            return this._elem.outerHeight(true)
        }
        else
        {
            var f = this._textRenderer;
            var c = f.getWidth(d);
            var e = f.getHeight(d);
            var b = Math.abs(Math.cos(f.angle) * e)
                + Math.abs(Math.sin(f.angle) * c);
            return b
        }
    };
    a.jqplot.CanvasAxisTickRenderer.prototype.getTop = function (b) {
        if (this._elem)
        {
            return this._elem.position().top
        }
        else
        {
            return null
        }
    };
    a.jqplot.CanvasAxisTickRenderer.prototype.getAngleRad = function () {
        var b = this.angle * Math.PI / 180;
        return b
    };
    a.jqplot.CanvasAxisTickRenderer.prototype.setTick = function (b, d, c) {
        this.value = b;
        if (c)
        {
            this.isMinorTick = true
        }
        return this
    };
    a.jqplot.CanvasAxisTickRenderer.prototype.draw = function (c, f) {
        if (!this.label)
        {
            this.label = this.prefix
                + this.formatter(this.formatString, this.value)
        }
        if (this._elem)
        {
            if (a.jqplot.use_excanvas
                && window.G_vmlCanvasManager.uninitElement !== undefined)
            {
                window.G_vmlCanvasManager.uninitElement(this._elem.get(0))
            }
            this._elem.emptyForce();
            this._elem = null
        }
        var e = f.canvasManager.getCanvas();
        this._textRenderer.setText(this.label, c);
        var b = this.getWidth(c);
        var d = this.getHeight(c);
        e.width = b;
        e.height = d;
        e.style.width = b;
        e.style.height = d;
        e.style.textAlign = "left";
        e.style.position = "absolute";
        e = f.canvasManager.initCanvas(e);
        this._elem = a(e);
        this._elem.css(this._styles);
        this._elem.addClass("jqplot-" + this.axis + "-tick");
        e = null;
        return this._elem
    };
    a.jqplot.CanvasAxisTickRenderer.prototype.pack = function () {
        this._textRenderer.draw(this._elem.get(0).getContext("2d"), this.label)
    }
})(jQuery);
(function (a) {
    a.jqplot.CanvasAxisLabelRenderer = function (b) {
        this.angle = 0;
        this.axis;
        this.show = true;
        this.showLabel = true;
        this.label = "";
        this.fontFamily = '"Trebuchet MS", Arial, Helvetica, sans-serif';
        this.fontSize = "11pt";
        this.fontWeight = "normal";
        this.fontStretch = 1;
        this.textColor = "#666666";
        this.enableFontSupport = true;
        this.pt2px = null;
        this._elem;
        this._ctx;
        this._plotWidth;
        this._plotHeight;
        this._plotDimensions = {
            height: null,
            width: null
        };
        a.extend(true, this, b);
        if (b.angle == null && this.axis != "xaxis" && this.axis != "x2axis")
        {
            this.angle = -90
        }
        var c = {
            fontSize: this.fontSize,
            fontWeight: this.fontWeight,
            fontStretch: this.fontStretch,
            fillStyle: this.textColor,
            angle: this.getAngleRad(),
            fontFamily: this.fontFamily
        };
        if (this.pt2px)
        {
            c.pt2px = this.pt2px
        }
        if (this.enableFontSupport)
        {
            if (a.jqplot.support_canvas_text())
            {
                this._textRenderer = new a.jqplot.CanvasFontRenderer(c)
            }
            else
            {
                this._textRenderer = new a.jqplot.CanvasTextRenderer(c)
            }
        }
        else
        {
            this._textRenderer = new a.jqplot.CanvasTextRenderer(c)
        }
    };
    a.jqplot.CanvasAxisLabelRenderer.prototype.init = function (b) {
        a.extend(true, this, b);
        this._textRenderer.init({
            fontSize: this.fontSize,
            fontWeight: this.fontWeight,
            fontStretch: this.fontStretch,
            fillStyle: this.textColor,
            angle: this.getAngleRad(),
            fontFamily: this.fontFamily
        })
    };
    a.jqplot.CanvasAxisLabelRenderer.prototype.getWidth = function (d) {
        if (this._elem)
        {
            return this._elem.outerWidth(true)
        }
        else
        {
            var f = this._textRenderer;
            var c = f.getWidth(d);
            var e = f.getHeight(d);
            var b = Math.abs(Math.sin(f.angle) * e)
                + Math.abs(Math.cos(f.angle) * c);
            return b
        }
    };
    a.jqplot.CanvasAxisLabelRenderer.prototype.getHeight = function (d) {
        if (this._elem)
        {
            return this._elem.outerHeight(true)
        }
        else
        {
            var f = this._textRenderer;
            var c = f.getWidth(d);
            var e = f.getHeight(d);
            var b = Math.abs(Math.cos(f.angle) * e)
                + Math.abs(Math.sin(f.angle) * c);
            return b
        }
    };
    a.jqplot.CanvasAxisLabelRenderer.prototype.getAngleRad = function () {
        var b = this.angle * Math.PI / 180;
        return b
    };
    a.jqplot.CanvasAxisLabelRenderer.prototype.draw = function (c, f) {
        if (this._elem)
        {
            if (a.jqplot.use_excanvas
                && window.G_vmlCanvasManager.uninitElement !== undefined)
            {
                window.G_vmlCanvasManager.uninitElement(this._elem.get(0))
            }
            this._elem.emptyForce();
            this._elem = null
        }
        var e = f.canvasManager.getCanvas();
        this._textRenderer.setText(this.label, c);
        var b = this.getWidth(c);
        var d = this.getHeight(c);
        e.width = b;
        e.height = d;
        e.style.width = b;
        e.style.height = d;
        e = f.canvasManager.initCanvas(e);
        this._elem = a(e);
        this._elem.css({
            position: "absolute"
        });
        this._elem.addClass("jqplot-" + this.axis + "-label");
        e = null;
        return this._elem
    };
    a.jqplot.CanvasAxisLabelRenderer.prototype.pack = function () {
        this._textRenderer.draw(this._elem.get(0).getContext("2d"), this.label)
    }
})(jQuery);
(function (j) {
    j.jqplot.Cursor = function (q) {
        this.style = "crosshair";
        this.previousCursor = "auto";
        this.show = j.jqplot.config.enablePlugins;
        this.showTooltip = true;
        this.followMouse = false;
        this.tooltipLocation = "se";
        this.tooltipOffset = 6;
        this.showTooltipGridPosition = false;
        this.showTooltipUnitPosition = true;
        this.showTooltipDataPosition = false;
        this.tooltipFormatString = "%.4P, %.4P";
        this.useAxesFormatters = true;
        this.tooltipAxisGroups = [];
        this.zoom = false;
        this.zoomProxy = false;
        this.zoomTarget = false;
        this.looseZoom = true;
        this.clickReset = false;
        this.dblClickReset = true;
        this.showVerticalLine = false;
        this.showHorizontalLine = false;
        this.constrainZoomTo = "none";
        this.shapeRenderer = new j.jqplot.ShapeRenderer();
        this._zoom = {
            start: [],
            end: [],
            started: false,
            zooming: false,
            isZoomed: false,
            axes: {
                start: {},
                end: {}
            },
            gridpos: {},
            datapos: {}
        };
        this._tooltipElem;
        this.zoomCanvas;
        this.cursorCanvas;
        this.intersectionThreshold = 2;
        this.showCursorLegend = false;
        this.cursorLegendFormatString = j.jqplot.Cursor.cursorLegendFormatString;
        this._oldHandlers = {
            onselectstart: null,
            ondrag: null,
            onmousedown: null
        };
        this.constrainOutsideZoom = true;
        this.showTooltipOutsideZoom = false;
        this.onGrid = false;
        j.extend(true, this, q)
    };
    j.jqplot.Cursor.cursorLegendFormatString = "%s x:%s, y:%s";
    j.jqplot.Cursor.init = function (t, s, r) {
        var q = r || {};
        this.plugins.cursor = new j.jqplot.Cursor(q.cursor);
        var u = this.plugins.cursor;
        if (u.show)
        {
            j.jqplot.eventListenerHooks.push(["jqplotMouseEnter", b]);
            j.jqplot.eventListenerHooks.push(["jqplotMouseLeave", f]);
            j.jqplot.eventListenerHooks.push(["jqplotMouseMove", i]);
            if (u.showCursorLegend)
            {
                r.legend = r.legend || {};
                r.legend.renderer = j.jqplot.CursorLegendRenderer;
                r.legend.formatString = this.plugins.cursor.cursorLegendFormatString;
                r.legend.show = true
            }
            if (u.zoom)
            {
                j.jqplot.eventListenerHooks.push(["jqplotMouseDown", a]);
                if (u.clickReset)
                {
                    j.jqplot.eventListenerHooks.push(["jqplotClick", k])
                }
                if (u.dblClickReset)
                {
                    j.jqplot.eventListenerHooks.push(["jqplotDblClick", c])
                }
            }
            this.resetZoom = function () {
                var x = this.axes;
                if (!u.zoomProxy)
                {
                    for (var w in x)
                    {
                        x[w].reset();
                        x[w]._ticks = [];
                        if (u._zoom.axes[w] !== undefined)
                        {
                            x[w]._autoFormatString = u._zoom.axes[w].tickFormatString
                        }
                    }
                    this.redraw()
                }
                else
                {
                    var v = this.plugins.cursor.zoomCanvas._ctx;
                    v.clearRect(0, 0, v.canvas.width, v.canvas.height);
                    v = null
                }
                this.plugins.cursor._zoom.isZoomed = false;
                this.target.trigger("jqplotResetZoom", [this,
                    this.plugins.cursor])
            };
            if (u.showTooltipDataPosition)
            {
                u.showTooltipUnitPosition = false;
                u.showTooltipGridPosition = false;
                if (q.cursor.tooltipFormatString == undefined)
                {
                    u.tooltipFormatString = j.jqplot.Cursor.cursorLegendFormatString
                }
            }
        }
    };
    j.jqplot.Cursor.postDraw = function () {
        var x = this.plugins.cursor;
        if (x.zoomCanvas)
        {
            x.zoomCanvas.resetCanvas();
            x.zoomCanvas = null
        }
        if (x.cursorCanvas)
        {
            x.cursorCanvas.resetCanvas();
            x.cursorCanvas = null
        }
        if (x._tooltipElem)
        {
            x._tooltipElem.emptyForce();
            x._tooltipElem = null
        }
        if (x.zoom)
        {
            x.zoomCanvas = new j.jqplot.GenericCanvas();
            this.eventCanvas._elem.before(x.zoomCanvas.createElement(
                this._gridPadding, "jqplot-zoom-canvas",
                this._plotDimensions, this));
            x.zoomCanvas.setContext()
        }
        var v = document.createElement("div");
        x._tooltipElem = j(v);
        v = null;
        x._tooltipElem.addClass("jqplot-cursor-tooltip");
        x._tooltipElem.css({
            position: "absolute",
            display: "none"
        });
        if (x.zoomCanvas)
        {
            x.zoomCanvas._elem.before(x._tooltipElem)
        }
        else
        {
            this.eventCanvas._elem.before(x._tooltipElem)
        }
        if (x.showVerticalLine || x.showHorizontalLine)
        {
            x.cursorCanvas = new j.jqplot.GenericCanvas();
            this.eventCanvas._elem.before(x.cursorCanvas.createElement(
                this._gridPadding, "jqplot-cursor-canvas",
                this._plotDimensions, this));
            x.cursorCanvas.setContext()
        }
        if (x.showTooltipUnitPosition)
        {
            if (x.tooltipAxisGroups.length === 0)
            {
                var t = this.series;
                var u;
                var q = [];
                for (var r = 0; r < t.length; r++)
                {
                    u = t[r];
                    var w = u.xaxis + "," + u.yaxis;
                    if (j.inArray(w, q) == -1)
                    {
                        q.push(w)
                    }
                }
                for (var r = 0; r < q.length; r++)
                {
                    x.tooltipAxisGroups.push(q[r].split(","))
                }
            }
        }
    };
    j.jqplot.Cursor.zoomProxy = function (v, r) {
        var q = v.plugins.cursor;
        var u = r.plugins.cursor;
        q.zoomTarget = true;
        q.zoom = true;
        q.style = "auto";
        q.dblClickReset = false;
        u.zoom = true;
        u.zoomProxy = true;
        r.target.bind("jqplotZoom", t);
        r.target.bind("jqplotResetZoom", s);

        function t(x, w, z, y, A)
        {
            q.doZoom(w, z, v, A)
        }

        function s(w, x, y)
        {
            v.resetZoom()
        }
    };
    j.jqplot.Cursor.prototype.resetZoom = function (u, v) {
        var t = u.axes;
        var s = v._zoom.axes;
        if (!u.plugins.cursor.zoomProxy && v._zoom.isZoomed)
        {
            for (var r in t)
            {
                t[r].reset();
                t[r]._ticks = [];
                t[r]._autoFormatString = s[r].tickFormatString
            }
            u.redraw();
            v._zoom.isZoomed = false
        }
        else
        {
            var q = v.zoomCanvas._ctx;
            q.clearRect(0, 0, q.canvas.width, q.canvas.height);
            q = null
        }
        u.target.trigger("jqplotResetZoom", [u, v])
    };
    j.jqplot.Cursor.resetZoom = function (q) {
        q.resetZoom()
    };
    j.jqplot.Cursor.prototype.doZoom = function (G, t, C, u) {
        var I = u;
        var F = C.axes;
        var r = I._zoom.axes;
        var w = r.start;
        var s = r.end;
        var B, E, z, D, v, x, q, H, J;
        var A = C.plugins.cursor.zoomCanvas._ctx;
        if ((I.constrainZoomTo == "none"
            && Math.abs(G.x - I._zoom.start[0]) > 6 && Math.abs(G.y
                - I._zoom.start[1]) > 6)
            || (I.constrainZoomTo == "x" && Math
                .abs(G.x - I._zoom.start[0]) > 6)
            || (I.constrainZoomTo == "y" && Math
                .abs(G.y - I._zoom.start[1]) > 6))
        {
            if (!C.plugins.cursor.zoomProxy)
            {
                for (var y in t)
                {
                    if (I._zoom.axes[y] == undefined)
                    {
                        I._zoom.axes[y] = {};
                        I._zoom.axes[y].numberTicks = F[y].numberTicks;
                        I._zoom.axes[y].tickInterval = F[y].tickInterval;
                        I._zoom.axes[y].daTickInterval = F[y].daTickInterval;
                        I._zoom.axes[y].min = F[y].min;
                        I._zoom.axes[y].max = F[y].max;
                        I._zoom.axes[y].tickFormatString = (F[y].tickOptions != null) ? F[y].tickOptions.formatString
                            : ""
                    }
                    if ((I.constrainZoomTo == "none")
                        || (I.constrainZoomTo == "x" && y.charAt(0) == "x")
                        || (I.constrainZoomTo == "y" && y.charAt(0) == "y"))
                    {
                        z = t[y];
                        if (z != null)
                        {
                            if (z > w[y])
                            {
                                v = w[y];
                                x = z
                            }
                            else
                            {
                                D = w[y] - z;
                                v = z;
                                x = w[y]
                            }
                            q = F[y];
                            H = null;
                            if (q.alignTicks)
                            {
                                if (q.name === "x2axis" && C.axes.xaxis.show)
                                {
                                    H = C.axes.xaxis.numberTicks
                                }
                                else
                                {
                                    if (q.name.charAt(0) === "y"
                                        && q.name !== "yaxis"
                                        && q.name !== "yMidAxis"
                                        && C.axes.yaxis.show)
                                    {
                                        H = C.axes.yaxis.numberTicks
                                    }
                                }
                            }
                            if (this.looseZoom
                                && (F[y].renderer.constructor === j.jqplot.LinearAxisRenderer || F[y].renderer.constructor === j.jqplot.LogAxisRenderer))
                            {
                                J = j.jqplot.LinearTickGenerator(v, x,
                                    q._scalefact, H);
                                if (F[y].tickInset
                                    && J[0] < F[y].min + F[y].tickInset
                                    * F[y].tickInterval)
                                {
                                    J[0] += J[4];
                                    J[2] -= 1
                                }
                                if (F[y].tickInset
                                    && J[1] > F[y].max - F[y].tickInset
                                    * F[y].tickInterval)
                                {
                                    J[1] -= J[4];
                                    J[2] -= 1
                                }
                                if (F[y].renderer.constructor === j.jqplot.LogAxisRenderer
                                    && J[0] < F[y].min)
                                {
                                    J[0] += J[4];
                                    J[2] -= 1
                                }
                                F[y].min = J[0];
                                F[y].max = J[1];
                                F[y]._autoFormatString = J[3];
                                F[y].numberTicks = J[2];
                                F[y].tickInterval = J[4];
                                F[y].daTickInterval = [J[4] / 1000, "seconds"]
                            }
                            else
                            {
                                F[y].min = v;
                                F[y].max = x;
                                F[y].tickInterval = null;
                                F[y].numberTicks = null;
                                F[y].daTickInterval = null
                            }
                            F[y]._ticks = []
                        }
                    }
                }
                A.clearRect(0, 0, A.canvas.width, A.canvas.height);
                C.redraw();
                I._zoom.isZoomed = true;
                A = null
            }
            C.target.trigger("jqplotZoom", [G, t, C, u])
        }
    };
    j.jqplot.preInitHooks.push(j.jqplot.Cursor.init);
    j.jqplot.postDrawHooks.push(j.jqplot.Cursor.postDraw);

    function e(G, r, C)
    {
        var J = C.plugins.cursor;
        var w = "";
        var N = false;
        if (J.showTooltipGridPosition)
        {
            w = G.x + ", " + G.y;
            N = true
        }
        if (J.showTooltipUnitPosition)
        {
            var F;
            for (var E = 0; E < J.tooltipAxisGroups.length; E++)
            {
                F = J.tooltipAxisGroups[E];
                if (N)
                {
                    w += "<br />"
                }
                if (J.useAxesFormatters)
                {
                    for (var D = 0; D < F.length; D++)
                    {
                        if (D)
                        {
                            w += ", "
                        }
                        var H = C.axes[F[D]]._ticks[0].formatter;
                        var B = C.axes[F[D]]._ticks[0].formatString;
                        w += H(B, r[F[D]])
                    }
                }
                else
                {
                    w += j.jqplot.sprintf(J.tooltipFormatString, r[F[0]],
                        r[F[1]])
                }
                N = true
            }
        }
        if (J.showTooltipDataPosition)
        {
            var u = C.series;
            var M = d(C, G.x, G.y);
            var N = false;
            for (var E = 0; E < u.length; E++)
            {
                if (u[E].show)
                {
                    var y = u[E].index;
                    var t = u[E].label.toString();
                    var I = j.inArray(y, M.indices);
                    var z = undefined;
                    var x = undefined;
                    if (I != -1)
                    {
                        var L = M.data[I].data;
                        if (J.useAxesFormatters)
                        {
                            var A = u[E]._xaxis._ticks[0].formatter;
                            var q = u[E]._yaxis._ticks[0].formatter;
                            var K = u[E]._xaxis._ticks[0].formatString;
                            var v = u[E]._yaxis._ticks[0].formatString;
                            z = A(K, L[0]);
                            x = q(v, L[1])
                        }
                        else
                        {
                            z = L[0];
                            x = L[1]
                        }
                        if (N)
                        {
                            w += "<br />"
                        }
                        w += j.jqplot.sprintf(J.tooltipFormatString, t, z, x);
                        N = true
                    }
                }
            }
        }
        J._tooltipElem.html(w)
    }

    function g(C, A)
    {
        var E = A.plugins.cursor;
        var z = E.cursorCanvas._ctx;
        z.clearRect(0, 0, z.canvas.width, z.canvas.height);
        if (E.showVerticalLine)
        {
            E.shapeRenderer.draw(z, [[C.x, 0], [C.x, z.canvas.height]])
        }
        if (E.showHorizontalLine)
        {
            E.shapeRenderer.draw(z, [[0, C.y], [z.canvas.width, C.y]])
        }
        var G = d(A, C.x, C.y);
        if (E.showCursorLegend)
        {
            var r = j(A.targetId + " td.jqplot-cursor-legend-label");
            for (var B = 0; B < r.length; B++)
            {
                var v = j(r[B]).data("seriesIndex");
                var t = A.series[v];
                var s = t.label.toString();
                var D = j.inArray(v, G.indices);
                var x = undefined;
                var w = undefined;
                if (D != -1)
                {
                    var H = G.data[D].data;
                    if (E.useAxesFormatters)
                    {
                        var y = t._xaxis._ticks[0].formatter;
                        var q = t._yaxis._ticks[0].formatter;
                        var F = t._xaxis._ticks[0].formatString;
                        var u = t._yaxis._ticks[0].formatString;
                        x = y(F, H[0]);
                        w = q(u, H[1])
                    }
                    else
                    {
                        x = H[0];
                        w = H[1]
                    }
                }
                if (A.legend.escapeHtml)
                {
                    j(r[B]).text(
                        j.jqplot.sprintf(E.cursorLegendFormatString, s, x,
                            w))
                }
                else
                {
                    j(r[B]).html(
                        j.jqplot.sprintf(E.cursorLegendFormatString, s, x,
                            w))
                }
            }
        }
        z = null
    }

    function d(A, F, E)
    {
        var B = {
            indices: [],
            data: []
        };
        var G, w, u, C, v, q, t;
        var z;
        var D = A.plugins.cursor;
        for (var w = 0; w < A.series.length; w++)
        {
            G = A.series[w];
            q = G.renderer;
            if (G.show)
            {
                z = D.intersectionThreshold;
                if (G.showMarker)
                {
                    z += G.markerRenderer.size / 2
                }
                for (var v = 0; v < G.gridData.length; v++)
                {
                    t = G.gridData[v];
                    if (D.showVerticalLine)
                    {
                        if (Math.abs(F - t[0]) <= z)
                        {
                            B.indices.push(w);
                            B.data.push({
                                seriesIndex: w,
                                pointIndex: v,
                                gridData: t,
                                data: G.data[v]
                            })
                        }
                    }
                }
            }
        }
        return B
    }

    function n(r, t)
    {
        var v = t.plugins.cursor;
        var s = v._tooltipElem;
        switch (v.tooltipLocation)
        {
            case "nw":
                var q = r.x + t._gridPadding.left - s.outerWidth(true)
                    - v.tooltipOffset;
                var u = r.y + t._gridPadding.top - v.tooltipOffset
                    - s.outerHeight(true);
                break;
            case "n":
                var q = r.x + t._gridPadding.left - s.outerWidth(true) / 2;
                var u = r.y + t._gridPadding.top - v.tooltipOffset
                    - s.outerHeight(true);
                break;
            case "ne":
                var q = r.x + t._gridPadding.left + v.tooltipOffset;
                var u = r.y + t._gridPadding.top - v.tooltipOffset
                    - s.outerHeight(true);
                break;
            case "e":
                var q = r.x + t._gridPadding.left + v.tooltipOffset;
                var u = r.y + t._gridPadding.top - s.outerHeight(true) / 2;
                break;
            case "se":
                var q = r.x + t._gridPadding.left + v.tooltipOffset;
                var u = r.y + t._gridPadding.top + v.tooltipOffset;
                break;
            case "s":
                var q = r.x + t._gridPadding.left - s.outerWidth(true) / 2;
                var u = r.y + t._gridPadding.top + v.tooltipOffset;
                break;
            case "sw":
                var q = r.x + t._gridPadding.left - s.outerWidth(true)
                    - v.tooltipOffset;
                var u = r.y + t._gridPadding.top + v.tooltipOffset;
                break;
            case "w":
                var q = r.x + t._gridPadding.left - s.outerWidth(true)
                    - v.tooltipOffset;
                var u = r.y + t._gridPadding.top - s.outerHeight(true) / 2;
                break;
            default:
                var q = r.x + t._gridPadding.left + v.tooltipOffset;
                var u = r.y + t._gridPadding.top + v.tooltipOffset;
                break
        }
        s.css("left", q);
        s.css("top", u);
        s = null
    }

    function m(u)
    {
        var s = u._gridPadding;
        var v = u.plugins.cursor;
        var t = v._tooltipElem;
        switch (v.tooltipLocation)
        {
            case "nw":
                var r = s.left + v.tooltipOffset;
                var q = s.top + v.tooltipOffset;
                t.css("left", r);
                t.css("top", q);
                break;
            case "n":
                var r = (s.left + (u._plotDimensions.width - s.right)) / 2
                    - t.outerWidth(true) / 2;
                var q = s.top + v.tooltipOffset;
                t.css("left", r);
                t.css("top", q);
                break;
            case "ne":
                var r = s.right + v.tooltipOffset;
                var q = s.top + v.tooltipOffset;
                t.css({
                    right: r,
                    top: q
                });
                break;
            case "e":
                var r = s.right + v.tooltipOffset;
                var q = (s.top + (u._plotDimensions.height - s.bottom)) / 2
                    - t.outerHeight(true) / 2;
                t.css({
                    right: r,
                    top: q
                });
                break;
            case "se":
                var r = s.right + v.tooltipOffset;
                var q = s.bottom + v.tooltipOffset;
                t.css({
                    right: r,
                    bottom: q
                });
                break;
            case "s":
                var r = (s.left + (u._plotDimensions.width - s.right)) / 2
                    - t.outerWidth(true) / 2;
                var q = s.bottom + v.tooltipOffset;
                t.css({
                    left: r,
                    bottom: q
                });
                break;
            case "sw":
                var r = s.left + v.tooltipOffset;
                var q = s.bottom + v.tooltipOffset;
                t.css({
                    left: r,
                    bottom: q
                });
                break;
            case "w":
                var r = s.left + v.tooltipOffset;
                var q = (s.top + (u._plotDimensions.height - s.bottom)) / 2
                    - t.outerHeight(true) / 2;
                t.css({
                    left: r,
                    top: q
                });
                break;
            default:
                var r = s.right - v.tooltipOffset;
                var q = s.bottom + v.tooltipOffset;
                t.css({
                    right: r,
                    bottom: q
                });
                break
        }
        t = null
    }

    function k(r, q, v, u, t)
    {
        r.preventDefault();
        r.stopImmediatePropagation();
        var w = t.plugins.cursor;
        if (w.clickReset)
        {
            w.resetZoom(t, w)
        }
        var s = window.getSelection;
        if (document.selection && document.selection.empty)
        {
            document.selection.empty()
        }
        else
        {
            if (s && !s().isCollapsed)
            {
                s().collapse()
            }
        }
        return false
    }

    function c(r, q, v, u, t)
    {
        r.preventDefault();
        r.stopImmediatePropagation();
        var w = t.plugins.cursor;
        if (w.dblClickReset)
        {
            w.resetZoom(t, w)
        }
        var s = window.getSelection;
        if (document.selection && document.selection.empty)
        {
            document.selection.empty()
        }
        else
        {
            if (s && !s().isCollapsed)
            {
                s().collapse()
            }
        }
        return false
    }

    function f(w, t, q, z, u)
    {
        var v = u.plugins.cursor;
        v.onGrid = false;
        if (v.show)
        {
            j(w.target).css("cursor", v.previousCursor);
            if (v.showTooltip
                && !(v._zoom.zooming && v.showTooltipOutsideZoom && !v.constrainOutsideZoom))
            {
                v._tooltipElem.empty();
                v._tooltipElem.hide()
            }
            if (v.zoom)
            {
                v._zoom.gridpos = t;
                v._zoom.datapos = q
            }
            if (v.showVerticalLine || v.showHorizontalLine)
            {
                var B = v.cursorCanvas._ctx;
                B.clearRect(0, 0, B.canvas.width, B.canvas.height);
                B = null
            }
            if (v.showCursorLegend)
            {
                var A = j(u.targetId + " td.jqplot-cursor-legend-label");
                for (var s = 0; s < A.length; s++)
                {
                    var y = j(A[s]).data("seriesIndex");
                    var r = u.series[y];
                    var x = r.label.toString();
                    if (u.legend.escapeHtml)
                    {
                        j(A[s]).text(
                            j.jqplot.sprintf(v.cursorLegendFormatString, x,
                                undefined, undefined))
                    }
                    else
                    {
                        j(A[s]).html(
                            j.jqplot.sprintf(v.cursorLegendFormatString, x,
                                undefined, undefined))
                    }
                }
            }
        }
    }

    function b(r, q, u, t, s)
    {
        var v = s.plugins.cursor;
        v.onGrid = true;
        if (v.show)
        {
            v.previousCursor = r.target.style.cursor;
            r.target.style.cursor = v.style;
            if (v.showTooltip)
            {
                e(q, u, s);
                if (v.followMouse)
                {
                    n(q, s)
                }
                else
                {
                    m(s)
                }
                v._tooltipElem.show()
            }
            if (v.showVerticalLine || v.showHorizontalLine)
            {
                g(q, s)
            }
        }
    }

    function i(r, q, u, t, s)
    {
        var v = s.plugins.cursor;
        if (v.show)
        {
            if (v.showTooltip)
            {
                e(q, u, s);
                if (v.followMouse)
                {
                    n(q, s)
                }
            }
            if (v.showVerticalLine || v.showHorizontalLine)
            {
                g(q, s)
            }
        }
    }

    function o(y)
    {
        var x = y.data.plot;
        var t = x.eventCanvas._elem.offset();
        var w = {
            x: y.pageX - t.left,
            y: y.pageY - t.top
        };
        var u = {
            xaxis: null,
            yaxis: null,
            x2axis: null,
            y2axis: null,
            y3axis: null,
            y4axis: null,
            y5axis: null,
            y6axis: null,
            y7axis: null,
            y8axis: null,
            y9axis: null,
            yMidAxis: null
        };
        var v = ["xaxis", "yaxis", "x2axis", "y2axis", "y3axis", "y4axis",
            "y5axis", "y6axis", "y7axis", "y8axis", "y9axis", "yMidAxis"];
        var q = x.axes;
        var r, s;
        for (r = 11; r > 0; r--)
        {
            s = v[r - 1];
            if (q[s].show)
            {
                u[s] = q[s].series_p2u(w[s.charAt(0)])
            }
        }
        return {
            offsets: t,
            gridPos: w,
            dataPos: u
        }
    }

    function h(z)
    {
        var x = z.data.plot;
        var y = x.plugins.cursor;
        if (y.show && y.zoom && y._zoom.started && !y.zoomTarget)
        {
            z.preventDefault();
            var B = y.zoomCanvas._ctx;
            var v = o(z);
            var w = v.gridPos;
            var t = v.dataPos;
            y._zoom.gridpos = w;
            y._zoom.datapos = t;
            y._zoom.zooming = true;
            var u = w.x;
            var s = w.y;
            var A = B.canvas.height;
            var q = B.canvas.width;
            if (y.showTooltip && !y.onGrid && y.showTooltipOutsideZoom)
            {
                e(w, t, x);
                if (y.followMouse)
                {
                    n(w, x)
                }
            }
            if (y.constrainZoomTo == "x")
            {
                y._zoom.end = [u, A]
            }
            else
            {
                if (y.constrainZoomTo == "y")
                {
                    y._zoom.end = [q, s]
                }
                else
                {
                    y._zoom.end = [u, s]
                }
            }
            var r = window.getSelection;
            if (document.selection && document.selection.empty)
            {
                document.selection.empty()
            }
            else
            {
                if (r && !r().isCollapsed)
                {
                    r().collapse()
                }
            }
            l.call(y);
            B = null
        }
    }

    function a(w, s, r, x, t)
    {
        var v = t.plugins.cursor;
        if (t.plugins.mobile)
        {
            j(document).one("vmouseup.jqplot_cursor", {
                plot: t
            }, p)
        }
        else
        {
            j(document).one("mouseup.jqplot_cursor", {
                plot: t
            }, p)
        }
        var u = t.axes;
        if (document.onselectstart != undefined)
        {
            v._oldHandlers.onselectstart = document.onselectstart;
            document.onselectstart = function () {
                return false
            }
        }
        if (document.ondrag != undefined)
        {
            v._oldHandlers.ondrag = document.ondrag;
            document.ondrag = function () {
                return false
            }
        }
        if (document.onmousedown != undefined)
        {
            v._oldHandlers.onmousedown = document.onmousedown;
            document.onmousedown = function () {
                return false
            }
        }
        if (v.zoom)
        {
            if (!v.zoomProxy)
            {
                var y = v.zoomCanvas._ctx;
                y.clearRect(0, 0, y.canvas.width, y.canvas.height);
                y = null
            }
            if (v.constrainZoomTo == "x")
            {
                v._zoom.start = [s.x, 0]
            }
            else
            {
                if (v.constrainZoomTo == "y")
                {
                    v._zoom.start = [0, s.y]
                }
                else
                {
                    v._zoom.start = [s.x, s.y]
                }
            }
            v._zoom.started = true;
            for (var q in r)
            {
                v._zoom.axes.start[q] = r[q]
            }
            if (t.plugins.mobile)
            {
                j(document).bind("vmousemove.jqplotCursor", {
                    plot: t
                }, h)
            }
            else
            {
                j(document).bind("mousemove.jqplotCursor", {
                    plot: t
                }, h)
            }
        }
    }

    function p(y)
    {
        var v = y.data.plot;
        var x = v.plugins.cursor;
        if (x.zoom && x._zoom.zooming && !x.zoomTarget)
        {
            var u = x._zoom.gridpos.x;
            var r = x._zoom.gridpos.y;
            var t = x._zoom.datapos;
            var z = x.zoomCanvas._ctx.canvas.height;
            var q = x.zoomCanvas._ctx.canvas.width;
            var w = v.axes;
            if (x.constrainOutsideZoom && !x.onGrid)
            {
                if (u < 0)
                {
                    u = 0
                }
                else
                {
                    if (u > q)
                    {
                        u = q
                    }
                }
                if (r < 0)
                {
                    r = 0
                }
                else
                {
                    if (r > z)
                    {
                        r = z
                    }
                }
                for (var s in t)
                {
                    if (t[s])
                    {
                        if (s.charAt(0) == "x")
                        {
                            t[s] = w[s].series_p2u(u)
                        }
                        else
                        {
                            t[s] = w[s].series_p2u(r)
                        }
                    }
                }
            }
            if (x.constrainZoomTo == "x")
            {
                r = z
            }
            else
            {
                if (x.constrainZoomTo == "y")
                {
                    u = q
                }
            }
            x._zoom.end = [u, r];
            x._zoom.gridpos = {
                x: u,
                y: r
            };
            x.doZoom(x._zoom.gridpos, t, v, x)
        }
        x._zoom.started = false;
        x._zoom.zooming = false;
        j(document).unbind("mousemove.jqplotCursor", h);
        if (document.onselectstart != undefined
            && x._oldHandlers.onselectstart != null)
        {
            document.onselectstart = x._oldHandlers.onselectstart;
            x._oldHandlers.onselectstart = null
        }
        if (document.ondrag != undefined && x._oldHandlers.ondrag != null)
        {
            document.ondrag = x._oldHandlers.ondrag;
            x._oldHandlers.ondrag = null
        }
        if (document.onmousedown != undefined
            && x._oldHandlers.onmousedown != null)
        {
            document.onmousedown = x._oldHandlers.onmousedown;
            x._oldHandlers.onmousedown = null
        }
    }

    function l()
    {
        var y = this._zoom.start;
        var u = this._zoom.end;
        var s = this.zoomCanvas._ctx;
        var r, v, x, q;
        if (u[0] > y[0])
        {
            r = y[0];
            q = u[0] - y[0]
        }
        else
        {
            r = u[0];
            q = y[0] - u[0]
        }
        if (u[1] > y[1])
        {
            v = y[1];
            x = u[1] - y[1]
        }
        else
        {
            v = u[1];
            x = y[1] - u[1]
        }
        s.fillStyle = "rgba(0,0,0,0.2)";
        s.strokeStyle = "#999999";
        s.lineWidth = 1;
        s.clearRect(0, 0, s.canvas.width, s.canvas.height);
        s.fillRect(0, 0, s.canvas.width, s.canvas.height);
        s.clearRect(r, v, q, x);
        s.strokeRect(r, v, q, x);
        s = null
    }

    j.jqplot.CursorLegendRenderer = function (q) {
        j.jqplot.TableLegendRenderer.call(this, q);
        this.formatString = "%s"
    };
    j.jqplot.CursorLegendRenderer.prototype = new j.jqplot.TableLegendRenderer();
    j.jqplot.CursorLegendRenderer.prototype.constructor = j.jqplot.CursorLegendRenderer;
    j.jqplot.CursorLegendRenderer.prototype.draw = function () {
        if (this._elem)
        {
            this._elem.emptyForce();
            this._elem = null
        }
        if (this.show)
        {
            var w = this._series, A;
            var r = document.createElement("table");
            this._elem = j(r);
            r = null;
            this._elem.addClass("jqplot-legend jqplot-cursor-legend");
            this._elem.css("position", "absolute");
            var q = false;
            for (var x = 0; x < w.length; x++)
            {
                A = w[x];
                if (A.show && A.showLabel)
                {
                    var v = j.jqplot.sprintf(this.formatString, A.label
                        .toString());
                    if (v)
                    {
                        var t = A.color;
                        if (A._stack && !A.fill)
                        {
                            t = ""
                        }
                        z.call(this, v, t, q, x);
                        q = true
                    }
                    for (var u = 0; u < j.jqplot.addLegendRowHooks.length; u++)
                    {
                        var y = j.jqplot.addLegendRowHooks[u].call(this, A);
                        if (y)
                        {
                            z.call(this, y.label, y.color, q);
                            q = true
                        }
                    }
                }
            }
            w = A = null;
            delete w;
            delete A
        }

        function z(D, C, F, s)
        {
            var B = (F) ? this.rowSpacing : "0";
            var E = j('<tr class="jqplot-legend jqplot-cursor-legend"></tr>')
                .appendTo(this._elem);
            E.data("seriesIndex", s);
            j(
                '<td class="jqplot-legend jqplot-cursor-legend-swatch" style="padding-top:'
                + B
                + ';"><div style="border:1px solid #cccccc;padding:0.2em;"><div class="jqplot-cursor-legend-swatch" style="background-color:'
                + C + ';"></div></div></td>').appendTo(E);
            var G = j('<td class="jqplot-legend jqplot-cursor-legend-label" style="vertical-align:middle;padding-top:'
                + B + ';"></td>');
            G.appendTo(E);
            G.data("seriesIndex", s);
            if (this.escapeHtml)
            {
                G.text(D)
            }
            else
            {
                G.html(D)
            }
            E = null;
            G = null
        }

        return this._elem
    }
})(jQuery);
(function (d) {
    d.jqplot.eventListenerHooks.push(["jqplotMouseMove", f]);
    d.jqplot.Highlighter = function (h) {
        this.show = d.jqplot.config.enablePlugins;
        this.markerRenderer = new d.jqplot.MarkerRenderer({
            shadow: false
        });
        this.showMarker = true;
        this.lineWidthAdjust = 2.5;
        this.sizeAdjust = 5;
        this.showTooltip = true;
        this.tooltipLocation = "nw";
        this.fadeTooltip = true;
        this.tooltipFadeSpeed = "fast";
        this.tooltipOffset = 2;
        this.tooltipAxes = "both";
        this.tooltipSeparator = ", ";
        this.tooltipContentEditor = null;
        this.useAxesFormatters = true;
        this.tooltipFormatString = "%.5P";
        this.formatString = null;
        this.yvalues = 1;
        this.bringSeriesToFront = false;
        this._tooltipElem;
        this.isHighlighting = false;
        this.currentNeighbor = null;
        d.extend(true, this, h)
    };
    var b = ["nw", "n", "ne", "e", "se", "s", "sw", "w"];
    var e = {
        nw: 0,
        n: 1,
        ne: 2,
        e: 3,
        se: 4,
        s: 5,
        sw: 6,
        w: 7
    };
    var c = ["se", "s", "sw", "w", "nw", "n", "ne", "e"];
    d.jqplot.Highlighter.init = function (k, j, i) {
        var h = i || {};
        this.plugins.highlighter = new d.jqplot.Highlighter(h.highlighter)
    };
    d.jqplot.Highlighter.parseOptions = function (i, h) {
        this.showHighlight = true
    };
    d.jqplot.Highlighter.postPlotDraw = function () {
        if (this.plugins.highlighter
            && this.plugins.highlighter.highlightCanvas)
        {
            this.plugins.highlighter.highlightCanvas.resetCanvas();
            this.plugins.highlighter.highlightCanvas = null
        }
        if (this.plugins.highlighter && this.plugins.highlighter._tooltipElem)
        {
            this.plugins.highlighter._tooltipElem.emptyForce();
            this.plugins.highlighter._tooltipElem = null
        }
        this.plugins.highlighter.highlightCanvas = new d.jqplot.GenericCanvas();
        this.eventCanvas._elem.before(this.plugins.highlighter.highlightCanvas
            .createElement(this._gridPadding, "jqplot-highlight-canvas",
                this._plotDimensions, this));
        this.plugins.highlighter.highlightCanvas.setContext();
        var h = document.createElement("div");
        this.plugins.highlighter._tooltipElem = d(h);
        h = null;
        this.plugins.highlighter._tooltipElem
            .addClass("jqplot-highlighter-tooltip");
        this.plugins.highlighter._tooltipElem.css({
            position: "absolute",
            display: "none"
        });
        this.eventCanvas._elem.before(this.plugins.highlighter._tooltipElem)
    };
    d.jqplot.preInitHooks.push(d.jqplot.Highlighter.init);
    d.jqplot.preParseSeriesOptionsHooks.push(d.jqplot.Highlighter.parseOptions);
    d.jqplot.postDrawHooks.push(d.jqplot.Highlighter.postPlotDraw);

    function a(o, q)
    {
        var j = o.plugins.highlighter;
        var r = o.series[q.seriesIndex];
        var h = r.markerRenderer;
        var i = j.markerRenderer;
        i.style = h.style;
        i.lineWidth = h.lineWidth + j.lineWidthAdjust;
        i.size = h.size + j.sizeAdjust;
        var l = d.jqplot.getColorComponents(h.color);
        var p = [l[0], l[1], l[2]];
        var k = (l[3] >= 0.6) ? l[3] * 0.6 : l[3] * (2 - l[3]);
        i.color = "rgba(" + p[0] + "," + p[1] + "," + p[2] + "," + k + ")";
        i.init();
        var n = r.gridData[q.pointIndex][0];
        var m = r.gridData[q.pointIndex][1];
        if (r.renderer.constructor == d.jqplot.BarRenderer)
        {
            if (r.barDirection == "vertical")
            {
                n += r._barNudge
            }
            else
            {
                m -= r._barNudge
            }
        }
        i.draw(n, m, j.highlightCanvas._ctx)
    }

    function g(A, q, m)
    {
        var k = A.plugins.highlighter;
        var D = k._tooltipElem;
        var r = q.highlighter || {};
        var t = d.extend(true, {}, k, r);
        if (t.useAxesFormatters)
        {
            var w = q._xaxis._ticks[0].formatter;
            var h = q._yaxis._ticks[0].formatter;
            var E = q._xaxis._ticks[0].formatString;
            var s = q._yaxis._ticks[0].formatString;
            var z;
            var u = w(E, m.data[0]);
            var l = [];
            for (var B = 1; B < t.yvalues + 1; B++)
            {
                l.push(h(s, m.data[B]))
            }
            if (typeof t.formatString === "string")
            {
                switch (t.tooltipAxes)
                {
                    case "both":
                    case "xy":
                        l.unshift(u);
                        l.unshift(t.formatString);
                        z = d.jqplot.sprintf.apply(d.jqplot.sprintf, l);
                        break;
                    case "yx":
                        l.push(u);
                        l.unshift(t.formatString);
                        z = d.jqplot.sprintf.apply(d.jqplot.sprintf, l);
                        break;
                    case "x":
                        z = d.jqplot.sprintf.apply(d.jqplot.sprintf, [
                            t.formatString, u]);
                        break;
                    case "y":
                        l.unshift(t.formatString);
                        z = d.jqplot.sprintf.apply(d.jqplot.sprintf, l);
                        break;
                    default:
                        l.unshift(u);
                        l.unshift(t.formatString);
                        z = d.jqplot.sprintf.apply(d.jqplot.sprintf, l);
                        break
                }
            }
            else
            {
                switch (t.tooltipAxes)
                {
                    case "both":
                    case "xy":
                        z = u;
                        for (var B = 0; B < l.length; B++)
                        {
                            z += t.tooltipSeparator + l[B]
                        }
                        break;
                    case "yx":
                        z = "";
                        for (var B = 0; B < l.length; B++)
                        {
                            z += l[B] + t.tooltipSeparator
                        }
                        z += u;
                        break;
                    case "x":
                        z = u;
                        break;
                    case "y":
                        z = l.join(t.tooltipSeparator);
                        break;
                    default:
                        z = u;
                        for (var B = 0; B < l.length; B++)
                        {
                            z += t.tooltipSeparator + l[B]
                        }
                        break
                }
            }
        }
        else
        {
            var z;
            if (typeof t.formatString === "string")
            {
                z = d.jqplot.sprintf.apply(d.jqplot.sprintf, [t.formatString]
                    .concat(m.data))
            }
            else
            {
                if (t.tooltipAxes == "both" || t.tooltipAxes == "xy")
                {
                    z = d.jqplot.sprintf(t.tooltipFormatString, m.data[0])
                        + t.tooltipSeparator
                        + d.jqplot
                            .sprintf(t.tooltipFormatString, m.data[1])
                }
                else
                {
                    if (t.tooltipAxes == "yx")
                    {
                        z = d.jqplot.sprintf(t.tooltipFormatString, m.data[1])
                            + t.tooltipSeparator
                            + d.jqplot.sprintf(t.tooltipFormatString,
                                m.data[0])
                    }
                    else
                    {
                        if (t.tooltipAxes == "x")
                        {
                            z = d.jqplot.sprintf(t.tooltipFormatString,
                                m.data[0])
                        }
                        else
                        {
                            if (t.tooltipAxes == "y")
                            {
                                z = d.jqplot.sprintf(t.tooltipFormatString,
                                    m.data[1])
                            }
                        }
                    }
                }
            }
        }
        if (d.isFunction(t.tooltipContentEditor))
        {
            z = t.tooltipContentEditor(z, m.seriesIndex, m.pointIndex, A)
        }
        D.html(z);
        var C = {
            x: m.gridData[0],
            y: m.gridData[1]
        };
        var v = 0;
        var j = 0.707;
        if (q.markerRenderer.show == true)
        {
            v = (q.markerRenderer.size + t.sizeAdjust) / 2
        }
        var o = b;
        if (q.fillToZero && q.fill && m.data[1] < 0)
        {
            o = c
        }
        switch (o[e[t.tooltipLocation]])
        {
            case "nw":
                var p = C.x + A._gridPadding.left - D.outerWidth(true)
                    - t.tooltipOffset - j * v;
                var n = C.y + A._gridPadding.top - t.tooltipOffset
                    - D.outerHeight(true) - j * v;
                break;
            case "n":
                var p = C.x + A._gridPadding.left - D.outerWidth(true) / 2;
                var n = C.y + A._gridPadding.top - t.tooltipOffset
                    - D.outerHeight(true) - v;
                break;
            case "ne":
                var p = C.x + A._gridPadding.left + t.tooltipOffset + j * v;
                var n = C.y + A._gridPadding.top - t.tooltipOffset
                    - D.outerHeight(true) - j * v;
                break;
            case "e":
                var p = C.x + A._gridPadding.left + t.tooltipOffset + v;
                var n = C.y + A._gridPadding.top - D.outerHeight(true) / 2;
                break;
            case "se":
                var p = C.x + A._gridPadding.left + t.tooltipOffset + j * v;
                var n = C.y + A._gridPadding.top + t.tooltipOffset + j * v;
                break;
            case "s":
                var p = C.x + A._gridPadding.left - D.outerWidth(true) / 2;
                var n = C.y + A._gridPadding.top + t.tooltipOffset + v;
                break;
            case "sw":
                var p = C.x + A._gridPadding.left - D.outerWidth(true)
                    - t.tooltipOffset - j * v;
                var n = C.y + A._gridPadding.top + t.tooltipOffset + j * v;
                break;
            case "w":
                var p = C.x + A._gridPadding.left - D.outerWidth(true)
                    - t.tooltipOffset - v;
                var n = C.y + A._gridPadding.top - D.outerHeight(true) / 2;
                break;
            default:
                var p = C.x + A._gridPadding.left - D.outerWidth(true)
                    - t.tooltipOffset - j * v;
                var n = C.y + A._gridPadding.top - t.tooltipOffset
                    - D.outerHeight(true) - j * v;
                break
        }
        if (q.renderer.constructor == d.jqplot.BarRenderer)
        {
            if (q.barDirection == "vertical")
            {
                p += q._barNudge
            }
            else
            {
                n -= q._barNudge
            }
        }
        D.css("left", p);
        D.css("top", n);
        if (t.fadeTooltip)
        {
            D.stop(true, true).fadeIn(t.tooltipFadeSpeed)
        }
        else
        {
            D.show()
        }
        D = null
    }

    function f(n, j, i, p, l)
    {
        var h = l.plugins.highlighter;
        var m = l.plugins.cursor;
        if (h.show)
        {
            if (p == null && h.isHighlighting)
            {
                var o = jQuery.Event("jqplotHighlighterUnhighlight");
                l.target.trigger(o);
                var q = h.highlightCanvas._ctx;
                q.clearRect(0, 0, q.canvas.width, q.canvas.height);
                if (h.fadeTooltip)
                {
                    h._tooltipElem.fadeOut(h.tooltipFadeSpeed)
                }
                else
                {
                    h._tooltipElem.hide()
                }
                if (h.bringSeriesToFront)
                {
                    l.restorePreviousSeriesOrder()
                }
                h.isHighlighting = false;
                h.currentNeighbor = null;
                q = null
            }
            else
            {
                if (p != null && l.series[p.seriesIndex].showHighlight
                    && !h.isHighlighting)
                {
                    var o = jQuery.Event("jqplotHighlighterHighlight");
                    o.which = n.which;
                    o.pageX = n.pageX;
                    o.pageY = n.pageY;
                    var k = [p.seriesIndex, p.pointIndex, p.data, l];
                    l.target.trigger(o, k);
                    h.isHighlighting = true;
                    h.currentNeighbor = p;
                    if (h.showMarker)
                    {
                        a(l, p)
                    }
                    if (l.series[p.seriesIndex].show && h.showTooltip
                        && (!m || !m._zoom.started))
                    {
                        g(l, l.series[p.seriesIndex], p)
                    }
                    if (h.bringSeriesToFront)
                    {
                        l.moveSeriesToFront(p.seriesIndex)
                    }
                }
                else
                {
                    if (p != null && h.isHighlighting && h.currentNeighbor != p)
                    {
                        if (l.series[p.seriesIndex].showHighlight)
                        {
                            var q = h.highlightCanvas._ctx;
                            q.clearRect(0, 0, q.canvas.width, q.canvas.height);
                            h.isHighlighting = true;
                            h.currentNeighbor = p;
                            if (h.showMarker)
                            {
                                a(l, p)
                            }
                            if (l.series[p.seriesIndex].show && h.showTooltip
                                && (!m || !m._zoom.started))
                            {
                                g(l, l.series[p.seriesIndex], p)
                            }
                            if (h.bringSeriesToFront)
                            {
                                l.moveSeriesToFront(p.seriesIndex)
                            }
                        }
                    }
                }
            }
        }
    }
})(jQuery);
(function (h) {
    h.jqplot.DateAxisRenderer = function () {
        h.jqplot.LinearAxisRenderer.call(this);
        this.date = new h.jsDate()
    };
    var c = 1000;
    var e = 60 * c;
    var f = 60 * e;
    var l = 24 * f;
    var b = 7 * l;
    var j = 30.4368499 * l;
    var k = 365.242199 * l;
    var g = [31, 28, 31, 30, 31, 30, 31, 30, 31, 30, 31, 30];
    var i = ["%M:%S.%#N", "%M:%S.%#N", "%M:%S.%#N", "%M:%S", "%M:%S", "%M:%S",
        "%M:%S", "%H:%M:%S", "%H:%M:%S", "%H:%M", "%H:%M", "%H:%M",
        "%H:%M", "%H:%M", "%H:%M", "%a %H:%M", "%a %H:%M", "%b %e %H:%M",
        "%b %e %H:%M", "%b %e %H:%M", "%b %e %H:%M", "%v", "%v", "%v",
        "%v", "%v", "%v", "%v"];
    var m = [0.1 * c, 0.2 * c, 0.5 * c, c, 2 * c, 5 * c, 10 * c, 15 * c,
        30 * c, e, 2 * e, 5 * e, 10 * e, 15 * e, 30 * e, f, 2 * f, 4 * f,
        6 * f, 8 * f, 12 * f, l, 2 * l, 3 * l, 4 * l, 5 * l, b, 2 * b];
    var d = [];

    function a(p, s, t)
    {
        var o = Number.MAX_VALUE;
        var u, r, v;
        for (var q = 0, n = m.length; q < n; q++)
        {
            u = Math.abs(t - m[q]);
            if (u < o)
            {
                o = u;
                r = m[q];
                v = i[q]
            }
        }
        return [r, v]
    }

    h.jqplot.DateAxisRenderer.prototype = new h.jqplot.LinearAxisRenderer();
    h.jqplot.DateAxisRenderer.prototype.constructor = h.jqplot.DateAxisRenderer;
    h.jqplot.DateTickFormatter = function (n, o) {
        if (!n)
        {
            n = "%Y/%m/%d"
        }
        return h.jsDate.strftime(o, n)
    };
    h.jqplot.DateAxisRenderer.prototype.init = function (E) {
        this.tickOptions.formatter = h.jqplot.DateTickFormatter;
        this.tickInset = 0;
        this.drawBaseline = true;
        this.baselineWidth = null;
        this.baselineColor = null;
        this.daTickInterval = null;
        this._daTickInterval = null;
        h.extend(true, this, E);
        var C = this._dataBounds, u, x, D, y, A, z, o;
        for (var t = 0; t < this._series.length; t++)
        {
            u = {
                intervals: [],
                frequencies: {},
                sortedIntervals: [],
                min: null,
                max: null,
                mean: null
            };
            x = 0;
            D = this._series[t];
            y = D.data;
            A = D._plotData;
            z = D._stackData;
            o = 0;
            for (var r = 0; r < y.length; r++)
            {
                if (this.name == "xaxis" || this.name == "x2axis")
                {
                    y[r][0] = new h.jsDate(y[r][0]).getTime();
                    A[r][0] = new h.jsDate(y[r][0]).getTime();
                    z[r][0] = new h.jsDate(y[r][0]).getTime();
                    if ((y[r][0] != null && y[r][0] < C.min) || C.min == null)
                    {
                        C.min = y[r][0]
                    }
                    if ((y[r][0] != null && y[r][0] > C.max) || C.max == null)
                    {
                        C.max = y[r][0]
                    }
                    if (r > 0)
                    {
                        o = Math.abs(y[r][0] - y[r - 1][0]);
                        u.intervals.push(o);
                        if (u.frequencies.hasOwnProperty(o))
                        {
                            u.frequencies[o] += 1
                        }
                        else
                        {
                            u.frequencies[o] = 1
                        }
                    }
                    x += o
                }
                else
                {
                    y[r][1] = new h.jsDate(y[r][1]).getTime();
                    A[r][1] = new h.jsDate(y[r][1]).getTime();
                    z[r][1] = new h.jsDate(y[r][1]).getTime();
                    if ((y[r][1] != null && y[r][1] < C.min) || C.min == null)
                    {
                        C.min = y[r][1]
                    }
                    if ((y[r][1] != null && y[r][1] > C.max) || C.max == null)
                    {
                        C.max = y[r][1]
                    }
                    if (r > 0)
                    {
                        o = Math.abs(y[r][1] - y[r - 1][1]);
                        u.intervals.push(o);
                        if (u.frequencies.hasOwnProperty(o))
                        {
                            u.frequencies[o] += 1
                        }
                        else
                        {
                            u.frequencies[o] = 1
                        }
                    }
                }
                x += o
            }
            if (D.renderer.bands)
            {
                if (D.renderer.bands.hiData.length)
                {
                    var w = D.renderer.bands.hiData;
                    for (var r = 0, q = w.length; r < q; r++)
                    {
                        if (this.name === "xaxis" || this.name === "x2axis")
                        {
                            w[r][0] = new h.jsDate(w[r][0]).getTime();
                            if ((w[r][0] != null && w[r][0] > C.max)
                                || C.max == null)
                            {
                                C.max = w[r][0]
                            }
                        }
                        else
                        {
                            w[r][1] = new h.jsDate(w[r][1]).getTime();
                            if ((w[r][1] != null && w[r][1] > C.max)
                                || C.max == null)
                            {
                                C.max = w[r][1]
                            }
                        }
                    }
                }
                if (D.renderer.bands.lowData.length)
                {
                    var w = D.renderer.bands.lowData;
                    for (var r = 0, q = w.length; r < q; r++)
                    {
                        if (this.name === "xaxis" || this.name === "x2axis")
                        {
                            w[r][0] = new h.jsDate(w[r][0]).getTime();
                            if ((w[r][0] != null && w[r][0] < C.min)
                                || C.min == null)
                            {
                                C.min = w[r][0]
                            }
                        }
                        else
                        {
                            w[r][1] = new h.jsDate(w[r][1]).getTime();
                            if ((w[r][1] != null && w[r][1] < C.min)
                                || C.min == null)
                            {
                                C.min = w[r][1]
                            }
                        }
                    }
                }
            }
            var B = 0, v = 0;
            for (var p in u.frequencies)
            {
                u.sortedIntervals.push({
                    interval: p,
                    frequency: u.frequencies[p]
                })
            }
            u.sortedIntervals.sort(function (s, n) {
                return n.frequency - s.frequency
            });
            u.min = h.jqplot.arrayMin(u.intervals);
            u.max = h.jqplot.arrayMax(u.intervals);
            u.mean = x / y.length;
            this._intervalStats.push(u);
            u = x = D = y = A = z = null
        }
        C = null
    };
    h.jqplot.DateAxisRenderer.prototype.reset = function () {
        this.min = this._options.min;
        this.max = this._options.max;
        this.tickInterval = this._options.tickInterval;
        this.numberTicks = this._options.numberTicks;
        this._autoFormatString = "";
        if (this._overrideFormatString && this.tickOptions
            && this.tickOptions.formatString)
        {
            this.tickOptions.formatString = ""
        }
        this.daTickInterval = this._daTickInterval
    };
    h.jqplot.DateAxisRenderer.prototype.createTicks = function (p) {
        var W = this._ticks;
        var L = this.ticks;
        var F = this.name;
        var H = this._dataBounds;
        var M = this._intervalStats;
        var n = (this.name.charAt(0) === "x") ? this._plotDimensions.width
            : this._plotDimensions.height;
        var w;
        var ad, J;
        var y, x;
        var ac, Z;
        var s = 30;
        var O = 1;
        var v = this.tickInterval;
        ad = ((this.min != null) ? new h.jsDate(this.min).getTime() : H.min);
        J = ((this.max != null) ? new h.jsDate(this.max).getTime() : H.max);
        var A = p.plugins.cursor;
        if (A && A._zoom && A._zoom.zooming)
        {
            this.min = null;
            this.max = null
        }
        var B = J - ad;
        if (this.tickOptions == null || !this.tickOptions.formatString)
        {
            this._overrideFormatString = true
        }
        if (L.length)
        {
            for (Z = 0; Z < L.length; Z++)
            {
                var P = L[Z];
                var X = new this.tickRenderer(this.tickOptions);
                if (P.constructor == Array)
                {
                    X.value = new h.jsDate(P[0]).getTime();
                    X.label = P[1];
                    if (!this.showTicks)
                    {
                        X.showLabel = false;
                        X.showMark = false
                    }
                    else
                    {
                        if (!this.showTickMarks)
                        {
                            X.showMark = false
                        }
                    }
                    X.setTick(X.value, this.name);
                    this._ticks.push(X)
                }
                else
                {
                    X.value = new h.jsDate(P).getTime();
                    if (!this.showTicks)
                    {
                        X.showLabel = false;
                        X.showMark = false
                    }
                    else
                    {
                        if (!this.showTickMarks)
                        {
                            X.showMark = false
                        }
                    }
                    X.setTick(X.value, this.name);
                    this._ticks.push(X)
                }
            }
            this.numberTicks = L.length;
            this.min = this._ticks[0].value;
            this.max = this._ticks[this.numberTicks - 1].value;
            this.daTickInterval = [
                (this.max - this.min) / (this.numberTicks - 1) / 1000,
                "seconds"]
        }
        else
        {
            if (this.min == null && this.max == null && H.min == H.max)
            {
                var E = h.extend(true, {}, this.tickOptions, {
                    name: this.name,
                    value: null
                });
                var S = 300000;
                this.min = H.min - S;
                this.max = H.max + S;
                this.numberTicks = 3;
                for (var Z = this.min; Z <= this.max; Z += S)
                {
                    E.value = Z;
                    var X = new this.tickRenderer(E);
                    if (this._overrideFormatString
                        && this._autoFormatString != "")
                    {
                        X.formatString = this._autoFormatString
                    }
                    X.showLabel = false;
                    X.showMark = false;
                    this._ticks.push(X)
                }
                if (this.showTicks)
                {
                    this._ticks[1].showLabel = true
                }
                if (this.showTickMarks)
                {
                    this._ticks[1].showTickMarks = true
                }
            }
            else
            {
                if (this.min == null && this.max == null)
                {
                    var N = h.extend(true, {}, this.tickOptions, {
                        name: this.name,
                        value: null
                    });
                    var ab, I;
                    if (!this.tickInterval && !this.numberTicks)
                    {
                        var R = Math.max(n, s + 1);
                        var Y = 115;
                        if (this.tickRenderer === h.jqplot.CanvasAxisTickRenderer
                            && this.tickOptions.angle)
                        {
                            Y = 115 - 40 * Math
                                .abs(Math.sin(this.tickOptions.angle / 180
                                    * Math.PI))
                        }
                        ab = Math.ceil((R - s) / Y + 1);
                        I = (J - ad) / (ab - 1)
                    }
                    else
                    {
                        if (this.tickInterval)
                        {
                            I = this.tickInterval
                        }
                        else
                        {
                            if (this.numberTicks)
                            {
                                ab = this.numberTicks;
                                I = (J - ad) / (ab - 1)
                            }
                        }
                    }
                    if (I <= 19 * l)
                    {
                        var Q = a(ad, J, I);
                        var r = Q[0];
                        this._autoFormatString = Q[1];
                        ad = Math.floor(ad / r) * r;
                        ad = new h.jsDate(ad);
                        ad = ad.getTime() + ad.getUtcOffset();
                        ab = Math.ceil((J - ad) / r) + 1;
                        this.min = ad;
                        this.max = ad + (ab - 1) * r;
                        if (this.max < J)
                        {
                            this.max += r;
                            ab += 1
                        }
                        this.tickInterval = r;
                        this.numberTicks = ab;
                        for (var Z = 0; Z < ab; Z++)
                        {
                            N.value = this.min + Z * r;
                            X = new this.tickRenderer(N);
                            if (this._overrideFormatString
                                && this._autoFormatString != "")
                            {
                                X.formatString = this._autoFormatString
                            }
                            if (!this.showTicks)
                            {
                                X.showLabel = false;
                                X.showMark = false
                            }
                            else
                            {
                                if (!this.showTickMarks)
                                {
                                    X.showMark = false
                                }
                            }
                            this._ticks.push(X)
                        }
                        O = this.tickInterval
                    }
                    else
                    {
                        if (I <= 9 * j)
                        {
                            this._autoFormatString = "%v";
                            var D = Math.round(I / j);
                            if (D < 1)
                            {
                                D = 1
                            }
                            else
                            {
                                if (D > 6)
                                {
                                    D = 6
                                }
                            }
                            var U = new h.jsDate(ad).setDate(1).setHours(0, 0,
                                0, 0);
                            var q = new h.jsDate(J);
                            var z = new h.jsDate(J).setDate(1).setHours(0, 0,
                                0, 0);
                            if (q.getTime() !== z.getTime())
                            {
                                z = z.add(1, "month")
                            }
                            var T = z.diff(U, "month");
                            ab = Math.ceil(T / D) + 1;
                            this.min = U.getTime();
                            this.max = U.clone().add((ab - 1) * D, "month")
                                .getTime();
                            this.numberTicks = ab;
                            for (var Z = 0; Z < ab; Z++)
                            {
                                if (Z === 0)
                                {
                                    N.value = U.getTime()
                                }
                                else
                                {
                                    N.value = U.add(D, "month").getTime()
                                }
                                X = new this.tickRenderer(N);
                                if (this._overrideFormatString
                                    && this._autoFormatString != "")
                                {
                                    X.formatString = this._autoFormatString
                                }
                                if (!this.showTicks)
                                {
                                    X.showLabel = false;
                                    X.showMark = false
                                }
                                else
                                {
                                    if (!this.showTickMarks)
                                    {
                                        X.showMark = false
                                    }
                                }
                                this._ticks.push(X)
                            }
                            O = D * j
                        }
                        else
                        {
                            this._autoFormatString = "%v";
                            var D = Math.round(I / k);
                            if (D < 1)
                            {
                                D = 1
                            }
                            var U = new h.jsDate(ad).setMonth(0, 1).setHours(0,
                                0, 0, 0);
                            var z = new h.jsDate(J).add(1, "year").setMonth(0,
                                1).setHours(0, 0, 0, 0);
                            var K = z.diff(U, "year");
                            ab = Math.ceil(K / D) + 1;
                            this.min = U.getTime();
                            this.max = U.clone().add((ab - 1) * D, "year")
                                .getTime();
                            this.numberTicks = ab;
                            for (var Z = 0; Z < ab; Z++)
                            {
                                if (Z === 0)
                                {
                                    N.value = U.getTime()
                                }
                                else
                                {
                                    N.value = U.add(D, "year").getTime()
                                }
                                X = new this.tickRenderer(N);
                                if (this._overrideFormatString
                                    && this._autoFormatString != "")
                                {
                                    X.formatString = this._autoFormatString
                                }
                                if (!this.showTicks)
                                {
                                    X.showLabel = false;
                                    X.showMark = false
                                }
                                else
                                {
                                    if (!this.showTickMarks)
                                    {
                                        X.showMark = false
                                    }
                                }
                                this._ticks.push(X)
                            }
                            O = D * k
                        }
                    }
                }
                else
                {
                    if (F == "xaxis" || F == "x2axis")
                    {
                        n = this._plotDimensions.width
                    }
                    else
                    {
                        n = this._plotDimensions.height
                    }
                    if (this.min != null && this.max != null
                        && this.numberTicks != null)
                    {
                        this.tickInterval = null
                    }
                    if (this.tickInterval != null)
                    {
                        if (Number(this.tickInterval))
                        {
                            this.daTickInterval = [Number(this.tickInterval),
                                "seconds"]
                        }
                        else
                        {
                            if (typeof this.tickInterval == "string")
                            {
                                var aa = this.tickInterval.split(" ");
                                if (aa.length == 1)
                                {
                                    this.daTickInterval = [1, aa[0]]
                                }
                                else
                                {
                                    if (aa.length == 2)
                                    {
                                        this.daTickInterval = [aa[0], aa[1]]
                                    }
                                }
                            }
                        }
                    }
                    if (ad == J)
                    {
                        var o = 24 * 60 * 60 * 500;
                        ad -= o;
                        J += o
                    }
                    B = J - ad;
                    var G = 2 + parseInt(Math.max(0, n - 100) / 100, 10);
                    var V, C;
                    V = (this.min != null) ? new h.jsDate(this.min).getTime()
                        : ad - B / 2 * (this.padMin - 1);
                    C = (this.max != null) ? new h.jsDate(this.max).getTime()
                        : J + B / 2 * (this.padMax - 1);
                    this.min = V;
                    this.max = C;
                    B = this.max - this.min;
                    if (this.numberTicks == null)
                    {
                        if (this.daTickInterval != null)
                        {
                            var u = new h.jsDate(this.max).diff(this.min,
                                this.daTickInterval[1], true);
                            this.numberTicks = Math.ceil(u
                                / this.daTickInterval[0]) + 1;
                            this.max = new h.jsDate(this.min).add(
                                (this.numberTicks - 1)
                                * this.daTickInterval[0],
                                this.daTickInterval[1]).getTime()
                        }
                        else
                        {
                            if (n > 200)
                            {
                                this.numberTicks = parseInt(
                                    3 + (n - 200) / 100, 10)
                            }
                            else
                            {
                                this.numberTicks = 2
                            }
                        }
                    }
                    O = B / (this.numberTicks - 1) / 1000;
                    if (this.daTickInterval == null)
                    {
                        this.daTickInterval = [O, "seconds"]
                    }
                    for (var Z = 0; Z < this.numberTicks; Z++)
                    {
                        var ad = new h.jsDate(this.min);
                        ac = ad.add(Z * this.daTickInterval[0],
                            this.daTickInterval[1]).getTime();
                        var X = new this.tickRenderer(this.tickOptions);
                        if (!this.showTicks)
                        {
                            X.showLabel = false;
                            X.showMark = false
                        }
                        else
                        {
                            if (!this.showTickMarks)
                            {
                                X.showMark = false
                            }
                        }
                        X.setTick(ac, this.name);
                        this._ticks.push(X)
                    }
                }
            }
        }
        if (this.tickInset)
        {
            this.min = this.min - this.tickInset * O;
            this.max = this.max + this.tickInset * O
        }
        if (this._daTickInterval == null)
        {
            this._daTickInterval = this.daTickInterval
        }
        W = null
    }
})(jQuery);
(function (c) {
    c.jqplot.PointLabels = function (e) {
        this.show = c.jqplot.config.enablePlugins;
        this.location = "n";
        this.labelsFromSeries = false;
        this.seriesLabelIndex = null;
        this.labels = [];
        this._labels = [];
        this.stackedValue = false;
        this.ypadding = 6;
        this.xpadding = 6;
        this.escapeHTML = true;
        this.edgeTolerance = -5;
        this.formatter = c.jqplot.DefaultTickFormatter;
        this.formatString = "";
        this.hideZeros = false;
        this._elems = [];
        c.extend(true, this, e)
    };
    var a = ["nw", "n", "ne", "e", "se", "s", "sw", "w"];
    var d = {
        nw: 0,
        n: 1,
        ne: 2,
        e: 3,
        se: 4,
        s: 5,
        sw: 6,
        w: 7
    };
    var b = ["se", "s", "sw", "w", "nw", "n", "ne", "e"];
    c.jqplot.PointLabels.init = function (j, h, f, g, i) {
        var e = c.extend(true, {}, f, g);
        e.pointLabels = e.pointLabels || {};
        if (this.renderer.constructor === c.jqplot.BarRenderer
            && this.barDirection === "horizontal"
            && !e.pointLabels.location)
        {
            e.pointLabels.location = "e"
        }
        this.plugins.pointLabels = new c.jqplot.PointLabels(e.pointLabels);
        this.plugins.pointLabels.setLabels.call(this)
    };
    c.jqplot.PointLabels.prototype.setLabels = function () {
        var f = this.plugins.pointLabels;
        var h;
        if (f.seriesLabelIndex != null)
        {
            h = f.seriesLabelIndex
        }
        else
        {
            if (this.renderer.constructor === c.jqplot.BarRenderer
                && this.barDirection === "horizontal")
            {
                h = 0
            }
            else
            {
                h = (this._plotData.length === 0) ? 0
                    : this._plotData[0].length - 1
            }
        }
        f._labels = [];
        if (f.labels.length === 0 || f.labelsFromSeries)
        {
            if (f.stackedValue)
            {
                if (this._plotData.length && this._plotData[0].length)
                {
                    for (var e = 0; e < this._plotData.length; e++)
                    {
                        f._labels.push(this._plotData[e][h])
                    }
                }
            }
            else
            {
                var g = this._plotData;
                if (this.renderer.constructor === c.jqplot.BarRenderer
                    && this.waterfall)
                {
                    g = this._data
                }
                if (g.length && g[0].length)
                {
                    for (var e = 0; e < g.length; e++)
                    {
                        f._labels.push(g[e][h])
                    }
                }
                g = null
            }
        }
        else
        {
            if (f.labels.length)
            {
                f._labels = f.labels
            }
        }
    };
    c.jqplot.PointLabels.prototype.xOffset = function (f, e, g) {
        e = e || this.location;
        g = g || this.xpadding;
        var h;
        switch (e)
        {
            case "nw":
                h = -f.outerWidth(true) - this.xpadding;
                break;
            case "n":
                h = -f.outerWidth(true) / 2;
                break;
            case "ne":
                h = this.xpadding;
                break;
            case "e":
                h = this.xpadding;
                break;
            case "se":
                h = this.xpadding;
                break;
            case "s":
                h = -f.outerWidth(true) / 2;
                break;
            case "sw":
                h = -f.outerWidth(true) - this.xpadding;
                break;
            case "w":
                h = -f.outerWidth(true) - this.xpadding;
                break;
            default:
                h = -f.outerWidth(true) - this.xpadding;
                break
        }
        return h
    };
    c.jqplot.PointLabels.prototype.yOffset = function (f, e, g) {
        e = e || this.location;
        g = g || this.xpadding;
        var h;
        switch (e)
        {
            case "nw":
                h = -f.outerHeight(true) - this.ypadding;
                break;
            case "n":
                h = -f.outerHeight(true) - this.ypadding;
                break;
            case "ne":
                h = -f.outerHeight(true) - this.ypadding;
                break;
            case "e":
                h = -f.outerHeight(true) / 2;
                break;
            case "se":
                h = this.ypadding;
                break;
            case "s":
                h = this.ypadding;
                break;
            case "sw":
                h = this.ypadding;
                break;
            case "w":
                h = -f.outerHeight(true) / 2;
                break;
            default:
                h = -f.outerHeight(true) - this.ypadding;
                break
        }
        return h
    };
    c.jqplot.PointLabels.draw = function (x, j, v) {
        var t = this.plugins.pointLabels;
        t.setLabels.call(this);
        for (var w = 0; w < t._elems.length; w++)
        {
            t._elems[w].emptyForce()
        }
        t._elems.splice(0, t._elems.length);
        if (t.show)
        {
            var r = "_" + this._stackAxis + "axis";
            if (!t.formatString)
            {
                t.formatString = this[r]._ticks[0].formatString;
                t.formatter = this[r]._ticks[0].formatter
            }
            var D = this._plotData;
            var A = this._xaxis;
            var q = this._yaxis;
            var z, f;
            for (var w = 0, u = t._labels.length; w < u; w++)
            {
                var o = t._labels[w];
                if (t.hideZeros && parseInt(t._labels[w], 10) == 0)
                {
                    o = ""
                }
                if (o != null)
                {
                    o = t.formatter(t.formatString, o)
                }
                f = document.createElement("div");
                t._elems[w] = c(f);
                z = t._elems[w];
                z.addClass("jqplot-point-label jqplot-series-" + this.index
                    + " jqplot-point-" + w);
                z.css("position", "absolute");
                z.insertAfter(x.canvas);
                if (t.escapeHTML)
                {
                    z.text(o)
                }
                else
                {
                    z.html(o)
                }
                var g = t.location;
                if ((this.fillToZero && D[w][1] < 0)
                    || (this.fillToZero && this._type === "bar"
                        && this.barDirection === "horizontal" && D[w][0] < 0)
                    || (this.waterfall && parseInt(o, 10)) < 0)
                {
                    g = b[d[g]]
                }
                var n = A.u2p(D[w][0]) + t.xOffset(z, g);
                var h = q.u2p(D[w][1]) + t.yOffset(z, g);
                if (this.renderer.constructor == c.jqplot.BarRenderer)
                {
                    if (this.barDirection == "vertical")
                    {
                        n += this._barNudge
                    }
                    else
                    {
                        h -= this._barNudge
                    }
                }
                z.css("left", n);
                z.css("top", h);
                var k = n + z.width();
                var s = h + z.height();
                var C = t.edgeTolerance;
                var e = c(x.canvas).position().left;
                var y = c(x.canvas).position().top;
                var B = x.canvas.width + e;
                var m = x.canvas.height + y;
                if (n - C < e || h - C < y || k + C > B || s + C > m)
                {
                    z.remove()
                }
                z = null;
                f = null
            }
        }
    };
    c.jqplot.postSeriesInitHooks.push(c.jqplot.PointLabels.init);
    c.jqplot.postDrawSeriesHooks.push(c.jqplot.PointLabels.draw)
})(jQuery);
PrimeFaces.widget.ChartUtils = {
    CONFIGURATORS: {
        pie: {
            configure: function (a) {
                a.cfg.seriesDefaults = {
                    shadow: a.cfg.shadow,
                    renderer: $.jqplot.PieRenderer,
                    rendererOptions: {
                        fill: a.cfg.fill,
                        diameter: a.cfg.diameter,
                        sliceMargin: a.cfg.sliceMargin,
                        showDataLabels: a.cfg.showDataLabels,
                        dataLabels: a.cfg.dataFormat || "percent",
                        highlightMouseOver: a.cfg.highlightMouseOver,
                        dataLabelFormatString: a.cfg.dataLabelFormatString,
                        dataLabelThreshold: a.cfg.dataLabelThreshold || 3
                    }
                };
                if (a.cfg.datatip)
                {
                    a.cfg.highlighter.useAxesFormatters = false
                }
            }
        },
        line: {
            configure: function (a) {
                a.cfg.axesDefaults = {
                    labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
                    tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                    tickOptions: {
                        enableFontSupport: !PrimeFaces.isIE(8)
                    }
                };
                a.cfg.seriesDefaults = {
                    shadow: a.cfg.shadow,
                    breakOnNull: a.cfg.breakOnNull,
                    pointLabels: {
                        show: a.cfg.showPointLabels ? true : false
                    },
                    rendererOptions: {
                        highlightMouseOver: a.cfg.highlightMouseOver
                    }
                };
                if (a.cfg.stackSeries
                    && a.cfg.axes.xaxis.renderer !== $.jqplot.DateAxisRenderer)
                {
                    PrimeFaces.widget.ChartUtils.transformStackedData(a)
                }
            }
        },
        bar: {
            configure: function (a) {
                a.cfg.axesDefaults = {
                    labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
                    tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                    tickOptions: {
                        enableFontSupport: !PrimeFaces.isIE(8)
                    }
                };
                a.cfg.seriesDefaults = {
                    shadow: a.cfg.shadow,
                    renderer: $.jqplot.BarRenderer,
                    rendererOptions: {
                        barDirection: a.cfg.orientation,
                        barPadding: a.cfg.barPadding,
                        barMargin: a.cfg.barMargin,
                        barWidth: a.cfg.barWidth,
                        highlightMouseOver: a.cfg.highlightMouseOver
                    },
                    fillToZero: true,
                    pointLabels: {
                        show: a.cfg.showPointLabels ? true : false
                    }
                };
                if (a.cfg.orientation === "horizontal")
                {
                    a.cfg.axes.yaxis.ticks = a.cfg.ticks
                }
                else
                {
                    if (a.cfg.axes.xaxis.renderer === $.jqplot.DateAxisRenderer)
                    {
                        PrimeFaces.widget.ChartUtils.transformDateData(a)
                    }
                    else
                    {
                        a.cfg.axes.xaxis.ticks = a.cfg.ticks
                    }
                }
            }
        },
        ohlc: {
            configure: function (a) {
                a.cfg.axesDefaults = {
                    labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
                    tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                    tickOptions: {
                        enableFontSupport: !PrimeFaces.isIE(8)
                    }
                };
                a.cfg.seriesDefaults = {
                    shadow: a.cfg.shadow,
                    renderer: $.jqplot.OHLCRenderer,
                    rendererOptions: {
                        candleStick: a.cfg.candleStick,
                        highlightMouseOver: a.cfg.highlightMouseOver
                    }
                }
            }
        },
        donut: {
            configure: function (a) {
                a.cfg.seriesDefaults = {
                    shadow: a.cfg.shadow,
                    renderer: $.jqplot.DonutRenderer,
                    rendererOptions: {
                        fill: a.cfg.fill,
                        diameter: a.cfg.diameter,
                        sliceMargin: a.cfg.sliceMargin,
                        showDataLabels: a.cfg.showDataLabels,
                        dataLabels: a.cfg.dataFormat || "percent",
                        highlightMouseOver: a.cfg.highlightMouseOver,
                        dataLabelFormatString: a.cfg.dataLabelFormatString,
                        dataLabelThreshold: a.cfg.dataLabelThreshold || 3
                    }
                };
                if (a.cfg.datatip)
                {
                    a.cfg.highlighter.useAxesFormatters = false
                }
            }
        },
        bubble: {
            configure: function (a) {
                a.cfg.axesDefaults = {
                    labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
                    tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                    tickOptions: {
                        enableFontSupport: !PrimeFaces.isIE(8)
                    }
                };
                a.cfg.seriesDefaults = {
                    shadow: a.cfg.shadow,
                    renderer: $.jqplot.BubbleRenderer,
                    rendererOptions: {
                        showLabels: a.cfg.showLabels,
                        bubbleGradients: a.cfg.bubbleGradients,
                        bubbleAlpha: a.cfg.bubbleAlpha,
                        highlightMouseOver: a.cfg.highlightMouseOver
                    }
                }
            }
        },
        metergauge: {
            configure: function (a) {
                a.cfg.seriesDefaults = {
                    shadow: a.cfg.shadow,
                    renderer: $.jqplot.MeterGaugeRenderer,
                    rendererOptions: {
                        intervals: a.cfg.intervals,
                        intervalColors: a.cfg.seriesColors,
                        label: a.cfg.gaugeLabel,
                        labelPosition: a.cfg.gaugeLabelPosition,
                        showTickLabels: a.cfg.showTickLabels,
                        ticks: a.cfg.ticks,
                        labelHeightAdjust: a.cfg.labelHeightAdjust,
                        intervalInnerRadius: a.cfg.intervalInnerRadius,
                        intervalOuterRadius: a.cfg.intervalOuterRadius,
                        min: a.cfg.min,
                        max: a.cfg.max
                    }
                };
                a.cfg.replotMode = "reinit"
            }
        }
    },
    transformStackedData: function (d) {
        var f = d.cfg.data, e = [];
        for (var c = 0; c < f.length; c++)
        {
            var b = f[c];
            for (var a = 0; a < b.length; a++)
            {
                e[a] = b[a][0];
                b[a] = b[a][1]
            }
        }
        d.cfg.axes.xaxis.ticks = e
    },
    transformDateData: function (d) {
        var f = d.cfg.data, e = d.cfg.ticks;
        for (var b = 0; b < f.length; b++)
        {
            for (var a = 0; a < f[b].length; a++)
            {
                var c = new Array(2);
                c[0] = e[a];
                c[1] = f[b][a];
                f[b][a] = c
            }
        }
        d.cfg.data = f
    }
};
PrimeFaces.widget.Chart = PrimeFaces.widget.DeferredWidget
    .extend({
        init: function (a) {
            this._super(a);
            this.jqpid = this.id.replace(/:/g, "\\:");
            this.configure();
            if (this.cfg.extender)
            {
                this.cfg.extender.call(this)
            }
            this.renderDeferred()
        },
        refresh: function (a) {
            if (this.plot)
            {
                this.plot.destroy()
            }
            this.init(a)
        },
        _render: function () {
            if (PrimeFaces.env.isCanvasSupported())
            {
                this._draw()
            }
            else
            {
                var a = this;
                $.getScript(PrimeFaces.getFacesResource(
                    "excanvas/excanvas.js", "primefaces"), function () {
                    a._draw()
                })
            }
        },
        _draw: function () {
            this.bindItemSelect();
            this.plot = $.jqplot(this.jqpid, this.cfg.data, this.cfg);
            this.adjustLegendTable();
            if (this.cfg.responsive)
            {
                this.makeResponsive()
            }
        },
        makeResponsive: function () {
            var b = this, a = "resize." + this.id;
            this.cfg.resetAxesOnResize = (this.cfg.resetAxesOnResize === false) ? false
                : true;
            $(window).off(a).on(a, function () {
                if (b.jq.is(":visible"))
                {
                    var c = {
                        resetAxes: b.cfg.resetAxesOnResize
                    };
                    if (b.cfg.replotMode)
                    {
                        c.replotMode = b.cfg.replotMode
                    }
                    b.plot.replot(c)
                }
            })
        },
        adjustLegendTable: function () {
            var f = this.jq.find("table.jqplot-table-legend"), e = f
                .find("tr.jqplot-table-legend");
            if (e.size() > 1)
            {
                var a = f.find("tr.jqplot-table-legend:first"), b = f
                    .find("tr.jqplot-table-legend:last"), d = a
                        .children("td").size()
                    - b.children("td").size();
                for (var c = 0; c < d; c++)
                {
                    b.append("<td></td>")
                }
            }
        },
        configure: function () {
            if (this.cfg.legendPosition)
            {
                this.cfg.legend = {
                    renderer: $.jqplot.EnhancedLegendRenderer,
                    show: true,
                    location: this.cfg.legendPosition,
                    placement: this.cfg.legendPlacement,
                    rendererOptions: {
                        numberRows: this.cfg.legendRows || 0,
                        numberColumns: this.cfg.legendCols || 0
                    }
                }
            }
            if (this.cfg.zoom)
            {
                this.cfg.cursor = {
                    show: true,
                    zoom: true,
                    showTooltip: false
                }
            }
            else
            {
                this.cfg.cursor = {
                    show: false
                }
            }
            if (this.cfg.datatip)
            {
                this.cfg.highlighter = {
                    show: true,
                    formatString: this.cfg.datatipFormat,
                    tooltipContentEditor: this.cfg.datatipEditor
                }
            }
            else
            {
                this.cfg.highlighter = {
                    show: false
                }
            }
            PrimeFaces.widget.ChartUtils.CONFIGURATORS[this.cfg.type]
                .configure(this)
        },
        exportAsImage: function () {
            return this.jq.jqplotToImageElem()
        },
        bindItemSelect: function () {
            var a = this;
            $(this.jqId).bind("jqplotClick", function (e, c, g, f) {
                if (f && a.cfg.behaviors)
                {
                    var b = a.cfg.behaviors.itemSelect;
                    if (b)
                    {
                        var d = {
                            params: [{
                                name: "itemIndex",
                                value: f.pointIndex
                            }, {
                                name: "seriesIndex",
                                value: f.seriesIndex
                            }]
                        };
                        b.call(a, d)
                    }
                }
            })
        },
        resetZoom: function () {
            this.plot.resetZoom()
        }
    });