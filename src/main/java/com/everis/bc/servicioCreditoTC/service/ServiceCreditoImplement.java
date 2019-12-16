package com.everis.bc.servicioCreditoTC.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everis.bc.servicioCreditoTC.model.CreditoTC;
import com.everis.bc.servicioCreditoTC.model.Movimientos;
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
	
	@Override
	public Mono<Map<String, Object>> saveData(CreditoTC tc) {
		Map<String, Object> respuesta = new HashMap<String, Object>();
		return repo1.save(tc).map(tdc->{
			respuesta.put("Mensaje: ", "guardado correcto");
			respuesta.put("Info: ", tdc);
			return  respuesta;
		});
		
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
						respuesta.put("Result", "Consumo realizado, su credito disponible es: "+(saldo-movimiento.getMonto()));
						return respuesta;
					}else {
						respuesta.put("Result", "Su saldo no es suficiente para realizar la operaciòn");
						return respuesta;
					}
					
				}
				case "pago":{
					if(saldo+movimiento.getMonto()<=tc.getLimite()) {
						tc.setSaldo(saldo+movimiento.getMonto());
						repo1.save(tc).subscribe();
						repoMov.save(movimiento).subscribe();
						respuesta.put("Result", "Pago realizado, su nuevo saldo es: "+(saldo+movimiento.getMonto()));
						return respuesta;
					}else {
						respuesta.put("Error", "El saldo de su tarjeta no debe ser mayor a su limite de credito");
						return respuesta;
					}
					
					
				}
				
			}
			respuesta.put("Error", "Movimiento desconocido");
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
	public Flux<CreditoTC> getDataByDoc(String doc) {
		// TODO Auto-generated method stub
		return repo1.findByTitularesDoc(doc)
				.switchIfEmpty(Mono.just("").flatMap(r->{
					CreditoTC tc=new CreditoTC();
					return Mono.just(tc);
				})
				);
	}

	@Override
	public Mono<Map<String, Object>> getSaldo(String nro_tarjeta) {
		// TODO Auto-generated method stub
		Map<String, Object> respuesta = new HashMap<String, Object>();
		
		return repo1.findByNro_tarjeta(nro_tarjeta).map(tc->{
			respuesta.put("Credito Disponible", tc.getSaldo());
			return respuesta;
		});
		//return null;
	}

}
