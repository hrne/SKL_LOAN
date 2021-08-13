package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger logger = LoggerFactory.getLogger(L9130ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
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
		sql += "     FROM ( ";
		sql += "         SELECT ACD.\"AcDate\" ";
		sql += "              , ACD.\"SlipBatNo\" ";
		sql += "              , NVL(TRIM(ACD.\"AcBookCode\"), '000') AS \"AcBookCode\" ";
		sql += "              , ACD.\"AcNoCode\" ";
		sql += "              , ACD.\"AcSubCode\" ";
		sql += "              , ACD.\"DbCr\" ";
		sql += "              , ACD.\"TxAmt\" ";
		sql += "              , N' '                                AS \"SlipNote\" ";
		sql += "              , ' '                                 AS \"RvNo\" ";
		sql += "         FROM \"AcDetail\" ACD ";
		sql += "         WHERE \"AcDate\" = :acDate ";
		sql += "               AND \"SlipBatNo\" = :slipBatNo ";
		sql += "               AND \"ReceivableFlag\" <> '8' ";
		sql += "         UNION ALL ";
		sql += "         SELECT ACD.\"AcDate\" ";
		sql += "              , ACD.\"SlipBatNo\" ";
		sql += "              , '000'  AS \"AcBookCode\" ";
		sql += "              , ACD.\"AcNoCode\" ";
		sql += "              , ACD.\"AcSubCode\" ";
		sql += "              , ACD.\"DbCr\" ";
		sql += "              , ACD.\"TxAmt\" ";
		sql += "              , N' '   AS \"SlipNote\" ";
		sql += "              , ' '    AS \"RvNo\" ";
		sql += "         FROM \"AcDetail\" ACD ";
		sql += "         WHERE \"AcDate\" = :acDate ";
		sql += "               AND \"SlipBatNo\" = :slipBatNo ";
		sql += "               AND \"ReceivableFlag\" <> '8' ";
		sql += "               AND NVL(TRIM(ACD.\"AcBookCode\"), '000') <> '000' ";
		sql += "               AND ACD.\"AcBookFlag\" <> '3' ";
		sql += "         UNION ALL ";
		sql += "         SELECT ACD.\"AcDate\" ";
		sql += "              , ACD.\"SlipBatNo\" ";
		sql += "              , NVL(TRIM(ACD.\"AcBookCode\"), '000') AS \"AcBookCode\" ";
		sql += "              , ACD.\"AcNoCode\" ";
		sql += "              , ACD.\"AcSubCode\" ";
		sql += "              , ACD.\"DbCr\" ";
		sql += "              , ACD.\"TxAmt\" ";
		sql += "              , ACD.\"SlipNote\" ";
		sql += "              , ACD.\"RvNo\" ";
		sql += "         FROM \"AcDetail\" ACD ";
		sql += "         WHERE \"AcDate\" = :acDate ";
		sql += "               AND \"SlipBatNo\"       = :slipBatNo ";
		sql += "               AND \"ReceivableFlag\"  = '8' ";
		sql += "         UNION ALL ";
		sql += "         SELECT ACD.\"AcDate\" ";
		sql += "              , ACD.\"SlipBatNo\" ";
		sql += "              , '000' AS \"AcBookCode\" ";
		sql += "              , ACD.\"AcNoCode\" ";
		sql += "              , ACD.\"AcSubCode\" ";
		sql += "              , ACD.\"DbCr\" ";
		sql += "              , ACD.\"TxAmt\" ";
		sql += "              , ACD.\"SlipNote\" ";
		sql += "              , ACD.\"RvNo\" ";
		sql += "         FROM \"AcDetail\" ACD ";
		sql += "         WHERE \"AcDate\" = :acDate ";
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
		sql += " ) RESULT ";
		sql += " ORDER BY RESULT.\"AcDate\" ";
		sql += "        , RESULT.\"SlipBatNo\" ";
		sql += "        , RESULT.\"AcBookCode\" ";
		sql += "        , RESULT.\"AcNoCode\" ";
		sql += "        , RESULT.\"AcSubCode\" ";
		sql += "        , RESULT.\"DbCr\" DESC ";

		logger.info("L9130ServiceImpl sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("acDate", acDate);
		query.setParameter("slipBatNo", slipBatNo);

		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> doQuery2022(int acDate, int slipBatNo, TitaVo titaVo) throws Exception {

		String sql = " ";
		sql += " SELECT ACD.\"AcBookCode\"                     "; // 帳冊別: 000:全公司
		sql += "      , ACD.\"AcDate\"                         "; // 會計日期
		sql += "      , ACD.\"AcNoCode\"                       "; // 科目代號
		sql += "      , ACD.\"AcSubCode\"                      "; // 子目代號
		sql += "      , ACD.\"DbCr\"                           "; // 借貸別
		sql += "      , SUM(NVL(ACD.\"TxAmt\",0)) AS \"TxAmt\" "; // 金額
		sql += "      , ACD.\"AcSubBookCode\"                  "; // 區隔帳冊
		sql += " FROM \"AcDetail\" ACD ";
		sql += " WHERE \"AcDate\" = :acDate ";
		sql += "   AND \"SlipBatNo\" = :slipBatNo ";
		sql += " GROUP BY ACD.\"AcDate\"        "; // 會計日期
		sql += "        , ACD.\"SlipBatNo\"     "; // 傳票批號
		sql += "        , ACD.\"AcBookCode\"    "; // 帳冊別: 000:全公司
		sql += "        , ACD.\"AcSubBookCode\" "; // 區隔帳冊
		sql += "        , ACD.\"AcNoCode\"      "; // 科目代號
		sql += "        , ACD.\"AcSubCode\"     "; // 子目代號
		sql += "        , ACD.\"DbCr\"          "; // 借貸別
		sql += " ORDER BY ACD.\"AcDate\"        "; // 會計日期
		sql += "        , ACD.\"AcBookCode\"    "; // 帳冊別: 000:全公司
		sql += "        , ACD.\"AcSubBookCode\" "; // 區隔帳冊
		sql += "        , ACD.\"AcNoCode\"      "; // 科目代號
		sql += "        , ACD.\"AcSubCode\"     "; // 子目代號
		sql += "        , ACD.\"DbCr\" DESC     "; // 借貸別

		logger.info("L9130ServiceImpl sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("acDate", acDate);
		query.setParameter("slipBatNo", slipBatNo);

		return this.convertToMap(query.getResultList());
	}
}