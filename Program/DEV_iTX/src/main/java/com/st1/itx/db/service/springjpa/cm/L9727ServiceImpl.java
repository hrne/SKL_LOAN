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
public class L9727ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int inputDrawdownDateStart, int inputDrawdownDateEnd, TitaVo titaVo)
			throws Exception {
		this.info("L9727ServiceImpl.findAll ");

		this.info("inputDrawdownDateStart = " + inputDrawdownDateStart);
		this.info("inputDrawdownDateEnd = " + inputDrawdownDateEnd);

		String sql = " ";
		sql += " SELECT CE.\"CenterCode2Name\" "; // F0 專員部門
		sql += "      , CE.\"CenterCode1Name\" "; // F1 專員區部
		sql += "      , CE.\"CenterCodeName\" "; // F2 專員單位
		sql += "      , FAC.\"BusinessOfficer\" "; // F3 專員員編
		sql += "      , CE.\"Fullname\" AS \"BsEmpName\" "; // F4 專員姓名
		sql += "      , LBM.\"CustNo\" "; // F5 戶號
		sql += "      , LBM.\"FacmNo\" "; // F6 額度
		sql += "      , LBM.\"BormNo\" "; // F7 撥款
		sql += "      , CM.\"CustName\" "; // F8 戶名
		sql += "      , CM.\"CustId\" "; // F9 客戶ID
		sql += "      , FAC.\"LineAmt\" "; // F10 核准額度
		sql += "      , LBM.\"LoanBal\" "; // F11 餘額
		sql += "      , LBM.\"DrawdownDate\" "; // F12 撥款日期
		sql += "      , FAC.\"UsageCode\" "; // F13 資金用途別
		sql += "      , CAR.\"IsRelated\" "; // F14 利害關係人
		sql += "      , CLM.\"EvaAmt\" "; // F15 評估淨值
		sql += "      , CAR.\"AmlRating\" "; // F16 風險等級
		sql += "      , LBM.\"PrevPayIntDate\" "; // F17 上次繳息日
		sql += "      , LBM.\"StoreRate\" "; // F18
		sql += "      , FAC.\"Supervisor\" "; // F19
		sql += "      , \"Fn_GetEmpName\"(FAC.\"Supervisor\", 0) AS \"SupEmpName\" "; // F20
		sql += "      , ROUND(FAC.\"GracePeriod\" / 12, 0)     AS \"FacGraceYears\" "; // F21 寬限期(年)
		sql += "      , LBM.\"RenewFlag\" "; // F22 展期/借新還舊
		sql += "      , FAC.\"CompensateFlag\" "; // F23 代償碼
		sql += "      , CLM.\"ClCode1\" "; // F24 擔保品代號1
		sql += "      , CLM.\"ClCode2\" "; // F25 擔保品代號2
		sql += "      , CLM.\"ClNo\" "; // F26 擔保品號碼
		sql += "      , LBM.\"GraceDate\" "; // F27 寬限到期日
		// F28 寬限區分
		// F29 更新會計日期
		// F30 更新交易代號
		sql += " FROM \"LoanBorMain\" LBM ";
		sql += " LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "                        AND FAC.\"FacmNo\" = LBM.\"FacmNo\" ";
		sql += " LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\" = FAC.\"BusinessOfficer\" ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = LBM.\"CustNo\" ";
		sql += " LEFT JOIN \"CustomerAmlRating\"  CAR ON CAR.\"CustId\" = CM.\"CustId\" ";
		sql += " LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "                     AND CF.\"FacmNo\" = LBM.\"FacmNo\" ";
		sql += "                     AND CF.\"MainFlag\" = 'Y' ";
		sql += " LEFT JOIN \"ClMain\" CLM ON CLM.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                       AND CLM.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                       AND CLM.\"ClNo\" = CF.\"ClNo\" ";
		sql += " WHERE LBM.\"DrawdownDate\" >= :inputDrawdownDateStart ";
		sql += "   AND LBM.\"DrawdownDate\" <= :inputDrawdownDateEnd ";
		sql += " ORDER BY LBM.\"CustNo\" ";
		sql += "        , LBM.\"FacmNo\" ";
		sql += "        , LBM.\"BormNo\" ";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("inputDrawdownDateStart", inputDrawdownDateStart);
		query.setParameter("inputDrawdownDateEnd", inputDrawdownDateEnd);

		return this.convertToMap(query);
	}

}