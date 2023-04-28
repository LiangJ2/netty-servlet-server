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

import java.net.InetAddress;

/**
 * A configurable {@link IHttpServer}.
 * @author LiangJ2
 *
 */
public interface IConfigurableHttpServer extends IHttpServer
{
   /**
    * Sets the specific network address that the server should bind to.
    * @param address the address to set (defaults to {@code null})
    */
   void setAddress(InetAddress address);
   
   /**
    * Sets the port that the http server should listen on.
    * @param port The port to set.
    */
   void setPort(int port);
   
   /**
    * Sets the main path associated with this context.
    * @param contextPath The main context path to set.
    */
   void setContextPath(String contextPath);
}
//================== End Interface IConfigurableHttpServer ==================
