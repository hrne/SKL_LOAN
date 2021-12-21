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
public class LM033ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM033ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> doQuery(TitaVo titaVo) throws Exception {

		String sql = "SELECT FC.\"BranchNo\""; // 單位別
		sql += "            ,FC.\"ApplDate\""; // 申請日期
		sql += "            ,FC.\"ApproveDate\""; // 准駁日期
		sql += "            ,FM.\"CustNo\""; // 戶號
		sql += "            ,FM.\"FacmNo\""; // 額度號碼
		sql += "            ,FM.\"LineAmt\""; // 核准額度
		sql += "            ,FM.\"UtilAmt\""; // 貸出金額
		sql += "            ,FM.\"UtilBal\""; // 已用額度
		sql += "            ,FM.\"RecycleCode\""; // 循環動用
		sql += "            ,FM.\"RecycleDeadline\""; // 循環動用期限
		sql += "            ,CL.\"ClCode1\""; // 擔保品代號1
		sql += "            ,FM.\"ApproveRate\""; // 核准利率
		sql += "            ,CM.\"EntCode\""; // 企金別
		sql += "            ,FC.\"PieceCode\""; // 計件代碼
		sql += "      FROM \"FacCaseAppl\" FC ";
		sql += "      LEFT JOIN \"FacMain\" FM ON FM.\"ApplNo\" = FC.\"ApplNo\"";
		sql += "                              AND FM.\"BranchNo\" = FC.\"BranchNo\"";
		sql += "      LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = FM.\"CustNo\"";
		sql += "                               AND CM.\"BranchNo\" = FC.\"BranchNo\"";
		sql += "      LEFT JOIN \"ClFac\" CL ON CL.\"ApproveNo\" = FC.\"ApplNo\"";
		sql += "                            AND CL.\"MainFlag\" = 'Y'"; // 主要擔保品
		sql += "      WHERE FC.\"BranchNo\" = '0000'"; // 單位別
		sql += "        AND FC.\"ApproveDate\" > 0"; // 已有准駁日期
		sql += "        AND FC.\"ApproveDate\" BETWEEN :inputDateStart AND :inputDateEnd"; // 准駁日期為輸入區間
		sql += "        AND FC.\"ProcessCode\" = '1'"; // 處理情形=1:准
		sql += "        AND NVL(FM.\"CustNo\",0) <> 0"; // 額度檔之戶號不為0
		sql += "      ORDER BY FM.\"CustNo\"";
		sql += "              ,FM.\"FacmNo\"";
		logger.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);

		query.setParameter("inputDateStart", Integer.toString(Integer.parseInt(titaVo.getParam("inputDateStart")) + 19110000));
		query.setParameter("inputDateEnd", Integer.toString(Integer.parseInt(titaVo.getParam("inputDateEnd")) + 19110000));

		return this.convertToMap(query.getResultList());
	}

}