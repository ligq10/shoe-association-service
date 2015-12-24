package com.ligq.shoe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ligq.shoe.entity.FeedbackFile;


@RepositoryRestResource(exported = false)
public interface FeedbackFileRepository extends
	CrudRepository<FeedbackFile, String>{

	public List<FeedbackFile> findByFeedbackId(String uuid);

}
