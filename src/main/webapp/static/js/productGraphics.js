// PRODUCTS INDEX PAGE: /products

function createDoughnutChart(id,name,smf,sof,tf,fontSize){

	// create data
	  var data = [
	    {x: getString("domain.feature.mandatory"), value: smf},
	    {x: getString("domain.feature.optional"), value: sof},
	    {x: getString("domain.feature.unselected"), value: (tf - (smf + sof)), fill: "#dcdcdc"}
	  ];

	// create a pie chart and set the data
	var chart = anychart.pie(data);

	// create and configure a label
	var label = anychart.standalones.label();
	label.text((smf + sof) + "/" + tf);
	label.fontFamily("Roboto");
	label.width("100%");
	label.height("100%");
	label.fontSize(fontSize);
	label.fontWeight(100);
	label.fontColor("#000000");
	label.hAlign("center");
	label.vAlign("middle");

	chart.center().content(label);

	/*
	 * set the inner radius (to turn the pie chart into a doughnut chart)
	 */
	chart.innerRadius("80%");

	// disable labels
	chart.labels(false);

	// configure tooltips
	chart.tooltip()
		.title(false)
		.separator(false)
		.fontFamily("Roboto")
		.useHtml(true)
		.format("<b>{%x}</b><br>{%value}");

	// disable legend
	chart.legend(false);

	// configure the labels: font, overlap, offsetz
	chart.overlapMode(true);

	// adjust title
	var title = chart.title();
	title.enabled(true);
	title.text(name);
	title.fontFamily("Roboto");
	title.orientation("bottom");
	title.align("center");
	title.fontWeight(100);
	title.fontSize(fontSize - 5);

	return chart;

}

function createDoughnutChart2(id,name,hasConcepts,totalConcepts,fontSize){

	// create data
	  var data = [
	    {x: getString("domain.cmap.hasConcepts"), value: hasConcepts},
	    {x: getString("domain.cmap.notHasConcepts"), value: (totalConcepts - hasConcepts), fill: "#dcdcdc"}
	  ];

	// create a pie chart and set the data
	var chart = anychart.pie(data);

	// create and configure a label
	var label = anychart.standalones.label();
	label.text(hasConcepts + "/" + totalConcepts);
	label.fontFamily("Roboto");
	label.width("100%");
	label.height("100%");
	label.fontSize(fontSize);
	label.fontWeight(100);
	label.fontColor("#000000");
	label.hAlign("center");
	label.vAlign("middle");

	chart.center().content(label);

	/*
	 * set the inner radius (to turn the pie chart into a doughnut chart)
	 */
	chart.innerRadius("80%");

	// disable labels
	chart.labels(false);

	// configure tooltips
	chart.tooltip()
		.title(false)
		.separator(false)
		.fontFamily("Roboto")
		.useHtml(true)
		.format("<b>{%x}</b><br>{%value}");

	// disable legend
	chart.legend(false);

	// configure the labels: font, overlap, offsetz
	chart.overlapMode(true);

	// adjust title
	var title = chart.title();
	title.enabled(true);
	title.text(name);
	title.fontFamily("Roboto");
	title.orientation("bottom");
	title.align("center");
	title.fontWeight(100);
	title.fontSize(fontSize - 5);

	return chart;

}

// PRODUCT PAGE

function createSunburstChart(data, clickFunction) {

	// create a chart and set the data
	treeData = anychart.data.tree(data, "as-tree");

	chart = anychart.sunburst(treeData);

	chart.calculationMode("parent-dependent");
	// set the inner radius
	chart.innerRadius(100);
	chart.labels().useHtml(true);

	chart.selected({
		fill : ''
	})

	// configure labels
	chart.labels().fontFamily("Roboto").position("circular").hAlign('center')
			.fontWeight("bold").format("{%name}");

	// configure labels of leaves
	chart.leaves().labels().fontFamily("Roboto").position("circular").hAlign(
			'center').fontWeight("bold").format("{%name}");

	// configure tooltips
	chart.tooltip().fontFamily("Roboto").useHtml(true).format(
			"<b>{%name}</b><br>{%value}");

	// create and configure a label
	chartLabel = anychart.standalones.label();
	chartLabel.width("100%")
		.text("")
		.height("100%")
		.fontWeight(100)
		.fontColor("#000000")
		.fontSize(40)
		.hAlign("center")
		.vAlign("middle")
		.fontFamily("Roboto");

	// set the center content
	chart.center().content(chartLabel);
	chart.level(0).enabled(false); // Root level
	chart.level(1).enabled(false); // Distinct FeatureModels' level

	// set the chart title
	chart.title(false);

	chart.interactivity().selectionMode("drill-down");

	/*
	 * listen to the chartDraw event and add the drilldown path to the chart
	 * title
	 */
	chart.listen("chartDraw", function() {
		if(clickFunction != undefined){
			clickFunction();
		}
	});

	return chart;

}

function featureInfo(el){

    $('.jstree-anchor').popover('dispose')

    var $info = $('#' + el.id);

    $info.popover({
      html: true,
      content: "<a href='/features/" + el.id.replace('_anchor','') +"'>"+ getString("views.feature.moreInfo") +"</a>"
    })

    $info.popover('show')

  }

function createJstree(data, containerId, clickFunction, readyFunction) {
	var j = $('#' + containerId).jstree({
		'core' : {
			'data' : data,
			'dblclick_toggle' : false
		},

		"types" : {
			"selected" : {
				"icon" : "/static/css/jstree/selected.png",
                "a_attr" : {
                    "onmouseover" : "featureInfo(this)"
                  }
			},
			"unselected" : {
				"icon" : "/static/css/jstree/unselected.png",
                "a_attr" : {
                    "onmouseover" : "featureInfo(this)"
                  }
			},
			"default" : {
				"icon" : "/static/css/jstree/unselected.png",
                "a_attr" : {
                    "onmouseover" : "featureInfo(this)"
                  }
			}
		},
		"plugins" : [ "wholerow", "types", "sort" ]
	});

	j.on('select_node.jstree', function(e, data) {
		e.preventDefault()
		if (clickFunction != undefined) {
			clickFunction(e, data)
		}
	});

	j.on('ready.jstree', function(e, data) {
		$('#' + containerId).jstree().open_all()
		if (readyFunction != undefined) {
			readyFunction(e, data)
		}
	})
}
