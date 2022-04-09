package Questao2_UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;


public class Thread_servidor_receber_repassar implements Runnable {



	final int PORTA_P1;

	DatagramSocket socket;
	final int NUMERO_PROCESSO;
	static int TAMANHO_BUFFER_RECEBIMENTO = 1024;

	public Thread_servidor_receber_repassar(DatagramSocket socket,int porta_P1, int numero_processo) {
		this.socket = socket;
		this.PORTA_P1 = porta_P1;
		this.NUMERO_PROCESSO = numero_processo;
	}

	@Override
	public void run() 
	{

		//ficar sempre escutando
		while(true)
		{
			
			///////////////////////////Parte do recebimento///////////////////////////
			
			try {
				Thread.sleep(1000);
				// criando o buffer de recebimento de mensagens

				byte[] buffer_recebimento = new byte[TAMANHO_BUFFER_RECEBIMENTO];

				DatagramPacket datagrama_recebimento = 
						new DatagramPacket(buffer_recebimento, TAMANHO_BUFFER_RECEBIMENTO);

				//coloca os bytes recebidos no datagrama de recebimento

				socket.receive(datagrama_recebimento);


				// colocando os bytes recebidos no datagrama de recebimento no vetor de bytes
				buffer_recebimento = datagrama_recebimento.getData();

				String mensagem_recebida = new String(buffer_recebimento, java.nio.charset.StandardCharsets.UTF_8);


				System.out.println("Servidor P"+ NUMERO_PROCESSO +" Recebeu a"
						+ " mensagem = " + mensagem_recebida);

				
				
				
				///////////////////////////Parte do envio///////////////////////////
				
				
				// mensagem vem invertida no padrão alvos,metodos (broadcast ou unicast)
				String parametros_configuracao = retornar_metodo_envio_alvos(mensagem_recebida);

				//invertendo os parametros para ficar na ordem metodo, alvos

				parametros_configuracao = inverter_string(parametros_configuracao);

				String mensagem_a_ser_enviada = retirar_parametros_configuracao(mensagem_recebida, 
						parametros_configuracao);

				byte[] buffer_envio = mensagem_a_ser_enviada.getBytes(StandardCharsets.UTF_8);

				
				///////////////////////////Unicast///////////////////////////
				if(parametros_configuracao.charAt(0) == '0')
				{
					
					try {
						InetAddress localhost;
						int id_processo_receptor = (parametros_configuracao.charAt(1)-'0');
						int porta_alvo = PORTA_P1+id_processo_receptor-1;

						localhost = InetAddress.getByName("localhost");
						DatagramPacket datagrama_envio;

						datagrama_envio = new DatagramPacket(buffer_envio,
								buffer_envio.length,
								localhost,
								porta_alvo);


						System.out.println("Servidor P"+ NUMERO_PROCESSO+" Encaminhou para"
								+ " o processo P"+id_processo_receptor +" A mensagem: " +mensagem_a_ser_enviada);

						socket.send(datagrama_envio);

					}catch (UnknownHostException e) 
					{
						System.out.println("Erro do tipo UnknownHostException");
					} catch (IOException e) {
						System.out.println("Erro do tipo IOException");
					}
				}else
				{
					
					///////////////////////////Broadcast///////////////////////////
					
					if(parametros_configuracao.charAt(0) == '1')
					{
						//definido id processo de origem, sendo que o processo de origem
						//não será alvo do broadcast
						int id_requisitante = (parametros_configuracao.charAt(1) - '0');

						InetAddress localhost;

						for(int cont=2;cont<=4;cont++)
						{

							if(cont != id_requisitante)
							{

								try {

									int porta_alvo = PORTA_P1+cont-1;

									localhost = InetAddress.getByName("localhost");
									DatagramPacket datagrama_envio;

									datagrama_envio = new DatagramPacket(buffer_envio,
											buffer_envio.length,
											localhost,
											porta_alvo);


									System.out.println("Servidor P"+ NUMERO_PROCESSO+" Encaminhou para"
											+ " o processo P"+cont +" A mensagem: " +mensagem_a_ser_enviada);

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
				}

			} catch (IOException e) 
			{
				System.out.println("Erro do tipo IOException");
			} catch (InterruptedException e) {

				System.out.println("Erro do tipo InterruptedException");
			}

		}


	}



	String retornar_metodo_envio_alvos(String entrada)
	{
		entrada = entrada.trim();
		
		String ultimos_caracteres = "";

		int cont = entrada.length()-1;
		boolean arroba_encontrado = false;
		while(cont>=0 &&  (arroba_encontrado == false) && cont>= entrada.length()-3)
		{

			if(entrada.charAt(cont) == '@')
			{
				arroba_encontrado = true;
			}else
			{
				ultimos_caracteres += entrada.charAt(cont);
			}

			cont--;
		}

		return ultimos_caracteres;
	}

	
	
	
	String inverter_string(String texto)
	{
		String resultado ="";

		for(int cont=texto.length()-1;cont>=0;cont--)
		{
			resultado += texto.charAt(cont);
		}
		return resultado;
	}


	
	
	
	
	String retirar_parametros_configuracao(String entrada,String parametros_sem_arroba)
	{
		String retorno;

		String parametros_com_arroba = '@' + parametros_sem_arroba;

		//substituindo parametros de configuração por espaço
		retorno = entrada.replace(parametros_com_arroba," ");

		//retirando espaço extra da etapa anterior

		retorno = retorno.trim();
		return retorno;

	}



}
