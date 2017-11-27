
package cmc;
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
import java.util.Iterator;
import java.util.List;

import utils.ExampleFactory;
import utils.ExampleNode;
import utils.Map;
import graficos.Area;
import graficos.Punto;
import mapa.MapaInfo;

public class CmcDemoJuan {
	private MapaInfo mapa;
	private CmcImple cmc;
	
	public CmcDemoJuan(MapaInfo mapa, CmcImple cmc) {
		this.mapa = mapa;
		this.cmc = cmc;
		mostarColeccionDeAreas();
		mostarColeccionDePuntos();
		demoObtenerCamino();
	}
	
	private void demoObtenerCamino() {
		Punto a = null, b = null;	
		Iterator<Punto> iter = mapa.getPuntos().iterator();
		if (iter.hasNext()) {
			a = iter.next();
		
			while(iter.hasNext()) {
				b = iter.next();
				expandirPuntosContiguos(a, b);
				a = b;
			}
			//expandirPuntosContiguos(a, mapa.getPuntos().get(0));
			mapa.enviarMensaje("Camino uniendo " + mapa.getPuntos().size() + " puntos");
		}
	}
	
	private void expandirPuntosContiguos(Punto a, Punto b) {
		List<Punto> listaPuntos = new ArrayList<Punto>();
		Map<ExampleNode> myMap = new Map<ExampleNode>(800, 600, new ExampleFactory(),this.mapa);
        List<ExampleNode> path = myMap.findPath(a.x, a.y, b.x, b.y);
		for(int i=0; i < path.size(); i++){
			listaPuntos.add(new Punto(path.get(i).getxPosition(), path.get(i).getyPosition()));
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
