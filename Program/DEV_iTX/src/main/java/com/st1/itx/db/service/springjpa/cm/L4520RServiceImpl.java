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
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.parse.Parse;

@Service("L4520RServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4520RServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	/* 轉換工具 */
	@Autowired
	public Parse parse;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		int PerfMonth = parse.stringToInteger(titaVo.getParam("PerfMonth")) + 191100;
		
		this.info("L4520.Fire");

		String sql = "    SELECT                      ";
		sql +=		 "    substr(ed.\"TitaTxtNo\", 0, 2) AS \"BatchNo\",   "; 
		sql +=		 "    ed.\"CustNo\"          AS \"CustNo\",            "; 
		sql +=		 "    ed.\"PerfMonth\"       AS \"PerfMonth\",         "; 
		sql +=		 "    ir.\"PrevInsuNo\"      AS \"PrevInsuNo\",        "; 
		sql +=		 "    ce.\"Fullname\"        AS \"FullName\",          "; 
		sql +=		 "    ce.\"EmployeeNo\"      AS \"EmployeeNo\",        ";
		sql +=		 "    cm.\"CustId\"          AS \"CustId\",            "; 
		sql +=		 "    ed.\"TitaTxtNo\"       AS \"TitaTxtNo\",         "; 
		sql +=		 "    ir.\"TotInsuPrem\"     AS \"TotInsuPrem\",       "; 
		sql +=		 "    ir.\"AcDate\"          AS \"AcDate\"             "; 
		sql +=		 "    FROM                    "; 
		sql +=       "    \"EmpDeductDtl\"   ed   "; 
		sql +=       "    LEFT JOIN \"InsuRenew\"      ir ON ir.\"CustNo\" = ed.\"CustNo\" "; 
		sql +=		 "    LEFT JOIN \"CustMain\"       cm ON cm.\"CustNo\" = ed.\"CustNo\" "; 
		sql +=		 "    LEFT JOIN \"CdEmp\"          ce ON ce.\"EmployeeNo\" = ed.\"TitaTlrNo\" "; 
		sql +=       "    WHERE                   "; 
		sql +=		 "    ed.\"PerfMonth\" = :PerfMonth "; 
		sql +=		 "    AND ed.\"AchRepayCode\" = 5   ";
		sql +=		 "     ORDER BY \"BatchNo\",\"CustNo\"   ";
		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		query.setParameter("PerfMonth", PerfMonth);
		return this.convertToMap(query);
	}

}