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

@Service
@Repository
public class L9728ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int custNoStart, int custNoEnd, int findDateStart, int findDateEnd, TitaVo titaVo) {
		this.info("L9728ServiceImpl queryCollection ");

		this.info("L9728ServiceImpl custNoStart = " + custNoStart);
		this.info("L9728ServiceImpl custNoEnd = " + custNoEnd);
		this.info("L9728ServiceImpl findDateStart = " + findDateStart);
		this.info("L9728ServiceImpl findDateEnd = " + findDateEnd);

		String sql = "";
		sql += " WITH CNSeq AS ";
		sql += "          (SELECT \"CustNo\" ";
		sql += "                , \"FacmNo\" ";
		sql += "                , \"CreateEmpNo\" ";
		sql += "                , \"CreateDate\" ";
		sql += "                , \"LastUpdateEmpNo\" ";
		sql += "                , \"LastUpdate\" ";
		sql += "                , ROW_NUMBER() OVER (PARTITION BY \"CustNo\" ";
		sql += "                                                 ,\"FacmNo\" ";
		sql += "                                     ORDER BY \"CreateDate\" ASC)  \"CreateSeq\" ";
		sql += "                , ROW_NUMBER() OVER (PARTITION BY \"CustNo\" ";
		sql += "                                                 ,\"FacmNo\" ";
		sql += "                                     ORDER BY \"LastUpdate\" DESC) \"UpdateSeq\" ";
		sql += "           FROM \"CustNotice\" ";
		sql += "          ) ";
		sql += " SELECT UNIQUE CN.\"CustNo\"               AS \"CustNo\" ";
		sql += "             , CN.\"FacmNo\"               AS \"FacmNo\" ";
		sql += "             , CM.\"CustName\"             AS \"CustName\" ";
		sql += "             , CECreate.\"Fullname\"       AS \"CreateName\" ";
		sql += "             , CNSeqCreate.\"CreateDate\"  AS \"CreateDate\" ";
		sql += "             , CEUpdate.\"Fullname\"       AS \"UpdateName\" ";
		sql += "             , CNSeqUpdate.\"LastUpdate\"  AS \"UpdateDate\" ";
		sql += " FROM \"CustNotice\" CN ";
		sql += " LEFT JOIN CnSeq CNSeqCreate ON CNSeqCreate.\"CustNo\" = CN.\"CustNo\" ";
		sql += "                            AND CNSeqCreate.\"FacmNo\" = CN.\"FacmNo\" ";
		sql += " LEFT JOIN CnSeq CNSeqUpdate ON CNSeqUpdate.\"CustNo\" = CN.\"CustNo\" ";
		sql += "                            AND CNSeqUpdate.\"FacmNo\" = CN.\"FacmNo\" ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = CN.\"CustNo\" ";
		sql += " LEFT JOIN \"CdEmp\" CECreate ON CECreate.\"EmployeeNo\" = CNSeqCreate.\"CreateEmpNo\" ";
		sql += " LEFT JOIN \"CdEmp\" CEUpdate ON CEUpdate.\"EmployeeNo\" = CNSeqUpdate.\"LastUpdateEmpNo\" ";
		sql += " WHERE 'N' IN (CN.\"PaperNotice\", CN.\"MsgNotice\", CN.\"EmailNotice\") "; // 需有任一項設定為不通知
		sql += "   AND CNSeqCreate.\"CreateSeq\" = 1 "; // 顯示該額度最早的建立者與建立日期（配合 UNIQUE）
		sql += "   AND CNSeqUpdate.\"UpdateSeq\" = 1 "; // 顯示該額度最新的修改者與建立日期（配合 UNIQUE）
		sql += "   AND CN.\"CustNo\" BETWEEN :custNoStart AND :custNoEnd "; // 戶號區間
		sql += "   AND CNSeqUpdate.\"LastUpdate\" BETWEEN TO_TIMESTAMP(TO_CHAR(:findDateStart), 'YYYYMMDD') AND TO_TIMESTAMP(TO_CHAR(:findDateEnd) || ' 23:59:59', 'YYYYMMDD HH24:MI:SS')"; // 日期區間
		sql += " ORDER BY CN.\"CustNo\" ASC ";
		sql += "        , CN.\"FacmNo\" ASC ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("custNoStart", custNoStart);
		query.setParameter("custNoEnd", custNoEnd);
		query.setParameter("findDateStart", findDateStart);
		query.setParameter("findDateEnd", findDateEnd);

		return this.convertToMap(query);
	}

}