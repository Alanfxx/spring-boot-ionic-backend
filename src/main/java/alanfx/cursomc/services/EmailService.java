package alanfx.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import alanfx.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
}
