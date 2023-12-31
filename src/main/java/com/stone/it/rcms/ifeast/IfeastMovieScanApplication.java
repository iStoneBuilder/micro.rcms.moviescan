package com.stone.it.rcms.ifeast;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author cj.stone
 * @Date 2022/12/9
 * @Desc
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.stone.it"})
public class IfeastMovieScanApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(IfeastMovieScanApplication.class);
    public static void main(String[] args) {
        LOGGER.info("Ifeast Application Start now ........");
        SpringApplication.run(IfeastMovieScanApplication.class, args);
        LOGGER.info("Ifeast Application Running ........");
    }


}
