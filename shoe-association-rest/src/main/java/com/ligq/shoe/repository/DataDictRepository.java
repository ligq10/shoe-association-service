package com.ligq.shoe.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ligq.shoe.entity.DataDict;

@RepositoryRestResource(exported = false)
public interface DataDictRepository extends
	PagingAndSortingRepository<DataDict, String>{
	
	public DataDict findByTypeCodeAndDictCode(String typeCode,String dictCode);
	
	public List<DataDict> findByDictCode(String dictCode);

	public List<DataDict> findByTypeCode(String typecode);
}
