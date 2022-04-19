package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.SpecInnReCheck;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.SpecInnReCheckId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface SpecInnReCheckService {

	/**
	 * findByPrimaryKey
	 *
	 * @param specInnReCheckId PK
	 * @param titaVo           Variable-Length Argument
	 * @return SpecInnReCheck SpecInnReCheck
	 */
	public SpecInnReCheck findById(SpecInnReCheckId specInnReCheckId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice SpecInnReCheck SpecInnReCheck of List
	 */
	public Slice<SpecInnReCheck> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice SpecInnReCheck SpecInnReCheck of List
	 */
	public Slice<SpecInnReCheck> findCustNo(int custNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param facmNo_1 facmNo_1
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice SpecInnReCheck SpecInnReCheck of List
	 */
	public Slice<SpecInnReCheck> findCustFacmNo(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By SpecInnReCheck
	 * 
	 * @param specInnReCheckId key
	 * @param titaVo           Variable-Length Argument
	 * @return SpecInnReCheck SpecInnReCheck
	 */
	public SpecInnReCheck holdById(SpecInnReCheckId specInnReCheckId, TitaVo... titaVo);

	/**
	 * hold By SpecInnReCheck
	 * 
	 * @param specInnReCheck key
	 * @param titaVo         Variable-Length Argument
	 * @return SpecInnReCheck SpecInnReCheck
	 */
	public SpecInnReCheck holdById(SpecInnReCheck specInnReCheck, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param specInnReCheck Entity
	 * @param titaVo         Variable-Length Argument
	 * @return SpecInnReCheck Entity
	 * @throws DBException exception
	 */
	public SpecInnReCheck insert(SpecInnReCheck specInnReCheck, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param specInnReCheck Entity
	 * @param titaVo         Variable-Length Argument
	 * @return SpecInnReCheck Entity
	 * @throws DBException exception
	 */
	public SpecInnReCheck update(SpecInnReCheck specInnReCheck, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param specInnReCheck Entity
	 * @param titaVo         Variable-Length Argument
	 * @return SpecInnReCheck Entity
	 * @throws DBException exception
	 */
	public SpecInnReCheck update2(SpecInnReCheck specInnReCheck, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param specInnReCheck Entity
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(SpecInnReCheck specInnReCheck, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param specInnReCheck Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<SpecInnReCheck> specInnReCheck, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param specInnReCheck Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<SpecInnReCheck> specInnReCheck, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param specInnReCheck Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<SpecInnReCheck> specInnReCheck, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (日終批次)維護 InnReCheck 覆審案件明細檔
	 * 
	 * @param tbsdyf int
	 * @param empNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L5_InnReCheck_Upd(int tbsdyf, String empNo, TitaVo... titaVo);

}
