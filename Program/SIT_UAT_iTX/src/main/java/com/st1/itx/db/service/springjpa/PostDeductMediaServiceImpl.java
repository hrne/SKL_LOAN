package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

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
import com.st1.itx.db.domain.PostDeductMedia;
import com.st1.itx.db.domain.PostDeductMediaId;
import com.st1.itx.db.repository.online.PostDeductMediaRepository;
import com.st1.itx.db.repository.day.PostDeductMediaRepositoryDay;
import com.st1.itx.db.repository.mon.PostDeductMediaRepositoryMon;
import com.st1.itx.db.repository.hist.PostDeductMediaRepositoryHist;
import com.st1.itx.db.service.PostDeductMediaService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("postDeductMediaService")
@Repository
public class PostDeductMediaServiceImpl extends ASpringJpaParm implements PostDeductMediaService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private PostDeductMediaRepository postDeductMediaRepos;

	@Autowired
	private PostDeductMediaRepositoryDay postDeductMediaReposDay;

	@Autowired
	private PostDeductMediaRepositoryMon postDeductMediaReposMon;

	@Autowired
	private PostDeductMediaRepositoryHist postDeductMediaReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(postDeductMediaRepos);
		org.junit.Assert.assertNotNull(postDeductMediaReposDay);
		org.junit.Assert.assertNotNull(postDeductMediaReposMon);
		org.junit.Assert.assertNotNull(postDeductMediaReposHist);
	}

	@Override
	public PostDeductMedia findById(PostDeductMediaId postDeductMediaId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + postDeductMediaId);
		Optional<PostDeductMedia> postDeductMedia = null;
		if (dbName.equals(ContentName.onDay))
			postDeductMedia = postDeductMediaReposDay.findById(postDeductMediaId);
		else if (dbName.equals(ContentName.onMon))
			postDeductMedia = postDeductMediaReposMon.findById(postDeductMediaId);
		else if (dbName.equals(ContentName.onHist))
			postDeductMedia = postDeductMediaReposHist.findById(postDeductMediaId);
		else
			postDeductMedia = postDeductMediaRepos.findById(postDeductMediaId);
		PostDeductMedia obj = postDeductMedia.isPresent() ? postDeductMedia.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<PostDeductMedia> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PostDeductMedia> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "MediaDate", "MediaSeq"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "MediaDate", "MediaSeq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = postDeductMediaReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = postDeductMediaReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = postDeductMediaReposHist.findAll(pageable);
		else
			slice = postDeductMediaRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public PostDeductMedia detailSeqFirst(int acDate_0, String batchNo_1, int detailSeq_2, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("detailSeqFirst " + dbName + " : " + "acDate_0 : " + acDate_0 + " batchNo_1 : " + batchNo_1 + " detailSeq_2 : " + detailSeq_2);
		Optional<PostDeductMedia> postDeductMediaT = null;
		if (dbName.equals(ContentName.onDay))
			postDeductMediaT = postDeductMediaReposDay.findTopByAcDateIsAndBatchNoIsAndDetailSeqIs(acDate_0, batchNo_1, detailSeq_2);
		else if (dbName.equals(ContentName.onMon))
			postDeductMediaT = postDeductMediaReposMon.findTopByAcDateIsAndBatchNoIsAndDetailSeqIs(acDate_0, batchNo_1, detailSeq_2);
		else if (dbName.equals(ContentName.onHist))
			postDeductMediaT = postDeductMediaReposHist.findTopByAcDateIsAndBatchNoIsAndDetailSeqIs(acDate_0, batchNo_1, detailSeq_2);
		else
			postDeductMediaT = postDeductMediaRepos.findTopByAcDateIsAndBatchNoIsAndDetailSeqIs(acDate_0, batchNo_1, detailSeq_2);

		return postDeductMediaT.isPresent() ? postDeductMediaT.get() : null;
	}

	@Override
	public PostDeductMedia receiveCheckFirst(String postUserNo_0, BigDecimal repayAmt_1, String outsrcRemark_2, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("receiveCheckFirst " + dbName + " : " + "postUserNo_0 : " + postUserNo_0 + " repayAmt_1 : " + repayAmt_1 + " outsrcRemark_2 : " + outsrcRemark_2);
		Optional<PostDeductMedia> postDeductMediaT = null;
		if (dbName.equals(ContentName.onDay))
			postDeductMediaT = postDeductMediaReposDay.findTopByPostUserNoIsAndRepayAmtIsAndOutsrcRemarkIsOrderByMediaDateDesc(postUserNo_0, repayAmt_1, outsrcRemark_2);
		else if (dbName.equals(ContentName.onMon))
			postDeductMediaT = postDeductMediaReposMon.findTopByPostUserNoIsAndRepayAmtIsAndOutsrcRemarkIsOrderByMediaDateDesc(postUserNo_0, repayAmt_1, outsrcRemark_2);
		else if (dbName.equals(ContentName.onHist))
			postDeductMediaT = postDeductMediaReposHist.findTopByPostUserNoIsAndRepayAmtIsAndOutsrcRemarkIsOrderByMediaDateDesc(postUserNo_0, repayAmt_1, outsrcRemark_2);
		else
			postDeductMediaT = postDeductMediaRepos.findTopByPostUserNoIsAndRepayAmtIsAndOutsrcRemarkIsOrderByMediaDateDesc(postUserNo_0, repayAmt_1, outsrcRemark_2);

		return postDeductMediaT.isPresent() ? postDeductMediaT.get() : null;
	}

	@Override
	public Slice<PostDeductMedia> mediaDateEq(int mediaDate_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PostDeductMedia> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("mediaDateEq " + dbName + " : " + "mediaDate_0 : " + mediaDate_0);
		if (dbName.equals(ContentName.onDay))
			slice = postDeductMediaReposDay.findAllByMediaDateIsOrderByMediaSeqAsc(mediaDate_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = postDeductMediaReposMon.findAllByMediaDateIsOrderByMediaSeqAsc(mediaDate_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = postDeductMediaReposHist.findAllByMediaDateIsOrderByMediaSeqAsc(mediaDate_0, pageable);
		else
			slice = postDeductMediaRepos.findAllByMediaDateIsOrderByMediaSeqAsc(mediaDate_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public PostDeductMedia holdById(PostDeductMediaId postDeductMediaId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + postDeductMediaId);
		Optional<PostDeductMedia> postDeductMedia = null;
		if (dbName.equals(ContentName.onDay))
			postDeductMedia = postDeductMediaReposDay.findByPostDeductMediaId(postDeductMediaId);
		else if (dbName.equals(ContentName.onMon))
			postDeductMedia = postDeductMediaReposMon.findByPostDeductMediaId(postDeductMediaId);
		else if (dbName.equals(ContentName.onHist))
			postDeductMedia = postDeductMediaReposHist.findByPostDeductMediaId(postDeductMediaId);
		else
			postDeductMedia = postDeductMediaRepos.findByPostDeductMediaId(postDeductMediaId);
		return postDeductMedia.isPresent() ? postDeductMedia.get() : null;
	}

	@Override
	public PostDeductMedia holdById(PostDeductMedia postDeductMedia, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + postDeductMedia.getPostDeductMediaId());
		Optional<PostDeductMedia> postDeductMediaT = null;
		if (dbName.equals(ContentName.onDay))
			postDeductMediaT = postDeductMediaReposDay.findByPostDeductMediaId(postDeductMedia.getPostDeductMediaId());
		else if (dbName.equals(ContentName.onMon))
			postDeductMediaT = postDeductMediaReposMon.findByPostDeductMediaId(postDeductMedia.getPostDeductMediaId());
		else if (dbName.equals(ContentName.onHist))
			postDeductMediaT = postDeductMediaReposHist.findByPostDeductMediaId(postDeductMedia.getPostDeductMediaId());
		else
			postDeductMediaT = postDeductMediaRepos.findByPostDeductMediaId(postDeductMedia.getPostDeductMediaId());
		return postDeductMediaT.isPresent() ? postDeductMediaT.get() : null;
	}

	@Override
	public PostDeductMedia insert(PostDeductMedia postDeductMedia, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + postDeductMedia.getPostDeductMediaId());
		if (this.findById(postDeductMedia.getPostDeductMediaId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			postDeductMedia.setCreateEmpNo(empNot);

		if (postDeductMedia.getLastUpdateEmpNo() == null || postDeductMedia.getLastUpdateEmpNo().isEmpty())
			postDeductMedia.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return postDeductMediaReposDay.saveAndFlush(postDeductMedia);
		else if (dbName.equals(ContentName.onMon))
			return postDeductMediaReposMon.saveAndFlush(postDeductMedia);
		else if (dbName.equals(ContentName.onHist))
			return postDeductMediaReposHist.saveAndFlush(postDeductMedia);
		else
			return postDeductMediaRepos.saveAndFlush(postDeductMedia);
	}

	@Override
	public PostDeductMedia update(PostDeductMedia postDeductMedia, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + postDeductMedia.getPostDeductMediaId());
		if (!empNot.isEmpty())
			postDeductMedia.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return postDeductMediaReposDay.saveAndFlush(postDeductMedia);
		else if (dbName.equals(ContentName.onMon))
			return postDeductMediaReposMon.saveAndFlush(postDeductMedia);
		else if (dbName.equals(ContentName.onHist))
			return postDeductMediaReposHist.saveAndFlush(postDeductMedia);
		else
			return postDeductMediaRepos.saveAndFlush(postDeductMedia);
	}

	@Override
	public PostDeductMedia update2(PostDeductMedia postDeductMedia, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + postDeductMedia.getPostDeductMediaId());
		if (!empNot.isEmpty())
			postDeductMedia.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			postDeductMediaReposDay.saveAndFlush(postDeductMedia);
		else if (dbName.equals(ContentName.onMon))
			postDeductMediaReposMon.saveAndFlush(postDeductMedia);
		else if (dbName.equals(ContentName.onHist))
			postDeductMediaReposHist.saveAndFlush(postDeductMedia);
		else
			postDeductMediaRepos.saveAndFlush(postDeductMedia);
		return this.findById(postDeductMedia.getPostDeductMediaId());
	}

	@Override
	public void delete(PostDeductMedia postDeductMedia, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + postDeductMedia.getPostDeductMediaId());
		if (dbName.equals(ContentName.onDay)) {
			postDeductMediaReposDay.delete(postDeductMedia);
			postDeductMediaReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			postDeductMediaReposMon.delete(postDeductMedia);
			postDeductMediaReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			postDeductMediaReposHist.delete(postDeductMedia);
			postDeductMediaReposHist.flush();
		} else {
			postDeductMediaRepos.delete(postDeductMedia);
			postDeductMediaRepos.flush();
		}
	}

	@Override
	public void insertAll(List<PostDeductMedia> postDeductMedia, TitaVo... titaVo) throws DBException {
		if (postDeductMedia == null || postDeductMedia.size() == 0)
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
		for (PostDeductMedia t : postDeductMedia) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			postDeductMedia = postDeductMediaReposDay.saveAll(postDeductMedia);
			postDeductMediaReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			postDeductMedia = postDeductMediaReposMon.saveAll(postDeductMedia);
			postDeductMediaReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			postDeductMedia = postDeductMediaReposHist.saveAll(postDeductMedia);
			postDeductMediaReposHist.flush();
		} else {
			postDeductMedia = postDeductMediaRepos.saveAll(postDeductMedia);
			postDeductMediaRepos.flush();
		}
	}

	@Override
	public void updateAll(List<PostDeductMedia> postDeductMedia, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (postDeductMedia == null || postDeductMedia.size() == 0)
			throw new DBException(6);

		for (PostDeductMedia t : postDeductMedia)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			postDeductMedia = postDeductMediaReposDay.saveAll(postDeductMedia);
			postDeductMediaReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			postDeductMedia = postDeductMediaReposMon.saveAll(postDeductMedia);
			postDeductMediaReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			postDeductMedia = postDeductMediaReposHist.saveAll(postDeductMedia);
			postDeductMediaReposHist.flush();
		} else {
			postDeductMedia = postDeductMediaRepos.saveAll(postDeductMedia);
			postDeductMediaRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<PostDeductMedia> postDeductMedia, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (postDeductMedia == null || postDeductMedia.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			postDeductMediaReposDay.deleteAll(postDeductMedia);
			postDeductMediaReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			postDeductMediaReposMon.deleteAll(postDeductMedia);
			postDeductMediaReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			postDeductMediaReposHist.deleteAll(postDeductMedia);
			postDeductMediaReposHist.flush();
		} else {
			postDeductMediaRepos.deleteAll(postDeductMedia);
			postDeductMediaRepos.flush();
		}
	}

}
