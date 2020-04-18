<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
	<style><%@include file="../resources/fonts/font-awesome-4.7.0/css/font-awesome.min.css"%></style>
	<style><%@include file="../resources/fonts/Linearicons-Free-v1.0.0/icon-font.min.css"%></style>
	<style><%@include file="../resources/vendor/animate/animate.css"%></style>
	<style><%@include file="../resources/vendor/css-hamburgers/hamburgers.min.css"%></style>
	<style><%@include file="../resources/vendor/animsition/css/animsition.min.css"%></style>
	<style><%@include file="../resources/vendor/select2/select2.min.css"%></style>
	<style><%@include file="../resources/vendor/daterangepicker/daterangepicker.css"%></style>
	<style><%@include file="../resources/css/util.css"%></style>
	<style><%@include file="../resources/css/main.css"%></style>
</head>
<body>
	<div class="limiter">
		<div class="container-login100">
			<div class="wrap-login100 p-t-30 p-b-50">
				<span class="login100-form-title p-b-41">
					Nuevo Usuario
				</span>
				<form:form class="login100-form" method="post" action="registrarUsuario" commandName="user">
					<c:if test="${repetido>0}">
						<div class="wrap-input100">
							<h3>Error al registrar, el email o el usuario introducido ya existe</h3>
						</div>
					</c:if>
					<div class="wrap-input100-r">
						<input class="input100-r" type="text" name="usuario" placeholder="Nombre de usuario">
						<span class="focus-input100"></span>
						<div class="form-group">
							<form:errors path="usuario" cssClass="error"/>
						</div>
					</div>
					
					<div class="wrap-input100-r">
						<input class="input100-r" type="text" name="email" placeholder="Email">
						<span class="focus-input100"></span>
						<div class="form-group">
							<form:errors path="email" cssClass="error"/>
						</div>
					</div>
					<div class="wrap-input100-r">
						<input class="input100-r" type="password" name="clave" placeholder="Contraseña">
						<span class="focus-input100"></span>
						<div class="form-group">
							<form:errors path="clave" cssClass="error"/>
						</div>
					</div>
					<div class="wrap-input100-r">
						<input class="input100-r" type="text" name="nombre" placeholder="Nombre">
						<span class="focus-input100"></span>
						<div class="form-group">
							<form:errors path="nombre" cssClass="error"/>
						</div>
					</div>
					<div class="wrap-input100-r">
						<input class="input100-r" type="text" name="apellidos" placeholder="Apellidos">
						<span class="focus-input100"></span>
					</div>

					<div class="container-login100-form-btn m-t-32">
						<input type="submit" class="login100-form-btn" value="Registrarse"/>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
</html>