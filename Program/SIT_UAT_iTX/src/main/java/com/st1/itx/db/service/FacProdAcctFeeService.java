package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FacProdAcctFee;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.FacProdAcctFeeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacProdAcctFeeService {

  /**
   * findByPrimaryKey
   *
   * @param facProdAcctFeeId PK
   * @param titaVo Variable-Length Argument
   * @return FacProdAcctFee FacProdAcctFee
   */
  public FacProdAcctFee findById(FacProdAcctFeeId facProdAcctFeeId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacProdAcctFee FacProdAcctFee of List
   */
  public Slice<FacProdAcctFee> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ProdNo =  ,AND LoanLow &gt;= ,AND LoanLow &lt;=
   *
   * @param prodNo_0 prodNo_0
   * @param loanLow_1 loanLow_1
   * @param loanLow_2 loanLow_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacProdAcctFee FacProdAcctFee of List
   */
  public Slice<FacProdAcctFee> acctFeeProdNoEq(String prodNo_0, BigDecimal loanLow_1, BigDecimal loanLow_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By FacProdAcctFee
   * 
   * @param facProdAcctFeeId key
   * @param titaVo Variable-Length Argument
   * @return FacProdAcctFee FacProdAcctFee
   */
  public FacProdAcctFee holdById(FacProdAcctFeeId facProdAcctFeeId, TitaVo... titaVo);

  /**
   * hold By FacProdAcctFee
   * 
   * @param facProdAcctFee key
   * @param titaVo Variable-Length Argument
   * @return FacProdAcctFee FacProdAcctFee
   */
  public FacProdAcctFee holdById(FacProdAcctFee facProdAcctFee, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param facProdAcctFee Entity
   * @param titaVo Variable-Length Argument
   * @return FacProdAcctFee Entity
   * @throws DBException exception
   */
  public FacProdAcctFee insert(FacProdAcctFee facProdAcctFee, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param facProdAcctFee Entity
   * @param titaVo Variable-Length Argument
   * @return FacProdAcctFee Entity
   * @throws DBException exception
   */
  public FacProdAcctFee update(FacProdAcctFee facProdAcctFee, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param facProdAcctFee Entity
   * @param titaVo Variable-Length Argument
   * @return FacProdAcctFee Entity
   * @throws DBException exception
   */
  public FacProdAcctFee update2(FacProdAcctFee facProdAcctFee, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param facProdAcctFee Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(FacProdAcctFee facProdAcctFee, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param facProdAcctFee Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<FacProdAcctFee> facProdAcctFee, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param facProdAcctFee Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<FacProdAcctFee> facProdAcctFee, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param facProdAcctFee Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<FacProdAcctFee> facProdAcctFee, TitaVo... titaVo) throws DBException;

}
