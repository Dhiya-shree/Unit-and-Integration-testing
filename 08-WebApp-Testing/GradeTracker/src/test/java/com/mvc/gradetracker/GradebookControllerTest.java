package com.mvc.gradetracker;

import com.mvc.gradetracker.models.CollegeStudent;
import com.mvc.gradetracker.models.GradebookCollegeStudent;
import com.mvc.gradetracker.repository.MathGradesDao;
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
import org.springframework.beans.factory.annotation.Value;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = GradeTrackerApplication.class)
@TestPropertySource("/application-test.properties")
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

    @Autowired//@Mock
    StudentAndGradeService studentAndGradeService2;

    @Autowired
    StudentDao studentDao;

    @Autowired
    MathGradesDao mathGradesDao;

    @Value("${sql.script.create.student}")
    private String sqlAddStudent;

    @Value("${sql.script.create.math.grade}")
    private String sqlAddMathGrade;

    @Value("${sql.script.create.science.grade}")
    private String sqlAddScienceGrade;

    @Value("${sql.script.create.history.grade}")
    private String sqlAddHistoryGrade;

    @Value("${sql.script.delete.student}")
    private String sqlDelStudent;

    @Value("${sql.script.delete.math.grade}")
    private String sqlDelMathGrade;

    @Value("${sql.script.delete.science.grade}")
    private String sqlDelScienceGrade;

    @Value("${sql.script.delete.history.grade}")
    private String sqlDelHistoryGrade;


    @BeforeAll
    public static void setUp(){
        request=new MockHttpServletRequest();
        request.setParameter("firstname","A");
        request.setParameter("lastname","A");
        request.setParameter("emailAddress","A");
    }

    @BeforeEach
    void setUpDB(){
        jdbc.execute(sqlAddStudent);
        jdbc.execute(sqlAddMathGrade);
        jdbc.execute(sqlAddScienceGrade);
        jdbc.execute(sqlAddHistoryGrade);
        //jdbc.execute("Insert into student(firstname,lastname,email_address) values ('A','B','C')");
    }

    @AfterEach
    void closeDB(){
        jdbc.execute(sqlDelStudent);
        jdbc.execute(sqlDelMathGrade);
        jdbc.execute(sqlDelScienceGrade);
        jdbc.execute(sqlDelHistoryGrade);
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

    // Additional test
    @Test
    public void studentInfoHttpRequest() throws Exception{
        assertTrue(studentDao.findById(1).isPresent());

        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}",1))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"studentInformation");
    }


    @Test
    public void studentDoesNotExist() throws Exception{
        assertFalse(studentDao.findById(0).isPresent());

        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}",0))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"error");
    }

    @Test
    public void createValidGrade() throws Exception{

        assertTrue(studentDao.findById(1).isPresent());

        GradebookCollegeStudent gradebook=studentAndGradeService2.studentInformation(1);

        assertEquals(1,gradebook.getStudentGrades().getMathGradeResults().size());

        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.post("/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grade","85.00")
                .param("gradeType","math")
                .param("studentId","1"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"studentInformation");

        gradebook=studentAndGradeService2.studentInformation(1);

        assertEquals(2,gradebook.getStudentGrades().getMathGradeResults().size());
    }

    @Test
    public void createGradeBookStudentDoesNotExist() throws Exception{
        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("grade","85.00")
                        .param("gradeType","history")
                        .param("studentId","0"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"error");
    }

    @Test
    public void createGradeBookGradeTypeDoesNotExist() throws Exception{
        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("grade","85.00")
                        .param("gradeType","literature")
                        .param("studentId","1"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView,"error");
    }

    @Test
    public void deleteGrade() throws Exception{
        assertTrue(mathGradesDao.findById(1).isPresent());

        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/grades/{id}/{gradeType}",1,"math"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView=mvcResult.getModelAndView();


        ModelAndViewAssert.assertViewName(modelAndView,"studentInformation");

        assertFalse(mathGradesDao.findById(1).isPresent());
    }

    @Test
    public void deleteGradeNoStudent() throws Exception{
        assertFalse(mathGradesDao.findById(0).isPresent());

        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/grades/{id}/{gradeType}",0,"math"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView=mvcResult.getModelAndView();


        ModelAndViewAssert.assertViewName(modelAndView,"error");

    }


    @Test
    public void deleteGradeNoType() throws Exception{
        assertFalse(mathGradesDao.findById(0).isPresent());

        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/grades/{id}/{gradeType}",0,"literature"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView=mvcResult.getModelAndView();


        ModelAndViewAssert.assertViewName(modelAndView,"error");

    }



}
