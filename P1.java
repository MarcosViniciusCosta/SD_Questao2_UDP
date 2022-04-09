package Questao2_UDP;

import java.net.DatagramSocket;
import java.net.SocketException;


public class P1 {

	static final int PORTA_P1 = 4096;
	static DatagramSocket socket;
	static final int NUMERO_PROCESSO = 1;

	public static void main(String[] args) 
	{

		try {
			socket = new DatagramSocket(PORTA_P1);
		}
		catch (SocketException e1) {

			System.out.println("Erro do tipo SocketException");
		}
		
		System.out.println("Servidor UDP rodando na porta "+socket.getLocalPort());
		Thread receber_e_repassar_mensagem = new Thread(new Thread_servidor_receber_repassar
				(socket,PORTA_P1,NUMERO_PROCESSO));
	
		receber_e_repassar_mensagem.start();
		
	}

	
	
}
