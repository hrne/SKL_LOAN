package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdBaseRate;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdBaseRateId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdBaseRateService {

	/**
	 * findByPrimaryKey
	 *
	 * @param cdBaseRateId PK
	 * @param titaVo       Variable-Length Argument
	 * @return CdBaseRate CdBaseRate
	 */
	public CdBaseRate findById(CdBaseRateId cdBaseRateId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice CdBaseRate CdBaseRate of List
	 */
	public Slice<CdBaseRate> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * CurrencyCode = ,AND BaseRateCode = ,AND EffectDate &gt;= ,AND EffectDate
	 * &lt;=
	 *
	 * @param currencyCode_0 currencyCode_0
	 * @param baseRateCode_1 baseRateCode_1
	 * @param effectDate_2   effectDate_2
	 * @param effectDate_3   effectDate_3
	 * @param index          Page Index
	 * @param limit          Page Data Limit
	 * @param titaVo         Variable-Length Argument
	 * @return Slice CdBaseRate CdBaseRate of List
	 */
	public Slice<CdBaseRate> baseRateCodeEq(String currencyCode_0, String baseRateCode_1, int effectDate_2, int effectDate_3, int index, int limit, TitaVo... titaVo);

	/**
	 * CurrencyCode = ,AND BaseRateCode = ,AND EffectDate &gt;= ,AND EffectDate
	 * &lt;=
	 *
	 * @param currencyCode_0 currencyCode_0
	 * @param baseRateCode_1 baseRateCode_1
	 * @param effectDate_2   effectDate_2
	 * @param effectDate_3   effectDate_3
	 * @param titaVo         Variable-Length Argument
	 * @return Slice CdBaseRate CdBaseRate of List
	 */
	public CdBaseRate baseRateCodeDescFirst(String currencyCode_0, String baseRateCode_1, int effectDate_2, int effectDate_3, TitaVo... titaVo);

	/**
	 * CurrencyCode = ,AND BaseRateCode = ,AND EffectDate &gt;= ,AND EffectDate
	 * &lt;=
	 *
	 * @param currencyCode_0 currencyCode_0
	 * @param baseRateCode_1 baseRateCode_1
	 * @param effectDate_2   effectDate_2
	 * @param effectDate_3   effectDate_3
	 * @param titaVo         Variable-Length Argument
	 * @return Slice CdBaseRate CdBaseRate of List
	 */
	public CdBaseRate baseRateCodeAscFirst(String currencyCode_0, String baseRateCode_1, int effectDate_2, int effectDate_3, TitaVo... titaVo);

	/**
	 * CurrencyCode = ,AND BaseRateCode &gt;= ,AND BaseRateCode &lt;= ,AND
	 * EffectDate &gt;= ,AND EffectDate &lt;=
	 *
	 * @param currencyCode_0 currencyCode_0
	 * @param baseRateCode_1 baseRateCode_1
	 * @param baseRateCode_2 baseRateCode_2
	 * @param effectDate_3   effectDate_3
	 * @param effectDate_4   effectDate_4
	 * @param index          Page Index
	 * @param limit          Page Data Limit
	 * @param titaVo         Variable-Length Argument
	 * @return Slice CdBaseRate CdBaseRate of List
	 */
	public Slice<CdBaseRate> baseRateCodeRange(String currencyCode_0, String baseRateCode_1, String baseRateCode_2, int effectDate_3, int effectDate_4, int index, int limit, TitaVo... titaVo);

	/**
	 * CurrencyCode = ,AND BaseRateCode = ,AND EffectFlag = ,AND EffectDate &gt;=
	 *
	 * @param currencyCode_0 currencyCode_0
	 * @param baseRateCode_1 baseRateCode_1
	 * @param effectFlag_2   effectFlag_2
	 * @param effectDate_3   effectDate_3
	 * @param index          Page Index
	 * @param limit          Page Data Limit
	 * @param titaVo         Variable-Length Argument
	 * @return Slice CdBaseRate CdBaseRate of List
	 */
	public Slice<CdBaseRate> effectFlagEq(String currencyCode_0, String baseRateCode_1, int effectFlag_2, int effectDate_3, int index, int limit, TitaVo... titaVo);

	/**
	 * CurrencyCode = ,AND BaseRateCode = ,AND EffectFlag =
	 *
	 * @param currencyCode_0 currencyCode_0
	 * @param baseRateCode_1 baseRateCode_1
	 * @param effectFlag_2   effectFlag_2
	 * @param titaVo         Variable-Length Argument
	 * @return Slice CdBaseRate CdBaseRate of List
	 */
	public CdBaseRate effectFlagDescFirst(String currencyCode_0, String baseRateCode_1, int effectFlag_2, TitaVo... titaVo);

	/**
	 * CurrencyCode = ,AND BaseRateCode =
	 *
	 * @param currencyCode_0 currencyCode_0
	 * @param baseRateCode_1 baseRateCode_1
	 * @param index          Page Index
	 * @param limit          Page Data Limit
	 * @param titaVo         Variable-Length Argument
	 * @return Slice CdBaseRate CdBaseRate of List
	 */
	public Slice<CdBaseRate> baseRateCodeEq2(String currencyCode_0, String baseRateCode_1, int index, int limit, TitaVo... titaVo);

	/**
	 * CurrencyCode = ,AND BaseRateCode =
	 *
	 * @param currencyCode_0 currencyCode_0
	 * @param baseRateCode_1 baseRateCode_1
	 * @param index          Page Index
	 * @param limit          Page Data Limit
	 * @param titaVo         Variable-Length Argument
	 * @return Slice CdBaseRate CdBaseRate of List
	 */
	public Slice<CdBaseRate> effectFlagDescFirst1(String currencyCode_0, String baseRateCode_1, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By CdBaseRate
	 * 
	 * @param cdBaseRateId key
	 * @param titaVo       Variable-Length Argument
	 * @return CdBaseRate CdBaseRate
	 */
	public CdBaseRate holdById(CdBaseRateId cdBaseRateId, TitaVo... titaVo);

	/**
	 * hold By CdBaseRate
	 * 
	 * @param cdBaseRate key
	 * @param titaVo     Variable-Length Argument
	 * @return CdBaseRate CdBaseRate
	 */
	public CdBaseRate holdById(CdBaseRate cdBaseRate, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param cdBaseRate Entity
	 * @param titaVo     Variable-Length Argument
	 * @return CdBaseRate Entity
	 * @throws DBException exception
	 */
	public CdBaseRate insert(CdBaseRate cdBaseRate, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param cdBaseRate Entity
	 * @param titaVo     Variable-Length Argument
	 * @return CdBaseRate Entity
	 * @throws DBException exception
	 */
	public CdBaseRate update(CdBaseRate cdBaseRate, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param cdBaseRate Entity
	 * @param titaVo     Variable-Length Argument
	 * @return CdBaseRate Entity
	 * @throws DBException exception
	 */
	public CdBaseRate update2(CdBaseRate cdBaseRate, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param cdBaseRate Entity
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(CdBaseRate cdBaseRate, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param cdBaseRate Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<CdBaseRate> cdBaseRate, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param cdBaseRate Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<CdBaseRate> cdBaseRate, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param cdBaseRate Entity of List
	 * @param titaVo     Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<CdBaseRate> cdBaseRate, TitaVo... titaVo) throws DBException;

}
