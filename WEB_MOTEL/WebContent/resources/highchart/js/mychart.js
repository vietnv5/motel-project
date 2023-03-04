function drawHighStock(xhr, status, args)
{
    var data = args.data;
    var title = args.title;
    var seriesName = args.seriesName;
    $('#container')
        .highcharts('StockChart', {

            navigator: {
                adaptToUpdatedData: false,
                series: {
                    data: data
                }
            },
            scrollbar: {
                liveRedraw: false
            },
            title: {
                text: title
            },
            rangeSelector: {
                buttons: [{
                    type: 'day',
                    count: 1,
                    text: '1d'
                }, {
                    type: 'month',
                    count: 1,
                    text: '1m'
                }, {
                    type: 'year',
                    count: 1,
                    text: '1y'
                }, {
                    type: 'all',
                    text: 'All'
                }],
                inputEnabled: false, // it supports only days
                selected: 4 // all
            },
            xAxis: {
                events: {
                    afterSetExtremes: afterSetExtremes
                },
                minRange: 3600 * 1000 // one hour
            },

            yAxis: {
                floor: 0
            },
            series: [{
                name: seriesName,
                data: data,
                tooltip: {
                    valueDecimals: 2
                }
            }]
        });
}

function afterSetExtremes(e)
{

    var chart = $('#container').highcharts();

    chart.showLoading('Loading data from server...');
    $.getJSON('https://vipa.viettel.vn/getEnodeBChartService?start=' + Math.round(e.min) +
        '&end=' + Math.round(e.max) + '&callback=?', function (data) {

        chart.series[0].setData(data);
        chart.hideLoading();
    });
}

function drawHighChart(xhr, status, args)
{
    try
    {
        var categories = JSON.parse(args.categories);
        var chartData = JSON.parse(args.chartData);
        var chartId = (args.chartId);
        var chartType = (args.chartType);
        var title = {
            chartTitle: args.chartTitle,
            chartSubTitle: args.chartSubTitle,
            yAxisTitle: args.yAxisTitle,
            xAxisTitle: args.xAxisTitle
        }
        var type = {
            chartType: chartType,
            xAxisType: args.xAxisType
        }
        renderChart(chartId, type, title, chartData, categories);
    }
    catch (e)
    {
        console.error(e);
    }
}

function renderChart(divId, type, title, chartData, categories)
{
    // var options = createOption(divId, type, title, categories);
    var options;
    if (type.chartType == 'column')
    {
        options = createOptionStacked(divId, type, title, categories);
    }
    else if (type.chartType == 'StackedArea')
    {
        options = createOptionStackedArea(divId, type, title, categories);
    }
    else
    {
        options = createOption(divId, type, title, categories);
    }
    options.series = chartData;
    if (type.xAxisType == 'datetime')
    {
        options.xAxis.categories = null;
    }
    else if (type.chartType != 'column')
    {
        options.xAxis.labels.formatter = (function () {
            return this.value;
        });
    }
    var chart = new Highcharts.Chart(options);
    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });
}

function createOptionStackedArea(divId, type, title, categories)
{
    var options = {
        chart: {
            // colors: ['#058DC7', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4'],
            borderWidth: 0,
            plotBackgroundColor: 'rgba(255, 255, 255, .9)',
            plotShadow: true,
            plotBorderWidth: 1,
            renderTo: divId,
            zoomType: 'x',
            type: 'area'
        },
        exporting: {
            enabled: true
        },
        credits: {
            enabled: false
        },
        title: {
            text: title.chartTitle,
            style: {
                color: '#000',
                // fontSize: '14px',
                // fontWeight: 'bold',
                fontFamily: '"latobold", Verdana, sans-serif'
            }
        },
        subtitle: {
            text: title.chartSubTitle
        },
        xAxis: {
            categories: categories,
            tickmarkPlacement: 'on',
            title: {
                enabled: false
            },
            type: type.xAxisType
        },
        yAxis: {
            title: {
                text: title.yAxisTitle
            },
            labels: {
                formatter: function () {
                    return this.value;
                }
            },
            allowDecimals: false,
        },
        tooltip: {
            split: true,
            valueSuffix: ' hội viên'
        },
        plotOptions: {
            area: {
                stacking: 'normal',
                lineColor: '#666666',
                lineWidth: 1,
                marker: {
                    lineWidth: 1,
                    lineColor: '#666666'
                }
            }
        },
        series: [{
            name: 'Asia',
            data: [502, 635, 809, 947, 1402, 3634, 5268]
        }, {
            name: 'Africa',
            data: [106, 107, 111, 133, 221, 767, 1766]
        }, {
            name: 'Europe',
            data: [163, 203, 276, 408, 547, 729, 628]
        }, {
            name: 'America',
            data: [18, 31, 54, 156, 339, 818, 1201]
        }, {
            name: 'Oceania',
            data: [2, 2, 2, 6, 13, 30, 46]
        }]
    };
    return options;
}

function createOptionStacked(divId, type, title, categories)
{
    var options = {
        colors: ['#058DC7', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4'],
        chart: {
            // backgroundColor: {
            //     linearGradient: [0, 0, 500, 500],
            //     stops: [
            //         [0, 'rgb(255, 255, 255)'],
            //         [1, 'rgb(240, 240, 255)']
            //     ]
            // },
            borderWidth: 0,
            plotBackgroundColor: 'rgba(255, 255, 255, .9)',
            plotShadow: true,
            plotBorderWidth: 1,
            type: 'column',
            renderTo: divId,
            zoomType: 'x'
        },
        exporting: {
            enabled: true
        },
        credits: {
            enabled: false
        },
        title: {
            text: title.chartTitle,
            style: {
                color: '#000',
                fontSize: '14px',
                fontWeight: 'bold',
                fontFamily: 'latobold'
                // font: 'bold 16px "Trebuchet MS", Verdana, sans-serif'
            }
        },
        subtitle: {
            text: title.chartSubTitle,
            style: {
                color: '#666666',
                fontSize: '13px',
                fontFamily: 'latobold'
                // font: 'bold 12px "Trebuchet MS", Verdana, sans-serif'
            }
        },
        xAxis: {
            title: {
                text: title.xAxisTitle
            },
            categories: categories
        },
        yAxis: {
            min: 0,
            title: {
                text: title.yAxisTitle
            },
            allowDecimals: false,
        },
        // legend: {
        //     align: 'right',
        //     x: -30,
        //     verticalAlign: 'top',
        //     y: 25,
        //     floating: true,
        //     backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
        //     borderColor: '#CCC',
        //     borderWidth: 1,
        //     shadow: false
        // },
        tooltip: {
            headerFormat: '<b>{point.x}</b><br/>',
            pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
        },
        plotOptions: {
            column: {
                stacking: 'normal',
                dataLabels: {
                    enabled: true,
                    color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
                }
            }
        },
        series: []
    };
    return options;

}

function createOption(divId, type, title, categories)
{
    var options = {
        colors: ['#058DC7', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4'],
        chart: {
            backgroundColor: {
                linearGradient: [0, 0, 500, 500],
                stops: [
                    [0, 'rgb(255, 255, 255)'],
                    [1, 'rgb(240, 240, 255)']
                ]
            },
            borderWidth: 0,
            plotBackgroundColor: 'rgba(255, 255, 255, .9)',
            plotShadow: true,
            plotBorderWidth: 1,
            renderTo: divId,
            type: type.chartType,
            zoomType: 'x'

        },
        exporting: {
            enabled: false
        },
        credits: {
            enabled: false
        },
        title: {
            text: title.chartTitle,
            style: {
                color: '#000',
                fontSize: '14px',
                fontWeight: 'bold'
                // font: 'bold 16px "Trebuchet MS", Verdana, sans-serif'
            }
        },
        subtitle: {
            text: title.chartSubTitle,
            style: {
                color: '#666666',
                fontSize: '13px',
                // font: 'bold 12px "Trebuchet MS", Verdana, sans-serif'
            }
        },
        xAxis: {
            ridLineWidth: 1,
            lineColor: '#000',
            tickColor: '#000',
            categories: categories,
            labels: {
                style: {
                    color: '#000',
                    fontSize: '12px',
                    // font: '11px Trebuchet MS, Verdana, sans-serif'
                },

            },
            title: {
                style: {
                    color: '#333',
                    fontWeight: 'bold',
                    fontSize: '12px',
                    // fontFamily: 'Trebuchet MS, Verdana, sans-serif'

                },
                text: title.xAxisTitle
            },
            type: type.xAxisType
        },
        yAxis: {
            minorTickInterval: 'auto',
            lineColor: '#000',
            lineWidth: 1,
            tickWidth: 1,
            tickColor: '#000',
            title: {
                style: {
                    color: '#333',
                    fontWeight: 'bold',
                    fontSize: '12px',
                    // fontFamily: 'Trebuchet MS, Verdana, sans-serif'
                },
                text: title.yAxisTitle
            },
            labels: {
                formatter: function () {
                    return this.value;
                },
                style: {
                    color: '#000',
                    fontSize: '11px'
                    // font: '11px Trebuchet MS, Verdana, sans-serif'
                }
            }
        },
        plotOptions: {

            area: {
                fillColor: {
                    linearGradient: {
                        x1: 0,
                        y1: 0,
                        x2: 0,
                        y2: 1
                    },
                    stops: [
                        [
                            0,
                            Highcharts.getOptions().colors[0]],
                        [
                            1,
                            Highcharts
                                .Color(
                                    Highcharts
                                        .getOptions().colors[0])
                                .setOpacity(0).get(
                                'rgba')]]
                },
                marker: {
                    radius: 2
                },
                lineWidth: 1,
                states: {
                    hover: {
                        lineWidth: 1
                    }
                },
                threshold: null
            }
            /*
            area: {
                stacking: 'normal',
                lineColor: '#666666',
                lineWidth: 1,
                marker: {
                    lineWidth: 1,
                    lineColor: '#666666'
                }
            }*/
        },
        // tooltip: {
        //     formatter: function() {
        //         return ''+ this.x +': '+ Highcharts.numberFormat(this.y, 0, ',') +'';
        //     }
        // },
        legend: {
            itemStyle: {
                font: '9pt Trebuchet MS, Verdana, sans-serif',
                color: 'black'

            },
            itemHoverStyle: {
                color: '#039'
            },
            itemHiddenStyle: {
                color: 'gray'
            }
        },
        labels: {
            style: {
                color: '#99b'
            }
        },
        series: []
    };

    return options;
};