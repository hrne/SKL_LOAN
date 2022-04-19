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
import com.st1.itx.db.domain.AcAcctCheckDetail;
import com.st1.itx.db.domain.AcAcctCheckDetailId;
import com.st1.itx.db.repository.online.AcAcctCheckDetailRepository;
import com.st1.itx.db.repository.day.AcAcctCheckDetailRepositoryDay;
import com.st1.itx.db.repository.mon.AcAcctCheckDetailRepositoryMon;
import com.st1.itx.db.repository.hist.AcAcctCheckDetailRepositoryHist;
import com.st1.itx.db.service.AcAcctCheckDetailService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("acAcctCheckDetailService")
@Repository
public class AcAcctCheckDetailServiceImpl extends ASpringJpaParm implements AcAcctCheckDetailService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private AcAcctCheckDetailRepository acAcctCheckDetailRepos;

	@Autowired
	private AcAcctCheckDetailRepositoryDay acAcctCheckDetailReposDay;

	@Autowired
	private AcAcctCheckDetailRepositoryMon acAcctCheckDetailReposMon;

	@Autowired
	private AcAcctCheckDetailRepositoryHist acAcctCheckDetailReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(acAcctCheckDetailRepos);
		org.junit.Assert.assertNotNull(acAcctCheckDetailReposDay);
		org.junit.Assert.assertNotNull(acAcctCheckDetailReposMon);
		org.junit.Assert.assertNotNull(acAcctCheckDetailReposHist);
	}

	@Override
	public AcAcctCheckDetail findById(AcAcctCheckDetailId acAcctCheckDetailId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + acAcctCheckDetailId);
		Optional<AcAcctCheckDetail> acAcctCheckDetail = null;
		if (dbName.equals(ContentName.onDay))
			acAcctCheckDetail = acAcctCheckDetailReposDay.findById(acAcctCheckDetailId);
		else if (dbName.equals(ContentName.onMon))
			acAcctCheckDetail = acAcctCheckDetailReposMon.findById(acAcctCheckDetailId);
		else if (dbName.equals(ContentName.onHist))
			acAcctCheckDetail = acAcctCheckDetailReposHist.findById(acAcctCheckDetailId);
		else
			acAcctCheckDetail = acAcctCheckDetailRepos.findById(acAcctCheckDetailId);
		AcAcctCheckDetail obj = acAcctCheckDetail.isPresent() ? acAcctCheckDetail.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<AcAcctCheckDetail> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<AcAcctCheckDetail> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AcDate", "BranchNo", "CurrencyCode", "AcctCode", "CustNo", "FacmNo", "BormNo", "AcSubBookCode"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AcDate", "BranchNo", "CurrencyCode", "AcctCode", "CustNo", "FacmNo", "BormNo", "AcSubBookCode"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = acAcctCheckDetailReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = acAcctCheckDetailReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = acAcctCheckDetailReposHist.findAll(pageable);
		else
			slice = acAcctCheckDetailRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<AcAcctCheckDetail> findAcDate(int acDate_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<AcAcctCheckDetail> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findAcDate " + dbName + " : " + "acDate_0 : " + acDate_0);
		if (dbName.equals(ContentName.onDay))
			slice = acAcctCheckDetailReposDay.findAllByAcDateIsOrderByAcDateAscBranchNoAscCurrencyCodeAscAcctCodeAscCustNoAscFacmNoAscBormNoAsc(acDate_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = acAcctCheckDetailReposMon.findAllByAcDateIsOrderByAcDateAscBranchNoAscCurrencyCodeAscAcctCodeAscCustNoAscFacmNoAscBormNoAsc(acDate_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = acAcctCheckDetailReposHist.findAllByAcDateIsOrderByAcDateAscBranchNoAscCurrencyCodeAscAcctCodeAscCustNoAscFacmNoAscBormNoAsc(acDate_0, pageable);
		else
			slice = acAcctCheckDetailRepos.findAllByAcDateIsOrderByAcDateAscBranchNoAscCurrencyCodeAscAcctCodeAscCustNoAscFacmNoAscBormNoAsc(acDate_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public AcAcctCheckDetail holdById(AcAcctCheckDetailId acAcctCheckDetailId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + acAcctCheckDetailId);
		Optional<AcAcctCheckDetail> acAcctCheckDetail = null;
		if (dbName.equals(ContentName.onDay))
			acAcctCheckDetail = acAcctCheckDetailReposDay.findByAcAcctCheckDetailId(acAcctCheckDetailId);
		else if (dbName.equals(ContentName.onMon))
			acAcctCheckDetail = acAcctCheckDetailReposMon.findByAcAcctCheckDetailId(acAcctCheckDetailId);
		else if (dbName.equals(ContentName.onHist))
			acAcctCheckDetail = acAcctCheckDetailReposHist.findByAcAcctCheckDetailId(acAcctCheckDetailId);
		else
			acAcctCheckDetail = acAcctCheckDetailRepos.findByAcAcctCheckDetailId(acAcctCheckDetailId);
		return acAcctCheckDetail.isPresent() ? acAcctCheckDetail.get() : null;
	}

	@Override
	public AcAcctCheckDetail holdById(AcAcctCheckDetail acAcctCheckDetail, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + acAcctCheckDetail.getAcAcctCheckDetailId());
		Optional<AcAcctCheckDetail> acAcctCheckDetailT = null;
		if (dbName.equals(ContentName.onDay))
			acAcctCheckDetailT = acAcctCheckDetailReposDay.findByAcAcctCheckDetailId(acAcctCheckDetail.getAcAcctCheckDetailId());
		else if (dbName.equals(ContentName.onMon))
			acAcctCheckDetailT = acAcctCheckDetailReposMon.findByAcAcctCheckDetailId(acAcctCheckDetail.getAcAcctCheckDetailId());
		else if (dbName.equals(ContentName.onHist))
			acAcctCheckDetailT = acAcctCheckDetailReposHist.findByAcAcctCheckDetailId(acAcctCheckDetail.getAcAcctCheckDetailId());
		else
			acAcctCheckDetailT = acAcctCheckDetailRepos.findByAcAcctCheckDetailId(acAcctCheckDetail.getAcAcctCheckDetailId());
		return acAcctCheckDetailT.isPresent() ? acAcctCheckDetailT.get() : null;
	}

	@Override
	public AcAcctCheckDetail insert(AcAcctCheckDetail acAcctCheckDetail, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + acAcctCheckDetail.getAcAcctCheckDetailId());
		if (this.findById(acAcctCheckDetail.getAcAcctCheckDetailId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			acAcctCheckDetail.setCreateEmpNo(empNot);

		if (acAcctCheckDetail.getLastUpdateEmpNo() == null || acAcctCheckDetail.getLastUpdateEmpNo().isEmpty())
			acAcctCheckDetail.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return acAcctCheckDetailReposDay.saveAndFlush(acAcctCheckDetail);
		else if (dbName.equals(ContentName.onMon))
			return acAcctCheckDetailReposMon.saveAndFlush(acAcctCheckDetail);
		else if (dbName.equals(ContentName.onHist))
			return acAcctCheckDetailReposHist.saveAndFlush(acAcctCheckDetail);
		else
			return acAcctCheckDetailRepos.saveAndFlush(acAcctCheckDetail);
	}

	@Override
	public AcAcctCheckDetail update(AcAcctCheckDetail acAcctCheckDetail, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + acAcctCheckDetail.getAcAcctCheckDetailId());
		if (!empNot.isEmpty())
			acAcctCheckDetail.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return acAcctCheckDetailReposDay.saveAndFlush(acAcctCheckDetail);
		else if (dbName.equals(ContentName.onMon))
			return acAcctCheckDetailReposMon.saveAndFlush(acAcctCheckDetail);
		else if (dbName.equals(ContentName.onHist))
			return acAcctCheckDetailReposHist.saveAndFlush(acAcctCheckDetail);
		else
			return acAcctCheckDetailRepos.saveAndFlush(acAcctCheckDetail);
	}

	@Override
	public AcAcctCheckDetail update2(AcAcctCheckDetail acAcctCheckDetail, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + acAcctCheckDetail.getAcAcctCheckDetailId());
		if (!empNot.isEmpty())
			acAcctCheckDetail.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			acAcctCheckDetailReposDay.saveAndFlush(acAcctCheckDetail);
		else if (dbName.equals(ContentName.onMon))
			acAcctCheckDetailReposMon.saveAndFlush(acAcctCheckDetail);
		else if (dbName.equals(ContentName.onHist))
			acAcctCheckDetailReposHist.saveAndFlush(acAcctCheckDetail);
		else
			acAcctCheckDetailRepos.saveAndFlush(acAcctCheckDetail);
		return this.findById(acAcctCheckDetail.getAcAcctCheckDetailId());
	}

	@Override
	public void delete(AcAcctCheckDetail acAcctCheckDetail, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + acAcctCheckDetail.getAcAcctCheckDetailId());
		if (dbName.equals(ContentName.onDay)) {
			acAcctCheckDetailReposDay.delete(acAcctCheckDetail);
			acAcctCheckDetailReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			acAcctCheckDetailReposMon.delete(acAcctCheckDetail);
			acAcctCheckDetailReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			acAcctCheckDetailReposHist.delete(acAcctCheckDetail);
			acAcctCheckDetailReposHist.flush();
		} else {
			acAcctCheckDetailRepos.delete(acAcctCheckDetail);
			acAcctCheckDetailRepos.flush();
		}
	}

	@Override
	public void insertAll(List<AcAcctCheckDetail> acAcctCheckDetail, TitaVo... titaVo) throws DBException {
		if (acAcctCheckDetail == null || acAcctCheckDetail.size() == 0)
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
		for (AcAcctCheckDetail t : acAcctCheckDetail) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			acAcctCheckDetail = acAcctCheckDetailReposDay.saveAll(acAcctCheckDetail);
			acAcctCheckDetailReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			acAcctCheckDetail = acAcctCheckDetailReposMon.saveAll(acAcctCheckDetail);
			acAcctCheckDetailReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			acAcctCheckDetail = acAcctCheckDetailReposHist.saveAll(acAcctCheckDetail);
			acAcctCheckDetailReposHist.flush();
		} else {
			acAcctCheckDetail = acAcctCheckDetailRepos.saveAll(acAcctCheckDetail);
			acAcctCheckDetailRepos.flush();
		}
	}

	@Override
	public void updateAll(List<AcAcctCheckDetail> acAcctCheckDetail, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (acAcctCheckDetail == null || acAcctCheckDetail.size() == 0)
			throw new DBException(6);

		for (AcAcctCheckDetail t : acAcctCheckDetail)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			acAcctCheckDetail = acAcctCheckDetailReposDay.saveAll(acAcctCheckDetail);
			acAcctCheckDetailReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			acAcctCheckDetail = acAcctCheckDetailReposMon.saveAll(acAcctCheckDetail);
			acAcctCheckDetailReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			acAcctCheckDetail = acAcctCheckDetailReposHist.saveAll(acAcctCheckDetail);
			acAcctCheckDetailReposHist.flush();
		} else {
			acAcctCheckDetail = acAcctCheckDetailRepos.saveAll(acAcctCheckDetail);
			acAcctCheckDetailRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<AcAcctCheckDetail> acAcctCheckDetail, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (acAcctCheckDetail == null || acAcctCheckDetail.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			acAcctCheckDetailReposDay.deleteAll(acAcctCheckDetail);
			acAcctCheckDetailReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			acAcctCheckDetailReposMon.deleteAll(acAcctCheckDetail);
			acAcctCheckDetailReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			acAcctCheckDetailReposHist.deleteAll(acAcctCheckDetail);
			acAcctCheckDetailReposHist.flush();
		} else {
			acAcctCheckDetailRepos.deleteAll(acAcctCheckDetail);
			acAcctCheckDetailRepos.flush();
		}
	}

	@Override
	public void Usp_L6_AcAcctCheckDetail_Ins(int tbsdyf, String empNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			acAcctCheckDetailReposDay.uspL6AcacctcheckdetailIns(tbsdyf, empNo);
		else if (dbName.equals(ContentName.onMon))
			acAcctCheckDetailReposMon.uspL6AcacctcheckdetailIns(tbsdyf, empNo);
		else if (dbName.equals(ContentName.onHist))
			acAcctCheckDetailReposHist.uspL6AcacctcheckdetailIns(tbsdyf, empNo);
		else
			acAcctCheckDetailRepos.uspL6AcacctcheckdetailIns(tbsdyf, empNo);
	}

}
