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

package com.lj.web;

import java.util.EventListener;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;

import org.springframework.mock.web.MockServletContext;

/**
 * 
 * @author LiangJ2
 *
 */
public class HttpServletContext extends MockServletContext
{
   protected IConfigurableServletServer servletServer = null;
   
   public HttpServletContext(IConfigurableServletServer servletServer)
   {
      super();
      
      this.servletServer = servletServer;
   }
   //---------------------------------------------------------------------------
   
   public <T extends EventListener> void addListener(T listener) 
   {
      log(listener.getClass().getName() + " addListener.");
   }
   //---------------------------------------------------------------------------
   
   public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) 
   {
      servletServer.setDispatcher(servlet);
      log(servletName + " " + servlet.getClass().getName() + " addServlet.");
      
      try
      {
         servlet.init(new org.springframework.mock.web.MockServletConfig(this));
      } catch(Exception e) { e.printStackTrace(); }
      
      return null;
   }
   //---------------------------------------------------------------------------
   
   public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) 
   {
      log(filterName + " " + filter.getClass().getName() + " addFilter.");
      
      return null;
   }
   //---------------------------------------------------------------------------
}
//====================== End Class HttpServletContext =======================
