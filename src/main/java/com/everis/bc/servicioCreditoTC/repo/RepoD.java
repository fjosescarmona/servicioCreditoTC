package com.everis.bc.servicioCreditoTC.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.everis.bc.servicioCreditoTC.model.CreditoTC;
import com.everis.bc.servicioCreditoTC.model.Deudores;

import reactor.core.publisher.Flux;


public interface RepoD extends ReactiveMongoRepository<Deudores, String>{
	@Query("{ 'documento': {$in:[ ?0 ]} }")
	public Flux<CreditoTC> findByTitularesDocList(List<String> docs);
}
