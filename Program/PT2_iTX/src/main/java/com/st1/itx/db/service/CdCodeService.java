package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdCode;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdCodeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdCodeService {

  /**
   * findByPrimaryKey
   *
   * @param cdCodeId PK
   * @param titaVo Variable-Length Argument
   * @return CdCode CdCode
   */
  public CdCode findById(CdCodeId cdCodeId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCode CdCode of List
   */
  public Slice<CdCode> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * DefCode = ,AND Code % 
   *
   * @param defCode_0 defCode_0
   * @param code_1 code_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCode CdCode of List
   */
  public Slice<CdCode> defCodeEq(String defCode_0, String code_1, int index, int limit, TitaVo... titaVo);

  /**
   * DefCode = ,AND DefType = ,AND Code %
   *
   * @param defCode_0 defCode_0
   * @param defType_1 defType_1
   * @param code_2 code_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCode CdCode of List
   */
  public Slice<CdCode> defCodeEq2(String defCode_0, int defType_1, String code_2, int index, int limit, TitaVo... titaVo);

  /**
   * DefCode &lt;&gt; ,AND DefType = ,AND Code %, AND Item %
   *
   * @param defCode_0 defCode_0
   * @param defType_1 defType_1
   * @param code_2 code_2
   * @param item_3 item_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCode CdCode of List
   */
  public Slice<CdCode> DefTypeEq(String defCode_0, int defType_1, String code_2, String item_3, int index, int limit, TitaVo... titaVo);

  /**
   * DefType = 
   *
   * @param defType_0 defType_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCode CdCode of List
   */
  public Slice<CdCode> getDefList(int defType_0, int index, int limit, TitaVo... titaVo);

  /**
   * DefType = ,AND DefCode = 
   *
   * @param defType_0 defType_0
   * @param defCode_1 defCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCode CdCode of List
   */
  public Slice<CdCode> getCodeList(int defType_0, String defCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * DefType = ,AND DefCode = ,AND Code = 
   *
   * @param defType_0 defType_0
   * @param defCode_1 defCode_1
   * @param code_2 code_2
   * @param titaVo Variable-Length Argument
   * @return Slice CdCode CdCode of List
   */
  public CdCode getItemFirst(int defType_0, String defCode_1, String code_2, TitaVo... titaVo);

  /**
   * DefType = ,AND DefCode = ,AND Code ^i
   *
   * @param defType_0 defType_0
   * @param defCode_1 defCode_1
   * @param code_2 code_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCode CdCode of List
   */
  public Slice<CdCode> getCodeList2(int defType_0, String defCode_1, List<String> code_2, int index, int limit, TitaVo... titaVo);

  /**
   * DefCode = ,AND Item %
   *
   * @param defCode_0 defCode_0
   * @param item_1 item_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCode CdCode of List
   */
  public Slice<CdCode> defItemEq(String defCode_0, String item_1, int index, int limit, TitaVo... titaVo);

  /**
   * AND Item %
   *
   * @param item_0 item_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCode CdCode of List
   */
  public Slice<CdCode> defItemEq3(String item_0, int index, int limit, TitaVo... titaVo);

  /**
   * DefCode = ,AND Enable = ,AND EffectFlag = 
   *
   * @param defCode_0 defCode_0
   * @param enable_1 enable_1
   * @param effectFlag_2 effectFlag_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCode CdCode of List
   */
  public Slice<CdCode> getCodeListWithFlag(String defCode_0, String enable_1, int effectFlag_2, int index, int limit, TitaVo... titaVo);

  /**
   * DefCode %,AND Code %
   *
   * @param defCode_0 defCode_0
   * @param code_1 code_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCode CdCode of List
   */
  public Slice<CdCode> defCodeLikeAndCodeLike(String defCode_0, String code_1, int index, int limit, TitaVo... titaVo);

  /**
   * DefCode = ,AND DefType = ,AND Item %
   *
   * @param defCode_0 defCode_0
   * @param defType_1 defType_1
   * @param item_2 item_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCode CdCode of List
   */
  public Slice<CdCode> defItemEq2(String defCode_0, int defType_1, String item_2, int index, int limit, TitaVo... titaVo);

  /**
   * DefType = ,OR DefCode % ,OR Item %
   *
   * @param defType_0 defType_0
   * @param defCode_1 defCode_1
   * @param item_2 item_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCode CdCode of List
   */
  public Slice<CdCode> getL6064(int defType_0, String defCode_1, String item_2, int index, int limit, TitaVo... titaVo);

  /**
   * DefCode % ,OR Item %
   *
   * @param defCode_0 defCode_0
   * @param item_1 item_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCode CdCode of List
   */
  public Slice<CdCode> getL60642(String defCode_0, String item_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdCode
   * 
   * @param cdCodeId key
   * @param titaVo Variable-Length Argument
   * @return CdCode CdCode
   */
  public CdCode holdById(CdCodeId cdCodeId, TitaVo... titaVo);

  /**
   * hold By CdCode
   * 
   * @param cdCode key
   * @param titaVo Variable-Length Argument
   * @return CdCode CdCode
   */
  public CdCode holdById(CdCode cdCode, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdCode Entity
   * @param titaVo Variable-Length Argument
   * @return CdCode Entity
   * @throws DBException exception
   */
  public CdCode insert(CdCode cdCode, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdCode Entity
   * @param titaVo Variable-Length Argument
   * @return CdCode Entity
   * @throws DBException exception
   */
  public CdCode update(CdCode cdCode, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdCode Entity
   * @param titaVo Variable-Length Argument
   * @return CdCode Entity
   * @throws DBException exception
   */
  public CdCode update2(CdCode cdCode, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdCode Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdCode cdCode, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdCode Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdCode> cdCode, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdCode Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdCode> cdCode, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdCode Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdCode> cdCode, TitaVo... titaVo) throws DBException;

}
