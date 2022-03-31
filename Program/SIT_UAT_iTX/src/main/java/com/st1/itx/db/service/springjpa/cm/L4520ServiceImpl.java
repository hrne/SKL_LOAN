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
		String ProcCode = titaVo.getParam("ProcCode");
		int AcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		String sql = "WITH tx1 AS ("; 
		sql += "      SELECT" ; 
		sql += "          \"CustNo\"," ; 
		sql += "          \"FacmNo\"," ; 
		sql += "          \"BormNo\"," ; 
		sql += "          \"IntStartDate\"," ; 
		sql += "          \"IntEndDate\"," ; 
		sql += "          \"AcDate\"," ; 
		sql += "          \"TitaTlrNo\"," ; 
		sql += "          \"TitaTxtNo\"" ; 
		sql += "      FROM" ;
		sql += "          \"LoanBorTx\"" ; 
		sql += "      WHERE" ; 
		sql += "          \"AcDate\" = :inputacdate" ; 
		sql += "          AND \"TitaHCode\" = 0" ; 
		sql += "      GROUP BY" ; 
		sql += "          \"CustNo\"," ; 
		sql += "          \"FacmNo\"," ; 
		sql += "          \"BormNo\"," ; 
		sql += "          \"IntStartDate\"," ; 
		sql += "          \"IntEndDate\"," ; 
		sql += "          \"AcDate\"," ; 
		sql += "          \"TitaTlrNo\"," ; 
		sql += "          \"TitaTxtNo\"" ; 
		sql += "  ), tx2 AS (" ; 
		sql += "      SELECT" ; 
		sql += "          \"CustNo\"," ; 
		sql += "          \"FacmNo\"," ; 
		sql += "          \"BormNo\"," ; 
		sql += "          \"IntStartDate\"," ; 
		sql += "          \"IntEndDate\"," ; 
		sql += "          \"AcDate\"," ; 
		sql += "          \"TitaTlrNo\"," ; 
		sql += "          \"TitaTxtNo\"," ; 
		sql += "          SUM(\"TxAmt\") AS \"TxAmt\"," ; 
		sql += "          SUM(\"Principal\") AS \"Principal\"," ; 
		sql += "          SUM(\"Interest\") AS \"Interest\"," ; 
		sql += "          SUM(\"DelayInt\") AS \"DelayInt\"," ; 
		sql += "          SUM(\"BreachAmt\") AS \"BreachAmt\"," ; 
		sql += "          SUM(\"CloseBreachAmt\") AS \"CloseBreachAmt\"," ; 
		sql += "          SUM(\"TempAmt\") AS \"TempAmt\"," ; 
		sql += "          SUM(\"Shortfall\") AS \"Shortfall\"," ; 
		sql += "          SUM(nvl(JSON_VALUE(\"OtherFields\", '$.AcctFee'), 0)) AS \"AcctFee\"," ; 
		sql += "          SUM(nvl(JSON_VALUE(\"OtherFields\", '$.ModifyFee'), 0)) AS \"ModifyFee\"," ; 
		sql += "          SUM(nvl(JSON_VALUE(\"OtherFields\", '$.FireFee'), 0)) AS \"FireFee\"," ; 
		sql += "          SUM(nvl(JSON_VALUE(\"OtherFields\", '$.LawFee'), 0)) AS \"LawFee\"," ; 
		sql += "          SUM(nvl(JSON_VALUE(\"OtherFields\", '$.ShortPri'), 0)) AS \"ShortPri\"," ; 
		sql += "          SUM(nvl(JSON_VALUE(\"OtherFields\", '$.ShortInt'), 0)) AS \"ShortInt\"" ; 
		sql += "      FROM" ; 
		sql += "          \"LoanBorTx\"" ; 
		sql += "      WHERE" ; 
		sql += "          \"AcDate\" = :inputacdate" ; 
		sql += "          AND \"TitaHCode\" = 0" ; 
		sql += "      GROUP BY" ; 
		sql += "          \"CustNo\"," ; 
		sql += "          \"FacmNo\"," ; 
		sql += "          \"BormNo\"," ; 
		sql += "          \"IntStartDate\"," ; 
		sql += "          \"IntEndDate\"," ; 
		sql += "          \"AcDate\"," ; 
		sql += "          \"TitaTlrNo\"," ; 
		sql += "          \"TitaTxtNo\"" ; 
		sql += "  )";
		
		sql += "  SELECT ";
		sql += "  substr(ed.\"TitaTxtNo\", 0, 2) AS \"BaTxNo\",";
		sql += "  ed.\"CustNo\" AS \"CustNo\",   ";
		sql += "  tx2.\"IntStartDate\" AS \"IntStartDate\",";
		sql += "  tx2.\"IntEndDate\" AS \"IntEndDate\",  ";
		sql += "  ce.\"Fullname\" AS \"Fullname\", ";
		sql += "  tx2.\"Principal\" AS \"Principal\",";
		sql += "  tx2.\"Interest\"  AS \"Interest\" , ";
		sql += "  tx2.\"BreachAmt\" + tx2.\"CloseBreachAmt\" + tx2.\"DelayInt\"  AS \"BreachAmt\",";
		sql += "  tx2.\"TempAmt\"       AS \"TempAmt\",  ";
		sql += "  tx2.\"Shortfall\"  AS \"Shortfall\",";
		sql += "  tx2.\"AcctFee\" + tx2.\"ModifyFee\" + tx2.\"FireFee\" + tx2.\"LawFee\" AS \"OtherAmt\",";
		sql += "  ce.\"CenterCode\" AS \"CenterCode\", ";
		sql += "  ce.\"EmployeeNo\" AS \"EmployeeNo\", ";
		sql += "  cm.\"CustId\" AS \"CustId\", ";
		sql += "  ed.\"RepayCode\" AS \"RepayCode\",  ";
		sql += "  ed.\"AcctCode\" as \"AcctCode\",  ";
		sql += "  ed.\"ProcCode\" as \"ProcCode\",";
		sql += "  ed.\"Acdate\" as \"Acdate\"" ;
		sql += "  FROM";
		sql += "  \"EmpDeductDtl\"   ed ";
		
		sql += "  LEFT JOIN \"CustMain\"       cm ON cm.\"CustNo\" = ed.\"CustNo\"              ";
		sql += "  LEFT JOIN \"CdEmp\"          ce ON ce.\"EmployeeNo\" = ed.\"EmpNo\"       ";
		
		sql += "  LEFT JOIN tx1 ON tx1.\"CustNo\" = ed.\"CustNo\"";
		sql += "                       AND tx1.\"AcDate\" = ed.\"Acdate\"";  
		sql += "                       AND tx1.\"TitaTlrNo\" = ed.\"TitaTlrNo\"";
		sql += "                       AND tx1.\"TitaTxtNo\" = ed.\"TitaTxtNo\"";
		sql += "  LEFT JOIN tx2 ON tx2.\"CustNo\" = tx1.\"CustNo\"";
		sql += "                       AND tx2.\"FacmNo\" = tx1.\"FacmNo\"";
		sql += "                       AND tx2.\"BormNo\" = tx1.\"BormNo\"";
		sql += "                       AND tx2.\"IntStartDate\" = tx1.\"IntStartDate\"";
		sql += "                       AND tx2.\"IntEndDate\" = tx1.\"IntEndDate\"";
		sql += "                       AND tx2.\"AcDate\" = tx1.\"AcDate\"";
		sql += "                       AND tx2.\"TitaTlrNo\" = tx1.\"TitaTlrNo\""; 
		sql += "                       AND tx2.\"TitaTxtNo\" = tx1.\"TitaTxtNo\"";
				
				
				
		sql += "  WHERE \"AchRepayCode\" IN ( 1, 5) ";
		sql += "  AND \"PerfMonth\" = :PerfMonth";
		sql += "    AND ed.\"BatchNo\" >= :BatchNoFm";
		sql += "    AND ed.\"BatchNo\" <= :BatchNoTo";
		if(!"0".equals(ProcCode)) {
			sql += "    AND ed.\"ProcCode\" = :ProcCode";
		}
		
		sql += "  ORDER BY   ed.\"RepayCode\", ed.\"AcctCode\", ed.\"ProcCode\", ed.\"CustNo\", tx2.\"IntStartDate\", tx2.\"IntEndDate\"";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("BatchNoFm", BatchNoFm);
		query.setParameter("BatchNoTo", BatchNoTo);
		query.setParameter("PerfMonth", PerfMonth);
		query.setParameter("inputacdate", AcDate);
		if(!"0".equals(ProcCode)) {
		  query.setParameter("ProcCode", ProcCode);
		}
		return this.convertToMap(query);
	}

}