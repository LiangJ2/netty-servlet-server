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

package com.lj.web;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;

/**
 * A configurable {@link IServletServer}.
 * @author Liang
 *
 */
public interface IConfigurableServletServer extends IConfigurableHttpServer, IServletServer
{
   /**
    * Sets the servlet dispatcher.
    * @param dispatcher The servlet dispatcher to set.
    */
   void setDispatcher(Servlet dispatcher);
   
   /**
    * Sets the servlet context.
    * @param servletContext The servlet context.
    */
   void setServletContext(ServletContext servletContext);
}
//================ End Interface IConfigurableServletServer =================
