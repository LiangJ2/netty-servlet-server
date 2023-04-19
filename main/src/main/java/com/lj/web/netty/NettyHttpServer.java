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

package com.lj.web.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerException;

import com.lj.web.IHttpHandler;
import com.lj.web.IHttpServer;

/**
 * 
 * @author LiangJ2
 *
 */
public class NettyHttpServer extends ChannelInitializer<Channel>
                             implements GenericFutureListener<Future<Void>>, IHttpServer, IHttpHandler<HttpRequest, Channel>
{
   protected int             port        = 8080;
   protected Object          sslContext  = null;
   protected String          contextPath = null, sslProtocol = null;
   protected ServerBootstrap bootstrap   = null;
   
   protected final EventLoopGroup bossGroup   = new NioEventLoopGroup();
   protected final EventLoopGroup workerGroup = new NioEventLoopGroup();
   
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
   
   public Object getSslContext()
   {
      return sslContext;
   }
   //---------------------------------------------------------------------------
   
   public void setSslContext(Object sslContext)
   {
      this.sslContext = sslContext;
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
               channelFuture.channel().closeFuture().addListener(this); // new GenericFutureListener<Future<Void>>()
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
   public void handle(HttpRequest request, Channel channel) throws Exception
   {      
   }
   //---------------------------------------------------------------------------
   
   protected void doInitChannel(Channel ch)
   {
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
   public void start() throws WebServerException
   {
      try
      {
         start(false);
      }
      catch(Exception e)
      {
         throw new WebServerException(e.getMessage(), e);
      }//--------End Try--------
   }
   //---------------------------------------------------------------------------

   @Override
   public void stop() throws WebServerException
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
   
   public static int Len(String Src)
   {
      if(Src == null)
         return 0;
      return Src.length();
   }
   //---------------------------------------------------------------------------
   
   public static <T> Iterator<T> GetIterator(Iterable<T> Src)
   {
      if(Src != null)
         return Src.iterator();
      return null;
   }
   //---------------------------------------------------------------------------
   
   public static <K, V> Iterator<Map.Entry<K, V>> GetIterator(Map<K, V> Src)
   {
      if(Src != null)
         return GetIterator(Src.entrySet());
      return null;
   }
   //---------------------------------------------------------------------------

   public static Class<?> GetClass(String ClassName)
   {
      Class<?> Result = null;
      
      if(Len(ClassName) > 0)
      try
      {
         Result = Class.forName(ClassName);
      } catch(Exception e) { }
      
      return Result;
   }
   //---------------------------------------------------------------------------
}
//======================== End Class NettyHttpServer ========================
