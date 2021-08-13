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
/* 逾期放款明細 */
public class LM041ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM041ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		logger.info("lM041.findAll ");

		String entdy = String.valueOf((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000));
                String yearMonth = entdy.substring(0,6);

		String sql = "SELECT CT.\"CityItem\"";
		sql += "            ,M.\"CityCode\"";
		sql += "            ,A.\"CustNo\"";
		sql += "            ,A.\"FacmNo\"";
		sql += "            ,M.\"Status\"";
		sql += "            ,C.\"CustName\"";
		sql += "            ,A.\"RvBal\"";
		sql += "            ,M.\"LawFee\"";
		sql += "            ,M.\"AcctFee\"";
		sql += "      FROM \"AcReceivable\" A";
		sql += "      LEFT JOIN \"MonthlyFacBal\" M ON M.\"YearMonth\" = :yearMonth";
		sql += "                                   AND M.\"CustNo\"    =  A.\"CustNo\"";
		sql += "                                   AND M.\"FacmNo\"    =  A.\"FacmNo\"";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = A.\"CustNo\"";
		sql += "      LEFT JOIN \"CdCity\" CT ON CT.\"CityCode\" = M.\"CityCode\"";
		sql += "      WHERE A.\"AcctCode\" = 'TAV'";
		sql += "          AND A.\"RvBal\" > 0";
		sql += "          AND M.\"AcctCode\" = '990'";
                sql += "          AND A.\"OpenAcDate\" = :entdy";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
                query.setParameter("yearMonth", yearMonth);
		return this.convertToMap(query.getResultList());
	}

}