package org.example.mymallapplication;

import cn.dev33.satoken.SaManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author chengyiyang
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
@MapperScan("org.example.mymallapplication.dal.dao.mapper")
public class MyMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyMallApplication.class, args);
        System.out.println("Sa-token start as: " + SaManager.getConfig());
    }

}
