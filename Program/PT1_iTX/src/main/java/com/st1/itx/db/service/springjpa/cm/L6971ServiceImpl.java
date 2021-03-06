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
import com.st1.itx.db.service.springjpa.cm.L9729ServiceImpl.WorkType;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class L6971ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(WorkType workType, TitaVo titaVo) throws LogicException {
		this.info("L6971ServiceImpl.findAll");

		int inputDate = parse.stringToInteger(titaVo.get("InputDate")) + 19110000;

		this.info("inputDate = " + inputDate);
		this.info("inputType = " + workType.getCode());

		String sql = "";

		// Getting general datas from TxArchiveTableLog
		sql += " SELECT TATL.\"CustNo\" ";
		sql += "       ,TATL.\"FacmNo\" ";
		sql += "       ,TATL.\"BormNo\" ";
		sql += "       ,TATL.\"ExecuteDate\" ";
		sql += "       ,TATL.\"BatchNo\" ";
		sql += " FROM (SELECT \"CustNo\" ";
		sql += "             ,\"FacmNo\" ";
		sql += "             ,\"BormNo\" ";
		sql += "             ,\"ExecuteDate\" ";
		sql += "             ,\"BatchNo\" ";
		sql += "             ,MIN(\"Result\")                                   AS \"MinResult\" ";
		sql += "             ,MAX(\"IsDeleted\")                                AS \"IsDeleted\" ";
		sql += "             ,ROW_NUMBER() OVER (PARTITION BY \"CustNo\" ";
		sql += "                                             ,\"FacmNo\" ";
		sql += "                                             ,\"BormNo\" ";
		sql += "                                 ORDER BY \"ExecuteDate\" DESC ";
		sql += "                                         ,\"BatchNo\"     DESC) AS \"Seq\" ";
		sql += "       FROM \"TxArchiveTableLog\" ";
		sql += "       WHERE \"DataFrom\" = 'ONLINE' ";
		sql += "         AND \"DataTo\" = 'HISTORY' "; // ?????????????????? ONLINE to HISTORY
		sql += "         AND \"Records\"   > 0 "; // ???????????????????????????
		
		// Condition by WorkType
		switch (workType) {
		case FiveYearsTX:
			sql += "         AND \"Type\" = '5YTX' ";
			break;
		default:
			break;
		}
		
		sql += "       GROUP BY \"CustNo\" ";
		sql += "               ,\"FacmNo\" ";
		sql += "               ,\"BormNo\" ";
		sql += "               ,\"ExecuteDate\" ";
		sql += "               ,\"BatchNo\" ";
		sql += "     ) TATL "; 

		// Joining table corresponding to workType
		switch (workType) {
		case FiveYearsTX:
			sql += " LEFT JOIN (SELECT COUNT(*) \"Count\"" + "               ,\"CustNo\" "
					+ "               ,\"FacmNo\" " + "               ,\"BormNo\" " + "         FROM \"LoanBorTx\" "
					+ "         GROUP BY \"CustNo\" " + "                 ,\"FacmNo\" "
					+ "                 ,\"BormNo\" " + "        ) LBTX ON LBTX.\"CustNo\" = TATL.\"CustNo\" ";
			sql += "                 AND LBTX.\"FacmNo\" = TATL.\"FacmNo\" ";
			sql += "                 AND LBTX.\"BormNo\" = TATL.\"BormNo\" ";
			break;

		default:
			break;
		}

		// TxArchiveTableLog WHERE conditions
		sql += " WHERE TATL.\"Seq\"       = 1 "; // ????????????????????????????????????
		sql += "   AND TATL.\"MinResult\" > 0 "; // ???????????????????????????????????????
		sql += "   AND TATL.\"IsDeleted\" = 0 "; // ??????????????????????????????????????????????????????????????????
		// WHERE conditions corresponding to workType
		switch (workType) {
		case FiveYearsTX:
			sql += "   AND LBTX.\"Count\"     > 0 "; // ?????? LoanBorTx ?????????????????????
			break;

		default:
			break;
		}

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

}