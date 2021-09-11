package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxToDoDetailReserve;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.TxToDoDetailReserveId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxToDoDetailReserveService {

	/**
	 * findByPrimaryKey
	 *
	 * @param txToDoDetailReserveId PK
	 * @param titaVo                Variable-Length Argument
	 * @return TxToDoDetailReserve TxToDoDetailReserve
	 */
	public TxToDoDetailReserve findById(TxToDoDetailReserveId txToDoDetailReserveId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TxToDoDetailReserve TxToDoDetailReserve of List
	 */
	public Slice<TxToDoDetailReserve> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * ItemCode = ,AND Status &gt;= ,AND Status &lt;=
	 *
	 * @param itemCode_0 itemCode_0
	 * @param status_1   status_1
	 * @param status_2   status_2
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice TxToDoDetailReserve TxToDoDetailReserve of List
	 */
	public Slice<TxToDoDetailReserve> detailStatusRange(String itemCode_0, int status_1, int status_2, int index, int limit, TitaVo... titaVo);

	/**
	 * ItemCode = ,AND Status &gt;= ,AND Status &lt;= ,AND DataDate &gt;= ,AND
	 * DataDate &lt;=
	 *
	 * @param itemCode_0 itemCode_0
	 * @param status_1   status_1
	 * @param status_2   status_2
	 * @param dataDate_3 dataDate_3
	 * @param dataDate_4 dataDate_4
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice TxToDoDetailReserve TxToDoDetailReserve of List
	 */
	public Slice<TxToDoDetailReserve> DataDateRange(String itemCode_0, int status_1, int status_2, int dataDate_3, int dataDate_4, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By TxToDoDetailReserve
	 * 
	 * @param txToDoDetailReserveId key
	 * @param titaVo                Variable-Length Argument
	 * @return TxToDoDetailReserve TxToDoDetailReserve
	 */
	public TxToDoDetailReserve holdById(TxToDoDetailReserveId txToDoDetailReserveId, TitaVo... titaVo);

	/**
	 * hold By TxToDoDetailReserve
	 * 
	 * @param txToDoDetailReserve key
	 * @param titaVo              Variable-Length Argument
	 * @return TxToDoDetailReserve TxToDoDetailReserve
	 */
	public TxToDoDetailReserve holdById(TxToDoDetailReserve txToDoDetailReserve, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param txToDoDetailReserve Entity
	 * @param titaVo              Variable-Length Argument
	 * @return TxToDoDetailReserve Entity
	 * @throws DBException exception
	 */
	public TxToDoDetailReserve insert(TxToDoDetailReserve txToDoDetailReserve, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param txToDoDetailReserve Entity
	 * @param titaVo              Variable-Length Argument
	 * @return TxToDoDetailReserve Entity
	 * @throws DBException exception
	 */
	public TxToDoDetailReserve update(TxToDoDetailReserve txToDoDetailReserve, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param txToDoDetailReserve Entity
	 * @param titaVo              Variable-Length Argument
	 * @return TxToDoDetailReserve Entity
	 * @throws DBException exception
	 */
	public TxToDoDetailReserve update2(TxToDoDetailReserve txToDoDetailReserve, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param txToDoDetailReserve Entity
	 * @param titaVo              Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TxToDoDetailReserve txToDoDetailReserve, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param txToDoDetailReserve Entity of List
	 * @param titaVo              Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TxToDoDetailReserve> txToDoDetailReserve, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param txToDoDetailReserve Entity of List
	 * @param titaVo              Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TxToDoDetailReserve> txToDoDetailReserve, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param txToDoDetailReserve Entity of List
	 * @param titaVo              Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TxToDoDetailReserve> txToDoDetailReserve, TitaVo... titaVo) throws DBException;

}
