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
  String index() {
    return "index";
  }

  // @RequestMapping("/db")
  // String db(Map<String, Object> model) {
  //   try (Connection connection = DriverManager.getConnection("jdbc:postgresql://ec2-35-174-88-65.compute-1.amazonaws.com/d1mhl73a6fevb9", "mgsvmsbodwlkui", "a6137c9c2b864ffce519baf9638c18e93f0908b7fcfada604710c71b9dcd2908")) {
 
  //           System.out.println("Java JDBC PostgreSQL Example");
            // When this class first attempts to establish a connection, it automatically loads any JDBC 4.0 drivers found within 
            // the class path. Note that your application must manually load any JDBC drivers prior to version 4.0.
//          Class.forName("org.postgresql.Driver"); 
 
  //           System.out.println("Connected to PostgreSQL database!");
  //           Statement statement = connection.createStatement();
  //           System.out.println("Reading car records...");
  //           System.out.printf("%-30.30s  %-30.30s%n", "Model", "Price");
  //           ResultSet resultSet = statement.executeQuery("SELECT * FROM Salesforce.Contact");
  //           ArrayList<String> output = new ArrayList<String>();
  //           while (resultSet.next()) {
  //             output.add("Name : " + resultSet.getString("Name") +"|| " +"Email : " +  resultSet.getString("Email"));
  //             System.out.printf("%-30.30s  %-30.30s%n", resultSet.getString("Name"), resultSet.getString("Email"));
  //           }
  //           model.put("records", output);
  //           }catch (SQLException e) {
  //           System.out.println("Connection failure.");
  //           e.printStackTrace();
  //       }
  //       return "db";
  // }
  @RequestMapping(value="/redirect",method = RequestMethod.POST)    
  public String redirect(){     
    return "db";//will redirect to viewemp request mapping    
  }   

  @RequestMapping(value="/save",method = RequestMethod.POST)    
  String save(){    
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
      String queryInsert = " insert into Salesforce.Contact (Name,LastName,Email)"
        + " values (?, ? , ?)";
      String queryUpdate = "update Salesforce.Contact SET LastName = ?, Email = ? WHERE LastName = ?";

      //create the mysql insert preparedstatement 
      PreparedStatement preparedStmt = connection.prepareStatement(queryInsert);
      preparedStmt.setString (1, "Barney");
      preparedStmt.setString (2, "Tran Minh Quan");
      preparedStmt.setString (3, "briandent@trailhead.com");
      preparedStmt.execute();
      
      //update
      // preparedStmt.setString(1, "Monkey D Luffy");
        // preparedStmt.setString(2, "Monkey D Luffy");
        // preparedStmt.setString(3, "Monkey D Luffy");
        // preparedStmt.executeUpdate();

        Statement stmt = connection.createStatement();
        //stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
        //stmt.executeUpdate("INSERT INTO Salesforce.Contact VALUES (now())");

        ResultSet rs = stmt.executeQuery("SELECT * FROM Salesforce.Contact");
        
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

  @RequestMapping("/hello")
  String hello(Map<String, Object> model) {
   try (Connection connection = dataSource.getConnection()) {
      String queryInsert = " INSERT INTO public.userAccount (user_id, username, lastname, email)"
        + " VALUES (?, ?, ?, ?)";

      //create the mysql insert preparedstatement 
      PreparedStatement preparedStmt = connection.prepareStatement(queryInsert);
      preparedStmt.setString (1, "1");
      preparedStmt.setString (2, "tranquan");
      preparedStmt.setString (3, "Quan");
      preparedStmt.setString (4, "briandent@trailhead.com");
      preparedStmt.execute();

      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM User");
      return "hello";
    } catch (Exception e) {
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
