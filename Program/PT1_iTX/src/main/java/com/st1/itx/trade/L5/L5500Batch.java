package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.domain.HlAreaData;
import com.st1.itx.db.domain.HlAreaLnYg6Pt;
import com.st1.itx.db.domain.HlAreaLnYg6PtId;
import com.st1.itx.db.domain.HlCusData;
import com.st1.itx.db.domain.HlEmpLnYg5Pt;
import com.st1.itx.db.domain.HlEmpLnYg5PtId;
import com.st1.itx.db.domain.HlThreeDetail;
import com.st1.itx.db.domain.HlThreeDetailId;
import com.st1.itx.db.domain.HlThreeLaqhcp;
import com.st1.itx.db.domain.HlThreeLaqhcpId;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.service.HlAreaDataService;
import com.st1.itx.db.service.HlAreaLnYg6PtService;
import com.st1.itx.db.service.HlCusDataService;
import com.st1.itx.db.service.HlEmpLnYg5PtService;
import com.st1.itx.db.service.HlThreeDetailService;
import com.st1.itx.db.service.HlThreeLaqhcpService;
import com.st1.itx.db.service.springjpa.cm.L5500ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L5500Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L5500Batch extends TradeBuffer {
	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public HlAreaDataService hlAreaDataService;

	@Autowired
	public HlCusDataService hlCusDataService;

	@Autowired
	public HlEmpLnYg5PtService hlEmpLnYg5PtService;

	@Autowired
	public HlAreaLnYg6PtService hlAreaLnYg6PtService;

	@Autowired
	public HlThreeLaqhcpService hlThreeLaqhcpService;

	@Autowired
	public HlThreeDetailService hlThreeDetailService;

	@Autowired
	CdWorkMonthService cdWorkMonthService;

	@Autowired
	public L5500ServiceImpl l5500ServiceImpl;

	@Autowired
	public WebClient webClient;

	private int entday;
	private int bworkmonth = 0;
	private String bworkmonthX = "";
	private int lworkmonth = 0;
	private String lworkmonthX = "";
	private int workmonth = 0;
	private String workmonthX = "";
	private int workseason = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5500Batch ");
		this.totaVo.init(titaVo);

		entday = Integer.valueOf(titaVo.getParam("ENTDY")) + 19110000;

		CdWorkMonth cdWorkMonth = cdWorkMonthService.findDateFirst(entday, entday, titaVo);
		if (cdWorkMonth == null) {
			throw new LogicException("E0001", "放款業績工作月對照檔");
		}

		// 工作月
		workmonth = cdWorkMonth.getYear() * 100 + cdWorkMonth.getMonth();

		workmonthX = String.valueOf(workmonth);

		// 上工作月
		if (cdWorkMonth.getMonth() > 1) {
			lworkmonth = cdWorkMonth.getYear() * 100 + (cdWorkMonth.getMonth() - 1);
		} else {
			lworkmonth = (cdWorkMonth.getYear() - 1) * 100 + 12;
		}
		lworkmonthX = String.valueOf(lworkmonth);

		// 年度首月
		bworkmonth = cdWorkMonth.getYear() * 100 + 1;
		bworkmonthX = String.valueOf(bworkmonth);

		this.info("L5500Batch workmonth = " + workmonth);

		ProcHlAreaData(titaVo);

		ProcHlCusData(titaVo);

		procHlEmpLnYg5Pt(titaVo);

		procHlAreaLnYg6Pt(titaVo);

		procHlThreeLaqhcp(titaVo);

		procHlThreeDetail(titaVo);

		String msg = "L5500已處理完畢";

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "", "", msg, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 三階明細
	public void procHlThreeDetail(TitaVo titaVo) throws LogicException {
		this.info("L5500Batch.procHlThreeDetail begin ");
		// delete
		Slice<HlThreeDetail> slHlThreeDetail = hlThreeDetailService.findAll(0, Integer.MAX_VALUE, titaVo);
		List<HlThreeDetail> lHlThreeDetail = slHlThreeDetail == null ? null : slHlThreeDetail.getContent();
		if (lHlThreeDetail != null) {
			try {
				hlThreeDetailService.deleteAll(lHlThreeDetail, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "HlThreeDetail");
			}
		}
		String sql = "select ";
		sql += "a.\"CustNo\",";
		sql += "a.\"FacmNo\",";
		sql += "a.\"BormNo\",";
		sql += "a.\"PieceCode\",";
		sql += "a.\"CntingCode\",";
		sql += "a.\"DrawdownAmt\",";
		sql += "f.\"Introducer\",";
		sql += "NVL(b1.\"Fullname\",'') AS \"Name1\",";
		sql += "NVL(b1.\"AgentId\",'')  AS \"Id1\",";
		sql += "NVL(b1.\"AgLevel\",'')  as \"Level1\",";
		sql += "NVL(b2.\"Fullname\",'') as \"Name2\",";
		sql += "NVL(b2.\"AgentId\",'')  as \"Id2\",";
		sql += "NVL(b2.\"AgLevel\",'')  as \"Level2\",";
		sql += "NVL(b3.\"Fullname\",'') as \"Name3\",";
		sql += "NVL(b3.\"AgentId\",'')  as \"Id3\",";
		sql += "NVL(b3.\"AgLevel\",'')  as \"Level3\",";
		sql += "NVL(b4.\"Fullname\",'') as \"Name4\",";
		sql += "NVL(b4.\"AgentId\",'')  as \"Id4\",";
		sql += "NVL(b4.\"AgLevel\",'')  as \"Level4\",";
		sql += "f.\"FirstDrawdownDate\","; // 初貸日
		sql += "f.\"ProdNo\","; // 商品代碼(利率代碼)
		sql += "f.\"UtilBal\","; // 已動用額度餘額
		sql += "f.\"UtilAmt\","; // 貸出金額(放款餘額)
		sql += "a.\"PerfEqAmt\","; // 換算業績
		sql += "a.\"PerfReward\","; // 換算報酬
		sql += "a.\"UnitCode\",";
		sql += "a.\"DistCode\",";
		sql += "a.\"DeptCode\",";
		sql += "c1.\"UnitItem\" AS \"DeptName\",";
		sql += "c2.\"UnitItem\" AS \"DistName\",";
		sql += "c3.\"UnitItem\" AS \"UnitName\" ";
		sql += "from \"PfItDetail\" a ";
		sql += "left join \"FacMain\" f on f.\"CustNo\"=a.\"CustNo\" and f.\"FacmNo\"=a.\"FacmNo\" ";
		sql += "left join \"CdEmp\" b1 on b1.\"EmployeeNo\"=f.\"Introducer\" ";
		sql += "left join \"CdEmp\" b2 on b2.\"AgentCode\"=b1.\"DirectorId\" ";
		sql += "left join \"CdEmp\" b3 on b3.\"AgentCode\"=b2.\"DirectorId\" ";
		sql += "left join \"CdEmp\" b4 on b4.\"AgentCode\"=b3.\"DirectorId\" ";
		sql += "LEFT JOIN \"CdBcm\" c1 ON c1.\"UnitCode\"=a.\"DeptCode\" ";
		sql += "LEFT JOIN \"CdBcm\" c2 ON c2.\"UnitCode\"=a.\"DistCode\" ";
		sql += "LEFT JOIN \"CdBcm\" c3 ON c3.\"UnitCode\"=a.\"UnitCode\" ";
		sql += "where a.\"WorkMonth\">=:workmonth1 and a.\"WorkMonth\"<=:workmonth2 ";
		sql += "and a.\"RepayType\"=0 ";
		sql += "and a.\"PieceCode\" in ('1','2','3','4','8','A','F','G') ";
		sql += "and a.\"DrawdownAmt\">0 ";
		sql += "and f.\"Introducer\" is not null ";

		Map<String, String> conds = new HashMap<String, String>();

		conds.put("workmonth1", workmonthX);
		conds.put("workmonth2", workmonthX);

		String custNo = "";
		String facmNo = "";

		List<Map<String, String>> data = l5500ServiceImpl.findData(index, limit, sql, conds, titaVo);
		lHlThreeDetail = new ArrayList<HlThreeDetail>();
		if (data != null && data.size() > 0) {
			this.info("L5500Batch.procHlThreeDetail = " + data.size());
			HlThreeDetail hlThreeDetail = new HlThreeDetail();
			for (Map<String, String> d : data) {
				if (custNo.equals(d.get("CustNo")) && facmNo.equals(d.get("FacmNo"))) {
					BigDecimal sum = hlThreeDetail.getThreeYag().add(new BigDecimal(d.get("PerfEqAmt")));
					hlThreeDetail.setThreeYag(sum);
					sum = hlThreeDetail.getThreePay().add(new BigDecimal(d.get("PerfReward")));
					hlThreeDetail.setThreePay(sum);
				} else {

					if (!custNo.isEmpty()) {
						lHlThreeDetail.add(hlThreeDetail);
					}

					hlThreeDetail = new HlThreeDetail();

					HlThreeDetailId hlThreeDetailId = new HlThreeDetailId();

					hlThreeDetailId.setBrNo("0000");
					hlThreeDetailId.setCustNo(parse.stringToInteger(d.get("CustNo")));
					hlThreeDetailId.setFacmNo(parse.stringToInteger(d.get("FacmNo")));

					hlThreeDetail.setHlThreeDetailId(hlThreeDetailId);

//					ActAmt	已用額度
					hlThreeDetail.setActAmt(new BigDecimal(d.get("UtilBal")));
//					PieceCode	計件代碼
					hlThreeDetail.setPieceCode(d.get("PieceCode"));
//					CntingCode	是否已計件
					hlThreeDetail.setCntingCode(d.get("CntingCode"));
//					TActAmt	累計已用額度
					hlThreeDetail.setTActAmt(new BigDecimal(d.get("UtilAmt")));
//					EmpNo	員工代號(介紹人)
					hlThreeDetail.setEmpNo(d.get("Introducer"));
//					EmpId	統一編號(介紹人)
					hlThreeDetail.setEmpId(d.get("Id1"));
//					EmpName	員工姓名(介紹人)
					hlThreeDetail.setEmpName(d.get("Name1"));
//					DeptCode	部室代號
					hlThreeDetail.setDeptCode(d.get("DeptCode"));
//					DistCode	區部代號
					hlThreeDetail.setDistCode(d.get("DistCode"));
//					UnitCode	單位代號
					hlThreeDetail.setUnitCode(d.get("UnitCode"));
//					DeptName	部室中文
					hlThreeDetail.setDeptName(d.get("DeptName"));
//					DistName	區部中文
					hlThreeDetail.setDistName(d.get("DistName"));
//					UnitName	單位中文
					hlThreeDetail.setUnitName(d.get("UnitName"));
//					FirAppDate	首次撥款日
					hlThreeDetail.setFirAppDate(parse.stringToInteger(d.get("FirstDrawdownDate")));
//					BiReteNo	基本利率代碼
					hlThreeDetail.setBiReteNo(d.get("ProdNo"));
//					TwoYag	二階換算業績
					hlThreeDetail.setTwoYag(BigDecimal.ZERO);
//					ThreeYag	三階換算業績
					hlThreeDetail.setThreeYag(new BigDecimal(d.get("PerfEqAmt")));
//					TwoPay	二階業務報酬
					hlThreeDetail.setTwoPay(BigDecimal.ZERO);
//					ThreePay	三階業務報酬
					hlThreeDetail.setThreePay(new BigDecimal(d.get("PerfReward")));
//					UnitChiefNo	統一編號(單位主管/處長)
					hlThreeDetail.setUnitChiefNo(d.get(""));
//					UnitChiefName	員工姓名
					hlThreeDetail.setUnitChiefName(d.get(""));
//					AreaChiefNo	統一編號(主任)
					hlThreeDetail.setAreaChiefNo(d.get(""));
//					AreaChiefName	員工姓名
					hlThreeDetail.setAreaChiefName(d.get(""));
//					Id3	統一編號(組長)
					hlThreeDetail.setId3(d.get(""));
//					Id3Name	員工姓名
					hlThreeDetail.setId3Name(d.get(""));
//					TeamChiefNo	統一編號(展業代表)
					hlThreeDetail.setTeamChiefNo(d.get(""));
//					TeamChiefName	員工姓名
					hlThreeDetail.setTeamChiefName(d.get(""));
//					Id0	統一編號
					hlThreeDetail.setId0("");
//					Id0Name	員工姓名
					hlThreeDetail.setId0Name("");
//					UpNo	UpdateIdentifier
					hlThreeDetail.setUpNo(1);
//					CalDate	更新日期
					hlThreeDetail.setCalDate(entday);

					hlThreeDetail = sethlThreeDetailLevel(hlThreeDetail, d.get("Id1"), d.get("Name1"), d.get("Level1"));
					hlThreeDetail = sethlThreeDetailLevel(hlThreeDetail, d.get("Id2"), d.get("Name2"), d.get("Level2"));
					hlThreeDetail = sethlThreeDetailLevel(hlThreeDetail, d.get("Id3"), d.get("Name3"), d.get("Level3"));
					hlThreeDetail = sethlThreeDetailLevel(hlThreeDetail, d.get("Id4"), d.get("Name4"), d.get("Level4"));
				}

				custNo = d.get("CustNo");
				facmNo = d.get("FacmNo");
			}
			lHlThreeDetail.add(hlThreeDetail);
		}

		if (lHlThreeDetail != null && lHlThreeDetail.size() != 0) {
			try {
				hlThreeDetailService.insertAll(lHlThreeDetail, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "HlThreeDetail");
			}
		}

		this.batchTransaction.commit();
	}

	private HlThreeDetail sethlThreeDetailLevel(HlThreeDetail hlThreeDetail, String id, String name, String level) {
		if (level.length() > 0) {
			if ("Z".equals(level.substring(0, 1))) {
//				TeamChiefNo	統一編號(展業代表)
				hlThreeDetail.setTeamChiefNo(id);
//				TeamChiefName	員工姓名
				hlThreeDetail.setTeamChiefName(name);
			} else if ("K".equals(level.substring(0, 1))) {
//				Id3	統一編號(組長)
				hlThreeDetail.setId3(id);
//				Id3Name	員工姓名
				hlThreeDetail.setId3Name(name);
			} else if ("H".equals(level.substring(0, 1))) {
//				AreaChiefNo	統一編號(主任)
				hlThreeDetail.setAreaChiefNo(id);
//				AreaChiefName	員工姓名
				hlThreeDetail.setAreaChiefName(name);
			} else if ("E".equals(level.substring(0, 1))) {
//				UnitChiefNo	統一編號(單位主管/處長)
				hlThreeDetail.setUnitChiefNo(id);
//				UnitChiefName	員工姓名
				hlThreeDetail.setUnitChiefName(name);
			}
		}
		return hlThreeDetail;
	}

	// 單位、區部、部室業績累計檔
	private void procHlThreeLaqhcp(TitaVo titaVo) throws LogicException {
		this.info("L5500Batch.procHlThreeLaqhcp");
		// delete
		Slice<HlThreeLaqhcp> slHlThreeLaqhcp = hlThreeLaqhcpService.findAll(0, Integer.MAX_VALUE, titaVo);
		List<HlThreeLaqhcp> lHlThreeLaqhcp = slHlThreeLaqhcp == null ? null : slHlThreeLaqhcp.getContent();
		if (lHlThreeLaqhcp != null) {
			try {
				hlThreeLaqhcpService.deleteAll(lHlThreeLaqhcp, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "HlThreeLaqhcp");
			}
		}
		String sql = "select a.*";
		sql += ",NVL(b.\"DeptCnt\",0) as \"DeptCnt\"";
		sql += ",NVL(b.\"DeptAmt\",0) as \"DeptAmt\"";
		sql += ",NVL(c.\"DistCnt\",0) as \"DistCnt\"";
		sql += ",NVL(c.\"DistAmt\",0) as \"DistAmt\"";
		sql += ",NVL(d.\"UnitCnt\",0) as \"UnitCnt\"";
		sql += ",NVL(d.\"UnitAmt\",0) as \"UnitAmt\"";
		sql += ",NVL(bb.\"DeptCnt\",0) as \"tDeptCnt\"";
		sql += ",NVL(bb.\"DeptAmt\",0) as \"tDeptAmt\"";
		sql += ",NVL(cc.\"DistCnt\",0) as \"tDistCnt\"";
		sql += ",NVL(cc.\"DistAmt\",0) as \"tDistAmt\"";
		sql += ",NVL(dd.\"UnitCnt\",0) as \"tUnitCnt\"";
		sql += ",NVL(dd.\"UnitAmt\",0) as \"tUnitAmt\"";
		sql += "from \"PfDeparment\" a ";
		sql += "left join (";
		sql += "  select \"DeptCode\",sum(\"PerfCnt\") as \"DeptCnt\",sum(\"PerfAmt\") as \"DeptAmt\"";
		sql += "    from \"PfItDetail\" ";
		sql += "   where \"WorkMonth\"=:workmonth2 and \"DrawdownAmt\">0";
		sql += "   group by \"DeptCode\" ";
		sql += ") b on b.\"DeptCode\"=a.\"DeptCode\" ";
		sql += "left join (";
		sql += "  select \"DistCode\",sum(\"PerfCnt\") as \"DistCnt\",sum(\"PerfAmt\") as \"DistAmt\"";
		sql += "    from \"PfItDetail\" ";
		sql += "   where \"WorkMonth\"=:workmonth2 and \"DrawdownAmt\">0";
		sql += "   group by \"DistCode\" ";
		sql += ") c on c.\"DistCode\"=a.\"DistCode\" ";
		sql += "left join (";
		sql += "  select \"UnitCode\",sum(\"PerfCnt\") as \"UnitCnt\",sum(\"PerfAmt\") as \"UnitAmt\"";
		sql += "    from \"PfItDetail\" ";
		sql += "   where \"WorkMonth\"=:workmonth2 and \"DrawdownAmt\">0";
		sql += "   group by \"UnitCode\" ";
		sql += ") d on d.\"UnitCode\"=a.\"UnitCode\" ";
		sql += "left join (";
		sql += "  select \"DeptCode\",sum(\"PerfCnt\") as \"DeptCnt\",sum(\"PerfAmt\") as \"DeptAmt\"";
		sql += "    from \"PfItDetail\" ";
		sql += "   where \"WorkMonth\">=:workmonth1 and \"WorkMonth\"<=:workmonth2 and \"DrawdownAmt\">0";
		sql += "   group by \"DeptCode\" ";
		sql += ") bb on bb.\"DeptCode\"=a.\"DeptCode\" ";
		sql += "left join (";
		sql += "  select \"DistCode\",sum(\"PerfCnt\") as \"DistCnt\",sum(\"PerfAmt\") as \"DistAmt\"";
		sql += "    from \"PfItDetail\" ";
		sql += "   where \"WorkMonth\">=:workmonth1 and \"WorkMonth\"<=:workmonth2 and \"DrawdownAmt\">0";
		sql += "   group by \"DistCode\" ";
		sql += ") cc on cc.\"DistCode\"=a.\"DistCode\" ";
		sql += "left join (";
		sql += "  select \"UnitCode\",sum(\"PerfCnt\") as \"UnitCnt\",sum(\"PerfAmt\") as \"UnitAmt\"";
		sql += "    from \"PfItDetail\" ";
		sql += "   where \"WorkMonth\">=:workmonth1 and \"WorkMonth\"<=:workmonth2 and \"DrawdownAmt\">0";
		sql += "   group by \"UnitCode\" ";
		sql += ") dd on dd.\"UnitCode\"=a.\"UnitCode\" ";

		Map<String, String> conds = new HashMap<String, String>();

		conds.put("workmonth1", bworkmonthX);
		conds.put("workmonth2", workmonthX);

		List<Map<String, String>> data = l5500ServiceImpl.findData(index, limit, sql, conds, titaVo);
		lHlThreeLaqhcp = new ArrayList<HlThreeLaqhcp>();
		if (data != null && data.size() > 0) {
			for (Map<String, String> d : data) {
				HlThreeLaqhcp hlThreeLaqhcp = new HlThreeLaqhcp();

				HlThreeLaqhcpId hlThreeLaqhcpId = new HlThreeLaqhcpId();

				hlThreeLaqhcpId.setDeptCode(d.get("DeptCode"));
				hlThreeLaqhcpId.setDistCode(d.get("DistCode"));
				hlThreeLaqhcpId.setUnitCode(d.get("UnitCode"));

				hlThreeLaqhcp.setHlThreeLaqhcpId(hlThreeLaqhcpId);

				hlThreeLaqhcp.setCalDate(entday);
				hlThreeLaqhcp.setEmpNo(d.get("EmpNo"));
				hlThreeLaqhcp.setEmpName(d.get("EmpName"));
				hlThreeLaqhcp.setDeptName(d.get("DeptItem"));
				hlThreeLaqhcp.setDistName(d.get("DistItem"));
				hlThreeLaqhcp.setUnitName(d.get("UnitItem"));
				hlThreeLaqhcp.setDepartOfficer(d.get("DepartOfficer"));
				hlThreeLaqhcp.setDirectorCode(d.get("DirectorCode"));
				hlThreeLaqhcp.setGoalNum(new BigDecimal(d.get("GoalCnt")));
				hlThreeLaqhcp.setGoalAmt(new BigDecimal(d.get("GoalAmt")));
				hlThreeLaqhcp.setTGoalNum(new BigDecimal(d.get("SumGoalCnt")));
				hlThreeLaqhcp.setTGoalAmt(new BigDecimal(d.get("SumGoalAmt")));

				if (d.get("UnitCode").isEmpty() && d.get("DistCode").isEmpty()) {
					hlThreeLaqhcp.setActNum(new BigDecimal(d.get("DeptCnt")));
					hlThreeLaqhcp.setActAmt(new BigDecimal(d.get("DeptAmt")));
					hlThreeLaqhcp.setTActNum(new BigDecimal(d.get("tDeptCnt")));
					hlThreeLaqhcp.setTActAmt(new BigDecimal(d.get("tDeptAmt")));
				} else if (d.get("UnitCode").isEmpty()) {
					hlThreeLaqhcp.setActNum(new BigDecimal(d.get("DistCnt")));
					hlThreeLaqhcp.setActAmt(new BigDecimal(d.get("DistAmt")));
					hlThreeLaqhcp.setTActNum(new BigDecimal(d.get("tDistCnt")));
					hlThreeLaqhcp.setTActAmt(new BigDecimal(d.get("tDistAmt")));
				} else {
					hlThreeLaqhcp.setActNum(new BigDecimal(d.get("UnitCnt")));
					hlThreeLaqhcp.setActAmt(new BigDecimal(d.get("UnitAmt")));
					hlThreeLaqhcp.setTActNum(new BigDecimal(d.get("tUnitCnt")));
					hlThreeLaqhcp.setTActAmt(new BigDecimal(d.get("tUnitAmt")));
				}
				if (hlThreeLaqhcp.getGoalAmt().compareTo(BigDecimal.ZERO) == 0 || hlThreeLaqhcp.getActAmt().compareTo(BigDecimal.ZERO) == 0) {
					hlThreeLaqhcp.setActRate(BigDecimal.ZERO);
				} else {
					BigDecimal ma = hlThreeLaqhcp.getActAmt().divide(hlThreeLaqhcp.getGoalAmt(), 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100").setScale(2, BigDecimal.ROUND_UP));

					hlThreeLaqhcp.setActRate(ma);
				}
				if (hlThreeLaqhcp.getTGoalAmt().compareTo(BigDecimal.ZERO) == 0 || hlThreeLaqhcp.getTActAmt().compareTo(BigDecimal.ZERO) == 0) {
					hlThreeLaqhcp.setTActRate(BigDecimal.ZERO);
				} else {
					BigDecimal ma = hlThreeLaqhcp.getTActAmt().divide(hlThreeLaqhcp.getTGoalAmt(), 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100").setScale(2, BigDecimal.ROUND_UP));

					hlThreeLaqhcp.setTActRate(ma);
				}

				hlThreeLaqhcp.setUpNo(1);
				hlThreeLaqhcp.setProcessDate(entday);

				lHlThreeLaqhcp.add(hlThreeLaqhcp);
			}

		}

		if (lHlThreeLaqhcp != null && lHlThreeLaqhcp.size() != 0) {
			try {
				hlThreeLaqhcpService.insertAll(lHlThreeLaqhcp, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "HlThreeLaqhcp");
			}
		}

		this.batchTransaction.commit();
	}

	// 區域中心房貸專員業績統計
	private void procHlAreaLnYg6Pt(TitaVo titaVo) throws LogicException {
		this.info("L5500Batch.procHlAreaLnYg6Pt begin");
		// delete
		Slice<HlAreaLnYg6Pt> slHlAreaLnYg6Pt = hlAreaLnYg6PtService.findCalDate(entday, 0, Integer.MAX_VALUE);
		List<HlAreaLnYg6Pt> lHlAreaLnYg6Pt = slHlAreaLnYg6Pt == null ? null : slHlAreaLnYg6Pt.getContent();
		if (lHlAreaLnYg6Pt != null) {
			try {
				hlAreaLnYg6PtService.deleteAll(lHlAreaLnYg6Pt, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "HlAreaLnYg6Pt");
			}
		}
		String sql = "select a.\"AreaCode\" ";
		sql += ",NVL(b.\"lPerfCnt\",0) as \"lPerfCnt\",NVL(b.\"lPerfAmt\",0) as \"lPerfAmt\" ";
		sql += ",NVL(c.\"PerfCnt\",0) as \"PerfCnt\",NVL(c.\"PerfAmt\",0) as \"PerfAmt\" ";
		sql += "from (select distinct(\"AreaCode\") as \"AreaCode\" from \"PfBsOfficer\" where \"AreaCode\" is not null) a ";
		sql += "left join (";
		sql += "  select bb.\"AreaCode\",sum(NVL(cc.\"AdjPerfCnt\",aa.\"PerfCnt\")) as \"lPerfCnt\",sum(NVL(cc.\"AdjPerfAmt\",aa.\"PerfAmt\")) as \"lPerfAmt\" ";
		sql += "  from \"PfBsDetail\" aa ";
		sql += "  left join \"PfBsOfficer\" bb on bb.\"WorkMonth\"=:lworkmonth and bb.\"EmpNo\"=aa.\"BsOfficer\" ";
		sql += "  left join \"PfBsDetailAdjust\" cc on cc.\"CustNo\"=aa.\"CustNo\" and cc.\"FacmNo\"=aa.\"FacmNo\" and cc.\"BormNo\"=aa.\"BormNo\" ";
		sql += "  where aa.\"WorkMonth\"=:lworkmonth and bb.\"AreaCode\" is not null ";
		sql += "  group by bb.\"AreaCode\"";
		sql += ") b on b.\"AreaCode\"=a.\"AreaCode\" ";
		sql += "left join (";
		sql += "  select bb.\"AreaCode\",sum(NVL(cc.\"AdjPerfCnt\",aa.\"PerfCnt\")) as \"PerfCnt\",sum(NVL(cc.\"AdjPerfAmt\",aa.\"PerfAmt\")) as \"PerfAmt\" ";
		sql += "  from \"PfBsDetail\" aa ";
		sql += "  left join \"PfBsOfficer\" bb on bb.\"WorkMonth\"=:workmonth and bb.\"EmpNo\"=aa.\"BsOfficer\" ";
		sql += "  left join \"PfBsDetailAdjust\" cc on cc.\"CustNo\"=aa.\"CustNo\" and cc.\"FacmNo\"=aa.\"FacmNo\" and cc.\"BormNo\"=aa.\"BormNo\" ";
		sql += "  where aa.\"WorkMonth\"=:workmonth and bb.\"AreaCode\" is not null ";
		sql += "  group by bb.\"AreaCode\" ";
		sql += ") c on c.\"AreaCode\"=a.\"AreaCode\" ";
		sql +="order by a.\"AreaCode\" ";

		Map<String, String> conds = new HashMap<String, String>();

		conds.put("lworkmonth", lworkmonthX);
		conds.put("workmonth", workmonthX);

		List<Map<String, String>> data = l5500ServiceImpl.findData(index, limit, sql, conds, titaVo);
		lHlAreaLnYg6Pt = new ArrayList<HlAreaLnYg6Pt>();
		if (data != null && data.size() > 0) {
			for (Map<String, String> d : data) {
				HlAreaLnYg6Pt hlAreaLnYg6Pt = new HlAreaLnYg6Pt();

				HlAreaLnYg6PtId hlAreaLnYg6PtId = new HlAreaLnYg6PtId();
				hlAreaLnYg6PtId.setWorkYM(workmonth);
				hlAreaLnYg6PtId.setAreaCode(d.get("AreaCode"));

				hlAreaLnYg6Pt.setHlAreaLnYg6PtId(hlAreaLnYg6PtId);
//				LstAppNum
//				LstAppAmt
//				TisAppNum
//				TisAppAmt

				hlAreaLnYg6Pt.setLstAppNum(new BigDecimal(d.get("lPerfCnt")));
				hlAreaLnYg6Pt.setLstAppAmt(new BigDecimal(d.get("lPerfAmt")));
				hlAreaLnYg6Pt.setTisAppNum(new BigDecimal(d.get("PerfCnt")));
				hlAreaLnYg6Pt.setTisAppAmt(new BigDecimal(d.get("PerfAmt")));
				hlAreaLnYg6Pt.setCalDate(entday);
				hlAreaLnYg6Pt.setUpNo(1);

				lHlAreaLnYg6Pt.add(hlAreaLnYg6Pt);
			}

		}

		if (lHlAreaLnYg6Pt != null && lHlAreaLnYg6Pt.size() != 0) {
			try {
				hlAreaLnYg6PtService.insertAll(lHlAreaLnYg6Pt, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "HlAreaLnYg6Pt");
			}
		}

		this.batchTransaction.commit();
	}

	// 房貨專員目標檔案

	private void procHlEmpLnYg5Pt(TitaVo titaVo) throws LogicException {
		this.info("L5500Batch.procHlEmpLnYg5Pt begin");
		// delete
		Slice<HlEmpLnYg5Pt> slHlEmpLnYg5Pt = hlEmpLnYg5PtService.findCalDate(entday, 0, Integer.MAX_VALUE);
		List<HlEmpLnYg5Pt> lHlEmpLnYg5Pt = slHlEmpLnYg5Pt == null ? null : slHlEmpLnYg5Pt.getContent();
		if (lHlEmpLnYg5Pt != null) {
			try {
				hlEmpLnYg5PtService.deleteAll(lHlEmpLnYg5Pt, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "HlEmpLnYg5Pt");
			}
		}

		String sql = "select a.*,NVL(b.\"PerfCnt\",0) as \"PerfCnt\",NVL(b.\"PerfAmt\",0) as \"PerfAmt\" ";
		sql += "from \"PfBsOfficer\" a ";
		sql += "left join (";
		sql += "select \"BsOfficer\",sum(\"PerfCnt\") as \"PerfCnt\",sum(\"PerfAmt\") as \"PerfAmt\" from ( ";
		sql += "  select aa.\"BsOfficer\"";
		sql += "        ,NVL(bb.\"AdjPerfCnt\",aa.\"PerfCnt\") as \"PerfCnt\"";
		sql += "        ,NVL(bb.\"AdjPerfAmt\",aa.\"PerfAmt\") as \"PerfAmt\"";
		sql += "    from \"PfBsDetail\" aa ";
		sql += "    left join \"PfBsDetailAdjust\" bb on bb.\"CustNo\"=aa.\"CustNo\" and bb.\"FacmNo\"=aa.\"FacmNo\" and bb.\"BormNo\"=aa.\"BormNo\" ";
		sql += "    where aa.\"WorkMonth\"=:workmonth and aa.\"RepayType\"=0 and aa.\"BsOfficer\" is not null ";
		sql += "  ) group by \"BsOfficer\" ";
		sql += ") b on b.\"BsOfficer\" = a.\"EmpNo\" ";
		sql += "where a.\"WorkMonth\"=:workmonth ";
		sql += "order by a.\"AreaCode\",a.\"EmpNo\" ";

		Map<String, String> conds = new HashMap<String, String>();

		conds.put("workmonth", workmonthX);

		List<Map<String, String>> data = l5500ServiceImpl.findData(index, limit, sql, conds, titaVo);
		lHlEmpLnYg5Pt = new ArrayList<HlEmpLnYg5Pt>();
		if (data != null && data.size() > 0) {
			for (Map<String, String> d : data) {
				HlEmpLnYg5Pt hlEmpLnYg5Pt = new HlEmpLnYg5Pt();

				HlEmpLnYg5PtId hlEmpLnYg5PtId = new HlEmpLnYg5PtId();
				hlEmpLnYg5PtId.setWorkYM(workmonth);
				hlEmpLnYg5PtId.setEmpNo(d.get("EmpNo"));

				hlEmpLnYg5Pt.setHlEmpLnYg5PtId(hlEmpLnYg5PtId);
				hlEmpLnYg5Pt.setFullname(d.get("Fullname"));
				hlEmpLnYg5Pt.setAreaCode(d.get("AreaCode"));
				hlEmpLnYg5Pt.setAreaItem(d.get("AreaItem"));
				hlEmpLnYg5Pt.setDeptCode(d.get("DeptCode"));
				hlEmpLnYg5Pt.setDepItem(d.get("DepItem"));
				hlEmpLnYg5Pt.setDistCode(d.get("DistCode"));
				hlEmpLnYg5Pt.setDistItem(d.get("DistItem"));
				hlEmpLnYg5Pt.setStationName(d.get("StationName"));
				hlEmpLnYg5Pt.setGoalAmt(new BigDecimal(d.get("GoalAmt")));
				hlEmpLnYg5Pt.setHlAppAmt(new BigDecimal(d.get("PerfAmt")));
				hlEmpLnYg5Pt.setHlAppNum(new BigDecimal(d.get("PerfCnt")));
				hlEmpLnYg5Pt.setCalDate(entday);
				hlEmpLnYg5Pt.setUpNo(1);

				lHlEmpLnYg5Pt.add(hlEmpLnYg5Pt);
			}

		}

		if (lHlEmpLnYg5Pt != null && lHlEmpLnYg5Pt.size() != 0) {
			try {
				hlEmpLnYg5PtService.insertAll(lHlEmpLnYg5Pt, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "HlEmpLnYg5Pt");
			}
		}

		this.batchTransaction.commit();
	}

	// 借款人資料
	private void ProcHlCusData(TitaVo titaVo) throws LogicException {
		this.info("L5500Batch.ProcHlCusData begin");
		// initialize
		Slice<HlCusData> slHlCusData = hlCusDataService.findAll(0, Integer.MAX_VALUE);
		List<HlCusData> lHlCusData = slHlCusData == null ? null : slHlCusData.getContent();
		if (lHlCusData != null) {
			try {
				hlCusDataService.deleteAll(lHlCusData, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "HlCusData");
			}
		}

		String sql = "select \"CustNo\",\"CustName\",\"LastUpdate\" ";
		sql += "from \"CustMain\" ";
		sql += "where \"CustNo\" > 0 ";

		List<Map<String, String>> dataCusData = l5500ServiceImpl.findData(index, limit, sql, null, titaVo);
		lHlCusData = new ArrayList<HlCusData>();
		if (dataCusData != null && dataCusData.size() > 0) {
			for (Map<String, String> d : dataCusData) {
				HlCusData hlCusData = new HlCusData();

				hlCusData.setHlCusNo(Long.valueOf(d.get("CustNo").toString()));
				hlCusData.setHlCusName(d.get("CustName").substring(0, d.get("CustName").length() > 50 ? 50 : d.get("CustName").length()));
				this.info(parse.stringToStringDate(d.get("LastUpdate")) + "-" + parse.stringToInteger(parse.stringToStringDate(d.get("LastUpdate"))));

				int processDate = parse.stringToInteger(parse.stringToStringDate(d.get("LastUpdate")));
				if (processDate > 0) {
					processDate += 19110000;
				}
				hlCusData.setProcessDate(processDate);
				lHlCusData.add(hlCusData);
			}

		}

		if (lHlCusData != null && lHlCusData.size() != 0) {
			try {
				hlCusDataService.insertAll(lHlCusData, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "HlCusData");
			}
		}

		this.batchTransaction.commit();

	}

	// 區域資料主檔
	private void ProcHlAreaData(TitaVo titaVo) throws LogicException {
		this.info("L5500.ProcHlAreaData begin");
		// initialize
		Slice<HlAreaData> slHlAreaData = hlAreaDataService.findAll(0, Integer.MAX_VALUE);
		List<HlAreaData> lHlAreaData = slHlAreaData == null ? null : slHlAreaData.getContent();
		if (lHlAreaData != null) {
			try {
				hlAreaDataService.deleteAll(lHlAreaData, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "HlAreaData");
			}
		}

		// insert
		this.info("L5500 DataInsertUpdateAreaData");
		String sql = "select A.\"UnitCode\",";
		sql += "A.\"UnitItem\",";
		sql += "A.\"UnitManager\",";
		sql += "B.\"Fullname\" ";
		sql += "from \"CdBcm\" A ";
		sql += "left join \"CdEmp\" B ON B.\"EmployeeNo\"=A.\"UnitManager\" ";
		sql += "where A.\"Enable\" = 'Y' ";

		List<Map<String, String>> dataAreaData = l5500ServiceImpl.findData(index, limit, sql, null, titaVo);
		lHlAreaData = new ArrayList<HlAreaData>();
		if (dataAreaData != null && dataAreaData.size() > 0) {
			for (Map<String, String> d : dataAreaData) {
//				this.info(d.get("UnitCode") + "=" + d.get("UnitItem") + "/" + d.get("UnitItem").toString().length());
				HlAreaData tHlAreaData = new HlAreaData();
				tHlAreaData.setAreaUnitNo(d.get("UnitCode"));// 區域代碼--VARCHAR2(6)
				tHlAreaData.setAreaName(d.get("UnitItem"));// 區域名稱--VARCHAR2(20)
				tHlAreaData.setAreaChiefEmpNo(d.get("UnitManager"));// 區域主管員編--VARCHAR2(6)
				tHlAreaData.setAreaChiefName(d.get("Fullname"));// 區域主管名稱--NVARCHAR2(15)

				lHlAreaData.add(tHlAreaData);
			}
		}

		if (lHlAreaData != null && lHlAreaData.size() != 0) {
//			this.info("lHlAreaData = " + lHlAreaData.size());
			try {
				hlAreaDataService.insertAll(lHlAreaData, titaVo);
			} catch (DBException e) {
//				this.info("L5500 HlAreaData[" + e.getErrorMsg() + "]");
				throw new LogicException(titaVo, "E0005", "HlAreaData");
			}
		}
		this.batchTransaction.commit();
	}

}