
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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import graficos.Area;
import graficos.Punto;
import mapa.MapaInfo;
import model2.Arista;
import model2.Nodo;

public class CmcDemo2 {
	private MapaInfo mapa;
	private CmcImple cmc;
	private Set<Punto> puntosCriticos = new HashSet<>();
	private Set<Arista> aristas = new HashSet<>();
	private Set<Area> areas = new HashSet<>();
	private Set<Nodo> grafo = new HashSet<>();

	public CmcDemo2(MapaInfo mapa, CmcImple cmc) {
		this.mapa = mapa;
		this.cmc = cmc;
		mostarColeccionDeAreas();
		procesarAreas();
		popularGrafo();
		mostarColeccionDePuntos();
		try {
			generarAristasIniciales();
		} catch (Exception e) {
			e.printStackTrace();
		}
		obtenerCaminos();
		dibujarAristas();
	}

	private void obtenerCaminos() {
		for (int i = 0; i < mapa.getPuntos().size(); i++) {
			for (int j = i+1; j < mapa.getPuntos().size(); j++) {
				
			}
		}
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

	private void dibujarAristas() {
		for (Arista arista : aristas) {
			List<Punto> lista = arista.puntos;
			System.out.println("arista: "+arista.origen.x+" "+arista.origen.y+" "+arista.destino.x+" "+arista.destino.y+" "+"costo: "+arista.costo);
			cmc.dibujarCamino(lista);
		}
		
	}

	private void popularGrafo() {
		for (Punto punto : mapa.getPuntos()) {
			Nodo nodo = new Nodo(punto);
			grafo.add(nodo);
		}
		Iterator<Area> iterador = areas.iterator();
		while(iterador.hasNext()) {
			Area area = iterador.next();
			Rectangle rec =area.getRectangle();
			int maxY = (int) ((rec.getMaxY()+1 > 600)?600:rec.getMaxY()+1);
			int maxX = (int) ((rec.getMaxX()+1 > 800)?800:rec.getMaxX()+1);
			int minX = (rec.x-1 < 0)?0:rec.x-1;
			int minY = (rec.y-1 < 0)?0:rec.y-1;
//			grafo.add(new Nodo(new Punto(minX,minY)));
//			grafo.add(new Nodo(new Punto(minX,maxY)));
//			grafo.add(new Nodo(new Punto(maxX,minY)));
//			grafo.add(new Nodo(new Punto(maxX,maxY)));
			puntosCriticos.add(new Punto(minX,minY));
			puntosCriticos.add(new Punto(minX,maxY));
			puntosCriticos.add(new Punto(maxX,minY));
			puntosCriticos.add(new Punto(maxX,maxY));
		}
	}
	
	private void generarAristasIniciales() throws Exception {
//		Iterator<Nodo> it = grafo.iterator();
//		while (it.hasNext()) {
//			Nodo nodo = (Nodo) it.next();
//			Iterator<Nodo> it2 = grafo.iterator();
//			while (it2.hasNext()) {
//				Nodo nodo2 = (Nodo) it2.next();
//				if(!nodo.equals(nodo2)){
//					Arista arista = generarArista(nodo.punto, nodo2.punto);
//					if (arista != null)
//						nodo.aristas.add(arista);
//						nodo2.aristas.add(arista);
//				}
//			}
//			
//		}
		List<Punto> listaPuntos = new ArrayList<>();
		listaPuntos.addAll(mapa.getPuntos());
		listaPuntos.addAll(puntosCriticos);
		for (Punto puntoA : listaPuntos) {
			for (Punto puntoB : listaPuntos) {
				if (!puntoA.igual(puntoB)) {
					Arista arista = generarArista(puntoA, puntoB);
					if (arista != null)
						aristas.add(arista);
				}
			}
		}
	}
	private Arista generarArista(Punto puntoA, Punto puntoB) {
		return new Arista(puntoA, puntoB, mapa);
	}

	private void demoObtenerCamino() {
		Punto a = null, b = null;
		Iterator<Punto> iter = mapa.getPuntos().iterator();
		if (iter.hasNext()) {
			a = iter.next();

			while (iter.hasNext()) {
				b = iter.next();
				expandirPuntosContiguos(a, b);
				a = b;
			}
			// expandirPuntosContiguos(a, mapa.getPuntos().get(0));
			mapa.enviarMensaje("Camino uniendo " + mapa.getPuntos().size() + " puntos");
		}
	}

	private void expandirPuntosContiguos(Punto a, Punto b) {
		List<Punto> listaPuntos = new ArrayList<Punto>();
		if (a.x < b.x) {
			for (int x = a.x; x < b.x; x++) {
				listaPuntos.add(new Punto(x, a.y));
			}
		} else {
			for (int x = a.x; x > b.x; x--) {
				listaPuntos.add(new Punto(x, a.y));
			}
		}
		if (a.y < b.y) {
			for (int y = a.y; y < b.y; y++) {
				listaPuntos.add(new Punto(b.x, y));
			}
		} else {
			for (int y = a.y; y > b.y; y--) {
				listaPuntos.add(new Punto(b.x, y));
			}
		}
		cmc.dibujarCamino(listaPuntos);
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
