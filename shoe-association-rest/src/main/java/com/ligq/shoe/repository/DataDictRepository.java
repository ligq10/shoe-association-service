package com.ligq.shoe.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ligq.shoe.entity.DataDict;

@RepositoryRestResource(exported = false)
public interface DataDictRepository extends
	PagingAndSortingRepository<DataDict, String>{
	
	List<DataDict> findByDictCode(String dictCode);
}
