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

@Service("lB090ServiceImpl")
@Repository

/*
 * LB090 擔保品關聯檔資料檔
 */

public class LB090ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int dataMonth, TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LB090.findAll ---------------");
		this.info("-----LB090 TitaVo=" + titaVo);
		this.info("-----LB090 Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		String sql = "";

		// LB090 擔保品關聯檔資料檔
		sql = "SELECT M.\"DataType\"" + "     , M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"Filler4\"" + "     , M.\"CustId\"" + " , M.\"ClActNo\"" + "     , M.\"FacmNo\""
				+ " , M.\"GlOverseas\"" + "   , M.\"JcicDataYM\"" + " FROM  \"JcicB090\" M" + " WHERE M.\"DataYM\" = :dataMonth " + " ORDER BY M.\"ClActNo\", M.\"FacmNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LB090.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("dataMonth", dataMonth);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}
