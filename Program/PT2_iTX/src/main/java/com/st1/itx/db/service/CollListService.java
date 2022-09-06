package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CollList;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CollListId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CollListService {

	/**
	 * findByPrimaryKey
	 *
	 * @param collListId PK
	 * @param titaVo     Variable-Length Argument
	 * @return CollList CollList
	 */
	public CollList findById(CollListId collListId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CollList CollList of List
	 */
	public Slice<CollList> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * ClCustNo=, AND ClFacmNo=
	 *
	 * @param clCustNo_0 clCustNo_0
	 * @param clFacmNo_1 clFacmNo_1
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CollList CollList of List
	 */
	public Slice<CollList> findCl(int clCustNo_0, int clFacmNo_1, int index, int limit, TitaVo... titaVo);

	/**
	 * CustNo &gt;= ,AND CustNo &lt;= ,AND FacmNo &gt;= ,AND FacmNo &lt;= ,AND
	 * Status ^i
	 *
	 * @param custNo_0 custNo_0
	 * @param custNo_1 custNo_1
	 * @param facmNo_2 facmNo_2
	 * @param facmNo_3 facmNo_3
	 * @param status_4 status_4
	 * @param index    Page Index
	 * @param limit    Page Data Limit
	 * @param titaVo   Variable-Length Argument
	 * @return Slice CollList CollList of List
	 */
	public Slice<CollList> statusRng(int custNo_0, int custNo_1, int facmNo_2, int facmNo_3, List<Integer> status_4, int index, int limit, TitaVo... titaVo);

	/**
	 * OvduDays &gt;= ,AND OvduDays &lt;=
	 *
	 * @param ovduDays_0 ovduDays_0
	 * @param ovduDays_1 ovduDays_1
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CollList CollList of List
	 */
	public Slice<CollList> ovduDaysRange(int ovduDays_0, int ovduDays_1, int index, int limit, TitaVo... titaVo);

	/**
	 * CityCode=
	 *
	 * @param cityCode_0 cityCode_0
	 * @param index      Page Index
	 * @param limit      Page Data Limit
	 * @param titaVo     Variable-Length Argument
	 * @return Slice CollList CollList of List
	 */
	public Slice<CollList> findCityCode(String cityCode_0, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By CollList
	 * 
	 * @param collListId key
	 * @param titaVo     Variable-Length Argument
	 * @return CollList CollList
	 */
	public CollList holdById(CollListId collListId, TitaVo... titaVo);

	/**
	 * hold By CollList
	 * 
	 * @param collList key
	 * @param titaVo   Variable-Length Argument
	 * @return CollList CollList
	 */
	public CollList holdById(CollList collList, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param collList Entity
	 * @param titaVo   Variable-Length Argument
	 * @return CollList Entity
	 * @throws DBException exception
	 */
	public CollList insert(CollList collList, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param collList Entity
	 * @param titaVo   Variable-Length Argument
	 * @return CollList Entity
	 * @throws DBException exception
	 */
	public CollList update(CollList collList, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param collList Entity
	 * @param titaVo   Variable-Length Argument
	 * @return CollList Entity
	 * @throws DBException exception
	 */
	public CollList update2(CollList collList, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param collList Entity
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(CollList collList, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param collList Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<CollList> collList, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param collList Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<CollList> collList, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param collList Entity of List
	 * @param titaVo   Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<CollList> collList, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * (日終批次)維護 CollList 法催紀錄清單檔
	 * 
	 * @param tbsdyf int
	 * @param empNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L5_CollList_Upd(int tbsdyf, String empNo, TitaVo... titaVo);

}
