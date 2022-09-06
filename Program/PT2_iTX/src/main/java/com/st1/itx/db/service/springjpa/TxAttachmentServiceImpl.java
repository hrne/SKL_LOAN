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
import com.st1.itx.db.domain.TxAttachment;
import com.st1.itx.db.repository.online.TxAttachmentRepository;
import com.st1.itx.db.repository.day.TxAttachmentRepositoryDay;
import com.st1.itx.db.repository.mon.TxAttachmentRepositoryMon;
import com.st1.itx.db.repository.hist.TxAttachmentRepositoryHist;
import com.st1.itx.db.service.TxAttachmentService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txAttachmentService")
@Repository
public class TxAttachmentServiceImpl extends ASpringJpaParm implements TxAttachmentService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private TxAttachmentRepository txAttachmentRepos;

	@Autowired
	private TxAttachmentRepositoryDay txAttachmentReposDay;

	@Autowired
	private TxAttachmentRepositoryMon txAttachmentReposMon;

	@Autowired
	private TxAttachmentRepositoryHist txAttachmentReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(txAttachmentRepos);
		org.junit.Assert.assertNotNull(txAttachmentReposDay);
		org.junit.Assert.assertNotNull(txAttachmentReposMon);
		org.junit.Assert.assertNotNull(txAttachmentReposHist);
	}

	@Override
	public TxAttachment findById(Long fileNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + fileNo);
		Optional<TxAttachment> txAttachment = null;
		if (dbName.equals(ContentName.onDay))
			txAttachment = txAttachmentReposDay.findById(fileNo);
		else if (dbName.equals(ContentName.onMon))
			txAttachment = txAttachmentReposMon.findById(fileNo);
		else if (dbName.equals(ContentName.onHist))
			txAttachment = txAttachmentReposHist.findById(fileNo);
		else
			txAttachment = txAttachmentRepos.findById(fileNo);
		TxAttachment obj = txAttachment.isPresent() ? txAttachment.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<TxAttachment> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxAttachment> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "FileNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "FileNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = txAttachmentReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txAttachmentReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txAttachmentReposHist.findAll(pageable);
		else
			slice = txAttachmentRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxAttachment> findByTran(String tranNo_0, String mrKey_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxAttachment> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByTran " + dbName + " : " + "tranNo_0 : " + tranNo_0 + " mrKey_1 : " + mrKey_1);
		if (dbName.equals(ContentName.onDay))
			slice = txAttachmentReposDay.findAllByTranNoIsAndMrKeyIsOrderByCreateDateDesc(tranNo_0, mrKey_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txAttachmentReposMon.findAllByTranNoIsAndMrKeyIsOrderByCreateDateDesc(tranNo_0, mrKey_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txAttachmentReposHist.findAllByTranNoIsAndMrKeyIsOrderByCreateDateDesc(tranNo_0, mrKey_1, pageable);
		else
			slice = txAttachmentRepos.findAllByTranNoIsAndMrKeyIsOrderByCreateDateDesc(tranNo_0, mrKey_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxAttachment> findOnlyTran(String tranNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxAttachment> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findOnlyTran " + dbName + " : " + "tranNo_0 : " + tranNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = txAttachmentReposDay.findAllByTranNoIsOrderByFileNoDesc(tranNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txAttachmentReposMon.findAllByTranNoIsOrderByFileNoDesc(tranNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txAttachmentReposHist.findAllByTranNoIsOrderByFileNoDesc(tranNo_0, pageable);
		else
			slice = txAttachmentRepos.findAllByTranNoIsOrderByFileNoDesc(tranNo_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public TxAttachment holdById(Long fileNo, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + fileNo);
		Optional<TxAttachment> txAttachment = null;
		if (dbName.equals(ContentName.onDay))
			txAttachment = txAttachmentReposDay.findByFileNo(fileNo);
		else if (dbName.equals(ContentName.onMon))
			txAttachment = txAttachmentReposMon.findByFileNo(fileNo);
		else if (dbName.equals(ContentName.onHist))
			txAttachment = txAttachmentReposHist.findByFileNo(fileNo);
		else
			txAttachment = txAttachmentRepos.findByFileNo(fileNo);
		return txAttachment.isPresent() ? txAttachment.get() : null;
	}

	@Override
	public TxAttachment holdById(TxAttachment txAttachment, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txAttachment.getFileNo());
		Optional<TxAttachment> txAttachmentT = null;
		if (dbName.equals(ContentName.onDay))
			txAttachmentT = txAttachmentReposDay.findByFileNo(txAttachment.getFileNo());
		else if (dbName.equals(ContentName.onMon))
			txAttachmentT = txAttachmentReposMon.findByFileNo(txAttachment.getFileNo());
		else if (dbName.equals(ContentName.onHist))
			txAttachmentT = txAttachmentReposHist.findByFileNo(txAttachment.getFileNo());
		else
			txAttachmentT = txAttachmentRepos.findByFileNo(txAttachment.getFileNo());
		return txAttachmentT.isPresent() ? txAttachmentT.get() : null;
	}

	@Override
	public TxAttachment insert(TxAttachment txAttachment, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + txAttachment.getFileNo());
		if (this.findById(txAttachment.getFileNo(), titaVo) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			txAttachment.setCreateEmpNo(empNot);

		if (txAttachment.getLastUpdateEmpNo() == null || txAttachment.getLastUpdateEmpNo().isEmpty())
			txAttachment.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txAttachmentReposDay.saveAndFlush(txAttachment);
		else if (dbName.equals(ContentName.onMon))
			return txAttachmentReposMon.saveAndFlush(txAttachment);
		else if (dbName.equals(ContentName.onHist))
			return txAttachmentReposHist.saveAndFlush(txAttachment);
		else
			return txAttachmentRepos.saveAndFlush(txAttachment);
	}

	@Override
	public TxAttachment update(TxAttachment txAttachment, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + txAttachment.getFileNo());
		if (!empNot.isEmpty())
			txAttachment.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txAttachmentReposDay.saveAndFlush(txAttachment);
		else if (dbName.equals(ContentName.onMon))
			return txAttachmentReposMon.saveAndFlush(txAttachment);
		else if (dbName.equals(ContentName.onHist))
			return txAttachmentReposHist.saveAndFlush(txAttachment);
		else
			return txAttachmentRepos.saveAndFlush(txAttachment);
	}

	@Override
	public TxAttachment update2(TxAttachment txAttachment, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + txAttachment.getFileNo());
		if (!empNot.isEmpty())
			txAttachment.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			txAttachmentReposDay.saveAndFlush(txAttachment);
		else if (dbName.equals(ContentName.onMon))
			txAttachmentReposMon.saveAndFlush(txAttachment);
		else if (dbName.equals(ContentName.onHist))
			txAttachmentReposHist.saveAndFlush(txAttachment);
		else
			txAttachmentRepos.saveAndFlush(txAttachment);
		return this.findById(txAttachment.getFileNo());
	}

	@Override
	public void delete(TxAttachment txAttachment, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + txAttachment.getFileNo());
		if (dbName.equals(ContentName.onDay)) {
			txAttachmentReposDay.delete(txAttachment);
			txAttachmentReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txAttachmentReposMon.delete(txAttachment);
			txAttachmentReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txAttachmentReposHist.delete(txAttachment);
			txAttachmentReposHist.flush();
		} else {
			txAttachmentRepos.delete(txAttachment);
			txAttachmentRepos.flush();
		}
	}

	@Override
	public void insertAll(List<TxAttachment> txAttachment, TitaVo... titaVo) throws DBException {
		if (txAttachment == null || txAttachment.size() == 0)
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
		for (TxAttachment t : txAttachment) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			txAttachment = txAttachmentReposDay.saveAll(txAttachment);
			txAttachmentReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txAttachment = txAttachmentReposMon.saveAll(txAttachment);
			txAttachmentReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txAttachment = txAttachmentReposHist.saveAll(txAttachment);
			txAttachmentReposHist.flush();
		} else {
			txAttachment = txAttachmentRepos.saveAll(txAttachment);
			txAttachmentRepos.flush();
		}
	}

	@Override
	public void updateAll(List<TxAttachment> txAttachment, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (txAttachment == null || txAttachment.size() == 0)
			throw new DBException(6);

		for (TxAttachment t : txAttachment)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txAttachment = txAttachmentReposDay.saveAll(txAttachment);
			txAttachmentReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txAttachment = txAttachmentReposMon.saveAll(txAttachment);
			txAttachmentReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txAttachment = txAttachmentReposHist.saveAll(txAttachment);
			txAttachmentReposHist.flush();
		} else {
			txAttachment = txAttachmentRepos.saveAll(txAttachment);
			txAttachmentRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<TxAttachment> txAttachment, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (txAttachment == null || txAttachment.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			txAttachmentReposDay.deleteAll(txAttachment);
			txAttachmentReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txAttachmentReposMon.deleteAll(txAttachment);
			txAttachmentReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txAttachmentReposHist.deleteAll(txAttachment);
			txAttachmentReposHist.flush();
		} else {
			txAttachmentRepos.deleteAll(txAttachment);
			txAttachmentRepos.flush();
		}
	}

}
