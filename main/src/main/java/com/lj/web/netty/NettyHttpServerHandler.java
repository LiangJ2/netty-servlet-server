package com.lj.web.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public class NettyHttpServerHandler extends SimpleChannelInboundHandler<Object>
{
   protected NettyHttpServer handle = null;
   
   public NettyHttpServerHandler(NettyHttpServer handle)
   {
      this.handle = handle;
   }
   //---------------------------------------------------------------------------
   
   @Override
   protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception 
   {      
      if(msg instanceof FullHttpRequest)
      {
         FullHttpRequest request = (FullHttpRequest)msg;
         
         Object response = handle.doHandle(ctx.channel(), request);
      
         if(response instanceof HttpResponse)
            ctx.writeAndFlush(response); 
      }//--------End If--------
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
