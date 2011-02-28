     <h1>Release Notes</h1>
     

     <h2>February 24, 2011</h2>
     <ul>
     	<li>Changed <strong>Template</strong> label to <strong>App Library</strong></li>
     	<li>Added a <strong>Create New</strong> button on App Library tab that creates a new pipeline and makes that pipeline tab visible</li>
     	<li>Added <strong>(right click to modify)</strong> to document details text above document display</li>
     	<li>Removed <strong>Annotation</strong> place holder panel in document details</li>
     	<li>Fetch panel now shows <strong>Fetch - {type}</strong> in the title bar, and the info button was moved to the right</li>
     	<li><strong>Delete</strong> works in annotation text area, how should it work in the summary table above? Simply adjust the number as needed? Or show it differently?</li>
     	<li><strong>Tag Editor</strong> should now work (at the corpus level). Was at the document level before, but does not make sense at that level.</li>
     	<li>In Labels grid, <strong>double clicking</strong> now opens the tag editor</li>
     	<li>Templates are now <strong>grouped</strong> by their sub-directory.</li>
     	<li>Color of pipeline <strong>canvas background</strong> is now a darker shade of grey. We may wish to investigate a common CSS theme for all Flex apps in the future.</li>
     	<li>Pipeline operation sizes are now variable height, and start at minimum height</li>
     	<li>Default templates in place.</li>
     	<li>Initial Architecture Draft ready for review.</li>
     </ul>
     
     
     <h2>January 2011</h2>
      <ul>
	 	<li>[UI] Codebase moved to Flex 4.x</li>
	 	<li>[UI] Now using Common Model</li>
		<li>[UI] Highlighting performance improved, now uses TextLayoutFrameowk</li>
		<li>[UI] Numerous small bug fixes</li>
	 	<li>[UI] Metamap Integeration
	 		<ul>
	 			<li>Works on any datasource</li>
	 			<li>If sections are selected, limits to those sections</li>
	 		</ul>
	 	</li>
	 	<li>[Server Side] Code updated to common model</li>
	 	<li>[Server Side] Code re-structured for loose coupling</li>
	 	<li>[Server Side] Metamap integrated as a Spring service</li>
	 	<li>[Common Model] First implementation</li>
	 	<li>[Common Model] Added some unit test coverage</li>
	 </ul>

     <h2>August 2010</h2>
     <ul>
	 	<li>Advanced panel to Negation.</li>
		<li>Sectionizer:
			<ul>
				<li>Added a configuration editor at the <strong>pipeline level</strong> for Sectionizer.</li>
				<li>Users can now set custom sectionizer configuration</li>
			</ul>
		</li>
		<li>Feature: Added statistics to summary window to keep count of added and deleted annotations during a session. (at the corpus level)</li>
		<li>Feature: Added a "Annotations Changed" datagrid to show all added and deleted annotations during a session.</li>
		<li>Expression Library
			<ul>
				<li>Initial infrastructure in place, uses an SQLITE database <strong>outside</strong> of the war.</li>
				<li>No editing capability outside of database tools yet.</li>
				<li>Web page to show / search current expressions.</li>
			</ul>
		</li> 
		<li>Bug: Fix expression bug where it was always trying to use sections, even if sections weren't included.</li>
		<li>Increased Remote Object timeout to 60 seconds for longer queries (in beans.mxml). Previously 10 seconds.</li>
   		<li>Bug: Remove processing window if fault occurs while processing.</li>
   		<li>Framework: Updated to Swiz 1.0RC1</li>
  		<li>Summary report that saves as CSV.</li>
     	<li>[Priority 1.1] Map HITEx section names to human readable format</li>
     	<li>Directory Panel Heading shows NAN</li>
     	<li>Make all manual annotation the same color.</li>
     	<li>Update capture groups on annotation changes.</li>
     	<li>Enter key in new Pipeline name window submits the panel</li>
     	<li>[Main Window] Top button bar buttons all the same size.</li>
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
     	<li><Strong>HITEx issue corrected, the final war now runs in a path with spaces in the name.</Strong></li>
	 	<li>UI: Fixed bug importing a saved pipeline when no pane was open</li>
     	<li>UI: Fixed bug importing a saved pipeline, with it over writing the current pipeline</li>
     	<li>UI: First inclusion of an annotator on the results screen. This version is 75% functional, and a starting point for discussion. Modeled after VTTViewer from NLM.</li>
     	<li>UI: Make keyword/concept/expression consistent to <strong>expression</strong> throughout application</li>
     	<li>UI: Make all panels initially the same size</li>
     	<li>UI: Change rank and notes to one field : <i>comments</i></li>
     	<li>Code Base: Performance improvement on results. The server now ONLY returns matching concept annotations, instead
     		of the entire annotated document in XML. This should be very performant.</li>
   		<li>UI: Remove Fetch from results label.</li>	
   		<li>Sections: Multiple exclude bug fixed.</li>
   	 </ul>
     
     <h2>February 10, 2010 ~ Initial Version</h2>
     <ul>
     	<li>Load/save pipeline definitions implemented</li>
     	<li>File, Copy &amp; Paste, and Directory datasource implemented</li>
     	<li>Negation, Keyword, and Review Results implemented</li>
     	<li>Sections (include/exclude) partially implemented</li>
     	<li>Review Results preliminary interface implemented.</li>
     </ul> 