package gov.va.vinci.v3nlp.controller;

import gov.va.vinci.v3nlp.expressionlib.ExpressionServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.*;

@Controller
@RequestMapping(value = "/connection-test.html")
public class ConnectionControllerTest {

    @RequestMapping(method = RequestMethod.GET)
    public String getView(Model model) throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionUrl = "jdbc:sqlserver://localhost;database=Test;integratedSecurity=false;user=VHAVINJVCSVC;password=R@inst0rm;";
        Connection con = DriverManager.getConnection(connectionUrl);
        System.out.println("Got connection:" + con);

        Statement stmnt = con.createStatement();
        ResultSet rs = stmnt.executeQuery("select 1 from 1;");

        while (rs.next()) {
            System.out.println("In here!");
        }
        stmnt.close();
        con.close();
        return "junkReturn";
    }
}
