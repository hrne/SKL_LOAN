package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FacCaseAppl;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacCaseApplService {

  /**
   * findByPrimaryKey
   *
   * @param applNo PK
   * @param titaVo Variable-Length Argument
   * @return FacCaseAppl FacCaseAppl
   */
  public FacCaseAppl findById(int applNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacCaseAppl FacCaseAppl of List
   */
  public Slice<FacCaseAppl> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ApplNo &gt;= ,AND ApplNo &lt;= ,AND ProcessCode &gt;= ,AND ProcessCode &lt;= 
   *
   * @param applNo_0 applNo_0
   * @param applNo_1 applNo_1
   * @param processCode_2 processCode_2
   * @param processCode_3 processCode_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacCaseAppl FacCaseAppl of List
   */
  public Slice<FacCaseAppl> caseApplNoRange(int applNo_0, int applNo_1, String processCode_2, String processCode_3, int index, int limit, TitaVo... titaVo);

  /**
   * CustUKey = ,AND ProcessCode &gt;= ,AND ProcessCode &lt;= 
   *
   * @param custUKey_0 custUKey_0
   * @param processCode_1 processCode_1
   * @param processCode_2 processCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacCaseAppl FacCaseAppl of List
   */
  public Slice<FacCaseAppl> caseApplCustUKeyEq(String custUKey_0, String processCode_1, String processCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * GroupUKey = ,AND ProcessCode &gt;= ,AND ProcessCode &lt;= 
   *
   * @param groupUKey_0 groupUKey_0
   * @param processCode_1 processCode_1
   * @param processCode_2 processCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacCaseAppl FacCaseAppl of List
   */
  public Slice<FacCaseAppl> caseApplGroupUKeyEq(String groupUKey_0, String processCode_1, String processCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * GroupUKey = ,AND ApplNo &gt;= ,AND ApplNo &lt;=
   *
   * @param groupUKey_0 groupUKey_0
   * @param applNo_1 applNo_1
   * @param applNo_2 applNo_2
   * @param titaVo Variable-Length Argument
   * @return Slice FacCaseAppl FacCaseAppl of List
   */
  public FacCaseAppl caseApplGroupUKeyFirst(String groupUKey_0, int applNo_1, int applNo_2, TitaVo... titaVo);

  /**
   * CreditSysNo =
   *
   * @param creditSysNo_0 creditSysNo_0
   * @param titaVo Variable-Length Argument
   * @return Slice FacCaseAppl FacCaseAppl of List
   */
  public FacCaseAppl CreditSysNoFirst(int creditSysNo_0, TitaVo... titaVo);

  /**
   * SyndNo = 
   *
   * @param syndNo_0 syndNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacCaseAppl FacCaseAppl of List
   */
  public Slice<FacCaseAppl> syndNoEq(int syndNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By FacCaseAppl
   * 
   * @param applNo key
   * @param titaVo Variable-Length Argument
   * @return FacCaseAppl FacCaseAppl
   */
  public FacCaseAppl holdById(int applNo, TitaVo... titaVo);

  /**
   * hold By FacCaseAppl
   * 
   * @param facCaseAppl key
   * @param titaVo Variable-Length Argument
   * @return FacCaseAppl FacCaseAppl
   */
  public FacCaseAppl holdById(FacCaseAppl facCaseAppl, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param facCaseAppl Entity
   * @param titaVo Variable-Length Argument
   * @return FacCaseAppl Entity
   * @throws DBException exception
   */
  public FacCaseAppl insert(FacCaseAppl facCaseAppl, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param facCaseAppl Entity
   * @param titaVo Variable-Length Argument
   * @return FacCaseAppl Entity
   * @throws DBException exception
   */
  public FacCaseAppl update(FacCaseAppl facCaseAppl, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param facCaseAppl Entity
   * @param titaVo Variable-Length Argument
   * @return FacCaseAppl Entity
   * @throws DBException exception
   */
  public FacCaseAppl update2(FacCaseAppl facCaseAppl, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param facCaseAppl Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(FacCaseAppl facCaseAppl, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param facCaseAppl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<FacCaseAppl> facCaseAppl, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param facCaseAppl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<FacCaseAppl> facCaseAppl, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param facCaseAppl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<FacCaseAppl> facCaseAppl, TitaVo... titaVo) throws DBException;

}
