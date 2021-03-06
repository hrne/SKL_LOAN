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
import com.st1.itx.eum.ContentName;

@Service("lB096ServiceImpl")
@Repository

/*
 * LB096 不動產擔保品明細-地號附加檔
 */

public class LB096ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int dataMonth, TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LB096.findAll ---------------");
		this.info("-----LB096 TitaVo=" + titaVo);
		this.info("-----LB096 Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		String sql = "";

		// LB096 不動產擔保品明細-地號附加檔
		sql = "SELECT M.\"DataType\"" + "     , M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"Filler4\"" + "     , M.\"ClActNo\"" + "     , M.\"OwnerId\"" + "     , M.\"CityJCICCode\""
				+ "     , M.\"AreaJCICCode\"" + "     , M.\"IrCode\"" + "     , M.\"LandNo1\"" + "     , M.\"LandNo2\"" + "     , M.\"LandCode\"" + "     , M.\"Area\"" + "     , M.\"LandZoningCode\""
				+ "     , M.\"LandUsageType\"" + "     , M.\"PostedLandValue\"" + "     , M.\"PostedLandValueYearMonth\"" + "     , M.\"Filler18\"" + "     , M.\"JcicDataYM\""
				+ " FROM  \"JcicB096\" M" + " WHERE M.\"DataYM\" = :dataMonth " + " ORDER BY M.\"ClActNo\", \"CityJCICCode\", \"AreaJCICCode\", \"IrCode\", \"LandNo1\", \"LandNo2\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LB096.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("dataMonth", dataMonth);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}
