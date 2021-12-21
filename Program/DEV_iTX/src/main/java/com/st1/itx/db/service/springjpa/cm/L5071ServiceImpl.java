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
/* 債權案件明細查詢 */
public class L5071ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L5071ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo, int index, int limit) throws Exception {
		logger.info("L5071ServiceImpl.findAll ");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		String sql = " ";

		sql += " SELECT C.\"CustId\"                                                     "; // -- F0 身分證字號
		sql += "      , N.\"CaseKindCode\"                                               "; // -- F1 案件種類
		sql += "      , N.\"CustLoanKind\"                                               "; // -- F2 債權戶別
		sql += "      , N.\"Status\"                                                     "; // -- F3 債權戶況
		sql += "      , N.\"CustNo\"                                                     "; // -- F4 戶號
		sql += "      , N.\"CaseSeq\"                                                    "; // -- F5 案件序號
		sql += "      , N.\"ApplDate\"                                                   "; // -- F6 協商申請日
		sql += "      , N.\"DueAmt\"                                                     "; // -- F7 期款
		sql += "      , N.\"TotalPeriod\"                                                "; // -- F8 期數
		sql += "      , N.\"IntRate\"                                                    "; // -- F9 利率
		sql += "      , N.\"FirstDueDate\"                                               "; // -- F10 首次應繳日
		sql += "      , N.\"LastDueDate\"                                                "; // -- F11 還款結束日
		sql += "      , N.\"IsMainFin\"                                                  "; // -- F12 最大債權
		sql += "      , N.\"MainFinCode\"                                                "; // -- F13 最大債權機構
		sql += "      , N.\"TotalContrAmt\"                                              "; // -- F14 總金額

		sql += " FROM \"NegMain\" N";
		sql += "   LEFT JOIN \"CustMain\" C";
		sql += "          ON C.\"CustNo\" = N.\"CustNo\"";

		if (!"".equals(titaVo.getParam("CustId").trim())) {
			sql += "         AND C.\"CustId\" = :CustId";
		}

		sql += "  WHERE C.\"CustId\" IS NOT NULL";

		if (!"".equals(titaVo.getParam("CaseKindCode").trim())) {
			sql += "   AND N.\"CaseKindCode\" = :CaseKindCode";
		}

		if (!"".equals(titaVo.getParam("CustLoanKind").trim())) {
			sql += "     AND N.\"CustLoanKind\" = :CustLoanKind";
		}

		if (!"".equals(titaVo.getParam("Status").trim())) {
			sql += "     AND N.\"Status\" = :Status";

		}

		sql += "   ORDER BY  C.\"CustId\", N.\"CaseSeq\" ";
		sql += sqlRow;

		logger.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		if (!"".equals(titaVo.getParam("CustId").trim())) {
			query.setParameter("CustId", titaVo.getParam("CustId").trim());
		}
		if (!"".equals(titaVo.getParam("CaseKindCode").trim())) {
			query.setParameter("CaseKindCode", titaVo.getParam("CaseKindCode").trim());
		}
		if (!"".equals(titaVo.getParam("CustLoanKind").trim())) {
			query.setParameter("CustLoanKind", titaVo.getParam("CustLoanKind").trim());
		}
		if (!"".equals(titaVo.getParam("Status").trim())) {
			query.setParameter("Status", titaVo.getParam("Status").trim());
		}
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		return this.convertToMap(query.getResultList());
	}
}