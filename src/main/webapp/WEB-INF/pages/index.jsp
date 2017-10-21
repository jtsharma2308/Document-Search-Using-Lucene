<!DOCTYPE html>
<html lang="en">

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<head>

<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">

<title>Indexing and Searching Document Collections using Lucene</title>

<!-- Bootstrap core CSS -->
<link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
	crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
	integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp"
	crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
	integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
	crossorigin="anonymous"></script>

<!-- Custom styles for this template -->
<style>
body {
	padding-top: 54px;
}

@media ( min-width : 992px) {
	body {
		padding-top: 56px;
	}
}

@import "http://fonts.googleapis.com/css?family=Roboto:300,400,500,700";

.container {
	margin-top: 20px;
}

.mb20 {
	margin-bottom: 20px;
}

hgroup {
	padding-left: 15px;
	border-bottom: 1px solid #ccc;
}

hgroup h1 {
	font: 500 normal 1.625em "Roboto", Arial, Verdana, sans-serif;
	color: #2a3644;
	margin-top: 0;
	line-height: 1.15;
}

hgroup h2.lead {
	font: normal normal 1.125em "Roboto", Arial, Verdana, sans-serif;
	color: #2a3644;
	margin: 0;
	padding-bottom: 10px;
}

.search-result .thumbnail {
	border-radius: 0 !important;
}

.search-result:first-child {
	margin-top: 0 !important;
}

.search-result {
	margin-top: 20px;
}

.search-result .col-md-2 {
	border-right: 1px dotted #ccc;
	min-height: 140px;
}

.search-result ul {
	padding-left: 0 !important;
	list-style: none;
}

.search-result ul li {
	font: 400 normal .85em "Roboto", Arial, Verdana, sans-serif;
	line-height: 30px;
}

.search-result ul li i {
	padding-right: 5px;
}

.search-result .col-md-7 {
	position: relative;
}

.search-result h3 {
	font: 500 normal 1.375em "Roboto", Arial, Verdana, sans-serif;
	margin-top: 0 !important;
	margin-bottom: 10px !important;
}

.search-result h3>a, .search-result i {
	color: #248dc1 !important;
}

.search-result p {
	font: normal normal 1.125em "Roboto", Arial, Verdana, sans-serif;
}

.search-result span.plus {
	position: absolute;
	right: 0;
	top: 126px;
}

.search-result span.plus a {
	background-color: #248dc1;
	padding: 5px 5px 3px 5px;
}

.search-result span.plus a:hover {
	background-color: #414141;
}

.search-result span.plus a i {
	color: #fff !important;
}

.search-result span.border {
	display: block;
	width: 97%;
	margin: 0 15px;
	border-bottom: 1px dotted #ccc;
}

#search {
	float: right;
	margin-top: 9px;
	width: 250px;
}

.search {
	padding: 5px 0;
	width: 230px;
	height: 30px;
	position: relative;
	left: 10px;
	float: left;
	line-height: 22px;
}

.search input {
	position: absolute;
	width: 200px;
	float: Left;
	margin-left: 0px;
	-webkit-transition: all 0.7s ease-in-out;
	-moz-transition: all 0.7s ease-in-out;
	-o-transition: all 0.7s ease-in-out;
	transition: all 0.7s ease-in-out;
	height: 30px;
	line-height: 18px;
	padding: 0 2px 0 2px;
	border-radius: 1px;
}

.btn {
	height: 30px;
	position: absolute;
	right: 0;
	top: 5px;
	border-radius: 1px;
}
</style>

</head>

<body>


	<div class="container">
		<hgroup class="mb20">

			<c:choose>

				<c:when test="${fn:length(foundResults) >0}">
					<h1>Search Results</h1>
					<h2 class="lead">
						<strong class="text-danger"><cout>${fn:length(foundResults)}</cout></strong>
						file found for the search for text <strong class="text-danger"><cout>${searctText}</strong>
					</h2>
				</c:when>

				<c:otherwise>
				 <h1>Welcome to Document Search</h1>
					<c:choose>

						<c:when test="${empty fn:trim(ErrorMessage)}">
							<h2 class="lead">
								Enter the words to search. We should have search results for you.</strong>
							</h2>
						</c:when>
						<c:otherwise>
							<h2 class="lead">
								<strong class="text-danger"><cout>${ErrorMessage}</cout></strong>
								for <strong class="text-danger"><cout>${searctText}</strong>
								please enter another word to search
							</h2>

						</c:otherwise>
					</c:choose>

				</c:otherwise>
			</c:choose>
		</hgroup>

		<section class="col-xs-12 col-sm-6 col-md-12">
			<article class="search-result row">
				<div class="col-xs-12 col-sm-12 col-md-3">
					<div class="search">
						<form action="searchResults.htm" method="get">
							<input type="text" name="searchText"
								class="form-control input-sm" maxlength="64"
								placeholder="Search"  required/>
							<button type="submit" class="btn btn-primary btn-sm">Search</button>
						</form>
					</div>
				</div>
				
		
				<jsp:useBean id="date" class="java.util.Date" />

				<div class="col-xs-12 col-sm-12 col-md-2">
					<ul class="meta-search">
						<li><i class="glyphicon glyphicon-calendar"></i> <span><fmt:formatDate value="${date}" pattern="MM/dd/yyyy" />	</span></li>
						<li><i class="glyphicon glyphicon-time"></i> <span> <fmt:formatDate value="${date}" pattern="HH:mm:ss" /> </span></li>
						<li><i class="glyphicon glyphicon-tags"></i> <span>People</span></li>
					</ul>
				</div>

				<c:forEach var="data" items="${foundResults}">
					<div class="col-xs-12 col-sm-12 col-md-5"></div>
					<div class="col-xs-12 col-sm-12 col-md-7 excerpet">
						<h3>
							<a href="#" title=""><b>File Path:</b> <cout>${data.docPath}</cout></a>
						</h3>
						<h4 class="text-primary">
							<strong class="text-danger">Score:</strong>
							<cout>${data.score}</cout>
						</h4>
						<h5 class="text-primary">
							<strong class="text-danger">Highlighted Text is your Search:</strong>
						</h5>

						<c:forEach var="highlight" items="${data.highlightedText}">
							<p>
								<cout>${highlight}</cout>
								<br>
							</p>
						</c:forEach>
					</div>
					<span class="clearfix borda"></span>
				</c:forEach>
				
			</article>
		</section>
	</div>
</body>

</html>
