package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicB211;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicB211Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB211Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicB211Id PK
	 * @param titaVo     Variable-Length Argument
	 * @return JcicB211 JcicB211
	 */
	public JcicB211 findById(JcicB211Id jcicB211Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicB211 JcicB211 of List
	 */
	public Slice<JcicB211> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicB211
	 * 
	 * @param jcicB211Id key
	 * @param titaVo     Variable-Length Argument
	 * @return JcicB211 JcicB211
	 */
	public JcicB211 holdById(JcicB211Id jcicB211Id, TitaVo... titaVo);

	/**
	 * hold By JcicB211
	 * 
	 * @param jcicB211 key
	 * @param titaVo   Variable-Length Argument
	 * @return JcicB211 JcicB211
	 */
	public JcicB211 holdById(JcicB211 jcicB211, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicB211 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicB211 Entity
	 * @throws DBException exception
	 */
	public JcicB211 insert(JcicB211 jcicB211, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicB211 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicB211 Entity
	 * @throws DBException exception
	 */
	public JcicB211 update(JcicB211 jcicB211, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicB211 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicB211 Entity
	 * @throws DBException exception
	 */
	public JcicB211 update2(JcicB211 jcicB211, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicB211 Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicB211 jcicB211, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicB211 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicB211> jcicB211, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicB211 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicB211> jcicB211, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicB211 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicB211> jcicB211, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (每日日終批次)維護 JcicB211 聯徵每日授信餘額變動資料檔
	 * 
	 * @param TBSDYF int
	 * @param EmpNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L8_JcicB211_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
