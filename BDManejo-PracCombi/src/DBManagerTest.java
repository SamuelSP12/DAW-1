import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Clase de pruebas con JUnit
 * @author samuel
 * @version 1.7
 *
 */
public class DBManagerTest {

	DBManager dbManager= new DBManager();
	
	@Test
	public void testNewClienteProcAlma() {
		assertTrue(DBManager.newClienteProcAlma(" "," "));
	}

	@Test
	public void testNewTabla() {
		assertTrue(DBManager.newTabla("","","","",""));
	}

	@Test
	public void testFiltrarPor() {
		assertTrue(DBManager.filtrarPor("",""));
	}

	@Test
	public void testInsertarDatosFichero() {
		assertTrue(DBManager.insertarDatosFichero(""));
	}

	@Test
	public void testActualizarDatosFichero() {
		assertTrue(DBManager.actualizarDatosFichero(""));
	}

	@Test
	public void testBorrarDatosFichero() {
		assertTrue(DBManager.borrarDatosFichero(""));
	}

}
