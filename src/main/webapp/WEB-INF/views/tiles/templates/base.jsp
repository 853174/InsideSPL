<!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-spring3-3.dtd">
<%@ include file="taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	    <title>InsideSPL</title>
	    
	    <!-- IMPORTS -->
		<tiles:insertAttribute name="head"/>
	</head>
	<body>
		<!-- NAVBAR -->
		<tiles:insertAttribute name="navbar"/>
		
		<div class="container-fluid">
			<!-- CONTENT -->
			<section>
			    <tiles:insertAttribute name="body"/>
			</section>
			
			<!-- FOOTER -->
			<tiles:insertAttribute name="footer"/>
		</div>
	</body>
</html>