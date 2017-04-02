package com.example.controller;

/**
 * Created by Dominik on 21.03.2017.
 */

import com.example.DatabaseConnection;
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
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/logs")
public class LogController {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @ApiOperation(value = "Get all logs", notes = "Get all logs from db", nickname = "get_all_logs")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = List.class)})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sort_type", value = "Sort type module/date/description", required = true, dataType = "string", paramType = "path", defaultValue = "null")
    })
    @RequestMapping(value = "/showAllLogs/{sort_type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> showAllLogs(@PathVariable("sort_type") String sort_type) throws JsonProcessingException {
        List<Log> logs = new ArrayList<>();
        try{
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM log ORDER BY "+sort_type;
            ResultSet resultSet = statement.executeQuery(sql);
            StringBuffer stringBuffer = new StringBuffer();

            while (resultSet.next()){
                logs.add(new Log().getObjectFromSQL(resultSet));
            }
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

    //poprawic by errory wypisywaly 
    @RequestMapping(value = "/addLog", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewLog(@RequestBody String log) throws IOException{
        Log logD = MAPPER.readValue(log,Log.class);
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            String sql;
            sql = "insert into log values " +
                    "(DEFAULT, '" + logD.getDescription() + "'"
                    + ", '" + logD.getModule() + "'"
                    +", '" + logD.getDate() + "')";
            ResultSet rs = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).body(logD);
    }
}
