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
		sql += "    AND  \"AcctCode\" in ('T10','T11','T12','T13','TAM','TAV','TCK','TEM','THC','TLD','TMI','TRO','TSL') ";
		sql += "    AND  \"EntAc\" > 0";
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
		sql += "    AND  AD.\"AcctCode\" in ('T10','T11','T12','T13','TAM','TAV','TCK','TEM','THC','TLD','TMI','TRO','TSL') ";
		sql += "    AND  AD.\"EntAc\" > 0";
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
	 * L9134 放款暫收款對帳明細表 名稱不見了
	 * 
	 * @param titaVo TitaVo
	 * @return 查詢結果
	 */
	public List<Map<String, String>> doQueryL9134_3(TitaVo titaVo) {

		String sql = " ";
		sql += "SELECT * ";
		sql += "FROM ( ";
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
		sql += "     LEFT JOIN \"CdAcCode\" C ON C.\"AcctCode\" = AC.\"AcctCode\"";
		sql += "     LEFT JOIN \"CdCode\" CC ON CC.\"DefCode\" = 'AcSubBookCode' ";
		sql += "                            AND CC.\"Code\" = AC.\"AcSubBookCode\" ";
		sql += "     LEFT JOIN \"CollList\" CO ON CO.\"CustNo\" = AC.\"CustNo\" ";
		sql += "                              AND CO.\"FacmNo\" = AC.\"FacmNo\" ";
		sql += "     where  AC.\"AcctCode\" in ('T10','T11','T12','T13','TAM','TAV','TCK','TEM','THC','TLD','TRO','TSL') ";
		sql += "       AND AC.\"RvBal\" > 0 ";		
		sql += "     UNION ALL ";
		sql += "     SELECT C.\"AcNoItem\"";
		sql += "          , C.\"AcNoCode\"";
		sql += "          , C.\"AcSubCode\"";
		sql += "          , C.\"AcDtlCode\"";
		sql += "          , S1.\"CustNo\"";
		sql += "          , S1.\"FacmNo\"";
		sql += "          , S1.\"InsuStartDate\" AS \"OpenAcDate\"";
		sql += "          , S1.\"AcDate\"        AS \"LastTxDate\"";
		sql += "          , S1.\"TotInsuPrem\"   AS \"AcBal\"";
		sql += "          , S1.\"TotInsuPrem\"   AS \"RvBal\"";
		sql += "          , CC.\"Item\"";
		sql += "          , ' '                  AS \"RenewCode\"";
		sql += "     FROM \"InsuRenew\" S1 ";
		sql += "     LEFT JOIN \"SystemParas\" SP ON SP.\"BusinessType\" = 'LN' ";
		sql += "     LEFT JOIN \"CdAcCode\" C ON C.\"AcctCode\" = 'TMI' ";
		sql += "     LEFT JOIN \"CdCode\" CC ON CC.\"DefCode\" = 'AcSubBookCode' ";
		sql += "                            AND CC.\"Code\" = '00A' ";
		sql += "    where  S1.\"RenewCode\" = 2";
		sql += "       AND S1.\"StatusCode\" = 0 ";		
		sql += "       AND S1.\"AcDate\" > 0 ";		
		sql += "       AND S1.\"InsuYearMonth\"  >= trunc(SP.\"InsuSettleDate\" / 100) ";		
		sql += "    ) ";		
		sql += "     ORDER BY \"AcNoCode\" ASC ";
		sql += "            , \"AcSubCode\" ASC ";
		sql += "            , \"AcDtlCode\" ASC ";
		sql += "            , \"CustNo\" ASC ";
		sql += "            , \"FacmNo\" ASC ";

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
	 * @param iLmnDy 上個月底日(民國)
	 * @param iTmnDy 本營業日(民國)
	 * @return 查詢結果
	 * @throws LogicException
	 */
	public List<Map<String, String>> doQueryL9134_4(TitaVo titaVo, int iLmnDy, int iTmnDy) throws LogicException {
		this.info("doQueryL9134_4 Start");

		// 本營業日 民國年月(YYYMM)
		String rocYM = iTmnDy / 100 + "";

		// 民國年月(YYY/MM)
		rocYM = rocYM.substring(0, 3) + "/" + rocYM.substring(3, 5);

		// 西元年月日
		String bcYMD = iTmnDy + 19110000 + "";

		// 西元年月
		String bcYM = bcYMD.substring(0, 6);

		// 上個月底日 民國(YYYMMDD)
		String ixLmnDy = parse.IntegerToString(iLmnDy, 7);
		// 上個月底日 民國(YYY/MM/DD)
		String lastMonthEndDay = ixLmnDy.substring(0, 3) + "/" + ixLmnDy.substring(3, 5) + "/"
				+ ixLmnDy.substring(5, 7);


		// for迴圈使用長度
		String ixiTmnDy = parse.IntegerToString(iTmnDy, 7);
		int length = parse.stringToInteger(ixiTmnDy.substring(5, 7));


		String sql = " ";
		sql += " select \"AcDate\" ";
		sql += " , sum(\"TdBal\") as \"TdBal\"   ";
		sql += " , sum(\"TdBal\") - sum(\"YdBal\")  as \"DifTdBal\"  ";
		sql += " , sum(\"didTdal\") as \"didTdBal\"  ";
		sql += " ,  sum(\"didTdal\")- sum(\"didYdBal\")  as \"didDifTdBal\" ";
		sql += " , sum(\"DrAmt\") as \"DrAmt\" , sum(\"CrAmt\") as \"CrAmt\" ";
		sql += " from ";
		sql += " ( ";
		sql += " select";
		sql += "  :lastMonthEnd  as \"AcDate\" ";
		sql += "  ,sum(\"YdBal\") as \"YdBal\" ";
		sql += "  ,sum(\"TdBal\") as \"TdBal\" ";
		sql += "  ,0 as \"didYdBal\"";
		sql += "  ,0 as \"didTdal\" ";
		sql += " , 0 as \"DrAmt\" , 0 as \"CrAmt\" ";
		sql += " from \"AcMain\" ";
		sql += " where \"AcDate\" = :acdate ";// 抓上個月的月底日
		sql += "   AND  \"AcctCode\" in ('TAV','T10') ";
		sql += " union all ";
		sql += " select ";
		sql += " :lastMonthEnd as \"AcDate\" ";
		sql += " , 0 as \"YdBal\" ";
		sql += " , 0 as \"TdBal\" ";
		sql += " ,sum(\"YdBal\") as \"didYdBal\" ";
		sql += " ,sum(\"TdBal\") as \"didTdal\" ";
		sql += " ,0 as \"DrAmt\" , 0 as \"CrAmt\" ";
		sql += " from \"AcMain\" ";
		sql += " where \"AcDate\" = :acdate ";// 抓上個月的月底日
		sql += " and \"AcctCode\" in ('TCK')  ";
		for (int i = 1; i <= length; i++) {
			sql += " union all ";
			sql += " select ";
			sql += " '" + rocYM + "" + '/' + "" + parse.IntegerToString(i, 2) + "' as \"AcDate\" ";
			sql += " ,0 as \"YdBal\" ";
			sql += " ,0 as \"TdBal\" ";
			sql += " ,0 as \"didYdBal\" ";
			sql += " ,0 as \"didTdal\" ";
			sql += " ,0 as \"DrAmt\" , 0 as \"CrAmt\" ";
			sql += " from dual ";
			sql += " union all ";
			sql += " select  ";
			sql += " '" + rocYM + "" + '/' + "" + parse.IntegerToString(i, 2) + "'  as \"AcDate\" ";
			sql += " ,sum(\"YdBal\") as \"YdBal\" ";
			sql += " ,sum(\"TdBal\") as \"TdBal\" ";
			sql += " , 0 as \"didYdBal\" ";
			sql += " , 0 as \"didTdal\" ";
			sql += " , 0 as \"DrAmt\" , 0 as \"CrAmt\" ";
			sql += " from \"AcMain\" ";
			sql += " where \"AcDate\" = " + bcYM + parse.IntegerToString(i, 2) + "";
			sql += "   AND  \"AcctCode\" in ('TAV','T10') ";
			sql += " union all ";
			sql += " select ";
			sql += " '" + rocYM + "" + '/' + "" + parse.IntegerToString(i, 2) + "'  as \"AcDate\" ";
			sql += " ,0 as \"YdBal\" ";
			sql += " ,0 as \"TdBal\" ";
			sql += " , sum(\"YdBal\") as \"didYdBal\"";
			sql += " , sum(\"TdBal\") as \"didTdal\"";
			sql += " ,0 as \"DrAmt\" , 0 as \"CrAmt\" ";
			sql += " from \"AcMain\" ";
			sql += " where \"AcctCode\" in ('TCK') ";
			sql += " and \"AcDate\" = " + bcYM + parse.IntegerToString(i, 2) + " ";
			sql += " union all ";
			sql += " select ";
			sql += " '" + rocYM + "" + '/' + "" + parse.IntegerToString(i, 2) + "' as \"AcDate\" ";
			sql += " ,0 as \"YdBal\" ";
			sql += " ,0 as \"TdBal\" ";
			sql += " ,0 as \"didYdBal\" ";
			sql += " ,0 as \"didTdal\" ";
			sql += " ,sum(\"DrAmt\") as \"DrAmt\" , sum(\"CrAmt\") as \"CrAmt\" ";
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
			sql += " WHERE AD.\"AcDate\" >=" + bcYM + parse.IntegerToString(i, 2) + " ";
			sql += "   AND AD.\"AcDate\" <=" + bcYM + parse.IntegerToString(i, 2) + " ";
			sql += "   AND  AD.\"AcctCode\" in ('TAV','TCK','T10') ";
			sql += "   AND AD.\"EntAc\" > 0 ";
			sql += " ) ";

		}
		sql += " ) ";
		sql += " group by \"AcDate\" ";
		sql += " order by \"AcDate\" ";
		this.info("doQueryL9134_4 sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("acdate", iLmnDy + 19110000);
		query.setParameter("lastMonthEnd", lastMonthEndDay);
		return this.convertToMap(query);
	}

}