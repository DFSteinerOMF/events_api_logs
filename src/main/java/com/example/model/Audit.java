package com.example.model;

import com.example.AuditDeserializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Dominik on 10.05.2017.
 */
@JsonAutoDetect
@JsonDeserialize(using = AuditDeserializer.class)
@Entity
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotEmpty
    String date;

    @NotEmpty
    String module;

    @NotEmpty
    String userAction;

    @NotEmpty
    String description;

    public Audit(int id, String date, String module, String userAction, String description){
        this.date = date;
        this.module = module;
        this.userAction = userAction;
        this.description = description;
    }

    public Audit(){

    }

    public Audit getObjectFromSQL(ResultSet resultSQL) throws SQLException{
        int id = resultSQL.getInt("ID");
        String description = resultSQL.getString("description");
        String module = resultSQL.getString("module_name");
        String date = resultSQL.getString("date");
        String userAction = resultSQL.getString("userAction");
        return new Audit(id, date, module, userAction, description);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getUserAction() {
        return userAction;
    }

    public void setUserAction(String userAction) {
        this.userAction = userAction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
