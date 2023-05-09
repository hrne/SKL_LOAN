package com.st1.itx.trade.LC;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.TxAttachment;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.TxAttachmentService;
import com.st1.itx.db.service.springjpa.cm.L6064ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("LC014")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class LC014 extends TradeBuffer {
	@Autowired
	public TxAttachmentService txAttachmentService;

	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	public L6064ServiceImpl l6064ServiceImpls;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC014 ");
		this.totaVo.init(titaVo);

		String iTranNo = titaVo.getParam("TranNo");
		String iMrKey = titaVo.getParam("MrKey");
		String itranl = titaVo.getEmpNot(); // 櫃員 編號
		Slice<TxAttachment> slTxAttachment = txAttachmentService.findByTran(iTranNo, iMrKey, 0, Integer.MAX_VALUE,
				titaVo);
		List<TxAttachment> lTxAttachment = slTxAttachment == null ? null : slTxAttachment.getContent();

		if (lTxAttachment == null || lTxAttachment.size() == 0) {
			throw new LogicException(titaVo, "E0001", "附件資料"); // 查無資料
		}

		for (TxAttachment txAttachment : lTxAttachment) {
			OccursList occursList = new OccursList();
			occursList.putParam("OFileNo", txAttachment.getFileNo());
			occursList.putParam("OTypeItem", txAttachment.getTypeItem());
			occursList.putParam("ODesc", txAttachment.getDesc());
			occursList.putParam("OCreateDate", parse.stringToStringDateTime(txAttachment.getCreateDate().toString()));

			String empNo = txAttachment.getCreateEmpNo();

			CdEmp cdEmp = cdEmpService.findById(empNo, titaVo);

			if (cdEmp != null) {
				empNo = empNo + ' ' + cdEmp.getFullname();
			}

			occursList.putParam("OCreateEmp", empNo);
			String iCreate = txAttachment.getCreateEmpNo();
			// 新增刪除記號 判斷L6064是否有Code有紀錄該交易有的畫判斷是否為建立人查詢顯示，如果是本人才會顯示刪除按紐
			// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
			this.limit = Integer.MAX_VALUE; // 217 * 200 = 43400
			this.info("iTranNo   = " + iTranNo);
			this.info("iCreate   = " + iCreate);
			List<Map<String, String>> L6064DateList = null;
			try {
				L6064DateList = l6064ServiceImpls.findAll("", "UplowDelBtn", iTranNo, "", this.index, this.limit,
						titaVo);
			} catch (Exception e) {
				throw new LogicException(titaVo, "E0001", "SQL ERROR");
			}
			if (L6064DateList.size() != 0) {
				if (itranl.equals(iCreate)) {
					occursList.putParam("OCtrlCode", 1);
				} else {
					occursList.putParam("OCtrlCode", 0);
				}
			} else {
				occursList.putParam("OCtrlCode", 0);
			}

			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}