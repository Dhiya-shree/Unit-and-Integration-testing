package com.mvc.gradetracker.repository;

import com.mvc.gradetracker.models.CollegeStudent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentDao extends CrudRepository<CollegeStudent, Integer> {

    public CollegeStudent findByEmailAddress(String emailAddress);

    Optional<CollegeStudent> findById(int i);
}