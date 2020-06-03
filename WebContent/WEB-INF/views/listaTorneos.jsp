<%@page import="org.springframework.web.servlet.ModelAndView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="ISO-8859-1">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		 <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->

		<title>APP PROPADEL</title>

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
		<script type="text/javascript">
		function deshabilitaRetroceso(){
		    window.location.hash="no-back-button";
		    window.location.hash="Again-No-back-button" //chrome
		    window.onhashchange=function(){window.location.hash="no-back-button";}
		}
		</script>

    </head>
	<body onload="deshabilitaRetroceso()">
		<!-- HEADER -->
		<header>
			<!-- TOP HEADER -->
			<div id="top-header">
				<div class="container">
					<ul class="header-links pull-left">
						<li><a href=""><i class="fas fa-phone"></i> +021-95-51-84</a></li>
						<li><a href=""><i class="fas fa-envelope-open-text"></i> apppropadel@gmail.com</a></li>
					</ul>
					<ul class="header-links pull-right">
						<li><a href="perfil?usuario=${sessionScope.usuario}"><i class="fas fa-user"></i>${sessionScope.usuario}</a></li>
						<li><a href="logout"><i class="fas fa-sign-out-alt"></i> Cerrar sesion</a></li>
					</ul>
				</div>
			</div>
			<!-- /TOP HEADER -->

			<!-- MAIN HEADER -->
			<c:if test="${admin<1}">
				<div id="header">
					<!-- container -->
					<div class="container">
						<!-- row -->
						<div class="row">
						<div class="col-md-3">
								<div class="header-logo">
									<a href="cargarInicioUsuario" class="logo">
										<img src="https://i.ibb.co/P6p8kyq/309fee65-116d-474b-a0d1-466806f782a9-200x200-1.png" border="0" width="100px" height="100px">
									</a>
								</div>
							</div>
							<!-- ACCOUNT -->
							<div class="col-md-4" style="left:490px;">
								<div class="header-ctn">
									<div>
										<a href="listaFavoritos">
											<i class="fas fa-heart"></i>
											<span>Pistas favoritas</span>
											<div class="qty">${numFavoritas}</div>
										</a>
									</div>
									<div>
										<a href="listaReservas">
											<i class="fas fa-list"></i>
											<span>Mis reservas</span>
											<div class="qty">${numReservas}</div>
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
			</c:if>
			<c:if test="${admin>0}">
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
										<span>A&ntilde;adir nueva pista</span>
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
			</c:if>
			<!-- /MAIN HEADER -->
		</header>
		<!-- /HEADER -->
		<div class="section">
			<div class="container">
				<div class="row">
					<div class="col-md-12">
						<div class="row">
							<div class="products-tabs">
								<c:if test="${admin>0}">
									<h4>La eliminacion de torneos puede llevar algunos minutos</h4>
								</c:if>	
								<!-- tab -->
								<div id="tab1" class="tab-pane active">
									<div class="products-slick" data-nav="#slick-nav-1">
										<!-- product -->
										<c:forEach items="${torneos}" var="t">
										<div class="col-md-4">
										<div class="product">
											<c:if test="${admin<1}">
												<c:if test="${t.inscrito>0}">
													<div class="infoInscripcionNo">
														<br>
														<h5><a href="eliminarInscripcion?torneo=${t.idTorneo}" class="inscritoNo">Anular inscripcion</a></h5>
														<br>
													</div>
												</c:if>
												<c:if test="${t.inscrito<1}">
													<div class="infoInscripcion">
														<br>
														<h5><a href="aniadirInscripcion?torneo=${t.idTorneo}" class="inscrito">Inscribirse en el torneo</a></h5>
														<br>
													</div>
												</c:if>
											</c:if>
											<c:if test="${admin>0}">
												<div class="infoInscripcionNo">
														<br>
														<h5><a href="eliminarTorneo?torneo=${t.idTorneo}" class="inscritoNo"><i class="fas fa-trash"></i> Eliminar Torneo</a></h5>
														<br>
													</div>
											</c:if>
											<div class="product-body">
												<c:if test="${admin<1}">
													<h3 style="color: #0dc133">${t.nombre}</h3>
												</c:if>
												<c:if test="${admin>0}">
													<h3><a href="accederTorneo?torneo=${t.idTorneo}" style="color: #0dc133">${t.nombre}</a></h3>
												</c:if>
												<h4>${t.nombrePista}</h4>
												<p class="product-category">${t.localizacion}</p>
												<h4>Fecha: ${t.fecha}</h4>
												<h4>Inscritos: ${t.numInscritos}/${t.numJugadores}</h4>
											</div>
											<div class="product-body">
												<h3>${t.infoPremios}</h3>
												<h3>Inscripcion: ${t.inscripcion}&euro;</h3>
											</div>
											
										</div>
										</div>
										</c:forEach>
										<c:if test="${admin>0}">
											<div class="col-md-4">
											<div class="product">
												
													<div class="product-body">
														<a href="aniadirTorneo">
															<i class="fas fa-plus"></i>
															<span>A&ntilde;adir torneo</span>
														</a>
													</div>
												
												</div>
											</div>
										</c:if>
										</div>
										<!-- /product -->
									</div>
									
								</div>
								<!-- /tab -->
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
					<!-- Products tab & slick -->
		<!-- jQuery Plugins -->
		<script src="../resources/js/jquery.min.js"></script>
		<script src="../resources/js/bootstrap.min.js"></script>
		<script src="../resources/js/slick.min.js"></script>
		<script src="../resources/js/nouislider.min.js"></script>
		<script src="../resources/js/jquery.zoom.min.js"></script>
		<script src="../resources/js/main2.js"></script>
	</body>
</html>