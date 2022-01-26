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
public class LM004ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo, String kind) throws Exception {
		this.info("lM004.findAll ");

		// kind: "pdf", "excel"

		// 到期日
		String dueDate = String.valueOf((Integer.valueOf(titaVo.getParam("DueDate")) + 19110000));

		// 本金餘額
		String prinBal = titaVo.getParam("PrinBal");

		String sql = "SELECT DECODE（M.\"AmortizedCode\", 3, 'Y', 'N') AS F0";
		sql += "              ,F.\"ProdNo\" AS F1";
		sql += "              ,C1.\"CityCode\" AS F2";
		sql += "              ,C2.\"CityItem\" AS F3";
		sql += "              ,E0.\"Fullname\" AS F4";
		sql += "              ,M.\"CustNo\" AS F5";
		sql += "              ,M.\"FacmNo\" AS F6";
		sql += "              ,\"Fn_ParseEOL\"(C.\"CustName\",0) AS F7";
		sql += "              ,M.\"MaturityDate\" AS F8";
		sql += "              ,TO_CHAR(TO_DATE(M.\"MaturityDate\", 'YYYYMMDD') - 15, 'YYYYMMDD') AS F9";
		sql += "              ,SUM(F.\"UtilAmt\") / COUNT(F.\"UtilAmt\") AS F10";
		sql += "              ,M.\"PrevPayIntDate\" AS F11";
		sql += "              ,E1.\"Fullname\" AS F12";
		sql += "              ,F.\"Introducer\" AS F13";
		sql += "              ,E1.\"CenterCode2Short\" AS F14";
		sql += "              ,E1.\"CenterCode1Short\" AS F15";
		sql += "              ,E1.\"CenterShortName\" AS F16";
		sql += "        FROM \"LoanBorMain\" M";
		sql += "        LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "                               AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "        LEFT JOIN \"CdEmp\" E0 ON E0.\"EmployeeNo\" = F.\"BusinessOfficer\"";
		sql += "        LEFT JOIN \"CdEmp\" E1 ON E1.\"EmployeeNo\" = F.\"Introducer\"";
		sql += "        LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "        LEFT JOIN \"ClFac\" C0 ON C0.\"CustNo\" = M.\"CustNo\"";
		sql += "                              AND C0.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                              AND C0.\"MainFlag\" = 'Y'";
		sql += "        LEFT JOIN \"ClMain\" C1 ON C1.\"ClCode1\" = C0.\"ClCode1\"";
		sql += "                               AND C1.\"ClCode2\" = C0.\"ClCode2\" ";
		sql += "                               AND C1.\"ClNo\"    = C0.\"ClNo\"";
		sql += "        LEFT JOIN \"CdCity\" C2 ON C2.\"CityCode\" = C1.\"CityCode\"";
		sql += "        WHERE M.\"Status\" IN (0,4)";
		sql += "          AND M.\"MaturityDate\" " + (kind.equals("excel") ? "<" : "") + "= :dueDate";
//		sql += "          AND M.\"MaturityDate\" " + (kind.equals("excel") ? "<" : "") + "= :dueDate";
		sql += "          AND M.\"LoanBal\" >= :prinBal";
		sql += "		GROUP BY";
		sql += "    		   DECODE（M.\"AmortizedCode\", 3, 'Y', 'N') ";
		sql += "              ,F.\"ProdNo\" ";
		sql += "              ,C1.\"CityCode\" ";
		sql += "              ,C2.\"CityItem\" ";
		sql += "              ,E0.\"Fullname\" ";
		sql += "              ,M.\"CustNo\" ";
		sql += "              ,M.\"FacmNo\" ";
		sql += "              ,\"Fn_ParseEOL\"(C.\"CustName\",0)";
		sql += "              ,M.\"MaturityDate\" ";
		sql += "              ,M.\"PrevPayIntDate\" ";
		sql += "              ,E1.\"Fullname\" ";
		sql += "              ,F.\"Introducer\" ";
		sql += "              ,E1.\"CenterCode2Short\" ";
		sql += "              ,E1.\"CenterCode1Short\" ";
		sql += "              ,E1.\"CenterShortName\" ";
		sql += "		ORDER BY F8";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("dueDate", dueDate);
		query.setParameter("prinBal", prinBal);
		return this.convertToMap(query.getResultList());
	}

}