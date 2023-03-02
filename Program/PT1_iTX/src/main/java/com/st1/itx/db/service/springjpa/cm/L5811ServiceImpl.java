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

@Service("l5811ServiceImpl")
@Repository
public class L5811ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> doQuery(int iYYYYMM, int iCustNo, String iAcctCode, int iStartMonth, int iEndMonth,
			TitaVo titaVo) throws Exception {

		String sql = " ";
		sql += "SELECT YC.R1                              AS F0  "; // r1 借戶姓名空白
		sql += "     , YC.R2                              AS F1  "; // r2 統一編號空白
		sql += "     , YC.R3                              AS F2  "; // r3 貸款帳號空白
		sql += "     , YC.R4                              AS F3  "; // r4 最初貸款金額為０
		sql += "     , YC.R5                              AS F4  "; // r5 最初貸款金額＞核准額度
		sql += "     , YC.R6                              AS F5  "; // r6 最初貸款金額＜放款餘額
		sql += "     , YC.R7                              AS F6  "; // r7 貸款起日空白
		sql += "     , YC.R8                              AS F7  "; // r8 貸款訖日空白
		sql += "     , YC.R10                             AS F8  "; // r10 繳息所屬年月空白
		sql += "     , YC.R11                             AS F9  "; // r11 繳息金額為 0
		sql += "     , YC.R12                             AS F10 "; // r12 科子細目代號暨說明空白
		sql += "     , YC.C1                              AS F11 "; // c1 一額度一撥款
		sql += "     , YC.C2                              AS F12 "; // c2 一額度多撥款
		sql += "     , YC.C3                              AS F13 "; // c3 多額度多撥款
		sql += "     , YC.C4                              AS F14 "; // c4 借新還舊件
		sql += "     , YC.C5                              AS F15 "; // c5 清償件
		sql += "     , NVL(CA.\"Zip3\",' ')               AS F16 "; // CK01 郵遞區號
		sql += "     , Y.\"CustNo\"                       AS F17 "; // CK02 戶號
		sql += "     , Y.\"FacmNo\"                       AS F18 "; // CK04 額度
		sql += "     , NULL                               AS F19 "; // CK05 聯絡人姓名 ???
		sql += "     ,  NVL(C.\"CustName\",'')              AS F20  "; // CK06 戶名 -同借戶姓名
		sql += "     , NVL(JSON_VALUE(Y.\"JsonFields\",  '$.BdLoacation'), NVL(CB.\"BdLocation\",'') ) ";
		sql += "               AS F21  "; // 地址
		sql += "     , NVL(C.\"CustName\",'')             AS F22 "; // 借戶姓名
		sql += "     , NVL(C.\"CustId\",'')               AS F23 "; // 統一編號
		sql += "     , Y.\"LoanAmt\"                      AS F24 "; // 初貸金額
		sql += "     , Y.\"FirstDrawdownDate\" - 19110000 AS F25 "; // 貸款起日
		sql += "      ,Y.\"MaturityDate\" - 19110000      AS F26 "; // 貸款迄日
		sql += "     , Y.\"LoanBal\"                      AS F27 "; // 本期未償還本金額
		sql += "     , CASE ";
		sql += "         WHEN TRUNC(Y.\"FirstDrawdownDate\" / 100 ) < :iYYYYMM "; // 2022-03-18 智偉修改
		sql += "         THEN (TRUNC( :iYYYYMM / 100) - 1911) * 100 + 01 "; // 首撥日小於查詢區間起月時放查詢區間起月
		sql += "         ELSE TRUNC(Y.\"FirstDrawdownDate\" / 100 ) - 191100 "; // 否則放首撥日月份
		sql += "       END                                AS F28 "; // 繳息所屬年月(起)
		sql += "     , Y.\"YearMonth\" - 191100           AS F29 "; // 繳息所屬年月(止)
		sql += "     , Y.\"YearlyInt\"                    AS F30 "; // 繳息金額
		sql += "     , Y.\"AcctCode\"                     AS F31 "; // 科子細目代號暨說明 ???
		sql += "     , LPAD(Y.\"UsageCode\",2,'0')        AS F32 "; // 資金用途別
		sql += "     , NVL(C.\"CustUKey\",'')             AS F33 "; // 客戶識別碼

		sql += " FROM \"YearlyHouseLoanInt\" Y   ";
		sql += " LEFT JOIN \"YearlyHouseLoanIntCheck\" YC ON YC.\"YearMonth\" = Y.\"YearMonth\" ";
		sql += "                                         AND YC.\"CustNo\" = Y.\"CustNo\" ";
		sql += "                                         AND YC.\"FacmNo\" = Y.\"FacmNo\" ";
		sql += "                                         AND YC.\"UsageCode\" = Y.\"UsageCode\" ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" =  Y.\"CustNo\" ";
		sql += " LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = Y.\"CustNo\" ";
		sql += "                        AND F.\"FacmNo\" = Y.\"FacmNo\" ";
		sql += " LEFT JOIN \"ClFac\" CL "; // 擔保品與額度關聯檔
		sql += "  ON CL.\"CustNo\" = Y.\"CustNo\" ";
		sql += " AND CL.\"FacmNo\" = Y.\"FacmNo\" ";
		sql += " AND CL.\"MainFlag\"    = 'Y' "; // 主要擔保品
		sql += " LEFT JOIN \"ClBuilding\" CB "; // 擔保品不動產建物檔
		sql += "  ON CB.\"ClCode1\" = CL.\"ClCode1\" ";
		sql += " AND CB.\"ClCode2\" = CL.\"ClCode2\" ";
		sql += " AND CB.\"ClNo\"    = CL.\"ClNo\" ";
		sql += " LEFT JOIN \"CdArea\" CA "; // 縣市與鄉鎮區對照檔
		sql += "  ON CA.\"CityCode\" = CB.\"CityCode\" ";
		sql += " AND CA.\"AreaCode\" = CB.\"AreaCode\" ";
		sql += " WHERE Y.\"YearMonth\" = :iYYYYMM ";
		sql += " AND C.\"CuscCd\" = 1 "; // 自然人,2023/3/2調整
		//sql += "       WHEN C.\"CuscCd\" = 1 ";
		//sql += "       THEN 1 ";
		//sql += "       WHEN CL.\"ClCode1\" = 1 ";
		//sql += "       THEN 1";
		//sql += "     ELSE 0 END = 1 ";
		sql += " AND CASE "; //須有房地擔保品故建號需有值,2023/3/2調整
		sql += "       WHEN  CB.\"BdNo1\" > 0  THEN 1";
		sql += "       WHEN  CB.\"BdNo2\" > 0  THEN 1";
		sql += "     ELSE 0 END = 1";
		
		sql += " AND CASE "; // L5810連動有值時，依值篩選
		sql += "       WHEN :CustNo != 0";
		sql += "            AND Y.\"CustNo\" = :CustNo ";
		sql += "       THEN 1 ";
		sql += "       WHEN :CustNo != 0";
		sql += "       THEN 0 ";
		sql += "     ELSE 1 END = 1 ";
		sql += " AND CASE "; // L5810連動有值時，依值篩選
		sql += "       WHEN NVL(:AcctCode,' ') != ' '";
		sql += "            AND Y.\"AcctCode\" = :AcctCode ";
		sql += "       THEN 1 ";
		sql += "       WHEN NVL(:AcctCode,' ') = ' '";
		sql += "       THEN 1 ";
		sql += "     ELSE 0 END = 1 ";
		sql += " AND CASE "; // 2023/3/2調整
		sql += "       WHEN NVL(JSON_VALUE(Y.\"JsonFields\", '$.StartMonth'),0) = :StartMonth THEN 1 ";
		sql += "       WHEN :StartMonth = 0 AND NVL(JSON_VALUE(Y.\"JsonFields\", '$.StartMonth'),0) = TRUNC(:iYYYYMM / 100) * 100 + 01 THEN 1 ";
		sql += "     ELSE 0 END = 1 ";

		sql += " AND CASE "; // 2023/3/2調整
		sql += "       WHEN NVL(JSON_VALUE(Y.\"JsonFields\", '$.EndMonth'),0) = :EndMonth THEN 1";
		sql += "       WHEN :EndMonth = 0 AND NVL(JSON_VALUE(Y.\"JsonFields\", '$.EndMonth'),0) = TRUNC(:iYYYYMM / 100) * 100 + 12 THEN 1";
		sql += "     ELSE 0 END = 1 ";

		sql += " ORDER BY Y.\"CustNo\"  ";
		sql += "        , Y.\"FacmNo\"";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iYYYYMM", iYYYYMM);
		query.setParameter("CustNo", iCustNo);
		query.setParameter("AcctCode", iAcctCode);
		query.setParameter("StartMonth", iStartMonth);
		query.setParameter("EndMonth", iEndMonth);
		return this.convertToMap(query);
	}
	
	public List<Map<String, String>> queryCustTelNo(String custukey,  TitaVo titaVo) throws Exception {

		this.info("queryCustTelNo , custukey = " + custukey );

		if (custukey == null) {
			this.info("custukey == null return.");
			return null;
		}

		String sql = "　";
		sql += " SELECT NVL(CM.\"CustId\", '')     AS \"CustId\" ";
		sql += "      , NVL(CT.\"LiaisonName\",CM.\"CustName\")   AS \"ContactName\" ";
		sql += "      , ROW_NUMBER() ";
		sql += "        OVER ( ";
		sql += "          PARTITION BY ";
		sql += "              CT.\"CustUKey\" ";
		sql += "            , CT.\"TelTypeCode\" ";
		sql += "          ORDER BY ";
	    sql += "              NVL(CT.\"CustUKey\",CM.\"CustId\") ";
	    sql += "            , NVL(CT.\"TelTypeCode\",CM.\"CustId\") ";
	    sql += "        ) AS \"Seq\" ";
		sql += " FROM  \"CustTelNo\" CT ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustUKey\" = CT.\"CustUKey\" ";

		sql += " WHERE CT.\"CustUKey\" = :custukey ";
		sql += " ORDER BY \"Seq\" ";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("custukey", custukey);

		return this.convertToMap(query);
	}

}