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
public class L9704ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int lastMonth, int thisMonth, TitaVo titaVo) throws Exception {
		this.info("L9704ServiceImpl.findAll ");

		if (lastMonth == 0) {
			this.error("L9704ServiceImpl.findAll lastMonth = 0");
			return null;
		}

		if (thisMonth == 0) {
			this.error("L9704ServiceImpl.findAll thisMonth = 0");
			return null;
		}

		String sql = " ";
		sql += " SELECT F.\"CustNo\" "; // F0 戶號
		sql += "      , F.\"FacmNo\" "; // F1 額度
		sql += "      , \"Fn_ParseEOL\"(C.\"CustName\", 0) AS \"CustName\" "; // F2 戶名/公司名稱
		sql += "      , F.\"FirstDrawdownDate\" "; // F3 初貸日
		sql += "      , NVL(MFBOrder.\"PrevIntDate\", L.\"PrevIntDate\") AS \"PrevPayIntDate\" "; // F4 繳息迄日：MFB都沒有再取 CollList - 如果本月已還款解催收，CollList 日期會不對
		sql += "      , F.\"AcctCode\" "; // F5 核准科目
		sql += "      , CASE WHEN GREATEST(NVL(THISM.\"OvduDate\", 0), NVL(LASTM.\"OvduDate\", 0)) > 0";
		sql += "             THEN GREATEST(NVL(THISM.\"OvduDate\", 0), NVL(LASTM.\"OvduDate\", 0)) ";
		sql += "             ELSE NVL(LO.\"OvduDate\", 0) END AS \"OvduDate\" "; // F6 轉催收日期 優先取 THISM > LASTM > LoanOverdue
		sql += "      , NVL(LASTM.\"OvduPrinBal\",0)                AS \"LastOvduPrinBal\" "; // F7 上月催收本金餘額
		sql += "      , NVL(LASTM.\"OvduIntBal\" + LASTM.\"OvduBreachBal\",0) ";
		sql += "                                                    AS \"LastOvduIntBal\" "; // F8 上月催收利息餘額 + 上月催收違約金餘額
		sql += "      , NVL(THISM.\"OvduPrinBal\",0)                AS \"ThisOvduPrinBal\" "; // F9 本月催收本金餘額
		sql += "      , NVL(THISM.\"OvduIntBal\" + THISM.\"OvduBreachBal\",0) ";
		sql += "                                                    AS \"ThisOvduIntBal\" "; // F10 本月催收利息餘額 + 上月催收違約金餘額
		sql += "      , E.\"Fullname\" "; // F11 催收人員姓名
		sql += "      , CT.\"CityItem\" "; // F12 地區別名稱
		sql += "      , NVL(DF.\"Item\", '一般帳戶')                AS \"AcBookItem\" "; // F13 帳冊別中文
		sql += " FROM \"FacMain\" F ";
		sql += " LEFT JOIN \"CollList\" L ON L.\"CustNo\" = F.\"CustNo\" ";
		sql += "                         AND L.\"FacmNo\" = F.\"FacmNo\" ";
		sql += " LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = L.\"AccCollPsn\" ";
		sql += " LEFT JOIN \"MonthlyFacBal\" LASTM ON LASTM.\"CustNo\" = F.\"CustNo\" ";
		sql += "                                  AND LASTM.\"FacmNo\" = F.\"FacmNo\" ";
		sql += "                                  AND LASTM.\"YearMonth\" = :lastMonth ";
		sql += " LEFT JOIN \"MonthlyFacBal\" THISM ON THISM.\"CustNo\" = F.\"CustNo\" ";
		sql += "                                  AND THISM.\"FacmNo\" = F.\"FacmNo\" ";
		sql += "                                  AND THISM.\"YearMonth\" = :thisMonth ";
		sql += " LEFT JOIN ( SELECT \"CustNo\" ";
		sql += "                   ,\"FacmNo\" ";
		sql += "                   ,\"PrevIntDate\" ";
		sql += "                   ,ROW_NUMBER() over (PARTITION BY \"CustNo\" ";
		sql += "                                                   ,\"FacmNo\" ";
		sql += "                                       ORDER BY \"YearMonth\" DESC) \"Seq\" ";
		sql += "             FROM \"MonthlyFacBal\" ";
		sql += "             WHERE \"PrevIntDate\" > 0 ";
		sql += "               AND \"YearMonth\" <= :lastMonth ) MFBOrder ON MFBOrder.\"Seq\" = 1 ";               // 因為有可能上個月催收、這個月還款解催收了
		sql += "                                                         AND MFBOrder.\"CustNo\" = F.\"CustNo\" "; // 這種情況取 thisMonth 的日期就會不對
		sql += "                                                         AND MFBOrder.\"FacmNo\" = F.\"FacmNo\" "; // 如果依然還是催收，表示 thisMonth 這欄位會是 0
		sql += " LEFT JOIN \"CdCity\" CT ON CT.\"CityCode\" = NVL(LASTM.\"CityCode\", THISM.\"CityCode\") ";       // 所以在任何情況下，都是從 lastMonth 開始往回取第一個非 0 的 PrevIntDate
		sql += " LEFT JOIN \"CdCode\" DF ON DF.\"DefCode\" = 'AcSubBookCode' ";
		sql += "                        AND DF.\"Code\" = NVL(LASTM.\"AcSubBookCode\", THISM.\"AcSubBookCode\") ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = F.\"CustNo\" ";
		sql += " LEFT JOIN (SELECT \"CustNo\" ";
		sql += "                  ,\"FacmNo\" ";
		sql += "                  ,\"BormNo\" ";
		sql += "                  ,\"LastOvduNo\" ";
		sql += "                  ,\"PrevPayIntDate\" ";
		sql += "                  ,ROW_NUMBER() OVER (ORDER BY \"PrevPayIntDate\" ASC) \"Seq\" ";
		sql += "            FROM \"LoanBorMain\" LBM ";
		sql += "            WHERE TRUNC(\"LastEntDy\" / 100, 0) = :thisMonth) LBM ON LBM.\"CustNo\" = F.\"CustNo\" ";
		sql += "                                                                 AND LBM.\"FacmNo\" = F.\"FacmNo\" ";
		sql += "                                                                 AND LBM.\"Seq\" = 1 ";
		sql += " LEFT JOIN \"LoanOverdue\" LO ON LO.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "                             AND LO.\"FacmNo\" = LBM.\"FacmNo\" ";
		sql += "                             AND LO.\"BormNo\" = LBM.\"BormNo\" ";
		sql += "                             AND LO.\"OvduNo\" = LBM.\"LastOvduNo\" ";
		sql += " WHERE NVL(LASTM.\"OvduBal\", 0) + NVL(THISM.\"OvduBal\", 0) > 0 "; // 上月催收餘額 或 本月催收餘額 > 0 進表
		sql += " ORDER BY NVL(LASTM.\"AcSubBookCode\", THISM.\"AcSubBookCode\") ";
		sql += "        , F.\"CustNo\" ";
		sql += "        , F.\"FacmNo\" ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("lastMonth", lastMonth);
		query.setParameter("thisMonth", thisMonth);

		return this.convertToMap(query);
	}

}