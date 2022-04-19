package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CollTel;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CollTelId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CollTelService {

	/**
	 * findByPrimaryKey
	 *
	 * @param collTelId PK
	 * @param titaVo    Variable-Length Argument
	 * @return CollTel CollTel
	 */
	public CollTel findById(CollTelId collTelId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CollTel CollTel of List
	 */
	public Slice<CollTel> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * TelDate&gt;= , AND TelDate&lt;= ,AND CaseCode= ,AND CustNo= ,AND FacmNo= ,
	 *
	 * @param telDate_0  telDate_0
	 * @param telDate_1  telDate_1
	 * @param caseCode_2 caseCode_2
	 * @param custNo_3   custNo_3
	 * @param facmNo_4   facmNo_4
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CollTel CollTel of List
	 */
	public Slice<CollTel> telTimeBetween(int telDate_0, int telDate_1, String caseCode_2, int custNo_3, int facmNo_4, int index, int limit, TitaVo... titaVo);

	/**
	 * CaseCode= ,AND CustNo= ,AND FacmNo= ,
	 *
	 * @param caseCode_0 caseCode_0
	 * @param custNo_1   custNo_1
	 * @param facmNo_2   facmNo_2
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CollTel CollTel of List
	 */
	public Slice<CollTel> findSameCust(String caseCode_0, int custNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo);

	/**
	 * TelDate&gt;= , AND TelDate&lt;= ,AND CaseCode= ,AND CustNo=
	 *
	 * @param telDate_0  telDate_0
	 * @param telDate_1  telDate_1
	 * @param caseCode_2 caseCode_2
	 * @param custNo_3   custNo_3
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CollTel CollTel of List
	 */
	public Slice<CollTel> withoutFacmNo(int telDate_0, int telDate_1, String caseCode_2, int custNo_3, int index, int limit, TitaVo... titaVo);

	/**
	 * CaseCode= ,AND CustNo=
	 *
	 * @param caseCode_0 caseCode_0
	 * @param custNo_1   custNo_1
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CollTel CollTel of List
	 */
	public Slice<CollTel> withoutFacmNoAll(String caseCode_0, int custNo_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By CollTel
	 * 
	 * @param collTelId key
	 * @param titaVo    Variable-Length Argument
	 * @return CollTel CollTel
	 */
	public CollTel holdById(CollTelId collTelId, TitaVo... titaVo);

	/**
	 * hold By CollTel
	 * 
	 * @param collTel key
	 * @param titaVo  Variable-Length Argument
	 * @return CollTel CollTel
	 */
	public CollTel holdById(CollTel collTel, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param collTel Entity
	 * @param titaVo  Variable-Length Argument
	 * @return CollTel Entity
	 * @throws DBException exception
	 */
	public CollTel insert(CollTel collTel, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param collTel Entity
	 * @param titaVo  Variable-Length Argument
	 * @return CollTel Entity
	 * @throws DBException exception
	 */
	public CollTel update(CollTel collTel, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param collTel Entity
	 * @param titaVo  Variable-Length Argument
	 * @return CollTel Entity
	 * @throws DBException exception
	 */
	public CollTel update2(CollTel collTel, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param collTel Entity
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(CollTel collTel, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param collTel Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<CollTel> collTel, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param collTel Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<CollTel> collTel, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param collTel Entity of List
	 * @param titaVo  Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<CollTel> collTel, TitaVo... titaVo) throws DBException;

}
