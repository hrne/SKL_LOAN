package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcAcctCheckDetail;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.AcAcctCheckDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AcAcctCheckDetailService {

	/**
	 * findByPrimaryKey
	 *
	 * @param acAcctCheckDetailId PK
	 * @param titaVo              Variable-Length Argument
	 * @return AcAcctCheckDetail AcAcctCheckDetail
	 */
	public AcAcctCheckDetail findById(AcAcctCheckDetailId acAcctCheckDetailId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice AcAcctCheckDetail AcAcctCheckDetail of List
	 */
	public Slice<AcAcctCheckDetail> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * AcDate =
	 *
	 * @param acDate_0 acDate_0
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice AcAcctCheckDetail AcAcctCheckDetail of List
	 */
	public Slice<AcAcctCheckDetail> findAcDate(int acDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By AcAcctCheckDetail
	 * 
	 * @param acAcctCheckDetailId key
	 * @param titaVo              Variable-Length Argument
	 * @return AcAcctCheckDetail AcAcctCheckDetail
	 */
	public AcAcctCheckDetail holdById(AcAcctCheckDetailId acAcctCheckDetailId, TitaVo... titaVo);

	/**
	 * hold By AcAcctCheckDetail
	 * 
	 * @param acAcctCheckDetail key
	 * @param titaVo            Variable-Length Argument
	 * @return AcAcctCheckDetail AcAcctCheckDetail
	 */
	public AcAcctCheckDetail holdById(AcAcctCheckDetail acAcctCheckDetail, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param acAcctCheckDetail Entity
	 * @param titaVo            Variable-Length Argument
	 * @return AcAcctCheckDetail Entity
	 * @throws DBException exception
	 */
	public AcAcctCheckDetail insert(AcAcctCheckDetail acAcctCheckDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param acAcctCheckDetail Entity
	 * @param titaVo            Variable-Length Argument
	 * @return AcAcctCheckDetail Entity
	 * @throws DBException exception
	 */
	public AcAcctCheckDetail update(AcAcctCheckDetail acAcctCheckDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param acAcctCheckDetail Entity
	 * @param titaVo            Variable-Length Argument
	 * @return AcAcctCheckDetail Entity
	 * @throws DBException exception
	 */
	public AcAcctCheckDetail update2(AcAcctCheckDetail acAcctCheckDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param acAcctCheckDetail Entity
	 * @param titaVo            Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(AcAcctCheckDetail acAcctCheckDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param acAcctCheckDetail Entity of List
	 * @param titaVo            Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<AcAcctCheckDetail> acAcctCheckDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param acAcctCheckDetail Entity of List
	 * @param titaVo            Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<AcAcctCheckDetail> acAcctCheckDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param acAcctCheckDetail Entity of List
	 * @param titaVo            Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<AcAcctCheckDetail> acAcctCheckDetail, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (放款關帳 )維護AcAcctCheckDetail會計業務檢核明細檔
	 * 
	 * @param tbsdyf int
	 * @param empNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L6_AcAcctCheckDetail_Ins(int tbsdyf, String empNo, TitaVo... titaVo);

}
