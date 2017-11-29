package model;

import java.util.ArrayList;
import java.util.List;

import cmc.CmcImple;
import graficos.Punto;

public class Camino implements Comparable<Camino> {

	public Punto origen;
	public Punto destino;
	public Double costoAcumulado;
	public List<Arista> aristas = new ArrayList<>();
	public void dibujar(CmcImple cmc) {
		for (Arista arista : aristas) {
			cmc.dibujarCamino(arista.puntos);
		}
	}
	@Override
	public int compareTo(Camino o) {
		return this.costoAcumulado.compareTo(o.costoAcumulado);
	}
}
