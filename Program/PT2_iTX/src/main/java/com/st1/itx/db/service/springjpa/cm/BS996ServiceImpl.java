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

	public List<Map<String, String>> findAdjustList(int inputAcDate, TitaVo titaVo) {

		this.info("notToDoList inputWorkMonth = " + inputAcDate);

		String sql = " ";
		sql += " SELECT \"CustNo\"              ";  
		sql += "      , \"FacmNo\"              ";  
		sql += " FROM (                         ";
		sql += "       SELECT PI.\"CustNo\"     ";  
		sql += "            , PI.\"FacmNo\"     ";  
		sql += "       FROM \"PfItDetail\" PI   ";
		sql += "       LEFT JOIN \"LoanBorMain\" LN ON LN.\"CustNo\" = PI.\"CustNo\" ";
		sql += "                                   AND LN.\"FacmNo\" = PI.\"FacmNo\" ";
		sql += "                                   AND LN.\"BormNo\" = PI.\"BormNo\" ";
		sql += "       WHERE PI.\"PerfDate\" >= :inputAcDate ";
		sql += "         AND (   PI.\"AdjRange\" > 0   ";  // 已調整
		sql += "              OR PI.\"PieceCode\" <> LN.\"PieceCode\" ) ";  // 計件代碼已調整
		sql += "       UNION ALL               ";  
		sql += "       SELECT BS.\"CustNo\"     ";  
		sql += "            , BS.\"FacmNo\"     ";  
		sql += "       FROM \"PfBsDetail\" BS   ";
		sql += "       WHERE BS.\"PerfDate\" >= :inputAcDate ";
		sql += "         AND (   BS.\"AdjPerfCnt\" <> 0  OR BS.\"AdjPerfAmt\" <> 0 ) ";  // 已調整
    	sql += "      )                         "; 
		sql += " GROUP BY \"CustNo\"            ";
		sql += "        , \"FacmNo\"            "; 
		
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputAcDate", inputAcDate);

		return this.convertToMap(query);
	}
}