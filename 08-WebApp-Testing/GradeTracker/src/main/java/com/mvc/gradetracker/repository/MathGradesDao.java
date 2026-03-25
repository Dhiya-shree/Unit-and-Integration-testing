package com.mvc.gradetracker.repository;

import com.mvc.gradetracker.models.MathGrade;
import org.springframework.data.repository.CrudRepository;

public interface MathGradesDao extends CrudRepository<MathGrade, Integer> {

    public Iterable<MathGrade> findGradeByStudentId(int id);

    public void deleteByStudentId(int id);
}