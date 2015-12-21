package com.ligq.shoe.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ligq.shoe.entity.Image;


@RepositoryRestResource(exported = false)
public interface ImageRepository extends
	PagingAndSortingRepository<Image, String>{

}
