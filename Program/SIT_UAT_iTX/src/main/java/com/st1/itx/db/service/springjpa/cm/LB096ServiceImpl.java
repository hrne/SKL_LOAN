package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
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
import com.st1.itx.eum.ContentName;

@Service("lB096ServiceImpl")
@Repository

/*
 * LB096 不動產擔保品明細-地號附加檔
 */

public class LB096ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LB096ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List findAll(TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		logger.info("----------- LB096.findAll ---------------");
		logger.info("-----LB096 TitaVo=" + titaVo);
		logger.info("-----LB096 Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		int dateMonth = Integer.parseInt(titaVo.getEntDy().substring(0, 6)) + 191100; // 年月份(西元年月)

		// TEST
//		if (onLineMode == true) {
//			dateMonth = 202004;
//		}

		logger.info("dataMonth= " + dateMonth);

		String sql = "";

		// LB096 不動產擔保品明細-地號附加檔
		sql = "SELECT M.\"DataType\"" + "     , M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"Filler4\"" + "     , M.\"ClActNo\"" + "     , M.\"OwnerId\"" + "     , M.\"CityJCICCode\""
				+ "     , M.\"AreaJCICCode\"" + "     , M.\"IrCode\"" + "     , M.\"LandNo1\"" + "     , M.\"LandNo2\"" + "     , M.\"LandCode\"" + "     , M.\"Area\"" + "     , M.\"LandZoningCode\""
				+ "     , M.\"LandUsageType\"" + "     , M.\"PostedLandValue\"" + "     , M.\"PostedLandValueYearMonth\"" + "     , M.\"Filler18\"" + "     , M.\"JcicDataYM\""
				+ " FROM  \"JcicB096\" M" + " WHERE M.\"DataYM\" = " + dateMonth 
				+ " ORDER BY M.\"ClActNo\", \"CityJCICCode\", \"AreaJCICCode\", \"IrCode\", \"LandNo1\", \"LandNo2\"";

		logger.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode == true) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LB096.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}
