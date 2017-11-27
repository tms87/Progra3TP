package model2;

import java.util.ArrayList;
import java.util.List;

import graficos.Punto;

public class Nodo {

	public Punto punto;
	public List<Arista> aristas = new ArrayList<>();
	public Double costoMinimoAcumulado;
	public Nodo antecesor;

	public Nodo(Punto punto) {
		this.punto = punto;
	}
	
//	@Override
//	public boolean equals(Object obj) {
//		return this.punto.equals(((Nodo)obj).punto);
//	}
//	
//	@Override
//	public int hashCode() {
//		this.punto.hashCode();
//		return super.hashCode();
//	}
	
	public void removerArista(Punto punto){
		for (Arista arista : aristas) {
			if (arista.origen.igual(punto) || arista.destino.igual(punto)){
				aristas.remove(arista);
				break;
			}
		}
	}

	public boolean existeArista(Punto punto, Punto punto2) {
		for (Arista arista : aristas) {
			if(arista.origen.igual(punto) || arista.origen.igual(punto2)){
				if(arista.destino.igual(punto) || arista.destino.igual(punto2)){
					return true;
				}
			}
		}
		return false;
	}
}
