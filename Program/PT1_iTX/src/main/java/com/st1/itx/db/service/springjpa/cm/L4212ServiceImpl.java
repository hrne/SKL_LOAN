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

@Service("L4212ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4212ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

		String BatchNoFm = titaVo.getParam("BatchNoFm");
		String BatchNoTo = titaVo.getParam("BatchNoTo");
		int AcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;

		String sql = "WITH tx1 AS (";
		sql += "      SELECT";
		sql += "          \"SlipSumNo\",";
		sql += "          \"CustNo\",";
		sql += "          \"IntStartDate\",";
		sql += "          \"IntEndDate\",";
		sql += "          \"AcDate\",";
		sql += "          \"TitaTlrNo\",";
		sql += "          \"TitaTxtNo\",";
		sql += "          \"AcctCode\"";
		sql += "      FROM";
		sql += "          \"LoanBorTx\"";
		sql += "      WHERE";
		sql += "          \"AcDate\" = :inputacdate";
		sql += "          AND \"SlipSumNo\" > 0  ";
		sql += "          AND \"RepayCode\" = 3  ";
		sql += "    	 AND \"SlipSumNo\" >= :BatchNoFm";
		sql += "    	 AND \"SlipSumNo\" <= :BatchNoTo";
		sql += "      GROUP BY";
		sql += "          \"SlipSumNo\",";
		sql += "          \"CustNo\",";
		sql += "          \"IntStartDate\",";
		sql += "          \"IntEndDate\",";
		sql += "          \"AcDate\",";
		sql += "          \"TitaTlrNo\",";
		sql += "          \"TitaTxtNo\",";
		sql += "          \"AcctCode\"";
		sql += "  ), tx2 AS ( ";
		sql += "      SELECT";
		sql += "          \"CustNo\",";
		sql += "          \"FacmNo\",";
		sql += "          \"BormNo\",";
		sql += "          \"IntStartDate\",";
		sql += "          \"IntEndDate\",";
		sql += "          \"AcDate\",";
		sql += "          \"TitaTxCd\",";
		sql += "          \"TitaTlrNo\",";
		sql += "          \"TitaTxtNo\",";
		sql += "          \"AcctCode\",";
		sql += "          SUM(\"TxAmt\") AS \"TxAmt\",";
		sql += "          SUM(\"Principal\") AS \"Principal\",";
		sql += "          SUM(\"Interest\") AS \"Interest\",";
		sql += "          SUM(\"DelayInt\") AS \"DelayInt\",";
		sql += "          SUM(\"BreachAmt\") AS \"BreachAmt\",";
		sql += "          SUM(\"CloseBreachAmt\") AS \"CloseBreachAmt\",";
		sql += "          SUM(\"TempAmt\") AS \"TempAmt\",";
		sql += "          SUM(\"Overflow\") AS \"Overflow\",";
		sql += "          SUM(\"UnpaidInterest\"+\"UnpaidPrincipal\"+\"UnpaidCloseBreach\") AS \"Shortfall\",";
		sql += "          SUM(nvl(JSON_VALUE(\"OtherFields\", '$.AcctFee'), 0)) AS \"AcctFee\",";
		sql += "          SUM(nvl(JSON_VALUE(\"OtherFields\", '$.ModifyFee'), 0)) AS \"ModifyFee\",";
		sql += "          SUM(nvl(JSON_VALUE(\"OtherFields\", '$.FireFee'), 0)) AS \"FireFee\",";
		sql += "          SUM(nvl(JSON_VALUE(\"OtherFields\", '$.LawFee'), 0)) AS \"LawFee\",";
		sql += "          SUM(nvl(JSON_VALUE(\"OtherFields\", '$.ShortPri'), 0)) AS \"ShortPri\",";
		sql += "          SUM(nvl(JSON_VALUE(\"OtherFields\", '$.ShortInt'), 0)) AS \"ShortInt\",";
		sql += "          DECODE(\"AcSeq\",1,1,2) AS \"AcSeq\"";
		sql += "      FROM";
		sql += "          \"LoanBorTx\"";
		sql += "      WHERE";
		sql += "          \"AcDate\" = :inputacdate";
		sql += "          AND \"SlipSumNo\" > 0  ";
		sql += "          AND \"RepayCode\" = 3  ";
		sql += "      GROUP BY";
		sql += "          \"CustNo\",";
		sql += "          \"FacmNo\",";
		sql += "          \"BormNo\",";
		sql += "          \"IntStartDate\",";
		sql += "          \"IntEndDate\",";
		sql += "          \"AcDate\",";
		sql += "          \"TitaTxCd\",";
		sql += "          \"TitaTlrNo\",";
		sql += "          \"TitaTxtNo\",";
		sql += "          DECODE(\"AcSeq\",1,1,2), ";
		sql += "          \"AcctCode\"";
		sql += "  )";
		sql += "  SELECT ";
		sql += "  substr(tx1.\"TitaTxtNo\", 0, 2) AS \"BaTxNo\",";
		sql += "  tx1.\"CustNo\" AS \"CustNo\",   ";
		sql += "  MIN(tx2.\"IntStartDate\") AS \"IntStartDate\",";
		sql += "  MAX(tx2.\"IntEndDate\") AS \"IntEndDate\",  ";
		sql += "  MIN(cm.\"CustName\") AS \"Fullname\", ";
		sql += "  SUM(tx2.\"TxAmt\") AS \"TxAmt\",";
		sql += "  SUM(tx2.\"Principal\") AS \"Principal\",";
		sql += "  SUM(tx2.\"Interest\")  AS \"Interest\" , ";
		sql += "  SUM(tx2.\"BreachAmt\" + tx2.\"CloseBreachAmt\" + tx2.\"DelayInt\")  AS \"BreachAmt\", ";
		sql += "  SUM(tx2.\"TempAmt\")  AS \"TempDr\" , ";
		sql += "  SUM(tx2.\"Overflow\")  AS \"TempCr\" , ";
		sql += "  SUM(tx2.\"Shortfall\")  AS \"Shortfall\",";
		sql += "  SUM(tx2.\"AcctFee\" + tx2.\"ModifyFee\" + tx2.\"FireFee\" + tx2.\"LawFee\") AS \"OtherAmt\",";
		sql += "  CASE ";
		sql += "  	WHEN tx2.\"TitaTxCd\" = 'L3210' ";
		sql += "    THEN tx2.\"AcSeq\" ";
		sql += "   ELSE 0 END AS \"AcSeq\",";
		sql += "   tx2.\"AcctCode\" AS \"AcctCode\"";
		sql += "  FROM tx1 ";
		sql += "  LEFT JOIN \"CustMain\"       cm ON cm.\"CustNo\" = tx1.\"CustNo\"        ";
		sql += "  LEFT JOIN tx2 ON tx2.\"CustNo\" = tx1.\"CustNo\"";
		sql += "                       AND tx2.\"IntStartDate\" = tx1.\"IntStartDate\"";
		sql += "                       AND tx2.\"IntEndDate\" = tx1.\"IntEndDate\"";
		sql += "                       AND tx2.\"AcDate\" = tx1.\"AcDate\"";
		sql += "                       AND tx2.\"TitaTlrNo\" = tx1.\"TitaTlrNo\"";
		sql += "                       AND tx2.\"TitaTxtNo\" = tx1.\"TitaTxtNo\"";
		sql += "               		   AND tx2.\"AcctCode\" = tx1.\"AcctCode\"";
		sql += " LEFT JOIN \"FacMain\" fac ON fac.\"CustNo\" = tx2.\"CustNo\"";
		sql += "                          AND fac.\"FacmNo\" = tx2.\"FacmNo\"";
		sql += "  GROUP BY substr(tx1.\"TitaTxtNo\", 0, 2),";
		sql += "  		   tx1.\"CustNo\",";
		sql += " 		   CASE ";
		sql += "  			 WHEN tx2.\"TitaTxCd\" = 'L3210' ";
		sql += "    		 THEN tx2.\"AcSeq\" ";
		sql += "   		   ELSE 0 END ,";
		sql += "          tx2.\"AcctCode\" ";
		sql += "  ORDER BY   substr(tx1.\"TitaTxtNo\", 0, 2),tx1.\"CustNo\", tx2.\"AcctCode\" ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("BatchNoFm", BatchNoFm);
		query.setParameter("BatchNoTo", BatchNoTo);
		query.setParameter("inputacdate", AcDate);
		return this.convertToMap(query);
	}

}