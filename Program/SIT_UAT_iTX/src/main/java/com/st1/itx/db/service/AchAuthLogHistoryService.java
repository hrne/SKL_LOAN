package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AchAuthLogHistory;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.AchAuthLogHistoryId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AchAuthLogHistoryService {

	/**
	 * findByPrimaryKey
	 *
	 * @param achAuthLogHistoryId PK
	 * @param titaVo              Variable-Length Argument
	 * @return AchAuthLogHistory AchAuthLogHistory
	 */
	public AchAuthLogHistory findById(AchAuthLogHistoryId achAuthLogHistoryId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public Slice<AchAuthLogHistory> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo %
	 *
	 * @param custNo_0 custNo_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public Slice<AchAuthLogHistory> custNoLike(int custNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * RepayAcct %
	 *
	 * @param repayAcct_0 repayAcct_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public Slice<AchAuthLogHistory> repayAcctLike(String repayAcct_0, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public Slice<AchAuthLogHistory> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * RepayAcct =
	 *
	 * @param repayAcct_0 repayAcct_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public Slice<AchAuthLogHistory> repayAcctEq(String repayAcct_0, int index, int limit, TitaVo... titaVo);

	/**
	 * AuthCreateDate &gt;= ,AND AuthCreateDate &lt;=
	 *
	 * @param authCreateDate_0 authCreateDate_0
	 * @param authCreateDate_1 authCreateDate_1
	 * @param index            Page Index
	 * @param limit            Page Data Limit
	 * @param titaVo           Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public Slice<AchAuthLogHistory> authCreateDateEq(int authCreateDate_0, int authCreateDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * PropDate &gt;= ,AND PropDate &lt;=
	 *
	 * @param propDate_0 propDate_0
	 * @param propDate_1 propDate_1
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public Slice<AchAuthLogHistory> propDateEq(int propDate_0, int propDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * RetrDate &gt;= ,AND RetrDate &lt;=
	 *
	 * @param retrDate_0 retrDate_0
	 * @param retrDate_1 retrDate_1
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public Slice<AchAuthLogHistory> retrDateEq(int retrDate_0, int retrDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND RepayBank = ,AND RepayAcct = ,AND FacmNo =
	 *
	 * @param custNo_0    custNo_0
	 * @param repayBank_1 repayBank_1
	 * @param repayAcct_2 repayAcct_2
	 * @param facmNo_3    facmNo_3
	 * @param titaVo      Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public AchAuthLogHistory pkFacmNoFirst(int custNo_0, String repayBank_1, String repayAcct_2, int facmNo_3, TitaVo... titaVo);

	/**
	 * MediaCode ! ,AND AuthStatus ^i ,AND CustNo = ,AND PropDate =
	 *
	 * @param authStatus_1 authStatus_1
	 * @param custNo_2     custNo_2
	 * @param propDate_3   propDate_3
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public Slice<AchAuthLogHistory> l4040ARg(List<String> authStatus_1, int custNo_2, int propDate_3, int index, int limit, TitaVo... titaVo);

	/**
	 * MediaCode ! ,AND AuthStatus ^i ,AND PropDate =
	 *
	 * @param authStatus_1 authStatus_1
	 * @param propDate_2   propDate_2
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public Slice<AchAuthLogHistory> l4040BRg(List<String> authStatus_1, int propDate_2, int index, int limit, TitaVo... titaVo);

	/**
	 * MediaCode ! ,AND AuthStatus ^i ,AND CustNo =
	 *
	 * @param authStatus_1 authStatus_1
	 * @param custNo_2     custNo_2
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public Slice<AchAuthLogHistory> l4040CRg(List<String> authStatus_1, int custNo_2, int index, int limit, TitaVo... titaVo);

	/**
	 * MediaCode ! ,AND AuthStatus ^i
	 *
	 * @param authStatus_1 authStatus_1
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public Slice<AchAuthLogHistory> l4040DRg(List<String> authStatus_1, int index, int limit, TitaVo... titaVo);

	/**
	 * MediaCode ! ,AND PropDate &gt;= ,AND PropDate &lt;=
	 *
	 * @param propDate_1 propDate_1
	 * @param propDate_2 propDate_2
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public Slice<AchAuthLogHistory> mediaCodeIsnull(int propDate_1, int propDate_2, int index, int limit, TitaVo... titaVo);

	/**
	 * MediaCode = ,AND PropDate &gt;= ,AND PropDate &lt;=
	 *
	 * @param mediaCode_0 mediaCode_0
	 * @param propDate_1  propDate_1
	 * @param propDate_2  propDate_2
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public Slice<AchAuthLogHistory> mediaCodeEq(String mediaCode_0, int propDate_1, int propDate_2, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo = ,AND CreateFlag =
	 *
	 * @param custNo_0     custNo_0
	 * @param facmNo_1     facmNo_1
	 * @param createFlag_2 createFlag_2
	 * @param titaVo       Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public AchAuthLogHistory facmNoFirst(int custNo_0, int facmNo_1, String createFlag_2, TitaVo... titaVo);

	/**
	 * CustNo = ,AND RepayBank = ,AND RepayAcct =
	 *
	 * @param custNo_0    custNo_0
	 * @param repayBank_1 repayBank_1
	 * @param repayAcct_2 repayAcct_2
	 * @param titaVo      Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public AchAuthLogHistory repayAcctFirst(int custNo_0, String repayBank_1, String repayAcct_2, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param facmNo_1 facmNo_1
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public Slice<AchAuthLogHistory> facmNoEq(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param facmNo_1 facmNo_1
	 * @param titaVo   Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public AchAuthLogHistory facmNoBFirst(int custNo_0, int facmNo_1, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param facmNo_1 facmNo_1
	 * @param titaVo   Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public AchAuthLogHistory facmNoCFirst(int custNo_0, int facmNo_1, TitaVo... titaVo);

	/**
	 * AuthCreateDate = ,AND CustNo = ,AND RepayBank = ,AND RepayAcct = ,AND
	 * CreateFlag =
	 *
	 * @param authCreateDate_0 authCreateDate_0
	 * @param custNo_1         custNo_1
	 * @param repayBank_2      repayBank_2
	 * @param repayAcct_3      repayAcct_3
	 * @param createFlag_4     createFlag_4
	 * @param titaVo           Variable-Length Argument
	 * @return Slice AchAuthLogHistory AchAuthLogHistory of List
	 */
	public AchAuthLogHistory AuthLogKeyFirst(int authCreateDate_0, int custNo_1, String repayBank_2, String repayAcct_3, String createFlag_4, TitaVo... titaVo);

	/**
	 * hold By AchAuthLogHistory
	 * 
	 * @param achAuthLogHistoryId key
	 * @param titaVo              Variable-Length Argument
	 * @return AchAuthLogHistory AchAuthLogHistory
	 */
	public AchAuthLogHistory holdById(AchAuthLogHistoryId achAuthLogHistoryId, TitaVo... titaVo);

	/**
	 * hold By AchAuthLogHistory
	 * 
	 * @param achAuthLogHistory key
	 * @param titaVo            Variable-Length Argument
	 * @return AchAuthLogHistory AchAuthLogHistory
	 */
	public AchAuthLogHistory holdById(AchAuthLogHistory achAuthLogHistory, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param achAuthLogHistory Entity
	 * @param titaVo            Variable-Length Argument
	 * @return AchAuthLogHistory Entity
	 * @throws DBException exception
	 */
	public AchAuthLogHistory insert(AchAuthLogHistory achAuthLogHistory, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param achAuthLogHistory Entity
	 * @param titaVo            Variable-Length Argument
	 * @return AchAuthLogHistory Entity
	 * @throws DBException exception
	 */
	public AchAuthLogHistory update(AchAuthLogHistory achAuthLogHistory, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param achAuthLogHistory Entity
	 * @param titaVo            Variable-Length Argument
	 * @return AchAuthLogHistory Entity
	 * @throws DBException exception
	 */
	public AchAuthLogHistory update2(AchAuthLogHistory achAuthLogHistory, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param achAuthLogHistory Entity
	 * @param titaVo            Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(AchAuthLogHistory achAuthLogHistory, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param achAuthLogHistory Entity of List
	 * @param titaVo            Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<AchAuthLogHistory> achAuthLogHistory, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param achAuthLogHistory Entity of List
	 * @param titaVo            Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<AchAuthLogHistory> achAuthLogHistory, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param achAuthLogHistory Entity of List
	 * @param titaVo            Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<AchAuthLogHistory> achAuthLogHistory, TitaVo... titaVo) throws DBException;

}
