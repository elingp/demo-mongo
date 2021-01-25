package com.demo.company.service;

import com.demo.company.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonService {

    void create(Person person) throws Exception;

    void update(String code, Person person) throws Exception;

    void updateName(String code, Person person) throws Exception;

    Page<Person> find(Pageable pageable) throws Exception;

    Person findByPersonCode(String code) throws Exception;

    void deleteByPersonCode(String code) throws Exception;
}
