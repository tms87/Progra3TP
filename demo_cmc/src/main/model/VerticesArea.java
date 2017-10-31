package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import graficos.Punto;

public class VerticesArea {

	Punto[][] vertices = new Punto[2][2];
	public VerticesArea(graficos.Area area) {
		int[] cord = area.getCoordenadas();
		cord[2] += cord[0];
		cord[3] += cord[1];
		vertices[0][0] = new Punto(cord[0], cord[1],Color.BLACK);
		vertices[0][1] = new Punto(cord[2], cord[1],Color.BLACK);
		vertices[1][0] = new Punto(cord[0], cord[3],Color.BLACK);
		vertices[0][1] = new Punto(cord[2], cord[3],Color.BLACK);
	}
	
	public Punto getSuperiorIzquierdo(){
		return vertices[0][0];
	}
	public Punto getSuperiorDerecho(){
		return vertices[0][1];
	}
	public Punto getInferiorIzquierdo(){
		return vertices[1][0];
	}
	public Punto getInferiorDerecho(){
		return vertices[1][1];
	}
	
	public List<Punto> vertices(){
		List<Punto> ret = new ArrayList<>();
		for (Punto[] puntos : vertices) {
			for (Punto punto : puntos) {
				ret.add(punto);
			}
		}
		return ret;
	}
}
