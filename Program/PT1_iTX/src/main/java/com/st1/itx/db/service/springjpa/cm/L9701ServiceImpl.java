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
		String ISDAY = String.valueOf(Integer.valueOf(titaVo.get("BeginDate")) + 19110000);
		String IEDAY = String.valueOf(Integer.valueOf(titaVo.get("EndDate")) + 19110000);
		String iHFG = titaVo.get("CorrectType");
		String sql = "";

		// 找明細中最小額度
		sql += "  WITH \"tmpMain\" AS (";
		sql += "      SELECT Nvl(";
		sql += "          MIN(T.\"FacmNo\"), 1";
		sql += "      ) \"FacmNo\"";
		sql += "      FROM \"LoanBorTx\" T";
		sql += "      WHERE T.\"CustNo\" = :ICUSTNO";
		sql += "            AND";
		sql += "            Nvl(";
		sql += "                JSON_VALUE(T.\"OtherFields\", '$.TempReasonCode'), ' '";
		sql += "            ) NOT IN (";
		sql += "                '03'";
		sql += "              , '06'";
		sql += "            )";
		sql += "            AND";
		sql += "            ( T.\"TxAmt\" <> 0";
		sql += "              OR";
		sql += "              T.\"TempAmt\" <> 0 )";
		sql += "            AND";
		sql += "            Decode(";
		sql += "                T.\"EntryDate\", 0";
		sql += "                 , T.\"AcDate\"";
		sql += "                 , T.\"EntryDate\"";
		sql += "            ) >= :ISDAY";
		sql += "            AND";
		sql += "            Decode(";
		sql += "                T.\"EntryDate\", 0";
		sql += "                 , T.\"AcDate\"";
		sql += "                 , T.\"EntryDate\"";
		sql += "            ) <= :IEDAY";
		sql += "            AND";
		sql += "            T.\"FacmNo\" > 0";
		sql += "  ), \"Main\" AS (";
		sql += "      SELECT T.\"CustNo\"";
		sql += "           , Decode(";
		sql += "          T.\"FacmNo\", 0";
		sql += "       , Nvl(";
		sql += "              Ir.\"FacmNo\", Nvl(";
		sql += "                  Ac.\"FacmNo\", Ff.\"FacmNo\"";
		sql += "              )";
		sql += "          ), T.\"FacmNo\"";
		sql += "      ) AS \"FacmNo\"";
		sql += "           , T.\"AcDate\"                                                                                           AS \"AcDate\"";
		sql += "           , T.\"EntryDate\"                                                                                        AS \"EntryDate\"";
		sql += "           , SUM(T.\"LoanBal\" + T.\"Principal\")                                                                     AS \"Amount\"";
		sql += "           , T.\"IntStartDate\"                                                                                     AS \"IntStartDate\"";
		sql += "           , T.\"IntEndDate\"                                                                                       AS \"IntEndDate\"";
		sql += "           , T.\"Rate\"                                                                                             AS \"Rate\"";
		sql += "           , SUM(";
		sql += "          CASE";
		sql += "              WHEN T.\"TitaHCode\" IN(";
		sql += "                  1, 3";
		sql += "              ) THEN 0 - T.\"TxAmt\"";
		sql += "              ELSE T.\"TxAmt\"";
		sql += "          END";
		sql += "      ) AS \"TxAmt\"";
		sql += "           , SUM(";
		sql += "          CASE";
		sql += "              WHEN T.\"TitaHCode\" IN(";
		sql += "                  1, 3";
		sql += "              ) THEN 0 - T.\"Principal\"";
		sql += "              ELSE T.\"Principal\"";
		sql += "          END";
		sql += "      ) AS \"Principal\"";
		sql += "           , SUM(";
		sql += "          CASE";
		sql += "              WHEN T.\"TitaHCode\" IN(";
		sql += "                  1, 3";
		sql += "              ) THEN 0 - T.\"Interest\"";
		sql += "              ELSE T.\"Interest\"";
		sql += "          END";
		sql += "      ) AS \"Interest\"";
		sql += "           , SUM(";
		sql += "          CASE";
		sql += "              WHEN T.\"TitaHCode\" IN(";
		sql += "                  1, 3";
		sql += "              ) THEN 0 - T.\"DelayInt\" - T.\"BreachAmt\" - T.\"CloseBreachAmt\"";
		sql += "              ELSE T.\"DelayInt\" + T.\"BreachAmt\" + T.\"CloseBreachAmt\"";
		sql += "          END";
		sql += "      ) AS \"BreachAmt\"";
		sql += "           , SUM(";
		sql += "          CASE";
		sql += "              WHEN T.\"TitaHCode\" IN(";
		sql += "                  1, 3";
		sql += "              ) THEN 0 - T.\"FeeAmt\"";
		sql += "              ELSE T.\"FeeAmt\"";
		sql += "          END";
		sql += "      ) AS \"FeeAmt\"";
		sql += "           , SUM(T.\"Overflow\" -(T.\"UnpaidPrincipal\" + T.\"UnpaidInterest\" + T.\"UnpaidCloseBreach\"))                AS \"OverShort\"";
		sql += "           , SUM(Nvl(";
		sql += "          JSON_VALUE(T.\"OtherFields\", '$.Excessive'), 0";
		sql += "      )) AS \"Excessive\"";
		sql += "           , COUNT(Nvl(";
		sql += "          JSON_VALUE(T.\"OtherFields\", '$.Excessive'), 0";
		sql += "      )) AS \"ExcessiveCount\"";
		sql += "           , T.\"Desc\"";
		sql += "           , T.\"TxDescCode\"";
		sql += "           , T.\"AcctCode\"";
		sql += "           , T.\"TitaTxCd\"                                                                                         AS \"TitaTxCd\"";
		sql += "           , T.\"TitaCalDy\"                                                                                        AS \"TitaCalDy\"";
		sql += "           , T.\"TitaCalTm\"                                                                                        AS \"TitaCalTm\"";
		sql += "           , T.\"TitaHCode\"                                                                                        AS \"TitaHCode\"";
		sql += "           , T.\"TitaTlrNo\"                                                                                        AS \"TitaTlrNo\"";
		sql += "           , T.\"TitaTxtNo\"                                                                                        AS \"TitaTxtNo\"";
		sql += "           , T.\"RepayCode\"";
		sql += "           , L.\"RenewFlag\"                                                                                        AS \"RenewFlag\"";
		sql += "      FROM \"LoanBorTx\"     T";
		sql += "      LEFT JOIN (";
		sql += "          SELECT DISTINCT T.\"CustNo\"";
		sql += "                        , Nvl(";
		sql += "              Ir.\"FacmNo\", T.\"FacmNo\"";
		sql += "          ) \"FacmNo\"";
		sql += "                        , T.\"AcDate\"";
		sql += "          FROM \"LoanBorTx\"  T";
		sql += "          LEFT JOIN \"InsuRenew\"  Ir ON Ir.\"AcDate\" = T.\"AcDate\"";
		sql += "                                      AND";
		sql += "                                      Ir.\"CustNo\" = T.\"CustNo\"";
		sql += "          WHERE T.\"CustNo\" = :ICUSTNO";
		sql += "                AND";
		sql += "                Nvl(";
		sql += "                    JSON_VALUE(T.\"OtherFields\", '$.TempReasonCode'), ' '";
		sql += "                ) NOT IN (";
		sql += "                    '03'";
		sql += "                  , '06'";
		sql += "                )";
		sql += "                AND";
		sql += "                ( T.\"TxAmt\" <> 0";
		sql += "                  OR";
		sql += "                  T.\"TempAmt\" <> 0 )";
		sql += "                AND";
		sql += "                Decode(";
		sql += "                    T.\"EntryDate\", 0";
		sql += "                     , T.\"AcDate\"";
		sql += "                     , T.\"EntryDate\"";
		sql += "                ) >= :ISDAY";
		sql += "                AND";
		sql += "                Decode(";
		sql += "                    T.\"EntryDate\", 0";
		sql += "                     , T.\"AcDate\"";
		sql += "                     , T.\"EntryDate\"";
		sql += "                ) <= :IEDAY";
		sql += "                AND";
		sql += "                Ir.\"CustNo\" IS NOT NULL";
		sql += "      ) Ir ON Ir.\"AcDate\" = T.\"AcDate\"";
		sql += "              AND";
		sql += "              Ir.\"CustNo\" = T.\"CustNo\"";
		sql += "              AND";
		sql += "              JSON_VALUE(T.\"OtherFields\", '$.FireFee') IS NOT NULL";
		sql += "      LEFT JOIN \"AcReceivable\"  Ac ON Ac.\"TitaTlrNo\" = T.\"TitaTlrNo\"";
		sql += "                                     AND";
		sql += "                                     Ac.\"TitaTxtNo\" = To_Number(";
		sql += "                                         T.\"TitaTxtNo\"";
		sql += "                                     )";
		sql += "                                     AND";
		sql += "                                     Ac.\"CustNo\" = T.\"CustNo\"";
		sql += "                                     AND";
		sql += "                                     JSON_VALUE(T.\"OtherFields\", '$.AcctFee') IS NOT NULL";
		sql += "      LEFT JOIN (";
		sql += "          SELECT DISTINCT \"CustNo\"";
		sql += "                        , \"FacmNo\"";
		sql += "          FROM \"ForeclosureFee\"";
		sql += "      ) Ff ON Ff.\"CustNo\" = T.\"CustNo\"";
		sql += "              AND";
		sql += "              JSON_VALUE(T.\"OtherFields\", '$.LawFee') IS NOT NULL";
		sql += "      LEFT JOIN \"LoanBorMain\"   L ON L.\"CustNo\" = T.\"CustNo\"";
		sql += "                                   AND";
		sql += "                                   L.\"FacmNo\" = T.\"FacmNo\"";
		sql += "                                   AND";
		sql += "                                   L.\"BormNo\" = T.\"BormNo\"";
		sql += "      WHERE T.\"CustNo\" = :ICUSTNO";
		sql += "            AND";
		sql += "            Nvl(";
		sql += "                JSON_VALUE(T.\"OtherFields\", '$.TempReasonCode'), ' '";
		sql += "            ) NOT IN (";
		sql += "                '03'";
		sql += "              , '06'";
		sql += "            )";
		sql += "            AND";
		sql += "            ( T.\"TxAmt\" <> 0";
		sql += "              OR";
		sql += "              T.\"TempAmt\" <> 0 )";

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
		sql += "      GROUP BY T.\"CustNo\"";
		sql += "             , Decode(";
		sql += "          T.\"FacmNo\", 0";
		sql += "             , Nvl(";
		sql += "              Ir.\"FacmNo\", Nvl(";
		sql += "                  Ac.\"FacmNo\", Ff.\"FacmNo\"";
		sql += "              )";
		sql += "          ), T.\"FacmNo\"";
		sql += "      )";
		sql += "             , T.\"EntryDate\"";
		sql += "             , T.\"AcDate\"";
		sql += "             , T.\"IntStartDate\"";
		sql += "             , T.\"IntEndDate\"";
		sql += "             , T.\"Rate\"";
		sql += "             , T.\"Desc\"";
		sql += "             , T.\"TxDescCode\"";
		sql += "             , T.\"AcctCode\"";
		sql += "             , T.\"TitaTxCd\"";
		sql += "             , T.\"TitaCalDy\"";
		sql += "             , T.\"TitaCalTm\"";
		sql += "             , T.\"TitaHCode\"";
		sql += "             , T.\"TitaTlrNo\"";
		sql += "             , T.\"TitaTxtNo\"";
		sql += "             , T.\"RepayCode\"";
		sql += "             , L.\"RenewFlag\"";
		sql += "  )";
		sql += "  SELECT T.\"CustNo\"";
		sql += "       , T.\"FacmNo\"";
		sql += "       , T.\"Desc\"";
		sql += "       , CASE";
		sql += "      WHEN T.\"Desc\" NOT IN (";
		sql += "          '撥款'";
		sql += "        , '契約變更'";
		sql += "        , '結案登錄'";
		sql += "      )";
		sql += "           AND";
		sql += "           T.\"RepayCode\" = 9 THEN N'暫收款'";
		sql += "      ELSE T.\"RepayItem\"";
		sql += "  END AS \"RepayItem\"";
		sql += "       , T.\"RepayCode\"";
		sql += "       , T.\"EntryDate\"";
		sql += "       , T.\"AcDate\"";
		sql += "       , T.\"Amount\"";
		sql += "       , T.\"IntStartDate\"";
		sql += "       , T.\"IntEndDate\"";
		sql += "       , T.\"Rate\"";
		sql += "       , T.\"Principal\"";
		sql += "       , T.\"Interest\"";
		sql += "       , T.\"BreachAmt\"";
		sql += "       , T.\"TxAmt\"";
		sql += "       , T.\"FeeAmt\"";
		sql += "       , CASE";
		sql += "      WHEN T.\"TitaHCode\" IN (";
		sql += "          1";
		sql += "        , 3";
		sql += "      ) THEN 0 - T.\"Excessive\"";
		sql += "      ELSE T.\"Excessive\"";
		sql += "  END AS \"Excessive\"";
		sql += "       , T.\"OverShort\"";
		sql += "       , T.\"CustName\"";
		sql += "       , CASE";
		sql += "      WHEN Cb.\"BdLocation\" IS NOT NULL THEN Cb.\"BdLocation\"";
		sql += "      ELSE Cl.\"LandLocation\"";
		sql += "  END AS \"Location\"";
		sql += "       , T.\"TitaHCode\"";
		sql += "       , T.\"TitaTlrNo\"";
		sql += "       , T.\"TitaTxtNo\"";
		sql += "  FROM (";
		sql += "      SELECT T.\"CustNo\"";
		sql += "           , Nvl(";
		sql += "          T.\"FacmNo\",(";
		sql += "              SELECT \"FacmNo\"";
		sql += "              FROM \"tmpMain\"";
		sql += "          )";
		sql += "      ) AS \"FacmNo\"";
		sql += "           , CASE";
		sql += "          WHEN T.\"TxDescCode\" = '3202' THEN '回收登錄'";
		sql += "          WHEN T.\"TxDescCode\" IN (";
		sql += "              '3420'";
		sql += "            , '3421'";
		sql += "            , '3422'";
		sql += "          ) THEN '結案登錄'";
		sql += "          WHEN T.\"TxDescCode\" IN (";
		sql += "              '3101'";
		sql += "            , '3102'";
		sql += "          ) THEN '契約變更'";
		sql += "          WHEN T.\"TxDescCode\" IN (";
		sql += "              '3100'";
		sql += "          )";
		sql += "               AND";
		sql += "               T.\"RenewFlag\" IN (";
		sql += "                   '1'";
		sql += "                 , '2'";
		sql += "               ) THEN '契約變更'";
		sql += "          WHEN T.\"TxDescCode\" IN (";
		sql += "              '3100'";
		sql += "          )";
		sql += "               AND";
		sql += "               T.\"RenewFlag\" IN (";
		sql += "                   '0'";
		sql += "               ) THEN '撥款'";
		sql += "          WHEN Cc1.\"Code\" IS NOT NULL THEN To_Char(";
		sql += "              Nvl(";
		sql += "                  Cc1.\"Item\", '  '";
		sql += "              )";
		sql += "          )";
		sql += "          WHEN Cc2.\"Code\" IS NOT NULL THEN To_Char(";
		sql += "              Nvl(";
		sql += "                  Cc2.\"Item\", '  '";
		sql += "              )";
		sql += "          )";
		sql += "          ELSE";
		sql += "              CASE";
		sql += "                  WHEN T.\"TxDescCode\" = '3100' THEN To_Char(";
		sql += "                      Nvl(";
		sql += "                          T.\"Desc\", '  '";
		sql += "                      )";
		sql += "                  )";
		sql += "                  ELSE '契約變更'";
		sql += "              END";
		sql += "      END AS \"Desc\"";
		;// 借新還舊、展期=>顯示契約變更
		sql += "           , Rpcode.\"Item\"                           AS \"RepayItem\"";
		sql += "           , T.\"EntryDate\"";
		sql += "       	   , T.\"AcDate\"";
		sql += "           , Nvl(";
		sql += "          T.\"Amount\", 0";
		sql += "      ) AS \"Amount\"";
		sql += "           , T.\"IntStartDate\"";
		sql += "           , T.\"IntEndDate\"";
		sql += "           , T.\"Rate\"";
		sql += "           , T.\"Principal\"";
		sql += "           , T.\"Interest\"";
		sql += "           , T.\"BreachAmt\"";
		sql += "           , T.\"TxAmt\"";
		sql += "           , T.\"FeeAmt\"                              AS \"FeeAmt\"";
		sql += "           , CASE";
		sql += "          WHEN T.\"TitaHCode\" IN (";
		sql += "              1";
		sql += "            , 3";
		sql += "          ) THEN 0 - T.\"Excessive\"";
		sql += "          ELSE T.\"Excessive\"";
		sql += "      END AS \"Excessive\"";
		sql += "           , T.\"ExcessiveCount\"";
		sql += "           , T.\"Excessive\" / T.\"ExcessiveCount\"      AS \"Excessive2\"";
		sql += "           , T.\"OverShort\"";
		sql += "           , \"Fn_ParseEOL\"(";
		sql += "          C.\"CustName\", 0";
		sql += "      ) AS \"CustName\"";
		sql += "           , T.\"TitaHCode\"";
		sql += "           , T.\"TitaCalDy\"";
		sql += "           , T.\"TitaCalTm\"";
		sql += "           , T.\"TitaTlrNo\"";
		sql += "           , T.\"TitaTxtNo\"";
		sql += "           , T.\"RepayCode\"";
		sql += "      FROM \"Main\"      T";
		sql += "      LEFT JOIN \"CustMain\"  C ON C.\"CustNo\" = T.\"CustNo\"";
		sql += "      LEFT JOIN \"CdCode\"    Cc1 ON Cc1.\"DefCode\" = 'AcctCode'";
		sql += "                                AND";
		sql += "                                Cc1.\"Code\" = T.\"AcctCode\"";
		sql += "                                AND";
		sql += "                                T.\"TxDescCode\" = 'Fee'";
		sql += "      LEFT JOIN \"CdCode\"    Cc2 ON Cc2.\"DefCode\" = 'TxDescCode'";
		sql += "                                AND";
		sql += "                                Cc2.\"Code\" = T.\"TxDescCode\"";
		sql += "                                AND";
		sql += "                                T.\"TxDescCode\" <> 'Fee'";
		sql += "      LEFT JOIN \"CdCode\"    Rpcode ON Rpcode.\"DefCode\" = 'BatchRepayCode'";
		sql += "                                   AND";
		sql += "                                   Rpcode.\"Code\" = T.\"RepayCode\"";
		sql += "      LEFT JOIN \"CdCode\"    Cdt ON Cdt.\"DefCode\" = 'TxDescCode'";
		sql += "                                AND";
		sql += "                                Cdt.\"Code\" = T.\"TxDescCode\"";
		sql += "      LEFT JOIN \"CdCode\"    Cdf ON T.\"TxDescCode\" = 'Fee'";
		sql += "                                AND";
		sql += "                                Cdf.\"DefCode\" = 'AcctCode'";
		sql += "                                AND";
		sql += "                                Cdf.\"Code\" = T.\"AcctCode\"";
		sql += "  ) T";
		sql += "  LEFT JOIN \"ClFac\"       F ON F.\"CustNo\" = T.\"CustNo\"";
		sql += "                         AND";
		sql += "                         F.\"FacmNo\" = T.\"FacmNo\"";
		sql += "                         AND";
		sql += "                         F.\"MainFlag\" = 'Y'";
		sql += "  LEFT JOIN \"ClBuilding\"  Cb ON Cb.\"ClCode1\" = F.\"ClCode1\"";
		sql += "                               AND";
		sql += "                               Cb.\"ClCode2\" = F.\"ClCode2\"";
		sql += "                               AND";
		sql += "                               Cb.\"ClNo\" = F.\"ClNo\"";
		sql += "                               AND";
		sql += "                               F.\"ClCode1\" = 1";
		sql += "  LEFT JOIN \"ClLand\"      Cl ON Cl.\"ClCode1\" = F.\"ClCode1\"";
		sql += "                           AND";
		sql += "                           Cl.\"ClCode2\" = F.\"ClCode2\"";
		sql += "                           AND";
		sql += "                           Cl.\"ClNo\" = F.\"ClNo\"";
		sql += "                           AND";
		sql += "                           Cl.\"LandSeq\" = 0";
		sql += "                           AND";
		sql += "                           F.\"ClCode1\" = 2";
		sql += "  ORDER BY T.\"FacmNo\"";

		if (iHFG.equals("0")) {
			if (!isCalcuExcessive) {
				sql += "         , T.\"AcDate\"";
			}
		}

		sql += "         , T.\"EntryDate\"";
		sql += "         , T.\"TitaCalDy\"";
		sql += "         , T.\"TitaCalTm\"";
		sql += "         , T.\"TitaTlrNo\"";
		sql += "         , T.\"TitaTxtNo\"";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("ICUSTNO", iCUSTNO);
		query.setParameter("ISDAY", ISDAY);
		query.setParameter("IEDAY", IEDAY);
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
		String ISDAY = String.valueOf(Integer.valueOf(titaVo.get("BeginDate")) + 19110000);
		String IEDAY = String.valueOf(Integer.valueOf(titaVo.get("EndDate")) + 19110000);
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
		sql += "            FROM \"LoanBorTx\" ";
		sql += "            WHERE \"CustNo\" = :ICUSTNO";
		sql += "             AND NVL(JSON_VALUE(\"OtherFields\",'$.TempReasonCode'),' ') NOT IN ('03','06')";// --03期票
																												// 06即期票現金
		sql += "             AND ( \"TxAmt\" <> 0";
		sql += "               OR  \"TempAmt\" <> 0)";
		sql += "             AND  NVL(\"BormNo\", 0) > 0 ";

		if (iTYPE.equals("1")) {
			sql += "          AND DECODE(\"EntryDate\", 0, \"AcDate\", \"EntryDate\") >= :ISDAY";
			sql += "          AND DECODE(\"EntryDate\", 0, \"AcDate\", \"EntryDate\") <= :IEDAY";
		} else {
			sql += "          AND \"AcDate\" >= :ISDAY";
			sql += "          AND \"AcDate\" <= :IEDAY";
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
		sql += "            FROM  \"FacMain\" F";
		sql += "            LEFT JOIN \"LoanBorMain\" M ON M.\"CustNo\" = F.\"CustNo\"";
		sql += "                                       AND M.\"FacmNo\" = F.\"FacmNo\"";
		sql += "                                       AND M.\"Status\" < 10 ";
		sql += "            LEFT JOIN \"LoanOverdue\" O ON O.\"CustNo\" = M.\"CustNo\"";
		sql += "                                       AND O.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                       AND O.\"BormNo\" = M.\"BormNo\"";
		sql += "                                       AND O.\"OvduNo\" = M.\"LastOvduNo\"";
		sql += "            WHERE F.\"CustNo\" = :ICUSTNO";
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
		sql += "              ,CASE WHEN T.\"TxAmt\" > 0  THEN 0 ELSE 1 END  ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("icustno", iCUSTNO);
		query.setParameter("ISDAY", ISDAY);
		query.setParameter("IEDAY", IEDAY);
		return this.convertToMap(query);
	}

}