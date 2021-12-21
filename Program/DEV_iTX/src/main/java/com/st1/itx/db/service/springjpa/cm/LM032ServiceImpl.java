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
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class LM032ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	DateUtil dUtil;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		int thisYearMonth = (parse.stringToInteger(titaVo.get("ENTDY")) + 19110000) / 100;

		this.info("LM032ServiceImpl thisYearMonth: " + thisYearMonth);
		this.info("lM032.findAll ");
		String sql = " ";
		sql += " SELECT \"ADTYMT\"  "; // F0 前期資料年月
		sql += "       ,\"GDRID1\"  "; // F1 前期擔保品代號1
		sql += "       ,\"W08PPR\"  "; // F2 前期逾期期數
		sql += "       ,\"LMSACN\"  "; // F3 前期戶號
		sql += "       ,\"LMSAPN\"  "; // F4 前期額度號碼
		sql += "       ,\"W08LBL\"  "; // F5 前期本金餘額
		sql += "       ,\"W08DLY\"  "; // F6 前期逾期天數
		sql += "       ,CASE WHEN \"W08PPR\" >= 5 AND \"W08PPR01\" = 0 THEN '正'";
		sql += "        ELSE NULL END \"STATUS\""; // F7 當期戶況
		sql += "       ,CASE WHEN \"W08DLY01\" = 0 AND \"STATUS\" <> '轉催' THEN 0";
		sql += "        ELSE \"ADTYMT01\" END \"ADTYMT01\""; // F8 當期資料年月
		sql += "       ,CASE WHEN \"W08DLY01\" = 0 AND \"STATUS\" <> '轉催' THEN 0";
		sql += "        ELSE \"GDRID101\" END \"GDRID101\""; // F9 當期擔保品代號1
		sql += "       ,CASE WHEN \"W08DLY01\" = 0 AND \"STATUS\" <> '轉催' THEN 0";
		sql += "        ELSE \"W08PPR01\" END \"W08PPR01\""; // F10 當期逾期期數
		sql += "       ,CASE WHEN \"W08DLY01\" = 0 AND \"STATUS\" <> '轉催' THEN 0";
		sql += "        ELSE \"LMSACN01\" END \"LMSACN01\""; // F11 當期戶號
		sql += "       ,CASE WHEN \"W08DLY01\" = 0 AND \"STATUS\" <> '轉催' THEN 0";
		sql += "        ELSE \"LMSAPN01\" END \"LMSAPN01\""; // F12 當期額度號碼
		sql += "       ,CASE WHEN \"W08DLY01\" = 0 AND \"STATUS\" <> '轉催' THEN 0";
		sql += "        ELSE \"W08LBL01\" END \"W08LBL01\""; // F13 當期本金餘額
		sql += "       ,\"W08DLY01\""; // F14 當期逾期天數
		sql += "       ,CASE WHEN \"W08DLY01\" = 0 AND \"STATUS\" <> '轉催' THEN '0'";
		sql += "        ELSE \"ACTACT\" END \"ACTACT\""; // F15 當期業務科目
		sql += " FROM \"MonthlyLM032\" L";
		sql += " WHERE L.\"ADTYMT\" = \"Fn_GetLastMonth\"(:thisYearMonth) ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("thisYearMonth", thisYearMonth);

		return this.convertToMap(query);
	}

}