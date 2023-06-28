package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClFac;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClFacId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClFacService {

  /**
   * findByPrimaryKey
   *
   * @param clFacId PK
   * @param titaVo Variable-Length Argument
   * @return ClFac ClFac
   */
  public ClFac findById(ClFacId clFacId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClFac ClFac of List
   */
  public Slice<ClFac> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = 
   *
   * @param clCode1_0 clCode1_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClFac ClFac of List
   */
  public Slice<ClFac> clCode1Eq(int clCode1_0, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClFac ClFac of List
   */
  public Slice<ClFac> clCode2Eq(int clCode1_0, int clCode2_1, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo =
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClFac ClFac of List
   */
  public Slice<ClFac> clNoEq(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * ApproveNo = 
   *
   * @param approveNo_0 approveNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClFac ClFac of List
   */
  public Slice<ClFac> approveNoEq(int approveNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClFac ClFac of List
   */
  public Slice<ClFac> facmNoEq(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = 
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClFac ClFac of List
   */
  public Slice<ClFac> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * ApproveNo &gt;= ,AND ApproveNo &lt;= ,AND FacmNo &gt;= ,AND FacmNo &lt;=
   *
   * @param approveNo_0 approveNo_0
   * @param approveNo_1 approveNo_1
   * @param facmNo_2 facmNo_2
   * @param facmNo_3 facmNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClFac ClFac of List
   */
  public Slice<ClFac> findRange(int approveNo_0, int approveNo_1, int facmNo_2, int facmNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = ,AND ApproveNo &gt;= ,AND ApproveNo &lt;= ,AND CustNo &gt;= ,AND CustNo &lt;= ,AND FacmNo &gt;= ,AND FacmNo &lt;= 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param approveNo_3 approveNo_3
   * @param approveNo_4 approveNo_4
   * @param custNo_5 custNo_5
   * @param custNo_6 custNo_6
   * @param facmNo_7 facmNo_7
   * @param facmNo_8 facmNo_8
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClFac ClFac of List
   */
  public Slice<ClFac> selectForL2038(int clCode1_0, int clCode2_1, int clNo_2, int approveNo_3, int approveNo_4, int custNo_5, int custNo_6, int facmNo_7, int facmNo_8, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 &gt;= ,AND ClCode1 &lt;= ,AND ClCode2 &gt;= ,AND ClCode2 &lt;= ,AND ClNo &gt;= ,AND ClNo &lt;= ,AND CustNo &gt;= ,AND CustNo &lt;= ,AND FacmNo &gt;= ,AND FacmNo &lt;= ,AND ApproveNo &gt;= ,AND ApproveNo &lt;= 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode1_1 clCode1_1
   * @param clCode2_2 clCode2_2
   * @param clCode2_3 clCode2_3
   * @param clNo_4 clNo_4
   * @param clNo_5 clNo_5
   * @param custNo_6 custNo_6
   * @param custNo_7 custNo_7
   * @param facmNo_8 facmNo_8
   * @param facmNo_9 facmNo_9
   * @param approveNo_10 approveNo_10
   * @param approveNo_11 approveNo_11
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClFac ClFac of List
   */
  public Slice<ClFac> selectForL2049(int clCode1_0, int clCode1_1, int clCode2_2, int clCode2_3, int clNo_4, int clNo_5, int custNo_6, int custNo_7, int facmNo_8, int facmNo_9, int approveNo_10, int approveNo_11, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;= 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param facmNo_2 facmNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClFac ClFac of List
   */
  public Slice<ClFac> selectForL2017CustNo(int custNo_0, int facmNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo =  ,AND MainFlag = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param mainFlag_2 mainFlag_2
   * @param titaVo Variable-Length Argument
   * @return Slice ClFac ClFac of List
   */
  public ClFac mainClNoFirst(int custNo_0, int facmNo_1, String mainFlag_2, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo =,AND CustNo = ,AND FacmNo = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param custNo_3 custNo_3
   * @param facmNo_4 facmNo_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClFac ClFac of List
   */
  public Slice<ClFac> selectForL5064(int clCode1_0, int clCode2_1, int clNo_2, int custNo_3, int facmNo_4, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo =,AND MainFlag = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param mainFlag_3 mainFlag_3
   * @param titaVo Variable-Length Argument
   * @return Slice ClFac ClFac of List
   */
  public ClFac clMainFirst(int clCode1_0, int clCode2_1, int clNo_2, String mainFlag_3, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo =
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClFac ClFac of List
   */
  public Slice<ClFac> selecrFroL2411(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By ClFac
   * 
   * @param clFacId key
   * @param titaVo Variable-Length Argument
   * @return ClFac ClFac
   */
  public ClFac holdById(ClFacId clFacId, TitaVo... titaVo);

  /**
   * hold By ClFac
   * 
   * @param clFac key
   * @param titaVo Variable-Length Argument
   * @return ClFac ClFac
   */
  public ClFac holdById(ClFac clFac, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param clFac Entity
   * @param titaVo Variable-Length Argument
   * @return ClFac Entity
   * @throws DBException exception
   */
  public ClFac insert(ClFac clFac, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param clFac Entity
   * @param titaVo Variable-Length Argument
   * @return ClFac Entity
   * @throws DBException exception
   */
  public ClFac update(ClFac clFac, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param clFac Entity
   * @param titaVo Variable-Length Argument
   * @return ClFac Entity
   * @throws DBException exception
   */
  public ClFac update2(ClFac clFac, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param clFac Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(ClFac clFac, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param clFac Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<ClFac> clFac, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param clFac Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<ClFac> clFac, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param clFac Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<ClFac> clFac, TitaVo... titaVo) throws DBException;

}
