package com.mvc.spring;

import com.mvc.spring.model.CollegeStudent;
import com.mvc.spring.model.StudentGrades;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

@SpringBootTest(classes = Application.class)
public class ReflectionUtlisTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent collegeStudent;

    @Autowired
    StudentGrades studentGrades;

    @BeforeEach
    public  void beforeEach(){
        collegeStudent.setStudentGrades(studentGrades);
        collegeStudent.setFirstname("f");
        collegeStudent.setLastname("f");
        collegeStudent.setEmailAddress("e");


        ReflectionTestUtils.setField(collegeStudent,"id",111);
        ReflectionTestUtils.setField(collegeStudent,"studentGrades",
                new StudentGrades(List.of(1.1,2.1,3.1)));
    }

    @Test
    public void getPrivateField(){
        assertEquals(111,ReflectionTestUtils.getField(collegeStudent,"id"),
                "ID set and get by ReflectionTestUtlis : No getter and setter for ID -> it'z private");
    }

    @Test
    public void invokePrivateMethod(){
        assertEquals("f 111",ReflectionTestUtils.invokeMethod(collegeStudent,"getFirstNameAndId")
        ,"Private method call ");
    }
}
