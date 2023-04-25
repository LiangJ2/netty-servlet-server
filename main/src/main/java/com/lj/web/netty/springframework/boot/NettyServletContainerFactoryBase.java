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

import com.lj.web.HttpServletContext;
import com.lj.web.IConfigurableServletServer;
import com.lj.web.netty.NettyServletServer;

/**
 * 
 * @author LiangJ2
 *
 */
public class NettyServletContainerFactoryBase extends HttpServletContext
{
   public NettyServletContainerFactoryBase()
   {
      super(new NettyServletServer());
   }
   //---------------------------------------------------------------------------
   
   public NettyServletContainerFactoryBase(IConfigurableServletServer server)
   {
      super(server);
   }
   //---------------------------------------------------------------------------
   
   public NettyServletContainerFactoryBase(int port)
   {
      this();
      setPort(port);
   }
   //---------------------------------------------------------------------------
 
   public NettyServletContainerFactoryBase(String contextPath, int port)
   {
      this();      
      setPort(port);
      setContextPath(contextPath);
   }
   //---------------------------------------------------------------------------
   
   protected Object doStartupServletContext(org.springframework.boot.web.servlet.ServletContextInitializer... initializers)
   {      
      for(int i = 0; i < initializers.length; i++)
      try
      {
         if(servletServer.getServletContext() == null)
            servletServer.setServletContext(this);
         
         log(initializers[i].getClass() + " startup.");
         initializers[i].onStartup(servletServer.getServletContext());
      } catch(Exception e) { e.printStackTrace(); }
      
      return servletServer.getServletContext();
   }
   //---------------------------------------------------------------------------

   protected void doStart() throws Exception
   {
      log(this.getClass().getSimpleName() + ": start."); 
      servletServer.start();
   }
   //---------------------------------------------------------------------------
   
   protected void doStop() throws Exception
   {
      log(this.getClass().getSimpleName() + ": stop.");     
      servletServer.stop();
   }
   //---------------------------------------------------------------------------
   
   public int getPort()
   {
      return servletServer.getPort();
   }
   //---------------------------------------------------------------------------  

   public void setPort(int port)
   {
      servletServer.setPort(port);
   }
   //---------------------------------------------------------------------------  
}
//================ End Class NettyServletContainerFactoryBase ===============
