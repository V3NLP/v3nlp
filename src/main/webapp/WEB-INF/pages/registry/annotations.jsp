<%@page import="java.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>Annotations</h1>
<table width="100%" border="1px" cellpadding="4" cellspacing="0" >
    <tr>
        <th>Id</th>
        <th>Name</th>
    </tr>
<c:forEach var="item" items="${model}" varStatus="loopStatus">
    <tr class="${loopStatus.index % 2 == 0 ? 'even' : 'odd'}">
    	<td>${item.id}</td>
        <td>${item.name}</td>
    </tr>
</c:forEach>
</table>

