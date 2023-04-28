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

import com.lj.web.IHttpHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;

/**
 * 
 * @author Liang
 *
 */
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<Object>
{
   protected IHttpHandler<HttpRequest, Channel, ChannelFuture> handler = null;
   
   public NettyHttpServerHandler(IHttpHandler<HttpRequest, Channel, ChannelFuture> handler)
   {
      this.handler = handler;
   }
   //---------------------------------------------------------------------------
   
   @Override
   protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception 
   {      
      if(msg instanceof FullHttpRequest)
         handler.handle((HttpRequest)msg, ctx.channel());
   }
   //---------------------------------------------------------------------------

   @Override
   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
   {
      ctx.fireExceptionCaught(cause);
   }
   //---------------------------------------------------------------------------
}
//===================== End Class NettyHttpServerHandler ====================
