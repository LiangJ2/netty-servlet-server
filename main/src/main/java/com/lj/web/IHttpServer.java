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

import java.io.Closeable;
import java.net.InetAddress;

/**
 * The http server interface.
 * @author LiangJ2
 *
 */
public interface IHttpServer extends Closeable
{
   /**
    * The http server started status.
    * @return return true, not return false.
    */
   boolean started();
   
   /**
    * Start the http server.
    * @throws Exception if the server cannot be started
    */
   void start() throws Exception;
   
   /**
    * Stop the http server.
    * @throws Exception if the server cannot be stopped
    */
   void stop() throws Exception; 
   
   /**
    * Return the specific network address that the server should bind to.
    * @return the specific network address (defaults to {@code null})
    */
   InetAddress getAddress();
   
   /**
    * Return the port this http server is listening on.
    * @return the port (or 8080 if none)
    */
   int getPort();
   
   /**
    * Return the main path associated with this context.
    * @return The main context path
    */
   String getContextPath();
}
//======================== End Interface IHttpServer ========================
