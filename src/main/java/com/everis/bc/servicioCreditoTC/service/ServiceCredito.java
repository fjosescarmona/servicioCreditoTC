package com.everis.bc.servicioCreditoTC.service;


import java.util.Map;

import com.everis.bc.servicioCreditoTC.model.CreditoTC;
import com.everis.bc.servicioCreditoTC.model.Movimientos;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServiceCredito {
	
	public Mono<Map<String, Object>> saveData(CreditoTC tc);
	
	public Flux<CreditoTC> getData();
	
	public Flux<CreditoTC> getDataByDoc(String doc);

	public Mono<Map<String, Object>> getSaldo(String nro_tarjeta);

	public Mono<Void> deleteData(String id);
	
	public Mono<CreditoTC> editData(String id, CreditoTC tc);
	
	public Mono<Map<String, Object>> saveMovimiento(Movimientos mov);
	
	public Flux<Movimientos> getMovimientos();
}
