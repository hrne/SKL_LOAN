package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.PfReward;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfRewardService {

  /**
   * findByPrimaryKey
   *
   * @param logNo PK
   * @param titaVo Variable-Length Argument
   * @return PfReward PfReward
   */
  public PfReward findById(Long logNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfReward PfReward of List
   */
  public Slice<PfReward> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;= 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param facmNo_2 facmNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfReward PfReward of List
   */
  public Slice<PfReward> findFacmNoRange(int custNo_0, int facmNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = 
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfReward PfReward of List
   */
  public Slice<PfReward> findByCustNo(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfReward PfReward of List
   */
  public Slice<PfReward> findByCustNoAndFacmNo(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * Introducer=
   *
   * @param introducer_0 introducer_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfReward PfReward of List
   */
  public Slice<PfReward> findByIntroducer(String introducer_0, int index, int limit, TitaVo... titaVo);

  /**
   * WorkMonth&gt;= , AND WorkMonth&lt;=
   *
   * @param workMonth_0 workMonth_0
   * @param workMonth_1 workMonth_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfReward PfReward of List
   */
  public Slice<PfReward> findByWorkMonth(int workMonth_0, int workMonth_1, int index, int limit, TitaVo... titaVo);

  /**
   * Introducer= , AND WorkMonth&gt;= , AND WorkMonth&lt;=
   *
   * @param introducer_0 introducer_0
   * @param workMonth_1 workMonth_1
   * @param workMonth_2 workMonth_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfReward PfReward of List
   */
  public Slice<PfReward> findByItdWm(String introducer_0, int workMonth_1, int workMonth_2, int index, int limit, TitaVo... titaVo);

  /**
   * PerfDate &gt;= , AND PerfDate&lt;= 
   *
   * @param perfDate_0 perfDate_0
   * @param perfDate_1 perfDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfReward PfReward of List
   */
  public Slice<PfReward> findByPerfDate(int perfDate_0, int perfDate_1, int index, int limit, TitaVo... titaVo);

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
   * @return Slice PfReward PfReward of List
   */
  public PfReward findByTxFirst(int custNo_0, int facmNo_1, int bormNo_2, int perfDate_3, int repayType_4, String pieceCode_5, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND BormNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param bormNo_2 bormNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfReward PfReward of List
   */
  public Slice<PfReward> findBormNoEq(int custNo_0, int facmNo_1, int bormNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By PfReward
   * 
   * @param logNo key
   * @param titaVo Variable-Length Argument
   * @return PfReward PfReward
   */
  public PfReward holdById(Long logNo, TitaVo... titaVo);

  /**
   * hold By PfReward
   * 
   * @param pfReward key
   * @param titaVo Variable-Length Argument
   * @return PfReward PfReward
   */
  public PfReward holdById(PfReward pfReward, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param pfReward Entity
   * @param titaVo Variable-Length Argument
   * @return PfReward Entity
   * @throws DBException exception
   */
  public PfReward insert(PfReward pfReward, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param pfReward Entity
   * @param titaVo Variable-Length Argument
   * @return PfReward Entity
   * @throws DBException exception
   */
  public PfReward update(PfReward pfReward, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param pfReward Entity
   * @param titaVo Variable-Length Argument
   * @return PfReward Entity
   * @throws DBException exception
   */
  public PfReward update2(PfReward pfReward, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param pfReward Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(PfReward pfReward, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param pfReward Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<PfReward> pfReward, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param pfReward Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<PfReward> pfReward, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param pfReward Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<PfReward> pfReward, TitaVo... titaVo) throws DBException;

}
