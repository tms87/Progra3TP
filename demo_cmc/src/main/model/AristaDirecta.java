package model;

import graficos.Punto;

public class AristaDirecta extends Arista {

	public Double costo;
	
	public AristaDirecta(Punto puntoA, Punto puntoB, Double costo) {
		super(puntoA,puntoB);
		this.costo = costo;
	}

	@Override
	public Camino calcularCamino(Camino camino) {
		Camino tmp = new Camino();
		tmp.puntos.addFirst(destino);
		tmp.puntos.addFirst(origen);
		tmp.costoAcumulado += costo;
		if (camino != null){
			tmp.unirCaminos(camino);
		}
		return tmp;
	}

	@Override
	public Double getOrden() {
		return costo;
	}
}
