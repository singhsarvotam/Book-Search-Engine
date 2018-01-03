<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Book Search Engine</title>
<link href="bootstrap.min.css" rel="stylesheet">
<link href="1.css" rel="stylesheet" type="text/css">
<link href="2.css" rel="stylesheet" type="text/css">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script src="jquery.min.js"></script>

<script type="text/javascript">
/* $(document).ready(function() {
	$('#my_form').keydown(function() {
	var search = $("#search").val();
	
	var key = e.which;
	if (key == 13) {
	if (search == "") {
	alert("Search Query is Missing");
	}  else {
	$('#my_form').submit();
	alert("Form Submitted ...!!");
	}
	return false;
	}
	});
	});
 */
 
 $(function() {
	 $("#keywords_input").focus();
	    $("form input").keypress(function (e) {
	        if ((e.which && e.which == 13) || (e.keyCode && e.keyCode == 13)) {
	        	var search = $("#keywords_input").val();	        	
	        	if (search == "") {
	        		alert("Search Query is Missing");
	        		return false;
	        		} 	           
	            $('#my_form').submit();
	            //return false;
	        } 
	    });
	    
	    
	     $("#search_button").click(function (e) {
	    	 var search = $("#keywords_input").val();	        	
	        	if (search == "") {
	        		alert("Search Query is Missing");
	        		return false;
	        		} 	        
	            $('#my_form').submit();
	    	
	    }); 
	    
	    
	    
	});
</script>


<style type="text/css">
.imageStyle {
	background: url(books.jpg) no-repeat center center fixed;
	-webkit-background-size: cover;
	-moz-background-size: cover;
	-o-background-size: cover;
	background-size: cover;
}
</style>
</head>
<body>

	<!-- 
<h2 align="center" style="margin-top: 100px">
Book Search Engine
</h2>
 -->

	<!-- 
<table cellpadding="10" cellspacing="10" align="center">
<tr>
<td>
Search Term:
</td>
<td>
<input type="text" name="search" id="search"/>
</td>
</tr>
<tr>
<td colspan="2" align="center">
<input type="submit" value="Submit">
</td>
</tr>
</table>
-->
	<br />
	<!-- 
<form action="" id="my_form">

<div class="row">
    <div class="col-lg-4 col-lg-offset-4">
        <div class="input-group">
        
            <input type="text" class="form-control" id="search" placeholder="Enter Search Query"/> 
            <span class="input-group-btn">
                <button class="btn btn-default" type="button">Go!</button>
            </span>
        </div>
    </div>
</div>
 
<br />

</form>
-->



	<header class="header wrapper" id="header">

	<div class="col-s-16">

		<a class="logo logo--header brl" href="#"> <img
			aria-label="Book Search Home" src="goodreads.jpg">
		</a>


		<section class="header-navigation hom"> <section
			class="login-navigation" id="login-navigation">

		<h2 align="center">Book Search Engine (Based on Goodreads)</h2>



		</section> </section>



		<!-- end header-navigation -->
	</div>
	<!-- end col-s-16 --> </header>



	<div class="fix-position"></div>

	<div id="resp-search-container" class="search-box-area"></div>

	<div class="wrapper bd-txt-bg">

		<div class="h-city-main-en p-relative row"
			style="background-position: 0 0; background-image: url('books.jpg');">
			<h1 class="h-city-home-title"></h1>
			<div class="dark-mask-absolute"></div>


			<div class="logged-in-home-search" style="opacity: 0.9;">


				<div id="search_main_container" class="full_search wrapper plr20 "
					style="margin-left: 200px">
					<div id="search_bar_wrapper" class="search_bar" role="form">
						<!-- <div class="search_type left col-l-4 col-s-16 col-m-4 plr0i" >
            <div id="location_contianer" class="left" style="border-radius: 5px 0px 0px 5px;">
                <div id="location_pretext">
                <div class="l-pre-1" role="link" aria-label="Please type a location" aria-describedby="location_input_sp" tabindex="0" aria-flowto="explore-location-suggest">
                    	
                    </span> <span id="location_input_sp" class="location_input_sp">Dallas</span></div>
                    <div class="l-pre-2 hidden" style="display: none;"><span class="location_placeholder" data-icon="L">
                    <label class="hdn" id="label_search_location">Please type a location</label>
                    <input id="location_input" role="combobox" aria-labelledby="label_search_location" aria-expanded="true" aria-autocomplete="list" aria-owns="explore-location-suggest" placeholder="Please type a location"></span></div>
                </div>
               
            </div>
            <div class="clear"> </div>
        </div> -->

						<form id="my_form" method="POST" action="MainController"
							method="POST">
							<div class=" col-l-10 col-s-16 col-m-12 plr0i">
								<div id="keywords_container" class="col-s-16">
									<div id="keywords_pretext">
										<div class="k-pre-1 hidden"
											style="height: 100%; overflow: hidden; display: none;">
											<span class="search-bar-icon mr10" data-icon="ƹ"></span>

											<div class="keyword_placeholder">
												<div class="keyword_div">Search for restaurant,
													cuisine or a dish</div>
											</div>
										</div>
										<div class="k-pre-2  w100" style="display: inline-block;">
											<span class="glyphicon glyphicon-search"></span> <label
												id="label_search_res" class="hdn">Search for
												restaurant, cuisine or a dish</label> <input role="combobox"
												aria-expanded="true" aria-autocomplete="list"
												aria-owns="keywords-by" aria-labelledby="label_search_res"
												id="keywords_input" class="discover-search" name="query"
												placeholder="Search for restaurant, cuisine or a dish"
												value="">
										</div>
									</div>

								</div>
							</div>
							<div class=" col-l-2 col-s-16 col-m-2 plr0i">
								<div role="button" aria-flowto="search-start" tabindex="0"
									id="search_button" class="left ttupper btn btn--red">
									Search</div>
								<div class="hidden search-button-loading left">
									<img width="20" alt=""
										src="https://c.zmtcdn.com/images/loading-transparent.gif">
								</div>
							</div>



						</form>




						<div class="clear"></div>
					</div>

				</div>
				<div class="clear"></div>
			</div>

		</div>
	</div>

	<%String q=(String)request.getAttribute("resp"); %>
<%if(q!=null && !("".equals(q))){ %>
	<h3 align="center">Your query is: <%=q %></h3>
	<%} %>
	
	
	
<%ArrayList<String> list = (ArrayList<String>)request.getAttribute("list"); %>
<%if(list!=null && list.size()>0){ %>
	<h3 align="center">Our Search Engine Results without Query Expansion</h3>
	

	<table align="center" cellpadding="10" cellspacing="10" border="1" width="80%">
	<tr>
	<td align="center">
	Book Name
	</td>
	<td align="center">
	ISBN
	</td>
	<td align="center">
	Goodreads URL
	</td>
	</tr>
	
		<%for(int i=0;i<list.size();i++){ 
		String a[] = list.get(i).split("\n");
		String bookISBN=list.get(i).replace("\n", "");
		if(bookISBN.contains("ISBN"))
			bookISBN = bookISBN.substring(bookISBN.indexOf("ISBN")+4).split(" ")[1];
		else
			bookISBN="NO ISBN NUMBER AVAILABLE";
		if(bookISBN.contains("ISBN13")) {
			bookISBN = list.get(i).replace("\n", "").substring(list.get(i).replace("\n", "").indexOf("ISBN")+4).split(" ")[2];
		}
		String urlShort;
		if(a[0].length()>80) {
			urlShort=a[0].substring(0, 80)+"..." ;
		}
		else
			urlShort = a[0];
		%>
		
		<tr>
			<td align="center"><a href="<%=a[0]%>" target="_blank"> <%=a[1] %></a>
			</td>
		
			<td align="center"><%=bookISBN%>
			</td>
			
			<td align="center">
				<a href='<%=a[0] %>' target="_blank"><%=a[0]%></a>
			</td>
		</tr>
		<%} %>


	</table>
	<%}%>
	
	<br />
	
	<%ArrayList<String> associationList = (ArrayList<String>)request.getAttribute("associationList"); %>
<%if(associationList!=null && associationList.size()>0){ %>
	<h3 align="center">Our Search Engine Results with Query Expansion, Expanded Query: <%=request.getAttribute("expandedQueryAssoc")%></h3>
	

	<table align="center" cellpadding="10" cellspacing="10" border="1" width="80%">
	<tr>
	<td align="center">
	Book Name
	</td>
	<td align="center">
	ISBN
	</td>
	<td align="center">
	Goodreads URL
	</td>
	</tr>
	
		<%for(int i=0;i<associationList.size();i++){ 
		String a[] = associationList.get(i).split("\n");
		String bookISBN=associationList.get(i).replace("\n", "");
		if(bookISBN.contains("ISBN"))
			bookISBN = bookISBN.substring(bookISBN.indexOf("ISBN")+4).split(" ")[1];
		else
			bookISBN="NO ISBN NUMBER AVAILABLE";
		if(bookISBN.contains("ISBN13")) {
			bookISBN = associationList.get(i).replace("\n", "").substring(associationList.get(i).replace("\n", "").indexOf("ISBN")+4).split(" ")[2];
		}
		String urlShort;
		if(a[0].length()>80) {
			urlShort=a[0].substring(0, 80)+"..." ;
		}
		else
			urlShort = a[0];
		%>
		
		<tr>
			<td align="center"><a href="<%=a[0]%>" target="_blank"> <%=a[1] %></a>
			</td>
		
			<td align="center"><%=bookISBN%>
			</td>
			
			<td align="center">
				<a href='<%=a[0] %>' target="_blank"><%=a[0]%></a>
			</td>
		</tr>
		<%} %>


	</table>
	<%}%>
	
	<br />
	
	
	<%ArrayList<String> googleSearchList = (ArrayList<String>)request.getAttribute("googleSearchList"); %>
<%if(googleSearchList!=null && googleSearchList.size()>0){ %>
	<h3 align="center">Google Results</h3>
	

	<table align="center" cellpadding="10" cellspacing="10" border="1" width="80%">
	<tr>
	<td align="center">
	URL
	</td>
	<td align="center">
	Title
	</td>
	
	</tr>
	
		<%for(int i=0;i<googleSearchList.size();i++){ 		
		String a[] = googleSearchList.get(i).split(" ");
		String data="";
		for(int j=1;j<a.length;j++){
			data+=a[j]+" ";	
		}
		%>
		
		<tr>
		<td>
		<a href='<%=a[0] %>' target="_blank"><%=a[0] %></a>
		</td>
			<td><%=data %></td>
			
		</tr>
		<%} %>


	</table>
	<%}%>
	
	
	
	
	
	
	<br />
	<%ArrayList<String> bingList = (ArrayList<String>)request.getAttribute("bingList"); %>
<%if(bingList!=null && bingList.size()>0){ %>
	<h3 align="center">Bing Results</h3>
	

	<table align="center" cellpadding="10" cellspacing="10" border="1" width="80%">
	<tr>
	<td align="center">
	URL
	</td>
	<td align="center">
	Title
	</td>
	
	</tr>
	
		<%for(int i=0;i<bingList.size();i++){ 		
		String a[] = bingList.get(i).split(" ");
		String data="";
		for(int j=1;j<a.length;j++){
			data+=a[j]+" ";	
		}
		%>
		
		<tr>
		<td>
		<a href='<%=a[0] %>' target="_blank"><%=a[0] %></a>
		</td>
			<td><%=data %></td>
			
		</tr>
		<%} %>


	</table>
	<%}%>
	<br />
	
</body>
</html>