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
/* 逾期放款明細 */
public class LM017ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("lM017.findAll ");
		String sql = "SELECT \"F1\"";
		sql += "            , SUM(F2)";
		sql += "            , SUM(F3)";
		sql += "        FROM ( SELECT DECODE(Cl.\"CityCode\",'05', 1, '10', 2, '15', 3, '35', 4, '65', 5, '70', 6, 7) AS F1";
		sql += "                    , 1 AS F2";
		sql += "                    , M.\"LoanBal\" F3";
		sql += "               FROM \"LoanBorMain\" M";
		sql += "               LEFT JOIN \"FacMain\" F ON F.\"CustNo\"  = M.\"CustNo\" ";
		sql += "                                      AND F.\"FacmNo\"  = M.\"FacmNo\" ";
		sql += "               LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = M.\"CustNo\"";
		sql += "                                     AND CF.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                     AND CF.\"MainFlag\" = 'Y'";
		sql += "               LEFT JOIN \"ClMain\" Cl ON Cl.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                                      AND Cl.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                                      AND Cl.\"ClNo\"    = CF.\"ClNo\" ";
		sql += "               WHERE  M.\"Status\"= 0 AND F.\"ProdNo\" IN ('ER','ES'))";
		sql += "        GROUP BY \"F1\"";
		sql += "        ORDER BY \"F1\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query.getResultList());
	}

}