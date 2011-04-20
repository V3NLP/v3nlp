package gov.va.vinci.v3nlp.controller;

import gov.va.vinci.v3nlp.registry.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ServiceRegistryController {

    @Autowired
    RegistryService registryService;

    @RequestMapping("/registry/categories.html")
    public ModelAndView categoryList() {
        System.out.println("Category List!!! " + registryService);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("registry/categories");
        mav.addObject("categories", registryService.getNlpComponentCategoryList());
        return mav;
    }


    @RequestMapping("/registry/services.html")
    public ModelAndView serviceList() {
        System.out.println("Service List!!!");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("registry/services");
        mav.addObject("categories", registryService.getNlpComponentCategoryList());
        return mav;
    }

}
