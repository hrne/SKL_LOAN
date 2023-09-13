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
public class L9132ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * L9132A 傳票媒體明細表（總帳）
	 * 
	 * @param acDate  會計日期
	 * @param batchNo 傳票批號
	 * @param titaVo  TitaVo
	 * @return 查詢結果
	 */
	public List<Map<String, String>> doQueryL9132(int acDate, int batchNo, TitaVo titaVo) {
		String sql = " ";

		sql += " WITH rawData AS ( ";
		sql += "    SELECT AC.\"AcNoCode\" ";
		sql += "     	  ,CDAC.\"AcNoItem\" ";
		sql += "     	  ,AC.\"AcSubCode\" ";
		sql += "       	  ,CASE ";
		sql += "		  	 WHEN AC.\"SlipSumNo\" > 0  ";
		sql += "       		 THEN 0 ";
		sql += "       		 ELSE AC.\"TitaTxtNo\"";
		sql += "           END   AS \"TitaTxtNo\" ";
		sql += "       	  ,CASE ";
		sql += "		  	 WHEN AC.\"SlipSumNo\" > 0  ";
		sql += "       		 THEN AC.\"SlipSumNo\" ";
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
		sql += "       	  ,CASE ";
		sql += "		  	 WHEN AC.\"SlipSumNo\" > 0  ";
		sql += "       		 THEN ' ' ";
		sql += "              WHEN AC.\"CustNo\" != 0 ";
		sql += "              THEN LPAD(AC.\"CustNo\",7,'0') ";
		sql += "       		 ELSE ' ' ";
		sql += "           END   AS \"CustNo\" ";
		sql += "       	  ,CASE ";
		sql += "		  	 WHEN AC.\"SlipSumNo\" > 0  ";
		sql += "       		 THEN ' ' ";
		sql += "              WHEN AC.\"FacmNo\" != 0 ";
		sql += "              THEN LPAD(AC.\"FacmNo\",3,'0') ";
		sql += "       		 ELSE ' ' ";
		sql += "           END   AS \"FacmNo\" ";
		sql += "          , \"Fn_GetEmpName\"(AC.\"TitaTlrNo\",1) ";
		sql += "                                  AS \"EmpName\" ";
		sql += "     FROM \"AcDetail\" AC ";
		sql += "     LEFT JOIN \"CdAcCode\" CDAC ON CDAC.\"AcNoCode\" = AC.\"AcNoCode\" ";
		sql += "                              AND CDAC.\"AcSubCode\" = AC.\"AcSubCode\" ";
		sql += "                              AND CDAC.\"AcDtlCode\" = '  ' ";
		sql += "     LEFT JOIN \"CdCode\" CD ON CD.\"DefCode\" = 'AcSubBookCode' ";
		sql += "                              AND CD.\"Code\" = AC.\"AcSubBookCode\" ";
		sql += "     LEFT JOIN \"CustMain\" CM ON AC.\"CustNo\" != 0 ";
		sql += "                            AND CM.\"CustNo\" = AC.\"CustNo\" ";
		sql += "     WHERE AC.\"AcDate\" = :acDate ";
		sql += "       AND AC.\"SlipBatNo\" = :batchNo ";
		sql += "       AND \"EntAc\" > 0 ";// --已入帳
		sql += " ) ";
		// --批次
		sql += " , groupData AS ( ";
		sql += "     SELECT \"AcNoCode\" ";
		sql += "          , \"AcNoItem\" ";
		sql += "          , \"AcSubCode\" ";
		sql += "          , \"SlipNo\" ";
		sql += "          , SUM(\"DbAmt\") AS \"DbAmt\" ";
		sql += "          , SUM(\"CrAmt\") AS \"CrAmt\" ";
		sql += "          , \"AcSubBookCode\" ";
		sql += "          , \"AcSubBookItem\" ";
		sql += "          , \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += "          , \"EmpName\" ";
		sql += "     FROM rawData ";
		sql += "     WHERE \"SlipNo\" >= 90000";
		sql += "     GROUP BY \"AcNoCode\" ";
		sql += "            , \"AcNoItem\" ";
		sql += "            , \"AcSubCode\" ";
		sql += "            , \"SlipNo\" ";
		sql += "            , \"AcSubBookCode\" ";
		sql += "            , \"AcSubBookItem\" ";
		sql += "            , \"CustNo\" ";
		sql += "            , \"FacmNo\" ";
		sql += "            , \"EmpName\" ";
		sql += " ) ";
		// --單筆
		sql += " , groupData2 AS ( ";
		sql += "     SELECT \"AcNoCode\" ";
		sql += "          , \"AcNoItem\" ";
		sql += "          , \"AcSubCode\" ";
		sql += "          , \"SlipNo\" ";
		sql += "          , SUM(\"DbAmt\") AS \"DbAmt\" ";
		sql += "          , SUM(\"CrAmt\") AS \"CrAmt\" ";
		sql += "          , \"AcSubBookCode\" ";
		sql += "          , \"AcSubBookItem\" ";
		sql += "          , \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += "          , \"EmpName\" ";
		sql += "     FROM rawData ";
		sql += "     WHERE \"SlipNo\" < 90000";
		sql += "     GROUP BY \"AcNoCode\" ";
		sql += "            , \"AcNoItem\" ";
		sql += "            , \"AcSubCode\" ";
		sql += "            , \"SlipNo\" ";
		sql += "            , \"AcSubBookCode\" ";
		sql += "            , \"AcSubBookItem\" ";
		sql += "            , \"CustNo\" ";
		sql += "            , \"FacmNo\" ";
		sql += "            , \"EmpName\" ";
		sql += " ) ";
		sql += " SELECT * FROM ( ";
		sql += " SELECT \"AcNoCode\" ";
		sql += "      , \"AcNoItem\" ";
		sql += "      , \"AcSubCode\" ";
		sql += "      , \"SlipNo\"";
		sql += "      , \"AcSubBookCode\" ";
		sql += "      , \"AcSubBookItem\" ";
		sql += "      , \"CustNo\" ";
		sql += "      , \"FacmNo\" ";
		sql += "      , \"DbAmt\" ";
		sql += "      , \"CrAmt\" ";
		sql += "      , \"EmpName\" ";
		sql += "      , 1 as \"Seq\"";
		sql += " FROM groupData";
		sql += " UNION ";
		sql += " SELECT \"AcNoCode\" ";
		sql += "      , \"AcNoItem\" ";
		sql += "      , \"AcSubCode\" ";
		sql += "      , \"SlipNo\"";
		sql += "      , \"AcSubBookCode\" ";
		sql += "      , \"AcSubBookItem\" ";
		sql += "      , \"CustNo\" ";
		sql += "      , \"FacmNo\" ";
		sql += "      , \"DbAmt\" ";
		sql += "      , \"CrAmt\" ";
		sql += "      , \"EmpName\" ";
		sql += "      , 2 as \"Seq\"";
		sql += " FROM groupData2";
		sql += " ) ";
		sql += " ORDER BY \"AcNoCode\" ASC";
		sql += "		 ,\"AcSubCode\" ASC";
		// --20230511 QC2453 提出排序 批次帳 ASC、戶號 ASC、傳票號碼 ASC
		sql += "         , \"Seq\" ASC";
		sql += " 		 ,\"SlipNo\" ASC";
		sql += "		 ,\"CustNo\" ASC";
		this.info("doQueryL9132 sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("acDate", acDate);
		query.setParameter("batchNo", batchNo);
		return this.convertToMap(query);
	}

	/**
	 * L9132A 傳票媒體總表（總帳）
	 * 
	 * @param acDate  會計日期
	 * @param batchNo 傳票批號
	 * @param titaVo  TitaVo
	 * @return 查詢結果
	 */
	public List<Map<String, String>> doQueryL9132A(int acDate, int batchNo, TitaVo titaVo) {

		String sql = " ";
		sql += " WITH rawData AS ( ";
		sql += "    SELECT AC.\"AcNoCode\" ";
		sql += "     	  ,AC.\"AcSubCode\" ";
		sql += "     	  ,CDAC.\"AcNoItem\" ";
		sql += "       	  ,CASE ";
		sql += "		  	 WHEN AC.\"SlipSumNo\" > 0  ";
		sql += "       		 THEN AC.\"SlipSumNo\" ";
		sql += "       		 ELSE AC.\"SlipNo\"";
		sql += "           END   AS \"SlipNo\" ";
		sql += "     	  ,AC.\"AcSubBookCode\" ";
		sql += "     	  ,CD.\"Item\" AS \"AcSubBookItem\"";
		sql += "     	  ,AC.\"DbCr\" ";
		sql += "     	  ,AC.\"TxAmt\" ";
		sql += "     FROM \"AcDetail\" AC ";
		sql += "     LEFT JOIN \"CdAcCode\" CDAC ON CDAC.\"AcNoCode\" = AC.\"AcNoCode\" ";
		sql += "                              AND CDAC.\"AcSubCode\" = AC.\"AcSubCode\" ";
		sql += "                              AND CDAC.\"AcDtlCode\" = '  ' ";
		sql += "     LEFT JOIN \"CdCode\" CD ON CD.\"DefCode\" = 'AcSubBookCode' ";
		sql += "                              AND CD.\"Code\" = AC.\"AcSubBookCode\" ";
		sql += "     WHERE AC.\"AcDate\" = :acDate ";
		sql += "       AND AC.\"SlipBatNo\" = :batchNo ";
		sql += "       AND AC.\"EntAc\" > 0 ";// --已入帳
		sql += " ) ";
		sql += " , groupData AS ( ";
		sql += "     SELECT \"AcNoCode\" ";
		sql += "          , \"AcSubCode\" ";
		sql += "          , \"AcNoItem\" ";
		sql += "          , \"AcSubBookCode\" ";
		sql += "          , \"AcSubBookItem\" ";
		sql += "          , \"SlipNo\" ";
		sql += "          , \"DbCr\" ";
		sql += "          , SUM(\"TxAmt\") AS \"TxAmt\" ";
		sql += "     FROM rawData ";
		sql += "     GROUP BY \"AcNoCode\" ";
		sql += "            , \"AcSubCode\" ";
		sql += "            , \"AcNoItem\" ";
		sql += "            , \"AcSubBookCode\" ";
		sql += "            , \"AcSubBookItem\" ";
		sql += "            , \"SlipNo\" ";
		sql += "            , \"DbCr\" ";
		sql += " ) ";
		sql += " SELECT \"AcNoCode\" ";
		sql += "      , \"AcNoItem\" ";
		sql += "      , \"AcSubBookItem\" ";
		sql += "      , SUM(DECODE(\"DbCr\",'D',1,0 )) AS \"DbCnt\" ";
		sql += "      , SUM(DECODE(\"DbCr\",'D',\"TxAmt\",0 )) AS \"DbAmt\" ";
		sql += "      , SUM(DECODE(\"DbCr\",'C',1,0 )) AS \"CrCnt\" ";
		sql += "      , SUM(DECODE(\"DbCr\",'C',\"TxAmt\",0 )) AS \"CrAmt\" ";
		sql += " FROM groupData  ";
		sql += " GROUP BY \"AcNoCode\" ";
		sql += "        , \"AcNoItem\" ";
		sql += "        , \"AcSubBookItem\" ";
		sql += " ORDER BY \"AcNoCode\" ";
		sql += "        , \"AcNoItem\" ";
		sql += "        , \"AcSubBookItem\" ";
		this.info("doQueryL9132A sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("acDate", acDate);
		query.setParameter("batchNo", batchNo);
		return this.convertToMap(query);
	}

	/**
	 * L9132B 傳票媒體明細表-交易序號
	 * 
	 * @param acDate  會計日期
	 * @param batchNo 傳票批號
	 * @param titaVo  TitaVo
	 * @return 查詢結果
	 */
	public List<Map<String, String>> doQueryL9132B(int acDate, int batchNo, TitaVo titaVo) {

		String sql = " ";

		sql += " WITH rawData AS ( ";
		sql += "	SELECT CASE ";
		sql += "		  	 WHEN AC.\"SlipSumNo\" > 0  ";
		sql += "       		 THEN '000000000000000000' ";
		sql += "       		 ELSE AC.\"TitaKinbr\" || AC.\"TitaTlrNo\" || LPAD(AC.\"TitaTxtNo\",8,0) ";
		sql += "           END   AS \"TitaTxtNo\" ";
		sql += "       	  ,CASE ";
		sql += "		  	 WHEN AC.\"SlipSumNo\" > 0  ";
		sql += "       		 THEN AC.\"SlipSumNo\" ";
		sql += "       		 ELSE AC.\"SlipNo\"";
		sql += "           END   AS \"SlipNo\" ";
		sql += "          , AC.\"AcNoCode\" || ' ' || CDAC.\"AcNoItem\" ";
		sql += "                                  AS \"AcNo\" ";
		sql += "          , \"Fn_GetCdCode\"('AcSubBookCode',AC.\"AcSubBookCode\") ";
		sql += "                                  AS \"AcSubBookItem\" ";
		sql += "          , CASE ";
		sql += "              WHEN AC.\"DbCr\" = 'D' ";
		sql += "              THEN AC.\"TxAmt\" ";
		sql += "            ELSE 0 END            AS \"DbAmt\" ";
		sql += "          , CASE ";
		sql += "              WHEN AC.\"DbCr\" = 'C' ";
		sql += "              THEN AC.\"TxAmt\" ";
		sql += "            ELSE 0  ";
		sql += "            END                   AS \"CrAmt\" ";
		sql += "       	  ,CASE ";
		sql += "		  	 WHEN AC.\"SlipSumNo\" > 0  ";
		sql += "       		 THEN ' ' ";
		sql += "              WHEN AC.\"CustNo\" != 0 ";
		sql += "              THEN LPAD(AC.\"CustNo\",7,'0') ";
		sql += "       		 ELSE ' ' ";
		sql += "           END   AS \"CustNo\" ";
		sql += "       	  ,CASE ";
		sql += "		  	 WHEN AC.\"SlipSumNo\" > 0  ";
		sql += "       		 THEN ' ' ";
		sql += "              WHEN AC.\"FacmNo\" != 0 ";
		sql += "              THEN LPAD(AC.\"FacmNo\",3,'0') ";
		sql += "       		 ELSE ' ' ";
		sql += "           END   AS \"FacmNo\" ";
		sql += "       	  ,CASE ";
		sql += "		  	 WHEN AC.\"SlipSumNo\" > 0  ";
		sql += "             THEN N' '";
		sql += "            ELSE RPAD(NVL(CM.\"CustName\",' '),6,' ') ";
		sql += "           END   AS \"CustName\" ";
		sql += "          , \"Fn_GetEmpName\"(AC.\"TitaTlrNo\",1) ";
		sql += "                                  AS \"EmpName\" ";
		sql += "     FROM \"AcDetail\" AC ";
		sql += "     LEFT JOIN \"CdAcCode\" CDAC ON CDAC.\"AcNoCode\" = AC.\"AcNoCode\" ";
		sql += "                              AND CDAC.\"AcSubCode\" = AC.\"AcSubCode\" ";
		sql += "                              AND CDAC.\"AcDtlCode\" = AC.\"AcDtlCode\" ";
		sql += "     LEFT JOIN \"CustMain\" CM ON AC.\"CustNo\" != 0 ";
		sql += "                            AND CM.\"CustNo\" = AC.\"CustNo\" ";
		sql += "     WHERE AC.\"AcDate\" = :acDate ";
		sql += "       AND AC.\"SlipBatNo\" = :batchNo ";
		sql += "       AND \"EntAc\" > 0 ";
		sql += " ) ";
		sql += " , groupData AS ( ";
		sql += "     SELECT \"TitaTxtNo\" ";
		sql += "          , \"SlipNo\" ";
		sql += "          , \"AcNo\" ";
		sql += "          , \"AcSubBookItem\" ";
		sql += "          , SUM(\"DbAmt\") AS \"DbAmt\" ";
		sql += "          , SUM(\"CrAmt\") AS \"CrAmt\" ";
		sql += "          , \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += "          , \"CustName\" ";
		sql += "          , \"EmpName\" ";
		sql += "     FROM rawData ";
		sql += "     WHERE \"SlipNo\" >= 90000";
		sql += "     GROUP BY \"TitaTxtNo\" ";
		sql += "            , \"SlipNo\" ";
		sql += "            , \"AcNo\" ";
		sql += "            , \"AcSubBookItem\" ";
		sql += "            , \"CustNo\" ";
		sql += "            , \"FacmNo\" ";
		sql += "            , \"CustName\" ";
		sql += "            , \"EmpName\" ";
		sql += " ) ";
		sql += " , groupData2 AS ( ";
		sql += "     SELECT \"TitaTxtNo\" AS \"TitaTxtNo\"";
		sql += "          , \"SlipNo\" ";
		sql += "          , \"AcNo\" ";
		sql += "          , \"AcSubBookItem\" ";
		sql += "          , SUM(\"DbAmt\") AS \"DbAmt\" ";
		sql += "          , SUM(\"CrAmt\") AS \"CrAmt\" ";
		sql += "          , \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += "          , \"CustName\" ";
		sql += "          , \"EmpName\" ";
		sql += "     FROM rawData ";
		sql += "     WHERE \"SlipNo\" < 90000";
		sql += "     GROUP BY \"TitaTxtNo\" ";
		sql += "            , \"SlipNo\" ";
		sql += "            , \"AcNo\" ";
		sql += "            , \"AcSubBookItem\" ";
		sql += "            , \"CustNo\" ";
		sql += "            , \"FacmNo\" ";
		sql += "            , \"CustName\" ";
		sql += "            , \"EmpName\" ";
		sql += "     ORDER BY \"TitaTxtNo\" ASC";
		sql += "     		 ,\"SlipNo\" ASC";
		sql += " ) ";
		sql += " SELECT * FROM ( ";
		sql += " SELECT A.\"TitaTxtNo\" AS \"TitaTxtNo\"";
		sql += "      , A.\"SlipNo\" ";
		sql += "      , A.\"AcNo\" ";
		sql += "      , A.\"AcSubBookItem\" ";
		sql += "      , A.\"DbAmt\" ";
		sql += "      , A.\"CrAmt\" ";
		sql += "      , A.\"CustNo\" ";
		sql += "      , A.\"FacmNo\" ";
		sql += "      , A.\"CustName\" ";
		sql += "      , A.\"EmpName\" ";
		sql += " FROM groupData A ";
		sql += " UNION ";
		sql += " SELECT B.\"TitaTxtNo\" AS \"TitaTxtNo\"";
		sql += "       ,B.\"SlipNo\" ";
		sql += "      , B.\"AcNo\" ";
		sql += "      , B.\"AcSubBookItem\" ";
		sql += "      , B.\"DbAmt\" ";
		sql += "      , B.\"CrAmt\" ";
		sql += "      , B.\"CustNo\" ";
		sql += "      , B.\"FacmNo\" ";
		sql += "      , B.\"CustName\" ";
		sql += "      , B.\"EmpName\" ";
		sql += " FROM groupData2 B ";
		sql += " ) ORDER BY DECODE(TO_NUMBER(SUBSTR(\"TitaTxtNo\",11,8)),0,0,1) ASC";
		sql += " 		   ,CASE WHEN \"SlipNo\" like '9%' AND LENGTH(\"SlipNo\")=5 THEN \"SlipNo\" ELSE \"SlipNo\" * 100000 END ASC";

		this.info("doQueryL9132B sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("acDate", acDate);
		query.setParameter("batchNo", batchNo);
		return this.convertToMap(query);
	}

	/**
	 * L9132C 傳票媒體明細表-櫃員編號
	 * 
	 * @param acDate  會計日期
	 * @param batchNo 傳票批號
	 * @param titaVo  TitaVo
	 * @return 查詢結果
	 */
	public List<Map<String, String>> doQueryL9132C(int acDate, int batchNo, TitaVo titaVo) {

		String sql = " ";

		sql += " WITH rawData AS ( ";
		sql += "	SELECT CASE ";
		sql += "		  	 WHEN AC.\"SlipSumNo\" > 0  ";
		sql += "       		 THEN '000000000000000000' ";
		sql += "       		 ELSE AC.\"TitaKinbr\" || AC.\"TitaTlrNo\" || LPAD(AC.\"TitaTxtNo\",8,0) ";
		sql += "           END   AS \"TitaTxtNo\" ";
		sql += "       	  ,CASE ";
		sql += "		  	 WHEN AC.\"SlipSumNo\" > 0  ";
		sql += "       		 THEN AC.\"SlipSumNo\" ";
		sql += "       		 ELSE AC.\"SlipNo\"";
		sql += "           END   AS \"SlipNo\" ";
		sql += "		  ,AC.\"TitaTlrNo\" AS \"TitaTlrNo\" ";
		sql += "          , AC.\"AcNoCode\" || ' ' || CDAC.\"AcNoItem\" ";
		sql += "                                  AS \"AcNo\" ";
		sql += "          , \"Fn_GetCdCode\"('AcSubBookCode',AC.\"AcSubBookCode\") ";
		sql += "                                  AS \"AcSubBookItem\" ";
		sql += "          , CASE ";
		sql += "              WHEN AC.\"DbCr\" = 'D' ";
		sql += "              THEN AC.\"TxAmt\" ";
		sql += "            ELSE 0 END            AS \"DbAmt\" ";
		sql += "          , CASE ";
		sql += "              WHEN AC.\"DbCr\" = 'C' ";
		sql += "              THEN AC.\"TxAmt\" ";
		sql += "            ELSE 0  ";
		sql += "            END                   AS \"CrAmt\" ";
		sql += "       	  ,CASE ";
		sql += "		  	 WHEN AC.\"SlipSumNo\" > 0  ";
		sql += "       		 THEN ' ' ";
		sql += "              WHEN AC.\"CustNo\" != 0 ";
		sql += "              THEN LPAD(AC.\"CustNo\",7,'0') ";
		sql += "       		 ELSE ' ' ";
		sql += "           END   AS \"CustNo\" ";
		sql += "       	  ,CASE ";
		sql += "		  	 WHEN AC.\"SlipSumNo\" > 0  ";
		sql += "       		 THEN ' ' ";
		sql += "              WHEN AC.\"FacmNo\" != 0 ";
		sql += "              THEN LPAD(AC.\"FacmNo\",3,'0') ";
		sql += "       		 ELSE ' ' ";
		sql += "           END   AS \"FacmNo\" ";
		sql += "       	  ,CASE ";
		sql += "		  	 WHEN AC.\"SlipSumNo\" > 0  ";
		sql += "             THEN N' '";
		sql += "            ELSE RPAD(NVL(CM.\"CustName\",' '),6,' ') ";
		sql += "           END   AS \"CustName\" ";
		sql += "          , \"Fn_GetEmpName\"(AC.\"TitaTlrNo\",1) ";
		sql += "                                  AS \"EmpName\" ";
		sql += "     FROM \"AcDetail\" AC ";
		sql += "     LEFT JOIN \"CdAcCode\" CDAC ON CDAC.\"AcNoCode\" = AC.\"AcNoCode\" ";
		sql += "                              AND CDAC.\"AcSubCode\" = AC.\"AcSubCode\" ";
		sql += "                              AND CDAC.\"AcDtlCode\" = AC.\"AcDtlCode\" ";
		sql += "     LEFT JOIN \"CustMain\" CM ON AC.\"CustNo\" != 0 ";
		sql += "                            AND CM.\"CustNo\" = AC.\"CustNo\" ";
		sql += "     WHERE AC.\"AcDate\" = :acDate ";
		sql += "       AND AC.\"SlipBatNo\" = :batchNo ";
		sql += "       AND \"EntAc\" > 0 ";
		sql += " ) ";
		sql += " , groupData AS ( ";
		sql += "     SELECT \"TitaTlrNo\" ";
		sql += "          , \"TitaTxtNo\" ";
		sql += "          , \"SlipNo\" ";
		sql += "          , \"AcNo\" ";
		sql += "          , \"AcSubBookItem\" ";
		sql += "          , SUM(\"DbAmt\") AS \"DbAmt\" ";
		sql += "          , SUM(\"CrAmt\") AS \"CrAmt\" ";
		sql += "          , \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += "          , \"CustName\" ";
		sql += "          , \"EmpName\" ";
		sql += "     FROM rawData ";
		sql += "     WHERE \"SlipNo\" >= 90000";
		sql += "     GROUP BY \"TitaTlrNo\" ";
		sql += "            , \"TitaTxtNo\" ";
		sql += "            , \"SlipNo\" ";
		sql += "            , \"AcNo\" ";
		sql += "            , \"AcSubBookItem\" ";
		sql += "            , \"CustNo\" ";
		sql += "            , \"FacmNo\" ";
		sql += "            , \"CustName\" ";
		sql += "            , \"EmpName\" ";
		sql += "     ORDER BY \"EmpName\" ASC";
		sql += "     		 ,\"CustNo\" ASC";
		sql += " ) ";
		sql += " , groupData2 AS ( ";
		sql += "     SELECT \"TitaTlrNo\" ";
		sql += "          , \"TitaTxtNo\" ";
		sql += "          , \"SlipNo\" ";
		sql += "          , \"AcNo\" ";
		sql += "          , \"AcSubBookItem\" ";
		sql += "          , \"DbAmt\" AS \"DbAmt\" ";
		sql += "          , \"CrAmt\" AS \"CrAmt\" ";
		sql += "          , \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += "          , \"CustName\" ";
		sql += "          , \"EmpName\" ";
		sql += "     FROM rawData ";
		sql += "     WHERE \"SlipNo\" < 90000";
		sql += "     ORDER BY \"TitaTxtNo\" ASC";
		sql += "     		 ,\"SlipNo\" ASC";
		sql += " ) ";
		sql += " SELECT * FROM ( ";
		sql += " SELECT A.\"TitaTlrNo\" ";
		sql += "      , A.\"TitaTxtNo\" AS \"TitaTxtNo\" ";
		sql += "      , A.\"SlipNo\" ";
		sql += "      , A.\"AcNo\" ";
		sql += "      , A.\"AcSubBookItem\" ";
		sql += "      , A.\"DbAmt\" ";
		sql += "      , A.\"CrAmt\" ";
		sql += "      , A.\"CustNo\" ";
		sql += "      , A.\"FacmNo\" ";
		sql += "      , A.\"CustName\" ";
		sql += "      , A.\"EmpName\" ";
		sql += " FROM groupData A ";
		sql += " UNION ";
		sql += " SELECT B.\"TitaTlrNo\" ";
		sql += "      , B.\"TitaTxtNo\" AS \"TitaTxtNo\" ";
		sql += "      , B.\"SlipNo\" ";
		sql += "      , B.\"AcNo\" ";
		sql += "      , B.\"AcSubBookItem\" ";
		sql += "      , B.\"DbAmt\" ";
		sql += "      , B.\"CrAmt\" ";
		sql += "      , B.\"CustNo\" ";
		sql += "      , B.\"FacmNo\" ";
		sql += "      , B.\"CustName\" ";
		sql += "      , B.\"EmpName\" ";
		sql += " FROM groupData2 B ";
		sql += " )";
		sql += " ORDER BY CASE";
		sql += " 			WHEN ASCII(SUBSTR(\"TitaTlrNo\",1,1)) >= 65 ";
		sql += "            THEN ASCII(SUBSTR(\"TitaTlrNo\",1,1)) ";
		sql += "          ELSE ASCII(SUBSTR(\"TitaTlrNo\",1,1)) + 43 END ASC";
		sql += "         ,\"TitaTlrNo\" ASC";
		sql += " 		 ,CASE WHEN \"SlipNo\" like '9%' AND LENGTH(\"SlipNo\")=5 THEN \"SlipNo\" ELSE \"SlipNo\" * 100000 END ASC";

		this.info("doQueryL9132C sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("acDate", acDate);
		query.setParameter("batchNo", batchNo);
		return this.convertToMap(query);
	}

	/**
	 * L9132D 放款部日計表（總帳）
	 * 
	 * @param acDate  會計日期
	 * @param batchNo 傳票批號
	 * @param titaVo  TitaVo
	 * @return 查詢結果
	 */
	public List<Map<String, String>> doQueryL9132D(int acDate, int batchNo, TitaVo titaVo) {

		String sql = " ";
		sql += " WITH TempCDAC AS ( ";
		sql += "     SELECT DISTINCT ";
		sql += "            CDAC.\"AcNoCode\" ";
		sql += "          , CDAC.\"AcNoItem\" ";
		sql += "          , NVL(CDC.\"Code\",'00A') AS \"AcSubBookCode\" ";
		sql += "     FROM \"CdAcCode\" CDAC ";
		sql += "     LEFT JOIN \"CdCode\" CDC ON CDAC.\"AcBookFlag\" = 1 ";
		sql += "                           AND CDC.\"DefCode\" = 'AcSubBookCode' ";
		sql += "     WHERE CDAC.\"AcSubCode\" = '     ' ";
		sql += "       AND CDAC.\"AcDtlCode\" = '  ' ";
		sql += " ) ";
		sql += " , detailData AS ( ";
		sql += "     SELECT ac.\"AcNoCode\", ";
		sql += "            ac.\"AcSubBookCode\" ";
		sql += "          , sum( CASE ";
		sql += "              WHEN NVL(AC.\"DbCr\",' ') = 'D' ";
		sql += "              THEN AC.\"TxAmt\" ";
		sql += "            ELSE 0 END              ) AS \"DbAmt\" ";
		sql += "          , sum( CASE ";
		sql += "              WHEN NVL(AC.\"DbCr\",' ') = 'C' ";
		sql += "              THEN AC.\"TxAmt\" ";
		sql += "            ELSE 0 END              ) AS \"CrAmt\" ";
		sql += "     FROM TempCDAC ";
		sql += "     LEFT JOIN  \"AcDetail\" AC ON AC.\"AcNoCode\" = TempCDAC.\"AcNoCode\" ";
		sql += "                             AND AC.\"AcSubBookCode\" = TempCDAC.\"AcSubBookCode\" ";
		sql += "                             AND AC.\"EntAc\" > 0 ";
		sql += "                       and ac.\"AcDate\" = :acDate  ";
		sql += "                       and ac.\"SlipBatNo\" = :batchNo ";
		sql += "                  group by ac.\"AcNoCode\", ac.\"AcSubBookCode\"  ";
		sql += " ) ";
		sql += " SELECT    dd.\"AcNoCode\" as \"AcNoCode\"  ,";
		sql += "   dd.\"AcNoItem\" as \"AcNoItem\" ,  ";
		sql += "      \"Fn_GetCdCode\"('AcSubBookCode', dd.\"AcSubBookCode\")    AS \"AcSubBookCode\", ";
		sql += "      nvl(ee.\"DbAmt\",0) AS \"DbAmt\" , ";
		sql += "      nvl(ee.\"CrAmt\",0) AS \"CrAmt\" ";
		sql += " FROM tempcdac dd ";
		sql += " left join detaildata ee on dd.\"AcNoCode\" = ee.\"AcNoCode\" and dd.\"AcSubBookCode\" = ee.\"AcSubBookCode\"  ";
		sql += " WHERE ";
		sql += " rtrim(ltrim(\"Fn_GetCdCode\"('AcSubBookCode', dd.\"AcSubBookCode\"), ' '), ' ') != ' ' ";
		sql += " ORDER BY dd.\"AcNoCode\" ";
		sql += "        , dd.\"AcSubBookCode\" ";
		this.info("doQueryL9132D sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("acDate", acDate);
		query.setParameter("batchNo", batchNo);
		return this.convertToMap(query);
	}

}