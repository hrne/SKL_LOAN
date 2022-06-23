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

	public List<Map<String, String>> TempQuery(int custNo, TitaVo titaVo) throws Exception {
		dateUtil.init();

		this.info("BankStatementServiceImpl Temp");
		int ieday = titaVo.getEntDyI() + 19110000;
		dateUtil.setDate_1(ieday);
		dateUtil.setMons(-6);
		int isday = Integer.parseInt(String.valueOf(dateUtil.getCalenderDay()).substring(0, 6) + "01");
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

	public List<Map<String, String>> doQuery(int custNo, TitaVo titaVo) throws Exception {
		dateUtil.init();

		this.info("BankStatementServiceImpl doQuery");
		int ieday = titaVo.getEntDyI() + 19110000;
		dateUtil.setDate_1(ieday);
		dateUtil.setMons(-6);
		int isday = Integer.parseInt(String.valueOf(dateUtil.getCalenderDay()).substring(0, 6) + "01");
		this.info("custNo ... " + custNo);
		this.info("isday ... " + isday);
		this.info("ieday ... " + ieday);

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
		sql += "       ,NVL(CB.\"BdLocation\", ' ')      AS \"Location\"      "; // 押品地址
		sql += "       ,NVL(CN.\"PaperNotice\", 'Y')     AS \"PaperNotice\"   "; // 書面通知與否 Y:寄送 N:不寄送
		sql += "       ,CASE WHEN BR.\"TxEffectDate\" = 0 THEN 0 WHEN BR.\"TxEffectDate\" IS NOT NULL THEN BR.\"TxEffectDate\" - 19110000 ELSE 0 END AS \"TxEffectDate\""; // 利率生效日
//		sql += "       ,CASE WHEN BR.\"TxEffectDate\" IS NOT NULL THEN BR.\"TxEffectDate\" - 19110000 ELSE 0 END AS \"TxEffectDate\""; // 利率生效日
		sql += "       ,BR.\"PresentRate\"                ";
		sql += "       ,BR.\"AdjustedRate\"               ";
		sql += "       ,CASE WHEN B.\"FacmNo\" = BR.\"FacmNo\" THEN 'Y' ELSE 'N' END AS \"Flag\"  "; // 放款利率變動檔生效日，利率未變動為零
																										// Y,N
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
		sql += " left join \"CustNotice\" CN                             "; // 客戶通知設定檔
		sql += "        on CN.\"FormNo\" = 'L4721'                       ";
		sql += "       and CN.\"CustNo\" = B.\"CustNo\"                  ";
		sql += "       and CN.\"FacmNo\" = B.\"FacmNo\"                  ";
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
		sql += " LEFT JOIN \"BatxRateChange\" BR ON BR.\"CustNo\" = B.\"CustNo\"                      ";
		sql += "                                AND BR.\"FacmNo\" = B.\"FacmNo\"                      ";
		sql += " ORDER BY B.\"CustNo\",B.\"FacmNo\"                                                   ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> doDetail(int custNo, TitaVo titaVo) throws Exception {
		dateUtil.init();

		this.info("BankStatementServiceImpl doDetail");
		int adjDate = Integer.parseInt(titaVo.getParam("AdjDate")) + 19110000;
		int ieday = titaVo.getEntDyI() + 19110000;
		dateUtil.setDate_1(ieday);
		dateUtil.setMons(-6);
		int isday = Integer.parseInt(String.valueOf(dateUtil.getCalenderDay()).substring(0, 6) + "01");
		this.info("custNo ... " + custNo);
		this.info("isday ... " + isday);
		this.info("ieday ... " + ieday);

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
		sql += "       ,CASE WHEN BR.\"TxEffectDate\" = 0 THEN 0 WHEN BR.\"TxEffectDate\" IS NOT NULL THEN BR.\"TxEffectDate\" - 19110000 ELSE 0 END AS \"TxEffectDate\"";
		sql += "       ,NVL(BR.\"PresentRate\",0)                            AS \"PresentRate\"";
		sql += "       ,NVL(BR.\"AdjustedRate\",0)                           AS \"AdjustedRate\"";
		sql += "       ,X.\"AcDate\"                      ";
		sql += "       ,X.\"RepayCode\"                   ";
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
		sql += "         AND  T.\"EntryDate\" >= " + isday; // tbsdy六個月前的月初日
		sql += "         AND  T.\"EntryDate\" <= " + ieday; // tbsdy
		sql += "       GROUP BY  t.\"FacmNo\", t.\"EntryDate\", t.\"AcDate\", CASE WHEN t.\"IntStartDate\" = 0 AND t.\"IntEndDate\" = 0 THEN 'Y' ELSE 'N' END                      ";
		sql += "      ) X                                                                             ";
		sql += " LEFT JOIN \"CdCode\" CD ON CD.\"DefCode\" = 'RepayCode'                              ";
		sql += "                        AND CD.\"Code\"    =  X.\"RepayCode\"                         ";
		sql += " LEFT JOIN \"BatxRateChange\" BR ON BR.\"CustNo\" = " + custNo;
		sql += "                                AND BR.\"FacmNo\" =  X.\"FacmNo\"                     ";
		sql += "                                AND BR.\"AdjDate\" = " + adjDate;
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

		sql += " WHERE LB.\"SpecificDd\" IS NOT NULL";
		sql += " ORDER BY X.\"FacmNo\",X.\"EntryDate\"                                                             ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

}