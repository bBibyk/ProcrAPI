package org.bem.procrapi.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/* Nous avons opté pour cette solution d'authentification
* parce qu'une solution colmpète (token, cookies...) n'était pas forcément nécessaire.
* Notre approche possède des défauts : BD plus souvent sollicitée, peu de sécurité.
* Néanmoins, on considère que c'est suffisant pour ce cadre d'étude.*/

@Component
public class EmailAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // On récupère le champs dans l'entête
        String email = request.getHeader("X-Utilisateur-Email");

        if (email != null) {
            // On le stockque dans notre holder
            EmailHolder.setEmail(email);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            EmailHolder.clear();
        }
    }
}
