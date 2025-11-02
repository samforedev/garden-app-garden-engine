package org.course.gardenappgardenengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GardenAppGardenEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(GardenAppGardenEngineApplication.class, args);
    }

}
