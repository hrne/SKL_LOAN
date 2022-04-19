package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicB680;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicB680Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB680Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param jcicB680Id PK
	 * @param titaVo     Variable-Length Argument
	 * @return JcicB680 JcicB680
	 */
	public JcicB680 findById(JcicB680Id jcicB680Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice JcicB680 JcicB680 of List
	 */
	public Slice<JcicB680> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By JcicB680
	 * 
	 * @param jcicB680Id key
	 * @param titaVo     Variable-Length Argument
	 * @return JcicB680 JcicB680
	 */
	public JcicB680 holdById(JcicB680Id jcicB680Id, TitaVo... titaVo);

	/**
	 * hold By JcicB680
	 * 
	 * @param jcicB680 key
	 * @param titaVo   Variable-Length Argument
	 * @return JcicB680 JcicB680
	 */
	public JcicB680 holdById(JcicB680 jcicB680, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param jcicB680 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicB680 Entity
	 * @throws DBException exception
	 */
	public JcicB680 insert(JcicB680 jcicB680, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param jcicB680 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicB680 Entity
	 * @throws DBException exception
	 */
	public JcicB680 update(JcicB680 jcicB680, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param jcicB680 Entity
	 * @param titaVo   Variable-Length Argument
	 * @return JcicB680 Entity
	 * @throws DBException exception
	 */
	public JcicB680 update2(JcicB680 jcicB680, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param jcicB680 Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(JcicB680 jcicB680, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param jcicB680 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<JcicB680> jcicB680, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param jcicB680 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<JcicB680> jcicB680, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param jcicB680 Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<JcicB680> jcicB680, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (每月日終批次)維護 JcicB680 每月聯徵貸款餘額扣除擔保品鑑估值之金額資料檔
	 * 
	 * @param TBSDYF int
	 * @param EmpNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L8_JcicB680_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo);

}
