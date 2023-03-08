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
//		this.info("CustNo = " + titaVo.getParam("CustNo"));
//		this.info("DelayCondition = " + titaVo.getParam("DelayCondition"));
//		this.info("OvduDayMin = " + titaVo.getParam("OvduDayMin"));
//		this.info("OvduDayMax =" + titaVo.getParam("OvduDayMax"));
//		this.info("OvduDayMin = " + titaVo.getParam("OvduTermMin"));
//		this.info("OvduDayMax = " + titaVo.getParam("OvduTermMax"));
//		this.info("PayMethod = " + titaVo.getParam("PayMethod"));
//		this.info("EntCode = " + titaVo.getParam("EntCode"));
		
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

//		if (!"0".equals(titaVo.getParam("CustNo"))) {
//			sql += "      AND L.\"CustNo\" = "+ titaVo.getParam("CustNo");
//		}
		// 滯繳條件 1.期數 2.日數
//		if ("1".equals(titaVo.getParam("DelayCondition"))) {
//			sql += "	  AND MF.\"OvduTerm\" BETWEEN TO_NUMBER(" + titaVo.getParam("OvduTermMin") + ") AND TO_NUMBER("
//					+ titaVo.getParam("OvduTermMax") + ")";
//		} else {
//			sql += "	  AND MF.\"OvduDays\" BETWEEN TO_NUMBER(" + titaVo.getParam("OvduDayMin") + ") AND TO_NUMBER("
//					+ titaVo.getParam("OvduDayMax") + ")";
//		}

		// 繳款方式 9其他 0全部
//		if ("0".equals(titaVo.getParam("PayMethod"))) {
//			sql += "	 ";
//		} else if ("9".equals(titaVo.getParam("PayMethod"))) {
//			sql += "	  AND F.\"RepayCode\" IN (5,6,7,8) ";
//		} else {
//			sql += "	  AND F.\"RepayCode\" = " + Integer.valueOf(titaVo.getParam("PayMethod"));
//		}

//		if ("1".equals(titaVo.getParam("EntCode"))) {
//			sql += "	  AND CM.\"EntCode\" IN (0,2) ";
//		} else if ("2".equals(titaVo.getParam("EntCode"))) {
//			sql += "	  AND CM.\"EntCode\" IN (1) ";
//		}
		
		sql += "    GROUP BY CD.\"CityItem\"";
		sql += "            ,\"Fn_GetEmpName\"(MF.\"AccCollPsn\", 1)";
		sql += "       	    ,L.\"CustNo\" ";
		sql += "            ,L.\"FacmNo\" ";
		sql += "            ,\"Fn_ParseEOL\"(CM.\"CustName\",0)";
		sql += "            ,TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(TO_CHAR(MF.\"NextIntDate\"),'YYYYMMDD'),6),'YYYYMMDD')) ";


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