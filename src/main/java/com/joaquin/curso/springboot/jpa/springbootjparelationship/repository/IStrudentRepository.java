package com.joaquin.curso.springboot.jpa.springbootjparelationship.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.joaquin.curso.springboot.jpa.springbootjparelationship.Entities.Student;

public interface IStrudentRepository extends CrudRepository<Student,Long>{
    
    @Query("select s from Student s left join fetch s.courses where s.id=?1")
    Optional<Student> findOneWithCourses(Long id);
}
