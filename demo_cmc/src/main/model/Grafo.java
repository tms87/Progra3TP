package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import graficos.Punto;

public class Grafo {

	public Set<Nodo> nodos = new HashSet<>();
	public List<List<Punto>> solucion = new ArrayList<>();
	public Double costoSolucion = Double.MAX_VALUE;
	
	public Nodo getNodo(Punto punto){
		Iterator<Nodo> it = nodos.iterator();
		while (it.hasNext()) {
			Nodo nodo = (Nodo) it.next();
			if (nodo.punto.igual(punto)){
				return nodo;
			}
		}
		return null;
	}
	
	public Camino generarCamino(Nodo nodo){
		Nodo tmp = nodo;
		Camino camino = new Camino();
		camino.costoAcumulado = nodo.costoMinimoAcumulado;
		while(tmp.antecesor != null){
			for (Arista arista : tmp.aristas) {
				if (arista.origen.igual(tmp.antecesor.punto) || arista.destino.igual(tmp.antecesor.punto)){
					camino.aristas.add(arista);
					tmp = getNodo(tmp.antecesor.punto);
					break;
				}
			}
		}
		return camino;
	}
}
