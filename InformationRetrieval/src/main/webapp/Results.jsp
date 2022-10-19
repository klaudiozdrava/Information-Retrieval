<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>


<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Search Page</title>

<link rel="stylesheet" href="Relative.css" />
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-0evHe/X+R7YkIZDRvuzKMRqM+OrBnVFBL6DOitfPri4tjfHxaWutUpFmBp4vmVor" crossorigin="anonymous">

</head>
<body>



<nav class="navbar navbar-dark bg-dark navbar-expand-sm">
  <a class="navbar-brand" href="#">
    <img src="https://s3.eu-central-1.amazonaws.com/bootstrapbaymisc/blog/24_days_bootstrap/logo_white.png" width="30" height="30" alt="logo">
    Communications of the ACM
  </a>
  <form class="form-inline" method="GET" action="IRServlet">
    <input class="form-control mr-2" type="search" name="SearchIR" placeholder="Search" style="width:500px;" aria-label="Search">
    <button class="btn btn-info" type="submit"><i class="fas fa-search"></i></button>
  </form>
  
  <div class="dropdown"  style="float:left;">
	  <button class="btn btn-warning dropdown-toggle " type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="true">
	    Similarity
	  </button>
	  <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
	    <li><a class="dropdown-item" href="<%=request.getContextPath()%>/BM25">Okapi BM25</a></li>
	    <li><a class="dropdown-item" href="<%=request.getContextPath()%>/Vectors">Vector Similarity</a></li>
	  </ul>
  </div>
   	
</nav>


<div class="container" style="margin-top:60px;"> 
	<div class="row"> 
		<div class="col-md-13"> 
			<div class="card"> 
			
				<table class="table table-bordered">
					
					<tbody>
					 	<c:set var = "i" scope = "page" value = "0"/>
						<c:forEach var="doc" items="${documents}">
							<tr>
						
								<td>
								 <button type="button" class="btn btn-light" data-toggle="modal" data-target="# ${i }">
								
								     ${doc.highlighted}
								 		
								 	</button>
								</td>  
								<td style="width: 35px;">
									<button type="button" class="btn btn-info">
										<a href="<%=request.getContextPath()%>/Similar?id=<c:out value='${doc.id}' />">
										
										<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-right-square" viewBox="0 0 16 16">
										  <path fill-rule="evenodd" d="M15 2a1 1 0 0 0-1-1H2a1 1 0 0 0-1 1v12a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V2zM0 2a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V2zm4.5 5.5a.5.5 0 0 0 0 1h5.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3a.5.5 0 0 0 0-.708l-3-3a.5.5 0 1 0-.708.708L10.293 7.5H4.5z"/>
										</svg>
																				
										</a>
									
									</button>
									
								</td>
							</tr>
							<!-- Modal -->
								<div class="modal fade" id=" ${i}" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
								  <div class="modal-dialog modal-dialog-centered" role="document">
								    <div class="modal-content">
								      <div class="modal-header">
								      	<h4 style=" border: 2px solid black;">${doc.docNumber}</h4>
								        <h5 class="modal-title" id="exampleModalLongTitle" style="margin-left:10px;">
								           <c:out value="${documents[i].title}" />
								          
								        </h5>
								        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
								          <span aria-hidden="true">&times;</span>
								        </button>
								      </div>
								      <div class="modal-body">
								         <c:out value="${documents[i].content}" />
								      </div>
								      <div class="modal-footer">
								          <c:out value="${documents[i].authors}" />
								      </div>
								    </div>
								  </div>
								</div>
							<c:set var = "i" scope = "page" value = "${i+1 }"/>
						</c:forEach>
					</tbody>
	
				</table>	
			</div> 
		</div> 
	</div>
</div>



<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>

</body>
</html>