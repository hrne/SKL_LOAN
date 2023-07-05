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
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class L6972ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findByCustNo(int custNo, TitaVo titaVo) throws Exception {
		this.info("L6972ServiceImpl.findByCustNo CustNo = " + custNo);

		// *** 折返控制相關 ***
		this.index = 0;
		// *** 折返控制相關 ***
		this.limit = Integer.MAX_VALUE;

		String sql = "";

		// Getting general datas from TxArchiveTableLog
		sql += " SELECT TATL.\"CustNo\" ";
		sql += "       ,TATL.\"FacmNo\" ";
		sql += "       ,TATL.\"BormNo\" ";
		sql += " FROM (SELECT \"CustNo\" ";
		sql += "             ,\"FacmNo\" ";
		sql += "             ,\"BormNo\" ";
		sql += "       FROM \"TxArchiveTableLog\" ";
		sql += "       WHERE \"IsDeleted\" = 1 ";
		sql += "         AND \"Records\"   > 0 "; // 此筆明細有操作資料
		sql += "         AND \"Type\" = '5YTX' ";
		sql += "         AND \"CustNo\" = :custNo ";
		sql += "       GROUP BY \"CustNo\" ";
		sql += "               ,\"FacmNo\" ";
		sql += "               ,\"BormNo\" ";
		sql += "     ) TATL ";
		sql += " LEFT JOIN ( ";
		sql += "   SELECT COUNT(*) \"Count\"";
		sql += "        , \"CustNo\" ";
		sql += "        , \"FacmNo\" ";
		sql += "        , \"BormNo\" ";
		sql += "   FROM \"LoanBorTx\" ";
		sql += "   WHERE \"CustNo\" = :custNo ";
		sql += "   GROUP BY \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += "          , \"BormNo\" ";
		sql += " ) LBTX ON LBTX.\"CustNo\" = TATL.\"CustNo\" ";
		sql += "       AND LBTX.\"FacmNo\" = TATL.\"FacmNo\" ";
		sql += "       AND LBTX.\"BormNo\" = TATL.\"BormNo\" ";
		sql += " WHERE NVL(LBTX.\"Count\",0) = 0 "; // 確認 LoanBorTx 沒有此撥款資料

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("custNo", custNo);

		return switchback(query);
	}

	public List<Map<String, String>> findByCustNoAndFacmNo(int custNo, int facmNo, TitaVo titaVo) throws Exception {
		this.info("L6972ServiceImpl.findByCustNoAndFacmNo CustNo = " + custNo + " , FacmNo = " + facmNo);

		// *** 折返控制相關 ***
		this.index = 0;
		// *** 折返控制相關 ***
		this.limit = Integer.MAX_VALUE;

		String sql = "";

		// Getting general datas from TxArchiveTableLog
		sql += " SELECT TATL.\"CustNo\" ";
		sql += "       ,TATL.\"FacmNo\" ";
		sql += "       ,TATL.\"BormNo\" ";
		sql += " FROM (SELECT \"CustNo\" ";
		sql += "             ,\"FacmNo\" ";
		sql += "             ,\"BormNo\" ";
		sql += "       FROM \"TxArchiveTableLog\" ";
		sql += "       WHERE \"IsDeleted\" = 1 ";
		sql += "         AND \"Records\"   > 0 "; // 此筆明細有操作資料
		sql += "         AND \"Type\" = '5YTX' ";
		sql += "         AND \"CustNo\" = :custNo ";
		sql += "         AND \"FacmNo\" = :facmNo ";
		sql += "       GROUP BY \"CustNo\" ";
		sql += "               ,\"FacmNo\" ";
		sql += "               ,\"BormNo\" ";
		sql += "     ) TATL ";
		sql += " LEFT JOIN ( ";
		sql += "   SELECT COUNT(*) \"Count\"";
		sql += "        , \"CustNo\" ";
		sql += "        , \"FacmNo\" ";
		sql += "        , \"BormNo\" ";
		sql += "   FROM \"LoanBorTx\" ";
		sql += "   WHERE \"CustNo\" = :custNo ";
		sql += "     AND \"FacmNo\" = :facmNo ";
		sql += "   GROUP BY \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += "          , \"BormNo\" ";
		sql += " ) LBTX ON LBTX.\"CustNo\" = TATL.\"CustNo\" ";
		sql += "       AND LBTX.\"FacmNo\" = TATL.\"FacmNo\" ";
		sql += "       AND LBTX.\"BormNo\" = TATL.\"BormNo\" ";
		sql += " WHERE NVL(LBTX.\"Count\",0) = 0 "; // 確認 LoanBorTx 沒有此撥款資料

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("custNo", custNo);
		query.setParameter("facmNo", facmNo);

		return switchback(query);
	}

	public List<Map<String, String>> findByCustNoAndFacmNoAndBormNo(int custNo, int facmNo, int bormNo, TitaVo titaVo)
			throws Exception {
		this.info("L6972ServiceImpl.findByCustNoAndFacmNo CustNo = " + custNo + " , FacmNo = " + facmNo + " , BormNo = "
				+ bormNo);

		// *** 折返控制相關 ***
		this.index = 0;
		// *** 折返控制相關 ***
		this.limit = Integer.MAX_VALUE;

		String sql = "";

		// Getting general datas from TxArchiveTableLog
		sql += " SELECT TATL.\"CustNo\" ";
		sql += "       ,TATL.\"FacmNo\" ";
		sql += "       ,TATL.\"BormNo\" ";
		sql += " FROM (SELECT \"CustNo\" ";
		sql += "             ,\"FacmNo\" ";
		sql += "             ,\"BormNo\" ";
		sql += "       FROM \"TxArchiveTableLog\" ";
		sql += "       WHERE \"IsDeleted\" = 1 ";
		sql += "         AND \"Records\"   > 0 "; // 此筆明細有操作資料
		sql += "         AND \"Type\" = '5YTX' ";
		sql += "         AND \"CustNo\" = :custNo ";
		sql += "         AND \"FacmNo\" = :facmNo ";
		sql += "        AND \"BormNo\" = :bormNo ";
		sql += "       GROUP BY \"CustNo\" ";
		sql += "               ,\"FacmNo\" ";
		sql += "               ,\"BormNo\" ";
		sql += "     ) TATL ";
		sql += " LEFT JOIN ( ";
		sql += "   SELECT COUNT(*) \"Count\"";
		sql += "        , \"CustNo\" ";
		sql += "        , \"FacmNo\" ";
		sql += "        , \"BormNo\" ";
		sql += "   FROM \"LoanBorTx\" ";
		sql += "   WHERE \"CustNo\" = :custNo ";
		sql += "     AND \"FacmNo\" = :facmNo ";
		sql += "     AND \"BormNo\" = :bormNo ";
		sql += "   GROUP BY \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += "          , \"BormNo\" ";
		sql += " ) LBTX ON LBTX.\"CustNo\" = TATL.\"CustNo\" ";
		sql += "       AND LBTX.\"FacmNo\" = TATL.\"FacmNo\" ";
		sql += "       AND LBTX.\"BormNo\" = TATL.\"BormNo\" ";
		sql += " WHERE NVL(LBTX.\"Count\",0) = 0 "; // 確認 LoanBorTx 沒有此撥款資料

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("custNo", custNo);
		query.setParameter("facmNo", facmNo);
		query.setParameter("bormNo", bormNo);

		return switchback(query);
	}
}