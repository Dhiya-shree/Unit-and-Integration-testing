package com.mvc.gradetracker;

import com.mvc.gradetracker.models.CollegeStudent;
import com.mvc.gradetracker.repository.StudentDao;
import com.mvc.gradetracker.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application.properties")
@SpringBootTest
class GradeTrackerApplicationTests {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private StudentAndGradeService service;

    @BeforeEach
    public void before(){
        jdbc.execute("Insert into student(firstname,lastname,email_address) values ('A','B','C')");
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
        Optional<CollegeStudent> studToDelete=studentDao.findById(1);
        assertTrue(studToDelete.isPresent());
        service.deleteStudent(studToDelete.get().getId());
        studToDelete=studentDao.findById(1);
        assertFalse(studToDelete.isPresent());
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

    @AfterEach
    public void after(){
        jdbc.execute("delete from student");
        jdbc.execute("Alter table student Alter column ID restart with 1");
    }

    @Test
    void contextLoads() {
    }

}
