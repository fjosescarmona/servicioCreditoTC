package com.everis.bc.servicioCreditoTC.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.everis.bc.servicioCreditoTC.model.Movimientos;

public interface RepoMovimientos extends ReactiveMongoRepository<Movimientos, String>{

}
