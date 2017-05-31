package com.example.controller;

import com.example.model.Audit;
import com.example.model.AuditRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.TextCodec;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Dominik on 10.05.2017.
 */

@RestController
@RequestMapping("/api/audit")
public class AuduitController {
    @Value("${bl.secret}") String blKey;
    @Value("${db.secret}") String dbKey;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    AuditRepository auditRepository;

    @Autowired
    public AuduitController(AuditRepository auditRepository){
        this.auditRepository = auditRepository;
    }

    @RequestMapping(value = "/showAllAudits/[sort_type]", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> showAllAudits(@RequestHeader("Authorization") String bearerAuthorization,
                                           @PathVariable("sort_type") String sort_type) throws JsonProcessingException{
        if (!checkIfBearerIsOk(bearerAuthorization)) {
            //throw new SignatureException(bearerAuthorization);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("FORBIDDEN, NO BEARER");
        }
        final String token = bearerAuthorization.substring(7);
        List<Audit> auditList = auditRepository.findAll();
        sortAuditList(auditList, sort_type);
        try {
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(TextCodec.BASE64.encode(blKey)))
                    .parseClaimsJws(token);
            //OK, we can trust this JWT
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(MAPPER.writeValueAsString(auditList));
        } catch (SignatureException e) {
            //don't trust the JWT!
        }
        try {
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(TextCodec.BASE64.encode(blKey)))
                    .parseClaimsJws(token);
            //OK, we can trust this JWT
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(MAPPER.writeValueAsString(auditList));
        } catch (SignatureException e) {
            //don't trust the JWT!
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("FORBIDDEN");
    }

    @RequestMapping(
            value =
            {"/showAudits/module/[module]/[sort_type]/",
            "/showAudits/user_action/[user_action]/[sort_type]/",
            "/showAudits/[module]/[user_action]/[sort_type]/"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>showAllAuditsWithGivenModuleAndUserAction(@RequestHeader("Authorization") String bearerAuthorization,
                                                                      @PathVariable("sort_type") String sort_type,
                                                                      @PathVariable("module") Optional<String> module,
                                                                      @PathVariable("user_action") Optional<String> userAction) throws JsonProcessingException {
        if (!checkIfBearerIsOk(bearerAuthorization)) {
            //throw new SignatureException(bearerAuthorization);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("FORBIDDEN, NO BEARER");
        }
        final String token = bearerAuthorization.substring(7);
        List<Audit> auditList;
        if(module.isPresent() && userAction.isPresent()){
            auditList = auditRepository.findAllByModuleAndUserAction(module.get(), userAction.get());
        }
        else if(module.isPresent()) {
            auditList = auditRepository.findAllByModule(module.get());
        }
        else{
            auditList = auditRepository.findAllByUserAction(userAction.get());
        }
        sortAuditList(auditList, sort_type);
        try {
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(TextCodec.BASE64.encode(blKey)))
                    .parseClaimsJws(token);
            //OK, we can trust this JWT
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(MAPPER.writeValueAsString(auditList));
        } catch (SignatureException e) {
            //don't trust the JWT!
        }
        try {
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(TextCodec.BASE64.encode(blKey)))
                    .parseClaimsJws(token);
            //OK, we can trust this JWT
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(MAPPER.writeValueAsString(auditList));
        } catch (SignatureException e) {
            //don't trust the JWT!
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("FORBIDDEN");
    }

    @ApiOperation(value = "Add new audit")
    @RequestMapping(value = "/addAudit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewAudit(@RequestHeader("Authorization") String bearerAuthorization, @RequestBody Audit audit) throws Exception {
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
            auditRepository.save(audit);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(audit);
        } catch (SignatureException e) {
            //don't trust the JWT!
        }
        try {
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(TextCodec.BASE64.encode(dbKey)))
                    .parseClaimsJws(token);
            //OK, we can trust this JWT
            auditRepository.save(audit);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(audit);
        } catch (SignatureException e) {
            //don't trust the JWT!
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("FORBIDDEN");
    }

    private List<Audit> sortAuditList(List<Audit> auditList, String sort_type){
        switch (sort_type){
            case "description":
                Collections.sort(auditList, (a, b) -> a.getDescription().compareToIgnoreCase(b.getDescription()));
                break;
            case "date":
                Collections.sort(auditList, (a, b) -> a.getDate().compareToIgnoreCase(b.getDate()));
                break;
            case "module":
                Collections.sort(auditList, (a, b) -> a.getModule().compareToIgnoreCase(b.getModule()));
                break;
            case "user_action":
                Collections.sort(auditList, (a, b) -> a.getUserAction().compareToIgnoreCase(b.getUserAction()));
        }
        return auditList;
    }

    private boolean checkIfBearerIsOk(String bearerAuthorization){
        if (bearerAuthorization == null || !bearerAuthorization.startsWith("Bearer ")) {
            //throw new SignatureException(bearerAuthorization);
            return false;
        }
        return true;
    }
}
