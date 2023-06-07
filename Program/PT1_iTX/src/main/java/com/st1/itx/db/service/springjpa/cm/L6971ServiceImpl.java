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

	public List<Map<String, String>> findAll(WorkType workType, int index, int limit, TitaVo titaVo) throws Exception {
		this.info("L6971ServiceImpl.findAll");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

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
		sql += "       WHERE \"IsDeleted\" = 0 ";
		sql += "         AND \"Records\"   > 0 "; // 此筆明細有操作資料

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
		sql += " WHERE TATL.\"Seq\"       = 1 "; // 確認該明細為最新一次搬運
		sql += "   AND TATL.\"MinResult\" > 0 "; // 確定該明細在當批中都是成功
		sql += "   AND TATL.\"IsDeleted\" = 0 "; // 此筆明細在當天當批中沒有任何資料已刪除的紀錄
		// WHERE conditions corresponding to workType
		switch (workType) {
		case FiveYearsTX:
			sql += "   AND LBTX.\"Count\"     > 0 "; // 確認 LoanBorTx 仍有此撥款資料
			break;

		default:
			break;
		}

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return switchback(query);
	}

	public int deleteWhenMatched(WorkType workType, TitaVo titaVo) throws Exception {
		this.info("L6971ServiceImpl.deleteWhenMatched");

		this.info("inputType = " + workType.getCode());

		String sql = "";

		// Getting general datas from TxArchiveTableLog
		sql += " MERGE INTO \"LoanBorTx\" T ";
		sql += " USING ( ";
		sql += " SELECT DISTINCT ";
		sql += "        LBTX.\"CustNo\" ";
		sql += "       ,LBTX.\"FacmNo\" ";
		sql += "       ,LBTX.\"BormNo\" ";
		sql += "       ,LBTX.\"BorxNo\" ";
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
		sql += "       WHERE \"IsDeleted\" = 0 ";
		sql += "         AND \"Records\"   > 0 "; // 此筆明細有操作資料

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
			sql += " LEFT JOIN \"LoanBorTx\" LBTX ON LBTX.\"CustNo\" = TATL.\"CustNo\" ";
			sql += "                             AND LBTX.\"FacmNo\" = TATL.\"FacmNo\" ";
			sql += "                             AND LBTX.\"BormNo\" = TATL.\"BormNo\" ";
			break;
		default:
			break;
		}

		// TxArchiveTableLog WHERE conditions
		sql += " WHERE TATL.\"Seq\"       = 1 "; // 確認該明細為最新一次搬運
		sql += "   AND TATL.\"MinResult\" > 0 "; // 確定該明細在當批中都是成功
		sql += "   AND TATL.\"IsDeleted\" = 0 "; // 此筆明細在當天當批中沒有任何資料已刪除的紀錄
		// WHERE conditions corresponding to workType
		switch (workType) {
		case FiveYearsTX:
			sql += "   AND NVL(LBTX.\"CustNo\",0) != 0 "; // 確認 LoanBorTx 仍有此撥款資料
			break;
		default:
			break;
		}
		sql += " ) S ";
		sql += " ON ( ";
		sql += "   T.\"CustNo\" = S.\"CustNo\" ";
		sql += "   AND T.\"FacmNo\" = S.\"FacmNo\" ";
		sql += "   AND T.\"BormNo\" = S.\"BormNo\" ";
		sql += "   AND T.\"BorxNo\" = S.\"BorxNo\" ";
		sql += " ) ";
		sql += " WHEN MATCHED THEN UPDATE SET ";
		sql += " \"LastUpdateEmpNo\" = 'X' ";
		sql += " DELETE WHERE ";
		sql += " \"LastUpdateEmpNo\" = 'X' ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		int delCnt = query.executeUpdate();

		this.info("delete count = " + delCnt);

		return delCnt;
	}
	

	public int updateIsDeleted(WorkType workType, TitaVo titaVo) throws Exception {
		this.info("L6971ServiceImpl.updateIsDeleted");

		this.info("inputType = " + workType.getCode());

		String sql = "";

		// Getting general datas from TxArchiveTableLog
		sql += " MERGE INTO \"TxArchiveTableLog\" T ";
		sql += " USING ( ";
		sql += " SELECT DISTINCT ";
		sql += "        TATL.\"CustNo\" ";
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
		sql += "       WHERE \"IsDeleted\" = 0 ";
		sql += "         AND \"Records\"   > 0 "; // 此筆明細有操作資料

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

		// TxArchiveTableLog WHERE conditions
		sql += " WHERE TATL.\"Seq\"       = 1 "; // 確認該明細為最新一次搬運
		sql += "   AND TATL.\"MinResult\" > 0 "; // 確定該明細在當批中都是成功
		sql += "   AND TATL.\"IsDeleted\" = 0 "; // 此筆明細在當天當批中沒有任何資料已刪除的紀錄
		sql += " ) S ";
		sql += " ON ( ";
		sql += "   T.\"CustNo\" = S.\"CustNo\" ";
		sql += "   AND T.\"FacmNo\" = S.\"FacmNo\" ";
		sql += "   AND T.\"BormNo\" = S.\"BormNo\" ";
		sql += "   AND T.\"ExecuteDate\" = S.\"ExecuteDate\" ";
		sql += "   AND T.\"BatchNo\" = S.\"BatchNo\" ";
		sql += " ) ";
		sql += " WHEN MATCHED THEN UPDATE SET ";
		sql += " \"IsDeleted\" = 1 ";
		sql += " , \"Description\" = '此次封存明細已從連線環境刪除' ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		int updCnt = query.executeUpdate();

		this.info("update count = " + updCnt);

		return updCnt;
	}
}