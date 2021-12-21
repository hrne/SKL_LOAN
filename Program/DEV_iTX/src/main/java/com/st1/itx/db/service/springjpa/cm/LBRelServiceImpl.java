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

@Service("lBRelServiceImpl")
@Repository

/*
 * LBRel 聯徵授信「同一關係企業及集團企業」資料報送檔
 */

public class LBRelServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LBRelServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List findAll(TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		logger.info("----------- LBRel.findAll ---------------");
		logger.info("-----LBRel TitaVo=" + titaVo);
		logger.info("-----LBRel Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		int acctDate = Integer.parseInt(titaVo.getEntDy()) + 19110000; // 西元

//		TEST
//		if (onLineMode == true) {
//			dateMonth = 202003;
//		}

		logger.info("acctDate= " + acctDate);

		String sql = "";

		// LBRel 聯徵授信「同一關係企業及集團企業」資料報送檔
		sql = "SELECT " + "  \"BankItem\", \"BranchItem\", \"RelYM\", \"TranCode\" " + ", \"CustId\", \"Filler6\", \"RelId\", \"Filler8\", \"RelationCode\" " + ", \"Filler10\", \"EndCode\" "
				+ " FROM  \"JcicRel\" " + " WHERE \"DataYMD\" = " + acctDate + " ORDER BY \"BankItem\", \"BranchItem\", \"TranCode\", \"CustId\", \"RelId\" ";

		logger.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode == true) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LBRel.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}
