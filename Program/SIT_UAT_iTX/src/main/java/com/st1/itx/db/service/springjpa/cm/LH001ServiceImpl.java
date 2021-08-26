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
public class LH001ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryA(TitaVo titaVo) throws Exception {
		this.info("lH001.queryA ");

		String sql = " ";
		// -- 表1:同一自然人或法人
		// -- 同一自然人或同一法人（含二、同一關係人）3000萬元(含)以上明細。
		// -- ??? 3000萬以上條件取消
		sql += " SELECT CRD.\"RelName\" "; // -- 姓名、名稱
		sql += "      , CRD.\"RelId\" "; // -- 身分證號碼/統一編號
		sql += "      , NVL(LBM.\"LoanBal\",0) AS \"LoanBal\" "; // -- 放款餘額
		sql += "      , NVL(LBM.\"LoanBal\",0) AS \"GroupLoanBal\" "; // -- 放款餘額
		sql += "      , CRM.\"Ukey\" ";
		sql += " FROM \"CustRelMain\" CRM ";
		sql += " LEFT JOIN \"CustRelDetail\" CRD ON CRD.\"CustRelMainUKey\" = CRM.\"Ukey\" ";
		sql += " LEFT JOIN ( SELECT CM.\"CustId\" ";
		sql += "                  , SUM(LBM.\"LoanBal\") AS \"LoanBal\" ";
		sql += "             FROM \"LoanBorMain\" LBM  ";
		sql += "             LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "             GROUP BY CM.\"CustId\" ";
		sql += "           ) LBM ON LBM.\"CustId\" = CRD.\"RelId\" ";
		// sql += " WHERE NVL(LBM.\"LoanBal\",0) >= 30000000 "; // -- ??? 3000萬以上條件取消
		sql += " WHERE NVL(LBM.\"LoanBal\",0) > 0 ";
		sql += " ORDER BY NVL(LBM.\"LoanBal\",0) DESC ";
		sql += "        , CRD.\"RelId\" ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		// 設定參數
//		query.setParameter("defno", defno);

		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryB(TitaVo titaVo) throws Exception {
		this.info("lH001.queryB ");

		// -- 表2:同一關係人
		// -- 同一關係人3000萬元(含)以上明細。
		// -- ??? 3000萬以上條件取消
		String sql = " ";
		sql += " SELECT CRD.\"RelName\" "; // -- 姓名、名稱
		sql += "        || '及其同一關係人' AS \"RelName\" ";
		sql += "      , CRD.\"RelId\" "; // -- 身分證號碼/統一編號
		sql += "      , NVL(LBM.\"LoanBal\",0) AS \"LoanBal\" "; // -- 放款餘額
		sql += "      , GP.\"GroupLoanBal\" "; // -- 整組放款餘額
		sql += "      , CRM.\"Ukey\" ";
		sql += " FROM \"CustRelMain\" CRM ";
		sql += " LEFT JOIN \"CustRelDetail\" CRD ON CRD.\"CustRelMainUKey\" = CRM.\"Ukey\" ";
		sql += " LEFT JOIN ( SELECT CM.\"CustId\" ";
		sql += "                  , SUM(LBM.\"LoanBal\") AS \"LoanBal\" ";
		sql += "             FROM \"LoanBorMain\" LBM  ";
		sql += "             LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "             GROUP BY CM.\"CustId\" ";
		sql += "           ) LBM ON LBM.\"CustId\" = CRD.\"RelId\" ";
		sql += " LEFT JOIN ( "; // -- 計算整組餘額
		sql += "             SELECT CRM.\"Ukey\" ";
		sql += "                  , SUM(NVL(LBM.\"LoanBal\",0)) AS \"GroupLoanBal\" "; // -- 放款餘額
		sql += "             FROM \"CustRelMain\" CRM ";
		sql += "             LEFT JOIN \"CustRelDetail\" CRD ON CRD.\"CustRelMainUKey\" = CRM.\"Ukey\" ";
		sql += "             LEFT JOIN ( SELECT CM.\"CustId\" ";
		sql += "                              , SUM(LBM.\"LoanBal\") AS \"LoanBal\" ";
		sql += "                         FROM \"LoanBorMain\" LBM  ";
		sql += "                         LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "                         GROUP BY CM.\"CustId\" ";
		sql += "                       ) LBM ON LBM.\"CustId\" = CRD.\"RelId\" ";
		sql += "             WHERE ( LENGTHB(CRM.\"CustRelId\") >= 10 "; // -- 篩選同一關係人
		sql += "                     OR LENGTHB(CRD.\"RelId\") >= 10 "; // -- 篩選同一關係人
		sql += "                   ) "; // -- 篩選同一關係人
		sql += "             GROUP BY CRM.\"Ukey\" ";
		sql += "           ) GP ON GP.\"Ukey\" = CRM.\"Ukey\" ";
		sql += " WHERE ( LENGTHB(CRM.\"CustRelId\") >= 10 "; // -- 篩選同一關係人
		sql += "         OR LENGTHB(CRD.\"RelId\") >= 10 "; // -- 篩選同一關係人
		sql += "       ) "; // -- 篩選同一關係人
		// sql += " AND NVL(LBM.\"LoanBal\",0) >= 30000000 "; // -- ??? 3000萬以上條件取消
		sql += "   AND NVL(LBM.\"LoanBal\",0) > 0 ";
		sql += "   AND GP.\"GroupLoanBal\" > NVL(LBM.\"LoanBal\",0) "; // 兩筆以上才進表
		sql += " ORDER BY GP.\"GroupLoanBal\" DESC ";
		sql += "        , CRM.\"Ukey\" ";
		sql += "        , CRD.\"RelId\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		// 設定參數
//		query.setParameter("defno", defno);

		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryC(TitaVo titaVo) throws Exception {
		this.info("lH001.queryC ");

		String sql = " ";

		// -- 表3:同一關係企業
		// -- 同一關係人3000萬元(含)以上明細。
		// -- ??? 3000萬以上條件取消
		sql += " SELECT CRD.\"RelName\" "; // -- 姓名、名稱
		sql += "        || '及其同一關係企業'  AS \"RelName\" ";
		sql += "      , CRD.\"RelId\" "; // -- 身分證號碼/統一編號
		sql += "      , NVL(LBM.\"LoanBal\",0) AS \"LoanBal\" "; // -- 放款餘額
		sql += "      , GP.\"GroupLoanBal\" "; // -- 整組放款餘額
		sql += "      , CRM.\"Ukey\" ";
		sql += " FROM \"CustRelMain\" CRM ";
		sql += " LEFT JOIN \"CustRelDetail\" CRD ON CRD.\"CustRelMainUKey\" = CRM.\"Ukey\" ";
		sql += " LEFT JOIN ( SELECT CM.\"CustId\" ";
		sql += "                  , SUM(LBM.\"LoanBal\") AS \"LoanBal\" ";
		sql += "             FROM \"LoanBorMain\" LBM  ";
		sql += "             LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "             GROUP BY CM.\"CustId\" ";
		sql += "           ) LBM ON LBM.\"CustId\" = CRD.\"RelId\" ";
		sql += " LEFT JOIN ( "; // -- 計算整組餘額
		sql += "             SELECT CRM.\"Ukey\" ";
		sql += "                  , SUM(NVL(LBM.\"LoanBal\",0)) AS \"GroupLoanBal\" "; // -- 放款餘額
		sql += "             FROM \"CustRelMain\" CRM ";
		sql += "             LEFT JOIN \"CustRelDetail\" CRD ON CRD.\"CustRelMainUKey\" = CRM.\"Ukey\" ";
		sql += "             LEFT JOIN ( SELECT CM.\"CustId\" ";
		sql += "                              , SUM(LBM.\"LoanBal\") AS \"LoanBal\" ";
		sql += "                         FROM \"LoanBorMain\" LBM  ";
		sql += "                         LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "                         GROUP BY CM.\"CustId\" ";
		sql += "                       ) LBM ON LBM.\"CustId\" = CRD.\"RelId\" ";
		sql += "             WHERE LENGTHB(CRM.\"CustRelId\") = 8 "; // -- 篩選同一關係企業
		sql += "               AND LENGTHB(CRD.\"RelId\") = 8 "; // -- 篩選同一關係企業
		sql += "             GROUP BY CRM.\"Ukey\" ";
		sql += "           ) GP ON GP.\"Ukey\" = CRM.\"Ukey\" ";
		sql += " WHERE LENGTHB(CRM.\"CustRelId\") = 8 "; // -- 篩選同一關係企業
		sql += "   AND LENGTHB(CRD.\"RelId\") = 8 "; // -- 篩選同一關係企業
		// sql += " AND NVL(LBM.\"LoanBal\",0) >= 30000000 "; // -- ??? 3000萬以上條件取消
		sql += "   AND NVL(LBM.\"LoanBal\",0) > 0 ";
		sql += "   AND GP.\"GroupLoanBal\" > NVL(LBM.\"LoanBal\",0) "; // 兩筆以上才進表
		sql += " ORDER BY GP.\"GroupLoanBal\" DESC ";
		sql += "        , CRM.\"Ukey\" ";
		sql += "        , CRD.\"RelId\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		// 設定參數
//		query.setParameter("defno", defno);

		return this.convertToMap(query.getResultList());
	}
}