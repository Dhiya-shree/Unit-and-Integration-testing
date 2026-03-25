package com.mvc.gradetracker;

import com.mvc.gradetracker.models.*;
import com.mvc.gradetracker.repository.HistoryGradesDao;
import com.mvc.gradetracker.repository.MathGradesDao;
import com.mvc.gradetracker.repository.ScienceGradesDao;
import com.mvc.gradetracker.repository.StudentDao;
import com.mvc.gradetracker.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc
class GradeTrackerApplicationTests {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private StudentAndGradeService service;

    @Autowired
    private MathGradesDao mathGradesDao;

    @Autowired
    private ScienceGradesDao scienceGradesDao;

    @Autowired
    private HistoryGradesDao historyGradesDao;

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
        System.out.println(" After each 1");

        jdbc.execute(sqlDelStudent);
        jdbc.execute(sqlDelMathGrade);
        jdbc.execute(sqlDelScienceGrade);
        jdbc.execute(sqlDelHistoryGrade);
        jdbc.execute("alter table student alter column ID restart with 1");
    }
    @Test
    public void createStudentService(){
        service.createStudent("A2","A2","A2");
        CollegeStudent student=studentDao.findByEmailAddress("A2");

        assertEquals("A2",student.getEmailAddress());
    }


    @Test
    public void studenntNull(){
        assertTrue(service.checkIfStudentIsNull(1));
        assertFalse(service.checkIfStudentIsNull(0));
    }

    @Test
    public void deleteStudent(){
       assertTrue(studentDao.findById(1).isPresent());
       service.deleteStudent(1);

       assertFalse(studentDao.findById(1).isPresent());
       assertFalse(mathGradesDao.findById(1).isPresent());
       assertFalse(scienceGradesDao.findById(1).isPresent());
       assertFalse(historyGradesDao.findById(1).isPresent());
    }

    @Sql("/insert_init.sql")
    @Test
    public void InitializeThroughSSQL(){
        Iterable<CollegeStudent> studs=service.getGradebook();
        AtomicInteger count= new AtomicInteger();
        studs.iterator().forEachRemaining((collegeStudent)->{
            count.getAndIncrement();
        });
        assertEquals(5,count.get());
    }

    @Test
    public void createGradeService(){
        // Create grade and check if DB success
        assertTrue(service.createGrade(80,1,"math"));
        assertTrue(service.createGrade(90,1,"science"));
        assertTrue(service.createGrade(100,1,"history"));

        // Get grade and check size
        assertTrue(((Collection<MathGrade>)mathGradesDao.findGradeByStudentId(1)).size()==2);
        assertTrue(((Collection<ScienceGrade>)scienceGradesDao.findGradeByStudentId(1)).size()==2,"message "+((Collection<ScienceGrade>)scienceGradesDao.findGradeByStudentId(1)).toString());
        assertTrue(((Collection<HistoryGrade>)historyGradesDao.findGradeByStudentId(1)).size()==2);

    }

    @Test
    public void createWrongGrade(){
        assertFalse(service.createGrade(115,1,"math"));// wrong val
        assertFalse(service.createGrade(-1,1,"math"));//wrong val<0
        assertFalse(service.createGrade(80,2,"math"));// no student
        assertFalse(service.createGrade(80,1,"literature"));// no type
    }


    @Test
    public void deleteGrade(){
        assertEquals(1,service.deleteGrade(1,"math"));

        assertEquals(1,service.deleteGrade(1,"science"));

        assertEquals(1,service.deleteGrade(1,"history"));
    }

    @Test
    public void deleteNoStduentGrade(){
        assertEquals(0,service.deleteGrade(0,"science"));

        assertEquals(0,service.deleteGrade(1,"literature"));
    }

    @Test
    public void studentInfo(){
        GradebookCollegeStudent student=service.studentInformation(1);

        assertNotNull(student);
        assertEquals(1,student.getId());
        assertEquals("A",student.getFirstname());
        assertTrue(student.getStudentGrades().getMathGradeResults().size()==1);
        assertTrue(student.getStudentGrades().getScienceGradeResults().size()==1);
        assertTrue(student.getStudentGrades().getHistoryGradeResults().size()==1);
    }

    @Test
    public void wrongStudent(){
        GradebookCollegeStudent student=service.studentInformation(0);
        assertNull(student);
    }

//
//    @AfterEach
//    public void after(){
//        System.out.println(" After each 2");
//        jdbc.execute("delete from student");
//        jdbc.execute("Alter table student Alter column ID restart with 1");
//    }

    @Test
    void contextLoads() {
    }

}
