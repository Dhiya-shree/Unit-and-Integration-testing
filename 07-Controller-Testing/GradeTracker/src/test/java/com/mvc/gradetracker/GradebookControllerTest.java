package com.mvc.gradetracker;

import com.mvc.gradetracker.models.CollegeStudent;
import com.mvc.gradetracker.repository.StudentDao;
import com.mvc.gradetracker.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = GradeTrackerApplication.class)
@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class GradebookControllerTest {

    static MockHttpServletRequest request;

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    MockMvc mockMvc;

    @Mock
    StudentAndGradeService studentAndGradeService;

    @Autowired
    StudentDao studentDao;

    @BeforeAll
    public static void setUp(){
        request=new MockHttpServletRequest();
        request.setParameter("firstname","A");
        request.setParameter("lastname","A");
        request.setParameter("emailAddress","A");
    }

    @BeforeEach
    void setUpDB(){
        jdbc.execute("Insert into student(firstname,lastname,email_address) values ('A','B','C')");
    }

    @AfterEach
    void closeDB(){
        jdbc.execute("delete from student");
        jdbc.execute("alter table student alter column ID restart with 1");
    }


    @Test
    public void getHttpRequest() throws Exception{
        CollegeStudent stud=new CollegeStudent("B","B","B");
        CollegeStudent stud2=new CollegeStudent("C","C","C");
        List<CollegeStudent> studs=List.of(stud,stud2);
        when(studentAndGradeService.getGradebook()).thenReturn(studs);
        assertIterableEquals(studs,studentAndGradeService.getGradebook());

        MvcResult mvcResult=mockMvc.perform(
                MockMvcRequestBuilders.get("/")
        ).andExpect(status().isOk()).andReturn();

        ModelAndView modelAndView=mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(modelAndView,"index");
    }

    @Test
    public void createHttpStudentRequest() throws Exception{
        CollegeStudent stud=new CollegeStudent("B","B","B");
        List<CollegeStudent> studs=List.of(stud);

        when(studentAndGradeService.getGradebook()).thenReturn(studs);
        assertIterableEquals(studs,studentAndGradeService.getGradebook());

        MvcResult mvcResult=mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstname",request.getParameterValues("firstname"))
                .param("lastname",request.getParameterValues("lastname"))
                .param("emailAddress",request.getParameterValues("emailAddress")))
                .andExpect(status().isOk()).andReturn();

        ModelAndView modelAndView=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"index");

        CollegeStudent stud2=studentDao.findByEmailAddress("A");
        assertNotNull(stud2);
    }


    @Test
    void deleteStudentHttpRequest() throws Exception{
        assertTrue(studentDao.findById(1).isPresent());
        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/{id}",1))
                .andExpect(status().isOk()).andReturn();

        ModelAndView modelAndView=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"index");

        assertFalse(studentDao.findById(1).isPresent());

    }

    @Test
    void deleteStudentErrorPage() throws Exception {
        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/{id}",0))
                .andExpect(status().isOk()).andReturn();

        ModelAndView modelAndView=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"error");
    }

}
