package com.techelevator.tenmo;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TenmoApplication {



    public static void main(String[] args) {

        SpringApplication.run(TenmoApplication.class, args);

    }

}
