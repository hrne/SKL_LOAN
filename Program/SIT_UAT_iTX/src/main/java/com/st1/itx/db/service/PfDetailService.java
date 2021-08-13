package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.PfDetail;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfDetailService {

  /**
   * findByPrimaryKey
   *
   * @param logNo PK
   * @param titaVo Variable-Length Argument
   * @return PfDetail PfDetail
   */
  public PfDetail findById(Long logNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfDetail PfDetail of List
   */
  public Slice<PfDetail> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * PerfDate&gt;= , AND PerfDate&lt;= 
   *
   * @param perfDate_0 perfDate_0
   * @param perfDate_1 perfDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfDetail PfDetail of List
   */
  public Slice<PfDetail> findByPerfDate(int perfDate_0, int perfDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;= ,AND WorkMonth &gt;= ,AND WorkMonth &lt;=
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param facmNo_2 facmNo_2
   * @param workMonth_3 workMonth_3
   * @param workMonth_4 workMonth_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfDetail PfDetail of List
   */
  public Slice<PfDetail> findFacmNoRange(int custNo_0, int facmNo_1, int facmNo_2, int workMonth_3, int workMonth_4, int index, int limit, TitaVo... titaVo);

  /**
   * WorkMonth &gt;= ,AND WorkMonth &lt;=
   *
   * @param workMonth_0 workMonth_0
   * @param workMonth_1 workMonth_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfDetail PfDetail of List
   */
  public Slice<PfDetail> findByWorkMonth(int workMonth_0, int workMonth_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND BormNo = ,AND BorxNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param bormNo_2 bormNo_2
   * @param borxNo_3 borxNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfDetail PfDetail of List
   */
  public Slice<PfDetail> findByBorxNo(int custNo_0, int facmNo_1, int bormNo_2, int borxNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * hold By PfDetail
   * 
   * @param logNo key
   * @param titaVo Variable-Length Argument
   * @return PfDetail PfDetail
   */
  public PfDetail holdById(Long logNo, TitaVo... titaVo);

  /**
   * hold By PfDetail
   * 
   * @param pfDetail key
   * @param titaVo Variable-Length Argument
   * @return PfDetail PfDetail
   */
  public PfDetail holdById(PfDetail pfDetail, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param pfDetail Entity
   * @param titaVo Variable-Length Argument
   * @return PfDetail Entity
   * @throws DBException exception
   */
  public PfDetail insert(PfDetail pfDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param pfDetail Entity
   * @param titaVo Variable-Length Argument
   * @return PfDetail Entity
   * @throws DBException exception
   */
  public PfDetail update(PfDetail pfDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param pfDetail Entity
   * @param titaVo Variable-Length Argument
   * @return PfDetail Entity
   * @throws DBException exception
   */
  public PfDetail update2(PfDetail pfDetail, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param pfDetail Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(PfDetail pfDetail, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param pfDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<PfDetail> pfDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param pfDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<PfDetail> pfDetail, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param pfDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<PfDetail> pfDetail, TitaVo... titaVo) throws DBException;

}
