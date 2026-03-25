package com.mvc.spring;

import com.mvc.spring.dao.ApplicationDao;
import com.mvc.spring.model.CollegeStudent;
import com.mvc.spring.model.StudentGrades;
import com.mvc.spring.service.ApplicationService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = Application.class)
public class MockAnnotationTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent collegeStudent;

    @Autowired
    StudentGrades studentGrades;

//    @Mock
//    ApplicationDao applicationDao;
//
//    @InjectMocks
//    ApplicationService applicationService;

    // Using Mockito bean - spring autowire the mock in all the beans needed
    // No need to use InjectMocks seperately
     @MockitoBean
     ApplicationDao applicationDao;

     @Autowired
     ApplicationService applicationService;

    @BeforeEach
    public void beforeEach(){
        collegeStudent.setEmailAddress("e");
        collegeStudent.setFirstname("f");
        collegeStudent.setLastname("l");
        collegeStudent.setStudentGrades(studentGrades);
    }

    @DisplayName("When and Verify ")
    @Test
    public void testGradeMock(){
        when(applicationDao.addGradeResultsForSingleClass
                (studentGrades.getMathGradeResults())).thenReturn(100.10);

        assertEquals(100.10,applicationService.addGradeResultsForSingleClass(studentGrades.getMathGradeResults()));

        verify(applicationDao).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());

        verify(applicationDao,times(1)).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());
    }


    @DisplayName("Find gpa")
    @Test
    public void findGpa(){
        when(applicationDao.findGradePointAverage(studentGrades.getMathGradeResults())).thenReturn(1000.00);
        assertNotEquals(100.00,applicationService.findGradePointAverage(studentGrades.getMathGradeResults()));
    }

    @DisplayName("Not null")
    @Test
    public void notNull(){

        when(applicationDao.checkNull(studentGrades.getMathGradeResults())).thenReturn(true);

        assertNotNull(applicationService.checkNull(studentGrades.getMathGradeResults()));

         }


    @DisplayName("Throw error")
    @Test
    public void throwerror(){
        CollegeStudent nullStud=(CollegeStudent) context.getBean("collegeStudent");

        doThrow(new RuntimeException()).when(applicationDao).checkNull(nullStud);

        assertThrows(RuntimeException.class,
                ()->applicationService.checkNull(nullStud));

        // Arguments passed in testing and the verify must be same
        verify(applicationDao,times(1)).checkNull(nullStud);


    }

    @DisplayName("Multi stubbing")
    @Test
    public void multiStub(){
        CollegeStudent nullStud=(CollegeStudent) context.getBean("collegeStudent");

        when(applicationDao.checkNull(nullStud)).thenThrow(new RuntimeException())
                .thenReturn(10.00);

        assertThrows(RuntimeException.class,()->applicationService.checkNull(nullStud));

        assertEquals(10.00,applicationService.checkNull(nullStud),"Second time throw 10");

        verify(applicationDao,times(2)).checkNull(nullStud);


    }


}
