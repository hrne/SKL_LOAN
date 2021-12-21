package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.Ias34Cp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.Ias34CpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias34CpService {

	/**
	 * findByPrimaryKey
	 *
	 * @param ias34CpId PK
	 * @param titaVo    Variable-Length Argument
	 * @return Ias34Cp Ias34Cp
	 */
	public Ias34Cp findById(Ias34CpId ias34CpId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice Ias34Cp Ias34Cp of List
	 */
	public Slice<Ias34Cp> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By Ias34Cp
	 * 
	 * @param ias34CpId key
	 * @param titaVo    Variable-Length Argument
	 * @return Ias34Cp Ias34Cp
	 */
	public Ias34Cp holdById(Ias34CpId ias34CpId, TitaVo... titaVo);

	/**
	 * hold By Ias34Cp
	 * 
	 * @param ias34Cp key
	 * @param titaVo  Variable-Length Argument
	 * @return Ias34Cp Ias34Cp
	 */
	public Ias34Cp holdById(Ias34Cp ias34Cp, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param ias34Cp Entity
	 * @param titaVo  Variable-Length Argument
	 * @return Ias34Cp Entity
	 * @throws DBException exception
	 */
	public Ias34Cp insert(Ias34Cp ias34Cp, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param ias34Cp Entity
	 * @param titaVo  Variable-Length Argument
	 * @return Ias34Cp Entity
	 * @throws DBException exception
	 */
	public Ias34Cp update(Ias34Cp ias34Cp, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param ias34Cp Entity
	 * @param titaVo  Variable-Length Argument
	 * @return Ias34Cp Entity
	 * @throws DBException exception
	 */
	public Ias34Cp update2(Ias34Cp ias34Cp, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param ias34Cp Entity
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(Ias34Cp ias34Cp, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param ias34Cp Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<Ias34Cp> ias34Cp, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param ias34Cp Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<Ias34Cp> ias34Cp, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param ias34Cp Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<Ias34Cp> ias34Cp, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (月底日日終批次)維護 IAS34 欄位清單C檔
	 * 
	 * @param TBSDYF int
	 * @param EmpNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L7_Ias34Cp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
