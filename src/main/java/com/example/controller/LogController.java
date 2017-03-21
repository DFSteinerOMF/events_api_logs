package com.example.controller;

/**
 * Created by Dominik on 21.03.2017.
 */

import com.example.model.Log;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {
    private static final String template = "Hello, %s";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping
    public Log log(@RequestParam(value = "name", defaultValue = "World") String name){
        return new Log(counter.incrementAndGet(), new Date(), "DESC", "SEVERITY", name);
    }
}
