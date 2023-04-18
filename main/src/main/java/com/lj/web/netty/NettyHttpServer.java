package com.lj.web.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

public class NettyHttpServer extends ChannelInitializer<Channel> implements GenericFutureListener<Future<Void>>, Closeable
{
   protected int            port           = 8080;
   protected Object         sslContext     = null;
   protected String         contextPath    = null, sslProtocol = null;
   protected Servlet        dispatcher     = null;
   protected ServletContext servletContext = null;
   
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

   public Servlet getDispatcher()
   {
      return dispatcher;
   }
   //---------------------------------------------------------------------------

   public void setDispatcher(Servlet dispatcher)
   {
      this.dispatcher = dispatcher;
   }
   //---------------------------------------------------------------------------

   public ServletContext getServletContext()
   {
      return servletContext;
   }
   //---------------------------------------------------------------------------

   public void setServletContext(ServletContext servletContext)
   {
      this.servletContext = servletContext;
   }
   //---------------------------------------------------------------------------
   
   public ServerBootstrap start(boolean autoWait)
   {      
      ServerBootstrap bootstrap     = new ServerBootstrap();
      ChannelFuture   channelFuture = null;

      if(servletContext != null && servletContext instanceof MockServletContext)
         ((MockServletContext)servletContext).setContextPath(contextPath);

      try
      {
         bootstrap.group(bossGroup, workerGroup);
         channelFuture = bootstrap.channel(NioServerSocketChannel.class).childHandler(this).bind(port).syncUninterruptibly().addListener(this);
      }
      catch(Exception e)
      {
         e.printStackTrace();
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
   
   public void print(String text)
   {
      if(servletContext == null)
         System.out.print(text);
      else
         servletContext.log(text);
   }
   //---------------------------------------------------------------------------
   
   protected Object doHandle(Channel channel, HttpRequest request) throws Exception
   {
      Object Result = null;
      
      if(dispatcher != null)
      {
         MockHttpServletResponse response = new MockHttpServletResponse();
         
         dispatcher.service(CreateServletRequest((ServletContext)servletContext, request), response);
         Result = CreateHttpResponse(response);
       }//--------End If--------
      
      return Result;
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
   
   public void operationComplete(Future<Void> future)
   {
      if(future.getClass().getName().indexOf("CloseFuture") > 0)
         doClose();
      else
      {
         String loginfo = "Netty server started. port=" + port;
         print(loginfo + "\n");
      }//--------End If--------
   }
   //---------------------------------------------------------------------------
   
   protected void doClose()
   {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();      
   }
   //---------------------------------------------------------------------------
   
   public void close() throws IOException
   {
      doClose();
   }
   //---------------------------------------------------------------------------
   
   @SuppressWarnings("deprecation")
   public static ServletRequest CreateServletRequest(ServletContext servletContext, HttpRequest request)
   {
      QueryStringDecoder                        uriDecoder = new QueryStringDecoder(request.getUri());
      MockHttpServletRequest                    Result     = null;
      Iterator<Map.Entry<String, String>>       headers    = request.headers().iterator();
      Iterator<Map.Entry<String, List<String>>> parameters = GetIterator(uriDecoder.parameters());
      
      Result = new MockHttpServletRequest(servletContext, request.getMethod().name(), uriDecoder.path());
      
      if(parameters != null)
      while(parameters.hasNext())
      {
         Map.Entry<String, List<String>> parameter = parameters.next();
         List<String>                    value     = parameter.getValue();
         
         if(value != null && value.size() > 0)
            Result.addParameter(parameter.getKey(), value.toArray(new String[value.size()]));
      }//--------End While--------
      
      if(headers != null)
      while(headers.hasNext())
      {
         Map.Entry<String, String> header = headers.next();
         
         Result.addHeader(header.getKey(), header.getValue());
      }//--------End While--------
      
      if(request instanceof ByteBufHolder)
      {
         ByteBuf content = ((ByteBufHolder)request).content();
         
         if(content.readableBytes() > 0)
         {
            byte[] buffer = new byte[content.readableBytes()];
            
            content.readBytes(buffer);
            Result.setContent(buffer);
         }//--------End If--------
      }//--------End If--------
      
      return Result;
   }
   //---------------------------------------------------------------------------
   
   public static HttpResponse CreateHttpResponse(MockHttpServletResponse response)
   {
      HttpResponse       Result  = null;     
      Iterator<String>   names   = GetIterator(response.getHeaderNames());
      byte[]             content = response.getContentAsByteArray();
      HttpResponseStatus status  = HttpResponseStatus.valueOf(response.getStatus());
      HttpHeaders        headers = null;
      
      if(content != null && content.length > 0)
         Result = new io.netty.handler.codec.http.DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, io.netty.buffer.Unpooled.wrappedBuffer(content));
      else
      {
         Result = new io.netty.handler.codec.http.DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
         Result.headers().set(io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH, "0");
         //io.netty.handler.codec.http.HttpUtil.setContentLength(Result, 0);
      }//--------End If--------
         
      headers = Result.headers();
      
      if(names != null)
      while(names.hasNext())
      {
         String name = names.next();
         
         headers.add(name, response.getHeaders(name));
      }//--------End While--------
      
      return Result;
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

   public static <T> T GetValue(io.netty.util.AttributeMap Src, io.netty.util.AttributeKey<T> Key)
   {
      io.netty.util.Attribute<T> Result = null;
      
      if(Src != null && Key != null)
         Result = Src.attr(Key);
      
      if(Result == null)
         return null;
      return Result.get();
   }
   //---------------------------------------------------------------------------
}
//======================== End Class NettyHttpServer ========================
