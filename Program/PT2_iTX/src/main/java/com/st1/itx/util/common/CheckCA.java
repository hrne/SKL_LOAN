package com.st1.itx.util.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Enumeration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Component("CheckCA")
@Scope("prototype")
public class CheckCA extends CommBuffer {

	@Autowired
	private DateUtil dDateUtil;
	@Autowired
	private WebClient webClient;
	@Autowired
	private SystemParasService sSystemParasService;

	private String pfxPath = "";

	private String pfxAuth = "";

	private KeyStore ks = null;

	private InputStream is = null;

	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void pfxExpiration(TitaVo titaVo) throws LogicException {
		this.info("CheckCA pfxExpiration start.");

		// 初始化
		init(titaVo);

		if (pfxPath.isEmpty()) {
			this.info("CheckCA pfxPath is Empty");
			return;
		}

		if (pfxAuth.isEmpty()) {
			this.info("CheckCA pfxAuth is Empty");
			return;
		}

		// 加載PFX
		loadPfx();

		// 遍歷keyStore中的每個別名
		checkAllAlias();

		this.info("CheckCA pfxExpiration finished.");
	}

	private void init(TitaVo titaVo) {
		// 清空跑馬燈
		webClient.sendTicker("0000", "CHECK_CA", "000000000000", "", true, titaVo);
		SystemParas systemParas = sSystemParasService.findById("LN", titaVo);
		// get pfxPath & pfxPw
		if (systemParas != null && systemParas.getPfxPath() != null && systemParas.getPfxAuth() != null) {
			String pfxFileName = "startwca.pfx";
			pfxPath = systemParas.getPfxPath() + pfxFileName; // get from SystemParas
			pfxAuth = systemParas.getPfxAuth(); // get from SystemParas
		}
	}

	private void loadPfx() {
		try {
			ks = KeyStore.getInstance("PKCS12");
		} catch (KeyStoreException e) {
			// if no Provider supports a KeyStoreSpi implementation for the specified type.
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("loadPfx KeyStore.getInstance FileNotFoundException = " + errors.toString());
		}
		try {
			is = new FileInputStream(pfxPath);
		} catch (FileNotFoundException e) {
			// if the file does not exist,is a directory rather than a regular file,or for
			// some other reason cannot be opened for reading.
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("loadPfx FileNotFoundException = " + errors.toString());
		}
		try {
			ks.load(is, pfxAuth.toCharArray());
		} catch (NoSuchAlgorithmException e) {
			// if the algorithm used to check the integrity of the KeyStore cannot be found
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("loadPfx ks.load NoSuchAlgorithmException = " + errors.toString());
		} catch (CertificateException e) {
			// if any of the certificates in the KeyStore could not be loaded
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("loadPfx ks.load CertificateException = " + errors.toString());
		} catch (IOException e) {
			// if there is an I/O or format problem with the KeyStore data, if a password is
			// required but not given,or if the given password was incorrect. If the error
			// is due to a wrong password, the cause of the IOException should be an
			// UnrecoverableKeyException
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("loadPfx ks.load IOException = " + errors.toString());
		}
		try {
			is.close();
		} catch (IOException e) {
			// if an I/O error occurs.
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("loadPfx is.close IOException = " + errors.toString());
		}
	}

	private void checkAllAlias() throws LogicException {
		Enumeration<String> enumeration = null;
		try {
			enumeration = ks.aliases();
		} catch (KeyStoreException e) {
			// if the KeyStore has not been initialized(loaded).
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("checkAllAlias ks.aliases KeyStoreException = " + errors.toString());
		}
		while (enumeration.hasMoreElements()) {
			String alias = enumeration.nextElement();

			// 獲取證書並檢查到期日期
			Certificate cert = null;
			try {
				cert = ks.getCertificate(alias);
			} catch (KeyStoreException e) {
				// if the KeyStore has not been initialized(loaded).
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("checkAllAlias ks.getCertificate KeyStoreException = " + errors.toString());
			}
			if (cert instanceof X509Certificate) {
				X509Certificate x509Cert = (X509Certificate) cert;
				Date expirationDate = x509Cert.getNotAfter();

				this.info("CheckCA pfxExpiration expirationDate = " + df.format(expirationDate));
				// 檢查證書是否將在 30 天內過期
				if (Instant.now().plus(30, ChronoUnit.DAYS).isAfter(expirationDate.toInstant())) {
					// 你可以在這裡添加發送郵件或其他形式的提醒
					String msg = "證書 " + alias + " 將在 30 天內過期";
					this.warn(msg);
					dDateUtil.init();
					dDateUtil.setDate_1(dDateUtil.getNowStringBc());
					dDateUtil.setDays(1); // 取明天的凌晨零點
					String stopTime = "" + dDateUtil.getCalenderDay() + "0000";

					// 2023-06-19 Wei 修改:將訊息修改為SKL User & IT 看得懂的
					webClient.sendTicker("0000", "CHECK_CA", stopTime, "SSL憑證將在30天內過期", false, titaVo);
				}
			}
		}
	}

	@Override
	public void exec() throws LogicException {
		// nothing
	}
}
