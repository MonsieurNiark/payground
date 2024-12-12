package fr.purse.payground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

/**
 * Main Payground Application
 */
@SpringBootApplication
@EnableR2dbcRepositories
public class PaygroundApplication {

    /**
     * Main runner
     *
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(PaygroundApplication.class, args);
    }
}
