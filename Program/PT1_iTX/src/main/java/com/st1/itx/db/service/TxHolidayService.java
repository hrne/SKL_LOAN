package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxHoliday;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.TxHolidayId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxHolidayService {

	/**
	 * findByPrimaryKey
	 *
	 * @param txHolidayId PK
	 * @param titaVo      Variable-Length Argument
	 * @return TxHoliday TxHoliday
	 */
	public TxHoliday findById(TxHolidayId txHolidayId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TxHoliday TxHoliday of List
	 */
	public Slice<TxHoliday> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * Country = ,AND Holiday &gt;= ,AND Holiday &lt;=
	 *
	 * @param country_0 country_0
	 * @param holiday_1 holiday_1
	 * @param holiday_2 holiday_2
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice TxHoliday TxHoliday of List
	 */
	public Slice<TxHoliday> findHoliday(String country_0, int holiday_1, int holiday_2, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By TxHoliday
	 * 
	 * @param txHolidayId key
	 * @param titaVo      Variable-Length Argument
	 * @return TxHoliday TxHoliday
	 */
	public TxHoliday holdById(TxHolidayId txHolidayId, TitaVo... titaVo);

	/**
	 * hold By TxHoliday
	 * 
	 * @param txHoliday key
	 * @param titaVo    Variable-Length Argument
	 * @return TxHoliday TxHoliday
	 */
	public TxHoliday holdById(TxHoliday txHoliday, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param txHoliday Entity
	 * @param titaVo    Variable-Length Argument
	 * @return TxHoliday Entity
	 * @throws DBException exception
	 */
	public TxHoliday insert(TxHoliday txHoliday, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param txHoliday Entity
	 * @param titaVo    Variable-Length Argument
	 * @return TxHoliday Entity
	 * @throws DBException exception
	 */
	public TxHoliday update(TxHoliday txHoliday, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param txHoliday Entity
	 * @param titaVo    Variable-Length Argument
	 * @return TxHoliday Entity
	 * @throws DBException exception
	 */
	public TxHoliday update2(TxHoliday txHoliday, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param txHoliday Entity
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TxHoliday txHoliday, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param txHoliday Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TxHoliday> txHoliday, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param txHoliday Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TxHoliday> txHoliday, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param txHoliday Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TxHoliday> txHoliday, TitaVo... titaVo) throws DBException;

}
