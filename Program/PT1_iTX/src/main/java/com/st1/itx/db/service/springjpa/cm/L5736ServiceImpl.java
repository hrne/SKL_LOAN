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

/**
 * L5736 正常戶餘額明細
 * 
 * @author ST1-ChihWei
 * @version 1.0.0
 *
 */
@Service("L5736ServiceImpl")
@Repository
public class L5736ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> getNormalCustomerLoanData(int inputDrawdownDate, TitaVo titaVo) {

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
		sql += "            ORDER BY NVL(CE_early.\"EvaDate\",0) DESC "; // 第1段. 最接近該額度核准日期，且擔保品鑑價日小於等於核准日期的那筆資料
        sql += "                   , NVL(CE_later.\"EvaDate\",0) "; // 第2段. 若第1段抓不到資料，才是改為抓鑑價日期最接近核准日期的那筆評估淨值
		sql += "          )                               AS \"Seq\" ";
		sql += "        , NVL(CE_early.\"EvaNetWorth\",NVL(CE_later.\"EvaNetWorth\",0)) ";
		sql += "                                          AS \"EvaNetWorth\" ";
		sql += "   FROM \"ClFac\" CF ";
		sql += "   LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = CF.\"CustNo\" ";
		sql += "                            AND FAC.\"FacmNo\" = CF.\"FacmNo\" ";
		sql += "   LEFT JOIN \"FacCaseAppl\" CAS ON CAS.\"ApplNo\" = CF.\"ApproveNo\" ";	
		sql += "   LEFT JOIN \"ClEva\" CE_early ON CE_early.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                               AND CE_early.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                               AND CE_early.\"ClNo\" = CF.\"ClNo\" ";
		sql += "                               AND CE_early.\"EvaDate\" <= CAS.\"ApproveDate\" ";
		sql += "   LEFT JOIN \"ClEva\" CE_later ON CE_later.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                               AND CE_later.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                               AND CE_later.\"ClNo\" = CF.\"ClNo\" ";
		sql += "                               AND CE_later.\"EvaDate\" > CAS.\"ApproveDate\" ";
		sql += "                               AND NVL(CE_early.\"EvaDate\",0) = 0 "; // 若第1段串不到,才串第2段
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
		sql += "        ELSE TRUNC(FM.\"LineAmt\" / CS.\"EvaNetWorth\", 2)";
		sql += "        END                        AS \"LoanRatio\" "; // 貸款成數
		sql += " FROM  \"MonthlyLoanBal\" MLB ";
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

}