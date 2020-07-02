<%@ include file="../../tiles/templates/taglibs.jsp"%>

<!-- GoJS -->
<script src="/static/js/go/go.js"></script>
<script src="/static/js/go-graphics.js"></script>

<span class="big-title" id="cmap-name">
	<spring:message code="domain.cmap.cmaps"></spring:message>
</span>
<hr>

<div id="cmaps" class="row">
	<c:forEach var="cmap" items="${ cmaps }">
		<div class="col-sm-6">
		    <div class="card">
		      <div class="card-top" id='${ cmap.getId() }'>
		
		      </div>
		      <div class="card-body">
		        <h5 class="card-title">${ cmap.getName() }</h5>
		        <a id='${ cmap.getId() }-go' href="/cmaps/${ cmap.getId() }" class="card-link"><spring:message code="link.show"></spring:message></a>
		        <a href="/cmaps/${ cmap.getId() }/products" class="card-link"><spring:message code="views.cmaps.products"></spring:message></a>
		      </div>
		    </div>
		  </div>
		<script>
			var nodeDataArray = ${ cmapInfo.get( cmap.getId().concat('-nodeArray')) };
			var linkDataArray = ${ cmapInfo.get( cmap.getId().concat('-linkArray')) };
			
			cmapGraph = createCMap('${ cmap.getId() }',nodeDataArray,linkDataArray);
		</script>
	</c:forEach>
</div>