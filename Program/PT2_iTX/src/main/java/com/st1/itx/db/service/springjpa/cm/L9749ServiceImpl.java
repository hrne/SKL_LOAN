package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class L9749ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private Parse parse;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws LogicException {
		this.info("L9749ServiceImpl.findAll");

		int entryDate = parse.stringToInteger(titaVo.get("EntryDate")) + 19110000;
		int notPrevIntDate = parse.stringToInteger(titaVo.get("NotPrevIntDate")) + 19110000;

		this.info("entryDate = " + entryDate);
		this.info("notPrevIntDate = " + notPrevIntDate);

		String sql = "";
		sql += " SELECT BD.\"EntryDate\" ";
		sql += "      , LPAD(BD.\"CustNo\",7,'0') AS \"CustNo\" ";
		sql += "      , LPAD(BD.\"FacmNo\",3,'0') AS \"FacmNo\" ";
		sql += "      , BD.\"PrevIntDate\" ";
		sql += "      , BD.\"RepayAmt\" ";
		sql += "      , CASE ";
		sql += "          WHEN NVL(BD.\"ReturnCode\",' ') = ' ' ";
		sql += "          THEN '未回' ";
		sql += "          WHEN NVL(BD.\"ReturnCode\",' ') = '00' ";
		sql += "          THEN '扣款成功' ";
		sql += "          WHEN BD.\"RepayBank\" = '700' ";
		sql += "          THEN \"Fn_GetCdCode\"('ProcCode', '003' || BD.\"ReturnCode\")";
		sql += "        ELSE \"Fn_GetCdCode\"('ProcCode', '002' || BD.\"ReturnCode\") ";
		sql += "        END                       AS \"ReturnCode\" ";
		sql += "      , BD.\"AcDate\" ";
		sql += "      , \"Fn_GetCdCode\"('RepayType', LPAD(BD.\"RepayType\",2,'0')) ";
		sql += "                                  AS \"RepayType\" ";
		sql += " FROM \"BankDeductDtl\" BD ";
		sql += " WHERE BD.\"EntryDate\" = :inputEntryDate ";
		sql += "   AND BD.\"PrevIntDate\" != :inputNotPrevIntDate ";
		sql += "   AND BD.\"RepayBank\" != '700' ";
		sql += " ORDER BY BD.\"CustNo\" ";
		sql += "        , BD.\"FacmNo\" ";
		sql += "        , BD.\"EntryDate\" ";
		sql += "        , BD.\"PrevIntDate\" ";
		sql += "        , BD.\"AcDate\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputEntryDate", entryDate);
		query.setParameter("inputNotPrevIntDate", notPrevIntDate);

		return this.convertToMap(query);
	}

}