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

@Service("lBRelServiceImpl")
@Repository

/*
 * LBRel 聯徵授信「同一關係企業及集團企業」資料報送檔
 */

public class LBRelServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int dataMonth, TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LBRel.findAll ---------------");
		this.info("-----LBRel TitaVo=" + titaVo);
		this.info("-----LBRel Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		String sql = "";

		// LBRel 聯徵授信「同一關係企業及集團企業」資料報送檔
		sql = "SELECT " + "  \"BankItem\", \"BranchItem\", \"RelYM\", \"TranCode\" "
				+ ", \"CustId\", \"Filler6\", \"RelId\", \"Filler8\", \"RelationCode\" "
				+ ", \"Filler10\", \"EndCode\" " + " FROM  \"JcicRel\" " + " WHERE \"DataYMD\" = :dataMonth "
				+ " ORDER BY \"BankItem\", \"BranchItem\", \"TranCode\", \"CustId\", \"RelId\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LBRel.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("dataMonth", dataMonth);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}
