package com.ligq.shoe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.ligq.shoe.entity.FeedbackScore;

@RepositoryRestResource(exported = false)
public interface FeedbackScoreRepository extends
	PagingAndSortingRepository<FeedbackScore, String>{
	
	public Page<FeedbackScore> findByCompanyId(String companyId,Pageable pageable);

	public Page<FeedbackScore> findByApproveStatus(Integer status, Pageable pageable);

	@RestResource(exported = false)
	@Query(value = "select t from FeedbackScore t where  (t.submitPerson like %:keyword% or t.submitTel like %:keyword%) and t.approveStatus = :approveStatus")
	public Page<FeedbackScore> findFeedbackBySearchKeywordAndAudit(
			@Param("keyword") String keyword,@Param("approveStatus") Integer approveStatus,Pageable pageable);

	public Page<FeedbackScore> findByCompanyIdAndApproveStatus(String uuid,
			Integer auditStatus, Pageable pageable);
}
