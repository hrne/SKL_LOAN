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
import com.st1.itx.eum.ContentName;

@Service
@Repository
public class L4606ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("L4606ServiceImpl.findAll");
		String insuYearMonth = String.valueOf((Integer.valueOf(titaVo.get("InsuEndMonth")) + 191100));
		this.info("insuYearMonth = " + insuYearMonth);
		String sql = " SELECT ";
		sql += "     I.\"NowInsuNo\"                                         AS F0  ";
		sql += "   , I.\"InsuCate\"                                          AS F1  ";
		sql += "   , I.\"InsuPrem\"                                          AS F2  ";
		sql += "   , I.\"InsuStartDate\"                                     AS F3  ";
		sql += "   , I.\"InsuEndDate\"                                       AS F4  ";
		sql += "   , SUBSTRB(I.\"InsuredAddr\",0,60)                         AS F5  ";
		sql += "   , I.\"CustNo\"                                            AS F6  ";
		sql += "   , I.\"FacmNo\"                                            AS F7  ";
		sql += "   , SUBSTRB(C.\"CustName\",0,14)                            AS F8  ";
		sql += "   , I.\"EmpId\"                                             AS F9  ";
		sql += "   , SUBSTRB(I.\"EmpName\",0,12)                             AS F10 ";
		sql += "   , I.\"DueAmt\"                                            AS F11 ";
		sql += "  FROM \"InsuComm\" I ";
		sql += "  LEFT JOIN \"CustMain\" C ON c.\"CustNo\"  = I.\"CustNo\" ";
		sql += "                          AND I.\"CustNo\" != 0";
		sql += "  WHERE I.\"InsuYearMonth\" = :insuYearMonth ";
		sql += "    and I.\"DueAmt\"  > 0 ";
		sql += "    and NVL(I.\"MediaCode\",'N') = 'Y' ";
		sql += "  ORDER BY I.\"EmpId\",I.\"NowInsuNo\", I.\"InsuCate\" ";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("insuYearMonth", insuYearMonth);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> find(TitaVo titaVo) throws Exception {
		this.info("L4606ServiceImpl.find");
		String insuYearMonth = String.valueOf((Integer.valueOf(titaVo.get("InsuEndMonth")) + 191100));
		this.info("insuYearMonth = " + insuYearMonth);
		String sql = " SELECT                                                    ";
		sql += "     I.\"NowInsuNo\"                                         AS F0  ";
		sql += "   , I.\"InsuCate\"                                          AS F1  ";
		sql += "   , I.\"InsuPrem\"                                          AS F2  ";
		sql += "   , I.\"InsuStartDate\"                                     AS F3  ";
		sql += "   , I.\"InsuEndDate\"                                       AS F4  ";
		sql += "   , SUBSTRB(I.\"InsuredAddr\",0,60)                         AS F5  ";
		sql += "   , I.\"CustNo\"                                            AS F6  ";
		sql += "   , I.\"FacmNo\"                                            AS F7  ";
		sql += "   , SUBSTRB(C.\"CustName\",0,14)                            AS F8  ";
		sql += "   , I.\"EmpId\"                                             AS F9  ";
		sql += "   , SUBSTRB(I.\"EmpName\",0,12)                             AS F10 ";
		sql += "   , I.\"DueAmt\"                                            AS F11 ";
		sql += "  FROM \"InsuComm\" I ";
		sql += "  LEFT JOIN \"CustMain\" C ON c.\"CustNo\" = I.\"CustNo\" ";
		sql += "                          AND I.\"CustNo\" != 0";
		sql += "  WHERE I.\"InsuYearMonth\" = :insuYearMonth ";
		sql += "    and I.\"DueAmt\"  > 0 ";
		sql += "    and NVL(I.\"MediaCode\",'N') = 'N' ";
		sql += "  ORDER BY I.\"EmpId\",I.\"NowInsuNo\", I.\"InsuCate\" ";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("insuYearMonth", insuYearMonth);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findEmployee(int custNo, int facmNo) {
		this.info("L4606ServiceImpl.findEmployee");
		this.info("custNo = " + custNo);
		this.info("facmNo = " + facmNo);

//		By I.T. Mail 火險服務抓取 額度檔之火險服務，如果沒有則為戶號的介紹人，若兩者皆為空白者，則為空白(為未發放名單)
//		業務人員任用狀況碼 AgStatusCode =   1:在職 ，才發放 

		String sql = " ";
		sql += " WITH empNoData AS ( ";
		sql += "     SELECT CASE ";
		sql += "              WHEN NVL(FM.\"FireOfficer\", ' ') != ' ' ";
		sql += "              THEN NVL(FM.\"FireOfficer\", ' ') ";
		sql += "            ELSE NVL(CM.\"Introducer\", ' ') END AS \"EmpNo\" ";
		sql += "          , CM.\"CustNo\" AS \"CustNo\" ";
		sql += "     FROM \"CustMain\" CM ";
		sql += "     LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = CM.\"CustNo\" ";
		sql += "                             AND FM.\"FacmNo\" = :facmNo ";
		sql += "     WHERE CM.\"CustNo\" = :custNo ";
		sql += " ) ";
		sql += " SELECT e.\"CustNo\"        AS \"CustNo\" ";
		sql += "      , CE.\"EmployeeNo\"   AS \"EmployeeNo\" ";
		sql += "      , CE.\"AgentId\"      AS \"AgentId\" ";
		sql += "      , CE.\"Fullname\"     AS \"Fullname\" ";
		sql += "      , CE.\"AgStatusCode\" AS \"AgStatusCode\" ";
		sql += " FROM empNoData e ";
		sql += " LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\" = e.\"EmpNo\" ";
		sql += " WHERE NVL(CE.\"EmployeeNo\",' ') != ' ' ";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("custNo", custNo);
		query.setParameter("facmNo", facmNo);

		return this.convertToMap(query);
	}
}