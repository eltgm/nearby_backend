package ru.vsultanyarov.nearby_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
public class NearbyBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NearbyBackendApplication.class, args);
    }
}
