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

@Service("L4520ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4520ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

		this.info("L4520.findAll");

		int PerfMonth = parse.stringToInteger(titaVo.getParam("PerfMonth")) + 191100;
		
		String sql = "SELECT "; 
		sql +=  "    substr(ed.\"TitaTxtNo\", 0, 2) as f0,        "; 
		sql +=	"    ed.\"CustNo\" as f1,           "; 
		sql +=	"    lb.\"IntStartDate\" as f2,     "; 
		sql +=	"    lb.\"IntEndDate\" as f3,       "; 
		sql +=	"    ce.\"Fullname\" as f4,         "; 
		sql +=	"    ed.\"JsonFields\" as f5,       "; 
		sql +=	"    lb.\"Principal\" - JSON_VALUE(ed.\"JsonFields\", '$.ShortPri') as f6,     "; 
		sql +=	"    lb.\"Interest\" - JSON_VALUE(ed.\"JsonFields\", '$.ShortInt') as f7,      "; 
		sql +=	"    LB.\"BreachAmt\" as f8,        "; 
		sql +=	"    lb.\"TempAmt\" as f9,          "; 
		sql +=	"    JSON_VALUE(ed.\"JsonFields\", '$.ShortPri') + JSON_VALUE(ed.\"JsonFields\", '$.ShortInt') as f10,    "; 
		sql +=	"    JSON_VALUE(ed.\"JsonFields\", '$.AcctFee') + JSON_VALUE(ed.\"JsonFields\", '$.ModifyFee')+ JSON_VALUE(ed.\"JsonFields\", '$.FireFee')  "; 
		sql +=	"    + JSON_VALUE(ed.\"JsonFields\", '$.LawFee') as f11,     "; 
		sql +=	"    ce.\"CenterCode\" as f12,      "; 
		sql +=	"    ce.\"EmployeeNo\" as f13,      "; 
		sql +=	"    cm.\"CustId\" as f14           "; 
		sql +=	"    FROM    "; 
		sql +=	"    \"EmpDeductDtl\"   ed          "; 
		sql +=	"    LEFT JOIN \"LoanBorTx\"      lb ON lb.\"TitaTxtNo\" = lpad(ed.\"TitaTxtNo\", 8, 0)   ";
		sql +=	"    LEFT JOIN \"CustMain\"       cm ON cm.\"CustNo\" = ed.\"CustNo\"                     ";  
		sql +=	"    LEFT JOIN \"CdEmp\"          ce ON ce.\"EmployeeNo\" = ed.\"TitaTlrNo\"               "; 
		sql +=	"    WHERE          "; 
		sql +=	"    \"AchRepayCode\" = 1 "; 
		sql +=	"    AND \"PerfMonth\" = :PerfMonth" ;
		sql +=	"    ORDER BY \"F0\",\"F1\",\"F2\",\"F3\"" ;
		
		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		query.setParameter("PerfMonth", PerfMonth);
		return this.convertToMap(query);
	}

}