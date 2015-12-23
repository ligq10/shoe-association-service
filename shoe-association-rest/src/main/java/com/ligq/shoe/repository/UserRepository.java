package com.ligq.shoe.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ligq.shoe.entity.User;

@RepositoryRestResource(exported = false)
public interface UserRepository  extends
	PagingAndSortingRepository<User, String>{

}
