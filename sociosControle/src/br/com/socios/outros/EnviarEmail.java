/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.socios.outros;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;


public class EnviarEmail {
  
    
//    public void enviandoEmail() throws EmailException{ // criando envio do gmail.com
//        SimpleEmail emailSimples = new SimpleEmail();
//        
//        emailSimples.setHostName("smtp.gmail.com"); // hostName do Gmail
//        System.out.println("criou host");
//        emailSimples.setSmtpPort(465); // setando a porta do gmail
//        System.out.println("setou porta");
//        emailSimples.setFrom("ssankys3@gmail.com", "socios"); // seta o endereço do email que enviará e nome de quem enviou
//        System.out.println("registrou email remetente");
//        emailSimples.addTo("samuel_sankys@hotmail.com","Samuel de Souza"); // email do destinatário e o nome dele
//        System.out.println("email destinatario");
//        emailSimples.setSubject("Teste do programa de socios"); // assunto do email;
//        System.out.println("setou assunto");
//        emailSimples.setMsg("Boleto de Doação completo"); // mensagem do email
//        System.out.println("setou msg");
//        emailSimples.setSSL(true); // assinatura
//        System.out.println("assinou");
//        emailSimples.setAuthentication("ssankys3@gmail.com", "samuelsk8");
//        System.out.println("autenticou ");
//        emailSimples.send(); // para que o email seja enviado é necessario configurar a conta google para acesso de app menos seguros.
//        System.out.println("iniciando envio");
//        
//    }   

	public EnviarEmail() throws EmailException, MalformedURLException {
		enviaEmailSimples();
		enviaEmailComAnexo();
		enviaEmailFormatoHtml();
                
	}
	/**
	 * envia email simples(somente texto)
	 * @throws EmailException
	 */
	public void enviaEmailSimples() throws EmailException {
		SimpleEmail email = new SimpleEmail();
		email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail
		email.addTo("teste@gmail.com", "Guilherme"); //destinatário
		email.setFrom("teste@gmail.com", "Eu"); // remetente
		email.setSubject("Teste -&gt; Email simples"); // assunto do e-mail
		email.setMsg("Teste de Email utilizando commons-email"); //conteudo do e-mail
		email.setAuthentication("teste&t", "xxxxx");
		email.setSmtpPort(465);
		email.setSSL(true);
		email.setTLS(true);
		email.send();	
	}
	/**
	 * envia email com arquivo anexo
	 * @throws EmailException
	 */
	public void enviaEmailComAnexo() throws EmailException{
		// cria o anexo 1.
		EmailAttachment anexo1 = new EmailAttachment();
		anexo1.setPath("teste/teste.txt"); //caminho do arquivo (RAIZ_PROJETO/teste/teste.txt)
		anexo1.setDisposition(EmailAttachment.ATTACHMENT);
		anexo1.setDescription("Exemplo de arquivo anexo");
		anexo1.setName("teste.txt");		
		// cria o anexo 2.
		EmailAttachment anexo2 = new EmailAttachment();
		anexo2.setPath("teste/teste2.jsp"); //caminho do arquivo (RAIZ_PROJETO/teste/teste2.jsp)
		anexo2.setDisposition(EmailAttachment.ATTACHMENT);
		anexo2.setDescription("Exemplo de arquivo anexo");
		anexo2.setName("teste2.jsp");		
		// configura o email
		MultiPartEmail email = new MultiPartEmail();
		email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail
		email.addTo("teste@gmail.com&quot;, &quot;Guilherme"); //destinatário
		email.setFrom("teste@gmail.com&quot;, &quot;Eu"); // remetente
		email.setSubject("Teste -&gt; Email com anexos"); // assunto do e-mail
		email.setMsg("Teste de Email utilizando commons-email"); //conteudo do e-mail
		email.setAuthentication("teste", "xxxxx");
		email.setSmtpPort(465);
		email.setSSL(true);
		email.setTLS(true);
		// adiciona arquivo(s) anexo(s)
		email.attach(anexo1);
		email.attach(anexo2);
		// envia o email
		email.send();
	}
	/**
	 * Envia email no formato HTML
	 * @throws EmailException 
	 * @throws MalformedURLException 
	 */
	public void enviaEmailFormatoHtml() throws EmailException, MalformedURLException {
		HtmlEmail email = new HtmlEmail();
		// adiciona uma imagem ao corpo da mensagem e retorna seu id
		URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
		String cid = email.embed(url, "Apache logo");	
		// configura a mensagem para o formato HTML
		email.setHtmlMsg("<html>Logo do Apache - <img ></html>");
		// configure uma mensagem alternativa caso o servidor não suporte HTML
		email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");
		email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail
		email.addTo("teste@gmail.com", "Guilherme"); //destinatário
		email.setFrom("teste@gmail.com", "Eu"); // remetente
		email.setSubject("Teste -&gt; Html Email"); // assunto do e-mail
		email.setMsg("Teste de Email HTML utilizando commons-email"); //conteudo do e-mail
		email.setAuthentication("teste", "xxxxx");
		email.setSmtpPort(465);
		email.setSSL(true);
		email.setTLS(true);
		// envia email
		email.send();
	}
	/**
	 * @param args
	 * @throws EmailException 
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws EmailException, MalformedURLException {
		new EnviarEmail();
	}

}
