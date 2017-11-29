
package cmc;

import java.awt.Rectangle;
/**
 * Obtiene la lista de los puntos marcados en la matriz (mapa)
 * Itera los mismos de la siguiente forma:
 * Obtiene los 2 primeros y expande los contiguos entre ambos.
 * Primero expande eje x, segundo expande el eje y.
 * Reitera la lista expandiendo el siguiente (siempre expandiendo de a pares)
 * El recorrido es secuencial (conforme al orden de marcado de los puntos en el mapa)
 * Invoca la método dibujar en cada iteración.
 * Al finalizar la iteración expande los contiguos entre el último y el primero de la lista.
 * Vuelve a Invocar la método dibujar para cerrar el ciclo.
 * No contempla las densidades definidas en la matriz (mapa)
 */
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import graficos.Area;
import graficos.Punto;
import mapa.MapaInfo;
import model.Arista;
import model.Camino;
import model.Grafo;
import model.Nodo;

public class CmcTP {
	private MapaInfo mapa;
	private CmcImple cmc;
	private Set<Area> areas = new HashSet<>();
	private Grafo grafo = new Grafo();
	List<Punto> puntosTest = new ArrayList<>();

	public CmcTP(MapaInfo mapa, CmcImple cmc) {
		this.mapa = mapa;
		this.cmc = cmc;
		mostarColeccionDeAreas();
		mostarColeccionDePuntos();
		procesarAreas();
		popularGrafo();
		try {
			generarAristasIniciales();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		dibujarAristas();
		obtenerCaminos();
	}

	private void obtenerCaminos() {
		for (int i = 0; i < mapa.getPuntos().size(); i++) {
			for (int j = i+1; j < mapa.getPuntos().size(); j++) {
				Camino camino = getCamino(mapa.getPuntos().get(i),mapa.getPuntos().get(j));
				camino.dibujar(cmc);
			}
		}
	}

	private Camino getCamino(Punto origen, Punto destino) {
//		System.out.println("inicia getCamino");
		Nodo nDest = grafo.getNodo(destino);
		Grafo grafoParcial = new Grafo();
		Nodo newDest = new Nodo(destino);
		newDest.costoMinimoAcumulado = 0D;
		grafoParcial.nodos.add(newDest);
		List<Nodo> nodosActivos = new ArrayList<>();
		for (Arista arista : nDest.aristas) {
			Punto punto = (arista.origen != destino)?arista.origen:arista.destino;
			Nodo nodo = new Nodo(punto);
			nodo.aristas.add(arista);
			nodo.costoMinimoAcumulado = arista.costo;
			nodo.antecesor = newDest;
			nodosActivos.add(nodo);
			grafoParcial.nodos.add(nodo);
		}
		System.out.println("primer paso listo");
		buscarCamino(nodosActivos,grafoParcial,origen);
		Camino camino = grafoParcial.generarCamino(grafoParcial.getNodo(origen));
		return camino;
	}

	private void buscarCamino(List<Nodo> nodosActivos, Grafo grafoParcial, Punto destino) {
//		System.out.println("inicia BuscarCamino");
		List<Nodo> nuevosActivos = new ArrayList<>();
		for (Nodo nodo : nodosActivos) {
			Nodo nodoGrafo = grafo.getNodo(nodo.punto);
			for (Arista arista : nodoGrafo.aristas) {
				Punto punto = (arista.origen != nodo.punto)?arista.origen:arista.destino;
				if(punto.igual(nodo.antecesor.punto)){
					continue;
				}
				double costo = nodo.costoMinimoAcumulado+arista.costo;
				if (costo < grafoParcial.costoSolucion){
					Nodo tmp = grafoParcial.getNodo(punto);
					if (tmp != null){
						if (tmp.costoMinimoAcumulado > costo){
							tmp.costoMinimoAcumulado = costo;
							tmp.removerArista(tmp.antecesor.punto);
							tmp.antecesor = nodo;
							tmp.aristas.add(arista);
							if (!nuevosActivos.contains(tmp) && !nuevosActivos.contains(tmp) && !tmp.punto.igual(destino))
								nuevosActivos.add(tmp);
						}
					} else {
						tmp = new Nodo(punto);
						tmp.aristas.add(arista);
						tmp.costoMinimoAcumulado = costo;
						tmp.antecesor = nodo;
						grafoParcial.nodos.add(tmp);
						if (!tmp.punto.igual(destino)){
							nuevosActivos.add(tmp);
						}
					}
				}
			}
		}
		if (!nuevosActivos.isEmpty())
			buscarCamino(nuevosActivos,grafoParcial,destino);
	}

	private void procesarAreas() {
		areas.addAll(mapa.getAreas());
		for (int i = 0; i < mapa.getAreas().size(); i++) {
			for (int j = i+1; j < mapa.getAreas().size(); j++) {
				Rectangle recI = mapa.getAreas().get(i).getRectangle();
				Rectangle recJ = mapa.getAreas().get(j).getRectangle();
				Rectangle newRec = recI.intersection(recJ);
				if (!newRec.isEmpty()){
					Punto centro = new Punto((int)newRec.getCenterX(),(int)newRec.getCenterY());
					areas.add(new Area(new Punto(newRec.x,newRec.y), new Punto((int)newRec.getMaxX(),(int)newRec.getMaxY()), mapa.getDensidad(centro)));
				}
			}
		}
	}

	private void popularGrafo() {
		for (Punto punto : mapa.getPuntos()) {
			Nodo nodo = new Nodo(punto);
			grafo.nodos.add(nodo);
		}
		Iterator<Area> iterador = areas.iterator();
		while(iterador.hasNext()) {
			Area area = iterador.next();
			Rectangle rec =area.getRectangle();
			agregarNodo(rec.x,rec.y,true,true);
			agregarNodo(rec.x,(int)rec.getMaxY()-1,true,false);
			agregarNodo((int)rec.getMaxX()-1,(int)rec.getMaxY()-1,false,false);
			agregarNodo((int)rec.getMaxX()-1,rec.y,false,true);
		}
	}
	
	private void agregarNodo(int x, int y, boolean upX, boolean upY) {
		int nodoX, nodoY;
		int color = mapa.getDensidad(x,y);
//		if(upX) {
//			if (mapa.getDensidad(x,y) >= mapa.getDensidad(x-1,y))
//				nodoX = x-1;
//			else
//				nodoX = x;
//		} else {
//			if (mapa.getDensidad(x,y) >= mapa.getDensidad(x+1,y))
//				nodoX = x+1;
//			else
//				nodoX = x;
//		}
//		if(upY){
//			if (mapa.getDensidad(x,y) >= mapa.getDensidad(x,y-1))
//				nodoY = y-1;
//			else
//				nodoY = y;
//		} else {
//			if (mapa.getDensidad(x,y) >= mapa.getDensidad(x,y+1))
//				nodoY = y+1;
//			else
//				nodoY = y;
//		}
		if(upX) {
				nodoX = x-1;
		} else {
				nodoX = x+1;
		}
		if(upY){
				nodoY = y-1;
		} else {
				nodoY = y+1;
		}
		if (!(mapa.getDensidad(x+1,y) == color && mapa.getDensidad(x-1,y) == color
				&& mapa.getDensidad(x,y+1) == color && mapa.getDensidad(x,y-1) == color
				&& mapa.getDensidad(x+1,y+1) == color && mapa.getDensidad(x-1,y-1) == color
				&& mapa.getDensidad(x-1,y+1) == color && mapa.getDensidad(x+1,y-1) == color))
			grafo.nodos.add(new Nodo(new Punto(x,y)));
//			grafo.nodos.add(new Nodo(new Punto(nodoX,nodoY)));
		if (!(mapa.getDensidad(nodoX+1,nodoY) == color && mapa.getDensidad(nodoX-1,nodoY) == color
				&& mapa.getDensidad(nodoX,nodoY+1) == color && mapa.getDensidad(nodoX,nodoY-1) == color
				&& mapa.getDensidad(nodoX+1,nodoY+1) == color && mapa.getDensidad(nodoX-1,nodoY-1) == color
				&& mapa.getDensidad(nodoX-1,nodoY+1) == color && mapa.getDensidad(nodoX+1,nodoY-1) == color))
			grafo.nodos.add(new Nodo(new Punto(nodoX,nodoY)));
	}

	private boolean validarColor(int x, int y) {
		int color = mapa.getDensidad(x,y);
		if (mapa.getDensidad(x+1,y) == color && mapa.getDensidad(x-1,y) == color
				&& mapa.getDensidad(x,y+1) == color && mapa.getDensidad(x,y-1) == color
				&& mapa.getDensidad(x+1,y+1) == color && mapa.getDensidad(x-1,y-1) == color
				&& mapa.getDensidad(x-1,y+1) == color && mapa.getDensidad(x+1,y-1) == color)
			return false;
		return true;
	}

	private void generarAristasIniciales() throws Exception {
		Iterator<Nodo> it = grafo.nodos.iterator();
		while (it.hasNext()) {
			Nodo nodo = (Nodo) it.next();
			Iterator<Nodo> it2 = grafo.nodos.iterator();
			while (it2.hasNext()) {
				Nodo nodo2 = (Nodo) it2.next();
				if(!nodo.equals(nodo2)){
					if (!nodo.existeArista(nodo.punto, nodo2.punto)){
						Arista arista = generarArista(nodo.punto, nodo2.punto);
						if (arista != null && arista.costo < Double.MAX_VALUE){
							nodo.aristas.add(arista);
							nodo2.aristas.add(arista);
						}
					}
				}
			}
		}
	}
	
	private Arista generarArista(Punto puntoA, Punto puntoB) {
		return new Arista(puntoA, puntoB, mapa);
	}
	
	private void dibujarAristas() {
		Iterator<Nodo> it = grafo.nodos.iterator();
		while (it.hasNext()) {
			Nodo nodo = (Nodo) it.next();
			for (Arista arista : nodo.aristas) {
				List<Punto> lista = arista.puntos;
				System.out.println("arista: "+arista.origen.x+" "+arista.origen.y+" "+arista.destino.x+" "+arista.destino.y+" "+"costo: "+arista.costo);
				cmc.dibujarCamino(lista);
			}
		}
	}

	/** consulta clase MapaInfo */
	private void mostarColeccionDeAreas() {
		System.out.println("Mapa: " + MapaInfo.LARGO + " x " + MapaInfo.ALTO);
		for (Area a : mapa.getAreas()) {
			System.out.println(a);
		}
	}

	/** consulta clase MapaInfo */
	private void mostarColeccionDePuntos() {
		for (Punto c : mapa.getPuntos()) {
			int densidad = mapa.getDensidad(c);
			System.out.println(c + " D+: " + densidad);
		}
	}
	
}
