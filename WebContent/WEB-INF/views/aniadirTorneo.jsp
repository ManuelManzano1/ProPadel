<%@page import="org.springframework.web.servlet.ModelAndView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
 		<!-- Google font -->
 		<link href="https://fonts.googleapis.com/css?family=Montserrat:400,500,700" rel="stylesheet">
		<!-- Bootstrap -->
		<style><%@include file="../resources/css/bootstrap.min.css"%></style>

		<!-- Slick -->
		<style><%@include file="../resources/css/slick.css"%></style>
		<style><%@include file="../resources/css/slick-theme.css"%></style>
		<!-- nouislider -->
		<style><%@include file="../resources/css/nouislider.min.css"%></style>

		<!-- Font Awesome Icon -->
		<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css" integrity="sha384-50oBUHEmvpQ+1lW4y57PTFmhCaXp0ML5d60M1M7uH2+nqUivzIebhndOJK28anvf" crossorigin="anonymous">
		<!-- Custom stlylesheet -->
		<style><%@include file="../resources/css/style.css"%></style>


    </head>
	<body>
			<!-- HEADER -->
		<header>
			<!-- TOP HEADER -->
			<div id="top-header">
				<div class="container">
					<ul class="header-links pull-left">
						<li><a href="#"><i class="fas fa-phone"></i> +021-95-51-84</a></li>
						<li><a href="#"><i class="fas fa-envelope-open-text"></i> apppropadel@gmail.com</a></li>
					</ul>
					<ul class="header-links pull-right">
						<li><a href="#"><i class="fas fa-user"></i>${sessionScope.usuario}</a></li>
						<li><a href="logout"><i class="fas fa-sign-out-alt"></i> Cerrar sesion</a></li>
					</ul>
				</div>
			</div>
			<!-- /TOP HEADER -->

			<!-- MAIN HEADER -->
			<div id="header">
				<!-- container -->
				<div class="container">
					<!-- row -->
					<div class="row">
					<div class="col-md-6">
							<div class="header-logo">
								<a href="cargarInicioAdmin" class="logo">
									<img src="https://i.ibb.co/P6p8kyq/309fee65-116d-474b-a0d1-466806f782a9-200x200-1.png" border="0" width="100px" height="100px">
								</a>
							</div>
						</div>
							<!-- ACCOUNT -->
						<div class="col-md-6">
							<div class="header-ctn">
								<div>
									<a href="listaReservasAdmin">
										<i class="fas fa-list"></i>
										<span>Acceder al calendario de reservas</span>
									</a>
								</div>
								<div>
									<a href="accederAniadirPista">
										<i class="fas fa-plus"></i>
										<span>Añadir nueva pista</span>
									</a>
								</div>
								<div>
									<a href="habilitarEliminarPista">
										<i class="fas fa-trash"></i>
										<span>Eliminar pistas<br></span>
									</a>
								</div>
								<div>
									<a href="listaTorneos">
										<i class="fas fa-trophy"></i>
										<span>Torneos</span>
									</a>
								</div>
							</div>
						</div>
						<!-- /ACCOUNT -->
						
					</div>
					<!-- row -->
				</div>
				<!-- container -->
			</div>
			<!-- /MAIN HEADER -->
		</header>
		<!-- /HEADER -->

		

		<!-- SECTION -->
		<div class="section">
			<!-- container -->
			<div class="container">
				<!-- row -->
				<div class="row">
					<!-- Product details -->
					<div class="col-md-6 col-md-offset-3">
						<form:form method="post" action="aniadirTorneo">
							<div class="form-group">
								<div class="wrap-input100">
									<c:if test="${vacio>0}">
										<h3>Alguno de los campos a rellenar está vacío</h3>
									</c:if>
									<c:if test="${formato>0}">
										<h3>El formato de la fecha no es el adecuado</h3>
									</c:if>
									<c:if test="${fecha>0}">
										<h3>La fecha no es actual</h3>
									</c:if>
								</div>
								<label for = "nombre">Nombre: </label>
								<form:input path="nombre" class="form-control"/>
							</div>
							<div class="form-group">
								<label for ="idPista">Pista:</label>
								<form:select path="idPista" class="form-control">
									<c:forEach items="${pistas}" var="p">
										<form:option value="${p.id}">${p.nombre}</form:option>
									</c:forEach>
								</form:select>
							</div>
							<div class="form-group">
								<label for ="numJugadores">Numero de jugadores:</label>
								<form:select path="numJugadores" class="form-control">
									<form:option value="8">8</form:option>
									<form:option value="16">16</form:option>
									<form:option value="32">32</form:option>
									<form:option value="64">64</form:option>
								</form:select>
							</div>
							<div class="form-group">
								<label for ="infoPremios">Info Premios:</label>
								<form:textarea path="infoPremios" class="form-control" />
							</div>
							<div class="form-group">
								<label for ="fecha">Fecha:</label>
								<form:input path="fecha" class="form-control" placeholder="dd-MM-yyyy HH:mm"/>
							</div>
							<div class="form-group">
								<label for ="inscripcion">Inscripcion:</label>
								<form:input path="inscripcion" class="form-control" />
							</div>
							<div class="form-group">
								<input type="submit" value="Añadir Torneo" class="btn btn-success"/>
							</div>
				</form:form>
					</div>
					<!-- /Product details -->
							
				</div>
				<!-- /row -->
			</div>
			<!-- /container -->
		</div>
		<!-- /SECTION -->

		<!-- jQuery Plugins -->
		<script src="../resources/js/jquery.min.js"></script>
		<script src="../resources/js/bootstrap.min.js"></script>
		<script src="../resources/js/slick.min.js"></script>
		<script src="../resources/js/nouislider.min.js"></script>
		<script src="../resources/js/jquery.zoom.min.js"></script>
		<script src="../resources/js/main2.js"></script>

	</body>
</html>