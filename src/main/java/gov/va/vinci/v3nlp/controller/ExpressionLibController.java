package gov.va.vinci.v3nlp.controller;

import gov.va.vinci.v3nlp.expressionlib.ExpressionServiceImpl;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/expressionlib.html")
public class ExpressionLibController {
	
    @Autowired
    private ExpressionServiceImpl expressionService;

	@RequestMapping(method=RequestMethod.GET)
	public String getView(Model model) {
		model.addAttribute("expressions", this.expressionService.getAllExpressions());
		model.addAttribute("expressionSearchForm", new ExpressionSearchForm());
		model.addAttribute("searchOptions", ExpressionServiceImpl.SEARCHABLE_FIELDS);
		return "expressionlib";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String doPost(ExpressionSearchForm form, BindingResult result, Model model) {
		if (!GenericValidator.isBlankOrNull(form.getSearchField()) && 
			!GenericValidator.isBlankOrNull(form.getSearchValue())) {
			model.addAttribute("expressions", this.expressionService.findExpressions(form.getSearchField(),form.getSearchValue()));			
		} else {
			model.addAttribute("expressions", this.expressionService.getAllExpressions());
		}
		model.addAttribute("searchOptions", ExpressionServiceImpl.SEARCHABLE_FIELDS);	
		return "expressionlib";
	}
}
