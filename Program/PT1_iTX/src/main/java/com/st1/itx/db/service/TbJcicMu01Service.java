package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TbJcicMu01;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.TbJcicMu01Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TbJcicMu01Service {

	/**
	 * findByPrimaryKey
	 *
	 * @param tbJcicMu01Id PK
	 * @param titaVo       Variable-Length Argument
	 * @return TbJcicMu01 TbJcicMu01
	 */
	public TbJcicMu01 findById(TbJcicMu01Id tbJcicMu01Id, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TbJcicMu01 TbJcicMu01 of List
	 */
	public Slice<TbJcicMu01> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * EmpId=
	 *
	 * @param empId_0 empId_0
	 * @param index   Page Index
	 * @param limit   Page Data Limit
	 * @param titaVo  Variable-Length Argument
	 * @return Slice TbJcicMu01 TbJcicMu01 of List
	 */
	public Slice<TbJcicMu01> empIdEq(String empId_0, int index, int limit, TitaVo... titaVo);

	/**
	 * DataDate=
	 *
	 * @param dataDate_0 dataDate_0
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice TbJcicMu01 TbJcicMu01 of List
	 */
	public Slice<TbJcicMu01> dataDateEq(int dataDate_0, int index, int limit, TitaVo... titaVo);

	/**
	 * EmpId= , AND DataDate=
	 *
	 * @param empId_0    empId_0
	 * @param dataDate_1 dataDate_1
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice TbJcicMu01 TbJcicMu01 of List
	 */
	public Slice<TbJcicMu01> empIdRcEq(String empId_0, int dataDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * HeadOfficeCode= , AND BranchCode= , AND DataDate=
	 *
	 * @param headOfficeCode_0 headOfficeCode_0
	 * @param branchCode_1     branchCode_1
	 * @param dataDate_2       dataDate_2
	 * @param index            Page Index
	 * @param limit            Page Data Limit
	 * @param titaVo           Variable-Length Argument
	 * @return Slice TbJcicMu01 TbJcicMu01 of List
	 */
	public Slice<TbJcicMu01> findByKey(String headOfficeCode_0, String branchCode_1, int dataDate_2, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By TbJcicMu01
	 * 
	 * @param tbJcicMu01Id key
	 * @param titaVo       Variable-Length Argument
	 * @return TbJcicMu01 TbJcicMu01
	 */
	public TbJcicMu01 holdById(TbJcicMu01Id tbJcicMu01Id, TitaVo... titaVo);

	/**
	 * hold By TbJcicMu01
	 * 
	 * @param tbJcicMu01 key
	 * @param titaVo     Variable-Length Argument
	 * @return TbJcicMu01 TbJcicMu01
	 */
	public TbJcicMu01 holdById(TbJcicMu01 tbJcicMu01, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param tbJcicMu01 Entity
	 * @param titaVo     Variable-Length Argument
	 * @return TbJcicMu01 Entity
	 * @throws DBException exception
	 */
	public TbJcicMu01 insert(TbJcicMu01 tbJcicMu01, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param tbJcicMu01 Entity
	 * @param titaVo     Variable-Length Argument
	 * @return TbJcicMu01 Entity
	 * @throws DBException exception
	 */
	public TbJcicMu01 update(TbJcicMu01 tbJcicMu01, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param tbJcicMu01 Entity
	 * @param titaVo     Variable-Length Argument
	 * @return TbJcicMu01 Entity
	 * @throws DBException exception
	 */
	public TbJcicMu01 update2(TbJcicMu01 tbJcicMu01, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param tbJcicMu01 Entity
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TbJcicMu01 tbJcicMu01, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param tbJcicMu01 Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TbJcicMu01> tbJcicMu01, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param tbJcicMu01 Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TbJcicMu01> tbJcicMu01, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param tbJcicMu01 Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TbJcicMu01> tbJcicMu01, TitaVo... titaVo) throws DBException;

}
