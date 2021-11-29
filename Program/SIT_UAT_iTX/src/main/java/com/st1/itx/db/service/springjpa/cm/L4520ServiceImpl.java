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

	public List<Map<String, String>> findSF(TitaVo titaVo) throws Exception {

		this.info("L4520.SF");
		int AcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		int PerfMonth = parse.stringToInteger(titaVo.getParam("PerfMonth")) + 191100;
		int ProcCode = parse.stringToInteger(titaVo.getParam("ProcCode"));
		String BatchNoFm = "BATX" + titaVo.getParam("BatchNoFm");
		String BatchNoTo = "BATX" + titaVo.getParam("BatchNoTo");
		
		String sql = "  SELECT * FROM \"EmpDeductMedia\" ";
		sql += "  WHERE \"AcDate\" = :AcDate " ;
		sql += "  AND   \"PerfMonth\" = :PerfMonth " ;
		if(ProcCode != 0) {
			sql += "  AND   \"FlowCode\" = :ProcCode " ;
		}
		sql += "  AND   \"AcDate\" = :AcDate " ;
		sql += "  AND   \"BatchNo\" >= :BatchNoFm " ;
		sql += "  AND   \"BatchNo\" <= :BatchNoTo " ;
		sql += "  ORDER BY  \"MediaKind\",\"FlowCode\",\"MediaSeq\"  " ;
		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("AcDate", AcDate);
		query.setParameter("PerfMonth", PerfMonth);
		if(ProcCode != 0) {
		  query.setParameter("ProcCode", ProcCode);
		}
		query.setParameter("BatchNoFm", BatchNoFm);
		query.setParameter("BatchNoTo", BatchNoTo);
		
		return this.convertToMap(query);
	}
	
	
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("L4520.findAll");

		int PerfMonth = parse.stringToInteger(titaVo.getParam("PerfMonth")) + 191100;
		String BatchNoFm = "BATX" + titaVo.getParam("BatchNoFm");
		String BatchNoTo = "BATX" + titaVo.getParam("BatchNoTo");
		String sql = "  SELECT  ";
		sql += "  substr(ed.\"TitaTxtNo\", 0, 2) as f0,";
		sql += "  ed.\"CustNo\" as f1,   ";
		sql += "  lb.\"IntStartDate\" as f2,";
		sql += "  lb.\"IntEndDate\" as f3,  ";
		sql += "  ce.\"Fullname\" as f4, ";
		sql += "  ed.\"JsonFields\" as f5,  ";
		sql += "  NVL(decode(lb.\"IntStartDate\",lb.\"IntEndDate\",sum((lb.\"Principal\" - JSON_VALUE(ed.\"JsonFields\", '$.ShortPri')))),0) as f6,";
		sql += "  NVL(decode(lb.\"IntStartDate\",lb.\"IntEndDate\",sum((lb.\"Interest\" - JSON_VALUE(ed.\"JsonFields\", '$.ShortInt')))),0)  as f7, ";
		sql += "  NVL(decode(lb.\"IntStartDate\",lb.\"IntEndDate\",sum(LB.\"BreachAmt\")),0) as f8,";
		sql += "  NVL(decode(lb.\"IntStartDate\",lb.\"IntEndDate\",sum(lb.\"TempAmt\")),0) as f9,  ";
		sql += "  NVL(decode(lb.\"IntStartDate\",lb.\"IntEndDate\",sum((JSON_VALUE(ed.\"JsonFields\", '$.ShortPri') + JSON_VALUE(ed.\"JsonFields\", '$.ShortInt')))),0)  as f10,";
		sql += "  NVL(decode(lb.\"IntStartDate\",lb.\"IntEndDate\",sum((JSON_VALUE(ed.\"JsonFields\", '$.AcctFee') + JSON_VALUE(ed.\"JsonFields\", '$.ModifyFee')+ JSON_VALUE(ed.\"JsonFields\", '$.FireFee') ";
		sql += "  + JSON_VALUE(ed.\"JsonFields\", '$.LawFee')))),0)  as f11,";
		sql += "  ce.\"CenterCode\" as f12, ";
		sql += "  ce.\"EmployeeNo\" as f13, ";
		sql += "  cm.\"CustId\" as f14";
		sql += "  FROM";
		sql += "  \"EmpDeductDtl\"   ed ";
		sql += "  LEFT JOIN \"LoanBorTx\"      lb ON lb.\"TitaTxtNo\" = lpad(ed.\"TitaTxtNo\", 8, 0)";
		sql += "  LEFT JOIN \"CustMain\"       cm ON cm.\"CustNo\" = ed.\"CustNo\"              ";
		sql += "  LEFT JOIN \"CdEmp\"          ce ON ce.\"EmployeeNo\" = ed.\"TitaTlrNo\"       ";
		sql += "  WHERE \"AchRepayCode\" = 1";
		sql += "  AND lb.\"IntStartDate\" <> 0 ";
		sql += "  AND lb.\"IntEndDate\"   <> 0 ";
		sql += "  AND \"PerfMonth\" = :PerfMonth";
		sql += "    AND ed.\"BatchNo\" >= :BatchNoFm";
		sql += "    AND ed.\"BatchNo\" <= :BatchNoTo";
		sql += "  GROUP BY ed.\"TitaTxtNo\", substr(ed.\"TitaTxtNo\", 0, 2), ed.\"CustNo\", lb.\"IntStartDate\", lb.\"IntEndDate\", ";
		sql += "  ce.\"Fullname\", ed.\"JsonFields\", ce.\"CenterCode\", ce.\"EmployeeNo\", cm.\"CustId\"";
		sql += "  ORDER BY ed.\"TitaTxtNo\",ed.\"CustNo\",lb.\"IntStartDate\",lb.\"IntEndDate\"";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("BatchNoFm", BatchNoFm);
		query.setParameter("BatchNoTo", BatchNoTo);
		query.setParameter("PerfMonth", PerfMonth);
		return this.convertToMap(query);
	}

}