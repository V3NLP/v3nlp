package gov.va.research.inlp.controller;

import gov.va.research.inlp.expressionlib.Expression;

import org.displaytag.decorator.TableDecorator;

public class ExpressionLibTableDecorator extends TableDecorator {

	public String getTitle() {
		Expression exp= (Expression)getCurrentRowObject();
		
		String citation = exp.getCitation();
		if (citation == null) {
			citation = "";
		}
		// create link
		String result = "<a id='inline' href='#data" + exp.getId() + "'>" + exp.getTitle() + "</a>";
		
		// Add div
		result += "<div style='display:none'><div id='data" + exp.getId() + "'>" +
				"<table width='600' cellpadding='5'>" +
				"<tr><td width='200' valign='top'><strong>Title</strong></td><td>" + exp.getTitle() + "</td></tr>" +
				"<tr><td valign='top'><strong>Description</strong></td><td>" + exp.getDescription() + "</td></tr>" +
				"<tr><td valign='top'><strong>Expression</strong></td><td>" + exp.getExpression() + "</td></tr>" +
				"<tr><td valign='top'><strong>Citation</strong></td><td>" +  citation + "</td></tr>" +
				"<tr><td valign='top'><strong>Created By</strong></td><td>" + exp.getCreatedBy() + "</td></tr>" +
				"</table></div></div>";
		return result;
	}
}
