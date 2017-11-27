package model2;

import java.util.ArrayList;
import java.util.List;

import graficos.Punto;
import mapa.MapaInfo;

public class Arista {

	public List<Punto> puntos;
	public Punto origen;
	public Punto destino;
	public Double costo = 0D;
	
	public Arista(Punto origen, Punto destino, MapaInfo mapa) {
		this.origen = origen;
		this.destino = destino;
		this.puntos = setPuntos(origen.x, origen.y, destino.x, destino.y, mapa);
	}
	
	 public List<Punto> setPuntos(int x1, int y1, int x2, int y2, MapaInfo mapa) {
	        int d = 0;
	 
	        int dx = Math.abs(x2 - x1);
	        int dy = Math.abs(y2 - y1);
	 
	        int dx2 = 2 * dx;
	        int dy2 = 2 * dy;
	 
	        int ix = x1 < x2 ? 1 : -1;
	        int iy = y1 < y2 ? 1 : -1;
	 
	        int x = x1;
	        int y = y1;
	        List<Punto> lista = new ArrayList<>();
	        if (dx >= dy) {
	            while (true) {
	            	Punto tmp =new Punto(x,y);
	            	int densidad = mapa.getDensidad(tmp);
	            	if(densidad == 4){
	            		lista.clear();
	            		costo = Double.MAX_VALUE;
	            		break;
	            	}
	            	lista.add(tmp);
	            	costo += mapa.getDensidad(tmp)+1;
	                if (x == x2)
	                    break;
	                x += ix;
	                d += dy2;
	                if (d > dx) {
	                    y += iy;
	                    d -= dx2;
	                }
	            }
	        } else {
	            while (true) {
	            	Punto tmp =new Punto(x,y);
	            	int densidad = mapa.getDensidad(tmp);
	            	if(densidad == 4){
	            		lista.clear();
	            		costo = Double.MAX_VALUE;
	            		break;
	            	}
	            	lista.add(tmp);
	            	costo += mapa.getDensidad(tmp)+1;
	                if (y == y2)
	                    break;
	                y += iy;
	                d += dx2;
	                if (d > dy) {
	                    x += ix;
	                    d -= dy2;
	                }
	            }
	        }
			return lista;
	    }
}
