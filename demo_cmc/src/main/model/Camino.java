package model;

import java.util.ArrayList;
import java.util.List;

import cmc.CmcImple;

public class Camino {

	Double costoAcumulado;
	List<Arista> aristas = new ArrayList<>();
	public void dibujar(CmcImple cmc) {
		for (Arista arista : aristas) {
			cmc.dibujarCamino(arista.puntos);
		}
	}
}
