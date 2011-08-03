 	 <%
 	 	// Get current endpoint address. 
 	 	String file = request.getRequestURI();
 	    String context= request.getContextPath();
		java.net.URL nlpServiceEnpointURL = new java.net.URL(request.getScheme(),
                               request.getServerName(),
                               request.getServerPort(),
                               context + "/messagebroker/amf");
	  %>
	  <h1>Installation</h1>
 	 <h2>Client Requirements</h2>
     	<ul>
     	  <li><a href="http://get.adobe.com/air/" target="_blank">Adobe Air</a></li>
     	  <li>Pentium 500 MHz or greater</li>
     	  <li>256 MB RAM / 20 MB Disk Space</li>
     	  <li>Linux / OSX / Windows</li>
     	</ul>
     <h2>Server Requirements</h2>
     	<ul>
     		<li>Java Servlet Container (Tested under <a href="http://tomcat.apache.org" target="_blank">Tomcat 6.0.24</a>)</li>
     		<li>512 MB Ram / 128 MB Permgen Space</li>
    		<li>100 MB Disk Space</li>
     		<li>Linux / OSX / Windows</li>
     	</ul>
     <h2>Installation Steps</h2>
     	<ul>
     		<li>Download the WAR package from ...</li>
     		<li>Deploy the WAR to your servlet container</li> 
			<li>Browse to http://&lt;your server&gt;/NlpWebServices/</li>
			<li>Launch the application from the link on the site</li>   		
     		<li>Once the client application is started, configure the correct NLP service endpoint URL in the <strong>File-&gt; Edit Configuration</strong> menu<br><br>
     			<div class="info-box"><strong>Note: The current url for this server is:</strong><br/><%= nlpServiceEnpointURL.toString() %></div>
     		</li>     		
     	</ul>
     	<h1>Un-installation</h1>
     	<h2>Client</h2>	
     	<ul>
			<li>On Windows: Using the Add/Remove Programs panel to remove the application.</li>
			<li>On Mac OS: Delete the application file from the install location.</li>
		</ul>
     	<h2>Server</h2>
     	<ul>
     		<li>Un-deploy application from servlet container</li>
     		<li>Remove .war</li>
     	</ul>