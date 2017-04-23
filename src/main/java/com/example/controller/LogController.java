package com.example.controller;

/**
 * Created by Dominik on 21.03.2017.
 */

import com.example.DatabaseConnection;
import com.example.model.Log;
import com.example.model.LogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/logs")
public class LogController {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private LogRepository logRepository;

    @Autowired
    public LogController(LogRepository logRepository){
        this.logRepository = logRepository;
    }

    @ApiOperation(value = "Get all logs", notes = "Get all logs from db", nickname = "get_all_logs")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = List.class)})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sort_type", value = "Sort type module/date/description", required = true, dataType = "string", paramType = "path", defaultValue = "null")
    })
    @RequestMapping(value = "/showAllLogs/{sort_type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> showAllLogs(@PathVariable("sort_type") String sort_type) throws JsonProcessingException {

        List<Log> logs = logRepository.findAll();
        sortLogList(logs, sort_type);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(MAPPER.writeValueAsString(logs));
    }

    //@RequestMapping(value = "/showLogs"
      //      , method = RequestMethod.GET)
    //public ResponseEntity<?> showLogs(@RequestParam(required = false) String description,
      //                                @RequestParam(required = false) String module_name,
        //                              @RequestParam(required = false) String date,
          //                            @RequestParam(required = false) String order_by) throws JsonProcessingException {
        /*String sql;
        description = "%" + description + "%";
        module_name = "%" + module_name + "%";
        date = "%" + date + "%";
        List<Log> logs = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            StringBuffer stringBuffer = new StringBuffer();

            while (resultSet.next()) {
                logs.add(new Log().getObjectFromSQL(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }*/
      //  return ResponseEntity.status(HttpStatus.OK)
         //       .contentType(MediaType.APPLICATION_JSON)
           //     .body(MAPPER.writeValueAsString(logs));
    //}

    //poprawic by errory wypisywaly 
    @RequestMapping(value = "/addLog", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewLog(@RequestBody String log) throws IOException {
        Log logD = MAPPER.readValue(log, Log.class);
        logRepository.save(logD);
        /*try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            String sql;
            sql = "insert into log values " +
                    "(DEFAULT, '" + logD.getDescription() + "'"
                    + ", '" + logD.getModule() + "'"
                    + ", '" + logD.getDate() + "')";
            ResultSet rs = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }*/
        return ResponseEntity.status(HttpStatus.OK).body(logD);
    }

    private List<Log> sortLogList(List<Log> logs, String sort_type){
        switch (sort_type){
            case "description":
                Collections.sort(logs, (a, b) -> a.getDescription().compareToIgnoreCase(b.getDescription()));
                break;
            case "date":
                Collections.sort(logs, (a, b) -> a.getDate().compareToIgnoreCase(b.getDate()));
                break;
            case "module":
                Collections.sort(logs, (a, b) -> a.getModule().compareToIgnoreCase(b.getModule()));
                break;
        }
        return logs;
    }
}
