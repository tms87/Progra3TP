package model;

import java.util.Iterator;
import java.util.LinkedList;

import graficos.Punto;

public class Camino {

	public LinkedList<Punto> puntos = new LinkedList<>();
	public Double costoAcumulado = 0D;
	
	@SuppressWarnings("unchecked")
	public void unirCaminos(Camino nuevoCamino) {
		if(this.puntos.isEmpty()){
			this.puntos = nuevoCamino.puntos;
		}else if (this.puntos.getFirst().igual(nuevoCamino.puntos.getLast())){
			LinkedList<Punto> clon = (LinkedList<Punto>) nuevoCamino.puntos.clone();
			clon.removeLast();
			this.puntos.addAll(0,clon);
		} else if (this.puntos.getFirst().igual(nuevoCamino.puntos.getFirst())) {
			LinkedList<Punto> clon = (LinkedList<Punto>) nuevoCamino.puntos.clone();
			clon.removeFirst();
			Iterator<Punto> it = clon.descendingIterator();
			LinkedList<Punto> tmp = new LinkedList<>();
			while (it.hasNext()){
				tmp.add(it.next());
			}
			this.puntos.addAll(this.puntos.size(),this.puntos);
		} else if (this.puntos.getLast().igual(nuevoCamino.puntos.getLast())) {
			LinkedList<Punto> clon = (LinkedList<Punto>) nuevoCamino.puntos.clone();
			clon.removeLast();
			Iterator<Punto> it = clon.descendingIterator();
			LinkedList<Punto> tmp = new LinkedList<>();
			while (it.hasNext()){
				tmp.add(it.next());
			}
			this.puntos.addAll(this.puntos);
		} else if (this.puntos.getLast().igual(nuevoCamino.puntos.getFirst())){
			LinkedList<Punto> clon = (LinkedList<Punto>) nuevoCamino.puntos.clone();
			clon.removeFirst();
			this.puntos.addAll(clon);
		}
		this.costoAcumulado += nuevoCamino.costoAcumulado;
	}
}
