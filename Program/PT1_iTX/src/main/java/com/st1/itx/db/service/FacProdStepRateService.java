package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FacProdStepRate;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.FacProdStepRateId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacProdStepRateService {

	/**
	 * findByPrimaryKey
	 *
	 * @param facProdStepRateId PK
	 * @param titaVo            Variable-Length Argument
	 * @return FacProdStepRate FacProdStepRate
	 */
	public FacProdStepRate findById(FacProdStepRateId facProdStepRateId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice FacProdStepRate FacProdStepRate of List
	 */
	public Slice<FacProdStepRate> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * ProdNo = ,AND MonthStart &gt;= ,AND MonthStart &lt;=
	 *
	 * @param prodNo_0     prodNo_0
	 * @param monthStart_1 monthStart_1
	 * @param monthStart_2 monthStart_2
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice FacProdStepRate FacProdStepRate of List
	 */
	public Slice<FacProdStepRate> stepRateProdNoEq(String prodNo_0, int monthStart_1, int monthStart_2, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By FacProdStepRate
	 * 
	 * @param facProdStepRateId key
	 * @param titaVo            Variable-Length Argument
	 * @return FacProdStepRate FacProdStepRate
	 */
	public FacProdStepRate holdById(FacProdStepRateId facProdStepRateId, TitaVo... titaVo);

	/**
	 * hold By FacProdStepRate
	 * 
	 * @param facProdStepRate key
	 * @param titaVo          Variable-Length Argument
	 * @return FacProdStepRate FacProdStepRate
	 */
	public FacProdStepRate holdById(FacProdStepRate facProdStepRate, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param facProdStepRate Entity
	 * @param titaVo          Variable-Length Argument
	 * @return FacProdStepRate Entity
	 * @throws DBException exception
	 */
	public FacProdStepRate insert(FacProdStepRate facProdStepRate, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param facProdStepRate Entity
	 * @param titaVo          Variable-Length Argument
	 * @return FacProdStepRate Entity
	 * @throws DBException exception
	 */
	public FacProdStepRate update(FacProdStepRate facProdStepRate, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param facProdStepRate Entity
	 * @param titaVo          Variable-Length Argument
	 * @return FacProdStepRate Entity
	 * @throws DBException exception
	 */
	public FacProdStepRate update2(FacProdStepRate facProdStepRate, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param facProdStepRate Entity
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(FacProdStepRate facProdStepRate, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param facProdStepRate Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<FacProdStepRate> facProdStepRate, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param facProdStepRate Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<FacProdStepRate> facProdStepRate, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param facProdStepRate Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<FacProdStepRate> facProdStepRate, TitaVo... titaVo) throws DBException;

}
