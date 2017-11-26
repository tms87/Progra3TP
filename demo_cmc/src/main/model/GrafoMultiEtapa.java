package model;

import java.util.ArrayList;
import java.util.List;

public class GrafoMultiEtapa {

	public AristaAbstracta arista;
	public List<Arista> v1 = new ArrayList<Arista>();
	public List<Arista> v1_2 = new ArrayList<Arista>();
	public List<Arista> v2 = new ArrayList<Arista>();

	public GrafoMultiEtapa(AristaAbstracta arista) {
		this.arista= arista;
	} 
}
