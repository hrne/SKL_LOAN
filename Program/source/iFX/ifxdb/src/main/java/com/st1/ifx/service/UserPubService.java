package com.st1.ifx.service;

import java.util.List;

import com.st1.ifx.domain.UserPubd;

public interface UserPubService {
	public void save(UserPubd userPub);

	public void updataSave(String tableName, String key, String strUserinfo);

	public List<UserPubd> findAll(String tableName);

	public UserPubd find(String tableName, String key);

	public void removeKey(String tableName, String key);

	public boolean containsKey(String tableName, String key);

	public void removeKeyDo(String string, String key);

	UserPubd findscriptSessionId(String tableName, String scriptSessionid);

	UserPubd findhttpSessionId(String tableName, String httpSessionId);

	List<UserPubd> findSupervisor(String tableName, String brno, String cldept);

	List<UserPubd> findBranchUsers(String tableName, String brno);

	List<UserPubd> findAllUsers(String tableName);

	void updataSavedata(String tableName, String key, String brno, String level, String name, String httpSessionId,
			String scriptSessionId, String dapKnd, String oapKnd, String ovrToken, String locate, int lastJnlSeq,
			String strUserinfo);

	Long findAllcount(String tableName);

}
