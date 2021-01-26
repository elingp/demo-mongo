package com.demo.company.controller;

public interface PersonControllerPath {

  String BASE_PATH = "/api/people";
  String FIND_BY_CODE = "/{code}";
  String UPDATE_BY_CODE = "/{code}";
  String DELETE_BY_CODE = "/{code}";
  String UPDATE_NAME_BY_CODE = "/{code}/name";
}
