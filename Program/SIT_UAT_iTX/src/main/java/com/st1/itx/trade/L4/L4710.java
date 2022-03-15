package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.domain.TxToDoDetailId;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4710")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4710 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4710.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	DateUtil dateUtil;

	@Autowired
	TxToDoDetailService txToDoDetailService;

	@Autowired
	public MakeFile makeFile;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
//		this.info("active L4710 ");
//		this.totaVo.init(titaVo);

//		每天14:30批次執行此交易，將txtododetail之批號=TEXT00、狀態:0.未處理 組簡訊產出file
//		L4453 L4454 L4603 L4703

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		Slice<TxToDoDetail> sTxToDoDetail = null;

		dateUtil.init();

		dateUtil.setDate_1(dateUtil.getNowIntegerForBC());
		dateUtil.setDate_2(dateUtil.getNowIntegerForBC());

		this.info("dateUtil.getNowIntegerForBC()" + dateUtil.getNowIntegerForBC());
		this.info("dateUtil.isHoliDay()" + dateUtil.isHoliDay());

		if (!dateUtil.isHoliDay()) {
			List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();

			sTxToDoDetail = txToDoDetailService.detailStatusRange("TEXT00", 0, 0, this.index, this.limit);

			lTxToDoDetail = sTxToDoDetail == null ? null : sTxToDoDetail.getContent();

//			temp path = D:\\tmp\\LNM56OP.txt
			makeFile.open(titaVo, dateUtil.getNowIntegerForBC(), "0000", "L4710", "L4710" + "-簡訊媒體檔", "LNM56OP.txt", 2);

			if (lTxToDoDetail != null && lTxToDoDetail.size() != 0) {
				for (TxToDoDetail tTxToDoDetail : lTxToDoDetail) {
					TxToDoCom tTxToDoCom = new TxToDoCom();
//					1.產出
					makeFile.put(tTxToDoDetail.getProcessNote());

//					2.回寫狀態
					TxToDoDetailId tTxToDoDetailId = new TxToDoDetailId();
					tTxToDoDetailId.setCustNo(tTxToDoDetail.getCustNo());
					tTxToDoDetailId.setFacmNo(tTxToDoDetail.getFacmNo());
					tTxToDoDetailId.setBormNo(tTxToDoDetail.getBormNo());
					tTxToDoDetailId.setDtlValue(tTxToDoDetail.getDtlValue());
					tTxToDoDetailId.setItemCode(tTxToDoDetail.getItemCode());

					tTxToDoCom.updDetailStatus(2, tTxToDoDetailId, titaVo);
				}
				long sno = makeFile.close();

				this.info("sno : " + sno);
				makeFile.toFile(sno);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}