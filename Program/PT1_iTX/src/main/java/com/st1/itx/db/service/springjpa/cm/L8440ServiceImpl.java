package com.st1.itx.db.service.springjpa.cm;

import java.util.ArrayList;
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

@Service("l8440ServiceImpl")
@Repository
public class L8440ServiceImpl extends ASpringJpaParm implements InitializingBean {

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
	// 今日轉出有JCIC日期
	public List<Map<String, String>> FindData(TitaVo titaVo, String table) throws Exception {

	this.info("L8440FindData");
	
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"))+19110000;
		this.info("iJcicDate     = " + iJcicDate);

			
		String sql = " select A.\"F0\",B.\"F1\" from "; 
		sql += " ( SELECT  \"F0\"  as \"F0\" ";
		sql += " ,"+ iJcicDate +" AS \"F3\" ";
		sql += " FROM  ";
		sql += " ( select  count(*) AS \"F0\" ";
		sql += " FROM \"" + table + "\" ";
		sql += " WHERE \"OutJcicTxtDate\" = " + iJcicDate + ") ) A ";
		sql += " left join (";
		sql += " SELECT  \"F0\"  as \"F1\" ";
		sql += " ,"+ iJcicDate +" AS \"F3\" ";
		sql += " FROM  ";
		sql += " ( select  count(*) AS \"F0\" ";
		sql += " FROM \"" + table + "\" ";
		sql += " where \"OutJcicTxtDate\" = 0) ) B  ";
		sql += " on A.\"F3\" = B.\"F3\" ";
		
		this.info("FindDataL8440 sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
//		this.info("iJcicDate sql=" + iJcicDate);
//		query.setParameter("iJcicDate", iJcicDate);
		return this.convertToMap(query);

	}

}