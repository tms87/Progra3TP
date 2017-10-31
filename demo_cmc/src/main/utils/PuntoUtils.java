package utils;

import enums.Direccion;
import graficos.Punto;

public class PuntoUtils {

	public static Direccion direccionRelativa(Punto a, Punto b){
		return Direccion.getDireccion(Integer.compare(a.x, b.x),Integer.compare(a.y, b.y));
	}
}
