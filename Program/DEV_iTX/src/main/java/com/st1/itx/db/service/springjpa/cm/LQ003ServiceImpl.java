package com.st1.itx.db.service.springjpa.cm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
/* 逾期放款明細 */
public class LQ003ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo, int Q) throws Exception {
		this.info("lQ003.findAll ");

//		// 當西元年月
//		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000).substring(0, 6);
//		// 前一西元年
//		String lastYear = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000 - 10000).substring(0, 4);
//		// 當西元年
//		String thisYear = iENTDY.substring(0, 4);
//		// 當月份
//		String iMM = iENTDY.substring(4, 6);
//		
		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		// 當日(int)
		int nowDate = Integer.valueOf(iEntdy);
		Calendar calMonthLastDate = Calendar.getInstance();
		// 設當年月底日
		calMonthLastDate.set(iYear, iMonth, 0);

		int monthLastDate = Integer.valueOf(dateFormat.format(calMonthLastDate.getTime()));

		boolean isMonthZero = iMonth - 1 == 0;

		if (nowDate < monthLastDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}

		// 去年
		String lastYear = String.valueOf(iYear - 1);
		// 年
		String thisYear = String.valueOf(iYear);
		// 月
		String iMM = String.format("%02d", iMonth);

		String lastYm = "";
		String nowYm = "";
		String resYm = "";

		switch (iMM) {
		case "01":
		case "02":
		case "03":

			lastYm = lastYear + "12";

			nowYm = thisYear + "03";
			break;
		case "04":
		case "05":
		case "06":

			lastYm = thisYear + "03";

			nowYm = thisYear + "06";
			break;
		case "07":
		case "08":
		case "09":

			lastYm = thisYear + "06";

			nowYm = thisYear + "09";
			break;
		case "10":
		case "11":
		case "12":

			lastYm = thisYear + "09";

			nowYm = thisYear + "12";
			break;
		}

		if (Q == 0) {
			resYm = nowYm;
		} else {
			resYm = lastYm;
		}

		this.info("type:" + Q + "-" + resYm);

		String sql = "SELECT CC.\"CityItem\" AS F0";
		sql += "			,CC.\"CityCode\" AS F1";
		sql += "            ,ROUND(SUM(M.\"PrinBalance\") / 1000000,8) AS F2"; // 總額

		sql += "            ,ROUND(SUM(";
		sql += "					 CASE";
		sql += "					   WHEN M.\"AcctCode\" = 990 ";
		sql += "						   THEN M.\"PrinBalance\"";
		sql += "						 ELSE 0 END";
		sql += "					  ) / 1000000,8) + ";
		sql += "             ROUND(SUM(";
		sql += "					 CASE";
		sql += "					   WHEN M.\"AcctCode\" <> 990 ";
		sql += "						AND M.\"OvduTerm\" >= 3 ";
		sql += "						   THEN M.\"PrinBalance\"";
		sql += "						 ELSE 0 END";
		sql += "					  ) / 1000000,8) AS F3";// 逾放
		if (Q == 0) {

			sql += "            ,ROUND(SUM(";
			sql += "					 CASE";
			sql += "					   WHEN M.\"AcctCode\" = 990 ";
			sql += "						   THEN M.\"PrinBalance\"";
			sql += "						 ELSE 0 END";
			sql += "					  ) / 1000000,8) AS F4";// 催收

			sql += "            ,ROUND(SUM(";
			sql += "					 CASE";
			sql += "					   WHEN M.\"AcctCode\" = 990 ";
			sql += "						   THEN M.\"Cnt\"";
			sql += "						 ELSE 0 END";
			sql += "					  ) / 1000000,8) + ";
			sql += "             ROUND(SUM(";
			sql += "					 CASE";
			sql += "					   WHEN M.\"AcctCode\" <> 990 ";
			sql += "						AND M.\"OvduTerm\" >= 3 ";
			sql += "						   THEN M.\"Cnt\"";
			sql += "						 ELSE 0 END";
			sql += "					  ) / 1000000,0) AS F5";// 逾放件數
		}
		sql += "      FROM (SELECT M.\"AcctCode\"";
		sql += "				  ,M.\"OvduTerm\"";
		sql += "				  ,DECODE(M.\"EntCode\", '1', 1, 0) AS \"EntCode\"";
		sql += "                  ,LPAD(M.\"CityCode\",1,0) AS \"CityCode\"";
		sql += "                  ,1 AS \"Cnt\"";
		sql += "                  ,M.\"PrinBalance\"";
		sql += "            FROM \"MonthlyFacBal\" M";
		sql += "            WHERE M.\"YearMonth\" = :entdy";
		sql += "              AND M.\"PrinBalance\" > 0) M";
		sql += "            LEFT JOIN \"CdCity\" CC ON CC.\"CityCode\" = M.\"CityCode\"";
		sql += "            WHERE M.\"EntCode\" = 0 ";
		sql += "              AND M.\"CityCode\" <> 0 ";
		sql += "      GROUP BY CC.\"CityItem\"";
		sql += "			  ,CC.\"CityCode\"";
		sql += "      ORDER BY CC.\"CityItem\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		query.setParameter("entdy", resYm.trim());

		return this.convertToMap(query.getResultList());
	}
}
