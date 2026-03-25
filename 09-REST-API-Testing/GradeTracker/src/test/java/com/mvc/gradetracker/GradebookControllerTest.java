package com.mvc.gradetracker;

import com.mvc.gradetracker.models.CollegeStudent;
import com.mvc.gradetracker.repository.HistoryGradesDao;
import com.mvc.gradetracker.repository.MathGradesDao;
import com.mvc.gradetracker.repository.ScienceGradesDao;
import com.mvc.gradetracker.repository.StudentDao;
import com.mvc.gradetracker.service.StudentAndGradeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = GradeTrackerApplication.class)
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@Transactional
@ExtendWith(MockitoExtension.class)
public class GradebookControllerTest {
    private static MockHttpServletRequest request;

    @PersistenceContext
    private EntityManager entityManager;

    @Mock
    StudentAndGradeService serviceMock;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private MathGradesDao mathGradesDao;

    @Autowired
    private ScienceGradesDao scienceGradesDao;

    @Autowired
    private HistoryGradesDao historyGradesDao;


    @Autowired
    private StudentAndGradeService service;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CollegeStudent student;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${sql.script.create.student}")
    String sqlAddStudent;
    @Value("${sql.script.create.math.grade}")
    String sqlAddMath;
    @Value("${sql.script.create.science.grade}")
    String sqlAddScience;
    @Value("${sql.script.create.history.grade}")
    String sqlAddHistory;

    @Value("${sql.script.delete.student}")
    String sqlDelStudent;
    @Value("${sql.script.delete.math.grade}")
    String sqlDelMath;
    @Value("${sql.script.delete.science.grade}")
    String sqlDelScience;
    @Value("${sql.script.delete.history.grade}")
    String sqlDelHistory;

    @BeforeAll
    static void setReq(){
        request=new MockHttpServletRequest();
        request.setParameter("firstName","A");
        request.setParameter("lastName","A");
        request.setParameter("emailAddress","A");
    }

    @BeforeEach
     void setDB(){
        jdbc.execute(sqlAddHistory);
        jdbc.execute(sqlAddStudent);
        jdbc.execute(sqlAddScience);
        jdbc.execute(sqlAddMath);
    }

    @AfterEach
    void closeDB(){
        jdbc.execute(sqlDelStudent);
        jdbc.execute(sqlDelHistory);
        jdbc.execute(sqlDelMath);
        jdbc.execute(sqlDelScience);
    }

    @Test
    public void createStudent() throws Exception{

        student.setFirstname("B");
        student.setLastname("B");
        student.setEmailAddress("B");

        mockMvc.perform(MockMvcRequestBuilders.post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        assertNotNull(studentDao.findByEmailAddress("B"));

    }


    @Test
    public void getStudengt() throws Exception{

        student.setFirstname("B");
        student.setLastname("B");
        student.setEmailAddress("B");
        entityManager.persist(student); entityManager.flush();

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        assertNotNull(studentDao.findByEmailAddress("B"));

    }

    @Test
    void deleteStud() throws Exception{
        assertTrue(studentDao.findById(1).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/{id}",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(0)));

        assertFalse(studentDao.findById(1).isPresent());
    }

    @Test
    void deleteStudNotPresent() throws Exception{
        assertFalse(studentDao.findById(0).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/{id}",0))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status",is(404)))
                .andExpect(jsonPath("$.message",is("Student or Grade was not found")));

    }


    @Test
    void studInfo() throws Exception{
        assertTrue(studentDao.findById(1).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}",1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(1)))
                .andExpect(jsonPath("$.firstname",is("A")))
                .andExpect(jsonPath("$.lastname",is("A")))
                .andExpect(jsonPath("$.emailAddress",is("A@A.com")));
    }

    @Test
    void studInfoNoStud() throws Exception{
        assertFalse(studentDao.findById(0).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}",0))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status",is(404)))
                .andExpect(jsonPath("$.message",is("Student or Grade was not found")));


    }

    @Test
    void createGrade() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grade","90")
                .param("gradeType","math")
                .param("studentId","1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(1)))
                .andExpect(jsonPath("$.firstname",is("A")))
                .andExpect(jsonPath("$.lastname",is("A")))
                .andExpect(jsonPath("$.emailAddress",is("A@A.com")))
                .andExpect(jsonPath("$.studentGrades.mathGradeResults",hasSize(2)));
    }

    @Test
    void createGradeNoStudent() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("grade","90")
                        .param("gradeType","math")
                        .param("studentId","0"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status",is(404)))
                .andExpect(jsonPath("$.message",is("Student or Grade was not found")));

    }

    @Test
    void createGradeNoGradeType() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("grade","90")
                        .param("gradeType","literature")
                        .param("studentId","1"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status",is(404)))
                .andExpect(jsonPath("$.message",is("Student or Grade was not found")));

    }

    @Test
    void deleteValidGrade() throws  Exception{
        assertTrue(mathGradesDao.findById(1).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/grades/{id}/{gradeType}",1,"math"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id",is(1)))
                .andExpect(jsonPath("$.firstname",is("A")))
                .andExpect(jsonPath("$.lastname",is("A")))
                .andExpect(jsonPath("$.emailAddress",is("A@A.com")))
                .andExpect(jsonPath("$.studentGrades.mathGradeResults",hasSize(0)));

        assertFalse(mathGradesDao.findById(1).isPresent());
    }

    @Test
    void deleteInValidGrade() throws Exception{
        assertTrue(mathGradesDao.findById(1).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/grades/{id}/{gradeType}",0,"matheMatics"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status",is(404)))
                .andExpect(jsonPath("$.message",is("Student or Grade was not found")));

    }





}
