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

@Service("l5106ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L5106ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		int iYearMonth = Integer.parseInt(titaVo.getParam("YearMonth")) + 191100; // 資料年月
		int iYearMonth6 = iYearMonth % 100 <= 5 ? iYearMonth - 100 + 12 - 5 : iYearMonth - 5; // 覆審月份當月起算前6個月
		String sql = "　";
		sql += "  SELECT                                           \r\n" + iYearMonth + " AS F0    --資料年月  \r\n" + "   ,\"ConditionCode\"       AS F1    --條件代碼  \r\n"
				+ "   ,\"CustNo\"              AS F2    --借款人戶號  \r\n" + "   ,\"FacmNo\"              AS F3    --額度號碼  \r\n" + "   ,\"ReCheckCode\"         AS F4    --覆審記號     1.指定覆審、2.免覆審、空白\r\n"
				+ "   ,\"FollowMark\"          AS F5    --是否追蹤     自行註記\r\n" + "   ,\"ReChkYearMonth\"      AS F6    --覆審年月   \r\n" + "   ,\"DrawdownDate\"        AS F7    --撥款日期   \r\n"
				+ "   ,\"LoanBal\"             AS F8    --貸放餘額  \r\n" + "   ,\"Evaluation\"          AS F9    --評等         \r\n" + "   ,\"CustTypeItem\"        AS F10   --客戶別      \r\n"
				+ "   ,\"UsageItem\"           AS F11   --用途別      \r\n" + "   ,\"CityItem\"            AS F12   --地區別      \r\n" + "   ,\"ReChkUnit\"           AS F13   --應覆審單位    同區域中心\r\n"
				+ "   ,\"Remark\"              AS F14   --備註  \r\n" + "  FROM (\r\n" + "        SELECT \r\n"
				+ "          CASE WHEN C.\"EntCode\" <> '1' AND  M.\"LoanBal\" >= 30000000    --  01-個金3000萬以上 =>按全戶餘額條件 \r\n" + "                    THEN  1\r\n"
				+ "               WHEN C.\"EntCode\" = '1' AND  M.\"LoanBal\" >= 30000000     --  02-企金3000萬以上 =>按全戶餘額條件 \r\n" + "                    THEN  2          \r\n"
				+ "               WHEN C.\"EntCode\" <> '1' AND  M.\"LoanBal\" >= 20000000    --  03-個金2000萬以上小於3000萬 => 按全戶餘額條件 \r\n" + "                    THEN  3          \r\n"
				+ "               WHEN C.\"EntCode\" <> '1' AND  M.\"LoanBal\" >= 1000000     --	04-個金100萬以上小於2000萬=> 按全戶餘額條件、最小撥貸日條件 \r\n"
				+ "                AND TRUNC (M.\"DrawdownDate\" / 100) = " + iYearMonth6 + " --  覆審月份起算前6個月\r\n" + "                    THEN  4          \r\n"
				+ "               WHEN C.\"EntCode\" = '1' AND  M.\"LoanBal\" > 0             --  05-企金未達3000萬 => 按全戶餘額條件\r\n" + "                    THEN  5  \r\n"
				+ "               ELSE       0     \r\n" + "          END                        AS \"ConditionCode\"       --條件代碼  \r\n"
				+ "         ,L.\"CustNo\"                 AS \"CustNo\"              --借款人戶號  \r\n" + "         ,L.\"FacmNo\"                 AS \"FacmNo\"              --額度號碼  \r\n"
				+ "         ,NVL(R.\"ReCheckCode\",' ')   AS \"ReCheckCode\"         --覆審記號     1.指定覆審、2.免覆審、空白\r\n"
				+ "         ,NVL(R.\"FollowMark\",' ')    AS \"FollowMark\"          --是否追蹤     自行註記\r\n" + "         ,NVL(R.\"ReChkYearMonth\",0)  AS \"ReChkYearMonth\"      --覆審年月   \r\n"
				+ "         ,F.\"FirstDrawdownDate\"      AS \"DrawdownDate\"        --撥款日期   \r\n" + "         ,L.\"PrinBalance\"            AS \"LoanBal\"             --貸放餘額  \r\n"
				+ "         ,0                          AS \"Evaluation\"          --評等  \r\n" + "         ,CC.\"Item\"                  AS \"CustTypeItem\"        --客戶別  \r\n"
				+ "         ,CU.\"Item\"                  AS \"UsageItem\"           --用途別  \r\n" + "         ,CI.\"CityItem\"              AS \"CityItem\"            --地區別  \r\n"
				+ "         ,NVL(R.\"ReChkUnit\",' ')     AS \"ReChkUnit\"           --應覆審單位    同區域中心\r\n" + "         ,NVL(R.\"Remark\",' ')        AS \"Remark\"              --備註  \r\n"
				+ "         FROM ( SELECT\r\n" + "                 T.\"CustNo\"                    AS \"CustNo\" \r\n" + "                ,SUM(T.\"PrinBalance\")          AS \"LoanBal\" \r\n"
				+ "                ,MIN(CASE WHEN T.\"Status\" IN (0,4) \r\n" + "                               THEN 0 \r\n" + "                          ELSE 1\r\n"
				+ "                     END)                      AS \"Status\"\r\n" + "                ,MIN(FAC.\"FirstDrawdownDate\")	AS \"DrawdownDate\"    \r\n"
				+ "                FROM  \"CollListTmp\" T  -- 法催紀錄清單暫存檔\r\n" + "                LEFT JOIN \"FacMain\" FAC \r\n" + "                       ON  FAC.\"CustNo\" = T.\"CustNo\"\r\n"
				+ "                      AND  FAC.\"FacmNo\" = T.\"FacmNo\"\r\n" + "                WHERE T.\"Status\" in (0,2,4,7)   --0: 正常戶  2: 催收戶 4.逾期戶 7: 部分轉呆戶 \r\n"
				+ "                GROUP BY T.\"CustNo\"\r\n" + "              ) M   \r\n" + "         LEFT JOIN \"CollListTmp\" L ON  L.\"CustNo\" = M.\"CustNo\"      \r\n"
				+ "                                  AND  L.\"Status\" in (0,2,4,7)                                                            \r\n" + "         LEFT JOIN (\r\n"
				+ "                     SELECT \r\n" + "                      \"CustNo\" \r\n" + "                     ,\"FacmNo\"                           \r\n"
				+ "                     ,\"ReCheckCode\"       \r\n" + "                     ,\"FollowMark\"         \r\n" + "                     ,\"ReChkYearMonth\"     \r\n"
				+ "                     ,\"ReChkUnit\"         \r\n" + "                     ,\"Remark\"             \r\n"
				+ "                     ,ROW_NUMBER() Over (Partition By \"CustNo\" ,\"FacmNo\" \r\n" + "                                         Order By \"YearMonth\" DESC) -- 最新案件 \r\n"
				+ "                                    AS  ROW_NUMBER\r\n" + "                     FROM \"InnReCheck\" 	-- 覆審案件明細檔\r\n"
				+ "                    ) R ON R.\"CustNo\" = L.\"CustNo\"                                                \r\n" + "                       AND R.\"FacmNo\" = L.\"FacmNo\"\r\n"
				+ "                       AND R.\"ROW_NUMBER\" = 1               -- 最新\r\n" + "          LEFT JOIN \"FacMain\" F ON  F.\"CustNo\" = L.\"CustNo\"\r\n"
				+ "                               AND  F.\"FacmNo\" = L.\"FacmNo\"\r\n"
				+ "          LEFT JOIN \"CustMain\" C ON  C.\"CustNo\" = L.\"CustNo\"      --客戶資料主檔 EntCode	企金別	0:個金 1:企金 2:企金自然人 \r\n"
				+ "          LEFT JOIN \"ClMain\" CL ON CL.\"ClCode1\" = L.\"ClCode1\"                                             -- 擔保品主檔\r\n"
				+ "                               AND CL.\"ClCode2\" = L.\"ClCode2\" \r\n" + "                               AND CL.\"ClNo\"    = L.\"ClNo\"\r\n"
				+ "          LEFT JOIN \"CdCity\" CI ON CI.\"CityCode\" = CL.\"CityCode\"\r\n" + "          LEFT JOIN \"CdCode\" CU ON CU.\"DefCode\" = 'UsageCode' \r\n"
				+ "                               AND CU.\"Code\"    = F.\"UsageCode\" \r\n" + "          LEFT JOIN \"CdCode\" CC ON CC.\"DefCode\" = 'CustTypeCode' \r\n"
				+ "                               AND CC.\"Code\"    = C.\"CustTypeCode\"\r\n" + "          WHERE M.\"Status\" = 0                                    --全戶催收則跳過\r\n"
				+ "           AND      ( CASE WHEN C.\"EntCode\" <> '1' AND  M.\"LoanBal\" >= 30000000    --  01-個金3000萬以上 =>按全戶餘額條件 \r\n" + "                           THEN  1\r\n"
				+ "                      WHEN C.\"EntCode\" = '1' AND  M.\"LoanBal\" >= 30000000          --  02-企金3000萬以上 =>按全戶餘額條件 \r\n" + "                           THEN  2          \r\n"
				+ "                      WHEN C.\"EntCode\" <> '1' AND  M.\"LoanBal\" >= 20000000         --  03-個金2000萬以上小於3000萬 => 按全戶餘額條件 \r\n" + "                           THEN  3          \r\n"
				+ "                      WHEN C.\"EntCode\" <> '1' AND  M.\"LoanBal\" >= 1000000          --	04-個金100萬以上小於2000萬=> 按全戶餘額條件、最小撥貸日條件 \r\n"
				+ "                       AND TRUNC (M.\"DrawdownDate\" / 100) = " + iYearMonth6 + " --  覆審月份起算前6個月\r\n" + "                           THEN  4          \r\n"
				+ "                      WHEN C.\"EntCode\" = '1' AND  M.\"LoanBal\" > 0                  --  05-企金未達3000萬 => 按全戶餘額條件\r\n" + "                           THEN  5  \r\n"
				+ "                      ELSE       0     \r\n" + "                  END  )   > 0  \r\n" + "        UNION ALL\r\n" + "        SELECT \r\n"
				+ "         6                          AS \"ConditionCode\"       --條件代碼     -- 06-土地追蹤 => 按全戶餘額條件 > 0\r\n"
				+ "        ,L.\"CustNo\"                 AS \"CustNo\"              --借款人戶號  \r\n" + "        ,L.\"FacmNo\"                 AS \"FacmNo\"              --額度號碼  \r\n"
				+ "        ,NVL(R.\"ReCheckCode\",' ')   AS \"ReCheckCode\"         --覆審記號     1.指定覆審、2.免覆審、空白\r\n"
				+ "        ,NVL(R.\"FollowMark\",' ')    AS \"FollowMark\"          --是否追蹤     自行註記\r\n" + "        ,NVL(R.\"ReChkYearMonth\",0)  AS \"ReChkYearMonth\"      --覆審年月   \r\n"
				+ "        ,F.\"FirstDrawdownDate\"      AS \"DrawdownDate\"        --撥款日期   \r\n" + "        ,L.\"PrinBalance\"            AS \"LoanBal\"             --貸放餘額  \r\n"
				+ "        ,0                          AS \"Evaluation\"          --評等  \r\n" + "        ,CC.\"Item\"                  AS \"CustTypeItem\"        --客戶別  \r\n"
				+ "        ,CU.\"Item\"                  AS \"UsageItem\"           --用途別  \r\n" + "        ,CI.\"CityItem\"              AS \"CityItem\"            --地區別  \r\n"
				+ "        ,NVL(R.\"ReChkUnit\",' ')     AS \"ReChkUnit\"           --應覆審單位    同區域中心\r\n" + "        ,NVL(R.\"Remark\",' ')        AS \"Remark\"              --備註  \r\n"
				+ "        FROM ( SELECT\r\n" + "                T.\"CustNo\"                    AS \"CustNo\" \r\n" + "               ,SUM(T.\"PrinBalance\")          AS \"LoanBal\" \r\n"
				+ "               ,MIN(CASE WHEN T.\"Status\" IN (0,4) \r\n" + "                              THEN 0 \r\n" + "                         ELSE 1\r\n"
				+ "                    END)                      AS \"Status\"\r\n" + "               ,MAX(CASE WHEN T.\"ClCode1\" = 2                 -- 土地\r\n"
				+ "                              THEN 1 \r\n" + "                         ELSE 0\r\n" + "                    END)                      AS \"Land\"        \r\n"
				+ "               FROM  \"CollListTmp\" T  -- 法催紀錄清單暫存檔\r\n" + "               LEFT JOIN \"FacMain\" FAC \r\n" + "                      ON  FAC.\"CustNo\" = T.\"CustNo\"\r\n"
				+ "                     AND  FAC.\"FacmNo\" = T.\"FacmNo\"\r\n" + "               WHERE T.\"Status\" in (0,2,4,7)   --0: 正常戶  2: 催收戶 4.逾期戶 7: 部分轉呆戶 \r\n"
				+ "               GROUP BY T.\"CustNo\"\r\n" + "             ) M   \r\n" + "        LEFT JOIN \"CollListTmp\" L ON  L.\"CustNo\" = M.\"CustNo\"      \r\n"
				+ "                                 AND  L.\"Status\" in (0,2,4,7)          \r\n" + "        LEFT JOIN (\r\n" + "                    SELECT \r\n"
				+ "                     \"CustNo\" \r\n" + "                    ,\"FacmNo\"                           \r\n" + "                    ,\"ReCheckCode\"       \r\n"
				+ "                    ,\"FollowMark\"         \r\n" + "                    ,\"ReChkYearMonth\"     \r\n" + "                    ,\"ReChkUnit\"         \r\n"
				+ "                    ,\"Remark\"             \r\n" + "                    ,ROW_NUMBER() Over (Partition By \"CustNo\" ,\"FacmNo\" \r\n"
				+ "                                        Order By \"YearMonth\" DESC) -- 最新案件 \r\n" + "                                   AS  ROW_NUMBER\r\n"
				+ "                    FROM \"InnReCheck\" 	-- 覆審案件明細檔\r\n" + "                   ) R ON R.\"CustNo\" = L.\"CustNo\"                                                \r\n"
				+ "                      AND R.\"FacmNo\" = L.\"FacmNo\"\r\n" + "                      AND R.\"ROW_NUMBER\" = 1               -- 最新\r\n"
				+ "         LEFT JOIN \"FacMain\" F ON  F.\"CustNo\" = L.\"CustNo\"\r\n" + "                              AND  F.\"FacmNo\" = L.\"FacmNo\"\r\n"
				+ "         LEFT JOIN \"CustMain\" C ON  C.\"CustNo\" = L.\"CustNo\"      --客戶資料主檔 EntCode	企金別	0:個金 1:企金 2:企金自然人 \r\n"
				+ "         LEFT JOIN \"ClMain\" CL ON CL.\"ClCode1\" = L.\"ClCode1\"                                             -- 擔保品主檔\r\n"
				+ "                              AND CL.\"ClCode2\" = L.\"ClCode2\" \r\n" + "                              AND CL.\"ClNo\"    = L.\"ClNo\"\r\n"
				+ "         LEFT JOIN \"CdCity\" CI ON CI.\"CityCode\" = CL.\"CityCode\"\r\n" + "         LEFT JOIN \"CdCode\" CU ON CU.\"DefCode\" = 'UsageCode' \r\n"
				+ "                              AND CU.\"Code\"    = F.\"UsageCode\" \r\n" + "         LEFT JOIN \"CdCode\" CC ON CC.\"DefCode\" = 'CustTypeCode' \r\n"
				+ "                              AND CC.\"Code\"    = C.\"CustTypeCode\"\r\n" + "         WHERE M.\"Status\" = 0                                    --全戶催收則跳過\r\n"
				+ "          AND  M.\"Land\"   = 1                                    -- 土地\r\n" + "       )";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query.getResultList());
	}
}
