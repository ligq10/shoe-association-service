package com.ligq.shoe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ligq.shoe.entity.FeedbackFile;


public interface FeedbackFileRepository extends
	CrudRepository<FeedbackFile, String>{
	
	@Query("SELECT n from FeedbackFile n where n.feedbackId = ?1")
	public List<FeedbackFile> findByFeedbackId(String feedbackId);
}
