package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.net.*;  // URLEncoder y URLDecoder
import javax.servlet.annotation.WebServlet;

@WebServlet("/CreaCookie")
public class CreaCookie extends HttpServlet {

    protected void procesaSolicitud(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        // se inicializa un objeto Cookie "vacío"  
        
        String nombreCookie = "";
        String contenidoCookie = "";
        String clave="";
        String idioma="";
        Cookie unaCookie = null;
        
        // Recepción de parámetros
        nombreCookie = request.getParameter("usuario");
        clave = request.getParameter("clave");
        idioma = request.getParameter("idioma");
       
        contenidoCookie=request.getQueryString();

        imprimir(response, out, nombreCookie, contenidoCookie);
    }

	private void imprimir(HttpServletResponse response, PrintWriter out,
			String nombreCookie, String contenidoCookie) {
		out.println("<!DOCTYPE html>");
		out.println("<html>");
        out.println("<head>");
        out.println("<title>Crea y Recupera</title>");            
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet Crea y Recupera</h1>");

        try {
          crearCookie(response, nombreCookie, contenidoCookie);                           
          
          out.println("Se crea una cookie de nombre " + nombreCookie);
          out.println("<br />");
          out.println("El contenido de la cookie es " + contenidoCookie);
          out.println("<br />");
      } 
      catch (Exception e){ 
          out.println("Se produce una excepción: ");
          out.println(e.getMessage());
          out.println("<br />");
      }
      finally {            
          out.println("<a href=\"RecuperaCookie\">Ir a RecuperaCookie <a/> ");
          out.println("</body>");
          out.println("</html>");
    	  out.close();
      }
	}

	private void crearCookie(HttpServletResponse response, String nombreCookie,
			String contenidoCookie) {
		Cookie unaCookie;
		// se crea el objeto cookie en el servidor
          unaCookie = new Cookie(nombreCookie, contenidoCookie);
            
          // se añade a la respuesta para enviar al cliente
          response.addCookie(unaCookie);
	}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesaSolicitud(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesaSolicitud(request, response);
    }

}