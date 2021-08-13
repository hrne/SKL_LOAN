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
public class LM035ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM035ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo, String yearSeason) throws Exception {

		String entdy = yearSeason;
		logger.info("yearSeason = " + yearSeason);
//		String entdy = String.valueOf((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000) / 100);
//		String year[] = new String[7];
//
//		int temp = 0;
//
//		if (Integer.valueOf(entdy.substring(4, 6)) % 3 != 0) { // 不是3得倍數月
//			temp = Integer.valueOf(entdy.substring(4, 6)) - (Integer.valueOf(entdy.substring(4, 6)) % 3);
//		} else {
//			temp = Integer.valueOf(entdy.substring(4, 6)) - 3;
//		}
//
//		if (temp == 0) { // 12月
//			temp = 12;
//			year[0] = String.valueOf(Integer.valueOf(entdy.substring(0, 4)) - 1) + temp;
//		} else { // 3 6 9月
//			year[0] = String.valueOf(Integer.valueOf(entdy.substring(0, 4))) + "0" + temp;
//		} // else
//
//		for (int i = 0; i < 4; i++) {
//			if (Integer.valueOf(year[i].substring(4, 6)) == 3) { // 3月
//				year[i + 1] = String.valueOf(Integer.valueOf(year[i].substring(0, 4)) - 1) + "12";
//			} else { // 12 9 6月
//				year[i + 1] = year[i].substring(0, 4) + "0" + String.valueOf(Integer.valueOf(year[i].substring(4, 6)) - 3);
//			}
//		}
//
//		for (int i = 4; i < 6; i++) {
//			if (Integer.valueOf(year[i].substring(4, 6)) == 12) {
//				year[i + 1] = String.valueOf(Integer.valueOf(year[i].substring(0, 4)) - 1) + year[i].substring(4, 6);
//			} else {
//				year[i + 1] = String.valueOf(Integer.valueOf(year[i].substring(0, 4)) - 1) + "12";
//			}
//		}

		logger.info("lM035.findAll ");
		
//		String sql = " ";
//		sql += " SELECT D.\"CityCode\"                          "; // F0 擔保品地區別代號
//		sql += "       ,NVL(C.\"CityItem\",' ') AS \"CityItem\" "; // F1 擔保品地區別中文
//		sql += "       ,D.\"YearMonth\"                         "; // F2 資料年月
//		sql += "       ,D.\"LoanBalTotal\"                      "; // F3 放款餘額加總
//		sql += "       ,D.\"OvdBalTotal\"                       "; // F4 逾期餘額加總
//		sql += "       ,D.\"ColBalTotal\"                       "; // F5 催收餘額加總
//		sql += "       ,CASE";
//		sql += "          WHEN D.\"LoanBalTotal\" + D.\"ColBalTotal\" > 0"; // 分母大於0才計算
//		sql += "          THEN (D.\"OvdBalTotal\" + D.\"ColBalTotal\") / (D.\"LoanBalTotal\" + D.\"ColBalTotal\")";
//		sql += "        ELSE 0 END            AS \"OvduRatio\""; // F6 逾放比 = (逾期 + 催收) / (放款 + 催收)
//		sql += " FROM (SELECT NVL(M.\"CityCode\",' ') AS \"CityCode\""; // 擔保品地區別代號
//		sql += "             ,M.\"YearMonth\""; // 資料年月
//		sql += "             ,SUM(CASE";
//		sql += "                    WHEN M.\"Status\" IN (0,4) THEN M.\"PrinBalance\"";
//		sql += "                  ELSE 0 END) AS \"LoanBalTotal\""; // 放款餘額加總 (戶況為00,04)
//		sql += "             ,SUM(CASE";
//		sql += "                    WHEN M.\"Status\" = 0";
//		sql += "                     AND M.\"OvduTerm\" >= 3";
//		sql += "                    THEN M.\"PrinBalance\"";
//		sql += "                  ELSE 0 END) AS \"OvdBalTotal\""; // 逾期餘額加總 (戶況為04)
//		sql += "             ,SUM(CASE";
//		sql += "                    WHEN M.\"Status\" IN (2,6) THEN M.\"PrinBalance\"";
//		sql += "                  ELSE 0 END) AS \"ColBalTotal\""; // 催收餘額加總 (戶況為02,06)
//		sql += "       FROM \"MonthlyFacBal\" M";
//		sql += "       WHERE M.\"YearMonth\" IN (:entdy,:year0,:year1,:year2,:year3,:year4,:year5,:year6)";
//		sql += "         AND M.\"Status\" IN (0,2,6)";
//		sql += "         AND DECODE(M.\"DepartmentCode\", '1', 1, 0) <> 1"; // 案件隸屬單位為0:非企金單位
//		sql += "         AND M.\"ClCode1\" IN (1,2)"; // 擔保品代號1為1:不動產建物,2:不動產土地
//		sql += "       GROUP BY M.\"CityCode\"";
//		sql += "               ,M.\"YearMonth\"";
//		sql += "       ORDER BY M.\"CityCode\"";
//		sql += "               ,M.\"YearMonth\"";
//		sql += "      ) D";
//		sql += " LEFT JOIN \"CdCity\" C ON C.\"CityCode\" = D.\"CityCode\"";
//		sql += "                     AND NVL(D.\"CityCode\",' ') <> ' '";
		String sql = "SELECT DECODE(S1.\"CityCode\", '85', '96', S1.\"CityCode\") AS \"CityCode\"";
		sql += "            ,S1.\"CityItem\" AS \"CityItem\"";
		sql += "            ,ROUND((SUM(S1.\"LoanBal\") + SUM(S1.\"ColBal\") + SUM(S1.\"OvduBal\")) / 1000000, 2) AS \"LoanBal\"";
		sql += "            ,ROUND(SUM(S1.\"OvduBal\") / 1000000, 2) AS \"OvduBal\"";
		sql += "            ,ROUND(SUM(S1.\"ColBal\") / 1000000, 2) AS \"ColBal\"";
		sql += "            ,CASE WHEN SUM(S1.\"LoanBal\") + SUM(S1.\"ColBal\") > 0";
		sql += "                  THEN ROUND((SUM(S1.\"OvduBal\") + SUM(S1.\"ColBal\")) / (SUM(S1.\"LoanBal\") + SUM(S1.\"ColBal\")), 4)";
		sql += "             ELSE 0 END AS \"Ratio\"";
		sql += "      FROM (SELECT F.\"CityCode\" AS \"CityCode\"";
		sql += "                  ,C.\"CityItem\" AS \"CityItem\"";
		sql += "                  ,SUM(CASE WHEN F.\"Status\" = '0' AND F.\"OvduTerm\" < 3";
		sql += "                            THEN M.\"LoanBalance\"";
		sql += "                       ELSE 0 END) AS \"LoanBal\"";
		sql += "                  ,SUM(CASE WHEN F.\"Status\" = '0' AND F.\"OvduTerm\" >= 3";
		sql += "                            THEN M.\"LoanBalance\"";
		sql += "                       ELSE 0 END) AS \"OvduBal\"";
		sql += "                  ,SUM(CASE WHEN M.\"AcctCode\" = '990'";
		sql += "                            THEN M.\"LoanBalance\"";
		sql += "                       ELSE 0 END) AS \"ColBal\"";
		sql += "            FROM \"MonthlyLoanBal\" M";
		sql += "            LEFT JOIN \"MonthlyFacBal\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "                                         AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                         AND F.\"YearMonth\" = M.\"YearMonth\"";
		sql += "            LEFT JOIN \"CdCity\" C ON C.\"CityCode\" = F.\"CityCode\"";
		sql += "            WHERE  M.\"YearMonth\" = :entdy";
		sql += "              AND DECODE(M.\"DepartmentCode\", '1', 1, 0) <> 1";
		sql += "              AND M.\"ClCode1\" IN (1, 2)";
		sql += "              AND TO_NUMBER(C.\"CityCode\") < 96";
		sql += "            GROUP BY F.\"CityCode\",  C.\"CityItem\"";
		sql += "            UNION ALL";
		sql += "            SELECT C.\"CityCode\" AS \"CityCode\"";
		sql += "                  ,C.\"CityItem\" AS \"CityItem\"";
		sql += "                  ,0 AS \"LoanBal\"";
		sql += "                  ,0 AS \"OvduBal\"";
		sql += "                  ,0 AS \"ColBal\"";
		sql += "            FROM \"CdCity\" C";
		sql += "            WHERE TO_NUMBER(C.\"CityCode\") < 96) S1";
		sql += "      GROUP BY S1.\"CityCode\", S1.\"CityItem\"";
		sql += "      ORDER BY \"CityCode\"";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
//		query.setParameter("year0", year[0]);
//		query.setParameter("year1", year[1]);
//		query.setParameter("year2", year[2]);
//		query.setParameter("year3", year[3]);
//		query.setParameter("year4", year[4]);
//		query.setParameter("year5", year[5]);
//		query.setParameter("year6", year[6]);

		return this.convertToMap(query.getResultList());
	}

}