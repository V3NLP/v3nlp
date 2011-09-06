<%@page import="java.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>Categories</h1>
<table width="100%" border="1px" cellpadding="4" cellspacing="0" >
    <tr>
        <th>Id</th>
        <th>Name</th>
        <th>Description</th>
        <th>Short Name</th>
        <th>Order</th>
    </tr>
<c:forEach var="cat" items="${categories}" varStatus="loopStatus">
    <tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
    	<td>${cat.id}</td>
        <td>${cat.categoryName}</td>
        <td>${cat.categoryDescription}</td>
        <td>${cat.shortName}</td>
        <td align="center">${cat.sortOrder}</td>
    </tr>
</c:forEach>
</table>

