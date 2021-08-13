package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.PfBsOfficer;
import com.st1.itx.db.domain.PfBsOfficerId;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.PfBsOfficerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5R10")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R10 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5R10.class);
	
	/* DB服務注入 */
	@Autowired
	public CdEmpService sCdEmpService;
	
	@Autowired
	public PfBsOfficerService iPfBsOffcierService;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		//L5401查詢調RIM用，資料來源為PfBsOfficer表
		this.info("active L5R10 ");
		this.info("L5R10 titaVo=["+titaVo+"]");
		this.totaVo.init(titaVo);
		String iEmpNo = titaVo.getParam("RimEmpNo").trim();//員工代號
		this.info("TWorkMOnth="+titaVo.getParam("RimWorkMonth"));
		int iWorkMonth = Integer.valueOf(titaVo.getParam("RimWorkMonth"))+191100;
		PfBsOfficerId iPfBsOfficerId = new PfBsOfficerId();
		iPfBsOfficerId.setEmpNo(iEmpNo);
		iPfBsOfficerId.setWorkMonth(iWorkMonth);
		PfBsOfficer rPfBsOfficer = iPfBsOffcierService.findById(iPfBsOfficerId,titaVo);
		if(rPfBsOfficer==null) {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}else {
			totaVo.putParam("L5R10FullName", rPfBsOfficer.getFullname());//員工姓名
			totaVo.putParam("L5R10AreaCode",rPfBsOfficer.getAreaCode()); //區域中心
			totaVo.putParam("L5R10AreaItem",rPfBsOfficer.getAreaItem()); //區域中文
			totaVo.putParam("L5R10DeptCode",rPfBsOfficer.getDeptCode()); //區部代號
			totaVo.putParam("L5R10DepItem",rPfBsOfficer.getDepItem()); //區部中文
			totaVo.putParam("L5R10DistCode",rPfBsOfficer.getDistCode()); //部室代號
			totaVo.putParam("L5R10DistItem",rPfBsOfficer.getDistItem()); //部室中文
			totaVo.putParam("L5R10GoalAmt",rPfBsOfficer.getGoalAmt()); //目標金額
			totaVo.putParam("L5R10SmryGoalAmt",rPfBsOfficer.getSmryGoalAmt()); //累計目標金額
			totaVo.putParam("L5R10StationName",rPfBsOfficer.getStationName()); //駐在地
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}