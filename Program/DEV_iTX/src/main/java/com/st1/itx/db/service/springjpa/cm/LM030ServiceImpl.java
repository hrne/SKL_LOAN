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
import com.st1.itx.util.parse.Parse;

@Service
@Repository
/* 逾期放款明細 */
public class LM030ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;
	
	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("lM030.findAll ");

		this.info("inputted AcDate = " + titaVo.getParam("AcDate"));
		
		String sql = "";
		sql += " SELECT CITY.\"CityItem\" ";
		sql += "       ,\"Fn_GetEmpName\"(F.\"AccCollPsn\", 1) \"AccCollPsn\" ";
		sql += "       ,D.\"CustNo\" ";
		sql += "       ,D.\"FacmNo\" ";
		sql += "       ,C.\"CustName\" ";
		sql += "       ,D.\"DrawdownDate\" ";
		sql += "       ,D.\"LoanBal\" ";
		sql += "       ,D.\"Interest\" ";
		sql += "       ,D.\"StoreRate\" ";
		sql += "       ,D.\"PrevPayIntDate\" ";
		sql += "       ,D.\"NextPayIntDate\" ";
		sql += "       ,TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(TO_CHAR(D.\"NextPayIntDate\"),'YYYYMMDD'),6),'YYYYMMDD')) \"OvduDate\" ";
		sql += " FROM (SELECT M.\"CustNo\" ";
		sql += "             ,M.\"FacmNo\" ";
		sql += "             ,MIN(M.\"DrawdownDate\") \"DrawdownDate\" ";
		sql += "             ,MAX(M.\"LoanBal\") \"LoanBal\" ";
		sql += "             ,SUM(NVL(I.\"Interest\",0)) \"Interest\" ";
		sql += "             ,MAX(M.\"StoreRate\") \"StoreRate\" ";
		sql += "             ,MIN(M.\"PrevPayIntDate\") \"PrevPayIntDate\" ";
		sql += "             ,MIN(M.\"NextPayIntDate\") \"NextPayIntDate\" ";
		sql += "       FROM \"LoanBorMain\" M ";
		sql += "       LEFT JOIN \"AcLoanInt\" I ON I.\"CustNo\" = M.\"CustNo\" ";
		sql += "                                AND I.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "                                AND I.\"BormNo\" = M.\"BormNo\" ";
		sql += "       WHERE M.\"Status\" = 0 ";
		sql += "         AND M.\"NextRepayDate\" <= :AcDate ";
		sql += "         AND (:CustNo = 0 OR M.\"CustNo\" = :CustNo) ";
		sql += "         AND (:CustNo = 0 OR :FacmNo = 0 OR M.\"FacmNo\" = :FacmNo) ";
		sql += "       GROUP BY M.\"CustNo\", M.\"FacmNo\" ";
		sql += "       ORDER BY M.\"CustNo\", M.\"FacmNo\") D ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = D.\"CustNo\" ";
		sql += " LEFT JOIN \"MonthlyFacBal\" F ON F.\"YearMonth\" = SUBSTR(:AcDate,1,6) ";
		sql += "                              AND F.\"CustNo\"    = D.\"CustNo\" ";
		sql += "                              AND F.\"FacmNo\"    = D.\"FacmNo\" ";
		sql += " LEFT JOIN \"CdCity\" CITY ON CITY.\"CityCode\" = F.\"CityCode\" ";
		sql += " LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = F.\"CustNo\" ";
		sql += "                         AND FM.\"FacmNo\" = F.\"FacmNo\" ";
		sql += " WHERE (CASE :DelayCondition WHEN '1' ";
		sql += "                             THEN (CASE WHEN F.\"OvduTerm\" BETWEEN TO_NUMBER(:OvduTermMin) AND TO_NUMBER(:OvduTermMax) THEN 1 ELSE 0 END) ";
		sql += "                             WHEN '2' ";
		sql += "                             THEN (CASE WHEN F.\"OvduDays\" BETWEEN TO_NUMBER(:OvduDayMin) AND TO_NUMBER(:OvduDayMax) THEN 1 ELSE 0 END) ";
		sql += "        ELSE 0 ";
		sql += "        END) = '1' ";
		sql += "   AND CASE WHEN :PayMethod = 0 ";
		sql += "            THEN 1 ";
		sql += "            WHEN :PayMethod = 9 AND FM.\"RepayCode\" IN (5,6,7,8) ";
		sql += "            THEN 1 ";
		sql += "            WHEN FM.\"RepayCode\" = :PayMethod ";
		sql += "            THEN 1 ";
		sql += "       ELSE 0 END = 1 ";
		sql += "   AND CASE WHEN :EntCode = 0 ";
		sql += "            THEN :EntCode - 1 ";
		sql += "       ELSE DECODE(C.\"EntCode\", 1, 1, 0) END = :EntCode - 1 ";
		sql += " ORDER BY \"PrevPayIntDate\" ";
		sql += "         ,\"CustNo\" ";
		sql += "         ,\"FacmNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("AcDate", parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000);
		query.setParameter("CustNo", titaVo.getParam("CustNo"));
		query.setParameter("FacmNo", titaVo.getParam("FacmNo"));
		query.setParameter("DelayCondition", titaVo.getParam("DelayCondition"));
		query.setParameter("OvduDayMin", titaVo.getParam("OvduDayMin"));
		query.setParameter("OvduTermMin", titaVo.getParam("OvduTermMin"));
		query.setParameter("OvduDayMax", titaVo.getParam("OvduDayMax"));
		query.setParameter("OvduTermMax", titaVo.getParam("OvduTermMax"));
		query.setParameter("PayMethod", titaVo.getParam("PayMethod"));
		query.setParameter("EntCode", titaVo.getParam("EntCode"));

		return this.convertToMap(query);
	}

}