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
import com.st1.itx.util.parse.Parse;;

@Service("l9135ServiceImpl")
@Repository
public class L9135ServiceImpl extends ASpringJpaParm implements InitializingBean {
	@Autowired
	Parse parse;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}



	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("L9135 ServiceImpl");

		int iAcDate = this.parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		
		
		String sql = " "; 
		sql += " WITH rawData AS ( ";
		sql += "    SELECT AC.\"AcNoCode\" ";
		sql += "     	  ,CDAC.\"AcNoItem\" ";
		sql += "     	  ,AC.\"AcSubCode\" ";
		sql += "     	  ,AC.\"EntAc\" ";
		sql += "		  ,CASE ";
		sql += "			 WHEN AC.\"EntAc\" IN (1) ";
		sql += "       		  AND SUBSTR(LPAD(AC.\"TitaTxtNo\",8,0),1,2) = SUBSTR(B.\"BatchNo\",5,2) ";
		sql += "       		  AND SUBSTR(LPAD(AC.\"TitaTxtNo\",8,0),3,6) = AC.\"TitaBatchSeq\"";
		sql += "       		 THEN 1";
		sql += "       		 WHEN AC.\"EntAc\" IN (2) ";
		sql += "       		  AND SUBSTR(AC.\"RelTxseq\",11,2) = SUBSTR(B.\"BatchNo\",5,2) ";
		sql += "       		  AND SUBSTR(AC.\"RelTxseq\",13,6) = AC.\"TitaBatchSeq\"";
		sql += "       		  AND SUBSTR(LPAD(AC.\"TitaTxtNo\",8,0),1,2) = SUBSTR(B.\"BatchNo\",5,2) ";
		sql += "       		 THEN 2";
		sql += "       		 WHEN AC.\"EntAc\" IN (3) ";
		sql += "       		  AND SUBSTR(AC.\"RelTxseq\",11,2) = SUBSTR(B.\"BatchNo\",5,2) ";
		sql += "       		  AND SUBSTR(AC.\"RelTxseq\",13,6) = AC.\"TitaBatchSeq\"";
		sql += "       		  AND SUBSTR(LPAD(AC.\"TitaTxtNo\",8,0),1,2) = SUBSTR(B.\"BatchNo\",5,2) ";
		sql += "       		 THEN 3 ";
		sql += "       		 ELSE 0 ";
		sql += "            END   AS \"EntAcCode\" ";
		sql += "		  ,CASE ";
		sql += "			 WHEN AC.\"EntAc\" IN (1) ";
		sql += "       		  AND SUBSTR(LPAD(AC.\"TitaTxtNo\",8,0),1,2) = SUBSTR(B.\"BatchNo\",5,2) ";
		sql += "       		  AND SUBSTR(LPAD(AC.\"TitaTxtNo\",8,0),3,6) = AC.\"TitaBatchSeq\"";
		sql += "       		 THEN 0";
		sql += "       		 WHEN AC.\"EntAc\" IN (2) ";
		sql += "       		  AND SUBSTR(AC.\"RelTxseq\",11,2) = SUBSTR(B.\"BatchNo\",5,2) ";
		sql += "       		  AND SUBSTR(AC.\"RelTxseq\",13,6) = AC.\"TitaBatchSeq\"";
		sql += "       		  AND SUBSTR(LPAD(AC.\"TitaTxtNo\",8,0),1,2) = SUBSTR(B.\"BatchNo\",5,2) ";
		sql += "       		 THEN 0";
		sql += "       		 WHEN AC.\"EntAc\" IN (3) ";
		sql += "       		  AND SUBSTR(AC.\"RelTxseq\",11,2) = SUBSTR(B.\"BatchNo\",5,2) ";
		sql += "       		  AND SUBSTR(AC.\"RelTxseq\",13,6) = AC.\"TitaBatchSeq\"";
		sql += "       		  AND SUBSTR(LPAD(AC.\"TitaTxtNo\",8,0),1,2) = SUBSTR(B.\"BatchNo\",5,2) ";
		sql += "       		 THEN 0 ";
		sql += "       		 ELSE AC.\"TitaTxtNo\"";
		sql += "            END   AS \"TitaTxtNo\" ";
		sql += "       	  ,CASE ";
		sql += "		  	 WHEN AC.\"EntAc\" IN (1) ";
		sql += "       	 	  AND SUBSTR(LPAD(AC.\"TitaTxtNo\",8,0),1,2) = SUBSTR(B.\"BatchNo\",5,2) ";
		sql += "       		  AND SUBSTR(LPAD(AC.\"TitaTxtNo\",8,0),3,6) = AC.\"TitaBatchSeq\"";
		sql += "       		 THEN 0";
		sql += "       		 WHEN AC.\"EntAc\" IN (2) ";
		sql += "       		  AND SUBSTR(AC.\"RelTxseq\",11,2) = SUBSTR(B.\"BatchNo\",5,2) ";
		sql += "       		  AND SUBSTR(AC.\"RelTxseq\",13,6) = AC.\"TitaBatchSeq\"";
		sql += "       		  AND SUBSTR(LPAD(AC.\"TitaTxtNo\",8,0),1,2) = SUBSTR(B.\"BatchNo\",5,2) ";
		sql += "       		 THEN 0";
		sql += "       		 WHEN AC.\"EntAc\" IN (3) ";
		sql += "       		  AND SUBSTR(AC.\"RelTxseq\",11,2) = SUBSTR(B.\"BatchNo\",5,2) ";
		sql += "       		  AND SUBSTR(AC.\"RelTxseq\",13,6) = AC.\"TitaBatchSeq\"";
		sql += "       		  AND SUBSTR(LPAD(AC.\"TitaTxtNo\",8,0),1,2) = SUBSTR(B.\"BatchNo\",5,2) ";
		sql += "       		 THEN 0 ";
		sql += "       		 ELSE AC.\"SlipNo\"";
		sql += "           END   AS \"SlipNo\" ";
		sql += "     	  ,AC.\"AcSubBookCode\" ";
		sql += "     	  ,CD.\"Item\" AS \"AcSubBookItem\"";
		sql += "          , CASE ";
		sql += "              WHEN AC.\"DbCr\" = 'D' ";
		sql += "              THEN AC.\"TxAmt\" ";
		sql += "            ELSE 0 END            AS \"DbAmt\" ";
		sql += "          , CASE ";
		sql += "              WHEN AC.\"DbCr\" = 'C' ";
		sql += "              THEN AC.\"TxAmt\" ";
		sql += "            ELSE 0  ";
		sql += "            END                   AS \"CrAmt\" ";
		sql += "          , CASE ";
		sql += "			  WHEN AC.\"EntAc\" IN (1) ";
		sql += "       		   AND SUBSTR(LPAD(AC.\"TitaTxtNo\",8,0),1,2) = SUBSTR(B.\"BatchNo\",5,2) ";
		sql += "       		   AND SUBSTR(LPAD(AC.\"TitaTxtNo\",8,0),3,6) = AC.\"TitaBatchSeq\"";
		sql += "       		  THEN ' '";
		sql += "       		  WHEN AC.\"EntAc\" IN (2) ";
		sql += "       		   AND SUBSTR(AC.\"RelTxseq\",11,2) = SUBSTR(B.\"BatchNo\",5,2) ";
		sql += "       		   AND SUBSTR(AC.\"RelTxseq\",13,6) = AC.\"TitaBatchSeq\"";
		sql += "       		   AND SUBSTR(LPAD(AC.\"TitaTxtNo\",8,0),1,2) = SUBSTR(B.\"BatchNo\",5,2) ";
		sql += "       		  THEN ' '";
		sql += "       		  WHEN AC.\"EntAc\" IN (3) ";
		sql += "       		   AND SUBSTR(AC.\"RelTxseq\",11,2) = SUBSTR(B.\"BatchNo\",5,2) ";
		sql += "       		   AND SUBSTR(AC.\"RelTxseq\",13,6) = AC.\"TitaBatchSeq\"";
		sql += "       		   AND SUBSTR(LPAD(AC.\"TitaTxtNo\",8,0),1,2) = SUBSTR(B.\"BatchNo\",5,2) ";
		sql += "       		  THEN ' '";
		sql += "              WHEN AC.\"CustNo\" != 0 ";
		sql += "              THEN LPAD(AC.\"CustNo\",7,'0') ";
		sql += "            ELSE ' ' ";
		sql += "            END                   AS \"CustNo\" ";
		sql += "          , \"Fn_GetEmpName\"(AC.\"TitaTlrNo\",1) ";
		sql += "                                  AS \"EmpName\" ";
		sql += "          , B.\"ReconCode\" AS \"ReconCode\"";
		sql += "          , AC.\"AcctCode\" AS \"AcctCode\"";
		sql += "          , AC.\"AcDtlCode\" AS \"AcDtlCode\"";
		sql += "          , AC.\"DbCr\" AS \"DbCr\"";
		sql += "          , CASE WHEN SUBSTR(NVL(AC.\"TitaBatchNo\",'000000'),1,2) = 'LN' AND SUBSTR(NVL(AC.\"TitaBatchNo\",'000000'),5,2) <> '00' THEN AC.\"TitaBatchNo\" ";
		sql += "                 ELSE ' '  END AS \"TitaBatchNo\"  "; //撥款的整批匯款

		sql += "     FROM \"AcDetail\" AC ";
		sql += "     LEFT JOIN \"CdAcCode\" CDAC ON CDAC.\"AcNoCode\" = AC.\"AcNoCode\" ";
		sql += "                              AND CDAC.\"AcSubCode\" = AC.\"AcSubCode\" ";
		sql += "                              AND CDAC.\"AcDtlCode\" = AC.\"AcDtlCode\" ";
		sql += "     LEFT JOIN \"CdCode\" CD ON CD.\"DefCode\" = 'AcSubBookCode' ";
		sql += "                              AND CD.\"Code\" = AC.\"AcSubBookCode\" ";
		sql += "     LEFT JOIN \"CustMain\" CM ON AC.\"CustNo\" != 0 ";
		sql += "                            AND CM.\"CustNo\" = AC.\"CustNo\" ";
		sql += "     LEFT JOIN \"BatxDetail\" B ON B.\"AcDate\" = AC.\"AcDate\" ";
		sql += "                               AND B.\"BatchNo\" = AC.\"TitaBatchNo\" ";
		sql += "                               AND LPAD(B.\"DetailSeq\",6,0) = AC.\"TitaBatchSeq\" ";
		sql += "                               AND B.\"RepayCode\" IN ('01','02','03','04') ";
		sql += "                           AND AC.\"TitaTxtNo\" = TO_NUMBER(SUBSTR(B.\"BatchNo\",5,2)) * 1000000 + B.\"DetailSeq\"";
		sql += "     WHERE AC.\"AcDate\" = :acDate ";
		sql += "       AND \"EntAc\" > 0 ";
		sql += "       AND AC.\"AcNoCode\" = '10121100000' ";
		sql += "       AND AC.\"AcNoCode\" NOT IN ('     ') ";		
		sql += " ) ";
		//--批次
		sql += " , groupData AS ( ";
		sql += "     SELECT R.\"AcNoCode\" ";
		sql += "          , R.\"AcNoItem\" ";
		sql += "          , R.\"AcSubCode\" ";
		sql += "          , R.\"AcDtlCode\" ";
		sql += "          , R.\"SlipNo\" ";
		sql += "          , R.\"AcSubBookCode\" ";
		sql += "          , R.\"AcSubBookItem\" ";
		sql += "          , R.\"DbCr\" AS \"DbCr\"";
		sql += "          , SUM(R.\"DbAmt\") AS \"DbAmt\" ";
		sql += "          , SUM(R.\"CrAmt\") AS \"CrAmt\" ";
		sql += "     FROM rawData R";
		sql += "     WHERE R.\"EntAcCode\" <> 0";
		sql += "     GROUP BY R.\"AcNoCode\" ";
		sql += "            , R.\"AcNoItem\" ";
		sql += "            , R.\"AcSubCode\" ";
		sql += "            , R.\"AcDtlCode\" ";
		sql += "            , R.\"SlipNo\" ";
		sql += "            , R.\"AcSubBookCode\" ";
		sql += "            , R.\"AcSubBookItem\" ";
		sql += "            , R.\"DbCr\"";
		sql += " ) ";
		//--單筆
		sql += " , groupData2 AS ( ";
		sql += "     SELECT R.\"AcNoCode\" ";
		sql += "          , R.\"AcNoItem\" ";
		sql += "          , R.\"AcSubCode\" ";
		sql += "          , R.\"AcDtlCode\" ";
		sql += "          , R.\"SlipNo\" ";
		sql += "          , R.\"AcSubBookCode\" ";
		sql += "          , R.\"AcSubBookItem\" ";
		sql += "          , R.\"DbCr\" AS \"DbCr\"";
		sql += "          , SUM(R.\"DbAmt\") AS \"DbAmt\" ";
		sql += "          , SUM(R.\"CrAmt\") AS \"CrAmt\" ";
		sql += "     FROM rawData R";
		sql += "     WHERE R.\"EntAcCode\" = 0";
		sql += "     GROUP BY R.\"AcNoCode\" ";
		sql += "            , R.\"AcNoItem\" ";
		sql += "            , R.\"AcSubCode\" ";
		sql += "            , R.\"AcDtlCode\" ";
		sql += "            , R.\"SlipNo\" ";
		sql += "            , R.\"AcSubBookCode\" ";
		sql += "            , R.\"AcSubBookItem\" ";
		sql += "            , R.\"DbCr\"";
		sql += " ) ";
		//--
		sql += " , groupData3 AS ( ";
		sql += "     SELECT R.\"AcNoCode\" ";
		sql += "          , R.\"AcNoItem\" ";
		sql += "          , R.\"AcSubCode\" ";
		sql += "          , R.\"AcDtlCode\" ";
		sql += "          , MIN(R.\"SlipNo\") AS \"SlipNo\" ";
		sql += "          , R.\"AcSubBookCode\" ";
		sql += "          , R.\"AcSubBookItem\" ";
		sql += "          , R.\"DbCr\" AS \"DbCr\"";
		sql += "          , SUM(R.\"DbAmt\") AS \"DbAmt\" ";
		sql += "          , SUM(R.\"CrAmt\") AS \"CrAmt\" ";
		sql += "     FROM rawData R";
		sql += "     WHERE R.\"EntAcCode\" = 0  AND R.\"TitaBatchNo\" <> ' ' ";
		sql += "     GROUP BY R.\"AcNoCode\" ";
		sql += "            , R.\"AcNoItem\" ";
		sql += "            , R.\"AcSubCode\" ";
		sql += "            , R.\"AcDtlCode\" ";
		sql += "            , R.\"TitaBatchNo\" ";
		sql += "            , R.\"AcSubBookCode\" ";
		sql += "            , R.\"AcSubBookItem\" ";
		sql += "            , R.\"DbCr\"";
		sql += " ) ";
		sql += " SELECT R.* FROM ( ";
		sql += " SELECT \"AcNoCode\" ";
		sql += "      , \"AcNoItem\" ";
		sql += "      , \"AcSubCode\" ";
		sql += "	  ,CASE";
		sql += "		 WHEN \"AcSubCode\" = '01004' THEN '郵局  18471256'";
		sql += "		 WHEN \"AcSubCode\" = '04004' THEN '新光  0116-10-000108-8'";
		sql += "		 WHEN \"AcSubCode\" = '04007' THEN '新光  0116-10-100100-6'";
		sql += "		 WHEN \"AcSubCode\" = '12005' THEN '台新  00201000001800'";
		sql += "	   END AS \"AcctItem\"";
		sql += "	  ,CASE";
		sql += "		 WHEN \"AcSubCode\" = '01004' THEN '18471256  '";
		sql += "		 WHEN \"AcSubCode\" = '04004' THEN '000108-8  '";
		sql += "		 WHEN \"AcSubCode\" = '04007' THEN '100100-6  '";
		sql += "		 WHEN \"AcSubCode\" = '12005' THEN '1000001800'";
		sql += "	   END AS \"AcctItem2\"";
		sql += "      , DECODE(\"SlipNo\",0,90000 + ROWNUM,\"SlipNo\")  AS \"SlipNo\" ";
		sql += "      , \"DbAmt\" AS \"DbTxAmt\"";
		sql += "      , \"CrAmt\"  AS \"CrTxAmt\"";
		sql += "      , \"AcDtlCode\"";
		sql += "      , \"DbCr\"";
		sql += " FROM groupData";
		sql += " UNION ";
		sql += " SELECT \"AcNoCode\" ";
		sql += "      , \"AcNoItem\" ";
		sql += "      , \"AcSubCode\" ";
		sql += "	  ,CASE";
		sql += "		 WHEN \"AcSubCode\" = '01004' THEN '郵局  18471256'";
		sql += "		 WHEN \"AcSubCode\" = '04004' THEN '新光  0116-10-000108-8'";
		sql += "		 WHEN \"AcSubCode\" = '04007' THEN '新光  0116-10-100100-6'";
		sql += "		 WHEN \"AcSubCode\" = '12005' THEN '台新  00201000001800'";
		sql += "	   END AS \"AcctItem\"";
		sql += "	  ,CASE";
		sql += "		 WHEN \"AcSubCode\" = '01004' THEN '18471256  '";
		sql += "		 WHEN \"AcSubCode\" = '04004' THEN '000108-8  '";
		sql += "		 WHEN \"AcSubCode\" = '04007' THEN '100100-6  '";
		sql += "		 WHEN \"AcSubCode\" = '12005' THEN '1000001800'";
		sql += "	   END AS \"AcctItem2\"";
		sql += "      , \"SlipNo\" AS \"SlipNo\" ";
		sql += "      , \"DbAmt\" AS \"DbTxAmt\"";
		sql += "      , \"CrAmt\"  AS \"CrTxAmt\"";
		sql += "      , \"AcDtlCode\"";
		sql += "      , \"DbCr\"";
		sql += " FROM groupData2";
		sql += " UNION ";
		sql += " SELECT \"AcNoCode\" ";
		sql += "      , \"AcNoItem\" ";
		sql += "      , \"AcSubCode\" ";
		sql += "	  ,CASE";
		sql += "		 WHEN \"AcSubCode\" = '01004' THEN '郵局  18471256'";
		sql += "		 WHEN \"AcSubCode\" = '04004' THEN '新光  0116-10-000108-8'";
		sql += "		 WHEN \"AcSubCode\" = '04007' THEN '新光  0116-10-100100-6'";
		sql += "		 WHEN \"AcSubCode\" = '12005' THEN '台新  00201000001800'";
		sql += "	   END AS \"AcctItem\"";
		sql += "	  ,CASE";
		sql += "		 WHEN \"AcSubCode\" = '01004' THEN '18471256  '";
		sql += "		 WHEN \"AcSubCode\" = '04004' THEN '000108-8  '";
		sql += "		 WHEN \"AcSubCode\" = '04007' THEN '100100-6  '";
		sql += "		 WHEN \"AcSubCode\" = '12005' THEN '1000001800'";
		sql += "	   END AS \"AcctItem2\"";
		sql += "      , \"SlipNo\" AS \"SlipNo\" ";
		sql += "      , \"DbAmt\" AS \"DbTxAmt\"";
		sql += "      , \"CrAmt\"  AS \"CrTxAmt\"";
		sql += "      , \"AcDtlCode\"";
		sql += "      , \"DbCr\"";
		sql += " FROM groupData3";
		sql += " ) R";
		sql += "     LEFT JOIN \"CdAcCode\" E ON E.\"AcNoCode\" = R.\"AcNoCode\"";
		sql += "     						 AND E.\"AcSubCode\" = '     '";
		sql += " ORDER BY  R.\"AcNoCode\" ASC";
		sql += "	      ,CASE";
		sql += "		     WHEN R.\"AcSubCode\" = '01004' THEN 99999  ";	
		sql += "		      ELSE TO_NUMBER(R.\"AcSubCode\") ";
		sql += "	       END ASC";
		sql += "          ,R.\"AcDtlCode\" ASC";
		sql += "	      ,CASE";
		sql += "		     WHEN R.\"SlipNo\" > 90000 THEN R.\"SlipNo\" - 99999  ";	
		sql += "	       END ASC";
		
		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("acDate", iAcDate);
		return this.convertToMap(query);
		
		
		
		
//		String sql = " "; 
//		sql += "	SELECT A.\"AcNoCode\" AS \"AcNoCode\"";
//		sql += "		  ,E2.\"AcNoItem\" AS \"AcNoItem\"";
////		sql += "		  ,'銀行存款－活期存款' AS \"AcNoItem\"";
//		sql += "		  ,A.\"AcSubCode\" AS \"AcSubCode\"";
//		sql += "		  ,CASE";
//		sql += "			 WHEN A.\"AcSubCode\" = '01004' THEN '郵局  18471256'";
//		sql += "			 WHEN A.\"AcSubCode\" = '04004' THEN '新光  0116-10-000108-8'";
//		sql += "			 WHEN A.\"AcSubCode\" = '04007' THEN '新光  0116-10-100100-6'";
//		sql += "			 WHEN A.\"AcSubCode\" = '12005' THEN '台新  00201000001800'";
//		sql += "		   END AS \"AcctItem\"";
//		sql += "		  ,LPAD(A.\"SlipNo\",5,0) AS \"SlipNo\"";
//		sql += "		  ,DECODE(A.\"DbCr\",'D',A.\"TxAmt\",0) AS \"DbTxAmt\"";
//		sql += "		  ,DECODE(A.\"DbCr\",'C',A.\"TxAmt\",0) AS \"CrTxAmt\"";
//		sql += "	FROM \"AcDetail\" A ";
//		sql += "	LEFT JOIN \"CdAcCode\" E ON E.\"AcNoCode\"=A.\"AcNoCode\"";
//		sql += "							AND E.\"AcSubCode\"=A.\"AcSubCode\"";
//		sql += "							AND E.\"AcDtlCode\"=A.\"AcDtlCode\"";
//		sql += "	LEFT JOIN \"CdAcCode\" E2 ON E2.\"AcNoCode\"=A.\"AcNoCode\"";
//		sql += "							 AND E.\"AcSubCode\" = '     ' ";
//		sql += "	WHERE A.\"AcDate\" = :AcDate ";
//		sql += "  	  AND A.\"AcNoCode\" = '10121100000'";// --銀行存款科目
//		sql += "  	  AND A.\"AcSubCode\" NOT IN ('     ')";// --欄位五位
//		sql += "  	  AND A.\"EntAc\" > 0 ";
//		sql += "	ORDER BY A.\"AcNoCode\" ASC";
//		sql += "			,A.\"AcSubCode\" DESC";
//		sql += "			,A.\"AcDtlCode\" ASC";
//		sql += "			,A.\"SlipNo\" ASC";
//
//
//		this.info("L9135ServiceImpl sql=" + sql);
//
//	
//		Query query;
//		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
//		query = em.createNativeQuery(sql);
//		query.setParameter("AcDate", iAcDate);
//		return this.convertToMap(query);
	}



}