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

function placeGraphicOnProductList(id,name,smf,sof,tf){

	  var x = 100 * ((smf + sof) / tf);
	  var size = 180 + (1.2 * x);
	  var fontSize = 25 + (0.2 * x)

	  var chart = createDoughnutChart(id,name,smf,sof,tf,fontSize)

	  // Main container
	  var div = document.createElement("div");
	  div.classList = "col-auto"

	  // Click behaviour -> goTo /products/id
	  var graphicClick = document.createElement("a");
	  graphicClick.href = "/products/" + id;
	  graphicClick.id = "graph-" + getId();

	  // Info for the filter
	  graphicClick.setAttribute("data-opt-size",sof)
	  graphicClick.setAttribute("data-size",(smf + sof))
	  graphicClick.setAttribute("data-hidden",false)
	  
	  if(opt_max < sof){
	      opt_max = sof
	  }

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
	<spring:message code="domain.product.products"></spring:message>
</div>
<hr>

<!-- FILTER && COMPARATION -->
<div class="clearfix">
	<a class="btn btn-outline-secondary float-left" data-toggle="collapse"
		href="#collapseExample" role="button" aria-expanded="false"
		aria-controls="collapseExample"><i class="fas fa-bars mr-1"></i><spring:message code="views.filters"></spring:message></a>

	<div class="float-right">
		<a id="goto-compare" class="btn btn-outline-success" href="#"
			style="display: none"> <spring:message
				code="views.productView.doCompare"></spring:message>
		</a>
		<div id="compare-btn" class="btn btn-outline-primary"
			data-comparing="false">
			<spring:message code="views.productView.compare"></spring:message>
		</div>
	</div>
</div>

<div class="collapse mt-2" id="collapseExample">
  <div class="card card-body">
    <p>
      <label for="amount"><spring:message code="domain.product.size"></spring:message></label>
      <input type="text" id="amount" readonly style="border:0; color:#f6931f; font-weight:bold;">
    </p>

    <div id="slider-range"></div>
    <hr>
    <p>
      <label for="amount-2"><spring:message code="domain.feature.optional"></spring:message></label>
      <input type="text" id="amount-2" readonly style="border:0; color:#f6931f; font-weight:bold;">
    </p>

    <div id="slider-range-2"></div>
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

<div id="comparation-feedback" class="big-title mt-3 text-center"
	style="display: none">
	<hr>
	<span id="first-prod">- - - -</span> <b> vs. </b> <span
		id="second-prod">- - - -</span>
	<hr>
</div>

<c:forEach var="p" items="${products}">
	<script>
		$(document).ready(function () {
			// Call to productGraphics.js function 
			placeGraphicOnProductList("${ p.getId() }","${ p.getFilename() }",${ productInfo.get(p.getId().concat('-smf')) } ,${ productInfo.get(p.getId().concat('-sof')) },${ productInfo.get(p.getId().concat('-total')) });
		});
	</script>
	
	<c:set var="allFeatures" value="${ productInfo.get(p.getId().concat('-total')) }"></c:set>
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
    var min_o = $( "#slider-range-2" ).slider( "values", 0 )
    var max_o = $( "#slider-range-2" ).slider( "values", 1 )

    // Filter graphics
    var hidden = 0;
    var total_graphics = $('#graphics .graphic').length;
    for(i = 1; i <= total_graphics ; i++){

      var tsize = $('#graph-' + i).data('size')
      var osize = $('#graph-' + i).data('opt-size')

      if( tsize < min_t || tsize > max_t || osize < min_o || osize > max_o){

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
	var total = ${ allFeatures };
	
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
	
	$( "#slider-range-2" ).slider({
	      range: true,
	      min: 0,
	      max: opt_max,
	      values: [ 0, opt_max ],
	      slide: function( event, ui ) {
	        // Feedback
	        $( "#amount-2" ).val( ui.values[ 0 ] + " - " + ui.values[ 1 ] );

	        // Filters
	        updateSliders()

	      }
	    });
    $( "#amount-2" ).val( $( "#slider-range-2" ).slider( "values", 0 ) +
      " - " + $( "#slider-range-2" ).slider( "values", 1 ) );
});
</script>

<!-- Comparation's JS -->
<script>
$(document).ready(function(){

  $('#compare-btn').click(function(){
    if(! $('#compare-btn').data('comparing')){
      // Star comparation selection
      $('#compare-btn').removeClass('btn-outline-primary')
      $('#compare-btn').addClass('btn-outline-danger')
      $('#compare-btn').html(getString("link.cancel"))
      $('#compare-btn').data('comparing',true);
      $('#goto-compare').show()
      $('#goto-compare').addClass('disabled')
      $('#comparation-feedback').show()
    }else{
      // Cancell compare
      $('#compare-btn').addClass('btn-outline-primary')
      $('#compare-btn').removeClass('btn-outline-danger')
      $('#compare-btn').html(getString("views.productView.compare"))
      $('#compare-btn').data('comparing',false);
      $('#goto-compare').hide()
      $('#goto-compare').removeClass('disabled')
      $('#comparation-feedback').hide()

      $('.comparation-selected-1').removeClass('comparation-selected-1')
      $('.comparation-selected-1').removeClass('comparation-selected-2')
    }
  })

  $('.graphic').click(function(e) {

    // It's selecting comparation graphics
    if($('#compare-btn').data('comparing')){
      // Prevent href
      e.preventDefault()

      if($(this).data('selected')){
        // Unselect
        $(this).data('selected',false);
        if($(this).hasClass('comparation-selected-1')){
          $(this).removeClass('comparation-selected-1')
          $('#compare-btn').data('first-prod',null)
          $('#first-prod').html("- - - -")
        }

        if($(this).hasClass('comparation-selected-2')){
          $(this).removeClass('comparation-selected-2')
          $('#compare-btn').data('second-prod',null)
          $('#second-prod').html("- - - -")
        }

        // Disable comparation button
        $('#goto-compare').addClass('disabled')
        $('#goto-compare').attr('href','#')
      }else{
        // Save info
        if($('#compare-btn').data('first-prod') == undefined){
          $('#compare-btn').data('first-prod',this.id)
          $('#first-prod').html($(this).data('name'))
          $(this).addClass('comparation-selected-1')
          $(this).data('selected',true);
        }else if ($('#compare-btn').data('second-prod') == undefined) {
          $('#compare-btn').data('second-prod',this.id)
          $('#second-prod').html($(this).data('name'))
          $(this).addClass('comparation-selected-2')
          $(this).data('selected',true);
        }

        // Enable comparation button
        if($('#compare-btn').data('first-prod') != undefined && $('#compare-btn').data('second-prod') != undefined){
          $('#goto-compare').removeClass('disabled')
          $('#goto-compare').attr('href','/products/compare/' + $('#compare-btn').data('first-prod') + "/" + $('#compare-btn').data('second-prod'))
        }
      }
    }
  });
});

</script>
