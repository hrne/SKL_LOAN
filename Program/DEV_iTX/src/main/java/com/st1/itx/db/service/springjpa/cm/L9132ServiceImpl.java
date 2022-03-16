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
	 * L9132A 傳票媒體總表（總帳）
	 * 
	 * @param acDate  會計日期
	 * @param batchNo 傳票批號
	 * @param titaVo  TitaVo
	 * @return 查詢結果
	 */
	public List<Map<String, String>> doQueryL9132A(int acDate, int batchNo, TitaVo titaVo) {

		String sql = " ";
		sql += " WITH detailData AS ( ";
		sql += "     SELECT AC.\"AcNoCode\" ";
		sql += "            || CASE ";
		sql += "                 WHEN TRIM(AC.\"AcSubCode\") != NULL ";
		sql += "                 THEN '-' || AC.\"AcSubCode\" ";
		sql += "               ELSE '' END           AS \"AcNoCode\" ";
		sql += "          , CDAC.\"AcNoItem\" ";
		sql += "          , \"Fn_GetCdCode\"('AcSubBookCode',AC.\"AcSubBookCode\") ";
		sql += "                                     AS \"AcSubBookItem\" ";
		sql += "          , CASE ";
		sql += "              WHEN AC.\"DbCr\" = 'D' ";
		sql += "              THEN 1 ";
		sql += "            ELSE 0 END               AS \"DbCnt\" ";
		sql += "          , CASE ";
		sql += "              WHEN AC.\"DbCr\" = 'D' ";
		sql += "              THEN AC.\"TxAmt\" ";
		sql += "            ELSE 0 END               AS \"DbAmt\" ";
		sql += "          , CASE ";
		sql += "              WHEN AC.\"DbCr\" = 'C' ";
		sql += "              THEN 1 ";
		sql += "            ELSE 0 END               AS \"CrCnt\" ";
		sql += "          , CASE ";
		sql += "              WHEN AC.\"DbCr\" = 'C' ";
		sql += "              THEN AC.\"TxAmt\" ";
		sql += "            ELSE 0 END               AS \"CrAmt\" ";
		sql += "     FROM \"AcDetail\" AC ";
		sql += "     LEFT JOIN \"CdAcCode\" CDAC ON CDAC.\"AcNoCode\" = AC.\"AcNoCode\" ";
		sql += "                              AND CDAC.\"AcSubCode\" = AC.\"AcSubCode\" ";
		sql += "                              AND CDAC.\"AcDtlCode\" = AC.\"AcDtlCode\" ";
		sql += "     WHERE AC.\"AcDate\" = :acDate ";
		sql += "       AND AC.\"SlipBatNo\" = :batchNo ";
		sql += " ) ";
		sql += " SELECT dd.\"AcNoCode\" ";
		sql += "      , dd.\"AcNoItem\" ";
		sql += "      , dd.\"AcSubBookItem\" ";
		sql += "      , SUM(dd.\"DbCnt\") AS \"DbCnt\" ";
		sql += "      , SUM(dd.\"DbAmt\") AS \"DbAmt\" ";
		sql += "      , SUM(dd.\"CrCnt\") AS \"CrCnt\" ";
		sql += "      , SUM(dd.\"CrAmt\") AS \"CrAmt\" ";
		sql += " FROM detailData dd ";
		sql += " GROUP BY dd.\"AcNoCode\" ";
		sql += "        , dd.\"AcNoItem\" ";
		sql += "        , dd.\"AcSubBookItem\" ";
		sql += " ORDER BY dd.\"AcNoCode\" ";
		sql += "        , dd.\"AcNoItem\" ";
		sql += "        , dd.\"AcSubBookItem\" ";
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
		sql += "     SELECT CASE ";
		sql += "              WHEN AC.\"TitaBatchNo\" LIKE 'BATX%' ";
		sql += "              THEN 0 ";
		sql += "            ELSE AC.\"TitaTxtNo\" ";
		sql += "            END                   AS \"TitaTxtNo\" ";
		sql += "          , CASE ";
		sql += "              WHEN AC.\"TitaBatchNo\" LIKE 'BATX%' ";
		sql += "              THEN 0 ";
		sql += "            ELSE AC.\"SlipNo\" ";
		sql += "            END                   AS \"SlipNo\" ";
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
		sql += "          , CASE ";
		sql += "              WHEN AC.\"TitaBatchNo\" LIKE 'BATX%' ";
		sql += "              THEN ' ' ";
		sql += "              WHEN AC.\"CustNo\" != 0 ";
		sql += "              THEN LPAD(AC.\"CustNo\",7,'0') ";
		sql += "            ELSE ' ' ";
		sql += "            END                   AS \"CustNo\" ";
		sql += "          , CASE ";
		sql += "              WHEN AC.\"TitaBatchNo\" LIKE 'BATX%' ";
		sql += "              THEN N' ' ";
		sql += "            ELSE RPAD(NVL(CM.\"CustName\",' '),6,' ') ";
		sql += "            END                   AS \"CustName\" ";
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
		sql += " ) ";
		sql += " , groupData AS ( ";
		sql += "     SELECT \"TitaTxtNo\" ";
		sql += "          , \"SlipNo\" ";
		sql += "          , \"AcNo\" ";
		sql += "          , \"AcSubBookItem\" ";
		sql += "          , SUM(\"DbAmt\") AS \"DbAmt\" ";
		sql += "          , SUM(\"CrAmt\") AS \"CrAmt\" ";
		sql += "          , \"CustNo\" ";
		sql += "          , \"CustName\" ";
		sql += "          , \"EmpName\" ";
		sql += "     FROM rawData ";
		sql += "     GROUP BY \"TitaTxtNo\" ";
		sql += "            , \"SlipNo\" ";
		sql += "            , \"AcNo\" ";
		sql += "            , \"AcSubBookItem\" ";
		sql += "            , \"CustNo\" ";
		sql += "            , \"CustName\" ";
		sql += "            , \"EmpName\" ";
		sql += " ) ";
		sql += " SELECT CASE ";
		sql += "          WHEN \"TitaTxtNo\" = 0 ";
		sql += "          THEN ' ' ";
		sql += "        ELSE TO_CHAR(\"TitaTxtNo\") ";
		sql += "        END               AS \"TitaTxtNo\" ";
		sql += "      , CASE ";
		sql += "          WHEN \"SlipNo\" = 0 ";
		sql += "          THEN 90000  ";
		sql += "               + ROW_NUMBER()  ";
		sql += "                 OVER ( ";
		sql += "                     ORDER BY \"AcNo\" ";
		sql += "                            , \"AcSubBookItem\" ";
		sql += "                 ) ";
		sql += "        ELSE \"SlipNo\" ";
		sql += "        END               AS \"SlipNo\" ";
		sql += "      , \"AcNo\" ";
		sql += "      , \"AcSubBookItem\" ";
		sql += "      , \"DbAmt\" ";
		sql += "      , \"CrAmt\" ";
		sql += "      , \"CustNo\" ";
		sql += "      , \"CustName\" ";
		sql += "      , \"EmpName\" ";
		sql += " FROM groupData ";
		sql += " ORDER BY \"TitaTxtNo\" ";
		sql += "        , \"SlipNo\" ";
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
		sql += " SELECT AC.\"TitaTlrNo\" ";
		sql += "      , AC.\"TitaTxtNo\" ";
		sql += "      , AC.\"SlipNo\" ";
		sql += "      , AC.\"AcNoCode\" || ' ' || CDAC.\"AcNoItem\" ";
		sql += "                                AS \"AcNo\" ";
		sql += "      , \"Fn_GetCdCode\"('AcSubBookCode',AC.\"AcSubBookCode\") ";
		sql += "                                AS \"AcSubBookItem\" ";
		sql += "      , CASE ";
		sql += "          WHEN AC.\"DbCr\" = 'D' ";
		sql += "          THEN AC.\"TxAmt\" ";
		sql += "        ELSE 0 END              AS \"DbAmt\" ";
		sql += "      , CASE ";
		sql += "          WHEN AC.\"DbCr\" = 'C' ";
		sql += "          THEN AC.\"TxAmt\" ";
		sql += "        ELSE 0 END              AS \"CrAmt\" ";
		sql += "      , CASE ";
		sql += "          WHEN AC.\"CustNo\" != 0 ";
		sql += "          THEN LPAD(AC.\"CustNo\",7,'0') ";
		sql += "        ELSE ' ' END            AS \"CustNo\" ";
		sql += "      , RPAD(NVL(CM.\"CustName\",' '),6,' ') ";
		sql += "                                AS \"CustName\" ";
		sql += "      , \"Fn_GetEmpName\"(AC.\"TitaTlrNo\",1) ";
		sql += "                                AS \"EmpName\" ";
		sql += " FROM \"AcDetail\" AC ";
		sql += " LEFT JOIN \"CdAcCode\" CDAC ON CDAC.\"AcNoCode\" = AC.\"AcNoCode\" ";
		sql += "                          AND CDAC.\"AcSubCode\" = AC.\"AcSubCode\" ";
		sql += "                          AND CDAC.\"AcDtlCode\" = AC.\"AcDtlCode\" ";
		sql += " LEFT JOIN \"CustMain\" CM ON AC.\"CustNo\" != 0 ";
		sql += "                        AND CM.\"CustNo\" = AC.\"CustNo\" ";
		sql += " WHERE AC.\"AcDate\" = :acDate ";
		sql += "   AND AC.\"SlipBatNo\" = :batchNo ";
		sql += " ORDER BY AC.\"TitaTlrNo\" ";
		sql += "        , AC.\"SlipNo\" ";
		sql += "        , AC.\"TitaTxtNo\" ";
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
		sql += "     SELECT TempCDAC.\"AcNoCode\" || ' ' || TempCDAC.\"AcNoItem\" ";
		sql += "                                     AS \"AcNo\" ";
		sql += "          , \"Fn_GetCdCode\"('AcSubBookCode',TempCDAC.\"AcSubBookCode\") ";
		sql += "                                     AS \"AcSubBookItem\" ";
		sql += "          , CASE ";
		sql += "              WHEN NVL(AC.\"DbCr\",' ') = 'D' ";
		sql += "              THEN AC.\"TxAmt\" ";
		sql += "            ELSE 0 END               AS \"DbAmt\" ";
		sql += "          , CASE ";
		sql += "              WHEN NVL(AC.\"DbCr\",' ') = 'C' ";
		sql += "              THEN AC.\"TxAmt\" ";
		sql += "            ELSE 0 END               AS \"CrAmt\" ";
		sql += "     FROM TempCDAC ";
		sql += "     LEFT JOIN  \"AcDetail\" AC ON AC.\"AcNoCode\" = TempCDAC.\"AcNoCode\" ";
		sql += "                             AND AC.\"AcSubBookCode\" = TempCDAC.\"AcSubBookCode\" ";
		sql += "     WHERE CASE ";
		sql += "             WHEN NVL(AC.\"AcDate\",0) = 0 ";
		sql += "             THEN 1 ";
		sql += "             WHEN NVL(AC.\"AcDate\",0) = :acDate ";
		sql += "                  AND NVL(AC.\"SlipBatNo\",0) = :batchNo ";
		sql += "             THEN 1 ";
		sql += "           ELSE 0 END = 1 ";
		sql += " ) ";
		sql += " SELECT dd.\"AcNo\" ";
		sql += "      , dd.\"AcSubBookItem\" ";
		sql += "      , SUM(dd.\"DbAmt\") AS \"DbAmt\" ";
		sql += "      , SUM(dd.\"CrAmt\") AS \"CrAmt\" ";
		sql += " FROM detailData dd ";
		sql += " GROUP BY dd.\"AcNo\" ";
		sql += "        , dd.\"AcSubBookItem\" ";
		sql += " ORDER BY dd.\"AcNo\" ";
		sql += "        , dd.\"AcSubBookItem\" ";
		this.info("doQueryL9132D sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("acDate", acDate);
		query.setParameter("batchNo", batchNo);
		return this.convertToMap(query);
	}
}