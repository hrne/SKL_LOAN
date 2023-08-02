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
public class BS996ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	// 業績已調整清單
	public List<Map<String, String>> findAdjustList(int inputStartDate, TitaVo titaVo) {

		this.info(" findAdjustList = " + inputStartDate);

		String sql = " ";
		sql += " SELECT \"CustNo\"               ";
		sql += "      , \"FacmNo\"               ";
		sql += "      , \"BormNo\"               ";
		sql += "      , MAX(\"IsPieceCodeAdjust\") AS  \"IsPieceCodeAdjust\""; // 計件代碼已調整
		sql += "      , MAX(\"IsItDetailAdjust\") AS  \"IsItDetailAdjust\""; // 介紹人業績已調整
		sql += "      , MAX(\"IsBsDetailAdjust\") AS  \"IsBsDetailAdjust\""; // 房貸專員業績已調整
		sql += " FROM (                          ";
		sql += "       SELECT PI.\"CustNo\"      ";
		sql += "            , PI.\"FacmNo\"      ";
		sql += "            , PI.\"BormNo\"      ";
		sql += "            , CASE WHEN PI.\"PieceCode\" <> LN.\"PieceCode\"  THEN 'Y' ELSE ' ' END AS \"IsPieceCodeAdjust\" ";
		sql += "            , CASE WHEN PI.\"AdjRange\" > 0  THEN 'Y' ELSE ' ' END  AS \"IsItDetailAdjust\" ";
		sql += "            , ' ' AS \"IsBsDetailAdjust\" ";
		sql += "       FROM \"PfItDetail\" PI   ";
		sql += "       LEFT JOIN \"LoanBorMain\" LN ON LN.\"CustNo\" = PI.\"CustNo\" ";
		sql += "                                   AND LN.\"FacmNo\" = PI.\"FacmNo\" ";
		sql += "                                   AND LN.\"BormNo\" = PI.\"BormNo\" ";
		sql += "       WHERE PI.\"PerfDate\" >= :inputstartdate ";
		sql += "         AND (   PI.\"AdjRange\" > 0   "; // 介紹人業績已調整
		sql += "              OR PI.\"PieceCode\" <> LN.\"PieceCode\" ) "; // 計件代碼已調整
		sql += "       UNION ALL               ";
		sql += "       SELECT BS.\"CustNo\"     ";
		sql += "            , BS.\"FacmNo\"     ";
		sql += "            , BS.\"BormNo\"     ";
		sql += "            , ' ' AS \"IsPieceCodeAdjust\" ";
		sql += "            , ' ' AS \"IsItDetailAdjust\" ";
		sql += "            , CASE WHEN BS.\"AdjPerfCnt\" <> 0  OR BS.\"AdjPerfAmt\" <> 0 THEN 'Y' ELSE ' ' END AS \"IsBsDetailAjust\" "; // 已調整
		sql += "       FROM \"PfBsDetail\" BS   ";
		sql += "       WHERE BS.\"PerfDate\" >= :inputstartdate ";
		sql += "         AND (   BS.\"AdjPerfCnt\" <> 0  OR BS.\"AdjPerfAmt\" <> 0 ) "; // 房貸專員業績已調整
		sql += "      )                         ";
		sql += " GROUP BY \"CustNo\"            ";
		sql += "        , \"FacmNo\"            ";
		sql += "        , \"BormNo\"            ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputstartdate", inputStartDate);

		return this.convertToMap(query);
	}

}