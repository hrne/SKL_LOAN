package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.Ias39Loan34Data;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.Ias39Loan34DataId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias39Loan34DataService {

	/**
	 * findByPrimaryKey
	 *
	 * @param ias39Loan34DataId PK
	 * @param titaVo            Variable-Length Argument
	 * @return Ias39Loan34Data Ias39Loan34Data
	 */
	public Ias39Loan34Data findById(Ias39Loan34DataId ias39Loan34DataId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice Ias39Loan34Data Ias39Loan34Data of List
	 */
	public Slice<Ias39Loan34Data> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By Ias39Loan34Data
	 * 
	 * @param ias39Loan34DataId key
	 * @param titaVo            Variable-Length Argument
	 * @return Ias39Loan34Data Ias39Loan34Data
	 */
	public Ias39Loan34Data holdById(Ias39Loan34DataId ias39Loan34DataId, TitaVo... titaVo);

	/**
	 * hold By Ias39Loan34Data
	 * 
	 * @param ias39Loan34Data key
	 * @param titaVo          Variable-Length Argument
	 * @return Ias39Loan34Data Ias39Loan34Data
	 */
	public Ias39Loan34Data holdById(Ias39Loan34Data ias39Loan34Data, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param ias39Loan34Data Entity
	 * @param titaVo          Variable-Length Argument
	 * @return Ias39Loan34Data Entity
	 * @throws DBException exception
	 */
	public Ias39Loan34Data insert(Ias39Loan34Data ias39Loan34Data, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param ias39Loan34Data Entity
	 * @param titaVo          Variable-Length Argument
	 * @return Ias39Loan34Data Entity
	 * @throws DBException exception
	 */
	public Ias39Loan34Data update(Ias39Loan34Data ias39Loan34Data, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param ias39Loan34Data Entity
	 * @param titaVo          Variable-Length Argument
	 * @return Ias39Loan34Data Entity
	 * @throws DBException exception
	 */
	public Ias39Loan34Data update2(Ias39Loan34Data ias39Loan34Data, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param ias39Loan34Data Entity
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(Ias39Loan34Data ias39Loan34Data, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param ias39Loan34Data Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<Ias39Loan34Data> ias39Loan34Data, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param ias39Loan34Data Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<Ias39Loan34Data> ias39Loan34Data, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param ias39Loan34Data Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<Ias39Loan34Data> ias39Loan34Data, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (月底日日終批次)維護 Ias39Loan34Data 聯徵IAS39放款34號公報資料檔
	 * 
	 * @param TBSDYF int
	 * @param EmpNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L7_Ias39Loan34Data_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
