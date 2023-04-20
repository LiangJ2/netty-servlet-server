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
 * A servlet {@link IHttpServer}.
 * @author LiangJ2
 *
 */
public interface IServletServer extends IHttpServer
{
   /**
    * Return the servlet dispatcher.
    * @return The servlet dispatcher.
    */
   Servlet getDispatcher();
   
   /**
    * Return the servlet context.
    * @return The servlet context.
    */
   ServletContext getServletContext();
}
//====================== End Interface IServletServer =======================
