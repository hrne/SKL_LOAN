package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.PostDeductMedia;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.PostDeductMediaId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PostDeductMediaService {

	/**
	 * findByPrimaryKey
	 *
	 * @param postDeductMediaId PK
	 * @param titaVo            Variable-Length Argument
	 * @return PostDeductMedia PostDeductMedia
	 */
	public PostDeductMedia findById(PostDeductMediaId postDeductMediaId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice PostDeductMedia PostDeductMedia of List
	 */
	public Slice<PostDeductMedia> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * AcDate = , AND BatchNo = , AND DetailSeq =
	 *
	 * @param acDate_0    acDate_0
	 * @param batchNo_1   batchNo_1
	 * @param detailSeq_2 detailSeq_2
	 * @param titaVo      Variable-Length Argument
	 * @return Slice PostDeductMedia PostDeductMedia of List
	 */
	public PostDeductMedia detailSeqFirst(int acDate_0, String batchNo_1, int detailSeq_2, TitaVo... titaVo);

	/**
	 * PostUserNo = ,AND RepayAmt = ,AND OutsrcRemark =
	 *
	 * @param postUserNo_0   postUserNo_0
	 * @param repayAmt_1     repayAmt_1
	 * @param outsrcRemark_2 outsrcRemark_2
	 * @param titaVo         Variable-Length Argument
	 * @return Slice PostDeductMedia PostDeductMedia of List
	 */
	public PostDeductMedia receiveCheckFirst(String postUserNo_0, BigDecimal repayAmt_1, String outsrcRemark_2, TitaVo... titaVo);

	/**
	 * MediaDate =
	 *
	 * @param mediaDate_0 mediaDate_0
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice PostDeductMedia PostDeductMedia of List
	 */
	public Slice<PostDeductMedia> mediaDateEq(int mediaDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By PostDeductMedia
	 * 
	 * @param postDeductMediaId key
	 * @param titaVo            Variable-Length Argument
	 * @return PostDeductMedia PostDeductMedia
	 */
	public PostDeductMedia holdById(PostDeductMediaId postDeductMediaId, TitaVo... titaVo);

	/**
	 * hold By PostDeductMedia
	 * 
	 * @param postDeductMedia key
	 * @param titaVo          Variable-Length Argument
	 * @return PostDeductMedia PostDeductMedia
	 */
	public PostDeductMedia holdById(PostDeductMedia postDeductMedia, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param postDeductMedia Entity
	 * @param titaVo          Variable-Length Argument
	 * @return PostDeductMedia Entity
	 * @throws DBException exception
	 */
	public PostDeductMedia insert(PostDeductMedia postDeductMedia, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param postDeductMedia Entity
	 * @param titaVo          Variable-Length Argument
	 * @return PostDeductMedia Entity
	 * @throws DBException exception
	 */
	public PostDeductMedia update(PostDeductMedia postDeductMedia, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param postDeductMedia Entity
	 * @param titaVo          Variable-Length Argument
	 * @return PostDeductMedia Entity
	 * @throws DBException exception
	 */
	public PostDeductMedia update2(PostDeductMedia postDeductMedia, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param postDeductMedia Entity
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(PostDeductMedia postDeductMedia, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param postDeductMedia Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<PostDeductMedia> postDeductMedia, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param postDeductMedia Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<PostDeductMedia> postDeductMedia, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param postDeductMedia Entity of List
	 * @param titaVo          Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<PostDeductMedia> postDeductMedia, TitaVo... titaVo) throws DBException;

}
