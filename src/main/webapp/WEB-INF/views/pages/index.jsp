<%@ include file="../tiles/templates/taglibs.jsp" %>
<script src="/static/js/momentjs/momentjs-locales.js"></script>

<script type="text/javascript">
	moment.locale("${pageContext.response.locale}")
</script>

<c:choose>
	<c:when test="${ selectedSpl != null }">

		<div class="alert alert-info">
			<spring:message code="domain.spl.selectedIs"></spring:message> <b>${ selectedSpl.getName() }</b>. <spring:message code="domain.spl.changeSelected"></spring:message> <a id="change-spl" href="/changeSpl" class="alert-link"><spring:message code="link.here"></spring:message></a>.
		</div>
		
		<div id="spl-views" class="card-deck">
			<!-- PRODUKTU IKUSPEGIA -->
			<div class="card">
			  <div class="card-body">
			  	<h5 class="card-title"><spring:message code="views.productView"></spring:message></h5>
			    <div class="card-text">
			    	<p><spring:message code="views.productView.description"></spring:message></p>
			    	<a id="product-view-go" href="/products" class="card-link"><spring:message code="link.show"></spring:message></a>
			    </div>
			  </div>
			</div>
			
			<!-- PRODUKTU IKUSPEGIA -->
			<div class="card">
			  <div class="card-body">
			  	<h5 class="card-title"><spring:message code="views.featureModel"></spring:message></h5>
			    <div class="card-text">
			    	<p><spring:message code="views.featureModel.description"></spring:message></p>
			    	<a id="features-view-go" href="/features" class="card-link"><spring:message code="link.show"></spring:message></a>
			    </div>
			  </div>
			</div>
			
			<!-- CONCEPT MAPS IKUSPEGIAK -->
			<c:if test="${ cmaps.size() > 0 }">
    			<div class="card">
				  <div class="card-body">
				  	<h5 class="card-title"><spring:message code="views.cmaps"></spring:message></h5>
				    <div class="card-text">
				    	<p><spring:message code="views.cmaps.description"></spring:message></p>
				    	<a id="cmaps-view-go" href="/cmaps" class="card-link"><spring:message code="link.show"></spring:message></a>
				    </div>
				  </div>
				</div>
    		</c:if>
			
			
			<!-- BESTELAKO IKUSPEGIAK -->
		</div>
				
	
	</c:when>
	<c:otherwise>
	
		<div class="alert alert-info">
			<h4 class="alert-heading"><spring:message code="tour"></spring:message> </h4>
			<p>
				<spring:message code="tour.need"></spring:message> <a id="tour-start" href="#" class="alert-link"><spring:message code="tour.begin"></spring:message></a>.
			</p>
		</div>
	
		<div id="spl-list" class="card-deck">
			<c:forEach var="spl" items="${ splList }">
				<div class="card">
				    <div class="card-body">
				      <h5 class="card-title">${ spl.getName() }</h5>
				      <div class="card-text">
				      	<hr>
				      	<p><b><spring:message code="domain.feature.features"></spring:message></b>: ${ splInfo.get(spl.getId().concat("_featureCount")) }</p>
				      	<p><b><spring:message code="domain.product.products"></spring:message></b>: ${ splInfo.get(spl.getId().concat("_productCount")) } </p>
				      	
				      	<a class="card-link spl-link" href="/${ spl.getId() }"><spring:message code="link.examine"></spring:message> </a>
				      	<c:if test="${ spl.getGitHubUrl() != null }">
				      		<a class="card-link" href="${ spl.getGitHubUrl() }"><spring:message code="link.github"></spring:message></a>
				      	</c:if>
				      </div>
				    </div>
				    <div class="card-footer">
				      <small class="text-muted"><spring:message code="views.index.lastChange"></spring:message> <span id="${ spl.getId() }_lastChange"></span></small>
				      <script>
				      	$('#${ spl.getId() }_lastChange').html(moment("${ spl.getLastChange() }","YYYY-MM-DD HH:mm:ss").fromNow())
				      </script>
				    </div>
				  </div>
			</c:forEach>
		</div>
		
	</c:otherwise>

</c:choose>