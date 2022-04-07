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
	 * @param titaVo titaVo
	 * @return 查詢結果
	 * @throws Exception
	 */
	public List<Map<String, String>> doQuery1(TitaVo titaVo) throws Exception {

		String iCUSTNO = titaVo.getParam("CustNo");
		String iTYPE = titaVo.getParam("DateType");
		String iSDAY = String.valueOf(Integer.valueOf(titaVo.getParam("BeginDate")) + 19110000);
		String iEDAY = String.valueOf(Integer.valueOf(titaVo.getParam("EndDate")) + 19110000);
		String iHFG = titaVo.getParam("CorrectType");

		String sql = "SELECT T.\"F0\"";
		sql += "            ,T.\"F1\"";
		sql += "            ,T.\"F2\"";
		sql += "            ,T.\"F3\"";
		sql += "            ,T.\"F4\"";
		sql += "            ,T.\"F5\"";
		sql += "            ,T.\"F6\"";
		sql += "            ,T.\"F7\"";
		sql += "            ,T.\"F8\"";
		sql += "            ,T.\"F5\" + T.\"F6\" + T.\"F7\" + T.\"F8\" AS F9";
		sql += "            ,T.\"F10\"";
		sql += "            ,T.\"CustNo\" F11";
		sql += "            ,C.\"CustName\" F12";
		sql += "            ,T.\"FacmNo\" F13";
		sql += "            ,NVL(NVL(CB.\"BdLocation\", CL.\"LandLocation\"),' ') F14";
		sql += "            ,T.\"F15\"";
		sql += "      FROM (SELECT DECODE(T.\"EntryDate\", 0, T.\"AcDate\" ,T.\"EntryDate\") F0";
		sql += "                  ,T.\"LoanBal\" + T.\"Principal\" F1";
		sql += "                  ,T.\"IntStartDate\" F2 ";
		sql += "                  ,T.\"IntEndDate\" F3";
		sql += "                  ,T.\"Rate\" F4";
		sql += "                  ,T.\"Interest\" F5";
		sql += "                  ,T.\"DelayInt\" F6";
		sql += "                  ,T.\"BreachAmt\" F7";
		sql += "                  ,T.\"Principal\" F8";
		sql += "                  ,T.\"DueDate\" F10";
		sql += "                  ,T.\"CustNo\"";
		sql += "                  ,T.\"FacmNo\"";
		sql += "                  ,1 F15";
		sql += "            FROM \"LoanBorTx\" T";
		sql += "            WHERE T.\"CustNo\" = :icustno";
		sql += "             AND T.\"Principal\" + T.\"Interest\" > 0";

		if (iTYPE.equals("1")) {
			sql += "          AND DECODE(T.\"EntryDate\", 0, T.\"AcDate\", T.\"EntryDate\") >= :isday";
			sql += "          AND DECODE(T.\"EntryDate\", 0, T.\"AcDate\", T.\"EntryDate\") <= :ieday";
		} else {
			sql += "          AND T.\"AcDate\" >= :isday";
			sql += "          AND T.\"AcDate\" <= :ieday";
		}

		if (iHFG.equals("0")) {
			sql += "          AND T.\"TitaHCode\" = 0";
		}
		sql += "            UNION ALL";
		sql += "            SELECT \"F0\"";
		sql += "                    ,SUM(F1) F1";
		sql += "                    ,0 F2";
		sql += "                    ,0 F3";
		sql += "                    ,0 F4";
		sql += "                    ,0 F5";
		sql += "                    ,0 F6";
		sql += "                    ,0 F7";
		sql += "                    ,0 F8";
		sql += "                    ,0 F10";
		sql += "                    ,\"CustNo\"";
		sql += "                    ,\"FacmNo\"";
		sql += "                    ,2 F15";
		sql += "            FROM (SELECT F.\"CustNo\"";
		sql += "                        ,F.\"FacmNo\"";
		sql += "                        ,NVL(M.\"MaturityDate\", 99999999) F0";
		sql += "                        ,CASE WHEN M.\"Status\" IN (2, 7) THEN O.\"OvduBal\"";
		sql += "                              WHEN M.\"Status\"  =  0     THEN M.\"LoanBal\"";
		sql += "                         ELSE 0 END F1";
		sql += "                  FROM  \"FacMain\" F";
		sql += "                  LEFT JOIN \"LoanBorMain\" M ON M.\"CustNo\" = F.\"CustNo\"";
		sql += "                                             AND M.\"FacmNo\" = F.\"FacmNo\"";
		sql += "                                             AND M.\"Status\" IN (0, 2, 7)";
		sql += "                  LEFT JOIN \"LoanOverdue\" O ON O.\"CustNo\" = M.\"CustNo\"";
		sql += "                                             AND O.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                             AND O.\"BormNo\" = M.\"BormNo\"";
		sql += "                                             AND O.\"OvduNo\" = M.\"LastOvduNo\"";
		sql += "                  WHERE F.\"CustNo\" = :icustno";
		sql += "                 )";
		sql += "            GROUP BY \"CustNo\"";
		sql += "                    ,\"FacmNo\"";
		sql += "                   , \"F0\"";
		sql += "           ) T";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = T.\"CustNo\"";
		sql += "      LEFT JOIN \"ClFac\" F ON F.\"CustNo\" = T.\"CustNo\"";
		sql += "                           AND F.\"FacmNo\" = T.\"FacmNo\"";
		sql += "                           AND F.\"MainFlag\" = 'Y'";
		sql += "      LEFT JOIN \"ClBuilding\" CB ON CB.\"ClCode1\" = F.\"ClCode1\"";
		sql += "                                 AND CB.\"ClCode2\" = F.\"ClCode2\"";
		sql += "                                 AND CB.\"ClNo\"    = F.\"ClNo\"";
		sql += "                                 AND F.\"ClCode1\" = 1 ";
		sql += "      LEFT JOIN \"ClLand\" CL ON CL.\"ClCode1\" = F.\"ClCode1\"";
		sql += "                             AND CL.\"ClCode2\" = F.\"ClCode2\"";
		sql += "                             AND CL.\"ClNo\"    = F.\"ClNo\"";
		sql += "                             AND F.\"ClCode1\" = 2 ";
		sql += "      ORDER BY T.\"FacmNo\",T.\"F15\" , T.\"F0\"";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("icustno", iCUSTNO);
		query.setParameter("isday", iSDAY);
		query.setParameter("ieday", iEDAY);
		return this.convertToMap(query);
	}

	public List<Map<String, String>> doQuery2(TitaVo titaVo) throws Exception {

		String iCUSTNO = titaVo.getParam("CustNo");
		String iTYPE = titaVo.getParam("DateType");
		String iSDAY = String.valueOf(Integer.valueOf(titaVo.getParam("BeginDate")) + 19110000);
		String iEDAY = String.valueOf(Integer.valueOf(titaVo.getParam("EndDate")) + 19110000);

		String sql = "SELECT D.\"EntryDate\"";
		sql += "            ,D.\"AcctItem\"";
		sql += "            ,D.\"TxAmt\"";
		sql += "            ,D.\"FacmNo\"";
		sql += "            ,C.\"CustName\"";
		sql += "      FROM (SELECT  D.\"EntryDate\"";
		sql += "                   ,CA.\"AcctItem\" AS \"AcctItem\"";
		sql += "                   ,SUM(\"TxAmt\")  AS \"TxAmt\"";
		sql += "                   ,D.\"FacmNo\"";
		sql += "            FROM (SELECT  DECODE(T.\"EntryDate\", 0, T.\"AcDate\", T.\"EntryDate\") AS \"EntryDate\"";
		sql += "                         ,A.\"AcctCode\"";
		sql += "                         ,DECODE(A.\"DbCr\", 'C', A.\"TxAmt\", - A.\"TxAmt\")       AS \"TxAmt\"";
		sql += "                         ,T.\"FacmNo\"                                              AS \"FacmNo\"";
		sql += "                  FROM \"LoanBorTx\" T";
		sql += "                  LEFT JOIN \"AcDetail\" A ON A.\"AcDate\"    = T.\"AcDate\"";
		sql += "                                          AND A.\"TitaTlrNo\" = T.\"TitaTlrNo\"";
		sql += "                                          AND A.\"TitaTxtNo\" = T.\"TitaTxtNo\"";
		sql += "                  WHERE T.\"CustNo\" = :icustno";

		if (iTYPE.equals("1")) {
			sql += "                AND DECODE(T.\"EntryDate\", 0, T.\"AcDate\", T.\"EntryDate\") >= :isday";
			sql += "                AND DECODE(T.\"EntryDate\", 0, T.\"AcDate\", T.\"EntryDate\") <= :ieday ";
		} else {
			sql += "                AND T.\"AcDate\" >= :isday";
			sql += "                AND T.\"AcDate\" <= :ieday ";
		}

		sql += "                    AND T.\"TitaHCode\" = 0";
		sql += "                    AND (A.\"AcctCode\" =  'TMI' OR A.\"AcctCode\" LIKE 'F%')";
		sql += "                 ) D";
		sql += "            LEFT JOIN \"CdAcCode\" CA ON CA.\"AcctCode\" = D.\"AcctCode\"";
		sql += "            GROUP BY D.\"EntryDate\"";
		sql += "                    ,CA.\"AcctItem\"";
		sql += "                    ,D.\"FacmNo\"";
		sql += "            UNION ALL";
		sql += "            SELECT 99999999                   AS \"EntryDate\"";
		sql += "                  ,CAST(' ' AS NVARCHAR2(20)) AS \"AcctItem\"";
		sql += "                  ,0                          AS \"TxAmt\"";
		sql += "                  ,F.\"FacmNo\"               AS \"FacmNo\"";
		sql += "            FROM \"FacMain\" F";
		sql += "            WHERE F.\"CustNo\" = :icustno ";
		sql += "           ) D";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = :icustno";
		sql += "      ORDER BY D.\"FacmNo\", D.\"EntryDate\"";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("icustno", iCUSTNO);
		query.setParameter("isday", iSDAY);
		query.setParameter("ieday", iEDAY);
		return this.convertToMap(query);
	}

	public List<Map<String, String>> doQuery3(TitaVo titaVo) throws Exception {

		String iCUSTNO = titaVo.getParam("CustNo");
		String iTYPE = titaVo.getParam("DateType");
		String iSDAY = String.valueOf(Integer.valueOf(titaVo.getParam("BeginDate")) + 19110000);
		String iEDAY = String.valueOf(Integer.valueOf(titaVo.getParam("EndDate")) + 19110000);
		String iHFG = titaVo.getParam("CorrectType");

		String sql = " ";
		sql += " SELECT TX.\"CustNo\" ";
		sql += "      , \"Fn_ParseEOL\"(CM.\"CustName\",0) AS \"CustName\" ";
		sql += "      , LPAD(TX.\"FacmNo\", 3, '0') AS \"FacmNo\" ";
		sql += "      , LPAD(TX.\"BormNo\", 3, '0') AS \"BormNo\" ";
		sql += "      , TX.\"EntryDate\" ";
		sql += "      , TX.\"Desc\" ";
		sql += "      , TX.\"TitaHCode\" ";
		sql += "      , \"Fn_GetCdCode\"('TitaHCode', TX.\"TitaHCode\") AS \"HItem\" ";
		sql += "      , TX.\"TxAmt\" ";
		sql += "      , TX.\"IntStartDate\" ";
		sql += "      , TX.\"IntEndDate\" ";
		sql += "      , TX.\"Principal\" AS \"Principal\" ";
		sql += "      , TX.\"Interest\" ";
		sql += "      , TX.\"DelayInt\" ";
		sql += "      , TX.\"BreachAmt\" + TX.\"CloseBreachAmt\" AS \"BreachAmt\" ";
		sql += "      , TX.\"TempAmt\" ";
		sql += " FROM \"LoanBorTx\" TX ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = TX.\"CustNo\" ";
		sql += " WHERE TX.\"CustNo\" = :inputCustNo ";
		sql += "   AND CASE ";
		sql += "         WHEN :inputType = 2 ";
		sql += "         THEN TX.\"AcDate\" ";
		sql += "       ELSE TX.\"EntryDate\" ";
		sql += "       END >= :inputStartDate ";
		sql += "   AND CASE ";
		sql += "         WHEN :inputType = 2 ";
		sql += "         THEN TX.\"AcDate\" ";
		sql += "       ELSE TX.\"EntryDate\" ";
		sql += "       END <= :inputEndDate ";
		sql += "   AND CASE ";
		sql += "         WHEN :inputHCode = 9 AND  TX.\"TitaHCode\" IN (0,1,2,3,4) ";
		sql += "         THEN 1 ";
		sql += "         WHEN TX.\"TitaHCode\" = 0 ";
		sql += "         THEN 1 ";
		sql += "       ELSE 0 END = 1 ";
		sql += " ORDER BY TX.\"TitaCalDy\" DESC ";
		sql += "        , TX.\"TitaCalTm\" DESC ";
		sql += "        , TX.\"TitaTxtNo\" DESC ";
		sql += "        , TX.\"BorxNo\" DESC ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputCustNo", iCUSTNO);
		query.setParameter("inputType", iTYPE);
		query.setParameter("inputStartDate", iSDAY);
		query.setParameter("inputEndDate", iEDAY);
		query.setParameter("inputHCode", iHFG);
		return this.convertToMap(query);
	}

}