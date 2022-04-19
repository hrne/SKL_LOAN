package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxAttachType;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxAttachTypeService {

	/**
	 * findByPrimaryKey
	 *
	 * @param typeNo PK
	 * @param titaVo Variable-Length Argument
	 * @return TxAttachType TxAttachType
	 */
	public TxAttachType findById(Long typeNo, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TxAttachType TxAttachType of List
	 */
	public Slice<TxAttachType> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * TranNo =
	 *
	 * @param tranNo_0 tranNo_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxAttachType TxAttachType of List
	 */
	public Slice<TxAttachType> findByTranNo(String tranNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * TranNo = ,AND TypeItem =
	 *
	 * @param tranNo_0   tranNo_0
	 * @param typeItem_1 typeItem_1
	 * @param titaVo     Variable-Length Argument
	 * @return Slice TxAttachType TxAttachType of List
	 */
	public TxAttachType findByTypeItemFirst(String tranNo_0, String typeItem_1, TitaVo... titaVo);

	/**
	 * hold By TxAttachType
	 * 
	 * @param typeNo key
	 * @param titaVo Variable-Length Argument
	 * @return TxAttachType TxAttachType
	 */
	public TxAttachType holdById(Long typeNo, TitaVo... titaVo);

	/**
	 * hold By TxAttachType
	 * 
	 * @param txAttachType key
	 * @param titaVo       Variable-Length Argument
	 * @return TxAttachType TxAttachType
	 */
	public TxAttachType holdById(TxAttachType txAttachType, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param txAttachType Entity
	 * @param titaVo       Variable-Length Argument
	 * @return TxAttachType Entity
	 * @throws DBException exception
	 */
	public TxAttachType insert(TxAttachType txAttachType, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param txAttachType Entity
	 * @param titaVo       Variable-Length Argument
	 * @return TxAttachType Entity
	 * @throws DBException exception
	 */
	public TxAttachType update(TxAttachType txAttachType, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param txAttachType Entity
	 * @param titaVo       Variable-Length Argument
	 * @return TxAttachType Entity
	 * @throws DBException exception
	 */
	public TxAttachType update2(TxAttachType txAttachType, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param txAttachType Entity
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TxAttachType txAttachType, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param txAttachType Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TxAttachType> txAttachType, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param txAttachType Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TxAttachType> txAttachType, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param txAttachType Entity of List
	 * @param titaVo       Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TxAttachType> txAttachType, TitaVo... titaVo) throws DBException;

}
