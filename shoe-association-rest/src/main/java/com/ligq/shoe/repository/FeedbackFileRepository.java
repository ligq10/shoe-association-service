package com.ligq.shoe.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ligq.shoe.entity.FeedbackFile;


@RepositoryRestResource(exported = false)
public interface FeedbackFileRepository extends
	PagingAndSortingRepository<FeedbackFile, String>{

}
