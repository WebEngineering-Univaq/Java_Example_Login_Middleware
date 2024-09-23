package it.univaq.f4i.iw.examples;

import it.univaq.f4i.iw.framework.result.HTMLResult;
import it.univaq.f4i.iw.framework.utils.ServletHelpers;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Giuseppe Della Penna
 */
public class SecuredPage extends HttpServlet {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HTMLResult result = new HTMLResult(getServletContext());
        result.setTitle("Secured page");
        result.setBody("<h1>Secured page</h1><p>Only logged-in users can access this content</p>");
        result.appendToBody("<p>VERY CONFIDENTIAL TEXT!!</p>");
        result.appendToBody("<p><a href=\"homepage\">Return to the Homepage</a></p>");
        result.activate(request, response);
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws jakarta.servlet.ServletException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {        
        //abbiamo un filtro che controlla la sessione, quindi non dobbiamo fare nulla qui
        //we have a filter that checks the session, so we have nothing to check here
        try {
            action_default(request, response);
        } catch (Exception ex) {
            ServletHelpers.handleError(ex, request, response, getServletContext());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
