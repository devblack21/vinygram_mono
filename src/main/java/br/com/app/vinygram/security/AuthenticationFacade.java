package br.com.app.vinygram.security;

import org.apache.commons.codec.binary.Base64;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
	
	public static final Authentication getAuthenticated() {

		return SecurityContextHolder.getContext().getAuthentication();
	}

	public static String criptografiaBase64Encoder(String valor) {
		return new Base64().encodeToString(valor.getBytes());
	}

	public static String descriptografiaBase64Decoder(String valorCriptografado) {
		return new String(new Base64().decode(valorCriptografado));
	}

}