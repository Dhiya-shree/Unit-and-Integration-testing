package com.mvc.gradetracker;

import com.mvc.gradetracker.models.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class GradeTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GradeTrackerApplication.class, args);
    }

    @Bean
    @Scope(value = "prototype")
    CollegeStudent collegeStudent(){
        return new CollegeStudent();
    }

    @Bean
    @Scope(value="prototype")
    Grade getMathGrade(){
        return new MathGrade();
    }

    @Bean
    @Scope(value="prototype")
    @Qualifier("mathGrades")
    MathGrade getMath(){
        return new MathGrade();
    }

    @Bean
    @Scope(value="prototype")
    @Qualifier("scienceGrades")
    ScienceGrade getScience(){
        return new ScienceGrade();
    }

    @Bean
    @Scope(value = "prototype")
    @Qualifier("historyGrades")
    HistoryGrade getHistoryGrade(){
        return new HistoryGrade();
    }

}
