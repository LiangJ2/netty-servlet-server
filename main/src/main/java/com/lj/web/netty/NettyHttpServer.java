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

package com.lj.web.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
//import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.security.KeyStore;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLEngine;

import org.springframework.util.ResourceUtils;

import com.lj.web.IConfigurableHttpServer;
import com.lj.web.IHttpHandler;

/**
 * 
 * @author LiangJ2
 *
 */
public class NettyHttpServer extends    ChannelInitializer<Channel>
                             implements GenericFutureListener<Future<Void>>, IConfigurableHttpServer, IHttpHandler<HttpRequest, Channel, ChannelFuture>
{
   protected InetAddress     address     = null;
   protected int             port        = 8080;
   protected SslContext      sslContext  = null;
   protected String          contextPath = "", sslProtocol = null;
   protected ServerBootstrap bootstrap   = null;
   
   protected final EventLoopGroup bossGroup   = new NioEventLoopGroup();
   protected final EventLoopGroup workerGroup = new NioEventLoopGroup();
   
   public SslContext createSSLContext(KeyStore keyStore, String keyPass, String keyAlias)
   {
      return CreateSSLContext(getSslProtocol(), keyStore, keyPass, keyAlias);
   }
   //---------------------------------------------------------------------------
   
   public void setSsl(String sslProtocol, String keystoreType, String keystorePath, String keystorePassword, String keyPass, String keyAlias)
   {
      KeyStore keyStore = LoadKeyStore(keystoreType, keystorePath, keystorePassword);
      
      setSslProtocol(sslProtocol);
      setSslContext(createSSLContext(keyStore, keyPass, keyAlias));
      
      log("setSsl: " + keystorePath);
   }
   //---------------------------------------------------------------------------
   
   public String getSslProtocol()
   {
      return sslProtocol;
   }
   //---------------------------------------------------------------------------

   public void setSslProtocol(String sslProtocol)
   {
      this.sslProtocol = sslProtocol;
   }
   //---------------------------------------------------------------------------
   
   public SslContext getSslContext()
   {
      return sslContext;
   }
   //---------------------------------------------------------------------------
   
   public void setSslContext(SslContext sslContext)
   {
      this.sslContext = sslContext;
   }
   //---------------------------------------------------------------------------
   
   public InetAddress getAddress()
   {
      return address;
   }
   //---------------------------------------------------------------------------
   
   public void setAddress(InetAddress address)
   {
      this.address = address;
   }
   //---------------------------------------------------------------------------
   
   public int getPort()
   {
      return port;
   }
   //---------------------------------------------------------------------------
   
   public void setPort(int port)
   {
      this.port = port;
   }
   //---------------------------------------------------------------------------
   
   public String getContextPath()
   {
      return contextPath;
   }
   //---------------------------------------------------------------------------
   
   public void setContextPath(String contextPath)
   {
      this.contextPath = contextPath;
   }
   //---------------------------------------------------------------------------
   
   public ServerBootstrap start(boolean autoWait) throws Exception
   {      
      ChannelFuture channelFuture = null;
      
      bootstrap = new ServerBootstrap();

      try
      {
         bootstrap.group(bossGroup, workerGroup);
         channelFuture = bootstrap.channel(NioServerSocketChannel.class).childHandler(this).bind(port).syncUninterruptibly().addListener(this);
      }
      catch(Exception e)
      {
         bootstrap = null;
         throw e;
      }
      finally
      {
         if(channelFuture != null)
         {
            if(autoWait)
               channelFuture.channel().closeFuture().awaitUninterruptibly();
            else
               channelFuture.channel().closeFuture().addListener(this);
         }//--------End If--------
      }//--------End Try--------
      
      return bootstrap;
   }
   //---------------------------------------------------------------------------
   
   public void log(String text)
   {
      System.out.println(text);
   }
   //---------------------------------------------------------------------------
   
   @Override
   public ChannelFuture handle(HttpRequest request, Channel channel)
   {      
      return null;
   }
   //---------------------------------------------------------------------------
   
   protected void doInitChannel(Channel ch)
   {
      SslHandler sslHandler = CreateSslHandler(sslContext, sslProtocol, ch.alloc());
      
      if(Assigned(sslHandler))
         ch.pipeline().addLast("ssl", sslHandler);
      
      ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
      ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(Integer.MAX_VALUE));
      ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
      ch.pipeline().addLast("http-server", new NettyHttpServerHandler(this));
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void initChannel(Channel ch) throws Exception 
   {
      doInitChannel(ch);
   }     
   //---------------------------------------------------------------------------
   
   @Override
   public void operationComplete(Future<Void> future)
   {
      if(future.getClass().getName().indexOf("CloseFuture") > 0)
         doClose();
      else
         log("Netty server started. port=" + getPort());      
   }
   //---------------------------------------------------------------------------

   @Override
   public boolean started()
   {
      return bootstrap != null;
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void start() throws Exception
   {
      start(false);
   }
   //---------------------------------------------------------------------------

   @Override
   public void stop() throws Exception
   {
      doClose();
   }
   //---------------------------------------------------------------------------
   
   protected void doClose()
   {
      if(started())
      {
         bossGroup.shutdownGracefully();
         workerGroup.shutdownGracefully();  
         bootstrap = null;
      }//--------End If--------
   }
   //---------------------------------------------------------------------------
   
   @Override
   public void close() throws IOException
   {
      doClose();
   }
   //---------------------------------------------------------------------------
   
   public static boolean IsNull(Object src)
   { 
      return src == null;
   }
   //---------------------------------------------------------------------------
   
   public static boolean Assigned(Object src) 
   { 
      return IsNull(src) == false; 
   }
   //---------------------------------------------------------------------------
   
   public static int Len(byte[] src)
   {
      if(IsNull(src))
         return 0;
      return src.length;
   }
   //---------------------------------------------------------------------------
   
   public static int Len(Collection<?> src)
   {
      if(IsNull(src))
         return 0;
      return src.size();
   }
   //---------------------------------------------------------------------------
   
   public static int Len(CharSequence src)
   {
      if(IsNull(src))
         return 0;
      return src.length();
   }
   //---------------------------------------------------------------------------
   
   public static <T> Iterator<T> GetIterator(Iterable<T> src)
   {
      if(Assigned(src))
         return src.iterator();
      return null;
   }
   //---------------------------------------------------------------------------
   
   public static <K, V> Iterator<Map.Entry<K, V>> GetIterator(Map<K, V> src)
   {
      if(Assigned(src))
         return GetIterator(src.entrySet());
      return null;
   }
   //---------------------------------------------------------------------------
   
   public static KeyStore LoadKeyStore(String keystoreType, String keystorePath, String keystorePassword)
   {
      KeyStore    Result = null;
      InputStream is     = null; 
      
      if(Len(keystoreType) > 0 && Len(keystorePath) > 0 && Len(keystorePassword) > 0)
      try
      {
         Result = KeyStore.getInstance(Len(keystoreType) == 0 ? "JKS" : keystoreType);
         is     = ResourceUtils.getURL(keystorePath).openStream();
         
         Result.load(is, keystorePassword.toCharArray());
      } catch(Exception e) { e.printStackTrace(); }

      if(Assigned(is))
      try
      {
         is.close();
      } catch(IOException e) { e.printStackTrace(); }
      
      return Result;
   }
   //---------------------------------------------------------------------------
   
   public static KeyManagerFactory GetKeyManagerFactory(KeyStore keyStore, String keyPass)
   {
      KeyManagerFactory Result = null;
      
      if(keyStore != null && Len(keyPass) > 0)
      try
      {
         Result = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
         
         Result.init(keyStore, keyPass.toCharArray());
      } catch(Exception e) { e.printStackTrace(); }
      
      return Result;
   }
   //---------------------------------------------------------------------------
   
   public static SslContext CreateSSLContext(String sslProtocol, KeyStore keyStore, String keyPass, String keyAlias)
   {
      SslContext Result = null;
      
      if(Len(sslProtocol) > 0 &&  Assigned(keyStore) && Len(keyPass) > 0)
      try
      {
         Result = SslContextBuilder.forServer(GetKeyManagerFactory(keyStore, keyPass)).protocols(sslProtocol).build();
      } catch(Exception e) { e.printStackTrace(); }
      
      return Result;
   }
   //---------------------------------------------------------------------------   

   public static SslHandler CreateSslHandler(SslContext sslContext, String sslProtocol, ByteBufAllocator alloc)
   {
      SSLEngine Result = null;
      
      if(Assigned(sslContext))
      {
         if(Assigned(alloc))
            Result = sslContext.newEngine(alloc);
         
         if(Assigned(Result) && Len(sslProtocol) > 0)
            Result.setEnabledProtocols(new String[] {sslProtocol});
         
         if(Assigned(Result))
            Result.setNeedClientAuth(false);
      }//--------End If--------
      
      if(IsNull(Result))
         return null;
      return new SslHandler(Result);
   }
   //---------------------------------------------------------------------------
}
//======================== End Class NettyHttpServer ========================
