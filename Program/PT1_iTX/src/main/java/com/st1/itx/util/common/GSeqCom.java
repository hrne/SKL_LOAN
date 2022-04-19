package com.st1.itx.util.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdGseq;
import com.st1.itx.db.domain.CdGseqId;
import com.st1.itx.db.service.CdGseqService;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 自動取號<BR>
 * getSeqNo 使用編號編碼檔取號 call by LXXXX<BR>
 * 
 * @author st1
 *
 */
@Component("gSeqCom")
@Scope("prototype")
public class GSeqCom {
	@Autowired
	public CdGseqService cdGseqService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;

	/**
	 * getSeqNo 使用編號編碼檔取號
	 * 
	 * @param gDate   取號日期(中曆日期)
	 * @param gCode   編號方式:0:不分 1:年度編號 2:月份編號 3:日編號
	 * @param gType   業務大類
	 * @param gKind   交易種類
	 * @param gOffset 取號最大有效值
	 * @param titaVo  TitaVo
	 * @return 新編號
	 * @throws LogicException 例外處理
	 */
	public int getSeqNo(int gDate, int gCode, String gType, String gKind, int gOffset, TitaVo titaVo) throws LogicException {
		int wkSeqNo = 0;
		int gseqDate = 0;

		// 0:不分 1090701->0
		// 1:年度編號 1090701->20200000
		// 2:月份編號 1090701->20200700
		// 3:日編號 1090701->20200701
		switch (gCode) {
		case 1:
			gseqDate = (gDate / 10000 * 10000);
			break;
		case 2:
			gseqDate = (gDate / 100 * 100);
			break;
		case 3:
			gseqDate = gDate;
			break;
		default:
			gseqDate = 0;
			break;
		}

		if (gseqDate > 0 && gseqDate < 19110000) {
			gseqDate = gseqDate + 19110000;
		}

		CdGseqId tCdGseqId = new CdGseqId();
		tCdGseqId.setGseqDate(gseqDate);
		tCdGseqId.setGseqCode(gCode);
		tCdGseqId.setGseqType(gType);
		tCdGseqId.setGseqKind(gKind);
		CdGseq tCdGseq = cdGseqService.holdById(tCdGseqId, titaVo);
		if (tCdGseq != null) {
			if (tCdGseq.getSeqNo() >= gOffset) {
				wkSeqNo = 1;
			} else {
				wkSeqNo = tCdGseq.getSeqNo() + 1;
			}
			tCdGseq.setSeqNo(wkSeqNo);
			try {
				cdGseqService.update(tCdGseq, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E6010", "編號編碼檔"); // 更新資料時，發生錯誤
			}
		} else {
			wkSeqNo = 1;
			tCdGseq = new CdGseq();
			tCdGseq.setGseqDate(gseqDate);
			tCdGseq.setGseqCode(gCode);
			tCdGseq.setGseqType(gType);
			tCdGseq.setGseqKind(gKind);
			tCdGseq.setCdGseqId(tCdGseqId);
			tCdGseq.setOffset(gOffset);
			tCdGseq.setSeqNo(wkSeqNo);
			try {
				cdGseqService.insert(tCdGseq, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E6009", "編號編碼檔"); // 新增資料時，發生錯誤
			}
		}
		return wkSeqNo;
	}
}
