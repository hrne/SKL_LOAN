package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.HlThreeLaqhcp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.HlThreeLaqhcpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface HlThreeLaqhcpService {

	/**
	 * findByPrimaryKey
	 *
	 * @param hlThreeLaqhcpId PK
	 * @param titaVo          Variable-Length Argument
	 * @return HlThreeLaqhcp HlThreeLaqhcp
	 */
	public HlThreeLaqhcp findById(HlThreeLaqhcpId hlThreeLaqhcpId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice HlThreeLaqhcp HlThreeLaqhcp of List
	 */
	public Slice<HlThreeLaqhcp> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By HlThreeLaqhcp
	 * 
	 * @param hlThreeLaqhcpId key
	 * @param titaVo          Variable-Length Argument
	 * @return HlThreeLaqhcp HlThreeLaqhcp
	 */
	public HlThreeLaqhcp holdById(HlThreeLaqhcpId hlThreeLaqhcpId, TitaVo... titaVo);

	/**
	 * hold By HlThreeLaqhcp
	 * 
	 * @param hlThreeLaqhcp key
	 * @param titaVo        Variable-Length Argument
	 * @return HlThreeLaqhcp HlThreeLaqhcp
	 */
	public HlThreeLaqhcp holdById(HlThreeLaqhcp hlThreeLaqhcp, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param hlThreeLaqhcp Entity
	 * @param titaVo        Variable-Length Argument
	 * @return HlThreeLaqhcp Entity
	 * @throws DBException exception
	 */
	public HlThreeLaqhcp insert(HlThreeLaqhcp hlThreeLaqhcp, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param hlThreeLaqhcp Entity
	 * @param titaVo        Variable-Length Argument
	 * @return HlThreeLaqhcp Entity
	 * @throws DBException exception
	 */
	public HlThreeLaqhcp update(HlThreeLaqhcp hlThreeLaqhcp, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param hlThreeLaqhcp Entity
	 * @param titaVo        Variable-Length Argument
	 * @return HlThreeLaqhcp Entity
	 * @throws DBException exception
	 */
	public HlThreeLaqhcp update2(HlThreeLaqhcp hlThreeLaqhcp, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param hlThreeLaqhcp Entity
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(HlThreeLaqhcp hlThreeLaqhcp, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param hlThreeLaqhcp Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<HlThreeLaqhcp> hlThreeLaqhcp, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param hlThreeLaqhcp Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<HlThreeLaqhcp> hlThreeLaqhcp, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param hlThreeLaqhcp Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<HlThreeLaqhcp> hlThreeLaqhcp, TitaVo... titaVo) throws DBException;

}
