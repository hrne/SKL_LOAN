package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.PostAuthLog;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.PostAuthLogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PostAuthLogService {

  /**
   * findByPrimaryKey
   *
   * @param postAuthLogId PK
   * @param titaVo Variable-Length Argument
   * @return PostAuthLog PostAuthLog
   */
  public PostAuthLog findById(PostAuthLogId postAuthLogId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLog PostAuthLog of List
   */
  public Slice<PostAuthLog> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo =
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLog PostAuthLog of List
   */
  public Slice<PostAuthLog> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * RepayAcct =
   *
   * @param repayAcct_0 repayAcct_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLog PostAuthLog of List
   */
  public Slice<PostAuthLog> repayAcctEq(String repayAcct_0, int index, int limit, TitaVo... titaVo);

  /**
   * RepayAcct %
   *
   * @param repayAcct_0 repayAcct_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLog PostAuthLog of List
   */
  public Slice<PostAuthLog> repayAcctLike(String repayAcct_0, int index, int limit, TitaVo... titaVo);

  /**
   * AuthCreateDate &gt;= ,AND AuthCreateDate &lt;=
   *
   * @param authCreateDate_0 authCreateDate_0
   * @param authCreateDate_1 authCreateDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLog PostAuthLog of List
   */
  public Slice<PostAuthLog> authCreateDateEq(int authCreateDate_0, int authCreateDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * PropDate &gt;= ,AND PropDate &lt;=
   *
   * @param propDate_0 propDate_0
   * @param propDate_1 propDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLog PostAuthLog of List
   */
  public Slice<PostAuthLog> propDateEq(int propDate_0, int propDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * RetrDate &gt;= ,AND RetrDate &lt;=
   *
   * @param retrDate_0 retrDate_0
   * @param retrDate_1 retrDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLog PostAuthLog of List
   */
  public Slice<PostAuthLog> retrDateEq(int retrDate_0, int retrDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId = ,AND PostDepCode = ,AND CustNo =
   *
   * @param custId_0 custId_0
   * @param postDepCode_1 postDepCode_1
   * @param custNo_2 custNo_2
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLog PostAuthLog of List
   */
  public PostAuthLog acctSeqFirst(String custId_0, String postDepCode_1, int custNo_2, TitaVo... titaVo);

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
   * @return Slice PostAuthLog PostAuthLog of List
   */
  public PostAuthLog pkFacmNoFirst(String authApplCode_0, int custNo_1, String postDepCode_2, String repayAcct_3, String authCode_4, int facmNo_5, TitaVo... titaVo);

  /**
   * PostMediaCode ! ,AND AuthErrorCode ^i ,AND CustNo = ,AND PropDate = 
   *
   * @param authErrorCode_1 authErrorCode_1
   * @param custNo_2 custNo_2
   * @param propDate_3 propDate_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLog PostAuthLog of List
   */
  public Slice<PostAuthLog> l4041ARg(List<String> authErrorCode_1, int custNo_2, int propDate_3, int index, int limit, TitaVo... titaVo);

  /**
   * PostMediaCode ! ,AND AuthErrorCode ^i ,AND PropDate = 
   *
   * @param authErrorCode_1 authErrorCode_1
   * @param propDate_2 propDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLog PostAuthLog of List
   */
  public Slice<PostAuthLog> l4041BRg(List<String> authErrorCode_1, int propDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * PostMediaCode ! ,AND AuthErrorCode ^i ,AND CustNo = 
   *
   * @param authErrorCode_1 authErrorCode_1
   * @param custNo_2 custNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLog PostAuthLog of List
   */
  public Slice<PostAuthLog> l4041CRg(List<String> authErrorCode_1, int custNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * PostMediaCode ! ,AND AuthErrorCode ^i 
   *
   * @param authErrorCode_1 authErrorCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLog PostAuthLog of List
   */
  public Slice<PostAuthLog> l4041DRg(List<String> authErrorCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * PostMediaCode = ,AND PropDate &gt;= ,AND PropDate &lt;= 
   *
   * @param postMediaCode_0 postMediaCode_0
   * @param propDate_1 propDate_1
   * @param propDate_2 propDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLog PostAuthLog of List
   */
  public Slice<PostAuthLog> mediaCodeEq(String postMediaCode_0, int propDate_1, int propDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * PostMediaCode ! ,AND PropDate &gt;= ,AND PropDate &lt;= 
   *
   * @param propDate_1 propDate_1
   * @param propDate_2 propDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLog PostAuthLog of List
   */
  public Slice<PostAuthLog> mediaCodeIsnull(int propDate_1, int propDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * PropDate = , AND AuthCode = , AND FileSeq = 
   *
   * @param propDate_0 propDate_0
   * @param authCode_1 authCode_1
   * @param fileSeq_2 fileSeq_2
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLog PostAuthLog of List
   */
  public PostAuthLog fileSeqFirst(int propDate_0, String authCode_1, int fileSeq_2, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLog PostAuthLog of List
   */
  public Slice<PostAuthLog> facmNoEq(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND PostDepCode = ,AND RepayAcct = ,AND AuthCode = 
   *
   * @param custNo_0 custNo_0
   * @param postDepCode_1 postDepCode_1
   * @param repayAcct_2 repayAcct_2
   * @param authCode_3 authCode_3
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLog PostAuthLog of List
   */
  public PostAuthLog repayAcctFirst(int custNo_0, String postDepCode_1, String repayAcct_2, String authCode_3, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLog PostAuthLog of List
   */
  public PostAuthLog facmNoBFirst(int custNo_0, int facmNo_1, TitaVo... titaVo);

  /**
   * CustNo = ,AND AuthApplCode ^i ,AND AuthErrorCode ^i
   *
   * @param custNo_0 custNo_0
   * @param authApplCode_1 authApplCode_1
   * @param authErrorCode_2 authErrorCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PostAuthLog PostAuthLog of List
   */
  public Slice<PostAuthLog> custNoAuthErrorCodeEq(int custNo_0, List<String> authApplCode_1, List<String> authErrorCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By PostAuthLog
   * 
   * @param postAuthLogId key
   * @param titaVo Variable-Length Argument
   * @return PostAuthLog PostAuthLog
   */
  public PostAuthLog holdById(PostAuthLogId postAuthLogId, TitaVo... titaVo);

  /**
   * hold By PostAuthLog
   * 
   * @param postAuthLog key
   * @param titaVo Variable-Length Argument
   * @return PostAuthLog PostAuthLog
   */
  public PostAuthLog holdById(PostAuthLog postAuthLog, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param postAuthLog Entity
   * @param titaVo Variable-Length Argument
   * @return PostAuthLog Entity
   * @throws DBException exception
   */
  public PostAuthLog insert(PostAuthLog postAuthLog, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param postAuthLog Entity
   * @param titaVo Variable-Length Argument
   * @return PostAuthLog Entity
   * @throws DBException exception
   */
  public PostAuthLog update(PostAuthLog postAuthLog, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param postAuthLog Entity
   * @param titaVo Variable-Length Argument
   * @return PostAuthLog Entity
   * @throws DBException exception
   */
  public PostAuthLog update2(PostAuthLog postAuthLog, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param postAuthLog Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(PostAuthLog postAuthLog, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param postAuthLog Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<PostAuthLog> postAuthLog, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param postAuthLog Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<PostAuthLog> postAuthLog, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param postAuthLog Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<PostAuthLog> postAuthLog, TitaVo... titaVo) throws DBException;

}
