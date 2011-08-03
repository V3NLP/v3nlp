<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/displaytagex.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/fancybox/jquery.fancybox-1.3.1.css" type="text/css" media="screen" />
<script type="text/javascript" src="<%= request.getContextPath() %>/fancybox/jquery.min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/fancybox/jquery.fancybox-1.3.1.pack.js"></script>

<h1>Expression Library</h1>

<display:table name="expressions" sort="list" requestURI="<%= request.getContextPath() + \"/app/expressionlib.html\" %>" pagesize="15" 
	style="width: 100%" decorator="gov.va.vinci.v3nlp.controller.ExpressionLibTableDecorator">
	<display:setProperty name="css.table" value="dataTable"/>
	  <display:column property="title" sortable="true"  title="Title" style="width: 25%" />
	  <display:column property="description" sortable="true" title="Description" style="width: 45%"/>
	  <display:column property="expression" sortable="true" maxLength="30"  style="width: 20%"/>
	  <display:column property="createdBy" sortable="true"  style="width: 10%"/>
</display:table>
<br/>
<form:form commandName="expressionSearchForm">
	<table>
		<tr>
			<td><strong>Search</strong>
			
				<form:select path="searchField">
			      <form:options  items="${searchOptions}"  itemLabel="label" itemValue="value"/>
				</form:select>
				   for 
				<input type="text" size="10" name="searchValue"/> <input type="submit" value="Go!"/></td>
		</tr>
	</table>
</form:form>
<script>

$(document).ready(function() {

	/* This is basic - uses default settings */
	
	$("a#inline").fancybox();
});

</script>