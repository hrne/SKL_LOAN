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
public class L9726ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(String inputYear, TitaVo titaVo) throws Exception {
		this.info("l9722.findAll ");

		this.info("inputYear = " + inputYear);

		String sql = " ";
		sql += " SELECT LPAD(NVL(LBM.\"CustNo\",0),7,'0')  ";
		sql += "        || '-' ";
		sql += "        || LPAD(NVL(LBM.\"FacmNo\",0),3,'0') ";
		sql += "                                   AS CustNo "; // F0 戶號
		sql += "      , CM.\"CustName\" "; // F1 戶名
		sql += "      , CM.\"CustId\" "; // F2 統一編號
		sql += "      , LBM.\"DrawdownDate\" "; // F3 放款日期
		sql += "      , FAC.\"LineAmt\" "; // F4 核准額度
		sql += "      , LBM.\"DrawdownAmt\" "; // F5 撥貸金額
		sql += "      , FAC.\"UtilBal\" "; // F6 目前餘額
		sql += "      , FAC.\"LineAmt\" - FAC.\"UtilBal\" AS \"AvailableBal\" "; // -- F7 可動用餘額
		sql += "      , LBM.\"StoreRate\" "; // F8 目前利率
		sql += "      , CASE ";
		sql += "          WHEN CL.\"EvaTotal\" > 0 ";
		sql += "          THEN ROUND(FAC.\"LineAmt\" / CL.\"EvaTotal\", 8) ";
		sql += "        ELSE 0 END                 AS LTV "; // F9 放款成數
		sql += "      , FAC.\"RecycleCode\" "; // F10 循環動用
		sql += "      , CL2.\"ClCode1Item\" "; // F11 擔保品類別
		sql += "      , CCD.\"Item\" AS \"RuleCode\" "; // F12 管制代碼
		sql += "      , CASE ";
		sql += "          WHEN CM.\"EntCode\" = 1 ";
		sql += "          THEN '法人' ";
		sql += "        ELSE '自然人' END AS \"IdType\" "; // F13 擔保品
		sql += "      , CL2.\"ClCityItem\" "; // F14 地區別
		sql += "      , NVL(CE.\"Fullname\",FAC.\"BusinessOfficer\") AS \"BusinessOfficer\" "; // F15
		sql += " FROM (SELECT LBM.\"CustNo\" ";
		sql += "            , LBM.\"FacmNo\" ";
		sql += "            , LBM.\"DrawdownDate\" ";
		sql += "            , SUM(LBM.\"DrawdownAmt\") AS \"DrawdownAmt\" ";
		sql += "            , MAX(LBM.\"StoreRate\")   AS \"StoreRate\" ";
		sql += "       FROM \"LoanBorMain\" LBM ";
		sql += "       LEFT JOIN \"CdWorkMonth\" CWM ON CWM.\"StartDate\" <= LBM.\"DrawdownDate\" ";
		sql += "                                    AND CWM.\"EndDate\" >= LBM.\"DrawdownDate\" ";
		sql += "       WHERE NVL(CWM.\"Year\",0) = :inputYear  "; // -- 篩選當年度(畫面為民國年，傳入SQL時要轉西元年)
		sql += "       GROUP BY LBM.\"CustNo\" ";
		sql += "              , LBM.\"FacmNo\" ";
		sql += "              , LBM.\"DrawdownDate\" ";
		sql += "      ) LBM ";
		sql += " LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "                        AND FAC.\"FacmNo\" = LBM.\"FacmNo\" ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = LBM.\"CustNo\" ";
		sql += " LEFT JOIN (SELECT CF.\"CustNo\" ";
		sql += "                 , CF.\"FacmNo\" ";
		sql += "                 , SUM(CASE ";
		sql += "                         WHEN CI.\"EvaNetWorth\" > 0 ";
		sql += "                         THEN CI.\"EvaNetWorth\" ";
		sql += "                       ELSE CL.\"EvaAmt\" END) AS \"EvaTotal\" ";
		sql += "            FROM \"ClFac\" CF ";
		sql += "            LEFT JOIN \"ClMain\" CL ON CL.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                                 AND CL.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                                 AND CL.\"ClNo\" = CF.\"ClNo\" ";
		sql += "            LEFT JOIN \"ClImm\" CI ON CI.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                                 AND CI.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                                 AND CI.\"ClNo\" = CF.\"ClNo\" ";
		sql += "            GROUP BY CF.\"CustNo\" ";
		sql += "                   , CF.\"FacmNo\" ";
		sql += "           ) CL ON CL.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "               AND Cl.\"FacmNo\" = LBM.\"FacmNo\" ";
		sql += " LEFT JOIN (SELECT CF.\"CustNo\" ";
		sql += "                 , CF.\"FacmNo\" ";
		sql += "                 , CC.\"CityItem\" AS \"ClCityItem\" ";
		sql += "                 , CCD.\"Item\"    AS \"ClCode1Item\" ";
		sql += "            FROM \"ClFac\" CF ";
		sql += "            LEFT JOIN \"ClMain\" CL ON CL.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                                 AND CL.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                                 AND CL.\"ClNo\" = CF.\"ClNo\" ";
		sql += "            LEFT JOIN \"CdCity\" CC ON CC.\"CityCode\" = CL.\"CityCode\" ";
		sql += "            LEFT JOIN \"CdCode\" CCD ON CCD.\"DefCode\" = 'ClCode1' ";
		sql += "                                  AND CCD.\"Code\" = CL.\"ClCode1\" ";
		sql += "            WHERE CF.\"MainFlag\" = 'Y' ";
		sql += "           ) CL2 ON CL2.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "                AND CL2.\"FacmNo\" = LBM.\"FacmNo\" ";
		sql += " LEFT JOIN \"CdCode\" CCD ON CCD.\"DefCode\" = 'RuleCode' ";
		sql += "                       AND CCD.\"Code\" = FAC.\"RuleCode\" ";
		sql += " LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\" = FAC.\"BusinessOfficer\" ";
		sql += " WHERE CM.\"EntCode\" IN ('1','2') ";
		sql += " ORDER BY LBM.\"DrawdownDate\" ";
		sql += "        , LBM.\"CustNo\" ";
		sql += "        , LBM.\"FacmNo\" ";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("inputYear", inputYear);

		return this.convertToMap(query.getResultList());
	}

}