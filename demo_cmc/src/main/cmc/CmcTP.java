
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
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
	private static final int PRECISION = 30;
	private MapaInfo mapa;
	private CmcImple cmc;
	private Set<Area> areas = new HashSet<>();
	private Grafo grafo = new Grafo();
	List<Punto> puntosTest = new ArrayList<>();
	List<Punto> vertices = new ArrayList<>();
	
	public CmcTP(MapaInfo mapa, CmcImple cmc) {
		this.mapa = mapa;
		this.cmc = cmc;
		procesarAreas();
		vertices.addAll(mapa.getPuntos());
		System.out.println("Precision: " + PRECISION);
		popularGrafo();
		try {
			generarAristasIniciales();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// dibujarAristas();
		obtenerCaminos();
	}

	private void obtenerCaminos() {
		LinkedList<Camino> caminos = new LinkedList<>();
		for (int i = 0; i < mapa.getPuntos().size(); i++) {
			for (int j = i + 1; j < mapa.getPuntos().size(); j++) {
				Camino camino = getCamino(mapa.getPuntos().get(i), mapa.getPuntos().get(j));
				if (camino != null)
					caminos.add(camino);
			}
		}
		Collections.sort(caminos);
		List<Camino> solucion = recorrido(caminos);
		dibujarCaminos(solucion);
	}

	private void dibujarCaminos(List<Camino> solucion) {
		for (Camino camino : solucion) {
			dibujarAristas(camino.aristas);
		}
	}

	private void dibujarAristas(List<Arista> aristas) {
		for (Arista arista : aristas) {
			cmc.dibujarCamino(arista.puntos);
		}
	}

	private List<Camino> recorrido(LinkedList<Camino> caminos) {
		List<Camino> recorrido = new ArrayList<>();
//		List<Punto> vertices = mapa.getPuntos();
		Punto punto = vertices.get(0);
		while (!(vertices.size() == 1)) {
			//while(!vertices.isEmpty()){ //Para cerrar el camino
			for (Camino camino : caminos) {
				if (camino.origen == punto) {
					if (vertices.contains(camino.destino)){
						recorrido.add(camino);
						vertices.remove(punto);
						caminos.remove(camino);
						punto = camino.destino;
						break;
					}
				} else if (camino.destino == punto) {
					if (vertices.contains(camino.origen)){
						recorrido.add(camino);
						vertices.remove(punto);
						caminos.remove(camino);
						punto = camino.origen;
						break;
					}
				}
			}
		}
		return recorrido;
	}

	@SuppressWarnings("unused")
	private List<Camino> kruskal(LinkedList<Camino> caminos) {
		System.out.println("empieza kruskal");
		List<Punto> vertices = mapa.getPuntos();
		List<List<Punto>> conjuntos = new ArrayList<>();
		for (Punto punto : vertices) {
			List<Punto> list = new ArrayList<>();
			list.add(punto);
			conjuntos.add(list);
		}
		List<Camino> solucion = new ArrayList<>();
		aristas: while (!(conjuntos.size() == 1)) {
			Camino camino = caminos.getFirst();
			for (List<Punto> conjunto : conjuntos) {
				System.out.println("reviso camino");
				if (conjunto.contains(camino.origen)) {
					if (conjunto.contains(camino.destino)) {
						// descarto el camino
						caminos.removeFirst();
						continue aristas;
					} else {
						// solo tiene el origen
						for (List<Punto> tmp : conjuntos) {
							if (tmp.contains(camino.destino)) {
								conjuntos.remove(tmp);
								conjunto.addAll(tmp);
								solucion.add(camino);
								continue aristas;
							}
						}
					}
				} else {
					if (conjunto.contains(camino.destino)) {
						// solo tiene el destino
						for (List<Punto> tmp : conjuntos) {
							if (tmp.contains(camino.origen)) {
								conjuntos.remove(tmp);
								conjunto.addAll(tmp);
								solucion.add(camino);
								continue aristas;
							}
						}
					}
				}
			}
		}
		return solucion;
	}

	private Camino getCamino(Punto origen, Punto destino) {
		Nodo nDest = grafo.getNodo(destino);
		Grafo grafoParcial = new Grafo();
		Nodo newDest = new Nodo(destino);
		newDest.costoMinimoAcumulado = 0D;
		grafoParcial.nodos.add(newDest);
		List<Nodo> nodosActivos = new ArrayList<>();
		for (Arista arista : nDest.aristas) {
			Punto punto = (arista.origen != destino) ? arista.origen : arista.destino;
			Nodo nodo = new Nodo(punto);
			nodo.aristas.add(arista);
			nodo.costoMinimoAcumulado = arista.costo;
			nodo.antecesor = newDest;
			nodosActivos.add(nodo);
			grafoParcial.nodos.add(nodo);
		}
		buscarCamino(nodosActivos, grafoParcial, origen);
		Camino camino = grafoParcial.generarCamino(grafoParcial.getNodo(origen));
		if (camino != null)
			return camino;
		else
			vertices.remove(origen);
			return null;
	}

	private void buscarCamino(List<Nodo> nodosActivos, Grafo grafoParcial, Punto destino) {
		List<Nodo> nuevosActivos = new ArrayList<>();
		for (Nodo nodo : nodosActivos) {
			Nodo nodoGrafo = grafo.getNodo(nodo.punto);
			for (Arista arista : nodoGrafo.aristas) {
				Punto punto = (arista.origen != nodo.punto) ? arista.origen : arista.destino;
				if (punto.igual(nodo.antecesor.punto)) {
					continue;
				}
				double costo = nodo.costoMinimoAcumulado + arista.costo;
				if (costo < grafoParcial.costoSolucion) {
					Nodo tmp = grafoParcial.getNodo(punto);
					if (tmp != null) {
						if (tmp.costoMinimoAcumulado > costo) {
							tmp.costoMinimoAcumulado = costo;
							tmp.removerArista(tmp.antecesor.punto);
							tmp.antecesor = nodo;
							tmp.aristas.add(arista);
							if (!nuevosActivos.contains(tmp) && !nuevosActivos.contains(tmp)
									&& !tmp.punto.igual(destino))
								nuevosActivos.add(tmp);
						}
					} else {
						tmp = new Nodo(punto);
						tmp.aristas.add(arista);
						tmp.costoMinimoAcumulado = costo;
						tmp.antecesor = nodo;
						grafoParcial.nodos.add(tmp);
						if (!tmp.punto.igual(destino)) {
							nuevosActivos.add(tmp);
						}
					}
				}
			}
		}
		if (!nuevosActivos.isEmpty())
			buscarCamino(nuevosActivos, grafoParcial, destino);
	}

	private void procesarAreas() {
		areas.addAll(mapa.getAreas());
		for (int i = 0; i < mapa.getAreas().size(); i++) {
			for (int j = i + 1; j < mapa.getAreas().size(); j++) {
				Rectangle recI = mapa.getAreas().get(i).getRectangle();
				Rectangle recJ = mapa.getAreas().get(j).getRectangle();
				Rectangle newRec = recI.intersection(recJ);
				if (!newRec.isEmpty()) {
					Punto centro = new Punto((int) newRec.getCenterX(), (int) newRec.getCenterY());
					areas.add(new Area(new Punto(newRec.x, newRec.y),
							new Punto((int) newRec.getMaxX(), (int) newRec.getMaxY()), mapa.getDensidad(centro)));
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
		while (iterador.hasNext()) {
			Area area = iterador.next();
			Rectangle rec = area.getRectangle();
			for (int i = 0; i < rec.width - 1; i = i + PRECISION) {
				agregarNodo(rec.x + i, rec.y, true, true);
			}
			for (int i = 0; i < rec.height - 1; i = i + PRECISION) {
				agregarNodo(rec.x, (int) rec.getMaxY() - 1 - i, true, false);
			}
			for (int i = 0; i < rec.width - 1; i = i + PRECISION) {
				agregarNodo((int) rec.getMaxX() - 1 - i, (int) rec.getMaxY() - 1, false, false);
			}
			for (int i = 0; i < rec.height - 1; i = i + PRECISION) {
				agregarNodo((int) rec.getMaxX() - 1, rec.y + i, false, true);
			}
			/** Para agilizar el algoritmo **/
//			 agregarNodo(rec.x,rec.y,true,true);
//			 agregarNodo(rec.x,(int)rec.getMaxY()-1,true,false);
//			 agregarNodo((int)rec.getMaxX()-1,(int)rec.getMaxY()-1,false,false);
//			 agregarNodo((int)rec.getMaxX()-1,rec.y,false,true);
		}
	}

	private void agregarNodo(int x, int y, boolean upX, boolean upY) {
		int nodoX = 0, nodoY = 0;
		int color = mapa.getDensidad(x, y);
		boolean xBorde0 = false, xBorde800 = false, yBorde0 = false, yBorde600 = false;
		if (upX) {
			if (x > 0)
				nodoX = x - 1;
			else
				xBorde0 = true;
		} else {
			if (x < 800)
				nodoX = x + 1;
			else
				xBorde800 = true;
		}
		if (upY) {
			if (y > 0)
				nodoY = y - 1;
			else
				yBorde0 = true;
		} else {
			if (y < 600)
				nodoY = y + 1;
			else
				yBorde600 = true;
		}
		if (!((!xBorde800 && mapa.getDensidad(x + 1, y) == color) && (!xBorde0 && mapa.getDensidad(x - 1, y) == color)
				&& (!yBorde600 && mapa.getDensidad(x, y + 1) == color)
				&& (!yBorde0 && mapa.getDensidad(x, y - 1) == color)
				&& (!xBorde800 && !yBorde600 && mapa.getDensidad(x + 1, y + 1) == color)
				&& (!xBorde0 && !yBorde0 && mapa.getDensidad(x - 1, y - 1) == color)
				&& (!xBorde0 && !yBorde600 && mapa.getDensidad(x - 1, y + 1) == color)
				&& (!xBorde800 && !yBorde0 && mapa.getDensidad(x + 1, y - 1) == color)))
			grafo.nodos.add(new Nodo(new Punto(x, y)));
		if (nodoX != 0 && nodoY != 0)
			if (!(mapa.getDensidad(nodoX + 1, nodoY) == color && mapa.getDensidad(nodoX - 1, nodoY) == color
					&& mapa.getDensidad(nodoX, nodoY + 1) == color && mapa.getDensidad(nodoX, nodoY - 1) == color
					&& mapa.getDensidad(nodoX + 1, nodoY + 1) == color
					&& mapa.getDensidad(nodoX - 1, nodoY - 1) == color
					&& mapa.getDensidad(nodoX - 1, nodoY + 1) == color
					&& mapa.getDensidad(nodoX + 1, nodoY - 1) == color))
				grafo.nodos.add(new Nodo(new Punto(nodoX, nodoY)));
	}

	private void generarAristasIniciales() throws Exception {
		Iterator<Nodo> it = grafo.nodos.iterator();
		while (it.hasNext()) {
			Nodo nodo = (Nodo) it.next();
			Iterator<Nodo> it2 = grafo.nodos.iterator();
			while (it2.hasNext()) {
				Nodo nodo2 = (Nodo) it2.next();
				if (!nodo.equals(nodo2)) {
					if (!nodo.existeArista(nodo.punto, nodo2.punto)) {
						Arista arista = generarArista(nodo.punto, nodo2.punto);
						if (arista != null && arista.costo < Double.MAX_VALUE) {
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

	// Test
	@SuppressWarnings("unused")
	private void dibujarAristas() {
		Iterator<Nodo> it = grafo.nodos.iterator();
		while (it.hasNext()) {
			Nodo nodo = (Nodo) it.next();
			for (Arista arista : nodo.aristas) {
				List<Punto> lista = arista.puntos;
				System.out.println("arista: " + arista.origen.x + " " + arista.origen.y + " " + arista.destino.x + " "
						+ arista.destino.y + " " + "costo: " + arista.costo);
				cmc.dibujarCamino(lista);
			}
		}
	}

	/** consulta clase MapaInfo */
	@SuppressWarnings("unused")
	private void mostarColeccionDeAreas() {
		System.out.println("Mapa: " + MapaInfo.LARGO + " x " + MapaInfo.ALTO);
		for (Area a : mapa.getAreas()) {
			System.out.println(a);
		}
	}

	/** consulta clase MapaInfo */
	@SuppressWarnings("unused")
	private void mostarColeccionDePuntos() {
		for (Punto c : mapa.getPuntos()) {
			int densidad = mapa.getDensidad(c);
			System.out.println(c + " D+: " + densidad);
		}
	}

}
