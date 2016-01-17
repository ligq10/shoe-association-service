package com.ligq.shoe.repository;

import java.util.List;

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
	
	@RestResource(exported = false)
	@Query(value = "select t from ShoeCompany t where t.namePhoneticize like %:namePhoneticize%")
	public Page<ShoeCompany> findByNamePhoneticize(@Param("namePhoneticize") String namePhoneticize,Pageable pageable);
	
	public Page<ShoeCompany> findByCreditLevel(Integer creditLevel,Pageable pageable);
	
	@RestResource(exported = false)
	@Query(value = "select t from ShoeCompany t where  (t.name like %:keyword% or t.submitPerson like %:keyword%) and t.auditStatus = :auditstatus")
	public Page<ShoeCompany> findByKeywordAndAuditStatus(@Param("keyword") String keyword,@Param("auditstatus") Integer auditstatus,Pageable pageable);


}
