package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CollLaw;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CollLawId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CollLawService {

  /**
   * findByPrimaryKey
   *
   * @param collLawId PK
   * @param titaVo Variable-Length Argument
   * @return CollLaw CollLaw
   */
  public CollLaw findById(CollLawId collLawId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CollLaw CollLaw of List
   */
  public Slice<CollLaw> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * RecordDate&gt;= , AND RecordDate&lt;= ,AND CaseCode= ,AND CustNo= ,AND FacmNo= ,
   *
   * @param recordDate_0 recordDate_0
   * @param recordDate_1 recordDate_1
   * @param caseCode_2 caseCode_2
   * @param custNo_3 custNo_3
   * @param facmNo_4 facmNo_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CollLaw CollLaw of List
   */
  public Slice<CollLaw> telTimeBetween(int recordDate_0, int recordDate_1, String caseCode_2, int custNo_3, int facmNo_4, int index, int limit, TitaVo... titaVo);

  /**
   * CaseCode= ,AND CustNo= ,AND FacmNo= ,
   *
   * @param caseCode_0 caseCode_0
   * @param custNo_1 custNo_1
   * @param facmNo_2 facmNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CollLaw CollLaw of List
   */
  public Slice<CollLaw> findSameCust(String caseCode_0, int custNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * RecordDate&gt;= , AND RecordDate&lt;= ,AND CaseCode= ,AND CustNo= 
   *
   * @param recordDate_0 recordDate_0
   * @param recordDate_1 recordDate_1
   * @param caseCode_2 caseCode_2
   * @param custNo_3 custNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CollLaw CollLaw of List
   */
  public Slice<CollLaw> withoutFacmNo(int recordDate_0, int recordDate_1, String caseCode_2, int custNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * CaseCode= ,AND CustNo= 
   *
   * @param caseCode_0 caseCode_0
   * @param custNo_1 custNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CollLaw CollLaw of List
   */
  public Slice<CollLaw> withoutFacmNoAll(String caseCode_0, int custNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * CaseCode= ,AND CustNo= ,AND FacmNo= ,
   *
   * @param caseCode_0 caseCode_0
   * @param custNo_1 custNo_1
   * @param facmNo_2 facmNo_2
   * @param titaVo Variable-Length Argument
   * @return Slice CollLaw CollLaw of List
   */
  public CollLaw findFacmNoFirst(String caseCode_0, int custNo_1, int facmNo_2, TitaVo... titaVo);

  /**
   * hold By CollLaw
   * 
   * @param collLawId key
   * @param titaVo Variable-Length Argument
   * @return CollLaw CollLaw
   */
  public CollLaw holdById(CollLawId collLawId, TitaVo... titaVo);

  /**
   * hold By CollLaw
   * 
   * @param collLaw key
   * @param titaVo Variable-Length Argument
   * @return CollLaw CollLaw
   */
  public CollLaw holdById(CollLaw collLaw, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param collLaw Entity
   * @param titaVo Variable-Length Argument
   * @return CollLaw Entity
   * @throws DBException exception
   */
  public CollLaw insert(CollLaw collLaw, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param collLaw Entity
   * @param titaVo Variable-Length Argument
   * @return CollLaw Entity
   * @throws DBException exception
   */
  public CollLaw update(CollLaw collLaw, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param collLaw Entity
   * @param titaVo Variable-Length Argument
   * @return CollLaw Entity
   * @throws DBException exception
   */
  public CollLaw update2(CollLaw collLaw, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param collLaw Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CollLaw collLaw, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param collLaw Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CollLaw> collLaw, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param collLaw Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CollLaw> collLaw, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param collLaw Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CollLaw> collLaw, TitaVo... titaVo) throws DBException;

}
