package com.eazybook.marcus.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/logging")
@Slf4j
public class LoggingController {

//    private static final Logger log = LoggerFactory.getLogger(LoggingController.class);
    @GetMapping
    public ResponseEntity<String> testLogging() {
        log.trace(" TRACE: This is a very detailed trace log. Used for tracking execution flow.");
        log.debug(" DEBUG: This is a debug message. Used for debugging.");
        log.info("ℹ INFO: This is an informational message. Application events.");
        log.warn(" WARN: This is a warning! Something might go wrong.");
        log.error(" ERROR: An error occurred! This needs immediate attention.");
        return ResponseEntity.ok("Logging tested sccessfully");
    }
}
