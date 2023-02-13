package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class L9134ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * L9134 暫收款傳票金額表(累計)
	 * 
	 * @param startDate 查詢區間起日
	 * @param endDate   查詢區間迄日
	 * @param titaVo    TitaVo
	 * @return 查詢結果
	 */
	public List<Map<String, String>> doQuery(int startDate, int endDate, TitaVo titaVo) {

		String sql = " ";
		sql += " WITH rawData AS ( ";
		sql += "     SELECT \"AcDate\" ";
		sql += "          , \"AcNoCode\" ";
		sql += "          , SUM( ";
		sql += "              CASE ";
		sql += "                WHEN \"DbCr\" = 'D' ";
		sql += "                THEN \"TxAmt\" ";
		sql += "              ELSE 0 END ";
		sql += "            ) AS \"DrAmt\" ";
		sql += "          , SUM( ";
		sql += "              CASE ";
		sql += "                WHEN \"DbCr\" = 'C' ";
		sql += "                THEN \"TxAmt\" ";
		sql += "              ELSE 0 END ";
		sql += "            ) AS \"CrAmt\" ";
		sql += "     FROM \"AcDetail\" ";
		sql += "     WHERE \"AcDate\" >= :startDate ";
		sql += "       AND \"AcDate\" <= :endDate ";
//		sql += "       AND \"AcNoCode\" IN ('20222010000','20222020000','20222180000', '20222180100' ,'20222180200') ";
		// sql += " AND \"AcNoCode\" LIKE '20222%' ";
		sql += "    AND  \"AcctCode\" in ('TAV','TCK','TEM','TAM','TRO','TLD') ";
		sql += "     GROUP BY \"AcDate\" ";
		sql += "            , \"AcNoCode\" ";
		sql += " ) ";
		sql += " SELECT \"AcDate\" ";
		sql += "      , \"AcNoCode\" ";
		sql += "      , \"DrAmt\" ";
		sql += "      , \"CrAmt\" ";
		sql += "      , \"CrAmt\" - \"DrAmt\" AS \"DiffAmt\" ";
		sql += " FROM rawData ";
		sql += " ORDER BY \"AcDate\" ";
		sql += "        , \"AcNoCode\" ";

		this.info("doQueryL9134A sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return this.convertToMap(query);
	}

	/**
	 * L9134 暫收款傳票金額表(明細)
	 * 
	 * @param startDate 查詢區間起日
	 * @param endDate   查詢區間迄日
	 * @param titaVo    TitaVo
	 * @return 查詢結果
	 */
	public List<Map<String, String>> doDetailQuery(int startDate, int endDate, TitaVo titaVo) {

		String sql = " ";
		sql += "     SELECT AD.\"AcDate\" ";
		sql += "          , AD.\"AcNoCode\" ";
		sql += "          , AD.\"AcSubCode\" ";
		sql += "          , AD.\"AcDtlCode\" ";
		sql += "          , C.\"AcNoItem\"  ";
		sql += "          , C.\"AcctItem\" ";
		sql += "          , CASE ";
		sql += "              WHEN AD.\"DbCr\" = 'D' ";
		sql += "              THEN AD.\"TxAmt\" ";
		sql += "            ELSE 0 END AS \"DrAmt\" ";
		sql += "          , CASE ";
		sql += "              WHEN AD.\"DbCr\" = 'C' ";
		sql += "              THEN AD.\"TxAmt\" ";
		sql += "            ELSE 0 END AS \"CrAmt\" ";
		sql += "          , AD.\"SlipNo\" ";
		sql += "          , LPAD(AD.\"TitaTlrNo\",6,0) || LPAD(AD.\"TitaTxtNo\",7,0) AS \"TitaTxtNo\"";
		sql += "          , LPAD(AD.\"CustNo\",7,'0') AS \"CustNo\" ";
		sql += "          , AD.\"FacmNo\" ";
		sql += "          , CM.\"CustName\" ";
		sql += "     FROM \"AcDetail\" AD ";
		sql += "     LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = AD.\"CustNo\" ";
		sql += "                              AND AD.\"CustNo\" != 0 ";
		sql += "     LEFT JOIN \"CdCode\" CC ON CC.\"DefCode\" = 'AcSubBookCode' ";
		sql += "                            AND CC.\"Code\" = AD.\"AcSubBookCode\" ";
		sql += "     LEFT JOIN \"CdAcCode\" C ON C.\"AcNoCode\" = AD.\"AcNoCode\" ";
		sql += "                             AND C.\"AcSubCode\" = AD.\"AcSubCode\" ";
		sql += "                             AND C.\"AcDtlCode\" = AD.\"AcDtlCode\" ";
		sql += "     WHERE AD.\"AcDate\" >= :startDate ";
		sql += "       AND AD.\"AcDate\" <= :endDate ";
//		sql += "       AND AD.\"AcNoCode\" IN ('20222010000','20222020000','20222180000', '20222180100' ,'20222180200') ";
		// sql += " AND AD.\"AcNoCode\" LIKE '20222%' ";
		// sql += " WHERE AC.\"AcNoCode\" IN ('20222010000','20222020000','20222180000',
		// '20222180100' ,'20222180200') ";
		// sql += " WHERE AC.\"AcNoCode\" LIKE '20222%' ";
		sql += "    AND  AD.\"AcctCode\" in ('TAV','TCK','TEM','TAM','TRO','TLD') ";
		sql += "     ORDER BY AD.\"AcDate\" ";
		sql += "            , AD.\"AcNoCode\" ASC ";
		sql += "            , AD.\"AcSubCode\" ASC ";
		sql += "            , AD.\"AcDtlCode\" ASC ";
		sql += "            , AD.\"SlipNo\" ASC ";
		sql += "            , AD.\"TitaTxtNo\" ASC ";

		this.info("doDetailQuery sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return this.convertToMap(query);
	}

	/**
	 * L9134 放款暫收款對帳明細表
	 * 
	 * @param titaVo TitaVo
	 * @return 查詢結果
	 */
	public List<Map<String, String>> doQueryL9134_3(TitaVo titaVo) {

		String sql = " ";
		sql += "     SELECT C.\"AcNoItem\" ";
		sql += "          , AC.\"AcNoCode\"";
		sql += "          , AC.\"AcSubCode\"";
		sql += "          , AC.\"AcDtlCode\"";
		sql += "          , AC.\"CustNo\"";
		sql += "          , AC.\"FacmNo\"";
		sql += "          , AC.\"OpenAcDate\"";
		sql += "          , AC.\"LastTxDate\"";
		sql += "          , AC.\"AcBal\"";
		sql += "          , AC.\"RvBal\"";
		sql += "          , CC.\"Item\"";
		sql += "          , NVL(CO.\"RenewCode\",' ') AS \"RenewCode\"";
		sql += "     FROM \"AcReceivable\" AC ";
		sql += "     LEFT JOIN \"CdAcCode\" C ON C.\"AcNoCode\" = AC.\"AcNoCode\" ";
		sql += "                             AND C.\"AcSubCode\" = AC.\"AcSubCode\" ";
		sql += "                             AND C.\"AcDtlCode\" = AC.\"AcDtlCode\" ";
		sql += "     LEFT JOIN \"CdCode\" CC ON CC.\"DefCode\" = 'AcSubBookCode' ";
		sql += "                            AND CC.\"Code\" = AC.\"AcSubBookCode\" ";
		sql += "     LEFT JOIN \"CollList\" CO ON CO.\"CustNo\" = AC.\"CustNo\" ";
		sql += "                              AND CO.\"FacmNo\" = AC.\"FacmNo\" ";
		// sql += " WHERE AC.\"AcNoCode\" IN ('20222010000','20222020000','20222180000',
		// '20222180100' ,'20222180200') ";
		// sql += " WHERE AC.\"AcNoCode\" LIKE '20222%' ";
		sql += "    WHERE  AC.\"AcctCode\" in ('TAV','TCK','TEM','TAM','TRO','TLD') ";
		sql += "       AND AC.\"RvBal\" > 0 ";
		sql += "     ORDER BY AC.\"AcNoCode\" ASC ";
		sql += "            , AC.\"AcSubCode\" ASC ";
		sql += "            , AC.\"AcDtlCode\" ASC ";
		sql += "            , AC.\"CustNo\" ASC ";
		sql += "            , AC.\"FacmNo\" ASC ";

		this.info("doQueryL9134C sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query);
	}

	/**
	 * L9134 暫收款對帳-日調結表(可抵繳暫收款) L9134 暫收款對帳-日調結表(不可抵繳暫收款) 
	 * 
	 * @param titaVo TitaVo
	 * @return 查詢結果
	 */
	public List<Map<String, String>> doQueryL9134_4_1(TitaVo titaVo, int idate) throws LogicException {
		this.info("L9134_4_1 Start");
		this.info("idate   = " + idate);
		String itdate = parse.IntegerToString(idate + 19110000, 8);
		this.info("itdate   =" + itdate);
		int idx = parse.stringToInteger(titaVo.getParam("StartDate").trim().substring(5, 7));
		this.info("idx  = " + idx);
		String itdx = itdate.substring(0, 6);
		this.info("itdx   = " + itdx);

		String sql = " ";
		sql += " select \"AcDate\" ";
		sql += " , sum(\"TdBal\") as \"TdBal\"   ";
		sql += " , sum(\"TdBal\") - sum(\"YdBal\") as \"DifTdBal\"  ";
		sql += " , sum(\"didTdal\") as \"didTdBal\"  ";
		sql += " , sum(\"didTdal\") - sum(\"didYdBal\") as \"didDifTdBal\" ";
		sql += " , sum(\"DrAmt\") as \"DrAmt\" , sum(\"CrAmt\") as \"CrAmt\" ";
		sql += " from ";
		sql += " ( ";
		sql += " select";
		sql += "  20220101||'~'||20220131 as \"AcDate\" ";
		sql += "  ,sum(\"YdBal\") as \"YdBal\" ";
		sql += "  ,sum(\"TdBal\") as \"TdBal\" ";
		sql += "  ,0 as \"didYdBal\"";
		sql += "  ,0 as \"didTdal\" ";
		sql += " , 0 as \"DrAmt\" , 0 as \"CrAmt\" ";
		sql += " from \"AcMain\" ";
		sql += " where \"AcDate\" >= 20220101 and \"AcDate\" <= 20220131 ";
		sql += "   and \"AcctCode\" in ('TAV','TAM','TLD','T11','T13','T12','T10') ";
		sql += " union all ";
		sql += " select ";
		sql += "  20220101||'~'||20220131 as \"AcDate\", ";
		sql += "  0 as \"YdBal\" ";
		sql += " ,0 as \"TdBal\" ";
		sql += " ,sum(\"YdBal\") as \"didYdBal\" ";
		sql += " ,sum(\"TdBal\") as \"didTdal\" ";
		sql += " ,0 as \"DrAmt\" , 0 as \"CrAmt\" ";
		sql += " from \"AcMain\" ";
		sql += " where \"AcDate\" >= 20220101 and \"AcDate\" <= 20220131 ";
		sql += " and \"AcctCode\" in ('TCK')  ";
		sql += " union all ";
		for (int i = 1; i <= idx; i++) {
			sql += " select ";
			sql += " to_char(" + itdx + parse.IntegerToString(i, 2) + ")  as \"AcDate\" ";
			sql += " ,0 as \"YdBal\" ";
			sql += " ,0 as \"TdBal\" ";
			sql += " ,0 as \"didYdBal\" ";
			sql += " ,0 as \"didTdal\" ";
			sql += " ,0 as \"DrAmt\" , 0 as \"CrAmt\" ";
			sql += " from dual ";
			sql += " union all ";
			sql += " select  ";
			sql += " to_char(\"AcDate\") as \"AcDate\" ";
			sql += " ,\"TdBal\"";
			sql += " ,\"YdBal\" ";
			sql += " , 0 as \"didYdBal\" ";
			sql += " , 0 as \"didTdal\" ";
			sql += " , 0 as \"DrAmt\" , 0 as \"CrAmt\" ";
			sql += " from \"AcMain\" ";
//		sql += " where \"AcDate\" = :itdate ";
			sql += " where \"AcDate\" = " + itdx + parse.IntegerToString(i, 2) + "";
			sql += "   and \"AcctCode\" in ('TAV','TAM','TLD','T11','T13','T12','T10') ";
			sql += " union all ";
			sql += " select ";
			sql += " to_char(\"AcDate\") as \"AcDate\" ";
			sql += " ,0 as \"YdBal\" ";
			sql += " ,0 as \"TdBal\" ";
			sql += " , \"YdBal\" as \"didYdBal\"";
			sql += " , \"TdBal\" as \"didTdal\"";
			sql += " ,0 as \"DrAmt\" , 0 as \"CrAmt\" ";
			sql += " from \"AcMain\" ";
			sql += " where \"AcctCode\" in ('TCK') ";
//		sql += " and  \"AcDate\" =:itdate ";
			sql += " and \"AcDate\" = " + itdx + parse.IntegerToString(i, 2) + " ";
			sql += " union all ";
			sql += " select ";
			sql += " to_char(" + itdx + parse.IntegerToString(i, 2) + ")  as \"AcDate\" ";
			sql += " ,0 as \"YdBal\" ";
			sql += " ,0 as \"TdBal\" ";
			sql += " ,0 as \"didYdBal\" ";
			sql += " ,0 as \"didTdal\" ";
//			sql += " ,sum(\"DrAmt\") as \"DrAmt\" , sum(\"CrAmt\") as \"CrAmt\" ";
			sql += " ,\"DrAmt\" as \"DrAmt\" , \"CrAmt\" as \"CrAmt\" ";
			sql += " from ( ";
			sql += " select ";
			sql += "   CASE";
			sql += "     WHEN AD.\"DbCr\" = 'D' ";
			sql += "     THEN AD.\"TxAmt\" ";
			sql += "   ELSE 0 END AS \"DrAmt\"";
			sql += " , CASE ";
			sql += "     WHEN AD.\"DbCr\" = 'C' ";
			sql += "     THEN AD.\"TxAmt\" ";
			sql += "   ELSE 0 END AS \"CrAmt\" ";
			sql += " FROM \"AcDetail\" AD  ";
			sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = AD.\"CustNo\" ";
			sql += "                           AND AD.\"CustNo\" != 0 ";
			sql += " LEFT JOIN \"CdCode\" CC ON CC.\"DefCode\" = 'AcSubBookCode' ";
			sql += "                         AND CC.\"Code\" = AD.\"AcSubBookCode\" ";
			sql += " LEFT JOIN \"CdAcCode\" C ON C.\"AcNoCode\" = AD.\"AcNoCode\" ";
			sql += "                          AND C.\"AcSubCode\" = AD.\"AcSubCode\"  ";
			sql += "                          AND C.\"AcDtlCode\" = AD.\"AcDtlCode\" ";
//			sql += " WHERE AD.\"AcDate\" >= :startDate  ";
//			sql += "   AND AD.\"AcDate\" <= :endDate ";
			sql += " WHERE AD.\"AcDate\" >=" + itdx + parse.IntegerToString(i, 2) + " ";
			sql += "   AND AD.\"AcDate\" <=" + itdx + parse.IntegerToString(i, 2) + " ";
			sql += "   AND  AD.\"AcctCode\" in ('TAV','TCK','TEM','TAM','TRO','TLD') ";
//			sql += " group by \"AcDate\" " ;
			sql += " ) ";	
			if (idx != i) {
				sql += " union all ";
			}
		}
		sql += " ) ";
		sql += " group by \"AcDate\" ";
		sql += " order by \"AcDate\" ";
		this.info("doQueryL9134_4_1 sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		// query.setParameter("itdate", itdate);
		return this.convertToMap(query);
	}

}