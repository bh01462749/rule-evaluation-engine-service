package com.hack.rule_evaluation_engine_service.controller;

import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/")
@Slf4j
@CrossOrigin(origins = "*")
public class HomeController {

    @GetMapping("/")
    public String check() {
        log.info("running");
        return "running";
    }

}