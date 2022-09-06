package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustNotice;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CustNoticeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CustNoticeService {

	/**
	 * findByPrimaryKey
	 *
	 * @param custNoticeId PK
	 * @param titaVo       Variable-Length Argument
	 * @return CustNotice CustNotice
	 */
	public CustNotice findById(CustNoticeId custNoticeId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CustNotice CustNotice of List
	 */
	public Slice<CustNotice> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice CustNotice CustNotice of List
	 */
	public Slice<CustNotice> findCustNo(int custNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * FormNo =
	 *
	 * @param formNo_0 formNo_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice CustNotice CustNotice of List
	 */
	public Slice<CustNotice> findFormNo(String formNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;=
	 *
	 * @param custNo_0 custNo_0
	 * @param facmNo_1 facmNo_1
	 * @param facmNo_2 facmNo_2
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice CustNotice CustNotice of List
	 */
	public Slice<CustNotice> facmNoEq(int custNo_0, int facmNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FormNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param formNo_1 formNo_1
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice CustNotice CustNotice of List
	 */
	public Slice<CustNotice> findCustNoFormNo(int custNo_0, String formNo_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By CustNotice
	 * 
	 * @param custNoticeId key
	 * @param titaVo       Variable-Length Argument
	 * @return CustNotice CustNotice
	 */
	public CustNotice holdById(CustNoticeId custNoticeId, TitaVo... titaVo);

	/**
	 * hold By CustNotice
	 * 
	 * @param custNotice key
	 * @param titaVo     Variable-Length Argument
	 * @return CustNotice CustNotice
	 */
	public CustNotice holdById(CustNotice custNotice, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param custNotice Entity
	 * @param titaVo     Variable-Length Argument
	 * @return CustNotice Entity
	 * @throws DBException exception
	 */
	public CustNotice insert(CustNotice custNotice, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param custNotice Entity
	 * @param titaVo     Variable-Length Argument
	 * @return CustNotice Entity
	 * @throws DBException exception
	 */
	public CustNotice update(CustNotice custNotice, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param custNotice Entity
	 * @param titaVo     Variable-Length Argument
	 * @return CustNotice Entity
	 * @throws DBException exception
	 */
	public CustNotice update2(CustNotice custNotice, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param custNotice Entity
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(CustNotice custNotice, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param custNotice Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<CustNotice> custNotice, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param custNotice Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<CustNotice> custNotice, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param custNotice Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<CustNotice> custNotice, TitaVo... titaVo) throws DBException;

}
