package com.hellblazer.utils.fd.impl;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.ArrayList;

import com.hellblazer.utils.fd.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class EndNode extends Thread {

	int port;
	InetAddress server;
	FailureDetectorFactory adapFDfac;
	public static Map<Integer, Integer> gossipList = new ConcurrentHashMap<Integer, Integer>();
	public static Map<Integer, FailureDetector> FDList = new ConcurrentHashMap<Integer, FailureDetector>();
	public static Integer[][] suspectMtx = new Integer[4][4];
	public static List<Integer> aliveList = new ArrayList<Integer>();
	public static List<Integer> deadList = new ArrayList<Integer>();
	public static long[] fctime = new long[4];
	public static int[] fcflag = new int[4];
	public static int fccount = 0;
	public static long[] ft = new long[100];
	// public static int[] ff = new int[100];
	public static int index = 1;
	public static int[] mc = new int[100];
	public String strtemp= new String();
	private Socket m_socket;//connect to python
	
	public EndNode(String pServer, int pPort) {
		try {
			port = pPort;
			server = InetAddress.getByName(pServer);
			adapFDfac = new AdaptiveFailureDetectorFactory(0.9, 1000, 1.0,
					20000, 100, 1000);
			FailureDetector adapFD = adapFDfac.create();
			FDList.put(port, adapFD);

			aliveList.add(port);

			// connect to python
			/*
			String Py_ip = "192.168.78.160";
			int Py_port = 12345;
			m_socket = new Socket(Py_ip, Py_port);
			*/
			// 建立連線。(ip為伺服器端的ip，port為伺服器端開啟的port)

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run(List<String> tempResult) {

		try {
			System.out.println(tempResult);
			
			ByteBuffer rvbuffer = ByteBuffer.allocateDirect(65507);

			InetSocketAddress client;
			if ((client = (InetSocketAddress) FdMain.channel.receive(rvbuffer)) != null) {
				System.out.println("Receive Success");
				System.out.println("Receive from: " + client.getPort());
				rvbuffer.flip();
				byte rvbufferarr[] = new byte[rvbuffer.remaining()];
				rvbuffer.get(rvbufferarr);
				ByteArrayInputStream bais = new ByteArrayInputStream(
						rvbufferarr);
				ObjectInputStream ois = new ObjectInputStream(bais);

				Object rvobj;
				if ((rvobj = ois.readObject()) instanceof Map<?, ?>) {
					Map<Integer, Integer> rvgl = (Map<Integer, Integer>) rvobj;
					for (int i = 0; i < 4; i++) {
						int rvhc = rvgl.get(5554 + i);
						int cuhc = gossipList.get(5554 + i);
						if (rvhc > cuhc) {
							FDList.get(5554 + i).record(
									System.currentTimeMillis(), 0L);
							gossipList.put(5554 + i, rvhc);
							// suspectMtx[i][i] = 0;
						}
						System.out.println("Receive 5554+" + i + ": " + cuhc);
					}
				}
				if ((rvobj = ois.readObject()) instanceof Integer[][]) {
					System.out.println("Receive Mtx");
					Integer[][] rvsm = (Integer[][]) rvobj;
					for (int i = 0; i < 4; i++) {
						if (i == FdMain.SendportList.get(client.getPort())) {
							for (int j = 0; j < 4; j++) {
								suspectMtx[i][j] = rvsm[i][j];
							}
						} else {
							if (i != FdMain.SendportList.get(5502)) {
								for (int j = 0; j < 4; j++) {
									if (suspectMtx[i][j] == -1
											&& rvsm[i][j] == 1)
										suspectMtx[i][j] = rvsm[i][j];
									else
										suspectMtx[i][j] = suspectMtx[i][j]
												| rvsm[i][j];
								}
							}
						}
					}
				}
			} else
				System.out.println("Receive Nothing");

			long now = System.currentTimeMillis();
			for (int i = 0; i < 4; i++) {
				FailureDetector adapFD = FDList.get(5554 + i);
				if (adapFD != null) {
					if (!adapFD.shouldConvict(now)) {
						fcflag[i] = 0;
						System.out.println("5554+" + i + "-Alive-");
						if (suspectMtx[2][i] == 1 || suspectMtx[2][i] == -1)
							suspectMtx[2][i] = -1;
						else
							suspectMtx[2][i] = 0;
						Integer p = new Integer(5554 + i);
						// Integer pi = new Integer(i);
						if (!aliveList.contains(p))
							aliveList.add(p);
						// if(deadList.contains(pi))
						// deadList.remove(pi);
					} else {

						System.out.println("5554+" + i + "-Dead-");
						suspectMtx[2][i] = 1;
						System.out.println(suspectMtx[2][i]);

						int dcount = 0;
						// dcount +=deadList.size();
						for (int j = 0; j < 4; j++) {
							if (suspectMtx[j][i] == 1)
								dcount++;
							if ((dcount > aliveList.size() / 2 || aliveList
									.size() == 2) && suspectMtx[i][i] == 0) {
								suspectMtx[i][i] = 1;
								if (j >= i) {
									dcount++;
								}
							}
							if (dcount == aliveList.size()) {
								if (fcflag[i] == 0) {
									fctime[i] = System.currentTimeMillis();
									fcflag[i] = 1;
									fccount++;
									if (i == 0
											&& gossipList.get(5554) > 1000 * index) {
										if (index == 1) {
											index++;
										} else {
											ft[index - 1] = fctime[i];
											fccount -= 1;
											int cmc = fccount;
											for (int m = 0; m < index - 1; m++)
												cmc -= mc[m];
											mc[index - 1] = cmc;
											index++;
										}
									}
								}

								// //////connect to python
								/*
								try {
									String Py_ip = "192.168.10.141";
									int Py_port = 12345;
									m_socket = new Socket(Py_ip, Py_port);
									if (m_socket != null)// 連線成功才繼續往下執行
									{
										for(int w=0;w<5;w++){
											if(w!=4){
												strtemp+=tempResult.get(w)+"w";
											}
											else{
												strtemp+=tempResult.get(w);
											}
										}
										// 由於Server端使用 PrintStream 和
										// BufferedReader 來接收和寄送訊息，所以Client端也要相同
										PrintStream writer = new PrintStream(
												m_socket.getOutputStream());
										BufferedReader reader = new BufferedReader(
												new InputStreamReader(m_socket
														.getInputStream()));
										int count = 0;
										

											count++;
											try {
												System.out
														.println("5554+"
																+ i
																+ "fail,send to python to switch migration");
												writer.println(strtemp);
												writer.flush();
											} catch (Exception e) {
												e.printStackTrace();
											}
											// System.out.println("Server:" +
											// reader.readLine());
										
										// m_socket.close();

									}
								} catch (IOException e) {
									System.out.println(e.getMessage());
								}
								*/
								// ////////////connect to python

								System.out.println("5554+" + i
										+ "-Dead Consensus-");
								System.out.println("Consensus time: "
										+ fctime[i]);
								// System.out.println("fc count: "+fccount);
								Integer p = new Integer(5554 + i);
								Integer pi = new Integer(i);
								aliveList.remove(p);
								if (!deadList.contains(pi))
									deadList.add(pi);
								break;
							}
						}
					}
				}
			}
			System.out.println("fc count: " + fccount);

			if (port != 5556) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(gossipList);
				oos.writeObject(suspectMtx);
				oos.flush();
				byte buffer[] = baos.toByteArray();

				DatagramPacket packet = new DatagramPacket(buffer,
						buffer.length, server, port);
				DatagramSocket socket = new DatagramSocket(5502);
				socket.send(packet);

				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
