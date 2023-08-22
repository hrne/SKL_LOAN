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

@Service
@Repository
public class L9701ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * 客戶往來本息明細表
	 * 
	 * @param isCalcuExcessive 是否要計算累溢短收 
	 * @param titaVo
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> doQuery1(boolean isCalcuExcessive,TitaVo titaVo) throws Exception {

		String iCUSTNO = titaVo.get("CustNo");
		String iTYPE = titaVo.get("DateType");
		String iSDAY = String.valueOf(Integer.valueOf(titaVo.get("BeginDate")) + 19110000);
		String iEDAY = String.valueOf(Integer.valueOf(titaVo.get("EndDate")) + 19110000);
		String iHFG = titaVo.get("CorrectType");

		String sql = "SELECT T.\"CustNo\"";
		sql += "            ,T.\"FacmNo\"";
		sql += "		    ,rpCode.\"Item\" AS \"RepayItem\"";
		sql += "            ,CASE";
		sql += "			   WHEN T.\"TxDescCode\" = '3202' THEN '回收登錄' "; // --回收利息都寫回收登陸
		sql += "			   WHEN T.\"TxDescCode\" IN ('3420','3421','3422') THEN '結案登錄' "; 
		sql += "			   WHEN T.\"TxDescCode\" IN ('3101','3102') THEN '契約變更' "; 
		sql += "               WHEN CC1.\"Code\" IS NOT NULL THEN TO_CHAR(NVL(CC1.\"Item\",'  '))";
		sql += "               WHEN CC2.\"Code\" IS NOT NULL THEN TO_CHAR(NVL(CC2.\"Item\",'  '))";
		sql += "               ELSE ";
		sql += "				 CASE WHEN T.\"TxDescCode\" = '3100' THEN TO_CHAR(NVL(T.\"Desc\",'  '))";
		sql += "				 ELSE '契約變更' END";// --若不是新撥款，需寫契約變更
		sql += "			 END AS \"Desc\"";
		sql += "            ,T.\"EntryDate\"";
		sql += "            ,NVL(T.\"Amount\",0) AS \"Amount\"";
		sql += "            ,T.\"IntStartDate\"";
		sql += "            ,T.\"IntEndDate\"";
		sql += "            ,T.\"Rate\"";
		sql += "            ,T.\"Principal\"";
		sql += "            ,T.\"Interest\"";
		sql += "            ,T.\"BreachAmt\"";
		sql += "            ,T.\"TxAmt\"";
		sql += "            ,T.\"FeeAmt\" AS \"FeeAmt\"";// --短繳
		sql += "            ,T.\"TempAmt\"";// --暫收借
		sql += "            ,T.\"Overflow\"";// --暫收貸
		sql += "            ,T.\"ShortAmt\"";// --短繳                                              ";
		sql += "            ,T.\"Excessive\"";// --累溢短收                                              ";
		sql += "            ,T.\"DB\"";
		sql += "            ,\"Fn_ParseEOL\"(C.\"CustName\",0) AS \"CustName\"";
		sql += "            ,CASE ";
		sql += "               WHEN CB.\"BdLocation\" IS NOT NULL ";
		sql += "               THEN CB.\"BdLocation\" ";
		sql += "             ELSE CL.\"LandLocation\" END AS \"Location\"";
		sql += "            ,T.\"TitaHCode\"";
		sql += "      FROM (SELECT \"CustNo\"";
		sql += "                  ,\"FacmNo\"";
		sql += "                  ,MAX(DECODE(\"EntryDate\", 0, \"AcDate\" ,\"EntryDate\")) AS \"EntryDate\"";
		sql += "                  ,SUM(\"LoanBal\" + \"Principal\")  AS \"Amount\"";
		sql += "                  ,\"IntStartDate\"  AS \"IntStartDate\" ";
		sql += "                  ,\"IntEndDate\"    AS \"IntEndDate\" ";
		sql += "                  ,\"Rate\"          AS \"Rate\" ";
		sql += "                  ,SUM(CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"Principal\" ";
		sql += "                            ELSE \"Principal\" END ) AS \"Principal\"";
		sql += "                  ,SUM(CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"Interest\" ";
		sql += "                            ELSE \"Interest\" END )  AS \"Interest\" ";
		sql += "                  ,SUM(CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"DelayInt\" - \"BreachAmt\" - \"CloseBreachAmt\" ";
		sql += "                            ELSE \"DelayInt\" + \"BreachAmt\" +  \"CloseBreachAmt\" END)   AS \"BreachAmt\" ";
		sql += "                  ,SUM(CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"TxAmt\" ";
		sql += "                            ELSE \"TxAmt\" END )    AS \"TxAmt\"";
		sql += "                  ,SUM(CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"TempAmt\" ";
		sql += "                            ELSE \"TempAmt\" END )  AS \"TempAmt\"";
		sql += "                  ,SUM(CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"Overflow\" ";
		sql += "                            ELSE \"Overflow\" END )  AS \"Overflow\"";
		sql += "                  ,SUM(CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"FeeAmt\" ";
		sql += "                            ELSE \"FeeAmt\" END )  AS \"FeeAmt\"";
		sql += "                  ,SUM(CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"UnpaidInterest\" - \"UnpaidPrincipal\" - \"UnpaidCloseBreach\" ";
		sql += "                            ELSE \"UnpaidInterest\" + \"UnpaidPrincipal\" + \"UnpaidCloseBreach\" END) AS \"ShortAmt\"";
		sql += "				  ,SUM(NVL(JSON_VALUE(\"OtherFields\",'$.Excessive'),0)) AS \"Excessive\""; 
		sql += "                  ,\"Desc\"        ";
		sql += "                  ,\"TxDescCode\"  ";
		sql += "                  ,\"AcctCode\"    ";
		sql += "                  ,MAX(\"TitaTxCd\")     AS \"TitaTxCd\"  ";
		sql += "                  ,MAX(\"TitaCalDy\")    AS \"TitaCalDy\" ";
		sql += "                  ,MAX(\"TitaCalTm\")    AS \"TitaCalTm\" ";
		sql += "				  ,\"TitaHCode\" AS \"TitaHCode\"";
		sql += "                  ,\"RepayCode\"    ";
		sql += "                  ,1                 AS \"DB\"   ";
		sql += "            FROM \"LoanBorTx\" ";
		sql += "            WHERE \"CustNo\" = :icustno";
		sql += "             AND NVL(JSON_VALUE(\"OtherFields\",'$.TempReasonCode'),' ') NOT IN ('03','06')";// --03期票
																												// 06即期票現金
		sql += "             AND ( \"TxAmt\" <> 0";
		sql += "               OR  \"TempAmt\" <> 0)";

		if (iTYPE.equals("1")) {
			sql += "          AND DECODE(\"EntryDate\", 0, \"AcDate\", \"EntryDate\") >= :isday";
			sql += "          AND DECODE(\"EntryDate\", 0, \"AcDate\", \"EntryDate\") <= :ieday";
		} else {
			sql += "          AND \"AcDate\" >= :isday";
			sql += "          AND \"AcDate\" <= :ieday";
		}

		if (iHFG.equals("0")) {
			if(!isCalcuExcessive) {
				sql += "          AND \"TitaHCode\" = 0";
			}
		}
		sql += "            GROUP BY \"CustNo\", \"FacmNo\", \"AcDate\", \"IntStartDate\", \"IntEndDate\", \"Rate\", ";
		sql += "                     \"Desc\", \"TxDescCode\", \"AcctCode\", \"TitaTlrNo\",  \"TitaTxtNo\" ";
		sql += "				  	,\"TitaHCode\" ,\"RepayCode\"";
		sql += "            UNION ALL";
		sql += "            SELECT F.\"CustNo\"";
		sql += "                  ,F.\"FacmNo\"";
		sql += "                  ,Max(M.\"MaturityDate\") AS \"EntryDate\"";
		sql += "                  ,SUM(CASE WHEN M.\"Status\" IN (2, 7) THEN O.\"OvduBal\"";
		sql += "                            WHEN M.\"Status\"  =  0     THEN M.\"LoanBal\"";
		sql += "                            ELSE 0 END )         AS \"Amount\" ";
		sql += "                  ,0    AS \"IntStartDate\" ";
		sql += "                  ,0    AS \"IntEndDate\" ";
		sql += "                  ,0    AS \"Rate\" ";
		sql += "                  ,0    AS \"Principal\"";
		sql += "                  ,0    AS \"Interest\" ";
		sql += "                  ,0    AS \"BreachAmt\" ";
		sql += "                  ,0    AS \"TxAmt\"";
		sql += "                  ,0    AS \"TempAmt\"";
		sql += "                  ,0    AS \"ShortAmt\"";
		sql += "                  ,0    AS \"Overflow\"";
		sql += "                  ,0    AS \"FeeAmt\"";
		sql += "				  ,0    AS \"Excessive\"";
		sql += "                  ,NULL AS \"Desc\" ";
		sql += "                  ,NULL AS \"TxDescCode\" ";
		sql += "                  ,NULL AS \"AcctCode\"   ";
		sql += "                  ,NULL AS \"TitaTxCd\"   ";
		sql += "                  ,0    AS \"TitaCalDy\"  ";
		sql += "                  ,0    AS \"TitaCalTm\"  ";
		sql += "				  ,'0'	AS \"TitaHCode\"";
		sql += "				  ,0	AS \"RepayCode\"";
		sql += "                  ,2    AS \"DB\"   ";
		sql += "            FROM  \"FacMain\" F";
		sql += "            LEFT JOIN \"LoanBorMain\" M ON M.\"CustNo\" = F.\"CustNo\"";
		sql += "                                       AND M.\"FacmNo\" = F.\"FacmNo\"";
		sql += "                                       AND M.\"Status\" < 10 ";
		sql += "            LEFT JOIN \"LoanOverdue\" O ON O.\"CustNo\" = M.\"CustNo\"";
		sql += "                                       AND O.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                       AND O.\"BormNo\" = M.\"BormNo\"";
		sql += "                                       AND O.\"OvduNo\" = M.\"LastOvduNo\"";
		sql += "            WHERE F.\"CustNo\" = :icustno";
		sql += "             AND  NVL(M.\"BormNo\", 0) > 0 ";
		sql += "            GROUP BY F.\"CustNo\"";
		sql += "                    ,F.\"FacmNo\"";
		sql += "           ) T";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = T.\"CustNo\"";
		sql += "      LEFT JOIN \"CdCode\" CC1 ON CC1.\"DefCode\" = 'AcctCode' ";
		sql += "                              AND CC1.\"Code\" = T.\"AcctCode\"";
		sql += "                              AND T.\"TxDescCode\" = 'Fee'";
		sql += "      LEFT JOIN \"CdCode\" CC2 ON CC2.\"DefCode\" = 'TxDescCode' ";
		sql += "                              AND CC2.\"Code\" = T.\"TxDescCode\"";
		sql += "                              AND T.\"TxDescCode\" <> 'Fee'";
		sql += "      LEFT JOIN \"CdCode\" rpCode ON rpCode.\"DefCode\" = 'BatchRepayCode' ";
		sql += "                              AND rpCode.\"Code\" = T.\"RepayCode\"";
		sql += "      LEFT JOIN \"ClFac\" F ON F.\"CustNo\" = T.\"CustNo\"";
		sql += "                           AND F.\"FacmNo\" = T.\"FacmNo\"";
		sql += "                           AND F.\"MainFlag\" = 'Y'";
		sql += "      LEFT JOIN \"ClBuilding\" CB ON CB.\"ClCode1\" = F.\"ClCode1\"";
		sql += "                                 AND CB.\"ClCode2\" = F.\"ClCode2\"";
		sql += "                                 AND CB.\"ClNo\"    = F.\"ClNo\"";
		sql += "                                 AND F.\"ClCode1\"  = 1 ";
		sql += "      LEFT JOIN \"ClLand\" CL ON CL.\"ClCode1\" = F.\"ClCode1\"";
		sql += "                             AND CL.\"ClCode2\" = F.\"ClCode2\"";
		sql += "                             AND CL.\"ClNo\"    = F.\"ClNo\"";
		sql += "                             AND CL.\"LandSeq\" = 0 ";
		sql += "                             AND F.\"ClCode1\"  = 2 ";
		sql += "      LEFT JOIN \"CdCode\" CDT												     	 ";
		sql += "            ON  CDT.\"DefCode\" = 'TxDescCode'   							         ";
		sql += "           AND  CDT.\"Code\"    = T.\"TxDescCode\" 									 ";
		sql += "      LEFT JOIN \"CdCode\" CDF  													 ";
		sql += "            ON   T.\"TxDescCode\" = 'Fee'   									     ";
		sql += "           AND  CDF.\"DefCode\" = 'AcctCode'   									     ";
		sql += "           AND  CDF.\"Code\"    = T.\"AcctCode\" 									 ";
		sql += "      ORDER BY T.\"FacmNo\", T.\"DB\",T.\"EntryDate\" ,T.\"TitaCalDy\", T.\"TitaCalTm\"";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("icustno", iCUSTNO);
		query.setParameter("isday", iSDAY);
		query.setParameter("ieday", iEDAY);
		return this.convertToMap(query);
	}

	/**
	 * 客戶往來本息明細表（額度）
	 * 
	 * @param titaVo
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> doQuery2(TitaVo titaVo) throws Exception {

		String iCUSTNO = titaVo.get("CustNo");
		String iTYPE = titaVo.get("DateType");
		String iSDAY = String.valueOf(Integer.valueOf(titaVo.get("BeginDate")) + 19110000);
		String iEDAY = String.valueOf(Integer.valueOf(titaVo.get("EndDate")) + 19110000);
		String iHFG = titaVo.get("CorrectType");

		String sql = "SELECT T.\"CustNo\"";
		sql += "            ,T.\"FacmNo\"";
		sql += "            ,LPAD(T.\"BormNo\",3,0) AS \"BormNo\"";
		sql += "            ,T.\"EntryDate\"";
		sql += "            ,NVL(T.\"Amount\",0) AS \"Amount\"";
		sql += "            ,T.\"IntStartDate\"";
		sql += "            ,T.\"IntEndDate\"";
		sql += "            ,T.\"Rate\"";
		sql += "            ,T.\"Principal\"";
		sql += "            ,T.\"Interest\"";
		sql += "            ,T.\"BreachAmt\"";
		sql += "            ,T.\"TxAmt\"";
		sql += "            ,T.\"FeeAmt\" AS \"FeeAmt\"";// --費用
		sql += "            ,T.\"TempAmt\"";// --暫收借
		sql += "            ,T.\"Overflow\"";// --暫收貸
		sql += "            ,T.\"ShortAmt\"";// --短繳
		sql += "            ,T.\"Desc\"";
		sql += "            ,T.\"DB\"";
		sql += "            ,\"Fn_ParseEOL\"(C.\"CustName\",0) AS \"CustName\"";
		sql += "            ,CASE ";
		sql += "               WHEN CB.\"BdLocation\" IS NOT NULL ";
		sql += "               THEN CB.\"BdLocation\" ";
		sql += "             ELSE CL.\"LandLocation\" END AS \"Location\"";
		sql += "			,T.\"Displayflag\" AS \"Displayflag\"";
		sql += "      FROM (SELECT \"CustNo\"";
		sql += "                  ,\"FacmNo\"";
		sql += "                  ,\"BormNo\"";
		sql += "                  ,DECODE(\"EntryDate\", 0, \"AcDate\" ,\"EntryDate\")AS \"EntryDate\"";
		sql += "                  ,\"LoanBal\" + \"Principal\" AS \"Amount\"";
		sql += "                  ,\"IntStartDate\"  ";
		sql += "                  ,\"IntEndDate\"    ";
		sql += "                  ,\"Rate\"          ";
		sql += "                  ,CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"Principal\" ";
		sql += "                            ELSE \"Principal\" END  AS \"Principal\"";
		sql += "                  ,CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"Interest\" ";
		sql += "                            ELSE \"Interest\" END   AS \"Interest\" ";
		sql += "                  ,CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"DelayInt\" - \"BreachAmt\" - \"CloseBreachAmt\" ";
		sql += "                            ELSE \"DelayInt\" + \"BreachAmt\" +  \"CloseBreachAmt\" END   AS \"BreachAmt\" ";
		sql += "                  ,CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"TxAmt\" ";
		sql += "                            ELSE \"TxAmt\" END    AS \"TxAmt\"";
		sql += "                  ,CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"TempAmt\" ";
		sql += "                            ELSE \"TempAmt\" END  AS \"TempAmt\"";
		sql += "                  ,CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"Overflow\" ";
		sql += "                            ELSE \"Overflow\" END  AS \"Overflow\"";
		sql += "                  ,CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"FeeAmt\" ";
		sql += "                            ELSE \"FeeAmt\" END  AS \"FeeAmt\"";
		sql += "                  ,CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"UnpaidInterest\" - \"UnpaidPrincipal\" - \"UnpaidCloseBreach\" ";
		sql += "                            ELSE \"UnpaidInterest\" + \"UnpaidPrincipal\" + \"UnpaidCloseBreach\" END AS \"ShortAmt\"";
		sql += "                  ,\"Desc\" ";
		sql += "                  ,\"TitaCalDy\"  ";
		sql += "                  ,\"TitaCalTm\"  ";
		sql += "                  ,1                 AS \"DB\"   ";
		sql += "				  ,\"Displayflag\" AS \"Displayflag\"";
		sql += "            FROM \"LoanBorTx\" ";
		sql += "            WHERE \"CustNo\" = :icustno";
		sql += "             AND NVL(JSON_VALUE(\"OtherFields\",'$.TempReasonCode'),' ') NOT IN ('03','06')";// --03期票
																												// 06即期票現金
		sql += "             AND ( \"TxAmt\" <> 0";
		sql += "               OR  \"TempAmt\" <> 0)";
		sql += "             AND  NVL(\"BormNo\", 0) > 0 ";

		if (iTYPE.equals("1")) {
			sql += "          AND DECODE(\"EntryDate\", 0, \"AcDate\", \"EntryDate\") >= :isday";
			sql += "          AND DECODE(\"EntryDate\", 0, \"AcDate\", \"EntryDate\") <= :ieday";
		} else {
			sql += "          AND \"AcDate\" >= :isday";
			sql += "          AND \"AcDate\" <= :ieday";
		}

		if (iHFG.equals("0")) {
			sql += "          AND \"TitaHCode\" = 0";
		}
//		sql += "            GROUP BY \"CustNo\", \"FacmNo\", \"BormNo\" , \"AcDate\", \"IntStartDate\", \"IntEndDate\", \"Rate\", \"Desc\",  \"TitaTlrNo\",  \"TitaTxtNo\" ";
		sql += "            UNION ALL";
		sql += "            SELECT F.\"CustNo\"";
		sql += "                  ,F.\"FacmNo\"";
		sql += "                  ,0 AS \"BormNo\"";
		sql += "                  ,Max(M.\"MaturityDate\") AS \"EntryDate\"";
		sql += "                  ,SUM(CASE WHEN M.\"Status\" IN (2, 7) THEN O.\"OvduBal\"";
		sql += "                            WHEN M.\"Status\"  =  0     THEN M.\"LoanBal\"";
		sql += "                            ELSE 0 END )         AS \"Amount\" ";
		sql += "                  ,0    AS \"IntStartDate\" ";
		sql += "                  ,0    AS \"IntEndDate\" ";
		sql += "                  ,0    AS \"Rate\" ";
		sql += "                  ,0    AS \"Principal\"";
		sql += "                  ,0    AS \"Interest\" ";
		sql += "                  ,0    AS \"BreachAmt\" ";
		sql += "                  ,0    AS \"TxAmt\"";
		sql += "                  ,0    AS \"TempAmt\"";
		sql += "                  ,0    AS \"Overflow\"";
		sql += "                  ,0    AS \"FeeAmt\"";
		sql += "                  ,0    AS \"ShortAmt\"";
		sql += "                  ,NULL AS \"Desc\" ";
		sql += "                  ,0    AS \"TitaCalDy\" ";
		sql += "                  ,0    AS \"TitaCalTm\" ";
		sql += "                  ,2    AS \"DB\"   ";
		sql += "                  ,NULL AS \"Displayflag\"   ";
		sql += "            FROM  \"FacMain\" F";
		sql += "            LEFT JOIN \"LoanBorMain\" M ON M.\"CustNo\" = F.\"CustNo\"";
		sql += "                                       AND M.\"FacmNo\" = F.\"FacmNo\"";
		sql += "                                       AND M.\"Status\" < 10 ";
		sql += "            LEFT JOIN \"LoanOverdue\" O ON O.\"CustNo\" = M.\"CustNo\"";
		sql += "                                       AND O.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                       AND O.\"BormNo\" = M.\"BormNo\"";
		sql += "                                       AND O.\"OvduNo\" = M.\"LastOvduNo\"";
		sql += "            WHERE F.\"CustNo\" = :icustno";
		sql += "             AND  NVL(M.\"BormNo\", 0) > 0 ";
		sql += "            GROUP BY F.\"CustNo\"";
		sql += "                    ,F.\"FacmNo\"";
		sql += "           ) T";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = T.\"CustNo\"";
		sql += "      LEFT JOIN \"ClFac\" F ON F.\"CustNo\" = T.\"CustNo\"";
		sql += "                           AND F.\"FacmNo\" = T.\"FacmNo\"";
		sql += "                           AND F.\"MainFlag\" = 'Y'";
		sql += "      LEFT JOIN \"ClBuilding\" CB ON CB.\"ClCode1\" = F.\"ClCode1\"";
		sql += "                                 AND CB.\"ClCode2\" = F.\"ClCode2\"";
		sql += "                                 AND CB.\"ClNo\"    = F.\"ClNo\"";
		sql += "                                 AND F.\"ClCode1\"  = 1 ";
		sql += "      LEFT JOIN \"ClLand\" CL ON CL.\"ClCode1\" = F.\"ClCode1\"";
		sql += "                             AND CL.\"ClCode2\" = F.\"ClCode2\"";
		sql += "                             AND CL.\"ClNo\"    = F.\"ClNo\"";
		sql += "                             AND CL.\"LandSeq\" = 0 ";
		sql += "                             AND F.\"ClCode1\"  = 2 ";
		sql += "      ORDER BY T.\"FacmNo\",T.\"DB\" ,T.\"BormNo\", T.\"TitaCalDy\", T.\"TitaCalTm\"";
		sql += "              ,T.\"Displayflag\" ";
		sql += "              ,CASE WHEN T.\"TxAmt\" > 0  THEN 0 ELSE 1 END  ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("icustno", iCUSTNO);
		query.setParameter("isday", iSDAY);
		query.setParameter("ieday", iEDAY);
		return this.convertToMap(query);
	}

	/**
	 * 客戶往來本息明細表（額度）
	 * 
	 * @param titaVo
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> doQuery3(TitaVo titaVo) throws Exception {

		String iCUSTNO = titaVo.get("CustNo");
		String iTYPE = titaVo.get("DateType");
		String iSDAY = String.valueOf(Integer.valueOf(titaVo.get("BeginDate")) + 19110000);
		String iEDAY = String.valueOf(Integer.valueOf(titaVo.get("EndDate")) + 19110000);
		String iHFG = titaVo.get("CorrectType");

		String sql = "SELECT T.\"CustNo\"";
		sql += "            ,T.\"FacmNo\"";
		sql += "            ,LPAD(T.\"BormNo\",3,0) AS \"BormNo\"";
		sql += "            ,NVL(T.\"Amount\",0) AS \"Amount\"";
		sql += "            ,T.\"EntryDate\"";
		sql += "            ,T.\"Principal\"";
		sql += "            ,T.\"Interest\"";
		sql += "            ,T.\"BreachAmt\"";
		sql += "            ,T.\"FeeAmt\" AS \"FeeAmt\"";// --費用
		sql += "            ,T.\"TempAmt\"";// --暫收借
		sql += "            ,T.\"Overflow\"";// --暫收貸
		sql += "            ,T.\"ShortAmt\"";// --短繳
		sql += "            ,T.\"Desc\"";
		sql += "            ,T.\"DB\"";
		sql += "            ,\"Fn_ParseEOL\"(C.\"CustName\",0) AS \"CustName\"";
		sql += "			,T.\"Displayflag\" AS \"Displayflag\"";
		sql += "      FROM (SELECT \"CustNo\"";
		sql += "                  ,\"FacmNo\"";
		sql += "                  ,\"BormNo\"";
		sql += "                  ,DECODE(\"EntryDate\", 0, \"AcDate\" ,\"EntryDate\")AS \"EntryDate\"";
		sql += "                  ,\"LoanBal\" + \"Principal\" AS \"Amount\"";
		sql += "                  ,\"IntStartDate\"  ";
		sql += "                  ,\"IntEndDate\"    ";
		sql += "                  ,\"Rate\"          ";
		sql += "                  ,CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"Principal\" ";
		sql += "                            ELSE \"Principal\" END  AS \"Principal\"";
		sql += "                  ,CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"Interest\" ";
		sql += "                            ELSE \"Interest\" END   AS \"Interest\" ";
		sql += "                  ,CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"DelayInt\" - \"BreachAmt\" - \"CloseBreachAmt\" ";
		sql += "                            ELSE \"DelayInt\" + \"BreachAmt\" +  \"CloseBreachAmt\" END   AS \"BreachAmt\" ";
		sql += "                  ,CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"TxAmt\" ";
		sql += "                            ELSE \"TxAmt\" END    AS \"TxAmt\"";
		sql += "                  ,CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"TempAmt\" ";
		sql += "                            ELSE \"TempAmt\" END  AS \"TempAmt\"";
		sql += "                  ,CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"Overflow\" ";
		sql += "                            ELSE \"Overflow\" END  AS \"Overflow\"";
		sql += "                  ,CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"FeeAmt\" ";
		sql += "                            ELSE \"FeeAmt\" END  AS \"FeeAmt\"";
		sql += "                  ,CASE WHEN \"TitaHCode\" IN (2,4) THEN - \"UnpaidInterest\" - \"UnpaidPrincipal\" - \"UnpaidCloseBreach\" ";
		sql += "                            ELSE \"UnpaidInterest\" + \"UnpaidPrincipal\" + \"UnpaidCloseBreach\" END AS \"ShortAmt\"";
		sql += "                  ,\"Desc\" ";
		sql += "                  ,\"TitaCalDy\"  ";
		sql += "                  ,\"TitaCalTm\"  ";
		sql += "                  ,1                 AS \"DB\"   ";
		sql += "				  ,\"Displayflag\" AS \"Displayflag\"";
		sql += "            FROM \"LoanBorTx\" ";
		sql += "            WHERE \"CustNo\" = :icustno";
//		sql += "             AND JSON_VALUE(\"OtherFields\",'$.TempReasonCode') NOT IN ('03','06')";//--03期票 06即期票現金
		sql += "             AND  NVL(\"BormNo\", 0) > 0 ";
		if (iTYPE.equals("1")) {
			sql += "          AND DECODE(\"EntryDate\", 0, \"AcDate\", \"EntryDate\") >= :isday";
			sql += "          AND DECODE(\"EntryDate\", 0, \"AcDate\", \"EntryDate\") <= :ieday";
		} else {
			sql += "          AND \"AcDate\" >= :isday";
			sql += "          AND \"AcDate\" <= :ieday";
		}

		if (iHFG.equals("0")) {
			sql += "          AND \"TitaHCode\" = 0";
		}
//		sql += "            GROUP BY \"CustNo\", \"FacmNo\", \"AcDate\", \"IntStartDate\", \"IntEndDate\", \"Rate\", \"Desc\",  \"TitaTlrNo\",  \"TitaTxtNo\" ";
		sql += "            UNION ALL";
		sql += "            SELECT F.\"CustNo\"";
		sql += "                  ,F.\"FacmNo\"";
		sql += "                  ,0 AS \"BormNo\"";
		sql += "                  ,Max(M.\"MaturityDate\") AS \"EntryDate\"";
		sql += "                  ,SUM(CASE WHEN M.\"Status\" IN (2, 7) THEN O.\"OvduBal\"";
		sql += "                            WHEN M.\"Status\"  =  0     THEN M.\"LoanBal\"";
		sql += "                            ELSE 0 END )         AS \"Amount\" ";
		sql += "                  ,0    AS \"IntStartDate\" ";
		sql += "                  ,0    AS \"IntEndDate\" ";
		sql += "                  ,0    AS \"Rate\" ";
		sql += "                  ,0    AS \"Principal\"";
		sql += "                  ,0    AS \"Interest\" ";
		sql += "                  ,0    AS \"BreachAmt\" ";
		sql += "                  ,0    AS \"TxAmt\"";
		sql += "                  ,0    AS \"TempAmt\"";
		sql += "                  ,0    AS \"Overflow\"";
		sql += "                  ,0    AS \"FeeAmt\"";
		sql += "                  ,0    AS \"ShortAmt\"";
		sql += "                  ,NULL AS \"Desc\" ";
		sql += "                  ,0    AS \"TitaCalDy\" ";
		sql += "                  ,0    AS \"TitaCalTm\" ";
		sql += "                  ,2    AS \"DB\"   ";
		sql += "                  ,NULL AS \"Displayflag\"   ";
		sql += "            FROM  \"FacMain\" F";
		sql += "            LEFT JOIN \"LoanBorMain\" M ON M.\"CustNo\" = F.\"CustNo\"";
		sql += "                                       AND M.\"FacmNo\" = F.\"FacmNo\"";
		sql += "                                       AND M.\"Status\" < 10 ";
		sql += "            LEFT JOIN \"LoanOverdue\" O ON O.\"CustNo\" = M.\"CustNo\"";
		sql += "                                       AND O.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                       AND O.\"BormNo\" = M.\"BormNo\"";
		sql += "                                       AND O.\"OvduNo\" = M.\"LastOvduNo\"";
		sql += "            WHERE F.\"CustNo\" = :icustno";
		sql += "             AND  NVL(M.\"BormNo\", 0) > 0 ";
		sql += "            GROUP BY F.\"CustNo\"";
		sql += "                    ,F.\"FacmNo\"";
		sql += "           ) T";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = T.\"CustNo\"";
		sql += "      LEFT JOIN \"ClFac\" F ON F.\"CustNo\" = T.\"CustNo\"";
		sql += "                           AND F.\"FacmNo\" = T.\"FacmNo\"";
		sql += "                           AND F.\"MainFlag\" = 'Y'";
		sql += "      LEFT JOIN \"ClBuilding\" CB ON CB.\"ClCode1\" = F.\"ClCode1\"";
		sql += "                                 AND CB.\"ClCode2\" = F.\"ClCode2\"";
		sql += "                                 AND CB.\"ClNo\"    = F.\"ClNo\"";
		sql += "                                 AND F.\"ClCode1\"  = 1 ";
		sql += "      LEFT JOIN \"ClLand\" CL ON CL.\"ClCode1\" = F.\"ClCode1\"";
		sql += "                             AND CL.\"ClCode2\" = F.\"ClCode2\"";
		sql += "                             AND CL.\"ClNo\"    = F.\"ClNo\"";
		sql += "                             AND CL.\"LandSeq\" = 0 ";
		sql += "                             AND F.\"ClCode1\"  = 2 ";
		sql += "      ORDER BY T.\"FacmNo\",T.\"DB\" ,T.\"BormNo\", T.\"TitaCalDy\", T.\"TitaCalTm\"";
		sql += "              ,T.\"Displayflag\" ";
		sql += "              ,CASE WHEN T.\"TxAmt\" > 0  THEN 0 ELSE 1 END  ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("icustno", iCUSTNO);
		query.setParameter("isday", iSDAY);
		query.setParameter("ieday", iEDAY);
		return this.convertToMap(query);
	}

}