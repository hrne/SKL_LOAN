package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxFile;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxFileService {

	/**
	 * findByPrimaryKey
	 *
	 * @param fileNo PK
	 * @param titaVo Variable-Length Argument
	 * @return TxFile TxFile
	 */
	public TxFile findById(Long fileNo, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TxFile TxFile of List
	 */
	public Slice<TxFile> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * FileDate = ,AND BrNo =
	 *
	 * @param fileDate_0 fileDate_0
	 * @param brNo_1     brNo_1
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice TxFile TxFile of List
	 */
	public Slice<TxFile> findByDate(int fileDate_0, String brNo_1, int index, int limit, TitaVo... titaVo);

	/**
	 * FileDate &gt;= ,AND FileDate &lt;= ,AND BrNo = ,AND CreateEmpNo % ,AND
	 * FileCode % ,AND FileItem %
	 *
	 * @param fileDate_0    fileDate_0
	 * @param fileDate_1    fileDate_1
	 * @param brNo_2        brNo_2
	 * @param createEmpNo_3 createEmpNo_3
	 * @param fileCode_4    fileCode_4
	 * @param fileItem_5    fileItem_5
	 * @param index         Page Index
	 * @param limit         Page Data Limit
	 * @param titaVo        Variable-Length Argument
	 * @return Slice TxFile TxFile of List
	 */
	public Slice<TxFile> findByLC009(int fileDate_0, int fileDate_1, String brNo_2, String createEmpNo_3, String fileCode_4, String fileItem_5, int index, int limit, TitaVo... titaVo);

	/**
	 * FileDate &gt;= ,AND FileDate &lt;= ,AND BrNo = ,AND CreateEmpNo % ,AND
	 * FileCode % ,AND FileItem %,AND CreateDate &gt;=,AND CreateDate &lt;
	 *
	 * @param fileDate_0    fileDate_0
	 * @param fileDate_1    fileDate_1
	 * @param brNo_2        brNo_2
	 * @param createEmpNo_3 createEmpNo_3
	 * @param fileCode_4    fileCode_4
	 * @param fileItem_5    fileItem_5
	 * @param createDate_6  createDate_6
	 * @param createDate_7  createDate_7
	 * @param index         Page Index
	 * @param limit         Page Data Limit
	 * @param titaVo        Variable-Length Argument
	 * @return Slice TxFile TxFile of List
	 */
	public Slice<TxFile> findByLC009WithCreateDate(int fileDate_0, int fileDate_1, String brNo_2, String createEmpNo_3, String fileCode_4, String fileItem_5, java.sql.Timestamp createDate_6,
			java.sql.Timestamp createDate_7, int index, int limit, TitaVo... titaVo);

	/**
	 * BatchNo =
	 *
	 * @param batchNo_0 batchNo_0
	 * @param titaVo    Variable-Length Argument
	 * @return Slice TxFile TxFile of List
	 */
	public TxFile findByBatchNoFirst(String batchNo_0, TitaVo... titaVo);

	/**
	 * FileCode =
	 *
	 * @param fileCode_0 fileCode_0
	 * @param titaVo     Variable-Length Argument
	 * @return Slice TxFile TxFile of List
	 */
	public TxFile findByFileCodeFirst(String fileCode_0, TitaVo... titaVo);

	/**
	 * hold By TxFile
	 * 
	 * @param fileNo key
	 * @param titaVo Variable-Length Argument
	 * @return TxFile TxFile
	 */
	public TxFile holdById(Long fileNo, TitaVo... titaVo);

	/**
	 * hold By TxFile
	 * 
	 * @param txFile key
	 * @param titaVo Variable-Length Argument
	 * @return TxFile TxFile
	 */
	public TxFile holdById(TxFile txFile, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param txFile Entity
	 * @param titaVo Variable-Length Argument
	 * @return TxFile Entity
	 * @throws DBException exception
	 */
	public TxFile insert(TxFile txFile, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param txFile Entity
	 * @param titaVo Variable-Length Argument
	 * @return TxFile Entity
	 * @throws DBException exception
	 */
	public TxFile update(TxFile txFile, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param txFile Entity
	 * @param titaVo Variable-Length Argument
	 * @return TxFile Entity
	 * @throws DBException exception
	 */
	public TxFile update2(TxFile txFile, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param txFile Entity
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TxFile txFile, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param txFile Entity of List
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TxFile> txFile, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param txFile Entity of List
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TxFile> txFile, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param txFile Entity of List
	 * @param titaVo Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TxFile> txFile, TitaVo... titaVo) throws DBException;

}
