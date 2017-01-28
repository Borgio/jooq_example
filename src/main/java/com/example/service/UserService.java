package com.example.service;

import com.example.model.PersonVO;

import java.util.List;
import java.util.Optional;

/**
 * Created by cjemison on 1/28/17.
 */
public interface UserService {

  List<PersonVO> findAll();

  Optional<PersonVO> findById(final Integer id);

  Optional<PersonVO> insert(final PersonVO personVO);
}
