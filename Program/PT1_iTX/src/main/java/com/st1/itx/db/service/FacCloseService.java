package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FacClose;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.FacCloseId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacCloseService {

  /**
   * findByPrimaryKey
   *
   * @param facCloseId PK
   * @param titaVo Variable-Length Argument
   * @return FacClose FacClose
   */
  public FacClose findById(FacCloseId facCloseId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacClose FacClose of List
   */
  public Slice<FacClose> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = 
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacClose FacClose of List
   */
  public Slice<FacClose> findCustNo(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacClose FacClose of List
   */
  public Slice<FacClose> findFacmNo(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * CloseDate &gt;= ,AND CloseDate &lt;= 
   *
   * @param closeDate_0 closeDate_0
   * @param closeDate_1 closeDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacClose FacClose of List
   */
  public Slice<FacClose> findCloseDate(int closeDate_0, int closeDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo =
   *
   * @param custNo_0 custNo_0
   * @param titaVo Variable-Length Argument
   * @return Slice FacClose FacClose of List
   */
  public FacClose findMaxCloseNoFirst(int custNo_0, TitaVo... titaVo);

  /**
   * EntryDate =
   *
   * @param entryDate_0 entryDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacClose FacClose of List
   */
  public Slice<FacClose> findEntryDate(int entryDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CloseDate = ,AND CloseNo &gt;= ,AND CloseNo &lt;= ,AND CarLoan &gt;= ,AND CarLoan &lt;=
   *
   * @param closeDate_0 closeDate_0
   * @param closeNo_1 closeNo_1
   * @param closeNo_2 closeNo_2
   * @param carLoan_3 carLoan_3
   * @param carLoan_4 carLoan_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacClose FacClose of List
   */
  public Slice<FacClose> findCloseNo(int closeDate_0, int closeNo_1, int closeNo_2, int carLoan_3, int carLoan_4, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND FunCode ^i
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param funCode_2 funCode_2
   * @param titaVo Variable-Length Argument
   * @return Slice FacClose FacClose of List
   */
  public FacClose findFacmNoFirst(int custNo_0, int facmNo_1, List<String> funCode_2, TitaVo... titaVo);

  /**
   * ApplDate &gt;= ,AND ApplDate &lt;=
   *
   * @param applDate_0 applDate_0
   * @param applDate_1 applDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacClose FacClose of List
   */
  public Slice<FacClose> findApplDateEq(int applDate_0, int applDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo &gt;= ,AND CustNo &lt;= 
   *
   * @param custNo_0 custNo_0
   * @param custNo_1 custNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacClose FacClose of List
   */
  public Slice<FacClose> findCustNoRange(int custNo_0, int custNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param titaVo Variable-Length Argument
   * @return Slice FacClose FacClose of List
   */
  public FacClose findFacmNoMaxCloseNoFirst(int custNo_0, int facmNo_1, TitaVo... titaVo);

  /**
   * EntryDate &gt;= ,AND EntryDate &lt;= 
   *
   * @param entryDate_0 entryDate_0
   * @param entryDate_1 entryDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacClose FacClose of List
   */
  public Slice<FacClose> findEntryDateRange(int entryDate_0, int entryDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * ApplDate =
   *
   * @param applDate_0 applDate_0
   * @param titaVo Variable-Length Argument
   * @return Slice FacClose FacClose of List
   */
  public FacClose findLastCloseNoFirst(int applDate_0, TitaVo... titaVo);

  /**
   * hold By FacClose
   * 
   * @param facCloseId key
   * @param titaVo Variable-Length Argument
   * @return FacClose FacClose
   */
  public FacClose holdById(FacCloseId facCloseId, TitaVo... titaVo);

  /**
   * hold By FacClose
   * 
   * @param facClose key
   * @param titaVo Variable-Length Argument
   * @return FacClose FacClose
   */
  public FacClose holdById(FacClose facClose, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param facClose Entity
   * @param titaVo Variable-Length Argument
   * @return FacClose Entity
   * @throws DBException exception
   */
  public FacClose insert(FacClose facClose, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param facClose Entity
   * @param titaVo Variable-Length Argument
   * @return FacClose Entity
   * @throws DBException exception
   */
  public FacClose update(FacClose facClose, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param facClose Entity
   * @param titaVo Variable-Length Argument
   * @return FacClose Entity
   * @throws DBException exception
   */
  public FacClose update2(FacClose facClose, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param facClose Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(FacClose facClose, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param facClose Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<FacClose> facClose, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param facClose Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<FacClose> facClose, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param facClose Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<FacClose> facClose, TitaVo... titaVo) throws DBException;

}
