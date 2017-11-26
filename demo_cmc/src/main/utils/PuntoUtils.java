package utils;

import enums.Direccion;
import graficos.Punto;
import model.PuntoKey;

public class PuntoUtils {

	public static Direccion direccionRelativa(Punto a, Punto b){
		return Direccion.getDireccion(Integer.compare(a.x, b.x),Integer.compare(a.y, b.y));
	}
	
	public static PuntoKey[] newPuntoKey(Punto[] puntos){
		PuntoKey[] keys= new PuntoKey[puntos.length];
		for (int i = 0; i < keys.length; i++) {
			keys[i] = new PuntoKey(puntos[i]);
		}
		return keys;
	}
}
