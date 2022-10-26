package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("L8443ServiceImpl")
@Repository
public class L8443ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> FindData(TitaVo titaVo, String table, String txDate) throws Exception {

		this.info("L8442FindData");
		this.info("txDate    = " + txDate);
		String sql = "Select count(*) as \"F0\" , ";
		//sql += " \"OutJcicTxtDate\" "+ -19110000 +" as \"F1\"  ";
		sql += " \"OutJcicTxtDate\"  as \"F1\" , ";
		sql += "  \"CustId\" as \"F2\" ";
		sql += " From \"" + table + "\" ";
		sql += " where  ";
		if (!"".equals(txDate)) {
			sql += " \"OutJcicTxtDate\"  not in " + (txDate) + " and ";
		}
		sql += "  \"OutJcicTxtDate\" != 0 ";
		sql += " group by \"OutJcicTxtDate\" , \"CustId\" ";

		this.info("FindDataL8440 sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
//		this.info("iJcicDate sql=" + iJcicDate);
//		query.setParameter("iJcicDate", iJcicDate);
		return this.convertToMap(query);

	}

	public List<Map<String, String>> Find(TitaVo titaVo, String table) throws Exception {

		this.info("L8442FindData");

		String sql = "Select ";
		sql += " \"JcicDate\" as \"F0\"  ";
		sql += " From \"JcicReFile\" ";
		sql += " where \"SubmitKey\" = " + table + " ";
		sql += "   group by \"JcicDate\"  ";

		this.info("FindDataL8440 sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
//		this.info("iJcicDate sql=" + iJcicDate);
//		query.setParameter("iJcicDate", iJcicDate);
		return this.convertToMap(query);

	}

}