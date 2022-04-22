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
public class LM029ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * 
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo, int yearMonth) throws Exception {
		this.info("LM029ServiceImpl findAll ");

		int entdy = yearMonth;
		this.info("yearMonth=" + entdy);

		String sql = "";
		sql += " SELECT M.\"CustNo\"                              AS F0 ";
		sql += "       ,M.\"FacmNo\"                              AS F1 ";
		sql += "       ,M.\"BormNo\"                              AS F2 ";
		sql += "       ,C.\"CustId\"                              AS F3 ";
		sql += "       ,\"Fn_ParseEOL\"(C.\"CustName\", 0)        AS F4 ";
		sql += "       ,M.\"AcctCode\"                            AS F5 ";
		sql += "       ,L.\"DrawdownDate\"                        AS F6 ";
		sql += "       ,L.\"MaturityDate\"                        AS F7 ";
		sql += "       ,M.\"StoreRate\"                           AS F8 ";
		sql += "       ,L.\"PayIntFreq\"                          AS F9 ";
		sql += "       ,L.\"PrevPayIntDate\"                      AS F10 ";
		sql += "       ,NVL(O.\"OvduDate\", 0)                    AS F11 ";
		sql += "       ,L.\"UsageCode\"                           AS F12 ";
//		sql += "       ,M.\"ClCode1\"                             AS F13 ";
//		sql += "       ,M.\"ClCode2\"                             AS F14 ";
//		sql += "       ,M.\"ClNo\"                                AS F15 ";
//		sql += "       ,CL.\"OwnerId\"                            AS F16 ";
//		sql += "       ,CL.\"OwnerName\"                          AS F17 ";
//		sql += "       ,NVL(CI.\"SettingSeq\",0)                  AS F18 ";
		sql += "       ,NVL(F.\"LineAmt\",0)                      AS F13 ";
		sql += "       ,NVL(L.\"DrawdownAmt\",0)                  AS F14 ";
		sql += "       ,M.\"LoanBalance\"                         AS F15 ";
//		sql += "       ,M.\"CityCode\"                            AS F22 ";
//		sql += "       ,NVL(CL.\"LandNo\",'00000000')             AS F23  "; // 地號格式為 4-4
//		sql += "       ,NVL(CL.\"BdNo\",'00000000')               AS F24  "; // 建號格式為 5-3
		sql += "       ,DECODE(M.\"AcSubBookCode\",'00A',' ','A') AS F16 ";
		sql += " FROM \"MonthlyLoanBal\" M ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\" ";
		sql += " LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = M.\"CustNo\" ";
		sql += "                            AND L.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "                            AND L.\"BormNo\" = M.\"BormNo\" ";
		sql += " LEFT JOIN \"LoanOverdue\" O ON O.\"CustNo\" = M.\"CustNo\" ";
		sql += "                            AND O.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "                            AND O.\"BormNo\" = M.\"BormNo\" ";
		sql += " LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\" ";
		sql += "                        AND F.\"FacmNo\" = M.\"FacmNo\" ";
//		sql += " LEFT JOIN \"ClImm\" CI ON CI.\"ClCode1\" = M.\"ClCode1\" ";
//		sql += "                       AND CI.\"ClCode2\" = M.\"ClCode2\" ";
//		sql += "                       AND CI.\"ClNo\"    = M.\"ClNo\" ";
//		sql += " LEFT JOIN ( SELECT M.\"ClCode1\" ";
//		sql += "                   ,M.\"ClCode2\" ";
//		sql += "                   ,M.\"ClNo\" ";
//		sql += "                   ,CM.\"CustId\"                                  AS \"OwnerId\" ";
//		sql += "                   ,\"Fn_ParseEOL\"(CM.\"CustName\", 0)            AS \"OwnerName\" ";
//		sql += "                   ,LPAD(NVL(CL.\"LandNo1\",'0'),4,'0') ";
//		sql += "                         || LPAD(NVL(CL.\"LandNo2\",'0'),4,'0')    AS \"LandNo\"  "; // 地號格式為 4-4
//		sql += "                   ,LPAD(NVL(CB.\"BdNo1\",'0'),5,'0') ";
//		sql += "                         || LPAD(NVL(CB.\"BdNo2\",'0'),3,'0')      AS \"BdNo\"  "; // 建號格式為 5-3
//		sql += "                   ,ROW_NUMBER() OVER (PARTITION BY M.\"ClCode1\" ";
//		sql += "                                                   ,M.\"ClCode2\" ";
//		sql += "                                                   ,M.\"ClNo\" ";
//		sql += "                                       ORDER BY M.\"ClNo\")        AS \"Seq\" ";
//		sql += "             FROM \"MonthlyLoanBal\" M ";
//		sql += "             LEFT JOIN \"ClBuilding\" CB ON CB.\"ClCode1\" = M.\"ClCode1\" ";
//		sql += "                                        AND CB.\"ClCode2\" = M.\"ClCode2\" ";
//		sql += "                                        AND CB.\"ClNo\"    = M.\"ClNo\" ";
//		sql += "             LEFT JOIN \"ClBuildingOwner\" CBO ON CBO.\"ClCode1\" = M.\"ClCode1\" ";
//		sql += "                                              AND CBO.\"ClCode2\" = M.\"ClCode2\" ";
//		sql += "                                              AND CBO.\"ClNo\"    = M.\"ClNo\" ";
//		sql += "             LEFT JOIN \"ClLand\" CL ON CL.\"ClCode1\" = M.\"ClCode1\" ";
//		sql += "                                    AND CL.\"ClCode2\" = M.\"ClCode2\" ";
//		sql += "                                    AND CL.\"ClNo\"    = M.\"ClNo\" ";
//		sql += "             LEFT JOIN \"ClLandOwner\" CLO ON CLO.\"ClCode1\" = M.\"ClCode1\" ";
//		sql += "                                          AND CLO.\"ClCode2\" = M.\"ClCode2\" ";
//		sql += "                                          AND CLO.\"ClNo\"    = M.\"ClNo\" ";
//		sql += "             LEFT JOIN \"CustMain\" CM ON CM.\"CustUKey\" = NVL(CBO.\"OwnerCustUKey\", CLO.\"OwnerCustUKey\") ";
//		sql += "             WHERE M.\"YearMonth\" = :entdy ";
//		sql += "               AND M.\"LoanBalance\" > 0 ";
//		sql += "           ) CL ON Cl.\"ClCode1\" = M.\"ClCode1\" ";
//		sql += "               AND Cl.\"ClCode2\" = M.\"ClCode2\" ";
//		sql += "               AND Cl.\"ClNo\"    = M.\"ClNo\" ";
//		sql += "               AND Cl.\"Seq\"     = 1 ";
		sql += " WHERE M.\"YearMonth\" = :entdy ";
		sql += "   AND M.\"LoanBalance\" > 0 ";
		sql += " ORDER BY F0,F1,F2 ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("entdy", entdy);

		return this.convertToMap(query);
	}

}