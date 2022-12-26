package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.EmpDeductDtl;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.EmpDeductDtlId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface EmpDeductDtlService {

  /**
   * findByPrimaryKey
   *
   * @param empDeductDtlId PK
   * @param titaVo Variable-Length Argument
   * @return EmpDeductDtl EmpDeductDtl
   */
  public EmpDeductDtl findById(EmpDeductDtlId empDeductDtlId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice EmpDeductDtl EmpDeductDtl of List
   */
  public Slice<EmpDeductDtl> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * MediaDate = , AND MediaKind = , AND MediaSeq = 
   *
   * @param mediaDate_0 mediaDate_0
   * @param mediaKind_1 mediaKind_1
   * @param mediaSeq_2 mediaSeq_2
   * @param titaVo Variable-Length Argument
   * @return Slice EmpDeductDtl EmpDeductDtl of List
   */
  public EmpDeductDtl mediaSeqFirst(int mediaDate_0, String mediaKind_1, int mediaSeq_2, TitaVo... titaVo);

  /**
   * ErrMsg !, AND EntryDate &gt;= , AND EntryDate &lt;= , AND ProcCode ^i
   *
   * @param entryDate_1 entryDate_1
   * @param entryDate_2 entryDate_2
   * @param procCode_3 procCode_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice EmpDeductDtl EmpDeductDtl of List
   */
  public Slice<EmpDeductDtl> entryDateRng(int entryDate_1, int entryDate_2, List<String> procCode_3, int index, int limit, TitaVo... titaVo);

  /**
   * MediaDate = , AND MediaKind = , AND MediaSeq = 
   *
   * @param mediaDate_0 mediaDate_0
   * @param mediaKind_1 mediaKind_1
   * @param mediaSeq_2 mediaSeq_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice EmpDeductDtl EmpDeductDtl of List
   */
  public Slice<EmpDeductDtl> mediaSeqEq(int mediaDate_0, String mediaKind_1, int mediaSeq_2, int index, int limit, TitaVo... titaVo);

  /**
   * MediaDate = , AND MediaKind = 
   *
   * @param mediaDate_0 mediaDate_0
   * @param mediaKind_1 mediaKind_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice EmpDeductDtl EmpDeductDtl of List
   */
  public Slice<EmpDeductDtl> mediaDateRng(int mediaDate_0, String mediaKind_1, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = ,AND TitaTlrNo = ,AND TitaTxtNo =
   *
   * @param acDate_0 acDate_0
   * @param titaTlrNo_1 titaTlrNo_1
   * @param titaTxtNo_2 titaTxtNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice EmpDeductDtl EmpDeductDtl of List
   */
  public Slice<EmpDeductDtl> findTxSeq(EmpDeductDtlId acDate_0, String titaTlrNo_1, String titaTxtNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By EmpDeductDtl
   * 
   * @param empDeductDtlId key
   * @param titaVo Variable-Length Argument
   * @return EmpDeductDtl EmpDeductDtl
   */
  public EmpDeductDtl holdById(EmpDeductDtlId empDeductDtlId, TitaVo... titaVo);

  /**
   * hold By EmpDeductDtl
   * 
   * @param empDeductDtl key
   * @param titaVo Variable-Length Argument
   * @return EmpDeductDtl EmpDeductDtl
   */
  public EmpDeductDtl holdById(EmpDeductDtl empDeductDtl, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param empDeductDtl Entity
   * @param titaVo Variable-Length Argument
   * @return EmpDeductDtl Entity
   * @throws DBException exception
   */
  public EmpDeductDtl insert(EmpDeductDtl empDeductDtl, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param empDeductDtl Entity
   * @param titaVo Variable-Length Argument
   * @return EmpDeductDtl Entity
   * @throws DBException exception
   */
  public EmpDeductDtl update(EmpDeductDtl empDeductDtl, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param empDeductDtl Entity
   * @param titaVo Variable-Length Argument
   * @return EmpDeductDtl Entity
   * @throws DBException exception
   */
  public EmpDeductDtl update2(EmpDeductDtl empDeductDtl, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param empDeductDtl Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(EmpDeductDtl empDeductDtl, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param empDeductDtl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<EmpDeductDtl> empDeductDtl, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param empDeductDtl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<EmpDeductDtl> empDeductDtl, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param empDeductDtl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<EmpDeductDtl> empDeductDtl, TitaVo... titaVo) throws DBException;

}
