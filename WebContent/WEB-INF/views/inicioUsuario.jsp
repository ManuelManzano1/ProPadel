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
					<div class="col-md-3">
							<div class="header-logo">
								<a href="cargarInicio" class="logo">
									<img src="https://i.ibb.co/P6p8kyq/309fee65-116d-474b-a0d1-466806f782a9-200x200-1.png" border="0" width="100px" height="100px">
								</a>
							</div>
						</div>
						<!-- ACCOUNT -->
						<div class="col-md-3 clearfix" style="left:590px;">
							<div class="header-ctn">
								<div>
									<a href="#">
										<i class="fas fa-heart"></i>
										<span>Pistas favoritas</span>
										<div class="qty">${numFavoritas}</div>
									</a>
								</div>
								
								<div>
									<a href="#">
										<i class="fas fa-list"></i>
										<span>Mis reservas</span>
										<div class="qty">${numReservas}</div>
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
		<div class="section">
			<div class="container">
				<div class="row">
					<div class="col-md-12">
						<div class="row">
							<div class="products-tabs">
								<!-- tab -->
								<div id="tab1" class="tab-pane active">
									<div class="products-slick" data-nav="#slick-nav-1">
										<!-- product -->
										<c:forEach items="${pistas}" var="p">
										<div class="col-md-4">
										<div class="product">
											<div class="product-img">
												<img src="${p.imagen}" width="100%" height="202px">
											</div>
											<div class="product-body">
												<p class="product-category">${p.localizacion}</p>
												<h3 class="product-name"><a href="pista?pista=${p.nombre}">${p.nombre}</a></h3>
											</div>
										</div>
										</div>
										</c:forEach>
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