package com.ligq.shoe.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ligq.shoe.entity.AuditMessage;

@RepositoryRestResource(exported = false)
public interface AuditMessageRepository extends
	PagingAndSortingRepository<AuditMessage, String>{

}
