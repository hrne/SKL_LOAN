package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

@Service("lC900ServiceImpl")
@Repository

public class LC900ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> findAll() throws Exception {

		String sql = "(select \"Code\" f1,\"Code\" f2,\"Item\" f3,1 f4,0 f5 from \"CdCode\" where \"DefCode\" like 'Menu%'\r\n" + "union all\r\n"
				+ "select concat(substr(\"DefCode\",8,2),\"Code\") f1,concat(substr(\"DefCode\",8,2),\"Code\") f2,\"Item\" f3,1 f4,0 f5 from \"CdCode\" where \"DefCode\" like 'SubMenu%'\r\n"
				+ "union all\r\n" + "select concat(\"MenuNo\",\"SubMenuNo\") f1,\"TranNo\" f2,\"TranItem\" f3,\"MenuFg\" f4,2 f5 from \"TxTranCode\"\r\n" + ") order by f1,f5,f2";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		// 設定參數
//		query.setParameter("defno", defno);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAuthNo(String authNo) throws Exception {

		String sql = "(";
		sql += "select \"Code\" f1,\"Code\" f2,\"Item\" f3,1 f4,0 f5 ";
		sql += "from \"CdCode\" ";
		sql += "where \"DefCode\" like 'Menu%' and \"Code\" like 'L%' ";
		sql += "union all ";
		sql += "select concat(substr(\"DefCode\",8,2),\"Code\") f1,concat(substr(\"DefCode\",8,2),\"Code\") f2,\"Item\" f3,1 f4,0 f5 ";
		sql += "from \"CdCode\" ";
		sql += "where \"DefCode\" like 'SubMenu%' ";
		sql += "union all ";
		sql += "select concat(\"MenuNo\",\"SubMenuNo\") f1,\"TranNo\" f2,\"TranItem\" f3,\"MenuFg\" f4,2 f5 ";
		sql += "from \"TxTranCode\" ";
		sql += "where \"TranNo\" in (select \"TranNo\" from \"TxAuthority\" where \"AuthNo\" = :AuthNo) and \"MenuFg\" = 1 ";
		sql += ")";
		sql += "order by f1,f5,f2";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		query.setParameter("AuthNo", authNo);

		// 設定參數
//		query.setParameter("defno", defno);

		return this.convertToMap(query);
	}
}
