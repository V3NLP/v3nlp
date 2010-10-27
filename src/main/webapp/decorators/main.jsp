<%@ page import="javax.servlet.http.*" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!-- ==========================================================	-->
<!--	Created by Devit Schizoper                          	-->
<!--	Created HomePages http://LoadFoo.starzonewebhost.com   	-->
<!--	Created Day 01.12.2006                              	-->
<!-- ========================================================== -->
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="content-type" content="text/html;charset=utf-8" />
	<meta name="author" content="University of Utah - Bioinformatics Department" />
	<meta name="description" content="iNlp application home page. " />
	<meta name="keywords" content="key, words" />
	<link rel="icon" href="<%=request.getContextPath()%>/img/magnify.ico" type="image/x-icon" /> 
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/magnify.ico" type="image/x-icon" />

	<title>iNLP - University of Utah</title>
	<link rel="stylesheet" type="text/css"
		href="<%=request.getContextPath()%>/css/style.css" />
	<link rel="stylesheet" type="text/css"
		href="<%=request.getContextPath()%>/css/style-screen.css" media="screen" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/style-print.css" type="text/css" media="print" />
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/textsizer.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/rel.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/mootools.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/slimbox.js"></script>
	<link rel="stylesheet"
		href="<%=request.getContextPath()%>/css/slimbox.css" type="text/css"
		media="screen" />
	<%!public String getCurrentMenuCssClass(HttpServletRequest request, String area) {
			StringBuffer url = request.getRequestURL();
			if (url.toString().endsWith(area + ".html")) {
				return "class='current'";
			} else if (url.toString().contains("/" + area + "/")) {
				return "class='current'";
			} else {
				return "";
			}
		}
	
		public String generateBreadCrumb(HttpServletRequest request){
			String builtUrl = request.getContextPath();
			String url =request.getRequestURL().toString();
			String result = "";
			url = url.substring(url.indexOf(request.getContextPath()) + request.getContextPath().length());
			String[] parts = url.split("/");
			for (int i=0; i< parts.length; i++) {				
				if (parts[i].trim().length() > 0) {
					builtUrl += "/" + parts[i];
					if (parts[i].endsWith(".html")){
						if (!parts[i].equals("home.html")) {				
							if (parts[i].equals("releasenotes.html")) {
								result += "release notes"; 
							} else if (parts[i].equals("regularexpressions.html")) {
									result += "regular expressions";
			
							} else if (parts[i].equals("expressionlib.html")) {
								result += "expression lib";
							} else {
								result += parts[i].substring(0, parts[i].length() - 5);
							}
						}
						continue;
					}
					if (parts[i].equals("app")) {
						result += "<a href='" + request.getContextPath() + "' >Home</a> &gt; ";
						continue;
					} else {												
						result += "<a href='" + builtUrl + ".html'>" + parts[i] + "</a> &gt; ";
					}
				}
			}
			return result;
		}
		%>
</head>
<body>
	<div id="wrap">
		<div id="top">
			<h2><a href="<%=request.getContextPath()%>/app/home.html"
				title="Back to main application page">iNLP [v1]</a></h2>
			<div id="menu">
			<ul>
				<li><a href="<%=request.getContextPath()%>/app/home.html"
					<%=getCurrentMenuCssClass(request, "home")%>>home</a></li>
				<li><a
					href="<%=request.getContextPath()%>/app/documentation.html"
					<%=getCurrentMenuCssClass(request, "documentation")%>>documentation</a></li>
				<li><a href="<%=request.getContextPath()%>/app/expressionlib.html"
					<%=getCurrentMenuCssClass(request, "expressionlib")%>>expression lib</a></li>
				<li><a
					href="<%=request.getContextPath()%>/iNLP.air">launch
				iNLP!</a></li>				
			</ul>
			</div>
		</div>
		<div id="content">
			<div class="breadcrumb"><%= generateBreadCrumb(request) %></div>
			<div id="left"><decorator:body /></div>
			<div id="clear"></div>
		</div>
		<div id="footer">
			<div class="copyright">&copy; University of Utah</div>
		</div>
	</div>
</body>
</html>