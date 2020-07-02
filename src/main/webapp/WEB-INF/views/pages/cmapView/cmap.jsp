<%@ include file="../../tiles/templates/taglibs.jsp"%>

<!-- GoJS -->
<script src="/static/js/go/go.js"></script>
<script src="/static/js/go-graphics.js"></script>

<span class="big-title" id="cmap-name"><c:out
		value="${ cmap.getName() }"></c:out></span>
<hr>
<div class="row no-gutters">
	<div id="cmap-graphic" class="d-sm-block col"
		style="background-color: white; width: 100%; height: 450px"></div>
	<div id="click-info" class="d-none col-12 col-sm-6 col-md-4 col-lg-3">
		<div class="scrollable-2">
			<i onclick="hideInfo()" style="cursor: pointer;"
				class="fas fa-long-arrow-alt-left fa-lg"></i>
			<div class="big-title" id="info-node-name"></div>
			<hr>
			<div id="info-node-info"></div>
		</div>
	</div>
</div>

<!-- click-info js -->
<script>

	var resString = getString("views.cmaps.resources");
	var incString = getString("views.cmaps.incoming");
	var outString = getString("views.cmaps.outgoing");
	var featString = getString("views.cmaps.features");

  function showInfo(name,info){
    if($('#click-info').hasClass('d-none')){
      $('#click-info').removeClass('d-none')
      $('#cmap-graphic').addClass('d-none');
    }

    updateInfo(name,info);
  }

  function hideInfo(){
    $('#click-info').addClass('d-none')
    $('#cmap-graphic').removeClass('d-none');
  }

  function updateInfo(name,info){
    $('#info-node-name').html(name)
    $('#info-node-info').html(info)
  }

  function updateInfoByKey(key){
      var part = cmapGraph.findNodeForKey(key)

      // If element is Node
      if(part){
        var nodeData = part.data;
        var nodeName = nodeData.text;

        showInfo(nodeName,getNodeInfo(part))
      } 
      else{

        var link;

        cmapGraph.links.each(function(e){
          if(e.data.key == key){
            link = e;
          }
        });
        
     	// Element is link
        if(link){
          var from = getNode(link.data.from);
          var to = getNode(link.data.to);

          showInfo(getConnectionString(from,to,link),getLinkInfo(link));
        }
      }
    }
  
  function getConnectionString(from,to,link){
    return "<span class='concept' onclick=\"updateInfoByKey('" + from.key + "')\">" + from.text +  "</span> <span class='linking-phrase'>" + link.data.text.replace("\n"," ") +  "</span> <span class='concept' onclick=\"updateInfoByKey('" + to.key + "')\">" + to.text + "</span>"

  }

  function addResourcesOnDiv(part,div){
    // Resources
    $resources = $("<ul></ul>");
    var resA = getResourcesByKey(part.data.key);

    $.each(resA,function(i,r){
      var rs = "<a href='" + r.url + "' target='_blank'>"  + r.label + "</a><br>" + r.description;
      $resources.append("<li>" + rs + "</li>");
    })

    if(resA.length > 0){
      div.append("<b>" + resString + " (" + resA.length + ")</b>");
      div.append($resources)
    }

  }

  function addRelatedFeaturesOnDiv(part,div,addDivisor){
    // Related features
    $relatedFeatures = $("<ul></ul>");
    var relF = getFeaturesByKey(part.data.key);

    $.each(relF,function(i,r){
      var rf = "<a href='/features/" + r.feature_id + "' target='_blank'>"  + r.feature_name + "</a>";
      $relatedFeatures.append("<li>" + rf + "</li>");
    })

    if(relF.length > 0){
      if(addDivisor)
        div.append("<hr>");
      div.append("<b>" + featString + " (" + relF.length + ")</b>");
      div.append($relatedFeatures)
    }

  }
  
  function getLinkInfo(part){
    var key = part.data.key;

    $info = $("<div><div>");

    // Insert info
    addResourcesOnDiv(part,$info);

    addRelatedFeaturesOnDiv(part,$info,true);

    return $info;

  }

  function getNodeInfo(part){
    var key = part.data.key;

    $info = $("<div><div>");

    // Resources
    $resources = $("<ul></ul>");
    var resA = getResourcesByKey(key);

    $.each(resA,function(i,r){
      var rs = "<a href='" + r.url + "' target='_blank'>"  + r.label + "</a><br>" + r.description;
      $resources.append("<li>" + rs + "</li>");
    })

    // Incomming and outcomming relations
    $incoming = $("<ul></ul>")
    var incI = 0;
    $outcoming = $("<ul></ul>")
    var outI = 0;

    part.linksConnected.each(function(r){

      var from = getNode(r.data.from);
      var to = getNode(r.data.to);

      var str =  getConnectionString(from,to,r);
      
      if(r.data.to == key){
        // Incoming
        $incoming.append("<li>" + str +"</li>");
        incI++;
      }else{
        // Outcoming
        $outcoming.append("<li>" + str +"</li>");
        outI++;
      }
    })

    // Insert info
    addResourcesOnDiv(part,$info);

    if(incI > 0){
      $info.append("<b>" + incString + " (" + incI + ")</b>");
      $info.append($incoming)
    }

    if(outI > 0){
      $info.append("<b>" + outString + " (" + outI + ")</b>");
      $info.append($outcoming)
    }
    
    addRelatedFeaturesOnDiv(part,$info,true);

    return $info;

  }
</script>

<!-- cmaps-graphics js -->
<script>
  var cmapGraph;

  var resources = ${ resourceArray };
	  
  function getResourcesByKey(key){
    var r = [];

    $.each(resources,function(i,obj){
      if(obj.parent == key){
        r.push(obj)
      }
    })

    return r
  }
  
  var relatedFeatures = ${ featureConnArray };
  
  function getFeaturesByKey(key){
    var r = [];

    $.each(relatedFeatures,function(i,obj){
      if(obj.key == key){
        r.push(obj)
      }
    })

    return r;
  }

  $(document).ready(function(){

    var click = function cliked(e,obj){
      var part = obj.part;
      if (part instanceof go.Adornment) part = part.adornedPart;

      updateInfoByKey(part.data.key)
    }
    
    var linkClick = function linkClicked(e,obj){
      var part = obj.part;
      if (part instanceof go.Adornment) part = part.adornedPart;

      updateInfoByKey(part.data.key);
    }

    // create the model for the concept map
    var nodeDataArray = ${ nodeArray };

    var linkDataArray =  ${ linkArray };

    cmapGraph = createCMap("cmap-graphic",nodeDataArray,linkDataArray,click,linkClick);
  });
</script>





