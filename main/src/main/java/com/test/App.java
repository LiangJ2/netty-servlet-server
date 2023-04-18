package com.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mock.web.MockServletContext;

import com.lj.web.netty.NettyHttpServer;

public class App extends HttpServlet
{
   private   static final long      serialVersionUID = -1726683241907860198L;
   protected static NettyHttpServer webServer        = null;

   protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      byte[] text = (request.getRequestURI() + " " + request.getMethod() + ": test ok. " + request.getParameterMap()).getBytes();
      
      response.setContentLength(text.length);
      response.getOutputStream().write(text);
   }
   //---------------------------------------------------------------------------   
   
   public static void main( String[] args )
   {
      webServer = new NettyHttpServer();
      
      webServer.setPort(8080);
      webServer.setServletContext(new MockServletContext());
      webServer.setDispatcher(new App());
      webServer.start(true);
   }
   //---------------------------------------------------------------------------
}
//============================== End Class App ==============================
