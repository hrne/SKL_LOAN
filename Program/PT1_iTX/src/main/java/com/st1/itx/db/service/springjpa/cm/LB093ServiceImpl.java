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

@Service("lB093ServiceImpl")
@Repository

/*
 * LB093 動產及貴重物品擔保品明細檔
 */

public class LB093ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int dateMonth, TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LB093.findAll ---------------");
		this.info("-----LB093 TitaVo=" + titaVo);
		this.info("-----LB093 Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));
		
		String sql = "";

		// LB093 動產及貴重物品擔保品明細檔
		sql = "SELECT M.\"DataType\"" + "     , M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"Filler4\"" + "     , M.\"ClActNo\"" + "     , M.\"ClTypeJCIC\"" + "     , M.\"OwnerId\""
				+ "     , M.\"EvaAmt\"" + "     , M.\"EvaDate\"" + "     , M.\"LoanLimitAmt\"" + "     , M.\"SettingDate\"" + "     , M.\"MonthSettingAmt\"" + "     , M.\"SettingSeq\""
				+ "     , M.\"SettingAmt\"" + "     , M.\"PreSettingAmt\"" + "     , M.\"DispPrice\"" + "     , M.\"IssueEndDate\"" + "     , M.\"InsuFg\"" + "     , M.\"Filler19\""
				+ "     , M.\"JcicDataYM\"" + " FROM  \"JcicB093\" M" + " WHERE M.\"DataYM\" = :dateMonth " + " ORDER BY M.\"ClActNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LB093.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("dateMonth", dateMonth);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}
