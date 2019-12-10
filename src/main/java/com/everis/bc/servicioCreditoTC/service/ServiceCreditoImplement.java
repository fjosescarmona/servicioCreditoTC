package com.everis.bc.servicioCreditoTC.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.everis.bc.servicioCreditoTC.model.CreditoTC;
import com.everis.bc.servicioCreditoTC.model.Empresa;
import com.everis.bc.servicioCreditoTC.model.Listas;
import com.everis.bc.servicioCreditoTC.model.Movimientos;
import com.everis.bc.servicioCreditoTC.model.Persona;
import com.everis.bc.servicioCreditoTC.repo.Repo;
import com.everis.bc.servicioCreditoTC.repo.RepoMovimientos;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ServiceCreditoImplement implements ServiceCredito {

	@Autowired
	private Repo repo1;
	@Autowired
	private RepoMovimientos repoMov;
	
	@Autowired
	private WebClient client;
	
	//private List<String> aux;
	@Override
	public Mono<Map<String, Object>> saveData(CreditoTC cuenta) {
		Map<String, Object> respuesta = new HashMap<String, Object>();
		//validando tipo de cuenta: Persona o Empresa
		if(cuenta.getTipo().equals("P")) {
			
			cuenta.getTitulares().stream().forEach(titular->{
			
			client.post().uri("/savePersonaData").accept(MediaType.APPLICATION_JSON_UTF8)
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.body(BodyInserters.fromObject(titular))
					.retrieve()
					.bodyToMono(Persona.class).subscribe();
			
			});
			return repo1.save(cuenta).map(cta->{
				respuesta.put("Mensaje: ", "guardado correcto");
				return  respuesta;
			});
			
		}else {
			return Mono.just(cuenta).map(c->{
				respuesta.put("Error", "Las cuentas de ahorro son solo para clintes Persona.");
				return respuesta;
			});
		}
		
		
		// TODO Auto-generated method stub
		
		/*return repo1.save(cuenta).map(cta->{
			respuesta.put("Mensaje: ", "guardado correcto");
			return  respuesta;
		});*/
		//respuesta.put("Mensaje: ", "guardado correcto");
		
	}

	@Override
	public Flux<CreditoTC> getData() {
		// TODO Auto-generated method stub
		return repo1.findAll();
	}

	@Override
	public Mono<Void> deleteData(String id) {
		// TODO Auto-generated method stub
		return repo1.findById(id).flatMap(tc->repo1.delete(tc));
	}

	@Override
	public Mono<CreditoTC> editData(String id, CreditoTC tc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Map<String, Object>> saveMovimiento(Movimientos movimiento) {
		Map<String, Object> respuesta = new HashMap<String, Object>();
		
		return repo1.findByNro_tarjeta(movimiento.getNro_tarjeta()).map(tc -> {
			Double saldo=tc.getSaldo();
			switch(movimiento.getDescripcion()) {
				case "consumo":{
					if(saldo>=movimiento.getMonto()) {
						tc.setSaldo(saldo-movimiento.getMonto());
						repo1.save(tc).subscribe();
						repoMov.save(movimiento).subscribe();
						respuesta.put("Result", "Consumo realizado, su nuevo saldo es: "+(saldo-movimiento.getMonto()));
						return respuesta;
					}else {
						respuesta.put("Result", "Su saldo no es suficiente para realizar la operaciòn");
						return respuesta;
					}
					
				}
				case "pago":{
					tc.setSaldo(saldo+movimiento.getMonto());
					repo1.save(tc).subscribe();
					repoMov.save(movimiento);
					respuesta.put("Result", "Pago realizado, su nuevo saldo es: "+(saldo+movimiento.getMonto()));
					return respuesta;
				}
			}
			respuesta.put("Error", "Especifique el movimiento a realizar");
			return respuesta;
		});
		
		/*return repoMov.save(movimiento).map(mov->{
			respuesta.put("Mensaje: ", "ok");
			return  respuesta;
		});*/
	}

	@Override
	public Flux<Movimientos> getMovimientos() {
		// TODO Auto-generated method stub
		return repoMov.findAll();
	}

	@Override
	public Mono<CreditoTC> getDataByDoc(String doc) {
		// TODO Auto-generated method stub
		return repo1.findByTitularesDoc(doc);
	}

	@Override
	public Mono<Map<String, Object>> getSaldo(String nro_tarjeta) {
		// TODO Auto-generated method stub
		Map<String, Object> respuesta = new HashMap<String, Object>();
		
		return repo1.findByNro_tarjeta(nro_tarjeta).map(tc->{
			respuesta.put("saldo", tc.getSaldo());
			return respuesta;
		});
		//return null;
	}

}
