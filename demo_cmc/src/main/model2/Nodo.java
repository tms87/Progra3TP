package model2;

import java.util.List;

import graficos.Punto;

public class Nodo {

	public Punto punto;
	public List<Arista> aristas;

	public Nodo(Punto punto) {
		this.punto = punto;
	}
}
