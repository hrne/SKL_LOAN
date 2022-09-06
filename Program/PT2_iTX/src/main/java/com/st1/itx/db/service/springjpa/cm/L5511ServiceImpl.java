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

import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("L5511ServiceImpl")
@Repository
public class L5511ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L5511ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;
	@Autowired
	private CdBcmService sCdBcmService;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	/**
	 * 
	 * @param titaVo titaVo
	 * @param workYM 工作月
	 * @return data
	 * @throws Exception 例外
	 */
	public List<Map<String, String>> FindData(TitaVo titaVo, int workYM) throws Exception {
		logger.info("L5511ServiceImpl workYM=" + workYM);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		String sql = "select a.\"PieceCode\",a.\"ProdCode\",a.\"EmployeeNo\",NVL(d.\"Fullname\",'') as \"Fullname\",a.\"CustNo\",a.\"FacmNo\",a.\"BormNo\",a.\"BonusType\",NVL(e.\"CustName\",'') as \"CustName\",NVL(b.\"DrawdownDate\",0) as \"DrawdownDate\",NVL(b.\"DrawdownAmt\",0) as \"DrawdownAmt\",a.\"AdjustBonus\",NVL(f.\"AcSubBookCode\",'') as \"AcSubBookCode\" ";
		sql += "from \"PfRewardMedia\" a ";
		sql += "left join \"PfItDetail\" b on b.\"PerfDate\" = a.\"PerfDate\" and b.\"CustNo\"=a.\"CustNo\" and b.\"FacmNo\"=a.\"FacmNo\" and b.\"BormNo\"=a.\"BormNo\" and a.\"BonusType\" != 6 and b.\"DrawdownAmt\" > 0 and b.\"RepayType\" = 0 ";
		sql += "left join \"FacMain\" c on c.\"CustNo\"=a.\"CustNo\" and c.\"FacmNo\"=a.\"FacmNo\" and a.\"BonusType\" != 6 " + "left join \"CdEmp\" d on d.\"EmployeeNo\"=a.\"EmployeeNo\" ";
		sql += "left join \"CustMain\" e on e.\"CustNo\"=a.\"CustNo\" and a.\"BonusType\" != 6 ";
		sql += "left join (select distinct \"CustNo\",first_value(\"AcSubBookCode\") over (partition by \"CustNo\" order by \"LastUpdate\" desc) \"AcSubBookCode\" from \"AcReceivable\") f on f.\"CustNo\"=a.\"CustNo\" and a.\"BonusType\" != 6 ";
//				"left join \"FacProd\" f on f.\"ProdNo\"=a.\"ProdCode\" " +
		sql += "where a.\"WorkMonth\"=:workMonth and a.\"BonusType\" != 7 " + "order by a.\"EmployeeNo\",a.\"PieceCode\",a.\"CustNo\",a.\"FacmNo\",a.\"BormNo\" ";

		query = em.createNativeQuery(sql);
		query.setParameter("workMonth", workYM);

		logger.info("L5053ServiceImpl sql=[" + sql + "]");
		logger.info("L5051Service FindData=" + query.toString());

		return this.convertToMap(query);
	}

}