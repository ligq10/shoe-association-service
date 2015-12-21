package com.ligq.shoe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ligq.shoe.entity.FeedbackScore;

@RepositoryRestResource(exported = false)
public interface FeedbackScoreRepository extends
	PagingAndSortingRepository<FeedbackScore, String>{
	
	public Page<FeedbackScore> findByCompanyId(String companyId,Pageable pageable);
}
