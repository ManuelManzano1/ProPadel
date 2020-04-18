<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
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
	<script type="text/javascript">
	function deshabilitaRetroceso(){
	    window.location.hash="no-back-button";
	    window.location.hash="Again-No-back-button" //chrome
	    window.onhashchange=function(){window.location.hash="no-back-button";}
	}
	</script>
</head>
<body onload="deshabilitaRetroceso()">
	
	<div class="limiter">
		<div class="container-login100">
			<div class="wrap-login100 p-t-30 p-b-50">
				<span class="login100-form-title p-b-41">
					Login
				</span>
				<form:form class="login100-form" method="post" action="comprobarLogin">
				<c:if test="${vacio>0}">
					<div class="wrap-input100">
						<h3>Login incorrecto</h3>
					</div>
				</c:if>
					<div class="wrap-input100">
						<input class="input100" type="text" name="usuario" placeholder="Nombre de usuario">
						<span class="focus-input100" data-placeholder="&#xe82a;"></span>
					</div>

					<div class="wrap-input100 ">
						<input class="input100" type="password" name="clave" placeholder="Contraseña">
						<span class="focus-input100" data-placeholder="&#xe80f;"></span>
					</div>

					<div class="container-login100-form-btn m-t-32">
						<input type="submit" class="login100-form-btn-r" value="Acceder"/>
					</div>
					<div class="container-login100-form-btn m-t-32">
						<a href= "registro" class="login100-form-btn-r">Registro</a>
					</div>
				</form:form>
				<br />
				<div class="login100-form-link">
					<a href= "comprobarClave" class="pass">He olvidado mi contraseña</a>
				</div>
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