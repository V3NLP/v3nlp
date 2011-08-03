 	 <%
 	 	// Get current endpoint address. 
 	 	String file = request.getRequestURI();
 	    String context= request.getContextPath();
		java.net.URL nlpServiceEnpointURL = new java.net.URL(request.getScheme(),
                               request.getServerName(),
                               request.getServerPort(),
                               context + "/messagebroker/amf");
	  %>
	  <h1>Building iNLP</h1>
 	 <h2>Requirements</h2>
     	<ul>
     	  <li>Java 1.6</li>
     	  <li>Adobe Flex SDK 3.X</li>     	  
     	  <li>Maven</li>
     	</ul>
     <h2>Building</h2>
     <br/>
     iNLP uses Maven as it's build mechanism. In addition, several Adobe Flex libraries are not currently (March 2010) 
     available via standard repositories. They need to be added to you repository manually:
     <br><br>
     	<strong>as3corelib v 0.92.1</strong><br/>
		mvn install:install-file  -DgroupId=as3corelib -DartifactId=as3corelib -Dversion=0.92.1 -Dpackaging=swc -Dfile=as3corelib.swc<br/><br/>
		
		<strong>swizframework v 0.6.4-flex3</strong><br/>
		mvn install:install-file -DgroupId=org.swizframework -DartifactId=swiz -Dversion=0.6.4-flex3 -Dfile=swiz-0.6.4-flex3.swc -Dpackaging=swc<br/><br/>
		
		<strong>datavisualization v 3.2.0.3958</strong><br/>
		mvn install:install-file -DgroupId=com.adobe.flex.framework -DartifactId=datavisualization -Dversion=3.2.0.3958 -Dpackaging=swc -Dfile=datavisualization.swc<br/>
		mvn install:install-file -DgroupId=com.adobe.flex.framework -DartifactId=datavisualization -Dversion=3.2.0.3958 -Dclassifier=en_US -Dpackaging=rb.swc -Dfile=datavisualization_rb.swc<br/><br/>
		
		<strong>Flex compiler v 3.2.0.3958</strong><br/>
		mvn install:install-file -DgroupId=com.adobe.flex.compiler -DartifactId=adt -Dversion=3.2.0.3958 -Dpackaging=jar -Dfile=adt.jar<br/><br/>
     	
     	<strong>License (for datavisualization components, part of SDK</strong><br/>
     	mvn install:install-file -DgroupId=com.adobe.flex -DartifactId=license -Dversion=3.2.0.3958 -Dpackaging=jar -Dfile=license.jar<br/><br/>
     	