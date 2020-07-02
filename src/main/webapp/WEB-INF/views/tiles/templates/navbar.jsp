<%@ include file="./taglibs.jsp" %>

<nav class="navbar navbar-expand-sm navbar-dark bg-dark mb-3">
  <a id="inside-spl-brand" class="navbar-brand" href="/">
    <span class="font-weight-light">Inside</span><span class="font-weight-bold">SPL</span>
  </a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbarSupportedContent">
  	<c:if test="${ sessionScope.selectedSplId != null }">
	  	<ul class="navbar-nav mr-auto">
	      <li class="nav-item">
	        <a class="nav-link" href="/products"><spring:message code="views.productView"></spring:message></a>
	      </li>
	      <li class="nav-item">
	        <a class="nav-link" href="/features"><spring:message code="views.featureModel"></spring:message></a>
	      </li>
	      <li class="nav-item">
	        <a class="nav-link" href="/cmaps"><spring:message code="domain.cmap.cmaps"></spring:message></a>
	      </li>
	    </ul>
  	</c:if>
    
    <ul class="navbar-nav ml-auto">
    	<li class="nav-item dropdown dropdown-right">
	        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
	          <spring:message code="lang"></spring:message> (${pageContext.response.locale})
	        </a>
	        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
		        <c:choose>
		        	<c:when test="${pageContext.response.locale == 'en'}">
		        		<a class="dropdown-item" href="?lang=eu"><spring:message code="lang.eu"></spring:message></a>
		          		<a class="dropdown-item" href="?lang=es"><spring:message code="lang.es"></spring:message></a>
		        	</c:when>
		        	<c:when test="${pageContext.response.locale == 'es'}">
		        		<a class="dropdown-item" href="?lang=eu"><spring:message code="lang.eu"></spring:message></a>
		          		<a class="dropdown-item" href="?lang=en"><spring:message code="lang.en"></spring:message></a>
		        	</c:when>
		        	<c:otherwise>
		        		<a class="dropdown-item" href="?lang=en"><spring:message code="lang.en"></spring:message></a>
		          		<a class="dropdown-item" href="?lang=es"><spring:message code="lang.es"></spring:message></a>
		        	</c:otherwise>
		        </c:choose>
	        </div>
	      </li>
    </ul>
  </div>
</nav>