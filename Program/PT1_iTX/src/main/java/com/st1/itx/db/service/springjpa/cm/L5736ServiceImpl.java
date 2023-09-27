package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

/**
 * L5736
 * 
 * @author ST1-ChihWei
 * @version 1.0.0
 *
 */
@Service("L5736ServiceImpl")
@Repository
public class L5736ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	Parse parse;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> getOverdueCustomerLoanData(int inputDrawdownDate, TitaVo titaVo)
			throws LogicException {

		this.info("getOverdueCustomerLoanData");

		// 轉西元年
		if (inputDrawdownDate <= 19110000) {
			inputDrawdownDate += 19110000;
		}

		int inputYearMonth = parse.stringToInteger(titaVo.getParam("inputYearMonth"));

		// 轉西元年
		if (inputYearMonth <= 19110000) {
			inputYearMonth += 19110000;
		}

		this.info("inputYearMonth =" + inputYearMonth);

		String sql = "  ";

		sql += " WITH \"CFSum\" AS ( ";
		sql += "   SELECT \"CustNo\" ";
		sql += "        , \"FacmNo\" ";
		sql += "        , SUM(NVL(\"OriEvaNotWorth\",0)) AS \"EvaNetWorth\" ";
		sql += "   FROM \"ClFac\"";
		sql += "   WHERE \"MainFlag\" = 'Y' ";
		sql += "   GROUP BY \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += " )";
		sql += " SELECT CM.\"CustName\" ";
		sql += "      , MLB.\"CustNo\" ";
		sql += "      , MLB.\"FacmNo\" ";
		sql += "      , MLB.\"BormNo\" ";
		sql += "      , MLB.\"LoanBalance\" ";
		sql += "      , FM.\"LineAmt\" ";
		sql += "      , LBM.\"PrevPayIntDate\" ";
		sql += "      , MLB.\"ClCode1\" ";
		sql += "      , FM.\"UsageCode\" ";
		sql += "      , MLB.\"AcctCode\" ";
		sql += "      , MLB.\"ProdNo\" ";
		sql += "      , LBM.\"Status\" ";
		sql += "      , LBM.\"DrawdownDate\" ";
		sql += "      , NVL(CS.\"EvaNetWorth\", 0) AS \"EvaNetWorth\"";
		sql += "      , CASE ";
		sql += "          WHEN NVL(CS.\"EvaNetWorth\", 0) = 0";
		sql += "          THEN 0 ";
		sql += "        ELSE ROUND(FM.\"LineAmt\" / CS.\"EvaNetWorth\", 2) ";
		sql += "        END                        AS \"LoanRatio\" "; // 貸款成數
		sql += " FROM  \"MonthlyLoanBal\" MLB ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = MLB.\"CustNo\" ";
		sql += " LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = MLB.\"CustNo\" ";
		sql += "                       AND FM.\"FacmNo\" = MLB.\"FacmNo\" ";
		sql += " LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = MLB.\"CustNo\" ";
		sql += "                            AND LBM.\"FacmNo\" = MLB.\"FacmNo\" ";
		sql += "                            AND LBM.\"BormNo\" = MLB.\"BormNo\" ";
		sql += "LEFT JOIN \"CFSum\" CS ON CS.\"CustNo\" = FM.\"CustNo\" ";
		sql += "                    AND CS.\"FacmNo\" = FM.\"FacmNo\" ";
		sql += " WHERE MLB.\"YearMonth\" = :inputYearMonth ";
		sql += "   AND MLB.\"LoanBalance\" > 0 ";
		sql += "   AND LBM.\"Status\" != 0 ";
		sql += " ORDER BY MLB.\"CustNo\" ";
		sql += "        , MLB.\"FacmNo\" ";
		sql += "        , MLB.\"BormNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", inputYearMonth);

		return this.convertToMap(query);
	}

}