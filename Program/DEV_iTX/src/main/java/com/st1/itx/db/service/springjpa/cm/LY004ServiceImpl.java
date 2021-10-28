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

@Service("LY004ServiceImpl")
@Repository
public class LY004ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int inputYearMonth, TitaVo titaVo) throws Exception {
		this.info("LY004ServiceImpl findAll ");

		// placeholder, 等規格確認
		// 暫時預定 F0~F25

		String sql = "";

		sql += " SELECT S2.\"LoanTotal\"                          AS F0 "; // 逾放金額：本期末餘額
		sql += "       ,S2.\"LoanTotal\" - S3.\"LoanTotal\"       AS F1 "; // 逾放金額：較上期末增減金額
		sql += "       ,S0.\"LoanTotal\"                          AS F2 "; // 放款總額：本期末餘額
		sql += "       ,S0.\"LoanTotal\" - S1.\"LoanTotal\"       AS F3 "; // 放款總額：較上期末增減餘額
		sql += "       ,CASE ";
		sql += "          WHEN S0.\"LoanTotal\" > 0 ";
		sql += "          THEN S2.\"LoanTotal\" / S0.\"LoanTotal\" ";
		sql += "        ELSE 0 END                                AS F4 "; // 逾放比率(%)
		sql += "       ,0 AS F5 "; // 備抵-放款：本期末餘額
		sql += "       ,0 AS F6 "; // 備抵-放款：本期提列金額
		sql += "       ,0 AS F7 "; // 備抵-其他：本期末餘額
		sql += "       ,0 AS F8 "; // 備抵-其他：本期提列金額
		sql += "       ,0 AS F9 "; // 備抵-營損：本期末餘額
		sql += "       ,0 AS F10 "; // 備抵-營損：本期提列金額
		sql += "       ,0 AS F11 "; // 備抵-合計：本期末餘額
		sql += "       ,0 AS F12 "; // 備抵-合計：本期提列金額
		sql += "       ,0 AS F13 "; // 營業稅降低盈餘：本期增加金額
		sql += "       ,0 AS F14 "; // 營業稅降低盈餘：累計增加金額
		sql += "       ,0 AS F15 "; // 轉銷-前一年轉銷呆帳總金額
		sql += "       ,0 AS F16 "; // 轉銷-放款：減少備抵呆帳金額
		sql += "       ,0 AS F17 "; // 轉銷-放款：直接認列損失金額
		sql += "       ,0 AS F18 "; // 轉銷-其他：減少備抵呆帳金額
		sql += "       ,0 AS F19 "; // 轉銷-其他：直接認列損失金額
		sql += "       ,0 AS F20 "; // 轉銷-營業損失準備本期沖銷數
		sql += "       ,0 AS F21 "; // 轉銷-本期轉（沖）銷呆帳金額合計
		sql += "       ,0 AS F22 "; // 轉銷-累計轉（沖）銷呆帳金額合計
		sql += "       ,0 AS F23 "; // 轉銷-本期常務董（理）事會決議通過日期
		sql += "       ,0 AS F24 "; // 轉為特別準備金
		sql += "       ,0 AS F25 "; // 轉銷本月月底持有財務困難公司有價證券餘額
		sql += " FROM ( ";
		sql += "     SELECT SUM(\"LoanBal\") AS \"LoanTotal\" ";
		sql += "     FROM \"MonthlyLM052AssetClass\" ";
		sql += "     WHERE \"YearMonth\" = :inputYearMonth ";
		sql += "       AND \"AssetClassNo\" IN ('11','12','21','22','23','3','4','5','6') ";
		sql += " ) S0 "; // 取本月放款總額
		sql += " , ( ";
		sql += "     SELECT SUM(\"LoanBal\") AS \"LoanTotal\" ";
		sql += "     FROM \"MonthlyLM052AssetClass\" ";
		sql += "     WHERE \"YearMonth\" = \"Fn_GetLastMonth\"(:inputYearMonth) ";
		sql += "       AND \"AssetClassNo\" IN ('11','12','21','22','23','3','4','5','6') ";
		sql += "   ) S1"; // 取上月放款總額
		sql += " , ( ";
		sql += "     SELECT SUM(\"LoanBal\") AS \"LoanTotal\" ";
		sql += "     FROM \"MonthlyLM052Ovdu\" ";
		sql += "     WHERE \"YearMonth\" = \"Fn_GetLastMonth\"(:inputYearMonth) ";
		sql += "       AND \"OvduNo\" IN ('1','2','3') ";
		sql += "   ) S2"; // 取本月逾放總額
		sql += " , ( ";
		sql += "     SELECT SUM(\"LoanBal\") AS \"LoanTotal\" ";
		sql += "     FROM \"MonthlyLM052Ovdu\" ";
		sql += "     WHERE \"YearMonth\" = \"Fn_GetLastMonth\"(:inputYearMonth) ";
		sql += "       AND \"OvduNo\" IN ('1','2','3') ";
		sql += "   ) S3"; // 取上月逾放總額

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("inputYearMonth", inputYearMonth);

		return this.convertToMap(query);
	}

}