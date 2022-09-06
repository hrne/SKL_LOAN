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

	public List<Map<String, String>> doQuery(int custNo, int facmNo, TitaVo titaVo) throws Exception {
		dateUtil.init();

		this.info("BankStatementServiceImpl doQuery");
		int ieday = titaVo.getEntDyI() + 19110000;
		dateUtil.setDate_1(ieday);
		dateUtil.setMons(-6);
		int isday = Integer.parseInt(String.valueOf(dateUtil.getCalenderDay()).substring(0, 6) + "01");
		this.info("custNo ... " + custNo);
		this.info("facmNo ... " + facmNo);
		this.info("isday ... " + isday);
		this.info("ieday ... " + ieday);

//		因抓取不到費用類，第一層group by 到額度(同額度同調整日、同源利率

		String sql = " ";
		sql += "SELECT  B.\"CustNo\"                                          "; // 戶號
		sql += "       ,B.\"FacmNo\"                                          "; // 額度
		sql += "       ,C.\"CustName\"                                        "; // 戶名
		sql += "       ,B.\"SpecificDd\"                                      "; // 應繳日
		sql += "       ,CD.\"Item\"                      AS \"RepayCodeX\"    "; // 繳款方式
		sql += "       ,B.\"LoanBal\"                                         "; // 貸放餘額
		sql += "       ,B.\"NextPayIntDate\"                                  "; // 下繳日
		sql += "       ,NVL(CB.\"BdLocation\", ' ')      AS \"Location\"      "; // 押品地址
		sql += "       ,NVL(CN.\"PaperNotice\", 'Y')     AS \"PaperNotice\"   "; // 書面通知與否 Y:寄送 N:不寄送
		sql += "  FROM (SELECT \"CustNo\"                                             ";
		sql += "                   ,\"FacmNo\"                                             ";
		sql += "                   ,SUM(\"LoanBal\")             AS \"LoanBal\"            ";
		sql += "                   ,MIN(\"NextPayIntDate\")      AS \"NextPayIntDate\"     ";
		sql += "                   ,MAX(\"SpecificDd\")          AS \"SpecificDd\"         ";
		sql += "             FROM \"LoanBorMain\"                                          ";
		sql += "             WHERE \"CustNo\" = " + custNo;
		sql += "               AND \"FacmNo\" = " + facmNo;
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
		sql += " ORDER BY B.\"CustNo\",B.\"FacmNo\"                                                   ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> doDetail(int custNo, int facmNo, TitaVo titaVo) throws Exception {
		dateUtil.init();

		this.info("BankStatementServiceImpl doDetail");
		int ieday = titaVo.getEntDyI() + 19110000;
		dateUtil.setDate_1(ieday);
		dateUtil.setMons(-6);
		int isday = Integer.parseInt(String.valueOf(dateUtil.getCalenderDay()).substring(0, 6) + "01");
		this.info("custNo ... " + custNo);
		this.info("facmNo ... " + facmNo);
		this.info("isday ... " + isday);
		this.info("ieday ... " + ieday);

//		因抓取不到費用類，第一層group by 到額度(同額度同調整日、同源利率

		String sql = " ";
		sql += " SELECT X.\"EntryDate\"                                       "; // F0:入帳日
		sql += "       ,X.\"IntStartDate\"                                    "; // F1:計息期間
		sql += "       ,X.\"IntEndDate\"                                      "; // F2:計息期間
		sql += "       ,CD.\"Item\"                                          AS \"RepayCodeX\" "; // F13:繳款方式
		sql += "       ,X.\"TxAmt\"                                           "; // F4:繳款金額
		sql += "       ,X.\"Principal\"                                       "; // F5:攤還本金
		sql += "       ,X.\"Interest\"                                        "; // F6:繳息金額
		sql += "       ,X.\"BreachAmt\" + \"DelayInt\"                       AS \"BreachAmt\"  "; // F7:違約金+延滯息
		sql += "       ,X.\"FEE1\" + X.\"FEE2\" + X.\"FEE3\" + X.\"FEE4\"    AS \"OtherFee\"   "; // F8:火險費或其他費用
		sql += "       ,X.\"AcDate\"                      ";
		sql += "       ,X.\"TitaTlrNo\"                   ";
		sql += "       ,X.\"TitaTxtNo\"                   ";
		sql += "       ,X.\"RepayCode\"                   ";
		sql += " FROM ( SELECT MAX(T.\"EntryDate\")                                         AS \"EntryDate\"         ";
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
		sql += "             ,T.\"TitaTlrNo\"                   ";
		sql += "             ,T.\"TitaTxtNo\"                   ";
		sql += "        FROM \"LoanBorTx\" T                                             ";
		sql += "        WHERE T.\"CustNo\" = " + custNo;
//		sql += "         AND  T.\"FacmNo\" = " + facmNo;
		sql += "         AND  T.\"TitaHCode\" = 0                                          ";
		sql += "         AND (T.\"Principal\" + T.\"ExtraRepay\" + T.\"Interest\" + T.\"BreachAmt\" + T.\"CloseBreachAmt\"";
		sql += "              +  NVL(JSON_VALUE(T.\"OtherFields\",  '$.AcctFee'),0)                   ";
		sql += "              +  NVL(JSON_VALUE(T.\"OtherFields\",  '$.ModifyFee'),0)                 ";
		sql += "              +	NVL(JSON_VALUE(T.\"OtherFields\",  '$.FireFee'),0)                    ";
		sql += "              +	NVL(JSON_VALUE(T.\"OtherFields\",  '$.LawFee'),0) > 0                 ";
		sql += "                or T.\"TitaTxCd\" = 'L3210' )                                         ";
		sql += "         AND  T.\"EntryDate\" >= " + isday; // tbsdy六個月前的月初日
		sql += "         AND  T.\"EntryDate\" <= " + ieday; // tbsdy
		sql += "       GROUP BY  T.\"AcDate\", T.\"TitaTlrNo\", T.\"TitaTxtNo\"                        ";
		sql += "      ) X                                                                             ";
		sql += " LEFT JOIN \"CdCode\" CD ON CD.\"DefCode\" = 'RepayCode'                              ";
		sql += "                        AND CD.\"Code\"    =  X.\"RepayCode\"                         ";
		sql += " ORDER BY X.\"EntryDate\"                                                             ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

}