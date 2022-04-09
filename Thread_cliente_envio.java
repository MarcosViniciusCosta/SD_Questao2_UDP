package Questao2_UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Thread_cliente_envio implements Runnable {

	DatagramSocket socket;
	final int NUMERO_PROCESSO;				// P1 - 1, P2 - 2, P3 - 3, P4 - 4
	Scanner leia;
	final int PORTA_SERVIDOR;
	
	public Thread_cliente_envio(DatagramSocket socket, int porta_servidor,int numero_processo) 
	{	
		this.NUMERO_PROCESSO = numero_processo;
		this.socket = socket;
		leia = new Scanner(System.in);
		this.PORTA_SERVIDOR = porta_servidor;
	}

	
	public void run() {
		
		
		int opcao, id_processo_alvo = 1;
		String mensagem ="";
		
		System.out.println("0 - Unicast");
		System.out.println("1 - Broadcast");
		System.out.print("Escolha a opção: ");
		opcao = leia.nextInt();
		
		//capturando \n que ficou solto após ler o inteiro
		leia.nextLine();
		
		if(opcao != 0 && opcao != 1)
		{
			System.out.println("Opcão inválida!!!\n");
		}
		
		
		
		while(true)
		{
			
			// formatando mensagem para tipo "mensagem enviada pelo teclado" vira
			//"mensagem enviada pelo teclado@1" caso tenha sido enviada por P1

			

			while(opcao <0 || opcao > 1)
			{
				
				System.out.println("0 - Unicast");
				System.out.println("1 - Broadcast");
				System.out.print("Escolha a opção: ");
				opcao = leia.nextInt();

				//capturando \n que ficou solto após ler o inteiro
				leia.nextLine();

				if(opcao != 0 && opcao != 1)
				{
					System.out.println("Opcão inválida!!!\n");
				}
			}

			String mensagem_final = "";
			switch(opcao)
			{
			//unicast
			case 0:

				// lendo o id do processo receptor
				do {
					System.out.print("Digite o id do processo receptor: ");
					id_processo_alvo = leia.nextInt();

					//capturando \n que ficou solto após ler o inteiro
					leia.nextLine();

					if(id_processo_alvo >4 && id_processo_alvo <2)
					{
						System.out.println("Opcão inválida!!!\n");
					}

				}while(id_processo_alvo>4 && id_processo_alvo<2);

				//lendo a mensagem a ser enviada
				System.out.print("Digite a mensagem a ser enviada: ");
				mensagem = leia.nextLine();

				mensagem_final = mensagem+"@0"+id_processo_alvo;
				break;

			//broadcast
			case 1:
				//lendo a mensagem a ser enviada
				System.out.print("Digite a mensagem a ser enviada: ");
				mensagem = leia.nextLine();
				mensagem_final = mensagem+"@1" + NUMERO_PROCESSO;
				break;
			default:
				System.out.println("Opção inválida!!!");
				break;
			}

			// criar o datagrama e enviar para o servidor
			
			byte[] buffer_envio = mensagem_final.getBytes(StandardCharsets.UTF_8);

			try {
				InetAddress localhost;
				
				

				localhost = InetAddress.getByName("localhost");
				DatagramPacket datagrama_envio;

				datagrama_envio = new DatagramPacket(buffer_envio,
						buffer_envio.length,
						localhost,
						PORTA_SERVIDOR);


				System.out.println("Cliente P"+ NUMERO_PROCESSO+" Encaminhou para"
						+ " o processo P"+id_processo_alvo +" A mensagem: " + mensagem_final);

				socket.send(datagrama_envio);

			}catch (UnknownHostException e) 
			{
				System.out.println("Erro do tipo UnknownHostException");
			} catch (IOException e) {
				System.out.println("Erro do tipo IOException");
			}
			
			
		}
	}

}
