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

		this.info("YearMonth = " + titaVo.getParam("YearMonth"));
		this.info("CustNo = " + titaVo.getParam("CustNo"));
		this.info("DelayCondition = " + titaVo.getParam("DelayCondition"));
		this.info("OvduDayMin = " + titaVo.getParam("OvduDayMin"));
		this.info("OvduDayMax =" + titaVo.getParam("OvduDayMax"));
		this.info("OvduDayMin = " + titaVo.getParam("OvduTermMin"));
		this.info("OvduDayMax = " + titaVo.getParam("OvduTermMax"));
		this.info("PayMethod = " + titaVo.getParam("PayMethod"));
		this.info("EntCode = " + titaVo.getParam("EntCode"));
		String sql = " ";

		sql += "	SELECT CD.\"CityItem\"";
		sql += "          ,\"Fn_GetEmpName\"(MF.\"AccCollPsn\", 1) \"AccCollPsn\" ";
		sql += "       	  ,L.\"CustNo\" ";
		sql += "          ,L.\"FacmNo\" ";
		sql += "          ,\"Fn_ParseEOL\"(CM.\"CustName\",0) AS \"CustName\" ";
		sql += "       	  ,MAX(M.\"DrawdownDate\") AS \"DrawdownDate\" ";
		sql += " 		  ,SUM(L.\"Principal\") AS \"LoanBal\"";
		sql += " 		  ,SUM(L.\"Interest\") AS \"Interest\"";
		sql += "       	  ,MAX(L.\"Rate\") AS \"StoreRate\" ";
		sql += "       	  ,MAX(MF.\"PrevIntDate\") AS \"PrevPayIntDate\" ";
		sql += "       	  ,MAX(MF.\"NextIntDate\") AS \"NextPayIntDate\" ";
		sql += "          ,TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(TO_CHAR(MF.\"NextIntDate\"),'YYYYMMDD'),6),'YYYYMMDD')) \"OvduDate\" ";
		sql += "	FROM \"LoanBorTx\" L";
		sql += "    LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = L.\"CustNo\" ";
		sql += "    LEFT JOIN \"LoanBorMain\" M ON M.\"CustNo\" = L.\"CustNo\" ";
		sql += "                               AND M.\"FacmNo\" = L.\"FacmNo\" ";
		sql += "                               AND M.\"BormNo\" = L.\"BormNo\" ";
		sql += "    LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = L.\"CustNo\" ";
		sql += "                           AND F.\"FacmNo\" = L.\"FacmNo\" ";
		sql += "    LEFT JOIN \"MonthlyFacBal\" MF ON MF.\"CustNo\" = L.\"CustNo\" ";
		sql += "                                  AND MF.\"FacmNo\" = L.\"FacmNo\" ";
		sql += "                                  AND MF.\"YearMonth\" = :yymm ";
		sql += "    LEFT JOIN \"CdCity\" CD ON CD.\"CityCode\" = MF.\"CityCode\" ";
		sql += "    WHERE L.\"TxDescCode\" = '3423' ";
		sql += "      AND L.\"TitaHCode\" = 0 ";
		sql += "      AND TRUNC(L.\"AcDate\" / 100 ) = :yymm ";

		if (!"0".equals(titaVo.getParam("CustNo"))) {
			sql += "      AND L.\"CustNo\" = "+ titaVo.getParam("CustNo");
		}
		// 滯繳條件 1.期數 2.日數
		if ("1".equals(titaVo.getParam("DelayCondition"))) {
			sql += "	  AND MF.\"OvduTerm\" BETWEEN TO_NUMBER(" + titaVo.getParam("OvduTermMin") + ") AND TO_NUMBER("
					+ titaVo.getParam("OvduTermMax") + ")";
		} else {
			sql += "	  AND MF.\"OvduDays\" BETWEEN TO_NUMBER(" + titaVo.getParam("OvduDayMin") + ") AND TO_NUMBER("
					+ titaVo.getParam("OvduDayMax") + ")";
		}

		// 繳款方式 9其他 0全部
		if ("0".equals(titaVo.getParam("PayMethod"))) {
			sql += "	 ";
		} else if ("9".equals(titaVo.getParam("PayMethod"))) {
			sql += "	  AND F.\"RepayCode\" IN (5,6,7,8) ";
		} else {
			sql += "	  AND F.\"RepayCode\" = " + Integer.valueOf(titaVo.getParam("PayMethod"));
		}

		if ("1".equals(titaVo.getParam("EntCode"))) {
			sql += "	  AND CM.\"EntCode\" IN (0,2) ";
		} else if ("2".equals(titaVo.getParam("EntCode"))) {
			sql += "	  AND CM.\"EntCode\" IN (1) ";
		}
		
		sql += "    GROUP BY CD.\"CityItem\"";
		sql += "            ,\"Fn_GetEmpName\"(MF.\"AccCollPsn\", 1)";
		sql += "       	    ,L.\"CustNo\" ";
		sql += "            ,L.\"FacmNo\" ";
		sql += "            ,\"Fn_ParseEOL\"(CM.\"CustName\",0)";
		sql += "            ,TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(TO_CHAR(MF.\"NextIntDate\"),'YYYYMMDD'),6),'YYYYMMDD')) ";

//		sql += " SELECT CITY.\"CityItem\" ";
//		sql += "       ,\"Fn_GetEmpName\"(F.\"AccCollPsn\", 1) \"AccCollPsn\" ";
//		sql += "       ,D.\"CustNo\" ";
//		sql += "       ,D.\"FacmNo\" ";
//		sql += "       ,\"Fn_ParseEOL\"(CM.\"CustName\",0) ";
//		sql += "       ,D.\"DrawdownDate\" ";
//		sql += "       ,D.\"LoanBal\" ";
//		sql += "       ,D.\"Interest\" ";
//		sql += "       ,D.\"StoreRate\" ";
//		sql += "       ,D.\"PrevPayIntDate\" ";
//		sql += "       ,D.\"NextPayIntDate\" ";
//		sql += "       ,TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(TO_CHAR(D.\"NextPayIntDate\"),'YYYYMMDD'),6),'YYYYMMDD')) \"OvduDate\" ";
//		sql += " FROM (SELECT M.\"CustNo\" ";
//		sql += "             ,M.\"FacmNo\" ";
//		sql += "             ,MIN(M.\"DrawdownDate\") \"DrawdownDate\" ";
//		sql += "             ,MAX(M.\"LoanBal\") \"LoanBal\" ";
//		sql += "             ,SUM(NVL(I.\"Interest\",0)) \"Interest\" ";
//		sql += "             ,MAX(M.\"StoreRate\") \"StoreRate\" ";
//		sql += "             ,MIN(M.\"PrevPayIntDate\") \"PrevPayIntDate\" ";
//		sql += "             ,MIN(M.\"NextPayIntDate\") \"NextPayIntDate\" ";
//		sql += "       FROM \"LoanBorMain\" M ";
//		sql += "       LEFT JOIN \"AcLoanInt\" I ON I.\"CustNo\" = M.\"CustNo\" ";
//		sql += "                                AND I.\"FacmNo\" = M.\"FacmNo\" ";
//		sql += "                                AND I.\"BormNo\" = M.\"BormNo\" ";
//		sql += "       WHERE M.\"Status\" = 0 ";
//		sql += "         AND M.\"NextRepayDate\" <= :AcDate ";
//		sql += "         AND (:CustNo = 0 OR M.\"CustNo\" = :CustNo) ";
//		sql += "         AND (:CustNo = 0 OR :FacmNo = 0 OR M.\"FacmNo\" = :FacmNo) ";
//		sql += "       GROUP BY M.\"CustNo\", M.\"FacmNo\" ";
//		sql += "       ORDER BY M.\"CustNo\", M.\"FacmNo\") D ";
//		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = D.\"CustNo\" ";
//		sql += " LEFT JOIN \"MonthlyFacBal\" F ON F.\"YearMonth\" = SUBSTR(:AcDate,1,6) ";
//		sql += "                              AND F.\"CustNo\"    = D.\"CustNo\" ";
//		sql += "                              AND F.\"FacmNo\"    = D.\"FacmNo\" ";
//		sql += " LEFT JOIN \"CdCity\" CITY ON CITY.\"CityCode\" = F.\"CityCode\" ";
//		sql += " LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = F.\"CustNo\" ";
//		sql += "                         AND FM.\"FacmNo\" = F.\"FacmNo\" ";
//		sql += " WHERE (CASE :DelayCondition WHEN '1' ";
//		sql += "                             THEN (CASE WHEN F.\"OvduTerm\" BETWEEN TO_NUMBER(:OvduTermMin) AND TO_NUMBER(:OvduTermMax) THEN 1 ELSE 0 END) ";
//		sql += "                             WHEN '2' ";
//		sql += "                             THEN (CASE WHEN F.\"OvduDays\" BETWEEN TO_NUMBER(:OvduDayMin) AND TO_NUMBER(:OvduDayMax) THEN 1 ELSE 0 END) ";
//		sql += "        ELSE 0 ";
//		sql += "        END) = '1' ";
//		sql += "   AND CASE WHEN :PayMethod = 0 ";
//		sql += "            THEN 1 ";
//		sql += "            WHEN :PayMethod = 9 AND FM.\"RepayCode\" IN (5,6,7,8) ";
//		sql += "            THEN 1 ";
//		sql += "            WHEN FM.\"RepayCode\" = :PayMethod ";
//		sql += "            THEN 1 ";
//		sql += "       ELSE 0 END = 1 ";
//		sql += "   AND CASE WHEN :EntCode = 0 ";
//		sql += "            THEN :EntCode - 1 ";
//		sql += "       ELSE DECODE(C.\"EntCode\", 1, 1, 0) END = :EntCode - 1 ";
//		sql += " ORDER BY \"PrevPayIntDate\" ";
//		sql += "         ,\"CustNo\" ";
//		sql += "         ,\"FacmNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("yymm", parse.stringToInteger(titaVo.getParam("YearMonth")) + 191100);
//		query.setParameter("CustNo", titaVo.getParam("CustNo"));
//		query.setParameter("FacmNo", titaVo.getParam("FacmNo"));
//		query.setParameter("DelayCondition", titaVo.getParam("DelayCondition"));
//		query.setParameter("OvduDayMin", titaVo.getParam("OvduDayMin"));
//		query.setParameter("OvduTermMin", titaVo.getParam("OvduTermMin"));
//		query.setParameter("OvduDayMax", titaVo.getParam("OvduDayMax"));
//		query.setParameter("OvduTermMax", titaVo.getParam("OvduTermMax"));
//		query.setParameter("PayMethod", titaVo.getParam("PayMethod"));
//		query.setParameter("EntCode", titaVo.getParam("EntCode"));

		
		return this.convertToMap(query);
	}

}