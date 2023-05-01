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

package com.lj.web.netty.spring.boot;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.web.server.Compression;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.Http2;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.SslStoreProvider;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.boot.web.servlet.server.Jsp;
import org.springframework.boot.web.servlet.server.Session;

/**
 * 
 * @author LiangJ2
 *
 */
public class NettyServletWebServerFactory extends NettyServletContainerFactoryBase implements ConfigurableServletWebServerFactory, WebServer
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
   
   public void setSsl(Ssl ssl)
   {
      if(ssl.isEnabled())
         setSsl(ssl.getProtocol(), ssl.getKeyStoreType(), ssl.getKeyStore(), ssl.getKeyStorePassword(), ssl.getKeyPassword(), ssl.getKeyAlias());      
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void setErrorPages(Set<? extends ErrorPage> errorPages)
   {
      // TODO Auto-generated method stub
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void setSslStoreProvider(SslStoreProvider sslStoreProvider)
   {
      // TODO Auto-generated method stub
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void setHttp2(Http2 http2)
   {
      // TODO Auto-generated method stub
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void setCompression(Compression compression)
   {
      // TODO Auto-generated method stub
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void setServerHeader(String serverHeader)
   {
      // TODO Auto-generated method stub
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void addErrorPages(ErrorPage... errorPages)
   {
      // TODO Auto-generated method stub
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void addWebListeners(String... webListenerClassNames)
   {
      // TODO Auto-generated method stub
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void setDisplayName(String displayName)
   {
      // TODO Auto-generated method stub
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void setSession(Session session)
   {
      // TODO Auto-generated method stub
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void setRegisterDefaultServlet(boolean registerDefaultServlet)
   {
      // TODO Auto-generated method stub
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void setMimeMappings(MimeMappings mimeMappings)
   {
      // TODO Auto-generated method stub
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void setDocumentRoot(File documentRoot)
   {
      // TODO Auto-generated method stub
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void setInitializers(List<? extends ServletContextInitializer> initializers)
   {
      // TODO Auto-generated method stub
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void addInitializers(ServletContextInitializer... initializers)
   {
      // TODO Auto-generated method stub
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void setJsp(Jsp jsp)
   {
      // TODO Auto-generated method stub
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void setLocaleCharsetMappings(Map<Locale, Charset> localeCharsetMappings)
   {
      // TODO Auto-generated method stub
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void setInitParameters(Map<String, String> initParameters)
   {
      // TODO Auto-generated method stub
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void setCookieSameSiteSuppliers(List<? extends CookieSameSiteSupplier> cookieSameSiteSuppliers)
   {
      // TODO Auto-generated method stub
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void addCookieSameSiteSuppliers(CookieSameSiteSupplier... cookieSameSiteSuppliers)
   {
      // TODO Auto-generated method stub
   }
   //---------------------------------------------------------------------------
}
//================== End Class NettyServletWebServerFactory =================
