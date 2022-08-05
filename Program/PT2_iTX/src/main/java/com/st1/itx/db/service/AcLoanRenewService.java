package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcLoanRenew;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.AcLoanRenewId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AcLoanRenewService {

  /**
   * findByPrimaryKey
   *
   * @param acLoanRenewId PK
   * @param titaVo Variable-Length Argument
   * @return AcLoanRenew AcLoanRenew
   */
  public AcLoanRenew findById(AcLoanRenewId acLoanRenewId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcLoanRenew AcLoanRenew of List
   */
  public Slice<AcLoanRenew> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND NewFacmNo &gt;= ,AND NewFacmNo &lt;= ,AND NewBormNo &gt;= ,AND NewBormNo &lt;=
   *
   * @param custNo_0 custNo_0
   * @param newFacmNo_1 newFacmNo_1
   * @param newFacmNo_2 newFacmNo_2
   * @param newBormNo_3 newBormNo_3
   * @param newBormNo_4 newBormNo_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcLoanRenew AcLoanRenew of List
   */
  public Slice<AcLoanRenew> NewFacmNoNoRange(int custNo_0, int newFacmNo_1, int newFacmNo_2, int newBormNo_3, int newBormNo_4, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo &gt;= ,AND CustNo &lt;= ,AND OldFacmNo &gt;= ,AND OldFacmNo &lt;= ,AND NewFacmNo &gt;= ,AND NewFacmNo &lt;= , AND AcDate &gt;=, AND AcDate &lt;=
   *
   * @param custNo_0 custNo_0
   * @param custNo_1 custNo_1
   * @param oldFacmNo_2 oldFacmNo_2
   * @param oldFacmNo_3 oldFacmNo_3
   * @param newFacmNo_4 newFacmNo_4
   * @param newFacmNo_5 newFacmNo_5
   * @param acDate_6 acDate_6
   * @param acDate_7 acDate_7
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcLoanRenew AcLoanRenew of List
   */
  public Slice<AcLoanRenew> findL2079(int custNo_0, int custNo_1, int oldFacmNo_2, int oldFacmNo_3, int newFacmNo_4, int newFacmNo_5, int acDate_6, int acDate_7, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo =
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcLoanRenew AcLoanRenew of List
   */
  public Slice<AcLoanRenew> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By AcLoanRenew
   * 
   * @param acLoanRenewId key
   * @param titaVo Variable-Length Argument
   * @return AcLoanRenew AcLoanRenew
   */
  public AcLoanRenew holdById(AcLoanRenewId acLoanRenewId, TitaVo... titaVo);

  /**
   * hold By AcLoanRenew
   * 
   * @param acLoanRenew key
   * @param titaVo Variable-Length Argument
   * @return AcLoanRenew AcLoanRenew
   */
  public AcLoanRenew holdById(AcLoanRenew acLoanRenew, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param acLoanRenew Entity
   * @param titaVo Variable-Length Argument
   * @return AcLoanRenew Entity
   * @throws DBException exception
   */
  public AcLoanRenew insert(AcLoanRenew acLoanRenew, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param acLoanRenew Entity
   * @param titaVo Variable-Length Argument
   * @return AcLoanRenew Entity
   * @throws DBException exception
   */
  public AcLoanRenew update(AcLoanRenew acLoanRenew, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param acLoanRenew Entity
   * @param titaVo Variable-Length Argument
   * @return AcLoanRenew Entity
   * @throws DBException exception
   */
  public AcLoanRenew update2(AcLoanRenew acLoanRenew, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param acLoanRenew Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(AcLoanRenew acLoanRenew, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param acLoanRenew Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<AcLoanRenew> acLoanRenew, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param acLoanRenew Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<AcLoanRenew> acLoanRenew, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param acLoanRenew Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<AcLoanRenew> acLoanRenew, TitaVo... titaVo) throws DBException;

}
