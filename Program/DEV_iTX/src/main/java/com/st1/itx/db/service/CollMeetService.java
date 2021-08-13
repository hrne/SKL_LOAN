package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CollMeet;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CollMeetId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CollMeetService {

  /**
   * findByPrimaryKey
   *
   * @param collMeetId PK
   * @param titaVo Variable-Length Argument
   * @return CollMeet CollMeet
   */
  public CollMeet findById(CollMeetId collMeetId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CollMeet CollMeet of List
   */
  public Slice<CollMeet> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * AcDate&gt;= , AND AcDate&lt;= ,AND CaseCode= ,AND CustNo= ,AND FacmNo= ,
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param caseCode_2 caseCode_2
   * @param custNo_3 custNo_3
   * @param facmNo_4 facmNo_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CollMeet CollMeet of List
   */
  public Slice<CollMeet> telTimeBetween(int acDate_0, int acDate_1, String caseCode_2, int custNo_3, int facmNo_4, int index, int limit, TitaVo... titaVo);

  /**
   * CaseCode= ,AND CustNo= ,AND FacmNo= ,
   *
   * @param caseCode_0 caseCode_0
   * @param custNo_1 custNo_1
   * @param facmNo_2 facmNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CollMeet CollMeet of List
   */
  public Slice<CollMeet> findSameCust(String caseCode_0, int custNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate&gt;= , AND AcDate&lt;= ,AND CaseCode= ,AND CustNo= 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param caseCode_2 caseCode_2
   * @param custNo_3 custNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CollMeet CollMeet of List
   */
  public Slice<CollMeet> withoutFacmNo(int acDate_0, int acDate_1, String caseCode_2, int custNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * CaseCode= ,AND CustNo= 
   *
   * @param caseCode_0 caseCode_0
   * @param custNo_1 custNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CollMeet CollMeet of List
   */
  public Slice<CollMeet> withoutFacmNoAll(String caseCode_0, int custNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CollMeet
   * 
   * @param collMeetId key
   * @param titaVo Variable-Length Argument
   * @return CollMeet CollMeet
   */
  public CollMeet holdById(CollMeetId collMeetId, TitaVo... titaVo);

  /**
   * hold By CollMeet
   * 
   * @param collMeet key
   * @param titaVo Variable-Length Argument
   * @return CollMeet CollMeet
   */
  public CollMeet holdById(CollMeet collMeet, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param collMeet Entity
   * @param titaVo Variable-Length Argument
   * @return CollMeet Entity
   * @throws DBException exception
   */
  public CollMeet insert(CollMeet collMeet, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param collMeet Entity
   * @param titaVo Variable-Length Argument
   * @return CollMeet Entity
   * @throws DBException exception
   */
  public CollMeet update(CollMeet collMeet, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param collMeet Entity
   * @param titaVo Variable-Length Argument
   * @return CollMeet Entity
   * @throws DBException exception
   */
  public CollMeet update2(CollMeet collMeet, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param collMeet Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CollMeet collMeet, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param collMeet Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CollMeet> collMeet, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param collMeet Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CollMeet> collMeet, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param collMeet Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CollMeet> collMeet, TitaVo... titaVo) throws DBException;

}
