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

@Service("lB092ServiceImpl")
@Repository

/*
 * LB092 不動產擔保品明細檔
 */

public class LB092ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int dataMonth, TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LB092.findAll ---------------");
		this.info("-----LB092 TitaVo=" + titaVo);
		this.info("-----LB092 Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		String sql = "";

		// LB092 不動產擔保品明細檔
		sql = "SELECT M.\"DataType\"" + "     , M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"Filler4\""
				+ "     , M.\"ClActNo\"" + "     , M.\"ClTypeJCIC\"" + "     , M.\"OwnerId\"" + "     , M.\"EvaAmt\""
				+ "     , M.\"EvaDate\"" + "     , M.\"LoanLimitAmt\"" + "     , M.\"SettingDate\""
				+ "     , M.\"MonthSettingAmt\"" + "     , M.\"SettingSeq\"" + "     , M.\"SettingAmt\""
				+ "     , M.\"PreSettingAmt\"" + "     , M.\"DispPrice\"" + "     , M.\"IssueEndDate\""
				+ "     , M.\"CityJCICCode\"" + "     , M.\"AreaJCICCode\"" + "     , M.\"IrCode\""
				+ "     , M.\"LandNo1\"" + "     , M.\"LandNo2\"" + "     , M.\"BdNo1\"" + "     , M.\"BdNo2\""
				+ "     , M.\"Zip\"" + "     , M.\"InsuFg\"" + "     , M.\"LVITax\"" + "     , M.\"LVITaxYearMonth\""
				+ "     , M.\"ContractPrice\"" + "     , M.\"ContractDate\"" + "     , M.\"ParkingTypeCode\""
				+ "     , M.\"Area\"" + "     , M.\"LandOwnedArea\"" + "     , M.\"BdTypeCode\""
				+ "     , M.\"Filler33\"" + "     , M.\"JcicDataYM\"" + " FROM  \"JcicB092\" M"
				+ " WHERE M.\"DataYM\" = :dataMonth "
				+ " ORDER BY M.\"ClActNo\",  M.\"OwnerId\",  M.\"CityJCICCode\",  M.\"AreaJCICCode\",  M.\"IrCode\",  M.\"LandNo1\",  M.\"LandNo2\",  M.\"BdNo1\",  M.\"BdNo2\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LB092.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("dataMonth", dataMonth);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}
