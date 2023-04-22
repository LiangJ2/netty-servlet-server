/*
 * Copyright © 2023 LiangJ2.
 *
 * Licensed under the GNU General Public License v2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lj.web.HttpServletContext;
import com.lj.web.IConfigurableServletServer;
import com.lj.web.IHttpServer;

import com.lj.web.netty.NettyServletServer;

/**
 * 
 * @author LiangJ2
 *
 */
public class App extends HttpServlet
{
   private   static final long  serialVersionUID = -1726683241907860198L;
   protected static IHttpServer httpServer        = null;

   protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      byte[] text = (httpServer.getPort() + " " + request.getRequestURI() + " " + request.getMethod() + ": test ok. " + request.getParameterMap()).getBytes();
      
      response.setContentLength(text.length);
      response.getOutputStream().write(text);
   }
   //---------------------------------------------------------------------------
     
   public static IHttpServer CreateHttpServer(int port)
   {
      IConfigurableServletServer Result = new NettyServletServer();
      
      Result.setPort(port);
      Result.setServletContext(new HttpServletContext(Result));
      Result.setDispatcher(new App());
      
      return Result;
   }
   //---------------------------------------------------------------------------
   
   public static void main(String[] args)
   {
      httpServer = CreateHttpServer(8080);
      
      try
      {
         httpServer.start();
      } catch(Exception e) { e.printStackTrace(); }
   }
   //---------------------------------------------------------------------------
}
//============================== End Class App ==============================
