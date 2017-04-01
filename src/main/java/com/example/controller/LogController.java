package com.example.controller;

/**
 * Created by Dominik on 21.03.2017.
 */

import com.example.model.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@SpringBootApplication
@RequestMapping("api/logs")
public class LogController {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @ApiOperation(value = "Get all logs", notes = "Get all logs from db", nickname = "get_all_logs")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success",
                    response = List.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "sort_type",
                    value = "Sort type module/date/description",
                    required = true,
                    dataType = "string",
                    paramType = "path",
                    defaultValue = "null")
    })
    @RequestMapping(value = "/showAllLogs/{sort_type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> showAllLogs(Model model,
                                         @PathVariable("sort_type") String sort_type) throws JsonProcessingException {
        List logs = new ArrayList<>();
        try{
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM log";
            ResultSet resultSet = statement.executeQuery(sql);
            StringBuffer stringBuffer = new StringBuffer();

            while (resultSet.next()){
                int id = resultSet.getInt("ID");
                String description = resultSet.getString("description");
                String module = resultSet.getString("module_name");
                String date = resultSet.getString("date");
                logs.add(new Log(id,description,module,date));
            }
            model.addAttribute("logs", logs);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(MAPPER.writeValueAsString(logs));
    }

    @RequestMapping(value = "/showLogs",
            params = {"id", "description", "module_name", "start_date", "end_date"}
            ,method = RequestMethod.GET)
    public String showLogs(){
        return "showLogs";
    }

    @RequestMapping(value = "/createLog", method = RequestMethod.POST)
    public String createNewLog(@ModelAttribute Log log, Model model){
        model.addAttribute("log", log);
            int id = log.getId();
            String date = log.getDate();
            String description = log.getDescription();
            String module = log.getModule();
            try {
                Connection connection = getConnection();
                Statement statement = connection.createStatement();
                String sql;
                sql = "insert into log values " +
                        "(DEFAULT, '" + description + "'"
                        + ", '" + module + "'"
                        +", '" + date + "')";
                ResultSet rs = statement.executeQuery(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return "result";
    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath()
                + "?sslmode=require";

        return DriverManager.getConnection(dbUrl, username, password);
    }

    public static void main(String[] args) {
        SpringApplication.run(
                LogController.class, args);
    }

}
