package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.InnFundApl;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface InnFundAplService {

	/**
	 * findByPrimaryKey
	 *
	 * @param acDate PK
	 * @param titaVo Variable-Length Argument
	 * @return InnFundApl InnFundApl
	 */
	public InnFundApl findById(int acDate, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice InnFundApl InnFundApl of List
	 */
	public Slice<InnFundApl> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * AcDate &gt;= ,AND AcDate &lt;=
	 *
	 * @param acDate_0 acDate_0
	 * @param acDate_1 acDate_1
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice InnFundApl InnFundApl of List
	 */
	public Slice<InnFundApl> acDateYearEq(int acDate_0, int acDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * ResrvStndrd &gt;
	 *
	 * @param resrvStndrd_0 resrvStndrd_0
	 * @param titaVo        Variable-Length Argument
	 * @return Slice InnFundApl InnFundApl of List
	 */
	public InnFundApl acDateFirst(BigDecimal resrvStndrd_0, TitaVo... titaVo);

	/**
	 * hold By InnFundApl
	 * 
	 * @param acDate key
	 * @param titaVo Variable-Length Argument
	 * @return InnFundApl InnFundApl
	 */
	public InnFundApl holdById(int acDate, TitaVo... titaVo);

	/**
	 * hold By InnFundApl
	 * 
	 * @param innFundApl key
	 * @param titaVo     Variable-Length Argument
	 * @return InnFundApl InnFundApl
	 */
	public InnFundApl holdById(InnFundApl innFundApl, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param innFundApl Entity
	 * @param titaVo     Variable-Length Argument
	 * @return InnFundApl Entity
	 * @throws DBException exception
	 */
	public InnFundApl insert(InnFundApl innFundApl, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param innFundApl Entity
	 * @param titaVo     Variable-Length Argument
	 * @return InnFundApl Entity
	 * @throws DBException exception
	 */
	public InnFundApl update(InnFundApl innFundApl, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param innFundApl Entity
	 * @param titaVo     Variable-Length Argument
	 * @return InnFundApl Entity
	 * @throws DBException exception
	 */
	public InnFundApl update2(InnFundApl innFundApl, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param innFundApl Entity
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(InnFundApl innFundApl, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param innFundApl Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<InnFundApl> innFundApl, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param innFundApl Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<InnFundApl> innFundApl, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param innFundApl Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<InnFundApl> innFundApl, TitaVo... titaVo) throws DBException;

}
