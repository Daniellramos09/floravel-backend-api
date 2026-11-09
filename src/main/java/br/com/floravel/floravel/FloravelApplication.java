package br.com.floravel.floravel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class FloravelApplication {

    public static void main(String[] args) {
        SpringApplication.run(FloravelApplication.class, args);
    }

}
