package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.Ias39LoanCommit;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.Ias39LoanCommitId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias39LoanCommitService {

	/**
	 * findByPrimaryKey
	 *
	 * @param ias39LoanCommitId PK
	 * @param titaVo            Variable-Length Argument
	 * @return Ias39LoanCommit Ias39LoanCommit
	 */
	public Ias39LoanCommit findById(Ias39LoanCommitId ias39LoanCommitId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice Ias39LoanCommit Ias39LoanCommit of List
	 */
	public Slice<Ias39LoanCommit> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * DataYm = ,AND CustNo = ,AND FacmNo = ,AND ApplNo =
	 *
	 * @param dataYm_0 dataYm_0
	 * @param custNo_1 custNo_1
	 * @param facmNo_2 facmNo_2
	 * @param applNo_3 applNo_3
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice Ias39LoanCommit Ias39LoanCommit of List
	 */
	public Slice<Ias39LoanCommit> ApplNoEq(int dataYm_0, int custNo_1, int facmNo_2, int applNo_3, int index, int limit, TitaVo... titaVo);

	/**
	 * DataYm =
	 *
	 * @param dataYm_0 dataYm_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice Ias39LoanCommit Ias39LoanCommit of List
	 */
	public Slice<Ias39LoanCommit> findDataYmEq(int dataYm_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By Ias39LoanCommit
	 * 
	 * @param ias39LoanCommitId key
	 * @param titaVo            Variable-Length Argument
	 * @return Ias39LoanCommit Ias39LoanCommit
	 */
	public Ias39LoanCommit holdById(Ias39LoanCommitId ias39LoanCommitId, TitaVo... titaVo);

	/**
	 * hold By Ias39LoanCommit
	 * 
	 * @param ias39LoanCommit key
	 * @param titaVo          Variable-Length Argument
	 * @return Ias39LoanCommit Ias39LoanCommit
	 */
	public Ias39LoanCommit holdById(Ias39LoanCommit ias39LoanCommit, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param ias39LoanCommit Entity
	 * @param titaVo          Variable-Length Argument
	 * @return Ias39LoanCommit Entity
	 * @throws DBException exception
	 */
	public Ias39LoanCommit insert(Ias39LoanCommit ias39LoanCommit, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param ias39LoanCommit Entity
	 * @param titaVo          Variable-Length Argument
	 * @return Ias39LoanCommit Entity
	 * @throws DBException exception
	 */
	public Ias39LoanCommit update(Ias39LoanCommit ias39LoanCommit, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param ias39LoanCommit Entity
	 * @param titaVo          Variable-Length Argument
	 * @return Ias39LoanCommit Entity
	 * @throws DBException exception
	 */
	public Ias39LoanCommit update2(Ias39LoanCommit ias39LoanCommit, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param ias39LoanCommit Entity
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(Ias39LoanCommit ias39LoanCommit, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param ias39LoanCommit Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<Ias39LoanCommit> ias39LoanCommit, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param ias39LoanCommit Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<Ias39LoanCommit> ias39LoanCommit, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param ias39LoanCommit Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<Ias39LoanCommit> ias39LoanCommit, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * LM011 更新IAS39放款承諾明細檔
	 * 
	 * @param tbsdyf int
	 * @param empNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L7_Ias39LoanCommit_Upd(int tbsdyf, String empNo, TitaVo... titaVo);

}
