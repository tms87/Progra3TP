package utils;

public class MathUtils {

	public static double getHipotenusa(int x1,int y1,int x2, int y2){
		int x = Math.abs(x1 - x2);
		int y = Math.abs(y1 - y2);
		return getHipotenusa(x, y);
	}

	public static double getHipotenusa(int x, int y) {
		return Math.sqrt(x^2 + y^2);
	}
}
