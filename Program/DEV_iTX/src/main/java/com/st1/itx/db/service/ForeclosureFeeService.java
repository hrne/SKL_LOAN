package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ForeclosureFee;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ForeclosureFeeService {

  /**
   * findByPrimaryKey
   *
   * @param recordNo PK
   * @param titaVo Variable-Length Argument
   * @return ForeclosureFee ForeclosureFee
   */
  public ForeclosureFee findById(int recordNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ForeclosureFee ForeclosureFee of List
   */
  public Slice<ForeclosureFee> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = 
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ForeclosureFee ForeclosureFee of List
   */
  public Slice<ForeclosureFee> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * ReceiveDate &gt;= ,AND ReceiveDate &lt;=
   *
   * @param receiveDate_0 receiveDate_0
   * @param receiveDate_1 receiveDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ForeclosureFee ForeclosureFee of List
   */
  public Slice<ForeclosureFee> receiveDateBetween(int receiveDate_0, int receiveDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * RecordNo &gt;= ,AND RecordNo &lt;=
   *
   * @param recordNo_0 recordNo_0
   * @param recordNo_1 recordNo_1
   * @param titaVo Variable-Length Argument
   * @return Slice ForeclosureFee ForeclosureFee of List
   */
  public ForeclosureFee findRecordNoFirst(int recordNo_0, int recordNo_1, TitaVo... titaVo);

  /**
   * ReceiveDate &gt;= ,AND ReceiveDate &lt;= ,AND CustNo &gt;= ,AND CustNo &lt;=
   *
   * @param receiveDate_0 receiveDate_0
   * @param receiveDate_1 receiveDate_1
   * @param custNo_2 custNo_2
   * @param custNo_3 custNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ForeclosureFee ForeclosureFee of List
   */
  public Slice<ForeclosureFee> selectForL2078(int receiveDate_0, int receiveDate_1, int custNo_2, int custNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * OpenAcDate &gt;= ,AND OpenAcDate &lt;=
   *
   * @param openAcDate_0 openAcDate_0
   * @param openAcDate_1 openAcDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ForeclosureFee ForeclosureFee of List
   */
  public Slice<ForeclosureFee> openAcDateBetween(int openAcDate_0, int openAcDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * CloseDate &gt;= ,AND CloseDate &lt;=
   *
   * @param closeDate_0 closeDate_0
   * @param closeDate_1 closeDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ForeclosureFee ForeclosureFee of List
   */
  public Slice<ForeclosureFee> closeDateBetween(int closeDate_0, int closeDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * OverdueDate &gt;= ,AND OverdueDate &lt;=
   *
   * @param overdueDate_0 overdueDate_0
   * @param overdueDate_1 overdueDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ForeclosureFee ForeclosureFee of List
   */
  public Slice<ForeclosureFee> overdueDateBetween(int overdueDate_0, int overdueDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo =  ,And FacmNo = ,AND CloseDate = 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param closeDate_2 closeDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ForeclosureFee ForeclosureFee of List
   */
  public Slice<ForeclosureFee> selectForL2R32(int custNo_0, int facmNo_1, int closeDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By ForeclosureFee
   * 
   * @param recordNo key
   * @param titaVo Variable-Length Argument
   * @return ForeclosureFee ForeclosureFee
   */
  public ForeclosureFee holdById(int recordNo, TitaVo... titaVo);

  /**
   * hold By ForeclosureFee
   * 
   * @param foreclosureFee key
   * @param titaVo Variable-Length Argument
   * @return ForeclosureFee ForeclosureFee
   */
  public ForeclosureFee holdById(ForeclosureFee foreclosureFee, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param foreclosureFee Entity
   * @param titaVo Variable-Length Argument
   * @return ForeclosureFee Entity
   * @throws DBException exception
   */
  public ForeclosureFee insert(ForeclosureFee foreclosureFee, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param foreclosureFee Entity
   * @param titaVo Variable-Length Argument
   * @return ForeclosureFee Entity
   * @throws DBException exception
   */
  public ForeclosureFee update(ForeclosureFee foreclosureFee, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param foreclosureFee Entity
   * @param titaVo Variable-Length Argument
   * @return ForeclosureFee Entity
   * @throws DBException exception
   */
  public ForeclosureFee update2(ForeclosureFee foreclosureFee, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param foreclosureFee Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(ForeclosureFee foreclosureFee, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param foreclosureFee Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<ForeclosureFee> foreclosureFee, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param foreclosureFee Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<ForeclosureFee> foreclosureFee, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param foreclosureFee Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<ForeclosureFee> foreclosureFee, TitaVo... titaVo) throws DBException;

}
