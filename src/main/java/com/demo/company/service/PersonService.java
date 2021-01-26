package com.demo.company.service;

import com.demo.company.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PersonService {

  void create(Person person) throws Exception;

  void update(String code, Person person) throws Exception;

  void updateName(String code, Person person) throws Exception;

  // Page<Person> find(Pageable pageable) throws Exception;

  List<Person> findAll() throws Exception;

  Person findByPersonCode(String code) throws Exception;

  void deleteByPersonCode(String code) throws Exception;
}
