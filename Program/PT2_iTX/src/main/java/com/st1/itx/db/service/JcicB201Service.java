package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicB201;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicB201Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB201Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicB201Id PK
	 * @param titaVo     Variable-Length Argument
	 * @return JcicB201 JcicB201
	 */
	public JcicB201 findById(JcicB201Id jcicB201Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicB201 JcicB201 of List
	 */
	public Slice<JcicB201> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicB201
	 * 
	 * @param jcicB201Id key
	 * @param titaVo     Variable-Length Argument
	 * @return JcicB201 JcicB201
	 */
	public JcicB201 holdById(JcicB201Id jcicB201Id, TitaVo... titaVo);

	/**
	 * hold By JcicB201
	 * 
	 * @param jcicB201 key
	 * @param titaVo   Variable-Length Argument
	 * @return JcicB201 JcicB201
	 */
	public JcicB201 holdById(JcicB201 jcicB201, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicB201 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicB201 Entity
	 * @throws DBException exception
	 */
	public JcicB201 insert(JcicB201 jcicB201, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicB201 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicB201 Entity
	 * @throws DBException exception
	 */
	public JcicB201 update(JcicB201 jcicB201, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicB201 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicB201 Entity
	 * @throws DBException exception
	 */
	public JcicB201 update2(JcicB201 jcicB201, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicB201 Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicB201 jcicB201, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicB201 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicB201> jcicB201, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicB201 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicB201> jcicB201, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicB201 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicB201> jcicB201, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (月底日日終批次)維護 JcicB201 聯徵授信餘額月報資料檔
	 * 
	 * @param TBSDYF int
	 * @param EmpNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L8_JcicB201_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
