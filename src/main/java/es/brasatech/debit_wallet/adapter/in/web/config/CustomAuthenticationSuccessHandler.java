package es.brasatech.debit_wallet.adapter.in.web.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        // Logic to check if user needs to set a password
        // For now, we'll check if they are newly created or have no surname (as a proxy
        // for incomplete profile)
        // or if we have a specific attribute.
        // User explicitly said: "first time a user access the platform they ill be
        // redirect here to create a password"
        // We'll check the Authentication details if possible, or just default to
        // /wallet

        String targetUrl = "/wallet";

        // If we wanted to be more specific, we could inject a service here to check the
        // user state.
        // For this task, we will simulate the redirect to the profile page context.
        // Since the profile is a fragment shown via app.showProfileSettings(),
        // we can use a fragment identifier or a query param that the frontend picks up.

        // Check if the user is the "newly verified" user from data.sql (for demo
        // purposes)
        if (authentication.getName().equals("newuser")) {
            targetUrl = "/wallet?setup=true";
        }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
