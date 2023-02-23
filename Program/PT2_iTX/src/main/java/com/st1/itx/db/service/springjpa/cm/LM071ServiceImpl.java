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
public class LM071ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public List<Map<String, String>> findAll(TitaVo titaVo, int yearMonth) {

		this.info("LM071ServiceImpl.findAll yearMonth=" + yearMonth);

		String sql = "";
		sql += " WITH RES AS ( ";
		sql += "     SELECT \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += "          , \"BormNo\" ";
		sql += "          , \"EffectDate\" ";
		sql += "          , \"FitRate\" ";
		sql += "          , ROW_NUMBER() ";
		sql += "            OVER ( ";
		sql += "              PARTITION BY \"CustNo\" ";
		sql += "                         , \"FacmNo\" ";
		sql += "                         , \"BormNo\" ";
		sql += "              ORDER BY \"EffectDate\" DESC ";
		sql += "            ) AS \"Seq\" ";
		sql += "     FROM \"LoanRateChange\" ";
		sql += " ) ";
		sql += " SELECT M.\"ProdNo\"                      AS F0 "; // 商品代碼
		sql += "      , C.\"CustId\"                      AS F1 ";
		sql += "      , Q.\"QuitDate\"                    AS F2 "; // 離職/停約日
		sql += "      , \"Fn_ParseEOL\"(C.\"CustName\",0) AS F3 "; // 戶名/公司名稱
		sql += "      , L.\"CustNo\"                      AS F4 "; // 借款人戶號
		sql += "      , L.\"FacmNo\"                      AS F5 "; // 額度編號
		sql += "      , L.\"BormNo\"                      AS F6 "; // 撥款序號, 預約序號
		sql += "      , L.\"DrawdownDate\"                AS F7 "; // 撥款日期, 預約日期
		sql += "      , L.\"MaturityDate\"                AS F8 "; // 到期日
		sql += "      , RES.\"EffectDate\"                AS F9 "; // 最新利率生效起日
		sql += "      , RES.\"FitRate\"                   AS F10 "; // 最新利率
		sql += "      , L.\"DrawdownAmt\"                 AS F11 "; // 撥款金額
		sql += "      , L.\"LoanBal\"                     AS F12 "; // 放款餘額
		sql += " FROM \"LoanBorMain\" L ";
		sql += " LEFT JOIN \"MonthlyLoanBal\" M ON M.\"YearMonth\" = :inputYearMonth ";
		sql += "                               AND M.\"CustNo\" = L.\"CustNo\" ";
		sql += "                               AND M.\"FacmNo\" = L.\"FacmNo\" ";
		sql += "                               AND M.\"BormNo\" = L.\"BormNo\" ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = L.\"CustNo\" ";
		sql += " LEFT JOIN \"QuitEmp\" Q ON Q.\"EmpNo\" = C.\"EmpNo\" ";
		sql += " LEFT JOIN RES ON RES.\"CustNo\" = L.\"CustNo\" ";
		sql += "              AND RES.\"FacmNo\" = L.\"FacmNo\" ";
		sql += "              AND RES.\"BormNo\" = L.\"BormNo\" ";
		sql += "              AND RES.\"Seq\" = 1 ";
		sql += " WHERE L.\"Status\" IN (0,4) ";
		sql += "   AND M.\"ProdNo\" = '11' ";
		sql += " ORDER BY L.\"CustNo\" ";
		sql += "         ,L.\"FacmNo\" ";
		sql += "         ,L.\"BormNo\" ";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("inputYearMonth", yearMonth);
		return this.convertToMap(query);
	}
}