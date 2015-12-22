package com.ligq.shoe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ligq.shoe.entity.ShoeCompany;

@RepositoryRestResource(exported = false)
public interface ShoeCompanyRepository extends
	PagingAndSortingRepository<ShoeCompany, String>{

	public Page<ShoeCompany> findByNameLike(String name,Pageable pageable);
	
	public Page<ShoeCompany> findByNamePhoneticize(String namePhoneticize,Pageable pageable);
	
	public Page<ShoeCompany> findByCreditLevel(String creditLevel,Pageable pageable);

}
