/*
  Deps:
    - GoJS
*/

var myDiagram;

function getNode(key){
  return myDiagram.findNodeForKey(key).data
}

/* 
view : /cmaps/{id} 
*/

function createCMap(divId,nodeDataArray,linkDataArray,nodeClicked,linkClicked){
  // if (window.goSamples) goSamples();  // init for these samples -- you don't need to call this
  var g = go.GraphObject.make;  // for conciseness in defining templates

  myDiagram =
    g(go.Diagram, divId,  // must name or refer to the DIV HTML element
      {
        initialAutoScale: go.Diagram.Uniform,  // an initial automatic zoom-to-fit
        contentAlignment: go.Spot.Center,  // align document to the center of the viewport
        layout:
          g(go.ForceDirectedLayout,  // automatically spread nodes apart
            { maxIterations: 200, defaultSpringLength: 30, defaultElectricalCharge: 100 })
      });

  // define each Node's appearance
  myDiagram.nodeTemplate =
    g(go.Node, "Auto",  // the whole node panel
      {
        locationSpot: go.Spot.Center,
        click: nodeClicked
      },
      // define the node's outer shape, which will surround the TextBlock
      g(go.Shape, "Rectangle",
        { fill: g(go.Brush, "Linear", { 0: "rgb(254, 201, 0)", 1: "rgb(254, 162, 0)" }), stroke: "black" }),
      g(go.TextBlock,
        {
          font: "15px Roboto",
          textAlign: "center",
          margin: 5
        },
        new go.Binding("text", "text"))
    );

  // replace the default Link template in the linkTemplateMap
  myDiagram.linkTemplate =
    g(go.Link,  // the whole link panel
      {
        routing : go.Link.Normal,
        click: linkClicked
      },
      g(go.Shape,  // the link shape
        { stroke: "black" }),
      g(go.Shape, { toArrow: "standard", stroke: null }),
      g(go.Shape, { fromArrow: "Line", stroke: null}),
      g(go.Panel, "Auto",
        g(go.Shape,  // the label background, which becomes transparent around the edges
          {
            fill: g(go.Brush, "Radial", { 0: "rgb(240, 240, 240)", 0.3: "rgb(240, 240, 240)", 1: "rgba(240, 240, 240, 0)" }),
            stroke: null
          }),
        g(go.TextBlock,  // the label text
          {
            textAlign: "center",
            font: "10pt Roboto",
            margin: 5
          },
          new go.Binding("text", "text"))
      )
    );

  myDiagram.model = g(go.GraphLinksModel,
    {
      nodeDataArray: nodeDataArray,
      linkDataArray: linkDataArray
    });

    return myDiagram;
}

/* 
view : /features/{id}
*/

function createReducedFeatureTree(divId,nodeDataArray,linkDataArray,nodeClicked,linkClicked){
  // if (window.goSamples) goSamples();  // init for these samples -- you don't need to call this
  var g = go.GraphObject.make;  // for conciseness in defining templates

  myDiagram =
    g(go.Diagram, divId,  // must name or refer to the DIV HTML element
      {
        initialAutoScale: go.Diagram.Uniform,  // an initial automatic zoom-to-fit
        contentAlignment: go.Spot.Center,  // align document to the center of the viewport
        layout:  // create a TreeLayout for the family tree
              g(go.TreeLayout,
                { angle: 90, nodeSpacing: 10, layerSpacing: 40, layerStyle: go.TreeLayout.LayerUniform })
      });

  // define each Node's appearance
  myDiagram.nodeTemplate =
    g(go.Node, "Auto",  // the whole node panel
      {
        locationSpot: go.Spot.Center,
        click: nodeClicked
      },
      // define the node's outer shape, which will surround the TextBlock
      g(go.Shape, "Rectangle",
        { fill: g(go.Brush, "Linear", { 0: "rgb(254, 201, 0)", 1: "rgb(254, 162, 0)" }), stroke: "black" }),
      g(go.Panel, "Horizontal",
        g(go.Picture,
          {
            name: "Picture",
            desiredSize: new go.Size(15,15),
            margin: 1.5
          },
          new go.Binding("source", "type", typeFunc)),
        g(go.TextBlock,
          {
            font: "15px Roboto",
            textAlign: "center",
            margin: 5
          },
          new go.Binding("text", "text"))
        )
    );

  // replace the default Link template in the linkTemplateMap
  myDiagram.linkTemplate =
    g(go.Link,  // the whole link panel
      {
        routing : go.Link.Normal,
        click: linkClicked
      },
      g(go.Shape,  // the link shape
        { stroke: "black" }),
      g(go.Shape, { toArrow: "standard", stroke: null }),
      g(go.Shape, { fromArrow: "Line", stroke: null})
    );

  myDiagram.model = g(go.GraphLinksModel,
    {
      nodeDataArray: nodeDataArray,
      linkDataArray: linkDataArray
    });

    return myDiagram;
}

/* 
	view : /features 
*/

var myFullDiagram;
var myLocalDiagram;

function createFeatureTree(fullId,localId,nodeDataArray,linkDataArray){

    var g = go.GraphObject.make;

    myFullDiagram =
      g(go.Diagram, fullId,  // each diagram refers to its DIV HTML element by id
        {
          initialAutoScale: go.Diagram.Uniform,  // automatically scale down to show whole tree
          //maxScale: 0.25,
          contentAlignment: go.Spot.Center,  // center the tree in the viewport
          isReadOnly: true,  // don't allow user to change the diagram
          "animationManager.isEnabled": false,
          layout: g(go.TreeLayout,
            { angle: 90, sorting: go.TreeLayout.SortingAscending }),
          maxSelectionCount: 1,  // only one node may be selected at a time in each diagram
          // when the selection changes, update the myLocalDiagram view
          "ChangedSelection": showLocalOnFullClick
        });

    myLocalDiagram =  // this is very similar to the full Diagram
      g(go.Diagram, localId,
        {
          autoScale: go.Diagram.UniformToFill,
          contentAlignment: go.Spot.Center,
          isReadOnly: true,
          layout: g(go.TreeLayout,
            { angle: 90, sorting: go.TreeLayout.SortingAscending }),
          "LayoutCompleted": function(e) {
            var sel = e.diagram.selection.first();
            if (sel !== null) myLocalDiagram.scrollToRect(sel.actualBounds);
          },
          maxSelectionCount: 1,
          // when the selection changes, update the contents of the myLocalDiagram
          "ChangedSelection": showLocalOnLocalClick
        });

    // Define a node template that is shared by both diagrams
    var myNodeTemplate =
      g(go.Node, "Auto",  // the whole node panel
        {
          locationSpot: go.Spot.Center,
        },
        // define the node's outer shape, which will surround the TextBlock
        g(go.Shape, "Rectangle",
          { fill: g(go.Brush, "Linear", { 0: "rgb(254, 201, 0)", 1: "rgb(254, 162, 0)" }), stroke: "black" }),
        g(go.Panel, "Horizontal",
          g(go.Picture,
            {
              name: "Picture",
              desiredSize: new go.Size(15,15),
              margin: 1.5
            },
            new go.Binding("source", "type", typeFunc)),
          g(go.TextBlock,
            {
              font: "15px Roboto",
              textAlign: "center",
              margin: 5
            },
            new go.Binding("text", "text"))
          )
      );

    myFullDiagram.nodeTemplate = myNodeTemplate;
    myLocalDiagram.nodeTemplate = myNodeTemplate;

    // Define a basic link template, not selectable, shared by both diagrams
    var myLinkTemplate =
      g(go.Link,  // the whole link panel
        {
          routing : go.Link.Normal
        },
        g(go.Shape,  // the link shape
          { stroke: "black" }),
        g(go.Shape, { toArrow: "standard", stroke: null }),
        g(go.Shape, { fromArrow: "Line", stroke: null})
      );

    myFullDiagram.linkTemplate = myLinkTemplate;
    myLocalDiagram.linkTemplate = myLinkTemplate;

    myFullDiagram.model = new go.GraphLinksModel(nodeDataArray,linkDataArray);

    // Create a part in the background of the full diagram to highlight the selected node
    highlighter =
      g(go.Part, "Auto",
        {
          layerName: "Background",
          selectable: false,
          isInDocumentBounds: false,
          locationSpot: go.Spot.Center
        },
        g(go.Shape, "Ellipse",
          {
            fill: g(go.Brush, "Radial", { 0.0: "rgb(254, 201, 0)", 1.0: "white" }),
            stroke: null,
            desiredSize: new go.Size(200, 200)
          })
      );
    myFullDiagram.add(highlighter);

    // Start by focusing the diagrams on the node at the top of the tree.
    // Wait until the tree has been laid out before selecting the root node.
    myFullDiagram.addDiagramListener("InitialLayoutCompleted", function(e) {
      var root = function(node){
        return (node.findTreeParentNode() == null)
      }

      var nodes = myFullDiagram.nodes.filter(root);
      nodes.each(function(n){
        n.isSelected = true;
      })
      showLocalOnFullClick();
    });

    return myFullDiagram;
}

function getParentLink(parent,child){

  var link;

  myFullDiagram.links.each(function(l){
    var data = l.data;
    if(data.from == parent.data.key && data.to == child.data.key)
      link = l;
  })

  return link;
}

// Make the corresponding node in the full diagram to that selected in the local diagram selected,
// then call showLocalOnFullClick to update the local diagram.
function showLocalOnLocalClick() {
  var selectedLocal = myLocalDiagram.selection.first();
  if (selectedLocal !== null) {
    // there are two separate Nodes, one for each Diagram, but they share the same key value
    myFullDiagram.select(myFullDiagram.findPartForKey(selectedLocal.data.key));
  }
}

function showLocalOnFullClick() {
  var node = myFullDiagram.selection.first();

  if (node !== null) {
    // make sure the selected node is in the viewport
	myFullDiagram.scrollToRect(node.actualBounds);
	// move the large yellow node behind the selected node to highlight it
	highlighter.location = node.location;
	// create a new model for the local Diagram
	var model = new go.GraphLinksModel();
	// add the selected node and its children and grandchildren to the local diagram
	var nearby = node.findTreeParts(3);  // three levels of the (sub)tree
	
	// add parent and grandparent
	var parent = node.findTreeParentNode();
	
	if (parent !== null) {
	  nearby.add(parent);
	  nearby.add(getParentLink(parent,node))
	}
	// create the model using the same node data as in myFullDiagram's model
	nearby.each(function(n) {
	
	  if (n instanceof go.Node) model.addNodeData(n.data);
	  else model.addLinkData(n.data);
	});
	
	myLocalDiagram.model = model;
	// select the node at the diagram's focus
	var selectedLocal = myLocalDiagram.findPartForKey(node.data.key);
	if (selectedLocal !== null) selectedLocal.isSelected = true;
  
	goToFeature(node.data)
  }
}

function goToFeature(nodeData){
  if($('#feature-info').is(':hidden')){
    $('#feature-info').fadeIn()
  }
  $('#feature-info-link')
    .attr('href','/features/' + nodeData.key)
    .html(nodeData.text)
}

function typeFunc(type){
  return '/static/img/' + type.toLowerCase() + '.png'
}