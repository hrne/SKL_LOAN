package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM014A;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.MonthlyLM014AId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM014AService {

	/**
	 * findByPrimaryKey
	 *
	 * @param monthlyLM014AId PK
	 * @param titaVo          Variable-Length Argument
	 * @return MonthlyLM014A MonthlyLM014A
	 */
	public MonthlyLM014A findById(MonthlyLM014AId monthlyLM014AId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice MonthlyLM014A MonthlyLM014A of List
	 */
	public Slice<MonthlyLM014A> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * DataYM =
	 *
	 * @param dataYM_0 dataYM_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice MonthlyLM014A MonthlyLM014A of List
	 */
	public Slice<MonthlyLM014A> DataYMEq(int dataYM_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By MonthlyLM014A
	 * 
	 * @param monthlyLM014AId key
	 * @param titaVo          Variable-Length Argument
	 * @return MonthlyLM014A MonthlyLM014A
	 */
	public MonthlyLM014A holdById(MonthlyLM014AId monthlyLM014AId, TitaVo... titaVo);

	/**
	 * hold By MonthlyLM014A
	 * 
	 * @param monthlyLM014A key
	 * @param titaVo        Variable-Length Argument
	 * @return MonthlyLM014A MonthlyLM014A
	 */
	public MonthlyLM014A holdById(MonthlyLM014A monthlyLM014A, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param monthlyLM014A Entity
	 * @param titaVo        Variable-Length Argument
	 * @return MonthlyLM014A Entity
	 * @throws DBException exception
	 */
	public MonthlyLM014A insert(MonthlyLM014A monthlyLM014A, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param monthlyLM014A Entity
	 * @param titaVo        Variable-Length Argument
	 * @return MonthlyLM014A Entity
	 * @throws DBException exception
	 */
	public MonthlyLM014A update(MonthlyLM014A monthlyLM014A, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param monthlyLM014A Entity
	 * @param titaVo        Variable-Length Argument
	 * @return MonthlyLM014A Entity
	 * @throws DBException exception
	 */
	public MonthlyLM014A update2(MonthlyLM014A monthlyLM014A, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param monthlyLM014A Entity
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(MonthlyLM014A monthlyLM014A, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param monthlyLM014A Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<MonthlyLM014A> monthlyLM014A, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param monthlyLM014A Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<MonthlyLM014A> monthlyLM014A, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param monthlyLM014A Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<MonthlyLM014A> monthlyLM014A, TitaVo... titaVo) throws DBException;

}
