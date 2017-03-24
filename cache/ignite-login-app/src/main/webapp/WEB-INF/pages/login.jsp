<!DOCTYPE HTML>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Login</title>
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
	<div class="main_slide signupSlide">
		<div class="container signupBox">
			<div class="row">
				<div class="col s12 signupHeading">
					<h3 class="indigo-text">Login</h3>
				</div>
				<form class="col s12 loginForm" action='login' method='POST'>
					<c:if test="${errorMessage!=null}">
						<div class='row'>
							<div class="input-field col s12">
								<span style="color: red"> ${errorMessage} </span>
							</div>
						</div>

					</c:if>
					<div class="row">
						<div class="input-field col s12">
							<input name='username' type="text" /> <label
								class="">Username</label>
						</div>
					</div>
					<div class="row">
						<div class="input-field col s12">
							<input name='password' type="password" /> <label
								class="">Password</label>
						</div>
					</div>
					<div class="row">
							<input class="waves-effect waves-light indigo btn-large"
								type="submit" name="submit" value="Submit" />
					</div>
				</form>
			</div>
		</div>
	</div>
	<script src="js/jquery-2.1.4.min.js"></script>
	<script src="http://materializecss.com/bin/materialize.js"></script>
	<script src="js/page.js"></script>


</body>
</html>