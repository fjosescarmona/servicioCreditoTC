package com.everis.bc.servicioCreditoTC.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.everis.bc.servicioCreditoTC.model.CreditoTC;
import com.everis.bc.servicioCreditoTC.model.Movimientos;
import com.everis.bc.servicioCreditoTC.service.ServiceCredito;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CreditoTCController {

	@Autowired
	private ServiceCredito s_cuenta;

	@PostMapping("/saveTCData")
	public Mono<Map<String, Object>> saveTCData(@RequestBody CreditoTC tc){
		return s_cuenta.saveData(tc);
	}
	
	@GetMapping("/getTCData/{doc}")
	public Flux<CreditoTC> getTCData(@PathVariable("doc") String doc){
		return s_cuenta.getDataByDoc(doc);
	}
	
	@GetMapping("/getTCSaldo/{nro_tarjeta}")
	public Mono<Map<String, Object>> getTCSaldo(@PathVariable("nro_tarjeta") String nro_tarjeta){
		return s_cuenta.getSaldo(nro_tarjeta);
	}
	
	@PostMapping("/saveConsumoTC")
	public Mono<Movimientos> saveConsumoTC(@RequestBody Movimientos movimiento){
		return s_cuenta.saveConsumo(movimiento);
	}
	
	@PostMapping("/savePagoTC")
	public Mono<Movimientos> savePagoTC(@RequestBody Movimientos movimiento){
		return s_cuenta.savePago(movimiento);
	}
	
	@GetMapping("/getMovimientosTC/{nro_tarjeta}")
	public Flux<Movimientos> getMovimientosTC(@PathVariable("nro_tarjeta") String nro_tarjeta){
		return s_cuenta.getMovimientos(nro_tarjeta);
	}

}
