package com.example.model;

import com.example.LogDeserializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by Dominik on 21.03.2017.
 */
@JsonAutoDetect
@JsonDeserialize(using = LogDeserializer.class)
public class Log {

    private int id;
    private String date;
    private String description;
    private String module;

    public Log(int id, String date, String description, String module) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.module = module;
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
