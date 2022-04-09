package Questao2_UDP;

import java.net.DatagramSocket;
import java.net.SocketException;

public class P2 {
	
	static final int PORTA_P1 = 4096;
	static final int PORTA_P2 = 4097;
	static final int NUMERO_PROCESSO = 2;
	public static void main(String[] args) 
	{
		DatagramSocket socket;
		try {
			socket = new DatagramSocket(PORTA_P2);
			Thread enviar_mensagem = new Thread(new Thread_cliente_envio(socket,PORTA_P1,NUMERO_PROCESSO));
			Thread receber_mensagem = new Thread (new Thread_cliente_recebimento(socket, NUMERO_PROCESSO));
			
			enviar_mensagem.start();
			receber_mensagem.start();
			
		} 
		catch (SocketException e1) {
			
			System.out.println("Erro do tipo SocketException");
		}
	}
}
