package Questao2_UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;


public class Thread_cliente_recebimento implements Runnable {



	DatagramSocket socket;
	final int NUMERO_PROCESSO;				// P1 - 1, P2 - 2, P3 - 3, P4 - 4
	Scanner leia = new Scanner(System.in);
	final int TAMANHO_BUFFER_RECEBIMENTO = 1024;
	
	public Thread_cliente_recebimento(DatagramSocket socket,int numero_processo) 
	{
		this.socket = socket;
		this.NUMERO_PROCESSO = numero_processo;
	}

	@Override
	public void run() {

		//ficar sempre escutando
		while(true)
		{
			try {
				//Thread.sleep(1000);
				// criando o buffer de recebimento de mensagens

				byte[] buffer_recebimento = new byte[TAMANHO_BUFFER_RECEBIMENTO];

				DatagramPacket datagrama_recebimento = 
						new DatagramPacket(buffer_recebimento, TAMANHO_BUFFER_RECEBIMENTO);

				//coloca os bytes recebidos no datagrama de recebimento

				socket.receive(datagrama_recebimento);


				// colocando os bytes recebidos no datagrama de recebimento no vetor de bytes
				buffer_recebimento = datagrama_recebimento.getData();

				String mensagem_recebida = new String(buffer_recebimento, java.nio.charset.StandardCharsets.UTF_8);


				System.out.println("Processo P"+ NUMERO_PROCESSO +" Recebeu a"
						+ " mensagem = " + mensagem_recebida);


			} catch (IOException e) 
			{
				System.out.println("Erro do tipo IOException");
			}	
		}

	}

}
