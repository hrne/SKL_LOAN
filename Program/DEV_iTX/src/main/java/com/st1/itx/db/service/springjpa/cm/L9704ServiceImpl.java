package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger logger = LoggerFactory.getLogger(L9704ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(int lastMonth, int thisMonth, TitaVo titaVo) throws Exception {
		logger.info("L9704ServiceImpl.findAll ");

		if (lastMonth == 0) {
			logger.error("L9704ServiceImpl.findAll lastMonth = 0");
			return null;
		}

		if (thisMonth == 0) {
			logger.error("L9704ServiceImpl.findAll thisMonth = 0");
			return null;
		}

		String sql = " ";
		sql += " SELECT F.\"CustNo\"                                                     "; // -- F0 戶號
		sql += "      , F.\"FacmNo\"                                                     "; // -- F1 額度
		sql += "      , C.\"CustName\"                                                   "; // -- F2 戶名/公司名稱
		sql += "      , F.\"FirstDrawdownDate\"                                          "; // -- F3 初貸日
		sql += "      , DECODE(L.\"PrevIntDate\", 0, LBM.\"PrevPayIntDate\", L.\"PrevIntDate\") F4"; // -- F4 繳息迄日
		sql += "      , F.\"AcctCode\"                                                   "; // -- F5 核准科目
		sql += "      , NVL(LASTM.\"OvduDate\",THISM.\"OvduDate\") AS \"OvduDate\"       "; // -- F6 轉催收日期
		sql += "      , NVL(LASTM.\"OvduPrinBal\",0)               AS \"LastOvduPrinBal\""; // -- F7 上月催收本金餘額
		sql += "      , NVL(LASTM.\"OvduIntBal\" + LASTm.\"OvduBreachBal\",0) ";
		sql += "                                                   AS \"LastOvduIntBal\" "; // -- F8 上月催收利息餘額 +
																							// 上月催收違約金餘額
		sql += "      , NVL(THISM.\"OvduPrinBal\",0)               AS \"ThisOvduPrinBal\""; // -- F9 本月催收本金餘額
		sql += "      , NVL(THISM.\"OvduIntBal\" + THISm.\"OvduBreachBal\",0) ";
		sql += "                                                   AS \"ThisOvduIntBal\" "; // -- F10 本月催收利息餘額 +
																							// 本月催收違約金餘額
		sql += "      , E.\"Fullname\"                                                   "; // -- F11 催收人員姓名
		sql += "      , CT.\"CityItem\"                                                  "; // -- F12 地區別名稱
		sql += "      , NVL(DF.\"Item\", '一般帳戶')               AS \"AcBookItem\"     "; // -- F13 帳冊別中文
		sql += " FROM \"FacMain\" F ";
		sql += " LEFT JOIN \"CollList\" L ON L.\"CustNo\" = F.\"CustNo\" ";
		sql += "                       AND L.\"FacmNo\" = F.\"FacmNo\" ";
		sql += " LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = L.\"AccCollPsn\" ";
		sql += " LEFT JOIN \"MonthlyFacBal\" LASTM ON LASTM.\"CustNo\" = F.\"CustNo\" ";
		sql += "                                AND LASTM.\"FacmNo\" = F.\"FacmNo\" ";
		sql += "                                AND LASTM.\"YearMonth\" = :lastMonth ";
		sql += " LEFT JOIN \"MonthlyFacBal\" THISM ON THISM.\"CustNo\" = F.\"CustNo\" ";
		sql += "                                AND THISM.\"FacmNo\" = F.\"FacmNo\" ";
		sql += "                                AND THISM.\"YearMonth\" = :thisMonth ";
		sql += " LEFT JOIN \"CdCity\" CT ON CT.\"CityCode\" = NVL(LASTM.\"CityCode\",THISM.\"CityCode\") ";
		sql += " LEFT JOIN \"CdCode\" DF ON DF.\"DefCode\" = 'AcSubBookCode' ";
		sql += "                      AND DF.\"Code\" = NVL(LASTM.\"AcSubBookCode\",THISM.\"AcSubBookCode\") ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = F.\"CustNo\" ";
                sql += " LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = F.\"CustNo\" ";
                sql += "                              AND LBM.\"FacmNo\" = F.\"FacmNo\" ";
                sql += "                              AND LBM.\"LastEntDy\" LIKE :thisMonth || '%' ";
		sql += " WHERE NVL(LASTM.\"OvduBal\",0) + NVL(THISM.\"OvduBal\",0) > 0 "; // -- 上月催收餘額 或 本月催收餘額 > 0 進表
		sql += " ORDER BY NVL(LASTM.\"AcSubBookCode\",THISM.\"AcSubBookCode\") ";
		sql += "        , F.\"CustNo\" ";
		sql += "        , F.\"FacmNo\" ";

		logger.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("lastMonth", lastMonth);
		query.setParameter("thisMonth", thisMonth);

		return this.convertToMap(query.getResultList());
	}

}