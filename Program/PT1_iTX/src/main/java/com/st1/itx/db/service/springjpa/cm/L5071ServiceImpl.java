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
/* 債權案件明細查詢 */
public class L5071ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;
	private Query query;
	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * L5071查詢用，須帶入折返相關參數
	 * 
	 * @param index  折返第幾次
	 * @param limit  每次明細筆數
	 * @param titaVo titaVo
	 * @return 查詢結果
	 * @throws Exception Exception
	 */

	public List<Map<String, String>> findAll( int index, int limit,TitaVo titaVo) throws Exception {
		this.info("L5071ServiceImpl.findAll ");
		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));

		String sql = " ";

		sql += " WITH AllData AS ( "; 
		sql += "   SELECT ";  
		sql += "        CASE WHEN NVL(C.\"CustId\",' ') = ' ' THEN  N.\"NegCustId\"      "; // -- F0 身分證字號
		sql += "             ELSE C.\"CustId\"  END  AS   \"CustId\"                     "; // 
		sql += "      , N.\"CaseKindCode\"                                               "; // -- F1 案件種類
		sql += "      , N.\"CustLoanKind\"                                               "; // -- F2 債權戶別
		sql += "      , N.\"Status\"                                                     "; // -- F3 債權戶況
		sql += "      , N.\"CustNo\"                                                     "; // -- F4 戶號
		sql += "      , N.\"CaseSeq\"                                                    "; // -- F5 案件序號
		sql += "      , N.\"ApplDate\"                                                   "; // -- F6 協商申請日
		sql += "      , N.\"DueAmt\"                                                     "; // -- F7 期款
		sql += "      , N.\"TotalPeriod\"                                                "; // -- F8 期數
		sql += "      , N.\"IntRate\"                                                    "; // -- F9 利率
		sql += "      , N.\"FirstDueDate\"                                               "; // -- F10 首次應繳日
		sql += "      , N.\"LastDueDate\"                                                "; // -- F11 還款結束日
		sql += "      , N.\"IsMainFin\"                                                  "; // -- F12 最大債權
		sql += "      , N.\"MainFinCode\"                                                "; // -- F13 最大債權機構
		sql += "      , N.\"TotalContrAmt\"                                              "; // -- F14 總金額
		sql += "      , N.\"NegCustId\"                                                  "; // -- F15 保證人保貸戶ID
		sql += "      , N.\"NegCustName\"                                                "; // -- F16 保證人保貸戶戶名

		sql += " FROM \"NegMain\" N";
		sql += "   LEFT JOIN \"CustMain\" C";
		sql += "          ON C.\"CustNo\" = N.\"CustNo\"";

		if (!"".equals(titaVo.getParam("CustId").trim())) {
			sql += "         AND C.\"CustId\" = :CustId";
		}

		sql += "  WHERE \"CustId\" IS NOT NULL";
		if (!"".equals(titaVo.getParam("CustId").trim())) {
			sql += "      AND \"CustId\" = :CustId";
		}

		if (iCustNo != 0) {
			sql += "   AND N.\"CustNo\" = :CustNo";
		}

		if (!"".equals(titaVo.getParam("CaseKindCode").trim())) {
			sql += "   AND N.\"CaseKindCode\" = :CaseKindCode";
		}

		if (!"".equals(titaVo.getParam("CustLoanKind").trim())) {
			sql += "     AND N.\"CustLoanKind\" = :CustLoanKind";
		}

		if (!"".equals(titaVo.getParam("Status").trim())) {
			sql += "     AND N.\"Status\" = :Status";
		}
		
		sql += "   ORDER BY  \"CustId\", N.\"CaseSeq\" ";
		sql += "  )        "; 

		sql += " , COUNTSEQ AS ( "; 
		sql += "       SELECT  DISTINCT \"CustNo\" , \"CaseSeq\"   "; 
		sql += "       FROM AllData    "; 
		sql += "  )        "; 
		sql += " , COUNTTRANS AS ( "; //計算各戶號各序號的交易筆數
		sql += "       SELECT A.\"CustNo\" , A.\"CaseSeq\" ,      "; 
		sql += "              SUM( CASE WHEN NVL(N.\"CustNo\",0) = 0 THEN 0     "; 
		sql += "                        ELSE 1  END   ) AS \"TransCount\" "; 
		sql += "       FROM COUNTSEQ A      "; 
		sql += "       LEFT JOIN \"NegTrans\" N ON N.\"CustNo\" = A.\"CustNo\"      "; 
		sql += "                             AND N.\"CaseSeq\" = A.\"CaseSeq\"      "; 
		sql += "       GROUP BY A.\"CustNo\" , A.\"CaseSeq\"      "; 
		sql += "  )        "; 
		sql += " , COUNTAPPR01 AS ( "; //計算各戶號各序號的撥付筆數
		sql += "       SELECT A.\"CustNo\" , A.\"CaseSeq\" ,      "; 
		sql += "              SUM( CASE WHEN NVL(N.\"CustNo\",0) = 0 THEN 0     "; 
		sql += "                        ELSE 1  END   ) AS \"Appr01Count\" "; 
		sql += "       FROM COUNTSEQ A      "; 
		sql += "       LEFT JOIN \"NegAppr01\" N ON N.\"CustNo\" = A.\"CustNo\"      "; 
		sql += "                                AND N.\"CaseSeq\" = A.\"CaseSeq\"      "; 
		sql += "       GROUP BY A.\"CustNo\" , A.\"CaseSeq\"      "; 
		sql += "  )        "; 

		sql += " SELECT ";  
		sql += "          A.* ";  
		sql += "        , 0    AS   \"CaseCount\"                                  "; // -- F17 案件種類筆數
		sql += "        , CT.\"TransCount\"                                        "; // -- F18 交易筆數
		sql += "        , CA.\"Appr01Count\"                                       "; // -- F19 撥付筆數
		sql += " FROM  AllData  A";
		sql += "   LEFT JOIN COUNTTRANS CT ON CT.\"CustNo\" = A.\"CustNo\"  ";
		sql += "                          AND CT.\"CaseSeq\" = A.\"CaseSeq\"    ";
		sql += "   LEFT JOIN COUNTAPPR01 CA ON CA.\"CustNo\" = A.\"CustNo\"  ";
		sql += "                           AND CA.\"CaseSeq\" = A.\"CaseSeq\"    ";

		sql += "  WHERE A.\"CustId\" IS NOT NULL   ";
		
		sql += "   ORDER BY  A.\"CustId\", A.\"CaseSeq\" ";

		
		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		if (iCustNo != 0) {
			query.setParameter("CustNo", titaVo.getParam("CustNo").trim());
		}
		if (!"".equals(titaVo.getParam("CustId").trim())) {
			query.setParameter("CustId", titaVo.getParam("CustId").trim());
		}
		if (!"".equals(titaVo.getParam("CaseKindCode").trim())) {
			query.setParameter("CaseKindCode", titaVo.getParam("CaseKindCode").trim());
		}
		if (!"".equals(titaVo.getParam("CustLoanKind").trim())) {
			query.setParameter("CustLoanKind", titaVo.getParam("CustLoanKind").trim());
		}
		if (!"".equals(titaVo.getParam("Status").trim())) {
			query.setParameter("Status", titaVo.getParam("Status").trim());
		}
		return switchback(query);
	}
	
	public List<Map<String, String>> findType1( int index, int limit,TitaVo titaVo) throws Exception {
		this.info("L5071ServiceImpl.findType1 ");
		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));

		String sql = " ";

		sql += " WITH AllData AS ( "; 
		sql += "   SELECT  N.\"CustNo\"         "; 
		sql += "         , N.\"CaseSeq\"   "; 
		sql += "         , N.\"CaseKindCode\"   "; 
		sql += "         , N.\"CustLoanKind\"    "; 
		sql += "         , N.\"Status\"         "; 
		sql += "         , CASE WHEN NVL(C.\"CustId\",' ') = ' ' THEN  N.\"NegCustId\"   "; // 身分證字號
		sql += "             ELSE C.\"CustId\"  END  AS   \"CustId\"                     "; // 
		sql += "         , ROW_NUMBER()       "; 
		sql += "           OVER (     "; 
		sql += "             PARTITION BY  N.\"CustNo\" , N.\"CaseKindCode\" "; 
		sql += "             ORDER BY  N.\"CaseSeq\" DESC"; 
		sql += "                )  AS \"Seq\"    "; 
		sql += "   FROM \"NegMain\" N";
		sql += "   LEFT JOIN \"CustMain\" C";
		sql += "          ON C.\"CustNo\" = N.\"CustNo\"";
		if (!"".equals(titaVo.getParam("CustId").trim())) {
			sql += "         AND C.\"CustId\" = :CustId";
		}

		sql += "  WHERE \"CustId\" IS NOT NULL";
		if (!"".equals(titaVo.getParam("CustId").trim())) {
			sql += "      AND \"CustId\" = :CustId";
		}

		if (iCustNo != 0) {
			sql += "     AND N.\"CustNo\" = :CustNo";
		}

		if (!"".equals(titaVo.getParam("CaseKindCode").trim())) {
			sql += "     AND N.\"CaseKindCode\" = :CaseKindCode";
		}

		if (!"".equals(titaVo.getParam("CustLoanKind").trim())) {
			sql += "     AND N.\"CustLoanKind\" = :CustLoanKind";
		}

		if (!"".equals(titaVo.getParam("Status").trim())) {
			sql += "     AND N.\"Status\" = :Status";
		}else {
			sql += "     AND N.\"Status\" IN ('0','2','3')";
		}
		sql += " ) "; 
		
		sql += " , COUNTCASE AS ( "; 
		sql += "       SELECT  DISTINCT \"CustNo\" , \"CaseKindCode\"   "; 
		sql += "       FROM AllData    "; 
		sql += "  )        "; 
		sql += " , COUNTDATA AS (        "; //計算各戶號各案件種類筆數
		sql += "       SELECT C.\"CustNo\" , C.\"CaseKindCode\" ,SUM(1) AS \"CaseCount\"   "; 
		sql += "       FROM COUNTCASE C   "; 
		sql += "       LEFT JOIN  \"NegMain\" N ON N.\"CustNo\" = C.\"CustNo\"   "; 
		sql += "                               AND N.\"CaseKindCode\" = C.\"CaseKindCode\" "; 
		sql += "       GROUP BY   C.\"CustNo\" , C.\"CaseKindCode\"   "; 
		sql += "  )        "; 

		sql += " , COUNTSEQ AS ( "; 
		sql += "       SELECT  DISTINCT \"CustNo\" , \"CaseSeq\"   "; 
		sql += "       FROM AllData    "; 
		sql += "  )        "; 
		sql += " , COUNTTRANS AS ( "; //計算各戶號各序號的交易筆數
		sql += "       SELECT A.\"CustNo\" , A.\"CaseSeq\" ,      "; 
		sql += "              SUM( CASE WHEN NVL(N.\"CustNo\",0) = 0 THEN 0     "; 
		sql += "                        ELSE 1  END   ) AS \"TransCount\" "; 
		sql += "       FROM COUNTSEQ A      "; 
		sql += "       LEFT JOIN \"NegTrans\" N ON N.\"CustNo\" = A.\"CustNo\"      "; 
		sql += "                             AND N.\"CaseSeq\" = A.\"CaseSeq\"      "; 
		sql += "       GROUP BY A.\"CustNo\" , A.\"CaseSeq\"      "; 
		sql += "  )        "; 
		sql += " , COUNTAPPR01 AS ( "; //計算各戶號各序號的撥付筆數
		sql += "       SELECT A.\"CustNo\" , A.\"CaseSeq\" ,      "; 
		sql += "              SUM( CASE WHEN NVL(N.\"CustNo\",0) = 0 THEN 0     "; 
		sql += "                        ELSE 1  END   ) AS \"Appr01Count\" "; 
		sql += "       FROM COUNTSEQ A      "; 
		sql += "       LEFT JOIN \"NegAppr01\" N ON N.\"CustNo\" = A.\"CustNo\"      "; 
		sql += "                                AND N.\"CaseSeq\" = A.\"CaseSeq\"      "; 
		sql += "       GROUP BY A.\"CustNo\" , A.\"CaseSeq\"      "; 
		sql += "  )        "; 
		
		sql += " SELECT "; 
		sql += "        A.\"CustId\"                                                     "; // -- F0 身分證字號 
		sql += "      , N.\"CaseKindCode\"                                               "; // -- F1 案件種類
		sql += "      , N.\"CustLoanKind\"                                               "; // -- F2 債權戶別
		sql += "      , N.\"Status\"                                                     "; // -- F3 債權戶況
		sql += "      , N.\"CustNo\"                                                     "; // -- F4 戶號
		sql += "      , N.\"CaseSeq\"                                                    "; // -- F5 案件序號
		sql += "      , N.\"ApplDate\"                                                   "; // -- F6 協商申請日
		sql += "      , N.\"DueAmt\"                                                     "; // -- F7 期款
		sql += "      , N.\"TotalPeriod\"                                                "; // -- F8 期數
		sql += "      , N.\"IntRate\"                                                    "; // -- F9 利率
		sql += "      , N.\"FirstDueDate\"                                               "; // -- F10 首次應繳日
		sql += "      , N.\"LastDueDate\"                                                "; // -- F11 還款結束日
		sql += "      , N.\"IsMainFin\"                                                  "; // -- F12 最大債權
		sql += "      , N.\"MainFinCode\"                                                "; // -- F13 最大債權機構
		sql += "      , N.\"TotalContrAmt\"                                              "; // -- F14 總金額
		sql += "      , N.\"NegCustId\"                                                  "; // -- F15 保證人保貸戶ID
		sql += "      , N.\"NegCustName\"                                                "; // -- F16 保證人保貸戶戶名
		sql += "      , C.\"CaseCount\"                                                  "; // -- F17 案件種類筆數
		sql += "      , CT.\"TransCount\"                                                "; // -- F18 交易筆數
		sql += "      , CA.\"Appr01Count\"                                               "; // -- F19 撥付筆數

		sql += " FROM  AllData  A";
		sql += "   LEFT JOIN \"NegMain\" N  ON N.\"CustNo\" =  A.\"CustNo\"   ";
		sql += "                           AND N.\"CaseSeq\" = A.\"CaseSeq\"   ";
		sql += "   LEFT JOIN COUNTDATA C ON C.\"CustNo\" = A.\"CustNo\"  ";
		sql += "                        AND C.\"CaseKindCode\" = A.\"CaseKindCode\"    ";
		sql += "   LEFT JOIN COUNTTRANS CT ON CT.\"CustNo\" = A.\"CustNo\"  ";
		sql += "                          AND CT.\"CaseSeq\" = A.\"CaseSeq\"    ";
		sql += "   LEFT JOIN COUNTAPPR01 CA ON CA.\"CustNo\" = A.\"CustNo\"  ";
		sql += "                           AND CA.\"CaseSeq\" = A.\"CaseSeq\"    ";

		sql += "  WHERE A.\"CustId\" IS NOT NULL   ";
		sql += "    AND A.\"Seq\" = 1              ";//個案件種類只取一筆
		
		sql += "   ORDER BY  A.\"CustId\", N.\"CaseSeq\" ";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		if (iCustNo != 0) {
			query.setParameter("CustNo", titaVo.getParam("CustNo").trim());
		}
		if (!"".equals(titaVo.getParam("CustId").trim())) {
			query.setParameter("CustId", titaVo.getParam("CustId").trim());
		}
		if (!"".equals(titaVo.getParam("CaseKindCode").trim())) {
			query.setParameter("CaseKindCode", titaVo.getParam("CaseKindCode").trim());
		}
		if (!"".equals(titaVo.getParam("CustLoanKind").trim())) {
			query.setParameter("CustLoanKind", titaVo.getParam("CustLoanKind").trim());
		}
		if (!"".equals(titaVo.getParam("Status").trim())) {
			query.setParameter("Status", titaVo.getParam("Status").trim());
		}
		return switchback(query);
	}
}