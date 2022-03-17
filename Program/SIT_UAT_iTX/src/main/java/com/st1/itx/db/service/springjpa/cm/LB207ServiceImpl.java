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

@Service("lB207ServiceImpl")
@Repository

/*
 * LB207 授信戶基本資料檔
 */

public class LB207ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int dataMonth, TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LB207.findAll ---------------");
		this.info("-----LB207 TitaVo=" + titaVo);
		this.info("-----LB207 Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		String sql = "";

		// LB207 授信戶基本資料檔
		sql = "SELECT M.\"TranCode\"" + "     , M.\"BankItem\"" + "     , M.\"Filler3\"" + "     , M.\"DataDate\"" + "     , M.\"CustId\"" + "     , M.\"CustName\"" + "     , M.\"EName\""
				+ "     , M.\"Birthday\"" + "     , M.\"RegAddr\"" + "     , M.\"CurrZip\"" + "     , M.\"CurrAddr\"" + "     , M.\"Tel\"" + "     , M.\"Mobile\"" + "     , M.\"Filler14\""
				+ "     , M.\"EduCode\"" + "     , M.\"OwnedHome\"" + "     , M.\"CurrCompName\"" + "     , M.\"CurrCompId\"" + "     , M.\"JobCode\"" + "     , M.\"CurrCompTel\""
				+ "     , M.\"JobTitle\"" + "     , M.\"JobTenure\"" + "     , M.\"IncomeOfYearly\"" + "     , M.\"IncomeDataDate\"" + "     , M.\"Sex\"" + "     , M.\"NationalityCode\""
				+ "     , M.\"PassportNo\"" + "     , M.\"PreTaxNo\"" + "     , M.\"FullCustName\"" + "     , M.\"Filler30\"" + " FROM  \"JcicB207\" M" + " WHERE M.\"DataYM\" = :dataMonth "
				+ " ORDER BY M.\"BankItem\", M.\"CustId\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LB207.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("dataMonth", dataMonth);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}
