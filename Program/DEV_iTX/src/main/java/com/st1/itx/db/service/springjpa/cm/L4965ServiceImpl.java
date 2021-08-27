package com.st1.itx.db.service.springjpa.cm;

import java.util.ArrayList;
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

@Service("l4965ServiceImpl")
@Repository
public class L4965ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;
	@Autowired
	public Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	public List<String[]> findData(int index, int limit, TitaVo titaVo) throws LogicException {
		// * 1:擔保品編號; 2:戶號; 3:核准號螞; 4:保險單號碼; 5:保險公司; 6:保險類別
		int iSearchFlag = parse.stringToInteger(titaVo.getParam("SearchFlag"));
		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iApplNo = parse.stringToInteger(titaVo.getParam("ApplNo"));
		String iNowInsuNo = titaVo.getParam("NowInsuNo").trim();
		String iInsuCompany = titaVo.getParam("InsuCompany");
		String iInsuTypeCode = titaVo.getParam("InsuTypeCode");
		int iInsuEndDateFrom = parse.stringToInteger(titaVo.getParam("InsuEndDateFrom")) + 19110000;
		int iInsuEndDateTo = parse.stringToInteger(titaVo.getParam("InsuEndDateTo")) + 19110000;
		//
		String sqlL4965 = "";
		String searchStr = "where o.\"InsuEndDate\" >= " + iInsuEndDateFrom + " and o.\"InsuEndDate\" <= "
				+ iInsuEndDateTo + "\r\n";
		String searchInsuNo1 = "";
		String searchInsuNo2 = "";
		switch (iSearchFlag) {
		case 1:
			searchStr += "  and o.\"ClCode1\" = " + iClCode1 + " and o.\"ClCode2\" = " + iClCode2
					+ " and  o.\"ClNo\" = " + iClNo + "\r\n";
			sqlL4965 = makeSql1(searchStr, "", "");
			break;
		case 2:
			if (iFacmNo == 0) {
				searchStr += "  and c.\"CustNo\" = " + iCustNo + "\r\n";
			} else {
				searchStr += "  and c.\"CustNo\" = " + iCustNo + " and c.\"FacmNo\" = " + iFacmNo + "\r\n";
			}
			sqlL4965 = makeSql2(searchStr);
			break;
		case 3:
			searchStr += "  and c.\"ApproveNo\" = " + iApplNo + "\r\n";
			sqlL4965 = makeSql2(searchStr);
			break;
		case 4:
			searchInsuNo1 += "  and o.\"OrigInsuNo\" = '" + iNowInsuNo + "' \r\n";
			searchInsuNo2 += "  and ( o.\"PrevInsuNo\" = '" + iNowInsuNo + "' or  o.\"NowInsuNo\" = '" + iNowInsuNo
					+ "') \r\n";
			sqlL4965 = makeSql1(searchStr, searchInsuNo1, searchInsuNo2);
			break;
		case 5:
			searchStr += "  and o.\"InsuCompany\" = '" + iInsuCompany + "' \r\n";
			sqlL4965 = makeSql1(searchStr, "", "");
			break;
		case 6:
			searchStr += "  and o.\"InsuTypeCode\" = '" + iInsuTypeCode + "' \r\n";
			sqlL4965 = makeSql1(searchStr, "", "");
			break;
		}

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sqlL4965);

		this.info("L5959.sqlL4965=" + sqlL4965);

		@SuppressWarnings("unchecked")
		List<Object> lObject = this.convertToMap(query.getResultList());

		return findItem(lObject);

	}

	private String makeSql1(String searchStr, String searchInsuNo1, String searchInsuNo2) throws LogicException {
		String sqlL4965 = "";
		sqlL4965 += "select i.\"CustNo\"            as  F0\r\n";
		sqlL4965 += "      ,c.\"CustName\"          as  F1\r\n";
		sqlL4965 += "      ,i.\"FacmNo\"            as  F2\r\n";
		sqlL4965 += "      ,i.\"ApproveNo\"         as  F3\r\n";
		sqlL4965 += "      ,i.\"ClCode1\"           as  F4\r\n";
		sqlL4965 += "      ,i.\"ClCode2\"           as  F5\r\n";
		sqlL4965 += "      ,i.\"ClNo\"              as  F6\r\n";
		sqlL4965 += "      ,i.\"NowInsuNo\"         as  F7\r\n";
		sqlL4965 += "      ,i.\"EndoInsuNo\"        as  F8\r\n";
		sqlL4965 += "      ,i.\"InsuCompany\"       as  F9\r\n";
		sqlL4965 += "      ,i.\"FireInsuCovrg\"     as  F10\r\n";
		sqlL4965 += "      ,i.\"FireInsuPrem\"      as  F11\r\n";
		sqlL4965 += "      ,i.\"EthqInsuCovrg\"     as  F12\r\n";
		sqlL4965 += "      ,i.\"EthqInsuPrem\"      as  F13\r\n";
		sqlL4965 += "      ,i.\"InsuStartDate\" - 19110000     as  F14\r\n";
		sqlL4965 += "      ,i.\"InsuEndDate\"   - 19110000     as  F15\r\n";
		sqlL4965 += "from ( select \r\n";
		sqlL4965 += "         c.\"CustNo\"          as  \"CustNo\" \r\n";
		sqlL4965 += "        ,c.\"FacmNo\"          as  \"FacmNo\" \r\n";
		sqlL4965 += "        ,c.\"ApproveNo\"       as  \"ApproveNo\" \r\n";
		sqlL4965 += "        ,o.\"ClCode1\"         as  \"ClCode1\" \r\n";
		sqlL4965 += "        ,o.\"ClCode2\"         as  \"ClCode2\" \r\n";
		sqlL4965 += "        ,o.\"ClNo\"            as  \"ClNo\" \r\n";
		sqlL4965 += "        ,o.\"OrigInsuNo\"      as  \"NowInsuNo\" \r\n";
		sqlL4965 += "        ,o.\"EndoInsuNo\"      as  \"EndoInsuNo\" \r\n";
		sqlL4965 += "        ,o.\"InsuCompany\"     as  \"InsuCompany\" \r\n";
		sqlL4965 += "        ,o.\"FireInsuCovrg\"   as  \"FireInsuCovrg\" \r\n";
		sqlL4965 += "        ,o.\"FireInsuPrem\"    as  \"FireInsuPrem\"  \r\n";
		sqlL4965 += "        ,o.\"EthqInsuCovrg\"   as  \"EthqInsuCovrg\" \r\n";
		sqlL4965 += "        ,o.\"EthqInsuPrem\"    as  \"EthqInsuPrem\"  \r\n";
		sqlL4965 += "        ,o.\"InsuStartDate\"   as  \"InsuStartDate\" \r\n";
		sqlL4965 += "        ,o.\"InsuEndDate\"     as  \"InsuEndDate\" \r\n";
		sqlL4965 += "        ,ROW_NUMBER() Over (Partition By o.\"ClCode1\", o.\"ClCode2\", o.\"ClNo\", o.\"OrigInsuNo\", o.\"EndoInsuNo\" \r\n";
		sqlL4965 += "                             order By c.\"ApproveNo\" ASC)\r\n";
		sqlL4965 += "                             as   \"RowNo\"\r\n";
		sqlL4965 += "        from \"InsuOrignal\" o\r\n";
		sqlL4965 += "        left join \"ClFac\" c on c.\"ClCode1\" = o.\"ClCode1\"\r\n";
		sqlL4965 += "                   and c.\"ClCode2\" = o.\"ClCode2\"\r\n";
		sqlL4965 += "                   and c.\"ClNo\" = o.\"ClNo\"\r\n";
		sqlL4965 += searchStr;
		sqlL4965 += searchInsuNo1;
		sqlL4965 += "        union all\r\n";
		sqlL4965 += "        select\r\n";
		sqlL4965 += "         o.\"CustNo\"             as \"CustNo\"       \r\n";
		sqlL4965 += "        ,o.\"FacmNo\"             as \"FacmNo\"       \r\n";
		sqlL4965 += "        ,c.\"ApproveNo\"          as \"ApproveNo\"    \r\n";
		sqlL4965 += "        ,o.\"ClCode1\"            as \"ClCode1\"      \r\n";
		sqlL4965 += "        ,o.\"ClCode2\"            as \"ClCode2\"      \r\n";
		sqlL4965 += "        ,o.\"ClNo\"               as \"ClNo\"         \r\n";
		sqlL4965 += "        ,o.\"NowInsuNo\"          as \"NowInsuNo\"    \r\n";
		sqlL4965 += "        ,o.\"EndoInsuNo\"         as \"EndoInsuNo\"   \r\n";
		sqlL4965 += "        ,o.\"InsuCompany\"        as \"InsuCompany\"  \r\n";
		sqlL4965 += "        ,o.\"FireInsuCovrg\"      as \"FireInsuCovrg\"\r\n";
		sqlL4965 += "        ,o.\"FireInsuPrem\"       as \"FireInsuPrem\" \r\n";
		sqlL4965 += "        ,o.\"EthqInsuCovrg\"      as \"EthqInsuCovrg\"\r\n";
		sqlL4965 += "        ,o.\"EthqInsuPrem\"       as \"EthqInsuPrem\" \r\n";
		sqlL4965 += "        ,o.\"InsuStartDate\"      as \"InsuStartDate\"\r\n";
		sqlL4965 += "        ,o.\"InsuEndDate\"        as \"InsuEndDate\"  \r\n";
		sqlL4965 += "        ,ROW_NUMBER() Over (Partition By o.\"ClCode1\", o.\"ClCode2\", o.\"ClNo\", o.\"PrevInsuNo\", o.\"EndoInsuNo\"\r\n";
		sqlL4965 += "                             order By c.\"ApproveNo\" ASC)\r\n";
		sqlL4965 += "                             as   \"RowNo\"\r\n";
		sqlL4965 += "        from \"InsuRenew\" o \r\n";
		sqlL4965 += "        left join \"ClFac\" c on c.\"ClCode1\" = o.\"ClCode1\"\r\n";
		sqlL4965 += "                   and c.\"ClCode2\" = o.\"ClCode2\"\r\n";
		sqlL4965 += "                   and c.\"ClNo\" = o.\"ClNo\"\r\n";
		sqlL4965 += "                   and c.\"CustNo\" = o.\"CustNo\"\r\n";
		sqlL4965 += "                   and c.\"FacmNo\" = o.\"FacmNo\"\r\n";
		sqlL4965 += searchStr;
		sqlL4965 += searchInsuNo2;
		sqlL4965 += "     ) i\r\n";
		sqlL4965 += "left join \"CustMain\" c on c.\"CustNo\" = i.\"CustNo\"\r\n";
		sqlL4965 += "where i.\"RowNo\" = 1\r\n";
		sqlL4965 += "order by i.\"InsuEndDate\" desc\r\n";
		sqlL4965 += "        ,i.\"ClCode1\"\r\n";
		sqlL4965 += "        ,i.\"ClCode2\"\r\n";
		sqlL4965 += "        ,i.\"ClNo\"\r\n";
		return sqlL4965;
	}

	private String makeSql2(String searchStr) throws LogicException {
		String sqlL4965 = "";
		sqlL4965 += "select i.\"CustNo\"            as  F0\r\n";
		sqlL4965 += "      ,c.\"CustName\"          as  F1\r\n";
		sqlL4965 += "      ,i.\"FacmNo\"            as  F2\r\n";
		sqlL4965 += "      ,i.\"ApproveNo\"         as  F3\r\n";
		sqlL4965 += "      ,i.\"ClCode1\"           as  F4\r\n";
		sqlL4965 += "      ,i.\"ClCode2\"           as  F5\r\n";
		sqlL4965 += "      ,i.\"ClNo\"              as  F6\r\n";
		sqlL4965 += "      ,i.\"NowInsuNo\"         as  F7\r\n";
		sqlL4965 += "      ,i.\"EndoInsuNo\"        as  F8\r\n";
		sqlL4965 += "      ,i.\"InsuCompany\"       as  F9\r\n";
		sqlL4965 += "      ,i.\"FireInsuCovrg\"     as  F10\r\n";
		sqlL4965 += "      ,i.\"FireInsuPrem\"      as  F11\r\n";
		sqlL4965 += "      ,i.\"EthqInsuCovrg\"     as  F12\r\n";
		sqlL4965 += "      ,i.\"EthqInsuPrem\"      as  F13\r\n";
		sqlL4965 += "      ,i.\"InsuStartDate\" - 19110000     as  F14\r\n";
		sqlL4965 += "      ,i.\"InsuEndDate\"   - 19110000     as  F15\r\n";
		sqlL4965 += "from ( select \r\n";
		sqlL4965 += "         c.\"CustNo\"          as  \"CustNo\" \r\n";
		sqlL4965 += "        ,c.\"FacmNo\"          as  \"FacmNo\" \r\n";
		sqlL4965 += "        ,c.\"ApproveNo\"       as  \"ApproveNo\" \r\n";
		sqlL4965 += "        ,c.\"ClCode1\"         as  \"ClCode1\" \r\n";
		sqlL4965 += "        ,c.\"ClCode2\"         as  \"ClCode2\" \r\n";
		sqlL4965 += "        ,c.\"ClNo\"            as  \"ClNo\" \r\n";
		sqlL4965 += "        ,o.\"OrigInsuNo\"      as  \"NowInsuNo\" \r\n";
		sqlL4965 += "        ,o.\"EndoInsuNo\"      as  \"EndoInsuNo\" \r\n";
		sqlL4965 += "        ,o.\"InsuCompany\"     as  \"InsuCompany\" \r\n";
		sqlL4965 += "        ,o.\"FireInsuCovrg\"   as  \"FireInsuCovrg\" \r\n";
		sqlL4965 += "        ,o.\"FireInsuPrem\"    as  \"FireInsuPrem\"  \r\n";
		sqlL4965 += "        ,o.\"EthqInsuCovrg\"   as  \"EthqInsuCovrg\" \r\n";
		sqlL4965 += "        ,o.\"EthqInsuPrem\"    as  \"EthqInsuPrem\"  \r\n";
		sqlL4965 += "        ,o.\"InsuStartDate\"   as  \"InsuStartDate\" \r\n";
		sqlL4965 += "        ,o.\"InsuEndDate\"     as  \"InsuEndDate\" \r\n";
		sqlL4965 += "        from   \"ClFac\" c     \r\n";
		sqlL4965 += "        left join \"InsuOrignal\" o\r\n";
		sqlL4965 += "                    on o.\"ClCode1\" = c.\"ClCode1\"\r\n";
		sqlL4965 += "                   and o.\"ClCode2\" = c.\"ClCode2\"\r\n";
		sqlL4965 += "                   and o.\"ClNo\" = c.\"ClNo\"\r\n";
		sqlL4965 += searchStr;
		sqlL4965 += "        union all\r\n";
		sqlL4965 += "        select\r\n";
		sqlL4965 += "         c.\"CustNo\"             as \"CustNo\"       \r\n";
		sqlL4965 += "        ,c.\"FacmNo\"             as \"FacmNo\"       \r\n";
		sqlL4965 += "        ,c.\"ApproveNo\"          as \"ApproveNo\"    \r\n";
		sqlL4965 += "        ,c.\"ClCode1\"            as \"ClCode1\"      \r\n";
		sqlL4965 += "        ,c.\"ClCode2\"            as \"ClCode2\"      \r\n";
		sqlL4965 += "        ,c.\"ClNo\"               as \"ClNo\"         \r\n";
		sqlL4965 += "        ,o.\"NowInsuNo\"          as \"NowInsuNo\"    \r\n";
		sqlL4965 += "        ,o.\"EndoInsuNo\"         as \"EndoInsuNo\"   \r\n";
		sqlL4965 += "        ,o.\"InsuCompany\"        as \"InsuCompany\"  \r\n";
		sqlL4965 += "        ,o.\"FireInsuCovrg\"      as \"FireInsuCovrg\"\r\n";
		sqlL4965 += "        ,o.\"FireInsuPrem\"       as \"FireInsuPrem\" \r\n";
		sqlL4965 += "        ,o.\"EthqInsuCovrg\"      as \"EthqInsuCovrg\"\r\n";
		sqlL4965 += "        ,o.\"EthqInsuPrem\"       as \"EthqInsuPrem\" \r\n";
		sqlL4965 += "        ,o.\"InsuStartDate\"      as \"InsuStartDate\"\r\n";
		sqlL4965 += "        ,o.\"InsuEndDate\"        as \"InsuEndDate\"  \r\n";
		sqlL4965 += "        from   \"ClFac\" c  \r\n";
		sqlL4965 += "        left join \"InsuRenew\" o\r\n";
		sqlL4965 += "                    on o.\"ClCode1\" = c.\"ClCode1\"\r\n";
		sqlL4965 += "                   and o.\"ClCode2\" = c.\"ClCode2\"\r\n";
		sqlL4965 += "                   and o.\"ClNo\" = c.\"ClNo\"\r\n";
		sqlL4965 += searchStr;
		sqlL4965 += "     ) i\r\n";
		sqlL4965 += "left join \"CustMain\" c on c.\"CustNo\" = i.\"CustNo\"\r\n";
		sqlL4965 += "order by i.\"InsuEndDate\" desc\r\n";
		sqlL4965 += "        ,i.\"ClCode1\"\r\n";
		sqlL4965 += "        ,i.\"ClCode2\"\r\n";
		sqlL4965 += "        ,i.\"ClNo\"\r\n";
		return sqlL4965;
	}

	@SuppressWarnings("unchecked")
	public List<String[]> findItem(List<Object> lObject) throws LogicException {
		List<String[]> data = new ArrayList<String[]>();
		if (lObject != null && lObject.size() != 0) {
			int col = ((Map<String, String>) lObject.get(0)).keySet().size();
			for (Object obj : lObject) {
				Map<String, String> MapObj = (Map<String, String>) obj;
				String row[] = new String[col];
				for (int i = 0; i < col; i++) {
					row[i] = MapObj.get("F" + String.valueOf(i));
					if (row[i] != null && row[i].length() != 0) {

					} else {
						row[i] = "";
					}
				}
				data.add(row);
			}
		}
		return data;
	}
}