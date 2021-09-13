package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.PostAuthLogHistory;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PostAuthLogHistoryService {

  /**
   * findByPrimaryKey
   *
   * @param logNo PK
   * @param titaVo Variable-Length Argument
   * @return PostAuthLogHistory PostAuthLogHistory
   */
  public PostAuthLogHistory findById(Long logNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public Slice<PostAuthLogHistory> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo =
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public Slice<PostAuthLogHistory> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * RepayAcct =
   *
   * @param repayAcct_0 repayAcct_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public Slice<PostAuthLogHistory> repayAcctEq(String repayAcct_0, int index, int limit, TitaVo... titaVo);

  /**
   * RepayAcct %
   *
   * @param repayAcct_0 repayAcct_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public Slice<PostAuthLogHistory> repayAcctLike(String repayAcct_0, int index, int limit, TitaVo... titaVo);

  /**
   * AuthCreateDate &gt;= ,AND AuthCreateDate &lt;=
   *
   * @param authCreateDate_0 authCreateDate_0
   * @param authCreateDate_1 authCreateDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public Slice<PostAuthLogHistory> authCreateDateEq(int authCreateDate_0, int authCreateDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * PropDate &gt;= ,AND PropDate &lt;=
   *
   * @param propDate_0 propDate_0
   * @param propDate_1 propDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public Slice<PostAuthLogHistory> propDateEq(int propDate_0, int propDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * RetrDate &gt;= ,AND RetrDate &lt;=
   *
   * @param retrDate_0 retrDate_0
   * @param retrDate_1 retrDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public Slice<PostAuthLogHistory> retrDateEq(int retrDate_0, int retrDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId = ,AND PostDepCode = ,AND CustNo =
   *
   * @param custId_0 custId_0
   * @param postDepCode_1 postDepCode_1
   * @param custNo_2 custNo_2
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public PostAuthLogHistory acctSeqFirst(String custId_0, String postDepCode_1, int custNo_2, TitaVo... titaVo);

  /**
   * AuthApplCode = ,AND CustNo = ,AND PostDepCode = ,AND RepayAcct = ,AND AuthCode = ,AND FacmNo = 
   *
   * @param authApplCode_0 authApplCode_0
   * @param custNo_1 custNo_1
   * @param postDepCode_2 postDepCode_2
   * @param repayAcct_3 repayAcct_3
   * @param authCode_4 authCode_4
   * @param facmNo_5 facmNo_5
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public PostAuthLogHistory pkFacmNoFirst(String authApplCode_0, int custNo_1, String postDepCode_2, String repayAcct_3, String authCode_4, int facmNo_5, TitaVo... titaVo);

  /**
   * PostMediaCode ! ,AND AuthErrorCode ^i ,AND CustNo = ,AND PropDate = 
   *
   * @param authErrorCode_1 authErrorCode_1
   * @param custNo_2 custNo_2
   * @param propDate_3 propDate_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public Slice<PostAuthLogHistory> l4041ARg(List<String> authErrorCode_1, int custNo_2, int propDate_3, int index, int limit, TitaVo... titaVo);

  /**
   * PostMediaCode ! ,AND AuthErrorCode ^i ,AND PropDate = 
   *
   * @param authErrorCode_1 authErrorCode_1
   * @param propDate_2 propDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public Slice<PostAuthLogHistory> l4041BRg(List<String> authErrorCode_1, int propDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * PostMediaCode ! ,AND AuthErrorCode ^i ,AND CustNo = 
   *
   * @param authErrorCode_1 authErrorCode_1
   * @param custNo_2 custNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public Slice<PostAuthLogHistory> l4041CRg(List<String> authErrorCode_1, int custNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * PostMediaCode ! ,AND AuthErrorCode ^i 
   *
   * @param authErrorCode_1 authErrorCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public Slice<PostAuthLogHistory> l4041DRg(List<String> authErrorCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * PostMediaCode = ,AND PropDate &gt;= ,AND PropDate &lt;= 
   *
   * @param postMediaCode_0 postMediaCode_0
   * @param propDate_1 propDate_1
   * @param propDate_2 propDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public Slice<PostAuthLogHistory> mediaCodeEq(String postMediaCode_0, int propDate_1, int propDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * PostMediaCode ! ,AND PropDate &gt;= ,AND PropDate &lt;= 
   *
   * @param propDate_1 propDate_1
   * @param propDate_2 propDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public Slice<PostAuthLogHistory> mediaCodeIsnull(int propDate_1, int propDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * PropDate = , AND AuthCode = , AND FileSeq = 
   *
   * @param propDate_0 propDate_0
   * @param authCode_1 authCode_1
   * @param fileSeq_2 fileSeq_2
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public PostAuthLogHistory fileSeqFirst(int propDate_0, String authCode_1, int fileSeq_2, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public Slice<PostAuthLogHistory> facmNoEq(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND PostDepCode = ,AND RepayAcct = ,AND AuthCode = 
   *
   * @param custNo_0 custNo_0
   * @param postDepCode_1 postDepCode_1
   * @param repayAcct_2 repayAcct_2
   * @param authCode_3 authCode_3
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public PostAuthLogHistory repayAcctFirst(int custNo_0, String postDepCode_1, String repayAcct_2, String authCode_3, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public PostAuthLogHistory facmNoBFirst(int custNo_0, int facmNo_1, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND AuthCode = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param authCode_2 authCode_2
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public PostAuthLogHistory facmNoCFirst(int custNo_0, int facmNo_1, String authCode_2, TitaVo... titaVo);

  /**
   * AuthCreateDate = ,AND AuthApplCode = ,AND CustNo = ,AND PostDepCode = ,AND RepayAcct = ,AND AuthCode = 
   *
   * @param authCreateDate_0 authCreateDate_0
   * @param authApplCode_1 authApplCode_1
   * @param custNo_2 custNo_2
   * @param postDepCode_3 postDepCode_3
   * @param repayAcct_4 repayAcct_4
   * @param authCode_5 authCode_5
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLogHistory PostAuthLogHistory of List
   */
  public PostAuthLogHistory authLogFirst(int authCreateDate_0, String authApplCode_1, int custNo_2, String postDepCode_3, String repayAcct_4, String authCode_5, TitaVo... titaVo);

  /**
   * hold By PostAuthLogHistory
   * 
   * @param logNo key
   * @param titaVo Variable-Length Argument
   * @return PostAuthLogHistory PostAuthLogHistory
   */
  public PostAuthLogHistory holdById(Long logNo, TitaVo... titaVo);

  /**
   * hold By PostAuthLogHistory
   * 
   * @param postAuthLogHistory key
   * @param titaVo Variable-Length Argument
   * @return PostAuthLogHistory PostAuthLogHistory
   */
  public PostAuthLogHistory holdById(PostAuthLogHistory postAuthLogHistory, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param postAuthLogHistory Entity
   * @param titaVo Variable-Length Argument
   * @return PostAuthLogHistory Entity
   * @throws DBException exception
   */
  public PostAuthLogHistory insert(PostAuthLogHistory postAuthLogHistory, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param postAuthLogHistory Entity
   * @param titaVo Variable-Length Argument
   * @return PostAuthLogHistory Entity
   * @throws DBException exception
   */
  public PostAuthLogHistory update(PostAuthLogHistory postAuthLogHistory, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param postAuthLogHistory Entity
   * @param titaVo Variable-Length Argument
   * @return PostAuthLogHistory Entity
   * @throws DBException exception
   */
  public PostAuthLogHistory update2(PostAuthLogHistory postAuthLogHistory, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param postAuthLogHistory Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(PostAuthLogHistory postAuthLogHistory, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param postAuthLogHistory Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<PostAuthLogHistory> postAuthLogHistory, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param postAuthLogHistory Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<PostAuthLogHistory> postAuthLogHistory, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param postAuthLogHistory Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<PostAuthLogHistory> postAuthLogHistory, TitaVo... titaVo) throws DBException;

}
