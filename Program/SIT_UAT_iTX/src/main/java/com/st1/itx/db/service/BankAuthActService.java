package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.BankAuthAct;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.BankAuthActId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BankAuthActService {

	/**
	 * findByPrimaryKey
	 *
	 * @param bankAuthActId PK
	 * @param titaVo        Variable-Length Argument
	 * @return BankAuthAct BankAuthAct
	 */
	public BankAuthAct findById(BankAuthActId bankAuthActId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice BankAuthAct BankAuthAct of List
	 */
	public Slice<BankAuthAct> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = , AND RepayAcct = , AND FacmNo &gt;=, AND FacmNo &lt;=
	 *
	 * @param custNo_0    custNo_0
	 * @param repayAcct_1 repayAcct_1
	 * @param facmNo_2    facmNo_2
	 * @param facmNo_3    facmNo_3
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice BankAuthAct BankAuthAct of List
	 */
	public Slice<BankAuthAct> authCheck(int custNo_0, String repayAcct_1, int facmNo_2, int facmNo_3, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = , AND FacmNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param facmNo_1 facmNo_1
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice BankAuthAct BankAuthAct of List
	 */
	public Slice<BankAuthAct> facmNoEq(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = , AND RepayAcct = , AND RepayBank = , AND FacmNo &gt;=, AND FacmNo
	 * &lt;=
	 *
	 * @param custNo_0    custNo_0
	 * @param repayAcct_1 repayAcct_1
	 * @param repayBank_2 repayBank_2
	 * @param facmNo_3    facmNo_3
	 * @param facmNo_4    facmNo_4
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice BankAuthAct BankAuthAct of List
	 */
	public Slice<BankAuthAct> findAcctNo(int custNo_0, String repayAcct_1, String repayBank_2, int facmNo_3, int facmNo_4, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By BankAuthAct
	 * 
	 * @param bankAuthActId key
	 * @param titaVo        Variable-Length Argument
	 * @return BankAuthAct BankAuthAct
	 */
	public BankAuthAct holdById(BankAuthActId bankAuthActId, TitaVo... titaVo);

	/**
	 * hold By BankAuthAct
	 * 
	 * @param bankAuthAct key
	 * @param titaVo      Variable-Length Argument
	 * @return BankAuthAct BankAuthAct
	 */
	public BankAuthAct holdById(BankAuthAct bankAuthAct, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param bankAuthAct Entity
	 * @param titaVo      Variable-Length Argument
	 * @return BankAuthAct Entity
	 * @throws DBException exception
	 */
	public BankAuthAct insert(BankAuthAct bankAuthAct, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param bankAuthAct Entity
	 * @param titaVo      Variable-Length Argument
	 * @return BankAuthAct Entity
	 * @throws DBException exception
	 */
	public BankAuthAct update(BankAuthAct bankAuthAct, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param bankAuthAct Entity
	 * @param titaVo      Variable-Length Argument
	 * @return BankAuthAct Entity
	 * @throws DBException exception
	 */
	public BankAuthAct update2(BankAuthAct bankAuthAct, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param bankAuthAct Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(BankAuthAct bankAuthAct, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param bankAuthAct Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<BankAuthAct> bankAuthAct, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param bankAuthAct Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<BankAuthAct> bankAuthAct, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param bankAuthAct Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<BankAuthAct> bankAuthAct, TitaVo... titaVo) throws DBException;

}
