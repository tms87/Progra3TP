package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graficos.Punto;

public class AristaAbstracta extends Arista {

	public Double distancia = Double.MAX_VALUE;
	public GrafoMultiEtapa grafo = new GrafoMultiEtapa(this);
	
	public AristaAbstracta(Punto puntoA, Punto puntoB,double distancia) {
		super(puntoA, puntoB);
		this.distancia = distancia;
	}
	
	@Override
	public boolean equals(Object obj) {
		AristaAbstracta o = (AristaAbstracta) obj;
		if(origen.equals(o.origen)){
			if(destino.equals(o.destino)){
				return true;
			}
		} else if(origen.equals(o.destino)){
			if(destino.equals(o.origen)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return origen.x + destino.x+ origen.y+destino.y;
	}

	public Camino calcularCamino(Camino camino) {
		Map<PuntoKey,Camino> memoria = new HashMap<>();
		for (Arista arista : grafo.v2) {
			Camino tmp = arista.calcularCamino(null);
			memoria.put(new PuntoKey(arista.origen), tmp);
		}
		agregarYCompararCamino(memoria,grafo.v1_2);
		agregarYCompararCamino(memoria,grafo.v1);
		Camino nuevoCamino = memoria.get(new PuntoKey(origen));
		if (camino != null){
			nuevoCamino.unirCaminos(camino);
		}
		return nuevoCamino;
	}

	private void agregarYCompararCamino(Map<PuntoKey, Camino> memoria,List<Arista> listaArista) {
		for (Arista arista : listaArista) {
			Camino destino = memoria.get(new PuntoKey(arista.destino));
			Camino tmp = arista.calcularCamino(destino);
			Camino tmp2 = memoria.get(new PuntoKey(arista.origen));
			if (tmp2 == null) {
				memoria.put(new PuntoKey(arista.origen), tmp);
			} else {
				if (tmp.costoAcumulado < tmp2.costoAcumulado){
					memoria.put(new PuntoKey(arista.origen), tmp);
				}
			}
		}
	}

	@Override
	public Double getOrden() {
		return distancia;
	}
}
