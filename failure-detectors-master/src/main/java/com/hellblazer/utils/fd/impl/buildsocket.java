package com.hellblazer.utils.fd.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class buildsocket extends Thread
{
    private Socket m_socket;//和伺服器端進行連線
    
    public buildsocket(String ip, int port)
    {
        try
        {
            m_socket = new Socket(ip, port);//建立連線。(ip為伺服器端的ip，port為伺服器端開啟的port)
        }
       catch (Exception e) { e.printStackTrace(); }
       
    }
    
    @Override
    public void run()
    {
        try
        {
            if (m_socket != null)//連線成功才繼續往下執行
            {

                //由於Server端使用 PrintStream 和 BufferedReader 來接收和寄送訊息，所以Client端也要相同
                PrintStream writer = new PrintStream(m_socket.getOutputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
                int count=0;
                while(true){
                
                count++;
                try{
                	System.out.println("yee"+count);
                	writer.println("What is the time？");
                    writer.flush();
                	Thread.sleep(3000);
                } catch (Exception e) 
                { e.printStackTrace(); }
                //System.out.println("Server:" + reader.readLine());
                }
                //m_socket.close();

            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    
    public static void main(String[] argv)
    {
        new buildsocket("127.0.0.1", 12345).start();//建立物件，傳入IP和Port並執行等待接受連線的動作
    
        //由於此範例都在自己電腦上執行，所以IP設定為127.0.0.1
        
    }
}



