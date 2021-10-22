package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CollLetter;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CollLetterId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CollLetterService {

  /**
   * findByPrimaryKey
   *
   * @param collLetterId PK
   * @param titaVo Variable-Length Argument
   * @return CollLetter CollLetter
   */
  public CollLetter findById(CollLetterId collLetterId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CollLetter CollLetter of List
   */
  public Slice<CollLetter> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * MailDate&gt;= , AND MailDate&lt;= ,AND CaseCode= ,AND CustNo= ,AND FacmNo= ,
   *
   * @param mailDate_0 mailDate_0
   * @param mailDate_1 mailDate_1
   * @param caseCode_2 caseCode_2
   * @param custNo_3 custNo_3
   * @param facmNo_4 facmNo_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CollLetter CollLetter of List
   */
  public Slice<CollLetter> telTimeBetween(int mailDate_0, int mailDate_1, String caseCode_2, int custNo_3, int facmNo_4, int index, int limit, TitaVo... titaVo);

  /**
   * CaseCode= ,AND CustNo= ,AND FacmNo= ,
   *
   * @param caseCode_0 caseCode_0
   * @param custNo_1 custNo_1
   * @param facmNo_2 facmNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CollLetter CollLetter of List
   */
  public Slice<CollLetter> findSameCust(String caseCode_0, int custNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * MailDate&gt;= , AND MailDate&lt;= ,AND CaseCode= ,AND CustNo= 
   *
   * @param mailDate_0 mailDate_0
   * @param mailDate_1 mailDate_1
   * @param caseCode_2 caseCode_2
   * @param custNo_3 custNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CollLetter CollLetter of List
   */
  public Slice<CollLetter> withoutFacmNo(int mailDate_0, int mailDate_1, String caseCode_2, int custNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * CaseCode= ,AND CustNo= 
   *
   * @param caseCode_0 caseCode_0
   * @param custNo_1 custNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CollLetter CollLetter of List
   */
  public Slice<CollLetter> withoutFacmNoAll(String caseCode_0, int custNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CollLetter
   * 
   * @param collLetterId key
   * @param titaVo Variable-Length Argument
   * @return CollLetter CollLetter
   */
  public CollLetter holdById(CollLetterId collLetterId, TitaVo... titaVo);

  /**
   * hold By CollLetter
   * 
   * @param collLetter key
   * @param titaVo Variable-Length Argument
   * @return CollLetter CollLetter
   */
  public CollLetter holdById(CollLetter collLetter, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param collLetter Entity
   * @param titaVo Variable-Length Argument
   * @return CollLetter Entity
   * @throws DBException exception
   */
  public CollLetter insert(CollLetter collLetter, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param collLetter Entity
   * @param titaVo Variable-Length Argument
   * @return CollLetter Entity
   * @throws DBException exception
   */
  public CollLetter update(CollLetter collLetter, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param collLetter Entity
   * @param titaVo Variable-Length Argument
   * @return CollLetter Entity
   * @throws DBException exception
   */
  public CollLetter update2(CollLetter collLetter, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param collLetter Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CollLetter collLetter, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param collLetter Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CollLetter> collLetter, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param collLetter Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CollLetter> collLetter, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param collLetter Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CollLetter> collLetter, TitaVo... titaVo) throws DBException;

}
