<%@page import="java.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>Services</h1>

<strong>Categories:</strong>
<ul style="padding-left: 40px">
<c:forEach var="cat" items="${categories}" varStatus="loopStatus">
   <li><a href="#${cat.categoryName}">${cat.categoryName}</a></li>
</c:forEach>
</ul>
<br/><br/>
<c:forEach var="cat" items="${categories}" varStatus="loopStatus">
    <a name="${cat.categoryName}">
        <h2 style="border-bottom: 1px; border-bottom-color: gray; border-bottom-style: solid; border-bottom-width: 1px; margin-bottom: 5px; padding-bottom: 4px; padding-top: 10px;">Category: ${cat.categoryName}</h2>
    </a>

    <c:forEach var="component" items="${cat.components}" varStatus="componentStatus">
         <h3 style="border: 1px solid black; background-color:  #d3d3d3; padding: 10px">Annotator: ${component.implementationClass}</h3>
         <div id="comp${comp.uid}}">
            <strong>Technology:</strong> ${component.technology}<br/>
            <strong>Requires:</strong> ${component.requiresString}<br/>
            <strong>Provides:</strong> ${component.providesString}<br/>
            <strong>Description:</strong><br/><br/>${component.description}<br/>
            <br/>
        </div>
    </c:forEach>

</c:forEach>



