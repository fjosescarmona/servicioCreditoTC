package com.everis.bc.servicioCreditoTC.repo;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.everis.bc.servicioCreditoTC.model.CreditoTC;
import com.everis.bc.servicioCreditoTC.model.Movimientos;

import reactor.core.publisher.Flux;

public interface RepoMovimientos extends ReactiveMongoRepository<Movimientos, String>{

	@Query("{ 'nro_tarjeta': ?0 }")
	public Flux<Movimientos> findByNro_tarjeta(String nro_tarjeta);
}
