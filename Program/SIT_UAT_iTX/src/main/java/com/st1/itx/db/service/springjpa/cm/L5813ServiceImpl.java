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

@Service("l5813ServiceImpl")
@Repository
/* AML每日有效客戶名單 */
public class L5813ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> checkAll(String iYear, TitaVo titaVo) throws Exception {

		String sql = "　";
		int iYYYYMM = Integer.parseInt(iYear + 12) + 191100;

		
		this.info("iYYYYMM=="+iYYYYMM);

		
		
		sql += "select \r\n";
		sql +="	\"CustNo\"\r\n" ; 
		sql +="	,\"FacmNo\"\r\n" ; 
		sql +="	,\"HouseBuyDate\"\r\n" ; 
		sql +="	,\"LoanAmt\"\r\n" ; 
		sql +="	,\"FirstDrawdownDate\"\r\n" ; 
		sql +="	,\"MaturityDate\"\r\n"; 
		sql +="	,\"LoanBal\"\r\n" ;
		sql +="	,\"YearMonth\"\r\n" ; 
		sql +="	,\"JsonFields\"\r\n" ;
		sql +="	,\"UsageCode\"\r\n" ; 
		sql +="	,\"YearlyInt\"\r\n" ; 
		sql +="	from  \"YearlyHouseLoanInt\"\r\n" ; 
		sql +="	where \"YearMonth\"= :iYYYYMM ";
		
		
		
		

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iYYYYMM", iYYYYMM);
		
		return this.convertToMap(query);
	}


}