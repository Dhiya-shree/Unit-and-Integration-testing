package com.mvc.spring;

import com.mvc.spring.dao.ApplicationDao;
import com.mvc.spring.model.CollegeStudent;
import com.mvc.spring.service.ApplicationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

    @Bean(name="applicationExample")
    ApplicationService getApplicationService(){
        return new ApplicationService();
    }

    @Bean(name="applicationDao")
    ApplicationDao geApplicationDao(){
        return new ApplicationDao();
    }

    @Bean(name="collegeStudent")
    @Scope(value = "prototype")
    CollegeStudent getCollegeStudent(){
        return new CollegeStudent();
    }

}
