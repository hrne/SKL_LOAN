package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.SlipMedia;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.SlipMediaId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface SlipMediaService {

	/**
	 * findByPrimaryKey
	 *
	 * @param slipMediaId PK
	 * @param titaVo      Variable-Length Argument
	 * @return SlipMedia SlipMedia
	 */
	public SlipMedia findById(SlipMediaId slipMediaId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice SlipMedia SlipMedia of List
	 */
	public Slice<SlipMedia> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * AcDate = ,AND BatchNo = ,AND MediaSeq =
	 *
	 * @param acDate_0   acDate_0
	 * @param batchNo_1  batchNo_1
	 * @param mediaSeq_2 mediaSeq_2
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice SlipMedia SlipMedia of List
	 */
	public Slice<SlipMedia> findMediaSeq(int acDate_0, int batchNo_1, int mediaSeq_2, int index, int limit, TitaVo... titaVo);

	/**
	 * AcDate = ,AND BatchNo =
	 *
	 * @param acDate_0  acDate_0
	 * @param batchNo_1 batchNo_1
	 * @param index     Page Index
	 * @param limit     Page Data Limit
	 * @param titaVo    Variable-Length Argument
	 * @return Slice SlipMedia SlipMedia of List
	 */
	public Slice<SlipMedia> findBatchNo(int acDate_0, int batchNo_1, int index, int limit, TitaVo... titaVo);

	/**
	 * AcDate = ,AND BatchNo =
	 *
	 * @param acDate_0  acDate_0
	 * @param batchNo_1 batchNo_1
	 * @param titaVo    Variable-Length Argument
	 * @return Slice SlipMedia SlipMedia of List
	 */
	public SlipMedia findMediaSeqFirst(int acDate_0, int batchNo_1, TitaVo... titaVo);

	/**
	 * hold By SlipMedia
	 * 
	 * @param slipMediaId key
	 * @param titaVo      Variable-Length Argument
	 * @return SlipMedia SlipMedia
	 */
	public SlipMedia holdById(SlipMediaId slipMediaId, TitaVo... titaVo);

	/**
	 * hold By SlipMedia
	 * 
	 * @param slipMedia key
	 * @param titaVo    Variable-Length Argument
	 * @return SlipMedia SlipMedia
	 */
	public SlipMedia holdById(SlipMedia slipMedia, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param slipMedia Entity
	 * @param titaVo    Variable-Length Argument
	 * @return SlipMedia Entity
	 * @throws DBException exception
	 */
	public SlipMedia insert(SlipMedia slipMedia, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param slipMedia Entity
	 * @param titaVo    Variable-Length Argument
	 * @return SlipMedia Entity
	 * @throws DBException exception
	 */
	public SlipMedia update(SlipMedia slipMedia, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param slipMedia Entity
	 * @param titaVo    Variable-Length Argument
	 * @return SlipMedia Entity
	 * @throws DBException exception
	 */
	public SlipMedia update2(SlipMedia slipMedia, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param slipMedia Entity
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(SlipMedia slipMedia, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param slipMedia Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<SlipMedia> slipMedia, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param slipMedia Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<SlipMedia> slipMedia, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param slipMedia Entity of List
	 * @param titaVo    Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<SlipMedia> slipMedia, TitaVo... titaVo) throws DBException;

}
