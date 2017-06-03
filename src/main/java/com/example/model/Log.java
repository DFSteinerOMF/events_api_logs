package com.example.model;

import com.example.LogDeserializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Dominik on 21.03.2017.
 */
@JsonAutoDetect
@JsonDeserialize(using = LogDeserializer.class)
@Entity
@Table(name = "logs")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    private String date;

    @NotEmpty
    private String description;

    @NotEmpty
    private String module;

    @NotEmpty
    private  String severity;

    public Log(){

    }

    public Log(int id, String date, String description, String module, String severity) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.module = module;
        this.severity = severity;
    }

    public Log getObjectFromSQL(ResultSet resultSQL) throws SQLException{
        int id = resultSQL.getInt("ID");
        String description = resultSQL.getString("description");
        String module = resultSQL.getString("module_name");
        String date = resultSQL.getString("date");
        return new Log(id,description,module,date,severity);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
