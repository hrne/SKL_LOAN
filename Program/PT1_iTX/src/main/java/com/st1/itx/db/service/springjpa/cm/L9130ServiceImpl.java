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
public class L9130ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> doQuery(int acDate, int slipBatNo, TitaVo titaVo) throws Exception {

		String sql = " ";
		sql += " SELECT RESULT.\"AcDate\" ";
		sql += "      , RESULT.\"SlipBatNo\" ";
		sql += "      , RESULT.\"AcBookCode\" ";
		sql += "      , RESULT.\"AcNoCode\" ";
		sql += "      , RESULT.\"AcSubCode\" ";
		sql += "      , RESULT.\"DbCr\" ";
		sql += "      , RESULT.\"TxAmt\" ";
		sql += "      , RESULT.\"SlipNote\" ";
		sql += "      , RESULT.\"RvNo\" ";
		sql += "      , RESULT.\"AcSubBookCode\" "; // 區隔帳冊
		sql += " FROM ( ";
		sql += "     SELECT S0.\"AcDate\" ";
		sql += "          , S0.\"SlipBatNo\" ";
		sql += "          , S0.\"AcBookCode\" ";
		sql += "          , S0.\"AcNoCode\" ";
		sql += "          , S0.\"AcSubCode\" ";
		sql += "          , S0.\"DbCr\" ";
		sql += "          , SUM(S0.\"TxAmt\")    AS \"TxAmt\" ";
		sql += "          , MAX(S0.\"SlipNote\") AS \"SlipNote\" ";
		sql += "          , S0.\"RvNo\" ";
		sql += "          , S0.\"AcSubBookCode\" "; // 區隔帳冊
		sql += "     FROM ( ";
		sql += "         SELECT ACD.\"AcDate\" ";
		sql += "              , ACD.\"SlipBatNo\" ";
		sql += "              , NVL(TRIM(ACD.\"AcBookCode\"), '000') AS \"AcBookCode\" ";
		sql += "              , CDAC.\"AcNoCodeOld\" AS \"AcNoCode\" ";
		sql += "              , ACD.\"AcSubCode\" ";
		sql += "              , ACD.\"DbCr\" ";
		sql += "              , ACD.\"TxAmt\" ";
		sql += "              , N' '                                AS \"SlipNote\" ";
		sql += "              , ' '                                 AS \"RvNo\" ";
		sql += "              , ACD.\"AcSubBookCode\" "; // 區隔帳冊
		sql += "         FROM \"AcDetail\" ACD ";
		sql += "         LEFT JOIN \"CdAcCode\" CDAC ON CDAC.\"AcNoCode\" = ACD.\"AcNoCode\" ";
		sql += "                                    AND CDAC.\"AcSubCode\" = ACD.\"AcSubCode\" ";
		sql += "                                    AND CDAC.\"AcDtlCode\" = ACD.\"AcDtlCode\" ";
		sql += "         WHERE ACD.\"AcDate\" = :acDate ";
		sql += "           AND ACD.\"SlipBatNo\" = :slipBatNo ";
		sql += "           AND ACD.\"ReceivableFlag\" <> '8' ";
		sql += "         UNION ALL ";
		sql += "         SELECT ACD.\"AcDate\" ";
		sql += "              , ACD.\"SlipBatNo\" ";
		sql += "              , '000'  AS \"AcBookCode\" ";
		sql += "              , CDAC.\"AcNoCodeOld\" AS \"AcNoCode\" ";
		sql += "              , ACD.\"AcSubCode\" ";
		sql += "              , ACD.\"DbCr\" ";
		sql += "              , ACD.\"TxAmt\" ";
		sql += "              , N' '   AS \"SlipNote\" ";
		sql += "              , ' '    AS \"RvNo\" ";
		sql += "              , ACD.\"AcSubBookCode\" "; // 區隔帳冊
		sql += "         FROM \"AcDetail\" ACD ";
		sql += "         LEFT JOIN \"CdAcCode\" CDAC ON CDAC.\"AcNoCode\" = ACD.\"AcNoCode\" ";
		sql += "                                    AND CDAC.\"AcSubCode\" = ACD.\"AcSubCode\" ";
		sql += "                                    AND CDAC.\"AcDtlCode\" = ACD.\"AcDtlCode\" ";
		sql += "         WHERE ACD.\"AcDate\" = :acDate ";
		sql += "           AND ACD.\"SlipBatNo\" = :slipBatNo ";
		sql += "           AND ACD.\"ReceivableFlag\" <> '8' ";
		sql += "           AND NVL(TRIM(ACD.\"AcBookCode\"), '000') <> '000' ";
		sql += "           AND ACD.\"AcBookFlag\" <> '3' ";
		sql += "         UNION ALL ";
		sql += "         SELECT ACD.\"AcDate\" ";
		sql += "              , ACD.\"SlipBatNo\" ";
		sql += "              , NVL(TRIM(ACD.\"AcBookCode\"), '000') AS \"AcBookCode\" ";
		sql += "              , CDAC.\"AcNoCodeOld\" AS \"AcNoCode\" ";
		sql += "              , ACD.\"AcSubCode\" ";
		sql += "              , ACD.\"DbCr\" ";
		sql += "              , ACD.\"TxAmt\" ";
		sql += "              , ACD.\"SlipNote\" ";
		sql += "              , ACD.\"RvNo\" ";
		sql += "              , ACD.\"AcSubBookCode\" "; // 區隔帳冊
		sql += "         FROM \"AcDetail\" ACD ";
		sql += "         LEFT JOIN \"CdAcCode\" CDAC ON CDAC.\"AcNoCode\" = ACD.\"AcNoCode\" ";
		sql += "                                    AND CDAC.\"AcSubCode\" = ACD.\"AcSubCode\" ";
		sql += "                                    AND CDAC.\"AcDtlCode\" = ACD.\"AcDtlCode\" ";
		sql += "         WHERE ACD.\"AcDate\" = :acDate ";
		sql += "           AND ACD.\"SlipBatNo\"       = :slipBatNo ";
		sql += "           AND ACD.\"ReceivableFlag\"  = '8' ";
		sql += "         UNION ALL ";
		sql += "         SELECT ACD.\"AcDate\" ";
		sql += "              , ACD.\"SlipBatNo\" ";
		sql += "              , '000' AS \"AcBookCode\" ";
		sql += "              , CDAC.\"AcNoCodeOld\" AS \"AcNoCode\" ";
		sql += "              , ACD.\"AcSubCode\" ";
		sql += "              , ACD.\"DbCr\" ";
		sql += "              , ACD.\"TxAmt\" ";
		sql += "              , ACD.\"SlipNote\" ";
		sql += "              , ACD.\"RvNo\" ";
		sql += "              , ACD.\"AcSubBookCode\" "; // 區隔帳冊
		sql += "         FROM \"AcDetail\" ACD ";
		sql += "         LEFT JOIN \"CdAcCode\" CDAC ON CDAC.\"AcNoCode\" = ACD.\"AcNoCode\" ";
		sql += "                                    AND CDAC.\"AcSubCode\" = ACD.\"AcSubCode\" ";
		sql += "                                    AND CDAC.\"AcDtlCode\" = ACD.\"AcDtlCode\" ";
		sql += "         WHERE ACD.\"AcDate\" = :acDate ";
		sql += "           AND ACD.\"SlipBatNo\"       = :slipBatNo ";
		sql += "           AND ACD.\"ReceivableFlag\"  = '8' ";
		sql += "           AND NVL(TRIM(ACD.\"AcBookCode\"), '000') <> '000' ";
		sql += "           AND ACD.\"AcBookFlag\" <> '3' ";
		sql += "     ) S0 ";
		sql += "     GROUP BY S0.\"AcDate\" ";
		sql += "            , S0.\"SlipBatNo\" ";
		sql += "            , S0.\"AcBookCode\" ";
		sql += "            , S0.\"AcNoCode\" ";
		sql += "            , S0.\"AcSubCode\" ";
		sql += "            , S0.\"DbCr\" ";
		sql += "            , S0.\"RvNo\" ";
		sql += "            , S0.\"AcSubBookCode\" ";
		sql += " ) RESULT ";
		sql += " ORDER BY RESULT.\"AcDate\" ";
		sql += "        , RESULT.\"SlipBatNo\" ";
		sql += "        , RESULT.\"AcBookCode\" ";
		sql += "        , RESULT.\"AcSubBookCode\" "; // 區隔帳冊
		sql += "        , RESULT.\"AcNoCode\" ";
		sql += "        , RESULT.\"AcSubCode\" ";
		sql += "        , RESULT.\"DbCr\" DESC ";

		this.info("L9130ServiceImpl sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("acDate", acDate);
		query.setParameter("slipBatNo", slipBatNo);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> doQuery2022(int acDate, int slipBatNo, TitaVo titaVo) throws Exception {

		String sql = " ";
		sql += " SELECT ACD.\"AcBookCode\"                     "; // 帳冊別: 000:全公司
		sql += "      , ACD.\"AcDate\"                         "; // 會計日期
		sql += "      , ACD.\"AcNoCode\"                       "; // 科目代號
		sql += "      , ACD.\"AcSubCode\"                      "; // 子目代號
		sql += "      , ACD.\"DbCr\"                           "; // 借貸別
		sql += "      , SUM(NVL(ACD.\"TxAmt\",0)) AS \"TxAmt\" "; // 金額
		sql += "      , ACD.\"AcSubBookCode\"                  "; // 區隔帳冊
		sql += "      , RPAD(CDAC.\"AcNoItem\",40,' ') AS \"AcNoItem\" "; // 傳票摘要
		sql += "      , CASE ";
		sql += "          WHEN ACD.\"ReceivableFlag\" = '8' ";
		sql += "          THEN ACD.\"RvNo\" ";
		sql += "        ELSE ' ' END              AS \"AcReceivableCode\" "; // 銷帳碼
		sql += " FROM \"AcDetail\" ACD ";
		sql += " LEFT JOIN \"CdAcCode\" CDAC ON CDAC.\"AcNoCode\" = ACD.\"AcNoCode\" ";
		sql += "                            AND CDAC.\"AcSubCode\" = ACD.\"AcSubCode\" ";
		sql += "                            AND CDAC.\"AcDtlCode\" = ACD.\"AcDtlCode\" ";
		sql += " WHERE \"AcDate\" = :acDate ";
		sql += "   AND \"SlipBatNo\" = :slipBatNo ";
		sql += "   AND \"EntAc\" != 9 "; // 排除入總帳記號為9的資料
		sql += " GROUP BY ACD.\"AcDate\"        "; // 會計日期
		sql += "        , ACD.\"SlipBatNo\"     "; // 傳票批號
		sql += "        , ACD.\"AcBookCode\"    "; // 帳冊別: 000:全公司
		sql += "        , ACD.\"AcSubBookCode\" "; // 區隔帳冊
		sql += "        , ACD.\"AcNoCode\"      "; // 科目代號
		sql += "        , ACD.\"AcSubCode\"     "; // 子目代號
		sql += "        , ACD.\"DbCr\"          "; // 借貸別
		sql += "        , RPAD(CDAC.\"AcNoItem\",40,' ') "; // 傳票摘要
		sql += "        , CASE ";
		sql += "            WHEN ACD.\"ReceivableFlag\" = '8' ";
		sql += "            THEN ACD.\"RvNo\" ";
		sql += "          ELSE ' ' END "; // 銷帳碼
		sql += " ORDER BY ACD.\"AcDate\"        "; // 會計日期
		sql += "        , ACD.\"AcBookCode\"    "; // 帳冊別: 000:全公司
		sql += "        , ACD.\"AcSubBookCode\" "; // 區隔帳冊
		sql += "        , ACD.\"AcNoCode\"      "; // 科目代號
		sql += "        , ACD.\"AcSubCode\"     "; // 子目代號
		sql += "        , ACD.\"DbCr\" DESC     "; // 借貸別
		sql += "        , RPAD(CDAC.\"AcNoItem\",40,' ') "; // 傳票摘要
		sql += "        , CASE ";
		sql += "            WHEN ACD.\"ReceivableFlag\" = '8' ";
		sql += "            THEN ACD.\"RvNo\" ";
		sql += "          ELSE ' ' END "; // 銷帳碼

		this.info("L9130ServiceImpl sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("acDate", acDate);
		query.setParameter("slipBatNo", slipBatNo);

		return this.convertToMap(query);
	}
}