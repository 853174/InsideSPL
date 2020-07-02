function rootUrl(){
	window.location.protocol + "\\" + window.location.host
}

function forceClick(){
	$('#popover-navigation-prev-next').hide()
}

var tour;

$(document).ready(function(){
	
	
	
	$('.spl-link:first').ready(function(){
		
		// First SPL
		var $fstSpl = $('.spl-link:first')
		var splUrl = $fstSpl.attr('href');
		$fstSpl.attr('id','first-spl');
		
		// First CMap
		var fstCmap = $('.card-top:first');
		
		$('#graph-1').ready(function(){
			
			var prodUrl = $("#graph-1").attr('href');
			
			$('#productUrl').ready(function(){
				
				var u = $("#productUrl").attr('href');
				if(u != undefined)
					prodUrl = u;
				
				tour = new Tour({
				    name: "InsideSPL Tour",
				    smartPlacement: true,
				    storage: window.localStorage,
				    debug: true,
				    backdrop: true,
				    backdropContainer: 'body',
				    backdropPadding: 10,
				    template: '<div class="popover" role="tooltip"> <div class="arrow"></div> <h3 class="popover-header"></h3> <div class="popover-body"></div> <div class="popover-navigation"> <div class="btn-group" id="popover-navigation-prev-next"> <button class="btn btn-sm btn-outline-secondary" data-role="prev" onclick="tour.prev()">&laquo; ' + getString("link.prev.short") + '</button> <button class="btn btn-sm btn-outline-secondary" data-role="next" onclick="tour.next()">' + getString("link.next.short") + ' &raquo;</button> </div> <button class="btn btn-sm btn-outline-secondary" data-role="end" onclick="tour.end()">' + getString("tour.end") +'</button> </div> </div>',
				    redirect: true,
				    basePath: rootUrl(),
				    steps: [
				      {
				        element: "#inside-spl-brand",
				        title: getString("tour.insidespl.title"),
				        content: getString("tour.insidespl.content"),
				        path: "/"
				      },
				      {
				        element: "#spl-list",
				        title: getString("tour.spllist.title"),
				        content: getString("tour.spllist.content"),
				        path: "/"
				      },
				      {
				        element: "#first-spl",
				        content: getString("tour.firstspl.content"),
				        path: "/",
				        onShown: function(tour){
				        	forceClick()
				        }
				      },
				      {
				    	element: "#spl-views",
				    	title: getString("tour.splviews.title"),
				    	content: getString("tour.splviews.content"),
				    	path: "/"
				      },
				      {
				    	element: "#product-view-go",
				    	content: getString("tour.productviewgo.content"),
				    	path: "/",
				    	reflex: true
				      },
				      {
				    	element: "#products",
				    	title: getString("tour.products.title"),
				    	content: getString("tour.products.content"),
				    	path: "/products"
				      },
				      {
				    	element: "#filters",
				    	content: getString("tour.filters.content"),
				    	path: "/products"
				      },
				      {
				    	element: "#graph-1",
				    	title: getString("tour.firstprod.title"),
				    	content: getString("tour.firstprod.content"),
				    	reflex: true,
				    	path: "/products"
				      },
				      {
				    	element: "#detailedGraphic",
				    	title: getString("tour.detailedgraphic.title"),
				    	content: getString("tour.detailedgraphic.content"),
				    	path: prodUrl
				      },
				      {
				    	element: "#detailed-graphic-help",
				    	content: getString("tour.detailedgraphichelp.content"),
				    	path: prodUrl
				      },
				      {
				    	element: '#zoom-buttons',
				    	content: getString("tour.zoombuttons.content"),
				    	path: prodUrl
				      },
				      {
				    	element: '#toggleButton',
				    	content: getString("tour.configtogle.content"),
				    	path: prodUrl,
				    	reflex: true
				      },
				      {
				    	element: "#productConfiguration",
				    	title: getString("tour.config.title"),
				    	content: getString("tour.config.content").replace("$selectedImg",'<img src="/static/css/jstree/selected.png" width="16"></img>'),
				    	path: prodUrl,
				    	onShow: function(tour){
				    		if($("#productConfiguration").is(":hidden")){
				    			// Simulate a click to show the configuration
				    			$('#toggleButton').trigger('click');
				    		}
				    	}
				      },
				      {
				    	element: "#compare-btn",
				    	content: getString("tour.comparebtn.content"),
				    	path: "/products",
				    	reflex: true
				      },
				      {
				    	element: "#products",
				    	title: getString("tour.compare.title"),
				    	content: getString("tour.compare.content"),
				    	path: "/products",
				    	onShow: function(tour){
				    		if($('#comparation-feedback').is(':hidden')){
				    			$('#compare-btn').trigger('click');
				    		}
				    	}
				      },
				      {
				    	element: "#goto-compare",
				    	content: getString("tour.comparebtn.content2"),
				    	path: "/products",
				    	reflex: true,
				    	onShow: function(tour){
				    		
				    		if($("#goto-compare").attr('href') == "#"){
				    			console.log("prevStep!")
				    		}
				    	},
				        onShown: function(tour){
				        	forceClick()
				        }
				      },
				      {
				    	  element: "#similarity",
				    	  title: getString("tour.similarity.title"),
				    	  content: getString("tour.similarity.content"),
				    	  // path: /products/compare/{id1}/{id2}
				    	  path: RegExp("/products/compare/.*")
				      },
				      {
				    	  element: "#features-view-go",
				    	  content: getString("tour.featureviewgo.content"),
				    	  path: "/",
				    	  reflex: true
				      },
				      {
				    	  orphan: true,
				    	  title: getString("tour.featuremodels.title"),
				    	  content: getString("tour.featuremodels.content"),
				    	  path: "/features"
				      },
				      {
				    	  element: "#feature-info-link",
				    	  content: getString("tour.featurego.content"),
				    	  path: "/features",
				    	  reflex: true,
					      onShown: function(tour){
					    	  forceClick()
					      }
				      },
				      {
				    	  element: "#feature-name",
				    	  content: getString("tour.feature.content"),
				    	  path: RegExp("/features/.+")
				      },
				      {
				    	  elemnt: "#cmaps-view-go",
				    	  concent: getString("tour.cmapsviewgo.content"),
				    	  path: "/"
				      },
				      {
				    	  element: "#cmaps",
				    	  title: getString("tour.cmaps.title"),
				    	  content: getString("tour.cmaps.content"),
				    	  path: "/cmaps"
				      },
				      {
				    	  element: "#" + fstCmap.attr('id') + "-go",
				    	  content: getString("tour.firstcmapgo.content"),
				    	  path: "/cmaps",
				    	  reflex: true,
					      onShown: function(tour){
					    	  forceClick()
					      }
				      },
				      {
				    	  element: "#cmap-graphic",
				    	  content: getString("tour.cmap.content"),
				    	  path: RegExp("/cmaps/.+")
				      },
				      {
				    	  element: "#inside-spl-brand",
				    	  title: getString("tour.end.title"),
				    	  content: getString("tour.end.content"),
				    	  path: "/"
				      }
				      ],
				    onEnd: function(tour){
				    	// Go to the start
				    	window.location = "/changeSpl";
				    }
				 });
				
				// Initialize the tour
			  	tour.init();
			});
		});
	});

	$('#tour-start').click(function(){
		// Initialize the tour
	  	tour.init();
		
		// Start the tour
		tour.restart();
	});
	
});