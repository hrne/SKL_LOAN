package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AchAuthLog;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.AchAuthLogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AchAuthLogService {

	/**
	 * findByPrimaryKey
	 *
	 * @param achAuthLogId PK
	 * @param titaVo       Variable-Length Argument
	 * @return AchAuthLog AchAuthLog
	 */
	public AchAuthLog findById(AchAuthLogId achAuthLogId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice AchAuthLog AchAuthLog of List
	 */
	public Slice<AchAuthLog> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo %
	 *
	 * @param custNo_0 custNo_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice AchAuthLog AchAuthLog of List
	 */
	public Slice<AchAuthLog> custNoLike(int custNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * RepayAcct %
	 *
	 * @param repayAcct_0 repayAcct_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice AchAuthLog AchAuthLog of List
	 */
	public Slice<AchAuthLog> repayAcctLike(String repayAcct_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice AchAuthLog AchAuthLog of List
	 */
	public Slice<AchAuthLog> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * RepayAcct =
	 *
	 * @param repayAcct_0 repayAcct_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice AchAuthLog AchAuthLog of List
	 */
	public Slice<AchAuthLog> repayAcctEq(String repayAcct_0, int index, int limit, TitaVo... titaVo);

	/**
	 * AuthCreateDate &gt;= ,AND AuthCreateDate &lt;=
	 *
	 * @param authCreateDate_0 authCreateDate_0
	 * @param authCreateDate_1 authCreateDate_1
	 * @param index            Page Index
	 * @param limit            Page Data Limit
	 * @param titaVo           Variable-Length Argument
	 * @return Slice AchAuthLog AchAuthLog of List
	 */
	public Slice<AchAuthLog> authCreateDateEq(int authCreateDate_0, int authCreateDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * PropDate &gt;= ,AND PropDate &lt;=
	 *
	 * @param propDate_0 propDate_0
	 * @param propDate_1 propDate_1
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice AchAuthLog AchAuthLog of List
	 */
	public Slice<AchAuthLog> propDateEq(int propDate_0, int propDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * RetrDate &gt;= ,AND RetrDate &lt;=
	 *
	 * @param retrDate_0 retrDate_0
	 * @param retrDate_1 retrDate_1
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice AchAuthLog AchAuthLog of List
	 */
	public Slice<AchAuthLog> retrDateEq(int retrDate_0, int retrDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND RepayBank = ,AND RepayAcct = ,AND FacmNo =
	 *
	 * @param custNo_0    custNo_0
	 * @param repayBank_1 repayBank_1
	 * @param repayAcct_2 repayAcct_2
	 * @param facmNo_3    facmNo_3
	 * @param titaVo      Variable-Length Argument
	 * @return Slice AchAuthLog AchAuthLog of List
	 */
	public AchAuthLog pkFacmNoFirst(int custNo_0, String repayBank_1, String repayAcct_2, int facmNo_3, TitaVo... titaVo);

	/**
	 * MediaCode ! ,AND AuthStatus ^i ,AND CustNo = ,AND PropDate =
	 *
	 * @param authStatus_1 authStatus_1
	 * @param custNo_2     custNo_2
	 * @param propDate_3   propDate_3
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice AchAuthLog AchAuthLog of List
	 */
	public Slice<AchAuthLog> l4040ARg(List<String> authStatus_1, int custNo_2, int propDate_3, int index, int limit, TitaVo... titaVo);

	/**
	 * MediaCode ! ,AND AuthStatus ^i ,AND PropDate =
	 *
	 * @param authStatus_1 authStatus_1
	 * @param propDate_2   propDate_2
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice AchAuthLog AchAuthLog of List
	 */
	public Slice<AchAuthLog> l4040BRg(List<String> authStatus_1, int propDate_2, int index, int limit, TitaVo... titaVo);

	/**
	 * MediaCode ! ,AND AuthStatus ^i ,AND CustNo =
	 *
	 * @param authStatus_1 authStatus_1
	 * @param custNo_2     custNo_2
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice AchAuthLog AchAuthLog of List
	 */
	public Slice<AchAuthLog> l4040CRg(List<String> authStatus_1, int custNo_2, int index, int limit, TitaVo... titaVo);

	/**
	 * MediaCode ! ,AND AuthStatus ^i
	 *
	 * @param authStatus_1 authStatus_1
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice AchAuthLog AchAuthLog of List
	 */
	public Slice<AchAuthLog> l4040DRg(List<String> authStatus_1, int index, int limit, TitaVo... titaVo);

	/**
	 * MediaCode ! ,AND PropDate &gt;= ,AND PropDate &lt;=
	 *
	 * @param propDate_1 propDate_1
	 * @param propDate_2 propDate_2
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice AchAuthLog AchAuthLog of List
	 */
	public Slice<AchAuthLog> mediaCodeIsnull(int propDate_1, int propDate_2, int index, int limit, TitaVo... titaVo);

	/**
	 * MediaCode = ,AND PropDate &gt;= ,AND PropDate &lt;=
	 *
	 * @param mediaCode_0 mediaCode_0
	 * @param propDate_1  propDate_1
	 * @param propDate_2  propDate_2
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice AchAuthLog AchAuthLog of List
	 */
	public Slice<AchAuthLog> mediaCodeEq(String mediaCode_0, int propDate_1, int propDate_2, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo = ,AND CreateFlag =
	 *
	 * @param custNo_0     custNo_0
	 * @param facmNo_1     facmNo_1
	 * @param createFlag_2 createFlag_2
	 * @param titaVo       Variable-Length Argument
	 * @return Slice AchAuthLog AchAuthLog of List
	 */
	public AchAuthLog facmNoFirst(int custNo_0, int facmNo_1, String createFlag_2, TitaVo... titaVo);

	/**
	 * CustNo = ,AND RepayBank = ,AND RepayAcct =
	 *
	 * @param custNo_0    custNo_0
	 * @param repayBank_1 repayBank_1
	 * @param repayAcct_2 repayAcct_2
	 * @param titaVo      Variable-Length Argument
	 * @return Slice AchAuthLog AchAuthLog of List
	 */
	public AchAuthLog repayAcctFirst(int custNo_0, String repayBank_1, String repayAcct_2, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param facmNo_1 facmNo_1
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice AchAuthLog AchAuthLog of List
	 */
	public Slice<AchAuthLog> facmNoEq(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param facmNo_1 facmNo_1
	 * @param titaVo   Variable-Length Argument
	 * @return Slice AchAuthLog AchAuthLog of List
	 */
	public AchAuthLog facmNoBFirst(int custNo_0, int facmNo_1, TitaVo... titaVo);

	/**
	 * hold By AchAuthLog
	 * 
	 * @param achAuthLogId key
	 * @param titaVo       Variable-Length Argument
	 * @return AchAuthLog AchAuthLog
	 */
	public AchAuthLog holdById(AchAuthLogId achAuthLogId, TitaVo... titaVo);

	/**
	 * hold By AchAuthLog
	 * 
	 * @param achAuthLog key
	 * @param titaVo     Variable-Length Argument
	 * @return AchAuthLog AchAuthLog
	 */
	public AchAuthLog holdById(AchAuthLog achAuthLog, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param achAuthLog Entity
	 * @param titaVo     Variable-Length Argument
	 * @return AchAuthLog Entity
	 * @throws DBException exception
	 */
	public AchAuthLog insert(AchAuthLog achAuthLog, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param achAuthLog Entity
	 * @param titaVo     Variable-Length Argument
	 * @return AchAuthLog Entity
	 * @throws DBException exception
	 */
	public AchAuthLog update(AchAuthLog achAuthLog, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param achAuthLog Entity
	 * @param titaVo     Variable-Length Argument
	 * @return AchAuthLog Entity
	 * @throws DBException exception
	 */
	public AchAuthLog update2(AchAuthLog achAuthLog, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param achAuthLog Entity
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(AchAuthLog achAuthLog, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param achAuthLog Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<AchAuthLog> achAuthLog, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param achAuthLog Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<AchAuthLog> achAuthLog, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param achAuthLog Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<AchAuthLog> achAuthLog, TitaVo... titaVo) throws DBException;

}
