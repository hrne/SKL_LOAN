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
		sql += "          \"IntStartDate\"," ; 
		sql += "          \"IntEndDate\"," ; 
		sql += "          \"AcDate\"," ; 
		sql += "          \"TitaTlrNo\"," ; 
		sql += "          \"TitaTxtNo\"," ;
		sql += "          \"AcctCode\"" ; 
		sql += "      FROM" ;
		sql += "          \"LoanBorTx\"" ; 
		sql += "      WHERE" ; 
		sql += "          \"AcDate\" = :inputacdate" ; 
		sql += "          AND \"TitaHCode\" = 0" ;
		sql += "          AND (\"IntStartDate\" > 0 " ; 
		sql += "           OR \"TitaTxCd\" = 'L3210' )" ; 
		sql += "      GROUP BY" ; 
		sql += "          \"CustNo\"," ; 
		sql += "          \"IntStartDate\"," ; 
		sql += "          \"IntEndDate\"," ; 
		sql += "          \"AcDate\"," ; 
		sql += "          \"TitaTlrNo\"," ; 
		sql += "          \"TitaTxtNo\"," ; 
		sql += "          \"AcctCode\"" ; 
		sql += "  ), tx2 AS (" ; 
		sql += "      SELECT" ; 
		sql += "          \"CustNo\"," ; 
		sql += "          \"FacmNo\"," ; 
		sql += "          \"BormNo\"," ; 
		sql += "          \"IntStartDate\"," ; 
		sql += "          \"IntEndDate\"," ; 
		sql += "          \"AcDate\"," ; 
		sql += "          \"TitaTxCd\",";
		sql += "          \"TitaTlrNo\"," ; 
		sql += "          \"TitaTxtNo\"," ; 
		sql += "          \"AcctCode\"," ; 
		sql += "          SUM(\"TxAmt\") AS \"TxAmt\"," ; 
		sql += "          SUM(\"Principal\") AS \"Principal\"," ; 
		sql += "          SUM(\"Interest\") AS \"Interest\"," ; 
		sql += "          SUM(\"DelayInt\") AS \"DelayInt\"," ; 
		sql += "          SUM(\"BreachAmt\") AS \"BreachAmt\"," ; 
		sql += "          SUM(\"CloseBreachAmt\") AS \"CloseBreachAmt\"," ; 
		sql += "          SUM(\"TempAmt\") AS \"TempAmt\"," ; 
		sql += "          SUM(\"Overflow\") AS \"Overflow\"," ; 
		sql += "          SUM(\"UnpaidInterest\"+\"UnpaidPrincipal\"+\"UnpaidCloseBreach\") AS \"Shortfall\"," ; 
		sql += "          SUM(nvl(JSON_VALUE(\"OtherFields\", '$.AcctFee'), 0)) AS \"AcctFee\"," ; 
		sql += "          SUM(nvl(JSON_VALUE(\"OtherFields\", '$.ModifyFee'), 0)) AS \"ModifyFee\"," ; 
		sql += "          SUM(nvl(JSON_VALUE(\"OtherFields\", '$.FireFee'), 0)) AS \"FireFee\"," ; 
		sql += "          SUM(nvl(JSON_VALUE(\"OtherFields\", '$.LawFee'), 0)) AS \"LawFee\"," ; 
		sql += "          SUM(nvl(JSON_VALUE(\"OtherFields\", '$.ShortPri'), 0)) AS \"ShortPri\"," ; 
		sql += "          SUM(nvl(JSON_VALUE(\"OtherFields\", '$.ShortInt'), 0)) AS \"ShortInt\"," ; 
		sql += "          DECODE(\"AcSeq\",1,1,2) AS \"AcSeq\"" ;
		sql += "      FROM" ; 
		sql += "          \"LoanBorTx\"" ; 
		sql += "      WHERE" ; 
		sql += "          \"AcDate\" = :inputacdate" ; 
		sql += "          AND \"TitaHCode\" = 0" ; 
		sql += "          AND (\"IntStartDate\" > 0 " ; 
		sql += "           OR \"TitaTxCd\" = 'L3210' )" ; 
		sql += "      GROUP BY" ; 
		sql += "          \"CustNo\"," ; 
		sql += "          \"FacmNo\"," ; 
		sql += "          \"BormNo\"," ; 
		sql += "          \"IntStartDate\"," ; 
		sql += "          \"IntEndDate\"," ; 
		sql += "          \"AcDate\"," ; 
		sql += "          \"TitaTxCd\",";
		sql += "          \"TitaTlrNo\"," ; 
		sql += "          \"TitaTxtNo\"," ; 
		sql += "          DECODE(\"AcSeq\",1,1,2), " ;
		sql += "          \"AcctCode\"," ;
		sql += "  )";
		sql += "  SELECT ";
		sql += "  MIN(substr(ed.\"TitaTxtNo\", 0, 2)) AS \"BaTxNo\",";
		sql += "  ed.\"CustNo\" AS \"CustNo\",   ";
		sql += "  MIN(tx2.\"IntStartDate\") AS \"IntStartDate\",";
		sql += "  MAX(tx2.\"IntEndDate\") AS \"IntEndDate\",  ";
		sql += "  MIN(ce.\"Fullname\") AS \"Fullname\", ";
		sql += "  SUM(tx2.\"TxAmt\") AS \"TxAmt\",";
		sql += "  SUM(tx2.\"Principal\") AS \"Principal\",";
		sql += "  SUM(tx2.\"Interest\")  AS \"Interest\" , ";
		sql += "  SUM(tx2.\"BreachAmt\" + tx2.\"CloseBreachAmt\" + tx2.\"DelayInt\")  AS \"BreachAmt\", ";
		sql += "  SUM(tx2.\"TempAmt\")  AS \"TempDr\" , ";
		sql += "  SUM(tx2.\"Overflow\")  AS \"TempCr\" , ";
		sql += "  SUM(tx2.\"Shortfall\")  AS \"Shortfall\",";
		sql += "  SUM(tx2.\"AcctFee\" + tx2.\"ModifyFee\" + tx2.\"FireFee\" + tx2.\"LawFee\") AS \"OtherAmt\",";
		sql += "  MIN(ce.\"CenterCode\") AS \"CenterCode\", ";
		sql += "  MIN(ce.\"EmployeeNo\") AS \"EmployeeNo\", ";
		sql += "  MIN(cm.\"CustId\") AS \"CustId\", ";
		sql += "  MIN(ed.\"RepayCode\") AS \"RepayCode\",  ";
		sql += "  MIN(ed.\"ProcCode\") as \"ProcCode\",";
		sql += "  MIN(ed.\"Acdate\") as \"Acdate\"," ;
		sql += "  CASE " ;
		sql += "  	WHEN tx2.\"TitaTxCd\" = 'L3210' " ;
		sql += "    THEN tx2.\"AcSeq\" " ;
		sql += "   ELSE 0 END AS \"AcSeq\"," ;
		sql += "   tx2.\"AcctCode\" AS \"AcctCode\"" ;
		sql += "  FROM";
		sql += "  ( ";
		sql += "  SELECT DISTINCT";
		sql += "  		 \"CustNo\"";
		sql += "  		,\"EmpNo\"";
		sql += "  		,\"Acdate\"";
		sql += "  		,\"TitaTlrNo\"";
		sql += "  		,\"TitaTxtNo\"";
		sql += "  		,\"ProcCode\"";
		sql += "  		,\"AcctCode\"";
		sql += "  		,\"RepayCode\"";
		sql += "  FROM \"EmpDeductDtl\" ";
		sql += "  WHERE \"AchRepayCode\" IN ( 1, 5) ";
		sql += "    AND \"PerfMonth\" = :PerfMonth";
		sql += "    AND \"BatchNo\" >= :BatchNoFm";
		sql += "    AND \"BatchNo\" <= :BatchNoTo";
		if(!"0".equals(ProcCode)) {
			sql += "    AND \"ProcCode\" = :ProcCode";
		}
		sql += "  ) ed ";
		sql += "  LEFT JOIN \"CustMain\"       cm ON cm.\"CustNo\" = ed.\"CustNo\"        ";
		sql += "  LEFT JOIN \"CdEmp\"          ce ON ce.\"EmployeeNo\" = ed.\"EmpNo\"       ";
		sql += "  LEFT JOIN \"BatxDetail\" BD ON BD.\"AcDate\" = ed.\"Acdate\"" ;
		sql += "                             AND BD.\"TitaTlrNo\" = ed.\"TitaTlrNo\""; 
		sql += "                             AND BD.\"TitaTxtNo\" = ed.\"TitaTxtNo\"";
		sql += "  LEFT JOIN tx1 ON tx1.\"CustNo\" = ed.\"CustNo\"";
		sql += "               AND tx1.\"AcDate\" = ed.\"Acdate\"";
		sql += "               AND tx1.\"AcctCode\" = ed.\"AcctCode\"";  
		sql += "               AND SUBSTR(tx1.\"TitaTxtNo\",1,2) = SUBSTR(BD.\"BatchNo\",5,2)";
		sql += "               AND TO_NUMBER(SUBSTR(tx1.\"TitaTxtNo\",3,6)) = BD.\"DetailSeq\"";
		sql += "  LEFT JOIN tx2 ON tx2.\"CustNo\" = tx1.\"CustNo\"";
		sql += "                       AND tx2.\"IntStartDate\" = tx1.\"IntStartDate\"";
		sql += "                       AND tx2.\"IntEndDate\" = tx1.\"IntEndDate\"";
		sql += "                       AND tx2.\"AcDate\" = tx1.\"AcDate\"";
		sql += "                       AND tx2.\"TitaTlrNo\" = tx1.\"TitaTlrNo\""; 
		sql += "                       AND tx2.\"TitaTxtNo\" = tx1.\"TitaTxtNo\"";
		sql += "               		   AND tx2.\"AcctCode\" = tx1.\"AcctCode\""; 
		sql += " LEFT JOIN \"FacMain\" fac ON fac.\"CustNo\" = tx2.\"CustNo\"";
		sql += "                      AND fac.\"FacmNo\" = tx2.\"FacmNo\"";
		sql += "  GROUP BY ed.\"CustNo\",";
		sql += " 		   CASE " ;
		sql += "  			 WHEN tx2.\"TitaTxCd\" = 'L3210' " ;
		sql += "    		 THEN tx2.\"AcSeq\" " ;
		sql += "   		   ELSE 0 END ," ;
		sql += "          tx2.\"AcctCode\""; 
		sql += "  ORDER BY   \"RepayCode\", \"AcctCode\", \"ProcCode\", \"CustNo\",\"AcSeq\", \"IntStartDate\", \"IntEndDate\"";

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