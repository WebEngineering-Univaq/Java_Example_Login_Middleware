/*
 * Questa classe implementa un Servlet Filter, cioè una classe che intercetta
 * e gestisce il traffico di dati in entrata e in uscita dalle servlet.
 * Perchè funzioni, debve essere configurata tramite il file web.xml.
 *
 */
package it.univaq.f4i.iw.framework.security;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Giuseppe Della Penna
 */
public class SessionCheckFilter implements Filter {

    private FilterConfig config = null;
    private Pattern protect = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
         //init protection pattern
        String p = config.getServletContext().getInitParameter("security.protect.patterns");
        if (p == null || p.isBlank()) {
            protect = null;
        } else {
            String[] split = p.split("\\s*,\\s*");
            protect = Pattern.compile(Arrays.stream(split).collect(Collectors.joining("$)|(?:", "(?:", "$)")));
        }                      
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httprequest = (HttpServletRequest) request;
        HttpServletResponse httpresponse = (HttpServletResponse) response;
        String uri = httprequest.getRequestURI();
        HttpSession s = SecurityHelpers.checkSession(httprequest);
        //non ridirezioniamo verso la login se richiediamo risorse da non proteggere
        //do not redirect to login if we are requesting unprotected resources
        if (s == null && protect != null && protect.matcher(uri).find()) {
            String completeRequestURL = httprequest.getRequestURL() + (httprequest.getQueryString() != null ? "?" + httprequest.getQueryString() : "");
            httpresponse.sendRedirect("login?referrer=" + URLEncoder.encode(completeRequestURL, "UTF-8"));
        } else {
            //serve unprotected resources
            chain.doFilter(httprequest, httpresponse);
        }
    }

    @Override
    public void destroy() {
        config = null;
    }
}
