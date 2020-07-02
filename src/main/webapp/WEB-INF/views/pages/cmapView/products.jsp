<%@ include file="../../tiles/templates/taglibs.jsp"%>

<!-- Slider -->
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<link rel="stylesheet"
	href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

<!-- Graphic generation JS -->
<script src="/static/js/productGraphics.js"></script>

<!-- This page's JS functions and global variables -->
<script>
var index = 1;

function getId(){
	return $('#graphics .graphic').length + 1;
}

function updateButtons(){

	if(index == 1){
		$('#prev').addClass('disabled');
	}else{
		$('#prev').removeClass('disabled');
	}

	if(index == $('#graphics .graphic').length){
		$('#next').addClass('disabled');
	}else{
		$('#next').removeClass('disabled');
	}
}

function previous(){
	if(index > 1){
		index -= 1;

		for(i = index; i <= $('#graphics .graphic').length ; i++){
			if(! $("#graph-" + i).data('hidden'))
		          $("#graph-" + i).show();
		}

	}

	updateButtons()
}

function next(){
	if(index < $('#graphics .graphic').length){
		index += 1;

		for(i = 0; i < index; i++){
			$("#graph-" + i).hide();
		}

	}

	updateButtons()
}

function placeGraphicOnProductList(id,name,hasConcepts,totalConcepts){

	  var x = 100 * (hasConcepts / totalConcepts);
	  var size = 180 + (1.2 * x);
	  var fontSize = 20 + (0.2 * x)

	  var chart = createDoughnutChart2(id,name,hasConcepts,totalConcepts,fontSize)

	  // Main container
	  var div = document.createElement("div");
	  div.classList = "col-auto"

	  // Click behaviour -> goTo /products/id
	  var graphicClick = document.createElement("a");
	  // graphicClick.href = "/products/" + id;
	  graphicClick.id = "graph-" + getId();

	  // Info for the filter
	  graphicClick.setAttribute("data-size",hasConcepts)
	  graphicClick.setAttribute("data-hidden",false)

	  // Graphic container
	  var graphic = document.createElement("div");
	  graphic.classList = "graphic";
	  graphic.style.width = size + "px";
	  graphic.style.height = "300px";
	  graphic.id = id;
	  graphic.dataset.name = name;

	  // Add to product list
	  graphicClick.appendChild(graphic);
	  div.appendChild(graphicClick);
	  $("#graphics").append(div);

	  // Draw chart
	  chart.container(id);
	  chart.draw();
	}
</script>

<div class="big-title">
	<spring:message code="domain.product.products"></spring:message>: ${ cmap.getName() }
</div>
<hr>

<!-- FILTER -->
<div class="clearfix">
	<a class="btn btn-outline-secondary float-left" data-toggle="collapse"
		href="#collapseExample" role="button" aria-expanded="false"
		aria-controls="collapseExample"><i class="fas fa-bars mr-1"></i><spring:message code="views.filters"></spring:message></a>
</div>

<div class="collapse mt-2" id="collapseExample">
  <div class="card card-body">
    <p>
      <label for="amount"><spring:message code="domain.cmap.hasConcepts"></spring:message></label>
      <input type="text" id="amount" readonly style="border:0; color:#f6931f; font-weight:bold;">
    </p>

    <div id="slider-range"></div>
  </div>
</div>

<div id="products" class="row align-items-center no-gutters">
	<div class="col-1 col-md-1 offset-md-1" align="center">
		<div class="btn" id="prev" onclick="previous()">
			<i class="fas fa-angle-left fa-lg"></i>
		</div>
	</div>
	<div class="col col-md-8">
		<div id="graphics"
			class="row no-gutters align-items-center justify-content-center"
			style="height: 300px; overflow: hidden;">
			<div class="col-auto" id="no-graphics" style="display: none">
				<span class="big-title"><spring:message
						code="views.productView.noProduct"></spring:message></span>
			</div>
		</div>
	</div>
	<div class="col-1 col-md-1" align="center">
		<div class="btn" id="next" onclick="next()">
			<i class="fas fa-angle-right fa-lg"></i>
		</div>
	</div>
</div>

<c:forEach var="p" items="${products}">
	<script>
		$(document).ready(function () {
			// Call to productGraphics.js function 
			placeGraphicOnProductList("${ p.getId() }","${ p.getFilename() }",${ productInfo.get( p.getId().concat('-hasConcepts') ) },${ cmap.getConcepts().size() });
		});
	</script>
</c:forEach>

<!-- Page's JS -->
<script>
	$(document).ready(function(){
		$('#prev').addClass('disabled');
	});
</script>

<!-- Filter's JS -->
<script>
var opt_max = 0;

function updateSliders(){
    var min_t = $( "#slider-range" ).slider( "values", 0 )
    var max_t = $( "#slider-range" ).slider( "values", 1 )

    // Filter graphics
    var hidden = 0;
    var total_graphics = $('#graphics .graphic').length;
    for(i = 1; i <= total_graphics ; i++){

      var tsize = $('#graph-' + i).data('size')

      if( tsize < min_t || tsize > max_t){

        $('#graph-' + i).hide()
        $('#graph-' + i).data('hidden',true);
        hidden++;
      }else{
        $('#graph-' + i).show()
        $('#graph-' + i).data('hidden',false);
      }
    }

    if(hidden == total_graphics){
      $('#no-graphics').show();
    }else{
      $('#no-graphics').hide();
    }
}
  

$(document).ready(function(){
	var total = ${ cmap.getConcepts().size() };
	
	$( "#slider-range" ).slider({
	  range: true,
	  min: 0,
	  max: total,
	  values: [ 0, total ],
	  slide: function( event, ui ) {
	    // Feedback
	    $( "#amount" ).val( ui.values[ 0 ] + " - " + ui.values[ 1 ] );
	
	 	// Filters
        updateSliders()
	  }
	});
	$( "#amount" ).val( $( "#slider-range" ).slider( "values", 0 ) +
	  " - " + $( "#slider-range" ).slider( "values", 1 ) );
});
</script>
