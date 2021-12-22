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

@Service("lB094ServiceImpl")
@Repository

/*
 * LB094 股票擔保品明細檔
 */

public class LB094ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LB094.findAll ---------------");
		this.info("-----LB094 TitaVo=" + titaVo);
		this.info("-----LB094 Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		int dateMonth = Integer.parseInt(titaVo.getEntDy().substring(0, 6)) + 191100; // 年月份(西元年月)

		this.info("dataMonth= " + dateMonth);

		String sql = "";

		// LB094 股票擔保品明細檔
		sql = "SELECT M.\"DataType\"" + "     , M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"Filler4\""
				+ "     , M.\"ClActNo\"" + "     , M.\"ClTypeJCIC\"" + "     , M.\"OwnerId\"" + "     , M.\"EvaAmt\""
				+ "     , M.\"EvaDate\"" + "     , M.\"LoanLimitAmt\"" + "     , M.\"SettingDate\""
				+ "     , M.\"CompanyId\"" + "     , M.\"CompanyCountry\"" + "     , M.\"StockCode\""
				+ "     , M.\"StockType\"" + "     , M.\"Currency\"" + "     , M.\"SettingBalance\""
				+ "     , M.\"LoanBal\"" + "     , M.\"InsiderJobTitle\"" + "     , M.\"InsiderPosition\""
				+ "     , M.\"LegalPersonId\"" + "     , M.\"DispPrice\"" + "     , M.\"Filler19\""
				+ "     , M.\"JcicDataYM\"" + " FROM  \"JcicB094\" M" + " WHERE M.\"DataYM\" = :dateMonth "
				+ " ORDER BY M.\"ClActNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LB094.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("dateMonth", dateMonth); 

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}
