package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxAttachment;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxAttachmentService {

	/**
	 * findByPrimaryKey
	 *
	 * @param fileNo PK
	 * @param titaVo Variable-Length Argument
	 * @return TxAttachment TxAttachment
	 */
	public TxAttachment findById(Long fileNo, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TxAttachment TxAttachment of List
	 */
	public Slice<TxAttachment> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * TranNo = ,AND MrKey =
	 *
	 * @param tranNo_0 tranNo_0
	 * @param mrKey_1  mrKey_1
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxAttachment TxAttachment of List
	 */
	public Slice<TxAttachment> findByTran(String tranNo_0, String mrKey_1, int index, int limit, TitaVo... titaVo);

	/**
	 * TranNo =
	 *
	 * @param tranNo_0 tranNo_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxAttachment TxAttachment of List
	 */
	public Slice<TxAttachment> findOnlyTran(String tranNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By TxAttachment
	 * 
	 * @param fileNo key
	 * @param titaVo Variable-Length Argument
	 * @return TxAttachment TxAttachment
	 */
	public TxAttachment holdById(Long fileNo, TitaVo... titaVo);

	/**
	 * hold By TxAttachment
	 * 
	 * @param txAttachment key
	 * @param titaVo       Variable-Length Argument
	 * @return TxAttachment TxAttachment
	 */
	public TxAttachment holdById(TxAttachment txAttachment, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param txAttachment Entity
	 * @param titaVo       Variable-Length Argument
	 * @return TxAttachment Entity
	 * @throws DBException exception
	 */
	public TxAttachment insert(TxAttachment txAttachment, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param txAttachment Entity
	 * @param titaVo       Variable-Length Argument
	 * @return TxAttachment Entity
	 * @throws DBException exception
	 */
	public TxAttachment update(TxAttachment txAttachment, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param txAttachment Entity
	 * @param titaVo       Variable-Length Argument
	 * @return TxAttachment Entity
	 * @throws DBException exception
	 */
	public TxAttachment update2(TxAttachment txAttachment, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param txAttachment Entity
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TxAttachment txAttachment, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param txAttachment Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TxAttachment> txAttachment, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param txAttachment Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TxAttachment> txAttachment, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param txAttachment Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TxAttachment> txAttachment, TitaVo... titaVo) throws DBException;

}
