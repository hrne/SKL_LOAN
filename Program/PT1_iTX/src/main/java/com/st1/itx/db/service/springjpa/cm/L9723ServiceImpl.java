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

@Service
@Repository
public class L9723ServiceImpl extends ASpringJpaParm implements InitializingBean {
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
	public List<Map<String, String>> FindData(TitaVo titaVo) throws Exception {
		this.info("L9723FindData");
		
//		int iJcicDate = (Integer.valueOf(titaVo.getParam("ReportDate"))+19110000)/100;
		int iJcicDate = ((Integer.valueOf(titaVo.getParam("ReportDateY"))+1911)*100)+Integer.valueOf(titaVo.getParam("ReportDateM"));
		
		this.info("iJcicDate     = " + iJcicDate);

		String sql = "select count(*) AS \"count\", ";
		sql += "           \"YearMonth\" as \"YearMonth\" ";
		sql += "            from (SELECT \"CustNo\", ";
		sql += "             \"YearMonth\"  ";
		sql += "              FROM \"MonthlyFacBal\" ";
		sql += "              WHERE \"YearMonth\" = " + iJcicDate + " ";
		sql += "                AND \"PrinBalance\" > 0 ";
		sql += "           GROUP BY \"CustNo\",\"YearMonth\" ";
		sql += "           )";
		sql += "   GROUP BY \"YearMonth\" ";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);

		
	}

}