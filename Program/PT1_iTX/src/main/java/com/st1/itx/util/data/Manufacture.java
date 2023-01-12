package com.st1.itx.util.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.db.domain.TxFile;
import com.st1.itx.db.service.TxFileService;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;

@Service("manufacture")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class Manufacture extends CommBuffer {
	/* DB服務注入 */
	@Autowired
	public TxFileService sTxFileService;

	@Autowired
	public MakeReport makeReport;

	@Autowired
	public MakeExcel makeExcel;

	@Autowired
	public MakeFile makeFile;

	@Autowired
	public DateUtil dateUtil;

	@Value("${iTXOutFolder}")
	private String OutFolder = "";

	private int showmode;

	private String filename = "";

	private String savename = "";

	private String titleName = "";

	private String ext = "";

	@Override
	public void exec() throws LogicException {
		long fileno = Long.parseLong(this.getTitaVo().getParam("fileno"));
		makeReport.setTitaVo(this.getTitaVo());
		makeExcel.setTitaVo(this.getTitaVo());

		TxFile tTxFile = sTxFileService.findById(fileno);

		if (tTxFile == null) {

			throw new LogicException(titaVo, "EC001", "輸出檔(TxFile)序號:" + fileno);
		} else {

			int filetype = tTxFile.getFileType();

			this.setFilename("txfile-" + fileno + "-" + titaVo.getKinbr() + titaVo.getTlrNo() + "-" + dateUtil.getNowStringBc() + "-" + dateUtil.getNowStringTime());
			this.setTitleName(tTxFile.getFileItem());

			if (filetype == 1) {
				this.setShowmode(1);
				makeReport.toPdf(fileno, this.getFilename());
				this.setSavename(tTxFile.getFileOutput() + ".pdf");
				this.setExt(".pdf");
			} else if (filetype == 2) {
				this.setShowmode(2);
				makeExcel.toExcel(fileno, this.getFilename());
				this.setSavename(tTxFile.getFileOutput());
			} else if (filetype >= 3 && filetype <= 5) {
				this.setShowmode(3);
				makeFile.toFile(fileno, this.getFilename());
				this.setSavename(tTxFile.getFileOutput());
			} else {
				throw new LogicException(titaVo, "EC001", "輸出檔(TxFile)序號:" + fileno + ",格式:" + filetype + "錯誤");
			}

			this.setFilename(this.OutFolder + this.getFilename());
		}
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the savename 預設存檔名稱
	 */
	public String getSavename() {
		return savename;
	}

	/**
	 * @param savename the savename to set
	 */
	public void setSavename(String savename) {
		this.savename = savename;
	}

	/**
	 * @return the showmode 1.顯示 2.存檔
	 */
	public int getShowmode() {
		return showmode;
	}

	/**
	 * @param showmode the showmode to set
	 */
	public void setShowmode(int showmode) {
		this.showmode = showmode;
	}

	public String getOutFolder() {
		return OutFolder;
	}

	public void setOutFolder(String outFolder) {
		OutFolder = outFolder;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getTitleName() {
		return this.titleName == null ? "" : this.titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

}