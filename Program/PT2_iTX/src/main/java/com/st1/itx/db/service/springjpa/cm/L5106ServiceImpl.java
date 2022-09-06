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

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		int iYearMonth = Integer.parseInt(titaVo.getParam("YearMonth")) + 191100; // 資料年月
		int iYearMonth6 = iYearMonth % 100 <= 5 ? iYearMonth - 100 + 12 - 5 : iYearMonth - 5; // 覆審月份當月起算前6個月

		String sql = "　";
		sql += " SELECT :YearMonth AS \"YearMonth\" "; // 資料年月
		sql += "      , \"ConditionCode\" "; // 條件代碼
		sql += "      , \"CustNo\" "; // 借款人戶號
		sql += "      , \"FacmNo\" "; // 額度號碼
		sql += "      , \"ReCheckCode\" "; // 覆審記號 1.指定覆審、2.免覆審、空白
		sql += "      , \"FollowMark\" "; // 是否追蹤 自行註記
		sql += "      , \"ReChkYearMonth\" "; // 覆審年月
		sql += "      , \"DrawdownDate\" "; // 撥款日期
		sql += "      , \"LoanBal\" "; // 貸放餘額
		sql += "      , \"Evaluation\" "; // 評等
		sql += "      , \"CustTypeItem\" "; // 客戶別
		sql += "      , \"UsageItem\" "; // 用途別
		sql += "      , \"CityItem\" "; // 地區別
		sql += "      , \"ReChkUnit\" "; // 應覆審單位 同區域中心
		sql += "      , \"Remark\" "; // 備註
		sql += " FROM (SELECT CASE WHEN C.\"EntCode\" <> '1' AND M.\"LoanBal\" >= 30000000 "; // 01-個金3000萬以上 =>按全戶餘額條件
		sql += "                       THEN 1 ";
		sql += "                   WHEN C.\"EntCode\" = '1' AND M.\"LoanBal\" >= 30000000 "; // 02-企金3000萬以上 =>按全戶餘額條件
		sql += "                       THEN 2 ";
		sql += "                   WHEN C.\"EntCode\" <> '1' AND M.\"LoanBal\" >= 20000000 "; // 03-個金2000萬以上小於3000萬 => 按全戶餘額條件
		sql += "                       THEN 3 ";
		sql += "                   WHEN C.\"EntCode\" <> '1' AND M.\"LoanBal\" >= 1000000 "; // 04-個金100萬以上小於2000萬=> 按全戶餘額條件、最小撥貸日條件
		sql += "                       AND TRUNC( M.\"DrawdownDate\" / 100 ) = :YearMonth6 "; // 覆審月份起算前6個月
		sql += "                       THEN 4 ";
		sql += "                   WHEN C.\"EntCode\" = '1' AND M.\"LoanBal\" > 0 "; // 05-企金未達3000萬 => 按全戶餘額條件
		sql += "                       THEN 5 ";
		sql += "                       ELSE 0 ";
		sql += "              END                            AS \"ConditionCode\" "; // 條件代碼
		sql += "            , L.\"CustNo\"                   AS \"CustNo\" "; // 借款人戶號
		sql += "            , L.\"FacmNo\"                   AS \"FacmNo\" "; // 額度號碼
		sql += "            , NVL( R.\"ReCheckCode\", ' ' )  AS \"ReCheckCode\" "; // 覆審記號 1.指定覆審、2.免覆審、空白
		sql += "            , NVL( R.\"FollowMark\", ' ' )   AS \"FollowMark\" "; // 是否追蹤 自行註記
		sql += "            , NVL( R.\"ReChkYearMonth\", 0 ) AS \"ReChkYearMonth\" "; // 覆審年月
		sql += "            , F.\"FirstDrawdownDate\"        AS \"DrawdownDate\" "; // 撥款日期
		sql += "            , L.\"PrinBalance\"              AS \"LoanBal\" "; // 貸放餘額
		sql += "            , 0                              AS \"Evaluation\" "; // 評等
		sql += "            , CC.\"Item\"                    AS \"CustTypeItem\" "; // 客戶別
		sql += "            , CU.\"Item\"                    AS \"UsageItem\" "; // 用途別
		sql += "            , CI.\"CityItem\"                AS \"CityItem\" "; // 地區別
		sql += "            , NVL( R.\"ReChkUnit\", ' ' )    AS \"ReChkUnit\" "; // 應覆審單位 同區域中心
		sql += "            , NVL( R.\"Remark\", ' ' )       AS \"Remark\" "; // 備註
		sql += "       FROM (SELECT T.\"CustNo\"                     AS \"CustNo\" ";
		sql += "                  , SUM( T.\"PrinBalance\" )         AS \"LoanBal\" ";
		sql += "                  , MIN( CASE WHEN T.\"Status\" IN (0, 4) ";
		sql += "                                  THEN 0 ";
		sql += "                                  ELSE 1 ";
		sql += "                         END )                     AS \"Status\" ";
		sql += "                  , MIN( FAC.\"FirstDrawdownDate\" ) AS \"DrawdownDate\" ";
		sql += "             FROM \"CollListTmp\" T "; // 法催紀錄清單暫存檔
		sql += "             LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = T.\"CustNo\" ";
		sql += "                                      AND FAC.\"FacmNo\" = T.\"FacmNo\" ";
		sql += "             WHERE T.\"Status\" in (0, 2, 4, 7) "; // 0: 正常戶 2: 催收戶 4.逾期戶 7: 部分轉呆戶
		sql += "             GROUP BY T.\"CustNo\" ";
		sql += "            ) M ";
		sql += "       LEFT JOIN \"CollListTmp\" L ON L.\"CustNo\" = M.\"CustNo\" ";
		sql += "                                  AND L.\"Status\" in (0, 2, 4, 7) ";
		sql += "       LEFT JOIN (SELECT \"CustNo\" ";
		sql += "                       , \"FacmNo\" ";
		sql += "                       , \"ReCheckCode\" ";
		sql += "                       , \"FollowMark\" ";
		sql += "                       , \"ReChkYearMonth\" ";
		sql += "                       , \"ReChkUnit\" ";
		sql += "                       , \"Remark\" ";
		sql += "                       , ROW_NUMBER( ) Over (Partition By \"CustNo\" ,\"FacmNo\" Order By \"YearMonth\" DESC) "; // 最新案件
		sql += "               AS ROW_NUMBER ";
		sql += "                  FROM \"InnReCheck\" "; // 覆審案件明細檔
		sql += "                  UNION ALL ";
		sql += "                  SELECT \"CustNo\" ";
		sql += "                       , \"FacmNo\" ";
		sql += "                       , ' ' "; // "ReCheckCode"
		sql += "                       , ' ' "; // "FollowMark"
		sql += "                       , \"ReChkYearMonth\" ";
		sql += "                       , u' ' "; // "ReChkUnit"
		sql += "                       , \"Remark\" ";
		sql += "                       , ROW_NUMBER( ) Over (Partition By \"CustNo\" ,\"FacmNo\" Order By \"ReChkYearMonth\" DESC) "; // 最新案件
		sql += "                      AS ROW_NUMBER ";
		sql += "                  FROM \"SpecInnReCheck\" "; // 覆審案件明細檔
		sql += "                 ) R ON R.\"CustNo\" = L.\"CustNo\" ";
		sql += "                    AND R.\"FacmNo\" = L.\"FacmNo\" ";
		sql += "                    AND R.\"ROW_NUMBER\" = 1 "; // 最新
		sql += "       LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = L.\"CustNo\" ";
		sql += "                              AND F.\"FacmNo\" = L.\"FacmNo\" ";
		sql += "       LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = L.\"CustNo\" "; // 客戶資料主檔 EntCode 企金別 0:個金 1:企金 2:企金自然人
		sql += "       LEFT JOIN \"ClMain\" CL ON CL.\"ClCode1\" = L.\"ClCode1\" "; // 擔保品主檔
		sql += "                              AND CL.\"ClCode2\" = L.\"ClCode2\" ";
		sql += "                              AND CL.\"ClNo\" = L.\"ClNo\" ";
		sql += "       LEFT JOIN \"CdCity\" CI ON CI.\"CityCode\" = CL.\"CityCode\" ";
		sql += "       LEFT JOIN \"CdCode\" CU ON CU.\"DefCode\" = 'UsageCode' ";
		sql += "                              AND CU.\"Code\" = F.\"UsageCode\" ";
		sql += "       LEFT JOIN \"CdCode\" CC ON CC.\"DefCode\" = 'CustTypeCode' ";
		sql += "                              AND CC.\"Code\" = C.\"CustTypeCode\" ";
		sql += "       WHERE M.\"Status\" = 0 ";
		sql += "         AND ( CASE WHEN C.\"EntCode\" <> '1' AND M.\"LoanBal\" >= 30000000 "; // 01-個金3000萬以上 =>按全戶餘額條件
		sql += "                        THEN 1 ";
		sql += "                    WHEN C.\"EntCode\" = '1' AND M.\"LoanBal\" >= 30000000 "; // 02-企金3000萬以上 =>按全戶餘額條件
		sql += "                        THEN 2 ";
		sql += "                    WHEN C.\"EntCode\" <> '1' AND M.\"LoanBal\" >= 20000000 "; // 03-個金2000萬以上小於3000萬 => 按全戶餘額條件
		sql += "                        THEN 3 ";
		sql += "                    WHEN C.\"EntCode\" <> '1' AND M.\"LoanBal\" >= 1000000 "; // 04-個金100萬以上小於2000萬=> 按全戶餘額條件、最小撥貸日條件
		sql += "                        AND TRUNC( M.\"DrawdownDate\" / 100 ) = :YearMonth6 "; // 覆審月份起算前6個月
		sql += "                        THEN 4 ";
		sql += "                    WHEN C.\"EntCode\" = '1' AND M.\"LoanBal\" > 0 "; // 05-企金未達3000萬 => 按全戶餘額條件
		sql += "                        THEN 5 ";
		sql += "                        ELSE 0 ";
		sql += "               END ) > 0 ";
		sql += "       UNION ALL ";
		sql += "       SELECT 6                              AS \"ConditionCode\" "; // 06-土地追蹤 => 按全戶餘額條件 > 0
		sql += "            , L.\"CustNo\"                   AS \"CustNo\" "; // 借款人戶號
		sql += "            , L.\"FacmNo\"                   AS \"FacmNo\" "; // 額度號碼
		sql += "            , NVL( R.\"ReCheckCode\", ' ' )  AS \"ReCheckCode\" "; // 覆審記號 1.指定覆審、2.免覆審、空白
		sql += "            , NVL( R.\"FollowMark\", ' ' )   AS \"FollowMark\" "; // 是否追蹤 自行註記
		sql += "            , NVL( R.\"ReChkYearMonth\", 0 ) AS \"ReChkYearMonth\" "; // 覆審年月
		sql += "            , F.\"FirstDrawdownDate\"        AS \"DrawdownDate\" "; // 撥款日期
		sql += "            , L.\"PrinBalance\"              AS \"LoanBal\" "; // 貸放餘額
		sql += "            , 0                              AS \"Evaluation\" "; // 評等
		sql += "            , CC.\"Item\"                    AS \"CustTypeItem\" "; // 客戶別
		sql += "            , CU.\"Item\"                    AS \"UsageItem\" "; // 用途別
		sql += "            , CI.\"CityItem\"                AS \"CityItem\" "; // 地區別
		sql += "            , NVL( R.\"ReChkUnit\", ' ' )    AS \"ReChkUnit\" "; // 應覆審單位 同區域中心
		sql += "            , NVL( R.\"Remark\", ' ' )       AS \"Remark\" "; // 備註
		sql += "       FROM (SELECT T.\"CustNo\"             AS \"CustNo\" ";
		sql += "                  , SUM( T.\"PrinBalance\" ) AS \"LoanBal\" ";
		sql += "                  , MIN( CASE WHEN T.\"Status\" IN (0, 4) ";
		sql += "                                  THEN 0 ";
		sql += "                                  ELSE 1 ";
		sql += "                         END )             AS \"Status\" ";
		sql += "                  , MAX( CASE WHEN T.\"ClCode1\" = 2 "; // 土地
		sql += "                                  THEN 1 ";
		sql += "                                  ELSE 0 ";
		sql += "                         END )             AS \"Land\" ";
		sql += "             FROM \"CollListTmp\" T "; // 法催紀錄清單暫存檔
		sql += "             LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = T.\"CustNo\" ";
		sql += "                                    AND FAC.\"FacmNo\" = T.\"FacmNo\" ";
		sql += "             WHERE T.\"Status\" in (0, 2, 4, 7) "; // 0: 正常戶 2: 催收戶 4.逾期戶 7: 部分轉呆戶
		sql += "             GROUP BY T.\"CustNo\" ";
		sql += "            ) M ";
		sql += "       LEFT JOIN \"CollListTmp\" L ON L.\"CustNo\" = M.\"CustNo\" ";
		sql += "                                AND L.\"Status\" in (0, 2, 4, 7) ";
		sql += "       LEFT JOIN (SELECT \"CustNo\" ";
		sql += "                       , \"FacmNo\" ";
		sql += "                       , \"ReCheckCode\" ";
		sql += "                       , \"FollowMark\" ";
		sql += "                       , \"ReChkYearMonth\" ";
		sql += "                       , \"ReChkUnit\" ";
		sql += "                       , \"Remark\" ";
		sql += "                       , ROW_NUMBER( ) Over (Partition By \"CustNo\" ,\"FacmNo\" Order By \"YearMonth\" DESC) "; // 最新案件
		sql += "               AS ROW_NUMBER ";
		sql += "                  FROM \"InnReCheck\" "; // 覆審案件明細檔
		sql += "                  UNION ALL ";
		sql += "                  SELECT \"CustNo\" ";
		sql += "                       , \"FacmNo\" ";
		sql += "                       , ' ' "; // "ReCheckCode"
		sql += "                       , ' ' "; // "FollowMark"
		sql += "                       , \"ReChkYearMonth\" ";
		sql += "                       , u' ' "; // "ReChkUnit"
		sql += "                       , \"Remark\" ";
		sql += "                       , ROW_NUMBER( ) Over (Partition By \"CustNo\" ,\"FacmNo\" Order By \"ReChkYearMonth\" DESC) "; // 最新案件
		sql += "                      AS ROW_NUMBER ";
		sql += "                  FROM \"SpecInnReCheck\" "; // 覆審案件明細檔
		sql += "                 ) R ON R.\"CustNo\" = L.\"CustNo\" ";
		sql += "                    AND R.\"FacmNo\" = L.\"FacmNo\" ";
		sql += "                    AND R.\"ROW_NUMBER\" = 1 "; // 最新
		sql += "       LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = L.\"CustNo\" ";
		sql += "                              AND F.\"FacmNo\" = L.\"FacmNo\" ";
		sql += "       LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = L.\"CustNo\" "; // 客戶資料主檔 EntCode 企金別 0:個金 1:企金 2:企金自然人
		sql += "       LEFT JOIN \"ClMain\" CL ON CL.\"ClCode1\" = L.\"ClCode1\" ";
		sql += "                              AND CL.\"ClCode2\" = L.\"ClCode2\" ";
		sql += "                              AND CL.\"ClNo\" = L.\"ClNo\" "; // 擔保品主檔
		sql += "       LEFT JOIN \"CdCity\" CI ON CI.\"CityCode\" = CL.\"CityCode\" ";
		sql += "       LEFT JOIN \"CdCode\" CU ON CU.\"DefCode\" = 'UsageCode' ";
		sql += "                              AND CU.\"Code\" = F.\"UsageCode\" ";
		sql += "       LEFT JOIN \"CdCode\" CC ON CC.\"DefCode\" = 'CustTypeCode' ";
		sql += "                              AND CC.\"Code\" = C.\"CustTypeCode\" ";
		sql += "       WHERE M.\"Status\" = 0 "; // 全戶催收則跳過
		sql += "         AND M.\"Land\" = 1 "; // 土地
		sql += "      ) ";

		// 這段 union all 的兩段 queries 條件很相似，可能還有合併空間以便後續維護
		// 但因不確定實際應用面上會不會出問題，先維持原樣 -- xiangwei

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("YearMonth", iYearMonth);
		query.setParameter("YearMonth6", iYearMonth6);

		return this.convertToMap(query);
	}
}
