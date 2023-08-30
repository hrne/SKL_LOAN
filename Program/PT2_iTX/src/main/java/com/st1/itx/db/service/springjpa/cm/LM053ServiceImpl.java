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

@Service
@Repository
/* 逾期放款明細 */
public class LM053ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("lM053.findAll");
		// 取得會計日(同頁面上會計日)
		// 年月日
		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		// 年
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		// 月
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		// 格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		// 當前日期
		int nowDate = Integer.valueOf(iEntdy);

		Calendar calendar = Calendar.getInstance();

		// 設當年月底日
		// calendar.set(iYear, iMonth, 0);
		calendar.set(Calendar.YEAR, iYear);
		calendar.set(Calendar.MONTH, iMonth - 1);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

		// 以當前月份取得月底日期 並格式化處理
		int thisMonthEndDate = Integer.valueOf(dateFormat.format(calendar.getTime()));

		this.info("1.thisMonthEndDate=" + thisMonthEndDate);

		// 確認是否為1月
		boolean isMonthZero = iMonth - 1 == 0;

		// 當前日期 比 當月底日期 前面 就取上個月底日
		if (nowDate < thisMonthEndDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}

		String iYearMonth = String.valueOf((iYear * 100) + iMonth);

		this.info("yymm:" + iYearMonth);

		String sql = " ";
		sql += "    WITH \"tmpData\" AS (";
		sql += "        SELECT DISTINCT L.\"CustNo\"";
		sql += "                      , L.\"FacmNo\"";
		sql += "                      , MAX(\"RecordDate\")     AS \"RecordDate\"";
		sql += "                      , SUM(Decode(";
		sql += "            L.\"LegalProg\", '056'";
		sql += "         , L.\"Amount\"";
		sql += "         , 0";
		sql += "        )) \"Amount056\"";
		sql += "                      , SUM(Decode(";
		sql += "            L.\"LegalProg\", '057'";
		sql += "         , L.\"Amount\"";
		sql += "         , 0";
		sql += "        )) \"Amount057\"";
		sql += "                      , SUM(Decode(";
		sql += "            L.\"LegalProg\", '058'";
		sql += "         , L.\"Amount\"";
		sql += "         , 0";
		sql += "        )) \"Amount058\"";
		sql += "                      , SUM(Decode(";
		sql += "            L.\"LegalProg\", '060'";
		sql += "         , L.\"Amount\"";
		sql += "         , 0";
		sql += "        )) \"Amount060\"";
		sql += "                      , SUM(0)                \"Amount092\"";
		sql += "        FROM (";
		sql += "            SELECT \"CustNo\"";
		sql += "                 , \"FacmNo\" AS \"FacmNo\"";
		sql += "                 , \"LegalProg\"";
		sql += "                 , \"RecordDate\"";
		sql += "                 , \"Amount\"";
		sql += "                 , ROW_NUMBER() OVER(PARTITION BY \"CustNo\", \"FacmNo\"";
		sql += "               , \"LegalProg\"";
		sql += "                ORDER BY \"RecordDate\" DESC";
		sql += "            ) AS \"Seq\"";
		sql += "            FROM \"CollLaw\"";
		sql += "           ";
		sql += "            WHERE \"LegalProg\" IN (";
		sql += "                '057'";
		sql += "              , '056'";
		sql += "              , '060'";
		sql += "              , '058'";
		sql += "            )";
		sql += "            AND \"Amount\" > 0";
		sql += "        ) L";
		sql += "        WHERE L.\"Seq\" = 1";
		sql += "        GROUP BY L.\"CustNo\"";
		sql += "               , L.\"FacmNo\"";
		sql += "    ), \"tmpMain\" AS (";
		sql += "        SELECT DISTINCT L.\"RecordDate\"";
		sql += "                      , L.\"CustNo\"";
		sql += "                      , 0  AS \"FacmNo\"";
		sql += "                      , L.\"Amount056\"";
		sql += "                      , L.\"Amount057\"";
		sql += "                      , L.\"Amount058\"";
		sql += "                      , L.\"Amount060\"";
		sql += "                      , 0  \"Amount092\"";
		sql += "                      , Lx.\"EntryDate\"";
		sql += "                      , Ml.\"CityCode\"";
		sql += "                      , Ml.\"LegalPsn\"";
		sql += "                      , ROW_NUMBER() OVER(PARTITION BY L.\"CustNo\", L.\"FacmNo\"";
		sql += "            ORDER BY Lx.\"EntryDate\" DESC";
		sql += "        ) AS \"Seq\"";
		sql += "            ,  1             \"MainSeq\"";
		sql += "        FROM \"tmpData\"        L";
		sql += "        LEFT JOIN \"LoanBorTx\"      Lx ON Lx.\"CustNo\" = L.\"CustNo\"";
		sql += "                                    AND";
		sql += "                                    Lx.\"TitaHCode\" = '0'";
		sql += "                                    AND";
		sql += "                                    Lx.\"EntryDate\" >= L.\"RecordDate\"";
		sql += "                                    AND";
		sql += "                                    Lx.\"TxAmt\" = L.\"Amount060\"";
		sql += "        LEFT JOIN \"MonthlyFacBal\"  Ml ON Ml.\"CustNo\" = L.\"CustNo\"";
		sql += "                                        AND";
		sql += "                                        Ml.\"FacmNo\" = L.\"FacmNo\"";
		sql += "                                        AND";
		sql += "                                        Ml.\"YearMonth\" = :yymm";
		sql += "    ), \"tmpMain092\" AS (";
		sql += "        SELECT DISTINCT L.\"RecordDate\"";
		sql += "                      , L.\"CustNo\"";
		sql += "                      , 0    AS \"FacmNo\"";
		sql += "                      , 0             \"Amount056\"";
		sql += "                      , 0             \"Amount057\"";
		sql += "                      , 0             \"Amount058\"";
		sql += "                      , 0             \"Amount060\"";
		sql += "                      , L.\"Amount\"    \"Amount092\"";
		sql += "                      , Lx.\"EntryDate\"";
		sql += "                      , Ml.\"CityCode\"";
		sql += "                      , Ml.\"LegalPsn\"";
		sql += "                      , 1             \"Seq\"";
		sql += "                      , 2             \"MainSeq\"";
		sql += "        FROM \"CollLaw\"        L";
		sql += "        LEFT JOIN \"LoanBorTx\"      Lx ON Lx.\"CustNo\" = L.\"CustNo\"";
		sql += "                                    AND";
		sql += "                                    Lx.\"TitaHCode\" = '0'";
		sql += "                                    AND";
		sql += "                                    Lx.\"EntryDate\" >= L.\"RecordDate\"";
		sql += "                                    AND";
		sql += "                                    Lx.\"TxAmt\" = L.\"Amount\"";
		sql += "        LEFT JOIN \"MonthlyFacBal\"  Ml ON Ml.\"CustNo\" = L.\"CustNo\"";
		sql += "                                        AND";
		sql += "                                        Ml.\"FacmNo\" = L.\"FacmNo\"";
		sql += "                                        AND";
		sql += "                                        Ml.\"YearMonth\" = :yymm";
		sql += "        WHERE L.\"LegalProg\" IN (";
		sql += "            '092'";
		sql += "        )AND \"Amount\" > 0";
		sql += "    )";
		sql += "    ";
		sql += "    SELECT DISTINCT L.\"RecordDate\"";
		sql += "                  , Ci.\"CityItem\"";
		sql += "                  , L.\"CustNo\"";
		sql += "                  , L.\"FacmNo\"";
		sql += "                  , C.\"CustName\"";
		sql += "                  , L.\"Amount056\"";
		sql += "                  , L.\"Amount057\"";
		sql += "                  , L.\"Amount058\"";
		sql += "                  , Decode( L.\"Amount057\", 0 , L.\"Amount058\"";
		sql += "         			, 0 ) - L.\"Amount060\" AS \"deficitAmount\"";
		sql += "                  , L.\"Amount060\"";
		sql += "                  , L.\"Amount092\"";
		sql += "                  , L.\"EntryDate\"";
		sql += "                  , E.\"Fullname\"";
		sql += "                  ,L.\"MainSeq\"";
		sql += "    FROM (";
		sql += "        SELECT *";
		sql += "        FROM (";
		sql += "            SELECT *";
		sql += "            FROM \"tmpMain\"";
		sql += "            UNION";
		sql += "            SELECT *";
		sql += "            FROM \"tmpMain092\"";
		sql += "        )";
		sql += "        WHERE \"Seq\" = 1";
		sql += "    ) L";
		sql += "    LEFT JOIN \"CustMain\"  C ON C.\"CustNo\" = L.\"CustNo\"";
		sql += "    LEFT JOIN \"CdCity\"    Ci ON Ci.\"CityCode\" = L.\"CityCode\"";
		sql += "    LEFT JOIN \"CdEmp\"     E ON E.\"EmployeeNo\" = L.\"LegalPsn\"";
		sql += "    ORDER BY  L.\"CustNo\"";
		sql += "            ,L.\"MainSeq\"";
		sql += "            ,L.\"RecordDate\"";
		sql += "            ,L.\"EntryDate\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", iYearMonth);

		return this.convertToMap(query);
	}

}
