<!DOCTYPE HTML>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Home</title>
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet" />
<link
	href="https://fonts.googleapis.com/css?family=Raleway:200,300,400,500,700"
	rel="stylesheet" />
<link href="https://fonts.googleapis.com/css?family=Bree+Serif"
	rel="stylesheet" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.7/css/materialize.min.css" />
<link href="css/front.css" rel="stylesheet" />
</head>
<body>
	<header class="main-header" th:fragment="header">
		<div class="menubar navbar-fixed">
			<nav>
				<div class="nav-wrapper">
					<ul class="right hide-on-med-and-down">
						<li><a href="logout">Logout</a></li>
					</ul>
				</div>
			</nav>
		</div>
	</header>
	<div class="main_slide signupSlide">
		<div class="container signupBox">
			<div class="row">
				<div class="col s12 signupHeading">
					<h3 class="indigo-text">${loginMessage}</h3>
				</div>
			</div>
		</div>
	</div>
	<script src="js/jquery-2.1.4.min.js"></script>
	<script src="http://materializecss.com/bin/materialize.js"></script>
	<script src="js/page.js"></script>
</body>
</html>