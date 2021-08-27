package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.InsuRenewMediaTemp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.InsuRenewMediaTempId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface InsuRenewMediaTempService {

	/**
	 * findByPrimaryKey
	 *
	 * @param insuRenewMediaTempId PK
	 * @param titaVo               Variable-Length Argument
	 * @return InsuRenewMediaTemp InsuRenewMediaTemp
	 */
	public InsuRenewMediaTemp findById(InsuRenewMediaTempId insuRenewMediaTempId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice InsuRenewMediaTemp InsuRenewMediaTemp of List
	 */
	public Slice<InsuRenewMediaTemp> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * FireInsuMonth &gt;= ,AND FireInsuMonth &lt;=
	 *
	 * @param fireInsuMonth_0 fireInsuMonth_0
	 * @param fireInsuMonth_1 fireInsuMonth_1
	 * @param index           Page Index
	 * @param limit           Page Data Limit
	 * @param titaVo          Variable-Length Argument
	 * @return Slice InsuRenewMediaTemp InsuRenewMediaTemp of List
	 */
	public Slice<InsuRenewMediaTemp> fireInsuMonthRg(String fireInsuMonth_0, String fireInsuMonth_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By InsuRenewMediaTemp
	 * 
	 * @param insuRenewMediaTempId key
	 * @param titaVo               Variable-Length Argument
	 * @return InsuRenewMediaTemp InsuRenewMediaTemp
	 */
	public InsuRenewMediaTemp holdById(InsuRenewMediaTempId insuRenewMediaTempId, TitaVo... titaVo);

	/**
	 * hold By InsuRenewMediaTemp
	 * 
	 * @param insuRenewMediaTemp key
	 * @param titaVo             Variable-Length Argument
	 * @return InsuRenewMediaTemp InsuRenewMediaTemp
	 */
	public InsuRenewMediaTemp holdById(InsuRenewMediaTemp insuRenewMediaTemp, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param insuRenewMediaTemp Entity
	 * @param titaVo             Variable-Length Argument
	 * @return InsuRenewMediaTemp Entity
	 * @throws DBException exception
	 */
	public InsuRenewMediaTemp insert(InsuRenewMediaTemp insuRenewMediaTemp, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param insuRenewMediaTemp Entity
	 * @param titaVo             Variable-Length Argument
	 * @return InsuRenewMediaTemp Entity
	 * @throws DBException exception
	 */
	public InsuRenewMediaTemp update(InsuRenewMediaTemp insuRenewMediaTemp, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param insuRenewMediaTemp Entity
	 * @param titaVo             Variable-Length Argument
	 * @return InsuRenewMediaTemp Entity
	 * @throws DBException exception
	 */
	public InsuRenewMediaTemp update2(InsuRenewMediaTemp insuRenewMediaTemp, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param insuRenewMediaTemp Entity
	 * @param titaVo             Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(InsuRenewMediaTemp insuRenewMediaTemp, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param insuRenewMediaTemp Entity of List
	 * @param titaVo             Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<InsuRenewMediaTemp> insuRenewMediaTemp, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param insuRenewMediaTemp Entity of List
	 * @param titaVo             Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<InsuRenewMediaTemp> insuRenewMediaTemp, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param insuRenewMediaTemp Entity of List
	 * @param titaVo             Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<InsuRenewMediaTemp> insuRenewMediaTemp, TitaVo... titaVo) throws DBException;

}
