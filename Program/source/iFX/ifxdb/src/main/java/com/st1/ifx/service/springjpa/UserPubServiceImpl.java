package com.st1.ifx.service.springjpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Assert;

import com.st1.ifx.domain.UserPubd;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.repository.UserPubReporitory;
import com.st1.ifx.service.UserPubService;

@Service("userPubService")
@Repository
@Transactional
public class UserPubServiceImpl implements UserPubService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(UserPubServiceImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private UserPubReporitory userpubRepository;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.assertNotNull(em);
	}

	@Override
	public void save(UserPubd userpub) {
		logger.info("Before touch.");
		userpub.touch();
		this.userpubRepository.save(userpub);
	}

	// 原本find的部分 直接改寫
	@Override
	public void updataSave(String tableName, String key, String strUserinfo) {
		logger.info("updataSave.");
		Query q = em.createQuery("update UserPubd c set c.userInfo = :struserinfo where c.tableName=:tablename and c.xey=:key").setParameter("tablename", tableName).setParameter("key", key)
				.setParameter("struserinfo", strUserinfo);
		int i = q.executeUpdate();
		logger.info("updataSave...done!!, update " + i + " records");
	}

	@Override
	public void updataSavedata(String tableName, String key, String brno, String level, String name, String httpSessionId, String scriptSessionId, String dapKnd, String oapKnd, String ovrToken,
			String locate, int lastJnlSeq, String strUserinfo) {
		logger.info("updataSavedata.");
		Query q = em
				.createQuery("update UserPubd c set " + "c.userInfo = :struserinfo " + "c.brno = :brno " + "c.lvel = :level " + "c.name = :name " + "c.httpSessionId = :httpSessionId "
						+ "c.scriptSessionId = :scriptSessionId " + "c.dapKnd = :dapKnd " + "c.oapKnd = :oapKnd " + "c.ovrToken = :ovrToken " + "c.locate = :locate " + "c.lastJnlSeq = :lastJnlSeq "
						+ "where c.tableName=:tablename and c.xey=:key")
				.setParameter("tablename", tableName).setParameter("key", key).setParameter("brno", brno).setParameter("level", level).setParameter("name", name)
				.setParameter("httpSessionId", httpSessionId).setParameter("scriptSessionId", scriptSessionId).setParameter("dapKnd", dapKnd).setParameter("oapKnd", oapKnd)
				.setParameter("ovrToken", ovrToken).setParameter("locate", locate).setParameter("lastJnlSeq", lastJnlSeq).setParameter("struserinfo", strUserinfo);
		int i = q.executeUpdate();
		logger.info("updataSavedata...done!!, update " + i + " records");
	}

	/**
	 * 取的指定USER
	 **/
	@Override
	@Transactional(readOnly = true)
	public UserPubd find(String tableName, String key) {
		logger.info(FilterUtils.escape("find by tableName:" + tableName + ", key:" + key));
		List listresult = em.createQuery("select c from UserPubd c where c.tableName=:tablename and c.xey=:key").setParameter("tablename", tableName).setParameter("key", key).getResultList();
		UserPubd result = (UserPubd) getSingleResultOrNull(listresult);
		// logger.info("after impl find.");
		// if (result != null) {
		// logger.info("result:" + FilterUtils.escape(result.toString()));
		// }
		// UserPubd result2 = this.userpubRepository.findByTableNameAndKey(tableName,
		// key);
		// if(result2!=null){
		// logger.info("result2:"+result2.toString());
		// }
		return result;
	}

	/**
	 * 取的指定 scriptSessionId
	 **/
	@Override
	@Transactional(readOnly = true)
	public UserPubd findscriptSessionId(String tableName, String scriptSessionid) {
		logger.info(FilterUtils.escape("findscriptSessionId by tableName:" + tableName + ", scriptSessionid:" + scriptSessionid));

		return this.userpubRepository.findByTableNameAndScriptSessionId(tableName, scriptSessionid);

		/*
		 * List listresult = em .createQuery(
		 * "select c from UserPubd c where c.tableName=:tablename and c.scriptSessionid=:scriptSessionid"
		 * ) .setParameter("tablename", tableName) .setParameter("scriptSessionid",
		 * scriptSessionid).getResultList(); UserPubd result =
		 * getSingleResultOrNull(listresult); logger.info("after impl find.");
		 * if(result!=null){ logger.info("result:"+result.toString()); } //UserPubd
		 * result2 = this.userpubRepository.findByTableNameAndKey(tableName, key);
		 * //if(result2!=null){ // logger.info("result2:"+result2.toString()); //}
		 * return result;
		 */
	}

	/**
	 * 取的指定 httpSessionId
	 **/
	@Override
	@Transactional(readOnly = true)
	public UserPubd findhttpSessionId(String tableName, String httpSessionId) {
		logger.info("findhttpSessionId by tableName:" + tableName + ", httpSessionId:" + httpSessionId);
		List listresult = em.createQuery("select c from UserPubd c where c.tableName=:tablename and c.httpSessionId=:httpSessionId").setParameter("tablename", tableName)
				.setParameter("httpSessionId", httpSessionId).getResultList();
		UserPubd result = (UserPubd) getSingleResultOrNull(listresult);
		// logger.info("after impl find.");
		// if (result != null) {
		// logger.info("result:" + FilterUtils.escape(result.toString()));
		// }
		// UserPubd result2 = this.userpubRepository.findByTableNameAndKey(tableName,
		// key);
		// if(result2!=null){
		// logger.info("result2:"+result2.toString());
		// }
		return result;
	}

	/**
	 * 取的指定 get Supervisor
	 **/
	@Override
	@Transactional(readOnly = true)
	public List<UserPubd> findSupervisor(String tableName, String brno, String cldept) {
		if (cldept != null)
			cldept = "%" + cldept + "%";
		logger.info("findSupervisor by tableName:" + tableName + ", brno:" + brno, ", cldept:" + cldept);
//		List listresult = em.createQuery("select c from UserPubd c where c.tableName=:tablename and c.brno=:brno and c.lvel in('1','2') and c.cldept like :cldept").setParameter("tablename", tableName)
//				.setParameter("brno", brno).setParameter("cldept", cldept).getResultList();
		List listresult = em.createQuery("select c from UserPubd c where c.tableName=:tablename and c.brno=:brno and c.lvel in('1','2')").setParameter("tablename", tableName)
				.setParameter("brno", brno).getResultList();
		return listresult;
	}

	/**
	 * 取的指定 get Brno User
	 **/
	@Override
	@Transactional(readOnly = true)
	public List<UserPubd> findBranchUsers(String tableName, String brno) {
		logger.info("findBranchUsers by tableName:" + tableName + ", brno:" + brno);
		List listresult = em.createQuery("select c from UserPubd c where c.tableName=:tablename and c.brno=:brno").setParameter("tablename", tableName).setParameter("brno", brno).getResultList();

		return listresult;
	}

	/**
	 * 取的指定 get ALL User
	 **/
	@Override
	@Transactional(readOnly = true)
	public List<UserPubd> findAllUsers(String tableName) {
		logger.info("findAllUsers by tableName:" + tableName);
		List listresult = em.createQuery("select c from UserPubd c where c.tableName=:tablename").setParameter("tablename", tableName).getResultList();

		return listresult;
	}

	/**
	 * 取的全部USER
	 **/
	@Override
	@Transactional(readOnly = true)
	public List<UserPubd> findAll(String tableName) {
		// logger.info("findAll by tableName:" + tableName);
		List result = em.createQuery("select c from UserPubd c where c.tableName=:tablename").setParameter("tablename", tableName).getResultList();
		// logger.info("result size:" + result.size());
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Long findAllcount(String tableName) {
		logger.info("findAllcount by tableName:" + tableName);
		return this.userpubRepository.getTableNameCounts(tableName);
	}

	@Override
	public void removeKey(String tableName, String key) {
		logger.info(FilterUtils.escape("remove by tableName:" + tableName + ", key:" + key));
		Query q = em.createQuery("delete from UserPubd c where c.tableName=:tablename and c.xey=:key");
		q.setParameter("tablename", tableName);
		q.setParameter("key", key);
		int i = q.executeUpdate();
		logger.info("remove...done!!, remove " + i + " records");
	}

	/**
	 * 取的指定USER
	 **/
	@Override
	@Transactional(readOnly = true)
	public boolean containsKey(String tableName, String key) {
		logger.info(FilterUtils.escape("contains by tableName:" + tableName + ", key:" + key));
		// UserPubd result = this.userpubRepository.findByTableNameAndKey(tableName,
		// key);
		List listresult = em.createQuery("select c from UserPubd c where c.tableName=:tablename and c.xey=:key").setParameter("tablename", tableName).setParameter("key", key).getResultList();
		UserPubd result = (UserPubd) getSingleResultOrNull(listresult);
		if (result != null) {
			logger.info("UserPub containsKey true!");
			return true;
		}
		logger.info("UserPub containsKey false!");
		return false;
	}

	@Override
	public void removeKeyDo(String tableName, String key) {
		logger.info(FilterUtils.escape("removeKeyDo! by tableName:" + tableName + ", key:" + key));
		// UserPubd result = this.userpubRepository.findByTableNameAndKey(tableName,
		// key);
		List listresult = em.createQuery("select c from UserPubd c where c.tableName=:tablename and c.xey=:key").setParameter("tablename", tableName).setParameter("key", key).getResultList();
		// UserPubd result = (UserPubd) getSingleResultOrNull(listresult);//潘
		if (listresult != null) {
			removeKey(tableName, key);
		}
	}

	public static <PP> PP getSingleResultOrNull(List<PP> results) {
		PP foundEntity = null;
		if (!results.isEmpty()) {
			foundEntity = results.get(0);
		}
		/*
		 * 潘 if (results.size() > 1) { for (PP result : results) { if (result !=
		 * foundEntity) { throw new NonUniqueResultException(); } } }
		 */
		return foundEntity;
	}
}
