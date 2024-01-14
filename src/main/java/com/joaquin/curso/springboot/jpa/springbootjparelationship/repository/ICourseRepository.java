package com.joaquin.curso.springboot.jpa.springbootjparelationship.repository;

import org.springframework.data.repository.CrudRepository;

import com.joaquin.curso.springboot.jpa.springbootjparelationship.Entities.Course;

public interface ICourseRepository extends CrudRepository<Course,Long>{
    
}
