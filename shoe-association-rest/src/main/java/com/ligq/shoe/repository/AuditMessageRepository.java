package com.ligq.shoe.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ligq.shoe.entity.AuditMessage;

@RepositoryRestResource(exported = false)
public interface AuditMessageRepository extends
	PagingAndSortingRepository<AuditMessage, String>{

	public List<AuditMessage> findByBusinessId(String uuid);

}
