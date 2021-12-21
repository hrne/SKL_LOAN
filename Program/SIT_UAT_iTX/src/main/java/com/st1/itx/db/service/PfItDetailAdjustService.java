package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.PfItDetailAdjust;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfItDetailAdjustService {

	/**
	 * findByPrimaryKey
	 *
	 * @param logNo  PK
	 * @param titaVo Variable-Length Argument
	 * @return PfItDetailAdjust PfItDetailAdjust
	 */
	public PfItDetailAdjust findById(Long logNo, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice PfItDetailAdjust PfItDetailAdjust of List
	 */
	public Slice<PfItDetailAdjust> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo = ,AND BormNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param facmNo_1 facmNo_1
	 * @param bormNo_2 bormNo_2
	 * @param titaVo   Variable-Length Argument
	 * @return Slice PfItDetailAdjust PfItDetailAdjust of List
	 */
	public PfItDetailAdjust findCustFacmBormFirst(int custNo_0, int facmNo_1, int bormNo_2, TitaVo... titaVo);

	/**
	 * hold By PfItDetailAdjust
	 * 
	 * @param logNo  key
	 * @param titaVo Variable-Length Argument
	 * @return PfItDetailAdjust PfItDetailAdjust
	 */
	public PfItDetailAdjust holdById(Long logNo, TitaVo... titaVo);

	/**
	 * hold By PfItDetailAdjust
	 * 
	 * @param pfItDetailAdjust key
	 * @param titaVo           Variable-Length Argument
	 * @return PfItDetailAdjust PfItDetailAdjust
	 */
	public PfItDetailAdjust holdById(PfItDetailAdjust pfItDetailAdjust, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param pfItDetailAdjust Entity
	 * @param titaVo           Variable-Length Argument
	 * @return PfItDetailAdjust Entity
	 * @throws DBException exception
	 */
	public PfItDetailAdjust insert(PfItDetailAdjust pfItDetailAdjust, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param pfItDetailAdjust Entity
	 * @param titaVo           Variable-Length Argument
	 * @return PfItDetailAdjust Entity
	 * @throws DBException exception
	 */
	public PfItDetailAdjust update(PfItDetailAdjust pfItDetailAdjust, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param pfItDetailAdjust Entity
	 * @param titaVo           Variable-Length Argument
	 * @return PfItDetailAdjust Entity
	 * @throws DBException exception
	 */
	public PfItDetailAdjust update2(PfItDetailAdjust pfItDetailAdjust, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param pfItDetailAdjust Entity
	 * @param titaVo           Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(PfItDetailAdjust pfItDetailAdjust, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param pfItDetailAdjust Entity of List
	 * @param titaVo           Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<PfItDetailAdjust> pfItDetailAdjust, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param pfItDetailAdjust Entity of List
	 * @param titaVo           Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<PfItDetailAdjust> pfItDetailAdjust, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param pfItDetailAdjust Entity of List
	 * @param titaVo           Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<PfItDetailAdjust> pfItDetailAdjust, TitaVo... titaVo) throws DBException;

}
