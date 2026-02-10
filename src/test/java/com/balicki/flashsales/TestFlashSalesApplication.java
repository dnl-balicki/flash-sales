package com.balicki.flashsales;

import org.springframework.boot.SpringApplication;

public class TestFlashSalesApplication {

    public static void main(String[] args) {
        SpringApplication.from(FlashSalesApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
