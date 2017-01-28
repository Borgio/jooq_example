package com.example.controller.impl;

import com.example.controller.PersonController;
import com.example.model.PersonVO;
import com.example.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Created by cjemison on 1/28/17.
 */
@RestController
@RequestMapping("/v1")
public class PersonControllerImpl implements PersonController {
  private final Logger LOGGER = LoggerFactory.getLogger(PersonControllerImpl.class);
  private final UserService userService;

  @Autowired
  public PersonControllerImpl(final UserService userService) {
    this.userService = userService;
  }

  @Override
  @RequestMapping(value = "/person", method = RequestMethod.GET)
  public ResponseEntity<List<PersonVO>> findAll() {
    return ResponseEntity.ok(userService.findAll());
  }

  @Override
  @RequestMapping(value = "/person/{id}", method = RequestMethod.GET)
  public ResponseEntity<PersonVO> findById(@PathVariable("id") final Integer id) {
    final Optional<PersonVO> personVOOptional = userService.findById(id);
    return personVOOptional.map(ResponseEntity::ok).orElse
          (ResponseEntity.notFound().build());
  }

  @Override
  @RequestMapping(value = "/person", method = RequestMethod.POST)
  public ResponseEntity<PersonVO> insert(@RequestBody final PersonVO personVO) {
    LOGGER.info("PersonVO: {}", personVO);
    final Optional<PersonVO> personVOOptional = userService.insert(personVO);
    return personVOOptional.map(ResponseEntity::ok).orElse(ResponseEntity
          .badRequest().build());
  }

  @Override
  @RequestMapping(value = "/person/{id}", method = RequestMethod.PUT)
  public ResponseEntity<?> update(@PathVariable("id")
                                  final Integer id,
                                  @RequestBody
                                  final PersonVO personVO) {
    LOGGER.info("id: {} PersonVO: {}", id, personVO);
    personVO.setId(id);
    final Optional<PersonVO> personVOOptional = userService.update(personVO);
    return personVOOptional.map(ResponseEntity::ok).orElse(ResponseEntity
          .badRequest().build());
  }
}
