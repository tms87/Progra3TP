package demo_cmc;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import enums.Direccion;
import graficos.Punto;
import utils.PuntoUtils;
import vista.CanvasFrame;

public class DireccionTest {
	
	@BeforeClass
	public static void init(){
		CanvasFrame frame = new CanvasFrame("Progra III - DEMO");
        frame.configurar();
	}
	
	
	@Test
	public void TestDireccion() {
		Punto a = new Punto(10,10);
		Punto b = new Punto(3,10);
		Direccion dire = PuntoUtils.direccionRelativa(a, b);
		Assert.assertTrue(Direccion.IZQUIERDA.equals(dire));
		b = new Punto(14,10);
		dire = PuntoUtils.direccionRelativa(a, b);
		Assert.assertTrue(Direccion.DERECHA.equals(dire));
		b = new Punto(10,4);
		dire = PuntoUtils.direccionRelativa(a, b);
		Assert.assertTrue(Direccion.ARRIBA.equals(dire));
		b = new Punto(10,13);
		dire = PuntoUtils.direccionRelativa(a, b);
		Assert.assertTrue(Direccion.ABAJO.equals(dire));
		b = new Punto(11,11);
		dire = PuntoUtils.direccionRelativa(a, b);
		Assert.assertTrue(Direccion.D_ABAJO.equals(dire));
		b = new Punto(11,9);
		dire = PuntoUtils.direccionRelativa(a, b);
		Assert.assertTrue(Direccion.D_ARRIBA.equals(dire));
		b = new Punto(9,14);
		dire = PuntoUtils.direccionRelativa(a, b);
		Assert.assertTrue(Direccion.I_ABAJO.equals(dire));
		b = new Punto(8,2);
		dire = PuntoUtils.direccionRelativa(a, b);
		Assert.assertTrue(Direccion.I_ARRIBA.equals(dire));
	}
}
