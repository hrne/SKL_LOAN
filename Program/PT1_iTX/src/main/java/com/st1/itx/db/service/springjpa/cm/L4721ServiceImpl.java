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
import com.st1.itx.util.date.DateUtil;

@Service
@Repository
public class L4721ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	DateUtil dateUtil;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * @param itxkind   利率種類
	 * @param iCustType 客戶別
	 * @param sAdjDate  利率調整起日
	 * @param eAdjDate  利率調整止日
	 * @param prodNos   商品利率代碼
	 * @param titaVo
	 * @return
	 * @throws Exception
	 * 
	 * 
	 */
	public List<Map<String, String>> findAll(int itxkind, int iCustType, int sAdjDate, int eAdjDate, String prodNos,
			TitaVo titaVo) throws Exception {
		this.info("L4721ServiceImpl findAll");

//		// iExecCode 0: 一般、 9.定期機動檢核件
		int iTxKind = itxkind;
//		輸入畫面 戶別 CustType 1:個金;2:企金（含企金自然人）
//		客戶檔 0:個金1:企金2:企金自然人
		int iEntCode1 = 0;
		int iEntCode2 = 0;
		if (iCustType == 2) {
			iEntCode1 = 1;
			iEntCode2 = 2;
		}

		String sql = "　";

		sql += " select  distinct ";
		sql += "    r.\"CustNo\"                                 as \"CustNo\"  "; // 戶號
		sql += "   ,MAX(b.\"SpecificDd\")                        as \"MaxSpecificDd\"  ";
		sql += "   ,MIN(b.\"SpecificDd\")                        as \"MinSpecificDd\"  ";
		sql += " from (                                             ";
		sql += "           select                                       ";
		sql += "            \"CustNo\"                              ";
		sql += "           ,\"FacmNo\"                              ";
		sql += "           ,\"BormNo\"                              ";
		sql += "           ,\"FitRate\"                              ";
		sql += "           ,\"EffectDate\"                           ";
		sql += "           ,\"RateCode\"                           ";
		sql += "           ,\"ProdNo\"                           ";
		sql += "           ,\"BaseRateCode\"                           ";
		sql += "           ,row_number() over (partition by \"CustNo\", \"FacmNo\", \"BormNo\" order by \"EffectDate\" Desc) as \"seq\" ";
		sql += "           from \"LoanRateChange\"                           ";
		sql += "  		   where \"EffectDate\" >=" + sAdjDate;
		sql += "   			 and \"EffectDate\" <=" + eAdjDate;
		sql += "        ) r ";
		sql += " left join(                                             ";
		sql += "           select                                       ";
		sql += "            \"CustNo\"                              ";
		sql += "           ,\"FacmNo\"                              ";
		sql += "           ,\"BormNo\"                              ";
		sql += "           ,\"FitRate\"                              ";
		sql += "           ,\"EffectDate\"                           ";
		sql += "           ,\"RateCode\"                           ";
		sql += "           ,row_number() over (partition by \"CustNo\", \"FacmNo\", \"BormNo\" order by \"EffectDate\" Desc) as \"seq\" ";
		sql += "           from \"LoanRateChange\" rb                          ";
		sql += "  		   where \"EffectDate\" <=" + eAdjDate;
		sql += "        ) r2            on  r2.\"CustNo\" = r.\"CustNo\"        ";
		sql += "                       and  r2.\"FacmNo\" = r.\"FacmNo\"        ";
		sql += "                       and  r2.\"BormNo\" = r.\"BormNo\"        ";
		sql += "                       and  r2.\"seq\" = 2                          ";
		sql += " left join \"FacProd\"  p on  p.\"ProdNo\" = r.\"ProdNo\"      ";
		sql += " left join \"LoanBorMain\" b on b.\"CustNo\" = r.\"CustNo\" ";

		sql += " left join \"CustMain\" c on  c.\"CustNo\" = r.\"CustNo\"      ";
		sql += " where b.\"Status\" = 0                                        ";
		sql += "   and c.\"EntCode\" >= " + iEntCode1;
		sql += "   and c.\"EntCode\" <= " + iEntCode2;
		sql += "   and r.\"seq\" = 1 ";
		sql += "   and r2.\"seq\" = 2 ";
		// 1.定期機動調整
		if (iTxKind == 1) {
			sql += "       and b.\"RateCode\" = '3' ";
			sql += "       and r.\"RateCode\" = '3'  ";

		}
		// 2.指數型利率調整
		if (iTxKind == 2) {
			sql += "       and b.\"RateCode\" = '1'  ";
			sql += "       and r.\"RateCode\" = '1'  ";
			sql += "       and r.\"BaseRateCode\" <> 99 ";
		}
		// 3.機動利率調整
		if (iTxKind == 3) {
			sql += "           and b.\"RateCode\" = '1' ";
			sql += "           and r.\"RateCode\" = '1' ";
			sql += "           and r.\"BaseRateCode\" = 99 ";
			sql += "           and p.\"EmpFlag\" <> 'Y' ";
		}
		// 4.員工利率調整
		if (iTxKind == 4) {
			sql += "           and b.\"RateCode\" = '1' ";
			sql += "           and p.\"EmpFlag\" = 'Y' ";
		}
		// 5.按商品別調整
		// 商品代碼
		if (iTxKind == 5) {
			if (prodNos.length() > 0) {
				sql += "   and p.\"ProdNo\" in ( " + prodNos + " ) ";
			}
		}

		sql += " group by r.\"CustNo\" ";

		sql += " order by r.\"CustNo\" ASC";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query);
	}

	/**
	 * 查是否暫收資料
	 * 
	 * @param custNo     戶號
	 * @param sEntryDate 入帳起日(六個月前月初)
	 * @param eEntryDate 入帳止日
	 * @param isTemp     是否為暫收
	 * @param titaVo
	 * @return
	 * @throws Exception
	 * 
	 * 
	 */
	public List<Map<String, String>> isTempQuery(int custNo, int sEntryDate, int eEntryDate, boolean isTemp,
			TitaVo titaVo) throws Exception {

		this.info("L4721ServiceImpl Temp");

		this.info("custNo ... " + custNo);
		this.info("sEntryDate ... " + sEntryDate);
		this.info("eEntryDate ... " + eEntryDate);

//		尋找該戶號是否有暫收款(回傳入帳日，金額，還款類別)

		String sql = "	";

//		sql += " SELECT ";
//		sql += "    lb.\"EntryDate\"  AS \"EntryDate\",";
//		sql += "    cd.\"Item\"       AS \"RepayCodeX\",";
//		sql += "    lb.\"TxAmt\"      AS \"TxAmt\"";
//		sql += "    FROM";
//		sql += "    \"LoanBorTx\"   lb";
//		sql += "    LEFT JOIN \"CdCode\"      cd ON cd.\"DefCode\" = 'RepayCode'";
//		sql += "                             AND cd.\"Code\" = lb.\"RepayCode\"";
//		sql += "    WHERE";
//		sql += "    lb.\"CustNo\" = " + custNo;
//		sql += "    AND lb.\"FacmNo\" = 0";
//		sql += "    AND lb.\"BormNo\" = 0";
//		sql += "    AND lb.\"EntryDate\" >= " + sEntryDate;
//		sql += "    AND lb.\"EntryDate\" <= " + eEntryDate;

		sql += " SELECT ";
		sql += "     a.\"EntryDate\" ";
		sql += "     , CASE ";
		sql += "         WHEN a.\"IntStartDate\" = 99991231  ";
		sql += "         THEN 0 ";
		sql += "         ELSE a.\"IntStartDate\" ";
		sql += "     END AS \"IntStartDate\" ";
		sql += "     , a.\"IntEndDate\" ";
		sql += "     , nvl(c.\"Item\", ' ') AS \"RepayCodeX\" ";
		sql += "     , a.\"TxAmt\" ";
		sql += "     , a.\"Principal\" ";
		sql += "     , a.\"Interest\" ";
		sql += "     , a.\"BreachAmt\" ";
		sql += "     , a.\"FeeAmt\" ";
		sql += "     , a.\"RepayCode\" ";
		sql += " FROM ";
		sql += "     ( ";
		sql += "         SELECT ";
		sql += "             \"EntryDate\" ";
		sql += "             , MIN( ";
		sql += "                 CASE ";
		sql += "                     WHEN \"IntStartDate\" = 0  ";
		sql += "                     THEN 99991231 ";
		sql += "                     ELSE \"IntStartDate\" ";
		sql += "                 END ";
		sql += "             ) AS \"IntStartDate\" ";
		sql += "             , MAX(\"IntEndDate\") AS \"IntEndDate\" ";
		sql += "             , SUM(\"TxAmt\") AS \"TxAmt\" ";
		sql += "             , SUM(\"Principal\") AS \"Principal\" ";
		sql += "             , SUM(\"Interest\") AS \"Interest\" ";
		sql += "             , SUM(\"DelayInt\" + \"BreachAmt\" + \"CloseBreachAmt\") AS \"BreachAmt\" ";
		sql += "             , SUM(\"FeeAmt\") AS \"FeeAmt\" ";
		sql += "             , \"RepayCode\" ";
		sql += "         FROM \"LoanBorTx\" ";
		sql += "         WHERE \"TitaHCode\" = '0' ";
		sql += "   		   AND \"EntryDate\" >= " + sEntryDate;
		sql += "    	   AND \"EntryDate\" <= " + eEntryDate;
		sql += "    	   AND \"CustNo\" = " + custNo;
		if (isTemp) {
			sql += "    AND \"FacmNo\" = 0";
			sql += "    AND \"BormNo\" = 0";
		}
		sql += "         GROUP BY ";
		sql += "             \"EntryDate\" ";
		sql += "             , \"RepayCode\" ";
		sql += "         ORDER BY ";
		sql += "             \"EntryDate\" ";
		sql += "     ) a ";
		sql += "     LEFT JOIN \"CdCode\" c ON c.\"DefCode\" = 'RepayCode' ";
		sql += "                         AND lpad(c.\"Code\",2, '0') = a.\"RepayCode\" ";
		sql += "                         AND a.\"RepayCode\" IN ( 1 , 2 , 3 , 4 ) ";
		sql += " ORDER BY ";
		sql += "     a.\"EntryDate\" ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

	/**
	 * @param custNo   戶號
	 * @param sAdjDate 利率調整起日
	 * @param eAdjDate 利率調整止日
	 * @param titaVo
	 * @return
	 * @throws Exception
	 * 
	 * 
	 */
	public List<Map<String, String>> doQuery(int custNo, int sAdjDate, int eAdjDate, TitaVo titaVo) throws Exception {

		this.info("L4721ServiceImpl doQuery");

		this.info("custNo ... " + custNo);

//		因抓取不到費用類，第一層group by 到額度(同額度同調整日、同源利率

		String sql = " ";
		sql += "  SELECT DISTINCT ";
		sql += "        B.\"CustNo\"                                          "; // 戶號
		sql += "       ,B.\"FacmNo\"                                          "; // 額度
		sql += "       ,C.\"CustName\"                                        "; // 戶名
		sql += "       ,B.\"SpecificDd\"                                      "; // 應繳日
		sql += "       ,CD.\"Item\"                      AS \"RepayCodeX\"    "; // 繳款方式
		sql += "       ,B.\"LoanBal\"                                         "; // 貸放餘額
		sql += "       ,B.\"NextPayIntDate\"                                  "; // 下繳日
		sql += "       ,NVL(r.\"EffectDate\",0) AS  \"TxEffectDate\" ";// 利率生效日
		sql += "       ,NVL(r2.\"FitRate\",0) AS \"PresentRate\" ";
		sql += "       ,NVL(r.\"FitRate\",0) AS \"AdjustedRate\" ";
		sql += "       ,CASE WHEN NVL(r2.\"EffectDate\",0) > 0 THEN 'Y' ELSE 'N' END AS \"Flag\"  "; // 放款利率變動檔生效日，利率未變動為零
																										// // Y,N
		sql += "  FROM (SELECT \"CustNo\"                                             ";
		sql += "                   ,\"FacmNo\"                                             ";
		sql += "                   ,SUM(\"LoanBal\")             AS \"LoanBal\"            ";
		sql += "                   ,MIN(\"NextPayIntDate\")      AS \"NextPayIntDate\"     ";
		sql += "                   ,MAX(\"SpecificDd\")          AS \"SpecificDd\"         ";
		sql += "             FROM \"LoanBorMain\"                                          ";
		sql += "             WHERE \"CustNo\" = " + custNo;
		sql += "               AND \"Status\" = 0                                           ";
		sql += "             GROUP By \"CustNo\", \"FacmNo\"                                ";
		sql += "             ) B                                                            ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\"   = B.\"CustNo\"                            ";
		sql += " LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\"  = B.\"CustNo\"                            ";
		sql += "                         AND FM.\"FacmNo\"  = B.\"FacmNo\"                            ";
		sql += " LEFT JOIN \"ClFac\" F ON F.\"CustNo\"   =  B.\"CustNo\"                              ";
		sql += "                      AND F.\"FacmNo\"   =  B.\"FacmNo\"                              ";
		sql += "                      AND F.\"MainFlag\" = 'Y'                                        ";
		sql += " LEFT JOIN \"ClBuilding\" CB ON CB.\"ClCode1\" =  F.\"ClCode1\"                       ";
		sql += "                            AND CB.\"ClCode2\" =  F.\"ClCode2\"                       ";
		sql += "                            AND CB.\"ClNo\"    =  F.\"ClNo\"                          ";
		sql += " LEFT JOIN \"CdCode\" CD ON CD.\"DefCode\" = 'RepayCode'                              ";
		sql += "                        AND CD.\"Code\"    =  FM.\"RepayCode\"                        ";
		sql += " LEFT JOIN (                                             ";
		sql += "           select                                       ";
		sql += "            \"CustNo\"                              ";
		sql += "           ,\"FacmNo\"                              ";
		sql += "           ,\"BormNo\"                              ";
		sql += "           ,\"FitRate\"                              ";
		sql += "           ,\"EffectDate\"                           ";
		sql += "           ,row_number() over (partition by \"CustNo\", \"FacmNo\", \"BormNo\" order by \"EffectDate\" Desc) as \"seq\" ";
		sql += "           from \"LoanRateChange\"                           ";
		sql += "  		   where \"EffectDate\" >=" + sAdjDate;
		sql += "   			 and \"EffectDate\" <=" + eAdjDate;
		sql += "        ) r            on  r.\"CustNo\" = B.\"CustNo\"        ";
		sql += "                       and r.\"FacmNo\" = B.\"FacmNo\"        ";
		sql += "                       and r.\"seq\" = 1                          ";
		sql += " LEFT JOIN (                                             ";
		sql += "           select                                       ";
		sql += "            \"CustNo\"                              ";
		sql += "           ,\"FacmNo\"                              ";
		sql += "           ,\"BormNo\"                              ";
		sql += "           ,\"FitRate\"                              ";
		sql += "           ,\"EffectDate\"                           ";
		sql += "           ,row_number() over (partition by \"CustNo\", \"FacmNo\", \"BormNo\" order by \"EffectDate\" Desc) as \"seq\" ";
		sql += "           from \"LoanRateChange\" rb                          ";
		sql += "  		   where \"EffectDate\" <=" + eAdjDate;
		sql += "        ) r2            on  r2.\"CustNo\" = r.\"CustNo\"        ";
		sql += "                       and  r2.\"FacmNo\" = r.\"FacmNo\"        ";
		sql += "                       and  r2.\"BormNo\" = r.\"BormNo\"        ";
		sql += "                       and  r2.\"seq\" = 2                          ";
		sql += " ORDER BY B.\"CustNo\",B.\"FacmNo\"                                                   ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

	/**
	 * @param custNo     戶號
	 * @param sAdjDate   利率調整起日
	 * @param eAdjDate   利率調整止日
	 * @param sEntryDate 入帳起日(六個月前月初)
	 * @param eEntryDate 入帳止日
	 * @param titaVo
	 * @return
	 * @throws Exception
	 * 
	 * 
	 */
	public List<Map<String, String>> doDetail(int custNo, int sAdjDate, int eAdjDate, int sEntryDate, int eEntryDate,
			TitaVo titaVo) throws Exception {
		dateUtil.init();

		this.info("L4721ServiceImpl doDetail");

		this.info("custNo ... " + custNo);

//		因抓取不到費用類，第一層group by 到額度(同額度同調整日、同源利率

		String sql = " ";
		sql += " SELECT DISTINCT X.\"CustNo\"                                AS \"CustNo\"   ";
		sql += "       ,X.\"FacmNo\"                                         AS \"FacmNo\"   ";
		sql += "       ,C.\"CustName\"                                        "; // 戶名
		sql += "       ,LB.\"SpecificDd\"                                      "; // 應繳日
		sql += "       ,LB.\"LoanBal\"                                         "; // 貸放餘額
		sql += "       ,LB.\"DueAmt\"                                         "; // 貸放餘額
		sql += "       ,X.\"EntryDate\"                                       ";
		sql += "       ,X.\"IntStartDate\"                                    "; // F1:計息期間
		sql += "       ,X.\"IntEndDate\"                                      "; // F2:計息期間
		sql += "       ,CD.\"Item\"                                          AS \"RepayCodeX\" "; // F13:繳款方式
		sql += "       ,X.\"TxAmt\"                                           "; // F4:繳款金額
		sql += "       ,X.\"Principal\"                                       "; // F5:攤還本金
		sql += "       ,X.\"Interest\"                                        "; // F6:繳息金額
		sql += "       ,X.\"BreachAmt\" + \"DelayInt\"                       AS \"BreachAmt\"  "; // F7:違約金+延滯息
		sql += "       ,X.\"FEE1\" + X.\"FEE2\" + X.\"FEE3\" + X.\"FEE4\"    AS \"OtherFee\"   "; // F8:火險費或其他費用
		sql += "       ,NVL(r.\"EffectDate\",0) AS  \"TxEffectDate\" ";// 利率生效日
		sql += "       ,NVL(r2.\"FitRate\",0) AS \"PresentRate\" ";
		sql += "       ,NVL(r.\"FitRate\",0) AS \"AdjustedRate\" ";
		sql += "       ,X.\"AcDate\"                      ";
		sql += "       ,X.\"RepayCode\"                   ";
		sql += "       ,NVL(CB.\"BdLocation\", ' ')      AS \"Location\"      "; // 押品地址
		sql += " FROM ( SELECT MAX(T.\"CustNo\")                                            AS \"CustNo\"            ";
		sql += "             ,T.\"FacmNo\"                                                  AS \"FacmNo\"            ";
		sql += "             ,MAX(T.\"EntryDate\")                                          AS \"EntryDate\"         ";
		sql += "             ,MIN(T.\"IntStartDate\")                                       AS \"IntStartDate\"      ";
		sql += "             ,MAX(T.\"IntEndDate\")                                         AS \"IntEndDate\"        ";
		sql += "             ,MAX(T.\"Rate\")                                               AS \"Rate\"              ";
		sql += "             ,SUM(T.\"Interest\")                                           AS \"Interest\"          ";
		sql += "             ,SUM(T.\"DelayInt\")                                           AS \"DelayInt\"          ";
		sql += "             ,SUM(T.\"BreachAmt\" + T.\"CloseBreachAmt\")                   AS \"BreachAmt\"         ";
		sql += "             ,SUM(T.\"Principal\")                                          AS \"Principal\"         ";
		sql += "             ,MAX(T.\"RepayCode\")                                          AS \"RepayCode\"         ";
		sql += "             ,SUM(T.\"TxAmt\")                                              AS \"TxAmt\"             ";
		sql += "             ,SUM(NVL(JSON_VALUE(T.\"OtherFields\",  '$.AcctFee'),0))       AS FEE1                  ";
		sql += "             ,SUM(NVL(JSON_VALUE(T.\"OtherFields\",  '$.ModifyFee'),0))     AS FEE2                  ";
		sql += "             ,SUM(NVL(JSON_VALUE(T.\"OtherFields\",  '$.FireFee'),0))       AS FEE3                  ";
		sql += "             ,SUM(NVL(JSON_VALUE(T.\"OtherFields\",  '$.LawFee'),0))        AS FEE4                  ";
		sql += "             ,T.\"AcDate\"                      ";
		sql += "             , CASE";
		sql += "                WHEN t.\"IntStartDate\" = 0";
		sql += "                  AND t.\"IntEndDate\" = 0 THEN";
		sql += "                    'Y'";
		sql += "                ELSE";
		sql += "                    'N' END AS \"Flag\"";
		sql += "        FROM \"LoanBorTx\" T                                             ";
		sql += "        WHERE T.\"CustNo\" = " + custNo;
		sql += "         AND  T.\"FacmNo\" != 0 ";
		sql += "         AND  T.\"TitaHCode\" = 0                                          ";
		sql += "         AND (T.\"Principal\" + T.\"ExtraRepay\" + T.\"Interest\" + T.\"BreachAmt\" + T.\"CloseBreachAmt\"";
		sql += "              +  NVL(JSON_VALUE(T.\"OtherFields\",  '$.AcctFee'),0)                   ";
		sql += "              +  NVL(JSON_VALUE(T.\"OtherFields\",  '$.ModifyFee'),0)                 ";
		sql += "              +	NVL(JSON_VALUE(T.\"OtherFields\",  '$.FireFee'),0)                    ";
		sql += "              +	NVL(JSON_VALUE(T.\"OtherFields\",  '$.LawFee'),0) > 0                 ";
		sql += "                or T.\"TitaTxCd\" = 'L3210' )                                         ";
		sql += "         AND  T.\"EntryDate\" >= " + sEntryDate; // tbsdy六個月前的月初日
		sql += "         AND  T.\"EntryDate\" <= " + eEntryDate; // tbsdy
		sql += "       GROUP BY  t.\"FacmNo\", t.\"EntryDate\", t.\"AcDate\", CASE WHEN t.\"IntStartDate\" = 0 AND t.\"IntEndDate\" = 0 THEN 'Y' ELSE 'N' END                      ";
		sql += "      ) X                                                                             ";
		sql += " LEFT JOIN \"CdCode\" CD ON CD.\"DefCode\" = 'RepayCode'                              ";
		sql += "                        AND CD.\"Code\"    =  X.\"RepayCode\"                         ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\"   = X.\"CustNo\"                            ";
		sql += " LEFT JOIN ( SELECT  \"CustNo\" AS \"CustNo\"";
		sql += "                    ,\"FacmNo\"";
		sql += "                    ,SUM(\"LoanBal\") AS \"LoanBal\"";
		sql += "                    ,SUM(\"DueAmt\") AS \"DueAmt\"";
		sql += "                    ,MIN(\"NextPayIntDate\") AS \"NextPayIntDate\"";
		sql += "                    ,MAX(\"SpecificDd\") AS \"SpecificDd\"";
		sql += "        FROM  \"LoanBorMain\"";
		sql += "        WHERE  \"CustNo\" = " + custNo;
		sql += "               AND \"Status\" != 3                                           ";
		sql += "        GROUP BY   \"CustNo\",\"FacmNo\") LB ON LB.\"CustNo\" = X.\"CustNo\"                         ";
		sql += "                                            AND LB.\"FacmNo\" =  X.\"FacmNo\"                        ";
		sql += " LEFT JOIN (                                             ";
		sql += "           select                                       ";
		sql += "            \"CustNo\"                              ";
		sql += "           ,\"FacmNo\"                              ";
		sql += "           ,\"BormNo\"                              ";
		sql += "           ,\"FitRate\"                              ";
		sql += "           ,\"EffectDate\"                           ";
		sql += "           ,row_number() over (partition by \"CustNo\", \"FacmNo\", \"BormNo\" order by \"EffectDate\" Desc) as \"seq\" ";
		sql += "           from \"LoanRateChange\"                           ";
		sql += "  		   where \"EffectDate\" >=" + sAdjDate;
		sql += "   			 and \"EffectDate\" <=" + eAdjDate;
		sql += "        ) r            on  r.\"CustNo\" = X.\"CustNo\"        ";
		sql += "                       and r.\"FacmNo\" = X.\"FacmNo\"        ";
		sql += "                       and r.\"seq\" = 1                          ";
		sql += " LEFT JOIN (                                             ";
		sql += "           select                                       ";
		sql += "            \"CustNo\"                              ";
		sql += "           ,\"FacmNo\"                              ";
		sql += "           ,\"BormNo\"                              ";
		sql += "           ,\"FitRate\"                              ";
		sql += "           ,\"EffectDate\"                           ";
		sql += "           ,row_number() over (partition by \"CustNo\", \"FacmNo\", \"BormNo\" order by \"EffectDate\" Desc) as \"seq\" ";
		sql += "           from \"LoanRateChange\" rb                          ";
		sql += "  		   where \"EffectDate\" <=" + eAdjDate;
		sql += "        ) r2            on  r2.\"CustNo\" = r.\"CustNo\"        ";
		sql += "                       and  r2.\"FacmNo\" = r.\"FacmNo\"        ";
		sql += "                       and  r2.\"BormNo\" = r.\"BormNo\"        ";
		sql += "                       and  r2.\"seq\" = 2                          ";
		sql += " LEFT JOIN \"ClFac\" F ON F.\"CustNo\"   =  LB.\"CustNo\"                              ";
		sql += "                      AND F.\"FacmNo\"   =  LB.\"FacmNo\"                              ";
		sql += "                      AND F.\"MainFlag\" = 'Y'                                        ";
		sql += " LEFT JOIN \"ClBuilding\" CB ON CB.\"ClCode1\" =  F.\"ClCode1\"                       ";
		sql += "                            AND CB.\"ClCode2\" =  F.\"ClCode2\"                       ";
		sql += "                            AND CB.\"ClNo\"    =  F.\"ClNo\"                          ";
		sql += " WHERE LB.\"SpecificDd\" IS NOT NULL";
		sql += " ORDER BY X.\"FacmNo\",X.\"EntryDate\"                                                             ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

	/**
	 * 
	 * 紙本資料(明細)
	 * 
	 * @param custNo           戶號
	 * @param sAdjDate         利率調整起日
	 * @param eAdjDate         利率調整止日
	 * @param sEntryDate       入帳起日(六個月前月初)
	 * @param eEntryDate       入帳止日
	 * @param isDoDetail       是否為明細(否則為額度變動日)
	 * @param isSameSpecificDd 應繳日額度是否一樣
	 * @param titaVo
	 * @return
	 * @throws Exception
	 * 
	 * 
	 */
	public List<Map<String, String>> doDetailTxffect(int custNo, int sAdjDate, int eAdjDate, int sEntryDate,
			int eEntryDate, boolean isDoDetail, boolean isSameSpecificDd, TitaVo titaVo) throws Exception {
		dateUtil.init();

		this.info("L4721ServiceImpl doDetailTxffect");

		this.info("custNo ... " + custNo);

		String sql = " ";

		sql += "   with \"MainByCustNo\" as (";
		sql += "      SELECT MAX(T.\"CustNo\")                                 AS \"CustNo\"";
		sql += "           , 0 AS \"FacmNo\"";
		sql += "            , MAX(T.\"EntryDate\")                              AS \"EntryDate\"";
		sql += "            , MIN( case when T.\"IntStartDate\" = 0 then  99991231";
		sql += "            else  T.\"IntStartDate\"  end ) AS \"IntStartDate\"";
		sql += "                     , MIN( case when T.\"IntEndDate\" = 0 then  99991231";
		sql += "            else  T.\"IntEndDate\"  end ) AS \"IntEndDate\"";
		sql += "            , MAX(T.\"Rate\")                                   AS \"Rate\"";
		sql += "            , SUM(T.\"Interest\")                               AS \"Interest\"";
		sql += "            , SUM(T.\"DelayInt\")                               AS \"DelayInt\"";
		sql += "            , SUM(T.\"BreachAmt\" + T.\"CloseBreachAmt\")         AS \"BreachAmt\"";
		sql += "            , SUM(T.\"Principal\")                              AS \"Principal\"";
		sql += "            , MIN(T.\"RepayCode\")                              AS \"RepayCode\"";
		sql += "            , SUM(T.\"TxAmt\")                                  AS \"TxAmt\"";
		sql += "            , SUM(Nvl(";
		sql += "           JSON_VALUE(T.\"OtherFields\", '$.AcctFee'), 0";
		sql += "       )) AS Fee1";
		sql += "            , SUM(Nvl(";
		sql += "           JSON_VALUE(T.\"OtherFields\", '$.ModifyFee'), 0";
		sql += "       )) AS Fee2";
		sql += "            , SUM(Nvl(";
		sql += "           JSON_VALUE(T.\"OtherFields\", '$.FireFee'), 0";
		sql += "       )) AS Fee3";
		sql += "            , SUM(Nvl(";
		sql += "           JSON_VALUE(T.\"OtherFields\", '$.LawFee'), 0";
		sql += "       )) AS Fee4";
		sql += "       FROM \"LoanBorTx\" T";
		sql += "       WHERE T.\"CustNo\" = " + custNo;
		sql += "             AND";
		sql += "             T.\"TitaHCode\" = 0";
		sql += "             AND";
		sql += "             NOT T.\"TxDescCode\" IN ('3100','3420','3421','3422') ";//--撥款、結案登錄
		sql += "             AND";
		sql += "             T.\"EntryDate\" >= " + sEntryDate;
		sql += "             AND";
		sql += "             T.\"EntryDate\" <= " + eEntryDate;
		sql += "       GROUP BY  ";
		sql += "      	 T.\"EntryDate\"";
		sql += "   ), \"MainByFacmNo\" as (";
		sql += "      SELECT MAX(T.\"CustNo\")                                 AS \"CustNo\"";
		sql += "            , T.\"FacmNo\" AS \"FacmNo\"";
		sql += "            , MAX(T.\"EntryDate\")                              AS \"EntryDate\"";
		sql += "            , MIN( case when T.\"IntStartDate\" = 0 then  99991231";
		sql += "            else  T.\"IntStartDate\"  end ) AS \"IntStartDate\"";
		sql += "                     , MIN( case when T.\"IntEndDate\" = 0 then  99991231";
		sql += "            else  T.\"IntEndDate\"  end ) AS \"IntEndDate\"";
		sql += "            , MAX(T.\"Rate\")                                   AS \"Rate\"";
		sql += "            , SUM(T.\"Interest\")                               AS \"Interest\"";
		sql += "            , SUM(T.\"DelayInt\")                               AS \"DelayInt\"";
		sql += "            , SUM(T.\"BreachAmt\" + T.\"CloseBreachAmt\")         AS \"BreachAmt\"";
		sql += "            , SUM(T.\"Principal\")                              AS \"Principal\"";
		sql += "            , MIN(T.\"RepayCode\")                              AS \"RepayCode\"";
		sql += "            , SUM(T.\"TxAmt\")                                  AS \"TxAmt\"";
		sql += "            , SUM(Nvl(";
		sql += "           JSON_VALUE(T.\"OtherFields\", '$.AcctFee'), 0";
		sql += "       )) AS Fee1";
		sql += "            , SUM(Nvl(";
		sql += "           JSON_VALUE(T.\"OtherFields\", '$.ModifyFee'), 0";
		sql += "       )) AS Fee2";
		sql += "            , SUM(Nvl(";
		sql += "           JSON_VALUE(T.\"OtherFields\", '$.FireFee'), 0";
		sql += "       )) AS Fee3";
		sql += "            , SUM(Nvl(";
		sql += "           JSON_VALUE(T.\"OtherFields\", '$.LawFee'), 0";
		sql += "       )) AS Fee4";
		sql += "       FROM \"LoanBorTx\" T";
		sql += "       WHERE T.\"CustNo\" = " + custNo;
		sql += "             AND";
		sql += "             T.\"TitaHCode\" = 0";
		sql += "             AND";
		sql += "             NOT T.\"TxDescCode\" IN ('3100','3420','3421','3422') ";//--撥款、結案登錄
		sql += "             AND";
		sql += "             T.\"EntryDate\" >= " + sEntryDate;
		sql += "             AND";
		sql += "             T.\"EntryDate\" <= " + eEntryDate;
		sql += "       GROUP BY  ";
		sql += "           T.\"FacmNo\" ,";
		sql += "      	 T.\"EntryDate\"";
		sql += "   ),\"tmpMain\" as (";
		sql += "   select ";
		sql += "    distinct \"CustNo\",\"FacmNo\"";
		sql += "   from \"MainByFacmNo\"";
		sql += "   )";
		sql += "   ";

		// 明細
		if (isDoDetail) {
			sql += "   SELECT * FROM ( ";
			sql += "   SELECT DISTINCT X.\"CustNo\"                                         AS \"CustNo\"";
			sql += "                 , X.\"FacmNo\"                                         AS \"FacmNo\"";
			sql += "                 , C.\"CustName\"";
			sql += "                 , Lb.\"SpecificDd\"";
			sql += "                 , Lb.\"LoanBal\"";
			sql += "                 , Lb.\"DueAmt\"";
			sql += "                 , X.\"EntryDate\"";
			sql += "                 , X.\"IntStartDate\"";
			sql += "                 , X.\"IntEndDate\"";
			sql += "                 , Cd.\"Item\"                                          AS \"RepayCodeX\"";
			sql += "                 , X.\"TxAmt\"";
			sql += "                 , X.\"Principal\"";
			sql += "                 , X.\"Interest\"";
			sql += "                 , X.\"BreachAmt\" + \"DelayInt\"                         AS \"BreachAmt\"";
			sql += "                 , X.\"FEE1\" + X.\"FEE2\" + X.\"FEE3\" + X.\"FEE4\"          AS \"OtherFee\"";
			sql += "                 ,ROW_NUMBER()OVER(PARTITION BY X.\"CustNo\",X.\"FacmNo\" ORDER BY X.\"EntryDate\" DESC)   AS \"Seq\"";
			if (isSameSpecificDd) {
				sql += "   FROM  \"MainByCustNo\" X";
			} else {
				sql += "   FROM  \"MainByFacmNo\" X";
			}
			sql += "   LEFT JOIN \"CdCode\"      Cd ON Cd.\"DefCode\" = 'RepayCode'";
			sql += "                            AND";
			sql += "                            Cd.\"Code\" = X.\"RepayCode\"";
			sql += "   LEFT JOIN \"CustMain\"    C ON C.\"CustNo\" = X.\"CustNo\"";
			sql += "   LEFT JOIN (";
			sql += "       SELECT \"CustNo\"                  AS \"CustNo\"";
			if (isSameSpecificDd) {
				sql += "           , 0 AS \"FacmNo\"";
			} else {
				sql += "           , \"FacmNo\" AS \"FacmNo\"";
			}
			sql += "            , SUM(\"LoanBal\")            AS \"LoanBal\"";
			sql += "            , SUM(\"DueAmt\")             AS \"DueAmt\"";
			sql += "            , MIN(\"NextPayIntDate\")     AS \"NextPayIntDate\"";
			sql += "            , MAX(\"SpecificDd\")         AS \"SpecificDd\"";
			sql += "       FROM \"LoanBorMain\"";
			sql += "       WHERE \"CustNo\" = " + custNo;
			sql += "             AND \"Status\" != 3";
			sql += "       GROUP BY \"CustNo\"";
			if (!isSameSpecificDd) {
				sql += "           , \"FacmNo\"";
			}
			sql += "   ";
			sql += "   ) Lb ON Lb.\"CustNo\" = X.\"CustNo\"";
			sql += "       AND Lb.\"FacmNo\" = X.\"FacmNo\"";
			sql += "   ";
			sql += "   WHERE Lb.\"SpecificDd\" IS NOT NULL";
			sql += "     AND X.\"TxAmt\" >= 0";
			sql += "   ) WHERE \"Seq\" <= 8 ";
			sql += "   ORDER BY  \"FacmNo\" , \"EntryDate\" ASC";

			// 利率變動日及地址
		} else {

			sql += "   SELECT DISTINCT X.\"CustNo\"    AS \"CustNo\"";
			sql += "        , X.\"FacmNo\"    AS \"FacmNo\"";
			sql += "        , Nvl(R.\"EffectDate\", 0 ) AS \"TxEffectDate\"";
			sql += "        , Nvl(R2.\"FitRate\", 0) AS \"PresentRate\"";
			sql += "        , Nvl(R.\"FitRate\", 0) AS \"AdjustedRate\"";
			sql += "        , Nvl(Cb.\"BdLocation\", ' ') AS \"Location\"";
			sql += "        , Nvl(CM.\"RegZip3\", ' ') || Nvl(CM.\"RegZip2\", ' ') AS \"Zip\"";
			sql += "   FROM \"tmpMain\"     X";
			sql += "   LEFT JOIN (";
			sql += "       SELECT \"CustNo\"";
			sql += "            , \"FacmNo\"";
			sql += "            , \"BormNo\"";
			sql += "            , \"FitRate\"";
			sql += "            , \"EffectDate\"";
			sql += "            , ROW_NUMBER() OVER(PARTITION BY \"CustNo\", \"FacmNo\"";
			sql += "              , \"BormNo\"";
			sql += "           ORDER BY \"EffectDate\" DESC";
			sql += "       ) AS \"seq\"";
			sql += "       FROM \"LoanRateChange\"";
			sql += "       WHERE  \"EffectDate\" >=" + sAdjDate;
			sql += "   		 AND \"EffectDate\" <=" + eAdjDate;
			sql += "   ";
			sql += "   ) R ON R.\"CustNo\" = X.\"CustNo\"";
			sql += "          AND";
			sql += "          R.\"FacmNo\" = X.\"FacmNo\"";
			sql += "          AND";
			sql += "          R.\"seq\" = 1";
			sql += "   LEFT JOIN (";
			sql += "       SELECT \"CustNo\"";
			sql += "            , \"FacmNo\"";
			sql += "            , \"BormNo\"";
			sql += "            , \"FitRate\"";
			sql += "            , \"EffectDate\"";
			sql += "            , ROW_NUMBER() OVER(PARTITION BY \"CustNo\", \"FacmNo\"";
			sql += "              , \"BormNo\"";
			sql += "           ORDER BY \"EffectDate\" DESC";
			sql += "       ) AS \"seq\"";
			sql += "       FROM \"LoanRateChange\" Rb";
			sql += "       WHERE \"EffectDate\" <=" + eAdjDate;
			sql += "   ) R2 ON R2.\"CustNo\" = R.\"CustNo\"";
			sql += "           AND";
			sql += "           R2.\"FacmNo\" = R.\"FacmNo\"";
			sql += "           AND";
			sql += "           R2.\"BormNo\" = R.\"BormNo\"";
			sql += "           AND";
			sql += "           R2.\"seq\" = 2";
			sql += "   LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = X.\"CustNo\"";
			sql += "   LEFT JOIN \"ClFac\"   F ON F.\"CustNo\" = X.\"CustNo\"";
			sql += "                          AND F.\"FacmNo\" = X.\"FacmNo\"";
			sql += "                          AND F.\"MainFlag\" = 'Y'";
			sql += "   LEFT JOIN \"ClBuilding\"  Cb ON Cb.\"ClCode1\" = F.\"ClCode1\"";
			sql += "                                AND Cb.\"ClCode2\" = F.\"ClCode2\"";
			sql += "                                AND Cb.\"ClNo\" = F.\"ClNo\"";
			sql += "   WHERE R.\"EffectDate\" > 0 ";
			sql += "   ORDER BY X.\"FacmNo\" ";

		}

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

}