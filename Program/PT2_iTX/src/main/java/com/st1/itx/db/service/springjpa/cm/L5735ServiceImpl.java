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

		sql += " WITH CF AS ( ";
		sql += "   SELECT CF.\"CustNo\" AS \"CustNo\" ";
		sql += "        , CF.\"FacmNo\" AS \"FacmNo\" ";
		sql += "        , ROW_NUMBER() ";
		sql += "          OVER ( ";
		sql += "            PARTITION BY CF.\"CustNo\" ";
		sql += "                       , CF.\"FacmNo\" ";
		sql += "                       , CF.\"ClCode1\" ";
		sql += "                       , CF.\"ClCode2\" ";
		sql += "                       , CF.\"ClNo\" ";
		// 20230522 佳怡說明：擔保品改成鑑價日最接近核准日期的評估
		sql += "            ORDER BY ABS(NVL(CAS.\"ApproveDate\",0) - NVL(CE.\"EvaDate\",19110101) ) ASC ";
		sql += "          )                               AS \"Seq\" ";
		sql += "        , NVL(CE.\"EvaNetWorth\",0) AS \"EvaNetWorth\" ";
		sql += "   FROM \"ClFac\" CF ";
		sql += "   LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = CF.\"CustNo\" ";
		sql += "                            AND FAC.\"FacmNo\" = CF.\"FacmNo\" ";
		sql += "   LEFT JOIN \"FacCaseAppl\" CAS ON CAS.\"ApplNo\" = CF.\"ApproveNo\" ";
		sql += "   LEFT JOIN \"ClEva\" CE ON CE.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                         AND CE.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                         AND CE.\"ClNo\" = CF.\"ClNo\" ";
		sql += "   WHERE NVL(CAS.\"ApproveDate\",0) > 0 ";
		sql += " ) ";
		sql += " , \"CFSum\" AS ( ";
		sql += "   SELECT \"CustNo\" ";
		sql += "        , \"FacmNo\" ";
		sql += "        , SUM(NVL(\"EvaNetWorth\",0)) AS \"EvaNetWorth\" ";
		sql += "   FROM CF ";
		sql += "   WHERE \"Seq\" = 1 "; // 每個擔保品只取一筆
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
		sql += "LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = FM.\"CustNo\" ";
		sql += "                    AND CF.\"FacmNo\" = FM.\"FacmNo\" ";
		sql += "                    AND CF.\"MainFlag\" = 'Y' ";
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