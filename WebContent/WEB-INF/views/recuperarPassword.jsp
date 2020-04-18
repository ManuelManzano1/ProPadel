<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html lang="en">
<html lang="en">
<head>
	<title>Login V16</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<style><%@include file="../resources/fonts/font-awesome-4.7.0/css/font-awesome.min.css"%></style>
	<style><%@include file="../resources/fonts/Linearicons-Free-v1.0.0/icon-font.min.css"%></style>
	<style><%@include file="../resources/vendor/animate/animate.css"%></style>
	<style><%@include file="../resources/vendor/css-hamburgers/hamburgers.min.css"%></style>
	<style><%@include file="../resources/vendor/animsition/css/animsition.min.css"%></style>
	<style><%@include file="../resources/vendor/select2/select2.min.css"%></style>
	<style><%@include file="../resources/vendor/daterangepicker/daterangepicker.css"%></style>
	<style><%@include file="../resources/css/util.css"%></style>
	<style><%@include file="../resources/css/main.css"%></style>
<!--===============================================================================================-->
</head>
<body>
	
	<div class="limiter">
		<div class="container-login100">
			<div class="wrap-login100 p-t-30 p-b-50">
				<span class="login100-form-title p-b-41">
					Recuperar Contraseña
				</span>
				<form:form class="login100-form" method="post" action="comprobarMail">
				<c:if test="${existe>0}">
					<div class="wrap-input100">
						<h3>Error el email introducido no esta registrado</h3>
					</div>
				</c:if>
					<div class="wrap-input100-r">
						<input class="input100-r" type="text" name="email" placeholder="Email">
						<span class="focus-input100"></span>
					</div>

					<div class="wrap-input100-r">
						<input class="input100-r" type="password" name="clave" placeholder="Nueva Contraseña">
						<span class="focus-input100"></span>
					</div>

					<div class="container-login100-form-btn m-t-32">
						<input type="submit" class="login100-form-btn" value="Aceptar"/>
					</div>
				</form:form>
				<br />
			</div>
		</div>
	</div>
	

	<div id="dropDownSelect1"></div>
	
<!--===============================================================================================-->
	<script src="../resources/vendor/jquery/jquery-3.2.1.min.js"></script>
<!--===============================================================================================-->
	<script src="../resources/vendor/animsition/js/animsition.min.js"></script>
<!--===============================================================================================-->
	<script src="../resources/vendor/bootstrap/js/popper.js"></script>
	<script src="../resources/vendor/bootstrap/js/bootstrap.min.js"></script>
<!--===============================================================================================-->
	<script src="../resources/vendor/select2/select2.min.js"></script>
<!--===============================================================================================-->
	<script src="../resources/vendor/daterangepicker/moment.min.js"></script>
	<script src="../resources/vendor/daterangepicker/daterangepicker.js"></script>
<!--===============================================================================================-->
	<script src="../resources/vendor/countdowntime/countdowntime.js"></script>
<!--===============================================================================================-->
	<script src="../resources/js/main.js"></script>

</body>
</html>