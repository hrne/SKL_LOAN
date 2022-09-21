package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.InsuRenew;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.InsuRenewId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface InsuRenewService {

  /**
   * findByPrimaryKey
   *
   * @param insuRenewId PK
   * @param titaVo Variable-Length Argument
   * @return InsuRenew InsuRenew
   */
  public InsuRenew findById(InsuRenewId insuRenewId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = 
   *
   * @param acDate_0 acDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> selectA(int acDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = ,AND RepayCode = 
   *
   * @param acDate_0 acDate_0
   * @param repayCode_1 repayCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> selectB(int acDate_0, int repayCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * InsuYearMonth = 
   *
   * @param insuYearMonth_0 insuYearMonth_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> selectC(int insuYearMonth_0, int index, int limit, TitaVo... titaVo);

  /**
   * InsuYearMonth = ,AND RepayCode = 
   *
   * @param insuYearMonth_0 insuYearMonth_0
   * @param repayCode_1 repayCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> selectD(int insuYearMonth_0, int repayCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * InsuYearMonth = ,AND AcDate = ,AND StatusCode = 
   *
   * @param insuYearMonth_0 insuYearMonth_0
   * @param acDate_1 acDate_1
   * @param statusCode_2 statusCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> selectE(int insuYearMonth_0, int acDate_1, int statusCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * InsuYearMonth = ,AND AcDate &gt; ,AND StatusCode = 
   *
   * @param insuYearMonth_0 insuYearMonth_0
   * @param acDate_1 acDate_1
   * @param statusCode_2 statusCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> selectF(int insuYearMonth_0, int acDate_1, int statusCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * InsuYearMonth = ,AND StatusCode =
   *
   * @param insuYearMonth_0 insuYearMonth_0
   * @param statusCode_1 statusCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> selectG(int insuYearMonth_0, int statusCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * InsuYearMonth = ,AND RenewCode = 
   *
   * @param insuYearMonth_0 insuYearMonth_0
   * @param renewCode_1 renewCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> selectH(int insuYearMonth_0, int renewCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * InsuYearMonth = ,AND RepayCode = ,AND AcDate = ,AND StatusCode = 
   *
   * @param insuYearMonth_0 insuYearMonth_0
   * @param repayCode_1 repayCode_1
   * @param acDate_2 acDate_2
   * @param statusCode_3 statusCode_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> selectI(int insuYearMonth_0, int repayCode_1, int acDate_2, int statusCode_3, int index, int limit, TitaVo... titaVo);

  /**
   * InsuYearMonth = ,AND RepayCode = ,AND AcDate &gt; ,AND StatusCode = 
   *
   * @param insuYearMonth_0 insuYearMonth_0
   * @param repayCode_1 repayCode_1
   * @param acDate_2 acDate_2
   * @param statusCode_3 statusCode_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> selectJ(int insuYearMonth_0, int repayCode_1, int acDate_2, int statusCode_3, int index, int limit, TitaVo... titaVo);

  /**
   * InsuYearMonth = ,AND RepayCode = ,AND StatusCode =
   *
   * @param insuYearMonth_0 insuYearMonth_0
   * @param repayCode_1 repayCode_1
   * @param statusCode_2 statusCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> selectK(int insuYearMonth_0, int repayCode_1, int statusCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * InsuYearMonth = ,AND RepayCode = ,AND RenewCode = 
   *
   * @param insuYearMonth_0 insuYearMonth_0
   * @param repayCode_1 repayCode_1
   * @param renewCode_2 renewCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> selectL(int insuYearMonth_0, int repayCode_1, int renewCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> findNowInsuEq(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * InsuEndDate &gt;= ,AND InsuEndDate &lt;= 
   *
   * @param insuEndDate_0 insuEndDate_0
   * @param insuEndDate_1 insuEndDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> insuEndDateRange(int insuEndDate_0, int insuEndDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND PrevInsuNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param prevInsuNo_2 prevInsuNo_2
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public InsuRenew prevInsuNoFirst(int custNo_0, int facmNo_1, String prevInsuNo_2, TitaVo... titaVo);

  /**
   * InsuYearMonth = ,AND CustNo = ,AND FacmNo = 
   *
   * @param insuYearMonth_0 insuYearMonth_0
   * @param custNo_1 custNo_1
   * @param facmNo_2 facmNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> findL4601A(int insuYearMonth_0, int custNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * InsuYearMonth = ,AND ClCode1 = ,AND ClCode2 = ,AND ClNo = 
   *
   * @param insuYearMonth_0 insuYearMonth_0
   * @param clCode1_1 clCode1_1
   * @param clCode2_2 clCode2_2
   * @param clNo_3 clNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> findL4601B(int insuYearMonth_0, int clCode1_1, int clCode2_2, int clNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * InsuYearMonth = ,AND RenewCode = ,AND AcDate &gt;= ,AND AcDate &lt;=
   *
   * @param insuYearMonth_0 insuYearMonth_0
   * @param renewCode_1 renewCode_1
   * @param acDate_2 acDate_2
   * @param acDate_3 acDate_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> findL4604A(int insuYearMonth_0, int renewCode_1, int acDate_2, int acDate_3, int index, int limit, TitaVo... titaVo);

  /**
   * InsuYearMonth &gt;= ,AND InsuYearMonth &lt;= 
   *
   * @param insuYearMonth_0 insuYearMonth_0
   * @param insuYearMonth_1 insuYearMonth_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> findL4962A(int insuYearMonth_0, int insuYearMonth_1, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = ,AND NowInsuNo = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param nowInsuNo_3 nowInsuNo_3
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public InsuRenew findL4600AFirst(int clCode1_0, int clCode2_1, int clNo_2, String nowInsuNo_3, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = ,AND PrevInsuNo = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param prevInsuNo_3 prevInsuNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> findPrevInsuNoEq(int clCode1_0, int clCode2_1, int clNo_2, String prevInsuNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * NotiTempFg = 
   *
   * @param notiTempFg_0 notiTempFg_0
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public InsuRenew findNotiTempFgFirst(String notiTempFg_0, TitaVo... titaVo);

  /**
   * CustNo = 
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> findCustEq(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = ,AND NowInsuNo = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param nowInsuNo_3 nowInsuNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public Slice<InsuRenew> findNowInsuNoEq(int clCode1_0, int clCode2_1, int clNo_2, String nowInsuNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND NowInsuNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param nowInsuNo_2 nowInsuNo_2
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public InsuRenew findNowInsuNoFirst(int custNo_0, int facmNo_1, String nowInsuNo_2, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND PrevInsuNo = ,AND EndoInsuNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param prevInsuNo_2 prevInsuNo_2
   * @param endoInsuNo_3 endoInsuNo_3
   * @param titaVo Variable-Length Argument
   * @return Slice InsuRenew InsuRenew of List
   */
  public InsuRenew findEndoInsuNoFirst(int custNo_0, int facmNo_1, String prevInsuNo_2, String endoInsuNo_3, TitaVo... titaVo);

  /**
   * hold By InsuRenew
   * 
   * @param insuRenewId key
   * @param titaVo Variable-Length Argument
   * @return InsuRenew InsuRenew
   */
  public InsuRenew holdById(InsuRenewId insuRenewId, TitaVo... titaVo);

  /**
   * hold By InsuRenew
   * 
   * @param insuRenew key
   * @param titaVo Variable-Length Argument
   * @return InsuRenew InsuRenew
   */
  public InsuRenew holdById(InsuRenew insuRenew, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param insuRenew Entity
   * @param titaVo Variable-Length Argument
   * @return InsuRenew Entity
   * @throws DBException exception
   */
  public InsuRenew insert(InsuRenew insuRenew, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param insuRenew Entity
   * @param titaVo Variable-Length Argument
   * @return InsuRenew Entity
   * @throws DBException exception
   */
  public InsuRenew update(InsuRenew insuRenew, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param insuRenew Entity
   * @param titaVo Variable-Length Argument
   * @return InsuRenew Entity
   * @throws DBException exception
   */
  public InsuRenew update2(InsuRenew insuRenew, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param insuRenew Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(InsuRenew insuRenew, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param insuRenew Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<InsuRenew> insuRenew, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param insuRenew Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<InsuRenew> insuRenew, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param insuRenew Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<InsuRenew> insuRenew, TitaVo... titaVo) throws DBException;

}
