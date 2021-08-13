package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.PfRewardMedia;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfRewardMediaService {

	/**
	 * findByPrimaryKey
	 *
	 * @param bonusNo PK
	 * @param titaVo  Variable-Length Argument
	 * @return PfRewardMedia PfRewardMedia
	 */
	public PfRewardMedia findById(Long bonusNo, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice PfRewardMedia PfRewardMedia of List
	 */
	public Slice<PfRewardMedia> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * WorkMonth = ,AND BonusType ^i ,AND MediaFg =
	 *
	 * @param workMonth_0 workMonth_0
	 * @param bonusType_1 bonusType_1
	 * @param mediaFg_2   mediaFg_2
	 * @param index       Page Index
	 * @param limit       Page Data Limit
	 * @param titaVo      Variable-Length Argument
	 * @return Slice PfRewardMedia PfRewardMedia of List
	 */
	public Slice<PfRewardMedia> findWorkMonth(int workMonth_0, List<Integer> bonusType_1, int mediaFg_2, int index, int limit, TitaVo... titaVo);

	/**
	 * hold By PfRewardMedia
	 * 
	 * @param bonusNo key
	 * @param titaVo  Variable-Length Argument
	 * @return PfRewardMedia PfRewardMedia
	 */
	public PfRewardMedia holdById(Long bonusNo, TitaVo... titaVo);

	/**
	 * hold By PfRewardMedia
	 * 
	 * @param pfRewardMedia key
	 * @param titaVo        Variable-Length Argument
	 * @return PfRewardMedia PfRewardMedia
	 */
	public PfRewardMedia holdById(PfRewardMedia pfRewardMedia, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param pfRewardMedia Entity
	 * @param titaVo        Variable-Length Argument
	 * @return PfRewardMedia Entity
	 * @throws DBException exception
	 */
	public PfRewardMedia insert(PfRewardMedia pfRewardMedia, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param pfRewardMedia Entity
	 * @param titaVo        Variable-Length Argument
	 * @return PfRewardMedia Entity
	 * @throws DBException exception
	 */
	public PfRewardMedia update(PfRewardMedia pfRewardMedia, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param pfRewardMedia Entity
	 * @param titaVo        Variable-Length Argument
	 * @return PfRewardMedia Entity
	 * @throws DBException exception
	 */
	public PfRewardMedia update2(PfRewardMedia pfRewardMedia, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param pfRewardMedia Entity
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(PfRewardMedia pfRewardMedia, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param pfRewardMedia Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<PfRewardMedia> pfRewardMedia, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param pfRewardMedia Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<PfRewardMedia> pfRewardMedia, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param pfRewardMedia Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<PfRewardMedia> pfRewardMedia, TitaVo... titaVo) throws DBException;

}
