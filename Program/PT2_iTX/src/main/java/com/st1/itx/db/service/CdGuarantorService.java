package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdGuarantor;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdGuarantorService {

	/**
	 * findByPrimaryKey
	 *
	 * @param guaRelCode PK
	 * @param titaVo     Variable-Length Argument
	 * @return CdGuarantor CdGuarantor
	 */
	public CdGuarantor findById(String guaRelCode, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CdGuarantor CdGuarantor of List
	 */
	public Slice<CdGuarantor> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * GuaRelJcic &gt;= ,AND GuaRelJcic &lt;=
	 *
	 * @param guaRelJcic_0 guaRelJcic_0
	 * @param guaRelJcic_1 guaRelJcic_1
	 * @param titaVo       Variable-Length Argument
	 * @return Slice CdGuarantor CdGuarantor of List
	 */
	public CdGuarantor guaRelJcicFirst(String guaRelJcic_0, String guaRelJcic_1, TitaVo... titaVo);

	/**
	 * GuaRelCode &gt;= ,AND GuaRelCode &lt;=
	 *
	 * @param guaRelCode_0 guaRelCode_0
	 * @param guaRelCode_1 guaRelCode_1
	 * @param index        Page Index
	 * @param limit        Page Data Limit
	 * @param titaVo       Variable-Length Argument
	 * @return Slice CdGuarantor CdGuarantor of List
	 */
	public Slice<CdGuarantor> findGuaRelCode(String guaRelCode_0, String guaRelCode_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By CdGuarantor
	 * 
	 * @param guaRelCode key
	 * @param titaVo     Variable-Length Argument
	 * @return CdGuarantor CdGuarantor
	 */
	public CdGuarantor holdById(String guaRelCode, TitaVo... titaVo);

	/**
	 * hold By CdGuarantor
	 * 
	 * @param cdGuarantor key
	 * @param titaVo      Variable-Length Argument
	 * @return CdGuarantor CdGuarantor
	 */
	public CdGuarantor holdById(CdGuarantor cdGuarantor, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param cdGuarantor Entity
	 * @param titaVo      Variable-Length Argument
	 * @return CdGuarantor Entity
	 * @throws DBException exception
	 */
	public CdGuarantor insert(CdGuarantor cdGuarantor, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param cdGuarantor Entity
	 * @param titaVo      Variable-Length Argument
	 * @return CdGuarantor Entity
	 * @throws DBException exception
	 */
	public CdGuarantor update(CdGuarantor cdGuarantor, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param cdGuarantor Entity
	 * @param titaVo      Variable-Length Argument
	 * @return CdGuarantor Entity
	 * @throws DBException exception
	 */
	public CdGuarantor update2(CdGuarantor cdGuarantor, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param cdGuarantor Entity
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(CdGuarantor cdGuarantor, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param cdGuarantor Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<CdGuarantor> cdGuarantor, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param cdGuarantor Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<CdGuarantor> cdGuarantor, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param cdGuarantor Entity of List
	 * @param titaVo      Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<CdGuarantor> cdGuarantor, TitaVo... titaVo) throws DBException;

}
