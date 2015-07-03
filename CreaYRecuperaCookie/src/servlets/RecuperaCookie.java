package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RecuperaCookie")
public class RecuperaCookie extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		procesaSolicitud(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		procesaSolicitud(request, response);
	}

	protected void procesaSolicitud(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		generaPagina(response, request);

	}

	private void generaPagina(HttpServletResponse response,
			HttpServletRequest request) {
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			// String oculto = "";
			int i;

			// Recepción del parámetro oculto
			// oculto = request.getParameter("oculto");
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Crea y Recupera</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Recupera Cookie</h1>");
			out.println("<h2>Cookies recibidas:</h2>");

			imprimeCookies(request, out);

			out.println("</body>");
			out.println("</html>");
		} catch (Exception e) {
			System.err.println("Se produce una excepción <br />");
			e.printStackTrace();
		}
	}

	private void imprimeCookies(HttpServletRequest request, PrintWriter out)
			throws UnsupportedEncodingException {
		int i;
		// Recepción de cookies
		Cookie[] arrayCookies = request.getCookies();
		int longitud = arrayCookies.length;
		Map<String, String> parametros;
		Map<String, List<String>> parametros2;
		if (arrayCookies != null && longitud > 0) {
			for (i = 0; i < longitud; i++) {
				String nombreCookie = arrayCookies[i].getName();
				String contenidoCookie = arrayCookies[i].getValue();
				out.print("Nombre: <b>" + nombreCookie + "</b>" + " <br />");
				out.print("Contenido: " + contenidoCookie + "<br />");
				// parametros = extraeDatosDeQueryString(contenidoCookie);
				parametros2 = splitQuery(contenidoCookie);
				/*
				 * if (!parametros.isEmpty()) { for (Entry<String, String>
				 * elemento : parametros.entrySet()){ String clave =
				 * elemento.getKey(); String valor = elemento.getValue();
				 * out.println("&emsp;&emsp;" + clave + " = " + valor +
				 * "<br />"); } }
				 */
				if (!parametros2.isEmpty()) {
					for (Entry<String, List<String>> elemento : parametros2
							.entrySet()) {
						String clave = elemento.getKey();
						List<String> valores = elemento.getValue();
						out.print("&emsp;&emsp;" + clave + " = [&nbsp;");
						for (String valor : valores) {
							out.print(valor + "&nbsp;");
						}
						out.print("]" + "<br />");
					}
				}

			}
		} else {
			out.print("El navegador no ha enviado ninguna cookie");
		}
	}

	// extrae los pares campo=valor de una QUERY_STRING
	// trata a cada parámetro de manera independiente, aunque se llamen igual
	// no permite valores nulos
	private static Map<String, String> extraeDatosDeQueryString(
			String queryString) throws UnsupportedEncodingException {
		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		String[] parametros = queryString.split("&");
		if (parametros.length > 0) { // la cadena de texto tiene estructura de
										// QUERY_STRING
			for (String pair : parametros) {
				int idx = pair.indexOf("=");
				query_pairs.put(
						URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
						URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
			}
		}
		return query_pairs;
	}

	// extrae los pares campo=valor de una QUERY_STRING
	// si un parámetro se envía con múltiples valores, lo "procesa" como si
	// fuera un array
	// permite valores nulos
	private static Map<String, List<String>> splitQuery(String queryString)
			throws UnsupportedEncodingException {
		Map<String, List<String>> query_pairs = new LinkedHashMap<String, List<String>>();
		String[] parametros = queryString.split("&");
		for (String pair : parametros) {
			int idx = pair.indexOf("=");
			String clave = idx > 0 ? URLDecoder.decode(pair.substring(0, idx),
					"UTF-8") : pair;
			if (!query_pairs.containsKey(clave)) {
				query_pairs.put(clave, new LinkedList<String>());
			}
			String valor = idx > 0 && pair.length() > idx + 1 ? URLDecoder
					.decode(pair.substring(idx + 1), "UTF-8") : null;
			query_pairs.get(clave).add(valor);
		}
		return query_pairs;
	}
}
