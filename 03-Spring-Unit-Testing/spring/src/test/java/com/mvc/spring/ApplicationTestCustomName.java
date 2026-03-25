package com.mvc.spring;

import com.mvc.spring.model.CollegeStudent;
import com.mvc.spring.model.StudentGrades;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = Application.class)
class ApplicationTestCustomName {

    @Value("${info.app.name}")
    private String appInfo;

    @Value("${info.app.description}")
    private String descrciption;

    @Value("${info.app.version}")
    private String appVersion;

    @Value("${info.school.name}")
    private String schoolName;

    @Autowired
    CollegeStudent student;

    @Autowired
    StudentGrades studentGrades;

    @Autowired
    ApplicationContext context;

    static int count=0;
    @BeforeEach
    public void BeforeEach(){
        count=count+1;
        System.out.println(" Testing "+appInfo+" "+descrciption+" Version "+appVersion
        +" For "+schoolName);

        student.setFirstname("firstName");
        student.setLastname("lastName");
        student.setEmailAddress("abc@email.com");
        studentGrades.setMathGradeResults(List.of(100.0, 85.0, 76.5, 99.9));
        student.setStudentGrades(studentGrades);
    }

    @Test
    @DisplayName("Add grade result for student")
    public  void addResultTest(){
        assertNotEquals(0,studentGrades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults()));
    }

    @Test
    @DisplayName("Grade greater")
    public void testGreat(){
        assertTrue(studentGrades.isGradeGreater(10,100));
    }

    @Test
    @DisplayName("Check for null")
    public void checkNull(){
        assertNotNull(studentGrades.checkNull(student.getStudentGrades().getMathGradeResults()));
    }

    @Test
    @DisplayName("Create stud without grade")
    public void createStudenet(){
        CollegeStudent student2=context.getBean("collegeStudent", CollegeStudent.class);
        student2.setFirstname("f2");
        student2.setLastname("l2");
        student2.setEmailAddress("e2");
        assertNull(student2.getStudentGrades());
    }

    @DisplayName(" Compare students")
    @Test
    public void compare(){
        CollegeStudent student2=context.getBean("collegeStudent",CollegeStudent.class);
        System.out.println(student2.toString());
        assertNotSame(student,student2);
    }

    @DisplayName("Find GPA")
    @Test
    public void findGpa(){
        assertAll("Assert GPA equal for multiple ",
        ()->assertEquals(353.25,studentGrades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults())),
        ()->assertEquals(88.31,studentGrades.findGradePointAverage(student.getStudentGrades().getMathGradeResults())),
        ()->assertEquals(353.25,studentGrades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults())),
        ()->assertEquals(88.31,studentGrades.findGradePointAverage(student.getStudentGrades().getMathGradeResults()))
        );
    }


    @Test
    void contextLoads() {
        System.out.println(" Context loads called ");
    }

}
