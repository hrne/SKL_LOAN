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

@Service("lB085ServiceImpl")
@Repository

/*
 * LB085 帳號轉換資料檔
 */

public class LB085ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LB085.findAll ---------------");
		this.info("-----LB085 TitaVo=" + titaVo);
		this.info("-----LB085 Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		int dateMonth = Integer.parseInt(titaVo.getEntDy().substring(0, 6)) + 191100; // 年月份(西元年月)

		this.info("dataMonth= " + dateMonth);

		String sql = "";

		// LB085 帳號轉換資料檔
		sql = "SELECT M.\"DataType\"" + "     , M.\"RenewYM\"" + "     , M.\"CustId\"" + "     , M.\"BefBankItem\""
				+ "     , M.\"BefBranchItem\"" + "     , M.\"Filler6\"" + "     , M.\"BefAcctNo\""
				+ "     , M.\"AftBankItem\"" + "     , M.\"AftBranchItem\"" + "     , M.\"Filler10\""
				+ "     , M.\"AftAcctNo\"" + "     , M.\"Filler12\"" + " FROM  \"JcicB085\" M"
				+ " WHERE M.\"DataYM\" = :dateMonth " + " ORDER BY M.\"BefAcctNo\", M.\"AftAcctNo\" ";
//				+ " ORDER BY M.\"RenewYM\", M.\"CustId\", M.\"BefAcctNo\", M.\"AftAcctNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LB085.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("dateMonth", dateMonth); 

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}
