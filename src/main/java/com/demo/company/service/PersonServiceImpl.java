package com.demo.company.service;

import com.demo.company.entity.Person;
import com.demo.company.repository.PersonRepository;
import com.demo.config.data.Credential;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

  @Autowired
  private PersonRepository personRepository;

  @Override
  public void create(Person person) throws Exception {
    person.setStoreId(MDC.get(Credential.CREDENTIAL_STORE_ID));
    personRepository.save(person);
  }

  @Override
  public void update(String code, Person person) throws Exception {
    Person oldPerson = personRepository.findFirstByPersonCode(code);
    oldPerson.setPersonName(person.getPersonName());
    oldPerson.setAddresses(person.getAddresses());
    personRepository.save(oldPerson);
  }

  @Override
  public void updateName(String code, Person person) throws Exception {
    Person oldPerson = personRepository.findFirstByPersonCode(code);
    oldPerson.setPersonName(person.getPersonName());
    personRepository.save(oldPerson);
  }

  // @Override
  // public Page<Person> find(Pageable pageable) throws Exception {
  // return personRepository.find(pageable);
  // }

  @Override
  public List<Person> findAll() throws Exception {
    return personRepository.findAll();
  }

  @Override
  public Person findByPersonCode(String code) throws Exception {
    return personRepository.findFirstByPersonCode(code);
  }

  @Override
  public void deleteByPersonCode(String code) throws Exception {
    personRepository.deleteByPersonCode(code);
  }
}
