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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
@SpringBootTest
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
     //   jdbc.execute("alter table student alter column ID restart with 1");
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
        assertTrue(mathGradesDao.findById(1).isPresent());
        assertTrue(scienceGradesDao.findById(1).isPresent());
        assertTrue(historyGradesDao.findById(1).isPresent());
       service.deleteStudent(1);

       assertFalse(studentDao.findById(1).isPresent());
       assertFalse(mathGradesDao.findById(1).isPresent());
       assertFalse(scienceGradesDao.findById(1).isPresent());
       assertFalse(historyGradesDao.findById(1).isPresent());
    }

  @Test
  void studInfo(){
        GradebookCollegeStudent stud=service.studentInformation(1);

        assertNotNull(stud);
        assertEquals(1,stud.getId());
        assertEquals(stud.getFirstname(),"A");
        assertEquals(stud.getLastname(),"A");
        assertEquals(stud.getEmailAddress(),"A@A.com");
        assertNotNull(stud.getStudentGrades().getMathGradeResults());
      assertNotNull(stud.getStudentGrades().getScienceGradeResults());
      assertNotNull(stud.getStudentGrades().getHistoryGradeResults());
  }



  @Test
  void checkNull(){
      assertTrue(service.checkIfGradeIsNull(1,"math"));
      assertTrue(service.checkIfGradeIsNull(1,"science"));
      assertTrue(service.checkIfGradeIsNull(1,"history"));
      assertFalse(service.checkIfGradeIsNull(0,"math"));
      assertFalse(service.checkIfGradeIsNull(0,"science"));

  }
    @Test
    public void deleteGrade(){
        assertEquals(1,service.deleteGrade(1,"math"));

        assertEquals(1,service.deleteGrade(1,"science"));

        assertEquals(1,service.deleteGrade(1,"history"));
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
        assertFalse(service.createGrade(800,0,"math"));// no student
        assertFalse(service.createGrade(80,1,"literature"));// no type
    }

    @SqlGroup({
            @Sql(scripts = "/insertData.sql",config = @SqlConfig(commentPrefix = "`")),
            @Sql(scripts = "/overrideData.sql"),
            @Sql("/insertGrade.sql")
    })
    @Test
    public void getGradeBookService(){
        Gradebook gradebook=service.getGradebook();
        Gradebook gradebook1=new Gradebook();

        for(GradebookCollegeStudent student:gradebook.getStudents()){
            if(student.getId()>10){
                gradebook1.getStudents().add(student);
            }
        }

        assertEquals(4,gradebook1.getStudents().size());
        assertTrue(gradebook1.getStudents().get(0).getStudentGrades().getMathGradeResults()!=null);
        assertTrue(gradebook1.getStudents().get(0).getStudentGrades().getScienceGradeResults()!=null);
        assertTrue(gradebook1.getStudents().get(0).getStudentGrades().getHistoryGradeResults()!=null);
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
