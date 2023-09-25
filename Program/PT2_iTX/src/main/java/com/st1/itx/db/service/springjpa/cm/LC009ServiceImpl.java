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
import com.st1.itx.util.parse.Parse;

@Service
@Repository

public class LC009ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	Parse parse;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo, int index, int limit) throws Exception {

		int iFiledate1 = Integer.valueOf(titaVo.get("iEntdyStart").trim()) + 19110000;
		int iFiledate2 = Integer.valueOf(titaVo.get("iEntdyEnd").trim()) + 19110000;
		int iCreateDate1 = 0;
		int iCreateDate2 = 0;
		if (!titaVo.get("iCreateDateStart").trim().isEmpty()) {
			this.info("iCreateDateStart =" + titaVo.get("iCreateDateStart").trim());
			this.info("iCreateDateEnd   =" + titaVo.get("iCreateDateEnd").trim());
			iCreateDate1 = Integer.valueOf(titaVo.get("iCreateDateStart").trim()) + 19110000;
			iCreateDate2 = Integer.valueOf(titaVo.get("iCreateDateEnd").trim()) + 19110000;
		}
		String iBrNo = titaVo.get("iBrNo").trim();
		String iTlrNo = titaVo.get("iTlrNo").trim();
		String iCode = titaVo.get("iCode").trim();
		String iItem = titaVo.get("iItem").trim();

		// 2023-09-25 Wei 增加 from Lai:
		// 各環境產表都寫回Online,
		// 但是各環境在LC009查詢時,只能查到各自環境產製的報表
		String queryEnv = getQueryEnv(titaVo.getDataBase());
		
		String sql = "SELECT  A.\"FileNo\"";
		sql += ",A.\"FileDate\" - 19110000 as \"FileDate\"";
		sql += ",A.\"FileCode\"";
		sql += ",A.\"FileItem\"";
		sql += ",A.\"FileType\"";
		sql += ",A.\"SignCode\"";
		sql += ",A.\"TlrNo\"";
		sql += ",A.\"SupNo\"";
		sql += ",A.\"CreateEmpNo\"";
		sql += ",A.\"CreateDate\"";
		sql += ",NVL(B.\"Fullname\",'') AS \"TlrName\"";
		sql += ",NVL(C.\"Fullname\",'') AS \"SupName\"";
		sql += ",NVL(D.\"Fullname\",'') AS \"EmpName\"";
		sql += ",NVL(E.\"ServerIp\",'') AS \"ServerIp\"";
		sql += ",NVL(E.\"Printer\",'') AS \"Printer\"";
		sql += " from \"TxFile\" A";
		sql += " left join \"CdEmp\" B on B.\"EmployeeNo\"=A.\"TlrNo\"";
		sql += " left join \"CdEmp\" C on C.\"EmployeeNo\"=A.\"SupNo\"";
		sql += " left join \"CdEmp\" D on D.\"EmployeeNo\"=A.\"CreateEmpNo\"";
		sql += " left join \"TxPrinter\" E on A.\"FileType\"=6 and E.\"StanIp\"=:stanip and E.\"FileCode\"=A.\"FileCode\" ";
		sql += " where A.\"FileDate\">=:filedate1 and A.\"FileDate\"<=:filedate2";
		sql += "   and A.\"BrNo\"=:brno";
		// 2023-09-25 Wei 增加 from Lai:
		// 各環境產表都寫回Online,
		// 但是各環境在LC009查詢時,只能查到各自環境產製的報表
		sql += "   and A.\"SourceEnv\" = :queryEnv ";

		if (iCreateDate1 > 0) {
			sql += "   and A.\"CreateDate\" >= to_date(:createdate1,'yyyymmdd hh24:mi:ss') AND A.\"CreateDate\" <= to_date(:createdate2,'yyyymmdd hh24:mi:ss') ";
		}
		if (!"".equals(iTlrNo)) {
			sql += "   and A.\"CreateEmpNo\" = :tlrno";
		}
		if (!"".equals(iCode)) {
			sql += "   and A.\"FileCode\" like :filecode";
		}
		if (!"".equals(iItem)) {
			sql += "   and A.\"FileItem\" like :fileitem";
		}

		sql += " order by A.\"CreateDate\" DESC";

		sql += " OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

		this.info("LC009ServiceImpl sql=" + sql);
		Query query;

		// 寫Txfile時需寫回onlineDB,但交易用的titaVo應維持原指向的DB
		TitaVo tmpTitaVo = (TitaVo) titaVo.clone();
		tmpTitaVo.putParam(ContentName.dataBase, ContentName.onLine);
		
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(tmpTitaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("filedate1", iFiledate1);
		query.setParameter("filedate2", iFiledate2);
		query.setParameter("brno", iBrNo);
		query.setParameter("stanip", titaVo.getParam("IP"));

		// 2023-09-25 Wei 增加 from Lai:
		// 各環境產表都寫回Online,
		// 但是各環境在LC009查詢時,只能查到各自環境產製的報表
		query.setParameter("queryEnv", queryEnv);

		if (iCreateDate1 > 0) {
			query.setParameter("createdate1", iCreateDate1 + " 00:00:00");
			query.setParameter("createdate2", iCreateDate2 + " 23:59:59");
		}
		if (!"".equals(iTlrNo)) {
			query.setParameter("tlrno", iTlrNo);
		}
		if (!"".equals(iCode)) {
			query.setParameter("filecode", iCode + "%");
		}
		if (!"".equals(iItem)) {
			query.setParameter("fileitem", "%" + iItem + "%");
		}

		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		return this.convertToMap(query);
	}

	private String getQueryEnv(String dataBase) {
		switch (dataBase) {
		case ContentName.onLine:
			return "O";
		case ContentName.onDay:
			return "D";
		case ContentName.onMon:
			return "M";
		case ContentName.onHist:
			return "H";
		default:
			return "O";
		}
	}

}