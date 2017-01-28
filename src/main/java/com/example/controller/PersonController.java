package com.example.controller;

import com.example.model.PersonVO;

import org.springframework.http.ResponseEntity;

/**
 * Created by cjemison on 1/28/17.
 */
public interface PersonController {

  ResponseEntity<?> findAll();

  ResponseEntity<?> findById(final Integer id);

  ResponseEntity<?> insert(final PersonVO personVO);
}
