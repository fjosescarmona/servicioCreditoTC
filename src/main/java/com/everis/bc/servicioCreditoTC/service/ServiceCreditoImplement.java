package com.everis.bc.servicioCreditoTC.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.everis.bc.servicioCreditoTC.model.CreditoTC;
import com.everis.bc.servicioCreditoTC.model.Deudores;
import com.everis.bc.servicioCreditoTC.model.Movimientos;
import com.everis.bc.servicioCreditoTC.repo.Repo;
import com.everis.bc.servicioCreditoTC.repo.RepoD;
import com.everis.bc.servicioCreditoTC.repo.RepoMovimientos;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ServiceCreditoImplement implements ServiceCredito {

	@Autowired
	@Qualifier("info")
	private WebClient info;
	
	@Autowired
	private RepoD repod;
	
	@Autowired
	private Repo repo1;
	@Autowired
	private RepoMovimientos repoMov;
	
	@Override
	public Mono<CreditoTC> saveData(CreditoTC tc) {
		Map<String, Object> params = new HashMap<String, Object>();

		List<String> doc = new ArrayList<>();
		doc.add(tc.getDoc());
		
		params.put("docs", doc);
		
		return repod.findByTitularesDocList(doc).flatMap(res -> {
			return Mono.just(new CreditoTC());
		}).switchIfEmpty( 
						repo1.save(tc).flatMap(tdc->{
							return  Mono.just(tdc);
						})
				).next();
		
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
		return repo1.findByTitularesDoc(doc);
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
			SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd");
			
			int anio=mov.getFecha().getYear()+1900;
			int mes=mov.getFecha().getMonth()+1;
			int dia=mov.getFecha().getDate()+1;
			if(tc.getSaldo()>=mov.getMonto()) {
				tc.setSaldo(tc.getSaldo()-mov.getMonto());
				tc.setMinimo(((tc.getLimite()-(tc.getSaldo()-mov.getMonto()))*10)/100);
				tc.setEstadoPago("pp");
				if(dia<5) {
					Date fecha=new Date();
					try {
						fecha = fdate.parse(anio+"-"+(mes+1)+"-"+05);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					tc.setFechaPago(fecha);
				}else {
					Date fecha=new Date();
					try {
						fecha = fdate.parse(anio+"-"+(mes+1)+"-"+05);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
			
			SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd");
			
			int anio=mov.getFecha().getYear()+1900;
			int mes=mov.getFecha().getMonth()+1;
			int dia=mov.getFecha().getDate()+1;
			//--------valida que no se pague mas del limite de credito---//
			if(tc.getSaldo()+mov.getMonto()<=tc.getLimite()) {
				
				tc.setMinimo(((tc.getLimite()-(tc.getSaldo()+mov.getMonto()))*10)/100);
				tc.setSaldo(tc.getSaldo()+mov.getMonto());
				//---si paga el total de la deuda no genera intereses y su estado de pago es vacio--//
				if((tc.getLimite()-(tc.getSaldo()+mov.getMonto()))==0) {
					tc.setEstadoPago("");
					//--si tiene deuda se asigna y estado de pago pp y fecha limite de pago--//
				}else {
					tc.setEstadoPago("pp");
					if(dia<5) {
						Date fecha=new Date();
						try {
							fecha = fdate.parse(anio+"-"+(mes+1)+"-"+05);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						tc.setFechaPago(fecha);
					}else {
						Date fecha=new Date();
						try {
							fecha = fdate.parse(anio+"-"+(mes+1)+"-"+05);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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

	@Override
	public Flux<CreditoTC> getDeudaVencida() {
		// TODO Auto-generated method stub
		
		return repo1.findByEstadoPago("pp").flatMap(d->{
			return repo1.findByEstadoPago("p").collectList().flatMapMany(p->{
				p.forEach(tc->{
					tc.setEstadoPago("pp");
				});
				return repo1.saveAll(p).flatMap(f->{
					return Flux.just(d);
				});
			}).switchIfEmpty(Flux.just(d));
		});
	}

	@Override
	public Flux<Deudores> saveDeudoresTC(List<Deudores> deudores) {
		// TODO Auto-generated method stub
		return repod.deleteAll().flatMapMany(v->{
			return repod.saveAll(deudores);
		}).switchIfEmpty(repod.saveAll(deudores));
	}

}
