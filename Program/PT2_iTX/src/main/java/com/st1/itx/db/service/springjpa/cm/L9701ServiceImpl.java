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
	public List<Map<String, String>> doQuery1(boolean isCalcuExcessive, TitaVo titaVo) throws Exception {

		String iCUSTNO = titaVo.get("CustNo");
		String iTYPE = titaVo.get("DateType");
		String iSDAY = String.valueOf(Integer.valueOf(titaVo.get("BeginDate")) + 19110000);
		String iEDAY = String.valueOf(Integer.valueOf(titaVo.get("EndDate")) + 19110000);
		String iHFG = titaVo.get("CorrectType");
		String sql = "";
		// 找明細中最小額度
		sql += "    WITH \"tmpMain\" AS (";
		sql += "        SELECT";
		sql += "          NVL(MIN(T.\"FacmNo\"),1) \"FacmNo\"";
		sql += "        FROM";
		sql += "            \"LoanBorTx\" T";
		sql += "        WHERE";
		sql += "            T.\"CustNo\" = :ICUSTNO";
		sql += "            AND NVL( JSON_VALUE(T.\"OtherFields\", '$.TempReasonCode'), ' ' ) NOT IN ( '03', '06' )";
		sql += "            AND ( T.\"TxAmt\" <> 0";
		sql += "            OR T.\"TempAmt\" <> 0 )";
		sql += "            AND DECODE( T.\"EntryDate\", 0, T.\"AcDate\", T.\"EntryDate\" ) >= :ISDAY";
		sql += "            AND DECODE( T.\"EntryDate\", 0, T.\"AcDate\", T.\"EntryDate\" ) <= :IEDAY";
		sql += "            AND T.\"FacmNo\" > 0";
		sql += "    )";
		sql += "    SELECT";
		sql += "        T.\"CustNo\",";
		sql += "        T.\"FacmNo\",";
		sql += "        T.\"Desc\",";
		sql += "		T.\"RepayItem\",";
		sql += "        T.\"EntryDate\",";
		sql += "        T.\"Amount\",";
		sql += "        T.\"IntStartDate\",";
		sql += "        T.\"IntEndDate\",";
		sql += "        T.\"Rate\",";
		sql += "        T.\"Principal\",";
		sql += "        T.\"Interest\",";
		sql += "        T.\"BreachAmt\",";
		sql += "        T.\"TxAmt\",";
		sql += "        T.\"FeeAmt\",";
		sql += "        T.\"Overflow\",";
		sql += "        T.\"ShortAmt\",";
		sql += "        T.\"Excessive\",";
		sql += "        T.\"OverShort\",";
		sql += "        T.\"DB\",";
		sql += "        T.\"CustName\",";
		sql += "        CASE";
		sql += "            WHEN CB.\"BdLocation\" IS NOT NULL THEN";
		sql += "                CB.\"BdLocation\"";
		sql += "            ELSE";
		sql += "                CL.\"LandLocation\"";
		sql += "        END AS \"Location\",";
		sql += "        T.\"TitaHCode\",";
		sql += "        T.\"TitaTlrNo\",";
		sql += "        T.\"TitaTxtNo\",";
		sql += "        T.\"AcSeq\",";
		sql += "        T.\"CreateDate\"";
		sql += "        T.\"Displayflag\"";
		sql += "    FROM";
		sql += "        (";
		sql += "    SELECT";
		sql += "        T.\"CustNo\",";
		sql += "        NVL(T.\"FacmNo\",(SELECT \"FacmNo\" FROM \"tmpMain\")) AS \"FacmNo\",";
		sql += "        CASE";
		sql += "            WHEN T.\"TxDescCode\" = '3202' THEN";
		sql += "                '回收登錄'";
		sql += "            WHEN T.\"TxDescCode\" IN ( '3420', '3421', '3422' ) THEN";
		sql += "                '結案登錄'";
		sql += "            WHEN T.\"TxDescCode\" IN ( '3101', '3102' ) THEN";
		sql += "                '契約變更'";
		sql += "            WHEN T.\"TxDescCode\" IN ( '3100') AND T.\"RenewFlag\" IN ('1','2') THEN";
		sql += "                '契約變更'";//借新還舊、展期=>顯示契約變更
		sql += "            WHEN T.\"TxDescCode\" IN ( '3100') AND T.\"RenewFlag\" IN ('0') THEN";
		sql += "                '撥款'";
		sql += "            WHEN CC1.\"Code\" IS NOT NULL THEN";
		sql += "                TO_CHAR( NVL( CC1.\"Item\", '  ' ) )";
		sql += "            WHEN CC2.\"Code\" IS NOT NULL THEN";
		sql += "                TO_CHAR( NVL( CC2.\"Item\", '  ' ) )";
		sql += "            ELSE";
		sql += "                CASE";
		sql += "                    WHEN T.\"TxDescCode\" = '3100' THEN";
		sql += "                        TO_CHAR( NVL( T.\"Desc\", '  ' ) )";
		sql += "                    ELSE";
		sql += "                        '契約變更'";
		sql += "                END";
		sql += "        END AS \"Desc\",";
		sql += "		RPCODE.\"Item\" AS \"RepayItem\",";
		sql += "        T.\"EntryDate\",";
		sql += "        NVL( T.\"Amount\",";
		sql += "        0 ) AS \"Amount\",";
		sql += "        T.\"IntStartDate\",";
		sql += "        T.\"IntEndDate\",";
		sql += "        T.\"Rate\",";
		sql += "        T.\"Principal\",";
		sql += "        T.\"Interest\",";
		sql += "        T.\"BreachAmt\",";
		sql += "        T.\"TxAmt\",";
		sql += "        T.\"FeeAmt\" AS \"FeeAmt\",";
		sql += "        CASE";
		sql += "          WHEN  T.\"Excessive\" / T.\"ExcessiveCount\" <  0 THEN";
		sql += "            CASE";
		sql += "              WHEN T.\"TitaHCode\" IN( 2, 4 ) THEN";
		sql += "                   T.\"Excessive\" / T.\"ExcessiveCount\"";
		sql += "              ELSE";
		sql += "                  -T.\"Excessive\" / T.\"ExcessiveCount\"";
		sql += "              END";
		sql += "          ELSE";
		sql += "             0";
		sql += "        END        AS \"ShortAmt\",";
		sql += "        CASE";
		sql += "          WHEN  T.\"Excessive\" / T.\"ExcessiveCount\" >  0 THEN";
		sql += "            CASE";
		sql += "              WHEN T.\"TitaHCode\" IN( 2, 4 ) THEN";
		sql += "                  -T.\"Excessive\" / T.\"ExcessiveCount\"";
		sql += "              ELSE";
		sql += "                   T.\"Excessive\" / T.\"ExcessiveCount\"";
		sql += "              END";
		sql += "          ELSE";
		sql += "             0";
		sql += "        END        AS \"Overflow\",";
		sql += "        T.\"Excessive\" / T.\"ExcessiveCount\" AS \"Excessive\",";
		sql += "        T.\"OverShort\",";
		sql += "        T.\"DB\",";
		sql += "        \"Fn_ParseEOL\"( C.\"CustName\",";
		sql += "        0 ) AS \"CustName\",";
		sql += "        T.\"TitaHCode\",";
		sql += "        T.\"TitaCalDy\",";
		sql += "        T.\"TitaCalTm\",";
		sql += "        T.\"TitaTlrNo\",";
		sql += "        T.\"TitaTxtNo\",";
		sql += "        T.\"AcSeq\",";
		sql += "        T.\"CreateDate\",";
		sql += "        T.\"Displayflag\"";
		sql += "    FROM";
		sql += "        (";
		sql += "            SELECT";
		sql += "                T.\"CustNo\",";
		sql += "                DECODE(T.\"FacmNo\",0,NVL(IR.\"FacmNo\",NVL(AC.\"FacmNo\",FF.\"FacmNo\")),T.\"FacmNo\")  AS  \"FacmNo\",";
		sql += "                MAX(DECODE( T.\"EntryDate\", 0, T.\"AcDate\", T.\"EntryDate\" )) AS \"EntryDate\",";
		sql += "                SUM(T.\"LoanBal\" + T.\"Principal\")                           AS \"Amount\",";
		sql += "                T.\"IntStartDate\"                                           AS \"IntStartDate\",";
		sql += "                T.\"IntEndDate\"                                             AS \"IntEndDate\",";
		sql += "                T.\"Rate\"                                                   AS \"Rate\",";
		sql += "                SUM(";
		sql += "                    CASE";
		sql += "                        WHEN T.\"TitaHCode\" IN( 2, 4 ) THEN";
		sql += "                            - T.\"TxAmt\"";
		sql += "                        ELSE";
		sql += "                            T.\"TxAmt\"";
		sql += "                    END )                                                  AS \"TxAmt\",";
		sql += "                SUM(";
		sql += "                    CASE";
		sql += "                        WHEN T.\"TitaHCode\" IN( 2, 4 ) THEN";
		sql += "                            - T.\"Principal\"";
		sql += "                        ELSE";
		sql += "                            T.\"Principal\"";
		sql += "                    END )                                                  AS \"Principal\",";
		sql += "                SUM(";
		sql += "                    CASE";
		sql += "                        WHEN T.\"TitaHCode\" IN( 2, 4 ) THEN";
		sql += "                            - T.\"Interest\"";
		sql += "                        ELSE";
		sql += "                            T.\"Interest\"";
		sql += "                    END )                                                  AS \"Interest\",";
		sql += "                SUM(";
		sql += "                    CASE";
		sql += "                        WHEN T.\"TitaHCode\" IN( 2, 4 ) THEN";
		sql += "                            - T.\"DelayInt\" - T.\"BreachAmt\" - T.\"CloseBreachAmt\"";
		sql += "                        ELSE";
		sql += "                            T.\"DelayInt\" + T.\"BreachAmt\" + T.\"CloseBreachAmt\"";
		sql += "                    END )                                                  AS \"BreachAmt\",";
		sql += "                SUM(";
		sql += "                    CASE";
		sql += "                        WHEN T.\"TitaHCode\" IN( 2, 4 ) THEN";
		sql += "                            - T.\"FeeAmt\"";
		sql += "                        ELSE";
		sql += "                            T.\"FeeAmt\"";
		sql += "                    END )                                                  AS \"FeeAmt\",";
		sql += "                SUM(T.\"Overflow\" - (T.\"UnpaidPrincipal\" + T.\"UnpaidInterest\" + T.\"UnpaidCloseBreach\"))   AS \"OverShort\",";
		sql += "            	SUM(NVL(JSON_VALUE(T.\"OtherFields\", '$.Excessive'),0)) AS \"Excessive\", ";
		sql += "            	COUNT(NVL(JSON_VALUE(T.\"OtherFields\", '$.Excessive'),0)) AS \"ExcessiveCount\", ";
		sql += "                T.\"Desc\",";
		sql += "                T.\"TxDescCode\",";
		sql += "                T.\"AcctCode\",";
		sql += "                MAX(T.\"TitaTxCd\")                                          AS \"TitaTxCd\",";
		sql += "                MAX(T.\"TitaCalDy\")                                         AS \"TitaCalDy\",";
		sql += "                MAX(T.\"TitaCalTm\")                                         AS \"TitaCalTm\",";
		sql += "                T.\"TitaHCode\"                                              AS \"TitaHCode\",";
		sql += "                MAX(T.\"TitaTlrNo\")                                         AS \"TitaTlrNo\",";
		sql += "                MAX(T.\"TitaTxtNo\")                                         AS \"TitaTxtNo\",";
		sql += "                MAX(T.\"AcSeq\")                                         AS \"AcSeq\",";
		sql += "                MAX(T.\"CreateDate\")                                         AS \"CreateDate\",";
		sql += "                MIN(L.\"Displayflag\")                                         AS \"Displayflag\",";
		sql += "                T.\"RepayCode\",";
		sql += "                MIN(L.\"RenewFlag\")                                         AS \"RenewFlag\",";
		
		sql += "            FROM";
		sql += "                \"LoanBorTx\" T";
		sql += "            LEFT JOIN (";
		sql += "        		SELECT DISTINCT ";
		sql += "           		 	T.\"CustNo\",";
		sql += "            	 	NVL(IR.\"FacmNo\", T.\"FacmNo\") \"FacmNo\",";
		sql += "           		 	T.\"AcDate\"";
		sql += "        		FROM";
		sql += "            		\"LoanBorTx\" T";
		sql += "            	LEFT JOIN \"InsuRenew\" IR ON IR.\"AcDate\" =T.\"AcDate\"";
		sql += "            							AND IR.\"CustNo\" = T.\"CustNo\"";
		sql += "        		WHERE";
		sql += "            		T.\"CustNo\" = :ICUSTNO";
		sql += "            		AND NVL( JSON_VALUE(T.\"OtherFields\", '$.TempReasonCode'), ' ' ) NOT IN ( '03', '06' )";
		sql += "            		AND ( T.\"TxAmt\" <> 0";
		sql += "            		OR T.\"TempAmt\" <> 0 )";
		sql += "            		AND DECODE( T.\"EntryDate\", 0, T.\"AcDate\", T.\"EntryDate\" ) >= :ISDAY";
		sql += "            		AND DECODE( T.\"EntryDate\", 0, T.\"AcDate\", T.\"EntryDate\" ) <= :IEDAY";
		sql += "            		AND IR.\"CustNo\" IS NOT NULL ) IR";
		sql += "             ON IR.\"AcDate\" =T.\"AcDate\"";
		sql += "            AND IR.\"CustNo\" = T.\"CustNo\"";
		sql += "            AND JSON_VALUE(T.\"OtherFields\", '$.FireFee') IS NOT NULL ";

		sql += "            LEFT JOIN \"AcReceivable\" AC";
		sql += "             ON AC.\"TitaTlrNo\" =T.\"TitaTlrNo\"";
		sql += "            AND AC.\"TitaTxtNo\" = TO_NUMBER(T.\"TitaTxtNo\")";
		sql += "            AND AC.\"CustNo\" = T.\"CustNo\"";
		sql += "            AND JSON_VALUE(T.\"OtherFields\", '$.AcctFee') IS NOT NULL ";

		sql += "            LEFT JOIN (";
		sql += "            		SELECT DISTINCT \"CustNo\",\"FacmNo\" FROM \"ForeclosureFee\"";
		sql += "            ) FF";

		sql += "             ON FF.\"CustNo\" =T.\"CustNo\"";
		sql += "            AND JSON_VALUE(T.\"OtherFields\", '$.LawFee') IS NOT NULL ";

		sql += "            LEFT JOIN \"LoanBorMain\" L";
		sql += "             ON L.\"CustNo\" =T.\"CustNo\"";
		sql += "            AND L.\"FacmNo\" = T.\"FacmNo\"";
		sql += "            AND L.\"BormNo\" = T.\"BormNo\"";

		sql += "            WHERE";
		sql += "                T.\"CustNo\" = :ICUSTNO";
		sql += "                AND NVL( JSON_VALUE(T.\"OtherFields\", '$.TempReasonCode'), ' ' ) NOT IN ( '03', '06' )";
		sql += "                AND ( T.\"TxAmt\" <> 0";
		sql += "                OR T.\"TempAmt\" <> 0 )";

		if (iTYPE.equals("1")) {
			sql += "                AND DECODE( T.\"EntryDate\", 0, T.\"AcDate\", T.\"EntryDate\" ) >= :ISDAY";
			sql += "                AND DECODE( T.\"EntryDate\", 0, T.\"AcDate\", T.\"EntryDate\" ) <= :IEDAY";
		} else {
			sql += "          AND T.\"AcDate\" >= :ISDAY";
			sql += "          AND T.\"AcDate\" <= :IEDAY";
		}

		if (iHFG.equals("0")) {
			if (!isCalcuExcessive) {
				sql += "          AND T.\"TitaHCode\" = 0";
			}
		}

		sql += "            GROUP BY";
		sql += "                T.\"CustNo\", ";
		sql += "                DECODE(T.\"FacmNo\",0,NVL(IR.\"FacmNo\",NVL(AC.\"FacmNo\",FF.\"FacmNo\")),T.\"FacmNo\") ,";
		sql += "				T.\"AcDate\", T.\"IntStartDate\", T.\"IntEndDate\", T.\"Rate\", T.\"Desc\", T.\"TxDescCode\", T.\"AcctCode\", T.\"TitaHCode\", T.\"RepayCode\"";
		sql += "        )            T";
		sql += "        LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = T.\"CustNo\"";
		sql += "        LEFT JOIN \"CdCode\" CC1 ON CC1.\"DefCode\" = 'AcctCode'";
		sql += "        						AND CC1.\"Code\" = T.\"AcctCode\"";
		sql += "        						AND T.\"TxDescCode\" = 'Fee'";
		sql += "        LEFT JOIN \"CdCode\" CC2 ON CC2.\"DefCode\" = 'TxDescCode'";
		sql += "        						AND CC2.\"Code\" = T.\"TxDescCode\"";
		sql += "        						AND T.\"TxDescCode\" <> 'Fee'";
		sql += "        LEFT JOIN \"CdCode\" RPCODE ON RPCODE.\"DefCode\" = 'BatchRepayCode'";
		sql += "        						   AND RPCODE.\"Code\" = T.\"RepayCode\"";
		sql += "        LEFT JOIN \"CdCode\" CDT ON CDT.\"DefCode\" = 'TxDescCode'";
		sql += "        					    AND CDT.\"Code\" = T.\"TxDescCode\"";
		sql += "        LEFT JOIN \"CdCode\" CDF ON T.\"TxDescCode\" = 'Fee'";
		sql += "       							AND CDF.\"DefCode\" = 'AcctCode'";
		sql += "        						AND CDF.\"Code\" = T.\"AcctCode\"";
		sql += ") T";
		sql += "        LEFT JOIN \"ClFac\" F ON F.\"CustNo\" = T.\"CustNo\"";
		sql += "        					 AND F.\"FacmNo\" = T.\"FacmNo\"";
		sql += "        					 AND F.\"MainFlag\" = 'Y'";
		sql += "        LEFT JOIN \"ClBuilding\" CB ON CB.\"ClCode1\" = F.\"ClCode1\"";
		sql += "        						   AND CB.\"ClCode2\" = F.\"ClCode2\"";
		sql += "       							   AND CB.\"ClNo\" = F.\"ClNo\"";
		sql += "        						   AND F.\"ClCode1\" = 1";
		sql += "        LEFT JOIN \"ClLand\" CL ON CL.\"ClCode1\" = F.\"ClCode1\"";
		sql += "        					   AND CL.\"ClCode2\" = F.\"ClCode2\"";
		sql += "        					   AND CL.\"ClNo\" = F.\"ClNo\"";
		sql += "        					   AND CL.\"LandSeq\" = 0";
		sql += "        					   AND F.\"ClCode1\" = 2";
		sql += "    ORDER BY T.\"FacmNo\",";
		sql += "        T.\"EntryDate\",";
		sql += "        T.\"TitaCalDy\",";
		sql += "        T.\"TitaCalTm\",";
		sql += "        T.\"TitaTlrNo\",";
		sql += "        T.\"TitaTxtNo\",";
		sql += "        T.\"AcSeq\",";
		sql += "        T.\"CreateDate\",";
		sql += "        T.\"Displayflag\"";
		


		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("ICUSTNO", iCUSTNO);
		query.setParameter("ISDAY", iSDAY);
		query.setParameter("IEDAY", iEDAY);
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


}