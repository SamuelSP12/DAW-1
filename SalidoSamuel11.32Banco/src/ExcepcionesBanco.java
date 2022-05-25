
public class ExcepcionesBanco extends Exception{

	//creamos 2 constructores, la primera sin parametros
	public ExcepcionesBanco() {}

	public ExcepcionesBanco(String msjError) {
		//podemos introducir un mensaje personalizado
		super(msjError);
	}
}
