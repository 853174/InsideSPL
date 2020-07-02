<%@ include file="../../tiles/templates/taglibs.jsp" %>

<!-- GoJS -->
<script src="/static/js/go/go.js"></script>
<script src="/static/js/go-graphics.js"></script>

<!-- Highlight.js -->
<link rel="stylesheet"
    href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/10.0.3/styles/default.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/10.0.3/highlight.min.js"></script>
<script>hljs.initHighlightingOnLoad();</script>

<!-- Tooltips -->
<script type="text/javascript">
  $(function () {
    $('[data-toggle="tooltip"]').tooltip()
  })
  </script>

<span class="big-title" id="feature-name">${ feature.getName() }</span>
<hr>

<div class="card-columns">
	<script type="text/javascript">
	function refresh(){
        if($(window).width() < 755){
          $('.card-columns').css('column-count',1);
        }else{
          $('.card-columns').css('column-count',2);
        }
      }
      
       $(document).ready(function(){
   	   		refresh();
   	   		
          	$(window).resize(function(){
            	refresh();
          	})
          	
          	$('.card-header').addClass('clickable')
          
         $('.card-header').click(function(){
           $(this).parent().find('.card-body').slideToggle();

           $icon = $(this).find('.fa-chevron-up')

           if($icon.hasClass('fa-rotate-180')){
             $icon.removeClass('fa-rotate-180')
           }else{
             $icon.addClass('fa-rotate-180')
           }
         })
       })
     </script>
     
     <div id="feature-tree-div" class="card mb-3">
     	<div class="card-header clearfix">
     		<div class="float-left">
	            <spring:message code="views.feature.immediateFTree"></spring:message>
	        </div>
	        <div class="float-right">
	            <i class="fas fa-chevron-up"></i>
	        </div>
     	</div>
     	<div class="card-body">
     	    <div id="reduced-feature-tree" style="background-color: white; width: 100%; height: 200px"></div>
     	</div>
     </div>
     
     <div id="feature-info" class="card mb-3">
     	<div class="card-header clearfix">
     		<div class="float-left">
	            <spring:message code="views.feature.information"></spring:message>
	        </div>
	        <div class="float-right">
	            <i class="fas fa-chevron-up"></i>
	        </div>
     	</div>
     	<div class="card-body">
     		<b><spring:message code="domain.feature.name"></spring:message>:</b> ${feature.getName()}<br>
     		
     		<c:set var="vtype" value="mandatory"></c:set>
			<c:set var="defaultSelected" value="yes"></c:set>
			
	        <c:if test="${ not fn:containsIgnoreCase(feature.getType(),'MANDATORY') }">
	        	<c:set var="defaultSelected" value="no"></c:set>
	       		<c:choose>
	       			<c:when test="${ fn:containsIgnoreCase(feature.getType(),'ALTERNATIVE') }">
	       				<c:set var="vtype" value="alternative"></c:set>
	       			</c:when>
	       			<c:when test="${ fn:containsIgnoreCase(feature.getType(),'OPTIONAL') }">
	       				<c:set var="vtype" value="optional"></c:set>
	       			</c:when>
	       			<c:when test="${ fn:containsIgnoreCase(feature.getType(),'OR') }">
	       				<c:set var="vtype" value="or"></c:set>
	       			</c:when>
	       			<c:otherwise>
	       				<c:set var="vtype" value="unk"></c:set>
	       			</c:otherwise>
	       		</c:choose>
	        </c:if>
	        
	        <b><spring:message code="domain.feature.type"></spring:message>:</b> <spring:message code="domain.feature.type.${ vtype }"></spring:message> <span data-toggle="tooltip" data-placement="right" data-html="true" title="<spring:message code="domain.feature.type.${ vtype }.help"></spring:message>"><i class="far fa-question-circle fa-click"></i></span><br>
	   		<b><spring:message code="domain.feature.default"></spring:message>:</b> <spring:message code="lang.basic.${ defaultSelected }"></spring:message>
	        
     	</div>
     </div>
     
     <c:if test="${ not empty feature.getAttributes() }">
     <div class="card mb-3" id="feature-attributes">
     	<div class="card-header clearfix">
     		<div class="float-left">
	            <spring:message code="domain.feature.properties"></spring:message>
	        </div>
	        <div class="float-right">
	            <i class="fas fa-chevron-up"></i>
	        </div>
     	</div>
     	<div class="card-body">
     		<c:forEach var="attr" items="${ feature.getAttributes() }">
				<b>${ attr.getName() }:</b> ${ attr.getValue() }<br>
			</c:forEach>
     	</div>
     </div>
     </c:if>
     
      <c:if test="${ not empty cmapElements }">
      <div class="card mb-3">
      	<div class="card-header clearfix">
     		<div class="float-left">
	            <spring:message code="views.cmaps"></spring:message>
	        </div>
	        <div class="float-right">
	            <i class="fas fa-chevron-up"></i>
	        </div>
     	</div>
     	<div class="card-body">
     		<div id="feature-resources">
     			<h6 class="card-title"><spring:message code="views.feature.relatedResources"></spring:message></h6>
     			<ul>
     				<c:forEach var="resource" items="${ resources }">
						<li><a target="_blank" href="${ resource.getUrl() }">${ resource.getLabel() }</a></li>
					</c:forEach>
     			</ul>
     		</div>
     		<div id="feature-concepts">
     			<h6 class="card-title"><spring:message code="views.feature.relatedFeatures"></spring:message></h6>
     			<h5 style="max-width: 300px;">
     				<c:forEach var="el" items="${ cmapElements }">
     					<c:choose>
     						<c:when test="${ fn:containsIgnoreCase(el.getClass().name,'Concept') }">
     							<span class="badge badge-primary">${ el.getLabel() }</span>
     						</c:when>
     						<c:otherwise>
     							<span class="badge badge-primary"><i>${ el.getLabel() }</i></span>
     						</c:otherwise>
     					</c:choose>
					</c:forEach>
     			</h5>
     				
     		</div>
     		
     		
     	</div>
      </div>
      </c:if>
      
</div>
      
      <c:if test="${ not empty vps }">
      <div class="card mb-3">
      	<div class="card-header clearfix">
      		<div class="float-left">
	            <spring:message code="views.feature.vps"></spring:message>
	        </div>
	        <div class="float-right">
	            <i class="fas fa-chevron-up"></i>
	        </div>
      	</div>
      	<div class="card-body">
      		<div class="row">
		        <div class="col-12 col-md-2">
		          <div class="card mb-3">
		            <div class="card-body">
		              <b><spring:message code="views.filters"></spring:message></b>
		              <div class="form-inline">
		                <div class="form-check mr-3">
		                  <input class="form-check-input" type="checkbox" value="" id="CLVP">
		                  <label class="form-check-label" for="CLVP">
		                    <spring:message code="domain.vp.codeLevel"></spring:message>
		                  </label>
		                </div>
		                <div class="form-check mr-3">
		                  <input class="form-check-input" type="checkbox" value="" id="FLVP">
		                  <label class="form-check-label" for="FLVP">
		                    <spring:message code="domain.vp.fileLevel"></spring:message>
		                  </label>
		                </div>
		                <div class="form-check mr-3">
		                  <input class="form-check-input" type="checkbox" value="" id="DLVP">
		                  <label class="form-check-label" for="DLVP">
		                    <spring:message code="domain.vp.dirLevel"></spring:message>
		                  </label>
		                </div>
		                <div class="form-check mr-3">
		                  <input class="form-check-input" type="checkbox" value="" id="PLVP">
		                  <label class="form-check-label" for="PLVP">
		                    <spring:message code="domain.vp.partLevel"></spring:message>
		                  </label>
		                </div>
		
		                <!-- form js -->
		                <script type="text/javascript">
		                  // Default: checked
		                  $('#CLVP').prop('checked',true);
		                  $('#FLVP').prop('checked',true);
		                  $('#DLVP').prop('checked',true);
		                  $('#PLVP').prop('checked',true);
		
		                  $(document).ready(function(){
		                    $('input[type=checkbox]').change(function(){
		                      var $elems = $("." + $(this).attr('id') + "-el");
		                      if($(this).is(':checked')){
		                        // Show this type's VPs
		                        $elems.fadeIn()
		                      }else{
		                        $elems.fadeOut()
		                      }
		                    })
		                  })
		                </script>
		              </div>
		            </div>
		          </div>
		        </div>
		        <div class="col-12 col-md-10">
		        	<div class="card-columns">
			       	<c:forEach var="vp" items="${ vps }">
			          	<c:choose>
			
			          		<c:when test="${ fn:containsIgnoreCase(vp.getClass().name,'Code_VariationPoint')  }">
			          			<!-- Code level VP -->
					              <div class="card CLVP-el">
					                <div class="card-body">
					                  <h5 class="card-title">${ vp.getFile().getFilename() } <small class="text-muted">[${ vp.getStartLine() }-${ vp.getEndLine() }]</small></h5>
					                  <h6><span class="badge badge-secondary"><i class="fas fa-code"></i> <spring:message code="domain.vp.codeLevel.short"></spring:message></span></h6>
					                  <pre><code>${ vp.getContent() }</code></pre>
					                </div>
					              </div>
			          		</c:when>
			          		
			   				<c:when test="${ fn:containsIgnoreCase(vp.getFile().getClass().name,'Directory') }">
			   					<!-- Directory level VP -->
					              <div class="card DLVP-el">
					                <div class="card-body">
					                  <h5 class="card-title">${ vp.getFile().getPath() }</h5>
					                  <h6><span class="badge badge-info"><i class="far fa-folder"></i> <spring:message code="domain.vp.dirLevel.short"></spring:message></span></h6>
					                  <pre><code>${ vp.getExpresion() }</code></pre>
					                </div>
					              </div>
			   				</c:when>
			   				<c:when test="${ fn:containsIgnoreCase(vp.getFile().getClass().name,'Part') }">
			   					<!-- Part level VP -->
						           <div class="card PLVP-el">
						             <div class="card-body">
						               <h5 class="card-title">${ vp.getFile().getPath() } <small class="text-muted">${ vp.getFile().getPartType() }</small></h5>
						               <h6><span class="badge badge-success"><i class="fas fa-align-right"></i> <spring:message code="domain.vp.partLevel.short"></spring:message></span></h6>
						               <pre><code>${ vp.getExpresion() }</code></pre>
						             </div>
						           </div>
			   				</c:when>
			   				<c:otherwise>
			   					<!-- File level VP -->
					              <div class="card FLVP-el">
					                <div class="card-body">
					                  <h5 class="card-title">${ vp.getFile().getFilename() }</h5>
					                  <h6><span class="badge badge-danger"><i class="far fa-file"></i> <spring:message code="domain.vp.fileLevel.short"></spring:message></span></h6>
					                  <pre><code>${ vp.getExpresion() }</code></pre>
					                </div>
					              </div>
			   				</c:otherwise>
			   			</c:choose>
				      </c:forEach>
		 
		          </div>
		        </div>
		
		
		      </div>
      	</div>
      </div>
      </c:if>

  
<script>
var featureGraph;

$(document).ready(function(){

  var click = function cliked(e,obj){
    var part = obj.part;
    if (part instanceof go.Adornment) part = part.adornedPart;

    window.location.replace("/features/" + part.data.key)
  }

  // create the model for the concept map
  var nodeDataArray = ${ nodeList };
  var linkDataArray = ${ linkList };
	  
  featureGraph = createReducedFeatureTree("reduced-feature-tree",nodeDataArray,linkDataArray,click,null)
});
</script>
