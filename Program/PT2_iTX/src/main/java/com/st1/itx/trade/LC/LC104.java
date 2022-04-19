package com.st1.itx.trade.LC;

import java.io.File;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxAttachment;
import com.st1.itx.db.service.TxAttachmentService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.parse.ZLibUtils;

@Service("LC104")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class LC104 extends TradeBuffer {
	// 檔案輸出路徑
	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Autowired
	public FileCom fileCom;

	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	ZLibUtils zLibUtils;

	@Autowired
	public TxAttachmentService txAttachmentService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC104 ");
		this.totaVo.init(titaVo);

		String fileItem = titaVo.getParam("FileItem");

		String fileName = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo() + File.separatorChar + fileItem;

		this.info("LC104 filename = " + fileName);

		TxAttachment txAttachment = new TxAttachment();

		txAttachment.setTranNo(titaVo.getParam("TranNo"));
		txAttachment.setCustNo(Integer.valueOf(titaVo.getParam("CustNo")));
		txAttachment.setFacmNo(Integer.valueOf(titaVo.getParam("FacmNo")));
		txAttachment.setBormNo(Integer.valueOf(titaVo.getParam("BormNo")));
		txAttachment.setMrKey(titaVo.getParam("MrKey"));
		txAttachment.setTypeItem(titaVo.getParam("TypeItem"));
		txAttachment.setFileItem(titaVo.getParam("FileItem"));
		txAttachment.setDesc(titaVo.getParam("Desc"));
		txAttachment.setStatus(0);

		txAttachment.setFileData(zLibUtils.compress(new File(fileName)));

		try {
			txAttachmentService.insert(txAttachment, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", e.getErrorMsg());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}