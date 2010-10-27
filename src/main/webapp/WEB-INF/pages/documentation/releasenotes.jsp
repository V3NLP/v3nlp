     <h1>Release Notes</h1>
     
     <h2>Bugs / Issues</h2>
     <ul>
     	<li>TagFactory: Black tag bug.</li>     	
     </ul>
    
     <h2>Todo</h2>
     <ul>     	     	
     	<li>[Priority 1.3] Test on HIPAA Data</li>
     	<li>[Priority 2] Section: Add advanced button that show full configuration and allows edit. Initial work done, but need further requirements.</li>
     	<li>Create additional annotations for manual annotation at the pipeline level (or application level?)</li>
     	<li>Tutorial/Example document (Tim/manual?)</li>
     	<li>Corpus Viewer (Thumbnail?)</li>
     	<li>Line through negations when we move to Flex 4.</li>
     	<li>Expression details panel: Flesh out functionality. </li>
     	<li>Ability to merge files</li>
     	<li>Compare saved results? (Likely a seperate tool.)</li>
     </ul>
     
     <h2>July 14, 2010</h2>
     <ul>
	 	<li>Finished advanced panel to Negation.</li>
		<li>Sectionizer:
			<ul>
				<li>Added a configuration editor at the <strong>pipeline level</strong> for Sectionizer.</li>
				<li>Users can now set custom sectionizer configuration</li>
				<li>TODO: Some requirements need defined, particularly, what about the section list? 
					Is it refreshed when configuration is changed? Or can you only configure the sectionizer 
					before adding a sectionizer module?</li>
			</ul>
		</li>
		<li>Feature: Added statistics to summary window to keep count of added and deleted annotations during a session. (at the corpus level)</li>
		<li>Feature: Added a "Annotations Changed" datagrid to show all added and deleted annotations during a session.</li>
		<li>Expression Library
			<ul>
				<li>Initial infrastructure in place, uses an SQLITE database <strong>outside</strong> of the war.</li>
				<li>Exposed as an AMF service currently, but can be expanded to Rest if other applications need access to it.</li>
				<li>Not wired into iNLP yet</li>
				<li>No editing capability outside of database tools yet.</li>
				<li>Web page to show / search current expressions.</li>
			</ul>
		</li>
   	 </ul>
   	 
     <h2>June 29, 2010</h2>
	 <ul>
	 	<li>Added advanced panel to Negation. (90% complete, working on save pipeline / serialization bug.)</li>
		<li>Bug: Fix expression bug where it was always trying to use sections, even if sections weren't included.</li>
		<li>Increased Remote Object timeout to 60 seconds for longer queries (in beans.mxml). Previously 10 seconds.</li>
   		<li>Bug: Remove processing window if fault occurs while processing.</li>
   		<li>Framework: Updated to Swiz 1.0RC1</li>
   	 </ul>
   	  
     <h2>June 15, 2010</h2>
	 <ul>
		<li>Testing</li>
   		<li>Pom changes for mvn release plugin to automate releases.</li>
   		<li>Release and deployment to RB03 (v. 1.1.1)</li> 
    	<li>[Priority 1.2] Section: Change drop-down to list for multiple selections</li>
    </ul>
    
	<h2>May 19, 2010</h2>
	<ul>
		<li>[Priority 1] Create a summary report that saves as CSV.</li>
     	<li>[Priority 1.1] Map HITEx section names to human readable format</li>
     	<li>Directory Panel Heading shows NAN</li>
     	<li>Make all manual annotation the same color.</li>
     	<li>Update capture groups on annotation changes.</li>
     	<li>Enter key in new Pipeline name window submits the panel</li>
     	<li>[Main Window] Top button bar buttons all the same size.</li>
	</ul>     
     
     <h2>May 5, 2010 (Tagged 1.0)</h2>
     <ul>
     	<li>Negation count now shows in legend.</li>
     	<li>Next/Previous buttons for navigating annotations. (Ctrl-P, Ctrl-N)</li>
     	<li>Load saved XML results</li>
     	<li>Save manual annotations, automatic annotations, or both</li>
     	<li>Expressions: Name/value expressions to return value parts of an expression. Requires HITEx changes.
     		<ul>
     			<li>Initial implementation, information comes back from HITEx / displays when annotation is clicked on in results window</li>
     			<li>Initial "Expression Details" mock-up created for refining concept configuration and naming of the capture groups</li>
     		</ul>
     	</li>     	
     </ul>
     
     <h2>April</h2>
     <ul>
     	<li>Saving to XML is now supported.
     		<ul>
     			<li>Saved at the corpus level</li>
     			<li>Includes documents, annotations, and format tags.</li>
     		</ul>
     	</li>
     	<li>Negations are shown as bold/italic with a larger font size in the document window, and bold/italic and red in the datagrid. Long term this should change to a Strikethrough, but Flex 3 does not support this without substantial kludgy code. Flex 4 apparently does support linethrough, and we should be migrating to it soon.</li>
     	<li>Expression Window now contains a table, where multiple expressions can be entered.</li>
     	<li>Change "File" menu to button bar</li>
     	<li>Change module help window colors to be more readable</li>
     	<li>Make the results document window resize with parent window</li>
     	<li>Change default highlight colors to pastels</li>
     	<li>Add time to Results Window title so it is unique</li>
     	<li>[Started] Document Regular Expressions (Part of HTML Site now, need to add examples)</li>
     	<li><strong>Full code base builds with maven</strong></li>
     	<li><Strong>HITEx issue corrected, the final war now runs in a path with spaces in the name.</Strong></li>
	</ul>
     <h2>March 10, 2010</h2>
     <ul>
     	<li>UI: Fixed bug importing a saved pipeline when no pane was open</li>
     	<li>UI: Fixed bug importing a saved pipeline, with it over writing the current pipeline</li>
     	<li>UI: First inclusion of an annotator on the results screen. This version is 75% functional, and a starting point for discussion. Modeled after VTTViewer from NLM.</li>
     	<li>Code Base: Many small fixes and clean-ups to improve the code.</li>
	</ul>
     <h2>February 24, 2010</h2>
     <ul>
     	<li>UI: Make keyword/concept/expression consistent to <strong>expression</strong> throughout application</li>
     	<li>UI: Make all panels initially the same size</li>
     	<li>UI: Change rank and notes to one field : <i>comments</i></li>
     	<li>Code Base: Performance improvement on results. The server now ONLY returns matching concept annotations, instead
     		of the entire annotated document in XML. This should be very performant.</li>
   		<li>UI: Remove Fetch from results label.</li>	
   		<li>Sections: Multiple exclude bug fixed.</li>
   		<li><strong>Code Base: Server code now builds with Maven.</strong></li>
     </ul>
     
     <h2>February 10, 2010 ~ Initial Version</h2>
     <ul>
     	<li>Load/save pipeline definitions implemented</li>
     	<li>File, Copy &amp; Paste, and Directory datasource implemented</li>
     	<li>Negation, Keyword, and Review Results implemented</li>
     	<li>Sections (include/exclude) partially implemented</li>
     	<li>Review Results preliminary interface implemented.</li>
     </ul> 