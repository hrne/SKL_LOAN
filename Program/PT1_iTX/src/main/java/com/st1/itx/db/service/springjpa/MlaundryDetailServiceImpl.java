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
import com.st1.itx.db.domain.MlaundryDetail;
import com.st1.itx.db.domain.MlaundryDetailId;
import com.st1.itx.db.repository.online.MlaundryDetailRepository;
import com.st1.itx.db.repository.day.MlaundryDetailRepositoryDay;
import com.st1.itx.db.repository.mon.MlaundryDetailRepositoryMon;
import com.st1.itx.db.repository.hist.MlaundryDetailRepositoryHist;
import com.st1.itx.db.service.MlaundryDetailService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("mlaundryDetailService")
@Repository
public class MlaundryDetailServiceImpl extends ASpringJpaParm implements MlaundryDetailService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private MlaundryDetailRepository mlaundryDetailRepos;

	@Autowired
	private MlaundryDetailRepositoryDay mlaundryDetailReposDay;

	@Autowired
	private MlaundryDetailRepositoryMon mlaundryDetailReposMon;

	@Autowired
	private MlaundryDetailRepositoryHist mlaundryDetailReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(mlaundryDetailRepos);
		org.junit.Assert.assertNotNull(mlaundryDetailReposDay);
		org.junit.Assert.assertNotNull(mlaundryDetailReposMon);
		org.junit.Assert.assertNotNull(mlaundryDetailReposHist);
	}

	@Override
	public MlaundryDetail findById(MlaundryDetailId mlaundryDetailId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + mlaundryDetailId);
		Optional<MlaundryDetail> mlaundryDetail = null;
		if (dbName.equals(ContentName.onDay))
			mlaundryDetail = mlaundryDetailReposDay.findById(mlaundryDetailId);
		else if (dbName.equals(ContentName.onMon))
			mlaundryDetail = mlaundryDetailReposMon.findById(mlaundryDetailId);
		else if (dbName.equals(ContentName.onHist))
			mlaundryDetail = mlaundryDetailReposHist.findById(mlaundryDetailId);
		else
			mlaundryDetail = mlaundryDetailRepos.findById(mlaundryDetailId);
		MlaundryDetail obj = mlaundryDetail.isPresent() ? mlaundryDetail.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<MlaundryDetail> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MlaundryDetail> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "EntryDate", "Factor", "CustNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "EntryDate", "Factor", "CustNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = mlaundryDetailReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = mlaundryDetailReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = mlaundryDetailReposHist.findAll(pageable);
		else
			slice = mlaundryDetailRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<MlaundryDetail> findEntryDateRange(int entryDate_0, int entryDate_1, List<String> rational_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MlaundryDetail> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findEntryDateRange " + dbName + " : " + "entryDate_0 : " + entryDate_0 + " entryDate_1 : " + entryDate_1 + " rational_2 : " + rational_2);
		if (dbName.equals(ContentName.onDay))
			slice = mlaundryDetailReposDay.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndRationalInOrderByEntryDateAscCustNoAsc(entryDate_0, entryDate_1, rational_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = mlaundryDetailReposMon.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndRationalInOrderByEntryDateAscCustNoAsc(entryDate_0, entryDate_1, rational_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = mlaundryDetailReposHist.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndRationalInOrderByEntryDateAscCustNoAsc(entryDate_0, entryDate_1, rational_2, pageable);
		else
			slice = mlaundryDetailRepos.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndRationalInOrderByEntryDateAscCustNoAsc(entryDate_0, entryDate_1, rational_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<MlaundryDetail> findbyDate(int entryDate_0, int entryDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MlaundryDetail> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findbyDate " + dbName + " : " + "entryDate_0 : " + entryDate_0 + " entryDate_1 : " + entryDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = mlaundryDetailReposDay.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByEntryDateAscCustNoAsc(entryDate_0, entryDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = mlaundryDetailReposMon.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByEntryDateAscCustNoAsc(entryDate_0, entryDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = mlaundryDetailReposHist.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByEntryDateAscCustNoAsc(entryDate_0, entryDate_1, pageable);
		else
			slice = mlaundryDetailRepos.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByEntryDateAscCustNoAsc(entryDate_0, entryDate_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public MlaundryDetail findEntryDateRangeFactorCustNoFirst(int entryDate_0, int entryDate_1, int factor_2, int custNo_3, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findEntryDateRangeFactorCustNoFirst " + dbName + " : " + "entryDate_0 : " + entryDate_0 + " entryDate_1 : " + entryDate_1 + " factor_2 : " + factor_2 + " custNo_3 : " + custNo_3);
		Optional<MlaundryDetail> mlaundryDetailT = null;
		if (dbName.equals(ContentName.onDay))
			mlaundryDetailT = mlaundryDetailReposDay.findTopByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndFactorIsAndCustNoIsOrderByEntryDateAsc(entryDate_0, entryDate_1, factor_2, custNo_3);
		else if (dbName.equals(ContentName.onMon))
			mlaundryDetailT = mlaundryDetailReposMon.findTopByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndFactorIsAndCustNoIsOrderByEntryDateAsc(entryDate_0, entryDate_1, factor_2, custNo_3);
		else if (dbName.equals(ContentName.onHist))
			mlaundryDetailT = mlaundryDetailReposHist.findTopByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndFactorIsAndCustNoIsOrderByEntryDateAsc(entryDate_0, entryDate_1, factor_2,
					custNo_3);
		else
			mlaundryDetailT = mlaundryDetailRepos.findTopByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndFactorIsAndCustNoIsOrderByEntryDateAsc(entryDate_0, entryDate_1, factor_2, custNo_3);

		return mlaundryDetailT.isPresent() ? mlaundryDetailT.get() : null;
	}

	@Override
	public MlaundryDetail holdById(MlaundryDetailId mlaundryDetailId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + mlaundryDetailId);
		Optional<MlaundryDetail> mlaundryDetail = null;
		if (dbName.equals(ContentName.onDay))
			mlaundryDetail = mlaundryDetailReposDay.findByMlaundryDetailId(mlaundryDetailId);
		else if (dbName.equals(ContentName.onMon))
			mlaundryDetail = mlaundryDetailReposMon.findByMlaundryDetailId(mlaundryDetailId);
		else if (dbName.equals(ContentName.onHist))
			mlaundryDetail = mlaundryDetailReposHist.findByMlaundryDetailId(mlaundryDetailId);
		else
			mlaundryDetail = mlaundryDetailRepos.findByMlaundryDetailId(mlaundryDetailId);
		return mlaundryDetail.isPresent() ? mlaundryDetail.get() : null;
	}

	@Override
	public MlaundryDetail holdById(MlaundryDetail mlaundryDetail, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + mlaundryDetail.getMlaundryDetailId());
		Optional<MlaundryDetail> mlaundryDetailT = null;
		if (dbName.equals(ContentName.onDay))
			mlaundryDetailT = mlaundryDetailReposDay.findByMlaundryDetailId(mlaundryDetail.getMlaundryDetailId());
		else if (dbName.equals(ContentName.onMon))
			mlaundryDetailT = mlaundryDetailReposMon.findByMlaundryDetailId(mlaundryDetail.getMlaundryDetailId());
		else if (dbName.equals(ContentName.onHist))
			mlaundryDetailT = mlaundryDetailReposHist.findByMlaundryDetailId(mlaundryDetail.getMlaundryDetailId());
		else
			mlaundryDetailT = mlaundryDetailRepos.findByMlaundryDetailId(mlaundryDetail.getMlaundryDetailId());
		return mlaundryDetailT.isPresent() ? mlaundryDetailT.get() : null;
	}

	@Override
	public MlaundryDetail insert(MlaundryDetail mlaundryDetail, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + mlaundryDetail.getMlaundryDetailId());
		if (this.findById(mlaundryDetail.getMlaundryDetailId(), titaVo) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			mlaundryDetail.setCreateEmpNo(empNot);

		if (mlaundryDetail.getLastUpdateEmpNo() == null || mlaundryDetail.getLastUpdateEmpNo().isEmpty())
			mlaundryDetail.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return mlaundryDetailReposDay.saveAndFlush(mlaundryDetail);
		else if (dbName.equals(ContentName.onMon))
			return mlaundryDetailReposMon.saveAndFlush(mlaundryDetail);
		else if (dbName.equals(ContentName.onHist))
			return mlaundryDetailReposHist.saveAndFlush(mlaundryDetail);
		else
			return mlaundryDetailRepos.saveAndFlush(mlaundryDetail);
	}

	@Override
	public MlaundryDetail update(MlaundryDetail mlaundryDetail, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + mlaundryDetail.getMlaundryDetailId());
		if (!empNot.isEmpty())
			mlaundryDetail.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return mlaundryDetailReposDay.saveAndFlush(mlaundryDetail);
		else if (dbName.equals(ContentName.onMon))
			return mlaundryDetailReposMon.saveAndFlush(mlaundryDetail);
		else if (dbName.equals(ContentName.onHist))
			return mlaundryDetailReposHist.saveAndFlush(mlaundryDetail);
		else
			return mlaundryDetailRepos.saveAndFlush(mlaundryDetail);
	}

	@Override
	public MlaundryDetail update2(MlaundryDetail mlaundryDetail, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + mlaundryDetail.getMlaundryDetailId());
		if (!empNot.isEmpty())
			mlaundryDetail.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			mlaundryDetailReposDay.saveAndFlush(mlaundryDetail);
		else if (dbName.equals(ContentName.onMon))
			mlaundryDetailReposMon.saveAndFlush(mlaundryDetail);
		else if (dbName.equals(ContentName.onHist))
			mlaundryDetailReposHist.saveAndFlush(mlaundryDetail);
		else
			mlaundryDetailRepos.saveAndFlush(mlaundryDetail);
		return this.findById(mlaundryDetail.getMlaundryDetailId());
	}

	@Override
	public void delete(MlaundryDetail mlaundryDetail, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + mlaundryDetail.getMlaundryDetailId());
		if (dbName.equals(ContentName.onDay)) {
			mlaundryDetailReposDay.delete(mlaundryDetail);
			mlaundryDetailReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			mlaundryDetailReposMon.delete(mlaundryDetail);
			mlaundryDetailReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			mlaundryDetailReposHist.delete(mlaundryDetail);
			mlaundryDetailReposHist.flush();
		} else {
			mlaundryDetailRepos.delete(mlaundryDetail);
			mlaundryDetailRepos.flush();
		}
	}

	@Override
	public void insertAll(List<MlaundryDetail> mlaundryDetail, TitaVo... titaVo) throws DBException {
		if (mlaundryDetail == null || mlaundryDetail.size() == 0)
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
		for (MlaundryDetail t : mlaundryDetail) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			mlaundryDetail = mlaundryDetailReposDay.saveAll(mlaundryDetail);
			mlaundryDetailReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			mlaundryDetail = mlaundryDetailReposMon.saveAll(mlaundryDetail);
			mlaundryDetailReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			mlaundryDetail = mlaundryDetailReposHist.saveAll(mlaundryDetail);
			mlaundryDetailReposHist.flush();
		} else {
			mlaundryDetail = mlaundryDetailRepos.saveAll(mlaundryDetail);
			mlaundryDetailRepos.flush();
		}
	}

	@Override
	public void updateAll(List<MlaundryDetail> mlaundryDetail, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (mlaundryDetail == null || mlaundryDetail.size() == 0)
			throw new DBException(6);

		for (MlaundryDetail t : mlaundryDetail)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			mlaundryDetail = mlaundryDetailReposDay.saveAll(mlaundryDetail);
			mlaundryDetailReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			mlaundryDetail = mlaundryDetailReposMon.saveAll(mlaundryDetail);
			mlaundryDetailReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			mlaundryDetail = mlaundryDetailReposHist.saveAll(mlaundryDetail);
			mlaundryDetailReposHist.flush();
		} else {
			mlaundryDetail = mlaundryDetailRepos.saveAll(mlaundryDetail);
			mlaundryDetailRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<MlaundryDetail> mlaundryDetail, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (mlaundryDetail == null || mlaundryDetail.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			mlaundryDetailReposDay.deleteAll(mlaundryDetail);
			mlaundryDetailReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			mlaundryDetailReposMon.deleteAll(mlaundryDetail);
			mlaundryDetailReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			mlaundryDetailReposHist.deleteAll(mlaundryDetail);
			mlaundryDetailReposHist.flush();
		} else {
			mlaundryDetailRepos.deleteAll(mlaundryDetail);
			mlaundryDetailRepos.flush();
		}
	}

}
