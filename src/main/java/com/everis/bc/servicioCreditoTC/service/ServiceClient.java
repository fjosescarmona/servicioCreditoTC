package com.everis.bc.servicioCreditoTC.service;

import com.everis.bc.servicioCreditoTC.model.Persona;

import reactor.core.publisher.Mono;

public interface ServiceClient {
	public Mono<Persona> saveData(Persona persona);
}
