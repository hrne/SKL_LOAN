package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicB080;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicB080Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB080Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicB080Id PK
	 * @param titaVo     Variable-Length Argument
	 * @return JcicB080 JcicB080
	 */
	public JcicB080 findById(JcicB080Id jcicB080Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicB080 JcicB080 of List
	 */
	public Slice<JcicB080> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicB080
	 * 
	 * @param jcicB080Id key
	 * @param titaVo     Variable-Length Argument
	 * @return JcicB080 JcicB080
	 */
	public JcicB080 holdById(JcicB080Id jcicB080Id, TitaVo... titaVo);

	/**
	 * hold By JcicB080
	 * 
	 * @param jcicB080 key
	 * @param titaVo   Variable-Length Argument
	 * @return JcicB080 JcicB080
	 */
	public JcicB080 holdById(JcicB080 jcicB080, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicB080 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicB080 Entity
	 * @throws DBException exception
	 */
	public JcicB080 insert(JcicB080 jcicB080, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicB080 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicB080 Entity
	 * @throws DBException exception
	 */
	public JcicB080 update(JcicB080 jcicB080, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicB080 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicB080 Entity
	 * @throws DBException exception
	 */
	public JcicB080 update2(JcicB080 jcicB080, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicB080 Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicB080 jcicB080, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicB080 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicB080> jcicB080, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicB080 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicB080> jcicB080, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicB080 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicB080> jcicB080, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (月底日日終批次)維護 JcicB080 每月聯徵授信額度資料檔
	 * 
	 * @param TBSDYF int
	 * @param EmpNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L8_JcicB080_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
