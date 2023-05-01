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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

import com.lj.web.IConfigurableServletServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;

/**
 * 
 * @author LiangJ2
 *
 */
public class NettyServletServer extends NettyHttpServer implements IConfigurableServletServer
{
   protected Servlet        dispatcher     = null;
   protected ServletContext servletContext = null;

   public NettyServletServer()
   {
      super();
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
   
   public ServerBootstrap start(boolean autoWait) throws Exception
   {
      if(Assigned(servletContext) && servletContext instanceof MockServletContext)
         ((MockServletContext)servletContext).setContextPath(contextPath);

      return super.start(autoWait);
   }
   //---------------------------------------------------------------------------
   
   public void log(String text)
   {
      if(IsNull(servletContext))
         super.log(text);
      else
         servletContext.log(text);
   }
   //---------------------------------------------------------------------------
   
   public ChannelFuture handle(HttpRequest request, Channel channel)
   {
      ChannelFuture Result = null;
      
      if(Assigned(request))
      try
      {
         Result = Handle(getDispatcher(), getServletContext(), request, channel);
      } catch(Exception e) { e.printStackTrace(); }
      
      return Result;
   }
   //---------------------------------------------------------------------------
   
   public static ChannelFuture Handle(Servlet dispatcher, ServletContext context, HttpRequest request, Channel channel) throws Exception
   {
      ChannelFuture Result = null;
      
      if(Assigned(dispatcher))
      {
         MockHttpServletResponse response = new MockHttpServletResponse();
         
         dispatcher.service(CreateServletRequest(context, request), response);
         Result = channel.writeAndFlush(CreateHttpResponse(response));
      }//--------End If--------      
      
      return Result;
   }
   //---------------------------------------------------------------------------
   
   public static ServletRequest CreateServletRequest(ServletContext servletContext, HttpRequest request)
   {
      ByteBuf content = null;
      
      if(request instanceof ByteBufHolder)
         content = ((ByteBufHolder)request).content();
      
      return CreateServletRequest(servletContext, request.uri(), request.method().name(), GetIterator(request.headers()), ToBytes(content));
   }
   //---------------------------------------------------------------------------
   
   public static byte[] ToBytes(ByteBuf content)
   {
      byte[] Result = null;
      
      if(Assigned(content) && content.readableBytes() > 0)
      {
         Result = new byte[content.readableBytes()];
         
         content.readBytes(Result);
      }//--------End If--------
      
      return Result;
   }
   //---------------------------------------------------------------------------
   
   public static ServletRequest CreateServletRequest(ServletContext servletContext, String uri, String method, Iterator<Map.Entry<String, String>> headers, byte[] content)
   {
      QueryStringDecoder                        uriDecoder = new QueryStringDecoder(uri);
      MockHttpServletRequest                    Result     = null;
      Iterator<Map.Entry<String, List<String>>> parameters = GetIterator(uriDecoder.parameters());
      
      Result = new MockHttpServletRequest(servletContext, method, uriDecoder.path());
      
      if(Assigned(parameters))
      while(parameters.hasNext())
      {
         Map.Entry<String, List<String>> parameter = parameters.next();
         List<String>                    value     = parameter.getValue();
         
         if(value != null && value.size() > 0)
            Result.addParameter(parameter.getKey(), value.toArray(new String[value.size()]));
      }//--------End While--------
      
      if(Assigned(headers))
      while(headers.hasNext())
      {
         Map.Entry<String, String> header = headers.next();
         
         Result.addHeader(header.getKey(), header.getValue());
      }//--------End While--------
      
      if(Len(content) > 0)
         Result.setContent(content);
      
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
      
      if(Len(content) > 0)
         Result = new io.netty.handler.codec.http.DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, io.netty.buffer.Unpooled.wrappedBuffer(content));
      else
      {
         Result = new io.netty.handler.codec.http.DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
         Result.headers().set(io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH, "0");
      }//--------End If--------
         
      headers = Result.headers();
      
      if(Assigned(names))
      while(names.hasNext())
      {
         String name = names.next();
         
         headers.add(name, response.getHeaders(name));
      }//--------End While--------
      
      return Result;
   }
   //---------------------------------------------------------------------------
}
//====================== End Class NettyServletServer =======================
