package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.db.service.CollRemindService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.CollList;
import com.st1.itx.db.domain.CollListId;
/* DB容器 */
import com.st1.itx.db.domain.CollRemind;
import com.st1.itx.db.domain.CollRemindId;

@Component("L5605")
@Scope("prototype")

/**
 * 提醒資料登錄
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5605 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public CollRemindService collremindservice;
	@Autowired
	public CollListService colllistservice;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		this.info("L5605 start");
		int icustno = Integer.valueOf(titaVo.getParam("CustNo"));
		int ifacmno = Integer.valueOf(titaVo.getParam("FacmNo"));
		String ifunctioncd = titaVo.getParam("FunctionCd");
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		// 進主檔找是否有這筆資料
		CollListId icolllistid = new CollListId();
		icolllistid.setCustNo(icustno);
		icolllistid.setFacmNo(ifacmno);
		CollList icolllist = colllistservice.findById(icolllistid, titaVo);
		if (icolllist == null) {
			throw new LogicException(titaVo, "E0005", "");
		}
		// 從找到的資料挖出擔保品戶號、額度
		int iclcustno = icolllist.getClCustNo();
		int iclfacmno = icolllist.getClFacmNo();
		// 用撈出的擔保品編號找全部相同擔保品的資料
		Slice<CollList> allcolllist = colllistservice.findCl(iclcustno, iclfacmno, this.index, this.limit, titaVo);
		if (allcolllist == null) {
			throw new LogicException(titaVo, "E0005", "");
		}
		// 處理找出的資料(包含法催檔登錄和主檔更新)
		for (CollList colllistVo : allcolllist) {
			CollRemindId icollremindid = new CollRemindId();
			CollRemind icollremind = new CollRemind();
			icollremindid.setCaseCode(titaVo.getParam("CaseCode"));
			icollremindid.setCustNo(colllistVo.getCustNo());
			icollremindid.setFacmNo(colllistVo.getFacmNo());
			if (ifunctioncd.equals("1")) {
				icollremindid.setTitaTlrNo(titaVo.getTlrNo());
				icollremindid.setTitaTxtNo(titaVo.getTxtNo());
				icollremindid.setAcDate(Integer.valueOf(titaVo.getEntDy()));// 營業日 放acdate
			} else {
				icollremindid.setTitaTlrNo(titaVo.getParam("TitaTlrNo"));
				icollremindid.setTitaTxtNo(titaVo.getParam("TitaTxtNo"));
				icollremindid.setAcDate(Integer.valueOf(titaVo.getParam("TitaAcDate")));// 營業日 放acdate
			}
			icollremind.setCollRemindId(icollremindid);
			icollremind.setCondCode(titaVo.getParam("CondCode"));
			icollremind.setRemindDate(Integer.valueOf(titaVo.getParam("RemindDate")));
			icollremind.setEditDate(Integer.valueOf(titaVo.getParam("EditDate")));
			icollremind.setEditTime(titaVo.getParam("EditTime"));
			icollremind.setRemindCode(titaVo.getParam("RemindCode"));
			icollremind.setRemark(titaVo.getParam("Remark"));
			CollRemind tcollremind = collremindservice.findById(icollremindid, titaVo);
			if (ifunctioncd.equals("1")) {
				if (tcollremind == null) {
					try {
						collremindservice.insert(icollremind, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg());
					}
				} else {
					throw new LogicException(titaVo, "E0012", "");
				}
			}
//			if(ifunctioncd.equals("2")) {
			else {
				if (tcollremind != null) {
					try {
						collremindservice.holdById(icollremindid);
						icollremind.setCreateDate(tcollremind.getCreateDate()); // 補上createdate
						icollremind.setCreateEmpNo(tcollremind.getCreateEmpNo()); // 補上createempno
						collremindservice.update(icollremind, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E00075", e.getErrorMsg());
					}
				} else {
					throw new LogicException(titaVo, "E0003", "");
				}
			}
//			if(ifunctioncd.equals("4")||ifunctioncd.equals("6")) {
//				if(tcollremind!=null) {
//					try{
//						collremindservice.delete(icollremind);
//					}catch(DBException e) {
//						throw new LogicException(titaVo, "E0005", e.getErrorMsg()); //電催檔更新資料錯誤
//					}
//				}else {
//					throw new LogicException(titaVo, "E0005", ""); //電催檔找不到資料錯誤
//				}
//			}
			// 更新list的上次作業日期和項目
			CollListId ccolllistid = new CollListId();
			ccolllistid.setCustNo(colllistVo.getCustNo());
			ccolllistid.setFacmNo(colllistVo.getFacmNo());
			CollList necollist = colllistservice.findById(ccolllistid, titaVo);
			if (necollist != null) {
				try {
					CollList upcollist = colllistservice.holdById(ccolllistid);
					upcollist.setTxCode("6"); // 上次作業項目
					upcollist.setTxDate(Integer.valueOf(titaVo.getEntDy())); // 上次作業日期
					colllistservice.update(upcollist, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 主檔更新錯誤訊息
				}
			} else {
				throw new LogicException(titaVo, "E0005", ""); // 主檔無資料錯誤訊息
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

}
