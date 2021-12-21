package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxAuthority;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.TxAuthorityId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxAuthorityService {

	/**
	 * findByPrimaryKey
	 *
	 * @param txAuthorityId PK
	 * @param titaVo        Variable-Length Argument
	 * @return TxAuthority TxAuthority
	 */
	public TxAuthority findById(TxAuthorityId txAuthorityId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TxAuthority TxAuthority of List
	 */
	public Slice<TxAuthority> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * AuthNo =
	 *
	 * @param authNo_0 authNo_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxAuthority TxAuthority of List
	 */
	public Slice<TxAuthority> findByAuthNo(String authNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * AuthNo = ,AND TranNo &lt;&gt;%
	 *
	 * @param authNo_0 authNo_0
	 * @param tranNo_1 tranNo_1
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice TxAuthority TxAuthority of List
	 */
	public Slice<TxAuthority> findByAuthNo2(String authNo_0, String tranNo_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By TxAuthority
	 * 
	 * @param txAuthorityId key
	 * @param titaVo        Variable-Length Argument
	 * @return TxAuthority TxAuthority
	 */
	public TxAuthority holdById(TxAuthorityId txAuthorityId, TitaVo... titaVo);

	/**
	 * hold By TxAuthority
	 * 
	 * @param txAuthority key
	 * @param titaVo      Variable-Length Argument
	 * @return TxAuthority TxAuthority
	 */
	public TxAuthority holdById(TxAuthority txAuthority, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param txAuthority Entity
	 * @param titaVo      Variable-Length Argument
	 * @return TxAuthority Entity
	 * @throws DBException exception
	 */
	public TxAuthority insert(TxAuthority txAuthority, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param txAuthority Entity
	 * @param titaVo      Variable-Length Argument
	 * @return TxAuthority Entity
	 * @throws DBException exception
	 */
	public TxAuthority update(TxAuthority txAuthority, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param txAuthority Entity
	 * @param titaVo      Variable-Length Argument
	 * @return TxAuthority Entity
	 * @throws DBException exception
	 */
	public TxAuthority update2(TxAuthority txAuthority, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param txAuthority Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TxAuthority txAuthority, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param txAuthority Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TxAuthority> txAuthority, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param txAuthority Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TxAuthority> txAuthority, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param txAuthority Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TxAuthority> txAuthority, TitaVo... titaVo) throws DBException;

}
