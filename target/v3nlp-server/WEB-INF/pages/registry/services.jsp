<%@page import="java.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>Services</h1>

<c:forEach var="cat" items="${categories}" varStatus="loopStatus">
    <h2 style="border-bottom: 1px; border-bottom-color: gray; border-bottom-style: solid; border-bottom-width: 1px; margin-bottom: 5px; padding-bottom: 4px; padding-top: 10px;">Category: ${cat.categoryName}</h2>
    <table width="95%" border="1px" align="right" cellpadding="4" cellspacing="0" >
        <tr>
            <th>Implementation</th>
            <th>Notes</th>
            <th>Requires</th>
            <th>Provides</th>
        </tr>
    <c:forEach var="component" items="${cat.components}" varStatus="componentStatus">
         <tr>
             <td>${component.implementationClass}</td>
             <td>${component.notes}</td>
             <td>
                 ${component.requiresString}
             </td>
             <td>
                 ${component.providesString}
             </td>
         </tr>
    </c:forEach>
    </table>
    <div style="height: 40px">
        &nbsp;&nbsp;
    </div>
    <br/><br/>
    <br/>
</c:forEach>



