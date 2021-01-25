package com.demo.company.repository;

import com.demo.company.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepository extends MongoRepository<Person, String> {

    Person findFirstByPersonCode(String code);

//    Page<Person> find(Pageable pageable);

    void deleteByPersonCode(String code);
}
