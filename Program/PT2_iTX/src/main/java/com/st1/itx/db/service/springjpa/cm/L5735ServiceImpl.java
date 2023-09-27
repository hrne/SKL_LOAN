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

/**
 * L5735 建商餘額明細
 * 
 * @author ST1-ChihWei
 * @version 1.0.0
 *
 */
@Service("L5735ServiceImpl")
@Repository
public class L5735ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> getConstructionCompanyLoanData(int inputDrawdownDate, TitaVo titaVo) {

		this.info("getConstructionCompanyLoanData");

		// 轉西元年
		if (inputDrawdownDate <= 19110000) {
			inputDrawdownDate += 19110000;
		}

		this.info("inputDrawdownDate = " + inputDrawdownDate);

		int entdy = titaVo.getEntDyI();

		// 轉西元年
		if (entdy <= 19110000) {
			entdy += 19110000;
		}

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
		sql += " FROM \"ConstructionCompany\" CC ";
		sql += " LEFT JOIN \"MonthlyLoanBal\" MLB ON MLB.\"CustNo\" = CC.\"CustNo\" ";
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
		sql += "   AND NVL(CC.\"DeleteFlag\", ' ') != '*' ";
		sql += "   AND LBM.\"DrawdownDate\" <= :inputDrawdownDate ";
		sql += " ORDER BY CC.\"CustNo\" ";
		sql += "        , MLB.\"FacmNo\" ";
		sql += "        , MLB.\"BormNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", entdy / 100);
		query.setParameter("inputDrawdownDate", inputDrawdownDate);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> getNormalCustomerLoanData(int inputDrawdownDate, String subTxCD, TitaVo titaVo) {

		this.info("getNormalCustomerLoanData");

		// 轉西元年
		if (inputDrawdownDate <= 19110000) {
			inputDrawdownDate += 19110000;
		}

		this.info("inputDrawdownDate = " + inputDrawdownDate);

		int entdy = titaVo.getEntDyI();

		// 轉西元年
		if (entdy <= 19110000) {
			entdy += 19110000;
		}

//		L5735A-建商餘額明細 X
//		L5735B-首購餘額明細O
//		L5735D-工業區土地抵押餘額明細O
//		L5735E-正常戶餘額明細O
//		L5735G-住宅貸款餘額明細O
//		L5735I-補助貸款餘額明細O
//		L5735J-政府優惠貸款餘額明細O
//		L5735K-保險業投資不動產及放款情形XS

		String cond = "";
		switch (subTxCD) {
		case "L5735B":
			cond += "   AND MLB.\"AcctCode\" IN ('340') ";
			cond += "   AND LBM.\"Status\" = 0 ";
			break;
		case "L5735D":
			cond += "   AND MLB.\"ClCode1\" IN ('2') ";
			cond += "   AND MLB.\"ClCode2\" IN ('3') ";
			cond += "   AND LBM.\"Status\" = 0 ";
			break;
		case "L5735E":
			cond += "   AND LBM.\"Status\" = 0 ";
			break;
		case "L5735G":
			cond += "   AND LBM.\"UsageCode\" IN ('02') ";
			cond += "   AND LBM.\"Status\" = 0 ";
			break;
		case "L5735I":
			cond += "   AND MLB.\"ProdNo\" BETWEEN '81' AND '88' ";
			break;
		case "L5735J":
			cond += "   AND MLB.\"ProdNo\" BETWEEN 'IA' AND 'II' ";
			break;
		}

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
		sql += "   AND LBM.\"DrawdownDate\" <= :inputDrawdownDate ";
		sql += cond;
		sql += " ORDER BY MLB.\"CustNo\" ";
		sql += "        , MLB.\"FacmNo\" ";
		sql += "        , MLB.\"BormNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", entdy / 100);
		query.setParameter("inputDrawdownDate", inputDrawdownDate);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> getNormalCustomerLoanDataL5735D(int inputDrawdownDate, TitaVo titaVo) {

		this.info("getNormalCustomerLoanData");

		// 轉西元年
		if (inputDrawdownDate <= 19110000) {
			inputDrawdownDate += 19110000;
		}

		this.info("inputDrawdownDate = " + inputDrawdownDate);

		int entdy = titaVo.getEntDyI();

		// 轉西元年
		if (entdy <= 19110000) {
			entdy += 19110000;
		}

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
		sql += "      , MLB.\"ClCode2\" ";
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
		sql += "   AND LBM.\"DrawdownDate\" <= :inputDrawdownDate ";
		sql += "   AND LBM.\"Status\" = 0 ";
		sql += " ORDER BY MLB.\"CustNo\" ";
		sql += "        , MLB.\"FacmNo\" ";
		sql += "        , MLB.\"BormNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", entdy / 100);
		query.setParameter("inputDrawdownDate", inputDrawdownDate);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> getBankOnline(TitaVo titaVo) {

		try {
			this.info("1." + titaVo.getParam("DATABASE"));
		} catch (LogicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String sql = "  ";
		sql += " SELECT COUNT(*) AS \"Int\" FROM \"CdBank\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> getBankMonth(TitaVo titaVo) {

		try {
			titaVo.setDataBaseOnMon();
			this.info("2." + titaVo.getParam("DATABASE"));
		} catch (LogicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String sql = "  ";

		sql += " SELECT COUNT(*) AS \"Int\" FROM \"CdBank\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}
}