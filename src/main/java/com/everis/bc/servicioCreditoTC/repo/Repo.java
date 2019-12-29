package com.everis.bc.servicioCreditoTC.repo;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.everis.bc.servicioCreditoTC.model.CreditoTC;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.lang.String;
import java.util.List;

public interface Repo extends ReactiveMongoRepository<CreditoTC, String>{
	@Query("{ 'doc': ?0 }")
	public Flux<CreditoTC> findByTitularesDoc(String doc);
	@Query("{ 'nro_tarjeta': ?0 }")
	public Mono<CreditoTC> findByNro_tarjeta(String nro_tarjeta);
	
	//public boolean existByTitulares(String doc);
	public Flux<CreditoTC> findByEstadoPago(String estadopago);
}
