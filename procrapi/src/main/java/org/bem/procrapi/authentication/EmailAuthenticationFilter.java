package org.bem.procrapi.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/* Nous avons opté pour cette solution d'authentification
* parce qu'un solution colmpète (token, cookies...) n'était pas forcément nécessaire.
* Notre approche possède des défauts : BD souvent sollicitée, peu de sécurité.
* Néanmoins, on considère que c'est suffisant pour ce cadre d'étude.*/

@Component
public class EmailAuthenticationFilter extends OncePerRequestFilter {

    private final RepositoryUtilisateur repositoryUtilisateur;

    public EmailAuthenticationFilter(RepositoryUtilisateur utilisateurRepository) {
        this.repositoryUtilisateur = utilisateurRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String email = request.getHeader("X-Utilisateur-Email");

        if (email != null) {
            repositoryUtilisateur.findByEmail(email).ifPresent(UtilisateurHolder::setCurrentUser);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            UtilisateurHolder.clear();
        }
    }
}
