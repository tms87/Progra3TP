package model;

import enums.Direccion;
import graficos.Punto;
import utils.PuntoUtils;

public abstract class Arista implements Comparable<Arista> {

	public Punto origen;
	public Punto destino;
	public Direccion direccion;

	public Arista(Punto puntoA, Punto puntoB) {
		boolean invertir = false;
		if (puntoA.x > puntoB.x){
			invertir = true;
		} else if (puntoA.x == puntoB.x){
			if (puntoA.y >= puntoB.y){
				invertir = true;
			}
		}
		if (invertir){
			origen = puntoB;
			destino = puntoA;
		} else {
			origen = puntoA;
			destino = puntoB;
		}
		direccion = PuntoUtils.direccionRelativa(origen, destino);
	}
	
	public abstract Camino calcularCamino(Camino camino);

	public  abstract Double getOrden();
	
	@Override
	public int compareTo(Arista o) {
		return this.getOrden().compareTo(o.getOrden());
	}
}
