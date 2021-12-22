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

@Service("lB211ServiceImpl")
@Repository

/*
 * @param titaVo B211 聯徵每日授信餘額變動資料檔
 */
public class LB211ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LB211.findAll ---------------");
		this.info("-----LB211 TitaVo=" + titaVo);
		this.info("-----LB211 Tita ENTDY=" + titaVo.getEntDy());

		int acctDate = Integer.parseInt(titaVo.getEntDy()) + 19110000; // 西元
		this.info("-----LB211 acctDate=" + acctDate);

		int iacdateStart = Integer.parseInt(titaVo.getParam("AcDateStart")) + 19110000; // 西元
		int iacdateEnd = Integer.parseInt(titaVo.getParam("AcDateEnd")) + 19110000; // 西元
		String acdateStart = String.valueOf(iacdateStart);
		String acdateEnd = String.valueOf(iacdateEnd);
		this.info("acdateStart=" + acdateStart + ",acdateEnd=" + acdateEnd);

//		if (onLineMode == true) {
//			acctDate = 20200423;
//		}

		this.info("acctDate= " + acctDate);

		String sql = "";

		// B211 聯徵每日授信餘額變動資料檔
		sql = "SELECT M.\"BankItem\" " + "     , M.\"BranchItem\" " + "     , M.\"TranCode\" " + "     , M.\"CustId\" "
				+ "     , M.\"SubTranCode\" " + "     , M.\"AcDate\" " + "     , M.\"AcctNo\" " + "     , M.\"TxAmt\" "
				+ "     , M.\"LoanBal\" " + "     , M.\"RepayCode\" " + "     , M.\"NegStatus\" "
				+ "     , M.\"AcctCode\" " + "     , M.\"SubAcctCode\" " + "     , M.\"BadDebtDate\" "
				+ "     , M.\"ConsumeFg\" " + "     , M.\"FinCode\" " + "     , M.\"UsageCode\" "
				+ "     , M.\"Filler18\" " + " FROM  \"JcicB211\" M " 
                + " WHERE ( M.\"DataYMD\" Between :acdateStart  AND :acdateEnd  )"
				+ " ORDER BY M.\"BankItem\", M.\"BranchItem\", M.\"AcctNo\", M.\"AcDate\", M.\"TranCode\", M.\"LoanBal\"  ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;

		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LB201.java 帶入資料庫環境
		}

		query = em.createNativeQuery(sql);
		query.setParameter("acdateStart", acdateStart); 
		query.setParameter("acdateEnd", acdateEnd); 

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}