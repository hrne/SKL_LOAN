package com.st1.ifx.service.springjpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Assert;

import com.st1.ifx.domain.LocalRim;
import com.st1.ifx.domain.LocalRim_;
import com.st1.ifx.file.item.rim.RimLine;
import com.st1.ifx.repository.LocalRimRepository;
import com.st1.ifx.service.LocalRimService;

@Service("localRimService")
@Repository
@Transactional
public class LocalRimServiceImpl implements LocalRimService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LocalRimServiceImpl.class);

	@Autowired
	private LocalRimRepository repository;

	@PersistenceContext
	private EntityManager em;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.assertNotNull(em);
		Assert.assertNotNull(repository);
	}

	@Override
	public LocalRim save(LocalRim localRim) {

		return this.repository.save(localRim);
	}

	@Override
	public void removeTable(String tableName) {
		// //logger.info("remove all LocalRim:" + tableName);
		Query q = em.createQuery("delete from LocalRim c where c.tableName=:tableName");
		q.setParameter("tableName", tableName);
		int i = q.executeUpdate();
		logger.info("remove...done!!, remove " + i + " records");

	}

	@Override
	public void removeKey(String tableName, String key) {
		// //logger.info("remove all LocalRim:" + tableName + ", key:" + key);
		Query q = em.createQuery("delete from LocalRim c where c.tableName=:tableName and c.xey=:key");
		q.setParameter("tableName", tableName);
		q.setParameter("key", key);
		int i = q.executeUpdate();
		// //logger.info("remove...done!!, remove " + i + " records");

	}

	// 先直接update再insert 提升匯率效率
	public void insertupdate(String tableName, String key, String data, LocalRim localRim) {

		try {
			Query q = em.createQuery("update LocalRim c set c.data=:data where c.tableName=:tableName and c.xey=:key");
			q.setParameter("data", data);
			q.setParameter("tableName", tableName);
			q.setParameter("key", key);
			int i = q.executeUpdate();
			logger.info("update...done!!,tableName:" + tableName + ",key:" + key + ",update " + i + " records");
			// 沒有update到資料=新增
			if (i == 0) {
				logger.info("insert -> save :" + localRim.toString());
				this.save(localRim);
			}
		} catch (Exception e) {
			logger.error("e :" + e.getMessage());
		}

		// //此方法未測試，因db2權限不夠
		// em.merge(localRim);

	}

	@Override
	@Transactional(readOnly = true)
	// @Cacheable("localRim")
	public LocalRim find(String tableName, String key) {
		return this.repository.findByTableNameAndXey(tableName, key);
	}

	@Override
	@Transactional(readOnly = true)
	public void findtablealltoKey(String tableName, String tokey, String recvTime) {
		// 要排序?
		Query query = em.createQuery("SELECT c FROM LocalRim " + "c WHERE  c.tableName=:table");
		query.setParameter("table", tableName);
		String data = "";
		removeKey(tableName, tokey);// 移除重複資料
		LocalRim r = new LocalRim();// 新建
		r.setTableName(tableName);
		r.setXey(tokey);

		List<LocalRim> list = query.getResultList();
		for (LocalRim j : list) {
			data += j.getData();
		}

		data = recvTime + data;
		if (data.endsWith("$")) {
			r.setData(data.substring(0, data.length() - 1));
		} else {
			r.setData(data);
		}
		// //logger.info("rrrr:"+r.getTableName());
		// //logger.info("rrrr:"+r.getKey());
		// //logger.info("rrrr:"+r.getData());
		this.save(r);
	}

	@Override
	// @CacheEvict(value = "localRim", allEntries = true)
	public void evict() {
		// //logger.info("evict localRim");
	}

	@Override
	public void updateList(List<RimLine> list) {
		// //logger.info("updateList size:"+list.size());
		for (RimLine rimLine : list) {
			// //logger.info(rimLine.toString());
			updateByFlag(rimLine);
		}
		// 柯不知加在哪裡較合適
		evict();
	}

	@Override
	public void updateByFlag(RimLine r) {
		if (r.isDeleteTable()) {
			this.removeTable(r.getTableName());
		} else if (r.isDeleteRecord()) {
			this.removeKey(r.getTableName(), r.getKey());
		} else if (r.isinsertupdate()) {
			this.insertupdate(r.getTableName(), r.getKey(), r.getData(), r.toLocalRim());
		} else {
			this.save(r.toLocalRim());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<LocalRim> findAll(String tableName) {
		return this.repository.findByTableName(tableName);
	}

	private boolean isNotEmpty(String s) {
		return s != null && s.trim().length() > 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List findLike1(String tableName, String matchKey) {

		Query query = em.createQuery("SELECT c FROM LocalRim " + "c WHERE  c.tableName=:table AND "
				+ " (c.xey LIKE :keyword OR c.data like :keyword)" + " ORDER BY c.xey");
		query.setParameter("table", tableName);
		query.setParameter("keyword", "%" + matchKey.toUpperCase() + "%");
		return query.getResultList();

	}

	@Override
	@Transactional(readOnly = true)
	// @Cacheable("localRim")
	public List<LocalRim> findLike2(String tableName, String matchKey) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<LocalRim> criteriaQuery = cb.createQuery(LocalRim.class);

		Root<LocalRim> root = criteriaQuery.from(LocalRim.class);
		criteriaQuery.select(root);

		Predicate criteria = cb.conjunction();

		String[] ss = matchKey.split(",");
		List<Predicate> pList = new ArrayList<Predicate>();
		for (String m : ss) {
			m = "%" + m.trim().toUpperCase() + "%";
			pList.add(cb.like(root.get(LocalRim_.xey), m));
			pList.add(cb.like(root.get(LocalRim_.data), m));
			// Predicate p1 = cb.like(root.get(LocalRim_.key), m);
			// Predicate p2 = cb.like(root.get(LocalRim_.data), m);
			// Predicate p = cb.or(p1,p2);
			// criteria = cb.and(p);

		}
		Predicate[] pArray = new Predicate[0];
		pArray = pList.toArray(pArray);
		Predicate pLikeList = cb.or(pArray);
		Predicate pTableName = cb.equal(root.get(LocalRim_.tableName), tableName);
		Predicate pAll = cb.and(pTableName, pLikeList);
		criteriaQuery.where(cb.and(pAll));

		criteriaQuery.orderBy(cb.asc(root.get(LocalRim_.xey)));

		return em.createQuery(criteriaQuery).getResultList();

	}

	@Override
	// @CacheEvict(value="localRim")
	public LocalRim findnocache(String tableName, String key) {
		return this.repository.findByTableNameAndXey(tableName, key);
	}

};
