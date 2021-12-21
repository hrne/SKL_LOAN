package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.BankDeductDtl;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.BankDeductDtlId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BankDeductDtlService {

	/**
	 * findByPrimaryKey
	 *
	 * @param bankDeductDtlId PK
	 * @param titaVo          Variable-Length Argument
	 * @return BankDeductDtl BankDeductDtl
	 */
	public BankDeductDtl findById(BankDeductDtlId bankDeductDtlId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice BankDeductDtl BankDeductDtl of List
	 */
	public Slice<BankDeductDtl> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND EntryDate &gt;= ,AND EntryDate &lt;=
	 *
	 * @param custNo_0    custNo_0
	 * @param entryDate_1 entryDate_1
	 * @param entryDate_2 entryDate_2
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice BankDeductDtl BankDeductDtl of List
	 */
	public Slice<BankDeductDtl> sortByAll(int custNo_0, int entryDate_1, int entryDate_2, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo =
	 *
	 * @param custNo_0 custNo_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice BankDeductDtl BankDeductDtl of List
	 */
	public Slice<BankDeductDtl> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo);

	/**
	 * EntryDate &gt;= ,AND EntryDate &lt;=
	 *
	 * @param entryDate_0 entryDate_0
	 * @param entryDate_1 entryDate_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice BankDeductDtl BankDeductDtl of List
	 */
	public Slice<BankDeductDtl> entryDateRng(int entryDate_0, int entryDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * MediaDate = , AND MediaKind = , AND MediaSeq =
	 *
	 * @param mediaDate_0 mediaDate_0
	 * @param mediaKind_1 mediaKind_1
	 * @param mediaSeq_2  mediaSeq_2
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice BankDeductDtl BankDeductDtl of List
	 */
	public Slice<BankDeductDtl> mediaSeqRng(int mediaDate_0, String mediaKind_1, int mediaSeq_2, int index, int limit, TitaVo... titaVo);

	/**
	 * RepayBank = , AND EntryDate &gt;= ,AND EntryDate &lt;=
	 *
	 * @param repayBank_0 repayBank_0
	 * @param entryDate_1 entryDate_1
	 * @param entryDate_2 entryDate_2
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice BankDeductDtl BankDeductDtl of List
	 */
	public Slice<BankDeductDtl> repayBankEq(String repayBank_0, int entryDate_1, int entryDate_2, int index, int limit, TitaVo... titaVo);

	/**
	 * EntryDate &gt;= ,AND EntryDate &lt;= ,AND RepayType =
	 *
	 * @param entryDate_0 entryDate_0
	 * @param entryDate_1 entryDate_1
	 * @param repayType_2 repayType_2
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice BankDeductDtl BankDeductDtl of List
	 */
	public Slice<BankDeductDtl> deductNoticeA(int entryDate_0, int entryDate_1, int repayType_2, int index, int limit, TitaVo... titaVo);

	/**
	 * EntryDate = ,AND CustNo = ,AND FacmNo = ,AND RepayType = ,AND PayIntDate =
	 *
	 * @param entryDate_0  entryDate_0
	 * @param custNo_1     custNo_1
	 * @param facmNo_2     facmNo_2
	 * @param repayType_3  repayType_3
	 * @param payIntDate_4 payIntDate_4
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice BankDeductDtl BankDeductDtl of List
	 */
	public Slice<BankDeductDtl> facmNoRange(int entryDate_0, int custNo_1, int facmNo_2, int repayType_3, int payIntDate_4, int index, int limit, TitaVo... titaVo);

	/**
	 * EntryDate &gt;= ,AND EntryDate &lt;= ,AND RepayBank = ,AND RepayType =
	 *
	 * @param entryDate_0 entryDate_0
	 * @param entryDate_1 entryDate_1
	 * @param repayBank_2 repayBank_2
	 * @param repayType_3 repayType_3
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice BankDeductDtl BankDeductDtl of List
	 */
	public Slice<BankDeductDtl> findL4943Eq(int entryDate_0, int entryDate_1, String repayBank_2, int repayType_3, int index, int limit, TitaVo... titaVo);

	/**
	 * MediaDate &gt;= , AND MediaDate &lt;=
	 *
	 * @param mediaDate_0 mediaDate_0
	 * @param mediaDate_1 mediaDate_1
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice BankDeductDtl BankDeductDtl of List
	 */
	public Slice<BankDeductDtl> findL4452Rng(int mediaDate_0, int mediaDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo = ,AND RepayType =
	 *
	 * @param custNo_0    custNo_0
	 * @param facmNo_1    facmNo_1
	 * @param repayType_2 repayType_2
	 * @param titaVo      Variable-Length Argument
	 * @return Slice BankDeductDtl BankDeductDtl of List
	 */
	public BankDeductDtl findL4450EntryDateFirst(int custNo_0, int facmNo_1, int repayType_2, TitaVo... titaVo);

	/**
	 * CustNo = ,AND FacmNo = ,AND PrevIntDate =
	 *
	 * @param custNo_0      custNo_0
	 * @param facmNo_1      facmNo_1
	 * @param prevIntDate_2 prevIntDate_2
	 * @param titaVo        Variable-Length Argument
	 * @return Slice BankDeductDtl BankDeductDtl of List
	 */
	public BankDeductDtl findL4450PrevIntDateFirst(int custNo_0, int facmNo_1, int prevIntDate_2, TitaVo... titaVo);

	/**
	 * EntryDate &gt;= ,AND EntryDate &lt;= , AND MediaKind =
	 *
	 * @param entryDate_0 entryDate_0
	 * @param entryDate_1 entryDate_1
	 * @param mediaKind_2 mediaKind_2
	 * @param titaVo      Variable-Length Argument
	 * @return Slice BankDeductDtl BankDeductDtl of List
	 */
	public BankDeductDtl findL4451First(int entryDate_0, int entryDate_1, String mediaKind_2, TitaVo... titaVo);

	/**
	 * RepayBank &lt;&gt; , AND EntryDate &gt;= ,AND EntryDate &lt;=
	 *
	 * @param repayBank_0 repayBank_0
	 * @param entryDate_1 entryDate_1
	 * @param entryDate_2 entryDate_2
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice BankDeductDtl BankDeductDtl of List
	 */
	public Slice<BankDeductDtl> repayBankNotEq(String repayBank_0, int entryDate_1, int entryDate_2, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By BankDeductDtl
	 * 
	 * @param bankDeductDtlId key
	 * @param titaVo          Variable-Length Argument
	 * @return BankDeductDtl BankDeductDtl
	 */
	public BankDeductDtl holdById(BankDeductDtlId bankDeductDtlId, TitaVo... titaVo);

	/**
	 * hold By BankDeductDtl
	 * 
	 * @param bankDeductDtl key
	 * @param titaVo        Variable-Length Argument
	 * @return BankDeductDtl BankDeductDtl
	 */
	public BankDeductDtl holdById(BankDeductDtl bankDeductDtl, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param bankDeductDtl Entity
	 * @param titaVo        Variable-Length Argument
	 * @return BankDeductDtl Entity
	 * @throws DBException exception
	 */
	public BankDeductDtl insert(BankDeductDtl bankDeductDtl, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param bankDeductDtl Entity
	 * @param titaVo        Variable-Length Argument
	 * @return BankDeductDtl Entity
	 * @throws DBException exception
	 */
	public BankDeductDtl update(BankDeductDtl bankDeductDtl, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param bankDeductDtl Entity
	 * @param titaVo        Variable-Length Argument
	 * @return BankDeductDtl Entity
	 * @throws DBException exception
	 */
	public BankDeductDtl update2(BankDeductDtl bankDeductDtl, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param bankDeductDtl Entity
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(BankDeductDtl bankDeductDtl, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param bankDeductDtl Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<BankDeductDtl> bankDeductDtl, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param bankDeductDtl Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<BankDeductDtl> bankDeductDtl, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param bankDeductDtl Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<BankDeductDtl> bankDeductDtl, TitaVo... titaVo) throws DBException;

}
