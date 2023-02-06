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
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service
@Repository
public class L7300ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int yearMonth, TitaVo titaVo) {
		this.info("L7300ServiceImpl findAll ");

		this.info("yearMonth = " + yearMonth);

		String sql = "";
		sql += " SELECT ROW_NUMBER() ";
		sql += "        OVER ( ";
		sql += "          ORDER BY m.\"CustNo\" ";
		sql += "                 , m.\"FacmNo\" ";
		sql += "        )                          AS \"TranSeq\" "; // 傳輸順序
		sql += "      , f.\"MaturityDate\"         AS \"MaturityDate\" "; // 到期日
		sql += "      , CASE";
		sql += "          WHEN c.\"EntCode\" = '0' ";
		sql += "          THEN '1' ";
		sql += "        ELSE '2' ";
		sql += "        END                        AS \"LoanType\" "; // 貸款類別
		sql += "      , CASE ";
		sql += "          WHEN c.\"EntCode\" = '0' ";
		sql += "          THEN m.\"CustNo\" || '-' || m.\"FacmNo\" ";
		sql += "        ELSE TO_CHAR(c.\"CustName\") ";
		sql += "        END                        AS \"Counterparty\" "; // 交易對手
		sql += "      , m.\"StoreRate\"            AS \"LoanInt\""; // 放款利率
		sql += "      , CASE ";
		sql += "          WHEN NVL(cl.\"EvaNetWorth\", 0) = 0";
		sql += "          THEN 0 ";
		sql += "        ELSE ROUND(f.\"LineAmt\" / cl.\"EvaNetWorth\" * 100, 2)";
		sql += "        END                        AS \"LtvRatio\" "; // 貸款成數
		sql += "      , m.\"AcSubBookCode\"        AS \"SubCompanyCode\" "; // 區隔帳冊別(資金來源)
		sql += "      , m.\"PrinBalance\"          AS \"MrktValue\" "; // 市價
		sql += "      , m.\"PrinBalance\"          AS \"BookValue\" "; // 期初帳面金額
		sql += " FROM \"MonthlyFacBal\"  m ";
		sql += " LEFT JOIN \"CustMain\" c ON c.\"CustNo\" = m.\"CustNo\" ";
		sql += " LEFT JOIN \"FacMain\" f ON f.\"CustNo\" = m.\"CustNo\" ";
		sql += "                        AND f.\"FacmNo\" = m.\"FacmNo\" ";
		sql += " LEFT JOIN \"ClImm\" cl ON cl.\"ClCode1\" = m.\"ClCode1\" ";
		sql += "                       AND cl.\"ClCode2\" = m.\"ClCode2\" ";
		sql += "                       AND cl.\"ClNo\" = m.\"ClNo\" ";
		sql += " WHERE m.\"Status\" IN (0,2,4,6,7) ";
		sql += "   AND m.\"YearMonth\" = :yearMonth ";
		sql += " ORDER BY \"TranSeq\" ";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("yearMonth", yearMonth);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findNow(TitaVo titaVo) {
		this.info("L7300ServiceImpl findNow ");

		String sql = "";
		sql += " WITH m AS ( ";
		sql += "   SELECT LBM.\"CustNo\" ";
		sql += "        , LBM.\"FacmNo\" ";
		sql += "        , MAX(AR.\"AcSubBookCode\") AS \"AcSubBookCode\" ";
		sql += "        , MIN(LBM.\"StoreRate\")    AS \"StoreRate\" ";
		sql += "        , SUM(LBM.\"LoanBal\")      AS \"LoanBal\" ";
		sql += "   FROM \"LoanBorMain\" LBM ";
		sql += "   LEFT JOIN \"FacMain\" f ON f.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "                          AND f.\"FacmNo\" = LBM.\"FacmNo\" ";
		sql += "   LEFT JOIN \"AcReceivable\" ar on ar.\"AcctCode\" = CASE ";
		sql += "                                                        WHEN LBM.\"Status\" IN (2,6,7) ";
		sql += "                                                        THEN '990' ";
		sql += "                                                      ELSE f.\"AcctCode\" END ";
		sql += "                                and ar.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "                                and ar.\"FacmNo\" = LBM.\"FacmNo\" ";
		sql += "                                and ar.\"RvNo\" = LPAD(LBM.\"BormNo\",3,'0') ";
		sql += "   WHERE LBM.\"Status\" IN (0,2,4,6,7) ";
		sql += "   GROUP BY LBM.\"CustNo\" ";
		sql += "          , LBM.\"FacmNo\" ";
		sql += " )";
		sql += " SELECT ROW_NUMBER() ";
		sql += "        OVER ( ";
		sql += "          ORDER BY m.\"CustNo\" ";
		sql += "                 , m.\"FacmNo\" ";
		sql += "        )                          AS \"TranSeq\" "; // 傳輸筆數序號
		sql += "      , f.\"MaturityDate\"         AS \"MaturityDate\" "; // 到期日
		sql += "      , CASE";
		sql += "          WHEN c.\"EntCode\" = '0' ";
		sql += "          THEN '1' ";
		sql += "        ELSE '2' ";
		sql += "        END                        AS \"LoanType\" "; // 貸款類別
		sql += "      , CASE ";
		sql += "          WHEN c.\"EntCode\" = '0' ";
		sql += "          THEN m.\"CustNo\" || '-' || m.\"FacmNo\" ";
		sql += "        ELSE TO_CHAR(c.\"CustName\") ";
		sql += "        END                        AS \"Counterparty\" "; // 交易對手
		sql += "      , m.\"StoreRate\"            AS \"LoanInt\""; // 放款利率
		sql += "      , CASE ";
		sql += "          WHEN NVL(cl.\"EvaNetWorth\", 0) = 0";
		sql += "          THEN 0 ";
		sql += "        ELSE ROUND(f.\"LineAmt\" / cl.\"EvaNetWorth\" * 100, 2)";
		sql += "        END                        AS \"LtvRatio\" "; // 貸款成數
		sql += "      , m.\"AcSubBookCode\"        AS \"SubCompanyCode\" "; // 區隔帳冊別(資金來源)
		sql += "      , m.\"LoanBal\"              AS \"MrktValue\" "; // 市價
		sql += "      , m.\"LoanBal\"              AS \"BookValue\" "; // 期初帳面金額
		sql += " FROM m ";
		sql += " LEFT JOIN \"CustMain\" c ON c.\"CustNo\" = m.\"CustNo\" ";
		sql += " LEFT JOIN \"FacMain\" f ON f.\"CustNo\" = m.\"CustNo\" ";
		sql += "                        AND f.\"FacmNo\" = m.\"FacmNo\" ";
		sql += " LEFT JOIN \"ClFac\" cf ON cf.\"CustNo\" = m.\"CustNo\" ";
		sql += "                       AND cf.\"FacmNo\" = m.\"FacmNo\" ";
		sql += "                       AND cf.\"MainFlag\" = 'Y' ";
		sql += " LEFT JOIN \"ClImm\" cl ON cl.\"ClCode1\" = cf.\"ClCode1\" ";
		sql += "                       AND cl.\"ClCode2\" = cf.\"ClCode2\" ";
		sql += "                       AND cl.\"ClNo\" = cf.\"ClNo\" ";
		sql += " ORDER BY \"TranSeq\" ";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

}