package com.example.controller;

/**
 * Created by Dominik on 21.03.2017.
 */

import com.example.model.Log;
import com.example.model.LogRepository;
import com.example.myJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.TextCodec;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/logs")
public class LogController {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Value("${bl.secret}") String blKey;
    @Value("${db.secret}") String dbKey;
    private LogRepository logRepository;

    @Autowired
    public LogController(LogRepository logRepository){
        this.logRepository = logRepository;
    }

    @ApiOperation(value = "Get all logs", notes = "Get all logs from db", nickname = "get_all_logs")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = List.class)})
            @ApiResponse(code = 404, message = "Not Found")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sort_type", value = "Sort type module/date/description", required = true, dataType = "string", paramType = "path", defaultValue = "null")
    })
    @RequestMapping(value = "/showAllLogs/{sort_type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> showAllLogs(@RequestHeader("Authorization") String bearerAuthorization, @PathVariable("sort_type") String sort_type) throws JsonProcessingException {
        if (!checkIfBearerIsOk(bearerAuthorization)) {
            //throw new SignatureException(bearerAuthorization);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("FORBIDDEN, NO BEARER");
        }
        final String token = bearerAuthorization.substring(7);
        List<Log> logs = logRepository.findAll();
        sortLogList(logs, sort_type);
        try {
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(TextCodec.BASE64.encode(blKey)))
                    .parseClaimsJws(token);
            //OK, we can trust this JWT
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(MAPPER.writeValueAsString(logs));
        } catch (SignatureException e) {
            //don't trust the JWT!
        }
        try {
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(TextCodec.BASE64.encode(dbKey)))
                    .parseClaimsJws(token);
            //OK, we can trust this JWT
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(MAPPER.writeValueAsString(logs));
        } catch (SignatureException e) {
            //don't trust the JWT!
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("FORBIDDEN");
    }

    @RequestMapping(
            value = "/showLogs/module/{module_name}/{sort_type}/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> showLogsWithGivenModule(@RequestHeader("Authorization") String bearerAuthorization,
                                                                   @PathVariable(value = "module_name") String module,
                                                                   @PathVariable(value = "sort_type") String sort_type) throws JsonProcessingException {
        if (!checkIfBearerIsOk(bearerAuthorization)) {
            //throw new SignatureException(bearerAuthorization);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("FORBIDDEN, NO BEARER");
        }
        final String token = bearerAuthorization.substring(7);
        List<Log> logs;
        logs = logRepository.findAllByModule(module);
        sortLogList(logs, sort_type);
        try {
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(TextCodec.BASE64.encode(blKey)))
                    .parseClaimsJws(token);
            //OK, we can trust this JWT
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(MAPPER.writeValueAsString(logs));
        } catch (SignatureException e) {
            //don't trust the JWT!
        }
        try {
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(TextCodec.BASE64.encode(dbKey)))
                    .parseClaimsJws(token);
            //OK, we can trust this JWT
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(MAPPER.writeValueAsString(logs));
        } catch (SignatureException e) {
            //don't trust the JWT!
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("FORBIDDEN");
    }

    @RequestMapping(
            value = "/showLogs/{module_name}/{severity_name}/{sort_type}/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> showLogsWithGivenSeverity(@RequestHeader("Authorization") String bearerAuthorization,
                                                       @PathVariable(value = "module_name") String module,
                                                       @PathVariable(value = "severity_name") String severity,
                                                       @PathVariable(value = "sort_type") String sort_type) throws JsonProcessingException {
        if (!checkIfBearerIsOk(bearerAuthorization)) {
            //throw new SignatureException(bearerAuthorization);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("FORBIDDEN, NO BEARER");
        }
        final String token = bearerAuthorization.substring(7);
        List<Log> logs;
        logs = logRepository.findAllByModuleAndSeverity(module,severity);
        sortLogList(logs, sort_type);
        try {
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(TextCodec.BASE64.encode(blKey)))
                    .parseClaimsJws(token);
            //OK, we can trust this JWT
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(MAPPER.writeValueAsString(logs));
        } catch (SignatureException e) {
            //don't trust the JWT!
        }
        try {
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(TextCodec.BASE64.encode(dbKey)))
                    .parseClaimsJws(token);
            //OK, we can trust this JWT
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(MAPPER.writeValueAsString(logs));
        } catch (SignatureException e) {
            //don't trust the JWT!
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("FORBIDDEN");
    }

    @RequestMapping(
            value = "/showLogs/severity_name/{severity_name}/{sort_type}/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> showLogsWithGivenModuleAndSeverity(@RequestHeader("Authorization") String bearerAuthorization,
                                                       @PathVariable(value = "severity_name") String severity,
                                                       @PathVariable(value = "sort_type") String sort_type) throws JsonProcessingException {
        if (!checkIfBearerIsOk(bearerAuthorization)) {
            //throw new SignatureException(bearerAuthorization);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("FORBIDDEN, NO BEARER");
        }
        final String token = bearerAuthorization.substring(7);
        List<Log> logs;
        logs = logRepository.findAllBySeverity(severity);
        sortLogList(logs, sort_type);
        try {
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(TextCodec.BASE64.encode(blKey)))
                    .parseClaimsJws(token);
            //OK, we can trust this JWT
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(MAPPER.writeValueAsString(logs));
        } catch (SignatureException e) {
            //don't trust the JWT!
        }
        try {
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(TextCodec.BASE64.encode(dbKey)))
                    .parseClaimsJws(token);
            //OK, we can trust this JWT
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(MAPPER.writeValueAsString(logs));
        } catch (SignatureException e) {
            //don't trust the JWT!
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("FORBIDDEN");
    }

    //poprawic by errory wypisywaly
    @ApiOperation(value = "Add new log")
    @RequestMapping(value = "/addLog", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewLog(@RequestHeader("Authorization") String bearerAuthorization, @RequestBody Log log) throws Exception {
        if (!checkIfBearerIsOk(bearerAuthorization)) {
            //throw new SignatureException(bearerAuthorization);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("FORBIDDEN, NO BEARER");
        }
        final String token = bearerAuthorization.substring(7);
        try {
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(TextCodec.BASE64.encode(dbKey)))
                    .parseClaimsJws(token);
            //OK, we can trust this JWT
            logRepository.save(log);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(log);
        } catch (SignatureException e) {
            //don't trust the JWT!
        }
        try {
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(TextCodec.BASE64.encode(blKey)))
                    .parseClaimsJws(token);
            //OK, we can trust this JWT
            logRepository.save(log);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(log);
        } catch (SignatureException e) {
            //don't trust the JWT!
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("FORBIDDEN");
    }

    private boolean checkIfBearerIsOk(String bearerAuthorization){
        if (bearerAuthorization == null || !bearerAuthorization.startsWith("Bearer ")) {
            //throw new SignatureException(bearerAuthorization);
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/calcStats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> calculateSatistics(){
        List<Log> logs = logRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }

    @RequestMapping(value = "/genToken", method = RequestMethod.GET)
    public String genToken(){
        myJWT jwt = new myJWT();
        String token = jwt.createHS256Token("0","LOG","DB", 100);
        return token + "\n";
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
            case "severity":
                Collections.sort(logs, (a, b) -> a.getSeverity().compareToIgnoreCase(b.getSeverity()));
        }
        return logs;
    }
}
