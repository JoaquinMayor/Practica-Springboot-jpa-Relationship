package com.joaquin.curso.springboot.jpa.springbootjparelationship.repository;

import org.springframework.data.repository.CrudRepository;

import com.joaquin.curso.springboot.jpa.springbootjparelationship.Entities.Invoice;

public interface IInvoiceRepository extends CrudRepository<Invoice, Long>{
    
}
