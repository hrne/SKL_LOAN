package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.PfItDetail;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfItDetailService {

  /**
   * findByPrimaryKey
   *
   * @param logNo PK
   * @param titaVo Variable-Length Argument
   * @return PfItDetail PfItDetail
   */
  public PfItDetail findById(Long logNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfItDetail PfItDetail of List
   */
  public Slice<PfItDetail> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;= 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param facmNo_2 facmNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfItDetail PfItDetail of List
   */
  public Slice<PfItDetail> findFacmNoRange(int custNo_0, int facmNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * WorkMonth &gt;= ,AND WorkMonth&lt;=
   *
   * @param workMonth_0 workMonth_0
   * @param workMonth_1 workMonth_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfItDetail PfItDetail of List
   */
  public Slice<PfItDetail> findByWorkMonth(int workMonth_0, int workMonth_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo =
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfItDetail PfItDetail of List
   */
  public Slice<PfItDetail> findByCustNoAndFacmNo(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = 
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfItDetail PfItDetail of List
   */
  public Slice<PfItDetail> findByCustNo(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * DrawdownDate &gt;= ,AND DrawdownDate&lt;=
   *
   * @param drawdownDate_0 drawdownDate_0
   * @param drawdownDate_1 drawdownDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfItDetail PfItDetail of List
   */
  public Slice<PfItDetail> findByDrawdownDate(int drawdownDate_0, int drawdownDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * PerfDate&gt;= ,AND PerfDate&lt;= 
   *
   * @param perfDate_0 perfDate_0
   * @param perfDate_1 perfDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfItDetail PfItDetail of List
   */
  public Slice<PfItDetail> findByPerfDate(int perfDate_0, int perfDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * RewardDate= ,AND MediaFg = 
   *
   * @param rewardDate_0 rewardDate_0
   * @param mediaFg_1 mediaFg_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfItDetail PfItDetail of List
   */
  public Slice<PfItDetail> findByRewardDate(int rewardDate_0, int mediaFg_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND BormNo = ,AND PerfDate = ,AND RepayType = ,AND PieceCode = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param bormNo_2 bormNo_2
   * @param perfDate_3 perfDate_3
   * @param repayType_4 repayType_4
   * @param pieceCode_5 pieceCode_5
   * @param titaVo Variable-Length Argument
   * @return Slice PfItDetail PfItDetail of List
   */
  public PfItDetail findByTxFirst(int custNo_0, int facmNo_1, int bormNo_2, int perfDate_3, int repayType_4, String pieceCode_5, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND BormNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param bormNo_2 bormNo_2
   * @param titaVo Variable-Length Argument
   * @return Slice PfItDetail PfItDetail of List
   */
  public PfItDetail findBormNoLatestFirst(int custNo_0, int facmNo_1, int bormNo_2, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND BormNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param bormNo_2 bormNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfItDetail PfItDetail of List
   */
  public Slice<PfItDetail> findBormNoEq(int custNo_0, int facmNo_1, int bormNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By PfItDetail
   * 
   * @param logNo key
   * @param titaVo Variable-Length Argument
   * @return PfItDetail PfItDetail
   */
  public PfItDetail holdById(Long logNo, TitaVo... titaVo);

  /**
   * hold By PfItDetail
   * 
   * @param pfItDetail key
   * @param titaVo Variable-Length Argument
   * @return PfItDetail PfItDetail
   */
  public PfItDetail holdById(PfItDetail pfItDetail, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param pfItDetail Entity
   * @param titaVo Variable-Length Argument
   * @return PfItDetail Entity
   * @throws DBException exception
   */
  public PfItDetail insert(PfItDetail pfItDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param pfItDetail Entity
   * @param titaVo Variable-Length Argument
   * @return PfItDetail Entity
   * @throws DBException exception
   */
  public PfItDetail update(PfItDetail pfItDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param pfItDetail Entity
   * @param titaVo Variable-Length Argument
   * @return PfItDetail Entity
   * @throws DBException exception
   */
  public PfItDetail update2(PfItDetail pfItDetail, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param pfItDetail Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(PfItDetail pfItDetail, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param pfItDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<PfItDetail> pfItDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param pfItDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<PfItDetail> pfItDetail, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param pfItDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<PfItDetail> pfItDetail, TitaVo... titaVo) throws DBException;

}
