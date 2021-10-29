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
public class LM046ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;
	
	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		int entYearMonth = parse.stringToInteger(titaVo.get("ENTDY")) / 100 + 191100;

		this.info("lM046.findAll entYearMonth = " + entYearMonth);

		String sql = "";
		// 兩個WITH blocks為主要資料
		// 最後再用主View做整合給產表程式用

		// 第一個WITH決定產出的月份與季
		// 這裡會把從本月開始向前推, 所有可能入表的時間點都找出來
		// 但目前Excel格式固定填11行
		// 所以在主view會篩選最近的11個時間點
		sql += " WITH AllMonthOrSeasons AS ( ";
		// 本季
		// LEVEL會從1開始算, 為了列入當月所以用 1 - LEVEL
		// 是往前推, 所以要是0或-1
		sql += "    SELECT TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(:entYearMonth, 'YYYYMM'), 1 - LEVEL), 'YYYYMM')) \"YearMonth\" ";
		sql += "    FROM DUAL ";
		sql += "    WHERE LEVEL <= CASE WHEN SUBSTR(:entYearMonth, 5, 2) IN ('03','06','09','12') ";
		sql += "                        THEN 1 ";
		sql += "                        WHEN SUBSTR(:entYearMonth, 5, 2) IN ('02','05','08','11') ";
		sql += "                        THEN 2 ";
		sql += "                        WHEN SUBSTR(:entYearMonth, 5, 2) IN ('01','04','07','10') ";
		sql += "                        THEN 1 ";
		sql += "                   ELSE 0 END ";
		sql += "    CONNECT BY LEVEL <= 2 "; // 只有 1Q, 2M, 1M 三種狀況
		sql += " UNION ALL ";
		// 今年與去年已過去的季
		// 每三個月一季，所以 -(3*LEVEL)
		sql += "    SELECT UNIQUE \"Fn_GetWorkSeason\"(TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(:entYearMonth, 'YYYYMM'), -LEVEL * 3), 'YYYYMM')), 3) \"YearMonth\" ";
		sql += "    FROM DUAL ";
		sql += "    WHERE TO_CHAR(ADD_MONTHS(TO_DATE(:entYearMonth, 'YYYYMM'), -LEVEL * 3), 'YYYYMM') >= (SUBSTR(:entYearMonth, 1, 4)-1 || '01') ";
		sql += "    CONNECT BY LEVEL <= 7 "; // 最多只有七個Q需要出
		sql += " UNION ALL ";
		// 前年開始向前推的所有Q4
		sql += "    SELECT TO_NUMBER((SUBSTR(:entYearMonth, 1, 4) - 1 - LEVEL) || '12') \"YearMonth\" ";
		sql += "    FROM DUAL ";
		sql += "    CONNECT BY LEVEL <= 6 "; // 最多只有六年
		sql += " ) ";
		sql += " , TotalData AS ( ";
		sql += "    SELECT COUNT(*) AS \"LoanCnt\" "; // 總件數
		sql += "          ,SUM(MFB.\"PrinBalance\") AS \"LoanBal\" "; // 總金額
		sql += "          ,COUNT(CASE WHEN MFB.\"Status\" = '0' ";
		sql += "                       AND MFB.\"OvduTerm\" >= 3 ";
		sql += "                       AND MFB.\"AcctCode\" != '990' ";
		sql += "                      THEN 1 END) AS \"OvduCnt\" "; // 逾期:戶況正常, 逾期數>=3, 非990
		sql += "          ,SUM(CASE WHEN MFB.\"Status\" = '0' ";
		sql += "                     AND MFB.\"OvduTerm\" >= 3 ";
		sql += "                     AND MFB.\"AcctCode\" != '990' ";
		sql += "                    THEN MFB.\"PrinBalance\" ";
		sql += "               ELSE 0 END) AS \"OvduBal\" "; // 逾期的金額
		sql += "          ,COUNT(CASE WHEN MFB.\"Status\" IN ('2','6') ";
		sql += "                       AND MFB.\"OvduTerm\" >= 6 ";
		sql += "                      THEN 1 ";
		sql += "                      WHEN MFB.\"AcctCode\" = '990' ";
		sql += "                      THEN 1 END) AS \"BadDebtCnt\" "; // 催收:戶況2或6; 逾期數>=6 或 為990 (目前資料庫990的逾期期數會登錄為0)
		sql += "          ,SUM(CASE WHEN MFB.\"Status\" IN ('2','6') ";
		sql += "                     AND MFB.\"OvduTerm\" >= 6 ";
		sql += "                    THEN MFB.\"PrinBalance\" ";
		sql += "                    WHEN MFB.\"AcctCode\" = '990' ";
		sql += "                    THEN MFB.\"PrinBalance\" ";
		sql += "               ELSE 0 END) AS \"BadDebtBal\" "; // 催收的金額
		sql += "   ,DECODE(CM.\"EntCode\", 1, 1, 0) \"EntType\" "; // 企金別
		sql += "   ,mons.\"YearMonth\" \"YearMonth\" "; // 主view排序用年月份
		sql += "   FROM AllMonthOrSeasons mons ";
		sql += "   LEFT JOIN \"MonthlyFacBal\" MFB ON MFB.\"YearMonth\" = mons.\"YearMonth\" ";
		sql += "   LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = MFB.\"CustNo\" ";
		sql += "   AND (MFB.\"Status\" IN ('0','2','6') ";
		sql += "        OR ";
		sql += "        MFB.\"AcctCode\" = '990') ";
		sql += "   AND MFB.\"PrinBalance\" > 0 ";
		sql += "   GROUP BY mons.\"YearMonth\" ";
		sql += "           ,DECODE(CM.\"EntCode\", 1, 1, 0) ";
		sql += " ) ";
		// 主View
		// 因為每個年月不一定有某種企金別的資料
		// 每個都做NVL
		sql += " SELECT CASE WHEN SUBSTR(mons.\"YearMonth\", 5, 2) NOT IN ('03','06','09','12') ";
		sql += "             THEN (SUBSTR(mons.\"YearMonth\", 1, 4) - 1911) || '年' || SUBSTR(mons.\"YearMonth\", 5, 2) ";
		sql += "        ELSE SUBSTR(mons.\"YearMonth\", 1, 4) - 1911 || '年Q' || SUBSTR(\"Fn_GetWorkSeason\"(mons.\"YearMonth\", 2), 5, 1) END AS F0 "; // 輸出格式的年月份
		sql += "       ,NVL(Ent.\"LoanCnt\", 0) F1 ";
		sql += "       ,NVL(Ent.\"LoanBal\", 0) F2 ";
		sql += "       ,NVL(Ent.\"OvduCnt\", 0) F3 ";
		sql += "       ,NVL(Ent.\"OvduBal\", 0) F4 ";
		sql += "       ,NVL(Ent.\"BadDebtCnt\", 0) F5 ";
		sql += "       ,NVL(Ent.\"BadDebtBal\", 0) F6 ";
		sql += "       ,NVL(Nat.\"LoanCnt\", 0) F7 ";
		sql += "       ,NVL(Nat.\"LoanBal\", 0) F8 ";
		sql += "       ,NVL(Nat.\"OvduCnt\", 0) F9 ";
		sql += "       ,NVL(Nat.\"OvduBal\", 0) F10 ";
		sql += "       ,NVL(Nat.\"BadDebtCnt\", 0) F11 ";
		sql += "       ,NVL(Nat.\"BadDebtBal\", 0) F12 ";
		sql += " FROM ( SELECT AllMonthOrSeasons.* ";
		sql += "              ,ROW_NUMBER() OVER (ORDER BY \"YearMonth\" DESC) seq ";
		sql += "        FROM AllMonthOrSeasons ) mons ";
		sql += " LEFT JOIN TotalData Nat ON Nat.\"YearMonth\" = mons.\"YearMonth\" ";
		sql += "                        AND Nat.\"EntType\" = 0 ";
		sql += " LEFT JOIN TotalData Ent ON Ent.\"YearMonth\" = mons.\"YearMonth\" ";
		sql += "                        AND Ent.\"EntType\" = 1 ";
		sql += " WHERE mons.seq <= 11 "; // 因應目前底稿11行空格，只出最近的11個時間點
		sql += " ORDER BY Nat.\"YearMonth\" DESC ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		
		query.setParameter("entYearMonth", entYearMonth);

		return this.convertToMap(query);
	}

}