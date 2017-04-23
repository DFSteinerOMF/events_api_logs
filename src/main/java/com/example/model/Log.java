package com.example.model;

import com.example.LogDeserializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.validator.constraints.NotEmpty;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Dominik on 21.03.2017.
 */
@JsonAutoDetect
@JsonDeserialize(using = LogDeserializer.class)
@Entity
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

    public Log(){

    }

    public Log(int id, String date, String description, String module) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.module = module;
    }

    public Log getObjectFromSQL(ResultSet resultSQL) throws SQLException{
        int id = resultSQL.getInt("ID");
        String description = resultSQL.getString("description");
        String module = resultSQL.getString("module_name");
        String date = resultSQL.getString("date");
        return new Log(id,description,module,date);
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

}
