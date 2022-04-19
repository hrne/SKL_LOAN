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
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("l8202ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L8202ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> findAll12(TitaVo titaVo) throws Exception {
		String iFactor1TotLimit = titaVo.getParam("Factor1TotLimit"); // 洗錢樣態一金額合計超過
		String iFactor2Count = titaVo.getParam("Factor2Count"); // 洗錢樣態二次數
		String iFactor2AmtStart = titaVo.getParam("Factor2AmtStart"); // 洗錢樣態二單筆起始金額
		String iFactor2AmtEnd = titaVo.getParam("Factor2AmtEnd"); // 洗錢樣態二單筆迄止金額
//		String iFactor3TotLimit = titaVo.getParam("Factor3TotLimit"); // 洗錢樣態三金額合計超過
		String iFactorDays = titaVo.getParam("FactorDays"); // 統計期間天數
//		String iFactorDays3 = titaVo.getParam("FactorDays3"); // 統計期間天數
		int iEntryDateS = Integer.parseInt(titaVo.getParam("EntryDateS")) + 19110000; // 入帳日期起日
		int iEntryDateE = Integer.parseInt(titaVo.getParam("EntryDateE")) + 19110000; // 入帳日期迄日

		String sql = "　";
		sql += " SELECT \n";
		sql += "   F.\"CustNo\"                              AS F0 \n"; // 戶號
		sql += "  ,F.\"EntryDate\"                           AS F1 \n"; // 入帳日期
		sql += "  ,CASE WHEN F.\"Factor1Amt\" >=  " + iFactor1TotLimit;
		sql += "            THEN  F.\"Factor1Cnt\"                 \n";
		sql += "        ELSE 0                                     \n";
		sql += "   END                                       AS F2 \n"; // 洗錢樣態一累計筆數
		sql += "  ,CASE WHEN F.\"Factor1Amt\" >=  " + iFactor1TotLimit;
		sql += "            THEN  F.\"Factor1Amt\"                 \n";
		sql += "        ELSE 0                                     \n";
		sql += "   END                                       AS F3 \n"; // 洗錢樣態一累計金額
		sql += "  ,CASE WHEN NVL(D1.\"EntryDate\",0) > 0           \n";
		sql += "             THEN 1                                \n";
		sql += "        ELSE 0                                     \n";
		sql += "   END                                       AS F4 \n"; // 洗錢樣態一資料重複 (1.是)
		sql += "  ,CASE WHEN F.\"Factor2Cnt\" >=" + iFactor2Count;
		sql += "            THEN  F.\"Factor2Cnt\"                 \n";
		sql += "        ELSE 0                                     \n";
		sql += "   END                                       AS F5 \n"; // 洗錢樣態二累計筆數
		sql += "  ,CASE WHEN F.\"Factor2Cnt\" >=" + iFactor2Count;
		sql += "            THEN  F.\"Factor2Amt\"                 \n";
		sql += "        ELSE 0                                     \n";
		sql += "   END                                       AS F6 \n"; // 洗錢樣態二累計金額
		sql += "  ,CASE WHEN NVL(D2.\"EntryDate\",0) > 0           \n";
		sql += "             THEN 1                                \n";
		sql += "        ELSE 0                                     \n";
		sql += "   END                                       AS F7 \n"; // 洗錢樣態二資料重複 (1.是)
		sql += "  FROM (                                            \n";
		sql += "         SELECT                                     \n";
		sql += "          S.\"CustNo\"                       AS \"CustNo\"     \n";
		sql += "         ,S.\"EntryDate\"                    AS \"EntryDate\"  \n";
		sql += "         ,SUM(S.\"B1Cnt\" + S.\"A1Cnt\" + S.\"P1Cnt\" + S.\"C1Cnt\") ";
		sql += "                                             AS \"Factor1Cnt\" \n";
		sql += "         ,SUM(S.\"B1Amt\" + S.\"A1Amt\" + S.\"P1Amt\" + S.\"C1Amt\") ";
		sql += "                                             AS \"Factor1Amt\" \n";
		sql += "         ,SUM(S.\"B2Cnt\" + S.\"A2Cnt\" + S.\"P2Cnt\" + S.\"C2Cnt\") ";
		sql += "                                             AS \"Factor2Cnt\" \n";
		sql += "         ,SUM(S.\"B2Amt\" + S.\"A2Amt\" + S.\"P2Amt\" + S.\"C2Amt\") ";
		sql += "                                             AS \"Factor2Amt\" \n";
		sql += "         FROM (                                                \n";
		sql += "               SELECT                                          \n";
		sql += "                M.\"CustNo\"               AS \"CustNo\"       \n";
		sql += "               ,M.\"EntryDate\"            AS \"EntryDate\"    \n";
		sql += "               ,CASE WHEN NVL(B.\"RepayAmt\",0) > 0            \n";
		sql += "                      AND NVL(H.\"BatxExeCode\",'0') <> '8'    \n";
		sql += "                         THEN 1                                \n";
		sql += "                     ELSE 0                                    \n";
		sql += "                END                         AS  \"B1Cnt\"      \n";
		sql += "               ,CASE WHEN NVL(B.\"RepayAmt\",0) > 0            \n";
		sql += "                      AND NVL(H.\"BatxExeCode\",'0') <> '8'    \n";
		sql += "                         THEN B.\"RepayAmt\"                   \n";
		sql += "                     ELSE 0                                    \n";
		sql += "                END                         AS  \"B1Amt\"      \n";
		sql += "               ,CASE WHEN NVL(A.\"RepayAmt\",0) > 0            \n";
		sql += "                         THEN 1                                \n";
		sql += "                     ELSE 0                                    \n";
		sql += "                END                         AS  \"A1Cnt\"      \n";
		sql += "               ,CASE WHEN NVL(A.\"RepayAmt\",0) > 0            \n";
		sql += "                         THEN A.\"RepayAmt\"                   \n";
		sql += "                     ELSE 0                                    \n";
		sql += "                END                         AS  \"A1Amt\"      \n";
		sql += "               ,CASE WHEN NVL(P.\"RepayAmt\",0) > 0            \n";
		sql += "                         THEN 1                                \n";
		sql += "                     ELSE 0                                    \n";
		sql += "                END                         AS  \"P1Cnt\"      \n";
		sql += "               ,CASE WHEN NVL(P.\"RepayAmt\",0) > 0            \n";
		sql += "                         THEN P.\"RepayAmt\"                   \n";
		sql += "                     ELSE 0                                    \n";
		sql += "                END                         AS  \"P1Amt\"      \n";
		sql += "               ,CASE WHEN NVL(C.\"ChequeAmt\",0) > 0           \n";
		sql += "                         THEN 1                                \n";
		sql += "                     ELSE 0                                    \n";
		sql += "                END                         AS  \"C1Cnt\"      \n";
		sql += "               ,CASE WHEN NVL(C.\"ChequeAmt\",0) > 0           \n";
		sql += "                         THEN C.\"ChequeAmt\"                  \n";
		sql += "                     ELSE 0                                    \n";
		sql += "                END                         AS  \"C1Amt\"      \n";
		sql += "               ,CASE WHEN M.\"Factor2\" = 1                    \n";
		sql += "                      AND NVL(B.\"RepayAmt\",0) >= " + iFactor2AmtStart;
		sql += "                      AND NVL(B.\"RepayAmt\",0) <= " + iFactor2AmtEnd;
		sql += "                      AND NVL(H.\"BatxExeCode\",'0') <> '8'    \n";
		sql += "                         THEN 1                                \n";
		sql += "                     ELSE 0                                    \n";
		sql += "                END                         AS \"B2Cnt\"       \n";
		sql += "               ,CASE WHEN M.\"Factor2\" = 1                    \n";
		sql += "                      AND NVL(B.\"RepayAmt\",0) >= " + iFactor2AmtStart;
		sql += "                      AND NVL(B.\"RepayAmt\",0) <= " + iFactor2AmtEnd;
		sql += "                      AND NVL(H.\"BatxExeCode\",'0') <> '8'    \n";
		sql += "                          THEN NVL(B.\"RepayAmt\",0)           \n";
		sql += "                     ELSE 0                                    \n";
		sql += "                END                         AS \"B2Amt\"       \n";
		sql += "               ,CASE WHEN M.\"Factor2\" = 1                    \n";
		sql += "                      AND NVL(A.\"RepayAmt\",0) >= " + iFactor2AmtStart;
		sql += "                      AND NVL(A.\"RepayAmt\",0) <= " + iFactor2AmtEnd;
		sql += "                         THEN 1                                \n";
		sql += "                     ELSE 0                                    \n";
		sql += "                END                         AS \"A2Cnt\"       \n";
		sql += "               ,CASE WHEN M.\"Factor2\" = 1                    \n";
		sql += "                      AND NVL(A.\"RepayAmt\",0) >= " + iFactor2AmtStart;
		sql += "                      AND NVL(A.\"RepayAmt\",0) <= " + iFactor2AmtEnd;
		sql += "                          THEN NVL(A.\"RepayAmt\",0)           \n";
		sql += "                     ELSE 0                                    \n";
		sql += "                END                         AS \"A2Amt\"       \n";
		sql += "               ,CASE WHEN M.\"Factor2\" = 1                    \n";
		sql += "                      AND NVL(P.\"RepayAmt\",0) >= " + iFactor2AmtStart;
		sql += "                      AND NVL(P.\"RepayAmt\",0) <= " + iFactor2AmtEnd;
		sql += "                         THEN 1                                \n";
		sql += "                     ELSE 0                                    \n";
		sql += "                END                         AS \"P2Cnt\"       \n";
		sql += "               ,CASE WHEN M.\"Factor2\" = 1                    \n";
		sql += "                      AND NVL(P.\"RepayAmt\",0) >= " + iFactor2AmtStart;
		sql += "                      AND NVL(P.\"RepayAmt\",0) <= " + iFactor2AmtEnd;
		sql += "                          THEN NVL(P.\"RepayAmt\",0)           \n";
		sql += "                     ELSE 0                                    \n";
		sql += "                END                         AS \"P2Amt\"       \n";
		sql += "               ,CASE WHEN M.\"Factor2\" = 1                    \n";
		sql += "                      AND NVL(C.\"ChequeAmt\",0) >= " + iFactor2AmtStart;
		sql += "                      AND NVL(C.\"ChequeAmt\",0) <= " + iFactor2AmtEnd;
		sql += "                         THEN 1                                \n";
		sql += "                     ELSE 0                                    \n";
		sql += "                END                         AS \"C2Cnt\"       \n";
		sql += "               ,CASE WHEN M.\"Factor2\" = 1                    \n";
		sql += "                      AND NVL(C.\"ChequeAmt\",0) >= " + iFactor2AmtStart;
		sql += "                      AND NVL(C.\"ChequeAmt\",0) <= " + iFactor2AmtEnd;
		sql += "                          THEN NVL(C.\"ChequeAmt\",0)          \n";
		sql += "                     ELSE 0                                    \n";
		sql += "                END                         AS \"C2Amt\"       \n";
		sql += "               FROM                                            \n";
		sql += "                (SELECT                                      \n";
		sql += "                   \"CustNo\"                                \n";
		sql += "                  ,\"EntryDate\"                             \n";
		sql += "                  ,MAX(\"Factor2\")     AS \"Factor2\"       \n";
		sql += "                 FROM (                                      \n";
		sql += "                       SELECT                                   \n";
		sql += "                        B0.\"CustNo\"        AS \"CustNo\"      \n";
		sql += "                       ,B0.\"EntryDate\"     AS \"EntryDate\"   \n";
		sql += "                       ,CASE WHEN B0.\"RepayAmt\" >= " + iFactor2AmtStart;
		sql += "                              AND B0.\"RepayAmt\" <= " + iFactor2AmtEnd;
		sql += "                                  THEN 1                        \n";
		sql += "                             ELSE 0                             \n";
		sql += "                        END                  AS \"Factor2\"     \n";
		sql += "                        FROM \"BankRmtf\" B0                    \n";
		sql += "                        LEFT JOIN \"BatxHead\" H0               \n";
		sql += "                          ON  H0.\"AcDate\" = B0.\"AcDate\"     \n";
		sql += "                         AND  H0.\"BatchNo\" = B0.\"BatchNo\"   \n";
		sql += "                        WHERE B0.\"EntryDate\" >= " + iEntryDateS;
		sql += "                          AND B0.\"EntryDate\" <= " + iEntryDateE;
		sql += "                          AND NVL(B0.\"CustNo\",0) > 0          \n";
		sql += "                          AND NVL(H0.\"BatxExeCode\",'0') <> '8'\n"; // 8.已刪除
		sql += "                       UNION ALL                                \n";
		sql += "                       SELECT                                   \n";
		sql += "                        A0.\"CustNo\"        AS \"CustNo\"      \n";
		sql += "                       ,A0.\"EntryDate\"     AS \"EntryDate\"   \n";
		sql += "                       ,CASE WHEN A0.\"RepayAmt\" >= " + iFactor2AmtStart;
		sql += "                              AND A0.\"RepayAmt\" <= " + iFactor2AmtEnd;
		sql += "                                  THEN 1                        \n";
		sql += "                             ELSE 0                             \n";
		sql += "                        END                  AS \"Factor2\"     \n";
		sql += "                        FROM \"AchDeductMedia\" A0              \n";
		sql += "                        WHERE A0.\"EntryDate\" >= " + iEntryDateS;
		sql += "                          AND A0.\"EntryDate\" <= " + iEntryDateE;
		sql += "                          AND A0.\"AcDate\" > 0                 \n";
		sql += "                          AND A0.\"ReturnCode\" IN ('00','  ')  \n";
		sql += "                       UNION ALL                                \n";
		sql += "                       SELECT                                   \n";
		sql += "                        P0.\"CustNo\"        AS \"CustNo\"      \n";
		sql += "                       ,P0.\"TransDate\"     AS \"EntryDate\"   \n";
		sql += "                       ,CASE WHEN P0.\"RepayAmt\" >= " + iFactor2AmtStart;
		sql += "                              AND P0.\"RepayAmt\" <= " + iFactor2AmtEnd;
		sql += "                                  THEN 1                        \n";
		sql += "                             ELSE 0                             \n";
		sql += "                        END                  AS \"Factor2\"     \n";
		sql += "                        FROM \"PostDeductMedia\" P0             \n";
		sql += "                        WHERE P0.\"TransDate\" >= " + iEntryDateS;
		sql += "                          AND P0.\"TransDate\" <= " + iEntryDateE;
		sql += "                          AND P0.\"AcDate\" > 0                 \n";
		sql += "                          AND P0.\"ProcNoteCode\" IN ('00','  ')\n";
		sql += "                       UNION ALL                                \n";
		sql += "                       SELECT                                   \n";
		sql += "                        C0.\"CustNo\"        AS \"CustNo\"      \n";
		sql += "                       ,C0.\"EntryDate\"     AS \"EntryDate\"   \n";
		sql += "                       ,CASE WHEN C0.\"ChequeAmt\" >= " + iFactor2AmtStart;
		sql += "                              AND C0.\"ChequeAmt\" <= " + iFactor2AmtEnd;
		sql += "                                  THEN 1                        \n";
		sql += "                             ELSE 0                             \n";
		sql += "                        END                  AS \"Factor2\"     \n";
		sql += "                        FROM \"LoanCheque\" C0                  \n";
		sql += "                        WHERE C0.\"EntryDate\" >= " + iEntryDateS;
		sql += "                          AND C0.\"EntryDate\" <= " + iEntryDateE;
		sql += "                          AND C0.\"StatusCode\" = '1'           \n"; // 1: 兌現入帳
		sql += "                      )                                         \n";
		sql += "                 GROUP BY \"CustNo\", \"EntryDate\"             \n";
		sql += "                ) M                                             \n";
		sql += "               LEFT JOIN \"BankRmtf\" B                         \n";
		sql += "                      ON  B.\"CustNo\" = M.\"CustNo\"           \n";
		sql += "                     AND (TO_DATE(M.\"EntryDate\",'YYYY-MM-DD') \n";
		sql += "                        - TO_DATE(B.\"EntryDate\",'YYYY-MM-DD') \n";
		sql += "                         )  BETWEEN 0 AND " + iFactorDays + " \n";
		sql += "               LEFT JOIN \"BatxHead\" H                         \n";
		sql += "                      ON  H.\"AcDate\" = B.\"AcDate\"           \n";
		sql += "                     AND  H.\"BatchNo\" = B.\"BatchNo\"         \n";
		sql += "               LEFT JOIN \"AchDeductMedia\" A                   \n";
		sql += "                      ON A.\"CustNo\" =  M.\"CustNo\"           \n";
		sql += "                     AND A.\"AcDate\" > 0                       \n";
		sql += "                     AND A.\"ReturnCode\" IN ('  ','00')        \n";
		sql += "                     AND (TO_DATE(M.\"EntryDate\",'YYYY-MM-DD') \n";
		sql += "                       -  TO_DATE(A.\"EntryDate\",'YYYY-MM-DD') \n";
		sql += "                          )  BETWEEN 0 AND " + iFactorDays + " \n";
		sql += "               LEFT JOIN \"PostDeductMedia\" P                  \n";
		sql += "                      ON P.\"CustNo\" =  M.\"CustNo\"           \n";
		sql += "                     AND P.\"ProcNoteCode\" IN ('  ','00')      \n";
		sql += "                     AND P.\"AcDate\" > 0                       \n";
		sql += "                     AND (TO_DATE(M.\"EntryDate\",'YYYY-MM-DD') \n";
		sql += "                        - TO_DATE(P.\"TransDate\",'YYYY-MM-DD') \n";
		sql += "                          )  BETWEEN 0 AND " + iFactorDays + " \n";
		sql += "               LEFT JOIN \"LoanCheque\" C                       \n";
		sql += "                      ON C.\"CustNo\" =  M.\"CustNo\"           \n";
		sql += "                     AND C.\"StatusCode\" =  '1'                \n"; // 1: 兌現入帳
		sql += "                     AND C.\"EntryDate\" > 0                    \n";
		sql += "                     AND (TO_DATE(M.\"EntryDate\",'YYYY-MM-DD') \n";
		sql += "                        - TO_DATE(C.\"EntryDate\",'YYYY-MM-DD') \n";
		sql += "                         )  BETWEEN 0 AND " + iFactorDays + "\n";
		sql += "              ) S                                               \n";
		sql += "         GROUP BY S.\"CustNo\", S.\"EntryDate\"                 \n";
		sql += "       ) F                                                      \n";
		sql += " LEFT JOIN  \"MlaundryDetail\" D1                               \n"; // 疑似洗錢交易合理性明細檔
		sql += "        ON  D1.\"CustNo\" =  F.\"CustNo\"                       \n";
		sql += "       AND  D1.\"EntryDate\" =  F.\"EntryDate\"                 \n";
		sql += "       AND  D1.\"Factor\" =  1                                  \n";
		sql += "       AND  F.\"Factor1Amt\" >= " + iFactor1TotLimit;
		sql += " LEFT JOIN  \"MlaundryDetail\" D2                               \n"; // 疑似洗錢交易合理性明細檔
		sql += "        ON  D2.\"CustNo\" =  F.\"CustNo\"                       \n";
		sql += "       AND  D2.\"EntryDate\" =  F.\"EntryDate\"                 \n";
		sql += "       AND  D2.\"Factor\" =  2                                  \n";
		sql += "       AND  F.\"Factor2Cnt\" >= " + iFactor2Count;
		sql += " WHERE (CASE WHEN F.\"Factor1Amt\" >= " + iFactor1TotLimit;
		sql += "                  THEN 1                                        \n";
		sql += "             WHEN F.\"Factor2Cnt\" >= " + iFactor2Count;
		sql += "                  THEN 2                                        \n";
		sql += "             ELSE 0                                             \n";
		sql += "        END ) > 0                                               \n";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAll3(TitaVo titaVo) throws Exception {
//		String iFactor1TotLimit = titaVo.getParam("Factor1TotLimit"); // 洗錢樣態一金額合計超過
//		String iFactor2Count = titaVo.getParam("Factor2Count"); // 洗錢樣態二次數
//		String iFactor2AmtStart = titaVo.getParam("Factor2AmtStart"); // 洗錢樣態二單筆起始金額
//		String iFactor2AmtEnd = titaVo.getParam("Factor2AmtEnd"); // 洗錢樣態二單筆迄止金額
		String iFactor3TotLimit = titaVo.getParam("Factor3TotLimit"); // 洗錢樣態三金額合計超過
		String iFactorDays = titaVo.getParam("FactorDays"); // 統計期間天數
		String iFactorDays3 = titaVo.getParam("FactorDays3"); // 統計期間天數
		int iEntryDateS = Integer.parseInt(titaVo.getParam("EntryDateS")) + 19110000; // 入帳日期起日
		int iEntryDateE = Integer.parseInt(titaVo.getParam("EntryDateE")) + 19110000; // 入帳日期迄日

		String sql = "　";
		sql += " SELECT \n";
		sql += "   F.\"CustNo\"                              AS F0 \n"; // 戶號
		sql += "  ,F.\"EntryDate\"                           AS F1 \n"; // 入帳日期
		sql += "  ,CASE WHEN F.\"Factor3Amt\" >=  " + iFactor3TotLimit;
		sql += "            THEN  F.\"Factor3Cnt\"                 \n";
		sql += "        ELSE 0                                     \n";
		sql += "   END                                       AS F8 \n"; // 洗錢樣態三累計筆數
		sql += "  ,CASE WHEN F.\"Factor3Amt\" >= " + iFactor3TotLimit;
		sql += "            THEN  F.\"Factor3Amt\"                 \n";
		sql += "        ELSE 0                                     \n";
		sql += "   END                                       AS F9 \n"; // 洗錢樣態三累計金額
		sql += "  ,CASE WHEN NVL(D3.\"EntryDate\",0) > 0           \n";
		sql += "             THEN 1                                \n";
		sql += "        ELSE 0                                     \n";
		sql += "   END                                       AS F10\n"; // 洗錢樣態三資料重複 (1.是)
		sql += "  FROM (                                            \n";
		sql += "         SELECT                                     \n";
		sql += "          S.\"CustNo\"                       AS \"CustNo\"     \n";
		sql += "         ,S.\"EntryDate\"                    AS \"EntryDate\"  \n";
		sql += "         ,SUM(S.\"B3Cnt\")                   AS \"Factor3Cnt\" \n";
		sql += "         ,SUM(S.\"B3Amt\")                   AS \"Factor3Amt\" \n";
		sql += "         FROM (                                                \n";
		sql += "               SELECT                                          \n";
		sql += "                M.\"CustNo\"               AS \"CustNo\"       \n";
		sql += "               ,M.\"EntryDate\"            AS \"EntryDate\"    \n";
		sql += "               ,CASE WHEN M.\"Factor3\" = 1                    \n";
		sql += "                      AND B.\"DscptCode\" IN ('0087','0001')   \n"; // 0087ＡＴ存入&0001現金存入
		sql += "                      AND NVL(H.\"BatxExeCode\",'0') <> '8'    \n";
		sql += "                          THEN 1                               \n";
		sql += "                     ELSE 0                                    \n";
		sql += "                END                         AS \"B3Cnt\"       \n";
		sql += "               ,CASE WHEN M.\"Factor3\" = 1                    \n";
		sql += "                      AND B.\"DscptCode\" IN ('0087','0001')   \n";
		sql += "                      AND NVL(H.\"BatxExeCode\",'0') <> '8'    \n";
		sql += "                              THEN NVL(B.\"RepayAmt\",0)       \n";
		sql += "                     ELSE 0                                    \n";
		sql += "                END                         AS \"B3Amt\"       \n";
		sql += "               FROM                                            \n";
		sql += "                (SELECT                                      \n";
		sql += "                   \"CustNo\"                                \n";
		sql += "                  ,\"EntryDate\"                             \n";
		sql += "                  ,MAX(\"Factor3\")     AS \"Factor3\"       \n";
		sql += "                 FROM (                                      \n";
		sql += "                       SELECT                                   \n";
		sql += "                        B0.\"CustNo\"        AS \"CustNo\"      \n";
		sql += "                       ,B0.\"EntryDate\"     AS \"EntryDate\"   \n";
		sql += "                       ,CASE WHEN B0.\"DscptCode\" IN ('0087','0001')   \n";
		sql += "                                  THEN 1                        \n";
		sql += "                             ELSE 0                             \n";
		sql += "                        END                  AS \"Factor3\"     \n";
		sql += "                        FROM \"BankRmtf\" B0                    \n";
		sql += "                        LEFT JOIN \"BatxHead\" H0               \n";
		sql += "                          ON  H0.\"AcDate\" = B0.\"AcDate\"     \n";
		sql += "                         AND  H0.\"BatchNo\" = B0.\"BatchNo\"   \n";
		sql += "                        WHERE B0.\"EntryDate\" >= " + iEntryDateS;
		sql += "                          AND B0.\"EntryDate\" <= " + iEntryDateE;
		sql += "                          AND NVL(B0.\"CustNo\",0) > 0          \n";
		sql += "                          AND NVL(H0.\"BatxExeCode\",'0') <> '8'\n"; // 8.已刪除
		sql += "                      )                                         \n";
		sql += "                 GROUP BY \"CustNo\", \"EntryDate\"             \n";
		sql += "                ) M                                             \n";
		sql += "               LEFT JOIN \"BankRmtf\" B                         \n";
		sql += "                      ON  B.\"CustNo\" = M.\"CustNo\"           \n";
		sql += "                     AND (TO_DATE(M.\"EntryDate\",'YYYY-MM-DD') \n";
		sql += "                        - TO_DATE(B.\"EntryDate\",'YYYY-MM-DD') \n";
		sql += "                         )  BETWEEN 0 AND " + iFactorDays + " \n";
		sql += "               LEFT JOIN \"BatxHead\" H                         \n";
		sql += "                      ON  H.\"AcDate\" = B.\"AcDate\"           \n";
		sql += "                     AND  H.\"BatchNo\" = B.\"BatchNo\"         \n";
		sql += "               LEFT JOIN \"AchDeductMedia\" A                   \n";
		sql += "                      ON A.\"CustNo\" =  M.\"CustNo\"           \n";
		sql += "                     AND A.\"AcDate\" > 0                       \n";
		sql += "                     AND A.\"ReturnCode\" IN ('  ','00')        \n";
		sql += "                     AND (TO_DATE(M.\"EntryDate\",'YYYY-MM-DD') \n";
		sql += "                       -  TO_DATE(A.\"EntryDate\",'YYYY-MM-DD') \n";
		sql += "                          )  BETWEEN 0 AND " + iFactorDays3 + " \n";
		sql += "               LEFT JOIN \"PostDeductMedia\" P                  \n";
		sql += "                      ON P.\"CustNo\" =  M.\"CustNo\"           \n";
		sql += "                     AND P.\"ProcNoteCode\" IN ('  ','00')      \n";
		sql += "                     AND P.\"AcDate\" > 0                       \n";
		sql += "                     AND (TO_DATE(M.\"EntryDate\",'YYYY-MM-DD') \n";
		sql += "                        - TO_DATE(P.\"TransDate\",'YYYY-MM-DD') \n";
		sql += "                          )  BETWEEN 0 AND " + iFactorDays3 + " \n";
		sql += "               LEFT JOIN \"LoanCheque\" C                       \n";
		sql += "                      ON C.\"CustNo\" =  M.\"CustNo\"           \n";
		sql += "                     AND C.\"StatusCode\" =  '1'                \n"; // 1: 兌現入帳
		sql += "                     AND C.\"EntryDate\" > 0                    \n";
		sql += "                     AND (TO_DATE(M.\"EntryDate\",'YYYY-MM-DD') \n";
		sql += "                        - TO_DATE(C.\"EntryDate\",'YYYY-MM-DD') \n";
		sql += "                         )  BETWEEN 0 AND " + iFactorDays3 + "\n";
		sql += "              ) S                                               \n";
		sql += "         GROUP BY S.\"CustNo\", S.\"EntryDate\"                 \n";
		sql += "       ) F                                                      \n";
		sql += " LEFT JOIN  \"MlaundryDetail\" D3                               \n"; // 疑似洗錢交易合理性明細檔
		sql += "        ON  D3.\"CustNo\" =  F.\"CustNo\"                       \n";
		sql += "       AND  D3.\"EntryDate\" =  F.\"EntryDate\"                 \n";
		sql += "       AND  D3.\"Factor\" =  3                                  \n";
		sql += "       AND  F.\"Factor3Amt\" >= " + iFactor3TotLimit;
		sql += " WHERE (CASE WHEN F.\"Factor3Amt\" >= " + iFactor3TotLimit;
		sql += "                  THEN 3                                        \n";
		sql += "             ELSE 0                                             \n";
		sql += "        END ) > 0                                               \n";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query);
	}

	public List<Map<String, String>> findChkDtl12(TitaVo titaVo) throws Exception {
		String iFactorDays = titaVo.getParam("FactorDays"); // 統計期間天數
		String iFactor2AmtStart = titaVo.getParam("Factor2AmtStart"); // 洗錢樣態二單筆起始金額
		String iFactor2AmtEnd = titaVo.getParam("Factor2AmtEnd"); // 洗錢樣態二單筆迄止金額
		String sql = "　";
		sql += " SELECT \n";
		sql += "  F.\"EntryDate\"            AS F0 \n"; // 入帳日期
		sql += " ,F.\"Factor\"               AS F1 \n"; // 交易樣態
		sql += " ,F.\"CustNo\"               AS F2 \n"; // 戶號
		sql += " ,row_number() over (partition by F.\"EntryDate\", F.\"Factor\", F.\"CustNo\" order by F.\"DtlEntryDate\" ASC) \n";
		sql += "                             AS F3 \n"; // 明細序號
		sql += " ,F.\"DtlEntryDate\"         AS F4 \n"; // 明細入帳日期
		sql += " ,F.\"RepayItem\"            AS F5 \n"; // 來源
		sql += " ,F.\"DscptCode\"            AS F6 \n"; // 摘要代碼
		sql += " ,F.\"TxAmt\"                AS F7 \n"; // 交易金額
		sql += " ,F.\"TotalCnt\"             AS F8 \n"; // 累積筆數
		sql += " ,F.\"TotalAmt\"             AS F9 \n"; // 累積金額
		sql += " ,TO_CHAR(TO_DATE(F.\"EntryDate\",'YYYY-MM-DD') - " + iFactorDays + ",'YYYYMMDD') \n";
		sql += "                             AS F10 \n"; // 統計期間起日
		sql += "  FROM (                                            \n";
		sql += "        SELECT                                           \n";
		sql += "         D1.\"EntryDate\"            AS \"EntryDate\"    \n";
		sql += "        ,D1.\"Factor\"               AS \"Factor\"       \n";
		sql += "        ,D1.\"CustNo\"               AS \"CustNo\"       \n";
		sql += "        ,B2.\"EntryDate\"            AS \"DtlEntryDate\" \n";
		sql += "        ,TO_CHAR(SUBSTR(B2.\"VirtualAcctNo\", 1, 5))     \n";
		sql += "                                     AS \"RepayItem\"    \n";
		sql += "        ,B2.\"DscptCode\"            AS \"DscptCode\"    \n";
		sql += "        ,B2.\"RepayAmt\"             AS \"TxAmt\"        \n";
		sql += "        ,D1.\"TotalCnt\"             AS \"TotalCnt\"     \n";
		sql += "        ,D1.\"TotalAmt\"             AS \"TotalAmt\"     \n";
		sql += "        FROM  \"MlaundryDetail\" D1                      \n";
		sql += "        LEFT JOIN \"BankRmtf\" B2                        \n";
		sql += "               ON  B2.\"CustNo\" = D1.\"CustNo\"         \n";
		sql += "              AND  (TO_DATE(D1.\"EntryDate\",'YYYY-MM-DD') \n";
		sql += "                 -  TO_DATE(B2.\"EntryDate\",'YYYY-MM-DD')\n";
		sql += "                   )  BETWEEN 0 AND " + iFactorDays + "\n";
		sql += "        LEFT JOIN \"BatxHead\" H2                        \n";
		sql += "               ON  H2.\"AcDate\" = B2.\"AcDate\"         \n";
		sql += "              AND  H2.\"BatchNo\" = B2.\"BatchNo\"       \n";
		sql += "        WHERE D1.\"Rational\" = '?'                      \n";
		sql += "          AND NVL(H2.\"BatxExeCode\",'0') <> '8'         \n"; // 8.已刪除
		sql += "          AND NVL(B2.\"CustNo\",0)  > 0                  \n";
		sql += "        UNION ALL                                        \n";
		sql += "        SELECT                                           \n";
		sql += "         D2.\"EntryDate\"            AS \"EntryDate\"    \n";
		sql += "        ,D2.\"Factor\"               AS \"Factor\"       \n";
		sql += "        ,D2.\"CustNo\"               AS \"CustNo\"    \n";
		sql += "        ,A2.\"EntryDate\"            AS \"DtlEntryDate\" \n";
		sql += "        ,'銀扣'                      AS \"RepayItem\"    \n";
		sql += "        ,''                          AS \"DscptCode\"    \n";
		sql += "        ,A2.\"RepayAmt\"             AS \"TxAmt\"        \n";
		sql += "        ,D2.\"TotalCnt\"             AS \"TotalCnt\"     \n";
		sql += "        ,D2.\"TotalAmt\"             AS \"TotalAmt\"     \n";
		sql += "        FROM  \"MlaundryDetail\" D2                      \n";
		sql += "        LEFT JOIN \"AchDeductMedia\" A2                  \n";
		sql += "               ON A2.\"CustNo\" =  D2.\"CustNo\"         \n";
		sql += "              AND A2.\"AcDate\" > 0                      \n";
		sql += "              AND A2.\"ReturnCode\" IN ('00','  ')       \n";
		sql += "              AND (TO_DATE(D2.\"EntryDate\",'YYYY-MM-DD') \n";
		sql += "                -  TO_DATE(A2.\"EntryDate\",'YYYY-MM-DD')\n";
		sql += "                   )  BETWEEN 0 AND " + iFactorDays + "\n";
		sql += "        WHERE D2.\"Rational\" = '?'                      \n";
		sql += "          AND NVL(A2.\"CustNo\",0)  > 0                  \n";
		sql += "        UNION ALL                                        \n";
		sql += "        SELECT                                           \n";
		sql += "         D3.\"EntryDate\"            AS \"EntryDate\"    \n";
		sql += "        ,D3.\"Factor\"               AS \"Factor\"       \n";
		sql += "        ,D3.\"CustNo\"               AS \"CustNo\"    \n";
		sql += "        ,P2.\"TransDate\"            AS \"DtlEntryDate\" \n";
		sql += "        ,'郵局'                      AS \"RepayItem\"    \n";
		sql += "        ,''                          AS \"DscptCode\"    \n";
		sql += "        ,P2.\"RepayAmt\"             AS \"TxAmt\"        \n";
		sql += "        ,D3.\"TotalCnt\"             AS \"TotalCnt\"     \n";
		sql += "        ,D3.\"TotalAmt\"             AS \"TotalAmt\"     \n";
		sql += "        FROM  \"MlaundryDetail\" D3                      \n";
		sql += "        LEFT JOIN \"PostDeductMedia\" P2                 \n";
		sql += "               ON P2.\"CustNo\" =  D3.\"CustNo\"         \n";
		sql += "              AND P2.\"ProcNoteCode\" IN ('00','  ')     \n";
		sql += "              AND P2.\"AcDate\" > 0                      \n";
		sql += "              AND (TO_DATE(D3.\"EntryDate\",'YYYY-MM-DD') \n";
		sql += "                -  TO_DATE(P2.\"TransDate\",'YYYY-MM-DD')\n";
		sql += "                   )  BETWEEN 0 AND " + iFactorDays + "\n";
		sql += "        WHERE D3.\"Rational\" = '?'                      \n";
		sql += "          AND NVL(P2.\"CustNo\",0)  > 0                  \n";
		sql += "        UNION ALL                                        \n";
		sql += "        SELECT                                           \n";
		sql += "         D4.\"EntryDate\"            AS \"EntryDate\"    \n";
		sql += "        ,D4.\"Factor\"               AS \"Factor1Cnt\"   \n";
		sql += "        ,D4.\"CustNo\"               AS \"CustNo\"    \n";
		sql += "        ,C2.\"EntryDate\"            AS \"DtlEntryDate\" \n";
		sql += "        ,'支票'                      AS \"RepayItem\"    \n";
		sql += "        ,''                          AS \"DscptCode\"    \n";
		sql += "        ,C2.\"ChequeAmt\"            AS \"TxAmt\"        \n";
		sql += "        ,D4.\"TotalCnt\"             AS \"TotalCnt\"     \n";
		sql += "        ,D4.\"TotalAmt\"             AS \"TotalAmt\"     \n";
		sql += "        FROM  \"MlaundryDetail\" D4                      \n";
		sql += "        LEFT JOIN \"LoanCheque\" C2                      \n";
		sql += "               ON C2.\"CustNo\" =  D4.\"CustNo\"         \n";
		sql += "              AND C2.\"EntryDate\" > 0                   \n";
		sql += "              AND C2.\"StatusCode\" =  '1'               \n"; // 1: 兌現入帳
		sql += "              AND (TO_DATE(D4.\"EntryDate\",'YYYY-MM-DD')\n";
		sql += "                -  TO_DATE(C2.\"EntryDate\",'YYYY-MM-DD')\n";
		sql += "                   )  BETWEEN 0 AND " + iFactorDays + "\n";
		sql += "        WHERE D4.\"Rational\" = '?'                      \n";
		sql += "          AND NVL(C2.\"CustNo\",0)  > 0                  \n";
		sql += "      ) F                                                \n";
		sql += " WHERE (CASE WHEN F.\"Factor\" = 1                       \n";
		sql += "                  THEN 1                                 \n";
		sql += "             WHEN F.\"Factor\" = 2                       \n";
		sql += "              AND F.\"TxAmt\" >= " + iFactor2AmtStart + "\n";
		sql += "              AND F.\"TxAmt\" <= " + iFactor2AmtEnd + "\n";
		sql += "                  THEN 1                                 \n";
		sql += "        END ) > 0                                        \n";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query);
	}

	public List<Map<String, String>> findChkDtl3(TitaVo titaVo) throws Exception {
		String iFactorDays = titaVo.getParam("FactorDays"); // 統計期間天數
		String iFactorDays3 = titaVo.getParam("FactorDays3"); // 統計期間天數
		String iFactor2AmtStart = titaVo.getParam("Factor2AmtStart"); // 洗錢樣態二單筆起始金額
		String iFactor2AmtEnd = titaVo.getParam("Factor2AmtEnd"); // 洗錢樣態二單筆迄止金額
		String sql = "　";
		sql += " SELECT \n";
		sql += "  F.\"EntryDate\"            AS F0 \n"; // 入帳日期
		sql += " ,F.\"Factor\"               AS F1 \n"; // 交易樣態
		sql += " ,F.\"CustNo\"               AS F2 \n"; // 戶號
		sql += " ,row_number() over (partition by F.\"EntryDate\", F.\"Factor\", F.\"CustNo\" order by F.\"DtlEntryDate\" ASC) \n";
		sql += "                             AS F3 \n"; // 明細序號
		sql += " ,F.\"DtlEntryDate\"         AS F4 \n"; // 明細入帳日期
		sql += " ,F.\"RepayItem\"            AS F5 \n"; // 來源
		sql += " ,F.\"DscptCode\"            AS F6 \n"; // 摘要代碼
		sql += " ,F.\"TxAmt\"                AS F7 \n"; // 交易金額
		sql += " ,F.\"TotalCnt\"             AS F8 \n"; // 累積筆數
		sql += " ,F.\"TotalAmt\"             AS F9 \n"; // 累積金額
		sql += " ,TO_CHAR(TO_DATE(F.\"EntryDate\",'YYYY-MM-DD') - " + iFactorDays3 + ",'YYYYMMDD') \n";
		sql += "                             AS F10 \n"; // 統計期間起日
		sql += "  FROM (                                            \n";
		sql += "        SELECT                                           \n";
		sql += "         D1.\"EntryDate\"            AS \"EntryDate\"    \n";
		sql += "        ,D1.\"Factor\"               AS \"Factor\"       \n";
		sql += "        ,D1.\"CustNo\"               AS \"CustNo\"       \n";
		sql += "        ,B2.\"EntryDate\"            AS \"DtlEntryDate\" \n";
		sql += "        ,TO_CHAR(SUBSTR(B2.\"VirtualAcctNo\", 1, 5))     \n";
		sql += "                                     AS \"RepayItem\"    \n";
		sql += "        ,B2.\"DscptCode\"            AS \"DscptCode\"    \n";
		sql += "        ,B2.\"RepayAmt\"             AS \"TxAmt\"        \n";
		sql += "        ,D1.\"TotalCnt\"             AS \"TotalCnt\"     \n";
		sql += "        ,D1.\"TotalAmt\"             AS \"TotalAmt\"     \n";
		sql += "        FROM  \"MlaundryDetail\" D1                      \n";
		sql += "        LEFT JOIN \"BankRmtf\" B2                        \n";
		sql += "               ON  B2.\"CustNo\" = D1.\"CustNo\"         \n";
		sql += "              AND  (TO_DATE(D1.\"EntryDate\",'YYYY-MM-DD') \n";
		sql += "                 -  TO_DATE(B2.\"EntryDate\",'YYYY-MM-DD')\n";
		sql += "                   )  BETWEEN 0 AND " + iFactorDays3 + "\n";
		sql += "        LEFT JOIN \"BatxHead\" H2                        \n";
		sql += "               ON  H2.\"AcDate\" = B2.\"AcDate\"         \n";
		sql += "              AND  H2.\"BatchNo\" = B2.\"BatchNo\"       \n";
		sql += "        WHERE D1.\"Rational\" = '?'                      \n";
		sql += "          AND NVL(H2.\"BatxExeCode\",'0') <> '8'         \n"; // 8.已刪除
		sql += "          AND NVL(B2.\"CustNo\",0)  > 0                  \n";
		sql += "        UNION ALL                                        \n";
		sql += "        SELECT                                           \n";
		sql += "         D2.\"EntryDate\"            AS \"EntryDate\"    \n";
		sql += "        ,D2.\"Factor\"               AS \"Factor\"       \n";
		sql += "        ,D2.\"CustNo\"               AS \"CustNo\"    \n";
		sql += "        ,A2.\"EntryDate\"            AS \"DtlEntryDate\" \n";
		sql += "        ,'銀扣'                      AS \"RepayItem\"    \n";
		sql += "        ,''                          AS \"DscptCode\"    \n";
		sql += "        ,A2.\"RepayAmt\"             AS \"TxAmt\"        \n";
		sql += "        ,D2.\"TotalCnt\"             AS \"TotalCnt\"     \n";
		sql += "        ,D2.\"TotalAmt\"             AS \"TotalAmt\"     \n";
		sql += "        FROM  \"MlaundryDetail\" D2                      \n";
		sql += "        LEFT JOIN \"AchDeductMedia\" A2                  \n";
		sql += "               ON A2.\"CustNo\" =  D2.\"CustNo\"         \n";
		sql += "              AND A2.\"AcDate\" > 0                      \n";
		sql += "              AND A2.\"ReturnCode\" IN ('00','  ')       \n";
		sql += "              AND (TO_DATE(D2.\"EntryDate\",'YYYY-MM-DD') \n";
		sql += "                -  TO_DATE(A2.\"EntryDate\",'YYYY-MM-DD')\n";
		sql += "                   )  BETWEEN 0 AND " + iFactorDays3 + "\n";
		sql += "        WHERE D2.\"Rational\" = '?'                      \n";
		sql += "          AND NVL(A2.\"CustNo\",0)  > 0                  \n";
		sql += "        UNION ALL                                        \n";
		sql += "        SELECT                                           \n";
		sql += "         D3.\"EntryDate\"            AS \"EntryDate\"    \n";
		sql += "        ,D3.\"Factor\"               AS \"Factor\"       \n";
		sql += "        ,D3.\"CustNo\"               AS \"CustNo\"    \n";
		sql += "        ,P2.\"TransDate\"            AS \"DtlEntryDate\" \n";
		sql += "        ,'郵局'                      AS \"RepayItem\"    \n";
		sql += "        ,''                          AS \"DscptCode\"    \n";
		sql += "        ,P2.\"RepayAmt\"             AS \"TxAmt\"        \n";
		sql += "        ,D3.\"TotalCnt\"             AS \"TotalCnt\"     \n";
		sql += "        ,D3.\"TotalAmt\"             AS \"TotalAmt\"     \n";
		sql += "        FROM  \"MlaundryDetail\" D3                      \n";
		sql += "        LEFT JOIN \"PostDeductMedia\" P2                 \n";
		sql += "               ON P2.\"CustNo\" =  D3.\"CustNo\"         \n";
		sql += "              AND P2.\"ProcNoteCode\" IN ('00','  ')     \n";
		sql += "              AND P2.\"AcDate\" > 0                      \n";
		sql += "              AND (TO_DATE(D3.\"EntryDate\",'YYYY-MM-DD') \n";
		sql += "                -  TO_DATE(P2.\"TransDate\",'YYYY-MM-DD')\n";
		sql += "                   )  BETWEEN 0 AND " + iFactorDays3 + "\n";
		sql += "        WHERE D3.\"Rational\" = '?'                      \n";
		sql += "          AND NVL(P2.\"CustNo\",0)  > 0                  \n";
		sql += "        UNION ALL                                        \n";
		sql += "        SELECT                                           \n";
		sql += "         D4.\"EntryDate\"            AS \"EntryDate\"    \n";
		sql += "        ,D4.\"Factor\"               AS \"Factor1Cnt\"   \n";
		sql += "        ,D4.\"CustNo\"               AS \"CustNo\"    \n";
		sql += "        ,C2.\"EntryDate\"            AS \"DtlEntryDate\" \n";
		sql += "        ,'支票'                      AS \"RepayItem\"    \n";
		sql += "        ,''                          AS \"DscptCode\"    \n";
		sql += "        ,C2.\"ChequeAmt\"            AS \"TxAmt\"        \n";
		sql += "        ,D4.\"TotalCnt\"             AS \"TotalCnt\"     \n";
		sql += "        ,D4.\"TotalAmt\"             AS \"TotalAmt\"     \n";
		sql += "        FROM  \"MlaundryDetail\" D4                      \n";
		sql += "        LEFT JOIN \"LoanCheque\" C2                      \n";
		sql += "               ON C2.\"CustNo\" =  D4.\"CustNo\"         \n";
		sql += "              AND C2.\"EntryDate\" > 0                   \n";
		sql += "              AND C2.\"StatusCode\" =  '1'               \n"; // 1: 兌現入帳
		sql += "              AND (TO_DATE(D4.\"EntryDate\",'YYYY-MM-DD')\n";
		sql += "                -  TO_DATE(C2.\"EntryDate\",'YYYY-MM-DD')\n";
		sql += "                   )  BETWEEN 0 AND " + iFactorDays3 + "\n";
		sql += "        WHERE D4.\"Rational\" = '?'                      \n";
		sql += "          AND NVL(C2.\"CustNo\",0)  > 0                  \n";
		sql += "      ) F                                                \n";
		sql += " WHERE (CASE WHEN F.\"Factor\" = 3                       \n";
		sql += "              AND F.\"DscptCode\" IN ('0087','0001')     \n"; // 0087ＡＴ存入&0001現金存入
		sql += "                  THEN 1                                 \n";
		sql += "             ELSE 0                                      \n";
		sql += "        END ) > 0                                        \n";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query);
	}
}