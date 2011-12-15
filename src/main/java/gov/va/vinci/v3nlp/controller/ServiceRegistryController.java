/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.controller;

import com.ddtek.jdbc.extensions.ExtConnection;
import gov.va.vinci.v3nlp.registry.NlpComponent;
import gov.va.vinci.v3nlp.registry.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Controller
public class ServiceRegistryController {

    @Autowired
    RegistryService registryService;

    @RequestMapping("/registry/categories.html")
    public ModelAndView categoryList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("registry/categories");
        mav.addObject("categories", registryService.getNlpComponentCategoryList());
        return mav;
    }


    @RequestMapping("/registry/services.html")
    public ModelAndView serviceList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("registry/services");
        mav.addObject("categories", registryService.getNlpComponentCategoryList());
        return mav;
    }


    @RequestMapping("/registry/annotations.html")
    public ModelAndView annotationList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("registry/annotations");
        mav.addObject("model", registryService.getNlpAnnotationList());
        return mav;
    }

    @RequestMapping("/registry/addcomponent")
    public ModelAndView addComponent() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("registry/addcomponent");
        mav.addObject("model", new NlpComponent());
        return mav;
    }

    @RequestMapping("/registry/doaddcomponent")
    public ModelAndView doAddComponent(NlpComponent comp) {
        return null;
    }


    @RequestMapping("/test/jdbctest")
    public ModelAndView doJdbcTest(HttpServletResponse response,
                                   HttpServletRequest request) throws Exception {
        try {
            response.getOutputStream().println("<html><body>" +
                    "<h1>Testing Connection</h1><br/>");
            response.getOutputStream().print("Registering Drive...");

            Class.forName("com.ddtek.jdbc.sqlserver.SQLServerDriver");

            response.getOutputStream().print("Complete.<br/>Making Connection....");

            Connection conn = DriverManager.getConnection("jdbc:datadirect:sqlserver://vhacdwdbs10:1433;databaseName=Test;User=VinciUser;Password=VinciUser");

            response.getOutputStream().print("Complete.<br/><br/>");
            response.getOutputStream().println("Connection=" + conn + "<br/><br/>");
            String selectString = "select * from VINCIStaff";

            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(selectString);
                while (rs.next()) {
                    response.getOutputStream().print("---> Got a row.<br/>");
                }
                stmt.close();
            } catch (Exception e) {
                response.getOutputStream().print("----> Select (without impersonate) failed. (" + e + ")");
            }


            response.getOutputStream().println("Changing current user to vhamaster\\vhaislcornir...");
            ((ExtConnection) conn).setCurrentUser("vhamaster\\vhaislcornir");

            response.getOutputStream().println("Complete.<br/><br/>Running sql as impersonated user....<br/>");
             try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(selectString);
                while (rs.next()) {
                    response.getOutputStream().print("---> Got a row.<br/>");
                }
                stmt.close();
            } catch (Exception e) {
                response.getOutputStream().print("----> Select (WITH impersonate) failed. (" + e + ")");
            }
            conn.close();

        } catch (Exception e) {
            response.getOutputStream().print("<br/><strong>Exception:</strong><br/><pre>" + e + "</pre>");
        } finally {
            response.getOutputStream().println("<br></body></html>");
        }

        return null;
    }

}
