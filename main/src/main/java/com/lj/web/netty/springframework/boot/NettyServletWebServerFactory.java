/*
 * Copyright (c) 2023 LiangJ2.
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

package com.lj.web.netty.springframework.boot;

import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;

/**
 * 
 * @author LiangJ2
 *
 */
public class NettyServletWebServerFactory extends NettyServletContainerFactoryBase implements ServletWebServerFactory, WebServer
{   
   public NettyServletWebServerFactory()
   {
      super();
   }
   //---------------------------------------------------------------------------
   
   public NettyServletWebServerFactory(int port)
   {
      super(port);
   }
   //---------------------------------------------------------------------------
 
   public NettyServletWebServerFactory(String contextPath, int port)
   {
      super(contextPath, port);      
   }
   //---------------------------------------------------------------------------
   
   public WebServer getWebServer(ServletContextInitializer... initializers)
   {
      doStartupServletContext(initializers);
      
      return this;
   }
   //---------------------------------------------------------------------------
   
   protected void doAction(boolean isStart) throws WebServerException
   {
      try
      {
         if(isStart)
            doStart();
         else
            doStop();
      }
      catch(Exception e)
      {
         if(e instanceof WebServerException)
            throw (WebServerException)e;
         else
            throw new WebServerException(e.getMessage(), e);
      }//--------End Try--------
   }
   //---------------------------------------------------------------------------
   
   public void start() throws WebServerException
   {
      doAction(true);
   }
   //---------------------------------------------------------------------------
   
   public void stop() throws WebServerException
   {
      doAction(false);
   }
   //---------------------------------------------------------------------------
}
//================== End Class NettyServletWebServerFactory =================
