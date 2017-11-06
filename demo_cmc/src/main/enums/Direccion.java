package enums;

public enum Direccion {

	ARRIBA(0,1),
	ABAJO(0,-1),
	DERECHA(-1,0),
	IZQUIERDA(1,0),
	D_ARRIBA(-1,1),
	D_ABAJO(-1,-1),
	I_ARRIBA(1,1),
	I_ABAJO(1,-1),
	IGUAL(0,0);

	private int x;
	private int y;
	
	private Direccion(int x, int y){
		this.x = x;
		this.y = y;
	}

	public static Direccion getDireccion(int x, int y){
		for (Direccion dire : Direccion.values()) {
			if (dire.x == x){
				if (dire.y == y){
					return dire;
				}
			}
		}
		return null;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
}
