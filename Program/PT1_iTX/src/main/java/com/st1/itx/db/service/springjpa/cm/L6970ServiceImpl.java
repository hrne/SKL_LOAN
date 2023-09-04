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
public class L6970ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("L6970ServiceImpl.findAll");

		this.index = titaVo.getReturnIndex();
		this.limit = 200;

		int batchResultCode = parse.stringToInteger(titaVo.getParam("BatchResultCode"));
		int inputStartDate = parse.stringToInteger(titaVo.getParam("InputStartDate")) + 19110000;
		int inputEndDate = parse.stringToInteger(titaVo.getParam("InputEndDate")) + 19110000;
		int errorTypeCode = parse.stringToInteger(titaVo.getParam("ErrorTypeCode"));
		String inputJobSeq = titaVo.getParam("InputJobTxSeq");

		String sql = "";
		sql += " WITH E AS ( ";
		sql += "   SELECT DISTINCT ";
		sql += "          \"JobTxSeq\"";
		sql += "   FROM \"UspErrorLog\" ";
		sql += "   UNION ";
		sql += "   SELECT DISTINCT ";
		sql += "          \"TxSeq\" AS \"JobTxSeq\" ";
		sql += "   FROM \"JobDetail\" ";
		sql += "   WHERE \"Status\" = 'F' ";
		sql += "     AND \"ErrContent\" IS NOT NULL ";
		sql += " )";
		sql += " SELECT I.\"JobCode\"       AS \"JobCode\" ";
		sql += "      , I.\"NestJobCode\"   AS \"NestJobCode\" ";
		sql += "      , CASE ";
		sql += "          WHEN I.\"StepStartTime\" IS NOT NULL ";
		sql += "          THEN TO_CHAR(I.\"StepStartTime\", 'HH24:MI:SS') ";
		sql += "        ELSE ' ' ";
		sql += "        END                 AS \"StepStartTime\" ";
		sql += "      , I.\"Status\"        AS \"Status\" ";
		sql += "      , I.\"ExecDate\"      AS \"ExecDate\" ";
		sql += "      , I.\"StepId\"        AS \"StepId\" ";
		sql += "      , CASE ";
		sql += "          WHEN I.\"StepEndTime\" IS NOT NULL ";
		sql += "          THEN TO_CHAR(I.\"StepEndTime\", 'HH24:MI:SS') ";
		sql += "        ELSE ' ' ";
		sql += "        END                  AS \"StepEndTime\" ";
		sql += "      , I.\"TxSeq\"         AS \"TxSeq\" ";
		sql += "      , CASE ";
		sql += "          WHEN NVL(E.\"JobTxSeq\",' ') = ' ' ";
		sql += "          THEN 'N' ";
		sql += "        ELSE 'Y' ";
		sql += "        END                 AS　\"HaveUspErrorLog\"";
		sql += " FROM \"JobDetail\" I";
		sql += " LEFT JOIN E ON I.\"Status\" = 'F' ";
		sql += "            AND E.\"JobTxSeq\" = I.\"TxSeq\" ";
		sql += " WHERE I.\"JobCode\" = 'eodFlow' ";
		sql += "   AND I.\"ExecDate\" BETWEEN :inputStartDate AND :inputEndDate ";
		switch (batchResultCode) {
		case 0: // 全部
			break;
		case 1: // 成功
			sql += " AND I.\"Status\" = 'S' ";
			break;
		case 2: // 失敗
			sql += " AND I.\"Status\" = 'F' ";
			break;
		default:
			this.error("unexpected batchResultCode : " + batchResultCode);
			break;
		}
		switch (errorTypeCode) {
		case 0: // 全部
			break;
		case 1: // 影響換日
			sql += " AND I.\"NestJobCode\" NOT LIKE '%RptFlow' ";
			break;
		case 2: // 不影響換日
			sql += " AND I.\"NestJobCode\" LIKE '%RptFlow' ";
			break;
		default:
			this.error("unexpected errorTypeCode : " + errorTypeCode);
			break;
		}
		if (inputJobSeq != null && !inputJobSeq.isEmpty()) {
			sql += " AND I.\"TxSeq\" = :inputJobSeq";
		}
		sql += " ORDER BY I.\"StepStartTime\" DESC ";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		Query query = em.createNativeQuery(sql);
		query.setParameter("inputStartDate", inputStartDate);
		query.setParameter("inputEndDate", inputEndDate);
		query.setParameter("inputJobSeq", inputJobSeq);

		return switchback(query);
	}
}