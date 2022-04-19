package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.PfBsDetail;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfBsDetailService {

	/**
	 * findByPrimaryKey
	 *
	 * @param logNo  PK
	 * @param titaVo Variable-Length Argument
	 * @return PfBsDetail PfBsDetail
	 */
	public PfBsDetail findById(Long logNo, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice PfBsDetail PfBsDetail of List
	 */
	public Slice<PfBsDetail> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;=
	 *
	 * @param custNo_0 custNo_0
	 * @param facmNo_1 facmNo_1
	 * @param facmNo_2 facmNo_2
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice PfBsDetail PfBsDetail of List
	 */
	public Slice<PfBsDetail> findFacmNoRange(int custNo_0, int facmNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo);

	/**
	 * BsOfficer = ,AND WorkMonth&gt;= ,AND WorkMonth &lt;=
	 *
	 * @param bsOfficer_0 bsOfficer_0
	 * @param workMonth_1 workMonth_1
	 * @param workMonth_2 workMonth_2
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice PfBsDetail PfBsDetail of List
	 */
	public Slice<PfBsDetail> findBsOfficerAndWorkMonth(String bsOfficer_0, int workMonth_1, int workMonth_2, int index, int limit, TitaVo... titaVo);

	/**
	 * BsOfficer= ,AND WorkMonth=
	 *
	 * @param bsOfficer_0 bsOfficer_0
	 * @param workMonth_1 workMonth_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice PfBsDetail PfBsDetail of List
	 */
	public Slice<PfBsDetail> findBsOfficerOneMonth(String bsOfficer_0, int workMonth_1, int index, int limit, TitaVo... titaVo);

	/**
	 * PerfDate&gt;= ,AND PerfDate &lt;=
	 *
	 * @param perfDate_0 perfDate_0
	 * @param perfDate_1 perfDate_1
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice PfBsDetail PfBsDetail of List
	 */
	public Slice<PfBsDetail> findByPerfDate(int perfDate_0, int perfDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * BsOfficer= ,AND PerfDate&gt;= ,AND PerfDate &lt;=
	 *
	 * @param bsOfficer_0 bsOfficer_0
	 * @param perfDate_1  perfDate_1
	 * @param perfDate_2  perfDate_2
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice PfBsDetail PfBsDetail of List
	 */
	public Slice<PfBsDetail> findByBsOfficerAndPerfDate(String bsOfficer_0, int perfDate_1, int perfDate_2, int index, int limit, TitaVo... titaVo);

	/**
	 * PerfDate&gt;= ,AND PerfDate &lt;=
	 *
	 * @param perfDate_0 perfDate_0
	 * @param perfDate_1 perfDate_1
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice PfBsDetail PfBsDetail of List
	 */
	public Slice<PfBsDetail> findByPerfDateAndCustNo(int perfDate_0, int perfDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * WorkMonth &gt;= , AND WorkMonth&lt;=
	 *
	 * @param workMonth_0 workMonth_0
	 * @param workMonth_1 workMonth_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice PfBsDetail PfBsDetail of List
	 */
	public Slice<PfBsDetail> findByWorkMonth(int workMonth_0, int workMonth_1, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param facmNo_1 facmNo_1
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice PfBsDetail PfBsDetail of List
	 */
	public Slice<PfBsDetail> findByCustNoAndFacmNo(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice PfBsDetail PfBsDetail of List
	 */
	public Slice<PfBsDetail> findByCustNo(int custNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo = ,AND BormNo = ,AND PerfDate = ,AND RepayType = ,AND
	 * PieceCode =
	 *
	 * @param custNo_0    custNo_0
	 * @param facmNo_1    facmNo_1
	 * @param bormNo_2    bormNo_2
	 * @param perfDate_3  perfDate_3
	 * @param repayType_4 repayType_4
	 * @param pieceCode_5 pieceCode_5
	 * @param titaVo      Variable-Length Argument
	 * @return Slice PfBsDetail PfBsDetail of List
	 */
	public PfBsDetail findByTxFirst(int custNo_0, int facmNo_1, int bormNo_2, int perfDate_3, int repayType_4, String pieceCode_5, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo = ,AND BormNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param facmNo_1 facmNo_1
	 * @param bormNo_2 bormNo_2
	 * @param titaVo   Variable-Length Argument
	 * @return Slice PfBsDetail PfBsDetail of List
	 */
	public PfBsDetail findBormNoLatestFirst(int custNo_0, int facmNo_1, int bormNo_2, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo = ,AND BormNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param facmNo_1 facmNo_1
	 * @param bormNo_2 bormNo_2
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice PfBsDetail PfBsDetail of List
	 */
	public Slice<PfBsDetail> findBormNoEq(int custNo_0, int facmNo_1, int bormNo_2, int index, int limit, TitaVo... titaVo);

	/**
	 * DrawdownDate &gt;= ,AND DrawdownDate &lt;=
	 *
	 * @param drawdownDate_0 drawdownDate_0
	 * @param drawdownDate_1 drawdownDate_1
	 * @param index          Page Index
	 * @param limit          Page Data Limit
	 * @param titaVo         Variable-Length Argument
	 * @return Slice PfBsDetail PfBsDetail of List
	 */
	public Slice<PfBsDetail> findDrawdownBetween(int drawdownDate_0, int drawdownDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By PfBsDetail
	 * 
	 * @param logNo  key
	 * @param titaVo Variable-Length Argument
	 * @return PfBsDetail PfBsDetail
	 */
	public PfBsDetail holdById(Long logNo, TitaVo... titaVo);

	/**
	 * hold By PfBsDetail
	 * 
	 * @param pfBsDetail key
	 * @param titaVo     Variable-Length Argument
	 * @return PfBsDetail PfBsDetail
	 */
	public PfBsDetail holdById(PfBsDetail pfBsDetail, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param pfBsDetail Entity
	 * @param titaVo     Variable-Length Argument
	 * @return PfBsDetail Entity
	 * @throws DBException exception
	 */
	public PfBsDetail insert(PfBsDetail pfBsDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param pfBsDetail Entity
	 * @param titaVo     Variable-Length Argument
	 * @return PfBsDetail Entity
	 * @throws DBException exception
	 */
	public PfBsDetail update(PfBsDetail pfBsDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param pfBsDetail Entity
	 * @param titaVo     Variable-Length Argument
	 * @return PfBsDetail Entity
	 * @throws DBException exception
	 */
	public PfBsDetail update2(PfBsDetail pfBsDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param pfBsDetail Entity
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(PfBsDetail pfBsDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param pfBsDetail Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<PfBsDetail> pfBsDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param pfBsDetail Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<PfBsDetail> pfBsDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param pfBsDetail Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<PfBsDetail> pfBsDetail, TitaVo... titaVo) throws DBException;

}
