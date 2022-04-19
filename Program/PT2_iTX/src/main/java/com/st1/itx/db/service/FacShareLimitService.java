package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FacShareLimit;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacShareLimitService {

	/**
	 * findByPrimaryKey
	 *
	 * @param applNo PK
	 * @param titaVo Variable-Length Argument
	 * @return FacShareLimit FacShareLimit
	 */
	public FacShareLimit findById(int applNo, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice FacShareLimit FacShareLimit of List
	 */
	public Slice<FacShareLimit> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * MainApplNo =
	 *
	 * @param mainApplNo_0 mainApplNo_0
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice FacShareLimit FacShareLimit of List
	 */
	public Slice<FacShareLimit> findMainApplNoEq(int mainApplNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice FacShareLimit FacShareLimit of List
	 */
	public Slice<FacShareLimit> findCustNoEq(int custNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By FacShareLimit
	 * 
	 * @param applNo key
	 * @param titaVo Variable-Length Argument
	 * @return FacShareLimit FacShareLimit
	 */
	public FacShareLimit holdById(int applNo, TitaVo... titaVo);

	/**
	 * hold By FacShareLimit
	 * 
	 * @param facShareLimit key
	 * @param titaVo        Variable-Length Argument
	 * @return FacShareLimit FacShareLimit
	 */
	public FacShareLimit holdById(FacShareLimit facShareLimit, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param facShareLimit Entity
	 * @param titaVo        Variable-Length Argument
	 * @return FacShareLimit Entity
	 * @throws DBException exception
	 */
	public FacShareLimit insert(FacShareLimit facShareLimit, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param facShareLimit Entity
	 * @param titaVo        Variable-Length Argument
	 * @return FacShareLimit Entity
	 * @throws DBException exception
	 */
	public FacShareLimit update(FacShareLimit facShareLimit, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param facShareLimit Entity
	 * @param titaVo        Variable-Length Argument
	 * @return FacShareLimit Entity
	 * @throws DBException exception
	 */
	public FacShareLimit update2(FacShareLimit facShareLimit, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param facShareLimit Entity
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(FacShareLimit facShareLimit, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param facShareLimit Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<FacShareLimit> facShareLimit, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param facShareLimit Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<FacShareLimit> facShareLimit, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param facShareLimit Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<FacShareLimit> facShareLimit, TitaVo... titaVo) throws DBException;

}
