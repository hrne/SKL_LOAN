package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

@Service("tableColumnServiceImpl")
@Repository

public class TableColumnServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(TableColumnServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);

	}

//	public List<TableColumnVo> findAll2(String tablename) throws Exception {
//		logger.info("TableColumnServiceImpl.findAll2 tablename = " + tablename);
//
//		String sql = "select NEW com.st1.itx.util.common.data.TableColumnVo ";
//		sql += "(tABLE_NAME, cOLUMN_NAME, cOMMENTS) ";
////		sql += "(tABLE_NAME, cOLUMN_NAME) ";
//		sql += "FROM SYS.USER_COL_COMMENTS ";
//		sql += "WHERE TABLE_NAME = :tablename ";
//		sql += "AND COMMENTS IS NOT NULL ";
//		logger.info("sql=" + sql);
//		Query query;
//		logger.info("TableColumnServiceImpl.findAll 1");
//		query = em.createQuery(sql, TableColumnVo.class);
//		logger.info("TableColumnServiceImpl.findAll 2");
//		query.setParameter("tablename", tablename);
//
//		logger.info("TableColumnServiceImpl.findAll 3");
//
//		return query.getResultList();
//	}

	public List findAll(String tablename) throws Exception {
		logger.info("TableColumnServiceImpl.findAll tablename = " + tablename);

		String sql = "SELECT TABLE_NAME, COLUMN_NAME, COMMENTS ";
		sql += "FROM SYS.USER_COL_COMMENTS ";
		sql += "WHERE TABLE_NAME = :tablename ";
		sql += "AND COMMENTS IS NOT NULL ";
		logger.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		logger.info("TableColumnServiceImpl.findAll 1");
		query = em.createNativeQuery(sql);
		logger.info("TableColumnServiceImpl.findAll 2");
		query.setParameter("tablename", tablename);

		logger.info("TableColumnServiceImpl.findAll 3");

		return this.convertToMap(query.getResultList());
	}

}
