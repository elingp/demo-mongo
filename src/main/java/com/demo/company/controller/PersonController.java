package com.demo.company.controller;

import com.demo.base.BaseResponse;
import com.demo.base.ListBaseResponse;
import com.demo.base.Metadata;
import com.demo.base.SingleBaseResponse;
import com.demo.company.entity.Address;
import com.demo.company.entity.Person;
import com.demo.company.service.PersonService;
import com.demo.dto.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = PersonControllerPath.BASE_PATH)
public class PersonController {

    @Autowired
    private PersonService personService;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse create(@RequestParam String storeId, @RequestParam String channelId,
                               @RequestParam String clientId, @RequestParam String requestId, @RequestParam String username,
                               @RequestBody PersonCreateRequest request) throws Exception {
        this.personService.create(toPerson(request));
        return new BaseResponse(null, null, true, requestId);
    }

    @RequestMapping(value = PersonControllerPath.UPDATE_BY_CODE, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse updateByPersonCode(@RequestParam String storeId, @RequestParam String channelId,
                                           @RequestParam String clientId, @RequestParam String requestId, @RequestParam String username, @PathVariable String code,
                                           @RequestBody PersonUpdateRequest request) throws Exception {
        this.personService.update(code, toPerson(request));
        return new BaseResponse(null, null, true, requestId);
    }

    @RequestMapping(value = PersonControllerPath.UPDATE_NAME_BY_CODE, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse updateNameByPersonCode(@RequestParam String storeId, @RequestParam String channelId,
                                               @RequestParam String clientId, @RequestParam String requestId, @RequestParam String username, @PathVariable String code,
                                               @RequestBody PersonUpdateNameRequest request) throws Exception {
        Person person = Person.builder().build();
        BeanUtils.copyProperties(request, person);
        this.personService.updateName(code, person);
        return new BaseResponse(null, null, true, requestId);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ListBaseResponse<PersonResponse> findPeople(@RequestParam String storeId, @RequestParam String channelId,
                                                       @RequestParam String clientId, @RequestParam String requestId, @RequestParam String username,
                                                       @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size)
            throws Exception {
        List<Person> people = this.personService.findAll();
        List<PersonResponse> personResponses = people.stream().map(this::toPersonResponse).collect(Collectors.toList());
        return new ListBaseResponse<>(null, null, true, requestId, personResponses,
                new Metadata(1, people.size(), (long) people.size()));
    }

    @RequestMapping(value = PersonControllerPath.FIND_BY_CODE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SingleBaseResponse<PersonResponse> findByPersonCode(@RequestParam String storeId,
                                                               @RequestParam String channelId, @RequestParam String clientId, @RequestParam String requestId,
                                                               @RequestParam String username, @PathVariable String code) throws Exception {
        Person person = this.personService.findByPersonCode(code);
        PersonResponse personResponse = Optional.ofNullable(person).map(this::toPersonResponse).orElse(null);
        return new SingleBaseResponse<>(null, null, true, requestId, personResponse);
    }

    @RequestMapping(value = PersonControllerPath.DELETE_BY_CODE, method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse deleteByPersonCode(@RequestParam String storeId, @RequestParam String channelId,
                                           @RequestParam String clientId, @RequestParam String requestId, @RequestParam String username,
                                           @RequestParam String code) throws Exception {
        this.personService.deleteByPersonCode(code);
        return new BaseResponse(null, null, true, requestId);
    }


    private PersonResponse toPersonResponse(Person person) {
        return Optional.ofNullable(person).map(p -> {
            PersonResponse personResponse = PersonResponse.builder().build();
            BeanUtils.copyProperties(p, personResponse);
            personResponse.setAddresses(toAddressResponse(person.getAddresses()));
            return personResponse;
        }).orElse(null);
    }

    private List<AddressResponse> toAddressResponse(List<Address> addresses) {
        return Optional.ofNullable(addresses).map(a -> {
//            List<AddressResponse> addressResponse = AddressResponse.builder().build();
            List<AddressResponse> addressResponses = new ArrayList<>();
            for (Address req :
                    a) {
                AddressResponse address = AddressResponse.builder().build();
                BeanUtils.copyProperties(req, address);
                addressResponses.add(address);
            }
            BeanUtils.copyProperties(a, addressResponses);
            return addressResponses;
        }).orElse(null);
    }

    private Person toPerson(PersonUpdateRequest request) {
        return Optional.ofNullable(request).map(p -> {
            Person person = Person.builder().build();
            BeanUtils.copyProperties(p, person);
            person.setAddresses(toAddresses(request.getAddresses()));
            return person;
        }).orElse(null);
    }

    private Person toPerson(PersonCreateRequest request) {
        return Optional.ofNullable(request).map(p -> {
            Person person = Person.builder().build();
            BeanUtils.copyProperties(p, person);
            person.setAddresses(toAddresses(request.getAddresses()));
            return person;
        }).orElse(null);
    }

    private List<Address> toAddresses(List<AddressRequest> request) {
        return Optional.ofNullable(request).map(a -> {
//            List<Address> addresses = Address.builder().build();
            List<Address> addresses = new ArrayList<>();
            for (AddressRequest req :
                    a) {
                Address address = Address.builder().build();
                BeanUtils.copyProperties(req, address);
                addresses.add(address);
            }
//            BeanUtils.copyProperties(a, addresses);
            return addresses;
        }).orElse(null);
    }
}
