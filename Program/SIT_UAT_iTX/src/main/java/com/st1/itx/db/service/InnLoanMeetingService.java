package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.InnLoanMeeting;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface InnLoanMeetingService {

	/**
	 * findByPrimaryKey
	 *
	 * @param meetNo PK
	 * @param titaVo Variable-Length Argument
	 * @return InnLoanMeeting InnLoanMeeting
	 */
	public InnLoanMeeting findById(int meetNo, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice InnLoanMeeting InnLoanMeeting of List
	 */
	public Slice<InnLoanMeeting> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * MeetingDate &gt;= ,AND MeetingDate &lt;=
	 *
	 * @param meetingDate_0 meetingDate_0
	 * @param meetingDate_1 meetingDate_1
	 * @param index         Page Index
	 * @param limit         Page Data Limit
	 * @param titaVo        Variable-Length Argument
	 * @return Slice InnLoanMeeting InnLoanMeeting of List
	 */
	public Slice<InnLoanMeeting> meetingDateRg(int meetingDate_0, int meetingDate_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By InnLoanMeeting
	 * 
	 * @param meetNo key
	 * @param titaVo Variable-Length Argument
	 * @return InnLoanMeeting InnLoanMeeting
	 */
	public InnLoanMeeting holdById(int meetNo, TitaVo... titaVo);

	/**
	 * hold By InnLoanMeeting
	 * 
	 * @param innLoanMeeting key
	 * @param titaVo         Variable-Length Argument
	 * @return InnLoanMeeting InnLoanMeeting
	 */
	public InnLoanMeeting holdById(InnLoanMeeting innLoanMeeting, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param innLoanMeeting Entity
	 * @param titaVo         Variable-Length Argument
	 * @return InnLoanMeeting Entity
	 * @throws DBException exception
	 */
	public InnLoanMeeting insert(InnLoanMeeting innLoanMeeting, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param innLoanMeeting Entity
	 * @param titaVo         Variable-Length Argument
	 * @return InnLoanMeeting Entity
	 * @throws DBException exception
	 */
	public InnLoanMeeting update(InnLoanMeeting innLoanMeeting, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param innLoanMeeting Entity
	 * @param titaVo         Variable-Length Argument
	 * @return InnLoanMeeting Entity
	 * @throws DBException exception
	 */
	public InnLoanMeeting update2(InnLoanMeeting innLoanMeeting, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param innLoanMeeting Entity
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(InnLoanMeeting innLoanMeeting, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param innLoanMeeting Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<InnLoanMeeting> innLoanMeeting, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param innLoanMeeting Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<InnLoanMeeting> innLoanMeeting, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param innLoanMeeting Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<InnLoanMeeting> innLoanMeeting, TitaVo... titaVo) throws DBException;

}
