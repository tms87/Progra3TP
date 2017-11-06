
package cmc;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import graficos.Area;
import graficos.Punto;
import mapa.MapaInfo;
import model.PuntoKey;
import model.VerticesArea;
import utils.AreaUtils;

public class CmcMiDemo {
	private MapaInfo mapa;
	private CmcImple cmc;
	private Map<Punto[],Double> mapDistancia = new HashMap<>();
	private List<graficos.Punto> disponibles;
	private Map<PuntoKey[],Punto[]> puntosIntermedios = new HashMap<>();
	private Set<Punto> usados = new HashSet<>();
	
	public CmcMiDemo(MapaInfo mapa, CmcImple cmc) {
		this.mapa = mapa;
		this.cmc = cmc;
		mostarColeccionDeAreas();
		mostarColeccionDePuntos();
		disponibles = mapa.getPuntos();
		popularMapDistancia();
		popularMapPuntosIntermedios();
		obtenerCamino();
	}
	
	private void obtenerCamino() {
		Punto[] puntos = seleccionarPuntos();
		Punto[] pIntermedios = puntosIntermedios.get(puntos);
	}

	private Punto[] seleccionarPuntos() {
		Double dist = Double.MAX_VALUE;
		Punto[] ret = null;
		for (Punto[] key : mapDistancia.keySet()) {
			if (dist.compareTo(mapDistancia.get(key))> 0){
				dist = mapDistancia.get(key);
				ret = key;
			}
		}
		mapDistancia.remove(ret);
		return ret;
	}

	private void popularMapPuntosIntermedios() {
		List<Area> areas = mapa.getAreas();
		for (int i = 0; i < disponibles.size(); i++) {
			for (int j = i+1; j < disponibles.size(); j++) {
				boolean isInside = false; 
				for (Area area : areas) {
					Area a = new Area(disponibles.get(i), disponibles.get(j), 0);
					isInside = AreaUtils.isAinsideB(a, area);
					if(isInside){
						puntosIntermedios.put(new PuntoKey[]{new PuntoKey(disponibles.get(i)),new PuntoKey(disponibles.get(j))},generarPuntosIntermedios(area));
					}
				}
			}
		}
	}

	private Punto[] generarPuntosIntermedios(Area area) {
		VerticesArea vArea = new VerticesArea(area);
		Punto id = vArea.getInferiorDerecho();
		id.x++;
		id.y++;
		Punto iz = vArea.getInferiorIzquierdo();
		id.x--;
		id.y++;
		Punto sz = vArea.getSuperiorIzquierdo();
		id.x--;
		id.y--;
		Punto sd = vArea.getSuperiorDerecho();
		id.x++;
		id.y--;
		return new Punto[]{sz,sd,iz,id};
	}

	/**
	 * Llena el mapDistancia con todos los puntos iniciales
	 */
	private void popularMapDistancia() {
		for (Punto puntoA : disponibles) {
			for (Punto puntoB : disponibles) {
				if (!puntoA.igual(puntoB)){
					double dif =Math.sqrt((Math.abs(puntoA.x - puntoB.x)^2 * Math.abs(puntoA.y - puntoB.y)^2));
					mapDistancia.put(new Punto[]{puntoA,puntoB},dif);
				}
			}
		}
	}
	
	private void expandirPuntosContiguos(Punto a, Punto b) {
		List<Punto> listaPuntos = new ArrayList<Punto>();
		if (a.x < b.x) {
			for(int x = a.x ; x < b.x; x++) {
				listaPuntos.add(new Punto(x, a.y));
			}
		} else {
			for(int x = a.x ; x > b.x; x--) {
				listaPuntos.add(new Punto(x, a.y));
			}
		}
		if (a.y < b.y) {
			for(int y = a.y ; y < b.y; y++) {
				listaPuntos.add(new Punto(b.x, y));
			}
		} else {
			for(int y = a.y ; y > b.y; y--) {
				listaPuntos.add(new Punto(b.x, y));
			}
		}
		cmc.dibujarCamino(listaPuntos);
	}

	/** consulta clase MapaInfo */
	private void mostarColeccionDeAreas() {
		System.out.println("Mapa: " + MapaInfo.LARGO + " x " + MapaInfo.ALTO);
		for(Area a : mapa.getAreas()) {
			System.out.println(a);
		}
	}
	
	/** consulta clase MapaInfo */
	private void mostarColeccionDePuntos() {
		for(Punto c : mapa.getPuntos()) {
			int densidad = mapa.getDensidad(c);
			System.out.println(c + " D+: " + densidad);
		}
	}

}
