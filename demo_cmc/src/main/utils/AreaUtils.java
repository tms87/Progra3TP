package utils;

import java.util.List;

import enums.Direccion;
import graficos.Area;
import model.VerticesArea;

public class AreaUtils {

	/**
	 * Retorna verdadero si al menos una parte de el area B esta dentro del area A
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isAinsideB(Area a,Area b){
		VerticesArea vertA = new VerticesArea(a);
		VerticesArea vertB = new VerticesArea(b);
		return isAinsideB(vertA, vertB);
	}

	public static boolean isAinsideB(VerticesArea vertA, VerticesArea vertB){
		Direccion dire =PuntoUtils.direccionRelativa(vertA.getSuperiorIzquierdo(),vertB.getSuperiorIzquierdo());
		Direccion dire2;
		switch (dire) {
			case D_ABAJO:
				dire2 =PuntoUtils.direccionRelativa(vertA.getInferiorDerecho(),vertB.getSuperiorIzquierdo());
				if(dire2.equals(Direccion.ARRIBA) || dire2.equals(Direccion.IZQUIERDA) || dire2.equals(Direccion.I_ARRIBA) || dire2.equals(Direccion.IGUAL))
					return true;
				break;
			case ABAJO:
				dire2 =PuntoUtils.direccionRelativa(vertA.getInferiorIzquierdo(),vertB.getSuperiorIzquierdo());
				if(dire2.equals(Direccion.ARRIBA) || dire2.equals(Direccion.IGUAL))
					return true;
				break;
			case DERECHA:
				dire2 =PuntoUtils.direccionRelativa(vertA.getSuperiorDerecho(),vertB.getSuperiorIzquierdo());
				if(dire2.equals(Direccion.IZQUIERDA) || dire2.equals(Direccion.IGUAL))
					return true;
				break;
			case I_ABAJO:
				dire2 =PuntoUtils.direccionRelativa(vertA.getInferiorIzquierdo(),vertB.getSuperiorDerecho());
				if(dire2.equals(Direccion.ARRIBA) || dire2.equals(Direccion.D_ARRIBA) || dire2.equals(Direccion.DERECHA) || dire2.equals(Direccion.IGUAL))
					return true;
				break;
			case ARRIBA:
				dire2 =PuntoUtils.direccionRelativa(vertA.getSuperiorIzquierdo(),vertB.getInferiorIzquierdo());
				if(dire2.equals(Direccion.ABAJO) || dire2.equals(Direccion.IGUAL))
					return true;
				break;
			case IZQUIERDA:
				dire2 =PuntoUtils.direccionRelativa(vertA.getSuperiorIzquierdo(),vertB.getSuperiorDerecho());
				if(dire2.equals(Direccion.DERECHA) || dire2.equals(Direccion.IGUAL))
					return true;
				break;
			case I_ARRIBA:
				dire2 =PuntoUtils.direccionRelativa(vertA.getSuperiorIzquierdo(),vertB.getInferiorDerecho());
				if(dire2.equals(Direccion.DERECHA) || dire2.equals(Direccion.IGUAL) || dire2.equals(Direccion.ABAJO) || dire2.equals(Direccion.D_ABAJO))
					return true;
				break;
			case D_ARRIBA:
				dire2 =PuntoUtils.direccionRelativa(vertA.getSuperiorDerecho(),vertB.getInferiorIzquierdo());
				if(dire2.equals(Direccion.IZQUIERDA) || dire2.equals(Direccion.IGUAL)  || dire2.equals(Direccion.ABAJO) || dire2.equals(Direccion.I_ABAJO) )
					return true;
				break;
			case IGUAL:
				return true;
		}
		return false;
	}
	
	public static boolean isIn(Area a, List<Area> lista){
		if (lista != null){
			for (Area area : lista) {
				if (a.equals(area)){
					return true;
				}
			}
		}
		return false;
	}
}
