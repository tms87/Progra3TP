
package cmc;

import java.awt.Rectangle;
/**
 * Obtiene la lista de los puntos marcados en la matriz (mapa)
 * Itera los mismos de la siguiente forma:
 * Obtiene los 2 primeros y expande los contiguos entre ambos.
 * Primero expande eje x, segundo expande el eje y.
 * Reitera la lista expandiendo el siguiente (siempre expandiendo de a pares)
 * El recorrido es secuencial (conforme al orden de marcado de los puntos en el mapa)
 * Invoca la m�todo dibujar en cada iteraci�n.
 * Al finalizar la iteraci�n expande los contiguos entre el �ltimo y el primero de la lista.
 * Vuelve a Invocar la m�todo dibujar para cerrar el ciclo.
 * No contempla las densidades definidas en la matriz (mapa)
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
//		mostarColeccionDeAreas();
		procesarAreas();
		popularGrafo();
//		mostarColeccionDePuntos();
		try {
			generarAristasIniciales();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("aristas generadas!");
//		dibujarAristas();
		obtenerCaminos();
	}
	
	class ComparadorCostos implements Comparator<Arista> {
	 	@Override
		public int compare(Arista o1, Arista o2) {
	 		return (int) (o1.costo - o2.costo);
		}
	}
	
	private List<Arista> prim(List<Arista> aristas){
		List<Arista> aristasResult = new ArrayList<Arista>();
		List<Punto> vertices = mapa.getPuntos();
		int cantVer = vertices.size()-1;
		Punto verTmp = vertices.get(0);
		vertices.remove(verTmp);
		while(cantVer > 0){
			for(int i=0; i< aristas.size(); i++){
				if (verTmp == aristas.get(i).origen){
					aristasResult.add(aristas.get(i));
					break;
				}
			}
			verTmp = vertices.get(0);
			vertices.remove(verTmp);
			cantVer--;
		}
		
		System.out.println(aristasResult.size());
		
		return aristasResult;
	}

	private void obtenerCaminos() {
		List<Arista> aristasResult = new ArrayList<>();
		for (int i = 0; i < mapa.getPuntos().size(); i++) {
			for (int j = i+1; j < mapa.getPuntos().size(); j++) {
				Camino camino = getCamino(mapa.getPuntos().get(i),mapa.getPuntos().get(j));
				if (camino != null){
					List<Arista> aristas = new ArrayList<>();
					for(int k=0; k< camino.getAristas().size(); k++)
						aristas.add(camino.getAristas().get(k));
					Punto origen = mapa.getPuntos().get(i);
					Punto destino = mapa.getPuntos().get(j);
					Arista aristaResult = new Arista(origen,destino);
					for(int k=0; k< aristas.size(); k++){
						for(int l=0; l< aristas.get(k).getPuntos().size(); l++){
							aristaResult.addPunto(aristas.get(k).getPuntos().get(l));
							
						}
						aristaResult.sumarCosto(aristas.get(k).getCosto());
					}
					aristasResult.add(aristaResult);
				}
				
			}
		}
		
		Collections.sort(aristasResult, new ComparadorCostos());
		
		for(int i=0; i < aristasResult.size(); i++)
			System.out.println(aristasResult.get(i).costo);
		
		List<Arista> caminoDef = this.prim(aristasResult);
		
		for (Arista arista : caminoDef) {
			cmc.dibujarCamino(arista.puntos);
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
			int maxY = (int) ((rec.getMaxY()+1 > 600)?600:rec.getMaxY()+1);
			int maxX = (int) ((rec.getMaxX()+1 > 800)?800:rec.getMaxX()+1);
			int minX = (rec.x-1 < 0)?0:rec.x-1;
			int minY = (rec.y-1 < 0)?0:rec.y-1;
			grafo.nodos.add(new Nodo(new Punto(minX,minY)));
			System.out.println("x: "+minX+" y: "+minY);
			grafo.nodos.add(new Nodo(new Punto(minX,maxY)));
			System.out.println("x: "+minX+" y: "+maxY);
			grafo.nodos.add(new Nodo(new Punto(maxX,minY)));
			System.out.println("x: "+maxX+" y: "+minY);
			grafo.nodos.add(new Nodo(new Punto(maxX,maxY)));
			System.out.println("x: "+maxX+" y: "+maxY);
		}
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

//	/** consulta clase MapaInfo */
//	private void mostarColeccionDeAreas() {
//		System.out.println("Mapa: " + MapaInfo.LARGO + " x " + MapaInfo.ALTO);
//		for (Area a : mapa.getAreas()) {
//			System.out.println(a);
//		}
//	}
//
//	/** consulta clase MapaInfo */
//	private void mostarColeccionDePuntos() {
//		for (Punto c : mapa.getPuntos()) {
//			int densidad = mapa.getDensidad(c);
//			System.out.println(c + " D+: " + densidad);
//		}
//	}
}
