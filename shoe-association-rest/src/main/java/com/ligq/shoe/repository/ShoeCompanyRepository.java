package com.ligq.shoe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.ligq.shoe.entity.ShoeCompany;

@RepositoryRestResource(collectionResourceRel = "shoecompanies", path = "shoecompanies")
public interface ShoeCompanyRepository extends
	PagingAndSortingRepository<ShoeCompany, String>{

	@RestResource(exported = false)
	@Query(value = "select t from ShoeCompany t where t.name like %:name%")
	public Page<ShoeCompany> findByName(@Param("name") String name,Pageable pageable);
	
	public Page<ShoeCompany> findByNamePhoneticize(String namePhoneticize,Pageable pageable);
	
	public Page<ShoeCompany> findByCreditLevel(String creditLevel,Pageable pageable);

}
