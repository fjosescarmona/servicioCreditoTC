package com.everis.bc.servicioCreditoTC.service;

import java.util.Date;
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
	public Flux<Movimientos> getMovimientos(String nro_tarjeta) {
		// TODO Auto-generated method stub
		return repoMov.findByNro_tarjeta(nro_tarjeta);
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

	@Override
	public Mono<Movimientos> saveConsumo(Movimientos mov) {
		// TODO Auto-generated method stub
		return repo1.findByNro_tarjeta(mov.getNro_tarjeta()).flatMap(tc->{
			if(tc.getSaldo()>=mov.getMonto()) {
				tc.setSaldo(tc.getSaldo()-mov.getMonto());
				tc.setMinimo(((tc.getLimite()-(tc.getSaldo()-mov.getMonto()))*10)/100);
				tc.setEstadoPago("pp");
				if(mov.getFecha().getDay()<5) {
					Date fecha=mov.getFecha();
					fecha.setDate(5);
					tc.setFechaPago(fecha);
				}else {
					Date fecha=mov.getFecha();
					fecha.setDate(5);
					fecha.setMonth(fecha.getMonth()+1);
					tc.setFechaPago(fecha);
				}
				return repo1.save(tc).flatMap(stc->{
					return repoMov.save(mov);
				});
				
				
			}else {
				return Mono.just(new Movimientos());
			}
		});
		
	}

	@Override
	public Mono<Movimientos> savePago(Movimientos mov) {
		// TODO Auto-generated method stub
		return repo1.findByNro_tarjeta(mov.getNro_tarjeta()).flatMap(tc->{
			//--------valida que no se pague mas del limite de credito---//
			if(tc.getSaldo()+mov.getMonto()<=tc.getLimite()) {
				tc.setSaldo(tc.getSaldo()+mov.getMonto());
				tc.setMinimo(((tc.getLimite()-(tc.getSaldo()+mov.getMonto()))*10)/100);
				//---si paga el total de la deuda no genera intereses y su estado de pago es vacio--//
				if((tc.getLimite()-(tc.getSaldo()+mov.getMonto()))==0) {
					tc.setEstadoPago("");
					//--si tiene deuda se asigna y estado de pago pp y fecha limite de pago--//
				}else {
					tc.setEstadoPago("pp");
					if(mov.getFecha().getDay()<5) {
						Date fecha=mov.getFecha();
						fecha.setDate(5);
						tc.setFechaPago(fecha);
					}else {
						Date fecha=mov.getFecha();
						fecha.setDate(5);
						fecha.setMonth(fecha.getMonth()+1);
						tc.setFechaPago(fecha);
					}
				}
				
				return repo1.save(tc).flatMap(stc->{
					return repoMov.save(mov);
				});
				
			}else {
				return Mono.just(new Movimientos());
			}
		});
	}

	@Override
	public Mono<Movimientos> savePagoMinimo(Movimientos mov) {
		// TODO Auto-generated method stub
		return repo1.findByNro_tarjeta(mov.getNro_tarjeta()).flatMap(tc->{
			if(mov.getMonto()==tc.getMinimo()) {
				//tc.setMinimo(mov.getMonto()-tc.getMinimo());
				tc.setEstadoPago("p");
				return repo1.save(tc).flatMap(stc->{
					return repoMov.save(mov);
				});
			}else {
				return Mono.just(new Movimientos());
			}
		});
	}

}
