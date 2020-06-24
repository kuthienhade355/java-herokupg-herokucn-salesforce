/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

////
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.List;    
import org.springframework.beans.factory.annotation.Autowired;    
import org.springframework.stereotype.Controller;  
import org.springframework.ui.Model;  
import org.springframework.web.bind.annotation.ModelAttribute;    
import org.springframework.web.bind.annotation.PathVariable;    
import org.springframework.web.bind.annotation.RequestMapping;    
import org.springframework.web.bind.annotation.RequestMethod;       


@Controller
@SpringBootApplication
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String index(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM Salesforce.contact");
      Statement stmt2 = connection.createStatement();
      ResultSet rsJava = stmt2.executeQuery("SELECT * FROM public.contact"); //Java insert
        
        ArrayList<String> outputSalesforce = new ArrayList<String>();
        ArrayList<String> outputJava = new ArrayList<String>();
        
        while (rs.next()) {
          outputSalesforce.add("Read from DB: " + " " + "Name : " + rs.getString("Name") +"|| " +"Email : " +  rs.getString("Email"));
        }

        while (rsJava.next()) {
          outputJava.add("Read from DB: " + " " + "Name : " + rsJava.getString("Name") +"|| " +"Email : " +  rsJava.getString("Email"));
        }

        model.put("records", outputSalesforce);
        model.put("recordJAVAs", outputJava);
        return "index";
      } catch (Exception e) {
        model.put("message", e.getMessage());
        return "error";
      }
      // return "index";
  }


  @RequestMapping(value="/redirect",method = RequestMethod.POST)    
  public String redirect(){     
    return "db";//will redirect to viewemp request mapping    
  }   

  @RequestMapping(params = "save", method = RequestMethod.POST)   
  public String saveUser() {  
      try (Connection connection = dataSource.getConnection()) {
      String queryInsert = " insert into Salesforce.Contact (Name,LastName,Email)"
        + " values (?, ? , ?)";
     
      // create the mysql insert preparedstatement 
      PreparedStatement preparedStmt = connection.prepareStatement(queryInsert);
      preparedStmt.setString (1, "Quandeptrai");
      preparedStmt.setString (2, "QuandeptraiQuadi");
      preparedStmt.setString (3, "Quandeptrai@trailhead.com");
      preparedStmt.execute(); 
      } catch (Exception e) {
      return "error";
    }
      return "db";//will redirect to viewemp request mapping    
  }

  @RequestMapping("/db")
  String db(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {

      //Syntax Query salesforce 
      String queryInsert = " insert into Salesforce.Contact (Name,LastName,Email)"
         + " values (?, ? , ?)";
      // String queryUpdate = "update Salesforce.Contact SET LastName = ?, Email = ? WHERE LastName = ?";

      //Syntax Query heroku
      String queryHeroku = " insert into userAccount (id,Name,LastName,email)"
         + " values (?, ?, ?, ?)";

      //create the mysql insert heroku 
      // PreparedStatement preparedStmt = connection.prepareStatement(queryHeroku);
      // preparedStmt.setInt(1, 1);
      // preparedStmt.setString (1, "tranquan");
      // preparedStmt.setString (2, "Quan");
      // preparedStmt.setString (3, "briandent@trailhead.com");
      // preparedStmt.execute();

      //create the mysql insert preparedstatement 
      // PreparedStatement preparedStmt1 = connection.prepareStatement(queryInsert);
      // preparedStmt1.setString (1, "Test");
      // preparedStmt1.setString (2, "TestInsert");
      // preparedStmt1.setString (3, "testInsert@trailhead.com");
      // preparedStmt1.execute();
      
      //update Salesforce
      // preparedStmt.setString(1, "Monkey D Luffy");
        // preparedStmt.setString(2, "Monkey D Luffy");
        // preparedStmt.setString(3, "Monkey D Luffy");
        // preparedStmt.executeUpdate();


        //Function Select data Postges 
        Statement stmt = connection.createStatement();
       // ResultSet rs = stmt.executeQuery("SELECT * FROM Salesforce.Contact");
        ResultSet rs = stmt.executeQuery("SELECT * FROM public.contact"); //heroku
        
        ArrayList<String> output = new ArrayList<String>();
        while (rs.next()) {
          output.add("Read from DB: " + " " + "Name : " + rs.getString("Name") +"|| " +"Email : " +  rs.getString("Email"));
          System.out.printf("%-30.30s  %-30.30s%n", rs.getString("Name"), rs.getString("Email"));
        }

        model.put("records", output);
        return "db";
      } catch (Exception e) {
        model.put("message", e.getMessage());
        return "error";
      }
  }
  

  @Bean 
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }
}
