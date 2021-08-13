package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class BankStatementServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(BankStatementServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	DateUtil dateUtil;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> doQuery(int custNo, int facmNo, int adjDate, TitaVo titaVo) throws Exception {
		dateUtil.init();

		logger.info("BankStatementServiceImpl doQuery");
		int ieday = titaVo.getEntDyI() + 19110000;
		dateUtil.setDate_1(ieday);
		dateUtil.setMons(-6);
		int isday = Integer.parseInt(String.valueOf(dateUtil.getCalenderDay()).substring(0, 6) + "01");

		logger.info("custNo ... " + custNo);
		logger.info("facmNo ... " + facmNo);
		logger.info("adjDate ... " + adjDate);
		logger.info("isday ... " + isday);
		logger.info("ieday ... " + ieday);

//		因抓取不到費用類，第一層group by 到額度(同額度同調整日、同源利率

		String sql = " ";
		sql += " SELECT T.\"EntryDate\"                                       "; // F0:入帳日
		sql += "       ,T.\"IntStartDate\"                                    "; // F1:計息期間
		sql += "       ,T.\"IntEndDate\"                                      "; // F2:計息期間
		sql += "       ,CD.\"Item\"                                           "; // F3:繳款方式 之前的繳款方式???
		sql += "       ,T.\"TxAmt\"                                           "; // F4:繳款金額
		sql += "       ,T.\"Principal\"                                       "; // F5:攤還本金
		sql += "       ,T.\"Interest\"                                        "; // F6:繳息金額
		sql += "       ,T.\"BreachAmt\" + \"DelayInt\"                       AS F7 "; // F7:違約金+延滯息
		sql += "       ,T.\"FEE1\" + T.\"FEE2\" + T.\"FEE3\" + T.\"FEE4\"    AS \"OtherFee\"   "; // F8:火險費或其他費用
//		sql += "       ,NVL(NVL(CB.\"BdLocation\", CL.\"LandLocation\"),' ') AS \"Location\"   "; // F9:押品地址
		sql += "       ,NVL(CB.\"BdLocation\", ' ')                          AS \"Location\"   "; // F9:押品地址
		sql += "       ,C.\"CustName\"                                        "; // F10:戶名
		sql += "       ,T.\"CustNo\"                                          "; // F11:戶號
		sql += "       ,'' as F12                                             "; // F12:額度
		sql += "       ,DECODE(T.\"DueDate\", 0,T.\"IntEndDate\",(T.\"DueDate\")) AS \"DueDate\" "; // F13:應繳日
		sql += "       ,CD.\"Item\" AS \"Item2\"                              "; // F14:繳款方式(最新一筆)
		sql += "       ,T.\"LoanBal\"                                         "; // F15:貸放餘額
		sql += "       ,T.\"CurtEffDate\"                                     "; // F16:利率變更日(999年 99月 99日) 0416改為本次生效日
		sql += "       ,T.\"Rate\"                                            "; // F17:原利率
		sql += "       ,T.\"AdjustedRate\"                                    "; // F18:現在利率
		sql += "       ,T.\"NextPayIntDate\"                                  "; // F19:下繳日 最大的 找尋java用
		sql += " FROM ( SELECT DECODE(T.\"EntryDate\", 0, T.\"AcDate\", T.\"EntryDate\")    AS \"EntryDate\"         ";
		sql += "             ,T.\"IntStartDate\"                                            AS \"IntStartDate\"      ";
		sql += "             ,T.\"IntEndDate\"                                              AS \"IntEndDate\"        ";
		sql += "             ,MAX(T.\"Rate\")                                               AS \"Rate\"              ";
		sql += "             ,SUM(T.\"Interest\")                                           AS \"Interest\"          ";
		sql += "             ,SUM(T.\"DelayInt\")                                           AS \"DelayInt\"          ";
		sql += "             ,SUM(T.\"BreachAmt\")                                          AS \"BreachAmt\"         ";
		sql += "             ,SUM(T.\"Principal\" + T.\"ExtraRepay\")                       AS \"Principal\"         ";
		sql += "             ,MAX(T.\"DueDate\")                                            AS \"DueDate\"           ";
		sql += "             ,SUM(T.\"TxAmt\")                                              AS \"TxAmt\"             ";
		sql += "             ,MAX(LB.\"NextPayIntDate\")                                    AS \"NextPayIntDate\"    ";
		sql += "             ,SUM(NVL(JSON_VALUE(T.\"OtherFields\",  '$.AcctFee'),0))       AS FEE1                  ";
		sql += "             ,SUM(NVL(JSON_VALUE(T.\"OtherFields\",  '$.ModifyFee'),0))     AS FEE2                  ";
		sql += "             ,SUM(NVL(JSON_VALUE(T.\"OtherFields\",  '$.FireFee'),0))       AS FEE3                  ";
		sql += "             ,SUM(NVL(JSON_VALUE(T.\"OtherFields\",  '$.LawFee'),0))        AS FEE4                  ";
		sql += "             ,B.\"CustNo\"                                                                           ";
//		sql += "             ,B.\"FacmNo\"                                                                           ";
		sql += "             ,MAX(B.\"CurtEffDate\")                                        AS \"CurtEffDate\"       ";
		sql += "             ,MAX(B.\"AdjustedRate\")                                       AS \"AdjustedRate\"      ";
		sql += "             ,SUM(LB.\"LoanBal\")                                           AS \"LoanBal\"           ";
		sql += "       FROM (SELECT R.\"CustNo\"                                                                     ";
		sql += "                   ,R.\"FacmNo\"                                                                     ";
		sql += "                   ,MAX(R.\"CurtEffDate\")  AS \"CurtEffDate\"                                       ";
		sql += "                   ,MAX(R.\"AdjustedRate\") AS \"AdjustedRate\"                                      ";
		sql += "                   ,MAX(DECODE(T.\"EntryDate\", 0, T.\"AcDate\", T.\"EntryDate\")) AS \"EntryDate\"  ";
		sql += "             FROM (SELECT \"CustNo\"                                      ";
		sql += "                         ,\"FacmNo\"                                      ";
		sql += "                         ,\"BormNo\"                                      ";
		sql += "                         ,\"CurtEffDate\"                                 ";
		sql += "                         ,\"AdjustedRate\"                                 ";
		sql += " 		                 ,ROW_NUMBER() OVER (PARTITION BY  \"CurtEffDate\",\"CustNo\",\"FacmNo\",\"BormNo\" ";
		sql += " 		                               ORDER BY \"CurtEffDate\",\"CustNo\",\"FacmNo\",\"BormNo\" ) AS SEQ   ";
		sql += "                   FROM \"BatxRateChange\"                                ";
		sql += "                   WHERE \"AdjCode\" in (1,2,5)                           ";
		sql += "                     AND \"CustNo\" = :custNo                             ";
		sql += "                     AND \"FacmNo\" = :facmNo                             ";
		sql += "                     AND \"AdjDate\" = :adjDate                           ";
		sql += "             ) R                                                          ";
		sql += "             LEFT JOIN \"LoanBorTx\" T ON T.\"CustNo\"   = R.\"CustNo\"   ";
//		sql += "                                      AND T.\"FacmNo\" = R.\"FacmNo\"     ";
		sql += "             WHERE R.SEQ = 1                                              ";
		sql += "               AND DECODE(T.\"EntryDate\", 0, T.\"AcDate\", T.\"EntryDate\") >= :isday  "; // tbsdy六個月前的月初日
		sql += "               AND DECODE(T.\"EntryDate\", 0, T.\"AcDate\", T.\"EntryDate\") <= :ieday  "; // tbsdy
		sql += "             GROUP By R.\"CustNo\", R.\"FacmNo\"                          ";
		sql += "            ) B                                                           ";
		sql += "       LEFT JOIN \"LoanBorTx\" T ON T.\"CustNo\" = B.\"CustNo\"           ";
//		sql += "               AND T.\"FacmNo\" = B.\"FacmNo\"                            ";
//		此層也需篩選，否則會把舊的也抓近來
		sql += "               AND DECODE(T.\"EntryDate\", 0, T.\"AcDate\", T.\"EntryDate\") >= :isday  "; // tbsdy六個月前的月初日
		sql += "               AND DECODE(T.\"EntryDate\", 0, T.\"AcDate\", T.\"EntryDate\") <= :ieday  "; // tbsdy
		sql += "       LEFT JOIN \"LoanBorMain\" LB ON LB.\"CustNo\" = T.\"CustNo\"       ";
		sql += "                                  AND LB.\"FacmNo\" = T.\"FacmNo\"        ";
		sql += "                                  AND LB.\"BormNo\" = T.\"BormNo\"        ";
		sql += "       WHERE T.\"TitaHCode\" = 0                                          ";
		sql += "         AND ( T.\"BreachAmt\" + T.\"Principal\" + T.\"ExtraRepay\" + T.\"Interest\"  ";
		sql += "              +  NVL(JSON_VALUE(T.\"OtherFields\",  '$.AcctFee'),0)                   ";
		sql += "              +  NVL(JSON_VALUE(T.\"OtherFields\",  '$.ModifyFee'),0)                 ";
		sql += "              +	NVL(JSON_VALUE(T.\"OtherFields\",  '$.FireFee'),0)                    ";
		sql += "              +	NVL(JSON_VALUE(T.\"OtherFields\",  '$.LawFee'),0) > 0                 ";
		sql += "                or T.\"TitaTxCd\" = 'L3210' )                                         ";
		sql += "       GROUP BY B.\"CustNo\",                                                         ";
		sql += "        DECODE(T.\"EntryDate\", 0, T.\"AcDate\", T.\"EntryDate\"), T.\"IntStartDate\", T.\"IntEndDate\"  ";
		sql += "      ) T                                                                             ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\"   = T.\"CustNo\"                            ";
		sql += " LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\"  = T.\"CustNo\"                            ";
		sql += "                         AND FM.\"FacmNo\"  = :facmNo                                 ";
		sql += " LEFT JOIN \"ClFac\" F ON F.\"CustNo\"   = T.\"CustNo\"                               ";
		sql += "                      AND F.\"FacmNo\"   = :facmNo                                    ";
		sql += "                      AND F.\"MainFlag\" = 'Y'                                        ";
		sql += " LEFT JOIN \"ClBuilding\" CB ON CB.\"ClCode1\" =  F.\"ClCode1\"                       ";
		sql += "                            AND CB.\"ClCode2\" =  F.\"ClCode2\"                       ";
		sql += "                            AND CB.\"ClNo\"    =  F.\"ClNo\"                          ";
//		sql += " LEFT JOIN \"ClLand\" CL ON CL.\"ClCode1\" =  F.\"ClCode1\"                           ";
//		sql += "                        AND CL.\"ClCode2\" =  F.\"ClCode2\"                           ";
//		sql += "                        AND CL.\"ClNo\"    =  F.\"ClNo\"                              ";
		sql += " LEFT JOIN \"CdCode\" CD ON CD.\"DefCode\" = 'RepayCode'                              ";
		sql += "                        AND CD.\"Code\"    =  FM.\"RepayCode\"                        ";
		sql += " ORDER BY T.\"CustNo\",T.\"EntryDate\",                                               ";
		sql += " DECODE(T.\"IntStartDate\",0,99991231,T.\"IntStartDate\"),T.\"IntEndDate\"            ";

		logger.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("adjDate", adjDate);
		query.setParameter("isday", isday);
		query.setParameter("ieday", ieday);
		query.setParameter("custNo", custNo);
		query.setParameter("facmNo", facmNo);
		return this.convertToMap(query.getResultList());
	}

}