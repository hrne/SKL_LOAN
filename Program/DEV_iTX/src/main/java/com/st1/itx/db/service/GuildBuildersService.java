package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.GuildBuilders;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface GuildBuildersService {

	/**
	 * findByPrimaryKey
	 *
	 * @param custNo PK
	 * @param titaVo Variable-Length Argument
	 * @return GuildBuilders GuildBuilders
	 */
	public GuildBuilders findById(int custNo, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice GuildBuilders GuildBuilders of List
	 */
	public Slice<GuildBuilders> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By GuildBuilders
	 * 
	 * @param custNo key
	 * @param titaVo Variable-Length Argument
	 * @return GuildBuilders GuildBuilders
	 */
	public GuildBuilders holdById(int custNo, TitaVo... titaVo);

	/**
	 * hold By GuildBuilders
	 * 
	 * @param guildBuilders key
	 * @param titaVo        Variable-Length Argument
	 * @return GuildBuilders GuildBuilders
	 */
	public GuildBuilders holdById(GuildBuilders guildBuilders, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param guildBuilders Entity
	 * @param titaVo        Variable-Length Argument
	 * @return GuildBuilders Entity
	 * @throws DBException exception
	 */
	public GuildBuilders insert(GuildBuilders guildBuilders, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param guildBuilders Entity
	 * @param titaVo        Variable-Length Argument
	 * @return GuildBuilders Entity
	 * @throws DBException exception
	 */
	public GuildBuilders update(GuildBuilders guildBuilders, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param guildBuilders Entity
	 * @param titaVo        Variable-Length Argument
	 * @return GuildBuilders Entity
	 * @throws DBException exception
	 */
	public GuildBuilders update2(GuildBuilders guildBuilders, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param guildBuilders Entity
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(GuildBuilders guildBuilders, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param guildBuilders Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<GuildBuilders> guildBuilders, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param guildBuilders Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<GuildBuilders> guildBuilders, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param guildBuilders Entity of List
	 * @param titaVo        Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<GuildBuilders> guildBuilders, TitaVo... titaVo) throws DBException;

}
