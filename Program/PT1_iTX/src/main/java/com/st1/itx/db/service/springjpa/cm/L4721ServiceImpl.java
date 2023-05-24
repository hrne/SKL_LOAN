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

	public List<Map<String, String>> findAll(int itxkind, int iCustType, int sDate, int eDate, String prodNos,
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

		sql += " select   ";
		sql += "    r.\"CustNo\"                                 as \"CustNo\"  "; // 戶號
		sql += "   ,r.\"FacmNo\"                                 as \"FacmNo\"  "; // 額度
		sql += "   ,r.\"BormNo\"                                 as \"BormNo\"  "; // 撥款
		sql += "   ,r.\"EffectDate\"                             as \"TxEffectDate\"  "; // 利率生效日
		sql += "   ,r.\"FitRate\"			                     as \"AdjustedRate\" "; // 適用利率
		sql += "   ,r2.\"FitRate\"                 				 as \"PresentRate\" ";// 前次利率
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
		sql += "  		   where \"EffectDate\" >=" + sDate;
		sql += "   			 and \"EffectDate\" <=" + eDate;
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
		sql += "  		   where \"EffectDate\" <=" + eDate;
		sql += "        ) r2            on  r2.\"CustNo\" = r.\"CustNo\"        ";
		sql += "                       and  r2.\"FacmNo\" = r.\"FacmNo\"        ";
		sql += "                       and  r2.\"BormNo\" = r.\"BormNo\"        ";
		sql += "                       and  r2.\"seq\" = 2                          ";
		sql += " left join \"FacProd\"  p on  p.\"ProdNo\" = r.\"ProdNo\"      ";
		sql += " left join \"LoanBorMain\" b on b.\"CustNo\" = r.\"CustNo\" ";
		sql += "       					    and b.\"FacmNo\" = r.\"FacmNo\" ";
		sql += "                            and b.\"BormNo\" = r.\"BormNo\" ";
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

		sql += " order by r.\"CustNo\" ";
		sql += " 		 ,r.\"FacmNo\" ";
		sql += " 		 ,r.\"BormNo\" ";
		sql += " 		 ,r.\"EffectDate\" DESC";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query);
	}

	public List<Map<String, String>> TempQuery(int custNo, int isday, int ieday, TitaVo titaVo) throws Exception {

		this.info("L4721ServiceImpl Temp");

		this.info("custNo ... " + custNo);
		this.info("isday ... " + isday);
		this.info("ieday ... " + ieday);

//		尋找該戶號是否有暫收款(回傳入帳日，金額，還款類別)

		String sql = "SELECT    ";
		sql += "    lb.\"EntryDate\"  AS \"EntryDate\",";
		sql += "    cd.\"Item\"       AS \"RepayCodeX\",";
		sql += "    lb.\"TxAmt\"      AS \"TxAmt\"";
		sql += "    FROM";
		sql += "    \"LoanBorTx\"   lb";
		sql += "    LEFT JOIN \"CdCode\"      cd ON cd.\"DefCode\" = 'RepayCode'";
		sql += "                             AND cd.\"Code\" = lb.\"RepayCode\"";
		sql += "    WHERE";
		sql += "    lb.\"CustNo\" = " + custNo;
		sql += "    AND lb.\"FacmNo\" = 0";
		sql += "    AND lb.\"BormNo\" = 0";
		sql += "    AND lb.\"EntryDate\" >= " + isday;
		sql += "    AND lb.\"EntryDate\" <= " + ieday;

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> doQuery(int custNo, int sDate, int eDate, TitaVo titaVo) throws Exception {

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
		sql += "  		   where \"EffectDate\" >=" + sDate;
		sql += "   			 and \"EffectDate\" <=" + eDate;
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
		sql += "  		   where \"EffectDate\" <=" + eDate;
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

	public List<Map<String, String>> doDetail(int custNo, int sDate, int eDate, TitaVo titaVo) throws Exception {
		dateUtil.init();

		this.info("L4721ServiceImpl doDetail");

		this.info("custNo ... " + custNo);
		this.info("isday ... " + sDate);
		this.info("ieday ... " + eDate);
//		因抓取不到費用類，第一層group by 到額度(同額度同調整日、同源利率

		String sql = " ";
		sql += " SELECT DISTINCT X.\"CustNo\"                                         AS \"CustNo\"   ";
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
		sql += "         AND  T.\"EntryDate\" >= " + sDate; // tbsdy六個月前的月初日
		sql += "         AND  T.\"EntryDate\" <= " + eDate; // tbsdy
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
		sql += "  		   where \"EffectDate\" >=" + sDate;
		sql += "   			 and \"EffectDate\" <=" + eDate;
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
		sql += "  		   where \"EffectDate\" <=" + eDate;
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

}