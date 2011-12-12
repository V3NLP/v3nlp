<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<h1>Add Component</h1>
<style>
    textarea {
        width: 100%;
    }

    th {
        vertical-align: top;
        width: 200px;
    }
</style>
<form:form method="POST" commandName="model" action="doaddcomponent">
    <form:errors path="*" cssClass="errorblock" element="div"/>
    <table width="100%" border="0px" cellpadding="4" cellspacing="0">
        <tr>
            <th>Component Name</th>
            <td><form:input path="componentName"/></td>
            <td><form:errors path="componentName" cssClass="error"/></td>
        </tr>
        <tr>
            <th>UID</th>
            <td><form:input path="uid"/></td>
            <td><form:errors path="uid" cssClass="error"/></td>
        </tr>
        <tr>
            <th>Category</th>
            <td><form:input path="category"/></td>
            <td><form:errors path="category" cssClass="error"/></td>
        </tr>
        <tr>
            <th>Implementation Class</th>
            <td><form:input path="implementationClass"/></td>
            <td><form:errors path="implementationClass" cssClass="error"/></td>
        </tr>
        <tr>
            <th>Technology</th>
            <td><form:input path="technology"/></td>
            <td><form:errors path="technology" cssClass="error"/></td>
        </tr>
        <tr>
            <th>Pedigree</th>
            <td><form:input path="pedigree"/></td>
            <td><form:errors path="pedigree" cssClass="error"/></td>
        </tr>
        <tr>
            <th>Description</th>
            <td><form:input path="description"/></td>
            <td><form:errors path="description" cssClass="error"/></td>
        </tr>
        <tr>
            <th>Active</th>
            <td><form:checkbox path="active"/>
            </td>
            <td><form:errors path="active" cssClass="error"/>
        </tr>
        <tr>
            <th>Assumptions</th>
            <td><form:textarea rows="10" path="assumptions"/></td>
            <td><form:errors path="assumptions" cssClass="error"/></td>
        </tr>


        <tr>
            <th>Configuration Form</th>
            <td><form:textarea rows="10" path="configurationForm"/></td>
            <td><form:errors path="configurationForm" cssClass="error"/></td>
        </tr>
        <tr>
            <th>Default Configuration</th>
            <td><form:textarea rows="10" path="defaultConfiguration"/></td>
            <td><form:errors path="defaultConfiguration" cssClass="error"/></td>
        </tr>

    </table>
    <br/><br/>
    <input type="submit" value="Add"/>
</form:form>

