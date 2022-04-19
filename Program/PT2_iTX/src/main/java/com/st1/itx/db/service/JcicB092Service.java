package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicB092;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicB092Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB092Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicB092Id PK
	 * @param titaVo     Variable-Length Argument
	 * @return JcicB092 JcicB092
	 */
	public JcicB092 findById(JcicB092Id jcicB092Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicB092 JcicB092 of List
	 */
	public Slice<JcicB092> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicB092
	 * 
	 * @param jcicB092Id key
	 * @param titaVo     Variable-Length Argument
	 * @return JcicB092 JcicB092
	 */
	public JcicB092 holdById(JcicB092Id jcicB092Id, TitaVo... titaVo);

	/**
	 * hold By JcicB092
	 * 
	 * @param jcicB092 key
	 * @param titaVo   Variable-Length Argument
	 * @return JcicB092 JcicB092
	 */
	public JcicB092 holdById(JcicB092 jcicB092, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicB092 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicB092 Entity
	 * @throws DBException exception
	 */
	public JcicB092 insert(JcicB092 jcicB092, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicB092 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicB092 Entity
	 * @throws DBException exception
	 */
	public JcicB092 update(JcicB092 jcicB092, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicB092 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicB092 Entity
	 * @throws DBException exception
	 */
	public JcicB092 update2(JcicB092 jcicB092, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicB092 Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicB092 jcicB092, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicB092 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicB092> jcicB092, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicB092 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicB092> jcicB092, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicB092 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicB092> jcicB092, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (每月日終批次)維護 JcicB092 每月聯徵不動產擔保品明細檔
	 * 
	 * @param TBSDYF int
	 * @param EmpNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L8_JcicB092_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
