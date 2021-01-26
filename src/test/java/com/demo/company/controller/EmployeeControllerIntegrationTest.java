package com.demo.company.controller;

import com.demo.DemoApplication;
import com.demo.base.BaseResponse;
import com.demo.company.entity.Employee;
import com.demo.company.repository.EmployeeRepository;
import com.demo.dto.DepartmentRequest;
import com.demo.dto.EmployeeCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIntegrationTest {

  public static final String STORE_ID_KEY = "storeId";
  public static final String CHANNEL_ID_KEY = "channelId";
  public static final String CLIENT_ID = "clientId";
  public static final String REQUEST_ID_KEY = "requestId";
  public static final String USERNAME_KEY = "username";

  public static final String STORE_ID_VALUE = "1";
  public static final String CHANNEL_ID_VALUE = "2";
  public static final String CLIENT_ID_VALUE = "3";
  public static final String REQUEST_ID_VALUE = "4";
  public static final String USERNAME_VALUE = "admin";

  public static final String DEPT_NAME = "SDE";
  public static final String LOC = "Ini apa";
  public static final int DEPT_NO = 1;
  public static final int EMP_NO = 1;
  public static final String EMP_NAME = "Joni";
  public static final double COMM = 10.0;
  public static final String HIRE_DATE = "2020-01-20";
  public static final int MGR = 1;
  public static final double SAL = 200000.0;
  public static final String CONTEXT_PATH = "/demo";

  @Value("${local.server.port}")
  private int port;

  private EmployeeCreateRequest request;
  private DepartmentRequest departmentRequest;
  private ObjectMapper objectMapper;
  private Employee employee;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Before
  public void setUp() {
    RestAssured.port = port;
    objectMapper = new ObjectMapper();
    employeeRepository.deleteAll();
    mongoTemplate.indexOps(Employee.class)
        .ensureIndex(new Index("empNo", Sort.Direction.ASC).unique());
    employee = Employee.builder().empNo(EMP_NO).build();
    departmentRequest =
        DepartmentRequest.builder().deptNo(DEPT_NO).deptName(DEPT_NAME).loc(LOC).build();
    request = EmployeeCreateRequest.builder().empNo(EMP_NO).empName(EMP_NAME).comm(COMM)
        .hireDate(HIRE_DATE).mgr(MGR).sal(SAL).department(departmentRequest).build();
  }

  @Test
  public void createEmployee_success_returnBaseResponse() throws Exception {
    ValidatableResponse validatableResponse =
        RestAssured.given().contentType("application/json").queryParam(STORE_ID_KEY, STORE_ID_VALUE)
            .queryParam(CHANNEL_ID_KEY, CHANNEL_ID_VALUE).queryParam(CLIENT_ID, CLIENT_ID_VALUE)
            .queryParam(REQUEST_ID_KEY, REQUEST_ID_VALUE).queryParam(USERNAME_KEY, USERNAME_VALUE)
            .body(request).post(CONTEXT_PATH + EmployeeControllerPath.BASE_PATH).then();
    // .body(ObjectMapper.writeValueAsString(request))
    BaseResponse baseResponse =
        objectMapper.readValue(validatableResponse.extract().asString(), BaseResponse.class);
    Assert.assertTrue(baseResponse.isSuccess());
    Employee employee = employeeRepository.findFirstByEmpNoAndMarkForDeleteFalse(EMP_NO);
    Assert.assertEquals(EMP_NAME, employee.getEmpName());
  }

  @Test
  public void createEmployee_failed_returnError() throws Exception {
    employeeRepository.save(employee);
    ValidatableResponse validatableResponse =
        RestAssured.given().contentType("application/json").queryParam(STORE_ID_KEY, STORE_ID_VALUE)
            .queryParam(CHANNEL_ID_KEY, CHANNEL_ID_VALUE).queryParam(CLIENT_ID, CLIENT_ID_VALUE)
            .queryParam(REQUEST_ID_KEY, REQUEST_ID_VALUE).queryParam(USERNAME_KEY, USERNAME_VALUE)
            .body(request).post(CONTEXT_PATH + EmployeeControllerPath.BASE_PATH).then();
    BaseResponse baseResponse =
        objectMapper.readValue(validatableResponse.extract().asString(), BaseResponse.class);
    Assert.assertFalse(baseResponse.isSuccess());
  }


}
