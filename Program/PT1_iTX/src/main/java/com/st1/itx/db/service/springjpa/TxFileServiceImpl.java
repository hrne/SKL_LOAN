package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxFile;
import com.st1.itx.db.repository.online.TxFileRepository;
import com.st1.itx.db.repository.day.TxFileRepositoryDay;
import com.st1.itx.db.repository.mon.TxFileRepositoryMon;
import com.st1.itx.db.repository.hist.TxFileRepositoryHist;
import com.st1.itx.db.service.TxFileService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txFileService")
@Repository
public class TxFileServiceImpl extends ASpringJpaParm implements TxFileService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private TxFileRepository txFileRepos;

	@Autowired
	private TxFileRepositoryDay txFileReposDay;

	@Autowired
	private TxFileRepositoryMon txFileReposMon;

	@Autowired
	private TxFileRepositoryHist txFileReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(txFileRepos);
		org.junit.Assert.assertNotNull(txFileReposDay);
		org.junit.Assert.assertNotNull(txFileReposMon);
		org.junit.Assert.assertNotNull(txFileReposHist);
	}

	@Override
	public TxFile findById(Long fileNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + fileNo);
		Optional<TxFile> txFile = null;
		if (dbName.equals(ContentName.onDay))
			txFile = txFileReposDay.findById(fileNo);
		else if (dbName.equals(ContentName.onMon))
			txFile = txFileReposMon.findById(fileNo);
		else if (dbName.equals(ContentName.onHist))
			txFile = txFileReposHist.findById(fileNo);
		else
			txFile = txFileRepos.findById(fileNo);
		TxFile obj = txFile.isPresent() ? txFile.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<TxFile> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxFile> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "FileNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "FileNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = txFileReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txFileReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txFileReposHist.findAll(pageable);
		else
			slice = txFileRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxFile> findByDate(int fileDate_0, String brNo_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxFile> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByDate " + dbName + " : " + "fileDate_0 : " + fileDate_0 + " brNo_1 : " + brNo_1);
		if (dbName.equals(ContentName.onDay))
			slice = txFileReposDay.findAllByFileDateIsAndBrNoIsOrderByFileNoAsc(fileDate_0, brNo_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txFileReposMon.findAllByFileDateIsAndBrNoIsOrderByFileNoAsc(fileDate_0, brNo_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txFileReposHist.findAllByFileDateIsAndBrNoIsOrderByFileNoAsc(fileDate_0, brNo_1, pageable);
		else
			slice = txFileRepos.findAllByFileDateIsAndBrNoIsOrderByFileNoAsc(fileDate_0, brNo_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxFile> findByLC009(int fileDate_0, int fileDate_1, String brNo_2, String createEmpNo_3, String fileCode_4, String fileItem_5, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxFile> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByLC009 " + dbName + " : " + "fileDate_0 : " + fileDate_0 + " fileDate_1 : " + fileDate_1 + " brNo_2 : " + brNo_2 + " createEmpNo_3 : " + createEmpNo_3 + " fileCode_4 : "
				+ fileCode_4 + " fileItem_5 : " + fileItem_5);
		if (dbName.equals(ContentName.onDay))
			slice = txFileReposDay.findAllByFileDateGreaterThanEqualAndFileDateLessThanEqualAndBrNoIsAndCreateEmpNoLikeAndFileCodeLikeAndFileItemLikeOrderByCreateDateDesc(fileDate_0, fileDate_1,
					brNo_2, createEmpNo_3, fileCode_4, fileItem_5, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txFileReposMon.findAllByFileDateGreaterThanEqualAndFileDateLessThanEqualAndBrNoIsAndCreateEmpNoLikeAndFileCodeLikeAndFileItemLikeOrderByCreateDateDesc(fileDate_0, fileDate_1,
					brNo_2, createEmpNo_3, fileCode_4, fileItem_5, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txFileReposHist.findAllByFileDateGreaterThanEqualAndFileDateLessThanEqualAndBrNoIsAndCreateEmpNoLikeAndFileCodeLikeAndFileItemLikeOrderByCreateDateDesc(fileDate_0, fileDate_1,
					brNo_2, createEmpNo_3, fileCode_4, fileItem_5, pageable);
		else
			slice = txFileRepos.findAllByFileDateGreaterThanEqualAndFileDateLessThanEqualAndBrNoIsAndCreateEmpNoLikeAndFileCodeLikeAndFileItemLikeOrderByCreateDateDesc(fileDate_0, fileDate_1, brNo_2,
					createEmpNo_3, fileCode_4, fileItem_5, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxFile> findByLC009WithCreateDate(int fileDate_0, int fileDate_1, String brNo_2, String createEmpNo_3, String fileCode_4, String fileItem_5, java.sql.Timestamp createDate_6,
			java.sql.Timestamp createDate_7, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxFile> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByLC009WithCreateDate " + dbName + " : " + "fileDate_0 : " + fileDate_0 + " fileDate_1 : " + fileDate_1 + " brNo_2 : " + brNo_2 + " createEmpNo_3 : " + createEmpNo_3
				+ " fileCode_4 : " + fileCode_4 + " fileItem_5 : " + fileItem_5 + " createDate_6 : " + createDate_6 + " createDate_7 : " + createDate_7);
		if (dbName.equals(ContentName.onDay))
			slice = txFileReposDay
					.findAllByFileDateGreaterThanEqualAndFileDateLessThanEqualAndBrNoIsAndCreateEmpNoLikeAndFileCodeLikeAndFileItemLikeAndCreateDateGreaterThanEqualAndCreateDateLessThanOrderByCreateDateDesc(
							fileDate_0, fileDate_1, brNo_2, createEmpNo_3, fileCode_4, fileItem_5, createDate_6, createDate_7, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txFileReposMon
					.findAllByFileDateGreaterThanEqualAndFileDateLessThanEqualAndBrNoIsAndCreateEmpNoLikeAndFileCodeLikeAndFileItemLikeAndCreateDateGreaterThanEqualAndCreateDateLessThanOrderByCreateDateDesc(
							fileDate_0, fileDate_1, brNo_2, createEmpNo_3, fileCode_4, fileItem_5, createDate_6, createDate_7, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txFileReposHist
					.findAllByFileDateGreaterThanEqualAndFileDateLessThanEqualAndBrNoIsAndCreateEmpNoLikeAndFileCodeLikeAndFileItemLikeAndCreateDateGreaterThanEqualAndCreateDateLessThanOrderByCreateDateDesc(
							fileDate_0, fileDate_1, brNo_2, createEmpNo_3, fileCode_4, fileItem_5, createDate_6, createDate_7, pageable);
		else
			slice = txFileRepos
					.findAllByFileDateGreaterThanEqualAndFileDateLessThanEqualAndBrNoIsAndCreateEmpNoLikeAndFileCodeLikeAndFileItemLikeAndCreateDateGreaterThanEqualAndCreateDateLessThanOrderByCreateDateDesc(
							fileDate_0, fileDate_1, brNo_2, createEmpNo_3, fileCode_4, fileItem_5, createDate_6, createDate_7, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public TxFile findByBatchNoFirst(String batchNo_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findByBatchNoFirst " + dbName + " : " + "batchNo_0 : " + batchNo_0);
		Optional<TxFile> txFileT = null;
		if (dbName.equals(ContentName.onDay))
			txFileT = txFileReposDay.findTopByBatchNoIsOrderByCreateDateDesc(batchNo_0);
		else if (dbName.equals(ContentName.onMon))
			txFileT = txFileReposMon.findTopByBatchNoIsOrderByCreateDateDesc(batchNo_0);
		else if (dbName.equals(ContentName.onHist))
			txFileT = txFileReposHist.findTopByBatchNoIsOrderByCreateDateDesc(batchNo_0);
		else
			txFileT = txFileRepos.findTopByBatchNoIsOrderByCreateDateDesc(batchNo_0);

		return txFileT.isPresent() ? txFileT.get() : null;
	}

	@Override
	public TxFile findByFileCodeFirst(String fileCode_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findByFileCodeFirst " + dbName + " : " + "fileCode_0 : " + fileCode_0);
		Optional<TxFile> txFileT = null;
		if (dbName.equals(ContentName.onDay))
			txFileT = txFileReposDay.findTopByFileCodeIsOrderByCreateDateDesc(fileCode_0);
		else if (dbName.equals(ContentName.onMon))
			txFileT = txFileReposMon.findTopByFileCodeIsOrderByCreateDateDesc(fileCode_0);
		else if (dbName.equals(ContentName.onHist))
			txFileT = txFileReposHist.findTopByFileCodeIsOrderByCreateDateDesc(fileCode_0);
		else
			txFileT = txFileRepos.findTopByFileCodeIsOrderByCreateDateDesc(fileCode_0);

		return txFileT.isPresent() ? txFileT.get() : null;
	}

	@Override
	public TxFile holdById(Long fileNo, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + fileNo);
		Optional<TxFile> txFile = null;
		if (dbName.equals(ContentName.onDay))
			txFile = txFileReposDay.findByFileNo(fileNo);
		else if (dbName.equals(ContentName.onMon))
			txFile = txFileReposMon.findByFileNo(fileNo);
		else if (dbName.equals(ContentName.onHist))
			txFile = txFileReposHist.findByFileNo(fileNo);
		else
			txFile = txFileRepos.findByFileNo(fileNo);
		return txFile.isPresent() ? txFile.get() : null;
	}

	@Override
	public TxFile holdById(TxFile txFile, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txFile.getFileNo());
		Optional<TxFile> txFileT = null;
		if (dbName.equals(ContentName.onDay))
			txFileT = txFileReposDay.findByFileNo(txFile.getFileNo());
		else if (dbName.equals(ContentName.onMon))
			txFileT = txFileReposMon.findByFileNo(txFile.getFileNo());
		else if (dbName.equals(ContentName.onHist))
			txFileT = txFileReposHist.findByFileNo(txFile.getFileNo());
		else
			txFileT = txFileRepos.findByFileNo(txFile.getFileNo());
		return txFileT.isPresent() ? txFileT.get() : null;
	}

	@Override
	public TxFile insert(TxFile txFile, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + txFile.getFileNo());
		if (this.findById(txFile.getFileNo(), titaVo) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			txFile.setCreateEmpNo(empNot);

		if (txFile.getLastUpdateEmpNo() == null || txFile.getLastUpdateEmpNo().isEmpty())
			txFile.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txFileReposDay.saveAndFlush(txFile);
		else if (dbName.equals(ContentName.onMon))
			return txFileReposMon.saveAndFlush(txFile);
		else if (dbName.equals(ContentName.onHist))
			return txFileReposHist.saveAndFlush(txFile);
		else
			return txFileRepos.saveAndFlush(txFile);
	}

	@Override
	public TxFile update(TxFile txFile, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + txFile.getFileNo());
		if (!empNot.isEmpty())
			txFile.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txFileReposDay.saveAndFlush(txFile);
		else if (dbName.equals(ContentName.onMon))
			return txFileReposMon.saveAndFlush(txFile);
		else if (dbName.equals(ContentName.onHist))
			return txFileReposHist.saveAndFlush(txFile);
		else
			return txFileRepos.saveAndFlush(txFile);
	}

	@Override
	public TxFile update2(TxFile txFile, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + txFile.getFileNo());
		if (!empNot.isEmpty())
			txFile.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			txFileReposDay.saveAndFlush(txFile);
		else if (dbName.equals(ContentName.onMon))
			txFileReposMon.saveAndFlush(txFile);
		else if (dbName.equals(ContentName.onHist))
			txFileReposHist.saveAndFlush(txFile);
		else
			txFileRepos.saveAndFlush(txFile);
		return this.findById(txFile.getFileNo());
	}

	@Override
	public void delete(TxFile txFile, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + txFile.getFileNo());
		if (dbName.equals(ContentName.onDay)) {
			txFileReposDay.delete(txFile);
			txFileReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txFileReposMon.delete(txFile);
			txFileReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txFileReposHist.delete(txFile);
			txFileReposHist.flush();
		} else {
			txFileRepos.delete(txFile);
			txFileRepos.flush();
		}
	}

	@Override
	public void insertAll(List<TxFile> txFile, TitaVo... titaVo) throws DBException {
		if (txFile == null || txFile.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("InsertAll...");
		for (TxFile t : txFile) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			txFile = txFileReposDay.saveAll(txFile);
			txFileReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txFile = txFileReposMon.saveAll(txFile);
			txFileReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txFile = txFileReposHist.saveAll(txFile);
			txFileReposHist.flush();
		} else {
			txFile = txFileRepos.saveAll(txFile);
			txFileRepos.flush();
		}
	}

	@Override
	public void updateAll(List<TxFile> txFile, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (txFile == null || txFile.size() == 0)
			throw new DBException(6);

		for (TxFile t : txFile)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txFile = txFileReposDay.saveAll(txFile);
			txFileReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txFile = txFileReposMon.saveAll(txFile);
			txFileReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txFile = txFileReposHist.saveAll(txFile);
			txFileReposHist.flush();
		} else {
			txFile = txFileRepos.saveAll(txFile);
			txFileRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<TxFile> txFile, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (txFile == null || txFile.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			txFileReposDay.deleteAll(txFile);
			txFileReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txFileReposMon.deleteAll(txFile);
			txFileReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txFileReposHist.deleteAll(txFile);
			txFileReposHist.flush();
		} else {
			txFileRepos.deleteAll(txFile);
			txFileRepos.flush();
		}
	}

}
