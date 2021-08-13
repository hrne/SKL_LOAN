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

@Service("lB204ServiceImpl")
@Repository

/*
 * @param titaVo B204 聯徵授信餘額日報檔
 */
public class LB204ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LB204ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List findAll(TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		logger.info("----------- LB204.findAll ---------------");
		logger.info("-----LB204 TitaVo=" + titaVo);
		logger.info("-----LB204 Tita ENTDY=" + titaVo.getEntDy());

		int acctDate = Integer.parseInt(titaVo.getEntDy()) + 19110000; // 西元
		logger.info("-----LB204 acctDate=" + acctDate);

//		if (onLineMode == true) {
//			acctDate = 20200423;
//		}

		logger.info("acctDate= " + acctDate);

		String sql = "";

		// B204 聯徵授信餘額日報檔
		sql = "SELECT M.\"BankItem\" " + "     , M.\"BranchItem\" " + "     , M.\"DataDate\" " + "     , M.\"AcctNo\" " + "     , M.\"CustId\" " + "     , M.\"AcctCode\" "
				+ "     , M.\"SubAcctCode\" " + "     , M.\"SubTranCode\" " + "     , M.\"LineAmt\" " + "     , M.\"DrawdownAmt\" " + "     , M.\"DBR22Amt\" " + "     , M.\"SeqNo\" "
				+ "     , M.\"Filler13\" " 
				+ " FROM  \"JcicB204\" M " 
				+ " WHERE ( M.\"DataYMD\" = " + acctDate + " )"
				+ " ORDER BY M.\"BankItem\", M.\"BranchItem\", M.\"DataDate\", M.\"AcctNo\", M.\"CustId\" " 
				+ "        , M.\"AcctCode\", M.\"SubAcctCode\", M.\"SeqNo\" ";

		logger.info("sql=" + sql);
		
		Query query;
		EntityManager em;

		if (onLineMode == true) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LB201.java 帶入資料庫環境
		}

		query = em.createNativeQuery(sql);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}