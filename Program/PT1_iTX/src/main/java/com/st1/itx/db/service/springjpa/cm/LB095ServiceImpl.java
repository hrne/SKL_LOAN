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

@Service("lB095ServiceImpl")
@Repository

/*
 * LB095 不動產擔保品明細-建號附加檔
 */

public class LB095ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int dataMonth, TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LB095.findAll ---------------");
		this.info("-----LB095 TitaVo=" + titaVo);
		this.info("-----LB095 Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		String sql = "";

		// LB095 不動產擔保品明細-建號附加檔
		sql = "SELECT M.\"DataType\"" + "     , M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"Filler4\"" + "     , M.\"ClActNo\"" + "     , M.\"OwnerId\"" + "     , M.\"CityJCICCode\""
				+ "     , M.\"AreaJCICCode\"" + "     , M.\"IrCode\"" + "     , M.\"BdNo1\"" + "     , M.\"BdNo2\"" + "     , M.\"CityName\"" + "     , M.\"AreaName\"" + "     , M.\"Addr\""
				+ "     , M.\"BdMainUseCode\"" + "     , M.\"BdMtrlCode\"" + "     , M.\"BdSubUsageCode\"" + "     , M.\"TotalFloor\"" + "     , M.\"FloorNo\"" + "     , M.\"BdDate\""
				+ "     , M.\"TotalArea\"" + "     , M.\"FloorArea\"" + "     , M.\"BdSubArea\"" + "     , M.\"PublicArea\"" + "     , M.\"Filler33\"" + "     , M.\"JcicDataYM\""
				+ " FROM  \"JcicB095\" M" + " WHERE M.\"DataYM\"  = :dataMonth " + " ORDER BY M.\"ClActNo\", \"CityJCICCode\", \"AreaJCICCode\", \"IrCode\", \"BdNo1\", \"BdNo2\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LB095.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("dataMonth", dataMonth);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}
