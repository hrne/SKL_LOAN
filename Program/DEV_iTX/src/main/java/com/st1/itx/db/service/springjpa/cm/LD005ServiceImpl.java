package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

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

@Service
@Repository
public class LD005ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LD005ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		logger.info("lD005.findAll ");

		String sql = "SELECT L.\"CustNo\" AS F0";// 借款人戶頭
		sql += "            ,L.\"ChequeName\" AS F1";// 發票人姓名
		sql += "            ,L.\"ChequeAcct\" AS F2";// 支票帳號
		sql += "            ,L.\"ChequeNo\" AS F3";// 支票號碼
		sql += "            ,L.\"ChequeAmt\" AS F4";// 支票金額
		sql += "            ,L.\"ReceiveDate\" AS F5";// 收票日
		sql += "            ,L.\"ChequeDate\" AS F6";// 支票到期日
		sql += "            ,B.\"BankItem\" AS F7";// 行庫名稱
		sql += "            ,B.\"BranchItem\" AS F8";// 分行名稱
		sql += "      FROM \"LoanCheque\" L ";
		sql += "      LEFT JOIN \"CdBank\" B ON TRIM ( B.\"BankCode\" || B.\"BranchCode\") = L.\"BankCode\"";// TRIM 清空前後空白字元
		sql += "      WHERE L.\"ReceiveDate\" = :inputDate";
		sql += "        AND L.\"CustNo\" = :inputCustNo";
		sql += "      ORDER BY L.\"CustNo\"";
		sql += "              ,L.\"ChequeDate\"";

		logger.info("sql=" + sql);
		
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		
		query.setParameter("inputDate", Integer.parseInt(titaVo.getParam("inputDate")) + 19110000);
		query.setParameter("inputCustNo", titaVo.getParam("inputCustNo"));
		
		return this.convertToMap(query.getResultList());
	}

}