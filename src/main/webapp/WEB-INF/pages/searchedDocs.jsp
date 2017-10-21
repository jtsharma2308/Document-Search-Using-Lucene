<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h2>SearchDocs</h2>

	<b>Total Hits:</b>
	<cout>${fn:length(foundResults)}</cout>
	</br>
	</br>
	<c:forEach var="data" items="${foundResults}">
		<b>File Path:</b>
		<cout>${data.docPath}</cout>
		<br>
		<b>Score:</b>
		<cout>${data.score}</cout>
		<br>
		<b>Highlighted Text:</b>
		</br>

		<c:forEach var="highlight" items="${data.highlightedText}">
			<cout>${highlight}</cout>
			<br>
			<br>
			<br>
		</c:forEach>
	</c:forEach>
</body>
</html>