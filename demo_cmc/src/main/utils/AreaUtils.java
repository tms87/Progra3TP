package utils;

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
		//Superior Izquierdo
		if(PuntoUtils.direccionRelativa(vertA.getSuperiorIzquierdo(),vertB.getSuperiorIzquierdo()).equals(Direccion.D_ABAJO)){
			Direccion dire = PuntoUtils.direccionRelativa(vertA.getInferiorDerecho(),vertB.getSuperiorIzquierdo());
			if (dire.equals(Direccion.I_ARRIBA) || dire.equals(Direccion.ARRIBA) || dire.equals(Direccion.IZQUIERDA)){
				return true;
			}
		} else if (PuntoUtils.direccionRelativa(vertA.getSuperiorIzquierdo(),vertB.getSuperiorIzquierdo()).equals(Direccion.ABAJO)) {
			if(PuntoUtils.direccionRelativa(vertA.getInferiorIzquierdo(),vertB.getSuperiorIzquierdo()).equals(Direccion.ARRIBA)){
				return true;
			}
		} else if (PuntoUtils.direccionRelativa(vertA.getSuperiorIzquierdo(),vertB.getSuperiorIzquierdo()).equals(Direccion.DERECHA)) {
			if(PuntoUtils.direccionRelativa(vertA.getSuperiorDerecho(),vertB.getSuperiorIzquierdo()).equals(Direccion.IZQUIERDA)){
				return true;
			}
		} else {
			//Superior Derecho
			if(PuntoUtils.direccionRelativa(vertA.getSuperiorDerecho(),vertB.getSuperiorDerecho()).equals(Direccion.I_ABAJO)){
				Direccion dire = PuntoUtils.direccionRelativa(vertA.getInferiorIzquierdo(),vertB.getSuperiorDerecho());
				if (dire.equals(Direccion.D_ARRIBA) || dire.equals(Direccion.ARRIBA) || dire.equals(Direccion.DERECHA)){
					return true;
				}
			} else if (PuntoUtils.direccionRelativa(vertA.getSuperiorDerecho(),vertB.getSuperiorDerecho()).equals(Direccion.ABAJO)) {
				if(PuntoUtils.direccionRelativa(vertA.getInferiorDerecho(),vertB.getSuperiorDerecho()).equals(Direccion.ARRIBA)){
					return true;
				}
			} else if (PuntoUtils.direccionRelativa(vertA.getSuperiorDerecho(),vertB.getSuperiorDerecho()).equals(Direccion.IZQUIERDA)) {
				if(PuntoUtils.direccionRelativa(vertA.getSuperiorIzquierdo(),vertB.getSuperiorDerecho()).equals(Direccion.DERECHA)){
					return true;
				}
			} else {
				//Inferior Derecho
				if(PuntoUtils.direccionRelativa(vertA.getInferiorDerecho(),vertB.getInferiorDerecho()).equals(Direccion.I_ARRIBA)){
					Direccion dire = PuntoUtils.direccionRelativa(vertA.getSuperiorIzquierdo(),vertB.getInferiorDerecho());
					if (dire.equals(Direccion.D_ARRIBA) || dire.equals(Direccion.ABAJO) || dire.equals(Direccion.DERECHA)){
						return true;
					}
				} else if (PuntoUtils.direccionRelativa(vertA.getInferiorDerecho(),vertB.getInferiorDerecho()).equals(Direccion.ARRIBA)) {
					if(PuntoUtils.direccionRelativa(vertA.getSuperiorDerecho(),vertB.getInferiorDerecho()).equals(Direccion.ABAJO)){
						return true;
					}
				} else if (PuntoUtils.direccionRelativa(vertA.getInferiorDerecho(),vertB.getInferiorDerecho()).equals(Direccion.IZQUIERDA)) {
					if(PuntoUtils.direccionRelativa(vertA.getInferiorIzquierdo(),vertB.getInferiorDerecho()).equals(Direccion.DERECHA)){
						return true;
					}
				} else {
					//Inferior Izquierdo
					if(PuntoUtils.direccionRelativa(vertA.getInferiorIzquierdo(),vertB.getInferiorIzquierdo()).equals(Direccion.D_ARRIBA)){
						Direccion dire = PuntoUtils.direccionRelativa(vertA.getSuperiorDerecho(),vertB.getInferiorIzquierdo());
						if (dire.equals(Direccion.I_ARRIBA) || dire.equals(Direccion.ABAJO) || dire.equals(Direccion.IZQUIERDA)){
							return true;
						}
					} else if (PuntoUtils.direccionRelativa(vertA.getInferiorIzquierdo(),vertB.getInferiorIzquierdo()).equals(Direccion.ARRIBA)) {
						if(PuntoUtils.direccionRelativa(vertA.getSuperiorIzquierdo(),vertB.getInferiorIzquierdo()).equals(Direccion.ABAJO)){
							return true;
						}
					} else if (PuntoUtils.direccionRelativa(vertA.getInferiorIzquierdo(),vertB.getInferiorIzquierdo()).equals(Direccion.DERECHA)) {
						if(PuntoUtils.direccionRelativa(vertA.getInferiorDerecho(),vertB.getInferiorIzquierdo()).equals(Direccion.IZQUIERDA)){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
