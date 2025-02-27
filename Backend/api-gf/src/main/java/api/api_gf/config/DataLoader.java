package api.api_gf.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase ejecutar scripts SQL al arrancar la aplicación. Se ejecuta codigo una
 * vez que la API este completamente cargada.
 */
@Component
public class DataLoader implements CommandLineRunner {

	/**
	 * DataSource ofrece la conexion a la base de datos. Es utilizado para obtener
	 * una conexion y ejecutar scripts SQL (En mi caso el views.sql).
	 */
	@Autowired
	private DataSource dataSource;

	/**
	 * Metodo que se ejecuta al iniciar la aplicacion. Se utiliza para ejecutar el
	 * script views.sql en la base de datos.
	 * 
	 * @param args Argumentos de la línea de comandos (No utilizados en mi caso).
	 * @throws Exception Si ocurre algún error al ejecutar el script o al obtener la
	 *                   conexión.
	 */
	@Override
	public void run(String... args) throws Exception {
		try (Connection connection = dataSource.getConnection()) {
			// Ejecutar el script views.sql despues que se ejecute la API
			ScriptUtils.executeSqlScript(connection, new ClassPathResource("views.sql"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
