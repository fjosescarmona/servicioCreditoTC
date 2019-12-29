package com.everis.bc.servicioCreditoTC.service;


import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import com.everis.bc.servicioCreditoTC.model.CreditoTC;
import com.everis.bc.servicioCreditoTC.model.Deudores;
import com.everis.bc.servicioCreditoTC.model.Movimientos;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServiceCredito {
	
	public Mono<CreditoTC> saveData(CreditoTC tc);
	
	public Flux<CreditoTC> getData();
	
	public Flux<CreditoTC> getDataByDoc(String doc);

	public Mono<Map<String, Object>> getSaldo(String nro_tarjeta);

	public Mono<Void> deleteData(String id);
	
	public Mono<CreditoTC> editData(String id, CreditoTC tc);
	
	public Mono<Movimientos> saveConsumo(Movimientos mov);
	
	public Mono<Movimientos> savePago(Movimientos mov);
	
	public Mono<Movimientos> savePagoMinimo(Movimientos mov);
	
	public Flux<Movimientos> getMovimientos(String nro_tarjeta);
	
	public Flux<CreditoTC> getDeudaVencida();
	
	public Flux<Deudores> saveDeudoresTC(List<Deudores> deudores);
}
