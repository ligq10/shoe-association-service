package com.ligq.shoe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.ligq.shoe.entity.Employee;

@RepositoryRestResource(exported = false)
public interface EmployeeRepository  extends
	PagingAndSortingRepository<Employee, String>{


	@RestResource(exported = false)
	@Query(value = "select t from Employee t where  t.name like %:keyword% ")
	public Page<Employee> findByKeyword(@Param("keyword") String keyword,Pageable pageable);

}
