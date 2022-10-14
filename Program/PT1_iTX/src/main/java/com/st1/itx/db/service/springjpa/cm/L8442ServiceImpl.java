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

@Service("l8442ServiceImpl")
@Repository
public class L8442ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

	this.info("L8442FindData");
	
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"))+19110000;
		this.info("iJcicDate     = " + iJcicDate);

			
		String sql = "Select \"SubmitKey\" as \"F0\" , "; 
		sql += " \"JcicDate\" as \"F1\" , ";
		sql += " \"ReportTotal\" as \"F2\" , ";
		sql += " \"CorrectCount\" as \"F3\" , ";
		sql += " \"MistakeCount\" as \"F4\" ";
		sql += " from \"JcicReFile\" ";
		sql += " where \"JcicDate\"  = " + iJcicDate + " ";
		sql += "   and \"SubmitKey\" =  " + table + " ";

		

		this.info("FindDataL8440 sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
//		this.info("iJcicDate sql=" + iJcicDate);
//		query.setParameter("iJcicDate", iJcicDate);
		return this.convertToMap(query);

	}

}