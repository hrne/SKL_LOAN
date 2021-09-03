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
/* 逾期放款明細 */
public class LM048ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> queryDetail(int inputYearMonth, String entLoanBalLimit, TitaVo titaVo) {

		this.info("LM048ServiceImpl findAll ");
		this.info("LM048ServiceImpl inputYearMonth = " + inputYearMonth);
		this.info("LM048ServiceImpl entLoanBalLimit = " + entLoanBalLimit);

		String sql = " ";
		sql += " SELECT CASE ";
		sql += "          WHEN CDI.\"IndustryRating\" IS NOT NULL "; // -- 有評等
		sql += "          THEN CDI.\"IndustryRating\" ";
		sql += "          WHEN ML.\"LoanBal\" > :entLoanBalLimit "; // -- 企業放款餘額大於限額
		sql += "          THEN 'B_Empty' ";
		sql += "          WHEN GP.\"GroupName\" IS NOT NULL "; // -- 有同一關係企業或集團戶
		sql += "          THEN 'B_Empty' ";
		sql += "        ELSE 'B_Else' "; // -- B評等其他
		sql += "        END                         AS Rating "; // -- F0 評等
		sql += "      , CDI.\"IndustryItem\"        AS IndustryItem "; // -- F1 行業別名稱
		sql += "      , CM.\"CustNo\"               AS CustNo "; // -- F2 戶號
		sql += "      , CM.\"CustName\"                           "; // -- F3 戶名
		sql += "      , FAC.\"LineAmt\"                           "; // -- F4 核貸金額
		sql += "      , ML.\"LoanBal\"                            "; // -- F5 放款本金餘額
		sql += "      , ML.\"StoreRate\"                          "; // -- F6 利率(%)
		sql += "      , LBM.\"MaturityDate\"                      "; // -- F7 到期日
		sql += "      , LBM.\"PrevPayIntDate\"                    "; // -- F8 繳息迄日
		sql += "      , GP.\"GroupName\"                          "; // -- F9 同一關係企業或集團名稱
		sql += "      , GP.\"GroupPercentage\"                    "; // -- F10 同一關係企業或集團核貸金額占淨值比例
		sql += "      , ROUND(FAC.\"LineAmt\" / CDV.\"Totalequity\" * 100 , 2) ";
		sql += "                            AS \"CustPercentage\" "; // -- F11 單一授信對象核貸金額占淨值比例
		sql += " FROM \"CustMain\" CM ";
		sql += " LEFT JOIN \"CdIndustry\" CDI ON CDI.\"IndustryCode\" = CM.\"IndustryCode\" ";
		sql += " LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = CM.\"CustNo\" ";
		sql += " LEFT JOIN \"CdVarValue\" CDV ON CDV.\"YearMonth\" = :inputYearMonth ";
		sql += " LEFT JOIN ( SELECT \"CustNo\" ";
		sql += "                  , \"FacmNo\" ";
		sql += "                  , SUM(\"LoanBalance\") AS \"LoanBal\" ";
		sql += "                  , MAX(\"StoreRate\") AS \"StoreRate\" ";
		sql += "             FROM \"MonthlyLoanBal\" ";
		sql += "             WHERE \"LoanBalance\" > 0 ";
		sql += "               AND \"YearMonth\" = :inputYearMonth ";
		sql += "             GROUP BY \"CustNo\" ";
		sql += "                    , \"FacmNo\" ";
		sql += "           ) ML ON ML.\"CustNo\" = FAC.\"CustNo\" ";
		sql += "               AND ML.\"FacmNo\" = FAC.\"FacmNo\" ";
		sql += " LEFT JOIN ( SELECT \"CustNo\" ";
		sql += "                  , \"FacmNo\" ";
		sql += "                  , MAX(\"MaturityDate\") AS \"MaturityDate\" ";
		sql += "                  , MIN(\"PrevPayIntDate\") AS \"PrevPayIntDate\" ";
		sql += "             FROM \"LoanBorMain\" ";
		sql += "             WHERE \"LoanBal\" > 0 ";
		sql += "               AND TRUNC(\"DrawdownDate\" / 100) <= :inputYearMonth ";
		sql += "             GROUP BY \"CustNo\" ";
		sql += "                    , \"FacmNo\" ";
		sql += "           ) LBM ON LBM.\"CustNo\" = FAC.\"CustNo\" ";
		sql += "                AND LBM.\"FacmNo\" = FAC.\"FacmNo\" ";
		sql += " LEFT JOIN ( SELECT CRD.\"RelId\" ";
		sql += "                  , CRM.\"CustRelName\" || '關係企業' AS \"GroupName\" ";
		sql += "                  , ROUND(GP.\"GroupLineAmt\" / CDV.\"Totalequity\" * 100 , 2) ";
		sql += "                                                   AS \"GroupPercentage\" ";
		sql += "             FROM \"CustRelMain\" CRM ";
		sql += "             LEFT JOIN \"CustRelDetail\" CRD ON CRD.\"CustRelMainUKey\" = CRM.\"Ukey\" ";
		sql += "             LEFT JOIN \"CdVarValue\" CDV ON CDV.\"YearMonth\" = :inputYearMonth ";
		sql += "             LEFT JOIN ( SELECT CM.\"CustId\" ";
		sql += "                               , SUM(FAC.\"LineAmt\") AS \"LineAmt\" ";
		sql += "                         FROM \"FacMain\" FAC  ";
		sql += "                         LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = FAC.\"CustNo\" ";
		sql += "                         WHERE TRUNC(FAC.\"FirstDrawdownDate\" / 100) <= :inputYearMonth ";
		sql += "                         GROUP BY CM.\"CustId\" ";
		sql += "                       ) FAC ON FAC.\"CustId\" = CRD.\"RelId\" ";
		sql += "             LEFT JOIN ( "; // -- 計算整組餘額
		sql += "                         SELECT CRM.\"Ukey\" ";
		sql += "                              , SUM(NVL(FAC.\"LineAmt\",0)) AS \"GroupLineAmt\" "; // -- 放款餘額
		sql += "                         FROM \"CustRelMain\" CRM ";
		sql += "                         LEFT JOIN \"CustRelDetail\" CRD ON CRD.\"CustRelMainUKey\" = CRM.\"Ukey\" ";
		sql += "                         LEFT JOIN ( SELECT CM.\"CustId\" ";
		sql += "                                          , SUM(FAC.\"LineAmt\") AS \"LineAmt\" ";
		sql += "                                     FROM \"FacMain\" FAC  ";
		sql += "                                     LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = FAC.\"CustNo\" ";
		sql += "                                     WHERE TRUNC(FAC.\"FirstDrawdownDate\" / 100) <= :inputYearMonth ";
		sql += "                                     GROUP BY CM.\"CustId\" ";
		sql += "                                   ) FAC ON FAC.\"CustId\" = CRD.\"RelId\" ";
		sql += "                         WHERE LENGTHB(CRM.\"CustRelId\") = 8 "; // -- 篩選關係企業
		sql += "                           AND LENGTHB(CRD.\"RelId\") = 8 "; // -- 篩選關係企業
		sql += "                         GROUP BY CRM.\"Ukey\" ";
		sql += "                       ) GP ON GP.\"Ukey\" = CRM.\"Ukey\" ";
		sql += "             WHERE LENGTHB(CRM.\"CustRelId\") = 8 "; // -- 篩選關係企業
		sql += "               AND LENGTHB(CRD.\"RelId\") = 8 "; // -- 篩選關係企業
		sql += "               AND NVL(FAC.\"LineAmt\",0) > 0 ";
		sql += "               AND GP.\"GroupLineAmt\" > NVL(FAC.\"LineAmt\",0) "; // -- 兩筆以上才進表
		sql += "           ) GP ON GP.\"RelId\" = CM.\"CustId\" ";
		sql += " WHERE CM.\"EntCode\" = '1' "; // -- 企金別為1
		sql += "   AND ML.\"LoanBal\" > 0 "; // -- 當月底有放款餘額
		sql += " ORDER BY Rating ";
		sql += "        , CustNo ";
		sql += "        , FAC.\"FacmNo\" ";

		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", inputYearMonth);
		query.setParameter("entLoanBalLimit", entLoanBalLimit);

		return this.convertToMap(query.getResultList());
	}

}