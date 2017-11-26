
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
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import enums.Direccion;
import graficos.Area;
import graficos.Punto;
import mapa.MapaInfo;
import model.Arista;
import model.AristaAbstracta;
import model.AristaDirecta;
import model.Camino;
import model.VerticesArea;
import utils.AreaUtils;
import utils.MathUtils;
import utils.PuntoUtils;

public class CmcMiDemo {
	private MapaInfo mapa;
	private CmcImple cmc;
	private List<graficos.Punto> puntosDisponibles;
	private LinkedList<Arista> listaAristas = new LinkedList<>();
	private Set<Punto> usados = new HashSet<>();

	public CmcMiDemo(MapaInfo mapa, CmcImple cmc) throws Exception {
		this.mapa = mapa;
		this.cmc = cmc;
		mostarColeccionDeAreas();
		mostarColeccionDePuntos();
		puntosDisponibles = mapa.getPuntos();
		generarAristas(puntosDisponibles);
		Collections.sort(listaAristas);
		// while (!puntosDisponibles.isEmpty()){
		Arista arista = listaAristas.getFirst();
		Camino camino = arista.calcularCamino(null);
		cmc.dibujarCamino(camino.puntos);
		// }
	}

	private Punto[] getPuntosDirectos(Area area, Punto punto, Punto punto2) {
		Direccion dire = PuntoUtils.direccionRelativa(punto, punto2);
		Punto[] ret = new Punto[2];
		switch (dire) {
		case ABAJO:
			ret[0] = new Punto(punto.x, area.getCoordenadas()[1] - 1);
			ret[1] = new Punto(punto.x, area.getCoordenadas()[1] + area.getCoordenadas()[3] + 1);
			break;
		case ARRIBA:
			ret[1] = new Punto(punto.x, area.getCoordenadas()[1] + 1);
			ret[0] = new Punto(punto.x, area.getCoordenadas()[1] + area.getCoordenadas()[3] - 1);
			break;
		case DERECHA:
			ret[0] = new Punto(area.getCoordenadas()[0] - 1, punto.y);
			ret[1] = new Punto(area.getCoordenadas()[0] + area.getCoordenadas()[2] + 1, punto.y);
			break;
		case IZQUIERDA:
			ret[1] = new Punto(area.getCoordenadas()[0] + 1, punto.y);
			ret[0] = new Punto(area.getCoordenadas()[0] + area.getCoordenadas()[2] - 1, punto.y);
			break;
		default:
			int x = Math.abs(punto.x - punto2.x);
			int y = Math.abs(punto.y - punto2.y);
			double m = (double) y / x;
			switch (dire) {
			// FIXME las correcciones de coordenadas (+1 -1) no son precisas
			case D_ABAJO:
				ret[0] = new Punto(area.getCoordenadas()[0] - 1,
						(int) (-m * (area.getCoordenadas()[0] - punto.x) + punto.y) - 1);
				ret[1] = new Punto(area.getCoordenadas()[0] + area.getCoordenadas()[2] + 1,
						(int) (m * (area.getCoordenadas()[0] + area.getCoordenadas()[2] - punto.x) + punto.y) + 1);
				break;
			case D_ARRIBA:
				ret[0] = new Punto(area.getCoordenadas()[0] - 1,
						(int) (-m * (area.getCoordenadas()[0] - punto.x) + punto.y) + 1);
				ret[1] = new Punto(area.getCoordenadas()[0] + area.getCoordenadas()[2] + 1,
						(int) (punto.y - m * (area.getCoordenadas()[0] + area.getCoordenadas()[2] - punto.x)) - 1);
				break;
			case I_ABAJO:
				ret[0] = new Punto(area.getCoordenadas()[0] + area.getCoordenadas()[2] + 1,
						(int) (m * (area.getCoordenadas()[0] + area.getCoordenadas()[2] - punto.x) + punto.y) - 1);
				ret[1] = new Punto(area.getCoordenadas()[0] - 1,
						(int) (m * (area.getCoordenadas()[0] - punto.x) + punto.y) + 1);
				break;
			case I_ARRIBA:
				ret[0] = new Punto(area.getCoordenadas()[0] + area.getCoordenadas()[2] + 1,
						(int) (m * (area.getCoordenadas()[0] + area.getCoordenadas()[2] - punto.x) + punto.y) + 1);
				ret[1] = new Punto(area.getCoordenadas()[0] - 1,
						(int) (punto.y - m * (area.getCoordenadas()[0] - punto.x)) - 1);
				break;
			default:
				break;
			}
		}
		return ret;
	}

	private void generarAristas(List<Punto> listaPuntos) throws Exception {
		for (Punto puntoA : listaPuntos) {
			for (Punto puntoB : listaPuntos) {
				if (!puntoA.igual(puntoB)) {
					Arista arista = generarArista(puntoA, puntoB);
					listaAristas.add(arista);
				}
			}
		}
	}

	private Arista generarArista(Punto puntoA, Punto puntoB) throws Exception {
		return generarArista(puntoA, puntoB, new ArrayList<Area>());
	}

	private Arista generarArista(Punto puntoA, Punto puntoB, List<Area> areasTomadas) throws Exception {
		List<Area> areas = mapa.getAreas();
		Arista arista = null;
//		List<Punto> 
		for (Area area : areas) {
			Area a = new Area(puntoA, puntoB, 0);
			boolean isInside = AreaUtils.isAinsideB(a, area);
			if (isInside) {
				if (AreaUtils.isIn(area, areasTomadas)) {
					break;
				}
				if (arista == null) {
					double dif = MathUtils.getHipotenusa(puntoA.x, puntoB.x, puntoA.y, puntoB.y);
					arista = new AristaAbstracta(puntoA, puntoB, dif);
				}
				areasTomadas.add(area);
			}
			generarAristasIntermedias(area, (AristaAbstracta) arista, areasTomadas);//movido aca porque si
		}
		if (arista == null) {
			Double costo = (mapa.getDensidad(puntoA)+1)*MathUtils.getHipotenusa(puntoA.x,puntoA.y,puntoB.x,puntoB.y);
			arista = new AristaDirecta(puntoA, puntoB,costo);
		}
		return arista;
	}

	private void generarAristasIntermedias(Area area, AristaAbstracta arista, List<Area> areasTomadas) throws Exception {
		VerticesArea vArea = new VerticesArea(area);
		Punto id = vArea.getInferiorDerecho();
		id.x++;
		id.y++;
		System.out.println("Inferior Derecho: "+id);
		Punto ii = vArea.getInferiorIzquierdo();
		ii.x--;
		ii.y++;
		System.out.println("Inferior Izquierdo: "+ii);
		Punto si = vArea.getSuperiorIzquierdo();
		si.x--;
		si.y--;
		System.out.println("Superior Izquierdo: "+si);
		Punto sd = vArea.getSuperiorDerecho();
		sd.x++;
		sd.y--;
		System.out.println("Superior Derecho: "+sd);
		Punto[] directos = getPuntosDirectos(area, arista.origen, arista.destino);
		System.out.println("Directo 0: "+directos[0]);
		System.out.println("Directo 1: "+directos[1]);
		Direccion direccion = PuntoUtils.direccionRelativa(si, directos[0]);
		List<Arista> v1 = new ArrayList<>();
		List<Arista> v2 = new ArrayList<>();
		List<Arista> v1_2 = new ArrayList<>();

		Arista a1 = generarArista(arista.origen, si,areasTomadas);
		if (a1 != null)
			v1.add(a1);
		Arista a2 = generarArista(arista.origen, directos[0],areasTomadas);
		if (a2 != null)
			v1.add(a2);
		Arista a3 = generarArista(arista.origen, ii,areasTomadas);
		Arista a4 = generarArista(sd,arista.destino,areasTomadas);
		switch (direccion) {
		case ABAJO:
			if (a3 != null)
				v1.add(a3);
			if (a4 != null)
				v2.add(a4);
			Arista a7 = generarArista(si, sd,areasTomadas);
			if (a7 != null)
				v1_2.add(a7);
			Arista a8 = generarArista(ii, id,areasTomadas);
			if (a8 != null)
				v1_2.add(a8);
			break;
		case DERECHA:
			if (a3 != null)
				v2.add(a3);
			if (a4 != null)
				v1.add(a4);
			Arista a9 = generarArista(si, ii,areasTomadas);
			if (a9 != null)
				v1_2.add(a9);
			Arista a10 = generarArista(sd, id,areasTomadas);
			if (a10 != null)
				v1_2.add(a10);
			break;
		default:
			throw new Exception("puntos directos mal ubicados");
		}
		Arista a5 = generarArista(directos[1],arista.destino,areasTomadas);
		if (a5 != null)
			v2.add(a5);
		Arista a6 = generarArista(id,arista.destino,areasTomadas);
		if (a6 != null)
			v2.add(a6);
		Arista a11 = generarArista(directos[0], directos[1],areasTomadas);
		if (a11 != null)
			v1_2.add(a11);
		arista.grafo.v1 = v1;
		arista.grafo.v2 = v2;
		arista.grafo.v1_2 = v1_2;
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