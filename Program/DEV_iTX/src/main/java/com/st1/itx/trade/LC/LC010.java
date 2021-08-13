package com.st1.itx.trade.LC;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.TxLock;
import com.st1.itx.db.service.TxLockService;

@Service("LC010")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LC010 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(LC010.class);

	/* DB服務注入 */
	@Autowired
	public TxLockService txLockService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC010 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		Slice<TxLock> slTxLock = txLockService.findAll(this.index, this.limit);
		List<TxLock> lTxLock = slTxLock == null ? null : slTxLock.getContent();

		if (lTxLock != null) {
			for (TxLock tTxLock : lTxLock) {
				OccursList occursList = new OccursList();
				occursList.putParam("OLockNo", tTxLock.getLockNo());
				occursList.putParam("OCustNo", tTxLock.getCustNo());
				occursList.putParam("OTranNo", tTxLock.getTranNo());
				occursList.putParam("OBrNo", tTxLock.getBrNo());
				occursList.putParam("OTlrNo", tTxLock.getCreateEmpNo());
				this.info("tTxLock.getCreateDate() = " + tTxLock.getCreateDate().toString());
				occursList.putParam("OTxDate", DbDateToRocDate(tTxLock.getCreateDate().toString()));
				occursList.putParam("OTxTime", DbDateToRocTime(tTxLock.getCreateDate().toString()));
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException(titaVo, "EC001", "");
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slTxLock != null && slTxLock.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String DbDateToRocDate(String DbDate) {
		this.info("LC010 DbDateToRocDate : " + DbDate);

		String SysDateY = DbDate.substring(0, 4);
		int RocDate9 = Integer.valueOf(SysDateY) - 1911;
		String RocDate = String.valueOf(RocDate9) + "/" + DbDate.substring(5, 7) + "/" + DbDate.substring(8, 10);

		return RocDate;
	}

	private String DbDateToRocTime(String DbDate) {
		// String SysTime = DbDate.substring(11, 13) + ":" + DbDate.substring(14, 16);
		String SysTime = DbDate.substring(11, 19);

		return SysTime;
	}

}