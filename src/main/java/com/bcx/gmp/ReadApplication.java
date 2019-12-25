package com.bcx.gmp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Design By Scrooged
 * @version 1.0
 * @description
 * @date 2019/2/15 9:39
 */
@SpringBootApplication
public class ReadApplication {

    private static Logger logger = LoggerFactory.getLogger(ReadApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(ReadApplication.class, args);

        logger.info("----------踏遍青山人未老，风景这边独好----------");
    }


}
