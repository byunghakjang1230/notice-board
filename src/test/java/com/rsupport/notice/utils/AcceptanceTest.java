package com.rsupport.notice.utils;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    @LocalServerPort
    int port;
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
//        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
//        }
        RestAssured.port = port;
        databaseCleanup.execute();
    }
}
