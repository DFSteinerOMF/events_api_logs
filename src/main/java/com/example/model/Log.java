package com.example.model;

import java.util.Date;

/**
 * Created by Dominik on 21.03.2017.
 */
public class Log {

    private long id;
    private Date date;
    private String description;
    private String severity;
    private String module;

    public Log(long id, Date date, String description, String severity, String module) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.severity = severity;
        this.module = module;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

}
