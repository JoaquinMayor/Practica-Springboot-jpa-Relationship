package com.joaquin.curso.springboot.jpa.springbootjparelationship.repository;

import org.springframework.data.repository.CrudRepository;

import com.joaquin.curso.springboot.jpa.springbootjparelationship.Entities.ClientDetail;

public interface IClientDetailsRepository extends CrudRepository<ClientDetail,Long>{
    
}
