package com.mvc.gradetracker.repository;

import com.mvc.gradetracker.models.ScienceGrade;
import org.springframework.data.repository.CrudRepository;

public interface ScienceGradesDao extends CrudRepository<ScienceGrade, Integer> {

    public Iterable<ScienceGrade> findGradeByStudentId(int id);

    public void deleteByStudentId(int id);
}