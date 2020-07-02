<%@ include file="../../tiles/templates/taglibs.jsp"%>

<!-- JSTREE -->
<link rel="stylesheet" href="/static/css/jstree/style.min.css" />
<script src="/static/js/jstree/jstree.js"></script>

<!-- Graphic generation JS -->
<script src="/static/js/productGraphics.js"></script>

<!-- This page's functions and global variables -->
<script>
	function placeGraphicOnComparation(id,name,smf,sof,tf,containerId){

	  var chart = createDoughnutChart(id,name,smf,sof,tf,25)

	  // Graphic container
	  var graphic = document.createElement("div");
	  graphic.style.height = "200px";
	  graphic.id = id;
	  graphic.dataset.name = name;

	  $('#' + containerId).append(graphic);

	  // Draw chart
	  chart.container(id);
	  chart.draw();
	}
	
	function showSimilarity(simJson){

	  // Similarity bar		
	  var sim = simJson["similarity"];
	  var diff = 100 - sim;
	  $('#similarity-result').html(sim + "%");
	  $('#similar-value').css('width',sim + "%");
	  $('#diff-value').css('width',diff + "%");
	  
	  // Hide tree togglers
      $('.jstree-ocl').hide()

	  var simFeatures = simJson["same"];
	  var diffFeatures = simJson["diff"];

	  var sameColor = "#92c45c";
	  var diffColor = "#e27171"

	  // Configuration trees
	  $('#prod-1-config').jstree().get_container().on('ready.jstree',function(e,data){
	    simFeatures.forEach(obj => {
	      $('#prod-1-config #' + obj.id + '> .jstree-wholerow').css('background',sameColor)
	    })

	    diffFeatures.forEach(obj => {
	      $('#prod-1-config #' + obj.id + '> .jstree-wholerow').css('background',diffColor)
	    })
	  });

	  $('#prod-2-config').jstree().get_container().on('ready.jstree',function(e,data){

	    simFeatures.forEach(obj => {
	      $('#prod-2-config #' + obj.id + '> .jstree-wholerow').css('background',sameColor)
	    })

	    diffFeatures.forEach(obj => {
	      $('#prod-2-config #' + obj.id + '> .jstree-wholerow').css('background',diffColor)
	    })
	  });

	}
</script>

<div class="big-title">
	<spring:message code="views.productView.comparation"></spring:message>
</div>
<hr>
<div id="comparation-result">
  <div id="product-graphics" class="row align-items-center no-gutters">
    <div class="col" align="center">
      <div id="prod-1-graphic">

      </div>
    </div>
    <div class="col-auto" align="center">
      <div class="compare-vl-1"></div>
    </div>
    <div class="col" align="center">
      <div id="prod-2-graphic">

      </div>
    </div>
  </div>
  <div id="similarity" class="text-center" style="margin-top: -10px;">
    <div id="similarity-result" class="big-title"></div>
    <div id="similarity-bar" class="progress">
     <div id="similar-value" class="progress-bar bg-success" role="progressbar"></div>
     <div id="diff-value" class="progress-bar bg-danger" role="progressbar"></div>
   </div>
  </div>
  <div id="product-configs" class="row justify-content-center no-gutters mt-3" style="margin: auto;">
    <div class="col-5">
      <div id="prod-1-config" style="overflow-x: scroll">

      </div>
    </div>
    <div class="col-1" align="center">
      <div class="compare-vl-2"></div>
    </div>
    <div class="col-5">
      <div id="prod-2-config" style="overflow-x: scroll">

      </div>
    </div>
  </div>
</div>

<!-- Comparation JS -->
<script type="text/javascript">
   $(document).ready(function(){
	   
     placeGraphicOnComparation(
    		 "${ prod1.getId() }",
    		 "${ prod1.getFilename() }",
    		 ${ info.get(prod1.getId().concat("_smf")) },
    		 ${ info.get(prod1.getId().concat("_sof")) },
    		 ${ info.get(prod1.getId().concat("_tf")) },
    		 "prod-1-graphic");
     
     placeGraphicOnComparation(
    		 "${ prod2.getId() }",
    		 "${ prod2.getFilename() }",
    		 ${ info.get(prod2.getId().concat("_smf")) },
    		 ${ info.get(prod2.getId().concat("_sof")) },
    		 ${ info.get(prod2.getId().concat("_tf")) },
    		 "prod-2-graphic");

     var prod1data = ${ prod1data };
     var prod2data = ${ prod2data };
     
     var readyFunc = function(a,b){
    	 $('.jstree-ocl').hide();
     }
    	 
     createJstree(prod1data,"prod-1-config",null,readyFunc);
     createJstree(prod2data,"prod-2-config",null,readyFunc);
     
     $('#prod-1-config').click(function(e){
    	 e.preventDefault()
     })
     
     $('#prod-2-config').click(function(e){
    	 e.preventDefault()
     })
     
     var similarityJson = ${ similarityData };

     showSimilarity(similarityJson);
     
   })
 </script>