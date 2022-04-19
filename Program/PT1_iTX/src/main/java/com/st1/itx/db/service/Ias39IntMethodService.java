package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.Ias39IntMethod;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.Ias39IntMethodId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias39IntMethodService {

	/**
	 * findByPrimaryKey
	 *
	 * @param ias39IntMethodId PK
	 * @param titaVo           Variable-Length Argument
	 * @return Ias39IntMethod Ias39IntMethod
	 */
	public Ias39IntMethod findById(Ias39IntMethodId ias39IntMethodId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice Ias39IntMethod Ias39IntMethod of List
	 */
	public Slice<Ias39IntMethod> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * YearMonth =
	 *
	 * @param yearMonth_0 yearMonth_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice Ias39IntMethod Ias39IntMethod of List
	 */
	public Slice<Ias39IntMethod> findYearMonthEq(int yearMonth_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By Ias39IntMethod
	 * 
	 * @param ias39IntMethodId key
	 * @param titaVo           Variable-Length Argument
	 * @return Ias39IntMethod Ias39IntMethod
	 */
	public Ias39IntMethod holdById(Ias39IntMethodId ias39IntMethodId, TitaVo... titaVo);

	/**
	 * hold By Ias39IntMethod
	 * 
	 * @param ias39IntMethod key
	 * @param titaVo         Variable-Length Argument
	 * @return Ias39IntMethod Ias39IntMethod
	 */
	public Ias39IntMethod holdById(Ias39IntMethod ias39IntMethod, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param ias39IntMethod Entity
	 * @param titaVo         Variable-Length Argument
	 * @return Ias39IntMethod Entity
	 * @throws DBException exception
	 */
	public Ias39IntMethod insert(Ias39IntMethod ias39IntMethod, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param ias39IntMethod Entity
	 * @param titaVo         Variable-Length Argument
	 * @return Ias39IntMethod Entity
	 * @throws DBException exception
	 */
	public Ias39IntMethod update(Ias39IntMethod ias39IntMethod, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param ias39IntMethod Entity
	 * @param titaVo         Variable-Length Argument
	 * @return Ias39IntMethod Entity
	 * @throws DBException exception
	 */
	public Ias39IntMethod update2(Ias39IntMethod ias39IntMethod, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param ias39IntMethod Entity
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(Ias39IntMethod ias39IntMethod, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param ias39IntMethod Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<Ias39IntMethod> ias39IntMethod, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param ias39IntMethod Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<Ias39IntMethod> ias39IntMethod, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param ias39IntMethod Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<Ias39IntMethod> ias39IntMethod, TitaVo... titaVo) throws DBException;

}
