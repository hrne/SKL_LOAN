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
public class L9729ServiceImpl extends ASpringJpaParm implements InitializingBean {
	
	public enum WorkType {
		FiveYearsTX("1", "5YTX", "已結清並領取清償證明五年之交易明細");
		
		private String helpNo;
		private String code;
		private String desc;
		
		public String getHelpNo()
		{
			return helpNo;
		}
		
		public String getCode()
		{
			return code;
		}
		
		public String getDesc()
		{
			return desc;
		}
		
		WorkType(String _helpNo, String _code, String _desc)
		{
			this.helpNo = _helpNo;
			this.code = _code;
			this.desc = _desc;
		}
		
		public static WorkType getWorkTypeByHelp(String helpCode) throws LogicException
		{
			// also used by L6971
			// 設定 workType
			for (WorkType w : WorkType.values())
			{
				if (w.getHelpNo().equals(helpCode))
				{
					return w;
				}
			}
			
			throw new LogicException("E0019", "L9729 不支援此明細種類: " + helpCode);
		}
	}

	@Autowired
	private BaseEntityManager baseEntityManager;
	
	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}
	
	public List<Map<String, String>> findAll(String inputType, TitaVo titaVo) throws LogicException {
		this.info("L9729ServiceImpl.findAll"); 
		
		int inputDate = parse.stringToInteger(titaVo.get("InputDate")) + 19110000;
		
		this.info("inputDate = " + inputDate);
		this.info("inputType = " + inputType);
		
		String sql = "";
		// 2023-05-18 Wei 增加判斷 from SKL 琦欣
		// 只產最後一批
		sql += " WITH rawData AS ( ";
		sql += "   SELECT MAX(\"BatchNo\") AS \"MaxBatchNo\" ";
		sql += "   FROM \"TxArchiveTableLog\" ";
		sql += "   WHERE \"ExecuteDate\" = :inputDate ";
		sql += "     AND \"Type\" = :inputType ";
		sql += " )  ";
		sql += " SELECT T.\"TableName\" ";
		sql += "      , T.\"BatchNo\" ";
		sql += "      , T.\"CustNo\" ";
		sql += "      , T.\"FacmNo\" ";
		sql += "      , T.\"BormNo\" ";
		sql += "      , DECODE(T.\"Result\", 1, '成功', '失敗') \"Result\" ";
		sql += "      , T.\"Description\" ";
		sql += "      , T.\"DataFrom\" ";
		sql += "      , T.\"DataTo\" ";
		sql += " FROM rawData R ";
		sql += " LEFT JOIN \"TxArchiveTableLog\" T ON T.\"BatchNo\" = R.\"MaxBatchNo\" ";
		sql += "                                  AND T.\"ExecuteDate\" = :inputDate ";
		sql += "                                  AND T.\"Type\" = :inputType ";
		sql += " WHERE NVL(T.\"BatchNo\",0) != 0 ";
		sql += " ORDER BY T.\"BatchNo\" DESC ";
		sql += "        , T.\"TableName\" ASC ";
		sql += "        , T.\"CustNo\" ASC ";
		sql += "        , T.\"FacmNo\" ASC ";
		sql += "        , T.\"BormNo\" ASC ";


		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputDate", inputDate);
		query.setParameter("inputType", inputType);
		
		return this.convertToMap(query);
	}

}