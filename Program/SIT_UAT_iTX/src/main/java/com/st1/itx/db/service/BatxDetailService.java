package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.BatxDetail;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.BatxDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BatxDetailService {

  /**
   * findByPrimaryKey
   *
   * @param batxDetailId PK
   * @param titaVo Variable-Length Argument
   * @return BatxDetail BatxDetail
   */
  public BatxDetail findById(BatxDetailId batxDetailId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND RepayCode = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param repayCode_2 repayCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findL492AAEq(int custNo_0, int facmNo_1, int repayCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findL492ABEq(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = 
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findL492ACEq(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * RepayCode = 
   *
   * @param repayCode_0 repayCode_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findL492ADEq(int repayCode_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND RepayCode = 
   *
   * @param custNo_0 custNo_0
   * @param repayCode_1 repayCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findL492AEEq(int custNo_0, int repayCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND RepayCode = ,AND RepayType = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param repayCode_2 repayCode_2
   * @param repayType_3 repayType_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findL492AFEq(int custNo_0, int facmNo_1, int repayCode_2, int repayType_3, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND RepayType = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param repayType_2 repayType_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findL492AGEq(int custNo_0, int facmNo_1, int repayType_2, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND RepayType = 
   *
   * @param custNo_0 custNo_0
   * @param repayType_1 repayType_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findL492AHEq(int custNo_0, int repayType_1, int index, int limit, TitaVo... titaVo);

  /**
   * RepayCode = ,AND RepayType = 
   *
   * @param repayCode_0 repayCode_0
   * @param repayType_1 repayType_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findL492AIEq(int repayCode_0, int repayType_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND RepayCode = ,AND RepayType = 
   *
   * @param custNo_0 custNo_0
   * @param repayCode_1 repayCode_1
   * @param repayType_2 repayType_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findL492AJEq(int custNo_0, int repayCode_1, int repayType_2, int index, int limit, TitaVo... titaVo);

  /**
   * RepayType = 
   *
   * @param repayType_0 repayType_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findL492AKEq(int repayType_0, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = , AND BatchNo = 
   *
   * @param acDate_0 acDate_0
   * @param batchNo_1 batchNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findL4002AEq(int acDate_0, String batchNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = , AND BatchNo = , AND TitaTlrNo = 
   *
   * @param acDate_0 acDate_0
   * @param batchNo_1 batchNo_1
   * @param titaTlrNo_2 titaTlrNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findL4002BEq(int acDate_0, String batchNo_1, String titaTlrNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= , AND AcDate &lt;= , AND CustNo &gt;= , AND CustNo &lt;= , AND RepayCode &gt;= , AND RepayCode &lt;= , AND ProcStsCode ^i 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param custNo_2 custNo_2
   * @param custNo_3 custNo_3
   * @param repayCode_4 repayCode_4
   * @param repayCode_5 repayCode_5
   * @param procStsCode_6 procStsCode_6
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findL4925AEq(int acDate_0, int acDate_1, int custNo_2, int custNo_3, int repayCode_4, int repayCode_5, List<String> procStsCode_6, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = , AND BatchNo = 
   *
   * @param acDate_0 acDate_0
   * @param batchNo_1 batchNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findL4200AEq(int acDate_0, String batchNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = , AND BatchNo = , AND CustNo = , AND RepayAmt = , AND ProcStsCode ^i
   *
   * @param acDate_0 acDate_0
   * @param batchNo_1 batchNo_1
   * @param custNo_2 custNo_2
   * @param repayAmt_3 repayAmt_3
   * @param procStsCode_4 procStsCode_4
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public BatxDetail findL4200BFirst(int acDate_0, String batchNo_1, int custNo_2, BigDecimal repayAmt_3, List<String> procStsCode_4, TitaVo... titaVo);

  /**
   * AcDate = , AND BatchNo = , AND CustNo = , AND ProcStsCode ^i
   *
   * @param acDate_0 acDate_0
   * @param batchNo_1 batchNo_1
   * @param custNo_2 custNo_2
   * @param procStsCode_3 procStsCode_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findL4930CAEq(int acDate_0, String batchNo_1, int custNo_2, List<String> procStsCode_3, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = , AND BatchNo = , AND CustNo = , AND ProcStsCode ^i
   *
   * @param acDate_0 acDate_0
   * @param batchNo_1 batchNo_1
   * @param custNo_2 custNo_2
   * @param procStsCode_3 procStsCode_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findL4930CHEq(int acDate_0, String batchNo_1, int custNo_2, List<String> procStsCode_3, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = , AND BatchNo = , AND ProcStsCode ^i
   *
   * @param acDate_0 acDate_0
   * @param batchNo_1 batchNo_1
   * @param procStsCode_2 procStsCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findL4930BAEq(int acDate_0, String batchNo_1, List<String> procStsCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = , AND BatchNo = , AND ProcStsCode ^i
   *
   * @param acDate_0 acDate_0
   * @param batchNo_1 batchNo_1
   * @param procStsCode_2 procStsCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findL4930BHEq(int acDate_0, String batchNo_1, List<String> procStsCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= , AND AcDate &lt;= , AND RepayCode = 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param repayCode_2 repayCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> findL4454AEq(int acDate_0, int acDate_1, int repayCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = , AND FileName = 
   *
   * @param acDate_0 acDate_0
   * @param fileName_1 fileName_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxDetail BatxDetail of List
   */
  public Slice<BatxDetail> fileCheck(int acDate_0, String fileName_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By BatxDetail
   * 
   * @param batxDetailId key
   * @param titaVo Variable-Length Argument
   * @return BatxDetail BatxDetail
   */
  public BatxDetail holdById(BatxDetailId batxDetailId, TitaVo... titaVo);

  /**
   * hold By BatxDetail
   * 
   * @param batxDetail key
   * @param titaVo Variable-Length Argument
   * @return BatxDetail BatxDetail
   */
  public BatxDetail holdById(BatxDetail batxDetail, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param batxDetail Entity
   * @param titaVo Variable-Length Argument
   * @return BatxDetail Entity
   * @throws DBException exception
   */
  public BatxDetail insert(BatxDetail batxDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param batxDetail Entity
   * @param titaVo Variable-Length Argument
   * @return BatxDetail Entity
   * @throws DBException exception
   */
  public BatxDetail update(BatxDetail batxDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param batxDetail Entity
   * @param titaVo Variable-Length Argument
   * @return BatxDetail Entity
   * @throws DBException exception
   */
  public BatxDetail update2(BatxDetail batxDetail, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param batxDetail Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(BatxDetail batxDetail, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param batxDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<BatxDetail> batxDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param batxDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<BatxDetail> batxDetail, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param batxDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<BatxDetail> batxDetail, TitaVo... titaVo) throws DBException;

}
