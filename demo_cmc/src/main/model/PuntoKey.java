package model;

public class PuntoKey extends graficos.Punto {

	private static final long serialVersionUID = -1093318451865547595L;

	public PuntoKey(int x, int y) {
		super(x, y);
	}
	public PuntoKey(graficos.Punto p) {
		super(p.x,p.y);
	}
	@Override
	public boolean equals(Object obj) {
		PuntoKey other = (PuntoKey) obj;
		if (this.x == other.x)
			if (this.y == other.y)
				return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.x+this.y;
	}
}
