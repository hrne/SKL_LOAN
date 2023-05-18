package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.BatxOthers;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.BatxOthersId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BatxOthersService {

  /**
   * findByPrimaryKey
   *
   * @param batxOthersId PK
   * @param titaVo Variable-Length Argument
   * @return BatxOthers BatxOthers
   */
  public BatxOthers findById(BatxOthersId batxOthersId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxOthers BatxOthers of List
   */
  public Slice<BatxOthers> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= ,AND AcDate &lt;= , AND BatchNo = 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param batchNo_2 batchNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxOthers BatxOthers of List
   */
  public Slice<BatxOthers> searchRuleA(int acDate_0, int acDate_1, String batchNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= ,AND AcDate &lt;= , AND BatchNo = , AND RepayCode = 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param batchNo_2 batchNo_2
   * @param repayCode_3 repayCode_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxOthers BatxOthers of List
   */
  public Slice<BatxOthers> searchRuleB(int acDate_0, int acDate_1, String batchNo_2, int repayCode_3, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= ,AND AcDate &lt;= , AND BatchNo = , AND CreateEmpNo = 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param batchNo_2 batchNo_2
   * @param createEmpNo_3 createEmpNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxOthers BatxOthers of List
   */
  public Slice<BatxOthers> searchRuleC(int acDate_0, int acDate_1, String batchNo_2, String createEmpNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= ,AND AcDate &lt;= , AND BatchNo = , AND RepayCode = , AND CreateEmpNo = 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param batchNo_2 batchNo_2
   * @param repayCode_3 repayCode_3
   * @param createEmpNo_4 createEmpNo_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxOthers BatxOthers of List
   */
  public Slice<BatxOthers> searchRuleD(int acDate_0, int acDate_1, String batchNo_2, int repayCode_3, String createEmpNo_4, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate =  ,AND BatchNo =
   *
   * @param acDate_0 acDate_0
   * @param batchNo_1 batchNo_1
   * @param titaVo Variable-Length Argument
   * @return Slice BatxOthers BatxOthers of List
   */
  public BatxOthers detSeqFirst(int acDate_0, String batchNo_1, TitaVo... titaVo);

  /**
   * AcDate &gt;= ,AND AcDate &lt;= 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxOthers BatxOthers of List
   */
  public Slice<BatxOthers> searchRuleE(int acDate_0, int acDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= ,AND AcDate &lt;= ,AND RepayCode = 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param repayCode_2 repayCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxOthers BatxOthers of List
   */
  public Slice<BatxOthers> searchRuleF(int acDate_0, int acDate_1, int repayCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= ,AND AcDate &lt;= ,AND CreateEmpNo = 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param createEmpNo_2 createEmpNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxOthers BatxOthers of List
   */
  public Slice<BatxOthers> searchRuleG(int acDate_0, int acDate_1, String createEmpNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= ,AND AcDate &lt;= ,AND RepayCode = ,AND CreateEmpNo = 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param repayCode_2 repayCode_2
   * @param createEmpNo_3 createEmpNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxOthers BatxOthers of List
   */
  public Slice<BatxOthers> searchRuleH(int acDate_0, int acDate_1, int repayCode_2, String createEmpNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * TitaEntdy = ,AND TitaTlrNo = ,AND TitaTxtNo =
   *
   * @param titaEntdy_0 titaEntdy_0
   * @param titaTlrNo_1 titaTlrNo_1
   * @param titaTxtNo_2 titaTxtNo_2
   * @param titaVo Variable-Length Argument
   * @return Slice BatxOthers BatxOthers of List
   */
  public BatxOthers findTxSeqFirst(int titaEntdy_0, String titaTlrNo_1, String titaTxtNo_2, TitaVo... titaVo);

  /**
   * AcDate &gt;= ,AND AcDate &lt;= ,AND CustNo = , AND BatchNo = 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param custNo_2 custNo_2
   * @param batchNo_3 batchNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxOthers BatxOthers of List
   */
  public Slice<BatxOthers> searchCustNoA(int acDate_0, int acDate_1, int custNo_2, String batchNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= ,AND AcDate &lt;= ,AND CustNo = , AND BatchNo = , AND RepayCode = 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param custNo_2 custNo_2
   * @param batchNo_3 batchNo_3
   * @param repayCode_4 repayCode_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxOthers BatxOthers of List
   */
  public Slice<BatxOthers> searchCustNoB(int acDate_0, int acDate_1, int custNo_2, String batchNo_3, int repayCode_4, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= ,AND AcDate &lt;= ,AND CustNo = , AND BatchNo = , AND CreateEmpNo = 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param custNo_2 custNo_2
   * @param batchNo_3 batchNo_3
   * @param createEmpNo_4 createEmpNo_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxOthers BatxOthers of List
   */
  public Slice<BatxOthers> searchCustNoC(int acDate_0, int acDate_1, int custNo_2, String batchNo_3, String createEmpNo_4, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= ,AND AcDate &lt;= ,AND CustNo = , AND BatchNo = , AND RepayCode = , AND CreateEmpNo = 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param custNo_2 custNo_2
   * @param batchNo_3 batchNo_3
   * @param repayCode_4 repayCode_4
   * @param createEmpNo_5 createEmpNo_5
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxOthers BatxOthers of List
   */
  public Slice<BatxOthers> searchCustNoD(int acDate_0, int acDate_1, int custNo_2, String batchNo_3, int repayCode_4, String createEmpNo_5, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= ,AND AcDate &lt;= ,AND CustNo = 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param custNo_2 custNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxOthers BatxOthers of List
   */
  public Slice<BatxOthers> searchCustNoE(int acDate_0, int acDate_1, int custNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= ,AND AcDate &lt;= ,AND CustNo = , AND RepayCode = 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param custNo_2 custNo_2
   * @param repayCode_3 repayCode_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxOthers BatxOthers of List
   */
  public Slice<BatxOthers> searchCustNoF(int acDate_0, int acDate_1, int custNo_2, int repayCode_3, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= ,AND AcDate &lt;= ,AND CustNo = , AND CreateEmpNo = 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param custNo_2 custNo_2
   * @param createEmpNo_3 createEmpNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxOthers BatxOthers of List
   */
  public Slice<BatxOthers> searchCustNoG(int acDate_0, int acDate_1, int custNo_2, String createEmpNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= ,AND AcDate &lt;= ,AND CustNo = , AND RepayCode = ,AND CreateEmpNo = 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param custNo_2 custNo_2
   * @param repayCode_3 repayCode_3
   * @param createEmpNo_4 createEmpNo_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxOthers BatxOthers of List
   */
  public Slice<BatxOthers> searchCustNoH(int acDate_0, int acDate_1, int custNo_2, int repayCode_3, String createEmpNo_4, int index, int limit, TitaVo... titaVo);

  /**
   * hold By BatxOthers
   * 
   * @param batxOthersId key
   * @param titaVo Variable-Length Argument
   * @return BatxOthers BatxOthers
   */
  public BatxOthers holdById(BatxOthersId batxOthersId, TitaVo... titaVo);

  /**
   * hold By BatxOthers
   * 
   * @param batxOthers key
   * @param titaVo Variable-Length Argument
   * @return BatxOthers BatxOthers
   */
  public BatxOthers holdById(BatxOthers batxOthers, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param batxOthers Entity
   * @param titaVo Variable-Length Argument
   * @return BatxOthers Entity
   * @throws DBException exception
   */
  public BatxOthers insert(BatxOthers batxOthers, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param batxOthers Entity
   * @param titaVo Variable-Length Argument
   * @return BatxOthers Entity
   * @throws DBException exception
   */
  public BatxOthers update(BatxOthers batxOthers, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param batxOthers Entity
   * @param titaVo Variable-Length Argument
   * @return BatxOthers Entity
   * @throws DBException exception
   */
  public BatxOthers update2(BatxOthers batxOthers, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param batxOthers Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(BatxOthers batxOthers, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param batxOthers Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<BatxOthers> batxOthers, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param batxOthers Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<BatxOthers> batxOthers, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param batxOthers Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<BatxOthers> batxOthers, TitaVo... titaVo) throws DBException;

}
