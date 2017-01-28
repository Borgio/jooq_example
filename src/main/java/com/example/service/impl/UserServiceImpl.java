package com.example.service.impl;

import com.example.db.Tables;
import com.example.db.tables.daos.IfsUsersTblDao;
import com.example.db.tables.pojos.IfsUsersTbl;
import com.example.model.PersonVO;
import com.example.service.UserService;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.db.Tables.IFS_USERS_TBL;

/**
 * Created by cjemison on 1/28/17.
 */
@Service
public class UserServiceImpl implements UserService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
  private static final Function<IfsUsersTbl, PersonVO> function = ifsUsersTbl -> {
    PersonVO personVO = new PersonVO();
    personVO.setId(ifsUsersTbl.getId());
    personVO.setFirstName(ifsUsersTbl.getFirstName());
    personVO.setLastName(ifsUsersTbl.getLastName());
    personVO.setEmailAddress(ifsUsersTbl.getEmailAddress());
    personVO.setCreatedDate(ifsUsersTbl.getCreatedDate());
    personVO.setUpdatedDate(ifsUsersTbl.getUpdatedDate());
    LOGGER.debug("PersonVO: {}", personVO);
    return personVO;
  };
  private final DSLContext dslContext;
  private final IfsUsersTblDao dao;


  @Autowired
  public UserServiceImpl(final DSLContext dslContext) {
    this.dslContext = dslContext;
    this.dao = new IfsUsersTblDao(dslContext.configuration());
  }

  @Override
  @Transactional(readOnly = true)
  public List<PersonVO> findAll() {
    return dslContext.select().from(IFS_USERS_TBL).fetch().into(IfsUsersTbl
          .class).stream().map(function).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<PersonVO> findById(final Integer id) {
    LOGGER.info("PersonVO.id: {}", id);
    if (id != null) {
      final IfsUsersTbl usersTbl = dao.findById(id);
      if (usersTbl != null) {
        return Optional.of(function.apply(usersTbl));
      }
    }
    return Optional.empty();
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public Optional<PersonVO> insert(final PersonVO personVO) {
    LOGGER.info("PersonVO: {}", personVO);
    final Record result = dslContext.insertInto(IFS_USERS_TBL)
          .columns(IFS_USERS_TBL.FIRST_NAME,
                IFS_USERS_TBL.LAST_NAME,
                IFS_USERS_TBL.EMAIL_ADDRESS)
          .values(personVO.getFirstName(),
                personVO.getLastName(),
                personVO.getEmailAddress())
          .returning(IFS_USERS_TBL.ID, IFS_USERS_TBL.CREATED_DATE, IFS_USERS_TBL.UPDATED_DATE)
          .fetchOne();

    if (result == null) {
      throw new IllegalStateException(String.format("PersonVO: %s didn't save. ", personVO));
    }
    LOGGER.debug("Result: {}", result);
    personVO.setId(result.getValue(IFS_USERS_TBL.ID));
    personVO.setCreatedDate(result.getValue(IFS_USERS_TBL.CREATED_DATE));
    personVO.setUpdatedDate(result.getValue(IFS_USERS_TBL.UPDATED_DATE));
    LOGGER.debug("UPDATED PersonVO: {}", personVO);
    return Optional.of(personVO);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public Optional<PersonVO> update(final PersonVO personVO) {
    LOGGER.info("PersonVO: {}", personVO);
    final int rows = dslContext.update(Tables.IFS_USERS_TBL)
          .set(IFS_USERS_TBL.FIRST_NAME, personVO.getFirstName())
          .set(IFS_USERS_TBL.LAST_NAME, personVO.getLastName())
          .set(IFS_USERS_TBL.EMAIL_ADDRESS, personVO.getEmailAddress())
          .set(IFS_USERS_TBL.UPDATED_DATE, new Timestamp(new DateTime(DateTimeZone.UTC)
                .getMillis()))
          .where(IFS_USERS_TBL.ID.eq(personVO.getId()))
          .execute();
    LOGGER.info("UPDATED rows: {} PersonVO: {}", rows, personVO);
    return findById(personVO.getId());
  }
}
